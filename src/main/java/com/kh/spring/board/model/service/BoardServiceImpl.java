package com.kh.spring.board.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.BoardDAO;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;

@Service("bService")
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	BoardDAO bDAO;

	@Override
	public int getListCount() {
		return bDAO.getListCount();
	}

	@Override
	public ArrayList<Board> selectList(PageInfo pi) {
		return bDAO.selectList(pi);
	}

	@Override
	public int insertBoard(Board b) {
		return bDAO.insertBoard(b);
	}

	@Override
	public void addReadCount(int bId) {
		bDAO.addReadCount(bId);
		
	}

	@Override
	public Board selectBoard(int bId) {
		return bDAO.selectBoard(bId);
	}

	@Override
	public int updateBoard(Board b) {
		return bDAO.updateBoard(b);
	}

	@Override
	public int deleteBoard(int bId) {
		return bDAO.deleteBoard(bId);
	}

	@Override
	public ArrayList<Reply> selectReplyList(int bId) {
		return bDAO.selectReplyList(bId);
	}

	@Override
	public ArrayList<Board> selectTopList() {
		return bDAO.selectTopList();
	}

	@Override
	public int insertReply(Reply r) {
		return bDAO.insertReply(r);
	}

}
