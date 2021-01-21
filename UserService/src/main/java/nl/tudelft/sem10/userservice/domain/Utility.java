package nl.tudelft.sem10.userservice.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

/**
 * A utility class to contain the necessary logic.
 */
public class Utility {

    /**
     * Encrypts the given text using SHA-512 algorithm.
     * source : https://www.geeksforgeeks.org/sha-512-hash-in-java/
     *
     * @param input of type String - the String to hash
     * @return hashText of type String - hashed representation of the string.
     * @throws NoSuchAlgorithmException - no such algorithm
     */
    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger sg = new BigInteger(1, messageDigest);
        String hashText = sg.toString(16);

        while (hashText.length() < 128) {
            hashText = "0" + hashText;
        }
        return hashText;
    }

    /**
     * Sends the plain text password to Authentication service and receives the encrypted password.
     *
     * @param password     of type String - the plain text password
     * @param restTemplate of type RestTemplate the rest API
     * @return String - encrypted
     */
    public static String getEncryptedPassword(String password, RestTemplate restTemplate) {
        //Hash the password
        String hashed = "";
        try {
            hashed += Utility.hash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Send it to authentication service and receive the encrypted password
        String url = "http://localhost:8080/encode/{password}";

        return restTemplate.getForObject(url, String.class, hashed);
    }

    /**
     * Method to turn a jsonString to an array of User fields.
     *
     * @param jsonString - of type String
     * @return String[] - an array of fields
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public static String[] jsonStringToFields(String jsonString) {
        JSONObject json = new JSONObject(jsonString);
        if (json.has("netId") && json.has("password") && json.has("type")) {
            String[] ret = new String[3];
            String netId = json.getString("netId");
            int type = json.getInt("type");
            String password = json.getString("password");
            ret[0] = netId;
            ret[1] = password;
            ret[2] = String.valueOf(type);
            return ret;
        }
        return new String[] {};
    }

}
