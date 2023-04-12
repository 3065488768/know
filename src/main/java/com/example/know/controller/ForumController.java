package com.example.know.controller;

import com.example.know.enumeration.Setting;
import com.example.know.security.utils.JwtProperties;
import com.example.know.security.utils.JwtTokenUtil;
import com.example.know.service.CommentService;
import com.example.know.service.impl.PlateServiceImpl;
import com.example.know.service.impl.PostServiceImpl;
import com.example.know.service.impl.UserServiceImpl;
import com.example.know.util.AjaxResult;
import com.example.know.util.GetIpUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于论坛的简易信息返回
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/forum")
public class ForumController {
    @Resource
    private PostServiceImpl postService;

    @Resource
    private PlateServiceImpl plateService;

    @Resource
    private UserServiceImpl userService;

    @Resource
    private CommentService commentService;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取定制化帖子
     */
    @GetMapping("/getExhibitionPost")
    public AjaxResult getExhibitionPost(String parameter, int type, int[] plateId, char postType, int begin, int number, int rankType) {
        return postService.getExhibitionPost(parameter, type, plateId, postType, begin, Setting.SHOWNUMBER, rankType, null, null);
    }

    @GetMapping("/getSimplePostAndAuthor")
    public AjaxResult getFuzzySearchPost(int flag) {
        return postService.getSimplePostAndAuthor(flag);
    }

    @GetMapping("/getPlateByRanking")
    public AjaxResult getPlateByRanking() {
        return plateService.getPlateByRanking();
    }

    @GetMapping("/getUserOfRanking")
    public AjaxResult getUserOfRanking() {
        return userService.userOfRanking();
    }

    @GetMapping("/getPlateById/{plateId}")
    public AjaxResult getPlateById(@PathVariable int plateId) {
        return plateService.getPlateById(plateId);
    }

    @PostMapping("/getPostDetails/{postId}")
    public AjaxResult getPostDetails(@PathVariable int postId, HttpServletRequest request) {
        String tokenHeader = request.getHeader(jwtProperties.getRequestHeader());
        String username = null;
        if (StringUtils.hasLength(tokenHeader)) {
            String token = tokenHeader.substring(this.jwtProperties.getTokenPrefix().length());
            username = jwtTokenUtil.getUserNameFromToken(token);
        }
        String ip = GetIpUtil.getIpAddr(request);
        return postService.getPostById(postId, username, ip);
    }

    @PostMapping("/getCommentById")
    public AjaxResult getCommentById(int id, int start, int number, char type) {
        return commentService.getCommentById(id, start, number, type);
    }

    @PostMapping("/getPostByBookTypeId")
    public AjaxResult getPostByBookTypeId(int[] bookTypeId, int start, int number) {
        return postService.getPostByBookTypeId(bookTypeId, start, number);
    }

    @GetMapping("/getCommentByCommentId/{commentId}")
    public AjaxResult getCommentByCommentId(@PathVariable int commentId, int start, int number, char type) {
        return commentService.getCommentById(commentId, start, number, type);
    }

    @GetMapping("/getPostByPlate")
    public AjaxResult getPostByPlate(int plateId, int start, int number) {
        return postService.getPostByPlateId(plateId, start, number);
    }

    @GetMapping("/getRecommendPost")
    public AjaxResult getRecommendPost(int postId, int startPage, int number) {
        Long countNumber = postService.getCountNumber(-1);
        Map map = new HashMap(2);
        map.put("isHave", (startPage - 1) * number < countNumber);
        map.put("postList", postService.getRecommendPost(postId, startPage, number));
        return AjaxResult.success(map);
    }

    @PostMapping("/getComment")
    public AjaxResult getComment(int postId) {
        return AjaxResult.success(commentService.getPostComment(postId));
    }
}
