package io.github.yukikaze.insert_chatgpt.dto.Listmodels;

import lombok.Data;

import java.util.List;


@Data
public class ListModelsResponse {
    private List<io.github.yukikaze.insert_chatgpt.dto.Listmodels.Data> data;
    private String object;

}
