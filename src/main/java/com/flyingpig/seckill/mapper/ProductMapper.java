package com.flyingpig.seckill.mapper;

import com.flyingpig.seckill.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-09-20
 */
@Mapper

public interface ProductMapper extends BaseMapper<Product> {

    int deductProductStock(Long productId);
}
