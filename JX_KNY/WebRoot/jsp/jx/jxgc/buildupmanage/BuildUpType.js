/**
 * 组成型号 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 已作废
 */
Ext.onReady(function(){
Ext.namespace('BuildUpType');                       //定义命名空间
//机车组成查询集合对象
BuildUpType.searchTrainParams = {};
//配件组成查询集合对象
BuildUpType.searchPartsParams = {};
//车型列表
BuildUpType.trainTypeGrid = new Ext.yunda.Grid({
	loadURL: ctx + "/trainType!findPageList.action",
	singleSelect: true, 
	tbar:[{
		xtype:"combo", id:"queryType_Id", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"车型名称", mode:"local",triggerAction: "all",
		store: new Ext.data.SimpleStore({
			fields: ["type"],
			data: [ ["车型名称"], ["车型简称"] ]
		})
	},{	            
        xtype:"textfield",  id:"typeName_Id", width: 100
	},{
		text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
		handler : function(){
			var typeName = Ext.getCmp("typeName_Id").getValue();
			var querytype = Ext.getCmp("queryType_Id").getValue();
			var searchParam = {};
			if(querytype == '车型名称'){
				searchParam.typeName = typeName;
				BuildUpType.trainTypeGrid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});
			}else{
				searchParam.shortName = typeName;
				BuildUpType.trainTypeGrid.getStore().load({
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
			BuildUpType.trainTypeGrid.getStore().load({
				params:{
					typeName : "",
					shortName : ""
				}																
			});
			//清空搜索输入框
			Ext.getCmp("typeName_Id").setValue("");
			Ext.getCmp("queryType_Id").setValue("车型名称");
			//清空机车组成查询集合
			BuildUpType.searchTrainParams = {};
		}
	}],
		fields: [{
			header:'车型名称', dataIndex:'typeName', editor:{ }
		},{
			header:'车型简称', dataIndex:'shortName', editor:{ }
        },{
            header:'车型代码', dataIndex:'typeID', hidden:true          
		}]
});
//单击车型记录过滤机车组成列表
BuildUpType.trainTypeGrid.on("rowclick", function(grid, rowIndex, e){
			var record = grid.getStore().getAt(rowIndex);
			BuildUpType.searchTrainParams.trainTypeIDX = record.get("typeID");
			BuildUpType.searchTrainParams.type = type_train;
			BuildUpType.trainBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchTrainParams);
			BuildUpType.trainBuildUpGrid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(BuildUpType.searchTrainParams)
				}
			})
		});
//移除侦听器
BuildUpType.trainTypeGrid.un('rowdblclick', BuildUpType.trainTypeGrid.toEditFn, BuildUpType.trainTypeGrid);
/**
 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * @param status 
 * @return 提示信息字符串
 */
