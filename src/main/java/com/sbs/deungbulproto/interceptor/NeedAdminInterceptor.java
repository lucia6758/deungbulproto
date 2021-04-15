package com.sbs.deungbulproto.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sbs.deungbulproto.util.Util;

@Component("needAdminInterceptor")
public class NeedAdminInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isAdmin = (boolean) request.getAttribute("isAdmin");

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
