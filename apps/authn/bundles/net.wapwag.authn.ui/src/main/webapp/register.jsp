<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Sign up</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="css/sign/sign.css" rel="stylesheet">
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <script src="scripts/jquery-1.11.1.min.js"></script>
<script src="scripts/jquery-form.js"></script>
<script src="scripts/jquery.cookie.js"></script>
<script src="scripts/jQuery.md5.js"></script>
<script>
	function on_submit() {

		if (form1.username.value == "") {
			alert("用户名不能为空，请输入用户名！");
			form1.username.focus();
			return false;
		}
		if (form1.email.value == "") {
			alert("邮箱不能为空，请输入邮箱！");
			form1.email.focus();
			return false;
		}
		if (form1.phone1.value == "") {
			alert("电话不能为空，请输入电话");
			form1.phone1.focus();
			return false;
		}
		if (form1.password_hash.value == "") {
			alert("用户密码不能为空，请输入密码！");
			form1.password_hash.focus();
			return false;
		}
		if (form1.password_salt.value == "") {
			alert("用户确认密码不能为空，请输入密码！");
			form1.password_salt.focus();
			return false;
		}
		if (form1.password_hash.value != form1.password_salt.value) {
			alert("密码与确认密码不同");
			form1.password_hash.focus();
			return false;
		}
		return true;
	}
	$(function() {
		$("#submit").on("click", function() {
			if(on_submit()) {
				$.ajax({
					cache: true,
					type: "POST",
					url:"/authn/register",
					data:$('#form1').serialize(),// 你的formid
					async: false,
					error: function(request) {
						alert("Connection error");
					},
					beforeSend: function(request) {
		                request.setRequestHeader("authorization", "Bearer token2");
		            },
					success: function(data) {
						$("#commonLayout_appcreshi").parent().html(data);
						window.location.href = "login.jsp";


					}
				});
			}
		});

	});
</script>

  <body>

    <div class="container">
      <form class="form-signin" name="form1" id="form1">
        <div class="logo-div">
            <img src="images/wpg.png" width="118" height="41">
        </div>
        <h2 class="form-signin-heading">Please sign up</h2>
			<label for="inputName" class="sr-only">username</label>
				<input type="text" name="username" id="inputName" class="form-control" placeholder="username" required autofocus >
			<label for="inputEmail" class="sr-only">email</label>
				<input type="email" name="email" id="inputEmail" class="form-control" placeholder="email" required>
			<label for="inputPhone" class="sr-only">phone</label>
				<input type="text" name="phone1" id="inputPhone" class="form-control" placeholder="phone" required>
			<label for="inputPassword" class="sr-only">password</label>
				<input type="password" name="password_hash" id="inputPassword" class="form-control" placeholder="password" required>
			<label for="confirmPassword" class="sr-only">password</label>
				<input type="password" name="password_salt" id="confirmPassword" class="form-control" placeholder="confirm password" required>
			<button class="btn btn-lg btn-primary btn-block" name="Submit" id="submit" type="button">Sign up</button>
			<div class="checkbox">
          <label>
            Already have an account? <a href="login.jsp">To login.</a>
          </label>
        </div>
      </form>
      <div class="bottom-content">
        Copyright � 2016.Wei Pai Ge All rights reserved
      </div>
    </div> <!-- /container -->

    <!-- Javascript for login -->
    <script src="js/jquery-1.11.1.min.js"></script>
    <script src="js/main/index.js"></script>
  </body>
</html>
