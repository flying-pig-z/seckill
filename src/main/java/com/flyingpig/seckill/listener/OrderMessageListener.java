package com.flyingpig.seckill.listener;

import com.flyingpig.seckill.entity.Order;
import com.flyingpig.seckill.service.IOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(topic = "order-topic", consumerGroup = "order-consumer-group")
@Component
public class OrderMessageListener implements RocketMQListener<Order> {

    @Autowired
    private IOrderService orderService;

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @Override
    public void onMessage(Order order) {
        // 处理订单创建逻辑
        orderService.save(order);

        // 发送延迟消息来取消订单
        String cancelOrderTopic = "cancel-order-topic";

        // 使用 Spring 的 MessageBuilder 来构建消息，并设置延迟等级
        org.springframework.messaging.Message<Long> message = MessageBuilder.withPayload(order.getId())
                .setHeader(RocketMQHeaders.KEYS, order.getId())
                .build();

        // 发送延迟消息，使用延迟等级（例如延迟 30 分钟）
        rocketMQTemplate.syncSend(cancelOrderTopic, message, 3000, 16);  // 超时时间3000ms，延迟等级16（30分钟）


    }
}
