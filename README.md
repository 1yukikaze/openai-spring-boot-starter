# openai-spring-boot-starter
**A tool that connects to the OpenAI API.**

基于Jersey Client服务与springboot整合框架提供的openAI快速调用工具.

### 快速开始

**1.导入pom依赖**

```xml
<dependency>
  <groupId>io.github.1yukikaze</groupId>
  <artifactId>openai-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

**2.配置application.yaml文件**

```yaml
chatgpt:
  #配置连接URL地址,可以选择配置反向代理地址,默认为官方地址
  URL: "https://api.openai.com/v1" 
  #你的API-key
  authorization: "sk-**************************************"
#  #可选: 你的组织标头
#  openAIOrganization: "***************"
```

**3.装配bean IChatgptClient到你想要的地方**

```java
@Autowired
private IChatgptClient ichatgptClient;
```

### 已实现的接口(示例)

方法接口与实体类均遵守命名规范有迹可循

接口与实现方法位置:
*io/github/yukikaze/insert_chatgpt/service/client/*

实体类与装配方法位置:
*io/github/yukikaze/insert_chatgpt/dto/*

模型枚举位置
*io/github/yukikaze/insert_chatgpt/enums/Models/*

openAI接口有着大量的参数配置选项,但大部分都不是必须的,你可以参考实体类的配置文档进行自行定义配置,以下只演示最简单的方法实现.
如有疑问请转至openAI官方文档:[API Reference - OpenAI API](https://platform.openai.com/docs/api-reference)

**1.查看官方模型列表**

```java
@Test
    public void listModelsTest() {
        ListModelsResponse listModelsResponse = ichatgptClient.listModels();
        System.out.println(listModelsResponse);
    }
```

**2.检索模型**

```java
@Test
public void retrieveModelTest() {
    Data response = ichatgptClient.retrieveModel(completionModels.TEXT_DAVINCI_002.getModels());
    System.out.println(response.toString());
}
```

3.**完成**

```java
@Test
public void completionTest() {
    //新建实体类并导入发送的参数
    CompletionRequest response = new CompletionRequest();
    //设置调用的模型
    response.setModel(completionModels.TEXT_DAVINCI_002.getModels());
    //发送需要补全的文本
    response.sendMessage("你好", "你好");
    //调用方法
    CompletionResponse completions = ichatgptClient.getCompletions(response);
    System.out.println(completions);
}
```

开启SSE协议的方式

```java
@Test
public void completionStreamTest() {
    CompletionRequest response = new CompletionRequest();
    response.setModel(completionModels.TEXT_DAVINCI_002.getModels());
    response.sendMessage("你好");
    response.setStream(true);
    //调用SSE协议模式的方法
    LinkedBlockingQueue<CompletionResponse> completionsStream = ichatgptClient.getCompletionsStream(response);
    for (CompletionResponse completionResponse : completionsStream) {
        System.out.println(completionResponse.toString());
    }
}
```

**4.对话(ChatGPT)**

```java
@Test
public void chatTest() {
    //新建实体类并导入参数
    ChatRequest chatRequest = new ChatRequest();
    //选择对话模型(必填)
    chatRequest.setModel(chatModels.GPT_3_5_TURBO.getModels());
    //设置chat风格(可选)
    chatRequest.setSystemMessage("请尽量说的简洁点,用中文回复");
    chatRequest.sendMessage("你好");
    //发送
    ChatResponse chat = ichatgptClient.getChat(chatRequest);
    System.out.println(chat);
```

如果你需要开启关联上下文模式:

```java
@Test
public void chatTest() {
    ChatRequest chatRequest = new ChatRequest();
    chatRequest.setModel(chatModels.GPT_3_5_TURBO.getModels());
    chatRequest.setSystemMessage("请尽量说的简洁点,用中文回复");
    
    //仅仅需要设置用户名,方便该工具识别开启上下文的用户并存储在 ChatUserData/{messageUser}.yaml 文件中
    chatRequest.sendMessage("你好","user");
    ChatResponse chat = ichatgptClient.getChat(chatRequest);
    System.out.println(chat);
}
```

上下文默认关联一组,如果你想关联更多上下文关系:

```java
chatRequest.sendMessage("你好","user",4);
```

直接在该方法后加上组数(默认两条一组) 

**<u>请注意:该方式会消耗大量token并有可能超过官方设置的token最大请求数</u>**

结束上下文关联对话(删除用户文档):

```
chatRequest.deleteUserData("user");
```



开启SSE协议的方式:

(未实现)

### 以下内容仍未实现

**编辑:**

**图像:**

**嵌入:**

**音频:**

**文件:**

**微调:**

**审核:**

(最近在找工作,随缘更新)

### 已知的bug以及解决方案

```yaml
框架冲突: fastjson在1.2.36后，加入JerseyAutoDiscoverable的实现，在jersey启动的时候会自动去加载FastJsonProvider导致Jersey Client服务无法正常启动.
解决方案: 降级 fastjson 为 1.2.35.
```
