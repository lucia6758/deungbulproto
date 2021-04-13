package com.sbs.deungbulproto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UsrHomeController {
	@RequestMapping("/usr/home/main")
	public String showMain() {
		return "usr/home/main";
	}

}
