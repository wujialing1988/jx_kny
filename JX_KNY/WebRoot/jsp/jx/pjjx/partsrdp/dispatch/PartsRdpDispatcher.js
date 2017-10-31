/**
 * 未派工任务单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpDispatcher');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpDispatcher.fieldWidth = 150;
	PartsRdpDispatcher.labelWidth = 90;
	PartsRdpDispatcher.searchParam = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【终止】按钮触发的函数处理
	PartsRdpDispatcher.terminateFn = function() {
		//未选择任务单记录，直接返回
		if(!$yd.isSelectedRecord(PartsRdpDispatcher.grid)) {
			return;
		}
		Ext.Msg.confirm('提示', '终止后检修作业将不可恢复，是否确认终止？', function(btn) {
			if (btn == 'yes') {
				//所选任务单idx
				var ids = $yd.getSelectedIdx(PartsRdpDispatcher.grid, PartsRdpDispatcher.grid.storeId);
				// Ajax 请求
				Ext.Ajax.request({
				    url: ctx + '/partsRdp!updateStatus.action',
				    params : {flag : STATUS_YZZ,ids : ids},
				    success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
				        	alertSuccess();
				            PartsRdpDispatcher.grid.store.reload();
				        } else {
				            alertFail(result.errMsg);
				        }
				    },
				    failure: function(response, options){
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		});
	}
	/** ************** 定义全局函数结束 ************** */
	
	PartsRdpDispatcher.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpQuery!findPartsRdpList.action',        //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdp!saveOrUpdate.action',            	 	//保存数据的请求URL
	    deleteURL: ctx + '/partsRdp!logicDelete.action',            	//删除数据的请求URL
	    viewConfig:{},
	    tbar:[{
	    	text:'终止',iconCls:'deleteIcon',handler:PartsRdpDispatcher.terminateFn
    	},{
    		text:'批量派工',iconCls:'checkIcon',handler:function(){
				//未选择任务单记录，直接返回
				if(!$yd.isSelectedRecord(PartsRdpDispatcher.grid)) return;
	      	    PartsRdpDispatcher.batchDispatchWin.show();
	      	    PartsRdpDispatcher.workerGrid.getView().refresh(); 
	      	    PartsRdpDispatcher.workerGrid.store.removeAll();
	      	}
      }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
				header:'指派作业人员', dataIndex:'idx', editor: { xtype:'hidden' }, width:100, sortable:false,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){			
					return "<img src='" + img + "' alt='指派作业人员' style='cursor:pointer' onclick='PartsRdpDispatcher.grid.toEditFn(\""+ PartsRdpDispatcher.grid + "\",\""+ rowIndex +"\")'/>";
				}
		},{
			header:'配件型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode', hidden:true,editor:{  maxLength:50 }
		},{
			header:'配件编号', dataIndex:'partsNo', editor:{  maxLength:50 }
		},{
			header:'扩展编号', dataIndex:'extendNo', editor:{  maxLength:300 },
			renderer: function(v,metadata, record, rowIndex, colIndex, store) {
				var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
				if (Ext.isEmpty(extendNo)) {
					return "";
				}
	            return extendNo;
			}
		},{
			header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
		},{
			header:'规格型号', dataIndex:'specificationModel',width:150, editor:{  maxLength:100 }
		},{
			header:'检修班组', dataIndex:'repairOrgName', editor:{  maxLength:50 }
		},{
			header:'检修负责人', dataIndex:'dutyEmpName', editor:{  maxLength:50 }
		},{
			header:'施修人员', dataIndex:'workNameStr', editor:{  maxLength:50 }
		},{
			header:'下车车型', dataIndex:'unloadTrainType', editor:{  maxLength:50 }
		},{
			header:'下车车型编码', dataIndex:'unloadTrainTypeIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'下车车号', dataIndex:'unloadTrainNo', editor:{  maxLength:50 }
		},{
			header:'下车修程编码', dataIndex:'unloadRepairClassIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'下车修程', dataIndex:'unloadRepairClass', editor:{  maxLength:50 }
		},{
			header:'下车修次编码', dataIndex:'unloadRepairTimeIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'下车修次', dataIndex:'unloadRepairTime', editor:{  maxLength:50 }
		},{
			header:'检修开始时间', dataIndex:'realStartTime',width:120, xtype:'datecolumn',format: "Y-m-d H:i"
		},{
			header:'检修结束时间', dataIndex:'realEndTime', width:120,xtype:'datecolumn',format: "Y-m-d H:i"
		},{
			header:'检修状态', dataIndex:'status', hidden:true, editor:{  maxLength:50 }
		},{
			header:'计划开始时间', dataIndex:'planStartTime', hidden:true,xtype:'datecolumn',format: "Y-m-d H:i"
		},{
			header:'计划结束时间', dataIndex:'planEndTime', hidden:true,xtype:'datecolumn',format: "Y-m-d H:i"
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = this.store.getAt(rowIndex);
			isFullDipatch = 'false';
			PartsRdpDispatchered.rdpId = record.get('idx') ;
			PartsRdpDispatchered.dispatchWin.show();
			PartsRdpDispatchered.baseForm.getForm().loadRecord(record);
			var extendNo = record.get("extendNo");
			extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(extendNo);
			
			// 格式化扩展编号值
			PartsRdpDispatchered.baseForm.find('name', 'extendNo')[0].setValue(extendNo);
			// 设置计划开始时间的显示值
			PartsRdpDispatchered.baseForm.find('name', 'planStartTime')[0].setValue(record.get('planStartTime').format('Y-m-d H:m'));
			// 设置计划结束时间的显示值
			PartsRdpDispatchered.baseForm.find('name', 'planEndTime')[0].setValue(record.get('planEndTime').format('Y-m-d H:m'));
			
			PartsRdpDispatchered.workerGrid.store.load();
		}
	});
	PartsRdpDispatcher.grid.store.on('beforeload', function() {
		PartsRdpDispatcher.searchParam = PartsRdpDispatcher.searchForm.getForm().getValues();
		delete PartsRdpDispatcher.searchParam.PartsTypeTreeSelect_select;
		var searchParams = MyJson.deleteBlankProp(PartsRdpDispatcher.searchParam);
		PartsRdpDispatcher.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	
//规格型号选择控件赋值函数
PartsRdpDispatcher.callReturnFn=function(node,e){
	PartsRdpDispatcher.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["text"]);
	PartsRdpDispatcher.searchForm.find("name","specificationModel")[0].setValue(node.attributes["specificationModel"]);
}
	/** *************** 定义查询表单开始 *************** */
	PartsRdpDispatcher.searchForm = new Ext.form.FormPanel({
		labelWidth: PartsRdpDispatcher.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        defaults:{
        	columnWidth:.33, layout:'form', defaults: {
				width:PartsRdpDispatcher.fieldWidth        		
        	}
    	},
        items: [{
			items:[{
				id:"trainType_comb",
				fieldLabel: "下车车型",
				xtype: "Base_combo",
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
	            fields:['idx','typeName','typeCode','vehicleType'],
	            queryParams: {},// 表示客货类型
			    displayField: "typeCode", valueField: "idx",
	            pageSize: 20, minListWidth: 200,
	            editable:false,				
				listeners: {   
					"select" : function(combo, record, index) {   
						//重新加载修程下拉数据
						var vehicleType = record.data.vehicleType ;
						var rc_comb = Ext.getCmp("rc_comb");
		                rc_comb.reset();
		                rc_comb.clearValue();
		                rc_comb.getStore().removeAll();
		                rc_comb.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
		                rc_comb.cascadeStore();
		        	}   
				}
			}, {
				xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
			}]
		},{
			items:[{
				id:"unloadTrainNo",xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"
			}, {
	         	xtype:"PartsTypeTreeSelect",
	         	fieldLabel: '规格型号',
	         	id:'PartsTypeTreeSelect_select',
				hiddenName: 'specificationModel', 
				editable:false,
				width:PartsRdpDispatcher.fieldWidth,
				returnFn: PartsRdpDispatcher.callReturnFn
	         }, {
	         	xtype:'hidden',fieldLabel:'规格型号',name:"specificationModel"
         	}]
     	},{
     		items:[{
				id:"rc_comb",
				xtype: "Base_combo",
				business: 'trainRC',
				entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
				fields:['xcID','xcName'],
				fieldLabel: "下车修程",
				hiddenName: "unloadRepairClassIdx", 
//					returnField: [{widgetId:"unloadRepairClass",propertyName:"xcName"}],
				displayField: "xcName",
				valueField: "xcID",
				pageSize: 20, minListWidth: 200,
				queryHql: 'from UndertakeRc',
				width: 140,
				editable:false
			}]
        }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			PartsRdpDispatcher.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	PartsRdpDispatcher.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb").clearValue();
            	Ext.getCmp("PartsTypeTreeSelect_select").setValue("");
            	PartsRdpDispatcher.searchParam = {};
			    PartsRdpDispatcher.grid.store.load();
            }
        }]
	});
	/** *************** 定义查询表单结束 *************** */
	
