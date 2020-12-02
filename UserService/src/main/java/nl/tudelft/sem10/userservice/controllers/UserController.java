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

@Controller
@RequestMapping(path = "/userService")
public class UserController {
    @Autowired
    private UserRepository userRepository; //NOPMD


    @GetMapping("/users")
    @ResponseBody
    public List<User> users() {
        return userRepository.getAllUsers();
    }

    @GetMapping("/userByNetId")
    @ResponseBody
    public User userByNetId(String netId) {
        return userRepository.getUserByNetId(netId);
    }

    @GetMapping("/userByNetIdAndPassword")
    @ResponseBody
    public User userByNetIdAndPassword(String netId, String password) {
        return userRepository.getUserByNetIdAndPassword(netId, password);
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> createUser(String netId, String password, int type) {
        User u = userRepository.getUserByNetId(netId);
        if (u != null) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        } else {
            userRepository.insertUser(netId, password, type);
            User n = new User(netId, password, type);
            return new ResponseEntity<>(n.toString(), HttpStatus.CREATED);
        }

        //return userRepository.insertUser(netId, password, type);
    }

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

    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity test() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
