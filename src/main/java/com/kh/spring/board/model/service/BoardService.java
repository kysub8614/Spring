package com.kh.spring.board.model.service;

import java.util.ArrayList;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

public interface BoardService {
	
	int getListCount();
	
	ArrayList<Board>selectList(PageInfo pi);
	
	int insertBoard(Board b);
	
	void addReadCount(int bId);
	
	Board selectBoard(int bId);
	
	int updateBoard(Board b);
	
	int deleteBoard(int bId);

	ArrayList<Reply> selectReplyList(int bId);

	ArrayList<Board> selectTopList();

	int insertReply(Reply r);
	
}
