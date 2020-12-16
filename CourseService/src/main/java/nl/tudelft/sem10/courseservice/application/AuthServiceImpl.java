package nl.tudelft.sem10.courseservice.application;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * An {@link AuthService} implementation.
 * Makes a validate request to the authentication service.
 */
@Component
public class AuthServiceImpl implements AuthService {
    // This needs to match the endpoint in the authentication microservice
    private static final String VALIDATE = "http://localhost:8080/validate/{token}";

    private final transient RestTemplate template = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public UserType getUser(String token) {
        // Validate request
        try {
            ResponseEntity<String> response = template.getForEntity(VALIDATE, String.class, token);

            // Unexpected status code (includes 404 - user not found)
            if (response.getStatusCode() != HttpStatus.OK) {
                return UserType.UNKNOWN;
            }

            // Parse the user type
            return UserType.of(response.getBody());
        } catch (RestClientException exception) {
            return UserType.UNKNOWN;
        }
    }
}