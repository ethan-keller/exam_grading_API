package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.StudentLogic;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
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

//TODO: ALL THESE TESTS NEED TO BE NEWLY MADE
@ExtendWith(MockitoExtension.class)
public class StudentGradeControllerTest {

    @InjectMocks
    private transient StudentGradeController studentGradeController;

    @Mock
    private transient GradeRepository gradeRepository;

    @Mock
    private transient StudentLogic studentLogic;

    @Mock
    private transient UserGradeService userGradeService;


    private transient String netId;
    private transient String courseCode;
    private transient List<Grade> allGradesOfUser;
    private transient List<Grade> gradesOfCourse;
    private transient List<Grade> gradesOfCourse2;
    private transient List<String> coursesOfStudent;

    /**
     * Sets up the Users used in multiple tests.
     */
    @BeforeEach
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public void setUp() {
        netId = "user1";
        courseCode = "CSE1";
        allGradesOfUser = new ArrayList<>();

        Grade grade1 = new Grade(1, 10, "user1", "CSE1", "A");
        allGradesOfUser.add(grade1);
        Grade grade2 = new Grade(2, 10, "user1", "CSE1", "B");
        allGradesOfUser.add(grade2);
        Grade grade3 = new Grade(3, 5, "user1", "CSE2", "A");
        allGradesOfUser.add(grade3);
        Grade grade4 = new Grade(4, 5, "user1", "CSE2", "B");
        allGradesOfUser.add(grade4);

        gradesOfCourse = new ArrayList<>();
        gradesOfCourse.add(grade1);
        gradesOfCourse.add(grade2);
        gradesOfCourse2 = new ArrayList<>();
        gradesOfCourse2.add(grade3);
        gradesOfCourse2.add(grade4);
        coursesOfStudent = new ArrayList<>();
        coursesOfStudent.add("CSE1");
        coursesOfStudent.add("CSE2");
    }

    //When returned list is not null
    @Test
    public void testGetMean() {

        float mean = 0.0F;
        for (Grade grade : allGradesOfUser) {
            mean += grade.getMark();
        }
        mean /= allGradesOfUser.size();
        when(userGradeService.getMean("user1")).thenReturn(mean);

        ResponseEntity<Float> responseEntity = studentGradeController.getMean("user1");

        verify(userGradeService).getMean("user1");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mean, responseEntity.getBody());
    }

    //When returned list is null
    @Test
    public void testGetMean2() {
        when(userGradeService.getMean(any(String.class))).thenThrow(new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Grades not here!"));
        assertThrows(ResponseStatusException.class, () -> studentGradeController.getMean("user1"));
    }

    @Test
    public void testGetGradeNull() throws JSONException {
        when(userGradeService.getGrade(any(), any())).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<Double> responseEntity = studentGradeController.getGrade(netId, courseCode);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testGetPassedCourses() throws JSONException {

        when(userGradeService.passedCourses(netId))
            .thenReturn(ResponseEntity.ok(Collections.singletonList("CSE1")));

        ResponseEntity<List<String>> responseEntity = studentGradeController.passedCourses(netId);

        List<String> expected = new ArrayList<>();
        expected.add("CSE1");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    public void testGetPassedCoursesNull() throws JSONException {
        when(userGradeService.passedCourses(netId)).thenReturn(ResponseEntity.notFound().build());
        ResponseEntity<List<String>> responseEntity = studentGradeController.passedCourses("user1");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testAllGrades() throws JSONException {
        List<String> expected = new ArrayList<>();
        expected.add("{\"course\":\"" + "CSE1" + "\", \"grade\":\"" + 10.0D + "\"}");
        expected.add("{\"course\":\"" + "CSE2" + "\", \"grade\":\"" + 5.0D + "\"}");

        when(userGradeService.allGrades(netId)).thenReturn(ResponseEntity.ok(expected));

        ResponseEntity<List<String>> response = studentGradeController.allGrades("user1");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
    }

    @Test
    public void testAllgradesNull() throws JSONException {
        when(userGradeService.allGrades(netId)).thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<List<String>> responseEntity = studentGradeController.allGrades(netId);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testPassingRate() throws JSONException {
        when(userGradeService.passingRate(courseCode)).thenReturn(ResponseEntity.ok(1.0));

        ResponseEntity<Double> responseEntity = studentGradeController.passingRate(courseCode);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(1.0, responseEntity.getBody());
    }

    @Test
    public void testPassingRateStudentsNull() throws JSONException {
        when(userGradeService.passingRate(courseCode))
            .thenReturn(ResponseEntity.notFound().build());
        ResponseEntity<Double> responseEntity = studentGradeController.passingRate(courseCode);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testPassingRateGradesNull() throws JSONException {
        when(userGradeService.passingRate(courseCode))
            .thenReturn(ResponseEntity.notFound().build());
        ResponseEntity<Double> responseEntity = studentGradeController.passingRate(courseCode);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}

