
Ext.onReady(function(){

	Ext.namespace("TrainWork");
	TrainWork.processTips = null;
	/*
	 * 显示处理遮罩
	 */
	TrainWork.showtip = function(win,msg){
		TrainWork.processTips = new Ext.LoadMask(win.getEl(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		TrainWork.processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	TrainWork.hidetip = function(){
		TrainWork.processTips.hide();
	}
	TrainWork.idx = '';	
	TrainWork.status = '';
	TrainWork.nodeCaseIDX = '';
	TrainWork.trainTypeIDX = '';
	/**
	 * 创建自定义工单编辑Form
	 */
	TrainWork.createForm = function(){
		
		TrainWork.mainForm =  new Ext.form.FormPanel({
			baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "99%" },
		    layout: "form",		border: false,	labelWidth: 50, buttonAlign: 'center',
		    items: [{
		        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
		        items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
		            columnWidth: 0.5, 
		            items: [
		        		{ fieldLabel:"编号", name:"workCardCode", id:"code_id", anchor: "95%", maxLength: 20, allowBlank: false, readOnly:true, style:'color:gray'},
		                { fieldLabel:"安全注意事项", name:"safeAnnouncements",  anchor: "95%", maxLength: 1000, xtype:"textarea"},
		                { fieldLabel:"ExtensionClass", name:"extensionClass", xtype: "hidden"},
		                { fieldLabel:"WorkStationIDX", name:"workStationIDX", xtype: "hidden"},
		                { fieldLabel:"workStationName", name:"workStationName", xtype: "hidden"},
		                { fieldLabel:"WorkStationCode", name:"workStationCode", xtype: "hidden"},
		                { fieldLabel:"idx", name:"idx", xtype: "hidden", id:"idx_id"},
		                { fieldLabel:"workStationBelongTeam", name:"workStationBelongTeam", xtype: "hidden"},
		                { fieldLabel:"workStationBelongTeamName", name:"workStationBelongTeamName", xtype: "hidden"},
		                { fieldLabel:"WorkStationBelongTeamSeq", name:"workStationBelongTeamSeq", xtype: "hidden"},
		                { fieldLabel:"rdpIDX", name:"rdpIDX", xtype: "hidden", id: "rdp_id"},
		                { fieldLabel:"status", name:"status", xtype: "hidden", id: "status_id"},
		                { fieldLabel:"nodeCaseIDX", name:"nodeCaseIDX", xtype: "hidden", id: "nodeCase_id"},
		                { fieldLabel:"repairActivityIDX", name:"repairActivityIDX", xtype: "hidden", id: "repairActivity_id"}
		            ]
		        },                {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
		            columnWidth: 0.5, 
		            items: [
		            	{ fieldLabel:"工单名称", name:"workCardName", anchor: "95%", maxLength: 50, allowBlank: false},
		                { fieldLabel:"分类", anchor: "95%", allowBlank: false,
		    				id:"workClass_combo", xtype: 'EosDictEntry_combo',
		    				hiddenName: 'workSeqClass',
		    				dicttypeid:'JXGC_WORKSEQ_WORKCLASS', displayField:'dictname',valueField:'dictid'
		    			},{ fieldLabel:"描述", name:"workScope", anchor: "95%", maxLength: 1000, xtype: "textarea"}
		            ]
		        }]
		    }]
		});
	}
	/**
	 * 创建tabpanel
	 */
	TrainWork.createTab = function(){
		
		TrainWork.fieldset = [{
			bodyStyle: 'padding-left:10px;',
			items:{
				xtype: "fieldset",
				title: "基本信息编辑",
				collapsible:true,   //渲染面板为可闭合的
				autoHeight: true,
				width: 720,
				items: TrainWork.mainForm
			}
		}];
		
		TrainWork.tabs = new Ext.TabPanel({
		    activeTab: 0, frame:true,
		    items:[{
	            title: "基本信息编辑", layout: "fit", frame: true, border: false, items: {
	                buttonAlign:"center",
	                autoScroll: true, //滚动条
	                defaults: {
	                    border: false
	                },            
	                items: TrainWork.fieldset,
	    		    buttons: [{
	    		    	text: '保存', iconCls: 'saveIcon', handler: function(){
	    		    		TrainWork.save();
	    		    	}
	    		    },{
	    		    	text: '关闭', iconCls: 'closeIcon', handler: function(){
	    		    		TrainWork.mainWin.hide();
	    		    	}
	    		    }]
	            }
	        },{
	            id: 'checkItem_id', title: "检测/检修项目", layout: "fit", border: false, items: [ WorkStep.grid ]
	        }]
		});
	}
	
	/**
	 * 创建自定义工单编辑界面
	 */
	TrainWork.createWin = function(){
		
		if(!TrainWork.mainForm) TrainWork.createForm();
		if(!TrainWork.tabs) TrainWork.createTab();
		
		TrainWork.mainWin = new Ext.Window({
			title:"自定义工单编辑", width:800, height:500, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
			items: TrainWork.tabs , maximized : true
		});
		
		//选择零部件型号前先选择车型
		Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.on("beforeshow",function(){
			
			if(!TrainWork.trainTypeIDX){
				MyExt.Msg.alert("请先选择检修车型！");
				return false;
			}else{
				//根据车型过滤可安装的组成型号
		        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.proxy = new Ext.data.HttpProxy({
		        												url: ctx + "/partsBuildSelect!buildUpTypeList.action" +
		        													"?typeIDX=" + TrainWork.trainTypeIDX
		        											        + "&type=" + 0});
		        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.baseParams.typeIDX = TrainWork.trainTypeIDX;
				Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.baseParams.type = 0;
		        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.load();
				return true;
			}
			
		});
	}
	
	/**
	 * 新增自定义工单
	 */
	TrainWork.showWin = function(){
		if(!TrainWork.mainWin) TrainWork.createWin();
		
		var form = TrainWork.mainForm.getForm();
		
		form.reset();
//		form.findField("workSeqType").clearValue();
//		form.findField("workSeqType").clearInvalid();		
		
		form.findField("status").setValue(TrainWork.status);
		
		TrainWork.mainWin.show();

//		Ext.getCmp("rdpType_id").setValue(rdpType);
		
		Ext.getCmp("rdp_id").setValue(workPlanIDX);
		
		Ext.getCmp("nodeCase_id").setValue(TrainWork.nodeCaseIDX);
//		Ext.getCmp("repairActivity_id").setValue(Editor.repairActivityIDX);	
		TrainWork.SetSelectPartsType(false); //新增时设置选择零部件
		Ext.getCmp("checkItem_id").disable();
		TrainWork.tabs.activate(0);
		Ext.getCmp("PartsTypeByBuild_SelectWin_Id").clearValue();
		
		Ext.Ajax.request({
            url: ctx + "/codeRuleConfig!getConfigRule.action",
            params: {ruleFunction: "xcode"},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    Ext.getCmp("code_id").setValue(result.rule);
                }
            },
            failure: function(response, options){
            	return ;
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
    	});
		WorkStepEdit.saveForm.getForm().reset();
//		Ext.Ajax.request({	//查询组成名称
//			url: ctx + "/buildUpType!getBuildUpTypeByIdx.action",
//			params: {partsBuildUpTypeIdx: Editor.buildUpTypeIdx},
//			success: function(response, options){
//				var result = Ext.util.JSON.decode(response.responseText);
//				if (result.errMsg == null) {
//					Editor.buildUpTypeName = result.entity.buildUpTypeName;
//				}
//			},
//			failure: function(response, options){
//				return ;
//				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
//			}
//		});
	}
	
	/**
	 * 加载自定义工单编辑窗口
	 */
	TrainWork.showEditWin = function(record){
		
		if(!TrainWork.mainWin) TrainWork.createWin();
		
		TrainWork.mainWin.show();
		
		form.reset();
		form.loadRecord(record);

//		form.findField("workSeqType").setDisplayValue(record.get("workSeqType"),
//				EosDictEntry.getDictname('JXGC_WORK_STEP_REPAIR_TYPE', record.get("workSeqType")));
		
		Ext.getCmp("workClass_combo").setDisplayValue(record.get("workSeqClass"),
				EosDictEntry.getDictname('JXGC_WORKSEQ_WORKCLASS', record.get("workSeqClass")));
		
		Ext.getCmp("PartsTypeByBuild_SelectWin_Id").setDisplayValue(record.get("buildUpTypeIDX"), record.get("buildUpTypeName"));
		
		TrainWork.tabs.activate(0);
		
		TrainWork.SetSelectPartsType(true);	//编辑时设置选择零部件
	}
	/**
	 * 保存自定义工单
	 */
	TrainWork.save = function(){
		var form = TrainWork.mainForm.getForm(); 
        if (!form.isValid()) return;
        
        var data = form.getValues();
        delete data.isVirtual;
	    var tmp = data;
    	var data = [];
    	data.push(MyJson.clone(tmp));
    	TrainWork.showtip(TrainWork.mainWin);
        var cfg = {
            url: ctx + "/workCard!saveDefineWork.action",
            jsonData: data,
            success: function(response, options){
                if(TrainWork.processTips) TrainWork.hidetip();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    TrainWork.idx = result.entity.idx;
                    if(TrainWork.isEdit == false){
                    	Ext.getCmp("idx_id").setValue(result.entity.idx);
                    	Ext.getCmp("checkItem_id").enable();
                    	TrainWork.tabs.activate(1);
                    	WorkStep.grid.store.load();
                    	
                    }
                    WorkCardEdit.grid.store.load();
                } else {
                    alertFail();
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	//设置选择零部件控件
	TrainWork.SetSelectPartsType = function(isEdit){
		TrainWork.isEdit = isEdit;
		Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.baseParams = {};
        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.baseParams.typeIDX = TrainWork.trainTypeIDX;//车型id
        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.baseParams.type = 0;//类型： 0 机车 1 配件
        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").win.buildGrid.store.load();
        Ext.getCmp("PartsTypeByBuild_SelectWin_Id").isSingSelect = TrainWork.isEdit;
        
        delete TrainWork.PartsPaths;
	}
});