package io.github.yukikaze.insert_chatgpt.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.yukikaze.insert_chatgpt.config.DoubleContextualSerializer;
import io.github.yukikaze.insert_chatgpt.config.Precision;
import io.github.yukikaze.insert_chatgpt.exception.ChatgptException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class ChatRequest {
    public ChatRequest() {
        this.messages = new ArrayList<>();
    }

    /**
     * 设置上下文消息的用户 解决上下文冲突
     * 开启上下文会生成 yaml文件保存用户聊天记录
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String messageUser;

    /**
     * 删除用户上下文(相当于结束当前对话)
     */
    public void deleteUserData(String messageUser) {
        File file = new File("ChatUserData/" + messageUser + ".yaml");
        if (!file.exists()) {
            log.error("No current user file.");
            return;
        }
        boolean delete = file.delete();
        if (delete) log.info("The conversation with user {} has been closed.", messageUser);
    }

    /**
     * 设置Chat预设(给AI加入对话风格)
     *
     * @param systemMessage 预设消息字符串
     */
    public void setSystemMessage(String systemMessage) {
        if (this.messages == null) this.messages = new ArrayList<>();
        Message m = new Message();
        m.setRole("system");
        m.setContent(systemMessage);
        this.messages.add(m);
    }

    /**
     * 发送一条消息
     *
     * @param message 对话消息字符串
     */
    public void sendMessage(String message) {
        if (this.messages == null) this.messages = new ArrayList<>();
        Message m = new Message();
        m.setRole("user");
        m.setContent(message);
        this.messages.add(m);
    }

    /**
     * 关联上下文的状态下发送一条消息 (默认关联一组上下文)
     *
     * @param message     对话消息字符串
     * @param messageUser 开启上下文->设置用户
     */
    public void sendMessage(String message, String messageUser) {
        if (!messageUser.matches("^[a-zA-Z0-9]*$")) {
            throw new ChatgptException("'messageUser' can only contain letters and numbers.");
        }
        this.messageUser = messageUser;
        File file = new File("ChatUserData/" + messageUser + ".yaml");
        if (!file.exists()) {
            this.sendMessage(message);
            return;
        }
        Yaml yaml = new Yaml();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            List<Message> load = yaml.load(fileInputStream);
            if (this.messages == null) this.messages = new ArrayList<>();
            this.messages.add(load.get(load.size() - 2));
            this.messages.add(load.get(load.size() - 1));
            sendMessage(message);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 关联上下文的状态下发送一条消息 设置关联条数
     *
     * @param message     对话消息字符串
     * @param messageUser 开启上下文-?设置用户
     * @param contextNum  上下文关联组数(有可能会超过token上限)
     */
    public void sendMessage(String message, String messageUser, Integer contextNum) {
        switch (contextNum) {
            case 0 -> {
                sendMessage(message);
                return;
            }
            case 1 -> {
                sendMessage(message, messageUser);
                return;
            }
        }
        File file = new File("ChatUserData/" + messageUser + ".yaml");
        if (!file.exists()) {
            this.sendMessage(message, messageUser);
            return;
        }
        if (!messageUser.matches("^[a-zA-Z0-9]*$")) {
            throw new ChatgptException("'messageUser' can only contain letters and numbers.");
        }
        this.messageUser = messageUser;
        Yaml yaml = new Yaml();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            List<Message> load = yaml.load(fileInputStream);
            if (load.size() < 2 * contextNum) {
                if (load.size() >= 2) {
                    log.warn("Insufficient data records, temporarily only associate one set of information.");
                    sendMessage(message, messageUser);
                } else {
                    log.warn("Insufficient data to associate with context, temporarily suspend the association.");
                    sendMessage(message);
                }
                return;
            }
            if (this.messages == null) this.messages = new ArrayList<>();
            for (int i = contextNum; i > 0; i--) {
                this.messages.add(load.get(load.size() - 2 * i));
                this.messages.add(load.get(load.size() - 2 * i + 1));
            }
            sendMessage(message);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * model
     * string
     * Required
     * ID of the model to use. See the model endpoint compatibility table for details on which models work with the Chat API.
     */
    private String model;

    /**
     * messages
     * array
     * Required
     * The messages to generate chat completions for,
     * <a href="https://platform.openai.com/docs/guides/chat/introduction">in the chat format.</a>
     */
    private List<Message> messages;

    /**
     * temperature
     * number
     * Optional
     * Defaults to 1
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random, while lower values like 0.2 will make it more focused and deterministic.
     * We generally recommend altering this or but not both.top_p
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 1)
    private Double temperature;

    /**
     * top_p
     * number
     * Optional
     * Defaults to 1
     * An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 1)
    @JsonProperty("top_p")
    private Double topP;

    /**
     * n
     * integer
     * Optional
     * Defaults to 1
     * How many chat completion choices to generate for each input message.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer n;

    /**
     * stream
     * boolean
     * Optional
     * Defaults to false
     * If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent as data-only server-sent events as they become available, with the stream terminated by a message. See the OpenAI Cookbook for example code.data: [DONE]
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean stream = false;

    /**
     * stop
     * string or array
     * Optional
     * Defaults to null
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> stop;

    /**
     * max_tokens
     * integer
     * Optional
     * Defaults to inf
     * The maximum number of tokens to generate in the chat completion.
     * The total length of input tokens and generated tokens is limited by the model's context length.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * presence_penalty
     * number
     * Optional
     * Defaults to 0
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 1)
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * frequency_penalty
     * number
     * Optional
     * Defaults to 0
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = DoubleContextualSerializer.class)
    @Precision(precision = 1)
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    // ! "Waiting for the official to fix the bug." //
    /**
     * logit_bias
     * map
     * Optional
     * Defaults to null
     * Modify the likelihood of specified tokens appearing in the completion.
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer) to an associated bias value from -100 to 100. Mathematically, the bias is added to the logits generated by the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive selection of the relevant token.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias = null;

    /**
     * user
     * string
     * Optional
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse. Learn more.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user;

}
