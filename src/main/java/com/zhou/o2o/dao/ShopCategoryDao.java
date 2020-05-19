package com.zhou.o2o.dao;

import com.zhou.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 店铺分类的持久层接口
 */
public interface ShopCategoryDao {

    /**
     * 查询全部的店铺分类
     * @param shopCategoryCondition 给参数指定一个名字，这样在mapper里面就可以通过
     *                              parent_id = #{shopCategoryCondition.parent.shopCategoryId}调用了。
     * @return
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
