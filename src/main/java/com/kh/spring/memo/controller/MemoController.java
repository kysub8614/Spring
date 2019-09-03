package com.kh.spring.memo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.memo.model.service.MemoService;

@Controller
public class MemoController {
	@Autowired
	MemoService mmService;
	
//	private Logger logger = LoggerFactory.getLogger(MemoController.class);
	
	@RequestMapping("memo.do")
	public ModelAndView memo() {
		
//		logger.debug("[before]MemoController - memo()");
		
		ArrayList<HashMap<String, String>> list = mmService.selectMemoList();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("list", list);
		mv.setViewName("memo/memo");
		
//		logger.debug("[after]MemoController - memo()");
		
		return mv;
	}
	
	
	@RequestMapping("memo/insertMemo.do")
	public ModelAndView insertMemo(@RequestParam("memo") String memo,
							 @RequestParam("password") String password,
							 ModelAndView mv) {
		
//		logger.debug("[before]MemoController - insertMemo()");
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("password", password);
		map.put("memo", memo);

		int result = mmService.insertMemo(map);

		if (result > 0) {
			mv.setViewName("redirect:"
					+ "/memo.do");
		}else {
			throw new BoardException("메모 등록에 실패하였습니다.");
		}
		
//		logger.debug("[after]MemoController - insertMemo()");
		
		return mv;

	}
	
	@RequestMapping("delete.do")
	public ModelAndView deleteMemo(@RequestParam("memo") String memo,
								   @RequestParam("password") String password,
								   @RequestParam("memono") String memono,
								   ModelAndView mv) {
		return mv;
	}
	 
	
	
	
}
