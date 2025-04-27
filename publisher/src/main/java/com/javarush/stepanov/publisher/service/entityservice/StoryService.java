package com.javarush.stepanov.publisher.service.entityservice;

import com.javarush.stepanov.publisher.mapper.StoryDto;
import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.model.mark.Mark;
import com.javarush.stepanov.publisher.model.story.Story;
import com.javarush.stepanov.publisher.model.storymark.StoryMark;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import com.javarush.stepanov.publisher.repository.dbrepo.MarkRepo;
import com.javarush.stepanov.publisher.repository.dbrepo.StoryMarkRepo;
import com.javarush.stepanov.publisher.repository.dbrepo.StoryRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.StoryRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
public class StoryService {

    private final StoryRepo repo;
    private final MarkRepo markRepo;
    private final StoryDto mapper;
    private final CreatorRepo creatorRepo;
    private final StoryMarkRepo storyMarkRepo;
    private final StoryRedisRepo redisRepo;

    public List<Story.Out> getAll() {
        if (redisRepo.isAllCollectionInRedis()) {
            return redisRepo.findAll();
        }
        List<Story.Out> listResult = repo.findAll()
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

    public List<Story.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Story.Out get(Long id) {
        if (redisRepo.exists(id)) {
            return redisRepo.findById(id);
        }else {
            Story.Out result = repo
                    .findById(id)
                    .map(mapper::out)
                    .orElseThrow();
            redisRepo.save(id, result);
            return result;
        }
    }

    @Transactional
    public Story.Out create(Story.In input) {
        chekStory(input);
        chekCreator(input);
        Set<String> marksString = input.getMarks();
        Story story = mapper.in(input);
        story.setCreated(LocalDateTime.now());
        story.setModified(LocalDateTime.now());
        Story storyWithId = repo.save(story);
        if(marksString!=null) {
            for (String markName : marksString) {
                System.out.println();
                if(!markRepo.existsByName(markName)){
                    Mark mark = Mark.builder().name(markName).build();
                    Mark markWithId = markRepo.save(mark);
                    saveStoryMark(markWithId, storyWithId);
                }else {
                    Mark markWithId = markRepo.findByName(markName);
                    saveStoryMark(markWithId, storyWithId);
                }
            }
        }
        Story.Out result = mapper.out(repo.save(storyWithId));
        result.setMarks(marksString);
        redisRepo.save(result.getId(), result);
        return result;
    }

    private void chekCreator(Story.In input) {
        Creator creator2 = (Creator) creatorRepo.findById(input.getCreatorId()).orElse(null);
        if (creator2 == null){
           throw  new NoSuchElementException();
        }
    }

    private void chekStory(Story.In input) {
        Story story2 = (Story) repo.findByTitle(input.getTitle()).orElse(null);
        if (story2 != null) {
            throw new IllegalArgumentException("Story with title '" + input.getTitle() + "' already exists");
        }
    }

    private void saveStoryMark(Mark markWithId, Story storyWithId) {
        StoryMark storyMark =  StoryMark
                .builder()
                .story(storyWithId)
                .mark(markWithId)
                .build();
        StoryMark storyMarkWithId = storyMarkRepo.save(storyMark);
        storyWithId.addMarks(storyMarkWithId);
        markWithId.addStorys(storyMarkWithId);
    }

    public Story.Out update(Story.In input) {
        Story existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Creator not found with id: " + input.getId()));
        Story updated = mapper.in(input); // или частичное обновление:
         existing.setCreatorId(input.getCreatorId());
         existing.setTitle(input.getTitle());
         existing.setContent(input.getContent());
         existing.setCreated(input.getCreated());
         existing.setModified(LocalDateTime.now());
        Story.Out result = mapper.out(repo.save(updated));
        redisRepo.save(result.getId(), result);
        return result;
    }

    @Transactional
    public void delete(Long id) {
        Story story = repo.findById(id).orElseThrow(()-> new NoSuchElementException("Story not found with id: " + id));
        repo.deleteById(id);
        redisRepo.delete(id);
    }

}
