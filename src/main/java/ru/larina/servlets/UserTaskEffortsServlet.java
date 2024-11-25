package ru.larina.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import ru.larina.mapper.MapperJson;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.model.dto.userReportDTO.UserTimeRequest;
import ru.larina.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.larina.server.SimpleHttpServer.printJSON;

@AllArgsConstructor
public class UserTaskEffortsServlet extends HttpServlet {
    private UserService userService;
    private MapperJson mapperJson;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Long userId = Long.valueOf(req.getParameter("userId"));
        LocalDateTime startTime = LocalDateTime.parse(req.getParameter("startDateTime"), formatter);
        LocalDateTime stopTime = LocalDateTime.parse(req.getParameter("stopDateTime"), formatter);

        UserTimeRequest request = UserTimeRequest.builder()
            .userId(userId)
            .startTime(startTime)
            .stopTime(stopTime)
            .build();

        UserTaskEffortResponse response = userService.getUserTaskEffort(userId);
        String jsonString = mapperJson.dtoToJson(response);
        printJSON(resp, jsonString);
    }
}
