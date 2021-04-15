package com.sbs.deungbulproto.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.deungbulproto.dto.Client;

@Mapper
public interface ClientDao {
	void join(Map<String, Object> param);

	Client getMemberByLoginId(@Param("loginId") String loginId);

	void modifyMember(Map<String, Object> param);

	Client getClient(@Param("id") int id);

	Client getForPrintClient(@Param("id") int id);

	Client getClientByLoginId(@Param("loginId") String loginId);

	void modifyClient(Map<String, Object> param);

	Client getClientByAuthKey(@Param("authKey") String authKey);

	List<Client> getClients();

	Client getClientByNameAndEmail(Map<String, Object> param);

	Client getMemberByLoginIdAndEmail(Map<String, Object> param);

	List<Client> getForPrintClients(Map<String, Object> param);

	void clientWithdrawal(@Param("clientId") int clientId);

	void delete(@Param("id") int id);

}
