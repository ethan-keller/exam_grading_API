package nl.tudelft.sem10.authenticationservice.application;

import java.io.Serializable;
import java.util.Objects;

/**
 * User entity class.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 3118960921901542232L;
    private final String netId;
    private final String password;
    private final Role role;

    /**
     * Constructor.
     *
     * @param netId    the netId of the user
     * @param password the password of the user
     */
    public User(String netId, String password) {
        this.netId = netId;
        this.password = password;
        // TODO: add roletype as parameter after JSON parsing determines the roletype
        this.role = new Role(RoleType.STUDENT);
    }

    /**
     * Getter for the user's identifying netId.
     *
     * @return netId
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Getter for the user's password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the user's role.
     *
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Checks equality between users.
     *
     * @param o other object to compare to
     * @return true if netId's are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return getNetId().equals(user.getNetId());
    }

    /**
     * Hash code generator.
     *
     * @return the hash value of this User instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(getNetId(), getPassword(), getRole());
    }
}
