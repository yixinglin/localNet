package org.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hsgt.entities.common.User;

import java.util.Date;

public class JwtsUtils {

    private static final long expire = 300; // Seconds
    private static String secret = "13f10949-4fea-45c2-96df-e377d9d5d674";

    public static String token(User user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * expire);
        return Jwts.builder().setHeaderParam("type", "JWT")
                .setSubject(user.getUsername())
                .setIssuedAt(now).setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Claims verify(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

}
