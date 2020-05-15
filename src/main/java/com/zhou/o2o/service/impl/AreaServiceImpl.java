package com.zhou.o2o.service.impl;

import com.zhou.o2o.dao.AreaDao;
import com.zhou.o2o.entity.Area;
import com.zhou.o2o.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Area业务层实现类
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
