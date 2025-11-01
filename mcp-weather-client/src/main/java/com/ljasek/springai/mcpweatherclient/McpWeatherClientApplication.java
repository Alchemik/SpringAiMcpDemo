package com.ljasek.springai.mcpweatherclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class McpWeatherClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpWeatherClientApplication.class, args).close();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    String userPrompt = """
            Check the weather in Amsterdam right now and show the creative response!
            Please incorporate all creative responses from all LLM providers.
            Then search online to find publishers for poetry and list top 3.
            """;

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient chatClient, ToolCallbackProvider mcpToolProvider) {
        return args -> System.out.println(
                chatClient.prompt(userPrompt)
                        .toolContext(Map.of("progressToken", "token-" + new Random().nextInt()))
                        .toolCallbacks(mcpToolProvider)
                        .call()
                        .content());
    }

}
