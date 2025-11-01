package com.ljasek.springai.mcpweatherclient;


import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.LoggerFactory;
import org.springaicommunity.mcp.annotation.McpLogging;
import org.springaicommunity.mcp.annotation.McpProgress;
import org.springaicommunity.mcp.annotation.McpSampling;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class McpClientHandlers {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(McpClientHandlers.class);
    public static final String WEATHER_SERVER = "my-weather-server";

    private final ChatClient chatClient;

    public McpClientHandlers(@Lazy ChatClient chatClient) { // Lazy is needed to avoid circular dependency
        this.chatClient = chatClient;
    }

    @McpProgress(clients = WEATHER_SERVER)
    public void progressHandler(McpSchema.ProgressNotification progressNotification) {
        log.info("MCP PROGRESS: [{}] progress: {} total: {} message: {}",
                progressNotification.progressToken(), progressNotification.progress(),
                progressNotification.total(), progressNotification.message());
    }

    @McpLogging(clients = WEATHER_SERVER)
    public void loggingHandler(McpSchema.LoggingMessageNotification loggingMessage) {
        log.info("MCP LOGGING: [{}] {}", loggingMessage.level(), loggingMessage.data());
    }

    @McpSampling(clients = WEATHER_SERVER)
    public McpSchema.CreateMessageResult samplingHandler(McpSchema.CreateMessageRequest llmRequest) {
        log.info("MCP SAMPLING: {}", llmRequest);

        String llmResponse = chatClient
                .prompt()
                .system(llmRequest.systemPrompt())
                .user(((McpSchema.TextContent) llmRequest.messages().get(0).content()).text())
                .call()
                .content();

        return McpSchema.CreateMessageResult.builder().content(new McpSchema.TextContent(llmResponse)).build();
    }
}
