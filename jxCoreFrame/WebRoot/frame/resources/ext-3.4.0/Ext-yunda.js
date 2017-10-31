/**
 * 定义命名空间，对于Ext相关的扩展、工具类、补充等，都以"Ext.yunda"作为起始命令空间
 */
Ext.namespace("Ext.yunda");
$yd = Ext.yunda;    //Ext.yunda命令控件的简写
/**
 * 创建Ext分页工具栏对象实例
 * @param {} cfg 参数配置项
 * {
 *  pageSize: 页面大小，不配置则默认50
 *  store：Ext.data.Store 数据源，不配置则返回未绑定数据源的分页工具
 * }
 * @return Ext.PagingToolbar
 */
$yd.createPagingToolbar = function(cfg){
    cfg = cfg || {};
    //配置分页工具栏，表格默认每页显示记录数
    var pageSize = cfg.pageSize || 50;  
    //每页显示条数下拉选择框
    var pageComboBox = new Ext.form.ComboBox({
        name: 'pagesize',     triggerAction: 'all',  mode : 'local',   width: 75,
        valueField: 'value',  displayField: 'text',  value: pageSize,  editable: false,
        store: new Ext.data.ArrayStore({
            fields: ['value', 'text'],
            data: [[10, i18n.common.yunda.pageRow10], [20, i18n.common.yunda.pageRow20], [50, i18n.common.yunda.pageRow50], [100, i18n.common.yunda.pageRow100]]
        })
    });
    // 改变每页显示条数reload数据
    pageComboBox.on("select", function(comboBox) {
        pagingToolbar.pageSize = parseInt(comboBox.getValue());
        pagingToolbar.store.reload({
            params: {
                start: 0,    limit: pagingToolbar.pageSize
            }
        });
    });
    //一个新实例化表格的分页工具栏
    var pagingToolbar = new Ext.PagingToolbar({
        pageSize: pageSize,   emptyMsg: i18n.common.tip.notFindRecords,
        displayInfo: true,    displayMsg: i18n.common.yunda.pageButton,    
    //        plugins : new Ext.ux.ProgressBarPager(), // 分页进度条
        items: ['-', '&nbsp;&nbsp;', pageComboBox]
    });
    pagingToolbar.pageComboBox = pageComboBox;
    //分页工具栏绑定数据源
    if(cfg.store != null) {
        pagingToolbar.bind(cfg.store);
        cfg.store.on('beforeload', function(store, options){
            store.baseParams.limit = pagingToolbar.pageSize;
        });
    }
    return pagingToolbar;
}

/**
 * 该函数功能实现：匹配数组中每个元素，若元素（类型为字符串）=repalceStr，则用repalceNode替换该元素，注意：只要匹配到第一个满足条件的元素，替换后就中止返回
 * @param {} array 节点数组
 * @param String repalceStr 被替换的节点，类型是字符串
 * @param {} repalceNode 替换后的节点对象
 * @return [] 替换后的数组对象
 */
$yd.repalceArrayNode = function(array, repalceStr, repalceNode){
    var index = null, len = array.length;
    for (var i = 0; i < len; i++) {
        if(array[ i ] == repalceStr) {
            index = i;
            break;
        }
    }
    if(index != null)   array[ index ] = repalceNode;
    return array;
}
/**
 * 判断表格是否有选中记录，若有选中记录返回true，没有选中记录返回false
 * @param {} grid 表格对象
 * @param {} showMsg 未选择记录时，是否给出提示，默认给出提示
 * @param {} msg 自定义未选择记录给出的提示
 * @return {Boolean} 
 */
$yd.isSelectedRecord = function(grid, showMsg, msg){
    if(showMsg == null) showMsg = true;
    if (grid.selModel.getCount() > 0) {
        return true;
    }
    if(showMsg) {
    	if(msg){
    		MyExt.Msg.alert(msg);
    	}else{
    		MyExt.Msg.alert(i18n.common.tip.notSelectRecords);
    	}
    }
    return false;    
}
/**
 * 获取表格中所有勾选记录的idx
 * @param {} grid 表格对象
 * @return {Array} idx主键数组
 */
$yd.getSelectedIdx = function(grid, storeId){
    var data = grid.selModel.getSelections();
    storeId = (storeId == null ? 'idx': storeId);
    var ids = [];
    for (var i = 0; i < data.length; i++){
        ids.push(data[ i ].get(storeId));
    }
    return ids;
}
/**
 * 封装基于表格的删除操作，首先弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求。
 * 目的是减少重复代码，减少代码量，对于删除有特殊要求的业务不建议使用该方法，对于配置项的理解很重要。
 * @param {} cfg 配置项是以Ext.Ajax.request()入参的配置项为基础进行扩展
 * {
 *      scope：  当期函数域指针，必须配置，建议配置为grid对象
 *      url：    请求路径，必须配置
 *      params： 请求参数，根据情况配置
 *      jsonData:   请求附带json数据，根据情况配置
 *      success：请求成功后的回调函数，有默认实现，可不配置
 *      failure：请求失败后的回调函数，有默认实现，可不配置
 *      operateSuccess: 操作成功回调函数，有默认实现也可覆盖，入参（result, response, options）,result:服务器端返回的结果信息json格式
 *      operateFail:    操作失败回调函数，有默认实现也可覆盖，入参（result, response, options）,result:服务器端返回的结果信息json格式
 * }
 * @return void
 */
