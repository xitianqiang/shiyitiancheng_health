package com.shiyitiancheng.service;

import com.shiyitiancheng.pojo.Member;

import java.util.List;

public interface MemberService {

    Member login(String telephone);

    void add(Member member);

    List<Integer> findMemeberCountByMonths(List<String> months);
}
