package com.flyingpig.seckill.service;

import com.flyingpig.seckill.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
public interface IOrderService extends IService<Order> {

    void seckill(Long productId, Long userId);

    void cancelOrder(String orderId);
}
