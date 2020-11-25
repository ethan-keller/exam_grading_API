package nl.tudelft.sem10.authenticationservice.domain;

import java.io.Serializable;

/**
 * Response that contains token for the requesting user
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -1128141568254635766L;
    private final String token;

    /**
     * JwtRespnse constructor.
     *
     * @param token the user's token
     */
    public JwtResponse(String token) {
        this.token = token;
    }

    /**
     * Getter for the token.
     *
     * @return user's token
     */
    public String getToken() {
        return token;
    }
}
