package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.common.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	BoardService bService;
	
	@RequestMapping("blist.do")
	public ModelAndView boardList(@RequestParam(value="page", required=false) Integer page,
								  ModelAndView mv) {
		
		int currentPage = 1;
		if(page != null) {
			currentPage = page;
		}
		
		int listCount = bService.getListCount();
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		if(list != null) {
			mv.addObject("list", list);
			mv.addObject("pi", pi);
			mv.setViewName("board/boardListView");
		} else {
			throw new BoardException("게시글 전체 조회에 실패하였습니다.");
		}
		
		return mv;
	}
	
	@RequestMapping("binsertView.do")
	public String boardInsertView() {
		return "board/boardInsertForm";
	}
	
	@RequestMapping("binsert.do")
	public String insertBoard(@ModelAttribute Board b, 
							  @RequestParam(value="uploadFile", required=false) MultipartFile uploadFile,
							  HttpServletRequest request) {
		System.out.println(b);
		System.out.println(uploadFile);
		System.out.println(uploadFile.getOriginalFilename());
		// 파일을 넣지 않은 결우 파일 이름은 ""로 들어감
		
//		if(uploadFile.getOriginalFilename().equals("")) {
		if(uploadFile != null && !uploadFile.isEmpty()) {
			// 저장할 경로를 지정하는 saveFile()메소드 생성
			String renameFileName = saveFile(uploadFile, request);
			
			if(renameFileName != null) {
				b.setOriginalFile(uploadFile.getOriginalFilename());
				b.setRenameFile(renameFileName);
			}
			
		}
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:blist.do";
		} else {
			throw new BoardException("게시글 등록에 실패하였습니다.");
		}
	}

	public String saveFile(MultipartFile file, HttpServletRequest request) {
		
		String root = request.getSession().getServletContext().getRealPath("resources");
		
		String savePath = root + "\\buploadFiles";
		
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String originalFileName = file.getOriginalFilename();
		String renameFileName = sdf.format(new java.sql.Date(System.currentTimeMillis())) + "."
								+ originalFileName.substring(originalFileName.lastIndexOf(".")+1);
		String renamePath = folder + "\\" + renameFileName;
		
		try {
			file.transferTo(new File(renamePath)); // 전달 받은 file이 rename명으로 저장
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return renameFileName;
	}
	
	@RequestMapping("bdetail.do")
	public ModelAndView boardDetail(@RequestParam("bId") int bId, 
							  @RequestParam("page") int page,
							  ModelAndView mv) {
		bService.addReadCount(bId);
		Board board = bService.selectBoard(bId);
		
		if(board != null) {
			mv.addObject("board", board)
			  .addObject("page", page)
			  .setViewName("board/boardDetailView");
		} else {
			throw new BoardException("게시글 상세보기에 실패하였습니다.");
		}
		
		return mv;
	}
	
	@RequestMapping("bupView.do")
	public ModelAndView boardUpdateView(@RequestParam("bId") int bId,
										@RequestParam("page") int page,
										ModelAndView mv) {
		Board board = bService.selectBoard(bId);
		
		mv.addObject("board", board)
		  .addObject("page", page)
		  .setViewName("board/boardUpdateForm");
		
		return mv;
	}
	
	@RequestMapping("bupdate.do")
	public ModelAndView boardUpdate(ModelAndView mv,
									@ModelAttribute Board b,
									@RequestParam("page") Integer page,
									@RequestParam("reloadFile") MultipartFile reloadFile,
									HttpServletRequest request) {
		if(reloadFile != null && !reloadFile.isEmpty()) {
			if(b.getRenameFile() != null) {
				deleteFile(b.getRenameFile(), request);
			}
			
			String renameFileName = saveFile(reloadFile, request);
			
			if (renameFileName != null) {
				b.setOriginalFile(reloadFile.getOriginalFilename());
				b.setRenameFile(renameFileName);
			}
		}
		
		int result = bService.updateBoard(b);
		
		if (result > 0) {
			mv.addObject("page", page)
			  .setViewName("redirect:bdetail.do?bId=" + b.getbId());
		}else {
			throw new BoardException("게시글 등록에 실패하였습니다.");
		}
		
		return mv;
	}
	
	public void deleteFile(String fileName, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String savePath = root + "\\buploadFiles";
		
		File f = new File(savePath + "\\" + fileName);
		
		if (f.exists()) {
			f.delete();
		}
	}
	
	/*@RequestMapping("bdelete.do")
	public Stirng deleteBoard(@RequestParam("bId") Integer bId) {
		int result = bService.deleteBoard(bId);
		
		if(result > 0) {
			return "redirect:blist.do";
		}else {
			throw new BoardException("삭제 실패");
		}
	}*/
	

	
}
