package nl.tudelft.sem10.authenticationservice.framework;

import nl.tudelft.sem10.authenticationservice.application.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * External communication (other microservices).
 */
@Service
public class RestService {

    private static final int USER_SERVICE_PORT = 8083;
    private static final String GET_USER_ENDPOINT = "/user";
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
    public User getUserFromUserService(String netId) {
        if (netId == null) {
            return null;
        }
        String url = "http://localhost:" + USER_SERVICE_PORT + GET_USER_ENDPOINT
                + "/" + netId;
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
