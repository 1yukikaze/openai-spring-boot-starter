package io.github.yukikaze.insert_chatgpt.service.client;

import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionRequest;
import io.github.yukikaze.insert_chatgpt.dto.completions.CompletionResponse;
import io.github.yukikaze.insert_chatgpt.dto.listmodels.Data;
import io.github.yukikaze.insert_chatgpt.dto.listmodels.ListModelsResponse;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 该starter的公共入口,请使用@Autowired装配该接口启用功能.
 * 除了基础功能以外,调用大部分URL请求需要先对需要传输的参数进行配置.
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
    Data retrieveModel(String modelId);

    /**
     * 预测并完成,传入预构建的参数以及文本,返回补全结果.
     * @param request 传入的参数
     * @return 模型实体类
     */
    CompletionResponse getCompletions(CompletionRequest request);

    /**
     * 预测并完成,传入预构建的参数以及文本,返回补全结果的阻塞队列
     * @param request 传入的参数
     * @return 管道输入流对象
     */
    LinkedBlockingQueue getCompletionsStream(CompletionRequest request);
}
