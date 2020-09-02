package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shiyitiancheng.dao.MemberDao;
import com.shiyitiancheng.pojo.Member;
import com.shiyitiancheng.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member login(String telephone) {
        Member member = memberDao.findByTelephone(telephone);
        return member;
    }

    @Override
    @Transactional
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemeberCountByMonths(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            String date = month + ".31";
            Integer memberCountBeforeDate = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(memberCountBeforeDate);
        }
        return memberCount;
    }
}
