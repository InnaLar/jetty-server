package ru.larina.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.larina.model.dto.taskTime.TaskTimeRs;
import ru.larina.service.TaskTimesService;

@RestController
@RequiredArgsConstructor
public class TaskTimeController {
    private final TaskTimesService taskTimesService;

    @PostMapping("/api/v1/task-time/start")
    TaskTimeRs start(final @RequestParam Long taskId) {
        return taskTimesService.start(taskId);
    }

    @PutMapping("/api/v1/task-time/stop")
    TaskTimeRs stop(final @RequestParam Long taskId) {
        return taskTimesService.stop(taskId);
    }

}