BuildUpType.alertOperate = function(infoArray,msg,status){
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
//设为缺省
BuildUpType.setDefault = function(_grid){
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
BuildUpType.updateStatus = function(validStatus, _grid, _operate){
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
        url = BuildUpType.trainBuildUpGrid.deleteURL;
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
    Ext.Msg.confirm('提示',BuildUpType.alertOperate(flag,'是否执行' + action + '操作，该操作将不能恢复！',_operate), function(btn){   
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
BuildUpType.addDefault = function(type, id, grid){
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
BuildUpType.deleteFn = function(){
    if(this.saveWin)    this.saveWin.hide();
    if(this.searchWin)  this.searchWin.hide();        
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(this)) return;
    //执行删除前触发函数，根据返回结果觉得是否执行删除动作
    if(!this.beforeDeleteFn()) return;
    BuildUpType.updateStatus(status_new, this, 'del');      
}
//机车组成列表
BuildUpType.trainBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2, searchFormColNum:1,
    tbar: ['search', {
		text: "添加默认组成", iconCls:"addIcon", handler : function(){        
            var records = BuildUpType.trainTypeGrid.selModel.getSelections();
            if(records == null || records.length < 1){
                MyExt.Msg.alert('请选择一条车型记录！');
                return;
            }
            var id = records[ 0 ].get('typeID');
            BuildUpType.addDefault(type_train, id, BuildUpType.trainBuildUpGrid);
		}
	},'add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            BuildUpType.updateStatus(status_new, BuildUpType.trainBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            BuildUpType.updateStatus(status_use, BuildUpType.trainBuildUpGrid, 'invalid');
        }
    },{
        text:"设为缺省", iconCls:"configIcon", handler:function(){
            BuildUpType.setDefault(BuildUpType.trainBuildUpGrid);
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_train_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkTrainQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_train_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				BuildUpType.checkTrainQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_train_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkTrainQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_train_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkTrainQuery(status_nullify);
    		}
    }}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号描述', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
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
		header:'是否缺省为标准组成', dataIndex:'isDefault', 
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
			xtype:'hidden'
		},
		searcher:{ disabled: true}
	}],
    deleteButtonFn: BuildUpType.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        var records = BuildUpType.trainTypeGrid.selModel.getSelections();
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
        var records = BuildUpType.trainTypeGrid.selModel.getSelections();
        Ext.getCmp('trainTypeShortName_Id').setValue(records[ 0 ].get('shortName'));
        Ext.getCmp('trainTypeIDX_Id').setValue(records[ 0 ].get('typeID'));
        Ext.getCmp('_typeT').setValue(type_train);
        Ext.getCmp('_statusT').setValue(status_new);
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	BuildUpType.searchTrainParams.buildUpTypeCode = "";
		BuildUpType.searchTrainParams.buildUpTypeName = "";
		BuildUpType.searchTrainParams.trainTypeShortName = "";

    	for(prop in searchParam){  
//	    	if(searchParam[prop] != ""){
//	    		BuildUpType.searchTrainParams[prop] = searchParam[prop];
//	    	}
	    	BuildUpType.searchTrainParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_train;
    	searchParam.trainTypeIDX = BuildUpType.searchTrainParams.trainTypeIDX;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchTrainParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    }
});
BuildUpType.trainBuildUpGrid.createSaveWin();
BuildUpType.trainBuildUpGrid.saveWin.modal = true;

//状态全局变量
BuildUpType.trainStatus = "";
//状态单击事件方法
BuildUpType.checkTrainQuery =  function (status){
	BuildUpType.trainStatus = status;	
	BuildUpType.trainBuildUpGrid.getStore().load();
}
//机车组成列表查询处理
BuildUpType.trainBuildUpGrid.getStore().on("beforeload",function(){
	BuildUpType.searchTrainParams.type = type_train;
	BuildUpType.searchTrainParams.status = BuildUpType.trainStatus;
	this.baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchTrainParams);
});
//机车组成页面
BuildUpType.trainBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '车型', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
        autoScroll: true, layout: 'fit', items : [ BuildUpType.trainTypeGrid ]
    }, {
        region : 'center', layout: 'fit', bodyBorder: false, items: [ BuildUpType.trainBuildUpGrid ]
    } ]
});

//配件规格型号列表
BuildUpType.partsTypeGrid = new Ext.yunda.Grid({
    loadURL: ctx + "/partsType!findpageList.action?statue="+partsStatus_use,
    singleSelect: true, 
    tbar:[{
        xtype:"combo", id:"queryType_Id_parts", hiddenName:"queryType", displayField:"type",
        width: 80, valueField:"type", value:"配件名称", mode:"local",triggerAction: "all",
        store: new Ext.data.SimpleStore({
            fields: ["type"],
            data: [ ["配件名称"], ["规格型号"] ]
        })
    },{             
        xtype:"textfield",  id:"typeName_Id_parts", width: 100
    },{
        text : "搜索", iconCls : "searchIcon", title: "按输入框条件查询", width: 40,
        handler : function(){
            var typeName = Ext.getCmp("typeName_Id_parts").getValue();
            var querytype = Ext.getCmp("queryType_Id_parts").getValue();
            var searchParam = {status:partsStatus_use};
            if(querytype == '配件名称'){
                searchParam.partsName = typeName;
                BuildUpType.partsTypeGrid.getStore().load({
                    params:{
                        entityJson:Ext.util.JSON.encode(searchParam)
                    }                                                               
                });
            }else{
                searchParam.specificationModel = typeName;
                BuildUpType.partsTypeGrid.getStore().load({
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
            BuildUpType.partsTypeGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(searchParam)
                }                                                               
            });
            //清空搜索输入框
            Ext.getCmp("typeName_Id_parts").setValue("");
            Ext.getCmp("queryType_Id_parts").setValue("配件名称");
            //清空配件组成查询集合
            BuildUpType.searchPartsParams = {};
        }
    }],
        fields: [{
            header:'配件名称', dataIndex:'partsName', editor:{ }
        },{
            header:'规格型号', dataIndex:'specificationModel', editor:{ }
        },{
            dataIndex:'idx', hidden:true
        }]  
        
});
//单击配件记录过滤配件组成列表
BuildUpType.partsTypeGrid.on("rowclick", function(grid, rowIndex, e){
			 var record = grid.getStore().getAt(rowIndex);
            BuildUpType.searchPartsParams.partsTypeIDX = record.get("idx");
            BuildUpType.searchPartsParams.type = type_parts;
            BuildUpType.partsBuildUpGrid.getStore().baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchPartsParams);
            BuildUpType.partsBuildUpGrid.getStore().load({
                params:{
                    entityJson:Ext.util.JSON.encode(BuildUpType.searchPartsParams)
                }
            })
		});
