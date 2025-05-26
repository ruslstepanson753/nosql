package com.javarush.stepanov.publisher.security;

import com.javarush.stepanov.publisher.model.creator.Creator;
import com.javarush.stepanov.publisher.service.CreatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2.0")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CreatorService creatorService;

    public AuthController(AuthenticationManager authenticationManager, 
                         JwtUtils jwtUtils,
                          CreatorService creatorService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.creatorService = creatorService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Creator.In loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getLogin(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
        
        return ResponseEntity.ok(new JwtResponse(jwt));
    }


}