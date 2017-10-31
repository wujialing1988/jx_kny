// 按钮鼠标移入移出显示方法
function btnShow(id,className) {
	document.getElementById(id).className = className;
}

// 提交登录表单方法
function loginSubmit(){
	// 这里可以做js的验证
	var userid = document.getElementById('userIdInput').value;
	var pwd = document.getElementById('passWordInput').value;
	if(userid == null || userid == '') {
		document.getElementById("msgIco").className = "msgIco";
		document.getElementById("megText").innerText = "请输入登录帐号！";
		document.getElementById('userIdInput').focus();
		return;
	}
	if(pwd == null || pwd == '') {
		document.getElementById("msgIco").className = "msgIco";
		document.getElementById("megText").innerHTML = "请输入登录密码！";
		document.getElementById('passWordInput').focus();
		return;
	}
	// 提交
	loginForm.submit();
}

// 重置登录表单方法
function loginReset(){
	document.getElementById("msgIco").className = "";
	document.getElementById("megText").innerText = "";
	document.getElementById('userIdInput').value="";
	document.getElementById('passWordInput').value="";
	document.getElementById('userIdInput').focus();
}

// 回车提交方法
document.onkeydown=function(event){
	if(window.event){
		event = window.event;
	}
	if(event.keyCode==13){
		loginSubmit();
	}
}


// 显示插件页面
function showSupport(){
//	alert("显示插件页面，尚未完成");
	window.open(ctx + '/frame/installfiles/support.jsp', 'supportWin', 'height=400,width=600,top=300,left=300,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
}

// 显示关于页面
function showAbout(){
	alert("显示关于页面，尚未完成");
}