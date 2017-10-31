/** 配件组成型号维护 */
Ext.onReady(function(){
Ext.namespace('PartsBuildUp');                       //定义命名空间
PartsBuildUp.searchPartsParams = {};                 //配件组成查询集合对象
PartsBuildUp.searchVirtualParams = {};               //虚拟组成查询集合对象
//根据业务编码生成规则生成组成型号编码
PartsBuildUp.getBuildUpTypeCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
        url: url,
        params: {ruleFunction: "JXGC_BUILDUP_TYPE_BUILDUP_TYPE_CODE"},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                Ext.getCmp("buildUpTypeCode_Id").setValue(result.rule);
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
}
//配件规格型号列表
PartsBuildUp.partsTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + "/partsType!findpageList.action?statue="+partsStatus_use,
    singleSelect: true, 
    autoScroll: true,
    //remoteSort: false,
    tbar:[{
        xtype:"combo", id:"queryType_Id_parts", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"配件名称", mode:"local",triggerAction: "all",
        store: new Ext.data.SimpleStore({
            fields: ["type"],
            data: [ ["配件名称"], ["规格型号"] ]
        })
    },{             
        xtype:"textfield",  id:"typeName_Id_parts", width: 70
    },{
        text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
        handler : function(){
            var typeName = Ext.getCmp("typeName_Id_parts").getValue();
            var querytype = Ext.getCmp("queryType_Id_parts").getValue();
            var searchParam = {status:partsStatus_use};
            if(querytype == '配件名称'){
                searchParam.partsName = typeName;
                PartsBuildUp.partsTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
                PartsBuildUp.partsTypeGrid.getStore().load({
                    params:{
                        entityJson:Ext.util.JSON.encode(searchParam)
                    }                                                               
                });
            }else{
                searchParam.specificationModel = typeName;
                PartsBuildUp.partsTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
                PartsBuildUp.partsTypeGrid.getStore().load({
                    params:{
                        entityJson:Ext.util.JSON.encode(searchParam)
                    }                                                               
                });
            }
        }
    },{
        text: "重置", iconCls: "resetIcon",
        handler : function(){
            var searchParam = {status:partsStatus_use};
            PartsBuildUp.partsTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
            PartsBuildUp.partsTypeGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(searchParam)
                }                                                               
            });
            //清空搜索输入框
            Ext.getCmp("typeName_Id_parts").setValue("");
            Ext.getCmp("queryType_Id_parts").setValue("配件名称");
            //清空配件组成查询集合
            PartsBuildUp.searchPartsParams = {};
        }
    },{
		text : "生成标准组成",
		iconCls : "addIcon",
		handler : function(){
			var cfg = Ext.apply($yd.cfgAjaxRequest(), {
		        params: {'type': type_parts},
		        url: ctx + '/buildUpType!addAllDefault.action', 
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.errMsg == null) {
		                //操作成功            
		                alertSuccess();
		                PartsBuildUp.partsTypeGrid.store.reload(); 
		            } else {
		                //操作失败
		                alertFail(result.errMsg);
		            }
		        }               
		    }); 
		    Ext.Ajax.request(cfg);
		}
	}],
        fields: [{
            header:'配件名称', dataIndex:'partsName', editor:{ }, sortable: true
        },{
            header:'规格型号', dataIndex:'specificationModel', editor:{ }
        },{
        	header:'配件专业类型', dataIndex:'professionalTypeIdx', hidden: true
        },{
        	header:'配件专业类型名称', dataIndex:'professionalTypeName', hidden: true
        },{
            dataIndex:'idx', hidden:true
        }]  
        
});
//单击配件记录过滤配件组成列表
PartsBuildUp.partsTypeGrid.on("rowclick", function(grid, rowIndex, e){
			 var record = grid.getStore().getAt(rowIndex);
            PartsBuildUp.searchPartsParams.partsTypeIDX = record.get("idx");
            PartsBuildUp.searchPartsParams.type = type_parts;
            PartsBuildUp.partsBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
            PartsBuildUp.partsBuildUpGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(PartsBuildUp.searchPartsParams)
                }
            })
            PartsBuildUp.searchVirtualParams.partsTypeIDX = record.get("idx");
			PartsBuildUp.searchVirtualParams.type = type_virtual;
			PartsBuildUp.virtualBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams);
			PartsBuildUp.virtualBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams)
				}
			})
		});
