package com.smart.config;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LegacyAwarePasswordEncoder implements PasswordEncoder {
    private static final Pattern BCRYPT_HASH = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bcrypt.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        if (BCRYPT_HASH.matcher(storedPassword).matches()) {
            return bcrypt.matches(rawPassword, storedPassword);
        }
        // Previous releases stored raw passwords; DaoAuthenticationProvider rehashes them on successful login.
        return rawPassword.toString().equals(storedPassword);
    }

    @Override
    public boolean upgradeEncoding(String storedPassword) {
        return storedPassword == null
                || !BCRYPT_HASH.matcher(storedPassword).matches()
                || bcrypt.upgradeEncoding(storedPassword);
    }
}
