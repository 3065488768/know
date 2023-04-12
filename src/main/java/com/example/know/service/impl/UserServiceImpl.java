package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.know.component.WebSocketServer;
import com.example.know.dao.*;
import com.example.know.entity.*;
import com.example.know.enumeration.TransactionType;
import com.example.know.security.MyUserDetailsService;
import com.example.know.security.utils.JwtProperties;
import com.example.know.security.utils.JwtTokenUtil;
import com.example.know.service.UserService;
import com.example.know.util.AjaxResult;
import com.example.know.util.Md5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户业务层实现类
 *
 * @author bookWorm
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Resource
    private UserRoleDao userRoleDao;

    @Resource
    private PostDao postDao;

    @Resource
    private AccessDao accessDao;

    @Resource
    private CommentDao commentDao;

    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MyUserDetailsService userDetailsService;

    @Resource
    private LikeDao likeDao;

    @Resource
    private TransactionsDao transactionsDao;

    @Resource
    private CollectionDao collectionDao;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private LetterDao letterDao;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public boolean checkUserNameUnique(String userName) {
        if (StringUtils.isBlank(userName)) {
            return false;
        }
        return userDao.checkOfUserNameUnique(userName) == null;
    }

    @Override
    public boolean checkNickNameUnique(String nickName) {
        return userDao.checkOfNickNameUnique(nickName) == null;
    }

    @Override
    public boolean checkEmailUnique(String email) {
        return userDao.checkOfEmailUnique(email) == null;
    }

    @Override
    public AjaxResult emailOfUserName(String userName) {
        if (!this.checkUserNameUnique(userName)) {
            return AjaxResult.success(userDao.selectOfUserName(userName).getEmail());
        } else {
            return new AjaxResult(AjaxResult.Type.USER_ACCOUNT_NOT_EXIST, "查无用户");
        }
    }

    @Override
    public AjaxResult forgetPassword(String userName) {
        User user = userDao.selectOfUserName(userName);
        user.setPassword(Md5Utils.hash(user.getSalt() + "@123456"));
        userDao.updateById(user);
        return AjaxResult.success("重置成功");
    }

    @Override
    public AjaxResult register(User user) {
        user.setSalt(Md5Utils.hash(user.getCreateIp() + System.currentTimeMillis()));
        user.setPassword(Md5Utils.hash(user.getSalt() + user.getPassword()));
        user.setNickName(user.getUserName());
        try {
            userDao.insert(user);
            return AjaxResult.success("注册成功");
        } catch (Exception e) {
            logger.error("注册失败" + e);
            return AjaxResult.error("注册失败");
        }
    }

    @Override
    public AjaxResult ModifyData(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", user.getUserName());
        userDao.update(user, queryWrapper);
        return AjaxResult.success("修改成功");
    }

    @Override
    public AjaxResult postOfUser(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("create_by", user.getUserId());
        List<Post> list = postDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult commentOfUser(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("comment_by", user.getUserId());
        List<Post> list = commentDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult accessOfUser(User user) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("access_by", user.getUserId());
        List<Post> list = accessDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult userOfRanking() {
        return AjaxResult.success(userDao.userOfRanking());
    }

    @Override
    public Long getCountNumber() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        return userDao.selectCount(queryWrapper);
    }

    @Override
    public AjaxResult login(String username, String password) {
        User user = userDao.selectOfUserName(username);
        if (Objects.isNull(user)) {
            return new AjaxResult(AjaxResult.Type.USER_ACCOUNT_NOT_EXIST, "用户账号不存在");
        }
        password = Md5Utils.hash(user.getSalt() + password);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!password.equals(userDetails.getPassword())) {
            return new AjaxResult(AjaxResult.Type.USER_PASSWORD_ERROR, "密码错误");
        }
        if (!userDetails.isEnabled()) {
            return new AjaxResult(AjaxResult.Type.USER_ACCOUNT_LOCKED, "用户停用");
        }
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            logger.error("登陆异常");
        }
//
        String token = jwtProperties.getTokenPrefix() + jwtTokenUtil.generateToken(userDetails);
        LambdaQueryWrapper<UsersRole> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(UsersRole::getUid, user.getUserId());
        List<UsersRole> usersRoleList = userRoleDao.selectList(lambdaQueryWrapper);
        if (usersRoleList.size() != 0) {
            return AjaxResult.success(token, true);
        }
        return AjaxResult.success(token);
    }

    @Override
    public User getUser(String username) {
        return userDao.selectOfUserName(username);
    }

    @Override
    public int getUserBookCurrency(String username) {
        int bookCurrency = userDao.selectOfUserName(username).getBookCurrency();
        return bookCurrency;
    }

    @Override
    @Transactional
    public boolean transferAccounts(String username, int userId, int currencyNumber) {
        User payer = userDao.selectOfUserName(username);
        if (payer.getUserId() == userId) return false;
        if (payer.getBookCurrency() < currencyNumber) return false;
        payer.setBookCurrency(payer.getBookCurrency() - currencyNumber);
        User payee = userDao.selectById(userId);
        payee.setBookCurrency(payee.getBookCurrency() + currencyNumber);
        synchronized (this) {
            //手动回滚
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            int i = userDao.updateById(payer);
            int j = userDao.updateById(payee);
            if (i == 1 && j == 1) {
                Transactions transactions = new Transactions();
                transactions.setPayerId(payer.getUserId());
                transactions.setPayeeId(userId);
                transactions.setTransactionsNumber(currencyNumber);
                transactions.setTransactionsType(TransactionType.REWARD.getTypeId());
                transactionsDao.insert(transactions);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean checkUserIsLikeThisPost(String username, int postId) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("liked_id", postId);
        queryWrapper.eq("like_by", userId);
        queryWrapper.eq("like_type", '0');
        Like like = likeDao.selectOne(queryWrapper);
        if (Objects.isNull(like)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkUserIsCollectionThisPost(String username, int postId) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("collected_id", postId);
        queryWrapper.eq("collection_by", userId);
        queryWrapper.eq("collection_type", '0');
        Collection collection = collectionDao.selectOne(queryWrapper);
        if (Objects.isNull(collection)) {
            return false;
        }
        return true;
    }

    @Override
    public List getUserList() {
        return userDao.selectList(null);
    }

    @Override
    public int setUserStatus(char flag, int userId) {
        User user = userDao.selectOfUserId(userId);
        if (flag == '0' || flag == '1') {
            user.setStatus(flag);
        } else {
            return 0;
        }
        return userDao.updateById(user);
    }

    @Override
    public String getNickNameById(int userId) {
        return userDao.selectOfUserId(userId).getNickName();
    }
}
