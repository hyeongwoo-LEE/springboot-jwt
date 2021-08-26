package com.cos.jwt.controller;


import com.cos.jwt.model.User;
import com.cos.jwt.model.UserRole;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class RestApiController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home(){
        return "<h1>home</h1>";
    }


    @PostMapping("/token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("/join")
    public String join(@RequestBody User user){

        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);

        user.setPassword(encPassword);

        user.setRoleSet(Set.of(UserRole.USER));

        userRepository.save(user);

        return "회원가입완료";


    }

}
