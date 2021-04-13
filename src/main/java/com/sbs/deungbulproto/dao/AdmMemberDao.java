package com.sbs.deungbulproto.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.deungbulproto.dto.Adm;

@Mapper
public interface AdmMemberDao {
	void join(Map<String, Object> param);

	Adm getMemberByLoginId(@Param("loginId") String loginId);

	void modifyMember(Map<String, Object> param);

	Adm getMember(@Param("id") int id);

	Adm getMemberByAuthKey(@Param("authKey") String authKey);

}
