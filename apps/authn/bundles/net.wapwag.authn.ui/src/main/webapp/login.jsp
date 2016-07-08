<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<script type="application/x-javascript"> addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0,1); } </script>
<!-- font files  -->
<link href='http://fonts.useso.com/css?family=Muli:400,300' rel='stylesheet' type='text/css'>
<link href='http://fonts.useso.com/css?family=Nunito:400,300,700' rel='stylesheet' type='text/css'>
<!-- /font files  -->
<!-- css files -->
<link href="css/style.css" rel='stylesheet' type='text/css' media="all" />
<!-- /css files -->
</head>
<body>
<h1>Login to our system</h1>
<div class="log">
	<div class="content1">
		<h2>Sign In Form</h2>
		<form action="/authn/login" method="post">
			<input type="text" name="userName" value="USERNAME" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'USERNAME';}">
			<input type="password" name="passWord" value="PASSWORD" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'PASSWORD';}">
			<div class="img-container">
				<div class="input-code">
					<input type="text" name="code" value="CODE" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'CODE';}">
				</div>
				<div class="img-code">
					<img src="/authn/checkCode" alt="refresh" border="0" align="absmiddle" onclick="this.src='/authn/checkCode?rnd=' + Math.random();" />
				</div>
			</div>
			<div class="button-row">
				<input type="submit" class="sign-in" value="Sign In">
				<input type="reset" class="reset" value="Reset">
				<div class="clear"></div>
			</div>
		</form>
	</div>
	<div class="clear"></div>
</div>
<div class="footer">
	<p>Copyright &copy; 2016.Company name All rights reserved</p>
</div>

</body>
</html>