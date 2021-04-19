package com.sbs.deungbulproto.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.service.ClientService;
import com.sbs.deungbulproto.util.Util;

@Controller
public class UsrClientController extends BaseController {
	@Autowired
	private ClientService clientService;

	@GetMapping("/usr/client/login")
	public String login() {
		return "/usr/client/login";
	}

	@PostMapping("/usr/client/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {
		if (loginId == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Client existingClient = clientService.getClientByLoginId(loginId);

		if (existingClient == null) {
			return Util.msgAndBack("존재하지 않는 아이디 입니다.");
		}

		if (loginPw == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}

		if (existingClient.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		session.setAttribute("loginedMemberId", existingClient.getId());

		String msg = String.format("%s님 환영합니다.", existingClient.getName());

		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");
		
		String deviceIdToken = (String) session.getAttribute("deviceIdToken");
		Map<String, Object> param = new HashMap<>();
		
		if(deviceIdToken.length() <= 0) {
			if( !deviceIdToken.equals( existingClient.getDeviceIdToken() ) ) {
				param.put("id", existingClient.getId() );
				param.put("deviceIdToken", deviceIdToken);
				
				clientService.modifyClient(param);
			}
		}

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@GetMapping("/usr/client/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedMemberId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}

	@PostMapping("/usr/client/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {
		if (param.isEmpty()) {
			return new ResultData("F-2", "수정할 정보를 입력해주세요.");
		}

		return clientService.modifyClient(param);
	}

	@GetMapping("/usr/client/join")
	public String showJoin() {
		return "usr/client/join";
	}

	@PostMapping("/usr/client/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Client existingClient = clientService.getClientByLoginId((String) param.get("loginId"));

		if (existingClient != null) {
			return new ResultData("F-2", String.format("%s (은)는 이미 사용중인 로그인아이디 입니다.", param.get("loginId")));
		}

		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (param.get("name") == null) {
			return new ResultData("F-1", "name을 입력해주세요.");
		}

		if (param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "cellphoneNo를 입력해주세요.");
		}

		if (param.get("email") == null) {
			return new ResultData("F-1", "email을 입력해주세요.");
		}

		if (param.get("region") == null) {
			return new ResultData("F-1", "region을 입력해주세요.");
		}


		return clientService.join(param);
	}

	@GetMapping("/usr/client/doDelete")
	@ResponseBody
	public ResultData doDelete(HttpServletRequest req, int id) {

		Client client = clientService.getForPrintClient(id);

		if (client == null) {
			return new ResultData("F-1", "로그인 후 이용가능합니다.");
		}

		clientService.delete(id);
		req.setAttribute("name", client.getName());

		return new ResultData("S-1", "성공", "body", client.getName());
	}

	@GetMapping("/usr/client/clientByAuthKey")
	@ResponseBody
	public ResultData showMemberByAuthKey(String authKey) {
		if (authKey == null) {
			return new ResultData("F-1", "authKey를 입력해주세요.");
		}

		Client existingClient = clientService.getClientByAuthKey(authKey);

		if (existingClient == null) {
			return new ResultData("F-2", "유효하지 않은 authKey입니다.");
		}

		return new ResultData("S-1", String.format("유효한 회원입니다."), "client", existingClient);
	}

	@PostMapping("/usr/client/authKey")
	@ResponseBody
	public ResultData showAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Client existingClient = clientService.getForPrintClientByLoginId(loginId);

		if (existingClient == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingClient.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		return new ResultData("S-1", String.format("%s님 반갑습니다.", existingClient.getName()), "authKey",
				existingClient.getAuthKey(), "client", existingClient);
	}

	@GetMapping("/usr/client/detail")
	@ResponseBody
	public ResultData showClientDetail(HttpServletRequest req, int id) {

		Client client = clientService.getForPrintClient(id);

		req.setAttribute("client", client);

		return new ResultData("S-1", "성공", "client", client);
	}

}
