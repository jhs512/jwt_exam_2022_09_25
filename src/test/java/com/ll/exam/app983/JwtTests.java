package com.ll.exam.app983;

import com.ll.exam.app983.app.security.jwt.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTests {
    @Test
    @DisplayName("secretKey 생성")
    void t1() {
        JwtProvider jwtProvider = new JwtProvider();

        SecretKey secretKey = jwtProvider.getSecretKey();

        System.out.println("secretKey : " + secretKey);

        assertThat(secretKey).isNotNull();
    }

    @Test
    @DisplayName("accessToken 을 얻는다.")
    void t2() {
        JwtProvider jwtProvider = new JwtProvider();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1L);
        claims.put("name", "jim");
        claims.put("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        String accessToken = jwtProvider.generateAccessToken(claims, 60 * 60 * 5);

        System.out.println("accessToken : " + accessToken);

        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("accessToken 을 통해서 claims 를 얻을 수 있다.")
    void t3() {
        JwtProvider jwtProvider = new JwtProvider();

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("id", 1L);
        claims.put("name", "jim");
        claims.put("authorities", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        String accessToken = jwtProvider.generateAccessToken(claims, 60 * 60 * 5);

        System.out.println("accessToken : " + accessToken);

        assertThat(jwtProvider.verify(accessToken)).isTrue();

        Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);

        System.out.println("claimsFromToken : " + claimsFromToken);
    }
}
