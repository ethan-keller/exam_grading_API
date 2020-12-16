package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.h2.engine.User;
import org.json.HTTP;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class StudentGradeControllerTest {
    private transient StudentGradeController studentGradeController;

    private transient List<Grade> grades;
    private static final String NETID = "jdoe";
    private static final String NETID2 = "kreeves";


    @Autowired
    @MockBean
    private transient GradeRepository gradeRepository;

    @Autowired
    @MockBean
    private transient UserGradeService userGradeService;

    private transient ServerCommunication serverCommunication;

    @BeforeEach
    void setUp() {
        this.grades = new ArrayList<>();
        this.gradeRepository = mock(GradeRepository.class);
        this.userGradeService = mock(UserGradeService.class);
        this.serverCommunication = mock(ServerCommunication.class);

        grades.add(new Grade(1, 10.0f, NETID, "CSE1", "A"));
        grades.add(new Grade(2, 5.8f, NETID2, "CSE1", "A"));

        this.studentGradeController = new StudentGradeController();
        this.studentGradeController.setGradeRepository(gradeRepository);
        this.studentGradeController.setServerCommunication(serverCommunication);
        this.studentGradeController.setUserService(userGradeService);
    }

    @Test
    void getMean() {
        String token = "Bearer myToken";

        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class))).thenReturn(true);

        when(userGradeService.getMean(NETID)).thenReturn(10.0f);
        when(userGradeService.getMean(NETID2)).thenReturn(5.8f);

        ResponseEntity<Float> response1 = studentGradeController.getMean(token, NETID);
        ResponseEntity<Float> response2 = studentGradeController.getMean(token, NETID2);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(10.0f, response1.getBody());
        assertEquals(5.8f, response2.getBody());
    }

    @Test
    void getMeanUnauthorized() {
        String token = "Bearer myToken";

        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class))).thenReturn(false);

        when(userGradeService.getMean(NETID)).thenReturn(10.0f);
        when(userGradeService.getMean(NETID2)).thenReturn(5.8f);

        ResponseEntity<Float> response1 = studentGradeController.getMean(token, NETID);
        ResponseEntity<Float> response2 = studentGradeController.getMean(token, NETID2);

        assertEquals(HttpStatus.FORBIDDEN, response1.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, response2.getStatusCode());
    }

    @Test
    void getMeanInvalidHeader() {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<Float> response = studentGradeController.getMean("Bearer TokenMcTokenface",
            NETID2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getGrade() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class))).thenReturn(true);

        when(userGradeService.getGrade(any(String.class), any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(10.0D));

        ResponseEntity<Double> result = studentGradeController.getGrade("Bearer token", NETID,
            "CSE1");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(10.0D, result.getBody());
    }

    @Test
    void passedCourses() {
    }

    @Test
    void allGrades() {
    }

    @Test
    void passingRate() {
    }
}