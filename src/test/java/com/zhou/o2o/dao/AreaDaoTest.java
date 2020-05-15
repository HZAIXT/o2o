package com.zhou.o2o.dao;

import com.zhou.o2o.BaseTest;
import com.zhou.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * AreaDaoTest区域测试类,每次启动该类的时候都会调用BaseTest
 * 里面的方法去加载spring-dao.xml里面的配置文件
 * @author 25677
 *
 */
public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    /**
     * 测试查询
     */
    @Test
    public void testQueryArea() {
        List<Area> areaList = areaDao.queryArea();
        /**
         * assertEquals()是断言
         * assertEquals([String message],Object target,Object result)
         * [String message]是可省略的
         * target参数是我们期望得到的结果，result的参数是实际得到的结果，
         * 如果两种相同，则表示结果正确，不相同，则抛出异常
         */
        assertEquals(2,areaList.size());
    }
}
