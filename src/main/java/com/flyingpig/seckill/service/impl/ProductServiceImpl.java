package com.flyingpig.seckill.service.impl;

import com.flyingpig.seckill.entity.Product;
import com.flyingpig.seckill.mapper.ProductMapper;
import com.flyingpig.seckill.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
