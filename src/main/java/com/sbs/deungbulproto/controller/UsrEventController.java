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

	@GetMapping("/usr/event/totalCount")
	@ResponseBody
	public ResultData totalCount(String memberType, Integer memberId) {

		if (memberType == null) {
			return new ResultData("F-1", "memberType를 입력해주세요.");
		}
		
		if (memberId == null) {
			return new ResultData("F-1", "memberId를 입력해주세요.");
		}

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberType", memberType);
		param.put("memberId", memberId);
		if(memberType.equals("expert")) {
			
			Expert expert = expertService.getExpert(memberId);
			String region = expert.getRegion();
			param.put("region", region);
		}

		int eventTotalCount = eventService.getTotalCount(param);

		return new ResultData("S-1", "성공", "totalCount", eventTotalCount);
	}

}
