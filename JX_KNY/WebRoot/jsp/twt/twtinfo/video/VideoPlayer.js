/** **************** 定义全局变量开始 **************** */
var g_szIP = "10.2.4.99",
	g_szPort = "80",
	g_szUsername = "admin",
	g_szPassword = "12345678a";

var g_iWndIndex = 0; //可以不用设置这个变量，有窗口参数的接口中，不用传值，开发包会默认使用当前选择窗口
var g_channelArray = [];		// 获取到的通道对象数组
/** **************** 定义全局变量结束 **************** */

/** **************** 定义全局函数开始 **************** */
/**
 * 提示信息
 * @param {} msg 信息内容
 */ 
function showOPInfo(msg){
	MyExt.Msg.alert(msg);
}
/** **************** 定义全局函数结束 **************** */

/**
 * 获取通道
 * @param {} szIP NVR网络IP地址（例如：127.0.0.1）
 */
function getChannelInfo(szIP) {
	var nAnalogChannel = 0;

	if ("" == szIP || null == szIP) {
		return;
	}

	// 模拟通道
	WebVideoCtrl.I_GetAnalogChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("VideoInputChannel");
			nAnalogChannel = oChannels.length;

			$.each(oChannels, function (i) {
				var id = parseInt($(this).find("id").eq(0).text(), 10),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Camera " + (id < 9 ? "0" + id : id);
				}
				g_channelArray.push({id: id, name: name});
			});
			showOPInfo(szIP + " 获取模拟通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取模拟通道失败！");
		}
	});
	// 数字通道
	WebVideoCtrl.I_GetDigitalChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("InputProxyChannelStatus");

			$.each(oChannels, function (i) {
				var id = parseInt($(this).find("id").eq(0).text(), 10),
					name = $(this).find("name").eq(0).text(),
					online = $(this).find("online").eq(0).text();
				if ("false" == online) {// 过滤禁用的数字通道
					return true;
				}
				if ("" == name) {
					name = "IPCamera " + ((id - nAnalogChannel) < 9 ? "0" + (id - nAnalogChannel) : (id - nAnalogChannel));
				}
				g_channelArray.push({id: id, name: name});
			});
			showOPInfo(szIP + " 获取数字通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取数字通道失败！");
		}
	});
	// 零通道
	WebVideoCtrl.I_GetZeroChannelInfo(szIP, {
		async: false,
		success: function (xmlDoc) {
			var oChannels = $(xmlDoc).find("ZeroVideoChannel");
			
			$.each(oChannels, function (i) {
				var id = parseInt($(this).find("id").eq(0).text(), 10),
					name = $(this).find("name").eq(0).text();
				if ("" == name) {
					name = "Zero Channel " + (id < 9 ? "0" + id : id);
				}
				if ("true" == $(this).find("enabled").eq(0).text()) {// 过滤禁用的零通道
					g_channelArray.push({id: id, name: name});
				}
			});
			showOPInfo(szIP + " 获取零通道成功！");
		},
		error: function () {
			showOPInfo(szIP + " 获取零通道失败！");
		}
	});
}

/**
 * 登录
 * @param {} szIP			NVR网络IP地址
 * @param {} szPort			NVR网络端口编号
 * @param {} szUsername		NVR用户名
 * @param {} szPassword		NVR密码
 */
function clickLogin(szIP, szPort, szUsername, szPassword) {

	if ("" == szIP || "" == szPort) {
		return;
	}

	var iRet = WebVideoCtrl.I_Login(szIP, 1, szPort, szUsername, szPassword, {
		success: function (xmlDoc) {
			showOPInfo(szIP + " 登录成功！");

			setTimeout(function () {
				getChannelInfo(szIP);
//				Ext.each(g_channelArray, function(record){
//					showOPInfo(record.name);
//					showOPInfo(record.id);
//				});
			}, 10);
		},
		error: function () {
			showOPInfo(szIP + " 登录失败！");
		}
	});

	if (-1 == iRet) {
		showOPInfo(szIP + " 已登录过！");
	}
}

/**
 * 退出
 */ 
function clickLogout(szIP) {

	if (szIP == "") {
		return;
	}

	var iRet = WebVideoCtrl.I_Logout(szIP);
	if (0 == iRet) {
		szInfo = "退出成功！";

		$("#ip option[value='" + szIP + "']").remove();
		getChannelInfo();
	} else {
		szInfo = "退出失败！";
	}
	showOPInfo(szIP + " " + szInfo);
}

/**
 * 建议当前页面要打开的视频通道号是否存在
 * @param {} iChannelID 视频通道号
 * @return {Boolean} true:存在 false:不存在
 */
function isExist(iChannelID) {
	if (null == iChannelID  || "" == iChannelID) {
		return false;
	}
	for (var i = 0; i < g_channelArray.length; i++) {
		if (iChannelID == g_channelArray[i].id) {
			return true;
		}
	}
	return false;
}

/**
 * 开始预览
 * @param {} szIP NVR网络IP地址
 * @param {} iChannelID 视频通道号
 */
