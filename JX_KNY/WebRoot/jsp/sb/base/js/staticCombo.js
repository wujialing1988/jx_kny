/**
 * 文件说明：静态数据选择控件
 * 说明： 些JS文件函数参数为具休Ext Combo控件属性，参数为JSON形式
 * @author:张凡
 * 成都运达科技股份有限公司
 */

/**
 * 是否选择控件
 */
function YesOrNo(param, isQuery){
	var data = new Array();
	if(isQuery)
		data.push(['', '全部']);
	data.push(['0',"否"]);
	data.push(['1',"是"]);
	return getCombo(param, data);
};

function getYesOrNo(value){
	if(value === 0){
		return '<span style="color:gray">否</span>';
	}else{
		return '是';
	}
	return '';
}

function DynamicCombo(param, pattern){
	var data = new Array();
	if(pattern === true)
		data.push(['', '全部']);
	else if(pattern === 'edit')
		data.push(['', '无']);
	data.push([1,"调入"]);
	data.push([2,"调出"]);
	data.push([3,"新购"]);
	data.push([4,"报废"]);
	data.push([5,"出租"]);
	data.push([6,"租用"]);
	return getCombo(param, data);
}
/**
 * 设备动态 
 * @param value
 * @returns {String}
 */
function getDynamic(value){
	if(value == 1) return '调入';
	if(value == 2) return '调出';
	if(value == 3) return '新购';
	if(value == 4) return '报废';
	if(value == 5) return '出租';
	if(value == 6) return '租用';
	return '';
}

/**
 * 等级
 * @param param
 * @param pattern [true||'query']
 * @returns {___anonymous866_1123}
 */
function Level(param, pattern){
	var data = new Array();
	if(pattern === true)
		data.push(['', '全部']);
	else if(pattern === 'edit')
		data.push(['', '无']);
	data.push([1,"一级"]);
	data.push([2,"二级"]);
	data.push([3,"三级"]);
	data.push([4,"四级"]);
	return getCombo(param, data);
}

function getLevel(v){
	var i = ['一', '二', '三', '四'];
	if(v){
		return i[v - 1] + "级";
	}
	return '';
}

/**
 * 管理级别
 * @param param
 * @param isQuery
 * @returns {___anonymous866_1123}
 */
function ManageLevel(param, pattern){
	var data = new Array();
	if(pattern === true)
		data.push(['', '全部']);
	if(pattern === 'edit')
		data.push(['', '无']);
	data.push(['段',"段"]);
	data.push(['局',"局"]);
	data.push(['部',"部"]);
	return getCombo(param, data);
}


/**
 * 管理类别
 * @param param
 * @param pattern
 * @returns {___anonymous866_1123}
 */
function ManageClass(param, pattern){
	var data = new Array();
	if(pattern === true)
		data.push(['', '全部']);
	if(pattern === 'edit')
		data.push(['', '无']);
	data.push(['A',"A"]);
	data.push(['B',"B"]);
	data.push(['C',"C"]);
	return getCombo(param, data);
}

/**
 * 班制选择
 * @param param
 * @param pattern
 * @returns {___anonymous957_1214}
 */
function RunShift(param, pattern){
	var data = new Array();
	if(pattern === true)
		data.push(['', '全部']);
	if(pattern === 'edit')
		data.push(['', '无']);
	data.push(['0',"一班制"]);
	data.push(['1',"二班制"]);
	data.push(['2',"三班制"]);
	data.push(['3',"四班制"]);
	return getCombo(param, data);
}

function getRunShift(v){
	var i = ['一', '二', '三', '四'];
	if(v === 0){
		return i[0] + "班制";
	}else if(v && v < 4 && v > 0){
		return i[v] + "班制";
	}
	return '';
}
/**
 * 获取选择年份的下拉框
 * @param param
 * @param min_year
 * @param isQuery
 * @param editor 编辑状态，可不填
 * @returns {___anonymous2372_2629}
 */
function yearCombo(param, min_year, isQuery, editor){
	
	var years = [];
	var max = param.max_year || year;
	delete param.max_year;
	min_year  = min_year || 1980;
	
	for(var i = max; i >= min_year; i--){
		years.push([i, i + "年"]);
	}
	var j = {
    	value: new Date().getFullYear(),
    	hiddenName: "year",
    	width: 70
    };
	if(isQuery){
		years.unshift(['', editor ? '无' : '全部']);
		delete j.value;
	}
	Ext.apply(j, param);
	return getCombo(j, years);
}

/**
 * 月下拉框
 * @param param
 * @param isQuery
 * @returns {___anonymous2779_3036}
 */
function monthCombo(param, isQuery){
	var months = [];
	for(var i = 1; i <= 12; i++){
		months.push([i, i + "月"]);
	}
	if(isQuery){
		months.unshift(['', '全部']);
	}
	return getCombo(param, months);
}

/**
 * 创建Combo控件
 * ** 此函数不推荐使用；
 */
function getCombo(param, data){
	var obj = {
		xtype: 'combo',
        store:new Ext.data.SimpleStore({
		    fields: ['value', 'text'],
			data: data
		}),
		valueField:'value',
		value: '',
		editable:false,
		displayField:'text',
		triggerAction:'all',
		mode:'local'/*,
		listeners:{
			
		}*/
	};
	/*if(param){
		for(var i in param){		
			obj[i] = param[i];
		}
	}*/
	Ext.apply(obj, param);
	return obj;
};

/**
 * 合并数据，返回一个新数组
 * descend 要合并到的目标数组
 * material 要被合并的数组或元素
 * end 是否追加到末尾，false为合并到开头
 */
function composite(descend, material, end){
	if(material instanceof Array){
		if(end)
			return descend.concat(material);
		else{
			return material.concat(descend);
		}
	}
	var newArr = descend.slice();
	if(end)
		newArr.push(material);
	else
		newArr.unshift(material);
	return newArr;
}