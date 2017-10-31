/**
 * 机车调拨明细---新配 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTransferDetailAdd');                       //定义命名空间
TrainTransferDetailAdd.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdateTransfer.action',             //保存数据的请求URL
    saveFormColNum: 2 ,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{
			id:"trainType_comb",xtype: "TrainType_combo", fieldLabel: "车型",
			hiddenName: "trainTypeIDX", allowBlank: false,
			returnField: [{widgetId:"trainTypeShortNameId",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 20, minListWidth: 200, editable:true,isCx:'no',
			listeners : {   
                "collapse" : function() {   
                    //重新加载组成型号数据
                    var buildUpType_combo = Ext.getCmp("buildUpType_combo");
                    buildUpType_combo.reset();
                    Ext.getCmp("buildUpType_combo").clearValue();
                    buildUpType_combo.queryParams = {"trainTypeIDX":this.getValue(),"recordStatus": "0","status":"20","type":"20"};
                    buildUpType_combo.cascadeStore();
                }   
            }
		}
	},{
		header:'车型名称', dataIndex:'trainTypeName', editor:{ id:"trainTypeShortNameId", xtype:'hidden', maxLength:20 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{ minLength:4 , maxLength:5 , allowBlank: false}
	},{
		header:'组成型号主键', dataIndex:'buildUpTypeIDX', editor:{
			id:"buildUpType_combo", 
			xtype: 'Base_combo',fieldLabel: "组成型号",
			hiddenName: 'buildUpTypeIDX',
			entity:'com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType',			
			fields: ["idx","buildUpTypeCode","buildUpTypeName"],displayField:'buildUpTypeName',valueField:'idx',
			returnField:[{widgetId:"buildUpTypeCodeId",propertyName:"buildUpTypeCode"},
						 {widgetId:"buildUpTypeNameId",propertyName:"buildUpTypeName"}],
			listeners : {   
                "beforequery" : function() {   
                    //获取所选车型的id
					var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
					if(trainTypeIdx==""||trainTypeIdx==null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
                }   
            }
		}
	},{
		header:'机车组成型号代码', dataIndex:'buildUpTypeCode', editor:{id:"buildUpTypeCodeId",xtype:'hidden',  maxLength:3 }
	},{
		header:'机车组成型号', dataIndex:'buildUpTypeName', editor:{id:"buildUpTypeNameId",xtype:'hidden',  maxLength:3 }
	},{
		header:'使用别ID', dataIndex:'trainUse', editor:{  
			id:"train_Use", allowBlank:false,
			xtype: 'Base_combo',fieldLabel: "使用别",
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID',
			returnField:[{widgetId:"trainUseNameId",propertyName:"useName"}]
		}
	},{
		header:'使用别', dataIndex:'trainUseName', editor:{id:"trainUseNameId", xtype:'hidden', maxLength:100 }
	},{
		header:'调入类型', dataIndex:'type', editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'调拨类型', dataIndex:'transferType', editor:{ xtype:'hidden', maxLength:1 }
	},{
		header:'部命令号', dataIndex:'ministryOrder', editor:{  maxLength:20}
	},{
		header:'局命令号', dataIndex:'bureauOrder', editor:{  maxLength:20 }
	},{
		header:'部命令日期', dataIndex:'ministryOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'局命令日期', dataIndex:'bureauOrderDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'制造厂家主键', dataIndex:'makeFactoryIDX',  editor:{ 
			 id:"Factory_Select",xtype:'GyjcFactory_SelectWin',fieldLabel: "制造厂家",
			 hiddenName:"makeFactoryIDX",editable:false,allowBlank:false,
			 queryHql:"From GyjcFactory where fcID='A'", 
			 returnField:[{widgetId:"makeFactoryNameId",propertyName:"fName"}]
		}
	},{
		header:'制造厂家', dataIndex:'makeFactoryName',  editor:{id:"makeFactoryNameId", xtype:'hidden' }
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'配属局ID', dataIndex:'bId', editor:{
			id: "comboTree_bId", xtype: "BureauSelect_comboTree",
			hiddenName: "bId", fieldLabel: "配属局", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "bName_add", propertyName: "text"},
			  			  {widgetId: "bShortName_add", propertyName: "orgname"}],
		    listeners : {
			  	"select" : function() {
			  		Ext.getCmp("comboTree_dId").reset();
	                Ext.getCmp("comboTree_dId").clearValue();
			  		Ext.getCmp("comboTree_dId").orgid = this.getValue();
			  		Ext.getCmp("comboTree_dId").orgname = this.lastSelectionText;
			  	}
			  }
		  }		
	},{
		header:'配属段ID', dataIndex:'dId', editor:{
			id: "comboTree_dId", xtype: "DeportSelect_comboTree",
			hiddenName: "dId", fieldLabel: "配属段", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "dName_add", propertyName: "text"},
			  			  {widgetId: "dShortName_add", propertyName: "orgname"}],
		    listeners : {
				"beforequery" : function(){
					//选择段前先选局
					var comboTree_bId =  Ext.getCmp("comboTree_bId").getValue();
					if(comboTree_bId == "" || comboTree_bId == null){
						MyExt.Msg.alert("请先选择配属局！");
						return false;
					}
				}
			}
		}
	},{
		header:'配属局名称', dataIndex:'bName', editor:{id:"bName_add", xtype:'hidden' }
	},{
		header:'配属局简称', dataIndex:'bShortName', editor:{id:"bShortName_add", xtype:'hidden', maxLength:300 }
	},{
		header:'配属段名称', dataIndex:'dName', editor:{id:"dName_add", xtype:'hidden', maxLength:512 }
	},{
		header:'配属段简称', dataIndex:'dShortName', editor:{id:"dShortName_add", xtype:'hidden', maxLength:300 }
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', editor:{
			id: "comboTree_holdOrgId", xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "holdOrgId", fieldLabel: "配属单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea", //只能选段级单位
		    returnField: [{widgetId: "holdOrgNameId", propertyName: "orgname"},
			  			  {widgetId: "holdOrgSeqId", propertyName: "orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', editor:{id:"holdOrgSeqId", xtype:'hidden', maxLength:512 }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', editor:{id:"holdOrgNameId", xtype:'hidden', maxLength:300 }
	},{
		header:'支配单位', dataIndex:'usedOrgId', editor:{ 
			id: "comboTree_usedOrgId", xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "usedOrgId", fieldLabel: "支配单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea" //只能选段级单位
		}
	},{
		header:'配属日期', dataIndex:'transferDate', xtype:'datecolumn', editor:{ xtype:'my97date' , allowBlank: false }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}],
	defaultData: {type: typeAdd, transferType: transferIn },  
	//处理新增打开之后的事件
	afterShowSaveWin: function(){
		this.saveWin.setTitle('新配机车');
	},
	afterSaveSuccessFn: function(result, response, options){
        JczlTrain.grid.store.reload();
        TrainTransferDetailAdd.grid.saveForm.find("name","trainNo")[0].setValue("");
        alertSuccess();
    }
});
});