package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.service.TaskService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class UserClearServlet extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        UserTaskTimeClearResponse userTaskTimeClearResponse = taskService.userClearTaskTimes(userId);
        String jsonString = objectMapper.writeValueAsString(userTaskTimeClearResponse);
        printJSON(resp, jsonString);
    }
}
