// 显示的快捷方式数量（如果以后修改添加或删除快捷方式，请修改该数量）
var maxShowNum = 4;

//按钮鼠标移入移出显示方法
function btnShow(id,className) {
	document.getElementById(id).className = className;
}

// 快捷方式鼠标移入移出显示
function linkBtnShow(id, className, pointMarginLeftWidthNum, showL2DivName) {
	document.getElementById(id).className = className;

	// 改箭头位置
		// 判断当前快捷方式是所有显示中的第几个
		var curLinkIndex = 0;
		for(var j = 1; j<=maxShowNum; j++){
			var displayObjId = "linkBtn00" + j;
			if(document.getElementById(displayObjId).style.display != "none"){
				if(displayObjId != id){
					curLinkIndex = curLinkIndex + 1;
				}else{
					break;
				}
			}
		}
		// 改箭头位置
		document.getElementById('linkBtnPointDiv').className = "linkBtnPointDiv" + curLinkIndex;

	// 改变二级菜单显示
	for(var i = 1; i<=maxShowNum; i++){
		document.getElementById("linkBtnL200" + i + "BarDiv").style.display = "none";
	}
	document.getElementById("linkBtnL2AddBarDiv").style.display = "none";

	document.getElementById(showL2DivName).style.display = "block";
}

// 鼠标移入移出显示
function btnMoverMoutShow(id, className){
	document.getElementById(id).className = className;
}

// 配置快捷方式
function cfgLink(l1BtnId, l2BtnId){
	var displayValue = getStyleById(l1BtnId, "display");

	if(displayValue=="block"){
		// 隐藏大图标
		document.getElementById(l1BtnId).style.display = "none";
		// 切换显示配置图标为彩色的
		document.getElementById(l2BtnId).style.display = "none";
		document.getElementById(l2BtnId + "_add").style.display = "block";
	}else{
		// 显示大图标
		document.getElementById(l1BtnId).style.display = "block";
		// 切换显示配置图标为彩色的
		document.getElementById(l2BtnId).style.display = "block";
		document.getElementById(l2BtnId + "_add").style.display = "none";
	}

	// **************************TODO (重要！！！！)这里需要发一个ajax请求，将该设置保存下来***********************************

	// 改箭头位置
		// 统计显示了几个快捷方式（不包含“添加快捷方式”那个图标）
		var showL1BtnNum = getLinkL1BtnNum();
		// 改箭头位置
		document.getElementById('linkBtnPointDiv').className = "linkBtnPointDiv" + showL1BtnNum;

}

// 通过ID和样式名获得样式值
function getStyleById(objId,propName){
	var objTemp = document.getElementById(objId);
	
	if(objTemp.currentStyle){
		// 针对IE
		return objTemp.currentStyle[propName];
	}else if(window.getComputedStyle){
		// 针对FF
		propName = propName.replace(/([A-Z])/g,"-$1");
		propName = propName.toLowerCase();
		return document.defaultView.getComputedStyle(objTemp, null)[propName];
	}
	return null;
}

// 获得显示的快捷方式的数量
function getLinkL1BtnNum(){
	var linkL1BtnNum = 0;
	for(var i = 1; i<=maxShowNum; i++){
		var displayStr = document.getElementById('linkBtn00' + i).style.display;
		if(displayStr != "none"){
			linkL1BtnNum = linkL1BtnNum +1;
		}
	}
	return linkL1BtnNum;
}

// 温馨提示链接方法
function tipLinkFunction(){
	alert("功能尚未完成，预期由系统进入时初始化这个链接地址。");
}

// 关闭温馨提示
function tipLinkDivHidden(){
	document.getElementById("tipDiv").style.display = "none";
}