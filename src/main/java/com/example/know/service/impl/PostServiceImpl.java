package com.example.know.service.impl;

import com.alibaba.fastjson2.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.*;
import com.example.know.entity.*;
import com.example.know.entity.Collection;
import com.example.know.enumeration.FailedReasonType;
import com.example.know.enumeration.Setting;
import com.example.know.service.PostService;
import com.example.know.util.AjaxResult;
import com.example.know.util.RedisUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 处理 post 的相关请求
 *
 * @author bookWorm
 */
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private SimplePostDao simplePostDao;

    @Resource
    private PostDao postDao;

    @Resource
    private UserDao userDao;

    @Resource
    private NoticeDao noticeDao;

    @Resource
    private AccessDao accessDao;

    @Resource
    private CommentDao commentDao;

    @Resource
    private LikeDao likeDao;

    @Resource
    private CollectionDao collectionDao;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private BookTypeDao bookTypeDao;

    @Resource
    private BookDao bookDao;

    @Override
    public AjaxResult getPostByDay(int quantity, int day) {
        QueryWrapper<SimplePost> queryWrapper = new QueryWrapper<>();
        if (day >= 0) {
            queryWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL " + day + " DAY) <= date(create_time)");
        }
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("create_time");
        PageHelper.startPage(0, quantity);
        List<SimplePost> posts = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(posts);
    }

    @Override
    public AjaxResult getHotPost(int quantity, int day) {
        QueryWrapper<SimplePost> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL " + day + " DAY) <= date(create_time)");
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("heat_degree");
        PageHelper.startPage(0, quantity);
        List<SimplePost> posts = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(posts);
    }

    @Override
    public AjaxResult getCustomizationPost(String datetime, int rankType, int begin, int number) {
        QueryWrapper<SimplePost> queryWrapper = new QueryWrapper();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        queryWrapper.apply(true, "create_time between '" + dateFormat.format(DateUtils.parseDate(datetime + " 00:00:00")) + "' and '" + dateFormat.format(DateUtils.parseDate(datetime + " 23:59:59")) + "'");
        queryWrapper.eq("status", '0');
        switch (rankType) {
            case 0:
                break;
            case 1:
                queryWrapper.orderByDesc("create_time");
                break;
            case 2:
                queryWrapper.orderByDesc("heat_degree");
                break;
            default:
                return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }
        PageHelper.startPage(begin, number);
        List<SimplePost> posts = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(posts);
    }

    @Override
    public AjaxResult getFuzzySearchPost(String parameter, int type, int postType, int begin, int number, int rankType, String datetimeStart, String datetimeEnd) {

        QueryWrapper<SimplePost> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        if (postType == 0) {
            queryWrapper.eq("post_type", "0");
        } else if (postType == 1) {
            queryWrapper.eq("post_type", "1");
        } else {
            return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }
        switch (rankType) {
            case 0:
                break;
            case 1:
                queryWrapper.orderByDesc("create_time");
                break;
            case 2:
                queryWrapper.orderByDesc("heat_degree");
                break;
            default:
                return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }
        if (!datetimeStart.isBlank() && !datetimeEnd.isBlank()) {
            Date dateStart = DateUtils.parseDate(datetimeStart + " 00:00:00");
            Date dateRestrict = DateUtils.parseDate(datetimeEnd + " 23:59:59");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            queryWrapper.apply(true, "create_time between '" + dateFormat.format(dateStart) + "' and '" + dateFormat.format(dateRestrict) + "'");
        }
        if (type < 0) {

        } else if (type == 0 || type == 2) {
            queryWrapper.like("post_title", parameter);
        } else if (type == 1 || type == 2) {
            queryWrapper.like("post_content", parameter);
        } else {
            return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }
        //对匹配的字符长短进行排序

        PageHelper.startPage(begin, number);
        List<SimplePost> list = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult releasePost(Post post, String username) {
        int userId = userDao.selectOfUserName(username).getUserId();
        Book book = bookDao.selectById(post.getBookId());
        BookType bookType = bookTypeDao.selectById(book.getBookTypeId());
        int plateId = bookType.getPlateId();
        post.setPlateId(plateId);
        post.setCreateBy(userId);
        post.setStatus('1');
        post.setPostType('0');
        postDao.insert(post);
        return AjaxResult.success("帖子已提交");
    }

    @Override
    public AjaxResult submitForReview(int postId) {
        Post post = postDao.selectById(postId);
        post.setStatus('1');
        postDao.updateById(post);
        return AjaxResult.success("您的帖子进入审核，请耐心等待");
    }

    @Override
    public AjaxResult getToBeReviewed(int start, int number) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", "1");
        queryWrapper.orderByAsc("create_time");
        PageHelper.startPage(start, number);
        List list = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult auditSucceeded(int postId, int status) {
        Post post = postDao.selectById(postId);
        post.setStatus((char) status);
//        post.setAuditTime(new LocalDateTime());
        postDao.updateById(post);
        return AjaxResult.success("审核成功");
    }

    @Override
    public AjaxResult auditFailed(int postId, int reasonType, String reasonContent) {
        Post post = postDao.selectById(postId);
        post.setStatus('2');
//        post.setAuditTime(new Date());
        int author = post.getCreateBy();
        Notice notice = new Notice();
        notice.setNoticeContent("您在" + post.getCreateTime() + "时创建的" + "帖子审核失败" +
                "原因类型：" + FailedReasonType.getContentByType(reasonType) +
                "说明" + reasonContent);
        notice.setNoticeBy(author);
        noticeDao.insert(notice);
        return AjaxResult.success("发送成功");
    }

    @Override
    public AjaxResult auditOfBatch(int[] postIds, char type) {
        int i = postDao.batchModification(postIds, type);
        if (i == postIds.length) {
            return AjaxResult.success("全部处理成功");
        } else {
            return AjaxResult.warn("部分处理成功");
        }
    }

    @Override
    public AjaxResult auditOfBatch(int postId, char type) {
        Post post = postDao.selectById(postId);
        post.setStatus(type);
        postDao.updateById(post);
        return AjaxResult.success("全部处理成功");
    }


    @Override
    public AjaxResult getSimplePostAndAuthor(int flag) {
        return AjaxResult.success(simplePostDao.getSimplePostAndAuthor(flag));
    }

    @Override
    public AjaxResult getExhibitionPost(String parameter, int type, int[] plateId, char postType, int begin, int number, int rankType, String datetimeStart, String datetimeEnd) {
        Map map = new HashMap();
        map.put("size", simplePostDao.getFuzzySearchPost(plateId, postType, parameter, type, rankType, datetimeStart, datetimeEnd).size());
        PageHelper.startPage(begin, number);
        map.put("list", simplePostDao.getFuzzySearchPost(plateId, postType, parameter, type, rankType, datetimeStart, datetimeEnd));
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getPostOfExhibition(int flag) {
        switch (flag) {
            case 0:
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("status", '0');
                queryWrapper.orderByDesc("create_time");
                PageHelper.startPage(0, 5);
                return AjaxResult.success(simplePostDao.selectList(queryWrapper));
            case 1:
                PageHelper.startPage(0, 5);
                return AjaxResult.success(simplePostDao.getSimplePostOfLatestComments());
            case 2:
                return getHotPost(5, 7);
            case 3:
                return getHotPost(5, 30);
            default:
                return new AjaxResult(AjaxResult.Type.PARAM_NOT_VALID, "参数无效");
        }

    }

    @Override
    public Long getCountNumber(int day) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        if (day > 0) {
            queryWrapper.apply(true, "DATE_SUB(CURDATE(), INTERVAL " + day + " DAY) <= date(create_time)");
        }
        return simplePostDao.selectCount(queryWrapper);
    }

    @Override
    public AjaxResult getPostById(int postId, String username, String ip) {
        Map map = new HashMap(3);
        if (StringUtils.hasLength(username) && username != null) {
            User user = userDao.selectOfUserName(username);
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("create_by", user.getUserId());
            queryWrapper.eq("post_id", postId);
            boolean postIsNull = Objects.isNull(postDao.selectOne(queryWrapper));
            if (!Objects.isNull(user) && postIsNull) {
                if (!redisUtil.exists(username + "accent" + postId)) {
                    redisUtil.setEx(username + "accent" + postId, "", 2000000L);
                    accessDao.insert(new Access(ip, postId, user.getUserId()));
                }
            }
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("liked_id", postId);
            queryWrapper1.eq("like_type", '0');
            queryWrapper1.eq("like_by", user.getUserId());
            if (likeDao.selectOne(queryWrapper1) != null) {
                map.put("isLike", true);
            }
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("collected_id", postId);
            queryWrapper2.eq("collection_type", '0');
            queryWrapper2.eq("collection_by", user.getUserId());
            if (collectionDao.selectOne(queryWrapper2) != null) {
                map.put("isCollection", true);
            }
        } else {
            map.put("isLike", false);
            map.put("isCollection", false);
        }
        Map post = postDao.getCompletePost(postId);
        map.put("post", post);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getPostByBookTypeId(int[] typeId, int start, int number) {
        PageHelper.startPage(start, number);
        return AjaxResult.success(simplePostDao.postIdByBookTypeId(typeId));
    }

    @Override
    public AjaxResult getPostByPlateId(int plateId, int start, int number) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        queryWrapper.eq("plate_id", plateId);
//        Long aLong = simplePostDao.selectCount(queryWrapper);
        List<SimplePost> list = simplePostDao.selectList(queryWrapper);
        return AjaxResult.success(list);
    }

    @Override
    public boolean likePost(String username, int postId) {
        int userId = userDao.selectOfUserName(username).getUserId();
        int createBy = postDao.selectById(postId).getCreateBy();
//        if(createBy == userId) throw new Exception();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("liked_id", postId);
        queryWrapper.eq("like_type", '0');
        queryWrapper.eq("like_by", userId);
        Like like = likeDao.selectOne(queryWrapper);
        if (Objects.isNull(like)) {
            Like like1 = new Like();
            like1.setLikedId(postId);
            like1.setLikeBy(userId);
            like1.setLikeType('0');
            likeDao.insert(like1);
            return true;
        } else {
            likeDao.deleteById(like.getLikeId());
            return false;
        }
    }

    @Override
    public boolean collectionPost(String username, int postId) {
        int userId = userDao.selectOfUserName(username).getUserId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("collected_id", postId);
        queryWrapper.eq("collection_type", '0');
        queryWrapper.eq("collection_by", userId);
        Collection collection = collectionDao.selectOne(queryWrapper);
        if (Objects.isNull(collection)) {
            Collection collection1 = new Collection();
            collection1.setCollectedId(postId);
            collection1.setCollectionBy(userId);
            collection1.setCollectionType('0');
            collectionDao.insert(collection1);
            return true;
        } else {
            collectionDao.deleteById(collection.getCollectionId());
            return false;
        }
    }

    @Override
    public List getRecommendPost(int postId, int startPage, int number) {
        Post post = postDao.selectById(postId);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("post_id", postId);
        queryWrapper.eq("status", '0');
        queryWrapper.orderByDesc("post_type = 3");
        queryWrapper.orderByDesc("book_id = " + post.getBookId());
        PageHelper.startPage(startPage, number);
        return simplePostDao.selectList(queryWrapper);
    }

    @Override
    public List getPostList(char status, int orderBy, char postType, int startPage) {
        PageHelper.startPage(startPage, Setting.SHOWNUMBER);
        return postDao.getPostList(status, orderBy, postType, startPage);
    }

    @Override
    public int getPostListNumber(char status, int orderBy, char postType, int startPage) {
        return postDao.getPostList(status, orderBy, postType, startPage).size();
    }

    @Override
    public int updatePostType(int postId, int type) {
        Post post = postDao.selectById(postId);
        post.setPostType((char) type);
        return postDao.updateById(post);
    }
}
