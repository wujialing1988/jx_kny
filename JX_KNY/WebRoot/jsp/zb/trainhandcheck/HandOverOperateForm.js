Ext.onReady(function(){
Ext.namespace('HandOverOperateForm');

HandOverOperateForm.infoForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.3, 
            items: [
//        		{ id: "fromPersonId_Id", fieldLabel:"交车司机", xtype: 'OmEmployee_SelectWin',hiddenName: 'fromPersonId', 
//        		  returnField:[{widgetId:"fromPersonName",propertyName:"empname"}],
//				  editable:true,allowBlank: false
//				},
				{
					id:"fromPersonId",fieldLabel:"司机工号", name: "fromPersonId",vtype: "numberInt",allowBlank: true,
					enableKeyEvents:true,
					listeners : {
						keyup : function(a,b){
							//通过ajax到后台查询，精确查询
							var pid = a.getValue();//交车司机ID，输入框中的值
							Ext.Ajax.request({
								url: ctx + "/omEmployeeSelect!findEmployeeByCardID.action",
								params: {fromPersonId : pid},
								success: function(r){
									var retn = Ext.util.JSON.decode(r.responseText);
									if(retn.success){
										Ext.getCmp("fromPersonName").setValue(retn.fromPersonName);
									}else{
										Ext.getCmp("fromPersonName").setValue("");
									}
								},
								failure: function(){
									alertFail("请求超时！");
								}
							});
						}
					}
				}
				
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.3, 
            items: [
				{
					id:"fromPersonName",fieldLabel:"司机名称", name: "fromPersonName",allowBlank: true
				}
            ]
        },{
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.4, 
            items: [
            	{
            	name: "arrivedTime", fieldLabel: "迎检时间",xtype: "my97date",allowBlank: true,labelWidth:80,width:150,
				format: "Y-m-d H:i:s",my97cfg: {dateFmt:"yyyy-MM-dd HH:mm:ss",maxDate:'%y-%M-%d'},
				validator :function(v){
					if(v) return true;
					else return "不允许为空";
					}
				}
            ]
        }]
    }]
});
HandOverOperateForm.confirmXCForm =  new Ext.form.FormPanel({
	baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,	labelWidth: 50,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.5, 
            items: [{ 
            	fieldLabel:"备注", name:"remarks", xtype: "textarea", maxLength:100,width:300
            }]
        },{
        	baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:80,
            columnWidth: 0.35, 
            items: [{
				xtype: 'radiogroup',
				name: 'isDoZbglRdpWi', 
				fieldLabel: '是否做范围活',
	            items: [
	                {
	                	name: 'isDoZbglRdpWi', 
	                	boxLabel: '做', 
	                	inputValue: 1,
	                	checked:true
	                },
	                {
	                	id:'isDoZbglRdpWiNotDo',
	                	name: 'isDoZbglRdpWi', 
	                	boxLabel: '不做', 
	                	inputValue: 0
	                }
	            ]
	            /*,
	            listeners: {
            		"change" : function(field,newValue,oldValue){
           				var val = newValue.getGroupValue();
    					if(val == 0){
    						//该车型车号下有jt6提票正在处理中，必须做范围活！
	                		if(HandOverOperateForm.jt6Flag){
	                			MyExt.Msg.alert("该车型车号下有jt6提票正在处理中，必须做范围活！");
								return;
	                		}
    					}
            		}
            	}*/
            }]
        }]
    }]
});
/****************2015年01月21日改进交接乘务员验证--开始**********************/
//增加机车乘务员角色验证
/*HandOverOperateForm.confirmCheckCwyForm =  new Ext.form.FormPanel({
   layout:"column",
   labelWidth:60,
   border:false,
   defaults:{
     columnWidth:1,
     layout:'form',
     anchor: "98%",
     defaultType:'textfield'
   },
   items:[{
      items:[{fieldLabel:"用户名",name:"empName",allowBlank:false}]
     },{
      items:[{fieldLabel:"密码",name:"empPw",inputType:"password",allowBlank:false}]
   }]
});
HandOverOperateForm.confirmCheckCwyWin =  new Ext.Window({
   title:"机车乘务员确认",width:300,height:150,maximizable:false,layout:"fit",
   closeAction:"hide",modal:true,moximized:false,buttonAlign:"center",
   items:{
   	 xtype:"panel",layout:"border",frame:true,
   	 items:[{
   	     layout:"fit",
   	     region:"center",
   	     items:[HandOverOperateForm.confirmCheckCwyForm]
   	 }]
   },
   buttons:[{
      text:"确认",iconCls:"checkIcon",handler:function(){
      	  var form = HandOverOperateForm.confirmCheckCwyForm.getForm(); 
          if (!form.isValid()) return;
           var checkdata = {};
           checkdata.empName = HandOverOperateForm.confirmCheckCwyForm.getForm().findField("empName").getValue();
           checkdata.empPw = HandOverOperateForm.confirmCheckCwyForm.getForm().findField("empPw").getValue();
           Ext.Ajax.request({
				url: ctx + "/trainHandOverRdp!checkTrainCwy.action",
				jsonData: checkdata,
				params:{empName: checkdata.empName,empPw:checkdata.empPw},
				success: function(response, options){
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.errMsg){
						alertFail(result.errMsg);
					}else{    						
						HandOverOperateForm.confirmCheckCwyWin.hide();
						HandOverOperateForm.comfirmSave();
					}					
				}
			});
        }
   },{
      text:"关闭",iconCls:"closeIcon",handler:function(){
        HandOverOperateForm.confirmCheckCwyWin.hide();
      }
   }]
});*/

