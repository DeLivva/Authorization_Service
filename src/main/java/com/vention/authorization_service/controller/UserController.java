package com.vention.authorization_service.controller;

import com.vention.authorization_service.dto.request.UserDeleteRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<UserUpdateResponseDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO dto) {
        return new ResponseEntity<>(userService.updateUser(dto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody UserDeleteRequestDTO dto) {
        userService.deleteUser(dto.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<UserUpdateResponseDTO> fillProfile(@Valid @RequestBody UserProfileFillRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.fillProfile(request));
    }

    @PostMapping("/picture/{user-id}")
    public ResponseEntity<String> uploadProfilePic(@PathVariable("user-id") Long userId,
                                                   @RequestParam("image") MultipartFile file) {
        String link = userService.uploadProfilePicture(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        UserResponseDTO userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/by-username")
    public ResponseEntity<UserResponseDTO> getByUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }
}
