package com.shiyitiancheng.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shiyitiancheng.dao.MemberDao;
import com.shiyitiancheng.pojo.Member;
import com.shiyitiancheng.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
