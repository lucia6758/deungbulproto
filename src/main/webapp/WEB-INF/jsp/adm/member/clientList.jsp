<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ include file="../part/mainLayoutHead.jspf"%>

<section class="section-1">
	<div class="bg-white shadow-md rounded container mx-auto p-8 mt-8">
		<div>
			<c:forEach items="${clients}" var="client">
				<c:set var="detailUrl" value="detail?id=${client.id}" />
				<div class="flex items-center mt-10">
					<a class="font-bold">NO. ${client.id}</a>
					<a class="ml-2 font-light text-gray-600">${client.regDate}</a>
					<div class="flex-grow"></div>
				</div>
				<div class="mt-2">
					<a class="mt-2 text-gray-600 block">
						<span
							class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">아이디</span>
						<span>${client.loginId}</span>
					</a>
					<a class="mt-2 text-gray-600 block">
						<span
							class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">이름</span>
						<span>${client.name}</span>
					</a>
					<a class="mt-2 text-gray-600 block">
						<span
							class="inline-flex justify-center items-center px-2 rounded-full bg-green-500 text-white">지역</span>
						<span>${client.region}</span>
					</a>
				</div>
				<div class="flex items-center mt-4">
					<a href="clientDetail?id=${client.id}"
						class="text-blue-500 hover:underline" title="회원상세정보">
						<span>
							<i class="fas fa-info"></i>
							<span class="hidden sm:inline">회원 상세 정보</span>
						</span>
					</a>
					<a onclick="if ( !confirm('이 회원을 탈퇴시키겠습니까?') ) return false;"
						href="doDelete?id=${client.id}"
						class="ml-2 text-blue-500 hover:underline">
						<span>
							<i class="fas fa-trash"></i>
							<span class="hidden sm:inline">강제탈퇴</span>
						</span>
					</a>
					<div class="flex-grow"></div>
				</div>
			</c:forEach>
		</div>
	</div>
</section>

<%@ include file="../part/mainLayoutFoot.jspf"%>
