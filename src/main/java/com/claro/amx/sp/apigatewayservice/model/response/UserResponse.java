package com.claro.amx.sp.apigatewayservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    protected String reason;
    protected String code;
    protected String level;
    protected String service;
    protected String message;
}
