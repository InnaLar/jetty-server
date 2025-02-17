package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.exception.ServiceException;
import ru.larina.model.dto.taskTime.TaskTimeResponse;
import ru.larina.service.TaskTimesService;

import java.io.IOException;

import static ru.larina.SimpleHttpServer.printJson;

@AllArgsConstructor
public class TaskTimeStartServlet extends HttpServlet {
    private TaskTimesService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServiceException {
        final Long taskId = Long.valueOf(req.getParameter("taskId"));
        final TaskTimeResponse response = taskTimeService.start(taskId);
        final String jsonString = objectMapper.writeValueAsString(response);
        printJson(resp, jsonString);
    }
}
