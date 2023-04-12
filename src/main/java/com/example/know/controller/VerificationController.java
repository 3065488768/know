package com.example.know.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.know.entity.User;
import com.example.know.service.impl.UserServiceImpl;
import com.example.know.util.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 验证登陆等接口
 * (未过滤)
 *
 * @author bookWorm
 */
@RestController
@RequestMapping("/verification")
public class VerificationController {
    @Resource
    private UserServiceImpl userService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private SendMailUtil sendMailUtil;

    @GetMapping("/checkUserNameUnique")
    public boolean checkUserNameUnique(String userName) {
        return userService.checkUserNameUnique(userName);
    }

    @GetMapping("/checkNickNameUnique")
    public boolean checkNickNameUnique(String nickName) {
        return userService.checkNickNameUnique(nickName);
    }

    @GetMapping("/checkEmailUnique")
    public boolean checkEmailUnique(String email) {
        return userService.checkEmailUnique(email);
    }

    @PostMapping("/emailOfUserName")
    public AjaxResult emailOfUserName(String userName) {
        return userService.emailOfUserName(userName);
    }

    //    待优化发送速度太慢
    @GetMapping("/sendEmail")
    public AjaxResult sendEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return AjaxResult.error("邮箱不能为空");
        }
        String emailCode = RandomUtil.getRandomValue();
//        发送邮件
        try {
            sendMailUtil.sendEmailMessage("知著论坛", "验证码：" + emailCode, emailCode);
        } catch (Exception e) {
            return AjaxResult.error("超时发送,如已发送请无视");
        }
        redisUtil.set(email + "emailCode", emailCode);
        redisUtil.expire(email + "emailCode", 300);
        return AjaxResult.success("发送成功");
    }

    @PostMapping("/register")
    public AjaxResult insertUser(User user, HttpServletRequest request) {
        user.setCreateIp(GetIpUtil.getIpAddr(request));
        return userService.register(user);
    }

    @PostMapping("/resetPassWord")
    public AjaxResult resetPassword(String userName, String email, String emailCode) {
        String emailCodeOfRedis = String.valueOf(redisUtil.get(email + "emailCode"));
        if (!redisUtil.exists(email + "emailCode")) {
            return AjaxResult.error("验证码过期,请重新获取");
        } else if (emailCode.equals(emailCodeOfRedis)) {
            return userService.forgetPassword(userName);
        } else {
            return AjaxResult.error("验证码错误,请重新填写");
        }
    }

    @PostMapping("/getNickNameById")
    public AjaxResult getNickNameById(int userId) {
        return AjaxResult.success(userService.getNickNameById(userId));
    }
}
