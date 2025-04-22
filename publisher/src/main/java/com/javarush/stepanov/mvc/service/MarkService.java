package com.javarush.stepanov.mvc.service;

import com.javarush.stepanov.mvc.mapper.MarkDto;
import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.repository.impl.MarkRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class MarkService {

    private final MarkRepo repo;
    private final MarkDto mapper;

    public List<Mark.Out> getAll() {
        return repo
                .findAll()
                .stream()
                .map(mapper::out)
                .toList();
    }

    public List<Mark.Out> getAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return repo.
                findAll(pageable)
                .map(mapper::out)
                .getContent();
    }

    public Mark.Out get(Long id) {
        return repo
                .findById(id)
                .map(mapper::out)
                .orElseThrow();
    }

    public Mark.Out create(Mark.In input) {
        Mark Mark = mapper.in(input);
        List<Mark> list= repo.findAll();
        for(Mark MarkInList : list ){
            if((MarkInList.getId().equals(Mark.getId()))||(MarkInList.getName().equals(Mark.getName()))){
                throw new NoSuchElementException();
            }
        }
        return mapper.out(
                repo.save(Mark));
    }

    public Mark.Out update(Mark.In input) {
        Mark existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Mark not found with id: " + input.getId()));
        Mark updated = mapper.in(input); // или частичное обновление:
         existing.setName(input.getName());
        return mapper.out(repo.save(updated));
    }

    public Mark.Out delete(Long id) {
        Mark mark = repo.findById(id).orElseThrow(()-> new ResponseStatusException(
                HttpStatus.CONFLICT,
                "mark with id '" + id + " not exists"
        ));
        repo.deleteById(id);
        return mapper.out(mark);
    }
}
