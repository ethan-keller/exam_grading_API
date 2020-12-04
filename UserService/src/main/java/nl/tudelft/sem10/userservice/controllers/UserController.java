package nl.tudelft.sem10.userservice.controllers;

import java.util.List;
import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller for userService module. Provides basic endpoints to interact with users database.
 */
@Controller
@RequestMapping(path = "/userService")
public class UserController {
    @Autowired
    private UserRepository userRepository; //NOPMD

    /**
     * Gets all the users in the database.
     *
     * @return return all users as a List of type user
     */
    @GetMapping("/all")
    @ResponseBody
    public List<User> users() {
        return userRepository.getAllUsers();
    }

    /**
     * Endpoint to get one specific user.
     *
     * @param netId netId of the user in question
     * @return returns the user identified with the string.
     */
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<String> userByNetId(String netId) {
        User u = userRepository.getUserByNetId(netId);
        if (u == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(u.toString(), HttpStatus.OK);
        }
    }

    /**
     * Endpoint to get a user by both password and netId.
     *
     * @param netId    netId of user
     * @param password password of user
     * @return Json of user
     */
    @GetMapping("/userByNetIdAndPassword")
    @ResponseBody
    public ResponseEntity<String> userByNetIdAndPassword(String netId, String password) {
        User u = userRepository.getUserByNetIdAndPassword(netId, password);
        if (u == null) {
            return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(u.toString(), HttpStatus.OK);
        }
    }

    /**
     * Endpoint to create and add user to database.
     *
     * @param netId    netId of user
     * @param password password of user
     * @param type     type of user
     * @return status code and a string with conformation
     */
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> createUser(String netId, String password, int type) {
        User u = userRepository.getUserByNetId(netId);
        if (u != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.IM_USED);
        } else {
            userRepository.insertUser(netId, password, type);
            User n = new User(netId, password, type);
            return new ResponseEntity<>(n.toString(), HttpStatus.CREATED);
        }
    }

    /**
     * Endpoint to delete a given user.
     *
     * @param netId netId of user
     * @return status code and response string
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteUser(String netId) {
        User u = userRepository.getUserByNetId(netId);
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            userRepository.deleteUser(netId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Endpoint to change an existing user.
     *
     * @param netId    netId of user
     * @param password password of user
     * @param type     type of user
     * @return status code and response string
     */
    @PostMapping(path = "/change")
    @ResponseBody
    public ResponseEntity<String> changeDetails(String netId, String password, int type) {
        User u = userRepository.getUserByNetId(netId);
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            userRepository.updateUser(netId, type, password);
            User n = new User(netId, password, type);
            return new ResponseEntity<>(n.toString(), HttpStatus.OK);
        }
    }

    /**
     * Endpoint to return a specific type of users.
     *
     * @param type type of user needed
     * @return list of users of given type
     */
    @GetMapping("/usersOfType")
    @ResponseBody
    public List<User> userByType(int type) {
        return userRepository.getUsersOfType(type);
    }

    /**
     * Endpoint to test connection.
     *
     * @return confirmation string and a 200OK
     */
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("This is a test", HttpStatus.OK);
    }

}
