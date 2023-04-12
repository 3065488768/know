package com.example.know.controller;

import com.example.know.service.impl.*;
import com.example.know.util.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 对于首页简易数据返回
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/index")
public class IndexController {

    @Resource
    private PostServiceImpl postService;

    @Resource
    private PlateServiceImpl plateService;

    @Resource
    private BookServiceImpl bookService;

    @Resource
    private DigestServiceImpl digestService;

    @Resource
    private UserServiceImpl userService;

    @GetMapping("/getCountNumber")
    public AjaxResult getCountNumber() {
        Map map = new HashMap(4);
        map.put("postNumberOfToday", postService.getCountNumber(1));
        map.put("postNumber", postService.getCountNumber(-1));
        map.put("userNumber", userService.getCountNumber());
        map.put("bookNumber", bookService.getCountNumber());
        return AjaxResult.success(map);
    }

    @GetMapping("/getPostOfExhibition")
    public AjaxResult getPostOfExhibition(int flag) {
        return postService.getPostOfExhibition(flag);
    }

    @GetMapping("/getPlateInformation")
    public AjaxResult getPlateInformation() {
        return plateService.getPlateInformation();
    }

    @GetMapping("/getBookOfRecommend")
    public AjaxResult getBookOfRecommend() {
        return bookService.getBookOfRecommend();
    }

    @GetMapping("/getDigestOfRecommend")
    public AjaxResult getDigestOfRecommend() {
        return digestService.getDigestOfRecommend();
    }
}
