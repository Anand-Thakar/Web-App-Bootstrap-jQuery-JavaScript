package com.example.demo.controller;

import com.example.demo.dto.JwtAuthResponse;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignUpDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
@Api(value = "Sign-in/Sign-up Rest Api")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Rest Api to Sign in to Blog app")
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserNameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get token from token provider
        String token = tokenProvider.generateToken(authentication);
        return new ResponseEntity<>(new JwtAuthResponse(token), HttpStatus.OK);
    }

    @ApiOperation(value = "Rest Api to Signup to Blog app")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        System.out.println(signUpDto);
        //checking for userName
        if (userRepository.existsByUserName(signUpDto.getUserName())){
            return new ResponseEntity<>("username not available", HttpStatus.BAD_REQUEST);
        }
        //checking for email exists in DB

        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("email isn't available", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUserName());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        userRepository.save(user);
        return new ResponseEntity<>("user registered successfully",HttpStatus.OK);
    }
}

