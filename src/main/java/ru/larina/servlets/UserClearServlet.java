package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userClear.UserDeleteTasksRs;
import ru.larina.service.UserService;

import java.io.IOException;

import static ru.larina.SimpleHttpServer.printJson;

@AllArgsConstructor
public class UserClearServlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Long userId = Long.valueOf(req.getParameter("userId"));
        final UserDeleteTasksRs userTaskTimeClearResponse = userService.deleteTasksByUser(userId);
        final String jsonString = objectMapper.writeValueAsString(userTaskTimeClearResponse);
        printJson(resp, jsonString);
    }
}
