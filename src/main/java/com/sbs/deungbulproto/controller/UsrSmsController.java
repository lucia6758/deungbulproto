package com.sbs.deungbulproto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.util.Util;
import com.sbs.deungbulproto.util.dto.Aligo__send__ResponseBody;

@Controller
public class UsrSmsController extends BaseController{	

	@GetMapping("/usr/home/doSendSms")
	@ResponseBody
	public ResultData doSendSms(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("msg") String msg) {		
		Aligo__send__ResponseBody rb = Util.sendSms(from, to, msg);

		return new ResultData("S-1", "발송되었습니다.", "from", from, "to", to, "msg", msg, "rb", rb);
	}

}