package nl.tudelft.sem10.userservice.repositories;

import nl.tudelft.sem10.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The User Repository to interact with the database.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns all users in the database.
     * @return - List<User>
     */
    @Query(value = "SELECT * FROM user", nativeQuery = true)
    List<User> getAllUsers();

    /**
     * Returns the user with a matching netId.
     * @param netId of type String to be searched
     * @return User
     */
    @Query(value = "SELECT * FROM user WHERE netid = ?1", nativeQuery = true)
    User getUserByNetId(String netId);

    /**
     * Returns the user with a matching netId and password.
     * @param netId of type String to be searched
     * @param password of type String to be searched
     * @return User
     */
    @Query(value = "SELECT * FROM user WHERE netid = ?1 AND password = ?2", nativeQuery = true)
    User getUserByNetIdAndPassword(String netId, String password);

    /**
     * Retrieves all users of a certain type.
     * @param type of type int - the role that's being searched
     * @return List<User>
     */
    @Query(value = "SELECT * FROM user WHERE type = ?1", nativeQuery = true)
    List<User> getUsersOfType(int type);

    /**
     * Add a new user to the database
     * @param netId of type String - to be inserted
     * @param password of type String - to be inserted
     * @param type of type int - to be inserted
     * Student -> 0
     * Teacher -> 1
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user (netid, password, type) "
            + "VALUES (?1, ?2, ?3)", nativeQuery = true)
    void insertUser(String netId, String password, int type);

    /**
     * Modify an existing user.
     * @param netId of type String - to search for, can't be updated at the moment
     * @param password of type String - to be updated
     * @param type of type int - to be updated
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET password = ?2, "
            + "type = ?3 WHERE netid = ?1", nativeQuery = true)
    void updateUser(String netId, String password, int type);

    /**
     * Delete an existing user from the database.
     * @param netId of type String - the netId of the user to be deleted
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE netid = ?1", nativeQuery = true)
    void deleteUser(String netId);

}
