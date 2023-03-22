# openai-spring-boot-starter
A tool that connects to the OpenAI API.
开发中.........
框架冲突: fastjson在1.2.36后，加入JerseyAutoDiscoverable的实现，在jersey启动的时候，会自动去加载FastJsonProvider。
解决方案: 降级 fastjson 为 1.2.35，