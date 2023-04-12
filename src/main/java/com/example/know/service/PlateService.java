package com.example.know.service;

import com.example.know.entity.Plate;
import com.example.know.util.AjaxResult;

/**
 * @author bookWorm
 */
public interface PlateService {
    /**
     * 获得综合评分排行的版块
     *
     * @return
     */
    public AjaxResult getPlateByRanking();

    /**
     * 获取默认的板块信息
     *
     * @return
     */
    public AjaxResult getPlateInformation();

    /**
     * 根据版块id获取版块信息
     *
     * @param plateId 版块id
     * @return 返回对应的版块信息
     */
    public AjaxResult getPlateById(int plateId);

    public int updatePlate(Plate plate);
}
