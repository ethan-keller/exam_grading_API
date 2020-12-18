package nl.tudelft.sem10.gradingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"nl.tudelft.sem10.gradingservice"})
//@EnableJpaRepositories("nl.tudelft.sem10.gradingservice.framework.repositories")
public class GradingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradingServiceApplication.class, args);
    }

}
