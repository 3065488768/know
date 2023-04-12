package com.example.know.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * redis基本指令    set
     *
     * @param key 键
     * @param val 值
     * @return 返回是否插入成功
     */
    public boolean set(String key, Object val) {
        try {
            redisTemplate.opsForValue().set(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * redis基本指令    get
     *
     * @param key 键值
     * @return 返回值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * redis基本指令    move
     *
     * @param key     移除的键
     * @param dbIndex 那个数据库
     * @return 移除的结果
     */
    public Boolean move(String key, int dbIndex) {
        try {
            return redisTemplate.move(key, dbIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令    exists
     *
     * @param key 需要判断的键
     * @return 返回是否存在这个键
     */
    public Boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令    expire
     *
     * @param key     键
     * @param seconds 设置的秒数
     * @return 是否设置成功
     */
    public Boolean expire(String key, long seconds) {
        try {
            return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除key
     * @param key 删除的key
     * */
    public void deleteKey(String key){
        if(this.exists(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * redis基本指令    ttl
     *
     * @param key 键
     * @return 返回该键的剩余时间 -1表示没有设置时间 -2表示不存在这个键 -3表示发生错误
     */
    public Long ttl(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return -3L;
        }
    }


    /**
     * redis基本指令    type
     *
     * @param key 需要查看类型的键
     * @return 返回类型
     */
    public Object type(String key) {
        try {
            return redisTemplate.type(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * redis基本指令    append
     *
     * @param key       需要追加字符串的键
     * @param appendStr 需要追加的字符串
     * @return 追加后的字符数 如果没有这个字符串就新建一个
     */
    public Integer append(String key, String appendStr) {
        try {
            return redisTemplate.opsForValue().append(key, appendStr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * redis基本指令    strLen
     *
     * @param key 需要查看字符串长度的键
     * @return 字符串的长度 0表示不存在 -1表示发生错误
     */
    public Long strLen(String key) {
        try {
            return redisTemplate.opsForValue().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * redis基本指令    getRange
     *
     * @param key   需要查询的键
     * @param start 开始位置
     * @param end   结束位置
     * @return 返回查询的字符串
     */
    public String getRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForValue().get(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * redis基本指令    incr
     *
     * @param key 需要+1的键
     * @return 返回+1后的值
     */
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * redis基本指令    decr
     *
     * @param key 需要-1的键
     * @return 返回-1后的值
     */
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * redis基本指令    incrBy
     *
     * @param key 需要的键
     * @param val 加的值
     * @return 加后的值
     */
    public Long incrBy(String key, Long val) {
        return redisTemplate.opsForValue().increment(key, val);
    }

    /**
     * redis基本指令    decrBy
     *
     * @param key 需要的键
     * @param val 减的值
     * @return 减后的值
     */
    public Long decrBy(String key, Long val) {
        return redisTemplate.opsForValue().decrement(key, val);
    }

    /**
     * redis基本指令    setRange
     *
     * @param key   需要修改的键
     * @param index 下标
     * @param val   修改的值
     * @return 返回是否修改成功
     */
    public Boolean setRange(String key, Long index, String val) {
        try {
            redisTemplate.opsForValue().set(key, val, index);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令    setEx
     *
     * @param key  设置的键
     * @param val  设置的值
     * @param time 设置是时间 单位秒
     * @return 返回是否设置成功
     */
    public Boolean setEx(String key, String val, Long time) {
        try {
            redisTemplate.opsForValue().set(key, val, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令 存在这个键就失败    setNx
     *
     * @param key 需要设置的键
     * @param val 需要设置的值
     * @return 返回是否设置成功
     */
    public Boolean setNx(String key, Object val) {
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令 mSet
     *
     * @param map 需要批量增加的map集合
     * @return 返回是否成功
     */
    public Boolean mSet(Map<String, Object> map) {
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * redis基本指令 mGet
     *
     * @param list 批量查询的键集合
     * @return 返回值的集合
     */
    public List<Object> mGet(List<String> list) {
        try {
            return redisTemplate.opsForValue().multiGet(list);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * redis基本指令 getSet
     *
     * @param key 获取（修改）的键
     * @param val 修改的值
     * @return 返回修改之前的值
     */
    public Object getSet(String key, String val) {
        try {
            return redisTemplate.opsForValue().getAndSet(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * List命令   lPush 采用的头插入
     *
     * @param key 存入的键
     * @param val 存入的值
     * @return 存入后集合的个数
     */
    public Long lPush(String key, Object val) {
        try {
            return redisTemplate.opsForList().leftPush(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * List命令   rPush 采用的尾插入
     *
     * @param key 存入的键
     * @param val 存入的值
     * @return 存入后集合的个数
     */
    public Long rPush(String key, Object val) {
        try {
            return redisTemplate.opsForList().rightPush(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * List命令   lRange
     *
     * @param key   获取集合的键
     * @param start 开始的位置从0开始
     * @param end   结束的位置
     * @return 返回list的集合数据
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List命令   lPop
     *
     * @param key 需要弹出的键
     * @return 返回头部元素
     */
    public Object lPop(String key) {
        try {
            return redisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List命令   lPop
     *
     * @param key 需要弹出的键
     * @return 返回尾部元素
     */
    public Object rPop(String key) {
        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List命令   lIndex
     *
     * @param key   需要查询集合的键
     * @param index 查询集合的下标
     * @return 这个下标的值
     */
    public Object lIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * List命令   lLen
     *
     * @param key 需要查看长度的键
     * @return 返回这个集合的长度
     */
    public Long lLen(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * List命令   lRem
     *
     * @param key   需要移除集合的键
     * @param index 填一个数
     * @param val   移除的值
     * @return 返回移除的个数
     */
    public Long lRem(String key, Long index, Object val) {
        try {
            return redisTemplate.opsForList().remove(key, index, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * List命令   lRem
     *
     * @param key   需要截取的集合的键
     * @param start 开始位置，0最小
     * @param end   结束位置
     * @return 返回是否截取成功
     */
    public Boolean lTrim(String key, Long start, Long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * List命令   lSet
     *
     * @param key   需要修改的集合的键
     * @param index 需要修改的下标
     * @param val   修改的值
     * @return 返回是否修改成功
     */
    public Boolean lSet(String key, long index, Object val) {
        if (lLen(key) <= index) {
            return false;
        }
        try {
            redisTemplate.opsForList().set(key, index, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * List基本指令     lInsert 键 after 那个值 需要插入的值
     *
     * @param key   需要插入的键
     * @param pivot 需要插入到那个值
     * @param val   需要插入的值
     * @return 插入后的总数  返回-1表示插入失败
     */
    public Long lInsertAfter(String key, Object pivot, Object val) {
        try {
            return redisTemplate.opsForList().rightPush(key, pivot, val);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * List基本指令     lInsert 键 before 那个值 需要插入的值
     *
     * @param key   需要插入的键
     * @param pivot 需要插入到那个值
     * @param val   需要插入的值
     * @return 插入后的总数  返回-1表示插入失败
     */
    public Long lInsertBefore(String key, Object pivot, Object val) {
        try {
            return redisTemplate.opsForList().leftPush(key, pivot, val);
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /**
     * List基本指令     rPopLPush list1 list2
     *
     * @param key1 需要移除的最后元素的键
     * @param key2 需要增加元素的键
     * @return 移除的元素
     */
    public Object rPopLPush(String key1, String key2) {
        try {
            return redisTemplate.opsForList().rightPopAndLeftPush(key1, key2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * set基本指令  sAdd
     *
     * @param key 需要添加的键
     * @param val 需要添加的值
     * @return 返回插入的元素个数
     */
    public Long sAdd(String key, Object... val) {
        try {
            return redisTemplate.opsForSet().add(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * set基本指令  sMembers
     *
     * @param key 查询的set集合的键
     * @return set集合的所有元素
     */
    public Set<Object> sMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sisMember
     *
     * @param key 需要判断的键
     * @param val 需要判断段的值
     * @return 返回是否存在
     */
    public Boolean sisMember(String key, Object val) {
        try {
            return redisTemplate.opsForSet().isMember(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * set基本指令  sCard
     *
     * @param key 需要查询元素集合的个数的键
     * @return set集合的个数
     */
    public Long sCard(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * set基本指令  sRem 移除元素
     *
     * @param key 需要移除的set集合的键
     * @param val 需要移除的值，可以是多个
     * @return 返回移除的个数
     */
    public Long sRem(String key, Object... val) {
        try {
            return redisTemplate.opsForSet().remove(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * set基本指令  sRandMember 随机返回元素的个数
     *
     * @param key   需要操作的det集合的键
     * @param count 需要返回元素的个数
     * @return 返回随机元素的set集合
     */
    public Set<Object> sRandMember(String key, long count) {
        try {
            return redisTemplate.opsForSet().distinctRandomMembers(key, count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sPop 随机弹出一个元素
     *
     * @param key 需要操的set集合的键
     * @return 弹出的元素
     */
    public Object sPop(String key) {
        try {
            return redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sMove 把一个元素从一个集合移动到另一个集合
     *
     * @param oldSet 需要移动的集合
     * @param newSet 需要到的集合
     * @param val    移动的元素
     * @return 返回是否移动成功
     */
    public Boolean sMove(String oldSet, String newSet, Object val) {
        try {
            return redisTemplate.opsForSet().move(oldSet, val, newSet);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * set基本指令  sDiff 两个集合的差集  key1 - key2
     *
     * @param key1 第一个集合
     * @param key2 第二个集合
     * @return 返回差集的集合
     */
    public Set<Object> sDiff(String key1, String key2) {
        try {
            return redisTemplate.opsForSet().difference(key1, key2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sDiff 一个集合和多个集合的差集  key1 - key2 - key3 - ....
     *
     * @param key1      第一个集合
     * @param otherKeys 其他集合
     * @return 返回差集的集合
     */
    public Set<Object> sDiff(String key1, Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().difference(key1, otherKeys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sInter 一个集合和多个集合的交集
     *
     * @param key1
     * @param key2
     * @return
     */
    public Set<Object> sInter(String key1, String key2) {
        try {
            return redisTemplate.opsForSet().intersect(key1, key2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sInter 一个集合和多个集合的交集
     *
     * @param key1      需要交的集合
     * @param otherKeys 与它交的其他集合
     * @return 返回集合的交集
     */
    public Set<Object> sInter(String key1, Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().intersect(key1, otherKeys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sInter 多个集合的交集
     *
     * @param keys 多个集合
     * @return 返回集合的交集
     */
    public Set<Object> sInter(Collection<String> keys) {
        try {
            return redisTemplate.opsForSet().intersect(keys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sUnion 一个集合和多个集合的并集
     *
     * @param key       一个集合键
     * @param otherKeys 多个集合键的集合
     * @return 并集合
     */
    public Set<Object> sUnion(String key, Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().union(key, otherKeys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sUnion 多个集合的并集
     *
     * @param otherKeys 多个集合键的集合
     * @return 并集合
     */
    public Set<Object> sUnion(Collection<String> otherKeys) {
        try {
            return redisTemplate.opsForSet().union(otherKeys);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set基本指令  sUnion 两个个集合的并集
     *
     * @param key1 第一集合
     * @param key2 第二个集合
     * @return 并集合
     */
    public Set<Object> sUnion(String key1, String key2) {
        try {
            return redisTemplate.opsForSet().union(key1, key2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Hash基本指令  hSet 往hash存入一个map 键为k1 值v1
     *
     * @param key    需要存入的键
     * @param hKey   map中的一个键
     * @param hValue map中该键对应的值
     * @return 是否存储成功
     */
    public Boolean hSet(String key, Object hKey, Object hValue) {
        try {
            redisTemplate.opsForHash().put(key, hKey, hValue);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hash基本指令  hGet 取出hash存入一个map的键为k1的值
     *
     * @param key  需要取出的键
     * @param hKey 需要取出该键对应map的那个键的值
     * @return 返回该map中这个键的值
     */
    public Object hGet(String key, String hKey) {
        try {
            return redisTemplate.opsForHash().get(key, hKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hash基本指令  hMSet 批量存储map值
     *
     * @param key 存在的键
     * @param map 存储的map
     * @return 存储的map中的个数
     */
    public Integer hMSet(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return map.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Hash基本指令  hMSet 批量取出map值
     *
     * @param key   需要操作的键
     * @param hKeys 需要取出map中键的集合
     * @return 这些键的值
     */
    public List<Object> hMGet(String key, List<String> hKeys) {
        try {
            Collection<Object> collections = new CopyOnWriteArrayList<>(hKeys);
            return redisTemplate.opsForHash().multiGet(key, collections);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hash基本指令  hKeys  获取map中的全部键
     *
     * @param key 需要获取map的键
     * @return 返回键的集合
     */
    public Set<Object> hKeys(String key) {
        try {
            return redisTemplate.opsForHash().keys(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hash基本指令  hValS  获取map中的全部值
     *
     * @param key 需要获取map的键
     * @return 返回值的集合
     */
    public List<Object> hValS(String key) {
        try {
            return redisTemplate.opsForHash().values(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hash基本指令  hDel  删除hash中指定hash的指定的键
     *
     * @param key  指定hash的键
     * @param hKey 之间hash之中的键 为可变长参数
     * @return 返回删除的个数
     */
    public Long hDel(String key, Object... hKey) {
        try {
            return redisTemplate.opsForHash().delete(key, hKey);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Hash基本指令  hLen  获取hash中的个数
     *
     * @param key hash的键
     * @return 返回个数
     */
    public Long hLen(String key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Hash基本指令  hExists  判断hash中指定的值是否存在
     *
     * @param key  需要判断的hash的键
     * @param hKey 需要判断hash中的键
     * @return 是否存在
     */
    public Boolean hExists(String key, Object hKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hash基本指令  hGetAll  获取map中的全部键值对
     *
     * @param key 需要获取map的键
     * @return 返回所有键和值的map
     */
    public Map<Object, Object> hGetAll(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hash基本指令  hIncrBy  hash中键的值 + addNum
     *
     * @param key    hash的键
     * @param hKey   需要操作hash中的键
     * @param addNum 增加的数，Long类型
     * @return 返回增加后的结果
     */
    public Long hIncrBy(String key, Object hKey, Long addNum) {
        try {
            return redisTemplate.opsForHash().increment(key, hKey, addNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Hash基本指令  hIncrBy  hash中键的值 + addNum
     *
     * @param key    hash的键
     * @param hKey   需要操作hash中的键
     * @param addNum 增加的数，Double类型
     * @return 返回增加后的结果
     */
    public Double hIncrBy(String key, Object hKey, Double addNum) {
        try {
            return redisTemplate.opsForHash().increment(key, hKey, addNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Hash基本指令  hSetNx  hash中键如果存在就存储失败，反之
     *
     * @param key  需要存储的hash的键
     * @param hKey 存储hash中的键
     * @param val  存储hash中的键对应的值
     * @return 返回是否存储成功
     */
    public Boolean hSetNx(String key, Object hKey, Object val) {
        try {
            return redisTemplate.opsForHash().putIfAbsent(key, hKey, val);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * zSet基本指令  zAdd  向有序集合中添加一个元素
     *
     * @param key   有序集合的键
     * @param val   存入有序集合的值
     * @param score 该值对应的序号，用来排序用的
     * @return 返回是否存储成功
     */
    public Boolean zAdd(String key, Object val, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, val, score);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * zSet基本指令  zRange  获取指定位置的数据
     *
     * @param key   需要获取的有序集合的键
     * @param start 开始的位置从0开始
     * @param end   结束的位置
     * @return 返回获取的集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * zSet基本指令  zRangeByScore  获取指定位置的排好序的数据
     *
     * @param key 有序集合的键
     * @param min 最小的值
     * @param max 最大的值
     * @return 返回这个范围排好序的集合
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * zSet基本指令  zRangeByScore  获取指定位置的排好序的数据带上排序的值Score
     *
     * @param key 有序集合的键
     * @param min 最小的值
     * @param max 最大的值
     * @return 返回这个范围排好序的集合带有Score
     */
    public Set<ZSetOperations.TypedTuple<Object>> zRangeByScoreWithScores(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * zSet基本指令  zRem  移除有序集合中指定的元素
     *
     * @param key 有序集合的键
     * @param val 需要移除的元素
     * @return 返回移除的个数
     */
    public Long zRem(String key, Object... val) {
        try {
            return redisTemplate.opsForZSet().remove(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * zSet基本指令  zCard  获取有序集合的个数
     *
     * @param key 有序集合的键
     * @return 有序集合的个数
     */
    public Long zCard(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * zSet基本指令  zCount  获取有序集合指定范围内的个数
     *
     * @param key 有序集合的键
     * @param min 最小值范围
     * @param max 最大值范围
     * @return 返回指定范围有序集合的个数
     */
    public Long zCount(String key, double min, double max) {
        try {
            return redisTemplate.opsForZSet().count(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * Hyperloglog基本指令 pFAdd 储存数据
     *
     * @param key 需要存储的键
     * @param val 需要存储的值，可变参数
     * @return 返回1表示成功，0表示失败
     */
    public Long pFAdd(String key, Object... val) {
        try {
            return redisTemplate.opsForHyperLogLog().add(key, val);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Hyperloglog基本指令 pFCount 获取个数
     *
     * @param key 需要获取键，可变长参数
     * @return 当前这个Hyperloglog的个数
     */
    public Long pFCount(String... key) {
        try {
            return redisTemplate.opsForHyperLogLog().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * Hyperloglog基本指令 pFMerge 把其他log的数据去重后合并到一个log中
     *
     * @param key  等待合并后的新log的键
     * @param keys 需要合并的一些log的键，可变长参数
     * @return 返回合并后的个数
     */
    public Long pFMerge(String key, String... keys) {
        try {
            return redisTemplate.opsForHyperLogLog().union(key, keys);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * geospatial 地理位置  基本指令    geoAdd    增加一个位置的坐标 经纬度
     *
     * @param key    需要操作地理位置的集合的key
     * @param point  地理位置的对象，x表示经度，y表示维度
     * @param member 存储地理位置的名称
     * @return 返回存储的个数
     */
    public Long geoAdd(String key, Point point, Object member) {
        try {
            return redisTemplate.opsForGeo().add(key, point, member);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * geospatial 地理位置  基本指令    geoPos    获取多个城市的 经纬度
     *
     * @param key    需要获取的键
     * @param member 需要的获取的地理位置名称
     * @return 返回经纬度集合
     */
    public List<Point> geoPos(String key, Object... member) {
        try {
            return redisTemplate.opsForGeo().position(key, member);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * geospatial 地理位置  基本指令    geoDist    获取两个城市之间的距离
     *
     * @param key     地理位置的集合
     * @param member1 地理位置1
     * @param member2 地理位置2
     * @return 返回两个地理位置之间的距离，默认单位为米
     */
    public Distance geoDist(String key, Object member1, Object member2) {
        try {
            return redisTemplate.opsForGeo().distance(key, member1, member2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * geospatial 地理位置  基本指令    geoRadius    距离地理位置集合中一个地理位置距离范围内的地理位置
     *
     * @param key    地理位置集合的键
     * @param member 地理位置名称
     * @param radius 范围单位 米
     * @return 返回地理位置的集合信息
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadius(String key, Object member, double radius) {
        try {
            return redisTemplate.opsForGeo().radius(key, member, radius);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * geospatial 地理位置  基本指令    geoRadius    距离给定一个经纬度的地理位置距离范围内的地理位置
     *
     * @param key    地理位置集合的键
     * @param circle 给定经纬度和范围的对象 ，需要Point表经纬度和Distinct表示距离
     * @return 返回地理位置的集合信息
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadius(String key, Circle circle) {
        try {
            return redisTemplate.opsForGeo().radius(key, circle);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * geospatial 地理位置  基本指令    geoHash    返回一个位置的hash值，"wx4g0bm9xh0"
     *
     * @param key     需要操作的地理位置集合的键
     * @param members 需要获取hash值的地理位置
     * @return 返回hash值的集合
     */
    public List<String> geoHash(String key, Object... members) {
        try {
            return redisTemplate.opsForGeo().hash(key, members);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