function clickStartRealPlay(szIP, iChannelID) {
	var oWndInfo = WebVideoCtrl.I_GetWindowStatus(g_iWndIndex),
		iStreamType = 1,
		bZeroChannel = false,
		szInfo = "";
		
		setTimeout(function(){}, 1000);

	if ("" == szIP) {
		return;
	}

	if (oWndInfo != null) {// 已经在播放了，先停止
		WebVideoCtrl.I_Stop();
	}

	var iRet = WebVideoCtrl.I_StartRealPlay(szIP, {
		iStreamType: iStreamType,
		iChannelID: iChannelID,
		bZeroChannel: bZeroChannel
	});

	if (0 == iRet) {
		szInfo = "开始预览成功！";
	} else {
		szInfo = "开始预览失败！";
	}

	showOPInfo(szIP + " " + szInfo);
}

/**
 * 台位图上右键【视频预览】视频播放
 * @param {} siteID 站场ID
 * @param {} videoCode 视频监控点编码
 */
function playVideo(siteID, videoCode) {
	if (Ext.isEmpty(siteID) || Ext.isEmpty(videoCode)) {
		MyExt.Msg.alert('数据异常，视频监控预览失败！');
	}
	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		url: ctx + "/siteVideoNvrChanel!getModel.action",
		jsonData: Ext.encode({
			siteID: siteID,				// 站场ID
			videoCode: videoCode		// 视频监控点编码
		}),
		success: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
	        var result = Ext.util.JSON.decode(response.responseText);
	        if (result.errMsg == null) {       //操作成功     
	        	if (!Ext.isEmpty(result.entity)) {
	        		var nvr = result.entity.siteVideoNvr;
					// 登录
	        		g_szIP = nvr.nvrIP;
					clickLogin(nvr.nvrIP, nvr.nvrPort, nvr.username, nvr.password);
	        		
					// 开启视频预览
					setTimeout(function(){
						if (isExist(result.entity.chanelID)) {
							clickStartRealPlay(nvr.nvrIP, result.entity.chanelID);
						} else {
							alert("视频监控预览失败，未知的视频通道！");
						}
					}, 500)
	        	} else {
	        		alert("视频监控预览失败，当前视频监控点还未进行摄像头绑定！");
	        	}
	        } else {                        
	            alertFail(result.errMsg);
	        }
	    }
	}));
}
/**
 * 台位图上右键视频监控绑定页面【视频预览】视频播放
 * @param {} videoNvrIDX 网络硬盘录像机NVR主键
 * @param {} chanelID 通道号
 */
function previewVideo(videoNvrIDX, chanelID) {
	if (Ext.isEmpty(videoNvrIDX) || Ext.isEmpty(chanelID)) {
		MyExt.Msg.alert('数据异常，视频监控预览失败！');
	}
	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
		url: ctx + "/siteVideoNvr!getModel.action",
		params: {id: videoNvrIDX},
		success: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
	        var result = Ext.util.JSON.decode(response.responseText);
	        if (result.errMsg == null) {       //操作成功     
	        	if (!Ext.isEmpty(result.entity)) {
	        		var nvr = result.entity;
					// 登录
	        		g_szIP = nvr.nvrIP;
					clickLogin(nvr.nvrIP, nvr.nvrPort, nvr.username, nvr.password);
	        		
					// 开启视频预览
					setTimeout(function(){
						if (isExist(chanelID)) {
							clickStartRealPlay(nvr.nvrIP, chanelID);
						} else {
							alert("视频监控预览失败，未知的视频通道！");
						}
					}, 500)
	        	}
	        } else {                        
	            alertFail(result.errMsg);
	        }
	    }
	}));
}

/**
 * 页面加载完成后的初始化操作
 */
$(function () {
	// 检查插件是否已经安装过
	if (-1 == WebVideoCtrl.I_CheckPluginInstall()) {
		alert('系统检测到您还未安装视频播放插件，请先下载安装后重试！');
		window.location.href = ctx + '/frame/installfiles/SoftwareDownload.jsp';
		// 如果没有安装插件，则提示用户进行插件下载并安装
		return;
	}
	
	// 初始化插件参数及插入插件
//	WebVideoCtrl.I_InitPlugin(400, 400, {
//	WebVideoCtrl.I_InitPlugin(document.body.scrollWidth, document.body.scrollHeight, {
	WebVideoCtrl.I_InitPlugin("100%", "100%", {
        iWndowType: 1,
		cbSelWnd: function (xmlDoc) {
			g_iWndIndex = $(xmlDoc).find("SelectWnd").eq(0).text();
			var szInfo = "当前选择的窗口编号：" + g_iWndIndex;
			showOPInfo(szInfo);
		}
	});
	WebVideoCtrl.I_InsertOBJECTPlugin("divPlugin");

	// 检查插件是否最新（暂时不对插件版本进行检查）
//	if (-1 == WebVideoCtrl.I_CheckPluginVersion()) {
//		alert("检测到新的插件版本，双击开发包目录里的WebComponents.exe升级！");
//		return;
//	}
	
	// 窗口事件绑定
	$(window).bind({
		unload: function () {
			// 退出登录
			clickLogout(g_szIP);
		}
	});
	
	// 设置窗口分割数为1
	WebVideoCtrl.I_ChangeWndNum(1);
	
	// 播放视频
	// previewType定义，详见：VideoPlayer.jsp
	if (previewType == previewType_0) {
		// 台位图上右键【视频预览】视频播放
		playVideo(siteID, videoCode);
		
	} else if (previewType == previewType_1) {
		// 台位图上右键视频监控绑定页面【视频预览】类型
		previewVideo(videoNvrIDX, chanelID)
	} else {
		alert("错误的视频监控预览类型！");
	}
	
});