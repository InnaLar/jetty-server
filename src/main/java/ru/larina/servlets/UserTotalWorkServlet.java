package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.mapper.MapperJson;
import ru.larina.model.dto.userReportDTO.UserTotalWorkByPeriodResponse;

import java.io.IOException;
import java.time.Duration;

@AllArgsConstructor
public class UserTotalWorkServlet extends HttpServlet {
    private MapperJson mapperJson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int rgUserId = Integer.parseInt(req.getParameter("userId"));
        UserTotalWorkByPeriodResponse response = UserTotalWorkByPeriodResponse.builder()
            .userId(rgUserId)
            .totalWork(Duration.ofHours(2).plusMinutes(5).plusSeconds(30))
            .build();
        String jsonString = mapperJson.dtoToJson(response);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
