package com.kh.spring.memo.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.memo.model.service.MemoServiceImpl;

@Repository("mmDAO")
public class MemoDAO {
	
//	private Logger logger = LoggerFactory.getLogger(MemoServiceImpl.class);
	
	@Autowired
	SqlSessionTemplate sqlSession;

	public ArrayList<HashMap<String, String>> selectMemoList() {
//		logger.debug("[before]MemoDAO - selectMemoList()");
//		
//		ArrayList<HashMap<String, String>> list = (ArrayList)sqlSession.selectList("memoMapper.selectMemoList");
//		
//		logger.debug("[before]MemoDAO - selectMemoList()");
		
		return  (ArrayList)sqlSession.selectList("memoMapper.selectMemoList");
	}

	public int insertMemo(Map<String, String> map) {
//		logger.debug("[before]MemoDAO - insertMemo()");
//		
//		int result = sqlSession.insert("memoMapper.insertMemo", map);
//		
//		logger.debug("[before]MemoDAO - insertMemo()");
		
		return sqlSession.insert("memoMapper.insertMemo", map);
	}

}
