package com.example.know.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.know.dao.CollectionDao;
import com.example.know.dao.DigestDao;
import com.example.know.dao.LikeDao;
import com.example.know.dao.UserDao;
import com.example.know.entity.Collection;
import com.example.know.entity.Digest;
import com.example.know.entity.Like;
import com.example.know.entity.User;
import com.example.know.service.DigestService;
import com.example.know.util.AjaxResult;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author bookWorm
 */
@Service
public class DigestServiceImpl implements DigestService {

    @Resource
    private DigestDao digestDao;

    @Resource
    private UserDao userDao;

    @Resource
    private LikeDao likeDao;

    @Resource
    private CollectionDao collectionDao;

    @Override
    public AjaxResult getDigestOfRecommend() {
        PageHelper.startPage(0, 10);
        return AjaxResult.success(digestDao.getDigestAndBook());
    }

    @Override
    public Long getCountNumber() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", '0');
        return digestDao.selectCount(queryWrapper);
    }

    @Override
    public AjaxResult getDigestByBookId(int bookId, String username, int start, int number) {
        QueryWrapper queryWrapperByDigest = new QueryWrapper();
        queryWrapperByDigest.eq("book_id", bookId);
        PageHelper.startPage(start, number);
        List<Digest> digestList = digestDao.selectList(queryWrapperByDigest);
        List<String> nickNameList = new ArrayList<>();
        List<Long> likeList = new ArrayList<>();
        List<Long> collectionList = new ArrayList<>();
        List<Boolean> isLikeList = new ArrayList<>();
        List<Boolean> isCollectionList = new ArrayList<>();
        for (Digest digest : digestList) {
            nickNameList.add(userDao.selectById(digest.getAuthorId()).getNickName());
//            获取书摘喜欢的数量
            QueryWrapper queryWrapperByLike = new QueryWrapper();
            queryWrapperByLike.eq("like_type", '2');
            queryWrapperByLike.eq("liked_id", digest.getDigestId());
            Long likeNumber = likeDao.selectCount(queryWrapperByLike);
            likeList.add(likeNumber);
//            收藏书摘的数量
            QueryWrapper queryWrapperByCollection = new QueryWrapper();
            queryWrapperByCollection.eq("collection_type", '2');
            queryWrapperByCollection.eq("collected_id", digest.getDigestId());
            Long collectionNumber = collectionDao.selectCount(queryWrapperByCollection);
            collectionList.add(collectionNumber);

        }
        //喜欢收藏数(以及当前用户是否收藏或喜欢)
        if (username != null) {
            int userId = userDao.selectOfUserName(username).getUserId();
            for (Digest digest : digestList) {
                QueryWrapper queryWrapperByLike = new QueryWrapper();
                queryWrapperByLike.eq("like_type", '2');
                queryWrapperByLike.eq("liked_id", digest.getDigestId());
                queryWrapperByLike.eq("like_by", userId);
                Like like = likeDao.selectOne(queryWrapperByLike);
                if (like != null) {
                    isLikeList.add(true);
                } else {
                    isLikeList.add(false);
                }
                QueryWrapper queryWrapperByCollection = new QueryWrapper();
                queryWrapperByCollection.eq("collection_type", '2');
                queryWrapperByCollection.eq("collected_id", digest.getDigestId());
                queryWrapperByCollection.eq("collection_by", userId);
                Collection collection = collectionDao.selectOne(queryWrapperByCollection);
                if (collection != null) {
                    isCollectionList.add(true);
                } else {
                    isCollectionList.add(false);
                }

            }
        } else {
            for (int i = 0; i < digestList.size(); i++) {
                isLikeList.add(false);
                isCollectionList.add(false);
            }
        }
        Map map = new HashMap(6);
        map.put("digestList", digestList);
        map.put("authorList", nickNameList);
        map.put("likeList", likeList);
        map.put("collectionList", collectionList);
        map.put("isLikeList", isLikeList);
        map.put("isCollectionList", isCollectionList);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult digestByUser(String username, int digestId, int type) {
        int userId = userDao.selectOfUserName(username).getUserId();
        Digest digest = digestDao.selectById(digestId);
        if (digest.getAuthorId() != userId) {
            if (type == 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("like_type", '2');
                queryWrapper.eq("like_by", userId);
                queryWrapper.eq("liked_id", digestId);
                Like like = likeDao.selectOne(queryWrapper);
                if (Objects.isNull(like)) {
                    Like like1 = new Like();
                    like1.setLikedId(digestId);
                    like1.setLikeType('2');
                    like1.setLikeBy(userId);
                    likeDao.insert(like1);
                    return AjaxResult.success("喜欢成功");
                } else {
                    likeDao.deleteById(like.getLikeId());
                    return AjaxResult.success("取消喜欢成功");
                }
            } else if (type == 1) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("collection_type", '2');
                queryWrapper.eq("collection_by", userId);
                queryWrapper.eq("collected_id", digestId);
                Collection collection = collectionDao.selectOne(queryWrapper);
                if (Objects.isNull(collection)) {
                    Collection collection1 = new Collection();
                    collection1.setCollectedId(digestId);
                    collection1.setCollectionType('2');
                    collection1.setCollectionBy(userId);
                    collectionDao.insert(collection1);
                    return AjaxResult.success("收藏成功");
                } else {
                    collectionDao.deleteById(collection.getCollectionId());
                    return AjaxResult.success("取消收藏成功");
                }
            } else {
                return new AjaxResult(AjaxResult.Type.PARAM_NOT_COMPLETE, "参数非法");
            }
        } else {
            return AjaxResult.error("请勿自娱自乐");
        }
    }
}
