package nl.tudelft.sem10.courseservice.application;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * An {@link AuthService} implementation.
 * Makes a validate request to the authentication service.
 * TODO: Tests
 */
@Component("AuthServiceImpl")
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final transient RestTemplate template = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(10))
            .build();

    @Autowired
    @Qualifier("validateUrl")
    private String url; //NOPMD

    @Override
    public UserType getUser(String token) {
        // Validate request
        try {
            ResponseEntity<String> response = template.getForEntity(url, String.class, token);

            // Unexpected status code (includes 404 - user not found)
            if (response.getStatusCode() != HttpStatus.OK) {
                LOGGER.warn("Validate request returned "
                        + response.getStatusCode()
                        + ", expected "
                        + HttpStatus.OK);
                return UserType.UNKNOWN;
            }

            // Parse the user type
            return UserType.of(response.getBody());
        } catch (RestClientException exception) {
            LOGGER.warn("Validate request failed: " + exception.getMostSpecificCause());
            return UserType.UNKNOWN;
        }
    }
}