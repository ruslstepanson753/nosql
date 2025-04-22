package com.javarush.stepanov.mvc.controller;


import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.service.MarkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1.0/marks")
public class MarkController {

    private final MarkService service;

    @GetMapping("/{id}")
    public Mark.Out getMarkById(@PathVariable Long id) {
        return service.get(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mark.Out> getAllMarks2(
    )   {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mark.Out createMark(@RequestBody @Valid Mark.In input) {
        try {
            return service.create(input);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mark.Out  updateMark(@RequestBody @Valid Mark.In input) {
        try {
            return service.update(input);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mark.Out deleteMark(@PathVariable Long id) {

        return service.delete(id);
    }
}
