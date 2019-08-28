package com.kh.spring.member.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.exception.MemberException;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;
import com.sun.javafx.collections.MappingChange.Map;

@SessionAttributes("loginUser") // Model에 loginUser라는 키 값으로 객체를 추가하면 자동으로 세션 추가
@Controller //Controller타입의 어노테이션을 붙여주면 빈 스캐닝을 통해 자동 등록
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	/************ 파라미터 전송 받는 방법 ************/
	
	/*
	 * 1. HttpServletRequest를 통해 전송 받기(JSP/Servlet방식)
	 *  	메소드의 매개변수로 HttpServletRequest를 작성하면
	 *  	메소드 실행 시 스프링 컨테이너가 자동으로 객체를 인자로 주입
	 */
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	public String memberLogin(HttpServletRequest request) {
//		
//		String id= request.getParameter("id");
//		String pwd= request.getParameter("pwd");
//		
//		System.out.println("Id1 : " + id);
//		System.out.println("Pwd1 : " + pwd);
//		
//		return "home";
//	}
	
	/*
	 * 2. @RequestParam 어노테이션 방식
	 * 		파라미터를 조금 더 간단하게 받아올 수 있는 방법(스프링에서 제공)
	 * 		(HttpServlet과 비슷하세 request객체를 받아옴)
	 */
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	//public String memberLogin(@RequestParam("id") String id, @RequestParam("pwd") String pwd) {
//	public String memberLogin(@RequestParam(value="id", required=false, defaultValue="HelloId") String id,
//			@RequestParam(value="pwd", required=false, defaultValue="HelloPwd") String pwd) {
//		
//		System.out.println("Id2 : " + id);
//		System.out.println("Pwd2 : " + pwd);
//		
//		return "home";
//	}
	
	/*
	 * 3. @RequestParam 어노테이션 생략
	 * 		메소드와 매개변수가 파라미터 명(name)과 동일할 때 자동으로 값을 주입하여 변수에 저장 가능
	 * 		단, 어노테이션 생략했으므로 defaultValue와 required 설정 불가능
	 */
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	public String memberLogin(String id, String pwd) {
//		
//		System.out.println("Id3 : " + id);
//		System.out.println("Pwd3 : " + pwd);
//		
//		return "home";
//	}
	
	/*
	 * 4. @ModeAttribute 이용한 값 전달 방법
	 * 		요청 파라미터가 많을 경우 객체 타입으로 넘겨 받음
	 * 		 이 때, 기본 생성자와 setter를 이용한 주입 방식이기 때문에 둘 중 하나라도 없으면 에러가 남
	 * 
	 * 		==> 이런 방식을 커맨드 방식이라고 함
	 * 			스프링 컨테이너가 기본 생성자를 통해 Member를 생성하고
	 * 			setter메소드로 꺼낸 파라미터들로 값을 변경한 후에 현재 이 메소드를 호출할 때
	 * 			인자로 전달하며 호출하는 방식으로 주입
	 * 			(단, 반드시 name속성 값과 필드명이 동일해야 하고 setter작성 규칙에 맞게 작성해야 됨)
	 */
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	public String memberLogin(@ModelAttribute Member m) {
//		
//		System.out.println("Id4 : " + m.getId());
//		System.out.println("Pwd4 : " + m.getPwd());
//		
//		return "home";
//	}
	
	/*
	 * 5. @ModeAttribute어노테이션 생략하고 객체로 바로 전달받는 방법
	 * 		어노테이션 생략해도 자동으로 커맨드 객체로 매핑
	 */
	
//	@RequestMapping(value="login.do", method=RequestMethod.POST)
//	   public String memberLogin(Member m, HttpSession session) {
//	      
//	      System.out.println("Id5 : " + m.getId());
//	      System.out.println("Pwd5 : " + m.getPwd());
//	      
////	      MemberService mService = new MemberServiceImpl();
//	      System.out.println(mService.hashCode());
//	      
//	      Member loginUser = mService.memberLogin(m); // service 에 만들어질거임
//	      System.out.println(loginUser);
//	      
//	      if(loginUser != null) {
//	         // 로그인 성공시 세션에 정보를 담아야하기 땜누에 세션 필요
//	         // 매개변수로 HttpSession 추가!!
//	         session.setAttribute("loginUser", loginUser);
//	      }
//	      return "home";
//	}
	
	/********** 요청 후 전달하고자 하는 데이터가 있을 경우에 대한 방법 **********/
	
	/*
	 * 1. Model 객체를 사용하는 방법
	 * 		커맨드 객체로 Model을 사용하게 되면 뷰로 전달하고자 하는 데이터를 맵 형식(key, value)으로 담을 때 사용
	 * 		scope는 request이며 서블릿에서 사용하던 requestScope와	비슷
	 */
	
//	@RequestMapping(value = "login.do", method = RequestMethod.POST)
//	public String memberLogin(Member m, Model model,HttpSession session) {
//
//		Member loginUser = mService.memberLogin(m); // service 에 만들어질거임
//
//		if (loginUser != null) {
//			// 로그인 성공시 세션에 정보를 담아야하기 땜누에 세션 필요
//			// 매개변수로 HttpSession 추가!!
//			session.setAttribute("loginUser", loginUser);
//			return "home";
//		}else {
//			model.addAttribute("msg", "로그인에 실패하였습니다.");
//			return "common/errorPage";
//		}
//	}
	
	/*
	 * 2. modelAndView객체 사용 방법
	 * 		위에서 Model은 전달하고자 하는 데이터를 맵 형식으로 담는 공간이라면
	 * 		View는 requestDiaspatcherServlet처럼 forward할 뷰 페이지 정보를 담은 객체라고 할 수 있다.
	 * 
	 * 		modelAndView는 이 두가지를 합쳐놓은 객체이며
	 * 		위처럼 Model은 따로 사용 가능하지만 View는 따로 사용 불가능 하다
	 * 
	 * 		커맨드 객체로 ModelAndView를 사용하며 전달하고자하는 데이터와 뷰를 set함
	 * 
	 */
	
//	@RequestMapping(value = "login.do", method = RequestMethod.POST)
//	public ModelAndView memberLogin(Member m, ModelAndView mv,HttpSession session) {
//
//		Member loginUser = mService.memberLogin(m); // service 에 만들어질거임
//
//		if (loginUser != null) {
//			// 로그인 성공시 세션에 정보를 담아야하기 땜누에 세션 필요
//			// 매개변수로 HttpSession 추가!!
//			session.setAttribute("loginUser", loginUser);
//			mv.setViewName("home");
//		}else {
//			mv.addObject("msg", "로그인에 실패하였습니다.");
//			mv.setViewName("common/errorPage");
//		}
//		
//		return mv;
//	}
	
//	// 로그아웃 용 컨트롤러1
//	@RequestMapping("logout.do")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "home";
//	}
	
	/* 
	 * 3. session에 저장할 때 @SessionAttribute사용하기
	 * 		Model에 Attribute가 추가될 때 자동으로 키 값을 찾아 세션에 등록하는 기능 제공
	 */
	
//	@RequestMapping(value = "login.do", method = RequestMethod.POST)
//	public String memberLogin(Member m, Model model) {
//
//		Member loginUser = mService.memberLogin(m); // service 에 만들어질거임
//
//		if (loginUser != null) {
//			model.addAttribute("loginUser", loginUser);
//			
//			return "home";
//		}else {
//			throw new MemberException("로그인에 실패하였습니다.");
//		}
//		
//	}
	
	// 로그아웃 용 컨트롤러2
	@RequestMapping("logout.do")
	public String logout(SessionStatus status) {
		// SessionStatus : 커맨드 객체로 세션 상태를 관리 할 수 있음
		status.setComplete();
		return "home";
	}
	
	// 회원가입용 컨트롤러
	// 회원가입 페이지 이동
	@RequestMapping("enrollView.do")
	public String enrollView() {
		
		if(logger.isDebugEnabled()) {
			logger.debug("회원등록페이지");
		}
		
		return "member/memberJoin";
	}
	
	// 가입 눌럿을 경우 처리해주는 메소드
	@RequestMapping("minsert.do")
	public String memberInsert(@ModelAttribute Member m, 
							   @RequestParam("post") String post,
							   @RequestParam("address1") String address1,
							   @RequestParam("address2") String address2) {
		
		System.out.println("암호화 전" + m);
		
		/*
		 * 1. 결과 값을 받아보면 한글이 깨짐
		 * 		스프링에서 제공하는 필터를 이용해서
		 * 		요청시 전달 받는 값에 한글이 있을 경우
		 * 		인코딩 하는 것 추가
		 * 2. 비밀번호 평문
		 * 		bcrypt : 스프링 시큐리티 모듈에서 제공하는 암호화 방식
		 */
		
		String encPwd = bcryptPasswordEncoder.encode(m.getPwd());
		m.setPwd(encPwd);
		
		System.out.println("암호화 후" + m);
		
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.insertMember(m);
		if(result > 0) {
			return "home";
		} else {
			throw new MemberException("회원가입에 실패하였습니다.");
		}
		
	}
	
	// 암호화 후 로그인
	@RequestMapping(value = "login.do", method = RequestMethod.POST)
	public String memberLogin(Member m, Model model) {
		
		Member loginUser = mService.memberLogin(m);
		
		if(bcryptPasswordEncoder.matches(m.getPwd(), loginUser.getPwd())) {
			model.addAttribute("loginUser", loginUser);
		}else {
			throw new MemberException("로그인에 실패하였습니다.");
		}
		
		return "home";
		
	}
	
	// 마이페이지
	// 마이페이지로 이동
	@RequestMapping("myinfo.do")
	public String myPage() {
		return "member/myPage";
	}
	// 정보 수정 페이지 이동
	@RequestMapping("mupdateView.do")
	public String memberUpdateForm() {
		return "member/memberUpdateForm";
	}
	// 회원 정보 업데이트 메서드
	@RequestMapping("mupdate.do")
	public String memberUpdate(Model model,
							   @ModelAttribute Member m, 
			   				   @RequestParam("post") String post,
			   				   @RequestParam("address1") String address1,
			   				   @RequestParam("address2") String address2) {
		
		m.setAddress(post + "/" + address1 + "/" + address2);
		
		int result = mService.updateMember(m);
		if(result > 0) {
			model.addAttribute("loginUser", m);
		} else {
			throw new MemberException("회원정보 수정에 실패하였습니다.");
		}
		return "member/myPage";
		
	}
	
	// 비밀번호 수정 페이지 이동
	@RequestMapping("mpwdUpdateView.do")
	public String pwdUpdatePage() {
		return "member/memberPwdUpdateForm";
	}

	// 비밀번호 변경 메서드
	@RequestMapping("mPwdUpdate.do")
	public String memberPwdUpdate(/* Model model, */ HttpSession session, @RequestParam("pwd") String pwd,
			@RequestParam("newPwd1") String newPwd1) {

		Member m = (Member) session.getAttribute("loginUser");

		if (bcryptPasswordEncoder.matches(pwd, m.getPwd())) {

			m.setPwd(bcryptPasswordEncoder.encode(newPwd1));

			int result = mService.updatePwd(m);

			if (result > 0) {
				/* model.addAttribute("loginUser", m); */
				return "member/myPage";
			} else {
				throw new MemberException("비밀번호 수정 중 오류 발생.");
			}

		} else {
			throw new MemberException("비밀번호가 일치하지 않습니다.");
		}
	}
	
	@RequestMapping("mdelete.do")
	public String deleteMember(@RequestParam("id") String id, SessionStatus status) {

		int result = mService.deleteMember(id);

		if (result > 0) {
			//status.setComplete();
			//return "home";
			return "redirect:logout.do";
		} else {
			throw new MemberException("회원탈퇴 실패");
		}

	}
	/*
	 * 아이디 중복확인
	 * 
	 * @RequestMapping("dupid.do")
	 * 
	 * public void dupid(HttpServletResponse response, String id) throws IOException
	 * {
	 * 
	 * boolean isUsable = mService.checkIdDup(id)== 0 ? true : false;
	 * 
	 * System.out.println(isUsable);
	 * 
	 * response.getWriter().print(isUsable); }
	 */
	
	/* 아이디 중복확인 */
	
	@RequestMapping("dupid.do")
	public ModelAndView dupid(ModelAndView mv, String id) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		
		boolean isUsable = mService.checkIdDup(id)==0 ? true : false;
		
		map.put("isUsable", isUsable);
		mv.addAllObjects(map);
		mv.setViewName("jsonView");
		
		return mv;
		
	}
}