package nl.tudelft.sem10.userservice.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "student")
@SuppressWarnings("unused")
public class Teacher extends User {
  
  /**
   * Regular constructor for teacher.
   *
   * @param netId    netId of the teacher
   * @param password password of the teacher not stored as plaintext
   */
  public Teacher(int netId, String password) {
    //1 = teacher
    super(netId, password, 1);
  }

}
