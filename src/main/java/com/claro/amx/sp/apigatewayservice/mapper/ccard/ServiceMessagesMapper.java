package com.claro.amx.sp.apigatewayservice.mapper.ccard;

import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServiceMessagesMapper {
    ServiceMessages getServiceMessagesData(String path, String code);
}
