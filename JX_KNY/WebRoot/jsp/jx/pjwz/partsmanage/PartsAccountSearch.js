/**
 * 配件信息 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsAccountSearch');                       //定义命名空间
	PartsAccountSearch.fieldWidth = 120;
	PartsAccountSearch.labelWidth = 80;
	PartsAccountSearch.searchParam = {};
	PartsAccountSearch.specificationModel = "";
	PartsAccountSearch.manageDeptId = "";//责任部门id
	PartsAccountSearch.manageDeptOrgseq = "";//责任部门序列
	PartsAccountSearch.manageDeptType = "";//责任部门类型
	
	// 显示扩展编号详情的函数
//	PartsAccountSearch.searchExtendNo = function(rowIndex, formColNum) {
//		var extendNoJson = PartsAccountSearch.grid.store.getAt(rowIndex).get("extendNoJson");
//		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
//	}
	
	PartsAccountSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear-1,currentMonth,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	
    // 判断是否有检修作业工单，没有则显示配件详情信息，有，则显示配件检修作业信息
	PartsAccountSearch.storeLoadFn = function(){
		var roomCount = this.getCount();	
		if(0 == roomCount){
			var json = PartsAccountSearch.record.data;
			json.partsAccountIDX = json.idx;
			json.idx = "####";	
			PartsRdpDetail.record = json;			
		}else{			
			var  jsonRdp= this.getAt(0).json;
			PartsRdpDetail.record = jsonRdp;
			if (typeof(jsonRdp) == "undefine" || !jsonRdp){
				PartsRdpDetail.record = PartsRdpDetail.record.data;
			}
		}
		PartsRdpDetail.win.show();			
	}
	
	//查询是否有检修作业	
	PartsAccountSearch.showPartsRdp = function(record, partsNo, identificationCode){

		//初始化配件检修作业配件编号，配件识别码
		PartsRdpDetail.partsNo = partsNo;
		PartsRdpDetail.identificationCode = identificationCode;
		//初始化查询参数
		var params = {};
        params.partsNo = partsNo;
        params.partsAccountIDX = record.id;
		//加载检修作业单
		PartsAccountSearch.store = new Ext.data.JsonStore({
			id : 'idx',
			root : "root",
			totalProperty : "totalProperty",
			autoLoad : true,
			remoteSort : false,
			url : ctx + '/partsRdp!pageList.action',
			fields : ["idx", "identificationCode", "partsNo","partsName"
						,'specificationModel', "unloadTrainType", "unloadTrainNo"
						,"unloadRepairClass",'unloadRepairTime'
						,"realStartTime",'realEndTime'
						,"repairOrgName",'wpDesc','partsAccountIDX'],
			sortInfo : {
				field : 'idx',
				direction : 'ASC'
			},
			listeners : {
				beforeload : function() {
			 		this.baseParams.entityJson = Ext.util.JSON.encode(params);
				},
				// 数据加载完成后的函数处理
				load : PartsAccountSearch.storeLoadFn
			}
		});		
	}
	
	PartsAccountSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
	    border:false,
	    viewConfig: null,
	    tbar:null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'配件型号表主键', dataIndex:'partsTypeIDX', hidden:true,  editor:{  maxLength:50 }
		},{
			header:'配件名称', dataIndex:'partsName', width:180,editor:{  maxLength:100 }
		},{
			header:'规格型号', dataIndex:'specificationModel',width:100
		},{
			header:'物料编码', dataIndex:'specificationModel',width:100
		},{
			header:'配件编号', dataIndex:'partsNo',width:150		
		},{
			header:'配件识别码', dataIndex:'identificationCode',width:150	
		},{
			header:'存放地点', dataIndex:'location', editor:{  maxLength:100 }
		},{
			header:'详细配置', dataIndex:'configDetail',  hidden:true,editor:{  maxLength:200 }
		},{
			header:'责任部门ID', dataIndex:'manageDeptId', hidden:true, editor:{  maxLength:50 }
		},{
			header:'责任部门', dataIndex:'manageDept', editor:{  maxLength:50 }
		},{
			header:'责任部门类型', dataIndex:'manageDeptType', hidden:true, editor:{ xtype:'numberfield', maxLength:1 }
		},{
			header:'配件状态编码', dataIndex:'partsStatus', hidden:true, editor:{  maxLength:50 }
		},{
			header:'配件旧编号', dataIndex:'oldPartsNo', editor:{  maxLength:50 }
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
			header:'下车日期', dataIndex:'unloadDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'下车位置', dataIndex:'unloadPlace', editor:{  maxLength:100 }
		},{
			header:'下车原因', dataIndex:'unloadReason', editor:{  maxLength:100 }
		},{
			header:'上车车型编码', dataIndex:'aboardTrainTypeIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'上车车型', dataIndex:'aboardTrainType', editor:{  maxLength:50 }
		},{
			header:'上车车号', dataIndex:'aboardTrainNo', editor:{  maxLength:50 }
		},{
			header:'上车修程编码', dataIndex:'aboardRepairClassIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'上车修程', dataIndex:'aboardRepairClass', editor:{  maxLength:50 }
		},{
			header:'上车修次编码', dataIndex:'aboardRepairTimeIdx', hidden:true, editor:{  maxLength:8 }
		},{
			header:'上车修次', dataIndex:'aboardRepairTime', editor:{  maxLength:50 }
		},{
			header:'上车日期', dataIndex:'aboardDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'上车位置', dataIndex:'aboardPlace', editor:{  maxLength:100 }
		},{
			header:'配件状态', dataIndex:'partsStatusName', editor:{  maxLength:50 }
		},{
			header:'状态修改日期', dataIndex:'partsStatusUpdateDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'计量单位', dataIndex:'unit', hidden:true, editor:{  maxLength:20 }
		},{
			header:'生产厂家主键', dataIndex:'madeFactoryIdx', hidden:true, editor:{  maxLength:5 }
		},{
			header:'生产厂家', dataIndex:'madeFactoryName',  hidden:true,editor:{  maxLength:50 }
		},{
			header:'出厂日期', dataIndex:'factoryDate', hidden:true, xtype:'datecolumn', editor:{ xtype:'my97date' }
		},{
			header:'是否新品', dataIndex:'isNewParts', editor:{  maxLength:10 }
		}],
		toEditFn: function(grid, rowIndex, e) {
			// 配件检修作业记录
			PartsAccountSearch.record = grid.store.getAt(rowIndex);
			var partsNo = PartsAccountSearch.record.get('partsNo');
			var identificationCode = PartsAccountSearch.record.get('identificationCode');
			PartsAccountSearch.showPartsRdp(PartsAccountSearch.record, partsNo, identificationCode);
		}
	});
	
	PartsAccountSearch.grid.store.setDefaultSort('partsStatusUpdateDate', 'DESC');//设置默认排序
	
	//树形面板选择按钮
   	jx.pjwz.partbase.component.returnFn = function(node){
	   	PartsAccountSearch.searchParam=PartsAccountSearch.searchForm.getForm().getValues();
	   	if(node.id == 'ROOT_0'){
	   		PartsAccountSearch.searchParam.specificationModel="";
	   		PartsAccountSearch.specificationModel = "";
	   	}else {
	   		PartsAccountSearch.searchParam.specificationModel=node.attributes.specificationModel;
	        PartsAccountSearch.specificationModel = node.attributes.specificationModel;
	   	}
	     PartsAccountSearch.searchParam.manageDeptId = PartsAccountSearch.manageDeptId;
	     PartsAccountSearch.searchParam.manageDeptOrgseq = PartsAccountSearch.manageDeptOrgseq;
	     
	     PartsAccountSearch.searchParam.manageDeptType = PartsAccountSearch.manageDeptType ;//责任部门类型
	     PartsAccountSearch.searchParam=MyJson.deleteBlankProp(PartsAccountSearch.searchParam);
		 PartsAccountSearch.grid.searchFn(PartsAccountSearch.searchParam);
   }
   
   PartsManageDeptTree.returnFn = function(node){
   	  var id = node.attributes.id ;
   	  var seq = node.attributes.orgseq ;
      PartsAccountSearch.searchParam=PartsAccountSearch.searchForm.getForm().getValues();PartsAccountSearch.manageDeptType
	  if(id == 'Z_' &&  seq == undefined){
	     	 PartsAccountSearch.searchParam.manageDeptId = "";
	         PartsAccountSearch.searchParam.manageDeptOrgseq = "";
	     	 PartsAccountSearch.manageDeptId = "";
	         PartsAccountSearch.manageDeptOrgseq = "";
	         PartsAccountSearch.manageDeptType = MANAGE_DEPT_TYPE_ORG ;//责任部门类型为机构
	     }else if(id == 'K_' &&  seq == undefined){
	     	 PartsAccountSearch.searchParam.manageDeptId = "";
	         PartsAccountSearch.searchParam.manageDeptOrgseq = "";
	     	 PartsAccountSearch.manageDeptId = "";
	         PartsAccountSearch.manageDeptOrgseq = "";
	         PartsAccountSearch.manageDeptType = MANAGE_DEPT_TYPE_WH ;//责任部门类型为库房
		  }else{
			if(id.indexOf('Z_') != -1 && seq != 'undefined' && seq != ""){  //责任部门为机构
		     	PartsAccountSearch.searchParam.manageDeptId = "";
		        PartsAccountSearch.searchParam.manageDeptOrgseq = seq;
		     	PartsAccountSearch.manageDeptId = "";
		        PartsAccountSearch.manageDeptOrgseq = seq;
	            PartsAccountSearch.manageDeptType = MANAGE_DEPT_TYPE_ORG ;//责任部门类型为机构
		     }else if(id!= "" && seq == ""){  //责任部门为库房
		     	PartsAccountSearch.searchParam.manageDeptId = id;
		        PartsAccountSearch.searchParam.manageDeptOrgseq = "";
		     	PartsAccountSearch.manageDeptId = id;
		        PartsAccountSearch.manageDeptOrgseq = "";
	        	PartsAccountSearch.manageDeptType = MANAGE_DEPT_TYPE_WH ;//责任部门类型为库房
		     }
		 }
      PartsAccountSearch.searchParam.specificationModel=PartsAccountSearch.specificationModel;
      PartsAccountSearch.searchParam=MyJson.deleteBlankProp(PartsAccountSearch.searchParam);
	  PartsAccountSearch.grid.searchFn(PartsAccountSearch.searchParam);
	}
	
   //公用checkbox查询方法
	PartsAccountSearch.isNewParts = "新,旧";
	
	//状态多选按钮
	PartsAccountSearch.checkQuery = function(){
		PartsAccountSearch.isNewParts = "-1";
		if(Ext.getCmp("isNewParts_new").checked){
			PartsAccountSearch.isNewParts = PartsAccountSearch.isNewParts + ",新";
		} 
		if(Ext.getCmp("isNewParts_old").checked){
			PartsAccountSearch.isNewParts = PartsAccountSearch.isNewParts + ",旧";
		} 
		PartsAccountSearch.grid.store.load();
	}
	
//	PartsAccountSearch.grid.un("rowdblclick",PartsAccountSearch.grid.toEditFn,PartsAccountSearch.grid);
	
	PartsAccountSearch.grid.store.on('beforeload',function(){
	 	PartsAccountSearch.searchParam = PartsAccountSearch.searchForm.getForm().getValues();
	 	PartsAccountSearch.searchParam.specificationModel = PartsAccountSearch.specificationModel;
        PartsAccountSearch.searchParam.manageDeptId = PartsAccountSearch.manageDeptId;
        PartsAccountSearch.searchParam.manageDeptOrgseq = PartsAccountSearch.manageDeptOrgseq;
        PartsAccountSearch.searchParam.manageDeptType = PartsAccountSearch.manageDeptType ;//责任部门类型
	     var searchParam=PartsAccountSearch.searchParam;
	     searchParam.isNewParts = PartsAccountSearch.isNewParts;
	     searchParam=MyJson.deleteBlankProp(searchParam);
//	     var sqlStr = " parts_status not in('已报废') or parts_status is null";
//		 var whereList = [
//		          		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
//					]
		 var whereList = [];
	     for(prop in searchParam){
	     	if(prop=='partsStatus' && searchParam[prop]!=null){
	     		var value = searchParam[prop];
	     		if (value == 'ROOT_0') {
	     			var value = "";
	     		}
	     		whereList.push({propName:prop,propValue:value,compare:Condition.LLIKE});
	     		continue;
	     	}
	     	if(prop=='unloadTrainTypeIdx' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
	     		continue;
	     	}
	     	if(prop=='aboardTrainTypeIdx' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ,stringLike:false});
	     		continue;
	     	}
	     	if(prop=='specificationModel' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if('partsStatusUpdateDate' == prop){
			 	var getDateVal_v = searchParam[prop];
			 	getDateVal_v = getDateVal_v.toString();
			 	var getDateVal = getDateVal_v.split(",");
			 	if(getDateVal instanceof Array){
			 		if(getDateVal.length == 2 ){
			 			if(getDateVal[1] != "" && getDateVal[1] != "undefind"){
			 				whereList.push({propName:'partsStatusUpdateDate',propValue:getDateVal[0],compare:4});
			 				whereList.push({propName:'partsStatusUpdateDate',propValue:getDateVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'partsStatusUpdateDate',propValue:getDateVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'partsStatusUpdateDate',propValue:getDateVal[0]+' 23:59:59',compare:6});
			 		}
	                } 
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
	
	PartsAccountSearch.searchForm=new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsAccountSearch.labelWidth,
	    buttonAlign:"center",
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	             columnWidth:.25,
			     layout:'form',labelWidth: PartsAccountSearch.labelWidth,
			     baseCls:'x-plain',
			     items:[{
			     	xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
			     }]
			 },{	
			     columnWidth:.25,labelWidth: PartsAccountSearch.labelWidth,
			     layout:'form',
			     baseCls:'x-plain',
			     items:[{
		     	  	  	id:"trainType_comb_s",
					    fieldLabel: "下车车型",
						xtype: "Base_combo",
						hiddenName: "unloadTrainTypeIdx",
					    business: 'trainVehicleType',
					    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		                fields:['idx','typeName','typeCode','vehicleType'],
		                returnField: [{widgetId:"PartsFixRegister_aboardTrainType",propertyName:"typeCode"}],
		                queryParams: {},// 表示客货类型
		    		    displayField: "typeCode", valueField: "idx",
		                pageSize: 20, minListWidth: 200,width:140,
		                editable:false,					  
						listeners : {   
				        	"select" : function(combo,record,index) {   
				                //重新加载修程下拉数据
				                var rc_comb_s = Ext.getCmp("rc_comb_s");
				                var vehicleType = record.data.vehicleType ;
				                rc_comb_s.reset();
				                rc_comb_s.clearValue();
				                rc_comb_s.getStore().removeAll();
				                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
				                rc_comb_s.cascadeStore();
				        	}   
				    	}
	                }]
	          },{
			     columnWidth:.25,
			     layout:'form',labelWidth: PartsAccountSearch.labelWidth,
			     baseCls:'x-plain',
			     items:[
			     		{xtype:'textfield',fieldLabel:'下车车号',name:"unloadTrainNo"}]
			  },{
			     columnWidth:.25,
			     layout:'form',labelWidth: PartsAccountSearch.labelWidth,
			     baseCls:'x-plain',
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
						width: 140,
						editable:false
					}]
	           }]
	    },{ 
	    	xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	             columnWidth:.25, labelWidth: PartsAccountSearch.labelWidth,
			     layout:'form',
			     baseCls:'x-plain',
			     items:[{
			     	xtype: 'Base_comboTree', hiddenName:'partsStatus',id:'comboTree_select',isExpandAll: true,//hiddenName: 'partsStatusBill',
				    fieldLabel:'配件状态',returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'all',width:125,minListWidth:200,
				    treeUrl: ctx + '/eosDictEntrySelect!tree.action',  queryParams: {'dicttypeid':'PJWZ_PARTS_ACCOUNT_STATUS'},
				    rootId: 'ROOT_0', rootText: '所有状态'}]
		    },{	
			     columnWidth:.25, labelWidth: PartsAccountSearch.labelWidth,
			     layout:'form',
			     baseCls:'x-plain',
			     items:[{
		     	  	  	id:"trainType_comb_x",
					    fieldLabel: "上车车型",
						xtype: "Base_combo",
						hiddenName: "aboardTrainTypeIdx",
					    business: 'trainVehicleType',
					    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		                fields:['idx','typeName','typeCode','vehicleType'],
		                returnField: [{widgetId:"PartsFixRegister_aboardTrainType",propertyName:"typeCode"}],
		                queryParams: {},// 表示客货类型
		    		    displayField: "typeCode", valueField: "idx",
		                pageSize: 20, minListWidth: 200,width:140,
		                editable:false,							  
						listeners : {   
				        	"select" : function(combo,record,index) {   
				                //重新加载修程下拉数据
				                var rc_comb_x = Ext.getCmp("rc_comb_x");
				                var vehicleType = record.data.vehicleType ;
				                rc_comb_x.reset();
				                rc_comb_x.clearValue();
				                rc_comb_x.getStore().removeAll();
				                rc_comb_x.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
				                rc_comb_x.cascadeStore();
				        	}   
				    	}
	                }]
            },{
			     columnWidth:.25,
			     layout:'form',labelWidth: PartsAccountSearch.labelWidth,
			     baseCls:'x-plain',
			     items:[{
		     		xtype:'textfield',fieldLabel:'上车车号',name:"aboardTrainNo"}]
		     },{
			     columnWidth:.25,
			     layout:'form',labelWidth: PartsAccountSearch.labelWidth,
			     baseCls:'x-plain',
			     items:[{
					id:"rc_comb_x",
					xtype: "Base_combo",
					business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
					fieldLabel: "上车修程",
					hiddenName: "aboardRepairClassIdx", 
//					returnField: [{widgetId:"unloadRepairClass",propertyName:"xcName"}],
					displayField: "xcName",
					valueField: "xcID",
					pageSize: 20, minListWidth: 200,
					queryHql: 'from UndertakeRc',
					width: 140,
					editable:false			 
				}]
	        }]
		},{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [{
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:100,
	            columnWidth: 0.5, 
	            items: [{
					xtype: 'compositefield', fieldLabel : '状态修改日期', combineErrors: false,
		        	items: [
			           { id:"partsStatusUpdateDate",name: "partsStatusUpdateDate",  xtype: "my97date",format: "Y-m-d",  
			             value : PartsAccountSearch.getCurrentMonth(),
						width: 150},
						{
				    	    xtype:'label', text: '结束：'
					    },
						{ id:"partsStatusUpdateDate_end",name: "partsStatusUpdateDate", xtype: "my97date",format: "Y-m-d",  
						width: 150}]
				}]
	        },{		        
			    columnWidth:.25,
			    layout:'form', labelWidth: PartsAccountSearch.labelWidth,
			    baseCls:'x-plain',
			    items:[{
					xtype: 'compositefield', fieldLabel : '是否新品', combineErrors: false,
		        	items: [{   
					    xtype:'checkbox', name:'isNewParts', id: 'isNewParts_new', boxLabel:'新&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	inputValue:'新', checked:true,
						    handler: function(){
						    	PartsAccountSearch.checkQuery();
						    }
					  },{   
					    xtype:'checkbox', name:'isNewParts', id: 'isNewParts_old', boxLabel:'旧&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	inputValue:'旧',checked:true,
						    handler: function(){
						    	PartsAccountSearch.checkQuery();
						    }
					  }]
				}]
	      },{
			  columnWidth:.25,
		      layout:'form',labelWidth: PartsAccountSearch.labelWidth,
		      baseCls:'x-plain',
		      items:[{
		     	 xtype:'textfield',fieldLabel:'配件识别码',id:'identificationCode',name:"identificationCode"
		      }]	   	
		   }]
	    }],
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsAccountSearch.searchParam = PartsAccountSearch.searchForm.getForm().getValues();
            	PartsAccountSearch.searchParam.specificationModel = PartsAccountSearch.specificationModel;
            	PartsAccountSearch.searchParam=MyJson.deleteBlankProp(PartsAccountSearch.searchParam);
	 			PartsAccountSearch.grid.searchFn(PartsAccountSearch.searchParam);
            }
        },{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	PartsAccountSearch.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb_s").clearValue();
            	Ext.getCmp("trainType_comb_x").clearValue();
            	Ext.getCmp("rc_comb_s").clearValue();
            	Ext.getCmp("rc_comb_x").clearValue();
            	Ext.getCmp("rc_comb_s").queryParams = {};
            	Ext.getCmp("rc_comb_x").queryParams = {};
            	Ext.getCmp("rc_comb_s").cascadeStore();
            	Ext.getCmp("rc_comb_x").cascadeStore();            	
            	Ext.getCmp("comboTree_select").setDisplayValue(PARTS_STATUS_ZC,'在册');
		     	PartsAccountSearch.manageDeptId = "";
		        PartsAccountSearch.manageDeptOrgseq = "";
		        PartsAccountSearch.manageDeptType = "" ;
		        PartsAccountSearch.specificationModel = "";
            	PartsAccountSearch.searchParam = {};
			    PartsAccountSearch.grid.store.load();
            }
        }]
	});
	var viewport=new Ext.Viewport({layout:'fit',items:[{
		    layout:'border',
		    items:[{
	    	   region:'west',
	    	   width:240,
	    	   layout:'fit',
	    	   items:[PartsManageDeptTree.panel]
	    	},{
	    	   region:'center',
	    	   layout:'border',
	    	   frame:true,
	    	   items:[{
	    	      region:'north',
	    	      collapsible :true,
	    	      height:160,
	    	      title:'查询',
	    	      items:[PartsAccountSearch.searchForm]
	    	   },{
	    	      region:'center',
	    	      border:false,
	    	      frame:true,
	    	      layout:'fit',
	    	      items:[PartsAccountSearch.grid]
	    	   }]
	    	}]
			
		}]
		});
	PartsAccountSearch.init = function(){
		Ext.getCmp("comboTree_select").setDisplayValue(PARTS_STATUS_ZC,'在册');
	};
	PartsAccountSearch.init();
});