package com.hburak_dev.psk_full_stack.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hburak_dev.psk_full_stack.authentication.AuthenticationService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final AuthenticationService service;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(Authentication connectedUser) {
        return ResponseEntity.ok(service.getUser(connectedUser));
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(
            @RequestBody @Valid UserRequest request,
            Authentication connectedUser) {
        return ResponseEntity.ok(service.updateUser(request, connectedUser));
    }
}
