package com.ll.exam.app983.app.security.jwt;

import com.ll.exam.app983.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtProvider {
    private final String keyPlain = "This is a public key. This key must bigger than 500bits.";
    private SecretKey cached__secretKey;

    public SecretKey getSecretKey() {
        if (cached__secretKey == null) {
            cached__secretKey = this._getSecretKey();
        }

        return cached__secretKey;
    }

    private SecretKey _getSecretKey() {
        return Keys.hmacShaKeyFor(getKeyBase64Encoded().getBytes());
    }

    private String getKeyBase64Encoded() {
        return Base64.getEncoder().encodeToString(keyPlain.getBytes());
    }

    public String generateAccessToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Util.json.toMap(body);
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
