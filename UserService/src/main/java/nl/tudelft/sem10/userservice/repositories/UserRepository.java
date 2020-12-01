package nl.tudelft.sem10.userservice.repositories;

import nl.tudelft.sem10.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query(value = "SELECT * FROM user", nativeQuery = true)
  List<User> getAllUsers();
}
