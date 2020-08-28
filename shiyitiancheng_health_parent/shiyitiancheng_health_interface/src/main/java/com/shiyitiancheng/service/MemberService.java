package com.shiyitiancheng.service;

import com.shiyitiancheng.pojo.Member;

public interface MemberService {

    Member login(String telephone);

    void add(Member member);
}
