package com.itheima.dao;

import com.itheima.pojo.CheckItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-dao.xml")
public class testDao {

    @Autowired
    private CheckItemDao checkItemDao;

    /*@Test
    public void test(){
        CheckItem checkItem = checkItemDao.findById(30);
        System.out.println(checkItem);
    }*/
}
