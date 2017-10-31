/**
 * 机车出入段台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainAccessAccount');                       
	TrainAccessAccount.searchParam = {};
	TrainAccessAccount.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请稍侯..." });
	/*** 确定入段去向 start ***/
	
	TrainAccessTogo.saveWin = new Ext.Window({
	    title:"确定机车入段去向", width: (TrainAccessTogo.labelWidth + TrainAccessTogo.fieldWidth + 8) * 2 + 60, 
	    plain:true, closeAction:"hide", items:TrainAccessTogo.saveForm
	});
	
	TrainAccessTogo.showWin = function(data) {
		var form = TrainAccessTogo.saveForm.getForm();
		form.reset();
		form.findField("trainNo").setDisplayValue(data.trainNo, data.trainNo);
		form.findField("trainTypeShortName").setDisplayValue(data.trainTypeShortName, data.trainTypeShortName);
	//	if (!Ext.isEmpty(dictName) && isZB)
	//		form.findField("toGo").setDisplayValue(TRAINTOGO_ZB, dictName);
		
		if (Ext.isEmpty(data.trainTypeShortName)) {
			form.findField("trainTypeShortName").enable();
		} else {
			form.findField("trainTypeShortName").disable();
		}
		
		if (Ext.isEmpty(data.trainNo)) {
			form.findField("trainNo").enable();
		} else {
			form.findField("trainNo").disable();
		}
		form.findField("idx").setValue(data.idx);
		form.findField("trainTypeIDX").setValue(data.trainTypeIDX);
		form.findField("inTime").setValue(new Date(data.inTime).format('Y-m-d H:i:s'));
		if (data.planOutTime != null)
			form.findField("planOutTime").setValue(new Date(data.planOutTime).format('Y-m-d H:i:s'));
			
		//林欢 20160802 可以修改入段去向，如果是机车交验则不允许，修改的时候，如果是不需生成整备单的去向，则逻辑删除作业工单（整备任务单）、整备单，提票状态更改为初始化，提票已处理时所填写数据均清空
		if (data.toGo)
		form.findField("toGo").setDisplayValue(data.toGo, EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",data.toGo));
		form.findField("toGo").enable();
		TrainAccessTogo.saveForm.buttons[1].setVisible(true);
		TrainAccessTogo.saveWin.show();
	}
	TrainAccessTogo.afterSaveSuccessFn = function(result, response, options) {
		TrainAccessAccount.grid.store.reload();
	    alertSuccess();
	    TrainAccessTogo.saveWin.hide();
	}
	TrainAccessTogo.afterSaveFailFn = function(result, response, options) {
		TrainAccessAccount.grid.store.reload();
	    alertFail(result.errMsg);
	    TrainAccessTogo.saveWin.hide();
	}
	/*** 确定入段去向 end ***/
	
	/*** 查询表单 start ***/
	TrainAccessAccount.searchLabelWidth = 120;
	TrainAccessAccount.searchAnchor = '95%';
	TrainAccessAccount.searchFieldWidth = 270;
	
	/** 获取当前日期及上个月当天的日期*/
	TrainAccessAccount.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay = new Date(currentYear,currentMonth-1,currentDay);
		if(arg == 'begin'){
			return MonthFirstDay.format('Y-m-d');
		}
		else if (arg == 'end'){
			return Nowdate.format('Y-m-d');
		}
	}
	TrainAccessAccount.searchForm = new Ext.form.FormPanel({
		layout:"form", border:false, 
		style:"padding:10px" ,
		labelWidth: TrainAccessAccount.searchLabelWidth, align:'center',baseCls: "x-plain",
		defaults:{anchor:"98%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
			items:[{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: TrainAccessAccount.searchFieldWidth, labelWidth: TrainAccessAccount.searchLabelWidth, defaults:{anchor:TrainAccessAccount.searchAnchor},
				items:[{ 
    				fieldLabel: "车型",
    				xtype: "Base_combo",
    				hiddenName: "trainTypeIDX",
    			    business: 'trainVehicleType',
    			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                    fields:['idx','typeName','typeCode'],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "typeCode", valueField: "idx",
                    pageSize: 20, minListWidth: 200,
                    disabled:false,
                    editable:false,					
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = TrainAccessAccount.searchForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.vehicleType = vehicleType ;
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
				},{
					id:"beginDate", fieldLabel: '入段日期(开始)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
					value: TrainAccessAccount.getCurrentMonth('begin'), width:TrainAccessAccount.searchFieldWidth
				}]
			},{
				align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.5,
				fieldWidth: TrainAccessAccount.searchFieldWidth, labelWidth: TrainAccessAccount.searchLabelWidth, defaults:{anchor:TrainAccessAccount.searchAnchor},
				items:[{
					id:"trainNo_comb_search",	
    				fieldLabel: "车号",
    				xtype: "Base_combo",
    				name:'trainNo',
    				hiddenName: "trainNo",
    			    business: 'jczlTrain',
    			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                    fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                    queryParams: {'vehicleType':vehicleType},// 表示客货类型
        		    displayField: "trainNo", valueField: "trainNo",
                    pageSize: 20, minListWidth: 200,
                    disabled:false,
                    editable:true
				},{
					id:"endDate", fieldLabel: '入段日期(结束)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
					value: TrainAccessAccount.getCurrentMonth('end'), width:TrainAccessAccount.searchFieldWidth
				}]
			}]
		}],
		buttons:[{
				text:'查询', iconCls:'searchIcon', 
				handler: function(){ 
					var form = TrainAccessAccount.searchForm.getForm();				
					var beginDate = form.findField("beginDate").getValue();
			        var endDate =form.findField("endDate").getValue();
			        if(endDate < beginDate){
			        	MyExt.TopMsg.msg('提示',"入段结束日期不能比入段开始日期早！", false, 1);
	    				return;
			        }
			        var searchParam = form.getValues();
			        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
						searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
					}
	                searchParam = MyJson.deleteBlankProp(searchParam);
					TrainAccessAccount.grid.searchFn(searchParam); 
				}
			},{
	            text: "重置", iconCls: "resetIcon", handler: function(){ 
	            	var form = TrainAccessAccount.searchForm;
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
	                var trainNo_comb = TrainAccessAccount.searchForm.getForm().findField("trainNo");   
	                delete trainNo_comb.queryParams.trainTypeIDX;
	                trainNo_comb.cascadeStore();	
	            	searchParam = {};
	            	TrainAccessAccount.grid.searchFn(searchParam);
	            }
			}]
	});
	/*** 查询表单 end ***/
	
	/*** 机车入段列表 start ***/
	TrainAccessAccount.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/trainAccessAccount!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/trainAccessAccount!saveOrUpdateIn.action',             //保存数据的请求URL
	    deleteURL: ctx + '/trainAccessAccount!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:2,
	    viewConfig:null,
	    tbar: [{
	    	text:"车辆入段", iconCls:"addIcon", handler: function() {
	    		var grid = TrainAccessAccount.grid;
	    		if(grid.beforeAddButtonFn() == false)   return;
		        //判断新增删除窗体是否为null，如果为null则自动创建后显示
		        if(grid.saveWin == null)  grid.createSaveWin();
		        if(grid.searchWin)  grid.searchWin.hide();
		        if(grid.saveWin.isVisible())    grid.saveWin.hide();
		        if(grid.beforeShowSaveWin() == false)   return;
		        
		        grid.saveWin.setTitle('车辆入段');
		        grid.saveWin.show();
		        grid.saveForm.getForm().reset();
		        grid.saveForm.getForm().setValues(grid.defaultData);
		        grid.saveWin.buttons[0].setText("确认入段");
		        grid.afterShowSaveWin();
	    	}
	    }, 'delete','refresh',
	    {
	    	text:"车辆批量入段", iconCls:"addIcon", handler: function() {
	    		var grid = TrainAccessAccount.grid;
	    		TrainAccessAccountGroupInPlan.showWin(grid);
	    	}
	    }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'出段', dataIndex:'idx',
			searcher: { disabled : true },
			editor: { xtype:'hidden' },
			renderer : function(v, celmeta, record, rowIndex) {
				return ['<input type="button" value="确认出段" onClick="TrainAccessAccount.showTrainOutWin('
						+ rowIndex + ')"></button>'].join('');		
			}, width:100		
		},{
			header:'车型编码', dataIndex:'trainTypeIDX', editor:{ 
				id:"trainType_comb",
				fieldLabel: "车型",
				xtype: "Base_combo",
				hiddenName: "trainTypeIDX",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode'],
                returnField:[{widgetId:"trainTypeShortName",propertyName:"typeCode"}],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:false,	
                allowBlank: false,
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = Ext.getCmp("trainNo_comb");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.vehicleType = vehicleType ;
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
			}, hidden:true
		},{
			header:'车型', dataIndex:'trainTypeShortName', 
			editor:{id:'trainTypeShortName', name:'trainTypeShortName',xtype:'hidden' },
			searcher:{xtype: 'textfield'}, width: 60
		},{
			header:'车号', dataIndex:'trainNo', editor:{
				id:"trainNo_comb",	
				fieldLabel: "车号",
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:true,				
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
						if(trainTypeIdx == "" || trainTypeIdx == null){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
			},
			searcher:{xtype: 'textfield'}, width: 90
		},{
			header:'车辆别名', dataIndex:'trainAliasName', editor:{ xtype:'hidden'},
			searcher:{disabled: true}, width: 120
		},{
			header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
			editor:{ xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'},allowBlank: false },
			searcher:{disabled: true}, width: 150
		},{
			header:'入段去向', dataIndex:'toGo', editor:{
				xtype: 'Base_comboTree',hiddenName: 'toGo',
				fieldLabel: '入段去向',
				allowBlank: false,
				treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
				queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
				selectNodeModel: 'leaf'
			},
			renderer : function(v){
				if (Ext.isEmpty(v))
					return "";
				return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
			},
			searcher:{xtype: 'textfield'}, width: 90
		},{
			header:'当前状态', dataIndex:'trainStatus', editor:{ xtype:'hidden' },
			searcher:{disabled: true}, width: 90
		},{
			header:'状态更改时间', dataIndex:'startTime', xtype:'datecolumn', format: "Y-m-d H:i:s", editor:{ xtype:'hidden' },
			searcher:{disabled: true}, width: 150
		},{
			header:'计划出段时间', dataIndex:'planOutTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
			editor:{ xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false  },
			searcher:{disabled: true}, width: 150
		},{
			header:'color', dataIndex:'color', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'客货类型', dataIndex:'vehicleType', hidden: true,editor:{ xtype:'hidden',value:vehicleType }
		}],
		editOrder:[
			'trainTypeIDX','trainNo',
			'toGo',
			'repairClassIDX','ctfx','inTime',
			'inDriver','arriveOrder',	
			'planOrder','planOutTime'	
		],
		beforeDeleteFn: function(){  
			var data = TrainAccessAccount.grid.selModel.getSelections();
			for (var i = 0; i < data.length; i++){
		        if (!Ext.isEmpty(data[ i ].get("toGo"))) {
		        	MyExt.Msg.alert("不能删除有入段去向的记录");
		        	return false;
		        }
		    }
	        return true;
	    },
	    searchFn: function(searchParam){
	    	TrainAccessAccount.searchParam = searchParam;
	    	this.store.load();
	    },
	    toEditFn: function(grid, rowIndex, e) {
	    	//获取当前行对象
		    var recordV = grid.store.getAt(rowIndex);
	    
	    	var toGo = recordV.data.toGo;//出入段台账idx
	    	var trainAccessAccountIDX = recordV.data.idx;//去向
	    	//exsitToGo(toGo,trainAccessAccountIDX,TrainAccessAccount.showTrainInWin,rowIndex);
	    	TrainAccessAccount.showTrainInWin(rowIndex);
	    },
	    beforeSaveFn: function(data){
			if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_comb").dom.value)) {
				data.trainNo = Ext.get("trainNo_comb").dom.value;
			}
			if(!Ext.isEmpty(data.planOutTime)){
				var planOutTime =new Date(data.planOutTime);
				var inTime =new Date(data.inTime);
				if(planOutTime < inTime){
					MyExt.Msg.alert("计划出段时间不能小于入段时间！");
					return false ;
				}
			}
			var form = this.saveForm.getForm();
			return true; 
		},
		afterSaveSuccessFn: function(result, response, options){
	        TrainAccessAccount.grid.store.reload();
	        alertSuccess();
	        TrainAccessAccount.grid.saveWin.hide();
	    },
	    afterShowSaveWin: function(){
	    //林欢 20160802 可以修改入段去向，如果是机车交验则不允许，修改的时候，如果是不需生成整备单的去向，则逻辑删除作业工单（整备任务单）、整备单，提票状态更改为初始化，提票已处理时所填写数据均清空
	    	var form = TrainAccessAccount.grid.saveForm.getForm();
	    	form.findField("toGo").setDisplayValue(TRAINTOGO_ZB, EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",TRAINTOGO_ZB));
	    	form.findField("toGo").enable();
	    },
	    saveFn: function(){
	        //表单验证是否通过
	        var form = this.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        this.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        this.afterGetFormData();
	        
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!this.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            scope: this, url: this.saveURL, jsonData: data,
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    this.afterSaveSuccessFn(result, response, options);
	                } else {
	                    this.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        //保存入段
	        // checkHasZbFw(cfg);
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    }
	});
	TrainAccessAccount.grid.store.setDefaultSort('inTime', 'DESC');
	TrainAccessAccount.grid.store.on("beforeload", function(){	
		var searchParam = TrainAccessAccount.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        if(!Ext.isEmpty(searchParam[prop]) && searchParam[prop] != " "){
	        	switch(prop){
				 	//入段日期(起) 运算符为">="
				 	case 'beginDate':
				 		whereList.push({propName:'inTime',propValue:searchParam[prop],compare:Condition.GE});
				 		break;
				 	//入段日期(止) 运算符为"<="
				 	case 'endDate':
				 		whereList.push({propName:'inTime',propValue:searchParam[prop]+' 23:59:59',compare:Condition.LE});
				 		break;	 	
				 	//车号查询
	 			 	case 'trainTypeIDX':
				 		whereList.push({propName:'trainTypeIDX',propValue:searchParam[prop],compare:Condition.EQ,stringLike: false});
				 		break;	 
				 	default:
		         		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	        	}
	        }
		}
		var sqlStr = " Out_Time is null and siteID = '" + siteID + "'" ;		
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		whereList.push({propName:'vehicleType',propValue:vehicleType,compare:Condition.EQ,stringLike: false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	TrainAccessAccount.grid.store.on("load",function(r){
		var girdcount = 0;
		this.each(function(r){		
			if (!Ext.isEmpty(r.get('color')))
				TrainAccessAccount.grid.getView().getRow(girdcount).style.backgroundColor = r.get('color');
			girdcount = girdcount + 1;
		});								
	});
	TrainAccessAccount.grid.store.load();
	/*** 机车入段列表 end ***/
	
	/*** 机车入段编辑 start ***/
	TrainAccessAccount.trainInForm = new Ext.form.FormPanel({
	    style: "padding:10px",     frame: true, 	 baseCls: "x-plain", labelAlign:"left", 
	    align: "center",  		   layout:"column",  border:false,
	    defaults: { xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
	    	defaults: {
	    		xtype:"textfield", 
			    labelWidth:TrainAccessAccount.grid.labelWidth, anchor:"98%",
			    defaults: {
			    	width: TrainAccessAccount.grid.fieldWidth
			    }
	    	}
	    },
		items:[{		    	
	    	items:[{
		    		fieldLabel: "车型",
					xtype: "Base_combo",
					hiddenName: "trainTypeShortName",
				    business: 'trainVehicleType',
				    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
	                fields:['idx','typeName','typeCode'],
	                returnField:[{widgetId:"trainTypeIDX_Id",propertyName:"idx"}],
	                queryParams: {'vehicleType':vehicleType},// 表示客货类型
	    		    displayField: "typeCode", valueField: "idx",
	                pageSize: 20, minListWidth: 200,
	                disabled:true,
	                editable:false,	
	                allowBlank: false,
					listeners : {   
			        	"select" : function() {   
			            	//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb_Id");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
			                trainNo_comb.cascadeStore();	
			        	}   
			    	}
			
	    		},{
	    			id:'trainTypeIDX_Id', name:'trainTypeIDX',xtype:'hidden'     		
	    		},{
					xtype: 'Base_comboTree',hiddenName: 'toGo',
					fieldLabel: '入段去向',
					allowBlank: false,
					treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
					queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
					selectNodeModel: 'leaf'
	    		},{
	    			fieldLabel: "计划出段时间", name: "planOutTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
	    		}]
		},{    	
	    	items:[{
				id:"trainNo_comb_Id",		
				fieldLabel: "车号",
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                disabled:true,
                editable:true,				
				allowBlank: false,					
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeShortName =  TrainAccessAccount.trainInForm.getForm().findField("trainTypeShortName").getValue();
						if(Ext.isEmpty(trainTypeShortName)){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
	    		},{
					fieldLabel:'idx主键', name:'idx', hidden:true
				},{
	    			fieldLabel: "入段时间",allowBlank: false, name: "inTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
	    		}]
		}]
	});
	TrainAccessAccount.trainInWin = new Ext.Window({
	    title:"车辆入段编辑", width: (TrainAccessAccount.grid.labelWidth + TrainAccessAccount.grid.fieldWidth + 8) * 2 + 60,
	    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
	    items:TrainAccessAccount.trainInForm, 
	    buttons: [{
	        text: "保存", iconCls: "saveIcon", handler: function() {  
			    var form = TrainAccessAccount.trainInForm.getForm(); 
			    if (!form.isValid()) return;
			    form.findField("trainNo").enable();
				form.findField("trainTypeShortName").enable();
				form.findField("toGo").enable();
			    var data = form.getValues();	
			    if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_comb_Id").dom.value)) {
					data.trainNo = Ext.get("trainNo_comb_Id").dom.value;
				}
				
				if(!Ext.isEmpty(data.planOutTime)){
					var planOutTime =new Date(data.planOutTime);
					var inTime =new Date(data.inTime);
					if(planOutTime < inTime){
						MyExt.Msg.alert("计划出段时间不能小于入段时间！");
						form.findField("trainNo").disable();
						form.findField("trainTypeShortName").disable();
						//form.findField("toGo").disable();
						return false ;
					}
				}
				
			    if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.show();
			    var cfg = {
			        scope: this, url: ctx + '/trainAccessAccount!updateIn.action', jsonData: data,
			        success: function(response, options){
			            if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                TrainAccessAccount.grid.store.reload();
						    alertSuccess();
						    TrainAccessAccount.trainInWin.hide();
			            } else {
			                TrainAccessAccount.grid.store.reload();
	    					alertFail(result.errMsg);
	    					TrainAccessAccount.trainInWin.hide();
			            }
			        }
			    };
				// checkHasZbFw(cfg);
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	        }
	    }, {
	        text: "取消", iconCls: "closeIcon", handler: function(){ TrainAccessAccount.trainInWin.hide(); }
	    }]
	});
	TrainAccessAccount.showTrainInWin = function(rowIndex) {
		var record = TrainAccessAccount.grid.store.getAt(rowIndex);
		TrainAccessAccount.trainInWin.show();
		var form = TrainAccessAccount.trainInForm.getForm();
		form.reset();
		form.loadRecord(record);
		form.findField("trainNo").setDisplayValue(record.get("trainNo"), record.get("trainNo"));
		form.findField("trainTypeShortName").setDisplayValue(record.get("trainTypeShortName"), record.get("trainTypeShortName"));
		form.findField("toGo").setDisplayValue(record.get("toGo"), EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",record.get("toGo")));	
		
		if (Ext.isEmpty(record.get("trainTypeShortName"))) {
			form.findField("trainTypeShortName").enable();
		} else {
			form.findField("trainTypeShortName").disable();
		}
		
		if (Ext.isEmpty(record.get("trainNo"))) {
			form.findField("trainNo").enable();
		} else {
			form.findField("trainNo").disable();
		}
		
		//林欢 20160802 可以修改入段去向，如果是机车交验则不允许，修改的时候，如果是不需生成整备单的去向，则逻辑删除作业工单（整备任务单）、整备单，提票状态更改为初始化，提票已处理时所填写数据均清空
		form.findField("toGo").enable();
	}
	/*** 机车入段编辑 end ***/
	
	/*** 机车出段 start ***/
	TrainAccessAccount.trainOutForm = new Ext.form.FormPanel({
	    style: "padding:10px",     frame: true, 	 baseCls: "x-plain", labelAlign:"left", 
	    align: "center",  		layout:"column", 	 border:false, 		 
	    defaults: { xtype:"container", autoEl:"div", layout:"form", columnWidth:0.5,
	    	defaults: {
	    		xtype:"textfield", 
			    labelWidth:TrainAccessAccount.grid.labelWidth, anchor:"98%",
			    defaults: {
			    	width: TrainAccessAccount.grid.fieldWidth
			    }
	    	}
	    },
		items:[{		    	
	    	items:[{
	    			fieldLabel: "车型", name: "trainTypeShortName", disabled: true
	    		},{
	    			fieldLabel: "出段时间", name: "outTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: true,
	    			allowBlank: false
	    		}]
		},{    	
	    	items:[{
	    			fieldLabel: "车号", name: "trainNo", disabled: true
	    		},{ xtype: 'hidden', name: 'idx'},{
	    			fieldLabel: "入段时间", name: "inTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: true,
	    			allowBlank: false,hidden:true
	    		}]
		}]
	});
	TrainAccessAccount.trainOutWin = new Ext.Window({
	    title:"机车出段", width: (TrainAccessAccount.grid.labelWidth + TrainAccessAccount.grid.fieldWidth + 8) * 2 + 60,
	    plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
	    items:TrainAccessAccount.trainOutForm, 
	    buttons: [{
	        text: "确认出段", iconCls: "saveIcon", handler: function() {        	
			    var form = TrainAccessAccount.trainOutForm.getForm(); 
			    if (!form.isValid()) return;
			    var data = form.getValues();
			    
			    var outTime =new Date(data.outTime);
				var inTime =new Date(data.inTime);
				if(outTime < inTime){
					MyExt.Msg.alert("出段时间不能小于入段时间！");
					return false ;
				}
			    
			    if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.show();
			    var cfg = {
			        scope: this, url: ctx + '/trainAccessAccount!saveOrUpdateOut.action', jsonData: data,
			        success: function(response, options){
			            if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            if (result.errMsg == null) {
			                TrainAccessAccount.grid.store.reload();
						    alertSuccess();
						    TrainAccessAccount.trainOutWin.hide();
			            } else {
			                TrainAccessAccount.grid.store.reload();
	    					alertFail(result.errMsg);
	    					TrainAccessAccount.trainOutWin.hide();
			            }
			        }
			    };
			    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
	        }
	    }, {
	        text: "取消", iconCls: "closeIcon", handler: function(){ TrainAccessAccount.trainOutWin.hide(); }
	    }]
	});
	TrainAccessAccount.showTrainOutWin = function(rowIndex) {
		var record = TrainAccessAccount.grid.store.getAt(rowIndex);	
		var form = TrainAccessAccount.trainOutForm.getForm();
		form.findField("trainTypeShortName").setValue(record.get("trainTypeShortName"));
		form.findField("trainNo").setValue(record.get("trainNo"));
		form.findField("idx").setValue(record.get("idx"));
		form.findField("outTime").setValue(new Date());
		form.findField("inTime").setValue(record.get("inTime"));
		TrainAccessAccount.trainOutWin.show();
		
	}
	/*** 机车出段 end ***/
	
	/*** 界面布局 start ***/
	TrainAccessAccount.panel = {
	    xtype: "panel", layout: "border", 
	    items: [{
	        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
	        collapsible:true, height: 172, bodyBorder: false,
	        items:[TrainAccessAccount.searchForm], frame: true, title: "查询"
	    },{
	        region : 'center', layout : 'fit', bodyBorder: false, items : [ TrainAccessAccount.grid ]
	    }]
	};
	var viewport = new Ext.Viewport({ layout:'fit', items:TrainAccessAccount.panel });
});

//点击保存或者确认入段前的判断
//cfg 调用入段的配置项
//Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
function checkHasZbFw(cfg){
	//判断入段去向是否是需要生成整备单
	var trainNo = cfg.jsonData.trainNo;//车号
	var trainTypeIDX = cfg.jsonData.trainTypeIDX;//车型
	var trainTypeShortName = cfg.jsonData.trainTypeShortName;//车型简称
	var toGo = cfg.jsonData.toGo;//去向
	
	Ext.Ajax.request({
		url: ctx + '/trainAccessAccount!existNeedToDoZbRdp.action',
		params: {toGo:toGo},
		success: function(r){
			var retn = Ext.util.JSON.decode(r.responseText);
			if(retn.success){
				//如果返回true表示需要生成整备单，此处继续下面选项
				Ext.Ajax.request({
					url: ctx + '/zbfwTrainCenter!findZbfwTrainCenterByTrainNoAndTrainTypeIDX.action',
					params: {trainNo:trainNo,trainTypeIDX:trainTypeIDX},
					success: function(response, options){
						//取消刷圈
						if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
			            var result = Ext.util.JSON.decode(response.responseText);
			            //如果通过车型车号能找到中间表与之对应，那么直接入段操作，无需提示
			            if (result.zbfwTrainCenter) {
			            	//取消刷圈
							Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
			            } else {
			            	//如果没有找到，此车未维护范围，查看此车型是否有维护整备范围
						 	Ext.Ajax.request({
								url: ctx + '/zbFw!getZbFwByTrain.action',
								params: {trainTypeIDX:trainTypeIDX},
								success: function(response, options){
						            var result = Ext.util.JSON.decode(response.responseText);
						            //如果通过车型可以找到对应的范围
						            if (result.zbFwList.length > 0) {
						            	Ext.Msg.show({
						            		title:'提示',
						            		msg:'该车型车号（'+trainNo+'）未配置整备范围，请做选择！',
						            		buttons:{
						            			'yes':'选择整备范围',
						            			'no':'直接入段'
						            		},
						            		fn:function callback(btn){
										        if(btn == 'yes')   {
										        	//车型idx
													ZbfwChoiceFormWin.trainTypeIDX = trainTypeIDX;
													//车型简称
													ZbfwChoiceFormWin.trainTypeShortName = trainTypeShortName
													//车号
													ZbfwChoiceFormWin.trainNo = trainNo;
													ZbfwChoiceFormWin.cfg = cfg;
													
													//重载下拉
													var zbfw_comb = Ext.getCmp("zbfw_comb");  
													zbfw_comb.queryParams.trainTypeIDX = trainTypeIDX; 
													zbfw_comb.cascadeStore();
													//为lable赋值
													Ext.getCmp("trainTypeShortName_idx").setText(trainTypeShortName);
													ZbfwChoiceFormWin.showWin();
										        }else{
										        	//取消刷圈
													if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
										        	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));  
										        }
										    }
						            	});
						            } else {
						            	//车型也未维护，直接入段操作
						            	//取消刷圈
										if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
						            	Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));  
						            }
						        }
							});
			            }
			        }
				});
			}else{
				//取消刷圈
				if(TrainAccessAccount.loadMask)   TrainAccessAccount.loadMask.hide();
				//如果返回false，表示不需要生成整备单，直接入段即可
				Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
			}
		},
		failure: function(){
			alertFail("请求超时！");
		}
	});
}
