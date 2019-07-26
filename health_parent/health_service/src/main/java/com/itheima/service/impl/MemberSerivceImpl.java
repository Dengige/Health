package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员业务服务实现类
 */
@Service(interfaceClass = MemberSerivce.class)
@Transactional
public class MemberSerivceImpl implements MemberSerivce {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 根据月份查询 会员数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        //定义一个集合返回统计会员数量
        List<Integer> rsCount = new ArrayList<>();
        for (String month : months) {
            month = month + "-31";
            Integer count = memberDao.findMemberCountBeforeDate(month);
            rsCount.add(count);
        }
        return rsCount;
    }
}
