package nl.tudelft.sem10.userservice.application;

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
  private transient User student1Cop;
  private transient User teacher1Cop;


  /**
   * Initialize User entities before each test.
   */
  @BeforeEach
  void setUp() {
    String password = "pass";
    String netIdStudent1 = "student1";
    student1 = new User(netIdStudent1, password,0);
    String password1 = "pass1";
    String netIdStudent2 = "student2";
    student2 = new User(netIdStudent2, password1,0);
    student1Cop = new User(netIdStudent1, password,0);
    String netIdTeacher1 = "teacher1";
    teacher1 = new User(netIdTeacher1, password,1);
    teacher1Cop = new User(netIdTeacher1, password,1);
    String netIdTeacher2 = "teacher2";
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
    Assertions.assertEquals(student1, student1);
    Assertions.assertEquals(teacher1, teacher1);
  }

  /**
   * Test if the Equals() method works for a different instance of the User entity with same parameters.
   */
  @Test
  void testEqualsSame() {
    Assertions.assertEquals(student1Cop, student1);
    Assertions.assertEquals(teacher1Cop, teacher1);
  }

  /**
   * Test if the Equals() method works for the comparison with a null User.
   */
  @Test
  void testEqualsNull() {
    Assertions.assertNotEquals(null, student1);
    Assertions.assertNotEquals(null, teacher1);
  }

  /**
   * Test if the Equals() method works for two different User entities.
   */
  @Test
  void testNotEquals() {
    Assertions.assertNotEquals(teacher1, student1);
  }

  /**
   * Test if the hashCode() generates the same HashCode for the Users with same parameters.
   */
  @Test
  void testHashCode() {
    Assertions.assertEquals(student1.hashCode(),student1Cop.hashCode());
    Assertions.assertEquals(teacher1.hashCode(),teacher1Cop.hashCode());
  }

  /**
   * Test to check the functionality of the empty constructor.
   */
  @Test
  void testEmptyConstructor() {
    User emptyUser = new User();
    Assertions.assertNull(emptyUser.getNetId());
    Assertions.assertNull(emptyUser.getPassword());
  }

  /**
   * Test to check the functionality of the empty constructor.
   */
  @Test
  void testEqualsObject() {
    Object o = new Object();
    Assertions.assertNotEquals(student1,o);
    Assertions.assertNotEquals(teacher1,o);
  }

}