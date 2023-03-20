package io.github.yukikaze.insert_chatgpt.dto.completions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Choice {
    private String text;
    private Integer index;
    private Integer logprobs;
    @JsonProperty("finish_reason")
    private String finishReason;

}
