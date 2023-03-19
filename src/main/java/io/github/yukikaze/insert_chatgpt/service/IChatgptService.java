package io.github.yukikaze.insert_chatgpt.service;

import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;

public interface IChatgptService {
    ListModelsResponse listModels();
}
