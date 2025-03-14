package com.example.demo.Controller;

import com.example.demo.Model.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.MyAppUser;
import com.example.demo.Model.MyAppUserRepository;

@RestController
public class RegistrationController {
    
    @Autowired
    private MyAppUserRepository myAppUserRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping(value = "/req/signup", consumes = "application/json")
    public MyAppUser createUser(@RequestBody SignupRequest signupRequest){
        MyAppUser user = new MyAppUser();
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());

        String role = signupRequest.isAdmin() ? "ADMIN" : "USER";
        user.setRole(role);
        return myAppUserRepository.save(user);
    }
    
}
