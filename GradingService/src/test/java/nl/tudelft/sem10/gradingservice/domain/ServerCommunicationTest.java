package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerCommunicationTest {

    private transient ServerCommunication serverCommunication;

    private transient RequestHelper requestHelper;
    private static final String SENT = "SENT";

    @BeforeEach
    void setUp() {
        serverCommunication = new ServerCommunication();
        requestHelper = mock(RequestHelper.class);
        serverCommunication.setRequestHelper(requestHelper);
    }


    @Test
    void getCourseWeights() {
        when(requestHelper.sendRequest(any(HttpRequest.class), any(HttpClient.class)))
            .thenReturn(SENT);
        when(requestHelper.getRequest(any(String.class), any(String.class))).thenCallRealMethod();

        String str = serverCommunication.getCourseWeights("CSE1", "Test", "bearer myToken");

        assertEquals(SENT, str);
    }

    @Test
    void validate() {
        when(requestHelper.sendRequest(any(HttpRequest.class), any(HttpClient.class)))
            .thenReturn(SENT);
        when(requestHelper.validateToken(any(String.class))).thenCallRealMethod();

        String str = serverCommunication.validate("token");

        assertEquals(SENT, str);
    }

    @Test
    void validateUser() {
        //TODO: Finish once the authentication method is added
    }
}