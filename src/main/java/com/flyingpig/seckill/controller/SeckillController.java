package com.flyingpig.seckill.controller;


import com.flyingpig.seckill.common.Result;
import com.flyingpig.seckill.entity.Product;
import com.flyingpig.seckill.service.IOrderService;
import com.flyingpig.seckill.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@RestController
@RequestMapping("/seckill")
@Slf4j
public class SeckillController {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostConstruct
    public void init() {
        List<Product> productList = productService.list();
        for (Product p : productList) {
            stringRedisTemplate.opsForValue().set("product_stock_" + p.getId(), p.getStock() + "");
        }
    }

    @PostMapping()
    public Result seckill(Long productId, Long userId) {
        Long stock = stringRedisTemplate.opsForValue().decrement("product_stock_" + productId);
        if (stock < 0) {
            return Result.error(500, "商品已售完");
        }
        try {
            orderService.seckill(productId, userId);
        } catch (Exception e) {
            stringRedisTemplate.opsForValue().increment("product_stock_" + productId);
            log.error("创建订单失败", e);
            return Result.error(500, "创建订单失败");
        }
        return Result.success();
    }
}
