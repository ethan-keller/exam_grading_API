package nl.tudelft.sem10.courseservice.application;

import java.util.Locale;

/**
 * An authentication service to validate tokens.
 */
@FunctionalInterface
public interface AuthService {
    /**
     * Get a user type for a given token.
     *
     * @param token - String Authorization token.
     * @return the user type.
     */
    public UserType getUser(String token);

    /**
     * User types.
     */
    // Note that no STUDENT type exists as the course application does not need it
    public static enum UserType {
        /**
         * The user is a teacher.
         */

        TEACHER,

        /**
         * The user has an other role.
         */
        OTHER,

        /**
         * Not a known user / unauthorized user.
         */
        UNKNOWN;

        /**
         * Get a user type from text representation.
         *
         * @param raw - String Text representation of user type (case insensitive).
         * @return the matching user type or OTHER as fallback.
         */
        public static UserType of(String raw) {
            switch (raw.toLowerCase(Locale.getDefault())) {
                case "teacher":
                    return TEACHER;
                default:
                    return OTHER;
            }
        }
    }
}
