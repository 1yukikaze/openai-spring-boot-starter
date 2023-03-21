package io.github.yukikaze.insert_chatgpt.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Choice {
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
    private Integer index;
}
