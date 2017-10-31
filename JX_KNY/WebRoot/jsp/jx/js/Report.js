/**
 * 获取报表文件路径的方法
 */
function getReportEffectivePath(url){
	if(url.indexOf('.cpt') > -1){
		// 解析国际化配置 中文使用默认
		if(!Ext.isEmpty(browserLang) && browserLang != 'zh_cn'){
			var urls = url.split('.cpt');
			url = urls[0] + "_"+browserLang + ".cpt" + urls[1];
		}
   		//报表页面
   		url = getReportPath() + "/ReportServer?reportlet=" + url.replace(/\?+/g,"&");
   		url = cjkEncode(url);
   	}
   	return url;
}
/*中文参数编码*/
function cjkEncode(text) {      
    if (text == null) {      
        return "";      
    }      
 	var newText = "";      
    for (var i = 0; i < text.length; i++) {      
        var code = text.charCodeAt (i);       
        if (code >= 128 || code == 91 || code == 93) {//91 is "[", 93 is "]".      
            newText += "[" + code.toString(16) + "]";      
        } else {      
            newText += text.charAt(i);      
        }      
    }      
 	return newText;      
}