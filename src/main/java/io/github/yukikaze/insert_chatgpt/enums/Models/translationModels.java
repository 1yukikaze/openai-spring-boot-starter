package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/audio/translations
 */
public enum translationModels {
    WHISPER_1("whisper-1");

    private final String models;

    translationModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
