/** 车辆检修校验页面 */
Ext.onReady(function(){
	
	Ext.namespace('TrainAcceptanceWin');                       //定义命名空间
	
	TrainAcceptanceWin.workPlanIdx = "" ;						//兑现单全局变量
	
	/** **************** 定义保存表单开始 **************** */
	TrainAcceptanceWin.saveForm = new Ext.form.FormPanel({
		border:false, baseCls:'x-plain',
		padding:'10px',
		labelWidth:100,
		defaults:{
			xtype:'textfield',
			anchor:'95%'
		},
		items:[{
			id:'fromPersonId',name:'fromPersonId', fieldLabel:'交车人ID',hidden:true
		},{
	        xtype: 'OmEmployee_SelectWin', fieldLabel: '交车人',
			hiddenName: 'fromPersonName', displayField:'empname', valueField: 'empname',
			returnField :[{widgetId: "fromPersonId", propertyName: "empid"}],
			allowBlank:false,editable: false
		},{
			name:'fromRemark', fieldLabel:'交验情况描述', xtype:'textarea', height:75, maxLength:200
		}]
	});
	/** **************** 定义保存表单结束 **************** */
	
	
	/** **************** 定义新增窗口开始 **************** */
	TrainAcceptanceWin.saveWin = new Ext.Window({
		title:'车辆检修校验',
		plain:true,
		modal: true,
		closeAction:'hide',
		layout:'fit',
		width:400, height:250,
		items:[TrainAcceptanceWin.saveForm],
		buttonAlign: 'center',
		buttons:[{
			text:"保存", iconCls:'saveIcon', handler:function() {
				TrainAcceptanceWin.saveFn();
			}
		}, {
			text:"关闭", iconCls:'cancelIcon', handler:function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	/** **************** 定义新增窗口结束 **************** */
	
	TrainAcceptanceWin.saveFn = function() {
		var form = TrainAcceptanceWin.saveForm.getForm();
		if (!form.isValid()) {
			return;	
		}
		var entityJson = form.getValues();
		// 清除json对象的空属性
		entityJson = MyJson.deleteBlankProp(entityJson);
		entityJson.idx = TrainAcceptanceWin.workPlanIdx ;
		// Ajax请求
		Ext.Ajax.request({
			url:ctx + '/trainWorkPlan!jyTrainWorkPlan.action',
			jsonData: entityJson,
		    //请求成功后的回调函数
			success: function(response, options){
	        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            TrainAcceptanceWin.saveWin.hide();
		            TrainAcceptance.TrainAcceptanceUnCompleteGrid.store.load();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	}	
	
	
});