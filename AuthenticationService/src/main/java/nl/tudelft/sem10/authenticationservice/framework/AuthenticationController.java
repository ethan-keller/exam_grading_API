package nl.tudelft.sem10.authenticationservice.framework;

import nl.tudelft.sem10.authenticationservice.domain.JwtRequest;
import nl.tudelft.sem10.authenticationservice.domain.JwtResponse;
import nl.tudelft.sem10.authenticationservice.domain.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication.
 */
@RestController
public class AuthenticationController {

    // use of transient for PMD
    //    @Autowired
    //    private AuthenticationManager authenticationManager;
    @Autowired
    private transient JwtTokenUtil jwtTokenUtil;
    @Autowired
    private transient UserDetailsService userDetailsService;

    /**
     * Authentication endpoint.
     *
     * @param request the JwtRequest that arrived at the endpoint
     * @return ResponseEntity with response
     */
    @GetMapping("/getToken")
    public ResponseEntity<JwtResponse> getToken(@RequestBody JwtRequest request) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getNetId());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
