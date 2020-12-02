package nl.tudelft.sem10.userservice.entities;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "users")
@SuppressWarnings("unused")
public class User {

  @Id
  @Column(name = "netid")
  private String netId;

  @Column(name = "password")
  private String password;

  @Column(name = "type")
  private int type;

  /**
   * Regular constructor for user.
   *
   * @param netId netId of the user
   * @param password password of the user not stored as plaintext
   * @param type type of the user, represented as an int
   */
  public User(String netId, String password, int type) {
    this.netId = netId;
    this.password = password;
    this.type = type;
  }

  public User() {
  }

  public String getNetId() {
    return netId;
  }

  public String getPassword() {
    return password;
  }

  public int getType() {
    return type;
  }

  public void setNetId(String netId) {
    this.netId = netId;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String toString() {
    return "netID: " + netId + " Pass: " + password + " type: " + type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return netId == user.netId
            && type == user.type
            && password.equals(user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(netId, password, type);
  }
}