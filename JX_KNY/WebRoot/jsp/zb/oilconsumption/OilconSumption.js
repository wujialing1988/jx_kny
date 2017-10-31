/**
 * 机油消耗台账 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('OilconSumption');                       //定义命名空间

	/** 获取最近的一个月 */
	var dateNow = new Date();
	var month = dateNow.getMonth();
	dateNow.setMonth(month-1);
	var lastMonth = dateNow.format('Y-m-d');
	/* ************* 定义全局变量开始 ************* */
	OilconSumption.labelWidth = 100;
	OilconSumption.fieldWidth = 140;
	OilconSumption.isAddAndNew = false; 
	/* ************* 定义全局变量结束 ************* */
	
OilconSumption.searchForm = new Ext.form.FormPanel({
		labelWidth: 60,
		labelAlign:"left",
		layout:"column",
		padding:'10px', 
		defaults:{
				layout:"form",
				columnWidth:0.33,
				defaults:{
					xtype:"textfield", width: OilconSumption.fieldWidth
				}
		},
		items:[{
			columnWidth:0.39,
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
                    editable:false
        		},{
					xtype: 'compositefield', fieldLabel: '领用日期', combineErrors: false, width:222,
					items: [{
						xtype:'my97date', name: 'fetchTime', id: 'startDate_d', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},value: lastMonth, width: 90, allowBlank: false,
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
						xtype:'my97date', name: 'toFetchTime', id: 'endDate_d', format:'Y-m-d', my97cfg: {dateFmt:"yyyy-MM-dd"},width: 90, allowBlank: false,
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
			columnWidth:0.3,
			items:[{
				id:"trainNo",xtype:'textfield',fieldLabel:'车号',name:"trainNo"			
			},{
				fieldLabel: '消耗站场',
				id: 'site_combo',
				name: 'siteName',
				xtype:"Base_combo",
				business:'workPlace',
				entity:"com.yunda.jxpz.workplace.entity.WorkPlace",		
				queryHql:"from WorkPlace",
				returnField: [{widgetId:"site_combo",propertyName:"workPlaceName"}],
				fields:['workPlaceName'],
				displayField:"workPlaceName",
				valueField: "siteName",
				isAll: 'yes'
			}]
		}, {
		    columnWidth:0.3,
			items:[{
				  	fieldLabel: '机油种类',
					id: 'jy_combo',
					name: 'jyName',
					xtype:"Base_combo", 
					business: 'jyzl',
					entity:"com.yunda.zb.oilconsumption.entity.Jyzl",						  
					queryHql:"from Jyzl",	
					returnField: [{widgetId:"jy_combo",propertyName:"jyName"}],
					fields:['jyName'],
					displayField:"jyName",
					valueField: "jyName",
					isAll: 'yes'
			  },{
				fieldLabel:"记录人",
				name: 'handlePersonName'
			}]
		}], 
		buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
			 var form = OilconSumption.searchForm.getForm();
						if (form.isValid()) {
							OilconSumption.grid.store.load();
							// 清除日期过滤条件元素的任何无效标志样式与信息
							Ext.getCmp('startDate_d').clearInvalid();
							Ext.getCmp('endDate_d').clearInvalid();
						}
			}
		},{
			text:'重置', iconCls:'resetIcon', handler: function() {
				//this.findParentByType('form').getForm().reset();
				    OilconSumption.searchForm.getForm().reset();
                    Ext.getCmp('trainType_combo').clearValue();
				    Ext.getCmp('jy_combo').clearValue();
				    Ext.getCmp('site_combo').clearValue();
				    // 重新加载表格
				    OilconSumption.grid.store.load();
			}
		}]
	})

