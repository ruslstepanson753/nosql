package com.javarush.stepanov.mvc.controller;

import com.javarush.stepanov.mvc.model.creator.Creator;
import com.javarush.stepanov.mvc.service.CreatorService;
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
@RequestMapping("/api/v1.0/creators")
public class CreatorController {

    private final CreatorService service;

    @GetMapping("/{id}")
    public Creator.Out getCreatorById(@PathVariable Long id) {
        return service.get(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Creator.Out> getAllCreators2(
    )   {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Creator.Out createCreator(@RequestBody @Valid Creator.In input) {
        try {
            return service.create(input);
        }catch (NoSuchElementException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Creator.Out  updateCreator(@RequestBody @Valid Creator.In input) {
        try {
            return service.update(input);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteCreator(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}