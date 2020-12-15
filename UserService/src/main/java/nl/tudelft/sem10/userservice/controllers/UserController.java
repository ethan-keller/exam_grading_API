package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.domain.Utility;
import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.security.NoSuchAlgorithmException;
import java.util.List;


/**
 * Controller for userService module. Provides basic endpoints to interact with users database.
 */
@Controller
@RequestMapping(path = "/user")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class UserController {


    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient RestTemplate restTemplate;

    // To adhere to the lovely PMD rules
    private transient final String netIdStr = "netId";

    /**
     * Setter for the userRepository, mainly used for testing purposes.
     * @param userRepository  of type UserRepository
     */
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Setter for the restTemplate, mainly used for testing purposes.
     * @param restTemplate  of type RestTemplate
     */
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
     * Endpoint to create and add user to database.
     *
     * @param jsonString - JSON representation of a User object
     * @return status code and a string with conformation
     */
    @PostMapping("")
    @Modifying
    @ResponseBody
    public ResponseEntity<String> createUser(@RequestBody String jsonString) throws JSONException, NoSuchAlgorithmException {
        JSONObject json = new JSONObject(jsonString);
        if (json.has(netIdStr) && json.has("password") && json.has("type")) {
            String netId = json.getString(netIdStr);
            User u = userRepository.getUserByNetId(netId);
            if (u != null) {
                return new ResponseEntity<>("User already exists", HttpStatus.IM_USED);
            }
            String password = json.getString("password");
            //Hash the password
            String hashed = Utility.encrypt(password);
            //Send it to authentication service and receive the encrypted password
            String url = "http://localhost:8080/encode/{password}";
            String encrypted = restTemplate.getForObject(url, String.class, hashed);
            int type = json.getInt("type");
            userRepository.insertUser(netId, encrypted, type);
            User n = new User(netId, encrypted, type);
            return new ResponseEntity<>(n.toString(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("Format not understood", HttpStatus.BAD_REQUEST);
        }
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
        if (json.has(netIdStr)) {
            String netId = json.getString(netIdStr);
            User u = userRepository.getUserByNetId(netId);
            if (u == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                userRepository.deleteUser(netId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>("Format not understood", HttpStatus.BAD_REQUEST);
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
        if (json.has(netIdStr) && json.has("password") && json.has("type")) {
            String netId = json.getString(netIdStr);
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
        else{
            return new ResponseEntity<>("Format not understood", HttpStatus.BAD_REQUEST);
        }
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
