Ext.onReady(function(){
	Ext.namespace('PartsFixRegisterSearch');  //定义命名空间
	
	PartsFixRegisterSearch.fieldWidth = 160;
	PartsFixRegisterSearch.labelWidth = 80;
	PartsFixRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsFixRegisterSearch.callReturnFn=function(node,e){
	  PartsFixRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  PartsFixRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	 //获取当前日期
	PartsFixRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	//查询Form
	PartsFixRegisterSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsFixRegisterSearch.labelWidth,
	    buttonAlign:"center",
	    defaults:{
	      xtype: "panel",
	      border: false,
	      baseCls: "x-plain",
	      layout: "column",
	      align: "center", 
	      	defaults:{
		      	 baseCls:"x-plain", 
		      	 align:"center",
		      	 layout:"form",
		      	 defaultType:"textfield", 
		         columnWidth: 0.25
	      	}
	    },
	    items: [{
	        items: [{
	            items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: PartsFixRegisterSearch.callReturnFn
                  	},{ 
                  		id:"partsTypeIDX_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
                 	},{ 
                  		id:"partsNo", name:"partsNo", fieldLabel:"配件编号"
                 	}]
	        	},{
	            items:[{ 
	            		id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	            	},{ 
	            		id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	           	   }]
	       		},{
	            items:[{ 
	                    id:"trainType_comb",
					    fieldLabel: "上车车型",
						xtype: "Base_combo",
						hiddenName: "aboardTrainTypeIdx",
					    business: 'trainVehicleType',
					    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
		                fields:['idx','typeName','typeCode','vehicleType'],
		                returnField: [{widgetId:"PartsFixRegister_aboardTrainType",propertyName:"typeCode"}],
		                queryParams: {},// 表示客货类型
		    		    displayField: "typeCode", valueField: "idx",
		                pageSize: 20, minListWidth: 200,
		                editable:false,					   
					    listeners : {   
					        	"select" : function() {   
					            	//重新加载车号下拉数据
					                var trainNo_comb = Ext.getCmp("trainNo_comb");   
					                trainNo_comb.reset();  
					                trainNo_comb.clearValue(); 
					                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
					                trainNo_comb.cascadeStore();
					        	}   
					    	}
		          },{ 
		             id:"PartsFixRegister_aboardTrainType",xtype:"hidden", name:"aboardTrainType"
		          },{
					xtype: 'compositefield', fieldLabel : '上车日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'aboardDate', id: 'startDate_d', format:'Y-m-d', value: PartsFixRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
						my97cfg: {maxDate: '#F{$dp.$D(\'endDate_d\')}'}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height: 23px;line-height:23px;'
					}, {
						xtype:'my97date', name: 'aboardDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
						my97cfg: {minDate: '#F{$dp.$D(\'startDate_d\')}'}
					}]
		         }]
	        },{
	            items: [{
	            		id:"trainNo_comb",
	            		fieldLabel: "上车车号",
	    				xtype: "Base_combo",
	    				name:'trainNo',
	    				hiddenName: "aboardTrainNo",
	    			    business: 'jczlTrain',
	    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
	                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
	                    queryParams: {},// 表示客货类型
	        		    displayField: "trainNo", valueField: "trainNo",
	                    pageSize: 20, minListWidth: 200,
	                    disabled:false,
	                    editable:true,		
						listeners:{
		    				"beforequery" : function(){
		                		
		                		}
		    			}
					}]
	          }]
	     }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsFixRegisterSearch.searchParam = PartsFixRegisterSearch.searchForm.getForm().getValues();
			    PartsFixRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsFixRegisterSearch.searchParam);
			    PartsFixRegisterSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                PartsFixRegisterSearch.searchForm.getForm().reset();
            	Ext.getCmp("trainType_comb").clearValue();
            	Ext.getCmp("trainNo_comb").clearValue();
            	PartsFixRegisterSearch.searchParam = {};
			    PartsFixRegisterSearch.grid.store.load();
            }
         }]
	});
	
	PartsFixRegisterSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsFixRegister!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig : [],
    tbar: ['refresh'],
	fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'检修任务单主键', dataIndex:'rdpIdx', hidden:true
		},{
			header:'检修任务单类型', dataIndex:'rdpType'
		},{
			header:'配件识别码', dataIndex:'identificationCode'
		},{
			header:'规格型号', dataIndex:'specificationModel', width:150
		},{
			header:'配件名称', dataIndex:'partsName'
		},{
			header:'配件编号', dataIndex:'partsNo', width:120
		},{
			header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true
		},{
			header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true
		},{
			header:'上车人主键', dataIndex:'fixEmpId',hidden:true
		},{
			header:'上车人', dataIndex:'fixEmpId'
		},{
			header:'是否范围内下车', dataIndex:'isInRange'
		},{
			header:'上车车型', dataIndex:'aboardTrainType'
		},{
			header:'上车车型编码', dataIndex:'aboardTrainTypeIdx',hidden:true
		},{
			header:'上车车号', dataIndex:'aboardTrainNo'
		},{
			header:'上车车型', dataIndex:'aboardTrainType'
		},{
			header:'上车修程编码', dataIndex:'aboardRepairClassIdx',hidden:true
		},{
			header:'上车修程', dataIndex:'aboardRepairClass'
		},{
			header:'上车修次编码', dataIndex:'aboardRepairTimeIdx',hidden:true
		},{
			header:'上车位置', dataIndex:'aboardPlace'
		},{
		    header:'上车日期', dataIndex:'aboardDate',xtype:'datecolumn'
		},{
			header:'单据状态', dataIndex:'status',hidden:true
		},{
			header:'生产厂家主键', dataIndex:'madeFactoryIdx',hidden:true
		},{
			header:'生产厂家', dataIndex:'madeFactoryName', width:150
		},{
			header:'详细配置', dataIndex:'configDetail', width:150
		},{
			header:'创建人名称', dataIndex:'creatorName', hidden:true
		}]
	});
	
	//移除监听
	PartsFixRegisterSearch.grid.un('rowdblclick', PartsFixRegisterSearch.grid.toEditFn, PartsFixRegisterSearch.grid);
	
	//查询前添加过滤条件
	PartsFixRegisterSearch.grid.store.on('beforeload',function(){
		PartsFixRegisterSearch.searchParam = PartsFixRegisterSearch.searchForm.getForm().getValues();
		PartsFixRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsFixRegisterSearch.searchParam);
		var searchParam = PartsFixRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('aboardDate' == prop){
			 	var whTimeVal_v = searchParam[prop];
			 	whTimeVal_v = whTimeVal_v.toString();
			 	var whTimeVal = whTimeVal_v.split(",");
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'aboardDate',propValue:whTimeVal[0],compare:4});
			 				whereList.push({propName:'aboardDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'aboardDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'aboardDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
			whereList.push({propName:prop,propValue:searchParam[prop]});
		}
	    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	var viewport=new Ext.Viewport({
	        layout:'fit',
	        items:[{
		    layout:'border',frame:true,
		    items:[{
		       	region:'north',
		       	collapsible :true,
		       	title:'查询',
			   	height:150,
		       	frame:true,
		       	items:[PartsFixRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsFixRegisterSearch.grid]
		    }]
		 }]
	 });
});