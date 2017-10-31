/**
 * 配件检修作业 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdp');                       //定义命名空间
	
	PartsRdp.fieldWidth = 150;
	PartsRdp.labelWidth = 90;
	PartsRdp.searchParam = {};
	PartsRdp.specificationModel = "";  //规格型号
	PartsRdp.partsTypeIdx = "";//规格型号id
	PartsRdp.flag = true ;//用于控制是否终止兑现的标记

	PartsRdp.isNewParts = "新,旧";
	//状态多选按钮
	PartsRdp.checkQuery = function(){
		PartsRdp.isNewParts = "-1";
		if(Ext.getCmp("isNewParts_Yes").checked){
			PartsRdp.isNewParts = PartsRdp.isNewParts + ",新";
		} 
		if(Ext.getCmp("isNewParts_No").checked){
			PartsRdp.isNewParts = PartsRdp.isNewParts + ",旧";
		} 
		PartsRdp.grid.store.load();
	}
	
	
	PartsRdp.partsTypeTree = new Ext.tree.TreePanel({
		tbar :new Ext.Toolbar(),
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/partsType!findRepairListPartsTypeTree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '配件规格型号',
	        id: 'ROOT_0',
	        leaf: false,
	        expanded :true
	    }),
	    rootVisible: true,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false,
	    listeners: {
	        click: function(node, e) {
	        	PartsRdp.returnFn(node);
	        },
	        beforeload: function(node){
				this.loader.dataUrl = ctx + '/partsType!findRepairListPartsTypeTree.action?searchParams=' + Ext.util.JSON.encode({repairOrgID:teamOrgId});
			}
	    }    
	});
	
	PartsRdp.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsAccount!pageQuery.action',                //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdp!saveOrUpdate.action',            	 	//保存数据的请求URL
	    deleteURL: ctx + '/partsRdp!logicDelete.action',            	//删除数据的请求URL
	    tbar:["是否新品：",{   
	        xtype:"checkbox", id:"isNewParts_Yes", boxLabel:"新&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: PartsRdp.checkQuery
	    }, {   
	        xtype:"checkbox", id:"isNewParts_No", boxLabel:"旧&nbsp;&nbsp;&nbsp;&nbsp;", checked:true , handler: PartsRdp.checkQuery
	    }, "-", {
	    	text:'生成检修任务单', iconCls:'checkIcon', handler:function() {
				if(PartsRdp.specificationModel == ""){
					MyExt.Msg.alert("请先选择配件规格型号！");
	           	  	return ;
				}
				//未选择配件记录，直接返回
				if(!$yd.isSelectedRecord(PartsRdp.grid)) return;
	//    		var ids = $yd.getSelectedIdx(PartsRdp.grid, PartsRdp.grid.storeId);
	      	    PartsRdp.createRdpWin.show();
	      	    PartsRdp.baseForm.getForm().reset();
	      	    //设置负责人默认值
	      	    Ext.getCmp('PartsRdp_selectEmp').setDisplayValue(empName, empName);
	      	    Ext.getCmp('PartsRdp_dutyEmpID').setValue(empId);
	      	    Ext.getCmp('PartsRdp_repairOrgID').setValue(teamOrgId);
	      	    Ext.getCmp('PartsRdp_repairOrgName').setValue(teamOrgName);
	      	    WP.grid.store.load();
	//	        WP.grid.selModel.selectRow(1);
	      	    
	      	    Emp.grid.getView().refresh(); 
	      	    Emp.grid.store.removeAll();
	      	    PartsRdp.flag = true;
	      	}
  		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'配件型号主键', dataIndex:'partsTypeIDX',hidden:true, editor:{  maxLength:50 }
		},{
			header:'物料编码', dataIndex:'matCode', hidden:true,editor:{  maxLength:50 }
		},{
			header:'配件编号', dataIndex:'partsNo', editor:{  maxLength:50 }
		},{
			header:'扩展编号', dataIndex:'extendNoJson', editor:{  maxLength:300 },
			renderer: function(v,metadata, record, rowIndex, colIndex, store) {
				var extendNoJson = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
				if (Ext.isEmpty(extendNoJson)) {
					return "";
				}
	            return extendNoJson;
			}
		},{
			header:'配件名称', dataIndex:'partsName', editor:{  maxLength:100 }
		},{
			header:'规格型号', dataIndex:'specificationModel', editor:{  maxLength:100 }
		},{
			header:'配件状态', dataIndex:'partsStatusName', editor:{  maxLength:50 }
		},{
			header:'责任部门ID', dataIndex:'manageDeptId', hidden:true, editor:{  maxLength:50 }
		},{
			header:'责任部门', dataIndex:'manageDept', editor:{  maxLength:50 }
		},{
			header:'配件状态编码', dataIndex:'partsStatus', hidden:true, editor:{  maxLength:50 }
		},{
			header:'是否新品', dataIndex:'isNewParts', editor:{  maxLength:10 }
		},{
			header:'配件旧编号', dataIndex:'oldPartsNo',hidden:true, editor:{  maxLength:50 }
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
		}]
	});
	
//树形面板选择按钮
	PartsRdp.returnFn = function(node) {
	   	PartsRdp.searchParam=PartsRdp.searchForm.getForm().getValues();
	   	if(node.id == 'ROOT_0'){
	   		PartsRdp.searchParam.specificationModel="";
	   		PartsRdp.specificationModel = "";
	   		PartsRdp.partsTypeIdx = "";
		} else {
	   		PartsRdp.searchParam.specificationModel=node.attributes.specificationModel;
	        PartsRdp.specificationModel = node.attributes.specificationModel;
	        PartsRdp.partsTypeIdx = node.id;
		}
		PartsRdp.searchParam=MyJson.deleteBlankProp(PartsRdp.searchParam);
		PartsRdp.grid.searchFn(PartsRdp.searchParam);
	}
	
 	//公用checkbox查询方法
	PartsRdp.grid.un("rowdblclick",PartsRdp.grid.toEditFn,PartsRdp.grid);
	PartsRdp.grid.store.on('beforeload',function(){
	 	PartsRdp.searchParam = PartsRdp.searchForm.getForm().getValues();
	 	PartsRdp.searchParam.specificationModel = PartsRdp.specificationModel;
	 	PartsRdp.searchParam.manageDeptId = teamOrgId;//责任部门为当前部门
	     var searchParam=PartsRdp.searchParam;
	     searchParam.isNewParts = PartsRdp.isNewParts;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     //排除已兑现的配件
	     var sqlStr = " idx not in (select nvl(PARTS_ACCOUNT_IDX,'0') from PJJX_Parts_Rdp where Record_Status=0 and status in ('"+STATUS_WQD+"','"+STATUS_JXZ+"','"+STATUS_DYS+"'))"+
	     	" and Parts_Type_IDX in (select Parts_Type_IDX from PJWZ_Parts_Repair_List where Record_Status = 0 and Repair_OrgID='"+teamOrgId+"')";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
					]
	     for(prop in searchParam){
	     	if(prop=='partsStatus' && searchParam[prop]!=null){
	     		var value = searchParam[prop];
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LLIKE});
	     		continue;
	     	}
	     	if(prop=='unloadTrainTypeIdx' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
	     		continue;
	     	}
	     	if(prop=='specificationModel' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if('isNewParts' == prop){
				var val = searchParam[prop];
				val = val.toString();
				val = val.split(",");
                if(val instanceof Array){
                    whereList.push({propName:'isNewParts', propValues:val, compare:Condition.IN });
                } else {
                    var valAry = [];
                    valAry.push(val);
                    whereList.push({propName:'isNewParts', propValues:valAry, compare:Condition.IN });
                }
                continue;
		 	}else{
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
	     		continue;
	     	}
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	
	/** *************** 定义查询表单开始 *************** */
	PartsRdp.searchForm=new Ext.form.FormPanel({
		    layout: "column", border: false, style: "padding:10px", labelWidth: PartsRdp.labelWidth,
		    defaults: {columnWidth: .33, layout: "form", defaults: {width: PartsRdp.fieldWidth}},
		    items: [{
				items:[{
					 id:"trainType_comb_s",xtype: "TrainType_combo",	fieldLabel: "下车车型",
					 hiddenName: "unloadTrainTypeIdx",
//					 returnField: [{widgetId:"unloadTrainType",propertyName:"shortName"}],
					displayField: "shortName", valueField: "typeID",
					pageSize: 20, minListWidth: 200,   //isCx:'no',
					editable:false  ,
					listeners : {   
						"select" : function() {   
							//重新加载修程下拉数据
							var rc_comb_s = Ext.getCmp("rc_comb_s");
							rc_comb_s.reset();
							rc_comb_s.clearValue();
							rc_comb_s.getStore().removeAll();
							rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue()};
					        rc_comb_s.cascadeStore();
						}   
					}
				}, {
					xtype: 'Base_comboTree', hiddenName:'partsStatus',id:'comboTree_select',isExpandAll: true,//hiddenName: 'partsStatusBill',
					fieldLabel:'配件状态',returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'all', minListWidth:200,
					treeUrl: ctx + '/eosDictEntrySelect!tree.action',  queryParams: {'dicttypeid':'PJWZ_PARTS_ACCOUNT_STATUS'},
					rootId: PARTS_STATUS_ZC, rootText: '在册'
				}]
		    }, {
				items:[{
					xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"
				}, {
					xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"					
				}]
		    }, {
				items:[{
					id:"rc_comb_s",
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
					editable:false
				}]
		    }],
		    buttonAlign:"center",
		    buttons: [{
	            text: "查询", iconCls: "searchIcon", handler: function(){
	            	PartsRdp.searchParam = PartsRdp.searchForm.getForm().getValues();
	            	PartsRdp.searchParam.specificationModel = PartsRdp.specificationModel;
	            	PartsRdp.searchParam=MyJson.deleteBlankProp(PartsRdp.searchParam);
		 			PartsRdp.grid.searchFn(PartsRdp.searchParam);
	            }
	        },{
	            text: "重置", iconCls: "resetIcon", handler: function(){
	            	PartsRdp.searchForm.getForm().reset();
	            	Ext.getCmp("trainType_comb_s").clearValue();
	            	Ext.getCmp("rc_comb_s").clearValue();
	            	Ext.getCmp("comboTree_select").setDisplayValue(PARTS_STATUS_ZC,'在册');
			        PartsRdp.specificationModel = "";
	            	PartsRdp.searchParam = {};
				    PartsRdp.grid.store.load();
	            }
	        }]
		});
		/** *************** 定义查询表单开始 *************** */
		
	PartsRdp.baseForm=new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsRdp.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
		     columnWidth:.5,labelWidth: PartsRdp.labelWidth,
		     layout:'form',
		     baseCls:'x-plain',
		     items:[
		     	  { id:"PartsRdp_selectEmp",xtype: 'TeamEmployee_SelectWin',allowBlank: false,width: PartsRdp.fieldWidth,fieldLabel : "负责人",hiddenName:'dutyEmpName',
					returnField:[{widgetId:"PartsRdp_dutyEmpID",propertyName:"empid"},
								 {widgetId:"PartsRdp_repairOrgID",propertyName:"orgid"},
								 {widgetId:"PartsRdp_repairOrgName",propertyName:"orgname"}],
					displayField:'empname',valueField:'empname',orgid:teamOrgId, editable:false
					},
				    {id:"PartsRdp_dutyEmpID",xtype:"hidden", name:"dutyEmpID"},
					{id:"PartsRdp_repairOrgID",xtype:"hidden", name:"repairOrgID"},
					{id:"PartsRdp_repairOrgName",xtype:"hidden", name:"repairOrgName"}
		        ]},{
			     columnWidth:.5,
			     layout:'form',labelWidth: PartsRdp.labelWidth,
			     baseCls:'x-plain',
			     items:[
			     		{ name: "planStartTime", fieldLabel: "计划开始时间", allowBlank: false,xtype: "my97date",format: "Y-m-d H:i",
	            	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"},initNow: true,width: PartsRdp.fieldWidth}
			   	 	
				]}
	        ]
	    }]
	});
	PartsRdp.loadMaskForm=new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsRdp.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
		     columnWidth:1,labelWidth: PartsRdp.labelWidth,
		     layout:'form',
		     baseCls:'x-plain',
		     items:[
				   {xtype:'tbtext',id:'PartsRdp_loadMask'}
		        ]}
	        ]
	    }]
	});
	//提示信息弹出窗口
	PartsRdp.loadMaskWin = new Ext.Window({
	    title:"提示", items:[PartsRdp.loadMaskForm],modal :true,
	    width: 250, height: 150, plain: true, closeAction: "hide",buttonAlign:'center',
	    buttons: [{
			text:"取消", iconCls:"closeIcon",handler:function(){
	    	 	PartsRdp.flag = false;
	    	 }
    	}]
	});
	//保存方法
	PartsRdp.saveFun = function(){
		var form = PartsRdp.baseForm.getForm();
	    if (!form.isValid()) return;
	    var rdpData = form.getValues(); //负责人、计划开始时间
	    var wpRecord = WP.grid.selModel.getSelections();
		if(wpRecord.length == 0){
			MyExt.Msg.alert("请选择检修需求单！");
			return ;
		}
		var wpIdx = wpRecord[0].data.idx ; //需求单id
		var record = Emp.grid.store.getModifiedRecords();
		var datas = new Array(); //施修人员信息
		if(record.length > 0){
			for (var i = 0; i < record.length; i++) {
				var data = {} ;
				data.workEmpID = record[i].data.empid;
				data.workEmpName = record[i].data.empname;
				datas.push(data);
			}
		}
		var record = PartsRdp.grid.selModel.getSelections();
//		var idx = "";//所选配件id
		var j = 0; //记录处理了多少条记录
		PartsRdp.loadMaskWin.show();
//		for (var i = 0; i < record.length; i++) {
//			idx = record[i].data.idx;
		Ext.getCmp("PartsRdp_loadMask").setText("正在处理，请稍侯...");
		PartsRdp.submitFun(PartsRdp.flag , record,datas, rdpData , wpIdx , j);
			
//		 }
	}
	
	PartsRdp.submitFun = function(flag , record , datas ,rdpData , wpIdx ,j){
		if(PartsRdp.flag == true && j<record.length){
		        Ext.Ajax.request({
		            url: ctx + '/partsRdp!savePartsRdp.action',
		            jsonData: datas,
		            timeout: 6000000,
		            params : {rdpData : Ext.util.JSON.encode(rdpData) ,wpIdx : wpIdx,id : record[j].data.idx},
		            success: function(response, options){
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                	j = j + 1 ;
		       			    Ext.getCmp("PartsRdp_loadMask").setText("正在处理，请稍侯...<br>共计"+record.length+"条，已处理"+j+"条！");
		                    PartsRdp.grid.store.reload();
		                    PartsRdp.submitFun(PartsRdp.flag , record ,datas, rdpData , wpIdx ,j );
	//	                    if(PartsRdp.flag == false) return ;
		                } else {
		                    alertFail(result.errMsg);
		                }
		            },
		            failure: function(response, options){
		                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		            }
		        });
		    } else {
		    	MyExt.TopMsg.successMsg("操作成功"+j+"条！");
		    	PartsRdp.loadMaskWin.hide();
		    	PartsRdp.createRdpWin.hide();
		     	PartsRdp.grid.store.reload();
		    	return;
		    }
	}
	/** *************** 定义生成作业计划单窗口开始 *************** */
	PartsRdp.createRdpWin = new Ext.Window({
		title: "生成作业计划单", width:850, height:600, maximizable:false, layout: "border", 
		closeAction: "hide", modal: true, 
		defaults:{border: false},
		items:[{
	        region:'north', layout: "border", height:260,
			items:[{
				region : 'north', layout: "fit",
				height: 50,
				baseCls:'x-plain',
		        border: false,
		        autoScroll : true,
		        items : [ PartsRdp.baseForm ]
			},{
				title:'<span style="font-weight:normal">选择检修需求单</span>',
				region : 'center',
		        xtype: "panel", layout: "fit",
		        items:[ WP.grid ]
			}]
	     },{
			region:"center", layout: "border",
			items:[{
				width: 300,layout: "fit",
				collapsible: true,
				title : '<span style="font-weight:normal">选择施修人员</span>',
	        	iconCls : 'icon-expand-all',
	        	tools : [ {
		            id : 'refresh',
		            handler: function() {
		            	Emp.tree.getRootNode().reload();
		            }
		        }],
				region : 'west',
		        items : [ Emp.tree ]
			},{
				title:'<span style="font-weight:normal">已选择施修人员列表</span>',
				region : 'center',
		        xtype: "panel", layout: "fit",
		        items:[ Emp.grid ]
			}]
		}],
		buttonAlign:"center",
		buttons: [{
            text: "确定", iconCls: "saveIcon", handler: function(){  PartsRdp.saveFun(); }
        },{
            text: "取消", iconCls: "closeIcon", handler: function(){ PartsRdp.createRdpWin.hide(); }
        }]
	});
	/** *************** 定义生成作业计划单窗口开始 *************** */
	
	// 页面自适应布局
	var viewport=new Ext.Viewport({
	    layout:'border',
	    items:[{
			region:'west', layout:'fit',
			title : '<span style="font-weight:normal">配件规格型号树</span>',
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	PartsRdp.partsTypeTree.getRootNode().reload();
	            }
	        }],
			collapsible: true,
			width:330,
			items:[ PartsRdp.partsTypeTree]
    	},{
    	   region:'center', layout:'border',
	       border:false,
    	   items:[{
    	      region:'north',
	    	  frame:true,
    	      collapsible :true,
    	      height:140,
    	      title:'查询',
    	      items:[PartsRdp.searchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      items:[PartsRdp.grid]
    	   }]
    	}]
	});
	
	// 页面初始化方法定义及调用
	PartsRdp.init = function(){
		Ext.getCmp("comboTree_select").setDisplayValue(PARTS_STATUS_ZC,'在册');
	}();
	
});