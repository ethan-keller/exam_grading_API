package nl.tudelft.sem10.userservice.domain;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
   * @throws NoSuchAlgorithmException
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
}
