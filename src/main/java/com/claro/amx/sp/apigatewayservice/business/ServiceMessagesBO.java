package com.claro.amx.sp.apigatewayservice.business;


import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;

public interface ServiceMessagesBO {
    ServiceMessages getServiceMessages(String path, String code);

    void removeAll();
}
