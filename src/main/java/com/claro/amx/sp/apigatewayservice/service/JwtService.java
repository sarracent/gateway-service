package com.claro.amx.sp.apigatewayservice.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Boolean validateToken(String token);

    Claims extractAllClaims(String token);
}
