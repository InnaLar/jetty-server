package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.task.TaskCreationRq;
import ru.larina.model.dto.task.TaskCreationRs;
import ru.larina.service.TaskService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.larina.SimpleHttpServer.printJson;

@AllArgsConstructor
public class TaskServlet extends HttpServlet {

    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        final TaskCreationRq taskCreationRq = objectMapper.readValue(jsonData, TaskCreationRq.class);
        final TaskCreationRs taskCreationRs = taskService.save(taskCreationRq);
        final String jsonString = objectMapper.writeValueAsString(taskCreationRs);
        printJson(resp, jsonString);

    }
}
