package io.github.yukikaze.insert_chatgpt.service.client.impl;

import io.github.yukikaze.insert_chatgpt.autoconfig.IChatgptProperties;
import io.github.yukikaze.insert_chatgpt.dto.chat.ChatRequest;
import io.github.yukikaze.insert_chatgpt.dto.chat.ChatResponse;
import io.github.yukikaze.insert_chatgpt.dto.chat.Choice;
import io.github.yukikaze.insert_chatgpt.dto.chat.Message;
import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionRequest;
import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionResponse;
import io.github.yukikaze.insert_chatgpt.dto.listmodels.Data;
import io.github.yukikaze.insert_chatgpt.dto.listmodels.ListModelsResponse;
import io.github.yukikaze.insert_chatgpt.dto.publicerror.ErrorResponse;
import io.github.yukikaze.insert_chatgpt.enums.HeaderTypes;
import io.github.yukikaze.insert_chatgpt.exception.ChatgptException;
import io.github.yukikaze.insert_chatgpt.service.client.IChatgptClient;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
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
        this.authorization = "Bearer " + iChatgptProperties.getAuthorization();
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
            log.error(errorResponse.toString());
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
    public Data retrieveModel(String modelId) {
        log.info("Start processing the request:{}",modelId);
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/models/" + modelId)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildGet().submit()
                    .get(20L, SECONDS);
            Response responseOK = throwErrorResponse(response);
            log.info("chatgpt retrieveModel success code:{},data:{}", responseOK.getStatus(), responseOK.getDate());
            Data data = responseOK.readEntity(Data.class);
            responseOK.close();
            return data;
        } catch (Exception e) {
            throw new ChatgptException(e);
        }
    }

    @Override
    public CompletionResponse getCompletions(CompletionRequest request) {
        log.info("Start processing the request:{}",request);
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
    public LinkedBlockingQueue<CompletionResponse> getCompletionsStream(CompletionRequest request) {
        log.info("Start processing the request:{}",request);
        if (request.getModel() == null) throw new ChatgptException("error:no model,You must select the model to use.");
        if (request.getStream()) {
            Client client = ClientBuilder.newBuilder().register(SseFeature.class).build();
            EventInput eventInput = client.target(URL).path("/completions")
                    .request(MediaType.APPLICATION_JSON)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .readEntity(EventInput.class);
            LinkedBlockingQueue<CompletionResponse> queue = new LinkedBlockingQueue<>();
            while (!eventInput.isClosed()) {
                InboundEvent read = eventInput.read();
                if (read == null) {
                    throw new ChatgptException("The input data is invalid, please check the configuration.");
                }
                if ("[DONE]".equals(read.readData())) {
                    eventInput.close();
                    break;
                }
                CompletionResponse readData = read.readData(CompletionResponse.class, MediaType.APPLICATION_JSON_TYPE);
                queue.add(readData);
            }
            return queue;
        } else {
            throw new ChatgptException("warn:The stream is not open, please switch to the 'getCompletions' method.");
        }
    }

    @Override
    public ChatResponse getChat(ChatRequest request) {
        log.info("Start processing the request:{}",request);
        String messageUser = null;
        if (request.getMessageUser() != null) {
            messageUser = request.getMessageUser();
            request.setMessageUser(null);
        }
        if (request.getModel() == null) throw new ChatgptException("error:no model,You must select the model to use.");
        if (request.getMessages() == null)
            throw new ChatgptException("error:no message,You must add a message for the conversation.");
        if (request.getStream())
            throw new ChatgptException("warn:Please use the 'getChatStream' method to start a stream.");
        try (Client client = ClientBuilder.newClient()) {
            Response response = client.target(URL)
                    .path("/chat/completions")
                    .request(MediaType.APPLICATION_JSON)
                    .header(HeaderTypes.AUTHORIZATION.getType(), authorization)
                    .buildPost(Entity.entity(request, MediaType.APPLICATION_JSON))
                    .submit().get(20L, SECONDS);
            Response responseOK = throwErrorResponse(response);
            log.info("chatgpt getCompletions success code:{},data:{}", responseOK.getStatus(), responseOK.getDate());
            ChatResponse chatResponse = responseOK.readEntity(ChatResponse.class);
            responseOK.close();

            // * 存储上下文
            if (messageUser != null) {
                File f = new File("ChatUserData");
                if (!f.exists()) {
                    boolean dr = f.mkdirs();
                    log.info("mkdir{}", dr);
                }
                File file = new File("ChatUserData/" + messageUser + ".yaml");
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                    if (newFile) log.info("New user file has been created.");
                }
                Yaml yaml = new Yaml();
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                options.setPrettyFlow(true);
                List<Choice> choices = chatResponse.getChoices();
                List<Message> messages = request.getMessages();
                ArrayList<Message> list = new ArrayList<>();
                Choice choice = choices.get(choices.size() - 1);
                Message reMessage = choice.getMessage();
                Message rqMessage = messages.get(messages.size() - 1);
                list.add(rqMessage);
                list.add(reMessage);
                String dump = yaml.dump(list);
                try (FileWriter fileWriter = new FileWriter(file, true)) {
                    fileWriter.write(dump);
                } catch (Exception e) {
                    throw new ChatgptException(e);
                }
            }
            return chatResponse;
        } catch (Exception e) {
            throw new ChatgptException(e);
        }
    }
}