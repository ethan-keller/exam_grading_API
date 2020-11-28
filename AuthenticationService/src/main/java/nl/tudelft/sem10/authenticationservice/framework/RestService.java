package nl.tudelft.sem10.authenticationservice.framework;

import nl.tudelft.sem10.authenticationservice.application.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// https://attacomsian.com/blog/http-requests-resttemplate-spring-boot

/**
 * External communication (other microservices).
 */
@Service
public class RestService {
    private final transient RestTemplate restTemplate;

    /**
     * Constructor.
     */
    public RestService() {
        this.restTemplate = new RestTemplateBuilder().build();
    }

    /**
     * Get a user from the USER microservice.
     *
     * @return the wanted user
     */
    public User getUserFromUserService() {
        String url = "http://localhost:" + "${user.service.server.port}";
        ResponseEntity<User> response = this.restTemplate.getForEntity(url, User.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
