package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.mapper.MapperJSON;
import ru.larina.mapper.TaskMapper;
import ru.larina.model.dto.taskDTO.TaskRegistrationRequest;
import ru.larina.model.dto.taskDTO.TaskRegistrationResponse;
import ru.larina.model.entity.Task;
import ru.larina.service.TaskService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TaskServlet extends HttpServlet {
    TaskService taskService = new TaskService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();
        ObjectMapper objectMapper = new ObjectMapper();
        TaskRegistrationRequest requestBody = objectMapper.readValue(jsonData, TaskRegistrationRequest.class);
        Task task = TaskMapper.taskRegistrationRequestToTask(requestBody);
        Task taskAdded = taskService.add(task);
        TaskRegistrationResponse rs = TaskMapper.TaskToTaskRegistrationResponse(taskAdded);
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();

    }
}