//删除函数
PartsRdpDispatcher.deleteFn = function(rowIndex) {
	PartsRdpDispatcher.workerGrid.store.removeAt(rowIndex); 
	PartsRdpDispatcher.workerGrid.getView().refresh(); 
}

PartsRdpDispatcher.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,
	    fields: [ "empid","empcode","empname" ]
	});
//批量派工施修人员列表
PartsRdpDispatcher.workerGrid=new Ext.grid.GridPanel({
  enableColumnMove: true, 
  border:false,
  stripeRows: true,
  loadMask:true,
  viewConfig: {forceFit: true},
   colModel: new Ext.grid.ColumnModel( [
              new Ext.grid.RowNumberer(),
   	         {
				header:'idx主键', dataIndex:'empid', hidden:true,editor: { xtype:'hidden' }
			},{
				header:'人员代码', dataIndex:'empcode', editor:{  maxLength:50 }
			},{
				header:'人员名称', dataIndex:'empname', editor:{  maxLength:50 }
			},{
				header:'操作', dataIndex:'idx', width:20, renderer:function(value, metaData, record, rowIndex, colIndex, store){			
					return "<img src='" + imgpathx + "' alt='删除' style='cursor:pointer' onclick='PartsRdpDispatcher.deleteFn(\"" + rowIndex + "\")'/>";
				}, sortable:false
			}]),
		   store: PartsRdpDispatcher.store
});
/** ************** 定义组织机构树开始 ************** */
	PartsRdpDispatcher.orgTree =  new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		border:false,
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : teamOrgName,
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'org',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	collapsed : false,
//    	enableDrag:true,
	    listeners: {
    		render : function() {
    			PartsRdpDispatcher.orgTree.root.reload();
			    PartsRdpDispatcher.orgTree.getRootNode().expand();
    		},
	        dblclick: function(node) {
	        	if (node.attributes.nodetype == 'emp'){
	        		var count=PartsRdpDispatcher.workerGrid.store.getCount();
        			//判断是否是第一次添加，如果不是，则判断当前添加的人员是否在列表中存在，如果存在，则不允许重复添加
   	          		if(count!=0){
   	          			for(var i=0;i<count;i++){
   	          				var record_v=PartsRdpDispatcher.workerGrid.store.getAt(i);
   	          				if(node.attributes.empid==record_v.get('empid')){
   	          					MyExt.Msg.alert("所选人员已添加，不可重复添加！");
   	          					return ;
   	          				}
   	          			}
   	          		}
	        		var record = new Ext.data.Record();
	        		record.set("empid",node.attributes.empid);
	        		record.set("empcode",node.attributes.empcode);
	        		record.set("empname",node.attributes.empname);
	        		PartsRdpDispatcher.workerGrid.store.insert(0, record); 
					PartsRdpDispatcher.workerGrid.getView().refresh(); 
    			}
	        },
	        beforeload: function(node){
		        var tempid;
				if(node.id=='ROOT_0') tempid = teamOrgId;
				else tempid = node.id.substring(2,node.id.length);
		    	this.loader.dataUrl = ctx + '/organization!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;}
		    }    
	});
	/** ************** 定义组织机构树结束 ************** */
