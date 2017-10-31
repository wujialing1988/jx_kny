Ext.onReady(function(){

	Ext.ns('Taskins');
	/** ************ 定义全局变量开始 ************ */	
	Taskins.execFlag = EXEC_FLAG_WZX + "," + EXEC_FLAG_ZX ;
	Taskins.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	Taskins.fieldWidth = 80;
		var data2 = [];  //排
		var data3 = [];  //列
		var data4 = [];  //层

		for (var i= 1; i < 9; i++) {
			var temp = [i];
			 data2.push(temp);
		}
		for (var i = 1; i < 25; i++) {		
			if(i < 10)
				var str = '0'+ i;
			else str = i;
			var temp = [str];
			data3.push(temp);		
		}
		for (var i = 1; i < 9; i++) {		
			var str = '0'+ i;
			var temp = [str];
			data4.push(temp);		
		}
		
	/** ************ 定义全局变量结束 ************ */
		
	/** ************ 定义函数开始 ************ */	
	Taskins.deletefn = function(ids){
		Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
			if(btn == 'yes') {
			    Ext.Ajax.request({
					url : ctx + '/taskins!delete.action',
					params :{ ids:ids },
					// 请求成功后的回调函数
					success : function(response, options) {
						if (self.loadMask)
							self.loadMask.hide();
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.errMsg == null) { // 操作成功
							alertSuccess("删除成功");
							Taskins.grid.store.load();
						} else { // 操作失败
							alertFail(result.errMsg);
						}
					},
					// 请求失败后的回调函数
					failure : function(response, options) {
						if (self.loadMask) self.loadMask.hide();
						Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		});
	}
	
		// 复选框多选查询函数处理
	Taskins.checkQueryFn = function() {
		Taskins.execFlag  = "-1";
		if(Ext.getCmp("execFlag_flag_wzx").checked){
			Taskins.execFlag += "," + EXEC_FLAG_WZX;
		} 
		if(Ext.getCmp("execFlag_flag_zx").checked){
			Taskins.execFlag  += "," + EXEC_FLAG_ZX;
		} 
		if(Ext.getCmp("execFlag_flag_ztzx").checked){
			Taskins.execFlag  += "," + EXEC_FLAG_ZTZX;
		} 
		if(Ext.getCmp("execFlag_flag_yzx").checked){
			Taskins.execFlag  += "," + EXEC_FLAG_YZX;
		} 
		Taskins.grid.store.load();
	}
	
	/** ************ 定义函数结束 ************ */	
	
	/** ************ 定义表单开始 ************ */	
	Taskins.baseForm = new Ext.form.FormPanel({
		layout:"column",
		padding:"10px", defaults:{labelWidth:30},
		items:[
			{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{	
					fieldLabel:"排", 
					name:"deckId", 
					xtype: 'combo',	
			        store:new Ext.data.SimpleStore({
					    fields: ['K'],
						data : data2
					}),
					valueField:'K',
					displayField:'K',
					triggerAction:'all',
					value: 1,
					mode:'local',
					width: Taskins.fieldWidth
				}]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					fieldLabel:"列", 
					name:"column", 
					xtype: 'combo',	
			        store:new Ext.data.SimpleStore({
					    fields: ['K'],
						data : data3
					}),
					valueField:'K',
					displayField:'K',
					triggerAction:'all',
					value: 1,
					mode:'local',
					width: Taskins.fieldWidth
				}]
			},{
				xtype:"panel",
				layout:"form",
				columnWidth:0.25,
				items:[{
					fieldLabel:"层", 
					name:"level", 
					xtype: 'combo',	
			        store:new Ext.data.SimpleStore({
					    fields: ['K'],
						data : data4
					}),
					valueField:'K',
					displayField:'K',
					triggerAction:'all',
					value: 1,
					mode:'local',
					width: Taskins.fieldWidth
				}]
			}],
			buttonAlign: 'center',
			buttons: [{		
				text: '生成指令', iconCls: 'saveIcon', handler: function() {
					var values = Taskins.baseForm.getForm().getValues();  
					Ext.Msg.confirm("提示  ", "是否生成分拣指令？  ", function(btn){
				        if(btn == 'yes') {
							Ext.Ajax.request({
					            url: ctx + '/taskins!insert.action',
					            jsonData: Ext.util.JSON.encode(values),
					            success: function(response, options){
					                if(self.loadMask)   self.loadMask.hide();
					                var result = Ext.util.JSON.decode(response.responseText);
					                if (result.errMsg == null) {	             // 操作成功
										alertSuccess("生成指令成功");
					                    Taskins.grid.store.load();
					                } else {
					                    alertFail(result.errMsg);
					                }
					            }
					        });
				        }
				   });		     
				}	
			}]
	});
	/** ************ 定义表单结束  ************ */	
	
	/** ************ 定义表格开始 ************ */	
	Taskins.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/taskins!pageList.action',
		deleteURL: ctx + '/taskins!delete.action',
		storeId: 'taskNo',
		tbar:['refresh','delete','-','-',{		
		    	xtype:"checkbox", id:"execFlag_flag_wzx", boxLabel:"未执行&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: Taskins.checkQueryFn
		    }, {
		    	xtype:"checkbox", id:"execFlag_flag_zx", boxLabel:"正在执行&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: Taskins.checkQueryFn
	    	}, {
		    	xtype:"checkbox", id:"execFlag_flag_ztzx", boxLabel:"暂停执行&nbsp;&nbsp;&nbsp;&nbsp;", checked:false , handler: Taskins.checkQueryFn
			},{		
		    	xtype:"checkbox", id:"execFlag_flag_yzx", boxLabel:"已执行&nbsp;&nbsp;&nbsp;&nbsp;", checked:false , handler: Taskins.checkQueryFn
		    }],
		fields: [{
			dataIndex: 'taskNo', header: '任务号'
		}, {
			dataIndex: 'cargoNo', header: '货位号'
		}, {
			dataIndex: 'insType', header: '指令类型'
		}, {
			dataIndex: 'laneWay', header: '巷道'
		}, {
			dataIndex: 'deckId', header: '输送机号'
		}, {
			dataIndex: 'execFlag', header: '执行标志'
		}, {
			dataIndex: 'inscreateTime', header: '下发时间'
		}, {
			header: '操作',dataIndex:"execFlag",renderer: function(value, record, rowIndex, colIndex, storeId){ 
				var id = rowIndex.id;
				var html = "";
				if(value == EXEC_FLAG_WZX){
		    		html = "<span><a href='#' onclick = 'Taskins.deletefn(" + id + ")'>删除</a></span>";
		            return html;  	
				}
				return html;
        	}	
		}],
		toEditFn: Ext.emptyFn //覆盖双击编辑事件
	});
	/** ************ 定义表格结束 ************ */	
	
//	Taskins.grid.store.sort({field: 'inscreateTime', direction: 'desc'});
	Taskins.grid.store.on('beforeload', function(){
		var searchParams = {};
		searchParams.execFlag = Taskins.execFlag; 	
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
	/** ************ 布局 ************ */	
	new Ext.Viewport({
		layout: 'border',
		defaults: {layout: 'fit'},
		items: [{
			region: 'north',
			frame:true,
			height: 100,
			items: Taskins.baseForm
		}, {
			region: 'center',
			frame:true,
			items: Taskins.grid
		}]
	});
	
});