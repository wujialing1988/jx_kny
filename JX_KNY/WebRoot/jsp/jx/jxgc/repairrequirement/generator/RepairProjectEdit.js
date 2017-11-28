/**
 * 检修项目编辑页面操作
 */
RepairProjectEdit = {} ;
//定义组成对象
RepairProjectEdit.buildUpType = {} ;
RepairProjectEdit.idx = "" ;  //定义工序卡主键
RepairProjectEdit.pTrainTypeIdx = "";//检修机组型号id
RepairProjectEdit.pTrainTypeName = "";//检修机组型号名称

//机车配件检修项目新增按钮操作方法
RepairProjectEdit.addFn = function(){
	RepairProject.grid.saveForm.getForm().reset();
	Ext.Ajax.request({
        url: ctx + "/codeRuleConfig!getConfigRule.action",
        params: {ruleFunction : "JXGC_REPAIR_PROJECT_REPAIR_PROJECT_CODE"},
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            Ext.getCmp("repairProjectCodeId").setValue(result.rule) ;
        }
	});
	Ext.getCmp("pTrainTypeIdx_comb").clearValue();
	Ext.getCmp("entityID").setValue("");
	//新增界面可用控件
	RepairProject.grid.enableAllColumns();
	Ext.getCmp("repairProjectCodeId").disable();
	Ext.getCmp("trainProjectSaveId").enable();
	RepairProject.editTabs.activate(0);
	RepairProject.editTabs.get(1).disable();
//	RepairProject.editTabs.get(2).disable();
    RepairProject.saveWin.show();
}

//机车配件检修项目编辑操作方法
RepairProjectEdit.toEditFn = function(grid, rowIndex, e){
	if(this.searchWin != null)  this.searchWin.hide();  
        var record = grid.store.getAt(rowIndex);
        RepairProjectEdit.idx = record.get("idx") ;
        RepairProjectEdit.pTrainTypeIdx = record.get("pTrainTypeIdx");
        RepairProjectEdit.pTrainTypeName = record.get("pTrainTypeShortname");
        RepairProject.saveWin.show();
        //显示编辑窗口
    	RepairProject.editTabs.activate(0);
    	RepairProject.editTabs.get(1).enable();
//    	RepairProject.editTabs.get(2).enable();
   		TrainQR.grid.store.load();  //刷新检修项目对应工序卡
//   		ApplyRuleRC.grid.store.load(); //刷新施修规则
        RepairProject.grid.saveForm.getForm().reset(); //异步方法
        RepairProject.grid.saveForm.getForm().loadRecord(record);
        Ext.getCmp("pTrainTypeIdx_comb").setDisplayValue(record.get("pTrainTypeIdx"),record.get("pTrainTypeShortname"));
}
//保存方法
RepairProjectEdit.saveFn = function(grid){
	//表单验证是否通过
    var form = grid.saveForm.getForm(); 
    if (!form.isValid()) return;    
    var data = form.getValues();
    data.idx = form.findField("idx").getValue();
    data.repairProjectCode = Ext.getCmp("repairProjectCodeId").getValue(); 
    if(Ext.isEmpty(data.vehicleType)){
    	data.vehicleType = vehicleType ;
    }
    grid.loadMask.show();
    var cfg = {
        url: grid.saveURL, 
        jsonData: data, 
        success: function(response, options){
            grid.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
            	if(result.entity != null){
            		RepairProjectEdit.idx = result.entity.idx ;
            		RepairProjectEdit.pTrainTypeIdx = result.entity.pTrainTypeIdx;
        			RepairProjectEdit.pTrainTypeName = result.entity.pTrainTypeShortname;
	            	Ext.getCmp("entityID").setValue(result.entity.idx);
            	}
            	grid.store.load(); //刷新检修项目
            	TrainQR.grid.store.load(); //刷新检修项目对应工序卡
//   				ApplyRuleRC.grid.store.load(); //刷新施修规则
            	RepairProject.editTabs.activate(1);
		    	RepairProject.editTabs.get(1).enable();
//		    	RepairProject.editTabs.get(2).enable();
            } else {
                RepairProject.grid.afterSaveFailFn(result, response, options);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}