package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.exception.ServiceException;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.service.TaskTimesService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskTimeStartServlet extends HttpServlet {
    private TaskTimesService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServiceException {
        Long taskId = Long.valueOf(req.getParameter("taskId"));
        TaskTimeResponse response = taskTimeService.start(taskId);
        String jsonString = objectMapper.writeValueAsString(response);
        printJSON(resp, jsonString);
    }
}
