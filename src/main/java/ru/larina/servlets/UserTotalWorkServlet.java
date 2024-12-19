package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userReportDTO.UserTotalWorkByPeriodResponse;
import ru.larina.service.ReportService;
import ru.larina.service.TaskService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.larina.server.SimpleHttpServer.printJson;

@AllArgsConstructor
public class UserTotalWorkServlet extends HttpServlet {
    final private ReportService reportService;
    final private ObjectMapper objectMapper;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final Long userId = Long.valueOf(req.getParameter("userId"));
        final LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startDateTime"), formatter);
        final LocalDateTime stopTime = LocalDateTime.parse(req.getParameter("stopDateTime"), formatter);

        final UserTotalWorkByPeriodResponse userTotalWorkByPeriodResponse = reportService.getUserTotalWorkByPeriod(userId, startTime, stopTime);

        final String jsonString = objectMapper.writeValueAsString(userTotalWorkByPeriodResponse);

        printJson(resp, jsonString);
    }
}
