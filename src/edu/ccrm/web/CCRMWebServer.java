package edu.ccrm.web;

import com.sun.net.httpserver.HttpServer;
import edu.ccrm.io.DatabaseInitializer;
import edu.ccrm.web.handler.ApiHandlers;
import edu.ccrm.web.handler.StaticFileHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CCRMWebServer {

    public static void main(String[] args) {
        System.out.println("Starting CCRM Web Server...");
        
        // Initialize the database just like the CLI did
        DatabaseInitializer.initialize();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            // Register handlers
            server.createContext("/", new StaticFileHandler());
            
            // Register API contexts
            server.createContext("/api/students", new ApiHandlers.StudentHandler());
            server.createContext("/api/courses", new ApiHandlers.CourseHandler());
            server.createContext("/api/instructors", new ApiHandlers.InstructorHandler());
            server.createContext("/api/enrollments", new ApiHandlers.EnrollmentHandler());

            server.setExecutor(null); // creates a default executor
            server.start();
            
            System.out.println("CCRM Web Server is running!");
            System.out.println("Access the application at: http://localhost:8080");
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
