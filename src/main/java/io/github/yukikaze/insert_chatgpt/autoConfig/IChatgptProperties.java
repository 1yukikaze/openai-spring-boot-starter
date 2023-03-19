package io.github.yukikaze.insert_chatgpt.autoConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "chatgpt")
public class IChatgptProperties {
    private String URL = "https://api.openai.com/v1";
    private String authorization = null;
    private String openAIOrganization = null;

}
