/**
 * 机车调拨明细---新配 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainTransferDetailMoveIn');                       //定义命名空间
TrainTransferDetailMoveIn.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainTransferDetail!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainTransferDetail!saveOrUpdateMoveIn.action',             //保存数据的请求URL
    saveFormColNum: 2 ,
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', editor:{
			id:"trainType_comb_In",xtype: "TrainType_combo", fieldLabel: "车型",
			hiddenName: "trainTypeIDX", allowBlank: false,
			returnField: [{widgetId:"trainTypeShortNameId_In",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 20, minListWidth: 200, editable:true,isCx:'no',
			listeners : {   
                "collapse" : function() {   
                	//重新加载车号数据
                	var trainNo_combo = Ext.getCmp("trainNo_comb_In");
                    trainNo_combo.reset();
                    Ext.getCmp("trainNo_comb_In").clearValue();
                    trainNo_combo.queryParams = {"trainTypeIDX":this.getValue(),"holdOrgId":'<>'+systemOrgid};
                    trainNo_combo.cascadeStore();
                    //重新加载组成型号数据
                    var buildUpType_combo = Ext.getCmp("buildUpType_combo_In");
                    buildUpType_combo.reset();
                    Ext.getCmp("buildUpType_combo_In").clearValue();
                    buildUpType_combo.queryParams = {"trainTypeIDX":this.getValue(),"recordStatus": "0","status":"20","type":"20"};
                    buildUpType_combo.cascadeStore();
                }   
            }
		}
	},{
		header:'车型名称', dataIndex:'trainTypeName', editor:{ id:"trainTypeShortNameId_In", xtype:'hidden', maxLength:20 }
	},{
		header:'车号', dataIndex:'trainNo', editor:{ 
			allowBlank:false ,
			id:"trainNo_comb_In",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", isIn:true,
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200, editable:true,
			returnField: [{widgetId:"oldHoldOrgId_In",propertyName:"holdOrgId"},
			              {widgetId:"oldHoldOrgSeq_In",propertyName:"holdOrgSeq"},
			              {widgetId:"oldHoldOrgName_In",propertyName:"holdOrgName"},
			              {widgetId:"makeFactoryNameId_In",propertyName:"makeFactoryName"},
			              {widgetId:"leaveDate_In",propertyName:"leaveDate"},
			              {widgetId:"buildUpTypeCodeId_In",propertyName:"buildUpTypeCode"},
			              {widgetId:"buildUpTypeNameId_In",propertyName:"buildUpTypeName"},
			              {widgetId:"trainUseNameId_In",propertyName:"trainUseName"}], 
			listeners : {   
                "beforequery" : function() {   
                    //获取所选车型的id
					var trainTypeIdx =  Ext.getCmp("trainType_comb_In").getValue();
					if(trainTypeIdx==""||trainTypeIdx==null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
                },
                "select" : function() {  
//                	alert(this.store.data.items[0].data.leaveDate);  //下拉选择框选中某一个值之后给其他特殊控制赋值的情况
                	var record = this.store.data.items[0] ;  //获取store记录
                	//设置组成型号数据
                    var buildUpType_combo = Ext.getCmp("buildUpType_combo_In"); //机车组成型号
                    buildUpType_combo.clearValue();
                    buildUpType_combo.setDisplayValue(record.get("buildUpTypeIDX"),record.get("buildUpTypeName"));
                    var trainUse_combo = Ext.getCmp("train_Use_In") ; //使用别
                    trainUse_combo.clearValue();
                    trainUse_combo.setDisplayValue(record.get("trainUse"),record.get("trainUseName"));
                }
            }
		}
	},{
		header:'组成型号主键', dataIndex:'buildUpTypeIDX', editor:{
			id:"buildUpType_combo_In", 
			xtype: 'Base_combo',fieldLabel: "组成型号",
			hiddenName: 'buildUpTypeIDX',
			entity:'com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType',			
			fields: ["idx","buildUpTypeCode","buildUpTypeName"],displayField:'buildUpTypeName',valueField:'idx',
			returnField:[{widgetId:"buildUpTypeCodeId_In",propertyName:"buildUpTypeCode"},
						 {widgetId:"buildUpTypeNameId_In",propertyName:"buildUpTypeName"}],
			listeners : {   
                "beforequery" : function() {   
                    //获取所选车型的id
					var trainTypeIdx =  Ext.getCmp("trainType_comb_In").getValue();
					if(trainTypeIdx==""||trainTypeIdx==null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
                }   
            }
		}
	},{
		header:'机车组成型号代码', dataIndex:'buildUpTypeCode', editor:{id:"buildUpTypeCodeId_In",xtype:'hidden',  maxLength:3 }
	},{
		header:'机车组成型号', dataIndex:'buildUpTypeName', editor:{id:"buildUpTypeNameId_In",xtype:'hidden',  maxLength:3 }
	},{
		header:'使用别ID', dataIndex:'trainUse', editor:{  
			id:"train_Use_In", allowBlank:false,
			xtype: 'Base_combo',fieldLabel: "使用别",
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID',
			returnField:[{widgetId:"trainUseNameId_In",propertyName:"useName"}]
		}
	},{
		header:'使用别', dataIndex:'trainUseName', editor:{id:"trainUseNameId_In", xtype:'hidden', maxLength:100 }
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
		header:'制造厂家', dataIndex:'makeFactoryName',  editor:{id:"makeFactoryNameId_In", disabled: true}
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', editor:{id:"leaveDate_In", xtype:'my97date',disabled: true }
	},{
		header:'配属局ID', dataIndex:'bId', editor:{
			id: "comboTree_bId_In", xtype: "BureauSelect_comboTree",
			hiddenName: "bId", fieldLabel: "配属局", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "bName_In", propertyName: "text"},
			  			  {widgetId: "bShortName_In", propertyName: "orgname"}],
		    listeners : {
			  	"select" : function() {
			  		Ext.getCmp("comboTree_dId_In").reset();
	                Ext.getCmp("comboTree_dId_In").clearValue();
			  		Ext.getCmp("comboTree_dId_In").orgid = this.getValue();
			  		Ext.getCmp("comboTree_dId_In").orgname = this.lastSelectionText;
			  	}
			  }
		  }		
	},{
		header:'配属段ID', dataIndex:'dId', editor:{
			id: "comboTree_dId_In", xtype: "DeportSelect_comboTree",
			hiddenName: "dId", fieldLabel: "配属段", allowBlank:false,
		    selectNodeModel: "leaf" ,
		    returnField: [{widgetId: "dName_In", propertyName: "text"},
			  			  {widgetId: "dShortName_In", propertyName: "orgname"}],
		    listeners : {
				"beforequery" : function(){
					//选择段前先选局
					var comboTree_bId =  Ext.getCmp("comboTree_bId_In").getValue();
					if(comboTree_bId == "" || comboTree_bId == null){
						MyExt.Msg.alert("请先选择配属局！");
						return false;
					}
				}
			}
		}
	},{
		header:'配属局名称', dataIndex:'bName', editor:{id:"bName_In", xtype:'hidden' }
	},{
		header:'配属局简称', dataIndex:'bShortName', editor:{id:"bShortName_In", xtype:'hidden', maxLength:300 }
	},{
		header:'配属段名称', dataIndex:'dName', editor:{id:"dName_In", xtype:'hidden', maxLength:512 }
	},{
		header:'配属段简称', dataIndex:'dShortName', editor:{id:"dShortName_In", xtype:'hidden', maxLength:300 }
	},{
		header:'配属单位', dataIndex:'holdOrgId', editor:{
			id: "comboTree_holdOrgId_In", xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "holdOrgId", fieldLabel: "配属单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea", //只能选段级单位
		    returnField: [{widgetId: "holdOrgNameId_In", propertyName: "orgname"},
			  			  {widgetId: "holdOrgSeqId_In", propertyName: "orgseq"}]
		}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', editor:{id:"holdOrgSeqId_In", xtype:'hidden', maxLength:512 }
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', editor:{id:"holdOrgNameId_In", xtype:'hidden', maxLength:300 }
	},{
		header:'支配单位ID', dataIndex:'usedOrgId', editor:{
			id: "comboTree_usedOrgId_In", xtype: "OmOrganizationCustom_comboTree",
			hiddenName: "usedOrgId", fieldLabel: "支配单位",allowBlank:false,
		    orgid: "0",orgname: orgRootName, selectNodeModel: "leaf" , queryHql:"[degree]oversea" //只能选段级单位
		}
	},{
		header:'原配属单位ID', dataIndex:'oldHoldOrgId', editor:{id:"oldHoldOrgId_In",xtype:'hidden'}
	},{
		header:'原配属单位部门序列', dataIndex:'oldHoldOrgSeq', editor:{id:"oldHoldOrgSeq_In", xtype:'hidden', maxLength:512 }
	},{
		header:'原配属单位', dataIndex:'oldHoldOrgName', editor:{id:"oldHoldOrgName_In", readOnly: true , maxLength:300 }
	},{
		header:'调入日期', dataIndex:'transferDate', xtype:'datecolumn', editor:{ xtype:'my97date' , allowBlank: false }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }
	}],
	defaultData: {type: typeTurn, transferType: transferIn },  
	//处理新增打开之后的事件
	afterShowSaveWin: function(){
		this.saveWin.setTitle('调入机车');
	},
	afterSaveSuccessFn: function(result, response, options){
		var trainNo_comb = Ext.getCmp("trainNo_comb_In") ;
		trainNo_comb.clearValue();
		trainNo_comb.cascadeStore();
        JczlTrain.grid.store.reload();
        alertSuccess();
    },
    afterSaveFailFn: function(result, response, options){
    	JczlTrain.grid.store.reload();
        alertFail(result.errMsg);
    }
});
});