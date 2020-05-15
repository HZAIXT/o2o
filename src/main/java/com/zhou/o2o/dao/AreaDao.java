package com.zhou.o2o.dao;

import com.zhou.o2o.entity.Area;

import java.util.List;

/**
 * 测试持久层
 */
public interface AreaDao {
    /**
     * 列出区域列表
     * @return areaList
     */
    List<Area>  queryArea();

}
