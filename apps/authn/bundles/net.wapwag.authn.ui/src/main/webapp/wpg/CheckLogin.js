        /*
        检查浏览器是否支持
        */
        var isIE = !!window.ActiveXObject;
        var isIE6 = isIE && !window.XMLHttpRequest;
        if (isIE6) {
            window.location.href = RootPath() + "/Error/Browser/BrowsError.html";
        }
        try {
            var bro = $.browser;
            if (bro.msie) {
                //binfo = "Microsoft Internet Explorer " + bro.version; 
                var version = bro.version;
                if (version <= 8) {
                    window.open(RootPath() + "/Error/Browser/BrowsError.html", 'IE浏览器升级提示');
                }
            }
        } catch (ex) {
        }

        //回车键
        document.onkeydown = function (e) {
            if (!e) e = window.event; //火狐中是 window.event
            if ((e.keyCode || e.which) == 13) {
                var btlogin = document.getElementById("btnLogin");
                btlogin.focus();
                btlogin.click();
            }
        }
        $(function () {

            ClearSession("");

            //读取COOKIE
            try {
                var cookie_phone = getCookie("loginphone");
                if (IsNullOrEmpty(cookie_phone)) {
                    $("#txtaccount").val(cookie_phone);
                }
            }
            catch (ex) {
            }

            /*
            验证码倒计时
            */
            $('#btnGetViryCode').on('click', function () {
                var strPhone = $("#txtaccount").val();
                if (!IsNullOrEmpty(strPhone)) {
                    $.dialog.tips("手机号码不能为空！", 1, 'fail.png');
                    $("#txtaccount").focus();
                    return false;
                }
                var postData = {
                    StrValidPhoneNo: strPhone
                }
                // public void GetVerifyCode(string StrValidPhoneNo)
                getAjax_Json('/Webservice/InnerUserService.asmx/GetVerifyCode', postData, function (data) {
                    var msgData = data;
                    if (IsNullOrEmpty(msgData)) {
                        if (msgData.MessageCode == 1 || msgData.MessageCode == "1") {
                            $.dialog.tips(msgData.Message, 1, 'succ.png');
                        }
                        else {
                            $.dialog.tips(msgData.Message, 1, 'fail.png');
                            $("#txtaccount").focus();
                            return false;
                        }
                        time($('#btnGetViryCode'), 60);
                    }
                    else {
                        $.dialog.tips("未能从服务器接收到数据,请重试！", 1, 'fail.png');
                        $("#txtaccount").focus();
                        return false;
                    }
                });
            });

            //验证码倒计时
            var time = function (o, wait) {
                if (wait == 0) {
                    o.removeAttr("disabled");
                    o.val("获取验证码");
                } else {
                    o.attr("disabled", true);
                    o.val('' + wait + '秒后重新获取');
                    wait--;
                    setTimeout(function () {
                        time(o, wait)
                    },
            1000)
                }
            };

            //登录按钮点击
            $("#btnLogin").click(function () {
                var txtaccount = $("#txtaccount").val();
                var txtpassword = $("#txtpassword").val();
                var msgCode = $("#txtMsgCode").val();
                if (!IsNullOrEmpty(txtaccount)) {
                    $("#txtaccount").focus();
                    $.dialog.tips('手机号码不能为空！', 1, 'hits.png');
                    return false;
                } else if (!IsNullOrEmpty(txtpassword)) {
                    $("#txtpassword").focus();
                    $.dialog.tips('登录密码不能为空！', 1, 'hits.png');
                    return false;
                }
                else if (!IsNullOrEmpty(msgCode)) {
                    $("#txtMsgCode").focus();
                    $.dialog.tips('短信验证码不能为空！', 1, 'hits.png');
                    return false;
                }
                else {
                    $("#btnLogin").attr("disabled", true);
                    $(".login").attr("disabled", true);
                    $.dialog.tips('请稍后,正在验证帐号...', 600, 'loading.gif');
                    window.setTimeout(function () {
                        var postData = {
                            userName: escape(txtaccount),
                            pwd: escape(txtpassword),
                            verifyCode: escape(msgCode)
                        }
                        // public string CheckUserLogin(string userName, string pwd, string verifyCode)
                        getAjax_Json('/Webservice/InnerUserService.asmx/CheckUserLogin', postData, function (rs) {
                            var userData = rs;
                            if (userData != undefined && userData != null) {
                                if (userData.CheckLoginFlag == 1 || userData.CheckLoginFlag == "1") {

                                    /*if ($("#jCheckBox").attr("checked") == true) {
                                        
                                    }*/

                                    try {
                                        addCookie("loginphone", txtaccount, 7 * 24);
                                    } catch (ex) {
                                    }


                                    $.dialog.tips('登录验证成功,正在跳转首页...', 1, 'succ.png');

                                    window.setTimeout(function () {
                                        Load(userData.Xh, userData.DeptOrderID)
                                    }, 500);
                                }
                                else {
                                    $.dialog.tips(userData.Msg, 1, 'fail.png');
                                    $(".login").attr("disabled", false);
                                    $("#txtaccount").focus();
                                    $("#btnLogin").attr("disabled", false);

                                    return false;
                                }
                            }
                            else {
                                $.dialog.tips("手机号码或者密码错误，请重新输入！", 1, 'fail.png');
                                $(".login").attr("disabled", false);
                                $("#txtaccount").focus();
                                $("#btnLogin").attr("disabled", false);
                                return false;
                            }
                        });
                    }, 500);
                }
            });

            /*
            忘记密码 找回密码
            */
            $("#findPwd").click(function () {
                var txtaccount = $("#txtaccount").val();
                var msgCode = $("#txtMsgCode").val();
                if (!IsNullOrEmpty(txtaccount)) {
                    $("#txtaccount").focus();
                    $.dialog.tips('手机号码不能为空！', 1, 'hits.png');
                    return false;
                }
                if (!IsNullOrEmpty(msgCode)) {
                    $("#txtMsgCode").focus();
                    $.dialog.tips('短信验证码不能为空！', 1, 'hits.png');
                    return false;
                }
                var postData = {
                    StrValidPhoneNo: txtaccount,
                    StrVerifyCode: msgCode
                }
                //public void FindPwd(string StrValidPhoneNo, string StrVerifyCode)
                getAjax_Json('/Webservice/InnerUserService.asmx/FindPwd', postData, function (rs) {
                    var findPwdRet = rs;
                    if (!IsNullOrEmpty(findPwdRet)) {
                        $("#txtaccount").focus();
                        $.dialog.tips('手机号或者验证码为空错误,请重新输入！', 1, 'fail.png');
                        return false;
                    } else {
                        $("#txtpassword").focus();
                        $.dialog.tips('密码已经发到您的手机上,请注意查收！', 1, 'succ.png');
                    }
                });
            });

            /*$("#btnGetViryCode").click(function () {
            GetViryCode();
            });*/
        });
        //登录加载
        function Load(xh, deptOrderID) {
            //window.location.href = RootPath() + "/Index.html?userXh=" + xh + "&t=" + deptOrderID;
            window.location.href = RootPath() + "/Index.html?userXh=" + xh;
            return false;
        }

        /*
         *	清除session
         */

        function ClearSession() {
            try {
                getAjax("/Webservice/InnerUserService.asmx/ClearSeeeion", "", function (data) {

                });
            }
            catch (ex) {
            }
        }
        //获取验证码
        function GetViryCode() {
            var strPhone = $("#txtaccount").val();
            if (!IsNullOrEmpty(strPhone)) {
                $.dialog.tips("手机号码不能为空！", 1, 'fail.png');
                $("#txtaccount").focus();
                return false;
            }
            var postData = {
                StrValidPhoneNo: strPhone
            }
            // public void GetVerifyCode(string StrValidPhoneNo)
            getAjax_Json('/Webservice/InnerUserService.asmx/GetVerifyCode', postData, function (data) {
                var msgData = data;
                if (IsNullOrEmpty(msgData)) {
                    if (msgData.MessageCode == 1 || msgData.MessageCode == "1") {
                        $.dialog.tips(msgData.Message, 1, 'succ.png');
                    }
                    else {
                        $.dialog.tips(msgData.Message, 1, 'fail.png');
                        $("#txtaccount").focus();
                        return false;
                    }
                }
                else {
                    $.dialog.tips("未能从服务器接收到数据,请重试！", 1, 'fail.png');
                    $("#txtaccount").focus();
                    return false;
                }
            });
        }