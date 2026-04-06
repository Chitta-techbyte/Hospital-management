package com.hms.hospitalmanagement.controller;

import com.hms.hospitalmanagement.entity.User;
import com.hms.hospitalmanagement.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.hms.hospitalmanagement.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.hms.hospitalmanagement.dto.LoginResponse;
import com.hms.hospitalmanagement.security.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        User user = userService.loginUser(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.ok(token);
    }
    @GetMapping("/test")
    public String test() {
        return "Working";
    }
}


