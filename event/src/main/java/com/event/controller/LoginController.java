package com.event.controller;

import com.event.config.JwtUtil;
import com.event.dto.request.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtUtil util;

    @PostMapping
    public String login(@RequestBody LoginDto dto){
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getName(), dto.getPassword()));
        String role = auth.getAuthorities().iterator()
                .next().getAuthority().replace("ROLE_", "");
        return util.generateToken(dto.getName(), role);
    }
}
