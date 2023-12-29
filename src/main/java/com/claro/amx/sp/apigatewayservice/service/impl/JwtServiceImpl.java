package com.claro.amx.sp.apigatewayservice.service.impl;

import com.claro.amx.sp.apigatewayservice.annotations.log.LogService;
import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import static com.claro.amx.sp.apigatewayservice.constants.Errors.*;
import static com.claro.amx.sp.apigatewayservice.constants.Errors.ERROR_EXPIRED_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${secret}")
    private String privateSecret;

    @Override
    @LogService
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    @Override
    @LogService
    public Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException e) {
            // Handle other exceptions that may occur during parsing
            throw new BusinessException(
                    String.format(ERROR_PARSING_TOKEN.getReason(),token),
                    ERROR_PARSING_TOKEN.getCode(),
                    ERROR_PARSING_TOKEN.getLevel(),
                    ERROR_PARSING_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            // Aquí manejas la excepción para el token expirado
            throw new BusinessException(
                    String.format(ERROR_EXPIRED_TOKEN.getReason(),token),
                    ERROR_EXPIRED_TOKEN.getCode(),
                    ERROR_EXPIRED_TOKEN.getLevel(),
                    ERROR_EXPIRED_TOKEN.getMessage());
        } catch (Exception e) {
            // Handle signature verification failure (e.g., invalid token)
            throw new BusinessException(
                    String.format(ERROR_TOKEN_INVALID.getReason(),token),
                    ERROR_TOKEN_INVALID.getCode(),
                    ERROR_TOKEN_INVALID.getLevel(),
                    ERROR_TOKEN_INVALID.getMessage());
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Key getSignKey() {
        byte[] keyBytes= privateSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
