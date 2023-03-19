package io.github.yukikaze.insert_chatgpt.dto.Listmodels;

import io.github.yukikaze.insert_chatgpt.dto.chaterror.Error;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class ListModelsResponse extends Error {
    private List<io.github.yukikaze.insert_chatgpt.dto.Listmodels.Data> data;
    private String object;

}
