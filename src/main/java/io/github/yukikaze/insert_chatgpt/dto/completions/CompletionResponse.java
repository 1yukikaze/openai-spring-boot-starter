package io.github.yukikaze.insert_chatgpt.dto.completions;

import lombok.Data;

import java.util.List;

@Data
public class CompletionResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
}
