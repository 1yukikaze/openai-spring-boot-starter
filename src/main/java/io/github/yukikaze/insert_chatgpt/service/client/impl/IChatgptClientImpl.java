package io.github.yukikaze.insert_chatgpt.service.client.impl;

import io.github.yukikaze.insert_chatgpt.autoconfig.IChatgptProperties;
import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;
import io.github.yukikaze.insert_chatgpt.dto.chaterror.ErrorResponse;
import io.github.yukikaze.insert_chatgpt.enums.HeaderTypes;
import io.github.yukikaze.insert_chatgpt.exception.ChatgptException;
import io.github.yukikaze.insert_chatgpt.service.client.IChatgptClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class IChatgptClientImpl implements IChatgptClient {
    protected final String URL;
    protected final String authorization;
    protected final String openAIOrganization;
    public IChatgptClientImpl(IChatgptProperties iChatgptProperties) {
        this.URL =iChatgptProperties.getURL();
        this.authorization = "Bearer123 "+iChatgptProperties.getAuthorization();
        this.openAIOrganization = iChatgptProperties.getOpenAIOrganization();
    }

    /**
     * response异常处理
     * @param response response请求
     */
    public void throwErrorResponse(Response response) {
        log.error("chatgpt response error code:{},data:{}" ,response.getStatus() , response.getDate());
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        response.close();
        throw new ChatgptException(errorResponse.toString());
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
            if (response.getStatus() != HttpURLConnection.HTTP_OK) throwErrorResponse(response);
            log.info("chatgpt listModels success code:{},data:{}",response.getStatus(),response.getDate());
            ListModelsResponse listModelsResponse = response.readEntity(ListModelsResponse.class);
            response.close();
            return listModelsResponse;
        } catch (Exception e) {
            log.error("chatgpt listModels error:",e);
            throw new ChatgptException(e);
        }
    }

    @Override
    public ListModelsResponse retrieveModel(String modelId) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/models/"+modelId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildGet().submit()
                    .get(20L, SECONDS);
            if (response.getStatus() != HttpURLConnection.HTTP_OK) throwErrorResponse(response);
            log.info("chatgpt retrieveModel success code:{},data:{}",response.getStatus(),response.getDate());
            ListModelsResponse listModelsResponse = response.readEntity(ListModelsResponse.class);
            response.close();
            return listModelsResponse;
        }catch (Exception e) {
            log.error("chatgpt retrieveModel error:",e);
            throw new ChatgptException(e);
        }
    }
}
