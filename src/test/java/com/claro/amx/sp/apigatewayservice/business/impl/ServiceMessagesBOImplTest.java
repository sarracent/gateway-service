package com.claro.amx.sp.apigatewayservice.business.impl;

import com.claro.amx.sp.apigatewayservice.dao.ccard.ServiceMessagesDAO;
import com.claro.amx.sp.apigatewayservice.dao.ccard.ServiceMessagesRepository;
import com.claro.amx.sp.apigatewayservice.model.cache.CacheServiceMessages;
import com.claro.amx.sp.apigatewayservice.model.ccard.ServiceMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceMessagesBOImplTest {

    @Mock
    private ServiceMessagesDAO serviceMessagesDAO;

    @Mock
    private ServiceMessagesRepository serviceMessagesRepository;

    @InjectMocks
    private ServiceMessagesBOImpl serviceMessagesBO;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(serviceMessagesBO, "country", "testCountry");
    }

    @Test
    void getServiceMessages_CacheHit() {

        String path = "testPath";
        String code = "testCode";
        CacheServiceMessages cachedMessages = new CacheServiceMessages("key", new ServiceMessages());

        when(serviceMessagesRepository.findById(anyString())).thenReturn(Optional.of(cachedMessages));


        ServiceMessages result = serviceMessagesBO.getServiceMessages(path, code);


        assertNotNull(result);
        verify(serviceMessagesDAO, never()).getServiceMessagesData(anyString(), anyString());
    }

    @Test
    void getServiceMessages_CacheMiss() {

        String path = "testPath";
        String code = "testCode";
        ServiceMessages fetchedMessages = new ServiceMessages();

        when(serviceMessagesRepository.findById(anyString())).thenReturn(Optional.empty());
        when(serviceMessagesDAO.getServiceMessagesData(anyString(), anyString())).thenReturn(fetchedMessages);


        ServiceMessages result = serviceMessagesBO.getServiceMessages(path, code);


        assertNotNull(result);
        verify(serviceMessagesRepository, times(1)).save(any(CacheServiceMessages.class));
    }

    @Test
    void removeAll() {

        serviceMessagesBO.removeAll();


        verify(serviceMessagesRepository, times(1)).deleteAll();
    }

}
