package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/completions
 */
public enum completionModels {
    TEXT_DAVINCI_003("text-davinci-003"),
    TEXT_DAVINCI_002("text-davinci-002"),
    TEXT_CURIE_001("text-curie-001"),
    TEXT_BABBAGE_001("text-babbage-001"),
    TEXT_ADA_001("text-ada-001"),
    DAVINCI("davinci"),
    CURIE("curie"),
    BABBAGE("babbage"),
    ADA("ada");
    private final String models;

    completionModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
