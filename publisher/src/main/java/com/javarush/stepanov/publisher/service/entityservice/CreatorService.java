package com.javarush.stepanov.publisher.service.entityservice;

import com.javarush.stepanov.publisher.mapper.CreatorDto;
import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.CreatorRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CreatorService {

    private final CreatorRepo repo;
    private final CreatorDto mapper;
    private final CreatorRedisRepo redisRepo;

    public List<Creator.Out> getAll() {
        return repo
                .findAll()
                .stream()
                .map(mapper::out)
                .toList();
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
        List<Creator> list= repo.findAll();
        for(Creator creatorInList : list ){
            if((creatorInList.getId().equals(creator.getId()))||(creatorInList.getLogin().equals(creator.getLogin()))){
                throw new NoSuchElementException();
            }
        }
        return mapper.out(
                repo.save(creator));
    }

    public Creator.Out update(Creator.In input) {
        Creator existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Creator not found with id: " + input.getId()));
        Creator updated = mapper.in(input); // или частичное обновление:
         existing.setLogin(input.getLogin());
         existing.setPassword(input.getPassword());
         existing.setFirstname(input.getFirstname());
         existing.setLastname(input.getLastname());
        return mapper.out(repo.save(updated));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

}
