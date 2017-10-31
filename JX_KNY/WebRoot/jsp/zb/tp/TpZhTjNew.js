Ext.onReady(function(){
	Ext.namespace('TpZhTj');                       //定义命名空间
	TpZhTj.searchParam = {};
	/*** 查询表单 start ***/
	TpZhTj.searchLabelWidth = 90;
	TpZhTj.searchAnchor = '95%';
	TpZhTj.searchFieldWidth = 270;
	//最近一个月
	TpZhTj.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	TpZhTj.searchForm = new Ext.form.FormPanel({
		layout:"form", border:false, style:"padding:10px" ,
		labelWidth: TpZhTj.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: TpZhTj.searchFieldWidth, labelWidth: TpZhTj.searchLabelWidth, defaults:{anchor:TpZhTj.searchAnchor},
				items:[{ 
					fieldLabel: "车型",
					hiddenName: "trainTypeIDX",
					displayField: "shortName", valueField: "typeID",
					pageSize: 0, minListWidth: 50,
					editable:true,
					forceSelection: true,
					xtype: "Base_combo",
		        	business: 'trainType',													
					fields:['typeID','shortName'],
					queryParams: {'isCx':'yes'},
					entity:'com.yunda.jx.base.jcgy.entity.TrainType',
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = TpZhTj.searchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},
				{ id:"professionalTypeNameID",fieldLabel: "专业类型", name: 'professionalTypeName', xtype: 'textfield'},
				{ id:"faultFixFullNameID",fieldLabel: "故障位置", name: 'faultFixFullName', xtype: 'textfield'},
				{ id:"faultNameID",fieldLabel: "故障现象", name: 'faultName', xtype: 'textfield'}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: TpZhTj.searchFieldWidth, labelWidth: TpZhTj.searchLabelWidth, defaults:{anchor:TpZhTj.searchAnchor},
				items:[{
					id: "trainNo_comb_search",
					fieldLabel: "车号",
					hiddenName: "trainNo", 
					displayField: "trainNo", valueField: "trainNo",
					pageSize: 20, minListWidth: 50,
					minChars : 1,
					vtype: "numberInt",
					anchor: "95%", 				
					xtype: "Base_combo",
					business: 'trainNo',
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
					fields:["trainNo","makeFactoryIDX","makeFactoryName",
					{name:"leaveDate", type:"date", dateFormat: 'time'},
					"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
					"bId","dId","bName","dName","bShortName","dShortName"],
					queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
					isAll: 'yes',
					editable:true
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: TpZhTj.searchFieldWidth, labelWidth: TpZhTj.searchLabelWidth, defaults:{anchor:TpZhTj.searchAnchor},
				items:[{
					xtype: 'compositefield', fieldLabel : '提票日期', combineErrors: false,
		        	items: [
		            { id:"startDateID",name: "startDate",  xtype: "my97date",format: "Y-m-d",value : TpZhTj.getCurrentMonth(),width: 100},
					{xtype:'label', text: '至：'},
					{ id:"overDateID",name: "overDate", xtype: "my97date",format: "Y-m-d", width: 100}]
				},{
					id : "group_by_fields",
					xtype: 'checkboxgroup',
			        width: 10,  //宽度220
			        columns: 5,  //在上面定义的宽度上展示3列
			        fieldLabel: '汇总条件',
			        items: [
			            {boxLabel: '车号', name: 'trainNo'},
			            {boxLabel: '专业类型', name: 'professionalTypeName'},
			            {boxLabel: '故障位置', name: 'faultFixFullName'},
			            {boxLabel: '故障现象', name: 'faultName'}
			        ]
				},{
					id:'model_type',
					name:'modelType',
					xtype:'combo',
					fieldLabel: "提票类型",
					typeAhead: true,
				    triggerAction: 'all',
				    lazyRender:true,
				    mode: 'local',
				    editable:false,
				    store: new Ext.data.ArrayStore({
				        fields: [
				            'text',
				            'value'
				        ],
				        data: [['全部', null],['临碎修', 1], ['修程修', 2]]
				    }),
				    valueField: 'value',
				    displayField: 'text'
				}]
			}]
		}],
		buttons:[{
				text:'汇总统计', iconCls:'searchIcon', 
				handler: function(){ 
					var form = TpZhTj.searchForm.getForm();	
			        var searchParam = form.getValues();
			        
			        if (Ext.get("trainNo_comb_search").dom.value && Ext.get("trainNo_comb_search").dom.value != '') {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}else{
						searchParam.trainNo = null;
					}
					//重新赋值
					if(Ext.get("professionalTypeNameID").dom.value){
						searchParam.professionalTypeName = Ext.get("professionalTypeNameID").dom.value;
					}else{
						searchParam.professionalTypeName = null;
					}
					if(Ext.get("faultFixFullNameID").dom.value){
						searchParam.faultFixFullName = Ext.get("faultFixFullNameID").dom.value;
					}else{
						searchParam.faultFixFullName = null;
					}
					if(Ext.get("faultNameID").dom.value){
						searchParam.faultName = Ext.get("faultNameID").dom.value;
					}else{
						searchParam.faultName = null;
					}
					if(Ext.get("model_type").dom.value){
						searchParam.modelType = Ext.getCmp('model_type').getValue();
					}else{
						searchParam.modelType = null;
					}
					
					TpZhTj.deteilgrid.store.removeAll();
	                searchParam = MyJson.deleteBlankProp(searchParam);
	                var fields = getFields("group_by_fields",2);
	                TpZhTj.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					TpZhTj.grid.store.load({
						params: { entityJson: Ext.util.JSON.encode(searchParam),fields : fields }       
					});
					//清空
					var fields = "";
				}
			},{
	            text: "重置条件", iconCls: "resetIcon", handler: function(){ 
	            	var form = TpZhTj.searchForm;
	            	var searchParam = form.getForm().getValues();
			        searchParam = MyJson.deleteBlankProp(searchParam);
	            	form.getForm().reset();
	            	//清空自定义组件的值
	                var componentArray = ["Base_combo"];
	                for (var j = 0; j < componentArray.length; j++) {
	                	var component = form.findByType(componentArray[j]);
	                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
							for (var i = 0; i < component.length; i++) {
								component[i].clearValue();
							}						
						}	                    
	                }
	                var trainNo_comb = TpZhTj.searchForm.getForm().findField("trainNo");   
	                delete trainNo_comb.queryParams.trainTypeIDX;
	                trainNo_comb.cascadeStore();	
	            	searchParam.trainTypeShortName = null;
	            	searchParam.trainNo = null;
	            	searchParam.faultName = null;
	            	searchParam.trainTypeIDX = null;
	            	searchParam.professionalTypeName = null;
	            	searchParam.faultFixFullName = null;
	            	searchParam.modelType = null;
	            	TpZhTj.deteilgrid.store.removeAll();
	            	fields = "trainNo,professionalTypeName,faultFixFullName,faultName";
	            	
					TpZhTj.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
					TpZhTj.grid.store.load({
						params: { entityJson: Ext.util.JSON.encode(searchParam),fields : fields }       
					});
	            }
			}]
	});
	
	var gridFields = getFields("group_by_fields",1);
	/*** 提票列表 start ***/
	TpZhTj.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tZbglAndFaultTp!findTpPageListByParm.action',//装载列表数据的请求URL
	    page : false,
	    storeAutoLoad: false,
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[],
		fields: gridFields,
		isEdit: false,
		viewConfig:{
			forceFit:true
		},
	    listeners:{  
			//单击  
	       	rowclick:function(grid,row){  
	       		
	       		var form = TpZhTj.searchForm.getForm();	
		        var searchParam = form.getValues();
	            searchParam = MyJson.deleteBlankProp(searchParam);
	            
			    var recordVal  = grid.store.getAt(row);
			    searchParam.trainNo = recordVal.get("trainNo");
			    searchParam.professionalTypeName = recordVal.get("professionalTypeName");
			    searchParam.faultFixFullName = recordVal.get("faultFixFullName");
			    searchParam.faultName = recordVal.get("faultName");
			    searchParam.modelType = Ext.getCmp('model_type').getValue();
	          	TpZhTj.deteilgrid.store.load({
					params: { entityJson: Ext.util.JSON.encode(searchParam) }       
				});
	      	}
   		},
   		//不需要双击显示
		toEditFn : function(){}  
	});
	
	TpZhTj.grid.toEditFn = function(){alert(1)}
	
	TpZhTj.grid.store.on("beforeload", function(){	
		var searchParam = TpZhTj.searchParam;
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		var fields = getFields("group_by_fields",2);
		this.baseParams.fields = fields;
		//隐藏字段
		var fieldsArry = getFields("group_by_fields",1);
		var colModels = TpZhTj.grid.colModel;
		var ele = colModels.config;
		if(fieldsArry.length > 1){
			var j = [];
			for(var i = 1; i < ele.length;i++){
				colModels.setHidden(i,false);
				for(var m = 0; m < fieldsArry.length;m++){
					if(ele[i].header == fieldsArry[m].header){
						j.push(i);
					}
				}
			}
			
			for(var k = 1; k < ele.length;k++){
				colModels.setHidden(k,true);
				for(var q = 0; q < j.length;q++){
					if(k == j[q]){
						colModels.setHidden(k,false);
					}
				}
			}
			j = [];
		}else{
			for(var i = 1; i < ele.length;i++){
				colModels.setHidden(i,false);
			}
		}
		
	});
	
	/*** 提票明细列表***/
	TpZhTj.deteilgrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/tZbglAndFaultTp!findTpDeteailPageListByParm.action',//装载列表数据的请求URL
	    page : false,
	    singleSelect: true,
	    viewConfig: null,
	    tbar:[],
	    storeAutoLoad: false,
	    isEdit: false,
		fields: [
			{header:'车型', dataIndex:'trainTypeShortName'},
			{header:'车号', dataIndex:'trainNo'},
			{header:'提票单号', dataIndex:'faultNoticeCode'},
			{header:'故障发生时间', dataIndex:'faultOccurDate', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'my97date' }},
			{
				header:'检修类型', hidden:true, dataIndex:'repairClass', editor:{  maxLength:20 },
		        renderer: function(v){
		            switch(v){
		                case REPAIRCLASS_SX:
		                    return "碎修";
		                case REPAIRCLASS_lX:
		                    return "临修";
		                default:
		                    return v;
		            }
		        }
			},
			{header:'专业类型', dataIndex:'professionalTypeName',width: 150},
			{header:'提票人', dataIndex:'noticePersonName'},
			{header:'故障位置', dataIndex:'faultFixFullName',width: 150},
			{header:'故障现象', dataIndex:'faultName'},
			{header:'故障描述', dataIndex:'faultDesc',width: 150},
			{header:'销票时间', dataIndex:'handleTime',xtype:'datecolumn', format: "Y-m-d H:i:s"},
			{header:'施修人', dataIndex:'handlePersonName'},
			{header:'处理结果', dataIndex:'repairDesc',renderer: function(value, metaData, record, rowIndex, colIndex, store){
					return value ;
		        },width: 150
			}
			
		],
		//不需要双击显示
		toEditFn : function(){}
	});
	
	/*** 界面布局 start ***/
	TpZhTj.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	        collapsible:true, height: 212, bodyBorder: false,
	        items:[TpZhTj.searchForm], frame: true, title: "查询"
	    },{
	        region : 'center', layout : 'fit', bodyBorder: false, items : [ TpZhTj.grid ]
	    },{
	        region : 'south', layout : 'fit',collapsible:true,frame: true, height: 172, bodyBorder: false, items:[ TpZhTj.deteilgrid ]
	    }]
	};

	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:TpZhTj.panel });
});

