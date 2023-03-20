package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/audio/transcriptions
 */
public enum transcriptionModels {
    WHISPER_1("whisper-1");

    private final String models;

    transcriptionModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
