### 测试流程
1. 浏览器执行`/sse/subscribe?clientId=1`接口后就会建立起长连接
2. 另起一个页面调用`/sse/push?clientId=1&content=hello`接口会向上一个页面推送内容
3. 调用`/sse/over?clientId=1`即可关闭指定客户端的SSE长连接