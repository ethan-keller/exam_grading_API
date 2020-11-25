package nl.tudelft.sem10.authenticationservice.application;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity class as database abstraction (Hibernate ORM).
 */
@Entity
@Table(name = "User")
public class User {

    public void setNetId(String netId) {
        this.netId = netId;
    }

    @Id
    @Column(name = "netid")
    private String netId;

    // use of transient for PMD
    @Column(name = "password")
    private transient String password;
    @Column(name = "role")
    @OneToOne(targetEntity = Role.class)
    private transient Role role;


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

    @Override
    public int hashCode() {
        return Objects.hash(getNetId(), getPassword(), getRole());
    }
}
