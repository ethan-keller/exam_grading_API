package nl.tudelft.sem10.userservice.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The UserController class's test.
 */
class UserControllerTest {

  private transient UserController userController;
  // The following fields were created to adhere to the PMD rules.
  transient String netId;
  transient String password;
  transient String student1;
  transient String n;
  transient String pass;
  transient String type0;
  transient String parenthesis;


  /**
   * Initialize these variables before each test case.
   */
  @BeforeEach
  void setUp() {
    MockRepository mock = new MockRepository();
    userController = new UserController();
    userController.setUserRepository(mock);
    netId = "netId";
    password = "password";
    student1 = "student1";
    n = "{\n";
    pass = "\"password\": \"pass\",\n";
    type0 = "        \"type\": 0\n";
    parenthesis = "    }";
  }


  /**
   * Test if the list of users is retrieved.
   */
  @Test
  void users() {
    String allUsers = userController.users().toString();
    assertTrue(allUsers.contains(netId));
    assertTrue(allUsers.contains(password));
    assertTrue(allUsers.contains("type"));
  }

  /**
   * Test the retrieval of a user given its netId.
   * If the user doesn't exist, 404 Not Found code must be present in the ResponseEntity.
   */
  @Test
  void userByNetId() {
    String valid = String.valueOf(userController.userByNetId(student1));
    String invalid = String.valueOf(userController.userByNetId("doesn't exist"));

    assertTrue(valid.contains(netId));
    assertTrue(valid.contains(student1));
    assertTrue(valid.contains(HttpStatus.OK.toString()));
    assertTrue(invalid.contains(HttpStatus.NOT_FOUND.toString()));
  }

  /**
   * Test the retrieval of a user given its netId and password.
   * If the user doesn't exist, 404 Not Found code must be present in the ResponseEntity.
   */
  @Test
  void userByNetIdAndPassword() {
    String valid = String.valueOf(userController.userByNetIdAndPassword("student1","pass"));
    String invalid = String.valueOf(userController.userByNetIdAndPassword("doesn't exist","invalid"));

    assertTrue(valid.contains(netId));
    assertTrue(valid.contains(student1));
    assertTrue(valid.contains(HttpStatus.OK.toString()));
    assertTrue(invalid.contains(HttpStatus.NOT_FOUND.toString()));
  }

  /**
   * Test if a user is created successfully.
   * If successful, Response Entity must contain status code 201.
   * If not, Response Entity must contain status code 226, meaning that the user already exists.
   *
   */
  @Test
  void createUser() {
    String existingJson = n +
            "        \"netId\": \"student1\",\n" +
                    pass +
            type0 +
            parenthesis;
    String  jsonString = n +
            "        \"netId\": \"newUser\",\n" +
                    pass +
            type0 +
            parenthesis;
    String valid = String.valueOf(userController.createUser(jsonString));
    String invalid = String.valueOf(userController.createUser(existingJson));

    System.out.println(valid);
    System.out.println(invalid);
    assertTrue(valid.contains(netId));
    assertTrue(valid.contains("newUser"));
    assertTrue(valid.contains(HttpStatus.CREATED.toString()));
    assertTrue(invalid.contains(HttpStatus.IM_USED.toString()));
  }

  /**
   * Test if deletion o an existing user is successful.
   * If user doesn't exist, Response Entity must contain status code 404.
   */
  @Test
  void deleteUser() {
    String existingJson = n +
            "        \"netId\": \"student1\",\n" +
                    pass +
            type0 +
            parenthesis;
    String  nonExistingJson = n +
            "        \"netId\": \"newUser\",\n" +
                    pass +
            type0 +
            parenthesis;
    String valid = String.valueOf(userController.deleteUser(existingJson));
    String invalid = String.valueOf(userController.deleteUser(nonExistingJson));

    System.out.println(valid);
    System.out.println(invalid);
    assertTrue(valid.contains(HttpStatus.OK.toString()));
    assertTrue(invalid.contains(HttpStatus.NOT_FOUND.toString()));
  }

  /**
   * Test if the attributes of an existing user can be changed.
   * If the user doesn't exist, Response Entity must contain status code 404.
   */
  @Test
  void changeDetails() {
    String existingJson = n +
            "        \"netId\": \"student1\",\n" +
            "        \"password\": \"newPass\",\n" +
            type0 +
            parenthesis;
    String  nonExistingJson = n +
            "        \"netId\": \"newUser\",\n" +
                    pass +
            type0 +
            parenthesis;
    String valid = String.valueOf(userController.changeDetails(existingJson));
    String invalid = String.valueOf(userController.changeDetails(nonExistingJson));

    System.out.println(valid);
    System.out.println(invalid);
    assertTrue(valid.contains(netId));
    assertTrue(valid.contains("newPass"));
    assertTrue(valid.contains(HttpStatus.OK.toString()));
    assertTrue(invalid.contains(HttpStatus.NOT_FOUND.toString()));
  }

  /**
   * Test if a list of users can be retrieved, given their type.
   */
  @Test
  void userByType() {
    String allStudents = userController.userByType(0).toString();
    String allTeachers = userController.userByType(1).toString();
    var invalid = userController.userByType(2);
    assertTrue(allStudents.contains(student1));
    assertTrue(allTeachers.contains("teacher1"));
    assertNull(invalid);
  }
}