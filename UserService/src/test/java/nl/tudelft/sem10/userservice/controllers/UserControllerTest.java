package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * The UserController class's test.
 */
@SpringBootTest
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class UserControllerTest {

  private transient UserController userController;

  private transient List<User> students;

  private transient List<User> teachers;

  private transient User user1;

  @Autowired
  @MockBean
  UserRepository userRepository; //NOPMD

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
    this.userRepository = mock(UserRepository.class);
    this.students = new ArrayList<>();
    this.teachers = new ArrayList<>();
    this.user1 = new User("student1","pass",0);
    User user2 = new User("teacher1", "pass", 1);
    User user3 = new User("student2", "pass", 0);
    User user4 = new User("teacher2", "pass", 1);
    students.add(user1);
    students.add(user3);
    teachers.add(user2);
    teachers.add(user4);


    userController = new UserController();
    userController.setUserRepository(userRepository);
    netId = "netId";
    password = "password";
    student1 = "student1";
    n = "{";
    pass = "\"password\":\"pass\", ";
    type0 = "\"type\":\"0\"";
    parenthesis = "}";
  }


  /**
   * Test if the list of users is retrieved.
   */
  @Test
  void users() {
    when(userRepository.getAllUsers()).thenReturn(students);
    List<User> users = userController.users();
    Iterator<User> iter = users.iterator();
    Iterator<User> iterComp = students.iterator();

    while (iter.hasNext() && iterComp.hasNext()){
      assertEquals(iter.next(),iterComp.next());
    }
  }

  /**
   * Test the retrieval of a user given its netId.
   * If the user doesn't exist, 404 Not Found code must be present in the ResponseEntity.
   */
  @Test
  void userByNetId() {
    when(userRepository.getUserByNetId(anyString())).thenReturn(user1);
    ResponseEntity<String> response = userController.userByNetId(student1);
    String jsonString = response.getBody();
    JSONObject json = new JSONObject(jsonString);
    String netId = json.getString("netId");
    String password = json.getString("password");
    int type = json.getInt("type");
    User n = new User(netId, password, type);

    assertEquals(n,user1);
    assertEquals(response.getStatusCode(),HttpStatus.OK);

    when(userRepository.getUserByNetId(anyString())).thenReturn(null);

    ResponseEntity<String> responseOther = userController.userByNetId(student1);
    assertEquals(responseOther.getStatusCode(),HttpStatus.NOT_FOUND);
  }

  /**
   * Test if a user is created successfully.
   * If successful, Response Entity must contain status code 201.
   * If not, Response Entity must contain status code 226, meaning that the user already exists.
   *
   */
  @Test
  void createUser() {
    when(userRepository.getUserByNetId(anyString())).thenReturn(null);
    User newUser = new User("newUser","pass",0);
    String  jsonStr = newUser.toString();
    ResponseEntity<String> response = userController.createUser(jsonStr);
    String jsonString = response.getBody();
    JSONObject json = new JSONObject(jsonString);
    String netId = json.getString("netId");
    String password = json.getString("password");
    int type = json.getInt("type");
    User n = new User(netId, password, type);

    verify(userRepository,times(1)).insertUser(anyString(),anyString(),anyInt());
    assertEquals(n, newUser);
    assertEquals(response.getStatusCode(),HttpStatus.CREATED);

    when(userRepository.getUserByNetId(anyString())).thenReturn(user1);
    String existingJson = n +
            "        \"netId\": \"student1\"," +
            pass +
            type0 +
            parenthesis;
    ResponseEntity<String> responseOther = userController.createUser(existingJson);
    assertEquals(responseOther.getStatusCode(),HttpStatus.IM_USED);

    String invalidJson = "{SQL injection attack:This is an SQL injection attack}";
    ResponseEntity<String> responseInvalid = userController.createUser(invalidJson);
    assertEquals(responseInvalid.getStatusCode(),HttpStatus.BAD_REQUEST);
  }

  /**
   * Test if deletion o an existing user is successful.
   * If user doesn't exist, Response Entity must contain status code 404.
   */
  @Test
  void deleteUser() {
    when(userRepository.getUserByNetId(anyString())).thenReturn(user1);
    String  jsonStr = user1.toString();
    ResponseEntity<String> response = userController.deleteUser(jsonStr);

    verify(userRepository,times(1)).deleteUser(anyString());
    assertEquals(response.getStatusCode(),HttpStatus.OK);

    when(userRepository.getUserByNetId(anyString())).thenReturn(null);
    String nonExistingJson = n +
            "        \"netId\": \"doesn't exist\"," +
            pass +
            type0 +
            parenthesis;
    ResponseEntity<String> responseOther = userController.deleteUser(nonExistingJson);
    assertEquals(responseOther.getStatusCode(),HttpStatus.NOT_FOUND);

    String invalidJson = "{SQL injection attack:This is an SQL injection attack}";
    ResponseEntity<String> responseInvalid = userController.createUser(invalidJson);
    assertEquals(responseInvalid.getStatusCode(),HttpStatus.BAD_REQUEST);
  }

  /**
   * Test if the attributes of an existing user can be changed.
   * If the user doesn't exist, Response Entity must contain status code 404.
   */
  @Test
  void changeDetails() {
    when(userRepository.getUserByNetId(anyString())).thenReturn(user1);
    User changedUser = new User("student1","newPass",0);
    String  jsonStr = changedUser.toString();
    ResponseEntity<String> response = userController.changeDetails(jsonStr);
    String jsonString = response.getBody();
    JSONObject json = new JSONObject(jsonString);
    String netId = json.getString("netId");
    String password = json.getString("password");
    int type = json.getInt("type");
    User n = new User(netId, password, type);

    verify(userRepository,times(1)).updateUser(anyString(),anyString(),anyInt());
    assertEquals(n, changedUser);
    assertEquals(response.getStatusCode(),HttpStatus.OK);

    when(userRepository.getUserByNetId(anyString())).thenReturn(null);
    String nonExistingJson = n +
            "        \"netId\": \"doesn't exist\"," +
            pass +
            type0 +
            parenthesis;
    ResponseEntity<String> responseOther = userController.changeDetails(nonExistingJson);
    assertEquals(responseOther.getStatusCode(),HttpStatus.NOT_FOUND);

    String invalidJson = "{SQL injection attack:This is an SQL injection attack}";
    ResponseEntity<String> responseInvalid = userController.changeDetails(invalidJson);
    assertEquals(responseInvalid.getStatusCode(),HttpStatus.BAD_REQUEST);
  }

  /**
   * Test if a list of users can be retrieved, given their type.
   */
  @Test
  void userByType() {
    when(userRepository.getUsersOfType(0)).thenReturn(students);
    when(userRepository.getUsersOfType(1)).thenReturn(teachers);

    List<User> studs = userController.userByType(0);
    Iterator<User> iter = studs.iterator();
    Iterator<User> iterComp = students.iterator();

    while (iter.hasNext() && iterComp.hasNext()){
      assertEquals(iter.next(),iterComp.next());
    }

    List<User> teach = userController.userByType(1);
    Iterator<User> iterTeach = teach.iterator();
    Iterator<User> iterCompTeach = teachers.iterator();

    while (iterTeach.hasNext() && iterCompTeach.hasNext()){
      assertEquals(iterTeach.next(),iterCompTeach.next());
    }

    var invalid = userController.userByType(2);
    assertTrue(invalid.isEmpty());
  }
}