package io.github.yukikaze.insert_chatgpt.dto.chat;

import io.github.yukikaze.insert_chatgpt.dto.completions.Usage;
import lombok.Data;

import java.util.List;

@Data
public class ChatResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;
}
