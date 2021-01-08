package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentLogicTest {

    private transient StudentLogic studentLogic;
    private transient ServerCommunication serverCommunication;
    private transient List<Grade> grades;
    private transient Grade test;
    private transient Grade test1;

    @BeforeEach
    void setUp() {
        studentLogic = new StudentLogic();
        serverCommunication = mock(ServerCommunication.class);
        studentLogic.setServerCommunication(serverCommunication);
        test = new Grade(12, 10.0f, "Silverhand", "CSE2077", "Samurai");
        test1 = new Grade(11, 0.0f, "Silverhand", "CSE2077", "Samurai");
        grades = new ArrayList<>();
        grades.add(test);
        grades.add(test1);
    }

    @Test
    void getGrade() throws JSONException {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class), any(String.class)))
            .thenReturn("{\n"
                + "  \"weight\": \"0.5\"\n"
                + "}");
        double result = studentLogic.getGrade(grades, "CSE1", "Bearer token");

        assertEquals(5.0, result);
    }

    @Test
    void getGradeNull() throws JSONException {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class), any(String.class)))
            .thenReturn(null);
        double result = studentLogic.getGrade(grades, "CSE1", "Bearer token");

        assertEquals(0.0, result);
    }

    @Test
    void getGradeException() {
        when(serverCommunication
            .getCourseWeights(any(String.class), any(String.class), any(String.class)))
            .thenReturn("nothing to see here");

        assertThrows(JSONException.class,
            () -> studentLogic.getGrade(grades, "CSE1", "Bearer token"));
    }
}