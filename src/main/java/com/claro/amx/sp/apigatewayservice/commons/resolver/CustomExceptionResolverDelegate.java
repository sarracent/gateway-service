package com.claro.amx.sp.apigatewayservice.commons.resolver;

import com.claro.amx.sp.apigatewayservice.exception.CustomException;
import com.claro.amx.sp.apigatewayservice.model.response.ServiceResponse;
import com.claro.amx.sp.apigatewayservice.model.response.UserResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.SERVICE;
import static com.claro.amx.sp.apigatewayservice.constants.Errors.*;

@Component
@RequiredArgsConstructor
public class CustomExceptionResolverDelegate {

    public static ServiceResponse buildServiceResponse(Throwable ex) {
        if (ex instanceof CustomException) {
            var iCustomException = (CustomException) ex;
            return ServiceResponse.builder()
                    .userResponse(
                            UserResponse.builder()
                                    .reason(iCustomException.getReason())
                                    .code(iCustomException.getCode())
                                    .level(iCustomException.getLevel())
                                    .service(SERVICE)
                                    .message(iCustomException.getMessage())
                                    .build()
                    )
                    .build();
        } else if (ex instanceof ExpiredJwtException) {
            var expiredJwtException = (ExpiredJwtException) ex;
            return ServiceResponse.builder()
                    .userResponse(
                            UserResponse.builder()
                                    .reason(String.format(ERROR_EXPIRED_TOKEN.getReason(), expiredJwtException.getMessage()))
                                    .code(ERROR_EXPIRED_TOKEN.getCode())
                                    .level(ERROR_EXPIRED_TOKEN.getLevel())
                                    .service(SERVICE)
                                    .message(ERROR_EXPIRED_TOKEN.getMessage())
                                    .build()
                    )
                    .build();
        }
        else if (ex instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;

            List<String> errors = new ArrayList<>();

            constraintViolationException.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

            return ServiceResponse.builder()
                    .userResponse(
                            UserResponse.builder()
                                    .reason(String.format(ERROR_BADREQUEST_GENERAL.getReason(), constraintViolationException.getMessage()))
                                    .code(ERROR_BADREQUEST_GENERAL.getCode())
                                    .level(ERROR_BADREQUEST_GENERAL.getLevel())
                                    .service(SERVICE)
                                    .message(ERROR_BADREQUEST_GENERAL.getMessage())
                                    .build()
                    )
                    .build();

        }
        else {
            return ServiceResponse.builder()
                    .userResponse(
                            UserResponse.builder()
                                    .reason(String.format(ERROR_GENERAL.getReason(), ex.getMessage()))
                                    .code(ERROR_GENERAL.getCode())
                                    .level(ERROR_GENERAL.getLevel())
                                    .service(SERVICE)
                                    .message(ERROR_GENERAL.getMessage())
                                    .build()
                    )
                    .build();
        }
    }

}