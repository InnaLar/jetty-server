package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.service.TaskTimesService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJson;

@AllArgsConstructor
public class TaskTimeClearServlet extends HttpServlet {
    private TaskTimesService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Long userId = Long.valueOf(req.getParameter("userId"));
        final UserTaskTimeClearResponse response = taskTimeService.clear(userId);
        final String jsonString = objectMapper.writeValueAsString(response);
        printJson(resp, jsonString);
    }
}
