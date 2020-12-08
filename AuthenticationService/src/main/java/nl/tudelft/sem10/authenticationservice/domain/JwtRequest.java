package nl.tudelft.sem10.authenticationservice.domain;

import java.io.Serializable;

/**
 * Request template for token demand that contains user information.
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 6049127233484238473L;
    private transient String netId;
    private transient String password;
    private transient int type;

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
    public JwtRequest(String netId, String password, int type) {
        this.netId = netId;
        this.password = password;
        this.type = type;
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

    /**
     * Getter for the type.
     *
     * @return type of user
     */
    public int getType() {
        return type;
    }

}
