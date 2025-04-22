package com.javarush.stepanov.mvc.service;


import com.javarush.stepanov.mvc.mapper.NoticeDto;
import com.javarush.stepanov.mvc.model.notice.Notice;
import com.javarush.stepanov.mvc.model.story.Story;
import com.javarush.stepanov.mvc.repository.impl.NoticeRepo;
import com.javarush.stepanov.mvc.repository.impl.StoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class NoticeService {

    private final StoryRepo storyRepo;
    private final NoticeRepo repo;
    private final NoticeDto mapper;

    public List<Notice.Out> getAll() {
        return repo
                .findAll()
                .stream()
                .map(mapper::out)
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
        List<Notice> list= repo.findAll();
        for(Notice NoticeInList : list ){
            if((NoticeInList.getId().equals(notice.getId()))){
                throw new NoSuchElementException();
            }
        }
        return mapper.out(
                repo.save(notice));
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