//封装数据map，死数据
var dataValue = {
	trainNo : "车号",
	professionalTypeName : "专业类型",
	faultFixFullName : "故障位置",
	faultName : "故障现象"
}

//获取动态字段数组
//cbName:复选框组件名称ID,flag返回标志 1返回数组 2返回字符串拼接
function getFields(cbName,flag){
	//收集复选框的值 trainNo professionalTypeName faultFixFullName faultName
	var fields = {};
	if(flag == 1){
	    fields = [];
	}else if(flag == 2){
		fields = "";
	}
	var cbitems = Ext.getCmp(cbName).getValue();  
	if(cbitems.length > 0){
		for (var i = 0; i < cbitems.length; i++) {  
        	if(flag == 1){
        		var cbName = cbitems[i].name;
        		var header = dataValue[cbName];
        		if(i == cbitems.length-1){
        			var data1 = {
        				header : "车型",
        				dataIndex : "trainTypeShortName"
        			};
	        		fields.push(data1);
	        		
        			var data2 = {
        				header : header,
        				dataIndex : cbName
        			};
	        		fields.push(data2);
	        		
        			var data3 = {
        				header : "次数",
        				dataIndex : "zbthtjCount"
        			};
	        		fields.push(data3);
	        	}else{
	        		var data1 = {
        				header : header,
        				dataIndex : cbName
        			};
	        		fields.push(data1);
	        	}
        	}else if(flag == 2){
        		if(i == cbitems.length-1){
	    	        fields += (cbitems[i].name);  
	        	}else{
	    	        fields += (cbitems[i].name) + ",";  
	        	}
        	}
	    }
	}else{
		if(flag == 1){
			fields = [{header : "车型",dataIndex : "trainTypeShortName"},{header : "车号",dataIndex : "trainNo"},{header : "专业类型",dataIndex : "professionalTypeName"},{header : "故障位置",dataIndex : "faultFixFullName"},{header : "故障现象",dataIndex : "faultName"},{header : "次数",dataIndex : "zbthtjCount"}];
		}else if(flag == 2){
			fields = "trainNo,professionalTypeName,faultFixFullName,faultName";
		}
	}
	return fields;
}
