package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.kh.spring.board.model.exception.BoardException;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.PageInfo;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.Pagination;
import com.kh.spring.member.model.vo.Member;

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
	
	@RequestMapping("bdelete.do")
	public ModelAndView deleteBoard(@RequestParam("bId") int bId, @RequestParam("page") Integer page, ModelAndView mv) {
		System.out.println(page);
		
		int result = bService.deleteBoard(bId);
		
		if(result > 0) {
			mv.addObject(page).setViewName("redirect:blist.do");
		}else {
			throw new BoardException("게시글 삭제에 실패하였습니다.");
		}
		return mv;
		
	}
	
	
//	@RequestMapping("topList.do")
//	public void boardTopList(HttpServletResponse response) throws IOException {
//		response.setContentType("application/json; charset=utf-8"); 
//		
//		
//		ArrayList<Board> list = bService.selectTopList();
//		
//		JSONArray jArr = new JSONArray();
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		
//		for(Board b : list) {
//			JSONObject jObj = new JSONObject();
//			jObj.put("bId", b.getbId());
//			jObj.put("bTitle", b.getbTitle());
//			jObj.put("bWriter", b.getbWriter());
//			jObj.put("originalFile", b.getOriginalFile());
//			jObj.put("bCount", b.getbCount());
//			jObj.put("bCreateDate", sdf.format(b.getbCreateDate()));
//			
//			jArr.add(jObj);
//		}
//		
//		JSONObject sendJson = new JSONObject();
//		sendJson.put("list", jArr);
//		
//		PrintWriter pw = response.getWriter();
//		pw.print(sendJson);
//		pw.flush();
//		pw.close();
//		
//	}
	
	
	@RequestMapping("topList.do")
	@ResponseBody
	public String boardTopList() throws UnsupportedEncodingException, JsonProcessingException{
		
		ArrayList<Board> list = bService.selectTopList();
		
		for(Board b : list) {
			b.setbTitle(URLEncoder.encode(b.getbTitle(), "utf-8"));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleDateFormat sdf = new SimpleDateFormat();
		
		mapper.setDateFormat(sdf);
		
		String jsonStr = mapper.writeValueAsString(list);
		
		return jsonStr;
		
	}
	
	/************* 댓글 가져오기 
	 * @throws IOException 
	 * @throws JsonIOException *************/
	@RequestMapping("rList.do")
	public void getReplyList(HttpServletResponse response, int bId) throws JsonIOException, IOException {
		ArrayList<Reply> rList = bService.selectReplyList(bId);
		
		for(Reply r: rList) {
			r.setrContent(URLEncoder.encode(r.getrContent(), "utf-8"));
		}
		
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		gson.toJson(rList, response.getWriter());
		
	}
	
	/*********** 댓글 등록 ***********/
	@RequestMapping("addReply.do")
	@ResponseBody
	public String addReply(Reply r, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		String rWriter = loginUser.getId();
		
		r.setrWriter(rWriter);
		int result = bService.insertReply(r);
		
		if(result > 0) {
			return "success";
		}else {
			throw new BoardException("Error");
		}
	}
	
}
