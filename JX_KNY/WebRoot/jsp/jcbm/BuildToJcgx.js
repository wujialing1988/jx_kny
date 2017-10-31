/** 机车组成型号维护*/
Ext.onReady(function(){
Ext.namespace('TrainBuildUp');                       //定义命名空间
//机车组成查询集合对象
TrainBuildUp.searchTrainParams = {};
var loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
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
			
		});
//移除侦听器
TrainBuildUp.trainTypeGrid.un('rowdblclick', TrainBuildUp.trainTypeGrid.toEditFn, TrainBuildUp.trainTypeGrid);

//设为标准组成
TrainBuildUp.setDefault = function(_grid){
	//未选择记录，直接返回
    if(!$yd.isSelectedRecord(_grid)) return;   
    var ids = $yd.getSelectedIdx(_grid);
    if(ids.length > 1){
    	MyExt.Msg.alert("请只选择一条记录！");
    	return;
    }
    if(loadMask)   loadMask.show();
    var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        params: {'id': ids[0]},
        timeout : 600000000,
        url: ctx + '/jcgxBuildCopy!saveByBuildType.action', 
        success: function(response, options){
        	if(loadMask)   loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();                
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Ajax.request(cfg);
     
}

TrainBuildUp.setFault = function() {	
	var cfg = Ext.apply($yd.cfgAjaxRequest(), {
        url: ctx + '/jcgxBuildCopy!savePlaceFault.action',
        timeout : 600000000,
        success: function(response, options){
        	if(loadMask)   loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                //操作成功            
                alertSuccess();
            } else {
                //操作失败
                alertFail(result.errMsg);
            }
        }               
    }); 
    Ext.Msg.confirm("提示  ", "请确认已复制完所有车型的构型？  ", function(btn){
        if(btn != 'yes')    return;
        if(loadMask)   loadMask.show();
        Ext.Ajax.request(cfg);
    });
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
        text:"复制为机车构型", iconCls:"configIcon", tooltip:'复制为机车构型', tooltipType:'title', handler:function(){
            TrainBuildUp.setDefault(TrainBuildUp.trainBuildUpGrid);
        }        
	},{
        text:"复制所有故障现象", iconCls:"configIcon", tooltip:'复制所有故障现象', tooltipType:'title', handler:function(){
            TrainBuildUp.setFault();
        }	
	},'refresh',"-","状态： ",{
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
	toEditFn: function(grid, rowIndex, e){}    
});

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
//tab选项卡布局
TrainBuildUp.tabs = new Ext.TabPanel({
    activeTab: 0, frame:true, singleSelect: true,
    items:[{  
       id: "trainBuildUpTab", title: '机车组成型号', layout:'fit', items: [TrainBuildUp.trainBuildUpGrid]
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