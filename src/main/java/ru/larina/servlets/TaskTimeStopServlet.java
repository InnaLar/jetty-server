package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.mapper.MapperJSON;
import ru.larina.model.entity.TaskTime;
import ru.larina.service.TaskTimeService;

import java.io.IOException;
import java.time.LocalDateTime;

public class TaskTimeStopServlet extends HttpServlet {
    TaskTimeService taskTimeService = new TaskTimeService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long taskId = Long.valueOf(req.getParameter("taskId"));
        TaskTime taskTime = taskTimeService.getLast(taskId);
        taskTime.setStopTime(LocalDateTime.now());
        TaskTime taskTimeChanged = taskTimeService.update(taskTime);
        TaskTimeResponse rs = TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTimeChanged);

        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
