package com.sbs.deungbulproto.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.deungbulproto.dto.Expert;

@Mapper
public interface ExpertDao {
	List<Expert> getForPrintExperts(Map<String, Object> param);

	void join(Map<String, Object> param);

	Expert getExpert(@Param("id") int id);

	Expert getForPrintExpert(@Param("id") int id);

	Expert getExpertByLoginId(@Param("loginId") String loginId);

	void modifyExpert(Map<String, Object> param);

	Expert getExpertByAuthKey(@Param("authKey") String authKey);

	List<Expert> getExperts();

	List<Expert> getForPrintExpertsByRegion(@Param("region") String region);

	void setWork2(@Param("expertId") Integer expertId);

	void setWork1(@Param("expertId") Integer expertId);

	Expert getExpertByNameAndEmail(Map<String, Object> param);

	Expert getMemberByLoginIdAndEmail(Map<String, Object> param);

	void delete(@Param("id") int id);

	void confirmExpert(@Param("id") int id);

	void rejectExpert(@Param("id") int id);

	List<Expert> getExpertsForSendSms(@Param("region") String region);

}
