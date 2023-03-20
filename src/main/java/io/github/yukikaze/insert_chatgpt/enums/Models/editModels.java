package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/edits {text-davinci-edit-001}
 */
public enum editModels {
    TEXT_DAVINCI_EDIT_001("text-davinci-edit-001"),
    CODE_DAVINCI_EDIT_001("code-davinci-edit-001");

    private final String models;

    editModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
