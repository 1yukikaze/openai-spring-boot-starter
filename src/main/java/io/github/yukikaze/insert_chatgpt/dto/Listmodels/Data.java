package io.github.yukikaze.insert_chatgpt.dto.Listmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@lombok.Data
public class Data {
    private String id;
    private String object;
    private Integer created;
    @JsonProperty("owned_by")
    private String ownedBy;
    private List<Permission> permission;
    private String root;
    private Boolean parent;
}