//移除侦听器
BuildUpType.partsTypeGrid.un('rowdblclick', BuildUpType.partsTypeGrid.toEditFn, BuildUpType.partsTypeGrid);
//配件组成列表
BuildUpType.partsBuildUpGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/buildUpType!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/buildUpType!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/buildUpType!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2, searchFormColNum: 2,
    tbar: ['search', {
        text: "添加默认组成", iconCls:"addIcon", handler : function(){
            var records = BuildUpType.partsTypeGrid.selModel.getSelections();
            if(records == null || records.length < 1){
                MyExt.Msg.alert('请选择一条配件规格型号记录！');
                return;
            }
            var id = records[ 0 ].get('idx');
            BuildUpType.addDefault(type_parts, id, BuildUpType.partsBuildUpGrid);
        }
    },'add',{
        text:"启用", iconCls:"acceptIcon", handler:function(){
            BuildUpType.updateStatus(status_new, BuildUpType.partsBuildUpGrid, 'start');
        }
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            BuildUpType.updateStatus(status_use, BuildUpType.partsBuildUpGrid, 'invalid');
        }
    },{
        text:"设为缺省", iconCls:"configIcon", handler:function(){
            BuildUpType.setDefault(BuildUpType.partsBuildUpGrid);
        }
    },'delete','refresh',"-","状态： ",{
        xtype:"radio", name:"status",id:"all_id", boxLabel:"全部&nbsp;&nbsp;&nbsp;&nbsp;", checked:true, 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkQuery("");
    		}
    }},{   
        xtype:"radio", name:"status",id:"new_id", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked){    			
				BuildUpType.checkQuery(status_new);
    		}
    }},{   
        xtype:"radio", name:"status",id:"use_id", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkQuery(status_use);
    		}
    }},{   
        xtype:"radio", name:"status",id:"nullify_id", boxLabel:"作废&nbsp;&nbsp;&nbsp;&nbsp;", 
        handler: function(radio, checked){
    		if(checked ){    			
				BuildUpType.checkQuery(status_nullify);
    		}
    }}],  
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName', editor:{  maxLength:50, allowBlank: false }
	},{
		header:'组成型号描述', dataIndex:'buildUpTypeDesc', editor:{  maxLength:1000 }, searcher:{ disabled: true}
    },{
        header:'组成类型', dataIndex:'type', hidden:true, editor:{xtype:'hidden', id:'_type' }, searcher:{ disabled: true}        
	},{
		header:'配件规格型号', dataIndex:'partsTypeIDX', hidden:true,
        editor:{xtype: 'hidden', id:'partsType_IDX'}
	},{
		header:'配件名称', dataIndex:'partsName', 
        editor:{ id:"parts_Name", readOnly:true, allowBlank: false},
		searcher:{ disabled: false, xtype:"textfield"}
	},{
		header:'配件规格型号', dataIndex:'specificationModel', 
        editor:{ id:"specification_Model", readOnly:true, allowBlank: false },
		searcher:{ disabled: false, xtype:"textfield"}
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
		editor:{  xtype: 'hidden', id:'_status' },
		searcher:{
			disabled:true,
        	xtype: 'radiogroup',
            fieldLabel: '状态',
            width:180,
            name:"status",
            items: [
                {id:"sea_all_id", boxLabel: '全部', name: 'status1',
	            	handler:function(radio, checked){
		                if(checked){
	            			BuildUpType.checkQuerySea("");
		                }
                }},
                {id:"sea_new_id", boxLabel: '新增', name: 'status1', 
                	handler:function(radio, checked){
		                if(checked){
	            			BuildUpType.checkQuerySea(status_new);
		                }
                }},
                {id:"sea_use_id",  boxLabel: '启用', name: 'status1', 
                	handler:function(radio, checked){
		                if(checked){
	            			BuildUpType.checkQuerySea(status_use);
		                }
		        }},
		        {id:"sea_nullify_id",  boxLabel: '作废', name: 'status1', 
                	handler:function(radio, checked){
		                if(checked){
	            			BuildUpType.checkQuerySea(status_nullify);
		                }
		        }}
            ]
        }
	},{
		header:'是否缺省为标准组成', dataIndex:'isDefault', 
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
			xtype:'hidden'			
		},
		searcher:{ disabled: true}
	}],
    deleteButtonFn: BuildUpType.deleteFn,                   //点击删除按钮触发的函数
    beforeShowSaveWin: function(){                          //进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
        var records = BuildUpType.partsTypeGrid.selModel.getSelections();
        if(records == null || records.length < 1){
            MyExt.Msg.alert('请选择一条配件规格型号记录！');
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
        var records = BuildUpType.partsTypeGrid.selModel.getSelections();
        Ext.getCmp('parts_Name').setValue(records[ 0 ].get('partsName'));
        Ext.getCmp('specification_Model').setValue(records[ 0 ].get('specificationModel'));
        Ext.getCmp('partsType_IDX').setValue(records[ 0 ].get('idx'));
        Ext.getCmp('_type').setValue(type_parts);
        Ext.getCmp('_status').setValue(status_new);
        return true;
    },
    //查询方法
    searchFn: function(searchParam){
    	BuildUpType.searchPartsParams.buildUpTypeCode = "";
    	BuildUpType.searchPartsParams.buildUpTypeName = "";
    	BuildUpType.searchPartsParams.partsName = "";
    	BuildUpType.searchPartsParams.specificationModel = "";
    	for(prop in searchParam){  
//	    	if(searchParam[prop] != ""){
//	    		BuildUpType.searchPartsParams[prop] = searchParam[prop];
//	    	}
	    	BuildUpType.searchPartsParams[prop] = searchParam[prop];
		}
    	searchParam.type = type_parts;   
    	searchParam.partsTypeIDX = BuildUpType.searchPartsParams.partsTypeIDX;
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchPartsParams);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    }
});
BuildUpType.partsBuildUpGrid.createSaveWin();
BuildUpType.partsBuildUpGrid.saveWin.modal = true;

