package nl.tudelft.sem10.userservice.application;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@SuppressWarnings("unused")
public class User {

    @Id
    @Column(name = "netid")
    private String netId;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private int type;

    /**
     * Regular constructor for user.
     *
     * @param netId    netId of the user
     * @param password password of the user not stored as plaintext
     * @param type     type of the user, represented as an int
     */
    public User(String netId, String password, int type) {
        this.netId = netId;
        this.password = password;
        this.type = type;
    }

    /**
     * The default constructor of the User object.
     */
    public User() {
    }

    /**
     * Getter for the parameter NetId.
     * @return netId - the NetId of the user
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Getter for the parameter password.
     * @return password - the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the parameter type.
     * @return type - the role of the user
     * Student -> 0
     * Teacher -> 1
     */
    public int getType() {
        return type;
    }

    /**
     *Setter for the parameter netId.
     * @param netId of type String - new netId of the user
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }

    /**
     * Setter for the parameter password.
     * @param password of type String - new password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter for the parameter type
     * @param type of type int - new role of the user
     * Student -> 0
     * Teacher -> 1
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Method to convert a User object to a string in a human-readable format.
     * @return toStr - The String representation of the user
     */
    public String toString() {
        String toStr = "{\"netId\":\"" + netId + "\", \"password\":\""
                + password + "\", \"type\":\"" + type + "\"}";
        return toStr;
    }

    /**
     * Overridden equals method to compare users.
     * @param o of type Object - The Object to compare
     * @return boolean - true if they're equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return netId.equals(user.netId) && type == user.type && password.equals(user.password);
    }

    /**
     * Hash code representation of the user object.
     * @return int - the generated hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(netId, password, type);
    }
}
