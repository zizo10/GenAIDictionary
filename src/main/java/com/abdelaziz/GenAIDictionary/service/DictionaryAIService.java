package com.abdelaziz.GenAIDictionary.service;

import com.abdelaziz.GenAIDictionary.dto.WordDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class DictionaryAIService {

    private final OpenAiChatModel chatModel;

    @Autowired
    public DictionaryAIService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public WordDefinition getDefinitionJson(String word,
                                String fromLanguage,
                                String toLanguage) {
        // Logic to interact with the AI model to get the definition of the word
        String promptMessage = """
                "What is the {toLanguage} translation, definition and synonyms of {fromLanguage} word: {word}?
                if you don't know the answer, just say 'I don't know'.
                parse the response and return the definition, synonyms and translation as following format
                {format}
                """;
        var outputConverter = new BeanOutputConverter(WordDefinition.class);
        PromptTemplate promptTemplate = new PromptTemplate(promptMessage);
        Prompt prompt = promptTemplate.create(Map.of("toLanguage", toLanguage, "fromLanguage", fromLanguage,
                "word", word, "format", outputConverter.getFormat()));
        log.info("Generated prompt: {}", prompt.getContents());
        ChatResponse response = chatModel.call(prompt);
        return (WordDefinition) outputConverter.convert(response.getResult().getOutput().getText());
    }
}
