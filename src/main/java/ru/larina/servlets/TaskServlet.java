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

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskServlet extends HttpServlet {

    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jsonData;

        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        TaskCreationRequest taskCreationRequest = objectMapper.readValue(jsonData, TaskCreationRequest.class);

        TaskCreationResponse taskCreationResponse = taskService.save(taskCreationRequest);

        String jsonString = objectMapper.writeValueAsString(taskCreationResponse);

        printJSON(resp, jsonString);

    }
}
