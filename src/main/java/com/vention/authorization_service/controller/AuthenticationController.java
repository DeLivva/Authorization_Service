package com.vention.authorization_service.controller;

import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.UserRegistrationResponse;
import com.vention.authorization_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registerUser(request));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<String> confirmEmail(@RequestParam String token){
        authenticationService.confirmEmail(token);
        return ResponseEntity.status(HttpStatus.OK).body("Email successfully verified");
    }

    @GetMapping("/resend-confirmation")
    public ResponseEntity<String> resendConfirmationToken(@RequestParam String email){
        authenticationService.sendConfirmationToken(email);
        return ResponseEntity.status(HttpStatus.CREATED).body("Confirmation link send to your email");
    }
}
