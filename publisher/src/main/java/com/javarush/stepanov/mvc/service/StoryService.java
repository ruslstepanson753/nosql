package com.javarush.stepanov.mvc.service;

import com.javarush.stepanov.mvc.mapper.StoryDto;
import com.javarush.stepanov.mvc.model.creator.Creator;
import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.story.Story;
import com.javarush.stepanov.mvc.model.storymark.StoryMark;
import com.javarush.stepanov.mvc.repository.impl.CreatorRepo;
import com.javarush.stepanov.mvc.repository.impl.MarkRepo;
import com.javarush.stepanov.mvc.repository.impl.StoryMarkRepo;
import com.javarush.stepanov.mvc.repository.impl.StoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
public class StoryService {

    private final StoryRepo storyRepo;
    private final MarkRepo markRepo;
    private final StoryDto mapper;
    private final CreatorRepo creatorRepo;
    private final StoryMarkRepo storyMarkRepo;

    public List<Story.Out> getAll() {
        return storyRepo
                .findAll()
                .stream()
                .map(mapper::out)
                .toList();
    }

    public List<Story.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return storyRepo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Story.Out get(Long id) {
        return storyRepo
                .findById(id)
                .map(mapper::out)
                .orElseThrow();
    }

    @Transactional
    public Story.Out create(Story.In input) {
        chekStory(input);
        chekCreator(input);
        Set<String> marksString = input.getMarks();
        Story story = mapper.in(input);
        story.setCreated(LocalDateTime.now());
        story.setModified(LocalDateTime.now());
        Story storyWithId = storyRepo.save(story);
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
        Story.Out result = mapper.out(storyRepo.save(storyWithId));
        result.setMarks(marksString);
        return result;    }

    private void chekCreator(Story.In input) {
        Creator creator2 = (Creator) creatorRepo.findById(input.getCreatorId()).orElse(null);
        if (creator2 == null){
           throw  new NoSuchElementException();
        }
    }

    private void chekStory(Story.In input) {
        Story story2 = (Story) storyRepo.findByTitle(input.getTitle()).orElse(null);
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
        Story existing = storyRepo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Creator not found with id: " + input.getId()));
        Story updated = mapper.in(input); // или частичное обновление:
         existing.setCreatorId(input.getCreatorId());
         existing.setTitle(input.getTitle());
         existing.setContent(input.getContent());
         existing.setCreated(input.getCreated());
         existing.setModified(LocalDateTime.now());
        return mapper.out(storyRepo.save(updated));
    }

    @Transactional
    public void delete(Long id) {
        Story story = storyRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Story not found with id: " + id));
        storyRepo.deleteById(id);
    }

}
