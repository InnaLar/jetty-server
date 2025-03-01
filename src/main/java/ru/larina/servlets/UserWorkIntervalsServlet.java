package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userReport.UserWorkIntervalsRs;
import ru.larina.service.ReportService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.larina.SimpleHttpServer.printJson;

@AllArgsConstructor
public class UserWorkIntervalsServlet extends HttpServlet {
    private final ReportService reportService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final Long userId = Long.valueOf(req.getParameter("userId"));
        final LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startDateTime"), formatter);
        final LocalDateTime stopTime = LocalDateTime.parse(req.getParameter("stopDateTime"), formatter);
        final UserWorkIntervalsRs userTaskEffortResponse = reportService.getUserWorkIntervalByPeriods(userId, startTime, stopTime);
        final String jsonString = objectMapper.writeValueAsString(userTaskEffortResponse);

        printJson(resp, jsonString);
    }
}
