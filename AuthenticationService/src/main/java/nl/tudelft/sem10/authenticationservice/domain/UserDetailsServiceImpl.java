package nl.tudelft.sem10.authenticationservice.domain;

import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.framework.RestService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service that handles the user details.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final transient RestService rest = new RestService();

    /**
     * Get user details for a specific user.
     *
     * @param username netId of the user
     * @return user details of the user
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = rest.getUserFromUserService(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find a user with netId: " + username);
        }
        return new UserDetailsImpl(user);
    }
}
