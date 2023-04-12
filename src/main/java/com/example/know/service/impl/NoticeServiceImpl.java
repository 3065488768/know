package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.NoticeDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.Notice;
import com.example.know.service.NoticeService;
import com.example.know.util.AjaxResult;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知
 *
 * @author bookWorm
 */
@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private UserDao userDao;

    @Resource
    private NoticeDao noticeDao;

    @Override
    public AjaxResult getNoticeByUser(String username, int start, int number) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("notice_by", userId);
        queryWrapper.ne("status", '2');
        queryWrapper.orderByDesc("status");
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(start, number);
        List<Notice> list = noticeDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getNoticeUnread(String username) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '1');
        queryWrapper.eq("notice_by", userId);
        return AjaxResult.success(noticeDao.selectCount(queryWrapper));
    }

    @Override
    public List getNoticeByUser(String username) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        return null;
    }
}
