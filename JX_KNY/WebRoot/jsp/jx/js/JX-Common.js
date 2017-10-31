/**
 * <li>标题: 机车检修管理信息系统
 * <li>说明： 基于检修系统内，所有公用的js工具函数都存放在该文件中，统一命名空间为"JX"
 * <li>创建人：刘晓斌
 * <li>创建日期：2012-08-29
 * <li>修改人: 
 * <li>修改日期：
 * <li>修改内容：
 * <li>版权: Copyright (c) 2008 运达科技公司
 * @author 测控部检修系统项目组
 * @version 1.0
 */ 
//统一命名空间
JX = {};
/**
 * 根据状态值返回中文业务含义，业务状态，0：新增；1：启用；2：作废，后续还要扩展
 * @param {} status 状态值
 * @return 业务状态含义
 */
JX.getBizStatus = function(status){
    if(status == 0) return "新增";
    if(status == 1) return "启用";
    if(status == 2) return "作废";
}
JX.getStatus = function(status){
    if(status == 10) return "新增";
    if(status == 20) return "启用";
    if(status == 30) return "作废";
}
/**
 *物料出库业务状态 
 * @param {} status
 * @return {String}
 */
JX.getExStatus = function(status){
    if(status == 0) return "未确认";
    if(status == 1) return "已确认";
}
/**
 * 是否为新
 * @param {} isNew
 * @return {String} 
 */
JX.getIsNew = function(isNew){
    if(isNew == 1) return "是";
    if(isNew == 0) return "否";
}
/**
 * 申领类型
 * @param {} type 类型值
 * @return {String} 业务类型含义
 */
JX.getApplyType = function(type){
    if(type == 1) return "定量";
    if(type == 2) return "临时";
}
/**
 * 技术改造计划状态
 * @param {} type 类型值
 * @return {String} 业务类型含义
 */
JX.getJSGZType = function(type){
    if(type == 10) return "未发布";
    if(type == 20) return "已发布";
}
/**
 * 物料申请单明细状态
 * @param {} type 类型值
 * @return {String} 业务类型含义
 */
JX.getMaterialApplyType = function(type){
    if(type == 0) return "新增";
    if(type == 1) return "已提交";
    if(type == 2) return "待入库";
    if(type == 3) return "已入库";
    if(type == 4) return "作废";
}

//表单提交方法
JX.submit = function(data,grid){
	var cfg = {
        scope: grid, url: grid.saveURL, jsonData: data,
        success: function(response, options){
            if(self.loadMask)   self.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
            	grid.afterSaveSuccessFn(result, response, options);
            } else {
            	grid.afterSaveFailFn(result, response, options);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}
/**
 * 清除自定义控件的值
 */
JX.cleanValue = function(){
	var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree",
            		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree",
            		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
            		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","TrainRC_combo"];
    for(var j = 0; j < componentArray.length; j++){
    	var component = WorkSeq.saveWin.findByType(componentArray[j]);
    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
    	}else{
    		for(var i = 0; i < component.length; i++){
                component[ i ].clearValue();
            }
    	}	                    
    }
}

/**
 * 清除自定义控件的值
 */
JX.clearValue = function(win){
	var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree",
        		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree",
        		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
        		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","TrainRC_combo","RT_SelectWin",
        		"BureauSelect_comboTree","DeportSelect_comboTree","PartsTypeByBuild_SelectWin"];
    for(var j = 0; j < componentArray.length; j++){
    	var component = win.findByType(componentArray[j]);
    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
    	}else{
    		for(var i = 0; i < component.length; i++){
                component[ i ].clearValue();
            }
    	}	                    
    }
}

/**
 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * @param status 
 * @param field  提示信息字段
 * @return 提示信息字符串
 */
JX.alertOperate = function(infoArray,msg,status,field){
    if(infoArray == null || infoArray.length <= 0)  return msg;
    var info = "";
    var msgInfo = "" ;
    var operInfo = "" ; //启用消息
    var invalidInfo = "" ; //作废消息
    var titleInfo = "";
    if(status=='del'){
        msgInfo = "该操作将不能恢复，是否继续【删除】？";
        operInfo = "不能删除";
        invalidInfo = "不能删除";
        titleInfo = '只能删除状态为【新增】的记录！';
    }
    if(status=='start'){
        msgInfo = "确定【启用】选择的项？";
        operInfo = "";
        invalidInfo = "不能启用";
        titleInfo = '只能启用状态为【新增】的记录！';
    }
    if(status=='invalid'){
        msgInfo = "确定【作废】选择的项？";
        operInfo = "不能作废";
        invalidInfo = "";
        titleInfo = '只能作废状态为【启用】的记录！';
    }
    
    if(infoArray instanceof Array){
        for(var i = 0; i < infoArray.length; i++){
            if(infoArray[ i ].get("status") == STATUS_ADD){
                info += (i + 1) + ". 【" + infoArray[ i ].get(field) + "】为新增"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == STATUS_USE){
                info += (i + 1) + ". 【" + infoArray[ i ].get(field) + "】已经启用"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == STATUS_INVALID){
                info += (i + 1) + ". 【" + infoArray[ i ].get(field) + "】已经作废"+invalidInfo+"！</br>";
                msgInfo = msg;
            }
        }
    } else {
        info = infoArray;
    }
    return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}

/**启用或作废或删除
 * (validStatus 验证通过的状态)
 * entity:操作的实体类
 * _grid : grid对象
 * _operate：操作方式（启用or作废or删除）
 * entity：操作的实体
 * field：提示信息字段
 * */
JX.UpdateStatus = function(validStatus, _grid, _operate,entity,field){
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(_grid)) return;
    
    var data = _grid.selModel.getSelections();
    var ids = new Array();
    var flag = new Array(); //标记选择的项目
    for (var i = 0; i < data.length; i++){
        if(data[i].get('status') == validStatus){
            ids.push(data[ i ].get("idx"));
        }else{
            flag.push(data[i]);
        }
    }
    //提示信息，请求参数
    var action, msgOnly, url, params;
    switch(_operate){
    case 'del':
        action = '删除';
        msgOnly = '只能【删除】新增状态的记录！';
        url = ctx + '/'+entity+'!logicDeleteCascade.action';
        params = {ids: ids};
        break;
    case 'start':
        action = '启用';
        msgOnly = '只能【启用】新增状态的记录！';
        url = ctx + '/'+entity+'!updateStatus.action';
        params = {ids: ids, status: STATUS_USE};
        break;
    case 'invalid':
        action = '作废';
        msgOnly = '只能【作废】启用状态的记录！';  
        url = ctx + '/'+entity+'!updateStatus.action';
        params = {ids: ids, status: STATUS_INVALID};
        break;
    }
    if(ids.length <= 0){
        MyExt.Msg.alert(msgOnly);
        return;
    }
    //弹出信息确认框，根据用户确认后执行操作
    Ext.Msg.confirm('提示',JX.alertOperate(flag,'是否执行' + action + '操作，该操作将不能恢复！',_operate,field), function(btn){   
        if(btn != 'yes')    return;
        var cfg = Ext.apply($yd.cfgAjaxRequest(), {
            url: url, params: params,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
		            //操作成功            
		            alertSuccess();
		            _grid.store.reload(); 
		            _grid.afterDeleteFn();
		        } else {
		            //操作失败
		        	  _grid.store.reload(); 
		            alertFail(result.errMsg);
		        }                
            }            
        });
        Ext.Ajax.request(cfg);
    });
}
