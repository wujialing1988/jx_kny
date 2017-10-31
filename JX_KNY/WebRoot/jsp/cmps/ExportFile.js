/*
 * grid的filed上加属性
 * 	ignore:true 标识不输出该列
 *  translate:[0,'不合格',1,'警告',2,'待检',3,'合格'] 翻译该列
 *  datePattern:'yyyy-MM-dd HH:mm:ss' 日期格式化 （默认为年月日）
 *  xconvert:自定义转换函数
 */
function exportExcel(url, params){
	var form = document.createElement("form");
	form.action = url;
	form.method = 'post',
	form.target = '_excel';
	if(params instanceof Array){
		loop(form, params);
	}else if(typeof params == 'object'){
		var patterns = getPatterns(params);
		append(form, patterns);
		var sp = getSearch(params);
		loop(form, sp);
		sort(form, params);
	}
	document.body.appendChild(form);
	form.submit();
	document.body.removeChild(form);
}
function sort(form, grid){
	if(grid.store.sortInfo){
		if(grid.store.sortInfo.field)
			append(form, {"name":"sort", value: grid.store.sortInfo.field});
		if(grid.store.sortInfo.direction)
			append(form, {"name":"dir", value: grid.store.sortInfo.direction});
	}
}
function loop(form, params){
	for(var i = 0; i < params.length; i++){
		append(form, params[i]);
	}
}
function append(form, params){
	var hidden = document.createElement("input");
	hidden.type = "hidden";
	hidden.name = params.name;
	hidden.value = params.value;
	form.appendChild(hidden);
}
function getPatterns(grid){
	var cols = grid.colModel.config;
	var columns = [];
	var pattern = {};
	for(var i = 0; i < cols.length; i++){
		if(cols[i].dataIndex && cols[i].header
				&& (cols[i].hidden == undefined || cols[i].hidden === false)
				&& cols[i].ignore != true){
			pattern = {};
			if(cols[i].xconvert){
				pattern.xconvert = cols[i].xconvert + "";
			}else if(cols[i].translate){
				pattern.translate = cols[i].translate;
			}
			if(cols[i].datePattern){
				pattern.datePattern = cols[i].datePattern;
			}
			
			pattern.header = cols[i].header;
			pattern.dataIndex = cols[i].dataIndex;
			columns.push(pattern);
		}
	}
	return {name: 'patterns', value :Ext.util.JSON.encode(columns)};
}

function getSearch(grid){
	var params = grid.store.baseParams;
	var sp = [];
	for(var i in params){
		if(typeof params[i] != 'object'){					
			sp.push({name: i, value: params[i]});
		}
	}
	return sp;
}