$yd.confirmAndDelete = function(cfg){
    Ext.Msg.confirm(i18n.common.tip.prompt, i18n.common.tip.dataNotRecover, function(btn){
        if(btn != 'yes')    return;
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    });    
}
/**
 * 该配置项默认定义Ajax请求（新增、编辑、删除）配置属性、函数，使用该配置项目的是减少重复代码量
 * 使用方式：Ext.apply($yd.cfgAjaxRequest(), cfg)
 * @return cfg 配置项json 
 */
$yd.cfgAjaxRequest = function(){
    var cfg = {
	    //当前函数域指针，建议配置为grid对象
//        scope: this,
	    //请求成功后的回调函数
	    success: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
	        var result = Ext.util.JSON.decode(response.responseText);
	        if (result.errMsg == null) {       //操作成功     
	            alertSuccess();
	            this.store.reload(); 
	            this.afterDeleteFn();
	        } else {                           //操作失败
	            alertFail(result.errMsg);
	        }
	    },
	    //请求失败后的回调函数
	    failure: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	    }
	};
    return cfg;
}

/**
 * Ajax请求通用方法
 * @param cfg 配置项是以Ext.Ajax.request()入参的配置项为基础进行扩展 {
 *      scope：  当期函数域指针，必须配置，建议配置为grid对象
 *      url：    请求路径，必须配置
 *      params： 请求参数，根据情况配置
 *      jsonData:   请求附带json数据，根据情况配置
 * }
 * @param callback 操作成功的回调函数，该函数可能有的唯一参数为Ajax请求返回的result对象
 */
$yd.request = function(cfg, callback){
    if(self.loadMask)    self.loadMask.show();
    Ext.Ajax.request(Ext.apply({
    	 success: function(response, options){
 	        if(self.loadMask)    self.loadMask.hide();
 	        var result = Ext.util.JSON.decode(response.responseText);
 	        if (true === result.success) {       //操作成功     
 	            if (callback) {
 	            	callback.call(this, result);
 	            } else {
 	            	alertSuccess();
 	            }
 	        } else {                           //操作失败
 	            alertFail(result.errMsg);
 	        }
 	    },
 	    //请求失败后的回调函数
 	    failure: function(response, options){
 	        if(self.loadMask)    self.loadMask.hide();
 	        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + ('' || response.responseText));
 	    }
    }, cfg));
};

//表格工具类
Ext.namespace("Ext.yunda.GridUtil");
/**
 * 复制对象数组
 * @param [] dest     目标对象
 */
$yd.GridUtil.copy = function(src){
    var dest = [];
    for (var i = 0; i < src.length; i++) {
        var obj = Ext.apply({}, src[ i ]);
        dest.push(obj);
    }
    return dest;
}
/**
 * 设置编辑表单字段显示顺序
 * @param {} fieldList       表格字段配置项数组
 * @param {} fieldOrder     编辑、查询表单字段显示顺序数组
 */
$yd.GridUtil.orderEditForm = function(fieldList, fieldOrder){
    if(fieldOrder == null || fieldOrder.lenght < 1)   return fieldList;
    
    var fieldAry = $yd.GridUtil.copy(fieldList);
    
    var fields = [];
    for (var i = 0; i < fieldOrder.length; i++) {
        var orderField = fieldOrder[ i ];
        for (var j = fieldAry.length - 1; j >= 0; j--) {
            if(fieldAry[ j ].dataIndex == null) continue;
            if(orderField == fieldAry[ j ].dataIndex){
                fields.push(fieldAry[ j ]);
                fieldAry.remove(fieldAry[ j ]);
                break;
            }
        }
    }
    for (var i = 0; i < fieldAry.length; i++) {
        if(fieldAry[ i ].dataIndex == null) continue;
        fields.push(fieldAry[ i ]);
    }
    return fields;
}

/**
 * 设置查询表单字段显示顺序
 * @param {} fieldList      表格字段配置项数组
 * @param {} fieldOrder     编辑、查询表单字段显示顺序数组
 */
$yd.GridUtil.orderSearchForm = function(fieldList, fieldOrder){
    if(fieldOrder == null || fieldOrder.lenght < 1)   return fieldList;
    var width = fieldList[ 0 ].width;
//    return fieldList;
    var fieldAry = $yd.GridUtil.copy(fieldList);
    for (var i = 0; i < fieldOrder.length; i++) {
        var orderField = fieldOrder[ i ];
        orderField.width = width;
        for (var j = fieldAry.length - 1; j >= 0; j--) {
            if(fieldAry[ j ].name == null) continue;
            if(fieldOrder[ i ] == fieldAry[ j ].name){
                fieldOrder[ i ] = fieldAry[ j ];
                break;
            }
        }
    }
    for (var i = fieldOrder.length - 1; i >= 0; i--) {
        if(typeof(fieldOrder[ i ]) == 'string') fieldOrder.remove(fieldOrder[ i ]);
    }
    return fieldOrder;
}

/**
 * 获取Grid的单条记录
 */
$yd.getSingleRecord = function(grid){
	var data = grid.selModel.getSelections();
	if(data.length == 0){
		MyExt.Msg.alert(i18n.common.tip.notSelectRecords);
		return null;
	}else if(data.length > 1){
		MyExt.Msg.alert(i18n.common.tip.onlySelectOneRecord);
		return null;
	}
	return data[0];
}

/**
 * 将JSON封装到Extjs Record对象中
 */
$yd.getRecord = function(json){
	var record = new Ext.data.Record();
	for(var i in json){
		record.set(i, json[i]);
	}
	return record;
}

/** 为_callback传入参数 add by hy*/
function MyEach(obj, _callback){
	for(var i = 0; i < obj.length; i++){
		_callback.call(obj[i], obj, i);
	}
}

