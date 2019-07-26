package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 清理图片任务类
 */
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 清理图片
     */
    public void clearImage(){
        System.out.println("任务运行了。。。。");
        //第一步：
        //1.用户选择图片上传后 需要往redis记录数据 key:setmealPicResources value:图片名称
        //2.当用户点击确定 记录redis key:setmealPicDbResources value:图片名称
        //第二步：
        //3.从redis中将两个集合中的数据相减  传入两个key参数就可以了
        //sdiff:两个相减  srem：删除集合中数据
        Set<String> diff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(diff != null && diff.size()>0){
            for (String pic : diff) {
                System.out.println("图片被清理了。。。。");
                //pic:垃圾图片名称
                //4.清理图片 redis删除  清理七牛云
                QiniuUtils.deleteFileFromQiniu(pic);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);//清理的集合key 和集合中图片名称
            }
        }
    }
}
