package nl.tudelft.sem10.userservice.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student")
@SuppressWarnings("unused")
public class Student extends User {

  /**
   * Regular constructor for student.
   *
   * @param netId netId of the student
   * @param password password of the student not stored as plaintext
   */
  public Student(int netId, String password) {
    //0 = student
    super(netId, password, 0);
  }

}
