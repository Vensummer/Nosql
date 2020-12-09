package com.bjtu.redis;

import com.sun.org.apache.xpath.internal.operations.String;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.swing.text.html.ObjectView;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class RedisDao {
    private RedisTemplate redisTemplate;
    Logger logger = LoggerFactory.getLogger(RedisDao.class);
    /**
     *  写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key,Object value){
        logger.info("key+value{}"+"->"+key+":"+value);
        boolean result = false;
        try{
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        }catch(Exception e){
            logger.info(e.getMessage());
        }
        return result;
    }
    /**
     * 判断缓存是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key){
        logger.info("key{}"+key);
        return redisTemplate.hasKey(key);
    }
    /**
     * 删除对应的value
     * @param key
     */
    public void RemoveKey(final String key){
        logger.info("key{}"+"->"+key);
        if(exists(key)){
            redisTemplate.delete(key);
        }

    }
    /**
     * 批量删除value
     * @param keys
     */
    public void RemoveAllValues(final String...keys){
        logger.info("keys is length{}->"+keys.length);
        if(keys.length>0&&!StringUtils.isEmpty(keys)){
            for(String key:keys){
                RemoveKey(key);
            }
        }
    }
    /**
     * 批量删除key
     * @param pattern
     */
    public void RemoveAllKeys(final String pattern){
        Set<Serializable>keys = redisTemplate.keys(pattern);
        if (keys.size() > 0){
            redisTemplate.delete(keys);
        }
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key)throws Exception{
        logger.info("key{}->"+key);
        Object result =null;
        ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        if(StringUtils.isEmpty(result)){
            throw new Exception("缓存读取状态：failure.....");
        }
        return result;
    }
    /**
     * 哈希添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void HashSet(String key, Object hashKey, Object value){
        HashOperations<String,Object,Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }
    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object HashGet(String key,Object hashKey){
        HashOperations<String,Object,Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }
    /**
     * 列表添加
     * @param k
     * @param v
     */
    public void IPush(String k,Object v){
        ListOperations<String,Object> list = redisTemplate.opsForList();
        list.rightPush(k,v);
    }
    /**
     *  列表获取
     * @param k
     * @param l
     * @param l1
     * @return
     */
    public List<Object>IRange(String k,long l,long l1){
        ListOperations<String,Object> list = redisTemplate.opsForList();
        return list.range(k,l,l1);
    }
    /**
     * 集合添加
     * @param key
     * @param value
     */
    public void AddMembers(String key,Object value){
        SetOperations<String,Object> set = redisTemplate.opsForSet();
        set.add(key,value);
    }
    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set<Object> setMembers(String key){
        SetOperations<String,Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }
    /**
     * 添加有序集合
     * @param key
     * @param value
     * @param s
     * @return
     */
    public void AddRange(String key,Object value,double s){
        ZSetOperations<String,Object> zset = redisTemplate.opsForZSet();
        zset.add(key,value,s);
    }
    /**
     * 获取有序集合
     * @param key
     * @param s
     * @param s0
     */
    public Set<Object> RangeByScore(String key,double s,double s0){
        ZSetOperations<String,Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key,s,s0);
    }
}
