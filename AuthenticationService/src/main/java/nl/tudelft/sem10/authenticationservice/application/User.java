package nl.tudelft.sem10.authenticationservice.application;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * User entity class.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 3118960921901542232L;
    private static final int student = 0;
    private static final int teacher = 1;
    private final String netId;
    private final String password;
    private final Role role;

    /**
     * Constructor.
     * Gets used when parsing JSON.
     *
     * @param netId    the netId of the user
     * @param password the password of the user
     * @param type     the type of the user
     */
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public User(@JsonProperty("netId") String netId,
                @JsonProperty("password") String password,
                @JsonProperty("type") int type) {
        this.netId = netId;
        this.password = password;
        if (type == student) {
            this.role = new Role(RoleType.STUDENT);
        } else if (type == teacher) {
            this.role = new Role(RoleType.TEACHER);
        } else {
            // if not an existing role, set role with lowest permissions
            this.role = new Role(RoleType.STUDENT);
        }
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
     * Getter for the user's role in integer representation.
     *
     * @return 0 if student and 1 if teacher
     */
    public int getRoleInteger() {
        if (getRole().getType() == RoleType.STUDENT) {
            return student;
        } else if (getRole().getType() == RoleType.TEACHER) {
            return teacher;
        } else {
            return -1;
        }
    }

    /**
     * Checks equality between users.
     *
     * @param o other object to compare to
     * @return true if netId's are equal, false otherwise
     */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
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
}
