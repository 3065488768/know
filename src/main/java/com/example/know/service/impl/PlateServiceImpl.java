package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.BookTypeDao;
import com.example.know.dao.PlateDao;
import com.example.know.dao.PostDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.BookType;
import com.example.know.entity.Plate;
import com.example.know.service.PlateService;
import com.example.know.util.AjaxResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版块
 *
 * @author bookWorm
 */
@Service
public class PlateServiceImpl implements PlateService {
    @Resource
    private PlateDao plateDao;

    @Resource
    private UserDao userDao;

    @Resource
    private PostDao postDao;

    @Resource
    private BookTypeDao bookTypeDao;

    @Override
    public AjaxResult getPlateByRanking() {
        return AjaxResult.success(plateDao.getPlateByRanking());
    }

    @Override
    public AjaxResult getPlateInformation() {
        return AjaxResult.success(plateDao.getPlateInformation());
    }

    @Override
    public AjaxResult getPlateById(int plateId) {
        LambdaQueryWrapper<Plate> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Plate::getPlateId, plateId);
        lambdaQueryWrapper.eq(Plate::getStatus, '0');
        Plate plate = plateDao.selectOne(lambdaQueryWrapper);
        String createBy = userDao.selectById(plate.getCreateBy()).getNickName();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.eq("plate_id", plateId);
        Long postNumber = postDao.selectCount(queryWrapper);
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("plate_id", plateId);
        List<BookType> bookTypeList = bookTypeDao.selectList(queryWrapper1);
        Map map = new HashMap();
        map.put("plate", plate);
        map.put("bookTypeList", bookTypeList);
        map.put("createBy", createBy);
        map.put("postNumber", postNumber);
        return AjaxResult.success(map);
    }

    @Override
    public int updatePlate(Plate plate) {
        Plate plate1 = plateDao.selectById(plate.getPlateId());
        if (StringUtils.hasLength(plate.getPlateName())) {
            plate1.setPlateName(plate.getPlateName());
        }
        if (StringUtils.hasLength(plate.getImgUrl())) {
            plate1.setImgUrl(plate.getImgUrl());
        }
        if (StringUtils.hasLength(plate.getRemark())) {
            plate1.setRemark(plate.getRemark());
        }
        if (plate.getStatus() == '0' || plate.getStatus() == '1' || plate.getStatus() == '2') {
            plate1.setStatus(plate.getStatus());
        }
        return plateDao.updateById(plate1);
    }
}
