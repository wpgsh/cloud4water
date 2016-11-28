jQuery(document).ready(function() {
		var isEncoded = initCookie();
		
		$('input[name="passWord"]').bind('input propertychange', function() {
			isEncoded = true;
			
		});
		var windowResetUrl = window.location.search;
		if(!isEmp(windowResetUrl) && windowResetUrl.indexOf("reset=success") > 0){
			showError("密码修改成功，请登录");
		}
		
		$("#submitButton").click(function() {
			$("#userNameMsg").html("");
			var windowUrl = window.location.search;
			var userName = $('input[name="userName"]').val();
			var passWord = $('input[name="passWord"]').val();
			if(isEmp(userName))
			{
				showError("登录名不可以为空");
				return;
			}
			if(isEmp(passWord))
			{
				showError("登录密码不可以为空");
				return;
			}
			$.ajax({
				url:"/authn/loginServlet",
				type:'post',
				dataType : 'json',
				data:{"userName":userName,"passWord":passWord,"check":"1"},
				success:function(data){
					var errorCode = data.errorCode;
					if("0" != errorCode){
						showError("");
						$('input[name="passWord"]').val("");
					}
					if("0" == errorCode){
						save();
						document.getElementById("form").submit();
					}
				},
				error:function(data)
				{
					alert("error");
				}
			});
		});
		
		// 回车键事件 
		// 绑定键盘按下事件  
	    $(document).keypress(function(e) {  
	    // 回车键事件  
	    	if(e.which == 13) {  
	    	   $("#submitButton").click();  
	       }  
	    }); 
	});

	function showError(msg){
		if("" == msg){
			msg="用户名或密码错误";
		}
		$("#errorMsg").remove();
		var model = "<div id='errorMsg' class='errMesage'><span>"+msg+"</span><label>x</label></div>";
		$(".logo-title").after(model);

		// 取消提示
		$(".errMesage label").on("click", function(){
			$(".errMesage").fadeOut(200, function(){
				$(this).remove();
			});
		});
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
	//记住用户信息
	function save() { 
		if ($("#ck_rmbUser").prop("checked")) { 
			var username = $('input[name="userName"]').val(); 
			var password = $('input[name="passWord"]').val(); 
			var user_cookie = username + "|" + password;
			user_cookie = jQuery.base64.encode(user_cookie);
			$.cookie("rmbUser", "true", { expires: 7 }); //存储一个带7天期限的cookie 
			$.cookie("user_cookie", user_cookie, { expires: 7 });  
		}else{ 
			$.cookie("rmbUser", "false", { expire: -1 }); 
			$.cookie("user_cookie", "", { expires: -1 }); 
		} 
	}; 

	function initCookie(){
		if ($.cookie("rmbUser") == "true") { 
			$("#ck_rmbUser").prop("checked", true); 
			var user_cookie = $.cookie("user_cookie");
			user_cookie = jQuery.base64.decode(user_cookie);
			$('input[name="userName"]').val(user_cookie.split("|")[0]); 
			$('input[name="passWord"]').val(user_cookie.split("|")[1]);
			return false;
		} 
		return true;
	}