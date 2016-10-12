<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Insert title here</title>
<style>
form div {
	margin-left: 2em;
}

div.form-group label {
	display: block;
}
</style>
<script>
	function valid_password(form) {
		if (form.password.value == form.password_confirm.value) {
			return true;
		}
		form.password_confirm.focus();
		alert("비밀번호를 확인해주세요.");
		return false;
	}
</script>
</head>
<body>
	<fieldset>
		<legend>회원가입</legend>
		<c:if test="REGISTER_EXCEPTION">
			<p>${REGISTER_EXCEPTION}</p>
		</c:if>
		<form action="${pageContext.request.contextPath}/register/submit"
			method="post" action="_self" onsubmit="return valid_password(this);">
			<input type="hidden" name="${_csrf.parameterName }"
				value="${_csrf.token }">
			<div class="form-group">
				<label for="userName">아이디(EMAIL)</label> 
				<input type="email"
					maxlength="64" id="userName" name="userName"
					value="${REGISTER.userName}" required autofocus>
			</div>
			<div class="form-group">
				<label for="password">비밀번호</label> 
				<input type="password"	maxlength="64" id="password" name="password" required>
			</div>
			<div class="form-group">
				<label for="password_confirm">비밀번호 확인</label> 
				<input type="password"	maxlength="64" id="password_confirm" name="password_confirm" required>
			</div>
			<div class="form-group">
				<label for="nickName">이름</label> 
				<input type="text" maxlength="32" id="nickName" name="nickName" value="${REGISTER.nickName }" required>
			</div>
			<div class="form-group">
				<button type="submit">가입</button>
			</div>
		</form>
	</fieldset>
</body>
</html>
