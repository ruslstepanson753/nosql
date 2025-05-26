package com.javarush.stepanov.publisher.security;

import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.repository.impl.CreatorRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatorDetailsService implements UserDetailsService {

    private final CreatorRepo creatorRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Creator creator = creatorRepository.findAll()
                .stream()
                .filter(u -> login.equals(u.getLogin()))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Editor not found with login: " + login));
        return creator;
    }

}