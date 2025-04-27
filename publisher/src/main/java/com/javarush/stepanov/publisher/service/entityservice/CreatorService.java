package com.javarush.stepanov.publisher.service.entityservice;

import com.javarush.stepanov.publisher.mapper.CreatorDto;
import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.CreatorRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class CreatorService {

    private final CreatorRepo repo;
    private final CreatorDto mapper;
    private final CreatorRedisRepo redisRepo;

    public List<Creator.Out> getAll() {
        if (redisRepo.isAllCollectionInRedis()) {
            return redisRepo.findAll();
        }
        List<Creator.Out> listResult = repo.findAll()
                .stream()
                .map(mapper::out)
                .filter(Objects::nonNull)  // Отсеиваем null
                .peek(x -> {  // peek() вместо map(), т.к. это side-эффект
                    if (!redisRepo.exists(x.id)) {
                        redisRepo.save(x.id, x);  // Сохраняем каждый элемент
                    }
                })
                .toList();
        redisRepo.setAllCollectionInRedis(true);
        return listResult;
    }

    public List<Creator.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Creator.Out get(Long id) {
        if (redisRepo.exists(id)) {
            return redisRepo.findById(id);
        }else {
            Creator.Out result = repo
                    .findById(id)
                    .map(mapper::out)
                    .orElseThrow();
            redisRepo.save(id, result);
            return result;
        }
    }

    public Creator.Out create(Creator.In input) {
        Creator creator = mapper.in(input);
        List<Creator.Out> list= getAll();
        for(Creator.Out creatorInList : list ){
            if((creatorInList.getId().equals(creator.getId()))||(creatorInList.getLogin().equals(creator.getLogin()))){
                throw new NoSuchElementException();
            }
        }
        Creator.Out result = mapper.out(
                repo.save(creator));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public Creator.Out update(Creator.In input) {
        Creator existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Creator not found with id: " + input.getId()));
        Creator updated = mapper.in(input); // или частичное обновление:
         existing.setLogin(input.getLogin());
         existing.setPassword(input.getPassword());
         existing.setFirstname(input.getFirstname());
         existing.setLastname(input.getLastname());
        Creator.Out result = mapper.out(repo.save(updated));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public void delete(Long id) {
        if (repo.findById(id).isEmpty()) {
            throw new NoSuchElementException("Creator not found with id: " + id);
        }
        redisRepo.delete(id);
        repo.deleteById(id);
    }

}
