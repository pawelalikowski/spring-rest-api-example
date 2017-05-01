<html>

<head></head>

<body>
<p>Dear ${user.firstName} ${user.lastName},</p>
<p>Somebody registered your email on our site. If you wish to confirm registration please click on link below:</p>
<a href="http://localhost:8080/auth/confirm?user=${user.email}&token=${token.token}">Confirm registration</a>
<p>Thanks</p>
</body>

</html>