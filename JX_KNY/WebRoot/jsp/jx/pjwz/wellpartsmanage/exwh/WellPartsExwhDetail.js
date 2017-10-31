Ext.onReady(function(){
	//配件选择窗口,数据容器
	Ext.namespace("WellPartsExwhDetail");
	WellPartsExwhDetail.fieldWidth = 200;
	WellPartsExwhDetail.labelWidth = 100;
	
	WellPartsExwhDetail.store=new Ext.data.JsonStore({
       id:'idx',totalProperty:"totalProperty", autoLoad:true, pruneModifiedRecords: true,
        url: ctx + "/wellPartsExwh!pageList.action",
	    fields: [ "idx","partsName","partsNo","specificationModel" ]
    });
    
    WellPartsExwhDetail.loadMask=new Ext.LoadMask(Ext.getBody(),{msg:'正在处理，请稍后...'});
    //配件选择模式，多选
    WellPartsExwhDetail.sm=new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    WellPartsExwhDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: WellPartsExwhDetail.store});
    WellPartsExwhDetail.grid=new Ext.grid.EditorGridPanel({
       border: false, enableColumnMove: true, stripeRows: true, selModel: WellPartsExwhDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	     colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
       	         {
					header:'idx主键', dataIndex:'idx', hidden:true,editor: { xtype:'hidden' }
				},{
					sortable:false,header:'配件编号', dataIndex:'partsNo'
				},{
		        	sortable:false,dataIndex:'identificationCode',header:'配件识别码'
		        },{
					sortable:false,header:'配件名称', dataIndex:'partsName'
				},{
					sortable:false,header:'规格型号', dataIndex:'specificationModel'
				},{
					sortable:false,dataIndex:'locationName',header:'存放位置'
		        },{
	        	sortable:false,dataIndex:'toGo',header:'出库去向',
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},
	        	editor:{
	        			id:"toGo",
						xtype:'combo',
			            fieldLabel: '出库去向',
			            store:new Ext.data.SimpleStore({
			               fields: ['value', 'text'],
                            data : [
                            	['上车', '上车'],
                            	['校验', '校验'],
                            	['检修', '检修']
                            ] 
			            }),
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            allowBlank: false,
			            editable:false,
			            mode:'local'
				}
	        },{
		        	sortable:false,dataIndex:'isDeliver',header:'是否配送',
			   	    renderer: function(v,m){
	     	   	   	 	m.css = 'x-grid-col';
	     	   	   	 	return v;
	     	   	   	},			        	
		        	editor:{
		        			id:"isDeliver",
							xtype:'combo',
				            fieldLabel: '是否配送',
				            store:new Ext.data.SimpleStore({
				               fields: ['value', 'text'],
	                            data : [
	                            	['是', '是'],
	                            	['否', '否']
	                            ] 
				            }),
				            triggerAction:'all',
				            emptyText:'请选择...',
				            valueField:'value',
				            displayField:'text',
				            allowBlank: false,
				            mode:'local',
				            listeners:{
		 				 	'collapse' : function(v,record, index){
		 				 		if("是" == v.value){
		 				 			Ext.getCmp("deliverLocation").setDisabled(false) ;
		 				 			Ext.getCmp("deliverTime").setDisabled(false) ;
		 				 		}else{
		 				 			Ext.getCmp("deliverLocation").setDisabled(true) ;
		 				 			Ext.getCmp("deliverTime").setDisabled(true) ;
		 				 		}
		 				 	}
		 				 }
					}
		        },{
		        	sortable:false,dataIndex:'deliverLocation',header:'配送地址',
			   	    renderer: function(v,m){
	     	   	   	 	m.css = 'x-grid-col';
	     	   	   	 	return v;
	     	   	   	},			        	
		        	editor:{id:"deliverLocation",xtype:'textarea',disabled:true}
		        },{
		        	sortable:false,dataIndex:'deliverTime',header:'配送时间',/*xtype: "datecolumn",format: "Y-m-d H:i",*/
		        	renderer: function(v,m){
	     	   	   	 	m.css = 'x-grid-col';
	     	   	   	 	var result = "" ;
	     	   	   	 	if(v){
	     	   	   	 		result = new Date(v).format("Y-m-d H:i");
	     	   	   	 	}
	     	   	   	 	return result;
	     	   	   	},	
		        	editor:{id:"deliverTime",xtype:'my97date', format: "Y-m-d H:i",disabled:true}
		        }]),
	           store:WellPartsExwhDetail.store,
	           tbar: [{
				        xtype:"label", text:"  配件编号： " 
				    },{
				        id:"detail_partsNo",xtype: "textfield" ,maxLength: 100
				    },{ text:'添加', iconCls:'addIcon', handler:function(){
				    	//表单验证是否通过
				        var whForm = WellPartsExwh.form.getForm(); 
				        if (!whForm.isValid()) return;
				    	var partsNo = Ext.getCmp("detail_partsNo").getValue();
				    	if(partsNo == ""){
				    		MyExt.Msg.alert("请输入配件编号！");
				    		return ;
				    	}
				    	var searchJson = {identificationCode : partsNo};
				    	 Ext.Ajax.request({
				            url: ctx + '/wellPartsExwh!getPartsAccount.action',
				            params: { searchJson: Ext.util.JSON.encode(searchJson) },
				            success: function(response, options){
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                    var entity = result.account;
				                    var count=WellPartsExwhDetail.grid.store.getCount();
				                    	if(null != entity){
				                    		var record_v = new Ext.data.Record();
				                    			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
					           	          		if(count!=0){
					           	          			for(var i=0;i<count;i++){
					           	          				var record=WellPartsExwhDetail.grid.store.getAt(i);
					           	          				if(entity.partsNo==record.get('partsNo')){
					           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
					           	          					return ;
					           	          				}
					           	          			}
					           	          		}
												record_v.set("partsAccountIDX",entity["idx"]);
												record_v.set("partsTypeIDX",entity["partsTypeIDX"]);
												record_v.set("partsName",entity["partsName"]);
												record_v.set("specificationModel",entity["specificationModel"]);
												record_v.set("partsNo",entity["partsNo"]);
												record_v.set("identificationCode",entity["identificationCode"]);
//												record_v.set("extendNoJson",entity["extendNoJson"]);
												record_v.set("locationName",entity["location"]);
												record_v.set("isDeliver","否");
												record_v.set("toGo","上车");
				                    			WellPartsExwhDetail.grid.store.insert(0, record_v); 
										        WellPartsExwhDetail.grid.getView().refresh(); 
										        WellPartsExwhDetail.grid.getSelectionModel().selectRow(0);
				                    		}
				                    		Ext.getCmp("whName_comb").disable();
				                } else {
				                    MyExt.Msg.alert(result.errMsg);
				                }
				            },
				            failure: function(response, options){
				                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				            }
		        });
			
			        }},{ text:'批量添加明细', iconCls:'chart_attributeConfigIcon', handler:function(){
			        			PartsAccount.manageDeptId = Ext.getCmp("WellPartsExwh_whIdx").getValue();
			           	        WellPartsExwhDetail.batchWin.show();
			           	        PartsAccount.grid.store.load();
						
			        }},{ text:'删除明细', iconCls:'deleteIcon', handler:function(){
				        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
			//	        if(!this.beforeDeleteFn()) return;
				        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
				        var data = WellPartsExwhDetail.grid.selModel.getSelections();
				        if(data.length == 0 ){
				        	MyExt.Msg.alert("尚未选择一条记录！");
				            return;
				        }
				        var storeAt = WellPartsExwhDetail.grid.store.indexOf(data[0]);
				        var count_v = WellPartsExwhDetail.grid.store.getCount(); 
				        var j = storeAt + 1;
				        if(count_v-1 == storeAt){
				        	j = storeAt-1;
				        }
					    WellPartsExwhDetail.grid.getSelectionModel().selectRow(j);
					    for (var i = 0; i < data.length; i++){
					        WellPartsExwhDetail.grid.store.remove(data[i]);
					    }
					    WellPartsExwhDetail.grid.getView().refresh(); 
					    var count = WellPartsExwhDetail.grid.store.getCount();
						if(count == 0){
							Ext.getCmp("whName_comb").enable();
						}
		        }},{
			    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
			    }]
    });
	WellPartsExwhDetail.grid.store.on("beforeload", function(){
		var beforeloadParam = {idx: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	//移除事件
	WellPartsExwhDetail.grid.un('rowdblclick',WellPartsExwhDetail.grid.toEditFn,WellPartsExwhDetail.grid);
	//登账并新增
	WellPartsExwhDetail.saveFun = function(){
		var form = WellPartsExwh.form.getForm();
	    if (!form.isValid()) return;
	    Ext.getCmp("whName_comb").enable();
	    var whData = form.getValues();
	    Ext.getCmp("whName_comb").disable();
		var record = WellPartsExwhDetail.grid.store.getModifiedRecords();
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			data.whIdx = whData.whIdx;
			data.whName = whData.whName;
			data.handOverEmpId = whData.handOverEmpId;
			data.handOverEmp = whData.handOverEmp;
			data.acceptEmp = whData.acceptEmp;
			data.acceptEmpId = whData.acceptEmpId;
			data.acceptOrgID = whData.acceptOrgID;
			data.acceptOrg = whData.acceptOrg;
			data.whTime = whData.whTime;
			if("是" == data.isDeliver){
				data.deliverTime = data.deliverTime.format('Y-m-d H:i');
			}else{
				delete data.deliverTime ;
				delete data.deliverLocation ;
			}
			datas.push(data);
		}
		
        Ext.Ajax.request({
            url: ctx + '/wellPartsExwh!saveWellPartsExwhBatch.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    WellPartsExwhDetail.grid.store.removeAll();
                    WellPartsExwhDetail.grid.store.load();
                    Ext.getCmp("whName_comb").enable();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	WellPartsExwhDetail.batchWin=new Ext.Window({
     title:'良好在库配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',
    	maximizable:false,  modal:true,
    	items:[{
    	   region:'west',
    	   width:280,
    	   layout:'fit',
    	   border:false,
    	   items:[PartsTypeTreeSelect.panel]
    	},{
    	   region:'center',
    	   layout:'border',
    	   border:false,
    	   items:[{
    	      region:'north',
    	      collapsible :true,
    	      layout:'fit',
    	      frame:true,
    	      height:90,
    	      title:'查询',
    	      items:[PartsAccount.batchForm]
    	   },{
    	      region:'center',
    	      layout:'fit',
    	      border:false,
    	      items:[PartsAccount.grid]
    	   }]
    	}]
});
});