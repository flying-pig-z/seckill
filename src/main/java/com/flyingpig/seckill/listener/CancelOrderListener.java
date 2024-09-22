package com.flyingpig.seckill.listener;

import com.flyingpig.seckill.service.IOrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "cancel-order-topic", consumerGroup = "cancel-order-consumer-group")
public class CancelOrderListener implements RocketMQListener<String> {

    @Autowired
    private IOrderService orderService; // 注入你的订单服务

    @Override
    public void onMessage(String orderId) {
        // 取消订单的逻辑
        orderService.cancelOrder(orderId);
    }
}

