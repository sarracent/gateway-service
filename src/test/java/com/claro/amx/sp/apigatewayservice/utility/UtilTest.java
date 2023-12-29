package com.claro.amx.sp.apigatewayservice.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @BeforeEach
    public void setup() {
        ThreadContext.clearAll(); // Ensure ThreadContext is clean before each test
    }

    @AfterEach
    public void tearDown() {
        ThreadContext.clearAll(); // Ensure ThreadContext is clean after each test
    }

    @Test
    void testGetMapperJson() {
        ObjectMapper mapper = Util.getMapperJson();
        assertNotNull(mapper);
        assertFalse(mapper.getSerializationConfig().hasSerializationFeatures(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS.getMask()));
    }

    @Test
    void testGenerateUniqueId_WithoutParam() {
        String uniqueId = Util.generateUniqueId(null);
        assertNotNull(uniqueId);
        assertEquals(32, uniqueId.length());
    }

    @Test
    void testGenerateUniqueId_WithParam() {
        String uniqueId = Util.generateUniqueId("Test");
        assertNotNull(uniqueId);
        assertTrue(uniqueId.endsWith("-Test"));
    }

    @Test
    void testIsNullOrEmpty_String() {
        assertTrue(Util.isNullOrEmpty((String) null));
        assertTrue(Util.isNullOrEmpty(""));
        assertFalse(Util.isNullOrEmpty("test"));
    }

    @Test
    void testToJSONString_ObjectNotNull() {
        String jsonString = Util.toJSONString(new Token("DPR", "PSP"));
        assertNotNull(jsonString);
        assertFalse(jsonString.contains("John"));
    }

    @Test
    void testToJSONString_ObjectNull() {
        assertNull(Util.toJSONString(null));
    }

}

class Token {
    private String sessionId;
    private String channelId;

    public Token(String sessionId, String channelId) {
        this.sessionId = sessionId;
        this.channelId = channelId;
    }

}