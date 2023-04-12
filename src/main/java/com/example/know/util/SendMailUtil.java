package com.example.know.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

/**
 * 邮件发送工具类
 *
 * @author bookWorm
 * @Date 2022/12/14
 */
@Component
public class SendMailUtil {
    @Value("${spring.mail.username}")
    private final static String USERNAME = "3065488768@qq.com";
    @Autowired
    private JavaMailSender mailSender;
    private static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

    /**
     * 读取邮件模板
     * 替换模板中的信息
     *
     * @param emailCode 邮箱验证码
     * @return
     */
    public String buildContent(String emailCode) {
        //加载邮件html模板
        Resource resource = new ClassPathResource("mailTemplate.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            logger.info("发送邮件读取模板失败{}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), emailCode);
    }

    public boolean sendEmailMessage(String toUser, String subject, String emailCode) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            //获取验证码 存入redis

//            redisUtil.set(Constant.CUSTOMER_EMAIL + email, randomCode, 1800);
            //邮箱发送内容组成
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setText(buildContent(emailCode), true);
            helper.setTo(toUser);
            helper.setFrom(USERNAME);
            mailSender.send(message);
            logger.info("发送" + toUser + "成功");
            return true;
        } catch (MessagingException e) {
            logger.error("发送" + toUser + "失败");
            return false;
        }
    }
}
