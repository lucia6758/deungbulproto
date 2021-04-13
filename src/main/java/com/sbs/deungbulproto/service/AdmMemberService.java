package com.sbs.deungbulproto.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.deungbulproto.dao.AdmMemberDao;
import com.sbs.deungbulproto.dto.Adm;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.util.Util;

@Service
public class AdmMemberService {
	@Autowired
	private AdmMemberDao admMemberDao;

	public ResultData join(Map<String, Object> param) {
		admMemberDao.join(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", String.format("%s님 환영합니다.", param.get("name")), "id", id);
	}

	public Adm getMemberByLoginId(String loginId) {
		return admMemberDao.getMemberByLoginId(loginId);
	}

	public ResultData modifyMember(Map<String, Object> param) {
		admMemberDao.modifyMember(param);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

	public static boolean isAdmin(int actorId) {
		return actorId == 1;
	}

	public boolean isAdmin(Adm actor) {
		return isAdmin(actor.getId());
	}

	public Adm getMember(int id) {
		return admMemberDao.getMember(id);
	}

	public Adm getMemberByAuthKey(String authKey) {
		return admMemberDao.getMemberByAuthKey(authKey);
	}

}
