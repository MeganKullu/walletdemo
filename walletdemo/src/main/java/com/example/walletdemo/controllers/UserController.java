package com.example.walletdemo.controllers;


import com.example.walletdemo.dto.RegisterUserRequest;
import com.example.walletdemo.dto.SetupPinRequest;
import com.example.walletdemo.services.JwtService;
import com.example.walletdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")

public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserRequest request){
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registered successfully! Please await admin approval.");
    }

    @PostMapping("/setup-pin")
    public ResponseEntity<String> setupPin(@RequestBody SetupPinRequest request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        userService.setupPin(email, request.getPin());
        return ResponseEntity.ok("PIN setup successful");
    }

    @PostMapping("/verify-pin")
    public ResponseEntity<Boolean> verifyPin(@RequestBody SetupPinRequest request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization").replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        boolean isValid = userService.verifyPin(email, request.getPin());
        return ResponseEntity.ok(isValid);
    }

}
