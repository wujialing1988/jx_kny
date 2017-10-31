/**
 * 领旧明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	//配件选择窗口,数据容器
	Ext.namespace("PartsWHRegisterDetail");
	PartsWHRegisterDetail.fieldWidth = 200;
	PartsWHRegisterDetail.labelWidth = 100;
	PartsWHRegisterDetail.rowIndex = "" ;
	
	PartsWHRegisterDetail.store=new Ext.data.JsonStore({
	   	id:'idx',totalProperty:'totalProperty',autoLoad:true, pruneModifiedRecords: true,
      	url:ctx+'/partsWHRegister!pageList.action',
 	    fields:['idx','partsNo','configDetail','partsName','specificationModel','partsAccountIdx']
	});
	
    PartsWHRegisterDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
      //行选择模式
    PartsWHRegisterDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    PartsWHRegisterDetail.grid=new Ext.grid.EditorGridPanel({
       border: false, enableColumnMove: true, stripeRows: true, selModel: PartsWHRegisterDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	     colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	      	{
	        	sortable:false,dataIndex:'idx',hidden:true,header:'idx'
	        },{
	       		sortable:false,dataIndex:'partsName',header:'配件名称'//,editor:{disabled:true}
	        },{
	           	sortable:false,dataIndex:'specificationModel',header:'规格型号'//,editor:{disabled:true}
	        },{
	        	sortable:false,dataIndex:'partsNo',header:'配件编号',
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},		        	
	        	editor:{xtype:'textfield'}
	        },{
	        	sortable:false,dataIndex:'identificationCode',header:'配件识别码',
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},		        	
	        	editor:{xtype:'textfield'}
	        },{ 
	        	sortable:false,dataIndex:'locationName',header:'存放位置',
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},		        	
	        	editor:{xtype:'textfield'}
	        },{
	        	sortable:false,dataIndex:'configDetail',header:'详细配置',
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},		        	
	        	editor:{xtype:'textarea'}
	        },{
	        	sortable:false,dataIndex:'billType',header:'入库类型',hidden:true,
	        	editor:{
	        			id:"billType",
						xtype:'combo',
			            fieldLabel: '入库类型',
			            store:new Ext.data.SimpleStore({
			               fields: ['value', 'text'],
                            data : [
                            	[BILLTYPE_SELF, '自修'],
                            	[BILLTYPE_OUTSOURCING, '委外']
                            ] 
			            }),
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            allowBlank: false,
			            mode:'local'
				},renderer: function(v,m){
					m.css = 'x-grid-col';
					if (v == BILLTYPE_SELF) return '自修';
					if (v == BILLTYPE_OUTSOURCING) return '委外';
				}
	        },{	
	        	sortable:false,dataIndex:'partsAccountIdx',header:'配件信息主键',hidden:true
	        },{	
	        	sortable:false,dataIndex:'partsTypeIDX',header:'配件型号表主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'repairedPartsWhIdx',header:'修竣配件入库主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'oldPartsNo',header:'老配件编号',hidden:true
	        }]),
	        store:PartsWHRegisterDetail.store,
	        tbar:['配件编号：',{xtype:'textfield',id:'detail_partsNo',width:120},
	             {text:'添加',iconCls:'addIcon',handler:function(){
							  //表单验证是否通过
				        var whForm = PartsWHRegister.form.getForm(); 
				        if (!whForm.isValid()) return;
				    	var partsNo = Ext.getCmp("detail_partsNo").getValue();
				    	if(partsNo == ""){
				    		MyExt.Msg.alert("请输入配件编号！");
				    		return ;
				    	}
				    	var searchJson = {identificationCode : partsNo};
				    	 Ext.Ajax.request({
				            url: ctx + '/partsWHRegister!getPartsAccount.action',
				            params: { searchJson: Ext.util.JSON.encode(searchJson) },
				            success: function(response, options){
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                   var entity = result.account;
				                    var count=PartsWHRegisterDetail.grid.store.getCount();
				                    	if(null != entity){
				                    		var record_v = new Ext.data.Record();
												//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
					           	          		if(count!=0){
					           	          			for(var i=0;i<count;i++){
					           	          				var record=PartsWHRegisterDetail.grid.store.getAt(i);
					           	          				if(entity.partsNo==record.get('oldPartsNo')){
					           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('oldPartsNo')+"】</font>已存在列表中，请不要重复添加");
					           	          					return ;
					           	          				}
					           	          			}
					           	          		}
												record_v.set("partsAccountIdx",entity["idx"]);
												record_v.set("partsTypeIDX",entity["partsTypeIDX"]);
												record_v.set("partsName",entity["partsName"]);
												record_v.set("specificationModel",entity["specificationModel"]);
												record_v.set("partsNo",entity["partsNo"]);
												record_v.set("configDetail",entity["configDetail"]);
												record_v.set("locationName",entity["location"]);
												record_v.set("identificationCode",entity["identificationCode"]);
												record_v.set("oldPartsNo",entity["partsNo"]);
												record_v.set("billType",BILLTYPE_SELF);
				                    			PartsWHRegisterDetail.grid.store.insert(0, record_v); 
										        PartsWHRegisterDetail.grid.getView().refresh(); 
										        PartsWHRegisterDetail.grid.getSelectionModel().selectRow(0);
				                    		}
				                    		
				                } else {
				                    MyExt.Msg.alert(result.errMsg);
				                }
				            },
				            failure: function(response, options){
				                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				            }
       				 });         	
	           } },'&nbsp;&nbsp;',
	           {text:'批量添加明细',iconCls:'chart_attributeConfigIcon',handler:function(){
	              		PartsWHRegisterDetail.batchWin.show();
	           	        PartsAccount.grid.store.load();
	           }},'&nbsp;&nbsp;',
	           {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	                var data = PartsWHRegisterDetail.grid.selModel.getSelections();
			        if(data.length == 0 ){
			        	MyExt.Msg.alert("尚未选择一条记录！");
			            return;
			        }
			        var storeAt = PartsWHRegisterDetail.grid.store.indexOf(data[0]);
			        var records = PartsWHRegisterDetail.store.getModifiedRecords();
			        var count = records.length; 
			        var j = storeAt + 1;
			        if(count-1 == storeAt){
			        	j = storeAt-1;
			        }
				    PartsWHRegisterDetail.grid.getSelectionModel().selectRow(j);
				    for (var i = 0; i < data.length; i++){
				        PartsWHRegisterDetail.grid.store.remove(data[i]);
				    }
				    PartsWHRegisterDetail.grid.getView().refresh();
	           }},{
		    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
		    }]
    });
	PartsWHRegisterDetail.grid.store.on("beforeload", function(){
		var beforeloadParam = {idx: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	//移除事件
	PartsWHRegisterDetail.grid.un('rowdblclick',PartsWHRegisterDetail.grid.toEditFn,PartsWHRegisterDetail.grid);
	//登帐并新增
	PartsWHRegisterDetail.saveFun = function(){
		var form = PartsWHRegister.form.getForm();
	    if (!form.isValid()) return;
	    var whData = form.getValues();
		var record = PartsWHRegisterDetail.grid.store.getModifiedRecords();
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			delete data.oldPartsNo;
			if(data.partsNo == undefined || data.partsNo == ''){
				MyExt.Msg.alert("配件编号不能为空！");
				return ;
			}
			for (var j = i+1; j < record.length; j++) {
				var data_v = {} ;
				data_v = record[j].data;
				if(data.partsNo == data_v.partsNo){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
//			if(data.identificationCode == undefined || data.identificationCode == ''){
//				MyExt.Msg.alert("识别码不能为空！");
//				return ;
//			}
			for (var j = i+1; j < record.length; j++) {
				var data_c = {} ;
				data_c = record[j].data;
				if(data.identificationCode != undefined && data.identificationCode != '' && data_c.identificationCode != undefined && data_c.identificationCode != '' && data.identificationCode == data_c.identificationCode){
					MyExt.Msg.alert("识别码不能重复！");
					return ;
				}
			}
			data.whIdx = whData.whIdx;
			data.whName = whData.whName;
			data.takeOverEmpId = whData.takeOverEmpId;
			data.takeOverEmp = whData.takeOverEmp;
			data.handOverEmpId = whData.handOverEmpId;
			data.handOverEmp = whData.handOverEmp;
			data.handOverOrgId = whData.handOverOrgId;
			data.handOverOrg = whData.handOverOrg;
			data.whTime = whData.whTime;
			datas.push(data);
		}
		PartsWHRegisterDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsWHRegister!savePartsWHRegisterBatch.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
            	PartsWHRegisterDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    PartsWHRegisterDetail.grid.store.removeAll();
                    PartsWHRegisterDetail.grid.getView().refresh();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
            	PartsWHRegisterDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	PartsWHRegisterDetail.batchWin=new Ext.Window({
     title:'修竣配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'west',
    	   width:280,
    	   layout:'fit',
    	   frame:true,
    	   items:[PartsTypeTreeSelect.panel]
    	},{
    	   region:'center',
    	   layout:'border',
    	   frame:true,
    	   items:[{
    	      region:'north',
    	      collapsible :true,
    	      height:100,
    	      title:'查询',
    	      items:[PartsAccount.batchForm]
    	   },{
    	      region:'center',
    	      frame:true,
    	      layout:'fit',
    	      items:[PartsAccount.grid]
    	   }]
    	}]
	});
});