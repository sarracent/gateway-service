package com.claro.amx.sp.apigatewayservice.model.cache;

import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.SERVICE_MESSAGES_CACHE;
import static com.claro.amx.sp.apigatewayservice.constants.Constants.TIME_TO_LIVE;

@Data
@AllArgsConstructor
@Getter
@Builder
@RedisHash(value= SERVICE_MESSAGES_CACHE, timeToLive = TIME_TO_LIVE)
public class CacheServiceMessages {
    @Id
    String key;
    ServiceMessages serviceMessages;
}
