package io.github.yukikaze.insert_chatgpt.dto.listmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Permission {
    private String id;
    private String object;
    private Integer created;
    @JsonProperty("allow_create_engine")
    private Boolean allowCreateEngine;
    @JsonProperty("allow_sampling")
    private Boolean allowSampling;
    @JsonProperty("allow_logprobs")
    private Boolean allowLogprobs;
    @JsonProperty("allow_search_indices")
    private Boolean allowSearchIndices;
    @JsonProperty("allow_view")
    private Boolean allowView;
    @JsonProperty("allow_fine_tuning")
    private Boolean allowFineTuning;
    private String organization;
    private String group;
    @JsonProperty("is_blocking")
    private Boolean isBlocking;
}
