package com.claro.amx.sp.apigatewayservice.config;

import io.lettuce.core.metrics.CommandLatencyCollectorOptions;
import io.lettuce.core.metrics.DefaultCommandLatencyCollector;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.time.Duration;
import java.util.List;

/**
 * Configuración de conexion a Redis en cluster
 * MaxIdle y Maxtotal que tenga el mismo valor
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisConfig {

    private List<String> nodes;
    private String userName;
    private String password;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private int maxWaitMillis;

    /**
     * Configuracion con pool
     * @return
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){

        RedisClusterConfiguration clusterConfRedis =
                new RedisClusterConfiguration(
                        nodes);
        clusterConfRedis.setUsername(userName);
        clusterConfRedis.setPassword(password);

        //Disabling command latency metrics
        ClientResources clientResources = DefaultClientResources.builder()

                .commandLatencyRecorder(

                        new DefaultCommandLatencyCollector(CommandLatencyCollectorOptions.disabled())).build();
        LettucePoolingClientConfiguration lettuceConfigClient = LettucePoolingClientConfiguration
                .builder()
                .clientResources(clientResources)
                .poolConfig(buildPoolConfig()).build();

        //Disabling shareNativeConnection
        var lettuceConnFactory = new LettuceConnectionFactory(clusterConfRedis, lettuceConfigClient);
        lettuceConnFactory.setShareNativeConnection(false);
        lettuceConnFactory.afterPropertiesSet();
        return lettuceConnFactory;


    }

    public GenericObjectPoolConfig buildPoolConfig(){
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWait(Duration.ofMillis(maxWaitMillis));
        return poolConfig;

    }


}
