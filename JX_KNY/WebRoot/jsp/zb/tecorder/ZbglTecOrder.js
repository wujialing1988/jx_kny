/**
 * 技术指令及措施 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglTecOrder');  //定义命名空间

	/** 获取最近的一个月 */
	var dateNow = new Date();
	var month = dateNow.getMonth();
	dateNow.setMonth(month-1);
	var lastMonth = dateNow.format('Y-m-d');
	/* ************* 定义全局变量开始 ************* */
	ZbglTecOrder.labelWidth = 100;
	ZbglTecOrder.fieldWidth = 140;
	ZbglTecOrder.isAddAndNew = false; 
	/* ************* 定义全局变量结束 ************* */
	
	/* ************* 未销号查询Form开始 ************* */
	ZbglTecOrder.searchForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
				layout:"form",
				columnWidth:0.33,
				defaults:{
					xtype:"textfield", width: ZbglTecOrder.fieldWidth
				}
		},
		items:[{
			items:[
				   {
    				id:"trainType_combo",	
    				fieldLabel: "车型",
    				hiddenName: "trainTypeIDX",
    				xtype: "Base_combo",
    			    business: 'trainType',
    			    entity:'com.yunda.jx.base.jcgy.entity.TrainType',
                    fields:['typeID','shortName'],
                    queryParams: {'isCx':'yes'},
        		    displayField: "shortName", valueField: "typeID",
                    pageSize: 0, minListWidth: 200,
                    editable:true
        		},{
					xtype: 'compositefield', fieldLabel: '发布日期', combineErrors: false, width:222,
					items: [{
						xtype:'my97date', name: 'releaseTime', id: 'startDate_d', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},value: lastMonth, width: 90, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "开始日期不能大于结束日期";
							}
						}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {							
						xtype:'my97date', name: 'toreleaseTime', id: 'endDate_d', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},width: 90, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "结束日期不能小于开始日期";
							}
						}
					}]
			   }
			]
		},{
			items:[{
    			fieldLabel: "车号",
    			name: "trainNo"
			}]
		}, {
			items:[{
		    fieldLabel: '指令状态',
		    id: 'zl_combo',
            name: 'orderStatus',
            xtype: 'combo',
            hiddenName: 'orderStatus', 
            displayField:'v',
            valueField:'t',
            store:new Ext.data.SimpleStore({
                        fields: ['v', 't'],
                        data : [
                                [STATUS_NEW_CH,STATUS_NEW],
                                [STATUS_PUBLISH_CH,STATUS_PUBLISH]
                               ]
                    }),
            triggerAction:'all',
            mode:'local'
         }]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			 var form = ZbglTecOrder.searchForm.getForm();
						if (form.isValid()) {
							ZbglTecOrder.grid.store.load();
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
						}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    ZbglTecOrder.searchForm.getForm().reset();
                    Ext.getCmp('trainType_combo').clearValue();
				    // Ext.getCmp('trainNo_combo').clearValue();
				    // 重新加载表格
				    ZbglTecOrder.grid.store.load();
			}
		}]
	})
	/* ************* 未销号查询Form结束 ************* */
	
	
    /*
	 * 发布，销号操作处理
	 */
	ZbglTecOrder.Operat = function(action, func){
		if(!$yd.isSelectedRecord(ZbglTecOrder.grid)) return;
		var idxs = $yd.getSelectedIdx(ZbglTecOrder.grid);	
		ZbglTecOrder.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + action,
			//params: {ids: idxs + ""},
		    params: {ids: idxs},
			success: function(response, options){
				ZbglTecOrder.grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if(result.success){
	            	alertSuccess();
	            	ZbglTecOrder.grid.store.load();
	            	
	            	if(func) func();	//执行函数
	            }else{
	            	alertFail(result.errMsg);
	            }
			},
	        failure: function(response, options){
				ZbglTecOrder.grid.loadMask.hide();
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
		});
	}
	
	/*
	 * 发布操作
	 */
	ZbglTecOrder.release = function(){
		ZbglTecOrder.Operat("/zbglTecOrder!release.action");
	/*	Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否确认发布？", function(btn){
			if(btn != 'yes')    return;
			
		})*/
	}
	
	/*
	 * 销号操作
	 */
	ZbglTecOrder.cancel = function(){
		ZbglTecOrder.Operat("/zbglTecOrder!cancel.action", function(){
				ZbglTecOrder.cancedGrid.store.load();
			});
/*		Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否确认销号？", function(btn){
			if(btn != 'yes')    return;
			
		});*/
	}
	
  /**
   * 点击查询操作的方法
   */
  ZbglTecOrder.findInfo = function(rowIndex){
  	var record = ZbglTecOrder.grid.store.getAt(rowIndex);
    ZbglTecInfo.showWin(record);
  }
  
   /**
   * 生成临时任务单
   */
  ZbglTecOrder.toRdpOrder = function(){
    if(!$yd.isSelectedRecord(ZbglTecOrder.grid)) return;
    var record = ZbglTecOrder.grid.selModel.getSelections()
    var idxs = $yd.getSelectedIdx(ZbglTecOrder.grid);	
    if(record.length != 1){
	  MyExt.Msg.alert("只能选择一条记录!");
	  return;
    }
    if(record[0].get("orderStatus") == STATUS_NEW){
	  MyExt.Msg.alert("未发布指令不能生成临时任务单");
	  return;
    }
	var tecIDX = record[0].get("idx");
	Ext.Ajax.request({
		url: ctx + "/zbglTecOrder!createLsRdp.action",
		//params:{zbglTecIdx: tecIDX},
		params:{ids: idxs},
		success:function(response, options){
			var result = Ext.util.JSON.decode(response.responseText);
			var msg = result.msg;
			MyExt.Msg.alert(msg);
			ZbglTecOrder.grid.store.load();
		}
	});
  }
