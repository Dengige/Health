package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * 会员服务接口层
 */
public interface MemberSerivce {
    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 注册会员
     * @param member
     */
    void add(Member member);

    /**
     * 根据月份查询 会员数量
     * @param months
     * @return
     */
    List<Integer> findMemberCountByMonth(List<String> months);
}
