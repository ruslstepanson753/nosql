package com.javarush.stepanov.publisher.service;

import com.javarush.stepanov.publisher.mapper.CreatorDto;
import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.model.creator.Role;
import com.javarush.stepanov.publisher.repository.dbrepo.CreatorRepo;
import com.javarush.stepanov.publisher.repository.redisrepo.impl.CreatorRedisRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class CreatorService {

    private final CreatorRepo repo;
    private final CreatorDto mapper;
    private final PasswordEncoder passwordEncoder;
    private final CreatorRedisRepo redisRepo;

    public List<Creator.Out> getAll() {
        return repo
                .findAll()
                .stream()
                .map(mapper::out)
                .toList();
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
        if(creator.getRole()==null){
            creator.setRole(Role.USER);
        }
        String dbPass = passwordEncoder.encode(input.getPassword());
        creator.setPassword(dbPass);
        Creator.Out result = mapper.out(
                repo.save(creator));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public Creator.Out update(Creator.In input) {
        Creator existing = repo.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("Creator not found with id: " + input.getId()));
         existing.setLogin(input.getLogin());
        String dbPass = passwordEncoder.encode(input.getPassword());
        existing.setPassword(dbPass);
         existing.setFirstname(input.getFirstname());
         existing.setLastname(input.getLastname());
        Creator.Out result = mapper.out(repo.save(existing));
        redisRepo.save(result.getId(), result);
        return result;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        redisRepo.delete(id);
        repo.deleteById(id);
    }

}
