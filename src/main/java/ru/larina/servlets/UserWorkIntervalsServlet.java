package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeLongSpent;
import ru.larina.model.dto.userReportDTO.UserWorkIntervalsResponse;
import ru.larina.mapper.MapperJSON;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class UserWorkIntervalsServlet extends HttpServlet {
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
        UserWorkIntervalsResponse rs = UserWorkIntervalsResponse.builder()
                .userId(Integer.valueOf(req.getParameter("userId")))
                .workIntervals(List.of(tts1, tts2))
                .build();
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
