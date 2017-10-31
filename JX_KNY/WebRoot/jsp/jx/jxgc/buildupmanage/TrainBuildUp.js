/** 机车组成型号维护*/
Ext.onReady(function(){
Ext.namespace('TrainBuildUp');                       //定义命名空间
//机车组成查询集合对象
TrainBuildUp.searchTrainParams = {};
//虚拟组成查询集合对象
TrainBuildUp.searchVirtualParams = {};
//根据业务编码生成规则生成组成型号编码
TrainBuildUp.getBuildUpTypeCode = function(){
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
//车型列表
TrainBuildUp.trainTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/trainType!findPageList.action",
	singleSelect: true, 
	//remoteSort: false,
	tbar:[{
		xtype:"combo", id:"queryType_Id", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"车型名称", mode:"local",triggerAction: "all",
		store: new Ext.data.SimpleStore({
			fields: ["type"],
			data: [ ["车型名称"], ["车型简称"] ]
		})
	},{	            
        xtype:"textfield",  id:"typeName_Id", width: 70
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			var typeName = Ext.getCmp("typeName_Id").getValue();
			var querytype = Ext.getCmp("queryType_Id").getValue();
			var searchParam = {};
			if(querytype == '车型名称'){
				searchParam.typeName = typeName;
				TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				TrainBuildUp.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}else{
				searchParam.shortName = typeName;
				TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
				TrainBuildUp.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}
		}
	},{
		text : "重置",
		iconCls : "resetIcon",
		handler : function(){
			TrainBuildUp.trainTypeGrid.store.baseParams.entityJson = Ext.util.JSON.encode({});
			TrainBuildUp.trainTypeGrid.getStore().load({
				params:{
					typeName : "",
					shortName : ""
				}																
			});
			//清空搜索输入框
			Ext.getCmp("typeName_Id").setValue("");
			Ext.getCmp("queryType_Id").setValue("车型名称");
			//清空机车组成查询集合
			TrainBuildUp.searchTrainParams = {};
		}
	},{
		text : "生成标准组成",
		iconCls : "addIcon",
		handler : function(){
			var cfg = Ext.apply($yd.cfgAjaxRequest(), {
		        params: {'type': type_train},
		        url: ctx + '/buildUpType!addAllDefault.action', 
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.errMsg == null) {
		                //操作成功            
		                alertSuccess();
		                TrainBuildUp.trainTypeGrid.store.reload(); 
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
			header:'车型名称', dataIndex:'typeName', editor:{ }, sortable: true
		},{
			header:'车型简称', dataIndex:'shortName', editor:{ }
        },{
            header:'车型代码', dataIndex:'typeID', hidden:true          
		}]
});
//单击车型记录过滤机车和虚拟组成列表
TrainBuildUp.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
			var record = grid.getStore().getAt(rowIndex);
			TrainBuildUp.searchTrainParams.trainTypeIDX = record.get("typeID");
			TrainBuildUp.searchTrainParams.type = type_train;
			TrainBuildUp.trainBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchTrainParams);
			TrainBuildUp.trainBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(TrainBuildUp.searchTrainParams)
				}
			});
			TrainBuildUp.searchVirtualParams.trainTypeIDX = record.get("typeID");
			TrainBuildUp.searchVirtualParams.type = type_virtual;
			TrainBuildUp.virtualBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams);
			TrainBuildUp.virtualBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams)
				}
			})
		});
//移除侦听器
TrainBuildUp.trainTypeGrid.un('rowdblclick', TrainBuildUp.trainTypeGrid.toEditFn, TrainBuildUp.trainTypeGrid);
/**
 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * @param status 
 * @return 提示信息字符串
 */
