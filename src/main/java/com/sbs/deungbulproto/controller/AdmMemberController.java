package com.sbs.deungbulproto.controller;

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

		Adm existingMember = admMemberService.getMemberByLoginId(loginId);

		if (existingMember == null) {
			return Util.msgAndBack("존재하지 않는 아이디 입니다.");
		}

		if (loginPw == null) {
			return Util.msgAndBack("비밀번호를 입력해주세요.");
		}

		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return Util.msgAndBack("비밀번호가 일치하지 않습니다.");
		}

		if (admMemberService.isAdmin(existingMember) == false) {
			return Util.msgAndBack("관리자만 접근할 수 있는 페이지 입니다.");
		}

		session.setAttribute("loginedMemberId", existingMember.getId());

		String msg = String.format("%s님 환영합니다.", existingMember.getName());

		redirectUrl = Util.ifEmpty(redirectUrl, "../home/main");

		return Util.msgAndReplace(msg, redirectUrl);
	}

	@RequestMapping("/adm/member/doLogout")
	@ResponseBody
	public String doLogout(HttpSession session) {
		session.removeAttribute("loginedMemberId");

		return Util.msgAndReplace("로그아웃 되었습니다.", "../member/login");
	}

	@RequestMapping("/adm/member/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, HttpServletRequest req) {
		if (param.isEmpty()) {
			return new ResultData("F-2", "수정할 정보를 입력해주세요.");
		}

		int loginedMemberId = (int) req.getAttribute("loginedMemberId");
		param.put("id", loginedMemberId);

		return admMemberService.modifyMember(param);
	}

	@RequestMapping("/adm/member/join")
	public String showJoin() {
		return "adm/member/join";
	}

	@RequestMapping("/adm/member/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Adm existingAdm = admMemberService.getMemberByLoginId((String) param.get("loginId"));

		if (existingAdm != null) {
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

		return admMemberService.join(param);
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
	public String doConfirmExpert(int expertId, String confirm) {
		if (confirm.contains("Y")) {
			expertService.confirmExpert(expertId);
		} else {
			expertService.rejectExpert(expertId);
		}

		return Util.msgAndBack("전문가 승인상태가 변경되었습니다.");
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
