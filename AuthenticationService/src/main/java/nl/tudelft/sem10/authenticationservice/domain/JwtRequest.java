package nl.tudelft.sem10.authenticationservice.domain;

import java.io.Serializable;

/**
 * Request that contains user information.
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 6049127233484238473L;
    // use of transient for PMD
    private transient String netId;
    private transient String password;

    /**
     * Default constructor.
     */
    // default constructor needed for JSON parsing
    public JwtRequest() {
    }

    /**
     * JwtRequest Constructor.
     *
     * @param netId    the netId of the user.
     * @param password the password of the user.
     */
    public JwtRequest(String netId, String password) {
        this.netId = netId;
        this.password = password;
    }

    /**
     * Getter for the netId.
     *
     * @return netId of the user
     */
    public String getNetId() {
        return netId;
    }

    /**
     * Getter for the password.
     *
     * @return password of the user
     */
    public String getPassword() {
        return password;
    }

}
