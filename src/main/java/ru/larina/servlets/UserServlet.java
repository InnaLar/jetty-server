package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.mapper.MapperJson;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class UserServlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        UserRegistrationRequest userRegistrationRequest = objectMapper.readValue(jsonData, UserRegistrationRequest.class);
        UserRegistrationResponse userRegistrationResponse = userService.save(userRegistrationRequest);
        String jsonString = objectMapper.writeValueAsString(userRegistrationResponse);
        printJSON(resp, jsonString);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String jsonData;
        try (Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8)) {
            jsonData = scanner.useDelimiter("\\A").next();
        }
        //jsonData = "{\"id\":\"123\",\"email\":\"InnaLar@mail.ru\"}";
        UserPutRequest userPutRequest = objectMapper.readValue(jsonData, UserPutRequest.class);
        UserPutResponse userPutResponse = userService.update(userPutRequest);
        String jsonString = objectMapper.writeValueAsString(userPutResponse);
        printJSON(resp, jsonString);
    }
}