TrainBuildUp.alertOperate = function(infoArray,msg,status){
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
TrainBuildUp.setDefault = function(_grid){
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
TrainBuildUp.updateStatus = function(validStatus, _grid, _operate){
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
    Ext.Msg.confirm('提示',TrainBuildUp.alertOperate(flag,'是否执行' + action + '操作，该操作将不能恢复！',_operate), function(btn){   
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
TrainBuildUp.addDefault = function(type, id, grid){
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
TrainBuildUp.deleteFn = function(){
    if(this.saveWin)    this.saveWin.hide();
    if(this.searchWin)  this.searchWin.hide();        
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(this)) return;
    //执行删除前触发函数，根据返回结果觉得是否执行删除动作
    if(!this.beforeDeleteFn()) return;
    TrainBuildUp.updateStatus(status_new, this, 'del');      
}
//机车组成列表
TrainBuildUp.trainBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2, 
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: ['search', {
		text: "默认", iconCls:"addIcon", tooltip:'添加默认组成', tooltipType:'title', handler : function(){        
            var records = TrainBuildUp.trainTypeGrid.selModel.getSelections();
            if(records == null || records.length < 1){
                MyExt.Msg.alert('请选择一条车型记录！');
                return;
            }
            var id = records[ 0 ].get('typeID');
            TrainBuildUp.addDefault(type_train, id, TrainBuildUp.trainBuildUpGrid);
		}
    },{
        text:"标准", iconCls:"configIcon", tooltip:'设置标准组成', tooltipType:'title', handler:function(){
            TrainBuildUp.setDefault(TrainBuildUp.trainBuildUpGrid);
        }        
	},'add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            TrainBuildUp.updateStatus(status_new, TrainBuildUp.trainBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            TrainBuildUp.updateStatus(status_use, TrainBuildUp.trainBuildUpGrid, 'invalid');
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_train_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkTrainQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_train_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				TrainBuildUp.checkTrainQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_train_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkTrainQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_train_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkTrainQuery(status_nullify);
    		}
    }}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', 
		editor:{ id: 'buildUpTypeCode_Id', allowBlank: false, disabled:true }, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号名称', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
	},{
		header:'组成类型', dataIndex:'type', hidden:true,
		renderer:function(v){
			switch(v){
				case type_train:
					return "机车组成";
				case type_parts:
					return "配件组成";
				default :
					return "";
			}
		},
		editor:{ xtype: 'hidden', id:'_typeT'},
 		searcher:{ disabled: true}
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,
		editor:{ id:"trainTypeIDX_Id", xtype:'hidden'},
		searcher:{ disabled: true}       
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{ id:"trainTypeShortName_Id", readOnly:true},
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
		editor:{xtype:'hidden',id:'_statusT'}
	},{
		header:'是否标准组成', dataIndex:'isDefault', 
		renderer:function(v){
			switch(v){
				case isDefault_yes:
					return "是";
					break;
				case isDefault_no:
					return "否";
					break;
				default :
					return "";
					break;
			}
		},
		editor:{
			xtype:'hidden',
			id: '_isDefault'
		},
		searcher:{ disabled: true}
	}],
    deleteButtonFn: TrainBuildUp.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        var records = TrainBuildUp.trainTypeGrid.selModel.getSelections();
        if(records == null || records.length < 1){
            MyExt.Msg.alert('请选择一条车型记录！');
            return false;
        }
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
        if(status == status_new)    return true;
        if(status == status_use || status == status_nullify){
            MyExt.Msg.alert('只能修改【新增】的记录！');
        }
        return false; 
    },    
    //显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
    afterShowSaveWin: function(){
        var records = TrainBuildUp.trainTypeGrid.selModel.getSelections();
        Ext.getCmp('trainTypeShortName_Id').setValue(records[ 0 ].get('shortName'));
        Ext.getCmp('trainTypeIDX_Id').setValue(records[ 0 ].get('typeID'));
        Ext.getCmp('_typeT').setValue(type_train);//新增时默认设置"类型"为"机车组成"
        Ext.getCmp('_statusT').setValue(status_new);//新增时默认设置"状态"为"新增"
        Ext.getCmp('_isDefault').setValue(isDefault_no);//新增时默认设置为"非标准组成"
        TrainBuildUp.getBuildUpTypeCode();
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	TrainBuildUp.searchTrainParams.buildUpTypeCode = "";
		TrainBuildUp.searchTrainParams.buildUpTypeName = "";
		TrainBuildUp.searchTrainParams.trainTypeShortName = "";

    	for(prop in searchParam){  
//	    	if(searchParam[prop] != ""){
//	    		TrainBuildUp.searchTrainParams[prop] = searchParam[prop];
//	    	}
	    	TrainBuildUp.searchTrainParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_train;
    	searchParam.trainTypeIDX = TrainBuildUp.searchTrainParams.trainTypeIDX;
    	searchParam.status = TrainBuildUp.trainStatus;
    	TrainBuildUp.searchTrainParams.status = TrainBuildUp.trainStatus;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchTrainParams);
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
    }
});
TrainBuildUp.trainBuildUpGrid.createSaveWin();
TrainBuildUp.trainBuildUpGrid.saveWin.modal = true;