//移除侦听器
PartsBuildUp.partsTypeGrid.un('rowdblclick', PartsBuildUp.partsTypeGrid.toEditFn, PartsBuildUp.partsTypeGrid);
/**
 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * @param status 
 * @return 提示信息字符串
 */
PartsBuildUp.alertOperate = function(infoArray,msg,status){
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
            if(infoArray[ i ].get("status") == status_new){
                info += (i + 1) + ". 【" + infoArray[ i ].get("buildUpTypeName") + "】为新增"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == status_use){
                info += (i + 1) + ". 【" + infoArray[ i ].get("buildUpTypeName") + "】已经启用"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == status_nullify){
                info += (i + 1) + ". 【" + infoArray[ i ].get("buildUpTypeName") + "】已经作废"+invalidInfo+"！</br>";
                msgInfo = msg;
            }
        }
    } else {
        info = infoArray;
    }
    return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}
//设为标准组成
PartsBuildUp.setDefault = function(_grid){
	//未选择记录，直接返回
    if(!$yd.isSelectedRecord(_grid)) return;   
    var ids = $yd.getSelectedIdx(_grid);
    if(ids.length > 1){
    	MyExt.Msg.alert("请只选择一条记录！");
    	return;
    }
    var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': ids[0]},
        url: ctx + '/buildUpType!setIsDefault.action', 
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
                _grid.store.reload(); 
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Ajax.request(cfg);
}
//更新组成记录业务状态，启用或作废
PartsBuildUp.updateStatus = function(validStatus, _grid, _operate){
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
        url = _grid.deleteURL;
        params = {ids: ids};
        break;
    case 'start':
        action = '启用';
        msgOnly = '只能【启用】新增状态的记录！';
        url = ctx + '/buildUpType!updateStatus.action';
        params = {ids: ids, status: status_use};
        break;
    case 'invalid':
        action = '作废';
        msgOnly = '只能【作废】启用状态的记录！';  
        url = ctx + '/buildUpType!updateStatus.action';
        params = {ids: ids, status: status_nullify};
        break;
    }
    if(ids.length <= 0){
        MyExt.Msg.alert(msgOnly);
        return;
    }
    //弹出信息确认框，根据用户确认后执行操作
    Ext.Msg.confirm('提示',PartsBuildUp.alertOperate(flag,'是否执行' + action + '操作，该操作将不能恢复！',_operate), function(btn){   
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
		            alertFail(result.errMsg);
		        }                
            }            
        });
        Ext.Ajax.request(cfg);
    });
}
//添加默认组成函数
PartsBuildUp.addDefault = function(type, id, grid){
    var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'type': type, 'id': id},
        url: ctx + '/buildUpType!addDefault.action', 
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
                grid.store.reload(); 
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Ajax.request(cfg);
}
//删除组成型号函数，点击删除按钮触发的函数
PartsBuildUp.deleteFn = function(){
    if(this.saveWin)    this.saveWin.hide();
    if(this.searchWin)  this.searchWin.hide();        
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(this)) return;
    //执行删除前触发函数，根据返回结果觉得是否执行删除动作
    if(!this.beforeDeleteFn()) return;
    PartsBuildUp.updateStatus(status_new, this, 'del');      
}
//配件组成列表
PartsBuildUp.partsBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
//    storeAutoLoad: false,
    //remoteSort: false,
    tbar: ['search', /*{
        text: "添加默认组成", iconCls:"addIcon", handler : function(){
            var records = PartsBuildUp.partsTypeGrid.selModel.getSelections();
            if(records == null || records.length < 1){
                MyExt.Msg.alert('请选择一条配件规格型号记录！');
                return;
            }
            var id = records[ 0 ].get('idx');
            PartsBuildUp.addDefault(type_parts, id, PartsBuildUp.partsBuildUpGrid);
        }
    },*/'add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            PartsBuildUp.updateStatus(status_new, PartsBuildUp.partsBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            PartsBuildUp.updateStatus(status_use, PartsBuildUp.partsBuildUpGrid, 'invalid');
        }
    },{
        text:"设置标准组成", iconCls:"configIcon", handler:function(){
            PartsBuildUp.setDefault(PartsBuildUp.partsBuildUpGrid);
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				PartsBuildUp.checkQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkQuery(status_nullify);
    		}
    }}],  
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', 
		editor:{ id: 'buildUpTypeCode_Id', allowBlank: false, disabled:true }, searcher:{ disabled: true}, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
    },{
        header:'组成类型', dataIndex:'type', hidden:true, editor:{xtype:'hidden', id:'_type' }, searcher:{ disabled: true}        
	},{
		header:'配件规格型号', dataIndex:'partsTypeIDX', hidden:true,
        editor:{xtype: 'hidden', id:'partsType_IDX'}
	},{
		header:'配件规格型号', dataIndex:'specificationModel', 
        editor:{  
        	  id: 'specification_Model', xtype: 'PartsTypeAndQuota_SelectWin', fieldLabel : '配件规格型号',
	          hiddenName: 'specificationModel', valueField: 'specificationModel', displayField: 'specificationModel',
	          returnField: [{widgetId: "parts_Name", propertyName: "partsName"},
	          				{widgetId: "partsType_IDX", propertyName: "idx"},
	          				{widgetId: "ProfessionalType_hiddenId", propertyName: "professionalTypeIdx"},
	          				{widgetId: "professionalTypeName_Id", propertyName: "professionalTypeName"}],
	          editable:false, //allowBlank: false,
	          listeners:{
				"change": function(){
					//根据选择的配件获取其专业类型
					Ext.getCmp("ProfessionalType_Id").setDisplayValue(Ext.getCmp("ProfessionalType_hiddenId").getValue(),Ext.getCmp("professionalTypeName_Id").getValue());
				}
			}
        },
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件名称', dataIndex:'partsName', 
        editor:{ id:"parts_Name", readOnly:true},
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件专业类型表主键', dataIndex:'professionalTypeIDX', hidden: true, 
		editor:{
			id: "ProfessionalType_Id", xtype: "ProfessionalType_comboTree", fieldLabel: "专业类型", 
			hiddenName: "professionalTypeIDX", returnField: [{widgetId:"professionalTypeName_Id",propertyName:"text"}], 
			selectNodeModel: "all"
		}
	},{
		header:'专业类型', dataIndex:'professionalTypeName', editor:{ id:'professionalTypeName_Id', xtype:'hidden' }
	},{
		header:'专业类型', dataIndex:'professionalTypeIDX1', hidden: true, editor:{ id:'ProfessionalType_hiddenId', xtype:'hidden' }
	},{
		header:'状态', dataIndex:'status', 
		renderer:function(v){
			switch(v){
				case status_new:
					return "新增";
				case status_use:
					return "启用";
				case status_nullify:
					return "作废";
				default :
					return "";
			}
		},
		editor:{  xtype: 'hidden', id:'_status' }
	},{
		header:'是否标准组成', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault_yes:
					return "是";
				case isDefault_no:
					return "否";
				default :
					return "";
			}
		},
		editor:{
			xtype:'hidden',
			id: '_isDefault'
		},
		searcher:{ disabled: true}
	}],
    deleteButtonFn: PartsBuildUp.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        /*var records = PartsBuildUp.partsTypeGrid.selModel.getSelections();
        if(records == null || records.length < 1){
            MyExt.Msg.alert('请选择一条配件规格型号记录！');
            return false;
        }*/
        return true;
    },
    /**
     * 进入编辑窗口之前触发的函数，如果返回false将不显示编辑窗口
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     * @return {Boolean} 如果返回fasle将不显示编辑窗口
     */
    beforeShowEditWin: function(record, rowIndex){
        var status = record.get('status');
//        if(status == status_new)    
        
//        if(status == status_use || status == status_nullify){
//            MyExt.Msg.alert('只能修改【新增】的记录！');
//        }        
//        return false; 
        if(status == status_nullify){
            MyExt.Msg.alert('不能修改【作废】的记录！');
        }else{
        	return true;
        }
        return false; 
    },    
    /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
    afterShowEditWin: function(record, rowIndex){
        Ext.getCmp('ProfessionalType_Id').setDisplayValue(record.get('professionalTypeIDX'),record.get('professionalTypeName'));
        Ext.getCmp('specification_Model').setDisplayValue(record.get('specificationModel'),record.get('specificationModel'));
    },
    //显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
    afterShowSaveWin: function(){
//        var records = PartsBuildUp.partsTypeGrid.selModel.getSelections();
//        Ext.getCmp('parts_Name').setValue(records[ 0 ].get('partsName'));
//        Ext.getCmp('specification_Model').setValue(records[ 0 ].get('specificationModel'));
//        Ext.getCmp('partsType_IDX').setValue(records[ 0 ].get('idx'));
        Ext.getCmp('_type').setValue(type_parts);//新增时默认设置"类型"为"配件组成"
        Ext.getCmp('_status').setValue(status_new);//新增时默认设置"状态"为"新增"
        Ext.getCmp('_isDefault').setValue(isDefault_no);//新增时默认设置为"非标准组成"
        PartsBuildUp.getBuildUpTypeCode();
        Ext.getCmp('ProfessionalType_Id').clearValue();
        Ext.getCmp('specification_Model').clearValue();
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	PartsBuildUp.searchPartsParams.buildUpTypeCode = "";
    	PartsBuildUp.searchPartsParams.buildUpTypeName = "";
    	PartsBuildUp.searchPartsParams.partsName = "";
    	PartsBuildUp.searchPartsParams.specificationModel = "";
    	for(prop in searchParam){
	    	PartsBuildUp.searchPartsParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_parts;   
    	searchParam.partsTypeIDX = PartsBuildUp.searchPartsParams.partsTypeIDX;
    	searchParam.status = PartsBuildUp.status;
    	PartsBuildUp.searchPartsParams.status = PartsBuildUp.status;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    },
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
    	this.enableColumns(["buildUpTypeCode"]);
    },
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
    	this.disableColumns(["buildUpTypeCode"]);
    },
    /**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
        PartsBuildUp.getBuildUpTypeCode();
    },
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){
    	delete data["professionalTypeIDX1"];
        return true; 
    }
});
PartsBuildUp.partsBuildUpGrid.createSaveWin();
PartsBuildUp.partsBuildUpGrid.saveWin.modal = true;
//状态全局变量
PartsBuildUp.status = "";
//状态单击事件方法
PartsBuildUp.checkQuery =  function (status){
	PartsBuildUp.status = status;	
	PartsBuildUp.partsBuildUpGrid.getStore().load();
}
//配件组成列表查询前的处理
PartsBuildUp.partsBuildUpGrid.getStore().on("beforeload",function(){
	PartsBuildUp.searchPartsParams.type = type_parts;
	PartsBuildUp.searchPartsParams.status = PartsBuildUp.status;
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchPartsParams);
	
});
//虚拟组成列表
PartsBuildUp.virtualBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
    storeAutoLoad: false,
    tbar: ['add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            PartsBuildUp.updateStatus(status_new, PartsBuildUp.virtualBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            PartsBuildUp.updateStatus(status_use, PartsBuildUp.virtualBuildUpGrid, 'invalid');
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_virtual_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkVirtualQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_virtual_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				PartsBuildUp.checkVirtualQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_virtual_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkVirtualQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_virtual_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				PartsBuildUp.checkVirtualQuery(status_nullify);
    		}
    }}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
	},{
		header:'组成类型', dataIndex:'type', hidden:true,
		editor:{ xtype: 'hidden', id:'_typeV'},
 		searcher:{ disabled: true}
	},{
		header:'配件规格型号', dataIndex:'partsTypeIDX', hidden:true,
        editor:{xtype: 'hidden', id:'partsType_IDX_v'}
	},{
		header:'配件名称', dataIndex:'partsName', 
        editor:{ id:"parts_Name_v", readOnly:true, allowBlank: false},
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件规格型号', dataIndex:'specificationModel', 
        editor:{ id:"specification_Model_v", readOnly:true, allowBlank: false },
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'状态', dataIndex:'status', 
		renderer:function(v){
			switch(v){
				case status_new:
					return "新增";
					break;
				case status_use:
					return "启用";
					break;
				case status_nullify:
					return "作废";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{xtype:'hidden',id:'_statusV'}
	}],
    deleteButtonFn: PartsBuildUp.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        /*var records = PartsBuildUp.partsTypeGrid.selModel.getSelections();
        if(records == null || records.length < 1){
            MyExt.Msg.alert('请选择一条配件记录！');
            return false;
        }*/
        return true;
    },
    /**
     * 进入编辑窗口之前触发的函数，如果返回false将不显示编辑窗口
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     * @return {Boolean} 如果返回fasle将不显示编辑窗口
     */
    beforeShowEditWin: function(record, rowIndex){
//        var status = record.get('status');
//        if(status == status_new)    
        return true;
        /*if(status == status_use || status == status_nullify){
            MyExt.Msg.alert('只能修改【新增】的记录！');
        }
        return false;*/ 
    },    
    //显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
    afterShowSaveWin: function(){
        var records = PartsBuildUp.partsTypeGrid.selModel.getSelections();
        Ext.getCmp('parts_Name_v').setValue(records[ 0 ].get('partsName'));
        Ext.getCmp('specification_Model_v').setValue(records[ 0 ].get('specificationModel'));
        Ext.getCmp('partsType_IDX_v').setValue(records[ 0 ].get('idx'));
        Ext.getCmp('_typeV').setValue(type_virtual);
        Ext.getCmp('_statusV').setValue(status_new);
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	PartsBuildUp.searchVirtualParams.buildUpTypeCode = "";
		PartsBuildUp.searchVirtualParams.buildUpTypeName = "";

    	for(prop in searchParam){
	    	PartsBuildUp.searchVirtualParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_virtual;
    	searchParam.TypeIDX = PartsBuildUp.searchVirtualParams.TypeIDX;
    	searchParam.status = PartsBuildUp.virtualStatus;
    	PartsBuildUp.searchVirtualParams.status = PartsBuildUp.virtualStatus;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    }
});
PartsBuildUp.virtualBuildUpGrid.createSaveWin();
PartsBuildUp.virtualBuildUpGrid.saveWin.modal = true;

