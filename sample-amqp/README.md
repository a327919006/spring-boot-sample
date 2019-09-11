### sample-amqp
此模块用于对接RabbitMQ

#### 延时消息
- 安装rabbitmq_delayed_message_exchange插件：https://www.rabbitmq.com/community-plugins.html
- 定义Exchange：
```
Map<String, Object> arguments = new HashMap<>();
arguments.put("x-delayed-type", "direct");
return new ExchangeBuilder("test14.delay.exchange", "x-delayed-message").durable(true).withArguments(arguments).build();
```
- 发送消息：
```
MessageProperties messageProperties = new MessageProperties();
messageProperties.setHeader("x-delay", 5000);
Message message = new Message(body.getBytes(), messageProperties);
rabbitTemplate.convertAndSend(exchange, routingKey, message);
```