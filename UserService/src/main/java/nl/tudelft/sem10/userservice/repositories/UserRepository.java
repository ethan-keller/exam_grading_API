package nl.tudelft.sem10.userservice.repositories;

import java.util.List;
import javax.transaction.Transactional;
import nl.tudelft.sem10.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user", nativeQuery = true)
    List<User> getAllUsers();

    @Query(value = "SELECT * FROM user WHERE netid = ?1", nativeQuery = true)
    User getUserByNetId(String netId);

    @Query(value = "SELECT * FROM user WHERE netid = ?1 AND password = ?2", nativeQuery = true)
    User getUserByNetIdAndPassword(String netId, String password);

    @Query(value = "SELECT * FROM user WHERE type = ?1", nativeQuery = true)
    List<User> getUsersOfType(int type);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user (netid, password, type) "
            + "VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertUser(String netId, String password, int type);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET netid = ?1, password = ?2, "
            + "type = ?3 WHERE netid = ?1", nativeQuery = true)
    void updateUser(String netId, int type, String password);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE netid = ?1", nativeQuery = true)
    void deleteUser(String netId);

}
