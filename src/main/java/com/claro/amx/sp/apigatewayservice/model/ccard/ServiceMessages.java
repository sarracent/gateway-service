package com.claro.amx.sp.apigatewayservice.model.ccard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(hidden = true)
public class ServiceMessages {
    private String level;
    private String service;
    private String message;
}
