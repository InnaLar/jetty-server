package ru.larina.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.larina.servlets.TaskServlet;
import ru.larina.servlets.TaskTimeClearServlet;
import ru.larina.servlets.TaskTimeStartServlet;
import ru.larina.servlets.TaskTimeStopServlet;
import ru.larina.servlets.UserClearServlet;
import ru.larina.servlets.UserEffortsServlet;
import ru.larina.servlets.UserServlet;
import ru.larina.servlets.UserTotalWorkServlet;
import ru.larina.servlets.UserWorkIntervalsServlet;

public class SimpleHttpServer {
    public static void main(String[] args) throws Exception {

        // Создаём сервер на порту 8080
        Server server = new Server(8080);

        // Настраиваем контекст сервлетов
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");

        // Добавляем сервлет для обработки запросов
        server.setHandler(handler);
        handler.addServlet(new ServletHolder(new UserServlet()), "/api/v1/user");
        handler.addServlet(new ServletHolder(new TaskServlet()), "/api/v1/task");
        handler.addServlet(new ServletHolder(new TaskTimeStartServlet()), "/api/v1/task-time/start");
        handler.addServlet(new ServletHolder(new TaskTimeStopServlet()), "/api/v1/task-time/stop");
        handler.addServlet(new ServletHolder(new TaskTimeClearServlet()), "/api/v1/task-time/clear");
        handler.addServlet(new ServletHolder(new UserEffortsServlet()), "/api/v1/report/getUserTaskEfforts");
        handler.addServlet(new ServletHolder(new UserWorkIntervalsServlet()), "/api/v1/report/getUserWorkIntervalsByPeriod");
        handler.addServlet(new ServletHolder(new UserTotalWorkServlet()), "/api/v1/report/getUserTotalWorkByPeriod");
        handler.addServlet(new ServletHolder(new UserClearServlet()), "/api/v1/user/clear");

        // Запускаем сервер
        server.start();
        server.join();
    }
    // Простой сервлет

}
