package nl.tudelft.sem10.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main class; application entry point.
 */
@SpringBootApplication(scanBasePackages = {"nl.tudelft.sem10.courseservice"})
@EnableJpaRepositories("nl.tudelft.sem10.courseservice.repositories")
public class CourseServiceApplication {

    /**
     * Application entry point.
     * This starts the Spring application.
     * @param args - String[] Command line arguments.
     */
    public static void main(String[] args) {
        // Check system requirements
        if (Float.parseFloat(System.getProperty("java.class.version")) < 52F) {
            System.err.println("This application requires Java 8 or higher to function");
            return;
        }

        SpringApplication.run(CourseServiceApplication.class, args);
    }

}
