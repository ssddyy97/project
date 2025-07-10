package com.example.homework.controller;

import com.example.homework.domain.User;
import com.example.homework.dto.UserDto;
import com.example.homework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger; // Added
import org.slf4j.LoggerFactory; // Added

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class); // Added

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto.Response> signUpApi(@RequestBody UserDto.SignUpRequest requestDto) {
        User user = userService.createUser(requestDto);
        return ResponseEntity.ok(UserDto.Response.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        List<UserDto.Response> users = userService.findAllUsers().stream()
                .map(UserDto.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id)
                .map(user -> ResponseEntity.ok(UserDto.Response.fromEntity(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    
}
