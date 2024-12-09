package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.service.ObjectMapperConfigured;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;
import ru.larina.model.dto.userReportDTO.UserWorkIntervalsResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class UserWorkIntervalsServlet extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TaskTimeLongSpent tts1 = TaskTimeLongSpent.builder()
            .taskId(321)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.now())
            .timeSpent(Duration.ofHours(4).plusMinutes(5).plusSeconds(30))
            .build();
        TaskTimeLongSpent tts2 = TaskTimeLongSpent.builder()
            .taskId(322)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.now())
            .timeSpent(Duration.ofHours(1).plusMinutes(5).plusSeconds(30))
            .build();
        UserWorkIntervalsResponse response = UserWorkIntervalsResponse.builder()
            .userId(Integer.valueOf(req.getParameter("userId")))
            .workIntervals(List.of(tts1, tts2))
            .build();
        // Преобразование объекта в JSON
        String jsonString = objectMapper.writeValueAsString(response);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
