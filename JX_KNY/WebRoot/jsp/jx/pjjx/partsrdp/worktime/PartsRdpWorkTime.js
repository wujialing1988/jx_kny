/**
 * 工时 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartsRdpWorkTime');                       //定义命名空间
PartsRdpWorkTime.searchParam = {};
PartsRdpWorkTime.rdpIDX = "";
PartsRdpWorkTime.ratedWorkTime = ""; //额定工时
PartsRdpWorkTime.labelWidth = 70;
PartsRdpWorkTime.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/partsRdpWorkTime!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/partsRdpWorkTime!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/partsRdpWorkTime!logicDelete.action',            //删除数据的请求URL
    storeAutoLoad : false,singleSelect : true,
    tbar:[],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'作业主键', dataIndex:'rdpIDX', hidden:true,editor:{  maxLength:50 }
	},{
		header:'人员代码', dataIndex:'workEmpID', editor:{ disabled:true }
	},{
		header:'人员名称', dataIndex:'workEmpName', editor:{disabled:true}
	},{
		header:'工时', dataIndex:'workTime', editor:{ allowBlank:false,xtype:'numberfield', maxLength:6 }
	}],
    saveFn: function(rowEditor, changes, record, rowIndex){
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(rowEditor, changes, record, rowIndex)) {
            rowEditor.stopEditing(false);
            return;
        }
        var cfg = {
            scope: this, url: this.saveURL, jsonData: record.data,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        var count = this.store.getTotalCount();
		var ratedWorkTime = PartsRdpWorkTime.ratedWorkTime;//总工时
		var workTime = 0;
	    for(var i = 0 ; i < count ; i++){
	    	var r = this.store.getAt(i);
	    	if(r.get("idx") != record.get("idx")){
	    		workTime += r.get("workTime");
	    	}
		}
        if(ratedWorkTime-workTime < record.get("workTime")){
        	Ext.Msg.confirm('提示', '工时超过总工时，确定保存？', function(btn) {
        		if (btn == 'yes') Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg)) ;
        		else if (btn == 'no') PartsRdpWorkTime.grid.store.reload() ;
        	});
        }else Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg)) ;
        
    }
});
PartsRdpWorkTime.grid.store.on('beforeload', function() {
		var searchParams = MyJson.deleteBlankProp(PartsRdpWorkTime.searchParam);
		searchParams.rdpIDX = PartsRdpWorkTime.rdpIDX ;
		PartsRdpWorkTime.grid.store.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
// 任务单基本信息
PartsRdpWorkTime.rdpForm = new Ext.form.FormPanel({
	labelWidth:PartsRdpWorkTime.labelWidth,
	border: false,
	labelAlign:"left",
	layout:"column",
	bodyStyle:"padding:10px;",
	defaults:{
		xtype:"container", autoEl:"div", layout:"form", columnWidth:0.25, 
		defaults:{
			style: 'border:none; background:none;', 
			xtype:"textfield", readOnly: true,
			anchor:"100%"
		}
	},
	items:[{
		items:[{
			fieldLabel:"配件编号", name:"partsNo"
		}, {
			fieldLabel:"下车车型号", name:"trainType"
		}, {
			fieldLabel:"检修班组", name:"repairOrgName"
		}]
	}, {
		items:[{
			fieldLabel:"配件名称", name:"partsName"
		}, {
			fieldLabel:"下车修程", name:"repair"
		}, {
			fieldLabel:"总工时", name:"ratedWorkTime"
		}]
	}, {
		items:[{
			fieldLabel:"规格型号", name:"specificationModel"
		}, {
			fieldLabel:"开始时间", name:"realStartTime"
		}]
	}, {
		items:[{
			fieldLabel:"扩展编号", name:"extendNo"
		}, {
			fieldLabel:"结束时间", name:"realEndTime"
		}]
	}]
});
//分配工时窗口
PartsRdpWorkTime.editWorkTimeWin = new Ext.Window({
		title: "配件检修工时分配",
		width: 950, height: 700, 
		maximizable:false, maximized: false, modal: true,
		closeAction: "hide",
		layout: "border",
		items:[{
				xtype:"panel",frame:true, title:'配件检修作业单信息',
				region: 'north',
				height: 140,
				border: true,
				collapsible: true,
				items: [PartsRdpWorkTime.rdpForm]
			}, {
				title:"作业人员",
				region: 'center',
				layout: 'fit',
				border: false,
				items: [PartsRdpWorkTime.grid]
			}],
		buttonAlign:"center",
		buttons: [{
	        text: "关闭", iconCls: "closeIcon", handler: function(){ PartsRdpWorkTime.editWorkTimeWin.hide(); }
	    }]
    });
});