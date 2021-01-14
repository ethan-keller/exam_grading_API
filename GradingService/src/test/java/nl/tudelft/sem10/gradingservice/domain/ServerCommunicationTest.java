package nl.tudelft.sem10.gradingservice.domain;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerCommunicationTest {

    private transient ServerCommunication serverCommunication;

    private transient RequestHelper requestHelper;
    private static final String SENT = "SENT";

    @Mock
    private transient HttpRequest request;

    @Mock
    private transient HttpResponse<String> response;

    @Mock
    private transient HttpClient client;

    @BeforeEach
    void setUp() {
        this.request = mock(HttpRequest.class);
        this.response = mock(HttpResponse.class);
        this.client = mock(HttpClient.class);
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
        when(requestHelper.sendRequest(any(HttpRequest.class), any(HttpClient.class)))
                .thenReturn("TRUE");
        when(requestHelper.validateNetIdToken(any(String.class), any(String.class))).thenCallRealMethod();

        Boolean bool = serverCommunication.validateUser("token", "netId");

        assertTrue(bool);
    }

    @Test
    void sendRequestTestExpected() throws IOException, InterruptedException {
        RequestHelper reqHelper = new RequestHelper();
        when(response.body()).thenReturn("TRUE");
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        String send = reqHelper.sendRequest(request, client);

        assertTrue(Boolean.parseBoolean(send));
    }

    @Test
    void sendRequestTestEmptyBody() throws IOException, InterruptedException {
        RequestHelper reqHelper = new RequestHelper();
        when(response.body()).thenReturn("");
        when(response.statusCode()).thenReturn(404);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        String send = reqHelper.sendRequest(request, client);

        assertEquals(404, Integer.parseInt(send));
    }

    @Test
    void sendRequestTestNullBody() throws IOException, InterruptedException {
        RequestHelper reqHelper = new RequestHelper();
        when(response.body()).thenReturn(null);
        when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        String send = reqHelper.sendRequest(request, client);

        assertEquals("0", send);
    }

    @Test
    void courseWeightsTest() {
        when(requestHelper.sendRequest(any(HttpRequest.class), any(HttpClient.class)))
                .thenReturn(SENT);
        when(requestHelper.getRequest(any(String.class), any(String.class))).thenCallRealMethod();

        String str = serverCommunication.getCourseWeights("CSE1", "bearer myToken");

        assertEquals(SENT, str);
    }
}