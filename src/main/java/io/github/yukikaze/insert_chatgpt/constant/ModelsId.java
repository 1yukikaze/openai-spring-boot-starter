package io.github.yukikaze.insert_chatgpt.constant;

/**
 * 从官网上记录的可用模型以及传参路径 不定时更新
 */
@SuppressWarnings("unused")
public class ModelsId {
    /**
     * /v1/chat/completions
     */
    private static final String GPT_4 = "gpt-4";
    private static final String GPT_4_0314 = "gpt-4-0314";
    private static final String GPT_4_32K = "gpt-4-32k";
    private static final String GPT_4_32K_0314 = "gpt-4-32k-0314";
    private static final String GPT_3_5_TURBO = "gpt-3.5-turbo";
    private static final String GPT_3_5_TURBO_0301 = "gpt-3.5-turbo-0301";

    /**
     * /v1/completions
     * -----or-----
     * davinci, curie, babbage, ada -> /v1/fine-tunes
     */
    private static final String TEXT_DAVINCI_003 = "text-davinci-003";
    private static final String TEXT_DAVINCI_002 = "text-davinci-002";
    private static final String TEXT_CURIE_001 = "text-curie-001";
    private static final String TEXT_BABBAGE_001 = "text-babbage-001";
    private static final String TEXT_ADA_001 = "text-ada-001";
    private static final String DAVINCI = "davinci";
    private static final String CURIE = "curie";
    private static final String BABBAGE = "babbage";
    private static final String ADA = "ada";

    /**
     * /v1/edits {text-davinci-edit-001}
     */
    private static final String TEXT_DAVINCI_EDIT_001 = "text-davinci-edit-001";
    private static final String CODE_DAVINCI_EDIT_001 = "code-davinci-edit-001";

    /**
     * /v1/audio/transcriptions or
     * /v1/audio/transcription
     */
    private static final String WHISPER_1 = "whisper-1";

    /**
     * /v1/embeddings
     */
    private static final String TEXT_EMBEDDING_ADA_002 = "text-embedding-ada-002";
    private static final String TEXT_SEARCH_ADA_DOC_001 = "text-search-ada-doc-001";

    /**
     * /v1/moderations
     */
    private static final String TEXT_MODERATION_STABLE = "text-moderation-stable";
    private static final String MODERATION_LATEST = "moderation-latest";
}
