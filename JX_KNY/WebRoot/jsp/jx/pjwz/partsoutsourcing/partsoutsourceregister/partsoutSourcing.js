Ext.onReady(function(){
    Ext.namespace("partsOutsourcing");
    
    //表单
    partsOutsourcing.form=new Ext.form.FormPanel({
       layout:'table',frame:true,baseCls:'x-plain',
       layoutConfig:{columns:4},margins:'10 0 5 20',
       labelAlign :'center',
        defaults:{bodyStyle:'padding:10px'},
       items:[{
          layout:'form',
          labelWidth:80,
          items:[{
		  		id:"outEmp_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,
			    fieldLabel : "经办人",hiddenName:'outEmpId',
			    returnField:[{
			    	widgetId:"outEmp",propertyName:"empname"
			    },{
			    	widgetId:"outOrgId",propertyName:"orgid"
			    },{
			    	widgetId:"outOrg",propertyName:"orgname"
			    }]
		  },{
				xtype:'hidden',id:'outEmp',fieldLabel:'经办人名称'
		  },{
				xtype:'hidden',id:'outOrgId',fieldLabel:'经办人机构'
		  },{
				xtype:'hidden',id:'outOrg',fieldLabel:'经办人机构名称'
		  }]
       },{
          layout:'form',
          labelWidth:80,
          labelAlign :'center',
          items:[{xtype:'my97date',id:'outsourcingDate',allowBlank:false,width:125,fieldLabel:'委外日期'}]
       },{
          layout:'form',
          labelWidth:80,
          items:[
                 {xtype:'textfield',id:'outsourcingFactory',fieldLabel:'委外厂家',hidden:true},
          	     {xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家',allowBlank:false,
          	        displayField:'factoryName',valueField:'id',
          	        hiddenName:'outsourcingFactoryId',editable:false,id:'PartsOutSourcingFactory_select',//outsourcingFactoryId
          	        returnField:[{widgetId:'outsourcingFactory',propertyName:'factoryName'}]
          	     }
         		]
       },{
          layout:'form',
          labelWidth:80,
          items:[{xtype:'textfield',id:'carNo',fieldLabel:'车牌号'}]
       }]
    });
    partsOutsourcing.store=new Ext.data.JsonStore({
      id:'idx',totalProperty:'totalProperty',autoLoad:true, pruneModifiedRecords: true,
      url:ctx+'/partsOutsourcing!pageList.action',
      fields:['idx','partsOutNo','extendNoJson','partsAccountIDX','partsName','specificationModel',
                'outsourcingReasion','repairContent','unit']
    });
    partsOutsourcing.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
    //行选择模式
    partsOutsourcing.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    partsOutsourcing.grid=new Ext.grid.EditorGridPanel({
         border: false, enableColumnMove: true, stripeRows: true, selModel: partsOutsourcing.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	        {
	        	sortable:false,dataIndex:'idx',hidden:true,header:'idx'
	        },{
	        	sortable:false,dataIndex:'partsOutNo',header:'配件编号'//,editor:{disabled:true}
	        },{
	        	sortable:false,dataIndex:'identificationCode',header:'配件识别码'//,editor:{disabled:true}
	        },{
	        	sortable:false,dataIndex:'partsAccountIDX',header:'配件信息主键',hidden:true//,editor:{disabled:true}
	        },{
	       		sortable:false,dataIndex:'partsName',header:'配件名称'//,editor:{disabled:true}
	        },{
	           	sortable:false,dataIndex:'specificationModel',header:'规格型号'//,editor:{disabled:true}
	        },{
	          	sortable:false,dataIndex:'outsourcingReasion',header:'委外检修原因',
	          	editor:{xtype:'textarea', maxLength: 200}
	        },{	
	        	sortable:false,dataIndex:'repairContent',header:'修理内容',
	        	editor:{xtype:'textarea'}
	        },{	
	        	sortable:false,dataIndex:'partsTypeIdx',header:'配件型号表主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'unit',header:'计量单位',hidden:true,editor:{disabled:true}
	        }]),
	    store:partsOutsourcing.store,
	    tbar:['配件编号：',{xtype:'textfield',id:'partsNo1',width:140},'&nbsp;&nbsp;',
	           {text:'添加',iconCls:'addIcon',handler:function(){
	              var partsNo=Ext.getCmp("partsNo1").getValue();
	           	       if(partsNo==""){
	           	       	  MyExt.Msg.alert("请输入配件编号!");
	           	       	  return ;
	           	       }
	           	       var searchJson = {identificationCode : partsNo};
	           	       Ext.Ajax.request({
	           	          url:ctx+'/partsOutsourcing!getPartsAccount.action',
	           	          params: { searchJson: Ext.util.JSON.encode(searchJson) },
	           	          success:function(response,options){
	           	          	var result=Ext.util.JSON.decode(response.responseText);
	           	          	if(result.success){
	           	          		var entity=result.account;
	           	          		var partsAccountIDX=entity.idx;//配件信息主键
	           	          		var partsTypeIdx=entity.partsTypeIDX;//配件规格型号主键
	           	          		var partsNo=entity.partsNo;//配件编号
	           	          		var extendNoJson=entity.extendNoJson;//扩展编号
	           	          		var partsName=entity.partsName;//配件名称
	           	          		var specificationModel=entity.specificationModel;//规格型号
	           	          		var identificationCode=entity.identificationCode;//识别码
	           	          		var unit=entity.unit;//计量单位
	           	          		var count=partsOutsourcing.grid.store.getCount();
	           	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
	           	          		if(count!=0){
	           	          			for(var i=0;i<count;i++){
	           	          				var record=partsOutsourcing.grid.store.getAt(i);
	           	          				if(entity.partsNo==record.get('partsOutNo')){
	           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsOutNo')+"】</font>已存在列表中，请不要重复添加");
	           	          					return ;
	           	          				}
	           	          			}
	           	          		}
	           	          		var defaultData = {partsAccountIDX:partsAccountIDX,partsOutNo:partsNo,extendNoJson:extendNoJson,partsName:partsName,specificationModel:specificationModel,identificationCode:identificationCode,unit:unit,partsTypeIdx:partsTypeIdx};
								var initData = Ext.apply({}, defaultData); 
								var record = new Ext.data.Record(defaultData);
						        partsOutsourcing.grid.store.insert(0, record); 
						        partsOutsourcing.grid.getView().refresh(); 
						        partsOutsourcing.grid.getSelectionModel().selectRow(0);
	           	          	}else{
	           	          		MyExt.Msg.alert(result.errMsg);
	           	          	}
	           	          },
	           	          failure: function(response, options){
                   			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                          }
	           	       });
	           	
	           } },'&nbsp;&nbsp;',
	           {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
	               
	           	   var madeFactoryId = Ext.getCmp("PartsOutSourcingFactory_select").getValue();
	           	   if(madeFactoryId==""){
	           	   	MyExt.Msg.alert("请选择委外厂家");
	           	      return ;
	           	   }
	           	   PartsOutSourcingTypeTreeSelect.loadOutsSourcingTree(madeFactoryId);
	           	   PartsAccountForOutSourcing.grid.store.load();
	           	   PartsAccountForOutSourcing.batchWin.show();
	           }},'&nbsp;&nbsp;',
	           {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	                var data=partsOutsourcing.grid.selModel.getSelections();
	                if(data.length<1){
	                  MyExt.Msg.alert("尚未选择一条记录！");
	                 return ;
	                }
	                var storeAt = partsOutsourcing.grid.store.indexOf(data[0]);
			        var records = partsOutsourcing.store.getModifiedRecords();
			        var count = records.length; 
			        var j = storeAt + 1;
			        if(count-1 == storeAt){
			       		j = storeAt-1;
			        }
				   partsOutsourcing.grid.getSelectionModel().selectRow(j);
	                for(var i=0;i<data.length;i++){
	                    var record=data[i];
	                    partsOutsourcing.grid.store.remove(record);
	                }
	                 partsOutsourcing.grid.getView().refresh();
	           }}]
    });
    
	var viewport=new Ext.Viewport({layout:'fit',items:[{
		 layout:'border',
		 frame:true,
		 items:[{
		    region:'north',
		    baseCls:'x-plain',
		    height:60,
		    items:[partsOutsourcing.form]
		 },{
		    region:'center',
		    layout:'fit',
		    frame:true,
		    items:[partsOutsourcing.grid]
		 }],
		 buttonAlign:'center',
		 buttons:[{xtype:'button',text:'登帐并新增',handler:function(){
		 	//获取表单数据
		     var form=partsOutsourcing.form.getForm();
		     if (!form.isValid()) return;
		     var formData=form.getValues();
		     //获取表格数据
		     var records=partsOutsourcing.grid.store.getModifiedRecords();
		     var datas=new Array();
		     if(records.length>0){
		       for(var i=0;i<records.length;i++){
		           var data=records[i].data;
		           data.outEmpId = formData.outEmpId;
				   data.outEmp = formData.outEmp;
		           data.outOrgId = formData.outOrgId;
				   data.outOrg = formData.outOrg;
		           data.outsourcingDate = formData.outsourcingDate;
				   data.outsourcingFactoryId = formData.outsourcingFactoryId;
		           data.outsourcingFactory = formData.outsourcingFactory;
				   data.carNo = formData.carNo;
		           datas.push(data);
		       }
		     }else{
		     	return ;
		     }
		     Ext.Ajax.request({
		      url:ctx+'/partsOutsourcing!saveBatchPartsOutsourcing.action',
		      params : {registerDatas : Ext.util.JSON.encode(datas)},
		      success:function(response,options){
    	  	var result=Ext.util.JSON.decode(response.responseText);
    	  	if(result.errMsg==null){
    	  	      alertSuccess();
                  partsOutsourcing.grid.store.removeAll();
                  partsOutsourcing.grid.getView().refresh();
            } else {
                  alertFail(result.errMsg);
            }
    	  },
    	  failure: function(response, options){
             MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
          }
		     });
		 }}]
	}]});
	partsOutsourcing.init = function(){
		Ext.getCmp("outEmp_select").setDisplayValue(empId,empName);
		Ext.getCmp("outEmp").setValue(empName);
		Ext.getCmp("outOrg").setValue(empOrg);
		Ext.getCmp("outOrgId").setValue(empOrgId);
	};
	partsOutsourcing.init();
});