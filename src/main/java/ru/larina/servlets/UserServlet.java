package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
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
        final UserRegistrationResponse userRegistrationResponse = userService.getById(Long.valueOf(req.getParameter("userId")));
        final String jsonString = objectMapper.writeValueAsString(userRegistrationResponse);
        printJson(resp, jsonString);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        final String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        final UserRegistrationRequest userRegistrationRequest = objectMapper.readValue(jsonData, UserRegistrationRequest.class);
        final UserRegistrationResponse userRegistrationResponse = userService.create(userRegistrationRequest);

//        objectMapper.writeValue(resp.getWriter(), userRegistrationResponse);
//        resp.setStatus(HttpServletResponse.SC_OK);

        final String jsonString = objectMapper.writeValueAsString(userRegistrationResponse);
        printJson(resp, jsonString);
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        final String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        //jsonData = "{\"id\":\"123\",\"email\":\"InnaLar@mail.ru\"}";
        final UserPutRequest userPutRequest = objectMapper.readValue(jsonData, UserPutRequest.class);
        final UserPutResponse userPutResponse = userService.update(userPutRequest);
        final String jsonString = objectMapper.writeValueAsString(userPutResponse);
        printJson(resp, jsonString);
    }
}
