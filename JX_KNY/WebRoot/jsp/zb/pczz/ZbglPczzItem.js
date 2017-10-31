/**
 * 普查整治项 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ZbglPczzItem');     //定义命名空间

   /* ************* 定义全局变量开始 ************* */
	ZbglPczzItem.searchParams = {};
	
   /* ************* 定义全局变量结束 ************* */
	
ZbglPczzItem.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/zbglPczzItem!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/zbglPczzItem!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/zbglPczzItem!deleteZbglPczzItem.action',            //删除数据的请求URL
    saveFormColNum:1,
    saveWinWidth:500,
    saveWinHeight:200,
    //viewConfig:true,
    tbar:['add','delete',
    	 {text:"适用机车", iconCls:"queryIcon",
    	 handler: function(){
    	 	if (!$yd.isSelectedRecord(ZbglPczzItem.grid)){
				MyExt.Msg.alert("请选择一条数据!");
				return;
    	 	}
		    ZbglPczzItem.ids = $yd.getSelectedIdx(ZbglPczzItem.grid);
		    TrainNoSelectWin.idx = ZbglPczzItem.ids;
    		TrainNoSelectWin.selectWin.show();
    		TrainNoSelectWin.grid.store.load();
    	}
    }],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'普查整治单主键', dataIndex:'zbglPczzIDX',hidden:true,editor:{  xtype:'hidden'}
	},{
		header:'项目名称', dataIndex:'itemName', editor:{  maxLength:50 ,width: 350}
	},{
		header:'项目内容', dataIndex:'itemContent', editor:{  maxLength:300 ,xtype:'textarea',width: 350},width:250
	},{
		header:'车号', dataIndex:'idx', editor:{ xtype:'hidden'},
		renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
            var html = "";
            html = "<span><a href='#' onclick='ZbglPczzItemToTraininfoWin.showWin(\""+ record.data.idx + "\")'>查看选择机车</a></span>";
            return html;
        }
	}],
	beforeSaveFn: function(data){ 
		data.idx = data.idx[0];
		return true; 
	},
	afterShowSaveWin: function(){
		this.saveForm.find('name', 'zbglPczzIDX')[0].setValue(ZbglPczz.zbglPczzIdx);
	},
    afterSaveSuccessFn: function(result, response, options){
	   alertSuccess();
	   this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	   ZbglPczzItem.grid.store.load();
	   this.saveWin.hide();
	 }
 });

//确认提交方法，后面可覆盖此方法完成查询
TrainNoSelectWin.submit = function(){
	var sm = TrainNoSelectWin.grid.getSelectionModel();
    if (sm.getCount() < 1) {
        MyExt.Msg.alert("尚未选择一条记录！");
        return;
    }
    var objData = sm.getSelections();
    var dataAry = new Array();
    for (var i = 0; i < objData.length; i++){
    	var data = {};
    	data.trainTypeShortName = objData[i].get("trainTypeShortName");  //车型编码（主键）
    	data.trainNo = objData[i].get("trainNo") ;
    	data.trainTypeIDX = objData[i].get("trainTypeIDX");
    	data.zbglPczzIDX = ZbglPczz.zbglPczzIdx;
        dataAry.push(data);
    }
    TrainNoSelectWin.loadMask.show();
    Ext.Ajax.request({
        url: ctx + '/zbglPczzItemToTraininfo!updatePczzItemTrainNo.action',
        jsonData: dataAry,
        params:{ids: ZbglPczzItem.ids},
        success: function(response, options){
          	TrainNoSelectWin.loadMask.hide();
            var result = Ext.util.JSON.decode(response.responseText);
            if (result.errMsg == null) {
                alertSuccess();
                TrainNoSelectWin.grid.store.reload();
                ZbglPczzItem.grid.store.reload();
            } else {
                alertFail(result.errMsg);
            }	
        },
        failure: function(response, options){
            TrainNoSelectWin.loadMask.hide();
            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
        }
    })
   }
   
   // 表格数据加载前的参数设置
ZbglPczzItem.grid.store.on('beforeload', function(){
	ZbglPczzItem.searchParams.zbglPczzIDX = ZbglPczz.zbglPczzIdx;
	this.baseParams.entityJson = Ext.util.JSON.encode(ZbglPczzItem.searchParams);
   });
});