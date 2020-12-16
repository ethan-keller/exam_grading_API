package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserGradeServiceTest {
    private static final transient String netId = "1234567";
    private static final transient String CSE_1 = "CSE1";
    private static final transient String CSE_2 = "CSE2";
    private static final transient String FINAL = "Final";
    private static final transient String MIDTERM = "Midterm";
    private static final transient String NON_EXISTENT = "42";
    private static final transient Grade GRADE_1 = new Grade(1, 10.0F, netId, CSE_1, MIDTERM);
    private static final transient Grade GRADE_2 = new Grade(2, 5.8F, netId, CSE_2, MIDTERM);
    private static final transient Grade GRADE_3 = new Grade(3, 5.75F, netId, CSE_1, FINAL);
    private static final transient List<Grade> GRADES_FOR_COURSE_1 =
        Arrays.asList(GRADE_1, GRADE_3);
    private static final transient Grade GRADE_4 = new Grade(4, 8.5F, netId, CSE_2, MIDTERM);
    private static final transient List<Grade> GRADES_FOR_COURSE_2 =
        Arrays.asList(GRADE_2, GRADE_4);
    private static final transient List<String> STUDENT_COURSES = Arrays.asList(CSE_1, CSE_2);

    @InjectMocks
    private transient UserGradeService userGradeService;
    @Mock
    private transient GradeRepository gradeRepository;
    private transient List<Grade> grades;

    @Mock
    private transient ServerCommunication serverCommunication;

    @Mock
    private transient StudentLogic studentLogic;

    @BeforeEach
    void setUp() {
        this.grades = new ArrayList<>();
        grades.add(GRADE_1);
        grades.add(GRADE_2);
        grades.add(GRADE_3);
        grades.add(GRADE_4);

    }

    @Test
    void getMean() {
        when(gradeRepository.getGradesByNetId(anyString()))
            .thenReturn(Collections.emptyList());
        when(gradeRepository.getGradesByNetId(netId)).thenReturn(grades);

        float result = userGradeService.getMean(netId);
        float expected = (10.0F + 5.8F + 5.75F + 8.5F) / 4;
        assertEquals(expected, result, 0.001f);
    }

    @Test
    void getMeanNonExistent() {
        when(gradeRepository.getGradesByNetId(anyString()))
            .thenReturn(Collections.emptyList());

        ResponseStatusException exception =
            assertThrows(ResponseStatusException.class, () -> userGradeService.getMean(
                NON_EXISTENT));
        assertTrue(exception.getStatus().is4xxClientError());
    }


    @Test
    void getGrade() throws JSONException {
        when(gradeRepository.getGradesByNetIdAndCourse(any(String.class), any(String.class)))
                .thenReturn(grades);
        when(studentLogic.getGrade(any(List.class), any(String.class), any(String.class)))
                .thenReturn(10.0);

        ResponseEntity<Double> result = userGradeService.getGrade("Test",
                "CSE1", "bearer token");

        assertEquals(10.0, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getGradeNonExistent() throws JSONException {
        when(gradeRepository.getGradesByNetIdAndCourse(anyString(), anyString()))
            .thenReturn(Collections.emptyList());
        ResponseEntity<Double> response = userGradeService.getGrade(NON_EXISTENT, CSE_1, "bearer token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // TODO: Find way to mock server communication
    @Disabled
    @Test
    void passedCourses() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(STUDENT_COURSES);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_2)).thenReturn(
            GRADES_FOR_COURSE_2);

        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId, "bearer Token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(STUDENT_COURSES, response.getBody());
    }

    @Test
    void passedCoursesNonExistent() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(anyString())).thenReturn(Collections.emptyList());
        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId, "bearer Token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void allGrades() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(any(String.class))).thenReturn(STUDENT_COURSES);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_2)).thenReturn(
            GRADES_FOR_COURSE_2);
        when(studentLogic.getGrade(any(List.class), any(String.class), any(String.class)))
                .thenReturn(10.0);

        ResponseEntity<List<String>> response = userGradeService.allGrades(netId, "bearer Token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().size());
    }

    @Test
    void allGradesNonExistent() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(anyString())).thenReturn(null);
        ResponseEntity<List<String>> response = userGradeService.allGrades(NON_EXISTENT, "bearer Token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Disabled because it needs server communication
    @Disabled
    @Test
    void passingRate() {
    }

    // Disabled because it needs server communication
    @Disabled
    @Test
    void meanAndVariance() {
    }

}