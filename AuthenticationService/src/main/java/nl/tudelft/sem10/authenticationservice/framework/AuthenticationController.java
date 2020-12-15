package nl.tudelft.sem10.authenticationservice.framework;

import nl.tudelft.sem10.authenticationservice.application.RoleType;
import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.domain.JwtRequest;
import nl.tudelft.sem10.authenticationservice.domain.JwtResponse;
import nl.tudelft.sem10.authenticationservice.domain.JwtTokenUtil;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST controller for authentication.
 */
@Controller
public class AuthenticationController {

    // use of transient for PMD
    @Autowired
    private transient JwtTokenUtil jwtTokenUtil;
    @Autowired
    private transient UserDetailsService userDetailsService;
    @Autowired
    private transient PasswordEncoder passwordEncoder;
    @Autowired
    private transient RestService rest;

    /**
     * Authentication endpoint.
     *
     * @param request the JwtRequest that arrived at the endpoint
     * @return ResponseEntity with response
     */
    @GetMapping("/getToken")
    public ResponseEntity<JwtResponse> getToken(@RequestBody JwtRequest request) {
        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(request.getNetId());
        if (userDetails != null && userDetails.validate(request.getPassword())) {
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Endpoint for password encoding.
     * The received password gets encoded and sent back
     *
     * @param hashedPassword the hashed password
     * @return the encoded representation of the hashed password
     */
    @GetMapping("/encode/{hashedPassword}")
    public ResponseEntity<String> encodePassword(@PathVariable final String hashedPassword) {
        return ResponseEntity.ok(passwordEncoder.encode(hashedPassword));
    }

    /**
     * Endpoint to validate token.
     *
     * @param token the token
     * @return name of the user's role if validated, else status code 400
     */
    @GetMapping("/validate/{token}")
    public ResponseEntity<String> validateToken(@PathVariable final String token) {
        String netId = jwtTokenUtil.getNetIdFromToken(token);
        if (netId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = rest.getUserFromUserService(netId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getRole().getType() == RoleType.STUDENT) {
            return ResponseEntity.ok(user.getRole().getName());
        } else {
            return ResponseEntity.ok(user.getRole().getName());
        }
    }
}
