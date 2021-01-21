package nl.tudelft.sem10.gradingservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentLogicTest {

    private transient StudentLogic studentLogic;
    private transient ServerCommunication serverCommunication;
    private transient List<Grade> grades;
    private transient Grade test;
    private transient Grade test1;
    private transient String bearer;
    private transient String cse1;

    @BeforeEach
    void setUp() {
        studentLogic = new StudentLogic();
        serverCommunication = mock(ServerCommunication.class);
        studentLogic.setServerCommunication(serverCommunication);
        test = new Grade(12, 10.0f, "Silverhand", "CSE2077", "category1");
        test1 = new Grade(11, 0.0f, "Silverhand", "CSE2077", "category2");
        grades = new ArrayList<>();
        grades.add(test);
        grades.add(test1);
        bearer = "Bearer token";
        cse1 = "CSE1";
    }

    @Test
    void getGrade() throws JSONException {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class)))
            .thenReturn("{\n"
                + "\"category1\":\"0.5\",\n"
                + "\"category2\":\"0.5\"\n"
                + "}");
        double result = studentLogic.getGrade(grades, cse1, bearer);

        assertEquals(5.0, result);
    }

    @Test
    void getGradeNull() throws JSONException {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class)))
            .thenReturn(null);
        double result = studentLogic.getGrade(grades, cse1, bearer);

        assertEquals(-1.0, result);
    }

    @Test
    void getGradeException() {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class)))
            .thenReturn("nothing to see here");

        assertThrows(JSONException.class,
            () -> studentLogic.getGrade(grades, cse1, bearer));
    }

    @Test
    void getGradeException1() throws JSONException {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class)))
            .thenReturn("{\n"
                + "\"non-existing\":\"0.5\",\n"
                + "\"non-existing1\":\"0.5\"\n"
                + "}");
        double result = studentLogic.getGrade(grades, cse1, bearer);
        assertEquals(Double.NaN, result);
    }
}