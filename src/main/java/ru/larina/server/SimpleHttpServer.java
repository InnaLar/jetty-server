package ru.larina.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.larina.mapper.TaskMapper;
import ru.larina.mapper.TaskTimeMapper;
import ru.larina.mapper.UserMapper;
import ru.larina.repository.TaskRepository;
import ru.larina.repository.TaskTimeRepository;
import ru.larina.repository.UserRepository;
import ru.larina.repository.impl.TaskRepositoryImpl;
import ru.larina.repository.impl.TaskTimeRepositoryImpl;
import ru.larina.repository.impl.UserRepositoryImpl;
import ru.larina.service.ObjectMapperConfigured;
import ru.larina.service.TaskService;
import ru.larina.service.TaskTimesService;
import ru.larina.service.UserService;
import ru.larina.servlets.TaskServlet;
import ru.larina.servlets.TaskTimeClearServlet;
import ru.larina.servlets.TaskTimeStartServlet;
import ru.larina.servlets.TaskTimeStopServlet;
import ru.larina.servlets.UserClearServlet;
import ru.larina.servlets.UserServlet;
import ru.larina.servlets.UserTaskEffortsServlet;
import ru.larina.servlets.UserTotalWorkServlet;
import ru.larina.servlets.UserWorkIntervalsServlet;

import java.io.IOException;

public class SimpleHttpServer {

    public static void printJSON(HttpServletResponse resp, String jsonString) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }

    public static void main(String[] args) throws Exception {

        // Создаём сервер на порту 8080
        Server server = new Server(8080);

        // Настраиваем контекст сервлетов
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        ObjectMapperConfigured objectMapperConfigured = new ObjectMapperConfigured(new ObjectMapper());
        ObjectMapper objectMapper = objectMapperConfigured.getObjectMapperConfigured();

        TaskRepository taskRepository = new TaskRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        TaskTimeRepository taskTimeRepository = new TaskTimeRepositoryImpl();

        UserMapper userMapper = new UserMapper();
        TaskMapper taskMapper = new TaskMapper();
        TaskTimeMapper taskTimeMapper = new TaskTimeMapper();

        UserService userService = new UserService(userRepository, userMapper);
        TaskService taskService = new TaskService(taskRepository, userRepository, taskMapper);
        TaskTimesService taskTimeService = new TaskTimesService(taskTimeRepository, taskRepository, userRepository, userMapper, taskTimeMapper);

        // Добавляем сервлет для обработки запросов
        server.setHandler(handler);
        server.setErrorHandler(new ErrorHandler());
        handler.addServlet(new ServletHolder(new UserServlet(userService, objectMapper)), "/api/v1/user");
        handler.addServlet(new ServletHolder(new TaskServlet(taskService, objectMapper)), "/api/v1/task");
        handler.addServlet(new ServletHolder(new TaskTimeStartServlet(taskTimeService, objectMapper)), "/api/v1/task-time/start");
        handler.addServlet(new ServletHolder(new TaskTimeStopServlet(taskTimeService, objectMapper)), "/api/v1/task-time/stop");
        handler.addServlet(new ServletHolder(new TaskTimeClearServlet(taskTimeService, objectMapper)), "/api/v1/task-time/clear");
        handler.addServlet(new ServletHolder(new UserTaskEffortsServlet(taskService, objectMapper)), "/api/v1/report/getUserTaskEfforts");
        handler.addServlet(new ServletHolder(new UserWorkIntervalsServlet(taskService, objectMapper)), "/api/v1/report/getUserWorkIntervalsByPeriod");
        handler.addServlet(new ServletHolder(new UserTotalWorkServlet(taskService, objectMapper)), "/api/v1/report/getUserTotalWorkByPeriod");
        handler.addServlet(new ServletHolder(new UserClearServlet(taskService, objectMapper)), "/api/v1/user/clear");

        // Запускаем сервер
        server.start();
        server.join();

    }
    // Простой сервлет

}
