package com.claro.amx.sp.apigatewayservice.dao.ccard;


import com.claro.amx.sp.apigatewayservice.model.cache.CacheServiceMessages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceMessagesRepository extends CrudRepository<CacheServiceMessages, String> {

}
