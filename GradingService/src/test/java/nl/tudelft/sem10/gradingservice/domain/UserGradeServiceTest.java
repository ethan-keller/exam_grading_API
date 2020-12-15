package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
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
    private static final transient Grade GRADE_4 = new Grade(4, 8.5F, netId, CSE_2, MIDTERM);

    @InjectMocks
    private transient UserGradeService userGradeService;

    @Mock
    private transient GradeRepository gradeRepository;
    private transient List<Grade> grades;

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

    // TODO: Find way to mock server communication
    @Disabled
    @Test
    void getGrade() {

    }

    @Disabled
    @Test
    void getGradeNonExistent() {

    }

    @Test
    void passedCourses() throws JSONException {
        List<String> coursesOfStudent = Arrays.asList(CSE_1, CSE_2);
        List<Grade> gradesForCourse1 = Arrays.asList(GRADE_1, GRADE_3);
        List<Grade> gradesForCourse2 = Arrays.asList(GRADE_2, GRADE_4);
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(coursesOfStudent);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(gradesForCourse1);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_2)).thenReturn(gradesForCourse2);

        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId);

        assertEquals(coursesOfStudent, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void passedCoursesNonExistent() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(anyString())).thenReturn(Collections.emptyList());
        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void allGrades() {
    }

    @Test
    void passingRate() {
    }

    @Test
    void meanAndVariance() {
    }

    @Test
    void updateGrade() {
    }

    @Test
    void getAllGrades() {
    }

    @Test
    void deleteGrade() {
    }

    @Test
    void insertGrade() {
    }
}