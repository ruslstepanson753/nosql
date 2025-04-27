package com.javarush.stepanov.publisher.service.entityservice;

import com.javarush.stepanov.publisher.mapper.MarkDto;
import com.javarush.stepanov.publisher.model.mark.Mark;
import com.javarush.stepanov.publisher.model.storymark.StoryMark;
import com.javarush.stepanov.publisher.repository.dbrepo.MarkRepo;
import com.javarush.stepanov.publisher.repository.dbrepo.StoryMarkRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.MarkRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class MarkService {

    private final MarkRepo repo;
    private final StoryMarkRepo storyMarkRepo;
    private final MarkDto mapper;
    private final MarkRedisRepo redisRepo;

    public List<Mark.Out> getAll() {
        if (redisRepo.isAllCollectionInRedis()) {
            return redisRepo.findAll();
        }
        List<Mark.Out> listResult = repo.findAll()
                .stream()
                .map(mapper::out)
                .filter(Objects::nonNull)  // Отсеиваем null
                .peek(x -> {  // peek() вместо map(), т.к. это side-эффект
                    if (!redisRepo.exists(x.getId())) {
                        redisRepo.save(x.getId(), x);  // Сохраняем каждый элемент
                    }
                })
                .toList();
        redisRepo.setAllCollectionInRedis(true);
        return listResult;
    }

    public List<Mark.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Mark.Out get(Long id) {
        if (redisRepo.exists(id)) {
            return redisRepo.findById(id);
        } else {
            Mark.Out result = repo
                    .findById(id)
                    .map(mapper::out)
                    .orElseThrow();
            redisRepo.save(id, result);
            return result;
        }
    }

    public Mark.Out create(Mark.In input) {
        Mark entity = mapper.in(input);
        List<Mark.Out> list = getAll();
        for (Mark.Out entityInList : list) {
            if ((entityInList.getId().equals(entity.getId())) || (entityInList.getName().equals(entity.getName()))) {
                throw new NoSuchElementException();
            }
        }
        Mark.Out result = mapper.out(
                repo.save(entity));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public Mark.Out update(Mark.In input) {
        Mark existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Mark not found with id: " + input.getId()));
        Mark updated = mapper.in(input); // или частичное обновление:
        existing.setName(input.getName());
        if(input.getStoryIds()!=null){
            Set<Long> storiesIds = input.getStoryIds();
            Set<StoryMark> storyMarkSet = new HashSet<>();
            storiesIds.forEach(id -> storyMarkSet.add(storyMarkRepo.getReferenceById(id)));
            existing.setStorys(storyMarkSet);
        }
        Mark.Out result = mapper.out(repo.save(updated));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public Mark.Out delete(Long id) {
        Mark entity = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.CONFLICT,
                "mark with id '" + id + " not exists"
        ));
        repo.deleteById(id);
        return mapper.out(entity);
    }
}
