package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.AccessDao;
import com.example.know.service.AccessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author bookWorm
 */
@Service
public class AccessServiceImpl implements AccessService {

    @Resource
    private AccessDao accessDao;

    @Override
    public Long accessByPostId(int postId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.eq("post_id", postId);
        Long aLong = accessDao.selectCount(queryWrapper);
        return aLong;
    }

    @Override
    public List countNumberByTime() {
        return accessDao.countNumberByTime();
    }

    @Override
    public long accessCount() {
        return accessDao.selectCount(null);
    }
}
