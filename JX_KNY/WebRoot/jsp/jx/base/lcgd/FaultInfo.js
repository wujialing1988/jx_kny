Ext.onReady(function(){
	Ext.namespace('FaultInfo');                       //定义命名空间
	FaultInfo.labelWidth = 80;
	FaultInfo.fieldWidth = 200;
	//提报信息
	FaultInfo.baseInfoForm = new Ext.form.FormPanel({
	    layout: "anchor", 	border: false, 		style: "padding:5px", 		labelWidth: FaultInfo.labelWidth,
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" },
	    items: [{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", anchor:"90%",
	        items: [
	    	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType:"textfield",
	            labelWidth: FaultInfo.labelWidth, 	 columnWidth: 0.33, 
	            items: [
					{ name: "trainTypeShortName", fieldLabel: "车型车号", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true},
					{ name: "specificationModel", fieldLabel: "配件型号", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
					{ name: "faultName", fieldLabel: "故障现象", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            ]
	    	},
	    	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType:"textfield",
	            labelWidth: FaultInfo.labelWidth, 	 columnWidth: 0.33, 
	            items: [
					{ id: "type_Id", name: "type", fieldLabel: "工作票类型",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },						
					{ name: "partsNo", fieldLabel: "配件编号",width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
					{ id: "faultOccurDate_Id", name: "faultOccurDateStr", fieldLabel: "故障发生时间", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            ]
	    	},
	    	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType:"textfield",
	            labelWidth: FaultInfo.labelWidth, 	 columnWidth: 0.34, 
	            items: [
					{ name: "fixPlaceFullName", fieldLabel: "故障位置",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
					{ name: "nameplateNo", value: "", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
					{ name: "noticePersonName", fieldLabel: "故障提报人", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            ]
	    	}
	        ]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", 
	        items: [
	    	
					{ name: "faultDesc", fieldLabel: "故障描述",style:"border:none;background:none;", width: FaultInfo.fieldWidth, readOnly:true }
	            ]		    	
	    }]
	});
	//处理信息
	FaultInfo.form = new Ext.form.FormPanel({
	    layout: "form",     border: false,                            labelWidth: FaultInfo.labelWidth, 
	    baseCls: "x-plain",   buttonAlign: "center",    defaultType: "textfield",   style: "padding:5px", 
	    items: [
	    	{
		    	xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center",  anchor:"90%",
		    	items : [{
		    		baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.66, defaultType: "textfield",
	            	items : 
	            		{ name: "realFixPlaceFullName", fieldLabel: "实际故障位置",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }		            	
		    	},{
		    		baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.34, defaultType: "textfield",
	            	items : 
	            		{ name: "realFaultName", fieldLabel: "故障现象",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }		            	
		    	}]
		    },
	    	{
	    	xtype: "panel", border: false, anchor:"90%", baseCls: "x-plain", layout: "column", align: "center", 
	    	items : [
	    		{
	    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.33, defaultType: "textfield",
	            	items : [
	            		{ name: "realSpecificationModel", fieldLabel: "配件型号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		{ name: "methodName", fieldLabel: "处理方法",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		{ name: "completeTimeStr", fieldLabel: "修竣时间",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            		//{ name: "isDropMeaning", fieldLabel: "落修配件",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		//{ name: "fixSpecificationModel", fieldLabel: "安装配件型号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            	]
	    		},
	    		{
	    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.33, defaultType: "textfield",
	            	items : [
	            		{ name: "realPartsNo", fieldLabel: "配件编号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		{ name: "methodDesc", fieldLabel: "处理方法描述",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		{ name: "worker", fieldLabel: "承修人",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            		//{ name: "dropSpecificationModel", fieldLabel: "落修配件型号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		//{ name: "fixPartsNo", fieldLabel: "安装配件编号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            	]
	    		},
	    		{
	    			baseCls: "x-plain", align: "center", layout: "form", columnWidth: 0.34, defaultType: "textfield",
	            	items : [
	            		{ name: "", fieldLabel: "", value: "",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		{ name: "repairResult", fieldLabel: "处理结果",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            		//{ name: "", fieldLabel: "", value: "", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		//{ name: "dropPartsNo", fieldLabel: "落修配件编号",  width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true },
	            		//{ name: "", fieldLabel: "", value: "", width: FaultInfo.fieldWidth, style:"border:none;background:none;", readOnly:true }
	            	]
	    		}
	    		]}
	    	]
	});
	//定义点击取消按钮的操作
	/*FaultInfo.closeWin = function(){
		alert("请覆盖方法（FaultInfo.closeWin）！");
	}*/
	FaultInfo.faultHandleForm = new Ext.Panel({
		layout: "form",  border: false, style: "padding:2px", labelWidth: FaultInfo.labelWidth,
		align: "center", defaults: { anchor: "98%" }, frame: true, buttonAlign:"center",
		items:[{
	    		xtype: "fieldset",
	    		title: "提报信息", autoHeight: true,
	    		items: {
						layout: "form", collapsible:true, frame: true,
						items: FaultInfo.baseInfoForm
					}
				},{
	    		xtype: "fieldset",
	    		title: "处理信息", autoHeight: true,
	    		items: {
						layout: "form", collapsible:true, frame: true,
						items: FaultInfo.form
					}
			}]/*,
		buttons:[{
			text: "关闭", iconCls:"closeIcon",
			handler:function(){
				FaultInfo.closeWin();
			}
		}]*/
	});
	//加载基本信息表单
	FaultInfo.loadBaseInfoForm = function(faultNotice, baseInfoForm){
		baseInfoForm.getForm().reset();
        baseInfoForm.getForm().loadRecord(new Ext.data.Record(faultNotice)); 
        //baseInfoForm.findById("faultOccurDate_Id").setValue(new Date(faultNotice.faultOccurDate).format('Y-m-d H:i'));//时间格式化
        baseInfoForm.findById("type_Id").setValue(EosDictEntry.getDictname("JCZL_FAULT_TYPE",faultNotice.type));//提票类型转义
        baseInfoForm.doLayout();
	}
	FaultInfo.loadFaultHandleForm = function(faultNotice, baseInfoForm){
		FaultInfo.loadBaseInfoForm(faultNotice, baseInfoForm);
		FaultInfo.form.getForm().reset();
        FaultInfo.form.getForm().loadRecord(new Ext.data.Record(faultNotice));
        FaultInfo.form.doLayout();
        //FaultInfo.form.getForm().findField("completeTime").setValue(new Date(faultNotice.completeTime).format('Y-m-d H:i'));
	}
	var myMask = new Ext.LoadMask(document.body, {msg:"正在处理，请稍侯..."});
	myMask.show();
	//表单展示前ajax查询后台数据
	Ext.Ajax.request({
        url: ctx + "/faultNotice!getFaultNoticeInfo.action",
        params: {sourceIdx: sourceIdx},
        success: function(response, options){	                  	
                var result = Ext.util.JSON.decode(response.responseText);                
                if (result.success == true && result.entity != null) {
                	myMask.hide();                	
					FaultInfo.loadFaultHandleForm(result.entity, FaultInfo.baseInfoForm);
                }else{
                	myMask.hide();
                	MyExt.Msg.alert("无对应提票信息！");
                }
        },
        failure: function(response, options){
        	myMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    });
	/*var viewport = new Ext.Viewport({
		layout : 'fit',
		items: FaultInfo.faultHandleForm
	});*/
});