package com.abdelaziz.GenAIDictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WordDefinition {

    private String wordToTranslation;
    private String definition;
    private List<String> synonyms;
}
