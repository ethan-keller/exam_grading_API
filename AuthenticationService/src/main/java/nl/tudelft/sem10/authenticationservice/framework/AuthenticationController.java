package nl.tudelft.sem10.authenticationservice.framework;

import java.security.NoSuchAlgorithmException;
import nl.tudelft.sem10.authenticationservice.application.User;
import nl.tudelft.sem10.authenticationservice.domain.JwtRequest;
import nl.tudelft.sem10.authenticationservice.domain.JwtResponse;
import nl.tudelft.sem10.authenticationservice.domain.JwtTokenUtil;
import nl.tudelft.sem10.authenticationservice.domain.UserDetailsImpl;
import nl.tudelft.sem10.authenticationservice.domain.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

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
    public ResponseEntity<JwtResponse> getToken(@RequestBody JwtRequest request)
            throws NoSuchAlgorithmException {
        final UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(request.getNetId());
        ResponseEntity<JwtResponse>
            response = getResponse(request, userDetails);
        if (response != null) {
            return response;
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gets an <code>OK</code> response containing a JWT response with a token if the user details
     * match the ones stored in our database.
     * If the credentials *don't* match, this returns <code>null</code>
     *
     * @param request the JwtRequest that arrived at the endpoint.
     * @param userDetails The user's details.
     * @return An <code>OK</code> response or <code>null</code> the credentials don't match
     * @throws NoSuchAlgorithmException
     */
    private ResponseEntity<JwtResponse> getResponse(JwtRequest request,
                                                                     UserDetailsImpl userDetails)
        throws NoSuchAlgorithmException {
        if (userDetails != null) {
            final String token = validateDetailsAndGetToken(request, userDetails);
            if (token != null) {
                return ResponseEntity.ok(new JwtResponse(token));
            }
        }
        return null;
    }

    /**
     * Returns a token if the user credentials match the ones stored in the database.
     * @param request the JwtRequest that arrived at the endpoint.
     * @param userDetails The user's details.
     * @return A token if the user credentials match or <code>null</code> if they don't
     * @throws NoSuchAlgorithmException
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private String validateDetailsAndGetToken(JwtRequest request, UserDetailsImpl userDetails)
        throws NoSuchAlgorithmException {
        String token = null;
        if (passwordEncoder
            .matches(Utility.hash(request.getPassword()), userDetails.getPassword())) {
            token = jwtTokenUtil.generateToken(userDetails);
        }
        return token;
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
        return ResponseEntity.ok(user.getRole().getName());
    }

    /**
     * Endpoint to check if netId corresponds to netId encoded in the token.
     *
     * @param netId netId to check
     * @param token token with encoded netId
     * @return true if netId's match, false otherwise
     */
    @GetMapping("/validate/netId/{netId}")
    public ResponseEntity<Boolean> validateNetIdToken(@PathVariable String netId,
                                                      @RequestHeader("Authorization")
                                                              String token) {
        String realNetId = jwtTokenUtil.getNetIdFromToken(token.substring(7));
        if (realNetId == null) {
            return ResponseEntity.ok(false);
        }
        if (realNetId.equals(netId)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
