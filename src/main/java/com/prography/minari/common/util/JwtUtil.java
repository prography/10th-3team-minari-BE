package com.prography.minari.common.util;

import com.prography.minari.common.execption.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

import static com.prography.minari.common.execption.ErrorCode.*;

@Component
@Slf4j
public class JwtUtil {

    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";

    private final Key key;
    private final Long expiration;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expiration}") Long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String createAccessToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))//expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .httpOnly(true)
                .secure(true)
                .domain("www.minari.shop")
                .path("/")
                .maxAge(Duration.ofMillis(expiration))
                .sameSite("None")
                .build();
    }

    public ResponseCookie deleteAccessTokenCookie() {
        return createAccessTokenCookie(null);
    }

    public String createRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 2))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(true)
                .domain("www.minari.shop")
                .path("/")
                .maxAge(Duration.ofMillis(expiration * 2))
                .sameSite("None")
                .build();
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        return createRefreshTokenCookie(null);
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Duration getDuration(String token) {
        Date tokenExpiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return Duration.ofMillis(tokenExpiration.getTime() - System.currentTimeMillis());
    }

    public void isValidateToken(String token) throws ExpiredJwtException {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch(SignatureException e) {
            log.info("유효하지 않은 서명입니다. JWT: {}", token);
            throw new ApiException(JWT_INVALID_SIGNATURE_EXCEPTION);
        } catch(UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 포맷입니다. JWT: {}", token);
            throw new ApiException(JWT_UNSUPPORT_FORMAT_EXCEPTION);
        } catch(MalformedJwtException e) {
            log.info("잘못된 JWT 형식입니다. JWT: {}", token);
            throw new ApiException(JWT_WRONG_FORM_EXCEPTION);
        } catch(IllegalStateException e) {
            log.info("JWT 파싱 중 예상치 못한 상태 오류가 발생했습니다. 설정 또는 키 값이 올바른지 확인하세요. JWT: {}", token);
            throw new ApiException(JWT_EXCEPTION);
        }
    }
}