//配件组成页面
BuildUpType.partsBuildUpPanel =  new Ext.Panel( {
    layout : 'border',
    items : [ {
        title: '配件规格型号', width: 300, minSize: 160, maxSize: 400, split: true, region: 'west',
        bodyBorder: false, autoScroll: true, layout: 'fit', items: [ BuildUpType.partsTypeGrid ]
    }, {
        region: 'center', layout: 'fit', bodyBorder: false, items: [ BuildUpType.partsBuildUpGrid ]
    }]
});
//tab选项卡布局
BuildUpType.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "trainBuildUpTab", title: '机车组成型号', layout:'fit', items: [BuildUpType.trainBuildUpPanel]
    },{ 
      id: "partsBuildUpTab", title: '配件组成型号', layout: 'fit', items: [BuildUpType.partsBuildUpPanel]
    }]
});
//状态全局变量
BuildUpType.status = "";
//状态单击事件方法
BuildUpType.checkQuery =  function (status){
	BuildUpType.status = status;	
	BuildUpType.partsBuildUpGrid.getStore().load();
}
//查询页面状态单击事件方法
BuildUpType.checkQuerySea =  function (status){
	BuildUpType.status = status;	
	if(status == ""){
		Ext.getCmp("all_id").setValue(true);
	}
	if(status == status_new){
		Ext.getCmp("new_id").setValue(true);
	}
	if(status == status_use){
		Ext.getCmp("use_id").setValue(true);
	}
	if(status == status_nullify){
		Ext.getCmp("nullify_id").setValue(true);
	}
	BuildUpType.partsBuildUpGrid.getStore().load();
}
//配件组成列表查询前的处理
BuildUpType.partsBuildUpGrid.getStore().on("beforeload",function(){
	BuildUpType.searchPartsParams.type = type_parts;
	BuildUpType.searchPartsParams.status = BuildUpType.status;
	this.baseParams.entityJson = Ext.util.JSON.encode(BuildUpType.searchPartsParams);
	
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:BuildUpType.tabs });
});