//未销号grid
ZbglTecOrder.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTecOrder!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglTecOrder!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglTecOrder!logicDelete.action',            //删除数据的请求URL
    viewConfig:true,
    saveFormColNum:1,	searchFormColNum:1,
    saveWinWidth: 600,
    saveWinHeight:300,
    tbar: ['add','delete',{
    	    text:"发布", iconCls:"pluginIcon",
    	    handler:ZbglTecOrder.release
    	  },{
    	    text:"销号", iconCls:"cmpIcon", 
    	    handler:ZbglTecOrder.cancel 
    	  }, 'refresh','->',{
    	    text:"生成临时任务单", iconCls:"bookIcon",
    	    handler:ZbglTecOrder.toRdpOrder
    	}],
	fields: [{
		header:'查看', editor: { xtype:'textfield' },
		renderer:function(value, metaData, record, rowIndex, colIndex, store){			
			return "<img src='" + imgpathx + "' alt='查看' style='cursor:pointer' onclick='ZbglTecOrder.findInfo(\"" + rowIndex + "\")'/>";
		}, sortable:false
	},{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true,
		editor: {
			id:"trainType_combo_save",	
			fieldLabel: "车型",
			hiddenName: "trainTypeIDX",
			allowBlank:false,
			xtype: "Base_combo",
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
		    business: 'trainType',
		    entity:'com.yunda.jx.base.jcgy.entity.TrainType',
	        fields:['typeID','shortName'],
	        queryParams: {'isCx':'yes'},
		    displayField: "shortName", valueField: "typeID",
	        pageSize: 0, minListWidth: 200,
	        editable:true,
			listeners:{
		       "select" : function(){   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_combo_save");   
	                trainNo_comb.reset();  
	                Ext.getCmp("trainNo_combo_save").clearValue(); 
	                //trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
	                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
	                trainNo_comb.cascadeStore();
	    	     } 
			}
	    }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{ id:'trainTypeShortName', name:'trainTypeShortName',xtype:'hidden'}
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{
		    id:"trainNo_combo_save",	
			fieldLabel: "车号",
			name: "trainNo", 
			allowBlank:false,
			xtype: "Base_combo",
			displayField: "trainNo", 
			valueField: "trainNo",
			pageSize: 20, minListWidth: 200,
		    minChars : 1,
			minLength : 4, 
			maxLength : 4,
			vtype: "numberInt",				
			business: 'trainNo',
			fields:["trainNo"],
			entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
			queryParams:{'isCx':'yes','isIn':'false','isRemoveRun':'false'},
			isAll: 'yes',
			editable:true,
			listeners : {
					"beforequery" : function(){
                		var trainTypeId =  Ext.getCmp("trainType_combo_save").getValue();
    					if(trainTypeId == "" || trainTypeId == null){
    						MyExt.Msg.alert("请先选择车型！");
    						return false;
    					}
                }
			}
		}
	},{
		header:'措施来源ID', dataIndex:'relIDX',hidden:true,editor:{  maxLength:50 },editor: { xtype:'hidden' }
	},{
		header:'措施类型', dataIndex:'orderClass', 
		editor:{
            fieldLabel: '措施类型',
            name: 'orderClass', 
            allowBlank: false,
            xtype: 'EosDictEntry_combo',
            hiddenName: 'orderClass', 
            displayField:'dictname',
            valueField:'dictname',
            dicttypeid:'JCZB_ORDERCLASS_TYPE',
            width:150,
		    height:100
         }
	},{
		header:'销号方式', dataIndex:'completeMethod',
		editor:{
	   		xtype:'radiogroup',fieldLabel:'销号方式',
	    	items:[{   
		        name:'completeMethod', id:'completeMethod_editId', boxLabel:'单次销号', inputValue:COMPLETE_SINGAL, checked:true
		    },{   
		        name:'completeMethod', id:'completeMethod_editId1', boxLabel:'多次销号', inputValue:COMPLETE_MANY, checked:false
		    }]
		 },
		renderer: function(v){
            switch(v){
                case COMPLETE_SINGAL:
                    return COMPLETE_SINGAL_CH;
                case COMPLETE_MANY:
                    return COMPLETE_MANY_CH;
                default:
                    return v;
            }
        }
	},{
		header:'指令内容', dataIndex:'orderContent',
		editor:{allowBlank:false , maxLength:500 , xtype:'textarea' , width: 400,height:80 },width:300
	},{
		header:'发布人ID', dataIndex:'releasePersonID',hidden:true,editor: { xtype:'hidden' }
	},{
		header:'发布人员', dataIndex:'releasePersonName',editor: { xtype:'hidden' }
	},{
		header:'发布时间', dataIndex:'releaseTime', xtype:'datecolumn',editor: { xtype:'my97date', format:'Y-m-d H:i', hidden:true },width:150
	},{
		header:'销号时间', dataIndex:'completeTime',hidden:true, xtype:'datecolumn',editor: { xtype:'hidden' }
	},{
		header:'销号人ID', dataIndex:'completePersonID',hidden:true,editor: { xtype:'hidden' }
	},{
		header:'销号人名称', dataIndex:'completePersonName',hidden:true,editor: { xtype:'hidden' }
	},{
		header:'措施状态', dataIndex:'orderStatus',editor: { xtype:'hidden' },
		renderer: function(v){
            switch(v){
                case STATUS_NEW:
                    return STATUS_NEW_CH;
                case STATUS_PUBLISH:
                    return STATUS_PUBLISH_CH;
                default:
                    return v;
            }
        }
		
	},{
		header:'站场名称', dataIndex:'siteName',hidden:true,editor: { xtype:'hidden' }
	},{
		header:'指令处理次数', dataIndex:'orderHandleTimes',editor: {xtype:'hidden' }
	}],	
	afterShowSaveWin: function(){
		this.saveForm.find('name', 'releasePersonID')[0].setValue(empId);
		this.saveForm.find('name', 'releasePersonName')[0].setValue(empName);
		this.saveForm.find('name', 'orderStatus')[0].setValue(STATUS_NEW);
		this.saveForm.find('name', 'releaseTime')[0].setValue(releaseDate);
		this.saveForm.find('name', 'orderHandleTimes')[0].setValue(0);
	},
	afterShowEditWin: function(record, rowIndex) {
		// 回显车型字段
		this.saveForm.find('hiddenName', 'trainTypeIDX')[0].setDisplayValue(record.get('trainTypeIDX'), record.get('trainTypeShortName'));
		// 回显车号字段
		this.saveForm.find('name', 'trainNo')[0].setDisplayValue(record.get('trainNo'), record.get('trainNo'));
	},
	createSaveWin: function(){
        if(this.saveForm == null) this.createSaveForm();
        //计算查询窗体宽度
        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
        this.saveWin = new Ext.Window({
            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.saveForm, 
            buttons: [{
                text: "发布", iconCls: "pluginIcon", scope: this, handler: function(){
					this.saveForm.find('name', 'orderStatus')[0].setValue(STATUS_PUBLISH);
                	this.saveFn();
                }
            }, {
                text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
                	this.saveFn();
                }
            }, {
                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
            }]
        });
	 },
   afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	         //清除控件中的值
	        this.saveForm.find('hiddenName', 'trainTypeIDX')[0].clearValue();
			this.saveForm.find('name', 'trainNo')[0].clearValue();
			this.saveForm.getForm().reset();
			//回显当前登录人/发布时间/发布状态
	        this.saveForm.find('name', 'releasePersonID')[0].setValue(empId);
		    this.saveForm.find('name', 'releasePersonName')[0].setValue(empName);
		    this.saveForm.find('name', 'releaseTime')[0].setValue(releaseDate);
		    this.saveForm.find('name', 'orderStatus')[0].setValue(STATUS_NEW);
	},
   beforeShowEditWin: function(record, rowIndex){
			if(record.get("orderStatus") == STATUS_PUBLISH){
				MyExt.Msg.alert("已发布的记录不能编辑");
				return false;
			}
			return true;
		},
   beforeDeleteFn: function(){
		var records = ZbglTecOrder.grid.selModel.getSelections();	    	
    	var filter = 0;
    	for(var i = 0; i < records.length; i++){
    		if(records[i].get("orderStatus") == STATUS_PUBLISH){
    			filter++;
    			ZbglTecOrder.grid.selModel.deselectRow(ZbglTecOrder.grid.store.indexOfId(records[i].get("idx")));
    		}
    	}
    	if(filter == records.length && filter != 0){
    		MyExt.Msg.alert("所选记录已发布不能被删除");
    		return false;
    	}
    	if(filter > 0){
    		MyExt.Msg.alert(filter + "条记录已发布不能被删除");
    		return false;
    	}
    	return true;
	 }
   });
	
   ZbglTecOrder.grid.store.on('beforeload', function() {
		ZbglTecOrder.searchParams = ZbglTecOrder.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(ZbglTecOrder.searchParams);
		var whereList = []; 
  	    //设置查询条件
		for(prop in searchParams) {
				switch(prop){
			 	//开始时间(起) 运算符为">="
			 	case 'releaseTime':
			 		whereList.push({propName:'releaseTime',propValue:searchParams[prop] + ' 00:00:00', compare:Condition.GE});
			 		break;
			 	//开始时间(止) 运算符为"<="
			 	case 'toreleaseTime':
			 		whereList.push({propName:'releaseTime',propValue:searchParams[prop]+' 23:59:59', compare:Condition.LE});
			 		break;
	 			case 'trainTypeIDX':
			 		whereList.push({propName:'trainTypeIDX',propValue:searchParams[prop],compare:Condition.EQ,stringLike: false});
			 		break;	 
		 		default:
				//whereList.push({propName:prop,propValue:searchParams[prop],compare:Condition.LIKE});
		 		whereList.push({propName:prop,propValue:searchParams[prop]});
			}
		}
		whereList.push({propName: "orderStatus", propValues: [STATUS_PUBLISH, STATUS_NEW], compare: Condition.IN});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	})


