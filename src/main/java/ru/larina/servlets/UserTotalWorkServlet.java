package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.model.dto.userReportDTO.UserTotalWorkByPeriodResponse;
import ru.larina.mapper.MapperJSON;

import java.io.IOException;
import java.time.Duration;

public class UserTotalWorkServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int rgUserId = Integer.parseInt(req.getParameter("userId"));
        UserTotalWorkByPeriodResponse rs = UserTotalWorkByPeriodResponse.builder()
                .userId(rgUserId)
                .totalWork(Duration.ofHours(2).plusMinutes(5).plusSeconds(30))
                .build();
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
