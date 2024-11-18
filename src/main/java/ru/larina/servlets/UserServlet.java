package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.larina.mapper.MapperJSON;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.entity.User;
import ru.larina.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();
        ObjectMapper objectMapper = new ObjectMapper();
        UserRegistrationRequest requestBody = objectMapper.readValue(jsonData, UserRegistrationRequest.class);
        User user = UserMapper.UserRegistrationRequestToUser(requestBody);
        User userAdded = userService.add(user);
        UserRegistrationResponse rs = UserMapper.UserToUserRegistrationResponse(userAdded);
        // Преобразование объекта в JSON
        String jsonString = objectMapper.writeValueAsString(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();
        ObjectMapper objectMapper = new ObjectMapper();
        //jsonData = "{\"id\":\"123\",\"email\":\"InnaLar@mail.ru\"}";
        UserPutRequest requestBody = objectMapper.readValue(jsonData, UserPutRequest.class);
        User user = UserMapper.UserPutRequestToUser(requestBody);
        User userChanged = userService.update(user);
        UserPutResponse rs = UserMapper.UserToUserPutResponse(userChanged);
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
