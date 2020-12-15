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
import java.util.Collections;
import java.util.List;
import nl.tudelft.sem10.gradingservice.framework.GradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UserGradeServiceTest {
    private static final transient String netId = "1234567";
    private static final transient String CSE_1 = "CSE1";
    private static final transient String CSE_2 = "CSE2";
    private static final transient String FINAL = "Final";
    private static final transient String MIDTERM = "Midterm";

    @InjectMocks
    private transient UserGradeService userGradeService;

    @Mock
    private transient GradeRepository gradeRepository;
    private transient List<Grade> grades;

    @BeforeEach
    void setUp() {
        this.grades = new ArrayList<>();

        Grade grade1 = new Grade(1, 10.0F, netId, CSE_1, MIDTERM);
        grades.add(grade1);
        Grade grade2 = new Grade(2, 5.8F, netId, CSE_2, MIDTERM);
        grades.add(grade2);
        Grade grade3 = new Grade(3, 5.75F, netId, CSE_1, FINAL);
        grades.add(grade3);
        Grade grade4 = new Grade(4, 8.5F, netId, CSE_2, MIDTERM);
        grades.add(grade4);

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
                "42"));
        assertTrue(exception.getStatus().is4xxClientError());
    }

    @Test
    void getGrade() {

    }

    @Test
    void passedCourses() {
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