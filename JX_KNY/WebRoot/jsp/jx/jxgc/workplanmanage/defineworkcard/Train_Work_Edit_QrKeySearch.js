
Ext.onReady(function(){

	Ext.namespace("TrainWorkSearch");
	
	TrainWorkSearch.idx = '';
	TrainWorkSearch.status = '';
	TrainWorkSearch.nodeCaseIDX = '';
	TrainWorkSearch.trainTypeIDX = '';
	
	/**
	 * 创建自定义工单编辑Form
	 */
	TrainWorkSearch.createForm = function(){
		
		TrainWorkSearch.mainForm =  new Ext.form.FormPanel({
			baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
		    layout: "form",		border: false,	labelWidth: 50, buttonAlign: 'center',
		    items: [{
		        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
		        items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
		            columnWidth: 0.5, 
		            items: [
		        		{ fieldLabel:"编号", name:"workCardCode", id:"code_id", anchor: "95%", maxLength: 20, allowBlank: false, readOnly:true, style:'color:gray', disabled: true},
		                { fieldLabel:"安全注意事项", name:"safeAnnouncements",  anchor: "95%", maxLength: 1000, xtype:"textarea", disabled: true},
		                { fieldLabel:"isVirtual", id:"isVirtual_Id", name:"isVirtual", xtype: "hidden"},
		                { fieldLabel:"ExtensionClass", name:"extensionClass", xtype: "hidden"},
		                { fieldLabel:"WorkStationIDX", name:"workStationIDX", xtype: "hidden"},
		                { fieldLabel:"workStationName", name:"workStationName", xtype: "hidden"},
		                { fieldLabel:"WorkStationCode", name:"workStationCode", xtype: "hidden"},
		                { fieldLabel:"idx", name:"idx", xtype: "hidden", id:"idx_id"},
		                { fieldLabel:"workStationBelongTeam", name:"workStationBelongTeam", xtype: "hidden"},
		                { fieldLabel:"workStationBelongTeamName", name:"workStationBelongTeamName", xtype: "hidden"},
		                { fieldLabel:"WorkStationBelongTeamSeq", name:"workStationBelongTeamSeq", xtype: "hidden"},
		                { fieldLabel:"rdpIDX", name:"rdpIDX", xtype: "hidden", id: "rdp_id"},
		                { fieldLabel:"status", name:"status", xtype: "hidden", value: TrainWorkSearch.status},
		                { fieldLabel:"nodeCaseIDX", name:"nodeCaseIDX", xtype: "hidden", id: "nodeCase_id"},
		                { fieldLabel:"repairActivityIDX", name:"repairActivityIDX", xtype: "hidden", id: "repairActivity_id"}
		            ]
		        },                {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
		            columnWidth: 0.5, 
		            items: [
		            	{ fieldLabel:"工单名称", name:"workCardName", anchor: "95%", maxLength: 50, allowBlank: false, disabled: true},
		                { fieldLabel:"分类", anchor: "95%", allowBlank: false,
		    				id:"workClass_combo", xtype: 'EosDictEntry_combo',
		    				hiddenName: 'workSeqClass',
		    				dicttypeid:'JXGC_WORKSEQ_WORKCLASS', displayField:'dictname',valueField:'dictid', disabled: true
		    			},{ fieldLabel:"描述", name:"workScope", anchor: "95%", maxLength: 1000, xtype: "textarea", disabled: true}
		            ]
		        }]
		    }]
		});
	}
	/**
	 * 创建tabpanel
	 */
	TrainWorkSearch.createTab = function(){
		
		TrainWorkSearch.fieldset = [{
			bodyStyle: 'padding-left:10px;',
			items:{
				xtype: "fieldset",
				title: "基本信息编辑",
				collapsible:true,   //渲染面板为可闭合的
				autoHeight: true,
				width: 720,
				items: TrainWorkSearch.mainForm
			}
		},{
			bodyStyle: 'padding-left:10px;',
			items:{
				xtype: "fieldset",
				title: "质量检查项",
				collapsible:true,   //渲染面板为可闭合的
				autoHeight: true,
				width: 720,
				items: WorkStepEditSearch.saveForm
			}
		}];
		
		TrainWorkSearch.tabs = new Ext.TabPanel({
		    activeTab: 0, frame:true,
		    items:[{
	            title: "基本信息编辑", layout: "fit", frame: true, border: false, items: {
	                buttonAlign:"center",
	                autoScroll: true, //滚动条
	                defaults: {
	                    border: false
	                },            
	                items: TrainWorkSearch.fieldset,
	    		    buttons: [{
	    		    	text: '关闭', iconCls: 'closeIcon', handler: function(){
	    		    		TrainWorkSearch.mainWin.hide();
	    		    	}
	    		    }]
	            }
	        },{
	            title: "检测/检修项目", layout: "fit", border: false, items: [ WorkStepSearch.grid ]
	        }]
		});
		
	}
	
	/**
	 * 创建自定义工单编辑界面
	 */
	TrainWorkSearch.createWin = function(){
		
		if(!TrainWorkSearch.mainForm) TrainWorkSearch.createForm();
		if(!TrainWorkSearch.tabs) TrainWorkSearch.createTab();
		
		TrainWorkSearch.mainWin = new Ext.Window({
			title:"自定义工单查看", width:800, height:500, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
			items: TrainWorkSearch.tabs , maximized : true
		});
	}
	
	/**
	 * 加载自定义工单编辑窗口
	 */
	TrainWorkSearch.showEditWin = function(record){
		
		if(!TrainWorkSearch.mainWin) TrainWorkSearch.createWin();
		
		TrainWorkSearch.mainWin.show();
		var form = TrainWorkSearch.mainForm.getForm();
		form.reset();
		form.loadRecord(record);
		WorkStepEditSearch.clearQC();
		WorkStepEditSearch.SetWorkSeqQC(record.get("idx"), WorkCardEditSearch.grid);
		
		Ext.getCmp("workClass_combo").setDisplayValue(record.get("workSeqClass"),
					EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS', record.get("workSeqClass")));
		
		TrainWorkSearch.tabs.activate(0);
		
		TrainWorkSearch.SetSelectPartsType(true);	//编辑时设置选择零部件
	}
	//设置选择零部件控件
	TrainWorkSearch.SetSelectPartsType = function(isEdit){
		TrainWorkSearch.isEdit = isEdit;
        
        delete TrainWorkSearch.PartsPaths;
	}
});