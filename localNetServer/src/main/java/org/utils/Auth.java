package org.utils;

import java.util.Base64;

public class Auth {
    public static String basicAuth(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
