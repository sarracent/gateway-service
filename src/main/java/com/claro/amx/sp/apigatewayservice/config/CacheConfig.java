package com.claro.amx.sp.apigatewayservice.config;



import com.claro.amx.sp.apigatewayservice.business.ServiceMessagesBO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CacheConfig {
    private final ServiceMessagesBO serviceMessagesBO;

    @Scheduled(cron = "${CacheConfig.cron}")
    public void clearCacheSchedule() {
        serviceMessagesBO.removeAll();
    }

}
