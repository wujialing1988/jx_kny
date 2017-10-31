/**
 * 定制的JSON处理工具类
 */
MyJson = {
    /**
     * 处理json数据对象，如果某属性值=""，则将该属性删除
     * @param {} data需要处理的json数据对象
     * @return {} 处理空白字符串后的json数据对象
     */    
    deleteBlankProp: function(data){
        if(data == null)    return null;
        for(prop in data){
            if(data[ prop ] == "" || data[ prop ] == " ")  delete data[ prop ];
        }
        return data;
    },    
    /**
     * 处理json数据对象，如果某属性值=""，则将该属性设置为null
     * @param {} data需要处理的json数据对象
     * @return {} 处理空白字符串后的json数据对象
     */    
    blank2null: function(data){
        if(data == null)    return null;
        for(prop in data){
            if(data[ prop ] == "")  data[ prop ] = null;
        }
        return data;
    },
    /**
     * 将json标准化处理，如{id:111,parent.name: 'li'}转换成{id:111,parent:{name: 'li'}},
     * 适用于extjs提交表单前获取的data数据对象中存在不标准注级联属性如（parent.parent.name）,
     * 通过调用改方法返回标准的JSON对象{parent:{parent:{name:'li'}}}
     * @param {} data 需要标准化处理的json数据对象
     * @return {} 标准化处理后的json数据对象
     */
    doStandard: function(data){
	    var json = {};
	    for(var p in data){
	        var idx = p.indexOf('.');
	        if(idx != -1){   //如果属性名称包含.进行对象层次分隔
	            MyJson.process(data[ p ], p, json);
	        } else {
	            json[ p ] = data[ p ];
	        }
	    }
	    return json;        
    },
    /**
     * json标准化处理的辅助方法，主要处理parent.parent.name这种级联属性
     * @param {} val 级联属性值
     * @param {} proName 级联属性
     * @param {} desJson 级联属性对应的JSON，一层一层递归地赋值
     */
    process: function(val, proName, desJson){
	    var idx = proName.indexOf('.');
	    if(idx == -1) {
	        desJson[ proName ] = val;
	        return;
	    }
	    var p1 = proName.substring(0, idx);
	    if(typeof(desJson[ p1 ]) == "undefined")   desJson[ p1 ] = {}; 
	    MyJson.process(val, proName.substring(idx + 1), desJson[ p1 ]);        
    },
	autoPacking: function(json,clazz){
	    return MyJson.autoPackingJoin(json,json,clazz);
	},
	autoPackingJoin: function(topObj,curObj,clazz,parents){
	    var obj = {};
	    var fields = clazz.fields;
	    for(var i = 0; i < fields.length; i++){
	        var fieldname = typeof(fields[ i ]) == "string" ? fields[ i ] : fields[ i ].name;
	        if(parents != null){
	            fieldname = parents + "." + fieldname;
	        }
	        if(typeof(fields[ i ]) == "object"){
	            //如果属性对象不存在，则考虑参考person.name拼接方式
	            if(curObj == null || curObj[fields[ i ].name] == null){
	                obj[fields[ i ].name] = MyJson.autoPackingJoin(topObj,null,fields[ i ],fieldname);
	            } else {
	                obj[fields[ i ].name] = MyJson.autoPackingJoin(topObj,curObj[fields[ i ].name],fields[ i ],fieldname);
	            }
	        } else {
	            //如果属性对象不存在，则考虑参考person.name拼接方式
	            if (curObj == null || curObj[fields[i]] == null) {
	                obj[fields[i]] = topObj[fieldname];
	            } else {
	                obj[fields[ i ]] = curObj[fields[i]];
	            }
	        }
	    }
	    return obj;
	},
	/**
	 * 复制JSON对象
	 * 张凡 2013/7/23
	 */
	clone : function(para){
		 var rePara = null;
		 var type = Object.prototype.toString.call(para);
		 if(type.indexOf("Object") > -1){
			 rePara = {};
			 for(var i in para){
				rePara[i] = para[i];
			 }
		 }else if(type.indexOf("Array") > 0){
			 rePara = [];
			 for(var i = 0; i < para.length; i++){
				rePara.push(this.clone(para[i]));
			 }
		 }else{
			 rePara = para;
		 }
		 return rePara;
    },
    
    /**
     * 获取json对象属性个数
     * @param json JSON对象 
     * 黄杨 2017.5.18
     */
    getJsonLength : function(json) {
    	var length = 0;
    	for (var prop in json) {
    		length++;
    	}
    	return length;
    }
}
