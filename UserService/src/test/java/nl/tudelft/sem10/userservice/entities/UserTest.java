package nl.tudelft.sem10.userservice.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The User entity unit test.
 */
class UserTest {

  private transient User student1;
  private transient User student2;
  private transient User teacher1;
  private transient User teacher2;
  private transient User nullUser = null;
  private transient User student1Cop;
  private transient User teacher1Cop;
  private transient final String password = "pass";
  private transient final String password1 = "pass1";
  private transient final String netIdStudent1 = "student1";
  private transient final String netIdStudent2 = "student2";
  private transient final String netIdTeacher1 = "teacher1";
  private transient final String netIdTeacher2 = "teacher2";


  /**
   * Initialize User entities before each test.
   */
  @BeforeEach
  void setUp() {
    student1 = new User(netIdStudent1, password,0);
    student2 = new User(netIdStudent2, password1,0);
    student1Cop = new User(netIdStudent1, password,0);
    teacher1 = new User(netIdTeacher1, password,1);
    teacher1Cop = new User(netIdTeacher1, password,1);
    teacher2 = new User(netIdTeacher2, password1,1);
  }

  /**
   * Test for the getter of the NetId field.
   */
  @Test
  void getNetId() {
    String netId = "student1";
    String test = student1.getNetId();

    String netIdTeacher = "teacher1";
    String testTeacher = teacher1.getNetId();

    Assertions.assertEquals(netId,test);
    Assertions.assertEquals(netIdTeacher,testTeacher);
  }

  /**
   * Test for the getter of the password field.
   */
  @Test
  void getPassword() {
    String password = "pass1";
    String test = student2.getPassword();

    String passwordTeacher = "pass1";
    String testTeacher = teacher2.getPassword();

    Assertions.assertEquals(password,test);
    Assertions.assertEquals(passwordTeacher,testTeacher);
  }

  /**
   * Test for the getter of the type field.
   */
  @Test
  void getType() {
    int type = 0;
    int test = student1.getType();

    int typeTeacher = 1;
    int testTeacher = teacher1.getType();

    Assertions.assertEquals(type,test);
    Assertions.assertEquals(typeTeacher,testTeacher);
  }

  /**
   * Test for the setter of the NetId field.
   */
  @Test
  void setNetId() {
    String update = "student1newNetId";
    student1.setNetId(update);

    Assertions.assertEquals(update, student1.getNetId());
  }

  /**
   * Test for the setter of the password field.
   */
  @Test
  void setPassword() {
    String update = "newPass";
    student1.setPassword(update);

    Assertions.assertEquals(update, student1.getPassword());
  }

  /**
   * Test for the setter of the type field.
   */
  @Test
  void setType() {
    int update = 1;
    student1.setType(update);

    Assertions.assertEquals(update, student1.getType());
  }

  /**
   * Test If the toString method outputs a human-readable String.
   */
  @Test
  void testToString() {
    String test = "{\"netId\":\"" + "student1" + "\", \"password\":\""
            + "pass" + "\", \"type\":\"" + 0 + "\"}";

    Assertions.assertEquals(test, student1.toString());
  }

  /**
   * Test if the Equals() method works for the same instance of the User entity.
   */
  @Test
  void testEquals() {
    Assertions.assertTrue(student1.equals(student1));
    Assertions.assertTrue(teacher1.equals(teacher1));
  }

  /**
   * Test if the Equals() method works for a different instance of the User entity with same parameters.
   */
  @Test
  void testEqualsSame() {
    Assertions.assertTrue(student1.equals(student1Cop));
    Assertions.assertTrue(teacher1.equals(teacher1Cop));
  }

  /**
   * Test if the Equals() method works for the comparison with a null User.
   */
  @Test
  void testEqualsNull() {
    Assertions.assertFalse(student1.equals(nullUser));
    Assertions.assertFalse(teacher1.equals(nullUser));
  }

  /**
   * Test if the Equals() method works for two different User entities.
   */
  @Test
  void testNotEquals() {
    Assertions.assertFalse(student1.equals(teacher1));
  }

  /**
   * Test if the hashCode() generates the same HashCode for the Users with same parameters.
   */
  @Test
  void testHashCode() {
    Assertions.assertEquals(student1.hashCode(),student1Cop.hashCode());
    Assertions.assertEquals(teacher1.hashCode(),teacher1Cop.hashCode());
  }
}