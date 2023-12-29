package com.claro.amx.sp.apigatewayservice.dao.ccard;

import com.claro.amx.sp.apigatewayservice.config.MyBatisConfig;
import com.claro.amx.sp.apigatewayservice.log.LogDAO;
import com.claro.amx.sp.apigatewayservice.mapper.ccard.ServiceMessagesMapper;
import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ServiceMessagesDAO {

    @Qualifier(MyBatisConfig.CCARD_SESSION_FACTORY)
    private final SqlSessionFactoryBean sessionFactoryBean;

    @Value("${service.messages.dao.timeout}")
    private Integer timeout;

    @LogDAO
    public ServiceMessages getServiceMessagesData(String path, String code) {
        try (var sqlSession = Objects.requireNonNull(sessionFactoryBean.getObject()).openSession()) {
            sqlSession.getConfiguration().setDefaultStatementTimeout(timeout);
            return sqlSession.getMapper(ServiceMessagesMapper.class).getServiceMessagesData(path, code);
        }
        catch (PersistenceException ex) {
            return ServiceMessages.builder().build();
        }
        catch (Exception e) {
            return ServiceMessages.builder().build();
        }
    }
}
