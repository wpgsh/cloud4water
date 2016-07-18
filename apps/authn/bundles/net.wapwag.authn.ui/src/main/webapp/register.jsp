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
    <title>Sign up</title>
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="css/signin.css" rel="stylesheet">
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
jQuery(document).ready(function() {
	
	$("#submit").click(function() {
		
		$("#form").ajaxSubmit({
			type:'post',
			dataType : 'json',  
			success:function(data){
				var errorCode = data.errorCode;
				if("1" == errorCode){
					
				}
				if("0" == errorCode){
					
				}
			},
			error:function(data)
			{
				alert("error");
			}
		});
	});
});
 
</script>  
  <body>

    <div class="container">
      <form class="form-signin" id="form" action="/authn/register" method="post">
        <h2 class="form-signin-heading">Please sign up</h2>
        <label for="inputName" class="sr-only">username</label>
        <input type="text" name="userName" id="inputName" class="form-control" placeholder="username" required autofocus>
        <label for="inputEmail" class="sr-only">email</label>
        <input type="email" name="email" id="inputEmail" class="form-control" placeholder="email" required>
        <label for="inputPhone" class="sr-only">phone</label>
        <input type="text" name="phone1" id="inputPhone" class="form-control" placeholder="phone" required>
        <label for="inputPassword" class="sr-only">password</label>
        <input type="password" name="password_hash" id="inputPassword" class="form-control" placeholder="password" required>
        <label for="confirmPassword" class="sr-only">password</label>
        <input type="password" id="confirmPassword" class="form-control" placeholder="confirm password" required>
        <button class="btn btn-lg btn-primary btn-block" id="submit" type="button">Sign up</button>
        <div class="checkbox">
          <label>
            Already have an account? <a href="login.jsp">To login.</a>
          </label>
        </div>
      </form>
    </div> <!-- /container -->
  </body>
</html>
