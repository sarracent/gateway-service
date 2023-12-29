package com.claro.amx.sp.apigatewayservice.model.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class ServiceResponseTest {

    @Test
     void testServiceResponseGettersAndSetters() {
        // Crear instancia usando el constructor por defecto
        ServiceResponse serviceResponse = new ServiceResponse();

        // Crear instancia de ServiceDetails para usarla como atributo
        UserResponse userResponse = new UserResponse("TestError", "TestResult", "TestLevel", "TestLevel", "TestLevel");

        // Establecer valores usando los setters
        serviceResponse.setUserResponse(userResponse);

        // Comprobar valores usando los getters
        assertNotNull(serviceResponse.getUserResponse());
        assertEquals("TestError", serviceResponse.getUserResponse().getReason());
        assertEquals("TestResult", serviceResponse.getUserResponse().getCode());
        assertEquals("TestLevel", serviceResponse.getUserResponse().getLevel());
    }

    @Test
     void testServiceResponseAllArgsConstructor() {
        UserResponse userResponse = new UserResponse("TestError", "TestResult", "TestLevel", "TestLevel", "TestLevel");

        ServiceResponse serviceResponse = new ServiceResponse(userResponse);

        // Comprobar valores usando los getters
        assertNotNull(serviceResponse.getUserResponse());
        assertEquals("TestError", serviceResponse.getUserResponse().getReason());
        assertEquals("TestResult", serviceResponse.getUserResponse().getCode());
        assertEquals("TestLevel", serviceResponse.getUserResponse().getLevel());
    }

    @Test
     void testServiceResponseBuilder() {
        UserResponse userResponse = UserResponse.builder()
                .reason("TestError")
                .code("TestResult")
                .level("TestLevel")
                .message("TestLevel")
                .build();

        ServiceResponse serviceResponse = ServiceResponse.builder()
                .userResponse(userResponse)
                .build();

        // Comprobar valores usando los getters
        assertNotNull(serviceResponse.getUserResponse());
        assertEquals("TestError", serviceResponse.getUserResponse().getReason());
        assertEquals("TestResult", serviceResponse.getUserResponse().getCode());
        assertEquals("TestLevel", serviceResponse.getUserResponse().getLevel());
    }
}
