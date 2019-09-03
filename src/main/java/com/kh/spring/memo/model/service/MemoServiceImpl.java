package com.kh.spring.memo.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.memo.model.dao.MemoDAO;

@Service("mmService")
public class MemoServiceImpl implements MemoService {
	
//	private Logger logger = LoggerFactory.getLogger(MemoServiceImpl.class);
	
	@Autowired
	MemoDAO mmDAO;

	@Override
	public ArrayList<HashMap<String, String>> selectMemoList() {
//		logger.debug("[before]Memosevice - selectMemoList()");
//		
//		ArrayList<HashMap<String, String>> list = mmDAO.selectMemoList();
//		
//		logger.debug("[after]Memosevice - selectMemoList()");
//		
//		return list;
		return mmDAO.selectMemoList();
	}

	@Override
	public int insertMemo(Map<String, String> map) {
//		logger.debug("[before]Memosevice - selectMemoList()");
//		
//		int result = mmDAO.insertMemo(map);
//		
//		logger.debug("[after]Memosevice - selectMemoList()");
		
		return mmDAO.insertMemo(map);
	}

	
}
