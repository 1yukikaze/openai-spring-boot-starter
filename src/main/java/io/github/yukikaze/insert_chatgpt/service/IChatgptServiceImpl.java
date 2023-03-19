package io.github.yukikaze.insert_chatgpt.service;

import io.github.yukikaze.insert_chatgpt.autoconfig.IChatgptProperties;
import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;
import io.github.yukikaze.insert_chatgpt.dto.chaterror.Error;
import io.github.yukikaze.insert_chatgpt.dto.chaterror.ErrorResponse;
import io.github.yukikaze.insert_chatgpt.enums.HeaderTypes;
import io.github.yukikaze.insert_chatgpt.exception.ChatgptException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class IChatgptServiceImpl implements IChatgptService {
    protected final String URL;
    protected final String authorization;
    protected final String openAIOrganization;
    public IChatgptServiceImpl(IChatgptProperties iChatgptProperties) {
        this.URL =iChatgptProperties.getURL();
        this.authorization = "Bearer123 "+iChatgptProperties.getAuthorization();
        this.openAIOrganization = iChatgptProperties.getOpenAIOrganization();
    }

    @Override
    public ListModelsResponse listModels()
     {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/models")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildGet().submit()
                    .get(20L, SECONDS);

            if (response.getStatus() != 200){
                log.error("chatgpt response error code:{},data:{}" ,response.getStatus() , response.getDate());
                ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
                response.close();
                throw new ChatgptException(errorResponse.toString());
            }
            log.info("chatgpt response success code:{},data:{}",response.getStatus(),response.getDate());
            ListModelsResponse listModelsResponse = response.readEntity(ListModelsResponse.class);
            response.close();
            return listModelsResponse;
        } catch (Exception e) {
            log.error("chatgpt listModels error",e);
            throw new RuntimeException(e);
        }
    }
}
