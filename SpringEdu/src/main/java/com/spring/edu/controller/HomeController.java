package com.spring.edu.controller;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.spring.edu.model.UserModel;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	// 1. 원래 있던 코드에서 HttpServletRequest request 라는 파라메터를 추가.
	// 2. @RequestMapping 어노테이션은 value값으로 된 URI값, 지정된 method방식으로 페이지 접근 요청이 들어오면
	// 해당 기능을 수행.
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		getUserSession(request, model);

		return "home";
	}

	// 1. 로그인 페이지 요청 시 작업내용
	// 2. ( method = RequestMethod.GET) 부분은 GET방식 요청으로만 접근 할 수 있다는 뜻
	 @RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginPage(HttpServletRequest request, Model model) {

		HttpSession session = request.getSession(false);

		if (session != null) {

			Object obj = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

			if (obj != null && obj instanceof AuthenticationException) {
				AuthenticationException excp = (AuthenticationException) obj;
				model.addAttribute("LOGIN_EXCEPTION", excp.getMessage());
			}
		}
		return new ModelAndView("login");
	}

	/*
	 * // 1. 로그인페이지 요청 시 작업내용 // 2. ( method = RequestMethod.GET ) 부분은 GET방식
	 * 요청으로만 접근 할 수 있다는 뜻
	 * 
	 * @RequestMapping(value = "/login", method = RequestMethod.GET) public
	 * ModelAndView loginPage(HttpServletRequest request, Model model) { //
	 * 세션정보에 유저 정보가 있으면 메인페이지로 연결 if (getUserSession(request) != null) {
	 * 
	 * return redirectView(request, "/"); } else { // 유저 정보가 없으면 로그인 페이지 연결
	 * return new ModelAndView("login"); } }
	 */

	/*
	 * // 1. 로그인 요청 작업 수행 페이지X // 2. ( method = RequestMethod.POST) 부분은 POST 방식
	 * 요청으로만 접근 할 수 있다는 뜻 // 3. @RequestParam 어노테이션은 페이지 요청 받았을 시 // 지정된 name으로
	 * 파라메터 값이 존재하면 바로 변수에 넣어줌. required 설정 가능(기본 = true)
	 * 
	 * @RequestMapping(value="/login-request",method = RequestMethod.POST)
	 * public ModelAndView loginProcess(HttpServletRequest request, Model model,
	 * 
	 * @RequestParam(name = "login_email") String userName,
	 * 
	 * @RequestParam(name = "login_password") String password) {
	 * 
	 * // 세션 값을 가져오고 만약 null값 이면 세션을 새로 생성함. HttpSession session =
	 * request.getSession(true);
	 * 
	 * // 로그인 오류 메세지 세션이 존재하면 해당 값의 로그인 오류 메세지 세션만 지움 if
	 * (session.getAttribute("LOGIN_EXCEPTION") != null) {
	 * 
	 * session.removeAttribute("LOGIN_EXCEPTION"); }
	 * 
	 * // 임의의 로그인 아이디 / 비밀번호 지정 final String testUserName = "test@test.com";
	 * final String testPassword = "test";
	 * 
	 * // 요청받은 아이디와 비밀번호가 임의로 정한 값과 동일할 시/ if (userName.equals(testUserName) &&
	 * password.equals(testPassword)) { // 유저 빈 생성후 빈에 들어갈 값들을 지정 UserModel user
	 * = new UserModel(); user.setUserName(userName);
	 * user.setPassword(password);
	 * user.setNickName("Login Tester User NickName...!!!");
	 * 
	 * // 세션에 유저 정보값을 저장 session.setAttribute("USER_SESSION", user);
	 * 
	 * // 메인 페이지로 강제이동 return redirectView(request, "/");
	 * 
	 * } else { // 비밀번호나 아이디가 틀렸을 시 로그인 오류 메세지를 세션에 저장.
	 * session.setAttribute("LOGIN_EXCEPTION", "없는 아이디이거나 비밀번호가 틀렸습니다."); }
	 * 
	 * return redirectView(request, "/login");
	 * 
	 * }
	 */
	/*
	 * // 1. 로그아웃 요청시 수행
	 * 
	 * @RequestMapping(value = "/logout", method = RequestMethod.GET) public
	 * ModelAndView logout(HttpServletRequest request, Model model) {
	 * 
	 * // 세션을 가져오되 새로 생성하진 않음. HttpSession session = request.getSession(false);
	 * 
	 * // 세션이 null값이 아니면 if (session != null) { // 세션을 초기화시킴
	 * session.invalidate();
	 * 
	 * } return redirectView(request, "/");
	 * 
	 * }
	 */

	/*
	 * // 로그인 된 유저정보를 세션에서 가져옴 private UserModel
	 * getUserSession(HttpServletRequest request,Model model) { // 세션을 가져오지만 새로
	 * 생성하진 않음 HttpSession session = request.getSession(false);
	 * 
	 * // 세션이 존제하면.. if (session != null) { // 세션에서 유저 정보를 가져옴 Object obj =
	 * session.getAttribute("USER_SESSION");
	 * 
	 * // 값이 null이 아니면서 UserModel Class 값이면, if (obj != null && obj instanceof
	 * UserModel) { // UserModel Class로 캐스팅 한 후 값 리턴 return (UserModel) obj; } }
	 * 
	 * return null; }
	 */

	/*
	 * private UserModel getUserSession(HttpServletRequest request, Model model)
	 * { HttpSession session = request.getSession(false); if (session != null) {
	 * Object obj = session.getAttribute(HttpSessionSecurityContextRepository.
	 * SPRING_SECURITY_CONTEXT_KEY); if (obj != null && obj instanceof
	 * SecurityContextImpl) { Object user = ((SecurityContextImpl)
	 * obj).getAuthentication().getPrincipal(); if (user != null && user
	 * instanceof UserModel) { if (model != null) {
	 * model.addAttribute("USER_SESSION", user); } return (UserModel) user; } }
	 * } return null; }
	 */

	// UserModel의 세션값 가져오기
	private UserModel getUserSession(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session != null){
            Object obj = session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if(obj != null && obj instanceof SecurityContextImpl){
                Object user = ((SecurityContextImpl) obj).getAuthentication().getPrincipal();
                if(user != null && user instanceof UserModel){
                    /*if(model != null){
                        model.addAttribute("USER_SESSION", user);
                    }*/
                    if(session.getAttribute("USER_SESSION") == null){
                        session.setAttribute("USER_SESSION", (UserModel) user);
                    }
                    return (UserModel) user;
                }
            }
        }
        return null;
    }

	// 페이지 강제 이동을 위한 메소드
	private ModelAndView redirectView(HttpServletRequest request, String url) {

		RedirectView rv = new RedirectView();
		ModelAndView mav = new ModelAndView(rv);
		rv.setUrl(request.getContextPath() + url);

		// GET 방식으로 된 파라미터들을 모두 없앰
		rv.setExposePathVariables(false);
		// Model에 등록 된 값들을 모두 없앰
		rv.setExposeModelAttributes(false);

		return mav;
	}

}
