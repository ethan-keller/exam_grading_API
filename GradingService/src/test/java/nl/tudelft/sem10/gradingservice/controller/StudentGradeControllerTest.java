package nl.tudelft.sem10.gradingservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;


import nl.tudelft.sem10.gradingservice.application.StudentGradeController;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

//TODO: ALL THESE TESTS NEED TO BE NEWLY MADE
@ExtendWith(MockitoExtension.class)
public class StudentGradeControllerTest {

    @InjectMocks
    private transient StudentGradeController studentGradeController;

    @Mock
    private transient GradeRepository gradeRepository;


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
    @SuppressWarnings("PMD")
    public void setUp() {
        netId = "user1";
        courseCode = "CSE1";
        Grade grade1 = new Grade(1, 10, "user1", "CSE1", "A");
        Grade grade2 = new Grade(2, 10, "user1", "CSE1", "B");
        Grade grade3 = new Grade(3, 5, "user1", "CSE2", "A");
        Grade grade4 = new Grade(4, 5, "user1", "CSE2", "B");
        allGradesOfUser = new ArrayList<>();
        allGradesOfUser.add(grade1);
        allGradesOfUser.add(grade2);
        allGradesOfUser.add(grade3);
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
    //@Test
    public void testGetMean() {
        when(gradeRepository.getGradesByNetId(netId)).thenReturn(allGradesOfUser);
        ResponseEntity<Float> responseEntity = studentGradeController.getMean("user1");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals((float) 7.5, responseEntity.getBody());
    }

    //When returned list is null
    //@Test
    public void testGetMean2() {
        when(gradeRepository.getGradesByNetId(netId)).thenReturn(null);
        ResponseEntity<Float> responseEntity = studentGradeController.getMean("user1");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

   // @Test
    public void testGetGradeNull() throws JSONException {
        when(gradeRepository.getGradesByNetIdAndCourse(netId, courseCode)).thenReturn(null);
        ResponseEntity<Double> responseEntity = studentGradeController.getGrade(netId, courseCode);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    //TODO:Needs to be updated since test method was removed
    //@Test
    @SuppressWarnings("PMD")
    public void testGetPassedCourses() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(coursesOfStudent);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE1")).thenReturn(gradesOfCourse);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE2")).thenReturn(gradesOfCourse2);
        //ResponseEntity<List<String>> responseEntity = studentGradeController.passedCoursesTestMethod("user1");
        List<String> res = new ArrayList<>();
        res.add("CSE1");
        //assertEquals(200, responseEntity.getStatusCodeValue());
        //assertEquals(res, responseEntity.getBody());
    }

    //TODO:Needs to be updated since test method was removed
    //@Test
    @SuppressWarnings("PMD")
    public void testGetPassedCoursesNull() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(null);
        //ResponseEntity<List<String>> responseEntity = studentGradeController.passedCoursesTestMethod("user1");
        //assertEquals(404, responseEntity.getStatusCodeValue());
    }

    //TODO:Needs to be updated since test method was removed
    //@Test
    @SuppressWarnings("PMD")
    public void testAllGrades() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(coursesOfStudent);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE1")).thenReturn(gradesOfCourse);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE2")).thenReturn(gradesOfCourse2);
        //ResponseEntity<List<String>> responseEntity = studentGradeController.allGradesTestMethod("user1");
        List<String> res = new ArrayList<>();
        res.add("{\"course\":\"" + "CSE1" + "\", \"grade\":\"" + 10 + "\"}");
        res.add("{\"course\":\"" + "CSE2" + "\", \"grade\":\"" + 10 + "\"}");
        //assertEquals(200, responseEntity.getStatusCodeValue());
        //assertEquals(res, responseEntity.getBody());
    }

   // @Test
    public void testAllgradesNull() throws JSONException {
        when(gradeRepository.getCoursesOfStudent(netId)).thenReturn(null);
        ResponseEntity<List<String>> responseEntity = studentGradeController.allGrades("user1");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    //@Test
    // public void testPassingRate() throws JSONException {
    //    List<String> students = new ArrayList<>();
    //    students.add("user1");
    //    when(gradeRepository.getStudentsTakingCourse("CSE1")).thenReturn(students);
    //    when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE1")).thenReturn(gradesOfCourse);
    //    ResponseEntity<Double> responseEntity = studentGradeController.passingRateTestMethod("CSE1");
    //    assertEquals(200, responseEntity.getStatusCodeValue());
    //    assertEquals(1.0, responseEntity.getBody());
    //}

    //@Test
    public void testPassingRateStudentsNull() throws JSONException {
        when(gradeRepository.getStudentsTakingCourse("CSE1")).thenReturn(null);
        ResponseEntity<Double> responseEntity = studentGradeController.passingRate("CSE1");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    //@Test
    public void testPassingRateGradesNull() throws JSONException {
        List<String> students = new ArrayList<>();
        students.add("user1");
        when(gradeRepository.getStudentsTakingCourse("CSE1")).thenReturn(students);
        when(gradeRepository.getGradesByNetIdAndCourse(netId, "CSE1")).thenReturn(null);
        ResponseEntity<Double> responseEntity = studentGradeController.passingRate("CSE1");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}

