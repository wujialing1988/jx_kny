/**
 * 台位绑定工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('BindWorkStation');
	/** **************** 定义全局变量开始 **************** */
	BindWorkStation.labelWidth = 100;
	BindWorkStation.fieldWidth = 140;
	BindWorkStation.idx = null;
	
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 初始化
	BindWorkStation.initFn = function(form) {
        
        // 设置作业计划基本信息
		form.find('fieldLabel', '台位名称')[0].setValue(deskName);
		form.find('fieldLabel', '台位编号')[0].setValue(deskCode);
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义基本信息表单开始 **************** */
	BindWorkStation.form = new Ext.form.FormPanel({
		style: 'padding-left:20px;',
		layout: 'column', labelWidth: BindWorkStation.labelWidth,
		defaults: {
			xtype: 'container', columnWidth: .25, layout: 'form',
			defaults: {
				xtype: 'textfield', readOnly: true,
				width: BindWorkStation.fieldWidth, 
				style: 'background:none; border: none;', anchor: '100%'
			}
		},
		items: [{
			items: [{ fieldLabel: '台位名称' }]
		}, {
			items: [{ fieldLabel: '台位编号' }]
		}]
	});
	/** **************** 定义基本信息表单结束 **************** */
	BindWorkStation.grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/workStation!pageQuery.action',                 //装载列表数据的请求URL
		    saveURL: ctx + '/workStation!saveOrUpdate.action',             //保存数据的请求URL
		    deleteURL: ctx + '/workStation!logicUpdate.action',            //删除数据的请求URL
		    searchFormColNum: 1,
		    saveFormColNum: 2, 
		    tbar: ['工位编码：',{xtype:'textfield',id:'workStationCode',width:140},'&nbsp;&nbsp;',
			           {text:'添加',iconCls:'addIcon',handler:function(){
			              	   var workStationCode=Ext.getCmp("workStationCode").getValue();
			           	       if(workStationCode==""){
			           	       	  MyExt.Msg.alert("请输入工位编码");
			           	       	  return ;
			           	       }
			           	       Ext.Ajax.request({
			           	          url: ctx + '/workStation!saveStationByCode.action',
						          params : {deskCode : deskCode , deskName : deskName ,mapcode : mapcode ,workStationCode : workStationCode},
			           	          success:function(response,options){
			           	          	var result=Ext.util.JSON.decode(response.responseText);
			           	          	if (result.errMsg == null) {
						                    alertSuccess();
						                    BindWorkStation.grid.store.load();
						                } else {
						                    alertFail(result.errMsg);
						                    BindWorkStation.grid.store.load();
						                }
			           	          },
			           	          failure: function(response, options){
		                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		                          }
			           	       });
			           	
			           } },'&nbsp;&nbsp;',
			           {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
		       	           BindWorkStation.batchWin.show();
		       	           WorkStationSelect.grid.store.load();
			           }},'&nbsp;&nbsp;','delete'],
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true, editor: { id:'workStationIdx', xtype:'hidden' }
			},{
				header:'工位编码', dataIndex:'workStationCode', editor:{ id:'workStationCode_Id', allowBlank:false, maxLength:50 }
			},{
				header:'工位名称', dataIndex:'workStationName', editor:{ id:'workStationName_Id', allowBlank:false, maxLength:100 }
			},{
				header:'流水线主键', dataIndex:'repairLineIdx', hidden:true, editor:{ xtype:'hidden', maxLength:50 }
			},{
				header:'所属流水线', dataIndex:'repairLineName', editor:{ xtype:'hidden', maxLength:100 }
			},{
				header:'所属台位编码', dataIndex:'deskCode', hidden:true, editor:{  xtype:'hidden', id:'deskCode' }
			},{
				header:'所属图', dataIndex:'ownerMap', hidden:true, editor:{  xtype:'hidden', id:'ownerMap' }
			},{
				header:'所属台位', dataIndex:'deskName', hidden:true
			},{
				header:'状态', dataIndex:'status', 
				renderer : function(v){if(v == stationStatus_new)return "新增";else if(v == stationStatus_use) return "启用";else return "作废";},
				editor:{ xtype:'hidden', value: stationStatus_new },
				searcher:{ disabled: true }			
			}	
			,{
				header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },
				searcher:{disabled: true}
			}]
		});
		BindWorkStation.grid.un("rowdblclick",BindWorkStation.grid.toEditFn,BindWorkStation.grid);
		BindWorkStation.grid.store.on('beforeload',function(){
		     var whereList = [];
	    	 whereList.push({propName:"deskCode",propValue:deskCode,compare:Condition.EQ});
	    	 whereList.push({propName:"deskName",propValue:deskName,compare:Condition.EQ});
		     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
		});
		BindWorkStation.batchWin=new Ext.Window({
		     title:'工位选择', width:700, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
		    	maximizable:false,  modal:true,
		    	items:[{
		    	      region:'north',
		    	      collapsible :true,
		    	      height:66,
		    	      title:'查询',
		    	      items:[WorkStationSelect.batchForm]
		    	   },{
		    	      region:'center',
		    	      frame:true,
		    	      layout:'fit',
		    	      items:[WorkStationSelect.grid]
		    	   }],
		       buttons:[{text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=WorkStationSelect.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                   var datas=new Array();
				       for(var i=0;i<records.length;i++){
				           var data=records[i].data;
				           datas.push(data);
				       }
	                   Ext.Ajax.request({
	           	          url: ctx + '/workStation!saveStationBySelect.action',
	           	          jsonData:datas,
				          params : {deskCode : deskCode , deskName : deskName ,mapcode : mapcode },
	           	          success:function(response,options){
	           	          	var result=Ext.util.JSON.decode(response.responseText);
	           	          	if (result.errMsg == null) {
				                    alertSuccess();
				                    BindWorkStation.grid.store.load();
				                } else {
				                    alertFail(result.errMsg);
				                    BindWorkStation.grid.store.load();
				                }
	           	          },
	           	          failure: function(response, options){
                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                          }
	           	       });
	                   BindWorkStation.batchWin.hide();
	                   
	              }},'&nbsp;&nbsp;',
	              {text:'关闭',iconCls:'closeIcon',handler:function(){
	              	    BindWorkStation.batchWin.hide();
	              }}]
		});
	// 自适应页面布局
	new Ext.Viewport({
		layout: 'border', defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north',
			height: 60,
			frame: true,
			title: '绑定工位',
			collapsible: true,
			collapseMode: 'mini',
			items: BindWorkStation.form
		}, {
			region: 'center', autoScroll : false,
			items:[BindWorkStation.grid]
		}], 
		listeners: {
			render: function(form) {
				BindWorkStation.initFn(form);
			}
		}
	});
})