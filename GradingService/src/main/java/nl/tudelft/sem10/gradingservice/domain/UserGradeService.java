package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem10.gradingservice.repositories.GradeRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserGradeService {

    @Autowired
    private GradeRepository gradeRepository; // NOPMD
    static final double passingGrade = 5.75;

    /**
     * TODO: some javadoc.
     * @param netId fhweu9ch
     * @return fweijfniw
     * @throws ResponseStatusException
     */
    public Float getMean(String netId) throws ResponseStatusException {
        List<Grade> list = gradeRepository.getGradesByNetId(netId);
        if (list.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Grades not here!");
        }
        return StudentLogic.getMean(list);
    }

    /**
     * Method to return grade of a specific student for a specific course.
     *
     * @param netId      netId of student
     * @param courseCode course code of the grade required
     * @return a double representing grade of that course
     * @throws JSONException exception if json is wrong
     */
    public ResponseEntity<Double> getGrade(String netId,
                                           String courseCode) throws JSONException {
        List<Grade> list =
                gradeRepository.getGradesByNetIdAndCourse(netId, courseCode);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double grade = StudentLogic.getGrade(list, courseCode);
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    /**
     * Method to return the course code of all courses a student has passed.
     *
     * @param netId netId of the student
     * @return A list of strings that are the course codes
     * @throws JSONException exception if json is wrong
     */
    public ResponseEntity<List<String>> passedCourses(String netId)
            throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> passed = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            double grade = StudentLogic.getGrade(l, course);
            if (grade >= passingGrade) {
                passed.add(course);
            }
        }
        return new ResponseEntity<>(passed, HttpStatus.OK);
    }


    /**
     * Method to get all the final grades a student has achieved for every course.
     *
     * @param netId netId of the student
     * @return List of json objects
     * @throws JSONException exception if json is wrong
     */
    public ResponseEntity<List<String>> allGrades(String netId) throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> gr = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            gr.add("{\"course\":\"" + course + "\", "
                    + "\"grade\":\"" + StudentLogic.getGrade(l, course) + "\"}");
        }
        return new ResponseEntity<>(gr, HttpStatus.OK);
    }

    /**
     * Get the passing rate of a course.
     *
     * @param course course code of the course
     * @return the passing rate as a double in between 0.0-1.0
     * @throws JSONException exception if json is wrong
     */
    @SuppressWarnings("PMD")
    public ResponseEntity<Double> passingRate(String course) throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double passCount = 0.0;
        for (String netId : students) {
            List<Grade> list =
                    gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double grade = StudentLogic.getGrade(list, course);
            if (grade > passingGrade) {
                passCount++;
            }
        }
        double passRate = passCount / students.size();
        return new ResponseEntity<>(passRate, HttpStatus.OK);
    }

    /**
     * Method to get the statistics such as mean and variance of final grades of a course.
     *
     * @param course course code whose statistics are needed
     * @return JSON containing the statistics
     * @throws JSONException json exception if wrong json
     */
    @SuppressWarnings("PMD")
    public ResponseEntity<String> meanAndVariance(String course)
            throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Double> grades = new ArrayList<>(students.size());
        double sum = 0.0;
        for (String netId : students) {
            List<Grade> list =
                    gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double g = StudentLogic.getGrade(list, course);
            grades.add(g);
            sum = sum + g;
        }
        sum = sum / students.size();
        double variance = StudentLogic.getVariance(grades, sum);
        String json =  "{\"mean\":\"" + sum + "\", \"variance\":\"" + variance + "\"}";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
