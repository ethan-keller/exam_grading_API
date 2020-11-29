package nl.tudelft.sem10.userservice.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@SuppressWarnings("unused")
public class Student extends User {

  @Id
  private int netId;

  private String password;

  private final int type = 0;


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

  public Student() {
    super(-1, "", -1);
  }
}
