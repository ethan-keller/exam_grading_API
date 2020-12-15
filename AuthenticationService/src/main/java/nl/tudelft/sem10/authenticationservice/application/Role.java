package nl.tudelft.sem10.authenticationservice.application;

import java.io.Serializable;
import java.util.Objects;

/**
 * Role entity class.
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -5677307558797652498L;
    private final RoleType type;

    /**
     * Constructor.
     *
     * @param type enum providing the type of the role
     */
    public Role(RoleType type) {
        this.type = type;
    }

    /**
     * Getter for the name of the role.
     *
     * @return name of the role
     */
    public String getName() {
        if (type == RoleType.STUDENT) {
            return "STUDENT";
        } else {
            return "TEACHER";
        }
    }

    /**
     * Getter for the type of the role.
     *
     * @return enum role type
     */
    public RoleType getType() {
        return type;
    }

    /**
     * Equals method which equates two roles of their types are equal.
     *
     * @param o other object to compare to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return getType() == role.getType();
    }

    /**
     * Hash code generator.
     *
     * @return the hash value of this role instance
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }
}
