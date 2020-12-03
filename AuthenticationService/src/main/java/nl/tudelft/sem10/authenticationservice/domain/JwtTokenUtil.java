package nl.tudelft.sem10.authenticationservice.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for JWT tokens.
 */
@Component
public class JwtTokenUtil implements Serializable {

    /**
     * Time of validity for a token (set to 1 day = 24 hours).
     */
    private static final long JWT_VALIDITY = 24 * 60 * 60;
    private static final long serialVersionUID = -1433881298817633516L;

    // transient for PMD
    @Value("${jwt.secret}")
    private transient String secret;

    /**
     * Get the user's netId from the token.
     *
     * @param token user's token
     * @return user's netId
     */
    public String getNetIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get the token's expiration date.
     *
     * @param token user's token
     * @return token's expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Checks if token is expired.
     *
     * @param token token
     * @return true if token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Get a certain claim (information) about a token.
     *
     * @param token          token
     * @param claimsResolver function which chooses what claim to return
     * @param <T>            generic type T
     * @return the claim (information)
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims (information) about a token.
     *
     * @param token token
     * @return all the claims (information) about the token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Checks the validity of the token.
     *
     * @param token       token
     * @param userDetails information about the user
     * @return true if token and user match, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String netId = getNetIdFromToken(token);
        return netId.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Generates a token from the user information.
     *
     * @param userDetails user information
     * @return string representation of the generated token
     */
    public String generateToken(UserDetails userDetails) {
        return internalGenerateToken(userDetails.getUsername());
    }

    /**
     * Sets issue date, expiration date and hashes a new token.
     *
     * @param netId user's netId
     * @return string representation of the generated token
     */
    private String internalGenerateToken(String netId) {
        return Jwts.builder().setClaims(new HashMap<>()).setSubject(netId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * JWT_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
