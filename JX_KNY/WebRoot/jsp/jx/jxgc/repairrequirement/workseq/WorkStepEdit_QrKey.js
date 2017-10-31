/**
 * 检测/修项目编辑（质量控制配置在作业工单上） 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('WorkStepEdit');                       //定义命名空间
//定义组成对象
WorkStepEdit.buildUpType = {} ;
WorkStepEdit.labelWidth = 20;
WorkStepEdit.fieldWidth = 100 ;
WorkStepEdit.idx = "" ;  //定义工步主键
WorkStepEdit.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});

WorkStepEdit.saveFn = function(){
	var form = WorkStep.stepForm.getForm(); 
    if (!form.isValid()) return;
    var data = form.getValues();
    data.workSeqIDX = QREdit.idx ; //检修工序卡主键   
    
	 //调用保存前触发函数，如果返回fasle将不保存记录
    if(!WorkStep.grid.beforeSaveFn(data)) return;
    WorkStepEdit.loadMask.show();
    var cfg = {
        scope: WorkStep.grid, 
        url: WorkStep.grid.saveURL, 
        jsonData: data, 
        success: function(response, options){
            WorkStepEdit.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
            	if(result.entity != null){
            		WorkStepEdit.idx = result.entity.idx ;
	            	Ext.getCmp("WorkStepId").setValue(result.entity.idx);
            	}
		    	WorkStep.grid.afterSaveSuccessFn(result, response, options)
            } else {
                WorkStep.grid.afterSaveFailFn(result, response, options);
            }
        }
    };
    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
}

});