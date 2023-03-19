package io.github.yukikaze.insert_chatgpt.dto.chaterror;

import lombok.Data;

@Data
public class Error {
    private String message;
    private String type;
    private Boolean param;
    private Boolean code;


}
