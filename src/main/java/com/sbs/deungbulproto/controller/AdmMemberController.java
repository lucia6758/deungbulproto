package com.sbs.deungbulproto.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.deungbulproto.container.Container;
import com.sbs.deungbulproto.dto.Adm;
import com.sbs.deungbulproto.dto.Client;
import com.sbs.deungbulproto.dto.Expert;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.service.AdmMemberService;
import com.sbs.deungbulproto.service.ClientService;
import com.sbs.deungbulproto.service.ExpertService;
import com.sbs.deungbulproto.util.Util;

@Controller
public class AdmMemberController extends BaseController {
	@Autowired
	private AdmMemberService admMemberService;
	@Autowired
	private ClientService clientService;
	@Autowired
	private ExpertService expertService;

	@RequestMapping("/adm/member/login")
	public String login() {
		return "adm/member/login";
	}

	@RequestMapping("/adm/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw, String redirectUrl, HttpSession session) {
		if (loginId == null) {
			return Util.msgAndBack("loginId를 입력해주세요.");
		}

		Adm existingAdm = admMemberService.getMemberByLoginId(loginId);

		if (existingAdm == null) {
			return Util.msgAndBack("존재하지 않는 아이디 입니다.");
		}

		if (loginPw == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}

		if (existingAdm.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		if (admMemberService.isAdmin(existingAdm) == false) {
			return Util.msgAndBack("관리자만 접근할 수 있는 페이지 입니다.");
		}

		session.setAttribute("loginedAdmId", existingAdm.getId());

		String msg = String.format("%s님 환영합니다.", existingAdm.getName());

		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@RequestMapping("/adm/member/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedAdmId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}

	@RequestMapping("/adm/member/modify")
	public String showModify(Integer id, HttpServletRequest req) {
		if (id == null) {
			return msgAndBack(req, "id를 입력해주세요.");
		}

		Adm adm = admMemberService.getAdm(id);

		if (adm == null) {
			return msgAndBack(req, "존재하지 않는 회원번호 입니다.");
		}

		req.setAttribute("adm", adm);

		return "adm/member/modify";
	}

	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public String doModify(@RequestParam Map<String, Object> param, HttpSession session) {
		int loginedAdmId = (int) session.getAttribute("loginedAdmId");
		param.put("id", loginedAdmId);

		ResultData modifyMemberRd = admMemberService.modifyMember(param);
		String redirectUrl = "/adm/home/main";
		return Util.msgAndReplace(modifyMemberRd.getMsg(), redirectUrl);
	}

	@RequestMapping("/adm/member/join")
	public String showJoin() {
		return "adm/member/join";
	}

	@RequestMapping("/adm/member/doJoin")
	@ResponseBody
	public String doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return Util.msgAndBack("아이디를 입력해주세요.");
		}

		Adm existingAdm = admMemberService.getMemberByLoginId((String) param.get("loginId"));

		if (existingAdm != null) {
			return Util.msgAndBack("이미 사용중인 로그인아이디 입니다.");
		}

		if (param.get("loginPw") == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}

		if (param.get("name") == null) {
			return Util.msgAndBack("이름을 입력해주세요.");
		}

		if (param.get("cellphoneNo") == null) {
			return Util.msgAndBack("핸드폰번호를 입력해주세요.");
		}

		if (param.get("email") == null) {
			return Util.msgAndBack("이메일주소를 입력해주세요.");
		}

		admMemberService.join(param);

		String msg = String.format("%s님 환영합니다.", param.get("name"));

		String redirectUrl = Util.ifEmpty((String) param.get("redirectUrl"), "../member/login");

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@RequestMapping("/adm/member/admByAuthKey")
	@ResponseBody
	public ResultData showMemberByAuthKey(String authKey) {
		if (authKey == null) {
			return new ResultData("F-1", "authKey를 입력해주세요.");
		}

		Adm existingAdm = admMemberService.getAdmByAuthKey(authKey);

		if (existingAdm == null) {
			return new ResultData("F-2", "유효하지 않은 authKey입니다.");
		}

		return new ResultData("S-1", String.format("유효한 회원입니다."), "adm", existingAdm);
	}

	@RequestMapping("/adm/member/authKey")
	@ResponseBody
	public ResultData showAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Adm existingAdm = admMemberService.getMemberByLoginId(loginId);

		if (existingAdm == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingAdm.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		// 회원의 디바이스 아이디 토큰 업데이트
		String deviceIdToken = (String) Container.session.deviceIdToken;
		
		if( deviceIdToken != null ) {			
			Map<String, Object> param = new HashMap<>();

			if (deviceIdToken.length() > 0) {
				if (!deviceIdToken.equals(existingAdm.getDeviceIdToken())) {
					param.put("id", existingAdm.getId());
					param.put("deviceIdToken", deviceIdToken);

					admMemberService.modifyMember(param);
				}
			}			
		}
		return new ResultData("S-1", String.format("%s님 환영합니다.", existingAdm.getName()), "authKey",
				existingAdm.getAuthKey(), "id", existingAdm.getId(), "name", existingAdm.getName());
	}

	@GetMapping("/adm/member/expertList")
	public String showExpertList(HttpServletRequest req, @RequestParam Map<String, Object> param) {

		List<Expert> experts = expertService.getForPrintExperts(param);

		req.setAttribute("experts", experts);

		return "adm/member/expertList";
	}

	@GetMapping("/adm/member/expertDetail")
	public String showExpertDetail(HttpServletRequest req, int id) {

		Expert expert = expertService.getForPrintExpert(id);

		req.setAttribute("expert", expert);

		return "adm/member/expertDetail";
	}

	@GetMapping("/adm/member/clientList")
	public String showClientList(HttpServletRequest req, @RequestParam Map<String, Object> param) {

		List<Client> clients = clientService.getForPrintClients(param);

		req.setAttribute("clients", clients);

		return "adm/member/clientList";
	}

	@GetMapping("/adm/member/clientDetail")
	public String showClientDetail(HttpServletRequest req, int id) {

		Client client = clientService.getForPrintClient(id);

		req.setAttribute("client", client);

		return "adm/member/clientDetail";
	}

	@RequestMapping("/adm/member/doConfirmExpert")
	@ResponseBody
	public String doConfirmExpert(int expertId, String confirmExpert) {
		if (confirmExpert.contains("Y")) {
			expertService.confirmExpert(expertId);
		} else {
			expertService.rejectExpert(expertId);
		}

		return Util.msgAndReplace("전문가 승인상태가 변경되었습니다.", "expertDetail?id=" + expertId);
	}

	@GetMapping("/adm/member/getLoginIdDup")
	@ResponseBody
	public ResultData getLoginIdDup(String loginId) {
		if (loginId == null) {
			return new ResultData("F-5", "loginId를 입력해주세요.");
		}

		if (Util.allNumberString(loginId)) {
			return new ResultData("F-3", "로그인아이디는 숫자만으로 구성될 수 없습니다.");
		}

		if (Util.startsWithNumberString(loginId)) {
			return new ResultData("F-4", "로그인아이디는 숫자로 시작할 수 없습니다.");
		}

		if (loginId.length() < 5) {
			return new ResultData("F-5", "로그인아이디는 5자 이상으로 입력해주세요.");
		}

		if (loginId.length() > 20) {
			return new ResultData("F-6", "로그인아이디는 20자 이하로 입력해주세요.");
		}

		if (Util.isStandardLoginIdString(loginId) == false) {
			return new ResultData("F-1", "로그인아이디는 영문소문자와 숫자의 조합으로 구성되어야 합니다.");
		}

		Adm existingAdm = admMemberService.getMemberByLoginId(loginId);

		if (existingAdm != null) {
			return new ResultData("F-2", String.format("%s(은)는 이미 사용중인 로그인아이디 입니다.", loginId));
		}

		return new ResultData("S-1", String.format("%s(은)는 사용가능한 로그인아이디 입니다.", loginId), "loginId", loginId);
	}

}
