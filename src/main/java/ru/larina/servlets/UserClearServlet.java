package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.service.TaskService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJson;

@AllArgsConstructor
public class UserClearServlet extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Long userId = Long.valueOf(req.getParameter("userId"));
        final UserTaskTimeClearResponse userTaskTimeClearResponse = taskService.userClearTaskTimes(userId);
        final String jsonString = objectMapper.writeValueAsString(userTaskTimeClearResponse);
        printJson(resp, jsonString);
    }
}
