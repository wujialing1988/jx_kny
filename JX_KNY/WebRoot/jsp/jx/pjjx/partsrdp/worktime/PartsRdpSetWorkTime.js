/**
 * 配件检修工时分配 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpChecking');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpChecking.fieldWidth = 150;
	PartsRdpChecking.labelWidth = 80;
	PartsRdpChecking.searchParam = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	//最近一个月
	PartsRdpChecking.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	// 【分配工时】按钮触发的函数处理
	PartsRdpChecking.editWorkTimeFn = function(rowIndex) {
		PartsRdpWorkTime.editWorkTimeWin.show();
		var record = PartsRdpChecking.grid.store.getAt(rowIndex);
		PartsRdpWorkTime.rdpForm.getForm().loadRecord(record);
		// 设置开始时间的显示值
		if(record.get('realStartTime') != null) PartsRdpWorkTime.rdpForm.find('name', 'realStartTime')[0].setValue(record.get('realStartTime').format('Y-m-d H:m'));
		// 设置结束时间的显示值
		if(record.get('realEndTime') != null) PartsRdpWorkTime.rdpForm.find('name', 'realEndTime')[0].setValue(record.get('realEndTime').format('Y-m-d H:m'));
		var trainType = record.get('unloadTrainType') == null ? '' : record.get('unloadTrainType') ;
		var trainNo = record.get('unloadTrainNo') == null ? '' : record.get('unloadTrainNo') ;
		PartsRdpWorkTime.rdpForm.find('name', 'trainType')[0].setValue(trainType+"|"+trainNo);
		var repairClass = record.get('unloadRepairClass') == null ? '' : record.get('unloadRepairClass') ;
		var repairTime = record.get('unloadRepairTime') == null ? '' : record.get('unloadRepairTime') ;
		PartsRdpWorkTime.rdpForm.find('name', 'repair')[0].setValue(repairClass+"|"+repairTime);
		//查询工时数据
		PartsRdpWorkTime.rdpIDX = record.get('idx') ;
		PartsRdpWorkTime.ratedWorkTime = record.get("ratedWorkTime");
		PartsRdpWorkTime.grid.store.reload();
	}
	// 【平均分配工时】按钮触发的函数处理
	PartsRdpChecking.averageEditWorkTimeFn = function() {
		//未选择任务单记录，直接返回
		if(!$yd.isSelectedRecord(PartsRdpChecking.grid)) {
			return;
		}
		Ext.Msg.confirm('提示', '确认平均分配工时？', function(btn) {
			if (btn == 'yes') {
				//所选任务单idx
				var ids = $yd.getSelectedIdx(PartsRdpChecking.grid, PartsRdpChecking.grid.storeId);
				// Ajax 请求
				Ext.Ajax.request({
				    url: ctx + '/partsRdpWorkTime!updateWorkTimeAverage.action',
				    params : {ids : ids},
				    success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
				        	alertSuccess();
				            PartsRdpChecking.grid.store.reload();
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
	PartsRdpChecking.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpQuery!pageQuery.action',        //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdp!saveOrUpdate.action',            	 	//保存数据的请求URL
	    deleteURL: ctx + '/partsRdp!logicDelete.action',            	//删除数据的请求URL
	    viewConfig:{},
	    tbar:[{
    		text:'平均分配工时',iconCls:'checkIcon',handler:PartsRdpChecking.averageEditWorkTimeFn
      }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'配件型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode', hidden:true,editor:{  maxLength:50 }
		},{
				header:'分配工时', dataIndex:'idx', editor: { xtype:'hidden' }, width:100, sortable:false,
				renderer:function(value, metaData, record, rowIndex, colIndex, store){			
					return "<img src='" + img + "' alt='分配工时' style='cursor:pointer' onclick='PartsRdpChecking.editWorkTimeFn(\""+ rowIndex +"\")'/>";
				}
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
//			header:'施修人员', dataIndex:'workNameStr', editor:{  maxLength:50 }
//		},{
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
			header:'额定工时', dataIndex:'ratedWorkTime', hidden:true
		},{
			header:'计划开始时间', dataIndex:'planStartTime', hidden:true,xtype:'datecolumn',format: "Y-m-d H:i"
		},{
			header:'计划结束时间', dataIndex:'planEndTime', hidden:true,xtype:'datecolumn',format: "Y-m-d H:i"
		}],
		toEditFn: function(grid, rowIndex, e){
			var record = this.store.getAt(rowIndex);
			PartsRdpChecking.rdpInfoWin.show();
			PartsRdpChecking.baseForm.getForm().loadRecord(record);
			// 设置开始时间的显示值
			if(record.get('realStartTime') != null) PartsRdpChecking.baseForm.find('name', 'realStartTime')[0].setValue(record.get('realStartTime').format('Y-m-d H:m'));
			// 设置结束时间的显示值
			if(record.get('realEndTime') != null) PartsRdpChecking.baseForm.find('name', 'realEndTime')[0].setValue(record.get('realEndTime').format('Y-m-d H:m'));
			var trainType = record.get('unloadTrainType') == null ? '' : record.get('unloadTrainType') ;
			var trainNo = record.get('unloadTrainNo') == null ? '' : record.get('unloadTrainNo') ;
			PartsRdpChecking.baseForm.find('name', 'trainType')[0].setValue(trainType+"|"+trainNo);
			var repairClass = record.get('unloadRepairClass') == null ? '' : record.get('unloadRepairClass') ;
			var repairTime = record.get('unloadRepairTime') == null ? '' : record.get('unloadRepairTime') ;
			PartsRdpChecking.baseForm.find('name', 'repair')[0].setValue(repairClass+"|"+repairTime);
			PartsRdpRecordCard.rdpIDX = record.get('idx') ;
			//查询所有状态的记录单数据
			PartsRdpRecordCard.grid.store.reload();
			
			//查询所有状态的工艺工单数据
			PartsRdpTecCard.rdpIDX = record.get('idx') ;
			PartsRdpTecCard.grid.store.reload();
			
			//查询所有状态的提票工单数据
			PartsRdpNotice.rdpIDX = record.get('idx') ;
			PartsRdpNotice.grid.store.reload();
			
			//查询所有物料消耗数据
			PartsRdpExpendMatQuery.rdpIDX = record.get('idx') ;
			PartsRdpExpendMatQuery.grid.store.reload();
		}
	});
	PartsRdpChecking.grid.store.on('beforeload', function() {
		PartsRdpChecking.searchParam = PartsRdpChecking.searchForm.getForm().getValues();
		delete PartsRdpChecking.searchParam.PartsTypeTreeSelect_select;
		var searchParams = MyJson.deleteBlankProp(PartsRdpChecking.searchParam);
	     //查询“待验收”和“检修合格”的任务单
	     var sqlStr = " status in ('"+STATUS_DYS+"','"+STATUS_JXHG+"')";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
	     for(prop in searchParams){
	     	if('realStartTime' == prop){
			 	var realStartTime_v = searchParams[prop];
			 	realStartTime_v = realStartTime_v.toString();
			 	var realStartTime = realStartTime_v.split(",");
			 	if(realStartTime instanceof Array){
			 		if(realStartTime.length == 2 ){
			 			if(realStartTime[1] != "" && realStartTime[1] != "undefind"){
			 				whereList.push({propName:'realStartTime',propValue:realStartTime[0],compare:4});
			 				whereList.push({propName:'realStartTime',propValue:realStartTime[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'realStartTime',propValue:realStartTime[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'realStartTime',propValue:realStartTime[0]+' 23:59:59',compare:6});
			 		}
	                } 
		 		continue;
		 	}else{
	     		whereList.push({propName:prop,propValue:searchParams[prop],compare:Condition.EQ});
	     		continue;
	     	}
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	
//规格型号选择控件赋值函数
PartsRdpChecking.callReturnFn=function(node,e){
	PartsRdpChecking.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["text"]);
	PartsRdpChecking.searchForm.find("name","specificationModel")[0].setValue(node.attributes["specificationModel"]);
}
/** ************** 定义配件检修作业计划单信息表单开始 ************** */
	// 任务单基本信息
	PartsRdpChecking.baseForm = new Ext.form.FormPanel({
		labelWidth:70,
		border: false,
		labelAlign:"left",
		layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25, 
			defaults:{
				style: 'border:none; background:none;', 
				xtype:"textfield", readOnly: true,
				anchor:"100%"
			}
		},
		items:[{
			items:[{
				fieldLabel:"配件编号", name:"partsNo"
			}, {
				fieldLabel:"下车车型号", name:"trainType"
			}]
		}, {
			items:[{
				fieldLabel:"配件名称", name:"partsName"
			}, {
				fieldLabel:"下车修程", name:"repair"
			}]
		}, {
			items:[{
				fieldLabel:"规格型号", name:"specificationModel"
			}, {
				fieldLabel:"开始时间", name:"realStartTime"
			}]
		}, {
			items:[{
				fieldLabel:"扩展编号", name:"extendNo"
			}, {
				fieldLabel:"结束时间", name:"realEndTime"
			}]
		}]
	})
	/** ************** 定义配件检修作业计划单信息表单结束 ************** */
