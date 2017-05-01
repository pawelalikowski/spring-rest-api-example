<html>

<head></head>

<body>
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>Somebody requested password reset for your account. If you wish to reset password please click link below:</p>
<a href="http://localhost:8080/auth/resetPassword?user=${user.email}&token=${token.token}">Reset password</a>
<p>Thanks</p>
</body>

</html>