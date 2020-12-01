package nl.tudelft.sem10.userservice.controllers;

import nl.tudelft.sem10.userservice.entities.User;
import nl.tudelft.sem10.userservice.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class UserController {
  private transient UserRepository userRepository;

  @GetMapping("/test")
  @ResponseBody
  public ResponseEntity test() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/users")
  @ResponseBody
  public List<User> users() {
    return userRepository.getAllUsers();
  }
}