//状态全局变量
TrainBuildUp.trainStatus = "";
//状态单击事件方法
TrainBuildUp.checkTrainQuery =  function (status){
	TrainBuildUp.trainStatus = status;	
	TrainBuildUp.trainBuildUpGrid.getStore().load();
}
//机车组成列表查询处理
TrainBuildUp.trainBuildUpGrid.getStore().on("beforeload",function(){
	TrainBuildUp.searchTrainParams.type = type_train;
	TrainBuildUp.searchTrainParams.status = TrainBuildUp.trainStatus;
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchTrainParams);
});
//根据业务编码生成规则生成组成型号编码
TrainBuildUp.getVirtualBuildUpTypeCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
        url: url,
        params: {ruleFunction: "JXGC_BUILDUP_TYPE_BUILDUP_TYPE_CODE"},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                Ext.getCmp("buildUpTypeCode_VId").setValue(result.rule);
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
}
//虚拟组成列表
TrainBuildUp.virtualBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
    storeAutoLoad: false,
    //remoteSort: false,
    tbar: ['add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            TrainBuildUp.updateStatus(status_new, TrainBuildUp.virtualBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            TrainBuildUp.updateStatus(status_use, TrainBuildUp.virtualBuildUpGrid, 'invalid');
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_virtual_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkVirtualQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_virtual_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				TrainBuildUp.checkVirtualQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_virtual_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkVirtualQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_virtual_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				TrainBuildUp.checkVirtualQuery(status_nullify);
    		}
    }}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', 
		editor:{ id: 'buildUpTypeCode_VId', allowBlank: false, disabled:true }, sortable: true
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
	},{
		header:'组成类型', dataIndex:'type', hidden:true,
		editor:{ xtype: 'hidden', id:'_typeV'},
 		searcher:{ disabled: true}
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,
		editor:{ id:"trainTypeIDX_VId", xtype:'hidden'},
		searcher:{ disabled: true}       
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{ id:"trainTypeShortName_VId", readOnly:true},
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
    deleteButtonFn: TrainBuildUp.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        var records = TrainBuildUp.trainTypeGrid.selModel.getSelections();
        if(records == null || records.length < 1){
            MyExt.Msg.alert('请选择一条车型记录！');
            return false;
        }
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
        if(status == status_new)    return true;
        if(status == status_use || status == status_nullify){
            MyExt.Msg.alert('只能修改【新增】的记录！');
        }
        return false; 
    },    
    //显示新增窗口后触发该函数，该函数依赖saveFn，若默认saveFn被覆盖则失效
    afterShowSaveWin: function(){
        var records = TrainBuildUp.trainTypeGrid.selModel.getSelections();
        Ext.getCmp('trainTypeShortName_VId').setValue(records[ 0 ].get('shortName'));
        Ext.getCmp('trainTypeIDX_VId').setValue(records[ 0 ].get('typeID'));
        Ext.getCmp('_typeV').setValue(type_virtual);//新增时默认设置"类型"为"虚拟组成"
        Ext.getCmp('_statusV').setValue(status_new);//新增时默认设置"状态"为"新增"
        TrainBuildUp.getVirtualBuildUpTypeCode();
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	TrainBuildUp.searchVirtualParams.buildUpTypeCode = "";
		TrainBuildUp.searchVirtualParams.buildUpTypeName = "";
		TrainBuildUp.searchVirtualParams.trainTypeShortName = "";

    	for(prop in searchParam){  
//	    	if(searchParam[prop] != ""){
//	    		TrainBuildUp.searchTrainParams[prop] = searchParam[prop];
//	    	}
	    	TrainBuildUp.searchVirtualParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_virtual;
    	searchParam.trainTypeIDX = TrainBuildUp.searchVirtualParams.trainTypeIDX;
    	searchParam.status = TrainBuildUp.virtualStatus;
    	TrainBuildUp.searchVirtualParams.status = TrainBuildUp.virtualStatus;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams);
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
    }
});
TrainBuildUp.virtualBuildUpGrid.createSaveWin();
TrainBuildUp.virtualBuildUpGrid.saveWin.modal = true;

//状态全局变量
TrainBuildUp.virtualStatus = "";
//状态单击事件方法
TrainBuildUp.checkVirtualQuery =  function (status){
	TrainBuildUp.virtualStatus = status;	
	TrainBuildUp.virtualBuildUpGrid.getStore().load();
}
//机车组成列表查询处理
TrainBuildUp.virtualBuildUpGrid.getStore().on("beforeload",function(){
	TrainBuildUp.searchVirtualParams.type = type_virtual;
	TrainBuildUp.searchVirtualParams.status = TrainBuildUp.virtualStatus;
	this.baseParams.entityJson = Ext.util.JSON.encode(TrainBuildUp.searchVirtualParams);
});
//tab选项卡布局
TrainBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "trainBuildUpTab", title: '机车组成型号', layout:'fit', items: [TrainBuildUp.trainBuildUpGrid]
    },{ 
      id: "virtualBuildUpTab", title: '关联虚拟组成型号', layout: 'fit', items: [TrainBuildUp.virtualBuildUpGrid]
    }]
});
//机车组成页面
TrainBuildUp.trainBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '车型', width: 350, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
        collapsible : true,
        autoScroll: true, layout: 'fit', items : [ TrainBuildUp.trainTypeGrid ]
    }, {
        region : 'center', layout: 'fit', bodyBorder: false, items: [ TrainBuildUp.tabs ]
    } ]
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainBuildUp.trainBuildUpPanel });
});