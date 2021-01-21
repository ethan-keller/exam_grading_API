package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
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
    private static final transient List<Grade> GRADES_FOR_COURSE_1 =
        Arrays.asList(GRADE_1, GRADE_3);
    private static final transient Grade GRADE_4 = new Grade(4, 8.5F, netId, CSE_2, MIDTERM);
    private static final transient List<Grade> GRADES_FOR_COURSE_2 =
        Arrays.asList(GRADE_2, GRADE_4);
    private static final transient List<String> STUDENT_COURSES = Arrays.asList(CSE_1, CSE_2);
    private static final String token = "bearer token";

    @InjectMocks
    private transient UserGradeService userGradeService;
    @Mock
    private transient GradeRepository gradeRepository;
    private transient List<Grade> grades;

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
        when(studentLogic.getGrade(anyList(), any(String.class), any(String.class)))
            .thenReturn(10.0);

        ResponseEntity<Double> result = userGradeService.getGrade("Test",
            CSE_1, token);

        assertEquals(10.0, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getGradeNonExistent() throws JSONException {
        when(gradeRepository.getGradesByNetIdAndCourse(anyString(), anyString()))
            .thenReturn(Collections.emptyList());
        ResponseEntity<Double> response = userGradeService.getGrade(NON_EXISTENT, CSE_1, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void passedCourses() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(STUDENT_COURSES);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_2)).thenReturn(
            GRADES_FOR_COURSE_2);
        when(studentLogic.getGrade(anyList(), anyString(), anyString()))
            .thenReturn(5.8D);

        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(STUDENT_COURSES, response.getBody());
    }

    @Test
    void passedCoursesNonExistent() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(anyString())).thenReturn(Collections.emptyList());
        ResponseEntity<List<String>> response = userGradeService.passedCourses(netId, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void allGrades() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(any(String.class))).thenReturn(STUDENT_COURSES);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_2)).thenReturn(
            GRADES_FOR_COURSE_2);
        when(studentLogic.getGrade(anyList(), any(String.class), any(String.class)))
            .thenReturn(10.0);

        ResponseEntity<List<String>> response = userGradeService.allGrades(netId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().size());
    }

    @Test
    void allGradesNonExistent() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(anyString())).thenReturn(null);
        ResponseEntity<List<String>> response = userGradeService.allGrades(NON_EXISTENT, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Disabled because it needs server communication
    @Test
    void passingRate() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class)))
            .thenReturn(Collections.singletonList(netId));
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(studentLogic.getGrade(anyList(), anyString(), anyString()))
            .thenReturn((10.0D + 5.75D) / 2.0D);

        ResponseEntity<Double> response = userGradeService.passingRate(CSE_1, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1.0D, response.getBody());
    }

    @Test
    void passingRateNoStudents() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class))).thenReturn(null);

        ResponseEntity<Double> response = userGradeService.passingRate(CSE_1, token);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void passingRateImpossible() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class)))
            .thenReturn(Collections.singletonList(netId));
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            Collections.emptyList());

        ResponseEntity<Double> response = userGradeService.passingRate(CSE_1, token);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void meanAndVariance() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class)))
            .thenReturn(Collections.singletonList(netId));
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            GRADES_FOR_COURSE_1);
        when(studentLogic.getGrade(anyList(), anyString(), anyString()))
            .thenReturn((10.0D + 5.75D) / 2.0D);

        ResponseEntity<String> response = userGradeService.meanAndVariance(CSE_1, token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void meanAndVarianceNoStudents() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class)))
            .thenReturn(null);

        ResponseEntity<String> response = userGradeService.meanAndVariance(CSE_1, token);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void meanAndVarianceImpossible() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse(any(String.class)))
            .thenReturn(Collections.singletonList(netId));
        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1)).thenReturn(
            null);

        ResponseEntity<String> response = userGradeService.meanAndVariance(CSE_1, token);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void insertGrade() {
        String json = "{\n"
            + "  \"course_code\": \"CSE1\",\n"
            + "  \"grade_type\": \"A\",\n"
            + "  \"netid\": \"1234567\",\n"
            + "  \"mark\": \"10.0\"\n"
            + "}";

        try {
            userGradeService.insertGrade(json);
        } catch (JSONException e) {
            fail();
        }
        verify(gradeRepository, atMostOnce()).insertGrade(10.0f, netId, CSE_1, "A");
    }

    @Test
    void insertGradeException() {
        String json = "not valid blah blah blah"; //NOPMD

        assertThrows(JSONException.class, () -> userGradeService.insertGrade(json));
        verify(gradeRepository, never()).insertGrade(10.0f, netId, CSE_1, "A");
    }

    @Test
    void getAllGradesNoInfo() {
        when(gradeRepository.findAll()).thenReturn(grades);

        ResponseEntity<List<Grade>> response = userGradeService.getAllGrades(null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
        verify(gradeRepository, times(1)).findAll();
    }

    @Test
    void getAllGradesForNetId() {
        when(gradeRepository.getGradesByNetId(netId)).thenReturn(grades);

        ResponseEntity<List<Grade>> response = userGradeService.getAllGrades(netId, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(grades, response.getBody());
        verify(gradeRepository, times(1)).getGradesByNetId(netId);
    }

    @Test
    void getAllGradesForCourse() {
        when(gradeRepository.getGradesByCourse(CSE_1)).thenReturn(GRADES_FOR_COURSE_1);

        ResponseEntity<List<Grade>> response = userGradeService.getAllGrades(null, CSE_1, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Grade> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains(GRADE_1));
        assertTrue(responseBody.contains(GRADE_3));
    }

    @Test
    void getAllGradesForCourseAndType() {
        List<Grade> resultList = new ArrayList<>();
        resultList.add(GRADE_1);
        resultList.add(GRADE_4);


        when(gradeRepository.getGradesByCourseAndType(CSE_2, MIDTERM))
            .thenReturn(resultList);

        ResponseEntity<List<Grade>> response = userGradeService
            .getAllGrades(null, CSE_2, MIDTERM);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Grade> responseBody = response.getBody();
        assertNotNull(responseBody);
        List<Grade> expected = new ArrayList<>(resultList);
        assertTrue(responseBody.containsAll(expected));
    }

    @Test
    void getAllGradesByNetIdAndCourse() {
        List<Grade> resultList = new ArrayList<>(GRADES_FOR_COURSE_1);

        when(gradeRepository.getGradesByNetIdAndCourse(netId, CSE_1))
            .thenReturn(resultList);

        ResponseEntity<List<Grade>> response = userGradeService
            .getAllGrades(netId, CSE_1, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Grade> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsAll(GRADES_FOR_COURSE_1));
    }

    @Test
    void getAllGradesAllDetails() {
        List<Grade> resultList = new ArrayList<>();
        resultList.add(GRADE_1);

        when(gradeRepository.getGradesByCourseAndTypeAndNetid(CSE_1, MIDTERM, netId))
            .thenReturn(resultList);


        final ResponseEntity<List<Grade>> response =
            userGradeService.getAllGrades(netId, CSE_1, MIDTERM);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        final List<Grade> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.contains(GRADE_1));
    }

    @Test
    void getAllGradesNoGrades() {
        when(gradeRepository.findAll()).thenReturn(Collections.emptyList());

        final ResponseEntity<List<Grade>> response =
            userGradeService.getAllGrades(null, null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        final List<Grade> responseBody = response.getBody();
        assertNull(responseBody);
    }

    @Test
    void updateGrade() {
        when(gradeRepository.findById(GRADE_4.getId()))
            .thenReturn(Optional.of(GRADE_4));
        when(gradeRepository.getGradesByCourseAndTypeAndNetid(CSE_2, MIDTERM, netId))
            .thenReturn(Collections.singletonList(GRADE_4));

        try {
            userGradeService.updateGrade(netId, CSE_2, MIDTERM, "{\"mark\" : \"10.0\"}");
        } catch (JSONException | NotFoundException e) {
            e.printStackTrace();
            fail();
        }

        verify(gradeRepository, times(1)).updateGrade(GRADE_4.getId(), 10.0f);
    }

    @Test
    void updateGradeNotFound() {
        when(gradeRepository
            .getGradesByCourseAndTypeAndNetid(anyString(), anyString(), anyString()))
            .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> userGradeService.updateGrade(NON_EXISTENT,
            "CSE0", "FAKE", "INVALID"));

        verify(gradeRepository, never()).updateGrade(any(Long.class), anyFloat());
    }

    @Test
    void deleteGrade() {
        when(gradeRepository.getGradesByCourseAndTypeAndNetid(CSE_1, MIDTERM, netId))
            .thenReturn(Collections.singletonList(GRADE_1));


        try {
            userGradeService.deleteGrade(netId, CSE_1, MIDTERM);
        } catch (NotFoundException e) {
            fail();
        }

        verify(gradeRepository, times(1)).deleteGrade(GRADE_1.getId());
    }

    @Test
    void deleteGradeNotFound() {
        when(gradeRepository.getGradesByCourseAndTypeAndNetid(CSE_1, MIDTERM, netId))
            .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> userGradeService.deleteGrade(netId, CSE_1,
            MIDTERM));

        verify(gradeRepository, never()).deleteGrade(GRADE_1.getId());
    }


}