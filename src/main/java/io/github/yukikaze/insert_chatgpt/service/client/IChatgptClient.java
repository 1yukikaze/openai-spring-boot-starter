package io.github.yukikaze.insert_chatgpt.service.client;

import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;

public interface IChatgptClient {
    ListModelsResponse listModels();
}
