package com.vention.authorization_service.controller;

import com.vention.authorization_service.dto.request.UserDeleteRequestDTO;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.CourierResponseDTO;
import com.vention.authorization_service.dto.response.UserResponseDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.service.SecurityHelperService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SecurityHelperService securityHelperService;

    @PutMapping
    public ResponseEntity<UserUpdateResponseDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO dto) {
        return new ResponseEntity<>(userService.updateUser(dto), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody UserDeleteRequestDTO dto) {
        userService.deleteUser(dto.getUserId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/fill-profile")
    public ResponseEntity<UserUpdateResponseDTO> fillProfile(@Valid @RequestBody UserProfileFillRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.fillProfile(request));
    }

    @PostMapping("/picture/{userId}")
    public ResponseEntity<String> uploadProfilePic(@PathVariable Long userId, MultipartFile image) {
        String link = userService.uploadProfilePicture(userId, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        UserResponseDTO userResponse = userService.getById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getByToken() {
        return ResponseEntity.ok(securityHelperService.getCurrentUser());
    }

    @GetMapping("/by-car-type")
    public ResponseEntity<List<CourierResponseDTO>> getAllByCarType(@RequestParam String carType) {
        return ResponseEntity.ok(userService.getAllByCarType(carType));
    }

    @GetMapping("/all-ids")
    public ResponseEntity<List<Long>> getUsersIdList(@RequestParam boolean isCourier) {
        return ResponseEntity.ok(userService.getUsersIdList(isCourier));
    }
}
