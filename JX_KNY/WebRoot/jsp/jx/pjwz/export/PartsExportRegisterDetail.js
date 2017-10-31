Ext.onReady(function(){
    Ext.namespace("PartsExportRegisterDetail");
    
    //调出配件明细store
    PartsExportRegisterDetail.store=new Ext.data.JsonStore({
       id:'idx',totalProperty:"totalProperty", autoLoad:true, pruneModifiedRecords: true,
        url: ctx + "/partsExportRegister!pageList.action",
	    fields: [ "idx","partsTypeIDX","partsNo","location" ]
    });
    
    PartsExportRegisterDetail.loadMask=new Ext.LoadMask(Ext.getBody(),{msg:'正在处理，请稍后...'});
    
    //配件报废行选择模式，多选
    PartsExportRegisterDetail.sm=new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    PartsExportRegisterDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsExportRegisterDetail.store});
    
    //配件明细grid
    PartsExportRegisterDetail.grid=new Ext.grid.GridPanel({
      border: false, 
      enableColumnMove: true, 
      stripeRows: true,
      selModel: PartsExportRegisterDetail.sm,
      loadMask:true,
      viewConfig: {forceFit: true},
       colModel: new Ext.grid.ColumnModel( [
                  new Ext.grid.RowNumberer(),
       	       {
	             	sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	         		sortable:false,header:'配件名称',dataIndex:'partsName',editor:{}
	           },{  
	                sortable:false,header:'配件识别码',dataIndex:'identificationCode',editor:{} 
	           },{
	          		sortable:false,header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         	 	sortable:false,header:'存放地点',dataIndex:'location',editor:{}
	           },{
	         	 	sortable:false,header:'下车车型',dataIndex:'unloadTraintype',editor:{}
	           },{
	         	 	sortable:false,header:'下车车号',dataIndex:'unloadTrainNo',editor:{}
	           },{
	         	 	sortable:false,header:'下车修程',dataIndex:'unloadRepairClass',editor:{}
	           },{
	         	 	sortable:false,header:'下车修次',dataIndex:'unloadRepairTime',editor:{}
	           },{
	         	 	sortable:false,header:'配件状态',dataIndex:'partsName',editor:{},hidden:true
	           },{
	         	 	sortable:false,header:'配件状态',dataIndex:'partsStatus',editor:{},hidden:true
	           },{
	         		sortable:false,header:'是否新品',dataIndex:'isNewParts',editor:{}
	           },{
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{ 
	           	    sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门id',dataIndex:'manageDeptId',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门',dataIndex:'manageDept',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'责任部门类型',dataIndex:'manageDeptType',hidden:true,editor:{}
	           }]),
	           store:PartsExportRegisterDetail.store,
	           tbar:['配件编号:',{xtype:'textfield',width:150,id:'billNo_bill'},'&nbsp;&nbsp;',
	           	     {text:'添加',iconCls:'addIcon',handler:function(){
	           	       var partsNo = Ext.getCmp("billNo_bill").getValue();
	           	       if(partsNo == ""){
	           	       	  MyExt.Msg.alert("请输入配件编号!");
	           	       	  return ;
	           	       }
	           	       var searchJson = {identificationCode : partsNo};
	           	       Ext.Ajax.request({
	           	          url:ctx+'/partsExportRegister!getPartsAccount.action',
	           	          params:{searchJson: Ext.util.JSON.encode(searchJson)},
	           	          success:function(response,options){
	           	          	var result=Ext.util.JSON.decode(response.responseText);
	           	          	if(result.success){
	           	          		var entity=result.account;
	           	          		var count=PartsExportRegisterDetail.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var i=0;i<count;i++){
	           	          				var record=PartsExportRegisterDetail.grid.store.getAt(i);
	           	          				if(entity.partsNo==record.get('partsNo')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			}
	           	          		}
	           	          		var defaultData = entity;
								var initData = Ext.apply({}, defaultData); 
								var record = new Ext.data.Record(defaultData);
						        PartsExportRegisterDetail.grid.store.insert(0, record); 
						        PartsExportRegisterDetail.grid.getView().refresh(); 
						        PartsExportRegisterDetail.grid.getSelectionModel().selectRow(0);
	           	          	}else{
	           	          		MyExt.Msg.alert(result.errMsg);
	           	          	}
	           	          },
	           	          failure: function(response, options){
                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                          }
	           	       });
	           	     }},'&nbsp;&nbsp;',
	           	     {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
	           	     	PartsAccountForExportRegister.grid.store.load();
	           	        PartsAccountForExportRegister.batchWin.show();
	           	     	
	           	     }},'&nbsp;&nbsp;',
	           	     {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	           	     
	           	     	var data=PartsExportRegisterDetail.grid.selModel.getSelections();
	           	     	if(data.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                   var storeAt = PartsExportRegisterDetail.grid.store.indexOf(data[0]);
				       var records = PartsExportRegisterDetail.store.getModifiedRecords();
				       var count = records.length; 
				       var j = storeAt + 1;
				       if(count-1 == storeAt){
				       		j = storeAt-1;
				       }
					   PartsExportRegisterDetail.grid.getSelectionModel().selectRow(j);
	                   for(var i=0;i<data.length;i++){
	                   	var record=data[i];
	                     PartsExportRegisterDetail.grid.store.remove(record); 
	                   }
	                     PartsExportRegisterDetail.grid.getView().refresh();
	           	     }}
	           	    ],
	           bbar: []
    });
    
/*    PartsExportRegisterDetail.store.on('beforeload',function(){
    	var beforeloadParam = {partsExportIdx: ""###""};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
    });*/
    
    //新增并登账按钮事件
    PartsExportRegisterDetail.savePartsExport=function(){
    	var records=PartsExportRegisterDetail.grid.store.getModifiedRecords();//获取表格中的行数据
    	if(records.length<1){
    	   MyExt.Msg.alert("列表中尚未有登账的数据！");
    	   return ;
    	}
    	var datas=new Array();
    	for(var i=0;i<records.length;i++){
    		var data={};
    		 //records[i].data.idx="";
    		data=records[i].data;
    		datas.push(data);
    	}
    	var form=PartsExportRegister.form.getForm();//获取表单中的数据
    	if (!form.isValid()) return;
    	var formData=form.getValues();
    	formData.creatorName=empName;
    	delete formData.billNo_bill;
    	Ext.Ajax.request({
    	  url:ctx+'/partsExportRegister!savePartdExportRegister.action',
    	  jsonData:datas,
    	  params:{formData:Ext.util.JSON.encode(formData)},
    	  success:function(response,options){
    	  	var result=Ext.util.JSON.decode(response.responseText);
    	  	if(result.errMsg==null){
    	  	      alertSuccess();
                  PartsExportRegisterDetail.store.removeAll();
                  PartsExportRegisterDetail.grid.getView().refresh();
            } else {
                  alertFail(result.errMsg);
            }
    	  },
    	  failure: function(response, options){
             MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
          }
    	});
    }
});