OilconSumption.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/oilconSumption!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/oilconSumption!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/oilconSumption!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,	searchFormColNum:1,
    tbar: ['add','delete','refresh',
    	  {text:"打印", iconCls:"printerIcon", 
    	   handler: function(){
    	   		var form = OilconSumption.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("startDate_d").getValue() != ""){
		        	startDate = Ext.getCmp("startDate_d").getValue().format('Y-m-d H:i');
		        }
		        if(Ext.getCmp("endDate_d").getValue() != ""){
		        	overDate = Ext.getCmp("endDate_d").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/oilconsumption/OilconSumption.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeIDX=" + data.trainTypeIDX +
				"&trainNo=" + data.trainNo + "&jyName=" + data.jyName + "&siteName=" + data.siteName + "&handlePersonName=" + data.handlePersonName;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机油消耗记录"));
    	   }
     }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'车型主键', dataIndex:'trainTypeIDX',hidden:true, 
		  editor: { 
    				id:"trainType_comb_save",	
    				fieldLabel: "车型",
    				hiddenName: "trainTypeIDX",
    				xtype: "Base_combo",
    				returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
    				displayField: "shortName", valueField: "typeID",
    			    business: 'trainType',
    			    entity:'com.yunda.jx.base.jcgy.entity.TrainType',
                    fields:['typeID','shortName'],
                    queryParams: {'isCx':'yes'},
                    pageSize: 0, minListWidth: 200,
                    editable:true,
                    allowBlank:false,
    				listeners : {
    			       "select" : function(){   
			            	//重新加载车号下拉数据
			                var trainNo_comb = Ext.getCmp("trainNo_comb_save");   
			                trainNo_comb.reset();  
			                Ext.getCmp("trainNo_comb_save").clearValue(); 
			                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
			               // trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
			                trainNo_comb.cascadeStore();
		        	 } 
    		  }
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ id:'trainTypeShortName', name:'trainTypeShortName',xtype:'hidden'}
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{
			    id:"trainNo_comb_save",	
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
	                		var trainTypeId =  Ext.getCmp("trainType_comb_save").getValue();
	    					if(trainTypeId == "" || trainTypeId == null){
	    						MyExt.Msg.alert("请先选择车型！");
	    						return false;
	    					}
	                }
    			}
		}
	},{
		header:'机油种类编码', dataIndex:'jyCode', hidden:true,editor:{
			        fieldLabel: '机油种类',
					hiddenName: 'jyCode',
					id: 'jy_como_save',
					xtype:"Base_combo", 
					business: 'jyzl',
					entity:"com.yunda.zb.oilconsumption.entity.Jyzl",						  
					queryHql:"from Jyzl",
					returnField:[{widgetId:"jyCode",propertyName:"jyCode"},{widgetId:"jyName",propertyName:"jyName"},{widgetId:"dw",propertyName:"dw"}],
					fields:['jyCode','jyName','dw'],
					displayField:"jyName",
					valueField: "jyCode",
					isAll: 'yes',
					allowBlank:false
				}
	},{
		header:'机油种类', dataIndex:'jyName', editor:{  id:'jyName', name:'jyName',xtype:'hidden'}
	},{
		header:'计量单位', dataIndex:'dw',editor:{ id:'dw', maxLength:20}
	},{
		header:'机油数量', dataIndex:'consumeQty', editor:{ xtype:'numberfield', maxLength:5,allowBlank:false }
	},{
		header:'领用日期', dataIndex:'fetchTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'记录人编码', dataIndex:'handlePersonID', editor:{ xtype:'numberfield', maxLength:18 }, hidden:true,editor:{xtype:'hidden'}
	},{
		header:'记录人', dataIndex:'handlePersonName', editor:{  maxLength:25 ,allowBlank:false, disabled:true}
	},{
		header:'记录日期', dataIndex:'handleTime', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
	    header:'站场ID', dataIndex:'siteID',hidden:true, editor:{  maxLength:50,xtype:'hidden'}
	},{
		header:'站场', dataIndex:'siteName', editor:{  maxLength:50,xtype:'hidden'}
	}],
	afterShowSaveWin: function(){
		this.saveForm.find('name', 'handlePersonID')[0].setValue(empId);
		this.saveForm.find('name', 'handlePersonName')[0].setValue(empName);
	},
	afterShowEditWin: function(record, rowIndex) {
		// 回显车型字段
		this.saveForm.find('hiddenName', 'trainTypeIDX')[0].setDisplayValue(record.get('trainTypeIDX'), record.get('trainTypeShortName'));
		// 回显车号字段
		this.saveForm.find('name', 'trainNo')[0].setDisplayValue(record.get('trainNo'), record.get('trainNo'));
	    // 回显机油种类名称字段
		this.saveForm.find('hiddenName', 'jyCode')[0].setDisplayValue(record.get('jyCode'), record.get('jyName'));
		// 回显机油单位字段
		this.saveForm.find('name', 'dw')[0].setValue(record.get('dw'));
		
		this.saveForm.find('name', 'siteID')[0].setValue(record.get('siteID'));
		this.saveForm.find('name', 'siteName')[0].setValue(record.get('siteName'));
	},
	beforeGetFormData: function () {
		this.saveForm.find('name', 'handlePersonName')[0].enable();
	}, 
	afterGetFormData: function() {
		this.saveForm.find('name', 'handlePersonName')[0].disable();
	},
	createSaveWin: function(){
        if(this.saveForm == null) this.createSaveForm();
        //计算查询窗体宽度
        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
        this.saveWin = new Ext.Window({
            title:"新增", width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide",
            buttonAlign:'center', maximizable:true, items:this.saveForm, autoHeight:true,
            buttons: [{
	                text: "保存", iconCls: "saveIcon", scope: this, handler: function() {
	                	OilconSumption.isSaveAndAdd = false;
	                	this.saveFn();
	                }
	            }, {
	                text: "保存并新增", iconCls: "addIcon", scope: this, handler: function() {
	                	OilconSumption.isSaveAndAdd = true;
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
	        if (!OilconSumption.isSaveAndAdd) {
	        	this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        	 //回显站场信息
		        this.saveForm.find('name', 'siteID')[0].setValue(result.entity.siteID);
		        this.saveForm.find('name', 'siteName')[0].setValue(result.entity.siteName);
	        	return;
	        }
	        this.saveForm.getForm().reset();
	        //清除控件中的值
	        this.saveForm.find('hiddenName', 'trainTypeIDX')[0].clearValue();
			this.saveForm.find('name', 'trainNo')[0].clearValue();
			this.saveForm.find('hiddenName', 'jyCode')[0].clearValue();
			//回显当前登录人
	        this.saveForm.find('name', 'handlePersonID')[0].setValue(empId);
		    this.saveForm.find('name', 'handlePersonName')[0].setValue(empName);
	    }
	});
	
	OilconSumption.grid.store.on('beforeload', function() {
		OilconSumption.searchParams = OilconSumption.searchForm.getForm().getValues();
		var searchParams = MyJson.deleteBlankProp(OilconSumption.searchParams);
		var whereList = []; 
  	    //设置查询条件
		for(prop in searchParams) {
				switch(prop){
			 	//开始时间(起) 运算符为">="
			 	case 'fetchTime':
			 		whereList.push({propName:'fetchTime',propValue:searchParams[prop] + ' 00:00:00', compare:Condition.GE});
			 		break;
			 	//开始时间(止) 运算符为"<="
			 	case 'toFetchTime':
			 		whereList.push({propName:'fetchTime',propValue:searchParams[prop]+' 23:59:59', compare:Condition.LE});
			 		break;
			 	case 'trainTypeIDX':
			 		whereList.push({propName:'trainTypeIDX',propValue:searchParams[prop], compare:Condition.EQ, stringLike:false});
			 		break;
		 		default:
				whereList.push({propName:prop,propValue:searchParams[prop],compare:Condition.LIKE});
			   
			}
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	})

  	// 页面自适应布局
OilconSumption.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 143,
			collapsible: true,
			items: [OilconSumption.searchForm]
		}, {
			// 结果显示列表区域
			region: 'center', 
			layout: "fit",
			bodyStyle:'padding-left:0px;', 
            bodyBorder: true,
			items: [OilconSumption.grid]
		}]
	});
})