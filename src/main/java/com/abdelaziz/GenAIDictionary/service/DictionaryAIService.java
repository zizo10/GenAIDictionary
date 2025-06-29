package com.abdelaziz.GenAIDictionary.service;

import com.abdelaziz.GenAIDictionary.dto.WordDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.PromptUserSpec;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.image.*;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DictionaryAIService {

    private final ChatClient chatClient;
    private final OpenAiImageModel imageModel;


    @Autowired
    public DictionaryAIService(ChatClient.Builder chatClientBuilder, OpenAiImageModel imageModel) {
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    public WordDefinition getDefinitionJson(String word,
                                            String fromLanguage,
                                            String toLanguage) {
        // Logic to interact with the AI model to get the definition of the word
        String promptMessage = """
                "What is the {toLanguage} translation, definition and synonyms of {fromLanguage} word: {word}?
                if you don't know the answer, just say 'I don't know'.
                parse the response and return the definition, synonyms and translation
                """;
        WordDefinition wordDefinition = chatClient.prompt()
                .user((PromptUserSpec spec) -> {
                    spec.text(promptMessage);
                    spec.params(Map.of("toLanguage", toLanguage, "fromLanguage", fromLanguage, "word", word));
                }).call().entity(WordDefinition.class);
        return wordDefinition;
    }

    public String getDefinitionVisual(String word,
                                      String fromLanguage) {
        String promptMessage = """
                Generate clear image for the Object this word "{word}" Stands for or describes
                """;
        PromptTemplate promptTemplate = new PromptTemplate(promptMessage);
        Prompt prompt = promptTemplate.create(Map.of(    "word", word));
        String msg= prompt.getContents();
        var imageResponse = imageModel.call(new ImagePrompt(msg, OpenAiImageOptions.builder()
                .quality("hd")
                .N(1)
                .height(1024)
                .width(1024)
                .model("gemini-2.0-flash-preview-image-generation")
                .responseFormat("url")
                .build()));
        return imageResponse.getResult().getOutput().getUrl();
    }
}
