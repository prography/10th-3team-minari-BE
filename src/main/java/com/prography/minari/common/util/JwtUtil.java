package com.prography.minari.common.util;

import com.prography.minari.common.execption.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") Long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void isValidateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch(ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다.");
            throw new ApiException("JWT001");
        } catch(SignatureException e) {
            log.warn("유효하지 않은 서명입니다.");
            throw new ApiException("JWT002");
        } catch(UnsupportedJwtException e) {
            log.warn("지원하지 않는 JWT 포맷입니다.");
            throw new ApiException("JWT003");
        } catch(MalformedJwtException e) {
            log.warn("잘못된 JWT 형식입니다.");
            throw new ApiException("JWT004");
        } catch(IllegalStateException e) {
            log.warn("JWT 파싱 중 예상치 못한 상태 오류가 발생했습니다. 설정 또는 키 값이 올바른지 확인하세요.");
            throw new ApiException("JWT005");
        }
    }
}
