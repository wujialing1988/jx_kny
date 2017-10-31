/**
 * 机车检修作业计划-检修项目选择 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RepairProjectSelect');                       //定义命名空间
RepairProjectSelect.pTrainTypeIdx = '';
RepairProjectSelect.nodeCaseIDX = '';
RepairProjectSelect.searchParam = {};
RepairProjectSelect.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairProject!pageQuery.action',                 //装载列表数据的请求URL
    saveFormColNum:1,	searchFormColNum:1,
    storeAutoLoad: false,
    tbar: [{
        xtype:"label", text:"  作业项目名称： " 
    },{
        xtype: "textfield" ,maxLength: 100,vtype:"validChar"
    },{
    	text: "查询", iconCls:"searchIcon", handler: function(){
    		var grid = RepairProjectSelect.grid;
    		var repairProjectName = grid.getTopToolbar().get(1).getValue();
    		RepairProjectSelect.searchParam.repairProjectName = repairProjectName;
    		grid.store.load();
    	}
    },'-',{
    	text: "确定", iconCls:"addIcon", handler: function(){
    			var grid = RepairProjectSelect.grid;
    			if(!$yd.isSelectedRecord(grid)) return;
        		var _this = this;        		
        		Ext.Msg.confirm("提示", "确认选择作业项目！", function(btn){
        			if(btn == 'yes'){        				
        				var projectData = grid.selModel.getSelections();
                		var dataAry = new Array();
        	            for (var i = 0; i < projectData.length; i++){
        	            	var data = {};
        	                data.workPlanIDX = workPlanIDX;
        	                data.repairProjectIDX = projectData[ i ].get("idx");
        	                data.nodeCaseIDX =  RepairProjectSelect.nodeCaseIDX;
        	                dataAry.push(data);
        	            }        	            
        	            _this.disable();
        	            grid.loadMask.show();
        	            Ext.Ajax.request({
        	                url:  ctx + "/workPlanRepairActivity!saveOrUpdateBatch.action",
        	                jsonData: dataAry,
        	                success: function(response, options){
        	                  	grid.loadMask.hide();
        	                    var result = Ext.util.JSON.decode(response.responseText);
        	                    
        	                    if (result.errMsg == null && result.success == true) {
        	                        alertSuccess();
        	                        RepairProjectSelect.win.hide();
        	                        WorkCardEdit.grid.store.load();
        	                        WorkCardEditWin.grid.store.load();
        	                        
        	                    } else {
        	                        alertFail(result.errMsg);
        	                    }	                    
        	                },
        	                failure: function(response, options){
        	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        	                }
                       });
        	            _this.enable();
        			}
        		});
	        }
    },{
    	text: "刷新", iconCls:"refreshIcon", handler: function(){
    		RepairProjectSelect.grid.store.load();
    	}
    },{
    	text: "关闭", iconCls:"closeIcon", handler: function(){
    		RepairProjectSelect.win.hide();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业项目编码', dataIndex:'repairProjectCode', editor:{  maxLength:50 }
	},{
		header:'作业项目名称', dataIndex:'repairProjectName', editor:{  maxLength:50 }
	},{
		header:'备注', dataIndex:'remark'
	}],
	toEditFn: function(grid, rowIndex, e){},
    searchFn: function(searchParam){
    	RepairProjectSelect.searchParam = searchParam;
    	this.store.load();
    }
});
RepairProjectSelect.grid.store.on("beforeload", function(){	
	var searchParam = RepairProjectSelect.searchParam;
	searchParam.pTrainTypeIdx = RepairProjectSelect.pTrainTypeIdx;
	var whereList = [] ;
	for (prop in searchParam) {			
       if(prop == 'pTrainTypeIdx'){
			whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
		}else{
        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
	}
	//TODO 是否需要筛选除本节点之外的检修项目
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

RepairProjectSelect.win = new Ext.Window({
    title:"作业项目选择", width: 500, height: 400, layout: 'fit',
    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
    items:RepairProjectSelect.grid
});
});