package com.vention.authorization_service.controller;

import com.vention.authorization_service.dto.response.SecurityCredentialsResponseDTO;
import com.vention.authorization_service.service.SecurityCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/security-credentials")
@RequiredArgsConstructor
public class SecurityCredentialsController {

    private final SecurityCredentialService securityCredentialService;

    @GetMapping()
    public ResponseEntity<SecurityCredentialsResponseDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(securityCredentialService.getByEmail(email));
    }
}
