package com.example.know.aspect;

import com.example.know.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AvoidDuplicateSubmissionsAspect {

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.example.know.aop.AvoidDuplicateSubmissions)")
    public  void annotationPointCut() {
    }

    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
    }

    @Around("annotationPointCut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable{
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String knowToken = request.getHeader("token");
            log.info(knowToken + "访问" + joinPoint.getClass().getName());
            if (!redisUtil.exists(knowToken)) {
                redisUtil.set(knowToken, true);
                redisUtil.expire(knowToken, 30);
            } else {
                log.warn("重复点击");
            }
        }
    }

    @After("annotationPointCut()")
    public void after(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            String knowToken = request.getHeader("token");
            log.info("after");
        }
    }
}
