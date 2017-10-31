/**
 * 机车作业进度 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainWP');                       //定义命名空间
TrainWP.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainWP!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainWP!saveTrainWPAndDetail.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainWP!logicDelete.action',            //删除数据的请求URL
    saveFormColNum:2,	searchFormColNum:2,
    tbar:['search',{
    	text:'新增',iconCls:'addIcon',handler:function(){
    		TrainEnforcePlanRdp.selectWin.show();
    		TrainEnforcePlanRdp.grid.store.load();
    	}
    },'delete'],
	fields:  [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型简称', dataIndex:'trainTypeShortName', editor:{  maxLength:8 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{  maxLength:50 }
	},{
		header:'配属局简称', dataIndex:'bShortName', editor:{  maxLength:50 }
	},{
		header:'配属段简称', dataIndex:'dShortName', editor:{  maxLength:50 }
	},{
		header:'委修段简称', dataIndex:'delegateDShortname', editor:{  maxLength:50 }
	},{
		header:'兑现单主键', dataIndex:'rdpIDX',  hidden:true, editor: { xtype:'hidden' }
	}/*,{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },searcher:{disabled :　true}
	}*/],
	afterShowEditWin: function(record, rowIndex){
		TrainWPDetail.trainWPIDX = record.get("idx");
		TrainWPDetail.grid.store.load();
	},
	createSaveWin: function(){
        if(this.saveForm == null) this.createSaveForm();
        this.disableAllColumns();
        this.saveWin = new Ext.Window({
            title:"新增", width:700, height:500, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, 
            items: [{
		            region: 'north', layout: "fit",height:180,frame: true, bodyBorder: false,items:[this.saveForm]
		        },{
		            region : 'center', layout : 'fit',frame: true,  bodyBorder: false, height:250,
		            items : TrainWPDetail.grid
		        }]
			,
            buttons: [{
//                text: "保存", iconCls: "saveIcon", scope: this, handler: this.saveFn
//            }, {
                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
            }]
        });
    },
	saveFn: function(){
        //表单验证是否通过
        var form = this.saveForm.getForm(); 
        if (!form.isValid()) return;
        
        //获取表单数据前触发函数
        this.beforeGetFormData();
        var data = form.getValues();
        //获取表单数据后触发函数
        this.afterGetFormData();
        
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(data)) return;
        
        if(self.loadMask)   self.loadMask.show();
        //获取进度项的值
	    var objs = []; //进度项对象
	    var length = objList.length ;
	    for (var i = 0; i < length; i++) {
	    	var obj = {} ;
	    	var key = objList[ i ].key;//获取进度项对象键
	    	delete Ext.getCmp(key).value;
	    	var value = Ext.getCmp(key).value;  //进度项对象值
	    	if(!Ext.isEmpty(value)){
	    		obj.progressCode = key;
		    	obj.progressName = objList[ i ].name;
		    	obj.progressTime = value;
		    	objs.push(obj);
	    	}
	    }
	    var cfg = {
	        scope: TrainWP.grid, 
	        url: TrainWP.grid.saveURL, 
	        jsonData: data, params: {trainWPDetails: Ext.util.JSON.encode(objs)},
	        success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null) {
	            	if(result.entity != null){
//	            		WorkStepEdit.idx = result.entity.idx ;
	//	            	Ext.getCmp("workCardIDX").setValue(result.entity.idx);
	            	}
			    	TrainWP.grid.afterSaveSuccessFn(result, response, options)
	            } else {
	                TrainWP.grid.afterSaveFailFn(result, response, options);
	            }
	        }
	    };
	    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    }
});
//选择生产计划明细过滤
TrainEnforcePlanRdp.grid.store.on("beforeload", function(){
	var sqlStr = " idx not in (select nvl(t.Rdp_IDX,'-11111') from SCDD_Train_WP t " +
				 " where t.record_status=0 )" ;
	TrainEnforcePlanRdp.searchParams = MyJson.deleteBlankProp(TrainEnforcePlanRdp.searchParams);
	var whereList = [
		{propName: "billStatus", propValue: rdp_status_nullfy, compare: Condition.NE},  //兑现状态（查询不为终止状态的生产计划明细）
		{sql: sqlStr, compare: Condition.SQL} //过滤已生产作业进度的检修计划
		] ;
	for(prop in TrainEnforcePlanRdp.searchParams){
		whereList.push({propName: prop, propValue: TrainEnforcePlanRdp.searchParams[prop]});
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
//选择兑现单之后执行确认的操作（生成一条作业进度记录）
TrainEnforcePlanRdp.submit = function(){
	var sm = TrainEnforcePlanRdp.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.trainTypeIDX = objData[i].get("trainTypeIDX");  //车型主键
    	data.trainTypeShortName = objData[i].get("trainTypeShortName");  //车型简称
    	data.trainNo = objData[i].get("trainNo");  //车号
    	data.rdpIDX = objData[i].get("idx"); //兑现单id
    	data.bShortName = objData[i].get("bshortName");//配属局
    	data.dShortName = objData[i].get("dshortName");//配属段
    	data.delegateDShortname = objData[i].get("delegateDShortName");//委修段
        dataAry.push(data);
    }
    Ext.Ajax.request({
        url: ctx + '/trainWP!saveOrUpdateList.action',
        jsonData: dataAry,
        success: function(response, options){
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                TrainEnforcePlanRdp.grid.store.reload();
                TrainWP.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }
        },
        failure: function(response, options){
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
};
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:TrainWP.grid });
});