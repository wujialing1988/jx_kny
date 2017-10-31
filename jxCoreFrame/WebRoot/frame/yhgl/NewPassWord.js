Ext.onReady(function() {
	Ext.namespace('newPwd'); 
	
	newPwd.labelWidth = 90;
	newPwd.anchor = '85%';
	newPwd.fieldWidth = 160;
	
	newPwd.Mask = new Ext.LoadMask(Ext.getBody(), {msg:null});
	//表单
	newPwd.form = new Ext.form.FormPanel({
		defaultType:'textfield', layout:"form",
		labelWidth: newPwd.labelWidth,	columnWidth:1, style:"padding:10px" ,  baseCls: "x-plain",
		items:[
			{ id:'operatorid',   fieldLabel:'操作员ID',   	name:'operatorid', 		hidden:true}, //主键
			{ id:'operatorname', fieldLabel:'操作员名称', 	name:'operatorname', 	hidden:false, 	width:160,	disabled:true},
			{ id:'userid',       fieldLabel:'登录帐号',   	name:'userid', 			hidden:false, 	width:160,	disabled:true},
			{ id:'password',     fieldLabel:'原始密码',  	name:'password', 		hidden:false, 	width:160,  allowBlank:false},
			{ id:'newpwd1',      fieldLabel:'新密码',		name:'newpwd1', 		hidden:false, 	width:160,  allowBlank:false},			
			{ id:'newpwd2',      fieldLabel:'确认新密码',	name:'newpwd2', 		hidden:false, 	width:160,  allowBlank:false}					
		]
	});
	
	newPwd.store = new Ext.data.JsonStore({
		url : ctx + "/operator!findOperatorToStore.action",
		autoLoad : true,
		root : "root",
		remoteSort : true,
		fields : ["operatorid", "operatorname", "userid", "password"]
	});
	
	newPwd.store.on('load', function() {
		if(newPwd.store.data.items!=null&&newPwd.store.data.items.length>0){
			var record = newPwd.store.data.items[0].data;
			Ext.getCmp('operatorid').setValue(record.operatorid);
			Ext.getCmp('operatorname').setValue(record.operatorname);
			Ext.getCmp('userid').setValue(record.userid);
		}
	});
	
	//面板
	newPwd.panel = new Ext.Panel({
		buttonAlign: "center",  baseCls: "x-plain",//baseCls: "x-plain", //anchor : '20% 25%', 
		items: [newPwd.form],
		buttons: [{
           	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
           		var newPWD = '';
           		var form = newPwd.form.getForm();
           		if (!form.isValid()) return;
           		var data = form.getValues();
           		if(data.newpwd1 != data.newpwd2){
           			alertFail('两次输入不符！');
           			return;
           		} else {
           			newPWD = data.newpwd1;
           		}
           		newPwd.Mask.show();
            		
           		delete data.newpwd1;
           		delete data.newpwd2;
            		
		        var cfg = {
		            scope: this, url: ctx+"/operator!updateOperatorPWD.action?newPwd="+newPWD, jsonData: data,
		            success: function(response, options){
		                if(newPwd.Mask)   newPwd.Mask.hide();
		                var result = Ext.util.JSON.decode(response.responseText);
		                if (result.errMsg == null) {
		                    alertSuccess();
		                } else {
		                    alertFail(result.errMsg);
		                }
		            }
		        };
		        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
           	}
		},{
			text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){
				newPwd.win.hide();
				newPwd.Mask.hide();
			}
		}],
		listeners: {
			render : function() {
				newPwd.store.load();
			}
		}
	});
	
	newPwd.win = new Ext.Window({
		title: "密码设置", maximizable: false, width: 300, height: 300, layout: "fit", closable:false, items : [newPwd.panel]
	});
	
//	//页面自适应布局
//	var viewport = new Ext.Viewport({ layout:'fit', items:[new Ext.Panel({title: "密码设置", layout: "fit"})]});
	newPwd.showWin = function(){
		if(Ext.getCmp("yhgl_7")!=null){
			Ext.getCmp("MainFrameTabPanel").remove(Ext.getCmp("yhgl_7"));
		}
		newPwd.Mask.show();
		newPwd.win.show();
	}
});