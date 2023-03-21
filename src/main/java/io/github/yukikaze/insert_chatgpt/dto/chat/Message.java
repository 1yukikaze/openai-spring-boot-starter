package io.github.yukikaze.insert_chatgpt.dto.chat;

import lombok.Data;

@Data
public class Message {
   private String role;
   private String content;
}
