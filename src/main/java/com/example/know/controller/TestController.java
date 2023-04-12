package com.example.know.controller;

import com.example.know.aop.AvoidDuplicateSubmissions;
import com.example.know.component.WebSocketServer;
import com.example.know.dao.CommentDao;
import com.example.know.service.CommentService;
import com.example.know.service.PostService;
import com.example.know.util.AjaxResult;
import com.example.know.util.GetIpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bookWorm
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private CommentService service;

    @Resource
    private CommentDao commentDao;

    @Resource
    private PostService postService;

    @GetMapping("/hello")
    public void sendMessage(String message, String userId, int type) {
        try {
            switch (type) {
                case 0:
                    Map map = new HashMap();
                    map.put("received", false);
//                     commentNumber:"",
//            repliesNumber:"",
                    map.put("comment", true);
                    map.put("replies", false);
                    WebSocketServer.sendInfo(message, map, userId);
                    break;
                case 1:
                    WebSocketServer.sendInfo(message, null, userId);
                    break;
                default:
                    log.error("参数为空");
            }
        } catch (NullPointerException nullPointerException) {
            log.error("发送信息不能为空");
        }
    }
    @AvoidDuplicateSubmissions
    @GetMapping("/ipTest")
    public String getIp(HttpServletRequest httpServletRequest,String value) {
        String ipAddr = GetIpUtil.getIpAddr(httpServletRequest);
        return ipAddr;
    }

    @GetMapping("/getPostList")
    public AjaxResult getPostList(char status, int orderBy, char postType, int startPage) {
//       return service.getPostComment(postId);
        return AjaxResult.success(postService.getPostList(status, orderBy, postType, startPage));
    }
}
