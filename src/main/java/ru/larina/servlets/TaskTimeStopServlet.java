package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.service.TaskTimesService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskTimeStopServlet extends HttpServlet {
    private TaskTimesService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Long taskId = Long.valueOf(req.getParameter("taskId"));
        final TaskTimeResponse response = taskTimeService.stop(taskId);
        final String jsonString = objectMapper.writeValueAsString(response);
        printJSON(resp, jsonString);
    }
}
