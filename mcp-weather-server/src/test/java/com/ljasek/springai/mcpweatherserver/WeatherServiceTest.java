package com.ljasek.springai.mcpweatherserver;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class WeatherServiceTest {

    public static final HttpClientStreamableHttpTransport STREAMABLE_HTTP_TRANSPORT = HttpClientStreamableHttpTransport.builder("http://localhost:8080").build();
    @Autowired
    private WeatherService weatherService;

    @Test
    void testGetTemperature_integration() {
        // Arrange
        double latitude = 52.0;
        double longitude = 19.0;

        // Act
        try (var client = McpClient.sync(STREAMABLE_HTTP_TRANSPORT).build()) {
            client.initialize();

            McpSchema.CallToolResult weather = client.callTool(new McpSchema.CallToolRequest("getTemperature", Map.of("latitude", latitude, "longitude", longitude)));
            System.out.println(weather.structuredContent());    // {current={time=2025-10-31T18:30:00, interval=900, temperature_2m=6.8}}
        }
    }
}
