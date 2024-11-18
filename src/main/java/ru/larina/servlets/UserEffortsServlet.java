package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.model.dto.taskTimeDTO.TaskTimeShortSpent;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.mapper.MapperJSON;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class UserEffortsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TaskTimeShortSpent tts1 = TaskTimeShortSpent.builder()
                .taskId(321)
                .timeSpent(Duration.ofHours(4).plusMinutes(5).plusSeconds(30))
                .build();
        TaskTimeShortSpent tts2 = TaskTimeShortSpent.builder()
                .taskId(322)
                .timeSpent(Duration.ofHours(1).plusMinutes(5).plusSeconds(30))
                .build();
        UserTaskEffortResponse rs = UserTaskEffortResponse.builder()
                .userId(Integer.valueOf(req.getParameter("userId")))
                .taskEfforts(List.of(tts1, tts2))
                .build();
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
