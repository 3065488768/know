package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.component.WebSocketServer;
import com.example.know.dao.LetterDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.Letter;
import com.example.know.entity.User;
import com.example.know.service.LetterService;
import com.example.know.util.AjaxResult;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 私信实现类(暂缓)
 *
 * @author bookWorm
 */
@Service
public class LetterServiceImpl implements LetterService {

    @Resource
    private UserDao userDao;

    @Resource
    private LetterDao letterDao;

    @Override
    public AjaxResult getLetterBuUser(String username, int start, int number) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();

        return null;
    }

    @Override
    public AjaxResult getUnreadLetterCount(String username) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recipient", userId);
        queryWrapper.eq("status", '1');
        return null;
    }

    @Override
    public boolean sendLetter(String username, int recipient, String content) {
        User user = userDao.selectOfUserName(username);
        Map map = new HashMap();
        map.put("received", false);
        map.put("comment", true);
        map.put("replies", false);
        WebSocketServer.sendInfo(content, map, String.valueOf(recipient));
        Letter letter = new Letter();
        letter.setSendId(user.getUserId());
        letter.setRecipientId(recipient);
        letter.setStatus('1');
        letter.setContent(content);
        int insert = letterDao.insert(letter);
        if (insert == 1) {
            return true;
        }
        return false;
    }
}
