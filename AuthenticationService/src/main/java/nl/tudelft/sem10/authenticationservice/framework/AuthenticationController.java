package nl.tudelft.sem10.authenticationservice.framework;

import nl.tudelft.sem10.authenticationservice.domain.JwtRequest;
import nl.tudelft.sem10.authenticationservice.domain.JwtResponse;
import nl.tudelft.sem10.authenticationservice.domain.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication.
 */
@RestController
public class AuthenticationController {

    // use of transient for PMD
    @Autowired
    private transient AuthenticationManager authenticationManager;
    @Autowired
    private transient JwtTokenUtil jwtTokenUtil;
    @Autowired
    private transient UserDetailsService userDetailsService;

    /**
     * Authentication endpoint.
     *
     * @param request the JwtRequest that arrived at the endpoint
     * @return ResponseEntity with response
     * @throws Exception exception if authentication fails
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest request) throws Exception {
        authenticate(request.getNetId(), request.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getNetId());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Authenticates a user.
     *
     * @param netId    netId of the user
     * @param password password of the user
     * @throws Exception exception if authentication fails
     */
    private void authenticate(String netId, String password) throws Exception {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(netId, password));
        } catch (BadCredentialsException e) {
            throw new Exception("User credentials are incorrect");
        }
    }


}
