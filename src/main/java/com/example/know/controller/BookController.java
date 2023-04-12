package com.example.know.controller;


import com.example.know.security.utils.JwtProperties;
import com.example.know.security.utils.JwtTokenUtil;
import com.example.know.service.BookPathService;
import com.example.know.service.BookService;
import com.example.know.service.DigestService;
import com.example.know.util.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 对于书库的简易信息返回
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    @Resource
    private DigestService digestService;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private BookPathService bookPathService;


    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @GetMapping("/getBookByNewOrder")
    public AjaxResult getBookByNewOrder() {
        return bookService.getBookByNewOrder();
    }

    @GetMapping("/getBookByHotOrder")
    public AjaxResult getBookByHotOrder() {
        return bookService.getBookByHotOrder();
    }

    @GetMapping("/getBookByLikeOrder")
    public AjaxResult getBookByLikeOrder() {
        return bookService.getBookByLikeOrder();
    }

    @GetMapping("/getBookByCollectionOrder")
    public AjaxResult getBookByCollectionOrder() {
        return bookService.getBookByCollectionOrder();
    }

    @GetMapping("/getBookByExpectOrder")
    public AjaxResult getBookByExpectOrder() {
        return bookService.getBookByExpectOrder();
    }

    @GetMapping("/getBookType")
    public AjaxResult getBookType() {
        return bookService.getBookType();
    }

    @PostMapping("/getBookPath")
    public AjaxResult getBookPath(int bookId) {
        return AjaxResult.success("获取路径成功", bookPathService.getPathOfValid(bookId));
    }

    @GetMapping("/getBookByCondition")
    public AjaxResult getBookByCondition(int bookType, String status, int order, String startTime, String endTime, int startPage) {
        return bookService.getBookByCondition(bookType, Integer.parseInt(status), order, startTime, endTime, startPage);
    }

    @PostMapping("/getBookDetailed/{bookId}")
    public AjaxResult getBookDetailed(@PathVariable int bookId) {
        return bookService.getBookById(bookId);
    }

    @PostMapping("/getBookDigest/{bookId}")
    public AjaxResult getBookDetails(@PathVariable int bookId, HttpServletRequest httpServletRequest, int start, int number) {
        String authToken = httpServletRequest.getHeader(jwtProperties.getRequestHeader());
        String username = null;
        if (StringUtils.hasLength(authToken)) {
            String token = authToken.substring(this.jwtProperties.getTokenPrefix().length());
            username = jwtTokenUtil.getUserNameFromToken(token);
        }
        return digestService.getDigestByBookId(bookId, username, start, number);
    }

    @PostMapping("/getBookClassification")
    public AjaxResult getBookClassification() {
        return bookService.getBookClassification();
    }

    @GetMapping("/getSubscribe")
    public Boolean getSubscribe(HttpServletRequest httpServletRequest, int bookId) {
        String authToken = httpServletRequest.getHeader(jwtProperties.getRequestHeader());
        String username = null;
        if (StringUtils.hasLength(authToken)) {
            String token = authToken.substring(this.jwtProperties.getTokenPrefix().length());
            username = jwtTokenUtil.getUserNameFromToken(token);
            return bookService.isSubscribeBook(username, bookId);
        }
        return false;
    }
}
