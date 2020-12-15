package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TeacherGradeControllerTest {
    private static final String USER_1 = "user1";
    private static final String CSE_1 = "CSE1";


    @InjectMocks
    private transient TeacherGradeController teacherGradeController;

    @Mock
    private transient UserGradeService userGradeService;


    private transient String netId;
    private transient String courseCode;
    private transient List<Grade> allGradesOfUser;
    private transient List<Grade> gradesOfCourse;
    private transient List<Grade> gradesOfCourse2;
    private transient List<String> coursesOfTeacher;
    private transient Grade grade1;
    private transient Grade grade2;
    private transient Grade grade3;
    private transient Grade grade4;

    /**
     * Sets up grades used in the test.
     */
    @Test
    public void setUp() {
        netId = USER_1;
        courseCode = CSE_1;
        grade1 = new Grade(1, 10, USER_1, CSE_1, "A");
        grade2 = new Grade(2, 10, USER_1, CSE_1, "B");
        grade3 = new Grade(3, 5, USER_1, "CSE2", "A");
        grade4 = new Grade(4, 5, USER_1, "CSE2", "B");
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
        coursesOfTeacher = new ArrayList<>();
        coursesOfTeacher.add(CSE_1);
        coursesOfTeacher.add("CSE2");
    }

    @Test
    public void testGetPassingRate() throws JSONException {
        when(userGradeService.passingRate(courseCode)).thenReturn(ResponseEntity.ok(1.0d));

        try {
            ResponseEntity<Double> response = teacherGradeController.passingRate(courseCode);
            verify(userGradeService).passingRate(courseCode);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1.0D, response.getBody(), 0.0001d);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


}
