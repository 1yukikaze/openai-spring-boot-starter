package io.github.yukikaze.insert_chatgpt.service;

import io.github.yukikaze.insert_chatgpt.autoConfig.IChatgptProperties;
import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;
import io.github.yukikaze.insert_chatgpt.enums.HeaderTypes;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Service;

@Service
public class IChatgptServiceImpl implements IChatgptService {
    protected final String URL;
    protected final String authorization;
    protected final String openAIOrganization;
    public IChatgptServiceImpl(IChatgptProperties iChatgptProperties) {
        this.URL =iChatgptProperties.getURL();
        this.authorization = "Bearer "+iChatgptProperties.getAuthorization();
        this.openAIOrganization = iChatgptProperties.getOpenAIOrganization();
    }

    @Override
    public ListModelsResponse listModels()
     {
        try (Client client = ClientBuilder.newClient()) {
            return client.target(URL)
                    .path("/models")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .get(ListModelsResponse.class);
        }
    }
}
