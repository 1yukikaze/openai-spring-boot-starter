package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/chat/completions
 */
public enum chatModels {
    GPT_4("gpt-4"),
    GPT_4_0314("gpt-4-0314"),
    GPT_4_32K("gpt-4-32k"),
    GPT_4_32K_0314("gpt-4-32k-0314"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301");

    private final String models;

    chatModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
