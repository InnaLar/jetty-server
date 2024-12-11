package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.taskDTO.TaskCreationRequest;
import ru.larina.model.dto.taskDTO.TaskCreationResponse;
import ru.larina.service.TaskService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.larina.server.SimpleHttpServer.printJson;

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
        final TaskCreationRequest taskCreationRequest = objectMapper.readValue(jsonData, TaskCreationRequest.class);
        final TaskCreationResponse taskCreationResponse = taskService.save(taskCreationRequest);
        final String jsonString = objectMapper.writeValueAsString(taskCreationResponse);
        printJson(resp, jsonString);

    }
}
