package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/fine-tunes
 */
public enum fineTuneModels {
    DAVINCI("davinci"),
    CURIE("curie"),
    BABBAGE("babbage"),
    ADA("ada");

    private final String models;

    fineTuneModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
