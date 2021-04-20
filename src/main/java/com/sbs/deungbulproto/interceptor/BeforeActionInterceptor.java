package com.sbs.deungbulproto.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sbs.deungbulproto.dto.Adm;
import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.service.AdmMemberService;
import com.sbs.deungbulproto.service.ClientService;
import com.sbs.deungbulproto.service.ExpertService;
import com.sbs.deungbulproto.util.Util;

@Component("beforeActionInterceptor")
public class BeforeActionInterceptor implements HandlerInterceptor {
	@Autowired
	private ClientService clientService;
	@Autowired
	private ExpertService expertService;
	@Autowired
	private AdmMemberService admMemberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Map<String, Object> param = Util.getParamMap(request);
		String paramJson = Util.toJsonStr(param);

		String requestUrl = request.getRequestURI();
		String queryString = request.getQueryString();

		if (queryString != null && queryString.length() > 0) {
			requestUrl += "?" + queryString;
		}

		String encodedRequestUrl = Util.getUrlEncoded(requestUrl);

		request.setAttribute("requestUrl", requestUrl);
		request.setAttribute("encodedRequestUrl", encodedRequestUrl);

		request.setAttribute("afterLoginUrl", requestUrl);
		request.setAttribute("encodedAfterLoginUrl", encodedRequestUrl);

		request.setAttribute("paramMap", param);
		request.setAttribute("paramJson", paramJson);

		int loginedClientId = 0;
		int loginedExpertId = 0;
		int loginedAdmId = 0;

		Client loginedClient = null;
		Expert loginedExpert = null;
		Adm loginedAdm = null;

		String authKey = request.getParameter("authKey");

		if (authKey != null && authKey.length() > 0) {
			String[] authKies = authKey.split("__");

			if (authKies[0].contains("1")) {
				loginedClient = clientService.getClientByAuthKey(authKey);
				if (loginedClient == null) {
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					request.setAttribute("authKeyStatus", "valid");
					loginedClientId = loginedClient.getId();
				}
			} else if (authKies[0].contains("2")) {
				loginedExpert = expertService.getExpertByAuthKey(authKey);
				if (loginedExpert == null) {
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					request.setAttribute("authKeyStatus", "valid");
					loginedExpertId = loginedExpert.getId();
				}
			} else if (authKies[0].contains("5")) {
				loginedAdm = admMemberService.getAdmByAuthKey(authKey);
				if (loginedAdm == null) {
					request.setAttribute("authKeyStatus", "invalid");
				} else {
					request.setAttribute("authKeyStatus", "valid");
					loginedAdmId = loginedAdm.getId();
				}
			}

		} else {
			HttpSession session = request.getSession();
			request.setAttribute("authKeyStatus", "none");

			if (session.getAttribute("loginedClientId") != null) {
				loginedClientId = (int) session.getAttribute("loginedClientId");
				loginedClient = clientService.getClient(loginedClientId);
			} else if (session.getAttribute("loginedExpertId") != null) {
				loginedExpertId = (int) session.getAttribute("loginedExpertId");
				loginedExpert = expertService.getExpert(loginedExpertId);
			} else if (session.getAttribute("loginedAdmId") != null) {
				loginedAdmId = (int) session.getAttribute("loginedAdmId");
				loginedAdm = admMemberService.getAdm(loginedAdmId);
			}
			
			
			System.out.println("klajsd;ljska : " + session.getAttribute("deviceIdToken"));
		}

		boolean isLogined = false;
		boolean isAdmin = false;

		if (loginedAdm != null) {
			isLogined = true;
			isAdmin = admMemberService.isAdmin(loginedAdm);
		}
		if (loginedClient != null || loginedExpert != null) {
			isLogined = true;
		}

		request.setAttribute("loginedClientId", loginedClientId);
		request.setAttribute("loginedExpertId", loginedExpertId);
		request.setAttribute("loginedAdm", loginedAdm);
		request.setAttribute("loginedClient", loginedClient);
		request.setAttribute("loginedExpert", loginedExpert);
		request.setAttribute("loginedAdm", loginedAdm);
		request.setAttribute("isLogined", isLogined);
		request.setAttribute("isAdmin", isAdmin);

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

}
