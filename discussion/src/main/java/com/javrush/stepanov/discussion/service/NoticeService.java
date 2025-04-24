package com.javrush.stepanov.discussion.service;

import com.javrush.stepanov.discussion.mapper.NoticeDto;
import com.javrush.stepanov.discussion.model.Notice;
import com.javrush.stepanov.discussion.model.NoticeKey;
import com.javrush.stepanov.discussion.repo.NoticeCassandraRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class NoticeService {

    private final NoticeCassandraRepository repo;
    private final NoticeDto mapper;

    public NoticeService(NoticeCassandraRepository repo, NoticeDto mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

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
        Optional<Notice> notice = repo.findOneByKeyId(id);
        return mapper.out(notice.orElseThrow(() -> new NoSuchElementException()));
    }

    public Notice.Out create(Notice.In input) {
        String country = "RU";
        Long id = UUID.randomUUID().getLeastSignificantBits();
        Long storyId = input.getStoryId();
        NoticeKey noticeKey = new NoticeKey(country,id,storyId);
        Notice notice = mapper.in(input);
        notice.setKey(noticeKey);
        return mapper.out(
                repo.save(notice));

    }

    public Notice.Out update(Notice.In input) {
        String country = "RU";
        Long id = input.getId();
        Long storyId = input.getStoryId();
        String content = input.getContent();
        NoticeKey noticeKey = new NoticeKey(country,id,storyId);
        Notice noticeOld = repo.findOneByKeyId(id).orElseThrow(() -> new NoSuchElementException());
        Notice newNotice = new Notice();
        newNotice.setKey(noticeKey);
        newNotice.setContent(content);
        repo.delete(noticeOld);
        return mapper.out(repo.save(newNotice));
    }

    public void delete(Long id) {

    }

}
