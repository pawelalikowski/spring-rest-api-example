package com.example.util;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.SecureRandom;

@Component
public class TokenGenerator {
    private SecureRandom random = new SecureRandom();

    public String generateToken() {
        return new BigInteger(130, random).toString(32);
    }
}
