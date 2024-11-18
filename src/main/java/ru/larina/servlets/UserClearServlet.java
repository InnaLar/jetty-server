package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.model.dto.userClearDTO.UserClearResponse;
import ru.larina.mapper.MapperJSON;

import java.io.IOException;

public class UserClearServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserClearResponse rs = UserClearResponse.builder()
                .userId(Integer.valueOf(req.getParameter("userId")))
                .build();
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}