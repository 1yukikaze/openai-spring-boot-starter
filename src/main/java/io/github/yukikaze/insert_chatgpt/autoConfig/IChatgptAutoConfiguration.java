package io.github.yukikaze.insert_chatgpt.autoConfig;

import io.github.yukikaze.insert_chatgpt.service.IChatgptService;
import io.github.yukikaze.insert_chatgpt.service.IChatgptServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(IChatgptService.class)
@EnableConfigurationProperties({IChatgptProperties.class})
public class IChatgptAutoConfiguration {
    private final IChatgptProperties iChatgptProperties;

    public IChatgptAutoConfiguration(IChatgptProperties iChatgptProperties) {
        this.iChatgptProperties = iChatgptProperties;
    }
    @Bean
    @ConditionalOnMissingBean(IChatgptService.class)
    public IChatgptService ichatgptService() {
        return new IChatgptServiceImpl(iChatgptProperties);
    }
}
