jQuery(document).ready(function() {
		var isEncoded = initCookie();
		
		$('input[name="passWord"]').bind('input propertychange', function() {
			isEncoded = true;
			
		});
		var windowResetUrl = window.location.search;
		if(!isEmp(windowResetUrl) && windowResetUrl.indexOf("reset=success") > 0){
			showError("密码修改成功，请登录");
		}
		
		$("#submit").click(function() {
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
			if(isEncoded){
				var newPassWord = $.md5(passWord);
				$('input[name="passWord"]').val(newPassWord);
			}
			$("#form").ajaxSubmit({
				type:'post',
				dataType : 'json',  
				success:function(data){
					var errorCode = data.errorCode;
					if("0" != errorCode){
						showError("");
						$('input[name="passWord"]').val("");
					}
					if("0" == errorCode){
						save();
						if(!isEmp(windowUrl) && windowUrl.indexOf("client_id") > 0 
								&& windowUrl.indexOf("redirect_uri") > 0 
								&& windowUrl.indexOf("return_to") > 0){
							var returnUrl = windowUrl.split("return_to=");
							returnUrl = returnUrl[1];
							window.location=returnUrl;
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
		
		// 回车键事件 
		// 绑定键盘按下事件  
	    $(document).keypress(function(e) {  
	    // 回车键事件  
	    	if(e.which == 13) {  
	    	   $("#submit").click();  
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