package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
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

    /**
     * Get a field using reflection.
     * The field will be accessible immediately.
     *
     * @param clazz     - Class&lt;?&gt; Class to get a field from.
     * @param fieldName - String Field name.
     * @return the field.
     * @throws ReflectiveOperationException If something goes wrong.
     */
    public static Field getField(Class<?> clazz,
                                 String fieldName) throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    /**
     * Set a field value using reflection.
     *
     * @param instance  - Object Instance to set a field for.
     * @param fieldName - String Field name.
     * @param value     - Object Value to set.
     * @throws ReflectiveOperationException If something goes wrong.
     */
    public static void setField(Object instance,
                                String fieldName,
                                Object value) throws ReflectiveOperationException {
        Field field = getField(instance.getClass(), fieldName);
        field.set(instance, value);
    }

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        this.grades = new ArrayList<>();
        this.gradeRepository = mock(GradeRepository.class);
        this.userGradeService = mock(UserGradeService.class);
        this.serverCommunication = mock(ServerCommunication.class);

        this.gradeController = new GradeController();
        setField(gradeController, "gradeRepository", gradeRepository);
        setField(gradeController, "serverCommunication", serverCommunication);
        setField(gradeController, "userService", userGradeService);
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