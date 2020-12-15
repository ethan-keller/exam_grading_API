package nl.tudelft.sem10.userservice.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.client.RestTemplate;

/**
 * A utility class to contain the necessary logic.
 */
public class Utility {

    /**
     * Encrypts the given text using SHA-512 algorithm.
     *
     * @param input of type String - the String to hash
     * @return hashText of type String - hashed representation of the string.
     *
     * @throws NoSuchAlgorithmException - no such algorithm
     */
    public static String encrypt(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger sg = new BigInteger(1, messageDigest);
        String hashText = sg.toString(16);

        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

    /**
     * Sends the plain text password to Authentication service and receives the encrypted password.
     *
     * @param password of type String - the plain text password
     * @param restTemplate of type RestTemplate the rest API
     * @return String - encrypted
     */
    public static String getEncryptedPassword(String password, RestTemplate restTemplate) {
        //Hash the password
        String hashed = "";
        try {
            hashed += Utility.encrypt(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Send it to authentication service and receive the encrypted password
        String url = "http://localhost:8080/encode/{password}";

        return restTemplate.getForObject(url, String.class, hashed);
    }

}
