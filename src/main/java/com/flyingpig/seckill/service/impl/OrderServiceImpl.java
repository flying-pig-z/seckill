package com.flyingpig.seckill.service.impl;

import com.flyingpig.seckill.entity.Order;
import com.flyingpig.seckill.entity.Product;
import com.flyingpig.seckill.mapper.OrderMapper;
import com.flyingpig.seckill.mapper.ProductMapper;
import com.flyingpig.seckill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flyingpig.seckill.service.IProductService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    RocketMQTemplate rocketMQTemplate;



    @Override
    @Transactional
    public void seckill(Long productId, Long userId) {
        // 查询商品
        Product product = productMapper.selectById(productId);
        if (product.getStock() < 0) {
            throw new RuntimeException("商品库存已售完");
        }

        // 减库存
        int updateNum = productMapper.deductProductStock(productId);
        if (updateNum <= 0) {
            throw new RuntimeException("商品库存已售完");
        }

        // 异步创建秒杀订单
        Order order = new Order();
        order.setProductId(productId);
        order.setAmount(product.getPrice());
        order.setUserId(userId);
        order.setStatus(0);
        // 发送消息到 RocketMQ
        rocketMQTemplate.convertAndSend("order-topic", order);


    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = new Order();
        order.setId(order.getId());
        order.setStatus(-1);
        updateById(order);
    }
}
