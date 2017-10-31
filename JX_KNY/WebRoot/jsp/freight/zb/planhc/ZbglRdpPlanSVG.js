/**
 * 获取车头svg图片
 */
function getHeadImgUrl(title,color){
	var svg = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
    '<polygon points="0,60 40,10  150,10 150,60" style="stroke:'+color+';stroke-width:5;fill-rule:nonzero;" />' +
    '<line x1="50" y1="10" x2="50" y2="60" style="stroke:'+color+';stroke-width:5" />' +
    '<circle cx="50" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
    '<circle cx="110" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
    '<text style="fill:white;font-size:12pt;" x="55" y="40">'+title+'</text>'+
    '</svg>';
    url = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg);
	return url ;
}

/**
 * 获取车身svg图片
 */
function getBodyImgUrl(title,color){
	var svg = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
    '<rect x = "10" y="10" width = "140" height="50" style="stroke:'+color+';stroke-width:5;fill-rule:nonzero;"/>' +
    '<line x1="0" y1="20" x2="10" y2="20" style="stroke:'+color+';stroke-width:5" />' +
    '<line x1="0" y1="50" x2="10" y2="50" style="stroke:'+color+';stroke-width:5" />' +
    '<line x1="50" y1="10" x2="50" y2="60" style="stroke:'+color+';stroke-width:5" />' +
    '<circle cx="50" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
    '<circle cx="110" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
    '<text style="fill:white;font-size:12pt;" x="55" y="40">'+title+'</text>'+
    '</svg>';
	url = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg);
	return url ;
}

/**
 * 获取车头颜色
 * 
 * @param {} status 列检计划状态
 * @return {} 
 */
function getHeadColorByStatus(status){
	var color = "#999999" ;
	if("UNRELEASED" == status){
		color = "#999999" ;
	}else if("COMPLETE" == status){
		color = "#008000" ;
	}else if("ONGOING" == status){
		color = "#00BFFF" ;
	}else if("INTERRUPT" == status){
		color = "red" ;
	}else if("DELAY" == status){
		color = "yellow" ;
	}
	return color ;
}

/**
 * 获取车身颜色
 * 
 * @param {} status 列检车辆状态
 * @return {} 
 */
function getBodyColorByStatus(status){
	var color = "#999999" ;
	if("INITIALIZATION" == status){
		color = "#999999" ;
	}else if("COMPLETE" == status){
		color = "#008000" ;
	}else if("ONGOING" == status){
		color = "#00BFFF" ;
	}
	// 选中事件颜色变化
	else if("SELECTED" == status){
		color = "#00BFFF" ;
	}
	return color ;
}
