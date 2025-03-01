package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.user.UserPutRq;
import ru.larina.model.dto.user.UserPutRs;
import ru.larina.model.dto.user.UserRegistrationRq;
import ru.larina.model.dto.user.UserRegistrationRs;
import ru.larina.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.larina.SimpleHttpServer.printJson;

@AllArgsConstructor
public class UserServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final UserRegistrationRs userRegistrationRs = userService.getById(Long.valueOf(req.getParameter("userId")));
        final String jsonString = objectMapper.writeValueAsString(userRegistrationRs);
        printJson(resp, jsonString);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        final UserRegistrationRq userRegistrationRq = objectMapper.readValue(jsonData, UserRegistrationRq.class);
        final UserRegistrationRs userRegistrationRs = userService.create(userRegistrationRq);

        final String jsonString = objectMapper.writeValueAsString(userRegistrationRs);
        printJson(resp, jsonString);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        //jsonData = "{\"id\":\"123\",\"email\":\"InnaLar@mail.ru\"}";
        final UserPutRq userPutRq = objectMapper.readValue(jsonData, UserPutRq.class);
        final UserPutRs userPutRs = userService.update(userPutRq);
        final String jsonString = objectMapper.writeValueAsString(userPutRs);
        printJson(resp, jsonString);
    }
}
