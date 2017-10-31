/**
 * 配件上车领用明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	//配件选择窗口,数据容器
	Ext.namespace("PartsFixRegisterDetail");
	PartsFixRegisterDetail.fieldWidth = 200;
	PartsFixRegisterDetail.labelWidth = 100;
	PartsFixRegisterDetail.store=new Ext.data.JsonStore({
       id:'idx',totalProperty:"totalProperty", autoLoad:true, pruneModifiedRecords: true,
        url: ctx + "/partsFixRegister!pageList.action",
	    fields: [ "idx","partsName","partsNo","specificationModel"]
    });
    PartsFixRegisterDetail.loadMask=new Ext.LoadMask(Ext.getBody(),{msg:'正在处理，请稍后...'});
    //配件报废行选择模式，多选
    PartsFixRegisterDetail.sm=new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    PartsFixRegisterDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsFixRegisterDetail.store});
    PartsFixRegisterDetail.grid=new Ext.grid.EditorGridPanel({
       border: false, enableColumnMove: true, stripeRows: true, selModel: PartsFixRegisterDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	     colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
       	         {
					header:'idx主键', dataIndex:'idx', hidden:true,editor: { xtype:'hidden' }
				},{
					sortable:false,header:'配件编号', dataIndex:'partsNo'
				},{
					sortable:false,header:'配件识别码', dataIndex:'identificationCode'
				},{
					sortable:false,header:'配件名称', dataIndex:'partsName'
				},{
					sortable:false,header:'规格型号', dataIndex:'specificationModel'
				},{ 
					sortable:false, header: "上车日期",  dataIndex: "aboardDate",xtype: "datecolumn",format: "Y-m-d",editor:{xtype:'my97date', format: "Y-m-d"}
				},{ 
					sortable:false, header: "上车位置",  dataIndex: "aboardPlace",maxLength: 100,
	    			editor:{maxLength: 100}
				}]),
	           store:PartsFixRegisterDetail.store,
	           tbar: [{
				        xtype:"label", text:"  配件编号： " 
				    },{
				        id:"detail_partsNo",xtype: "textfield" ,maxLength: 100
				    },{ text:'添加', iconCls:'addIcon', handler:function(){
				    	//表单验证是否通过
				        var whForm = PartsFixRegister.form.getForm(); 
				        if (!whForm.isValid()) return;
				    	var partsNo = Ext.getCmp("detail_partsNo").getValue();
				    	if(partsNo == ""){
				    		MyExt.Msg.alert("请输入配件编号！");
				    		return ;
				    	}
				    	var searchJson = {identificationCode : partsNo};
				    	 Ext.Ajax.request({
				            url: ctx + '/partsFixRegister!getPartsAccount.action',
				            params: { searchJson: Ext.util.JSON.encode(searchJson) },
				            success: function(response, options){
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                    var entity = result.account;
				                    var count=PartsFixRegisterDetail.grid.store.getCount();
				                    	if(null != entity){
				                    		var record_v = new Ext.data.Record();
				                    			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
					           	          		if(count!=0){
					           	          			for(var i=0;i<count;i++){
					           	          				var record=PartsFixRegisterDetail.grid.store.getAt(i);
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
												record_v.set("aboardDate",new Date());
				                    			PartsFixRegisterDetail.grid.store.insert(0, record_v); 
										        PartsFixRegisterDetail.grid.getView().refresh(); 
										        PartsFixRegisterDetail.grid.getSelectionModel().selectRow(0);
				                    		}
				                } else {
				                    MyExt.Msg.alert(result.errMsg);
				                }
				            },
				            failure: function(response, options){
				                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				            }
		        });
			
			        }},{ text:'批量添加明细', iconCls:'chart_attributeConfigIcon', handler:function(){
			           	        PartsFixRegisterDetail.batchWin.show();
			           	        PartsAccount.grid.store.load();
						
			        }},{ text:'删除明细', iconCls:'deleteIcon', handler:function(){
				        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
			//	        if(!this.beforeDeleteFn()) return;
				        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
				        var data = PartsFixRegisterDetail.grid.selModel.getSelections();
				        if(data.length == 0 ){
				        	MyExt.Msg.alert("尚未选择一条记录！");
				            return;
				        }
				        var storeAt = PartsFixRegisterDetail.grid.store.indexOf(data[0]);
				        var count_v = PartsFixRegisterDetail.grid.store.getCount(); 
				        var j = storeAt + 1;
				        if(count_v-1 == storeAt){
				        	j = storeAt-1;
				        }
					    PartsFixRegisterDetail.grid.getSelectionModel().selectRow(j);
					    for (var i = 0; i < data.length; i++){
					        PartsFixRegisterDetail.grid.store.remove(data[i]);
					    }
					    PartsFixRegisterDetail.grid.getView().refresh(); 
		        }},{
			    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
			    }]
    });
	PartsFixRegisterDetail.grid.store.on("beforeload", function(){
		var beforeloadParam = {idx: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	//移除事件
	PartsFixRegisterDetail.grid.un('rowdblclick',PartsFixRegisterDetail.grid.toEditFn,PartsFixRegisterDetail.grid);
	//登账并新增
	PartsFixRegisterDetail.saveFun = function(){
		var form = PartsFixRegister.form.getForm();
	    if (!form.isValid()) return;
	    var regData = form.getValues();
		var record = PartsFixRegisterDetail.grid.store.getModifiedRecords();
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			data.aboardDate = data.aboardDate.format('Y-m-d');
			data.aboardTrainTypeIdx = regData.aboardTrainTypeIdx ; 
			data.aboardTrainType = regData.aboardTrainType ; 
			data.aboardRepairTimeIdx = regData.aboardRepairTimeIdx ; 
			data.aboardRepairTime = regData.aboardRepairTime ; 
			data.aboardTrainNo = regData.aboardTrainNo ; 
			data.aboardRepairClassIdx = regData.aboardRepairClassIdx ; 
			data.aboardRepairClass = regData.aboardRepairClass ; 
			data.rdpIdx = regData.rdpIdx ; 
			data.fixEmpId = regData.fixEmpId ; 
			data.fixEmp = regData.fixEmp ; 
			datas.push(data);
		}
		
        Ext.Ajax.request({
            url: ctx + '/partsFixRegister!saveFixRegisterBatch.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    PartsFixRegisterDetail.grid.store.removeAll();
                    PartsFixRegisterDetail.grid.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	PartsFixRegisterDetail.batchWin=new Ext.Window({
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