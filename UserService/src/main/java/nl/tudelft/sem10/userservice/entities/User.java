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

  /**
   * Default constructor for user.
   */
  public User() {
    this.netId = null;
    this.password= null;
    this.type = -1;
  }

  /**
   * Gets netId of user
   * @return String netId
   */
  public String getNetId() {
    return netId;
  }

  /**
   * Gets password of user.
   * @return String password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets the type of user
   * @return int type
   */
  public int getType() {
    return type;
  }

  /**
   * Set the users netId
   * @param netId netId of user
   */
  public void setNetId(String netId) {
    this.netId = netId;
  }

  /**
   * Sets password of user
   * @param password password of user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set the type of user
   * @param type type of user
   */
  public void setType(int type) {
    this.type = type;
  }

  /**
   * Standard to string method for user.
   */
  public String toString() {
    //return "netID: " + netId + " Pass: " + password + " type: " + type;
    return "{\"netId\":\"" + netId + "\", \"password\":\"" + password + "\", \"type\":\"" + type + "\"}";
  }

  /**
   * Equals methid to check if two users are the same.
   */
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