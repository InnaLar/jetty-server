package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.mapper.MapperJson;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.service.TaskTimeService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskTimeStartServlet extends HttpServlet {
    private TaskTimeService taskTimeService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.valueOf(req.getParameter("taskId"));
        TaskTimeResponse response = taskTimeService.start(taskId);
        String jsonString = objectMapper.writeValueAsString(response);
        printJSON(resp, jsonString);
    }
}
