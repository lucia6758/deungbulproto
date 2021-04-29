package com.sbs.deungbulproto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;

	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;

	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**")
				.excludePathPatterns("/gen/**").excludePathPatterns("/adr/**");

		// 관리자
		registry.addInterceptor(needAdminInterceptor).addPathPatterns("/adm/**")
				.excludePathPatterns("/adm/member/login").excludePathPatterns("/adm/member/doLogin")
				.excludePathPatterns("/adm/member/join").excludePathPatterns("/adm/member/doJoin")
				.excludePathPatterns("/adm/member/getLoginIdDup");

		// 로그인
		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/**").excludePathPatterns("/")
				.excludePathPatterns("/swagger-ui/**").excludePathPatterns("/swagger-resources/**")
				.excludePathPatterns("/v2/api-docs").excludePathPatterns("/webjars/**").excludePathPatterns("/adm/**")
				.excludePathPatterns("/gen/**").excludePathPatterns("/resource/**")
				.excludePathPatterns("/usr/home/main").excludePathPatterns("/usr/client/authKey")
				.excludePathPatterns("/usr/client/list").excludePathPatterns("/usr/client/login")
				.excludePathPatterns("/usr/client/doLogin").excludePathPatterns("/usr/client/detail")
				.excludePathPatterns("/usr/client/join").excludePathPatterns("/usr/client/doJoin")
				.excludePathPatterns("/usr/client/getLoginIdDup").excludePathPatterns("/usr/client/doFindLoginId")
				.excludePathPatterns("/usr/client/doFindLoginPw").excludePathPatterns("/usr/expert/authKey")
				.excludePathPatterns("/usr/expert/list").excludePathPatterns("/usr/expert/login")
				.excludePathPatterns("/usr/expert/doLogin").excludePathPatterns("/usr/expert/detail")
				.excludePathPatterns("/usr/expert/join").excludePathPatterns("/usr/expert/doJoin")
				.excludePathPatterns("/usr/expert/getLoginIdDup").excludePathPatterns("/usr/expert/findLoginId")
				.excludePathPatterns("/usr/expert/doFindLoginId").excludePathPatterns("/usr/expert/findLoginPw")
				.excludePathPatterns("/usr/expert/doFindLoginPw").excludePathPatterns("/usr/order/list")
				.excludePathPatterns("/usr/order/detail").excludePathPatterns("/usr/review/list")
				.excludePathPatterns("/adr/**").excludePathPatterns("/common/**").excludePathPatterns("/usr/file/test*")
				.excludePathPatterns("/usr/file/doTest*").excludePathPatterns("/test/**").excludePathPatterns("/error");

		// 로그아웃
		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/adm/member/login")
				.addPathPatterns("/adm/member/doLogin").addPathPatterns("/usr/client/login")
				.addPathPatterns("/usr/client/doLogin").addPathPatterns("/usr/client/join")
				.addPathPatterns("/usr/client/doJoin").addPathPatterns("/usr/expert/login")
				.addPathPatterns("/usr/expert/doLogin").addPathPatterns("/usr/expert/join")
				.addPathPatterns("/usr/expert/doJoin").addPathPatterns("/usr/assistant/login");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**").addResourceLocations("file:///" + genFileDirPath + "/")
				.setCachePeriod(20);

	}

}