//保存批量派工的施修人员信息
PartsRdpDispatcher.saveFun = function(){
		var record = PartsRdpDispatcher.workerGrid.store.getModifiedRecords();
		var datas = new Array(); //施修人员信息
		if(record.length == 0){
			MyExt.Msg.alert("没有可提交的数据！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data.workEmpID = record[i].data.empid;
			data.workEmpName = record[i].data.empname;
			datas.push(data);
		}
		var ids = $yd.getSelectedIdx(PartsRdpDispatcher.grid, PartsRdpDispatcher.grid.storeId);//所选任务单idx
        Ext.Ajax.request({
            url: ctx + '/partsRdpWorker!saveBatch.action',
            jsonData: datas,
            params : {ids : ids},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	alertSuccess();
                    PartsRdpDispatcher.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
		 PartsRdpDispatcher.batchDispatchWin.hide();
	     PartsRdpDispatcher.grid.store.reload();
        
	}
	
	/** *************** 定义批量派工窗口开始 *************** */
	PartsRdpDispatcher.batchDispatchWin = new Ext.Window({
		title: "作业人员",
		width: 720, height: 560, 
		maximizable:false, maximized: false, modal: true,
		closeAction: "hide",
		layout: "border",
		items:[{
			collapsible: true,
			title : '<span style="font-weight:normal">选择施修人员</span>',
        	iconCls : 'icon-expand-all',
        	tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	PartsRdpDispatcher.orgTree.getRootNode().reload();
	            }
	        }],
			width: 200,layout: "fit",
			region : 'west',
	        autoScroll : true,
	        items : [ PartsRdpDispatcher.orgTree ]
		},{
			title:'<span style="font-weight:normal">已选择施修人员列表</span>',
			region : 'center',
	        layout: "fit",
	        items:[ PartsRdpDispatcher.workerGrid]
		}],
		buttonAlign:"center",
		buttons: [{
	        text: "确定", iconCls: "saveIcon", handler: function(){  PartsRdpDispatcher.saveFun(); }
	    },{
	        text: "关闭", iconCls: "closeIcon", handler: function(){ PartsRdpDispatcher.batchDispatchWin.hide(); }
	    }]
    });
	/** *************** 定义批量派工窗口开始 *************** */
    
	//页面自适应布局
	var viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 140,
			border: true,
			collapsible: true,
			items: [PartsRdpDispatcher.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			border: false,
			items: [PartsRdpDispatcher.grid]
		}]
	});
});