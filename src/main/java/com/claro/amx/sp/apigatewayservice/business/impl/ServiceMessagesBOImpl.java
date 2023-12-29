package com.claro.amx.sp.apigatewayservice.business.impl;

import com.claro.amx.sp.apigatewayservice.business.ServiceMessagesBO;
import com.claro.amx.sp.apigatewayservice.dao.ccard.ServiceMessagesDAO;
import com.claro.amx.sp.apigatewayservice.dao.ccard.ServiceMessagesRepository;
import com.claro.amx.sp.apigatewayservice.log.LogBO;
import com.claro.amx.sp.apigatewayservice.model.cache.CacheServiceMessages;
import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import com.claro.amx.sp.apigatewayservice.utility.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.claro.amx.sp.apigatewayservice.constants.Constants.UNDERSCORE;

@Component
@RequiredArgsConstructor
public class ServiceMessagesBOImpl implements ServiceMessagesBO {
    @Value("${country}")
    private String country;
    private final ServiceMessagesDAO serviceMessagesDAO;
    private final ServiceMessagesRepository serviceMessagesRepository;

    @Override
    @LogBO
    public ServiceMessages getServiceMessages(String path, String code) {
        //busco en la cache
        String redisKey = getKey(path, code);
        var messages =serviceMessagesRepository.findById(redisKey);
        //si existe devuelvo el obj
        if (messages.isPresent()) {
            return messages.get().getServiceMessages();
        }
        var serviceMessages = serviceMessagesDAO.getServiceMessagesData(path, code);

        serviceMessages = ServiceMessages.builder()
                .level(Optional.ofNullable(serviceMessages)
                        .filter(Util::isNotNullOrEmpty) // Using method reference for generic objects
                        .map(ServiceMessages::getLevel)
                        .filter(Util::isNotNullOrEmpty) // Using method reference for strings
                        .orElse("null"))
                .service(Optional.ofNullable(serviceMessages)
                        .filter(Util::isNotNullOrEmpty)
                        .map(ServiceMessages::getService)
                        .filter(Util::isNotNullOrEmpty)
                        .orElse("null"))
                .message(Optional.ofNullable(serviceMessages)
                        .filter(Util::isNotNullOrEmpty)
                        .map(ServiceMessages::getMessage)
                        .filter(Util::isNotNullOrEmpty)
                        .orElse("null"))
                .build();

        //guardo en la cache el resultado traido de la bd.
        serviceMessagesRepository.save(new CacheServiceMessages(redisKey, serviceMessages));
        return serviceMessages;

    }

    @Override
    public void removeAll() {
        serviceMessagesRepository.deleteAll();
    }

    private String getKey(String path, String code) {
        return path.concat(UNDERSCORE + code + UNDERSCORE + country);
    }
}
