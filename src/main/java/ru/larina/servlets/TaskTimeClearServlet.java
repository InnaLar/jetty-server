package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.mapper.MapperJson;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.service.TaskTimeService;

import java.io.IOException;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class TaskTimeClearServlet extends HttpServlet {
    private TaskTimeService taskTimeService;
    private MapperJson mapperJson;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        UserTaskTimeClearResponse response = taskTimeService.clear(userId);
        String jsonString = mapperJson.dtoToJson(response);
        printJSON(resp, jsonString);
    }
}
