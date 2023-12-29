package com.claro.amx.sp.apigatewayservice.config;


import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
@MapperScan(value = "com.claro.amx.sp.apigatewayservice.mapper.ccard", sqlSessionFactoryRef = MyBatisConfig.CCARD_SESSION_FACTORY)
public class MyBatisConfig {

    public static final String CCARD_SESSION_FACTORY = "ccardSessionFactory";

    @Bean(name = CCARD_SESSION_FACTORY, destroyMethod = "")
    public SqlSessionFactoryBean ccardSQLSessionFactory(@Qualifier(DataSourceConfig.CCARD_DATASOURCE) final DataSource dataSource) {
        final var sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

}



