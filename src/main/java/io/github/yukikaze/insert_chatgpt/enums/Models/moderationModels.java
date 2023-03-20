package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/moderations
 */
public enum moderationModels {
    TEXT_MODERATION_STABLE("text-moderation-stable"),
    MODERATION_LATEST("moderation-latest");

    private final String models;

    moderationModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
