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
	var isEncoded = initCookie();
	
	$('input[name="passWord"]').bind('input propertychange', function() {
		isEncoded = true;
		
	});
	
	$("#submit").click(function() {
		$("#userNameMsg").html("");
		var windowUrl = window.location.search;
		var userName = $('input[name="userName"]').val();
		var passWord = $('input[name="passWord"]').val();
		var i = 0;
		if(isEmp(userName))
		{
			showError();
			i++;
		}
		if(isEmp(passWord))
		{
			showError();
			i++;
		}
		if(0 < i){
			return;
		}
		if(isEncoded){
			var newPassWord = $.md5(passWord);
			$('input[name="passWord"]').val(newPassWord);
		}
		$("#form").ajaxSubmit({
			type:'post',
			dataType : 'json',  
			success:function(data){
				var errorCode = data.errorCode;
				if("1" == errorCode){
					showError();
				}
				if("0" == errorCode){
					save();
					window.location="index.jsp";  
				}
				if("000000" == errorCode){
					save();
					if(!isEmp(windowUrl) && windowUrl.indexOf("client_id") > 0 && windowUrl.indexOf("redirect_uri") > 0 ){
						window.location=data.errorMsg;
					}else{
						window.location="index.jsp";
					}
					 
				}
			},
			error:function(data)
			{
				alert("error");
			}
		});
	});
});

function showError(){
	$('input[name="passWord"]').val("");
	$("#userNameMsg").html("Incorrect username or password.");
}
function isEmp(str){
	if(undefined == str || null == str || "" == str){
		return true;
	}
	str = str.trim();
	if(undefined == str || null == str || "" == str){
		return true;
	}
	return false;
}
function refulsCodeImg(){
	$("#codeImg").attr("src","/authn/checkCode?" + Math.random());
}
//记住用户名密码 
function save() { 
	if ($("#ck_rmbUser").prop("checked")) { 
		var username = $('input[name="userName"]').val(); 
		var password = $('input[name="passWord"]').val(); 
		$.cookie("rmbUser", "true", { expires: 7 }); //存储一个带7天期限的cookie 
		$.cookie("username", username, { expires: 7 }); 
		$.cookie("password", password, { expires: 7 }); 
	}else{ 
		$.cookie("rmbUser", "false", { expire: -1 }); 
		$.cookie("username", "", { expires: -1 }); 
		$.cookie("password", "", { expires: -1 }); 
	} 
}; 

function initCookie(){
	if ($.cookie("rmbUser") == "true") { 
		$("#ck_rmbUser").prop("checked", true); 
		$('input[name="userName"]').val($.cookie("username")); 
		$('input[name="passWord"]').val($.cookie("password"));
		return false;
	} 
	return true;
}
 
</script>  
  <body>

    <div class="container">
      <form class="form-signin" id="form" action="/authn/loginServlet" method="post">
        <h2 class="form-signin-heading">Please sign in</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input name="userName" type="text" id="inputEmail" class="form-control" placeholder="username" required autofocus>
        <p class="p-container" id="userNameMsg"></p>
		<label for="inputPassword" class="sr-only">Password</label>
        <input name="passWord" type="password" id="inputPassword" class="form-control" placeholder="password" required>
        <p class="p-container" id="pwMsg"></p>
		<div class="checkbox">
          <label>
            <input type="checkbox" id="ck_rmbUser" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" id="submit" type="button">Sign in</button>
        <div class="checkbox">
          <label>
            New to here? <a href="register.jsp">Create an account.</a>
          </label>
        </div>
      </form>
    </div> <!-- /container -->
  </body>
</html>
