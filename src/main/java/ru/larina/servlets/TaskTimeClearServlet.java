package ru.larina.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.larina.mapper.MapperJSON;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearRequest;
import ru.larina.model.dto.userClearDTO.UserTaskTimeClearResponse;
import ru.larina.model.entity.Task;
import ru.larina.model.entity.TaskTime;
import ru.larina.model.entity.User;
import ru.larina.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TaskTimeClearServlet extends HttpServlet {
    UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Scanner scanner = new Scanner(req.getInputStream(), StandardCharsets.UTF_8);
        String jsonData = scanner.useDelimiter("\\A").next();
        scanner.close();
        ObjectMapper objectMapper = new ObjectMapper();
        UserTaskTimeClearRequest requestBody = objectMapper.readValue(jsonData, UserTaskTimeClearRequest.class);
        Long userId = Long.valueOf(requestBody.getUserId());
        User user = userService.get(userId);
        userService.clearTaskTimes(user);
        UserTaskTimeClearResponse rs = UserMapper.UserToUserTaskTimeClearResponse(user);
        // Преобразование объекта в JSON
        String jsonString = MapperJSON.dtoToJSON(rs);
        resp.setStatus(jakarta.servlet.http.HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }
}
