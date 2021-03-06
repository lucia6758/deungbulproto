package com.sbs.deungbulproto.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component("needLoginInterceptor")
public class NeedLoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isLogined = (boolean) request.getAttribute("isLogined");

		boolean isAjax = true;

		if (isLogined == false) {
			String authKeyStatus = (String) request.getAttribute("authKeyStatus");

			String resultCode = "F-A";
			String resultMsg = "로그인 후 이용해주세요.";

			if (authKeyStatus.equals("invalid")) {
				resultCode = "F-B";
				resultMsg = "인증키가 올바르지 않습니다.";
			}

			if (isAjax == false) {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().append("<script>");
				response.getWriter().append("alert('" + resultMsg + "');");
				response.getWriter().append("location.replace('/usr/home/main');");
				response.getWriter().append("</script>");
			} else {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().append("{\"resultCode\":\"" + resultCode + "\",\"msg\":\"" + resultMsg + "\"}");
			}

			return false;
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
