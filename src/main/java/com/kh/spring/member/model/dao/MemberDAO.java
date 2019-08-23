package com.kh.spring.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository("mDAO")
public class MemberDAO {

	@Autowired
	private SqlSessionTemplate sqlSession; // root-context.xml에서 작성한 bean으로 생성
	
	public Member selectmember(Member m) {
		
		return (Member)sqlSession.selectOne("memberMapper.selectOne", m);
	}

	public int insertMember(Member m) {
		return sqlSession.insert("memberMapper.insertMember", m);
	}

	public int updateMember(Member m) {
		return sqlSession.update("memberMapper.updateMember", m);
	}

	public int updatePwd(Member m) {
		return sqlSession.update("memberMapper.updatePwd", m);
	}

	public int deleteMember(String id) {
		return sqlSession.update("memberMapper.deleteMember", id);
	}


}
