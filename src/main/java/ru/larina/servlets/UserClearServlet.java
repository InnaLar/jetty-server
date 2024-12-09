package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.service.ObjectMapperConfigured;
import ru.larina.model.dto.userClearDTO.UserClearResponse;

import java.io.IOException;

@AllArgsConstructor
public class UserClearServlet extends HttpServlet {
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UserClearResponse response = UserClearResponse.builder()
            .userId(Integer.valueOf(req.getParameter("userId")))
            .build();
        // Преобразование объекта в JSON
        String jsonString = objectMapper.writeValueAsString(response);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
