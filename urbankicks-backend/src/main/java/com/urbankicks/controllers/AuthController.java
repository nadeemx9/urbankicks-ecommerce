package com.urbankicks.controllers;

import com.urbankicks.config.jwt.JwtService;
import com.urbankicks.entities.UserRegister;
import com.urbankicks.models.AuthenticationRequest;
import com.urbankicks.models.AuthenticationResponse;
import com.urbankicks.repositories.UserRegisterRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")

public class AuthController {
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final UserRegisterRepository userRepository;

    @Autowired
    public AuthController(UserDetailsService userDetailsService,
                          AuthenticationManager manager,
                          JwtService jwtService, UserRegisterRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.manager = manager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtService.generateToken(userDetails);

        UserRegister user = userRepository.findByUsernameIgnoreCase(userDetails.getUsername());
        user.setIsLoggedOut(false);
//        user.setJwtToken(token);
        userRepository.save(user);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .success(true)
                .token("Bearer " + token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Password.");
        }
    }

}