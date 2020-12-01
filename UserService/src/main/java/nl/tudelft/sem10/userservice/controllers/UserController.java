package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/userService")
public class UserController {
  @Autowired
  UserRepository userRepository; //NOPMD


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
    return userRepository.getUserByNetIdAndPassword(netId,password);
  }

  @GetMapping("/test")
  @ResponseBody
  public ResponseEntity test() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
