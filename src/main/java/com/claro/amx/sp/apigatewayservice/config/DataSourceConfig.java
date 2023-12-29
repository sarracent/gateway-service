package com.claro.amx.sp.apigatewayservice.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
   static final String CCARD_DATASOURCE = "CCARDDS";

    @Bean(name = CCARD_DATASOURCE, destroyMethod = "")
    @ConfigurationProperties(prefix = "spring.ccard.datasource")
    public DataSource dataSourceCCARD() {
        return getDataSource();
    }

    private DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

}