HandOverOperateForm.comfirm = function(){
	var form = HandOverOperateForm.infoForm.getForm(); 
    if (!form.isValid()) return;
/*	HandOverOperateForm.confirmCheckCwyForm.getForm().reset();
	HandOverOperateForm.confirmCheckCwyWin.show();*/
    HandOverOperateForm.comfirmSave();
}
HandOverOperateForm.comfirmSave = function(){
  	var rdpData = {};
	rdpData.rdpIdx = rdpIdx;
//	rdpData.trainTypeIDX = TaskBaseInfoForm.baseForm.getForm().findField("trainTypeIdx").getValue();
//	rdpData.trainTypeShortName = TaskBaseInfoForm.baseForm.getForm().findField("trainTypeShortName").getValue();
//	rdpData.trainNo = TaskBaseInfoForm.baseForm.getForm().findField("trainNo").getValue();
//	rdpData.fromPersonId = HandOverOperateForm.infoForm.getForm().findField("fromPersonId").getValue();
	rdpData.fromPersonName = HandOverOperateForm.infoForm.getForm().findField("fromPersonName").getValue();
	// 处理手工输入
//	if(Ext.isEmpty(rdpData.fromPersonId)){
//		rdpData.fromPersonName = Ext.get("fromPersonId_Id").dom.value;
//	}
	rdpData.handOverTrainOrder = HandOverOperateForm.infoForm.getForm().findField("handOverTrainOrder").getValue();
	rdpData.workItemID = workitemId;
	rdpData.processInstID = processInstID;
	rdpData.confirmXC = '趟检';
	    var itemDataArray = [];
		for (var i = 0; i < HandOverModelList.grid.store.getCount(); i++) {
			var itemData = {} ;
			var data = HandOverModelList.grid.store.getAt(i).data;
			if(Ext.isEmpty(data.handOverItemStatus) && Ext.isEmpty(data.handOverResultDesc)){
				MyExt.Msg.alert("请填写情况记录！");
				return;
			}
			itemData.handOverItemModelIDX = data.idx;
			itemData.handOverItemStatus = data.handOverItemStatus;
			itemData.handOverResultDesc = data.handOverResultDesc;
			itemDataArray.push(itemData);
		}
       Ext.Msg.confirm("提示", "确认处理完成", function(btn){
		  if(btn == 'yes'){
			//表单验证是否通过
             var form = HandOverOperateForm.infoForm.getForm(); 
             if (!form.isValid()) return;
			 parent.showtip();
			 Ext.Ajax.request({
				url: ctx + "/trainHandOverRdp!saveAndFinishWorkItem.action",
				jsonData: rdpData,
				params: {itemDataArray: Ext.util.JSON.encode(itemDataArray)},
				success: function(response, options){
					if(parent.processTips) parent.hidetip();
					var result = Ext.util.JSON.decode(response.responseText);
					if(result.errMsg){
						alertFail(result.errMsg);
					}else{    						
						alertSuccess();
						parent.closeHandlerWin();
						parent.refreshGrid();
					}					
				}
			});
		}
	});
 }
 
/****************2015年01月21日改进交接乘务员验证--结束**********************/
parent.confirm = HandOverOperateForm.comfirm;
});