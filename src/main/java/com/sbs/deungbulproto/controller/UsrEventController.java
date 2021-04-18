package com.sbs.deungbulproto.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.service.EventService;
import com.sbs.deungbulproto.service.ExpertService;

@Controller
public class UsrEventController {
	@Autowired
	private EventService eventService;
	@Autowired
	private ExpertService expertService;

	@GetMapping("/usr/event/count")
	@ResponseBody
	public ResultData count(String memberType, Integer memberId, String eventType) {

		if (memberType == null) {
			return new ResultData("F-1", "memberType를 입력해주세요.");
		}
		if (memberId == null) {
			return new ResultData("F-1", "memberId를 입력해주세요.");
		}
		if (eventType == null) {
			return new ResultData("F-1", "eventType를 입력해주세요.");
		}

		String region = "";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberType", memberType);
		param.put("memberId", memberId);
		param.put("region", region);
		
		if (eventType.equals("region")) {
			if (memberType.equals("expert")) {
				Expert expert = expertService.getExpert(memberId);
				region = expert.getRegion();
				param.put("region", region);
			}
			if (memberType.equals("client")) {
				region = "없음";
				param.put("region", region);
			}
		}

		int count = eventService.getCount(param);

		return new ResultData("S-1", "성공", "count", count);
	}

	@GetMapping("/usr/event/resetEvent")
	@ResponseBody
	public ResultData resetEvent(String memberType, Integer memberId, String eventType) {

		if (memberType == null) {
			return new ResultData("F-1", "memberType를 입력해주세요.");
		}
		if (memberId == null) {
			return new ResultData("F-1", "memberId를 입력해주세요.");
		}
		if (eventType == null) {
			return new ResultData("F-1", "eventType를 입력해주세요.");
		}

		String region = "";

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("relTypeCode2", memberType);
		param.put("relId2", memberId);
		param.put("region", region);
		
		if (eventType.equals("region")) {
			if (memberType.equals("expert")) {
				Expert expert = expertService.getExpert(memberId);
				region = expert.getRegion();
				param.put("region", region);
			}
			if (memberType.equals("client")) {
				region = "없음";
				param.put("region", region);
			}
		}

		eventService.resetEventCount(param);

		return new ResultData("S-1", "성공");
	}

}