//状态全局变量
PartsBuildUp.virtualStatus = "";
//状态单击事件方法
PartsBuildUp.checkVirtualQuery =  function (status){
	PartsBuildUp.virtualStatus = status;	
	PartsBuildUp.virtualBuildUpGrid.getStore().load();
}
//机车组成列表查询处理
PartsBuildUp.virtualBuildUpGrid.getStore().on("beforeload",function(){
	PartsBuildUp.searchVirtualParams.type = type_virtual;
	PartsBuildUp.searchVirtualParams.status = PartsBuildUp.virtualStatus;
	this.baseParams.entityJson = Ext.util.JSON.encode(PartsBuildUp.searchVirtualParams);
});
//tab选项卡布局
PartsBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "partsBuildUpTab", title: '配件组成型号', layout:'fit', items: [PartsBuildUp.partsBuildUpGrid]
    },{ 
      id: "virtualBuildUpTab", title: '关联虚拟组成型号', layout: 'fit', items: [PartsBuildUp.virtualBuildUpGrid]
    }]
});
//配件组成页面
PartsBuildUp.partsBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '配件', width: 350, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
        collapsible : true,
        autoScroll: true, layout: 'fit', items : [ PartsBuildUp.partsTypeGrid ]
    }, {
        region : 'center', layout: 'fit', bodyBorder: false, items: [ PartsBuildUp.tabs ]
    } ]
});
//页面自适应布局
//var viewport = new Ext.Viewport({ layout:'fit', items:PartsBuildUp.partsBuildUpPanel });
var viewport = new Ext.Viewport({ layout:'fit', items:PartsBuildUp.partsBuildUpGrid });
});