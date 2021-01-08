package nl.tudelft.sem10.gradingservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import nl.tudelft.sem10.gradingservice.framework.repositories.GradeRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserGradeService {

    static final double passingGrade = 5.75;
    private transient StudentLogic studentLogic = new StudentLogic();
    @Autowired
    private GradeRepository gradeRepository; // NOPMD

    /**
     * Method to get the mean grade for a student.
     *
     * @param netId The student's netid.
     * @return The mean grade for the student.
     * @throws ResponseStatusException If the grades cannot be found.
     */
    public Float getMean(String netId) throws ResponseStatusException {
        List<Grade> list = gradeRepository.getGradesByNetId(netId);
        if (list.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Grades not here!");
        }
        return Utility.mean(list.stream().map(x -> x.getMark()).collect(Collectors.toList()));
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
                                           String courseCode,
                                           String token) throws JSONException {
        List<Grade> list =
            gradeRepository.getGradesByNetIdAndCourse(netId, courseCode);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double grade = studentLogic.getGrade(list, courseCode, token);
        return new ResponseEntity<>(grade, HttpStatus.OK);
    }

    /**
     * Method to return the course code of all courses a student has passed.
     *
     * @param netId netId of the student
     * @return A list of strings that are the course codes
     * @throws JSONException exception if json is wrong
     */
    public ResponseEntity<List<String>> passedCourses(String netId, String token)
        throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> passed = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            double grade = studentLogic.getGrade(l, course, token);
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
    public ResponseEntity<List<String>> allGrades(String netId, String token)
        throws JSONException {
        List<String> list = gradeRepository.getCoursesOfStudent(netId);
        if (list == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<String> gr = new ArrayList<>();
        for (String course : list) {
            List<Grade> l = gradeRepository.getGradesByNetIdAndCourse(netId, course);
            gr.add("{\"course\":\"" + course + "\", "
                + "\"grade\":\"" + studentLogic.getGrade(l, course, token) + "\"}");
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
    public ResponseEntity<Double> passingRate(String course, String token)
        throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        double passCount = 0.0;
        for (String netId : students) {
            List<Grade> list =
                gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double grade = studentLogic.getGrade(list, course, token);
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
    public ResponseEntity<String> meanAndVariance(String course, String token)
        throws JSONException {
        List<String> students = gradeRepository.getStudentsTakingCourse(course);
        if (students == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Float> grades = new ArrayList<>(students.size());
        for (String netId : students) {
            List<Grade> list =
                gradeRepository.getGradesByNetIdAndCourse(netId, course);
            if (list == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            double g = studentLogic.getGrade(list, course, token);
            grades.add((float) g);
        }
        double mean = Utility.mean(grades);
        double variance = Utility.variance(grades);
        String json = "{\"mean\":\"" + mean + "\", \"variance\":\"" + variance + "\"}";
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    /**
     * updates a grade in the database if the newly given mark is higher than the stored one.
     *
     * @param netid      netid of student
     * @param courseCode course where the grade is from
     * @param gradeType  type of grade (midterm, ...)
     * @param jsonString body of request
     * @throws JSONException     if input format is wrong
     * @throws NotFoundException if grade is not found in database
     */
    public void updateGrade(String netid, String courseCode,
                            String gradeType, String jsonString)
        throws JSONException, NotFoundException {
        ResponseEntity<List<Grade>> response = getAllGrades(netid, courseCode, gradeType);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundException("Invalid arguments");
        }
        Grade grade = Objects.requireNonNull(Objects.requireNonNull(response.getBody())).get(0);
        updateGrade(jsonString, grade.getId());
    }

    /**
     * method to update grade that does actual database call.
     *
     * @param jsonString body of request containing new mark
     * @param gradeId    database id of grade to be updated
     * @throws JSONException if input format is wrong
     */
    private void updateGrade(String jsonString, final long gradeId)
        throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        float mark = (float) obj.getDouble("mark");
        float minBound = 0.0f;
        if (mark < minBound) {
            mark = minBound;
        }
        float maxBound = 10.0f;
        if (mark > maxBound) {
            mark = maxBound;
        }
        assert gradeRepository.findById(gradeId).isPresent();
        Grade currGrade = gradeRepository.findById(gradeId).get();
        if (currGrade.getMark() < mark) {
            gradeRepository.updateGrade(gradeId, mark);
        }
    }

    /**
     * returns all grades with several query options.
     *
     * @param netid      netid of student whose grades are wanted
     * @param courseCode course from which grades are wanted
     * @param gradeType  type of grades that are wanted
     * @return list of grades adhering to the query
     */
    public ResponseEntity<List<Grade>> getAllGrades(String netid,
                                                    String courseCode, String gradeType) {
        List<Grade> gradeList;
        if (netid == null && courseCode == null && gradeType == null) {
            gradeList = gradeRepository.findAll();
        } else if (netid != null && courseCode == null && gradeType == null) {
            gradeList = gradeRepository.getGradesByNetId(netid);
        } else if (netid == null && courseCode != null && gradeType == null) {
            gradeList = gradeRepository.getGradesByCourse(courseCode);
        } else if (netid == null && courseCode != null) {
            gradeList = gradeRepository.getGradesByCourseAndType(courseCode, gradeType);
        } else if (netid != null && courseCode != null && gradeType == null) {
            gradeList = gradeRepository.getGradesByNetIdAndCourse(netid, courseCode);
        } else {
            gradeList = gradeRepository.getGradesByCourseAndTypeAndNetid(courseCode,
                gradeType, netid);
        }
        if (gradeList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gradeList, HttpStatus.OK);
    }

    /**
     * deletes a grade from the database based on given params.
     *
     * @param netid      netid of student whose grade needs to be deleted
     * @param courseCode course from which grade needs to be deleted
     * @param gradeType  type of grades that needs to be deleted
     * @throws NotFoundException if grade is not found in the
     *                           database based on the given parameters
     */
    public void deleteGrade(String netid, String courseCode, String gradeType)
        throws NotFoundException {
        ResponseEntity<List<Grade>> response = getAllGrades(netid, courseCode, gradeType);
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new NotFoundException("Invalid arguments");
        }
        Grade grade = Objects.requireNonNull(response.getBody()).get(0);
        gradeRepository.deleteGrade(grade.getId());
    }

    /**
     * inserts a grade into the database.
     *
     * @param jsonString body of post message
     * @throws JSONException if input format is wrong
     */
    public void insertGrade(String jsonString) throws JSONException {
        JSONObject obj = new JSONObject(jsonString);
        String courseCode = obj.getString("course_code");
        String gradeType = obj.getString("grade_type");
        String netid = obj.getString("netid");
        float mark = (float) obj.getDouble("mark");
        float minBound = 0.0f;
        if (mark < minBound) {
            mark = minBound;
        }
        float maxBound = 10.0f;
        if (mark > maxBound) {
            mark = maxBound;
        }
        gradeRepository.insertGrade(mark, netid, courseCode, gradeType);
    }

}
