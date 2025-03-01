package ru.larina;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@SuppressWarnings({"UncommentedMain", "ClassDataAbstractionCoupling", "ClassFanOutComplexity"})
public class SimpleHttpServer {

    public static void printJson(final HttpServletResponse resp, final String jsonString) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().println(jsonString);
        resp.getWriter().close();
    }

    //    public static void main(final String[] args) throws Exception {
    //
    //    // Создаём сервер на порту 8080
    //    final Server server = new Server(8080);
    //
    //    // Настраиваем контекст сервлетов
    //    final ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    //    handler.setContextPath("/");
    //
    //    final ObjectMapperConfigured objectMapperConfigured = new ObjectMapperConfigured(new ObjectMapper());
    //    final ObjectMapper objectMapper = objectMapperConfigured.getObjectMapperConfigured();
    //
    //    final TaskRepository taskRepository = new TaskRepositoryImpl();
    //    final UserRepository userRepository = new UserRepositoryImpl();
    //    final TaskTimeRepository taskTimeRepository = new TaskTimeRepositoryImpl();
    //
    //    final UserMapper userMapper = new UserMapper();
    //    final TaskMapper taskMapper = new TaskMapper();
    //    final TaskTimeMapper taskTimeMapper = new TaskTimeMapper();
    //
    //    final UserService userService = new UserService(userRepository, userMapper);
    //    final TaskService taskService = new TaskService(taskRepository, userRepository, taskMapper);
    //    final TaskTimesService taskTimeService = new TaskTimesService(taskTimeRepository, taskRepository, userRepository, userMapper, taskTimeMapper);
    //    final ReportService reportService = new ReportService(taskRepository);
    //
    //    // Добавляем сервлет для обработки запросов
    //    server.setHandler(handler);
    //    server.setErrorHandler(new ErrorHandler());
    //    handler.addServlet(new ServletHolder(new UserServlet(userService, objectMapper)), "/api/v1/user");
    //    handler.addServlet(new ServletHolder(new TaskServlet(taskService, objectMapper)), "/api/v1/task");
    //    handler.addServlet(new ServletHolder(new TaskTimeStartServlet(taskTimeService, objectMapper)), "/api/v1/task-time/start");
    //    handler.addServlet(new ServletHolder(new TaskTimeStopServlet(taskTimeService, objectMapper)), "/api/v1/task-time/stop");
    //    handler.addServlet(new ServletHolder(new TaskTimeClearServlet(taskTimeService, objectMapper)), "/api/v1/task-time/clear");
    //    handler.addServlet(new ServletHolder(new UserTaskEffortsServlet(reportService, objectMapper)), "/api/v1/report/getUserTaskEfforts");
    //    handler.addServlet(new ServletHolder(new UserWorkIntervalsServlet(reportService, objectMapper)), "/api/v1/report/getUserWorkIntervalsByPeriod");
    //    handler.addServlet(new ServletHolder(new UserTotalWorkServlet(reportService, objectMapper)), "/api/v1/report/getUserTotalWorkByPeriod");
    //    handler.addServlet(new ServletHolder(new UserClearServlet(userService, objectMapper)), "/api/v1/user/clear");
    //
    //    // Запускаем сервер
    //    server.start();
    //    server.join();
    //
    //}
}
