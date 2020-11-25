package nl.tudelft.sem10.authenticationservice.application;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private RoleType type;

    public String getName() {
        return name;
    }

    public RoleType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }
}
