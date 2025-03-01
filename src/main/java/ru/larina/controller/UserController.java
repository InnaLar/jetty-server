package ru.larina.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.larina.model.dto.user.UserPutRq;
import ru.larina.model.dto.user.UserPutRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.model.dto.userClear.UserTaskTimeClearRs;
import ru.larina.model.dto.userReport.UserTaskEffortRs;
import ru.larina.model.projections.TaskTimeShortSpentProjection;
import ru.larina.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/v1/user/{userId}")
    public UserRegistrationRs getUserById(@PathVariable("userId") final Long id) {
        return userService.getById(id);
    }

    @PostMapping("/api/v1/user")
    public ResponseEntity<UserRegistrationRs> addUser(@Valid @RequestBody final UserRegistrationRq request) {
        if (!request.getEmail().startsWith("65")) {
            return ResponseEntity.badRequest()
                .build();
        }
        return ResponseEntity.ok(userService.create(request));
    }

    @PutMapping("/api/v1/user")
    public UserPutRs updateUser(@Valid @RequestBody final UserPutRq request) {
        return userService.update(request);
    }

    @PostMapping("api/v1/task-time/clear")
    public UserTaskTimeClearRs clearByUser(final @RequestParam Long userId) {
        return userService.clearByUser(userId);
    }

    @GetMapping("/api/v1/report/getUserTaskEfforts")
    public UserTaskEffortRs getUserTaskEffortsByPeriods(final @RequestParam Long userId,
                                                        final @RequestParam LocalDateTime startTime,
                                                        final @RequestParam LocalDateTime stopTime) {
        return userService.getUserTaskEffortsByPeriods(userId, startTime, stopTime);
    }
}
