package com.abdelaziz.GenAIDictionary.controller;

import com.abdelaziz.GenAIDictionary.dto.WordDefinition;
import com.abdelaziz.GenAIDictionary.service.DictionaryAIService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DictionaryController {

     private final DictionaryAIService dictionaryAIService;

    @GetMapping("/definition/{word}")
    public ResponseEntity<WordDefinition> getDefinition(@PathVariable String word,
                                                        @RequestParam(required = false) String fromLanguage,
                                                        @RequestParam(required = false, defaultValue = "English") String toLanguage) {
        return ResponseEntity.ok(dictionaryAIService.getDefinitionJson(word, fromLanguage, toLanguage));
    }


@GetMapping("/definition/{word}/image")
    public ResponseEntity<String> getDefinitionVisual(@PathVariable String word,
                                                        @RequestParam(required = true) String fromLanguage,
                                                        @RequestParam(required = false, defaultValue = "English") String toLanguage) {
        return ResponseEntity.ok(dictionaryAIService.getDefinitionVisual(word, fromLanguage));
    }



    // You can also add methods for adding words, updating definitions, etc.
}
