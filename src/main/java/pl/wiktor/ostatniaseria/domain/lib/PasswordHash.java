package pl.wiktor.ostatniaseria.domain.lib;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHash {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String plainTextPassword) {
        return passwordEncoder.encode(plainTextPassword);
    }
}