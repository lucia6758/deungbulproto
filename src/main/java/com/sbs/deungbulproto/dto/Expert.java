package com.sbs.deungbulproto.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sbs.deungbulproto.service.ExpertService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expert {
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	@JsonIgnore
	private String loginPw;
	private int acknowledgment_step;
	@JsonIgnore
	private String authKey;
	private String name;
	private String cellphoneNo;
	private String email;
	private String region;
	private String license;
	private String career;
	private int work;

	private String extra__thumbImg;
	private String extra__licenseImg;
	private float extra__ratingPoint;
	private List<Review> extra__reviews = new ArrayList<Review>();

	public String getAcknowledgmentStepName() {
		return ExpertService.getAcknowledgmentStepName(this);
	}

	public String getAcknowledgmentStepNameColor() {
		return ExpertService.getAcknowledgmentStepNameColor(this);
	}

}
