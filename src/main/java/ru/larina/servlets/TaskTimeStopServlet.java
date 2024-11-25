package ru.larina.servlets;

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
public class TaskTimeStopServlet extends HttpServlet {
    private TaskTimeService taskTimeService;
    private MapperJson mapperJson;

    @Override
    protected void doPost(HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final Long taskId = Long.valueOf(req.getParameter("taskId"));
        final TaskTimeResponse response = taskTimeService.stop(taskId);
        final String jsonString = mapperJson.dtoToJson(response);
        printJSON(resp, jsonString);
    }
}