/* ************* 已销号查询Form开始 ************* */
	ZbglTecOrder.searchCancelForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
				layout:"form",
				columnWidth:0.33,
				defaults:{
					xtype:"textfield", width: ZbglTecOrder.fieldWidth
				}
		},
		items:[{
			items:[{ 
				fieldLabel: "车型",
				id: 'trainType_combo_cancel',
				hiddenName: "trainTypeIDX",
				displayField: "shortName", valueField: "typeID",
				anchor: "95%", 	
				pageSize: 0, minListWidth: 200,
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
		                var trainNo_comb = ZbglTecOrder.searchCancelForm.getForm().findField("trainNo");   
			                trainNo_comb.reset();  
			                trainNo_comb.clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			                trainNo_comb.cascadeStore();	
		        	}   
		    	},
				hasEmpty: false,
				emptyText: ''
			}
			]
		},{
			items:[{
			    id:"trainNo_combo_cancel",	
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 100,
				minChars : 1,
//				minLength : 4, 
				maxLength : 5,
				anchor: "95%", 				
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'false'},
				isAll: 'yes',
				editable:true
			
			}]
		},{
			items:[{
					xtype: 'compositefield', fieldLabel: '发布日期', combineErrors: false, width:222,
					items: [{
						xtype:'my97date', name: 'releaseCancelTime', id: 'startDate_c', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},value: lastMonth, width: 90, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_c').getValue());
							var endDate = new Date(Ext.getCmp('endDate_c').getValue());
							if (startDate > endDate) {
								return "开始日期不能大于结束日期";
							}
						}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {							
						xtype:'my97date', name: 'toreleaseCancelTime', id: 'endDate_c', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},width: 90, allowBlank: false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_c').getValue());
							var endDate = new Date(Ext.getCmp('endDate_c').getValue());
							if (startDate > endDate) {
								return "结束日期不能小于开始日期";
							}
						}
					}]
			  }]
		
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			 var form = ZbglTecOrder.searchCancelForm.getForm();
						if (form.isValid()) {
							ZbglTecOrder.cancedGrid.store.load();
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_c').clearInvalid();
							Ext.getCmp('endDate_c').clearInvalid();
						}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				    ZbglTecOrder.searchCancelForm.getForm().reset();
				 var form = ZbglTecOrder.searchCancelForm;
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
                var trainNo_comb = ZbglTecOrder.searchCancelForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeIDX;
                trainNo_comb.cascadeStore();	
            	searchParam = {};
            	ZbglTecOrder.cancedGrid.searchFn(searchParam);
			}
		}]
	})
	/* ************* 已销号查询Form结束 ************* */

	
	
 /**
   * 已销号查询操作的方法
   */
  ZbglTecOrder.findcancedInfo = function(rowIndex){
  	var record = ZbglTecOrder.cancedGrid.store.getAt(rowIndex);
    ZbglTecInfo.showWin(record);
  }	
