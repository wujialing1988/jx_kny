Ext.onReady(function(){
	Ext.namespace("DisposeOpinion");
	DisposeOpinion.labelWidth = 80;
	DisposeOpinion.fieldWidth = 200;
	//定义点击提交按钮的操作
	/*DisposeOpinion.submit = function(){
		alert("请覆盖方法（DisposeOpinion.submit）！");
	}
	//定义点击取消按钮的操作
	DisposeOpinion.closeWin = function(){
		alert("请覆盖方法（DisposeOpinion.closeWin）！");
	}*/
	DisposeOpinion.form = new Ext.form.FormPanel({
	    layout: "anchor", 	border: false, 		style: "padding:5px", 		labelWidth: DisposeOpinion.labelWidth,
	    baseCls: "x-plain", align: "center", 	defaultType: "textfield",	defaults: { anchor: "98%" }, buttonAlign:"center",
	    items: [{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "column", align: "center", 
	        items: [
	    	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType:"textfield",
	            labelWidth: DisposeOpinion.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "disposePerson", fieldLabel: "处理人", width: DisposeOpinion.fieldWidth, readOnly:true, value:uname},
					{ xtype: 'Base_combo', fieldLabel: '处理意见', hiddenName: 'disposeIdea', allowBlank: false, 
					  business: 'disposeOpinion', entity:'com.yunda.jx.jxgc.producttaskmanage.entity.DisposeOpinion',
					  displayField: 'disposeIdea', valueField: 'disposeIdea',  fields: ["disposeIdea"], queryParams: {'workItemId': workitemId, 'isCx': 'true'},
					  pageSize: 0, minListWidth: 200, width: DisposeOpinion.fieldWidth, editable: false }
	            ]
	    	},
	    	{
	            baseCls: "x-plain", align: "center", layout: "form", defaultType:"textfield",
	            labelWidth: DisposeOpinion.labelWidth, 	 columnWidth: 0.5, 
	            items: [
					{ name: "disposeTime", fieldLabel: "处理时间",  width: DisposeOpinion.fieldWidth, xtype:"my97date", 
					  format: "Y-m-d H:i", my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank: false}
	            ]
	    	}]
	    },{
	        xtype: "panel", border: false,  baseCls: "x-plain", layout: "form", align: "center", 
	        items: { name: "disposeOpinion", fieldLabel: "处理意见描述",xtype:"textarea",  width: 400 }		    	
	    },
	    { xtype: "hidden", name : 'workItemName'},
	    { xtype: "hidden", name : 'businessIDX', value:idx},
	    { xtype: "hidden", name : 'processInstID', value:processInstID},
	    { xtype: "hidden", name : 'signType', value:signType},
	    { xtype: "hidden", name : 'activityInstID', value: actInstId},
	    { xtype: "hidden", name : 'workItemID', value: workitemId},
	    { xtype: "hidden", name : 'disposePersonID', value: empid},
	    { xtype: "hidden", name : 'disposeItemCode'}]/*,
	    buttons:[{
			text: "提交", iconCls:"saveIcon",
			handler:function(){
				DisposeOpinion.submit();
	    	}
		},{
			text: "取消", iconCls:"closeIcon",
			handler:function(){
				DisposeOpinion.closeWin();
			}
		}]*/
	});
	jQuery.ajax({
		url: ctx + "/processTask!getBeseInfo.action",
		data:{workitemid: workitemId},
		dataType:"json",
		type:"post",
		success:function(data){
			var record = new Ext.data.Record();
			for(var i in data){
				if(!Ext.isEmpty(data[i]))
					record.set(i,data[i]);
			}
			DisposeOpinion.form.getForm().loadRecord(record);
		}
	});
//	var viewport = new Ext.Viewport({
//		layout : 'fit',
//		items: DisposeOpinion.form
//	});
});