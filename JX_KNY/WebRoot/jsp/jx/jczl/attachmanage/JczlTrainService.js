/**
 * 机车信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JczlTrainService');                       //定义命名空间
JczlTrainService.searchParam = {} ;
JczlTrainService.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/jczlTrain!jczlTrainList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/jczlTrain!saveOrUpdateTransfer.action',             //保存数据的请求URL
    deleteURL: ctx + '/jczlTrain!logicDelete.action',            //删除数据的请求URL
    saveFormColNum: 2 , searchFormColNum:2,
//    viewConfig: null,    
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型主键', dataIndex:'trainTypeIDX', hidden:true, editor:{
			fieldLabel: "车辆车型", id:"trainType_comb", 
			allowBlank:false ,
			hiddenName: "trainTypeIDX",
			xtype: "Base_combo",
		    business: 'trainVehicleType',
		    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
            fields:['idx','typeName','typeCode'],
            queryParams: {'vehicleType':vehicleType},// 根据车辆类型查询所对应的车辆种类
		    displayField: "typeCode", valueField: "idx",
		    returnField: [{widgetId:"trainTypeShortNameId",propertyName:"typeCode"}],
            pageSize: 0, minListWidth: 150,
            editable:true,
			listeners : {   
                "select" : function() {   
                    //重新加载组成型号数据
                    var buildUpType_combo = Ext.getCmp("buildUpType_combo");
                    buildUpType_combo.reset();
                    buildUpType_combo.clearValue();
                    Ext.getCmp("buildUpTypeCodeId").setValue("");
                    Ext.getCmp("buildUpTypeNameId").setValue("");
                    buildUpType_combo.queryParams = {"trainTypeIDX":this.getValue(),"recordStatus": "0","status":"20","type":"20"};
                    buildUpType_combo.cascadeStore();
                }   
            }
		}
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{ 			
			id:'trainTypeShortNameId', xtype:"hidden"
		},searcher:{}
	},{
		header:'车号', dataIndex:'trainNo', editor:{ minLength:4 , maxLength:5 , allowBlank: false},
		searcher: {xtype: 'textfield'}
	},{
		header:'组成型号主键', dataIndex:'buildUpTypeIDX', hidden: true, editor:{
			id:"buildUpType_combo", 
			xtype: 'Base_combo',
			fieldLabel: "组成型号",
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
					else{
						this.queryParams = {"trainTypeIDX":trainTypeIdx,"recordStatus": "0","status":"20","type":"20"};
						this.cascadeStore();
					}
                }   
            }
		}
	},{
		header:'组成型号编码', dataIndex:'buildUpTypeCode', hidden: true, editor:{id:"buildUpTypeCodeId",xtype:'hidden',  maxLength:3 }
	},{
		header:'组成型号', dataIndex:'buildUpTypeName',  hidden: true, editor:{id:"buildUpTypeNameId",xtype:'hidden',  maxLength:3 }
	},{
		header:'使用别ID', dataIndex:'trainUse', hidden: true, editor:{  
			id:"train_Use",
			xtype: 'Base_combo',fieldLabel: "使用别",
			hiddenName: 'trainUse',
			entity:'com.yunda.jx.base.jcgy.entity.TrainUse',			
			fields: ["useID","useName"],displayField:'useName',valueField:'useID'
		}
	},{
		header:'使用类别', dataIndex:'trainUseName', editor:{xtype:'hidden' }
	},{
		header:'制造厂家主键', dataIndex:'makeFactoryIDX',hidden: true,  editor:{ 
			 id:"Factory_Select",xtype:'GyjcFactory_SelectWin',fieldLabel: "制造厂家",
			 hiddenName:"makeFactoryIDX",editable:false,
			 queryHql:"From GyjcFactory where fcID='A'", 
			 returnField:[{widgetId:"makeFactoryNameId",propertyName:"fName"}]
		}
	},{
		header:'制造厂家', dataIndex:'makeFactoryName',  editor:{id:"makeFactoryNameId", xtype:'hidden' }
	},{
		header:'出厂日期', dataIndex:'leaveDate', xtype:'datecolumn', 
		editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
		searcher:{disabled: true}
	},{
		header:'配属局ID', dataIndex:'bId', hidden: true, editor:{
			id: "comboTree_bId", xtype: "BureauSelect_comboTree",
			hiddenName: "bId", fieldLabel: "配属局", 
		    selectNodeModel: "leaf",
            allowBlank : false,
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
		header:'配属段ID', dataIndex:'dId', hidden: true, editor:{
			id: "comboTree_dId", xtype: "DeportSelect_comboTree",
			hiddenName: "dId", fieldLabel: "配属段", 
		    selectNodeModel: "leaf" ,
            allowBlank : false,
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
		header:'配属局', dataIndex:'bName',
		editor:{xtype: "hidden"},
		searcher:{ xtype: "textfield" }
	},{
		header:'配属段', dataIndex:'dName',
		editor:{xtype: "hidden"},
		searcher:{ xtype: "textfield" }
	},
	//需求新增维护字段
	//配属日期、改配日期、改配单位、命令号、状态、报废日期、报废原因
	{
		header:'配属日期', dataIndex:'attachmentTime', xtype:'datecolumn', 
		editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
		searcher:{disabled: true}
	},{
		header:'改配日期', dataIndex:'reAttachmentTime', xtype:'datecolumn', 
		editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false },
		searcher:{disabled: true}
	},{
		header:'命令号', dataIndex:'orderNumber', editor:{maxLength:20,allowBlank: true },
		searcher: {disabled: true}
	},{
		header:'状态', dataIndex:'status', editor:{ maxLength:10,allowBlank: true },
		searcher: {disabled: true}
	},{
		header:'报废日期', dataIndex:'scrapTime', xtype:'datecolumn', 
		editor:{ xtype:'my97date',  my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: false ,allowBlank: true},
		searcher:{disabled: true}
	},{
		header:'报废原因', dataIndex:'scrapReason', editor:{ xtype:'textarea', maxLength:1000 ,allowBlank: true},
		searcher:{ disabled:true }
	},{
		header:'支配段', dataIndex:'useDId', hidden: true, editor:{
			id: "comboTree_dId1", xtype: "DeportSelect2_comboTree",
			hiddenName: "useDId", fieldLabel: "支配段", 
		    selectNodeModel: "leaf" ,allowBlank: true
		}
	},{
		header:'改配单位', dataIndex:'reAttachmentDeportId', hidden: true, editor:{
			id: "comboTree_dId2", xtype: "DeportSelect2_comboTree",
			hiddenName: "reAttachmentDeportId", fieldLabel: "改配单位", 
		    selectNodeModel: "leaf" ,allowBlank: true
		}
	},{
		header:'支配段名称', dataIndex:'useDName',
		editor:{xtype: "hidden",allowBlank: true},
		searcher:{ disabled:true }
	},{
		header:'改配单位名称', dataIndex:'reAttachmentDeportName',
		editor:{xtype: "hidden",allowBlank: true},
		searcher:{ disabled:true }
	},
	
	
	{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden: true,
		editor:{xtype: "hidden"}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden: true,
		editor:{xtype: "hidden"}
	},{
		header:'配属单位名称', dataIndex:'holdOrgName', hidden: true,
		editor:{xtype: "hidden"}
	},{
		header:'支配单位', dataIndex:'usedOrgId', hidden: true,
		editor:{xtype: "hidden"}
	},{
		header:'登记人', dataIndex:'registerPersonName',
		editor:{xtype: "hidden"},searcher:{disabled: true}
	},{
		header:'登记时间', dataIndex:'registerTime', xtype:'datecolumn',
		editor:{xtype: "hidden"},searcher:{disabled: true}
	},{
		header:'是否有履历', dataIndex:'isHaveResume', hidden: true,
		editor:{xtype: "hidden"},
		renderer:function(v){
			switch(v){
				case haveResume:
					return "是";
					break;
				case notHaveResume:
					return "否";
					break;
				default:
					return v;
					break;
			}
		},
		searcher:{ disabled:true }
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 },
		searcher:{ disabled:true }
	}],
	searchFn: function(searchParam){ 
		JczlTrainService.searchParam = searchParam ;
        this.store.load();
	},
	/**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
    afterShowEditWin: function(record, rowIndex){    	
    	Ext.getCmp("trainType_comb").setDisplayValue(record.get("trainTypeIDX"),record.get("trainTypeShortName"));
    	Ext.getCmp("buildUpType_combo").setDisplayValue(record.get("buildUpTypeIDX"),record.get("buildUpTypeName"));
    	Ext.getCmp("train_Use").setDisplayValue(record.get("trainUse"),record.get("trainUseName"));
    	Ext.getCmp("Factory_Select").setDisplayValue(record.get("makeFactoryIDX"),record.get("makeFactoryName"));
    	Ext.getCmp("comboTree_bId").setDisplayValue(record.get("bId"),record.get("bName"));
    	Ext.getCmp("comboTree_dId").setDisplayValue(record.get("dId"),record.get("dName"));
    	Ext.getCmp("comboTree_dId").orgid = record.get("bId");
		Ext.getCmp("comboTree_dId").orgname = record.get("bShortName");
		//支配段
		Ext.getCmp("comboTree_dId1").setDisplayValue(record.get("useDId"),record.get("useDName"));
		//改配单位
		Ext.getCmp("comboTree_dId2").setDisplayValue(record.get("reAttachmentDeportId"),record.get("reAttachmentDeportName"));
    },
    
     /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){ 
    	delete data.registerTime;
    	delete data.bShortName;
    	delete data.dShortName;
    	return true; 
    }
});
//查询前添加过滤条件
JczlTrainService.grid.store.on('beforeload' , function(){
	var searchParam = JczlTrainService.searchParam;
	searchParam = MyJson.deleteBlankProp(searchParam);
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:JczlTrainService.grid });

});