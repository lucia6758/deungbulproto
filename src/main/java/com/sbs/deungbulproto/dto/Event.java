package com.sbs.deungbulproto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
	private int id;
	private String regDate;
	private String updateDate;
	private String relTypeCode;
	private String relTypeCode2;
	private int relId;
	private int relId2;
	private int accept;
	private int stepLevel;
	private int directOrder;
	private String region;

}
