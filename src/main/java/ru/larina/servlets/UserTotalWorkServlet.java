package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.model.dto.userReportDTO.UserTotalWorkByPeriodResponse;
import ru.larina.model.dto.userReportDTO.UserWorkIntervalsResponse;
import ru.larina.service.TaskService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class UserTotalWorkServlet extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Long userId = Long.valueOf(req.getParameter("userId"));
        LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startDateTime"), formatter);
        LocalDateTime stopTime = LocalDateTime.parse(req.getParameter("stopDateTime"), formatter);

        UserTotalWorkByPeriodResponse userTotalWorkByPeriodResponse  = taskService.getUserTotalWorkByPeriod(userId, startTime, stopTime);

        String jsonString = objectMapper.writeValueAsString(userTotalWorkByPeriodResponse);

        printJSON(resp, jsonString);
    }
}
