package io.github.yukikaze.insert_chatgpt.enums.Models;

/**
 * /v1/embeddings
 */
public enum embeddingModels {
    TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
    TEXT_SEARCH_ADA_DOC_001("text-search-ada-doc-001");

    private final String models;

    embeddingModels(String models) {
        this.models = models;
    }

    public String getModels() {
        return models;
    }
}
