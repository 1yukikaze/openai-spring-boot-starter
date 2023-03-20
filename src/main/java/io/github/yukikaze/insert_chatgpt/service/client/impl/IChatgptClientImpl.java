package io.github.yukikaze.insert_chatgpt.service.client.impl;

import io.github.yukikaze.insert_chatgpt.autoconfig.IChatgptProperties;
import io.github.yukikaze.insert_chatgpt.dto.chaterror.ErrorResponse;
import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionRequest;
import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionResponse;
import io.github.yukikaze.insert_chatgpt.dto.listmodels.ListModelsResponse;
import io.github.yukikaze.insert_chatgpt.enums.HeaderTypes;
import io.github.yukikaze.insert_chatgpt.exception.ChatgptException;
import io.github.yukikaze.insert_chatgpt.service.client.IChatgptClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.sse.SseEventSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.util.concurrent.LinkedBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@Service
public class IChatgptClientImpl implements IChatgptClient {
    protected final String URL;
    protected final String authorization;
    protected final String openAIOrganization;

    public IChatgptClientImpl(IChatgptProperties iChatgptProperties) {
        this.URL = iChatgptProperties.getURL();
        this.authorization = "Bearer123 " + iChatgptProperties.getAuthorization();
        this.openAIOrganization = iChatgptProperties.getOpenAIOrganization();
    }

    /**
     * response异常过滤处理
     *
     * @param response response请求
     */
    public Response throwErrorResponse(Response response) {
        if (response.getStatus() != HttpURLConnection.HTTP_OK) {
            log.error("chatgpt response error code:{},data:{}", response.getStatus(), response.getDate());
            ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
            response.close();
            throw new ChatgptException(errorResponse.toString());
        }
        return response;
    }

    @Override
    public ListModelsResponse listModels() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/models")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildGet().submit()
                    .get(20L, SECONDS);
            Response responseOK = throwErrorResponse(response);
            log.info("chatgpt listModels success code:{},data:{}", responseOK.getStatus(), responseOK.getDate());
            ListModelsResponse listModelsResponse = responseOK.readEntity(ListModelsResponse.class);
            responseOK.close();
            return listModelsResponse;
        } catch (Exception e) {
            throw new ChatgptException(e);
        }
    }

    @Override
    public ListModelsResponse retrieveModel(String modelId) {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/models/" + modelId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildGet().submit()
                    .get(20L, SECONDS);
            Response responseOK = throwErrorResponse(response);
            log.info("chatgpt retrieveModel success code:{},data:{}", responseOK.getStatus(), responseOK.getDate());
            ListModelsResponse listModelsResponse = responseOK.readEntity(ListModelsResponse.class);
            responseOK.close();
            return listModelsResponse;
        } catch (Exception e) {
            throw new ChatgptException(e);
        }
    }

    @Override
    public CompletionResponse getCompletions(CompletionRequest request) {
        if (request.getModel() == null) throw new ChatgptException("error:no model,You must select the model to use.");
        if (request.getStream())
            throw new ChatgptException("warn:Please use the 'getCompletionsStream' method to start a stream.");
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/completions")
                    .request(MediaType.APPLICATION_JSON)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildPost(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .submit().get(20L, SECONDS);
            Response responseOK = throwErrorResponse(response);
            log.info("chatgpt getCompletions success code:{},data:{}", responseOK.getStatus(), responseOK.getDate());
            CompletionResponse completionResponse = responseOK.readEntity(CompletionResponse.class);
            responseOK.close();
            return completionResponse;
        } catch (Exception e) {
            throw new ChatgptException(e);
        }
    }

    @Override
    public LinkedBlockingQueue getCompletionsStream(CompletionRequest request) {
        if (request.getModel() == null) throw new ChatgptException("error:no model,You must select the model to use.");
        if (request.getStream()) {
            try (Client client = ClientBuilder.newClient()) {
                WebTarget target = (WebTarget) client.target(URL)
                        .path("/completions")
                        .request(MediaType.APPLICATION_JSON)
                        .header(HeaderTypes.AUTHORIZATION.getType(), authorization);
                SseEventSource eventSource = SseEventSource.target(target).build();
                LinkedBlockingQueue<CompletionResponse> queue = new LinkedBlockingQueue<>();
                eventSource.register(event -> {
                    if (event.readData().equals("DONE")) eventSource.close();
                    CompletionResponse response = event.readData(CompletionResponse.class);
                    try {
                        queue.put(response);
                    } catch (InterruptedException e) {
                        throw new ChatgptException(e);
                    }
                });
                eventSource.open();
                return queue;
            } catch (Exception e) {
                throw new ChatgptException(e);
            }
        } else {
            throw new ChatgptException("warn:The stream is not open, please switch to the 'getCompletions' method.");
        }
    }
}
