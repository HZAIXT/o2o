package com.zhou.o2o.service;

import com.zhou.o2o.BaseTest;
import com.zhou.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList() {
        List<Area> areaList = areaService.getAreaList();
        /**
         * 因为数据库查询的时候是设置了按权重从大到小的排序方式，所以这里
         * 我们使用assertEquals()断言来判断第一个数据的area_name是不是叫西苑
         * 正常则不会报错
         */
        assertEquals("西苑",areaList.get(0).getAreaName());
    }
}
