<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="../part/head.jspf"%>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js"></script>

<script>
	const JoinForm__checkAndSubmitDone = false;
	
	let JoinForm__validLoginId = '';
	
	// 로그인 아이디 중복체크 함수
	function JoinForm__checkLoginIdDup() {
		const form = $('.formLogin').get(0);
		
		form.loginId.value = form.loginId.value.trim();
		
		if (form.loginId.value.length == 0) {
			return;
		}
		
		$.get('getLoginIdDup', {
			loginId : form.loginId.value
		}, function(data) {
			let colorClass = 'text-green-500';
			
			if (data.fail) {
				colorClass = 'text-red-500';
			}
			
			$('.loginIdInputMsg').html("<span class='" + colorClass + "'>" + data.msg + "</span>");
			
			if (data.fail) {
				form.loginId.focus();
			} else {
				JoinForm__validLoginId = data.body.loginId;
			}
			
		}, 'json');
	}
	function JoinForm__checkAndSubmit(form) {
		if (JoinForm__checkAndSubmitDone) {
			return;
		}
		
		form.loginId.value = form.loginId.value.trim();
		
		if (form.loginId.value.length == 0) {
			alert('아이디를 입력해주세요.');
			form.loginId.focus();
			
			return;
		}
		
		if (form.loginId.value != JoinForm__validLoginId) {
			alert('아이디 중복체크를 해주세요.');
			form.loginId.focus();
			
			return;
		}
		
		form.loginPw.value = form.loginPw.value.trim();
		
		if (form.loginPw.value.length == 0) {
			alert('비밀번호를 입력해주세요.');
			form.loginPw.focus();
			
			return;
		}
		
		if (form.loginPwConfirm.value.length == 0) {
			alert('비밀번호 확인을 입력해주세요.');
			form.loginPwConfirm.focus();
			
			return;
		}
		
		if (form.loginPw.value != form.loginPwConfirm.value) {
			alert('비밀번호가 일치하지 않습니다.');
			form.loginPwConfirm.focus();
			
			return;
		}
		
		form.name.value = form.name.value.trim();
		
		if (form.name.value.length == 0) {
			alert('이름을 입력해주세요.');
			form.name.focus();
			
			return;
		}
		
		form.email.value = form.email.value.trim();
		
		if (form.email.value.length == 0) {
			alert('이메일을 입력해주세요.');
			form.email.focus();
			
			return;
		}
		
		form.cellphoneNo.value = form.cellphoneNo.value.trim();
		
		if (form.cellphoneNo.value.length == 0) {
			alert('휴대전화번호를 입력해주세요.');
			form.cellphoneNo.focus();
			
			return;
		}
		
		form.submit();
		JoinForm__checkAndSubmitDone = true;
		
	}
	$(function() {
		$('.inputLoginId').change(function() {
			JoinForm__checkLoginIdDup();
		});
		
		$('.inputLoginId').keyup(_.debounce(JoinForm__checkLoginIdDup, 1000));
	});
</script>
<section class="section-login">
	<div
		class="container mx-auto min-h-screen flex items-center justify-center">
		<div class="w-full">
			<div class="logo-bar flex justify-center mt-3">
				<a href="../home/main" class="logo">
					<span>
						<i class="fas fa-people-arrows"></i>
					</span>
					<span>상장례 관리자페이지</span>
				</a>
			</div>
			<form
				class="formLogin bg-white shadow-md rounded px-8 pt-6 pb-8 mt-4"
				action="doJoin" method="POST"
				onsubmit="JoinForm__checkAndSubmit(this); return false;">
				<input type="hidden" name="redirectUrl" value="${param.redirectUrl}" />
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>아이디</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="inputLoginId shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="text" placeholder="로그인 아이디를 입력해주세요."
							name="loginId" maxlength="20" />
						<div class="loginIdInputMsg"></div>
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>비밀번호</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="password" placeholder="비밀번호를 입력해주세요."
							name="loginPw" maxlength="20" />
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>비밀번호 확인</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="shadow appearance-none border border-red rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="password" placeholder="비밀번호를 입력해주세요."
							name="loginPwConfirm" maxlength="20" />
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>이름</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="text" placeholder="이름을 입력해주세요."
							name="name" maxlength="20" />
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>이메일</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="email" placeholder="이메일을 입력해주세요."
							name="email" maxlength="100" />
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span>휴대전화번호</span>
					</div>
					<div class="p-1 md:flex-grow">
						<input
							class="shadow appearance-none border rounded w-full py-2 px-3 text-grey-darker"
							autofocus="autofocus" type="tel"
							placeholder="휴대전화번호를 입력해주세요.(ex)000-0000-0000)"
							name="cellphoneNo" maxlength="15" />
					</div>
				</div>
				<div class="flex flex-col mb-4 md:flex-row">
					<div class="p-1 md:w-36 md:flex md:items-center">
						<span></span>
					</div>
					<div class="p-1">
						<input
							class="btn-primary bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
							type="submit" value="회원가입" />
						<a onclick="history.back();"
							class="btn-info bg-green-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded inline-block">뒤로가기</a>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>

<%@ include file="../part/foot.jspf"%>