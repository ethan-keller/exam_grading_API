package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for userService module. Provides basic endpoints to interact with users database.
 */
@Controller
@RequestMapping(path = "/user")
public class UserController {


    @Autowired
    private UserRepository userRepository; //NOPMD

    /**
     * Setter for the userRepository, mainly used for testing purposes.
     * @param userRepository  of type UserRepository
     */
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets all the users in the database.
     *
     * @return return all users as a List of type user
     */
    @GetMapping("")
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
    @GetMapping("/{netId}")
    @ResponseBody
    public ResponseEntity<String> userByNetId(@PathVariable String netId) {
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
    @GetMapping("/{netId}/{password}")
    @ResponseBody
    public ResponseEntity<String> userByNetIdAndPassword(@PathVariable String netId, @PathVariable String password) {
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
     * @param jsonString - JSON representation of a User object
     * @return status code and a string with conformation
     */
    @PostMapping("")
    @Modifying
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        String netId = json.getString("netId");
        User u = userRepository.getUserByNetId(netId);
        if (u != null) {
            return new ResponseEntity<>("User already exists", HttpStatus.IM_USED);
        }
        String password = json.getString("password");
        int type = json.getInt("type");
        userRepository.insertUser(netId, password, type);
        User n = new User(netId, password, type);
        return new ResponseEntity<>(n.toString(), HttpStatus.CREATED);
    }

    /**
     * Endpoint to delete a given user.
     *
     * @param jsonString - JSON representation of a User object
     * @return status code and response string
     */
    @DeleteMapping("")
    @Modifying
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestBody String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        String netId = json.getString("netId");
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
     * @param jsonString - JSON representation of a User object
     * @return status code and response string
     */
    @PutMapping("")
    @Modifying
    @ResponseBody
    public ResponseEntity<String> changeDetails(@RequestBody String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        String netId = json.getString("netId");
        User u = userRepository.getUserByNetId(netId);
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String password = json.getString("password");
        int type = json.getInt("type");
        userRepository.updateUser(netId, password, type);
        User n = new User(netId, password, type);
        return new ResponseEntity<>(n.toString(), HttpStatus.OK);
    }

    /**
     * Endpoint to return a specific type of users.
     *
     * @param type type of user needed
     * @return list of users of given type
     */
    @GetMapping("/type/{type}")
    @ResponseBody
    public List<User> userByType(@PathVariable int type) {
        return userRepository.getUsersOfType(type);
    }

}