//已销号
ZbglTecOrder.cancedGrid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglTecOrder!pageQuery.action',                 //装载列表数据的请求URL
    tbar:[],
    storeAutoLoad: false,
    singleSelect: true,
	fields: [
	{
		header:'查看', editor: { xtype:'textfield' },
		renderer:function(value, metaData, record, rowIndex, colIndex, store){			
			return "<img src='" + imgpathx + "' alt='查看' style='cursor:pointer' onclick='ZbglTecOrder.findcancedInfo(\"" + rowIndex + "\")'/>";
		}, sortable:false
	},{
		header:'idx主键', dataIndex:'idx', hidden:true
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true
	},{
		header:'车型简称', dataIndex:'trainTypeShortName'
	},{
		header:'车号', dataIndex:'trainNo' 
	},{
		header:'措施来源ID', dataIndex:'relIDX', hidden:true
	},{
		header:'措施类型', dataIndex:'orderClass',hidden:true
	},{
		header:'指令内容', dataIndex:'orderContent'
	},{
		header:'销号方式', dataIndex:'completeMethod',
		renderer: function(v){
            switch(v){
                case COMPLETE_SINGAL:
                    return COMPLETE_SINGAL_CH;
                case COMPLETE_MANY:
                    return COMPLETE_MANY_CH;
                default:
                    return v;
            }
        }
	},{
		header:'发布人ID', dataIndex:'releasePersonID',hidden:true
	},{
		header:'发布人名称', dataIndex:'releasePersonName'
	},{
		header:'发布时间', dataIndex:'releaseTime',xtype:'datecolumn',editor:{xtype:'my97date', format:'Y-m-d H:i'}
	},{
		header:'处理次数', dataIndex:'orderHandleTimes'
	},{
		header:'销号人ID', dataIndex:'completePersonID',hidden:true
	},{
		header:'销号人名称', dataIndex:'completePersonName'
	},{
		header:'措施状态', dataIndex:'orderStatus',hidden:true
	},{
		header:'销号时间', dataIndex:'completeTime', xtype:'datecolumn',editor:{xtype:'my97date', format:'Y-m-d H:i'}
	},{
		header:'站场名称', dataIndex:'siteName', hidden:true
	}]
});

  /*
	* 已销号Grid数据过滤
  */
 ZbglTecOrder.cancedGrid.store.on("beforeload", function(){
    ZbglTecOrder.searchParams = ZbglTecOrder.searchCancelForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(ZbglTecOrder.searchParams);
		var whereList = []; 
  	    //设置查询条件
		for(prop in searchParams) {
				switch(prop){
			 	//开始时间(起) 运算符为">="
			 	case 'releaseCancelTime':
			 		whereList.push({propName:'releaseTime',propValue:searchParams[prop] + ' 00:00:00', compare:Condition.GE});
			 		break;
			 	//开始时间(止) 运算符为"<="
			 	case 'toreleaseCancelTime':
			 		whereList.push({propName:'releaseTime',propValue:searchParams[prop]+' 23:59:59', compare:Condition.LE});
			 		break;
			 	//车型查询
		 		case 'trainTypeIDX':
		 		whereList.push({propName:'trainTypeIDX',propValue:searchParams[prop], compare:Condition.EQ,stringLike: false});
		 		break;	
		 		
		 		default:
		 		whereList.push({propName:prop,propValue:searchParams[prop]});
			}
		}
		whereList.push({propName: "orderStatus", propValues: [STATUS_CANCEL], compare: Condition.IN});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
 });
 ZbglTecOrder.cancedGrid.un('rowdblclick',ZbglTecOrder.cancedGrid.toEditFn,ZbglTecOrder.cancedGrid);

//未销号和已销号面板
 ZbglTecOrder.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
            title: "未销号", layout: "border", border: false,
            items: [{
            	region:'north',
            	height:160,
            	frame:true,
            	collapsible:true,
            	items:[ZbglTecOrder.searchForm]
            	
            },{
				region: 'center', 
				layout: "fit",
				bodyStyle:'padding-left:0px;', 
	            bodyBorder: true,
				items: [ZbglTecOrder.grid]
			}]
        },{
            title: "已销号", layout: "border", border: false,
            listeners:{
            	activate:function(){
            		ZbglTecOrder.cancedGrid.store.load();
            	}
            },
            items: [{
	        	region:'north',
	        	height:150,
	        	frame:true,
	        	collapsible:true,
	        	items:[ZbglTecOrder.searchCancelForm]
           }, {
				region: 'center', 
				layout: "fit",
				bodyStyle:'padding-left:0px;', 
	            bodyBorder: true,
				items: [ZbglTecOrder.cancedGrid]
			}]
        }]
});
	
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items: ZbglTecOrder.tabs });
	
});