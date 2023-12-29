package com.claro.amx.sp.apigatewayservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.ERROR_LEVEL;


@Getter
@AllArgsConstructor
public enum Errors {
    ERROR_BADREQUEST_GENERAL("100400", "No se puede procesar la informacion entrante - Field: %s", ERROR_LEVEL, "No se puede procesar la informacion entrante"),
    ERROR_BADREQUEST_FAILHEADER_REQUIRED("100401", "El campo %s es requerido en el Header para procesar su consulta", ERROR_LEVEL, "Campo Requerido en el Header"),
    ERROR_BADREQUEST_UNAUTHORIZED_ORIGIN("100402", "El origin %s no esta autorizado", ERROR_LEVEL, "Origin no Autorizado"),

    ERROR_TOKEN_INVALID("100101", "El Token: %s no es válido", ERROR_LEVEL, "Token Inválido"),
    ERROR_PARSING_TOKEN("100102", "No se pudo extraer el contenido del Token: %s", ERROR_LEVEL, "No se puede parsear el Token"),
    ERROR_EXPIRED_TOKEN("100103", "El Token: %s ha expirado", ERROR_LEVEL, "Token expirado"),

    ERROR_DATABASE_SERVICE_MESSAGE("200001", "Error al consultar la base de datos CCARD ServiceMessagesDAO -> [%s] %s", ERROR_LEVEL, "Error al consultar la base de datos CCARD "),
    ERROR_DATABASE_TIMEOUT_SERVICE_MESSAGES("200002", "Timeout - %s segundos - <query>", ERROR_LEVEL, "Timeout al ejecutar query"),
    ERROR_DATABASE_SERVICE_MESSAGES_NOT_FOUND("200003", "Error no se encontraron registros en SP_SERVICE_MESSAGES con el valor %s", ERROR_LEVEL, "No se encontraron registros en SP_SERVICE_MESSAGES"),
    ERROR_DATABASE_SERVICE_MESSAGES_NOT_VALUE("200004", "Error no se encontraron datos cargados en SP_SERVICE_MESSAGES para el path %s y el code %s", ERROR_LEVEL, "No se encontraron datos cargados en SP_SERVICE_MESSAGES"),

    ERROR_GENERAL("900000","ERROR - %s",ERROR_LEVEL, "Error General");

    private final String code;
    private final String reason;
    private final String level;
    private final String message;
}