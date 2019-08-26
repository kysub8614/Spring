package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;

public interface BoardService {
	
	int getListCount();
	
	ArrayList<Board>selectList(PageInfo pi);
	
	int insertBoard(Board b);
	
	void addReadCount(int bId);
	
	Board selectBoard(int bId);
	
	int updateBoard(Board b);
	
	int deleteBoard(int bId);
	
}
