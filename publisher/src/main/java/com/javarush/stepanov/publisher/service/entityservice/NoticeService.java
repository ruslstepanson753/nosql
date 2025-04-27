package com.javarush.stepanov.publisher.service.entityservice;


import com.javarush.stepanov.publisher.mapper.NoticeDto;
import com.javarush.stepanov.publisher.model.notice.Notice;
import com.javarush.stepanov.publisher.model.story.Story;
import com.javarush.stepanov.publisher.repository.dbrepo.NoticeRepo;
import com.javarush.stepanov.publisher.repository.dbrepo.StoryRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.NoticeRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class NoticeService {

    private final StoryRepo storyRepo;
    private final NoticeRepo repo;
    private final NoticeDto mapper;
    private final NoticeRedisRepo redisRepo;

    public List<Notice.Out> getAll() {
        if (redisRepo.isAllCollectionInRedis()){
            return redisRepo.findAll();
        }
        redisRepo.setAllCollectionInRedis(true);
        return repo
                .findAll()
                .stream()
                .map(mapper::out)
                .filter(Objects::nonNull)  // Отсеиваем null
                .peek(x -> {  // peek() вместо map(), т.к. это side-эффект
                    if (!redisRepo.exists(x.getId())) {
                        redisRepo.save(x.getId(), x);  // Сохраняем каждый элемент
                    }
                })
                .toList();
    }

    public List<Notice.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Notice.Out get(Long id) {
        return repo
                .findById(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public Notice.Out create(Notice.In input) {
        Notice notice = mapper.in(input);
        Long storyId = notice.getStoryId();
        Story story = storyRepo.findById(storyId).orElseThrow(NoSuchElementException::new);
        chekExceptionForCreate(input);
        return mapper.out(
                repo.save(notice));
    }

    public void chekExceptionForCreate(Notice.In notice) {
        Long storyId = notice.getStoryId();
        if (!storyRepo.existsById(storyId)) {
            throw new NoSuchElementException();
        }
    }

    public Notice.Out update(Notice.In input) {
        Notice existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Notice not found with id: " + input.getId()));
        Notice updated = mapper.in(input); // или частичное обновление:
        existing.setContent(input.getContent());
        existing.setStoryId(input.getStoryId());
        return mapper.out(repo.save(updated));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
