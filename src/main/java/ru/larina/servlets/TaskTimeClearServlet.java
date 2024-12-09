package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.service.TaskTimesService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskTimeClearServlet extends HttpServlet {
    private TaskTimesService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        UserTaskTimeClearResponse response = taskTimeService.clear(userId);
        String jsonString = objectMapper.writeValueAsString(response);
        printJSON(resp, jsonString);
    }
}
