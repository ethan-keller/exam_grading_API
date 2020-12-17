package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("PMD")
@ExtendWith(MockitoExtension.class)
public class TeacherGradeControllerTest {
    private static final String NETID = "jdoe";
    private static final String NETID2 = "kreeves";
    private transient TeacherGradeController teacherGradeController;
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

        this.teacherGradeController = new TeacherGradeController();
        this.teacherGradeController.setGradeRepository(gradeRepository);
        this.teacherGradeController.setServerCommunication(serverCommunication);
        this.teacherGradeController.setUserService(userGradeService);
    }

    @Test
    void passingRate() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");
        when(userGradeService.passingRate(any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(1.0));

        ResponseEntity<Double> result = teacherGradeController.passingRate("bearer token", "CSE1");

        assertEquals(1.0, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void passingRateUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("NOTAVALIDUSER");

        ResponseEntity<Double> result = teacherGradeController.passingRate("bearer token", "CSE1");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void passingRateInvalid() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<Double> result = teacherGradeController.passingRate("bearer token", "CSE1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void meanAndVar() throws JSONException {
        String test = "{}";
        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");
        when(userGradeService.meanAndVariance(any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(test));

        ResponseEntity<String> result =
            teacherGradeController.meanAndVariance("Bearer token", "CSE1");

        assertEquals(test, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void meanAndVarUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");

        ResponseEntity<String> result =
            teacherGradeController.meanAndVariance("Bearer token", "CSE1");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void meanAndVarInvalid() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<String> result =
            teacherGradeController.meanAndVariance("Bearer token", "CSE1");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void updateGrade() throws NotFoundException, JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");

        ResponseEntity<String> result =
            teacherGradeController.updateGrade("Bearer token", "Silverhand",
                "CSE1", "A", "imagine a json here");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateGradeUnauthorized() throws NotFoundException, JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");

        ResponseEntity<String> result =
            teacherGradeController.updateGrade("Bearer token", "Silverhand",
                "CSE1", "A", "imagine a json here");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void updateGradeInvalid() throws NotFoundException, JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<String> result =
            teacherGradeController.updateGrade("Bearer token", "Silverhand",
                "CSE1", "A", "imagine a json here");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void deleteGrade() throws NotFoundException {
        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");

        ResponseEntity<String> result =
            teacherGradeController.deleteGrade("Bearer token", "Silverhand",
                "CSE1", "A");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void deleteGradeUnauthorized() throws NotFoundException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");

        ResponseEntity<String> result =
            teacherGradeController.deleteGrade("Bearer token", "Silverhand",
                "CSE1", "A");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void deleteGradeInvalid() throws NotFoundException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<String> result =
            teacherGradeController.deleteGrade("Bearer token", "Silverhand",
                "CSE1", "A");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void insertGrade() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");

        ResponseEntity<String> result = teacherGradeController.insertGrade("bearer token",
            "JSON");

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void insertGradeUnauthorized() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");

        ResponseEntity<String> result = teacherGradeController.insertGrade("bearer token",
            "JSON");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void insertGradeInvalid() throws JSONException {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<String> result = teacherGradeController.insertGrade("bearer token",
            "JSON");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
