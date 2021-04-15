package com.sbs.deungbulproto.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.service.RatingService;
import com.sbs.deungbulproto.util.Util;

@Controller
public class UsrRatingController {
	@Autowired
	private RatingService ratingService;

	@PostMapping("/usr/rating/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param) {

		if (param.get("relTypeCode") == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		if (param.get("relId") == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		if (param.get("clientId") == null) {
			return new ResultData("F-1", "clientId를 입력해주세요.");
		}
		String relTypeCode = (String) param.get("relTypeCode");
		Integer relId = Util.getAsInt(param.get("relId"), 0);
		Integer clientId = Util.getAsInt(param.get("clientId"), 0);
		
		boolean isClientCanAddRating = ratingService.isClientCanAddRating(relTypeCode, relId, clientId);

		if (isClientCanAddRating == false) {
			return new ResultData("F-2", "회원님은 이미 평점을 작성하셨습니다.");
		}

		return ratingService.addRating(param);
	}

	@GetMapping("/usr/rating/getRatingRelClient")
	@ResponseBody
	public ResultData getRatingRelClient(String relTypeCode, Integer relId, Integer clientId) {

		if (relTypeCode == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}
		if (relId == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		if (clientId == null) {
			return new ResultData("F-1", "clientId를 입력해주세요.");
		}

		return ratingService.getRatingRelClient(relTypeCode, relId, clientId);
	}

	@PostMapping("/usr/rating/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {

		if (param.get("relTypeCode") == null) {
			return new ResultData("F-1", "relTypeCode를 입력해주세요.");
		}

		if (param.get("relId") == null) {
			return new ResultData("F-1", "relId를 입력해주세요.");
		}
		if (param.get("clientId") == null) {
			return new ResultData("F-1", "clientId를 입력해주세요.");
		}
		if (param.get("point") == null) {
			return new ResultData("F-1", "point를 입력해주세요.");
		}

		return ratingService.modifyRating(param);
	}

}
