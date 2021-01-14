package nl.tudelft.sem10.gradingservice.application;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class StudentGradeControllerTest {
    private static final String NETID = "jdoe";
    private static final String NETID2 = "kreeves";
    private transient StudentGradeController studentGradeController;
    private transient List<Grade> grades;
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
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(true);

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
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(false);

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
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(true);

        when(userGradeService.getGrade(any(String.class), any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(10.0D));

        ResponseEntity<Double> result = studentGradeController.getGrade("Bearer token", NETID,
            "CSE1");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(10.0D, result.getBody());
    }

    @Test
    void getGradeUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(false);

        ResponseEntity<Double> result = studentGradeController.getGrade("Bearer token", NETID,
            "CSE1");
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void getGradeInvalid() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<Double> result = studentGradeController.getGrade("Bearer token", NETID,
            "CSE1");
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void passedCourses() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(true);

        List<String> passed = new ArrayList<>();
        passed.add("CSE1");

        when(userGradeService.passedCourses(any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(passed));

        ResponseEntity<List<String>> result =
            studentGradeController.passedCourses("Bearer token", NETID);

        assertEquals(passed, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void passedCoursesUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(false);

        ResponseEntity<List<String>> result =
            studentGradeController.passedCourses("Bearer token", NETID);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void allGrades() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(true);

        List<String> allGrades = new ArrayList<>();
        allGrades.add("{test}");

        when(userGradeService.allGrades(any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(allGrades));

        ResponseEntity<List<String>> result =
            studentGradeController.allGrades("Bearer token", NETID);

        assertEquals(allGrades, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void allGradesUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(false);

        ResponseEntity<List<String>> result =
            studentGradeController.allGrades("Bearer token", NETID);

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void allGradesInvalid() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<List<String>> result =
            studentGradeController.allGrades("Bearer token", NETID);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void passingRate() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
            .thenReturn(true);
        when(userGradeService.passingRate(any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(1.0));

        ResponseEntity<Double> result = studentGradeController.passingRate("bearer token", "CSE1");

        assertEquals(1.0, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void passingRateUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("NOTAVALIDUSER");

        ResponseEntity<Double> result = studentGradeController.passingRate("Bearer token", "CSE1");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

}