/** *************** 定义作业计划单窗口开始 *************** */
	PartsRdpChecking.rdpInfoWin = new Ext.Window({
		title: "作业计划单查看",
		width: 950, height: 700, 
		maximizable:false, maximized: false, modal: true,
		closeAction: "hide",
		layout: "border",
		items:[{
				title:"配件检修作业单信息",
				xtype:"panel", region:"north", frame: true, collapsible:true,
				height:105,
				items: PartsRdpChecking.baseForm
			// ------ 页面右边 - 下半部分 ------ //
			}, {
				xtype:"panel", region:"center", layout:"fit",
				items:[{
					xtype:"tabpanel", activeTab:0, id:"tabpanel_base",
					defaults:{layout:"fit"},
					items:[{
						title:"检修记录工单",
						items: PartsRdpRecordCard.grid
					}, {
						title:"检修工艺工单",
						items: PartsRdpTecCard.grid
					}, {
						title:"提票工单",
						items: PartsRdpNotice.grid
					}, {
						title:"物料消耗情况",
						items: PartsRdpExpendMatQuery.grid
					}
					]
				}]
			}],
		buttonAlign:"center",
		buttons: [{
	        text: "关闭", iconCls: "closeIcon", handler: function(){ PartsRdpChecking.rdpInfoWin.hide(); }
	    }]
    });
	/** *************** 定义作业计划单窗口开始 *************** */
    
	/** *************** 定义查询表单开始 *************** */
	PartsRdpChecking.searchForm = new Ext.form.FormPanel({
		labelWidth: PartsRdpChecking.labelWidth,
        border: false, layout: "column", style: "padding:10px",
        items: [{
        	columnWidth:.25, layout:'form', defaults: {
				width:PartsRdpChecking.fieldWidth        		
        	},
			items:[{
				id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "下车车型",
				hiddenName: "unloadTrainTypeIdx",
				// returnField: [{widgetId:"unloadTrainType",propertyName:"shortName"}],
				displayField: "shortName", valueField: "typeID",
				pageSize: 20, minListWidth: 200,   //isCx:'no',
				editable:false,
				listeners: {   
					"select" : function() {   
						//重新加载修程下拉数据
						var rc_comb = Ext.getCmp("rc_comb");
		                rc_comb.reset();
		                rc_comb.clearValue();
		                rc_comb.getStore().removeAll();
		                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
		                rc_comb.cascadeStore();
		        	}   
				}
			}, {
				xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
			}]
		},{
			columnWidth:.25, layout:'form', defaults: {
				width:PartsRdpChecking.fieldWidth        		
        	},
			items:[{
				id:"unloadTrainNo",xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"
			}, {
	         	xtype:"PartsTypeTreeSelect",
	         	fieldLabel: '规格型号',
	         	id:'PartsTypeTreeSelect_select',
				hiddenName: 'specificationModel', 
				editable:false,
				width:PartsRdpChecking.fieldWidth,
				returnFn: PartsRdpChecking.callReturnFn
	         }, {
	         	xtype:'hidden',fieldLabel:'规格型号',name:"specificationModel"
         	}]
     	},{
     		columnWidth:.5, layout:'form', 
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
			},{
				xtype: 'compositefield', fieldLabel : '配件检修日期', combineErrors: false,
	        	items: [
		           { id:"realStartTime",name: "realStartTime",  xtype: "my97date",format: "Y-m-d",  
		             value : PartsRdpChecking.getCurrentMonth(),
					width: 100},
					{
			    	    xtype:'label', text: '结束：'
				    },
					{ id:"realStartTime_end",name: "realStartTime", xtype: "my97date",format: "Y-m-d",  
					width: 100}]
			}]
        }],
	    buttonAlign:"center",
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
	 			PartsRdpChecking.grid.store.load();
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	PartsRdpChecking.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("rc_comb").clearValue();
            	Ext.getCmp("PartsTypeTreeSelect_select").setValue("");
            	PartsRdpChecking.searchParam = {};
			    PartsRdpChecking.grid.store.load();
            }
        }]
	});
	/** *************** 定义查询表单结束 *************** */
    
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
			items: [PartsRdpChecking.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			border: false,
			items: [PartsRdpChecking.grid]
		}]
	});
});