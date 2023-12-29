package com.claro.amx.sp.apigatewayservice.service.impl;

import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static com.claro.amx.sp.apigatewayservice.constants.Errors.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "privateSecret", "5367566B59703373367639792F423F6528482B4D6251655468576D5A71341708");
    }

    @Test
    void testExtractAllClaims() {
        String mockToken = getToken();

        Claims claims = jwtService.extractAllClaims(mockToken);

        assertNotNull(claims);
    }

    @Test
    void testExtractAllClaims_ErrorParsingToken_ThrowsBusinessException() {
        // Given
        String token = getTokenMalformed();

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> jwtService.extractAllClaims(token));

        // Then
        assertEquals(ERROR_PARSING_TOKEN.getCode(), exception.getCode());
        assertTrue(exception.getReason().contains("No se pudo extraer el contenido del Token:"));
        assertEquals(ERROR_PARSING_TOKEN.getLevel(), exception.getLevel());
    }

    @Test
    void testExtractAllClaims_ErrorExpiredToken_ThrowsBusinessException() {
        // Given
        String token = getExpiredToken();

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> jwtService.extractAllClaims(token));

        // Then
        assertEquals(ERROR_EXPIRED_TOKEN.getCode(), exception.getCode());
        assertEquals(ERROR_EXPIRED_TOKEN.getLevel(), exception.getLevel());
    }

    @Test
    void testExtractAllClaims_ErrorInvalidToken_ThrowsBusinessException() {
        // Given
        String token = getInvalidToken();

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> jwtService.extractAllClaims(token));

        // Then
        assertEquals(ERROR_TOKEN_INVALID.getCode(), exception.getCode());
        assertEquals(ERROR_TOKEN_INVALID.getLevel(), exception.getLevel());
    }

    @Test
    void validateToken_whenTokenIsNotExpired_shouldReturnTrue() {
        // Given
        String mockToken = getToken();

        // When
        Boolean isValid = jwtService.validateToken(mockToken);

        // Then
        assertTrue(isValid);
    }

    private String getToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJDaGFubmVsLUlkIjoicHNwIiwiU2Vzc2lvbi1JZCI6IkRQUiIsIlVzZXItSWQiOiJFWEM4NDc0NyIsImlhdCI6MTY5Njg2ODg2MiwiZXhwIjo0ODUwNDY4ODYyfQ.rpt9DjGzGAQ85zw2cCGiyD63Y7G73dIBX05sFwSmfRk";
    }

    private String getTokenMalformed() {
        return "dsdasd.sorñaslo34erWEE#R,dd";
    }

    private String getExpiredToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJDaGFubmVsLUlkIjoicHNwIiwiVXNlci1JZCI6IkVYQzg0NzQ3IiwiU2Vzc2lvbi1JZCI6IkRQUiIsImlhdCI6MTY5Njg3MDM3MCwiZXhwIjoxNjk2ODcwNDMwfQ.UPevSZpRYf4aE8CltEmG2kCZ2_XkAaT9ZOACkuSCo9M";
    }

    private String getInvalidToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiQ2hhbm5lbC1JZCI6IlBTUC02IiwiU2Vzc2lvbi1JZCI6IkRQUiIsImlhdCI6MTY5NTc2ODg3MCwiZXhwIjoxNjk1NzcwNjcwfQ.KWGsSE_6NccQXw6C_3Ep_zOAMBItyU2raQ-gQyH1i";
    }
}