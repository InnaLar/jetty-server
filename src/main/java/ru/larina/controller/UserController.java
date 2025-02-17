package ru.larina.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;
import ru.larina.model.dto.user.UserPutRequest;
import ru.larina.model.dto.user.UserPutResponse;
import ru.larina.model.dto.user.UserRegistrationRequest;
import ru.larina.model.dto.user.UserRegistrationResponse;
import ru.larina.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/user/{userId}")
    public UserRegistrationResponse getUserById(@PathVariable("userId") final Long Id) {
        return userService.getById(Id);
    }

    @PostMapping("/api/v1/user")
    public ResponseEntity<UserRegistrationResponse> addUser(@Valid @RequestBody final UserRegistrationRequest request) {
        if (!request.getEmail().startsWith("65")) {
            return ResponseEntity.badRequest()
                .build();
        }
        return ResponseEntity.ok(userService.create(request));
    }

    @PutMapping("/api/v1/user")
    public UserPutResponse updateUser(@Valid @RequestBody final UserPutRequest request) {
        return userService.update(request);
    }
}
