package com.claro.amx.sp.apigatewayservice.commons.resolver;

import com.claro.amx.sp.apigatewayservice.exception.impl.BusinessException;
import com.claro.amx.sp.apigatewayservice.model.response.ServiceResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.validation.ConstraintViolationException;
import java.util.Set;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.ERROR_LEVEL;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomExceptionResolverDelegateTest {

    @Test
    void buildServiceResponse() {
        Exception ex = new Exception();
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
        assertEquals("900000", serviceResponse.getUserResponse().getCode());
        assertFalse(serviceResponse.getUserResponse().getCode().contains("Error General ->"));
    }

    @Test
    void buildServiceResponseCustomException() {
        BusinessException ex = new BusinessException("100101", "El Token: %s no es válido", ERROR_LEVEL, "Message");
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
        assertEquals("100101", serviceResponse.getUserResponse().getReason());
    }

    @Test
    void buildServiceResponseExpiredJwtException() {
        Header<?> dummyHeader = null;
        Claims dummyClaims = null;
        String errorMessage = "JWT has expired";
        ExpiredJwtException ex = new ExpiredJwtException(dummyHeader, dummyClaims, errorMessage);
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(ex);
        assertEquals("100103", serviceResponse.getUserResponse().getCode());
    }

    @Test
    void buildServiceResponse2() {
        ConstraintViolationException cv = new ConstraintViolationException("No se puede procesar la informacion entrante", Set.of());
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(cv);
        assertEquals("100400", serviceResponse.getUserResponse().getCode());
    }

    @Test
    void buildServiceResponse3() {
        HttpMessageNotReadableException httpe = new HttpMessageNotReadableException("Error el campo ratingGroupList debería ser un array");
        ServiceResponse serviceResponse = CustomExceptionResolverDelegate.buildServiceResponse(httpe);
        assertEquals("900000", serviceResponse.getUserResponse().getCode());
        assertEquals(httpe.getMessage(), "Error el campo ratingGroupList debería ser un array");
    }
}