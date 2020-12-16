package nl.tudelft.sem10.gradingservice.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


import java.util.List;
import nl.tudelft.sem10.gradingservice.domain.Grade;
import nl.tudelft.sem10.gradingservice.domain.ServerCommunication;
import nl.tudelft.sem10.gradingservice.domain.UserGradeService;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.h2.engine.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class StudentGradeControllerTest {
    private transient StudentGradeController studentGradeController;

    private transient List<Grade> grades;
    private static final String NETID = "jdoe";
    private static final String NETID2 = "kreeves";


    @Autowired
    @MockBean
    private transient GradeRepository gradeRepository;

    @Autowired
    @MockBean
    private transient UserGradeService userGradeService;

    private transient ServerCommunication serverCommunication;

    @BeforeEach
    void setUp() {
        this.gradeRepository = mock(GradeRepository.class);
        this.userGradeService = mock(UserGradeService.class);
        this.serverCommunication = mock(ServerCommunication.class);

        grades.add(new Grade(1, 10.0f, NETID, "CSE1", "A"));
        grades.add(new Grade(2, 5.8f, NETID2, "CSE1", "A"));

        this.studentGradeController.setGradeRepository(gradeRepository);
        this.studentGradeController.setServerCommunication(serverCommunication);
        this.studentGradeController.setUserService(userGradeService);
    }

    @Test
    void getMean() {
        String token = "Bearer myToken";
        when(serverCommunication.validate(token)).thenReturn("STUDENT");
        when(userGradeService.getMean(NETID)).thenReturn(10.0f);
        when(userGradeService.getMean(NETID2)).thenReturn(5.8f);
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
}