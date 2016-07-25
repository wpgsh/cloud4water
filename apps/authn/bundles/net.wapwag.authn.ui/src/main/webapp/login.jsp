<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Sign in</title>
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
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <script src="scripts/jquery-1.11.1.min.js"></script>
  <script src="scripts/jquery-form.js"></script>
  <script src="scripts/jquery.cookie.js"></script>
  <script src="scripts/jQuery.md5.js"></script>
  <script src="js/login.js"></script>
  <script src="scripts/jQuery.base64.js"></script>  
  <body>

    <div class="container">
      <form class="form-signin" id="form" action="/authn/loginServlet" method="post">
        <div class="logo-div">
            <img src="images/wpg.png">
        </div>
        <h2 class="form-signin-heading logo-title">Authentication Application</h2>
        <label for="inputName" class="sr-only">Username</label>
        <input type="text" id="inputName" name="userName" class="form-control" placeholder="username" required autofocus>
		<p class="p-container" id="userNameMsg"></p>
        <label for="inputPassword" class="sr-only">Password</label>
        <input name="passWord" type="password" id="inputPassword" class="form-control" placeholder="password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" id="ck_rmbUser" value="remember-me"> Remember me
          </label>
          <a href="/authn/resetpassword/linkpwd.jsp" class="getpassword">Forgot password?</a>
        </div>
        <button id="submit" class="btn btn-lg btn-success btn-block" type="button">Sign in</button>
        <div class="checkbox">
          <label>
            New to here? <a href="register.jsp">Create an account.</a>
          </label>
        </div>
      </form>
      <div class="bottom-content">
        Copyright © 2016.Wei Pai Ge All rights reserved
      </div>
    </div> <!-- /container -->

    <!-- Javascript for login -->
  </body>
</html>
