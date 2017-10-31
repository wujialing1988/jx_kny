/**
 * 机车调拨明细---新配 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTransferDetailMoveOut');                       //定义命名空间
TrainTransferDetailMoveOut.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdateMoveOut.action',             //保存数据的请求URL
    saveFormColNum: 2 ,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{id:"trainTypeIDX_Out",xtype:'hidden'}
	},{
		header:'车型名称', dataIndex:'trainTypeName', editor:{id:"trainTypeName_Out", disabled:true , maxLength:20 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{id:"trainNo_Out", disabled:true }
	},{
		header:'调拨类型', dataIndex:'transferType', editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20}
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'配属局ID', dataIndex:'bId', editor:{
			id: "comboTree_bId_out", xtype: "BureauSelect_comboTree",
			hiddenName: "bId", fieldLabel: "配属局", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "bName_out", propertyName: "text"},
			  			  {widgetId: "bShortName_out", propertyName: "orgname"}],
		    listeners : {
			  	"select" : function() {
			  		Ext.getCmp("comboTree_dId_out").reset();
	                Ext.getCmp("comboTree_dId_out").clearValue();
			  		Ext.getCmp("comboTree_dId_out").orgid = this.getValue();
			  		Ext.getCmp("comboTree_dId_out").orgname = this.lastSelectionText;
			  	}
			  }
		  }		
	},{
		header:'配属段ID', dataIndex:'dId', editor:{
			id: "comboTree_dId_out", xtype: "DeportSelect_comboTree",
			hiddenName: "dId", fieldLabel: "配属段", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "dName_out", propertyName: "text"},
			  			  {widgetId: "dShortName_out", propertyName: "orgname"}],
		    listeners : {
				"beforequery" : function(){
					//选择段前先选局
					var comboTree_bId =  Ext.getCmp("comboTree_bId_out").getValue();
					if(comboTree_bId == "" || comboTree_bId == null){
						MyExt.Msg.alert("请先选择配属局！");
						return false;
					}
				}
			}
		}
	},{
		header:'配属局名称', dataIndex:'bName', editor:{id:"bName_out", xtype:'hidden' }
	},{
		header:'配属局简称', dataIndex:'bShortName', editor:{id:"bShortName_out", xtype:'hidden', maxLength:300 }
	},{
		header:'配属段名称', dataIndex:'dName', editor:{id:"dName_out", xtype:'hidden', maxLength:512 }
	},{
		header:'配属段简称', dataIndex:'dShortName', editor:{id:"dShortName_out", xtype:'hidden', maxLength:300 }
	},{
		header:'配属单位', dataIndex:'holdOrgId', editor:{
			id: "comboTree_holdOrgId_Out", xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "holdOrgId", fieldLabel: "接收单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea", //只能选段级单位
		    returnField: [{widgetId: "holdOrgNameId_Out", propertyName: "orgname"},
			  			  {widgetId: "holdOrgSeqId_Out", propertyName: "orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', editor:{id:"holdOrgSeqId_Out", xtype:'hidden', maxLength:512 }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', editor:{id:"holdOrgNameId_Out", xtype:'hidden', maxLength:300 }
	},{
		header:'现配属单位ID', dataIndex:'oldHoldOrgId', editor:{id:"oldHoldOrgId_Out",xtype:'hidden'}
	},{
		header:'现配属单位部门序列', dataIndex:'oldHoldOrgSeq', editor:{id:"oldHoldOrgSeq_Out", xtype:'hidden', maxLength:512 }
	},{
		header:'现配属单位', dataIndex:'oldHoldOrgName', editor:{id:"oldHoldOrgName_Out", disabled: true , maxLength:300 }
	},{
		header:'调出日期', dataIndex:'transferDate', xtype:'datecolumn', editor:{ xtype:'my97date' , allowBlank: false }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	},{
		header:'使用别', dataIndex:'trainUseName', editor:{id:"trainUseNameId_Out", xtype:'hidden', maxLength:100 }
	}],
	defaultData: { transferType: transferOut },  
	afterSaveSuccessFn: function(result, response, options){
        JczlTrain.grid.store.reload();
        alertSuccess();
        TrainTransferDetailMoveOut.grid.saveWin.hide();
    },
    beforeSaveFn: function(data){
    	if(data.holdOrgId == data.oldHoldOrgId){
    		MyExt.Msg.alert("【接收单位】不能为【现配属单位】！");
    		return false;
    	}
    	data.trainTypeName = Ext.getCmp("trainTypeName_Out").getValue();
    	data.trainNo = Ext.getCmp("trainNo_Out").getValue();
    	return true;
    },
    afterSaveFailFn: function(result, response, options){
    	JczlTrain.grid.store.reload();
        alertFail(result.errMsg);
        TrainTransferDetailMoveOut.grid.saveWin.hide();
    }
});
//创建调出窗口
TrainTransferDetailMoveOut.grid.createSaveWin();
});