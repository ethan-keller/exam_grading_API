package nl.tudelft.sem10.gradingservice.repositories;

import java.util.List;
import javax.transaction.Transactional;
import nl.tudelft.sem10.gradingservice.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface GradeRepository extends JpaRepository<Grade, Long> {

    //update building information
    @Modifying
    @Transactional
    @Query(value = "UPDATE grade SET mark = ?2 "
            + "WHERE id = ?1", nativeQuery = true)
    void updateGrade(long id, float mark);

    //insert new building in database
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO grade (mark, netid, course_code, grade_type) "
            + "VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertGrade(float mark, String netid, String courseCode, String gradeType);

    //deletes entry from building table
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM grade WHERE id = ?1", nativeQuery = true)
    void deleteGrade(long id);

    //Finds grades of a user
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM grade WHERE netid = ?1", nativeQuery = true)
    List<Grade> getGradesByNetId(String netId);

    //Finds grades of a user by course
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM grade WHERE netid = ?1 AND course_code = ?2", nativeQuery = true)
    List<Grade> getGradesByNetIdAndCourse(String netId, String course);

    //Finds the courses a user takes
    @Modifying
    @Transactional
    @Query(value = "SELECT DISTINCT course_code FROM semgrade.grade "
            + "WHERE netid = ?1", nativeQuery = true)
    List<String> getCoursesOfStudent(String netId);

    //Finds the students that take a course
    @Modifying
    @Transactional
    @Query(value = "SELECT DISTINCT netid FROM semgrade.grade "
            + "WHERE course_code = ?1", nativeQuery = true)
    List<String> getStudentsTakingCourse(String course);
}
