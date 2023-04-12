package com.example.know.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.know.dao.TransactionsDao;
import com.example.know.entity.Book;
import com.example.know.entity.BookPath;
import com.example.know.entity.Post;
import com.example.know.entity.User;
import com.example.know.enumeration.UrlType;
import com.example.know.security.utils.JwtProperties;
import com.example.know.security.utils.JwtTokenUtil;
import com.example.know.service.*;
import com.example.know.service.impl.BookServiceImpl;
import com.example.know.service.impl.CommentServiceImpl;
import com.example.know.service.impl.PostServiceImpl;
import com.example.know.service.impl.UserServiceImpl;
import com.example.know.util.*;
import org.apache.ibatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 用户的一些操作
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SendMailUtil sendMailUtil;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private PostService postService;

    @Resource
    private CommentService commentService;

    @Resource
    private BookPathService bookPathService;

    @Resource
    private DigestService digestService;

    @Resource
    private BookService bookService;

    @Resource
    private NoticeService noticeService;

    @Resource
    private LetterService letterService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/uploadBooks")
    public AjaxResult uploads(HttpServletRequest request, Book book) {
        List<MultipartFile> list = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file;
        for (int i = 0; i < list.size(); ++i) {
            file = list.get(i);
            if (!file.isEmpty()) {
                try {
                    String oldFileName = file.getOriginalFilename();
                    String filePath = "file/";
                    String newFileName = filePath + UUID.randomUUID() + oldFileName;
                    File dest = new File(newFileName);
                    file.transferTo(dest);
                } catch (IOException e) {
                    e.printStackTrace();
                    return AjaxResult.error("系统异常");
                }
            }
        }
        logger.info(book.getBookName());
        return AjaxResult.success("上传成功");
    }

    @RequestMapping("/uploadBook")
    public AjaxResult upload(MultipartFile file, Book book) {
        String oldFileName = file.getOriginalFilename();
        String filePath = "know\\public\\static\\pdf/";
        String newName = UUID.randomUUID() + oldFileName;
        String newFileName = filePath + newName;
        File dest = new File(newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            BookPath bookPath = new BookPath();
            bookPath.setBookId(book.getBookId());
            bookPath.setPath(newName);
            //缺少书籍贡献者id
            bookPathService.insert(bookPath);
            file.transferTo(dest);
            return AjaxResult.success(oldFileName + "上传成功");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            dest.delete();
            e.printStackTrace();
        }
        return AjaxResult.error("上传失败");
    }

    @RequestMapping("/uploadImg")
    public AjaxResult uploadImg(MultipartFile file, Book book,
                                boolean postOrNot, boolean isHaveMoney, int Money) {
        String oldFileName = file.getOriginalFilename();
        String filePath = "know\\src\\static\\img\\book/";
        String newName = UUID.randomUUID() + oldFileName;
        String newFileName = filePath + newName;
        File dest = new File(newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            book.setStatus('2');
            book.setImgUrl(newName);
            int i = bookService.insertBook(book);
//          并发布帖子进行奖励追加
            if (i == 1) {
                file.transferTo(dest);
                return AjaxResult.success(oldFileName + "上传成功");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            dest.delete();
            e.printStackTrace();
        }
        return AjaxResult.error("上传失败");
    }

    @RequestMapping("/uploadOtherImg")
    public String uploadOtherImg(MultipartFile file, int type) throws IOException {
        String oldFileName = file.getOriginalFilename();
        String filePath = "";
        switch (type) {
            case 0:
                filePath = UrlType.USERIMG.getUrl();
                break;
            case 1:
                filePath = UrlType.POSTIMG.getUrl();
                break;
            case 2:
                filePath = UrlType.PLATEIMG.getUrl();
                break;
            case 3:
                filePath = UrlType.ROTATIONCHART.getUrl();
                break;
            default:
                filePath = "know\\src\\static\\img\\other/";
        }
        String newName = UUID.randomUUID() + oldFileName;
        String newFileName = filePath + newName;
        File dest = new File(newFileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return "../static/img/post/" + newName;
    }

    @RequestMapping(value = "/downloadBook", produces = "application/json; charset=utf-8")
    public void download(HttpServletResponse httpServletResponse, int bookId) throws FileNotFoundException {
        String pathOfValid = bookPathService.getPathOfValid(bookId);
        File file = new File("E:/file/" + pathOfValid);
        FileInputStream inputStream = new FileInputStream(file);
        httpServletResponse.setContentType("application/force-download");
        httpServletResponse.setHeader("Content-Disposition", "attachment;fileName=" + pathOfValid);
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            OutputStream outputStream = httpServletResponse.getOutputStream();
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
            logger.info("下载成功");
        } catch (IOException ioException) {
            logger.error("下载失败");
        }
    }

    @PostMapping("/login")
    public AjaxResult login(String username, String password) {
        if (!StringUtils.isBlank(username) && !StringUtils.isBlank(password)) {
            return userService.login(username, password);
        } else {
            return new AjaxResult(AjaxResult.Type.PARAM_IS_BLANK, "参数为空");
        }
    }

    @PostMapping("/getUserInfo")
    public AjaxResult getUserInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(jwtProperties.getRequestHeader()).substring(this.jwtProperties.getTokenPrefix().length());
        String username = jwtTokenUtil.getUserNameFromToken(token);
        return AjaxResult.success(userService.getUser(username));
    }

    @PostMapping("/loginOut")
    public AjaxResult loginOut(String token) {
        return AjaxResult.success();
    }

    @PostMapping("/getUserDetails/{userId}")
    public AjaxResult getUserDetails(@PathVariable int userId) {
        return AjaxResult.success(userId);
    }

    @PostMapping("/likeDigestByUser")
    public AjaxResult DigestByUser(HttpServletRequest httpServletRequest, int digestId, int type) {
        String token = httpServletRequest.getHeader(jwtProperties.getRequestHeader()).substring(this.jwtProperties.getTokenPrefix().length());
        String username = jwtTokenUtil.getUserNameFromToken(token);
        if (digestId < 0) {
            return new AjaxResult(AjaxResult.Type.PARAM_NOT_COMPLETE, "参数非法");
        }
        return digestService.digestByUser(username, digestId, type);
    }

    @PostMapping("/getNoticeByUser")
    public AjaxResult getUserNotice(HttpServletRequest httpServletRequest, int start, int number) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        return noticeService.getNoticeByUser(userName, start, number);
    }

    @PostMapping("/getNoticeUnread")
    public AjaxResult getUserNoticeCount(HttpServletRequest httpServletRequest) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        return noticeService.getNoticeUnread(userName);
    }

    @PutMapping("/releasePost")
    public AjaxResult releasePost(Post post, HttpServletRequest httpServletRequest) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        return postService.releasePost(post, userName);
    }

    @PostMapping("/transferAccounts")
    public AjaxResult transferAccounts(HttpServletRequest httpServletRequest, int userId, int currencyNumber) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        User user = userService.getUser(userName);
        if (!org.springframework.util.StringUtils.hasLength(userName)) {
            return AjaxResult.error("用户异常");
        } else if (userService.getUserBookCurrency(userName) < currencyNumber) {
            return AjaxResult.error("账户余额不足");
        } else if (user.getUserId() == userId) {
            return AjaxResult.error("请勿无中生我");
        } else {
            boolean isSuccess = userService.transferAccounts(userName, userId, currencyNumber);
            if (isSuccess) {
                return AjaxResult.success("交易成功");
            } else {
                return AjaxResult.error("交易失败");
            }
        }
    }

    @PostMapping("/likePost")
    public AjaxResult likePost(HttpServletRequest httpServletRequest, int postId) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        boolean b = postService.likePost(userName, postId);
        if (b) {
            return AjaxResult.success("点赞成功", "+1");
        } else {
            return AjaxResult.success("取消点赞", "-1");
        }
    }

    @PostMapping("/collectionPost")
    public AjaxResult collectionPost(HttpServletRequest httpServletRequest, int postId) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        boolean b = postService.collectionPost(userName, postId);
        if (b) {
            return AjaxResult.success("收藏成功", "+1");
        } else {
            return AjaxResult.success("取消收藏", "-1");
        }
    }

    @PostMapping("/commentPost")
    public AjaxResult comment(HttpServletRequest httpServletRequest, int commentId, String content, char type) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        boolean b = commentService.insertComment(userName, commentId, content, type);
        if (commentId > 0 && StringUtils.isNotBlank(content) && (type == '0' || type == '1')) {
            if (b) return AjaxResult.success("评论成功");
        } else {
            return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数异常");
        }
        return AjaxResult.error("未知异常");
    }

    @PostMapping("/subscribeBook")
    public AjaxResult subscribeBook(HttpServletRequest httpServletRequest, int bookId) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        int b = bookService.subscribeBook(userName, bookId);
        return AjaxResult.success(b);
    }

    @PostMapping("/sendLetter")
    public AjaxResult subscribeBook(HttpServletRequest httpServletRequest, int recipient, String content) {
        String userName = jwtTokenUtil.getUserNameFromHttpServletRequest(httpServletRequest, jwtProperties.getRequestHeader(), jwtProperties.getTokenPrefix().length());
        if (StringUtils.isNotBlank(content)) {
            boolean b = letterService.sendLetter(userName, recipient, content);
            if (b) {
                return AjaxResult.success("发送成功");
            }
        } else {
            return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }
        return AjaxResult.error("发送失败");
    }
}
