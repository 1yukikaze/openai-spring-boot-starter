package io.github.yukikaze.insert_chatgpt.service.client;

import io.github.yukikaze.insert_chatgpt.dto.Listmodels.ListModelsResponse;

/**
 * 该starter的公共入口,请使用@Autowired装配该接口启用功能.
 * 除了基础功能以外,调用大部分URL请求需要先对信息进行配置.
 * 配置方法详情请转到 ../registration/IChatgptRegistration.java
 */
public interface IChatgptClient {
    /**
     * 列出模型
     * @return 模型实体类
     */
    ListModelsResponse listModels();

    /**
     * 检索模型
     * @param modelId 模型id
     * @return 模型实体类
     */
    ListModelsResponse retrieveModel(String modelId);

}
