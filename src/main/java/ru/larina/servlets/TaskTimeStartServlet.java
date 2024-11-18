package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.mapper.MapperJSON;
import ru.larina.model.dto.taskTimeDTO.TaskTimeResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.service.TaskService;
import ru.larina.service.TaskTimeService;

import java.io.IOException;
import java.time.LocalDateTime;

public class TaskTimeStartServlet extends HttpServlet {
    TaskTimeService taskTimeService = new TaskTimeService();
    TaskService taskService = new TaskService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Task task = taskService.get(Long.valueOf(req.getParameter("taskId")));
        TaskTime taskTime = TaskTime.builder()
            .task(task)
            .startTime(LocalDateTime.now())
            .build();
        TaskTime taskTimeAdded = taskTimeService.add(taskTime);
        TaskTimeResponse rs = TaskTimeMapper.TaskTimeToTaskTimeResponse(taskTimeAdded);
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
