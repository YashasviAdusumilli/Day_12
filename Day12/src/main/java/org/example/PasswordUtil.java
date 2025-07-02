package org.example;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // Hash the plain password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify entered password against hashed password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
