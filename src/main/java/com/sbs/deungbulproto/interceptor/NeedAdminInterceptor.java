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

		boolean isAjax = request.getParameter("isAjax") != null;

		if (isAdmin == false) {
			if (isAjax == false) {
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().append("<script>");
				response.getWriter().append("alert('관리자만 이용할 수 있습니다.');");
				response.getWriter().append("location.replace('/adm/member/login?redirectUrl="
						+ Util.reqAttr(request, "encodedAfterLoginUrl", "") + "');");
				response.getWriter().append("</script>");
			} else {
				response.setContentType("application/json; charset=UTF-8");
				response.getWriter().append("{\"resultCode\":\"F-A\",\"msg\":\"관리자만 이용할 수 있습니다.\"}");
			}

			return false;
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
