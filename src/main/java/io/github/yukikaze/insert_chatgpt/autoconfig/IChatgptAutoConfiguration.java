package io.github.yukikaze.insert_chatgpt.autoconfig;

import io.github.yukikaze.insert_chatgpt.service.IChatgptService;
import io.github.yukikaze.insert_chatgpt.service.IChatgptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Slf4j
@Configuration
@ConditionalOnClass(IChatgptService.class)
@EnableConfigurationProperties({IChatgptProperties.class})
public class IChatgptAutoConfiguration {
    private final IChatgptProperties iChatgptProperties;

    public IChatgptAutoConfiguration(IChatgptProperties iChatgptProperties) {
        this.iChatgptProperties = iChatgptProperties;
        log.info("insert-chatgpt-spring-boot-starter loaded");
    }
    @Bean
    @ConditionalOnMissingBean(IChatgptService.class)
    public IChatgptService ichatgptService() {
        return new IChatgptServiceImpl(iChatgptProperties);
    }
}
