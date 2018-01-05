/**
 * 编组信息维护
 */
Ext.onReady(function(){
		
	Ext.namespace('MarshallingTrainDemand');                       //定义命名空间
	//定义全局变量保存查询条件
	MarshallingTrainDemand.searchParam = {} ;
	MarshallingTrainDemand.trainDemandIdx = '###' ;	// 主键列表
	var processTips;
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		processTips = new Ext.LoadMask(Ext.getBody(),{
			msg: msg || i18n.MarshallingDemandMaintain.msg6,
			removeMask:true
		});
		processTips.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		processTips.hide();
	}
	
	// 覆盖MatInforList.submit()方法，添加配件检修用料信息
	JczlTrainServiceWin.submit = function(){
		//判断是否选择了数据
		var grid = JczlTrainServiceWin.grid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert(i18n.MarshallingDemandMaintain.msg7);
			return;
		}
	   var records = grid.getSelectionModel().getSelections();
	   var datas = new Array();
	   for(var i=0; i<grid.getSelectionModel().getCount(); i++){
	   	  var data = {} ;
		  var  temp = records[i].data;
		  data.vehicleKindName = temp.vehicleKindName
		  data.vehicleKindCode = temp.vehicleKindCode
		  data.trainNo = temp.trainNo
		  data.trainTypeShortName = temp.trainTypeShortName
		  data.trainTypeIDX = temp.trainTypeIDX
		  data.marshallingCode = JczlTrainServiceWin.marshallingCode;
		  datas.push(data);
	   }
		var cfg = {
	        url: ctx + "/marshallingTrain!saveOrUpdateList.action", 
			params : {datas : Ext.util.JSON.encode(datas)},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                MarshallingTrainDemand.grid.store.load();
	                JczlTrainServiceWin.addWin.hide();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert(i18n.MarshallingDemandMaintain.msg8, i18n.MarshallingDemandMaintain.msg9 + "\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm(i18n.MarshallingDemandMaintain.msg8, i18n.MarshallingDemandMaintain.msg10, function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	MarshallingTrainDemand.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/marshallingTrainDemand!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/marshallingTrainDemand!logicDelete.action',            //删除数据的请求URL
	    tbar: ['search',
//	    	{
//	    	text : "新增", iconCls : "addIcon",
//			handler : function(){
//				JczlTrainServiceWin.addWin.show();
//			}},
	    	{
	    	text : i18n.MarshallingDemandMaintain.refresh, iconCls : "refreshIcon",
			handler : function(){
				MarshallingTrainDemand.grid.store.reload();
			}}], 
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
		fields: [{
			header:i18n.MarshallingDemandMaintain.marshallingCode, dataIndex:'marshallingCode',width: 40, searcher: { hidden: true }
		},{
			header:i18n.MarshallingDemandMaintain.vehicleKindName, dataIndex:'vehicleKindName',width: 30
		},{
			header:i18n.MarshallingDemandMaintain.vehicleKindCode, dataIndex:'vehicleKindCode',hidden:true, editor: {xtype:"hidden",id:"vehicleKindCode"},searcher: { hidden: true }
		},{
			header:i18n.MarshallingDemandMaintain.trainNumber, dataIndex:'trainNo',width:30
		},{
			header:i18n.MarshallingDemandMaintain.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		},{
			header:i18n.MarshallingDemandMaintain.trainTypeName, dataIndex:'trainTypeShortName',width: 40, searcher: { hidden: true }
		},{
			header:i18n.MarshallingDemandMaintain.trainTypeIdx, dataIndex:'trainTypeIDX',hidden:true, width: 60, searcher: { hidden: true }
		},{
			header:i18n.MarshallingDemandMaintain.planIdx, dataIndex:'trainDemandIdx',hidden:true, width: 60, searcher: { hidden: true }
		}],
		toEditFn: Ext.emptyFn,  //覆盖双击编辑事件
		searchFn: function(searchParam){ 
			MarshallingTrainDemand.searchParam = searchParam ;
	        MarshallingTrainDemand.grid.store.load();
		}
	});
	
	//查询前添加过滤条件
	MarshallingTrainDemand.grid.store.on('beforeload' , function(){
	   var searchParam = MarshallingTrainDemand.searchParam ;
		searchParam.trainDemandIdx = MarshallingTrainDemand.trainDemandIdx ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
		
});