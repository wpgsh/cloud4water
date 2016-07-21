/*
 *index.js for login/register/set password
 *console.info($.fn.jquery);// jquery 版本
 *-------------------------------------------
 *checkLoginUser()  判断用户名和密码是否正确
 */
(function($){
	
	try{
		// 默认用户
		window.user = JSON.parse(localStorage.getItem("user"));
		if(!user){
			window.user = {};
			user.username = "admin";
			user.password = "123";
			user.phone = "12345678910";
			user.email = "123456@163.com";
			window.localStorage.setItem("user", JSON.stringify(user));
		}
		
		/*---------------------------------------------------------------*/
		/* 登录 */
		$("#login").on("click", function(){
			// 表单信息
			var username = $("#inputName").val();
			var password = $("#inputPassword").val();
			// console.info(username);
			// console.info(password);
			if(!checkLoginUser(username, password)){
				var message = "user is not defined.";
				showError($(".logo-title"), message);
				return false;
			}
			console.info("login success");
		});

		// 判断登录用户
		var checkLoginUser = function(name, pwd){
			var loginFlag = false;// 登录标示
			if(name == user.username && pwd == user.password){
				loginFlag = true;
			}
			return loginFlag;
		};

		/*---------------------------------------------------------------*/
		/* 注册 */
		$("#register").on("click", function(){
			// 表单信息
			var r_username = $("#inputName").val();
			var r_email = $("#inputEmail").val();
			var r_phone = $("#inputPhone").val();
			var r_password = $("#inputPassword").val();

			if(r_email == user.email || r_phone == user.phone){
				var message = "Email or phone has been userd.";
				showError($(".logo-title"), message);
				return false;
			} else {
				var registerUser = {};
				registerUser.username = r_username;
				registerUser.email = r_email;
				registerUser.phone = r_phone;
				registerUser.password = r_password;
				window.localStorage.setItem("user" ,JSON.stringify(registerUser));
			}
			
		});

		/*---------------------------------------------------------------*/
		/* 发送密码修改的链接 */
		$("#sendLink").on("click", function(){
			// Email
			var send_email = $("#inputEmail").val();
			if(send_email != user.email){
				var message = "The email you input is not defined.";
				showError($(".logo-title"), message);
				return false;
			}
		});

		/*---------------------------------------------------------------*/
		/* 修改密码 */
		$("#changePwd").on("click", function(){
			// password
			var new_pwd = $("#inputNewPwd").val();
			user.password = new_pwd;
			window.localStorage.setItem("user" ,JSON.stringify(user));
		});

		/*---------------------------------------------------------------*/
		/* 消息提示模板 */
		var showError = function(obj, msg){
			var model = "<div class='errMesage'><span>"+msg+"</span><label>x</label></div>";
			obj.after(model);

			// 取消提示
			$(".errMesage label").on("click", function(){
				$(".errMesage").fadeOut(200, function(){
					$(this).remove();
				});
			});
		}

	} catch(e){
		console.error(e);
	}

})(jQuery);