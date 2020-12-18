package nl.tudelft.sem10.gradingservice.application;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
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
class GradeControllerTest {
    private static final String NETID = "jdoe";
    private static final String NETID2 = "kreeves";
    private transient GradeController gradeController;
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

        this.gradeController = new GradeController();
        this.gradeController.setGradeRepository(gradeRepository);
        this.gradeController.setServerCommunication(serverCommunication);
        this.gradeController.setUserService(userGradeService);
    }

    @Test
    void allGrades() {
        List<Grade> grades = new ArrayList<>();
        grades.add(new Grade(12, 10.0f, "Silverhand", "CSE2077", "Samurai"));

        when(serverCommunication.validate(any(String.class))).thenReturn("TEACHER");
        when(userGradeService.getAllGrades(any(String.class), any(String.class), any(String.class)))
            .thenReturn(ResponseEntity.ok(grades));

        ResponseEntity<List<Grade>> result =
            gradeController.getAllGrades("Bearer token", "Silverhand",
                "CSE2077", "Samurai");

        assertEquals(grades, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void allGradesUnauthorized() {
        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");

        ResponseEntity<List<Grade>> result =
            gradeController.getAllGrades("Bearer token", "Silverhand",
                "CSE2077", "Samurai");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void allGradesInvalid() {
        when(serverCommunication.validate(any(String.class))).thenReturn(null);

        ResponseEntity<List<Grade>> result =
            gradeController.getAllGrades("Bearer token", "Silverhand",
                "CSE2077", "Samurai");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void allGradesStudent() {
        List<Grade> grades = new ArrayList<>();
        grades.add(new Grade(12, 10.0f, "Silverhand", "CSE2077", "Samurai"));

        when(serverCommunication.validate(any(String.class))).thenReturn("STUDENT");
        when(serverCommunication.validateUser(any(String.class), any(String.class)))
                .thenReturn(true);
        when(userGradeService.getAllGrades(any(String.class), any(String.class), any(String.class)))
                .thenReturn(ResponseEntity.ok(grades));

        ResponseEntity<List<Grade>> result =
                gradeController.getAllGrades("Bearer token", "Silverhand",
                        "CSE2077", "Samurai");

        assertEquals(grades, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void allGradesForbidden() {
        when(serverCommunication.validate(any(String.class))).thenReturn("NOT A VALID TYPE");

        ResponseEntity<List<Grade>> result =
                gradeController.getAllGrades("Bearer token", "Silverhand",
                        "CSE2077", "Samurai");

        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }
}