package nl.tudelft.sem10.authenticationservice.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.tudelft.sem10.authenticationservice.application.Role;
import nl.tudelft.sem10.authenticationservice.application.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Custom implementation for user details.
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -4120834141761298149L;
    // use of transient for PMD
    private final transient User user;

    /**
     * Constructor.
     *
     * @param user user
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * Gets role of the user.
     *
     * @return collection that contains the role of the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    /**
     * Checks if a password is correct.
     *
     * @param password the password
     * @return true if match, false otherwise
     */
    public boolean validate(final String password) {
        return password.equals(getPassword());
    }

    /**
     * Getter for the user's password.
     *
     * @return user's password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Getter for the user's netId.
     *
     * @return user's netId
     */
    @Override
    public String getUsername() {
        return user.getNetId();
    }

    /**
     * Checks if user is not expired.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if user is not locked.
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if user credentials are not expired.
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if user is enabled.
     *
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
