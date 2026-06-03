package com.example.backend.controller;

import com.example.backend.model.dto.GoogleLoginDTO;
import com.example.backend.model.dto.UserLoginDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.model.entity.User;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "Authentication API")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/register")
    @ApiOperation("User Register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO registerDTO) {
        try {
            User user = userService.register(registerDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @ApiOperation("User Login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO loginDTO) {
        try {
            // Verify Email and password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT
            String jwt = jwtTokenProvider.generateToken(authentication.getPrincipal());
            
            // get User Information
            User user = userService.login(loginDTO);
            user.setLastLoginTime(LocalDateTime.now());
            userService.updateUser(user);
            
            // Return Token and User info
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Incorrect email or password");
        }
    }

    @PostMapping("/google-login")
    @ApiOperation("Google Login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginDTO GoogleLoginDTO) {


        try {

            User user = userService.getUserByEmail(GoogleLoginDTO.getEmail());

            if (user == null) {

                user = new User();
                String randomPassword =
                        UUID.randomUUID().toString();

                user.setUser_name(GoogleLoginDTO.getUsername());
                user.setUser_email(GoogleLoginDTO.getEmail());
                user.setUser_avatar(GoogleLoginDTO.getAvatar());

                user.setCreateTime(LocalDateTime.now());
                user.setLastLoginTime(LocalDateTime.now());
                user.setUser_password(
                        passwordEncoder.encode(randomPassword)
                );
                userService.insertGoogle(user);

                user = userService.getUserByEmail(GoogleLoginDTO.getEmail());

            } else {

                user.setLastLoginTime(LocalDateTime.now());
                userService.updateUser(user);
            }

            // generate JWT
            String jwt = jwtTokenProvider.generateToken(user.getUser_email());

            Map<String, Object> response = new HashMap<>();

            response.put("token", jwt);
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 