/**
 * 领旧明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	//配件选择窗口,数据容器
	Ext.namespace("PartsCheckRegisterDetail");
	PartsCheckRegisterDetail.fieldWidth = 200;
	PartsCheckRegisterDetail.labelWidth = 100;
	PartsCheckRegisterDetail.rowIndex = "" ;
	
	PartsCheckRegisterDetail.store=new Ext.data.JsonStore({
	   	id:'idx',totalProperty:'totalProperty',autoLoad:true, pruneModifiedRecords: true,
      	url:ctx+'/partsCheck!pageList.action',
 	    fields:['idx','partsNo','configDetail','partsName','specificationModel','partsAccountIdx']
	});
	
    PartsCheckRegisterDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
      //行选择模式
    PartsCheckRegisterDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
    PartsCheckRegisterDetail.grid=new Ext.grid.EditorGridPanel({
       border: false, enableColumnMove: true, stripeRows: true, selModel: PartsCheckRegisterDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	     colModel:new Ext.grid.ColumnModel([
	        new Ext.grid.RowNumberer(),
	      	{
	        	sortable:false,dataIndex:'idx',hidden:true,header:'idx'
	        },{
	           	sortable:false,dataIndex:'specificationModel',header:'规格型号'
	        },{
	       		sortable:false,dataIndex:'partsName',header:'配件名称'
	        },{
	        	sortable:false,dataIndex:'partsNo',header:'配件编号'
	        },{
	        	sortable:false,dataIndex:'identificationCode',header:'配件识别码'
	        },{
	       		sortable:false,dataIndex:'nameplateNo',header:'配件铭牌号'
	        },{	
	        	sortable:false,dataIndex:'partsAccountIdx',header:'配件信息主键',hidden:true
	        },{	
	        	sortable:false,dataIndex:'partsTypeIDX',header:'配件型号表主键',hidden:true
	        },{
	        	sortable:false,dataIndex:'checkResult',header:'校验结果',
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},
	        	editor:{
	        			id:"checkResult",
						xtype:'combo',
			            fieldLabel: '校验结果',
			            store:new Ext.data.SimpleStore({
			               fields: ['value', 'text'],
                            data : [
                            	['合格', '合格'],
                            	['不合格', '不合格']
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
	        	sortable:false,dataIndex:'remark',header:'备注',
	        	renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},
	        	editor:{xtype:'textfield'}
	        }]),
	        store:PartsCheckRegisterDetail.store,
	        tbar:['配件编号：',{xtype:'textfield',id:'detail_partsNo',width:120},
	             {text:'添加',iconCls:'addIcon',handler:function(){
							  //表单验证是否通过
				        var whForm = PartsCheckRegister.form.getForm(); 
				        if (!whForm.isValid()) return;
				    	var partsNo = Ext.getCmp("detail_partsNo").getValue();
				    	if(partsNo == ""){
				    		MyExt.Msg.alert("请输入配件编号！");
				    		return ;
				    	}
				    	var searchJson = {identificationCode : partsNo};
				    	 Ext.Ajax.request({
				            url: ctx + '/partsCheck!getPartsAccount.action',
				            params: { searchJson: Ext.util.JSON.encode(searchJson) },
				            success: function(response, options){
				                var result = Ext.util.JSON.decode(response.responseText);
				                if (result.errMsg == null) {
				                   var entity = result.account;
				                    var count=PartsCheckRegisterDetail.grid.store.getCount();
				                    	if(null != entity){
				                    		var record_v = new Ext.data.Record();
												//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
					           	          		if(count!=0){
					           	          			for(var i=0;i<count;i++){
					           	          				var record=PartsCheckRegisterDetail.grid.store.getAt(i);
					           	          				if(entity.partsNo==record.get('partsNo')){
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
												record_v.set("identificationCode",entity["identificationCode"]);
												record_v.set("nameplateNo",entity["nameplateNo"]);
												record_v.set("checkResult","合格");
				                    			PartsCheckRegisterDetail.grid.store.insert(0, record_v); 
										        PartsCheckRegisterDetail.grid.getView().refresh(); 
										        PartsCheckRegisterDetail.grid.getSelectionModel().selectRow(0);
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
	              		PartsCheckRegisterDetail.batchWin.show();
	           	        PartsAccount.grid.store.load();
	           }},'&nbsp;&nbsp;',
	           {text:'删除明细',iconCls:'deleteIcon',handler:function(){
	                var data = PartsCheckRegisterDetail.grid.selModel.getSelections();
			        if(data.length == 0 ){
			        	MyExt.Msg.alert("尚未选择一条记录！");
			            return;
			        }
			        var storeAt = PartsCheckRegisterDetail.grid.store.indexOf(data[0]);
			        var records = PartsCheckRegisterDetail.store.getModifiedRecords();
			        var count = records.length; 
			        var j = storeAt + 1;
			        if(count-1 == storeAt){
			        	j = storeAt-1;
			        }
				    PartsCheckRegisterDetail.grid.getSelectionModel().selectRow(j);
				    for (var i = 0; i < data.length; i++){
				        PartsCheckRegisterDetail.grid.store.remove(data[i]);
				    }
				    PartsCheckRegisterDetail.grid.getView().refresh();
	           }},{
		    	text: "刷新", iconCls: "refreshIcon", handler: function(){self.location.reload();}
		    }]
    });
	PartsCheckRegisterDetail.grid.store.on("beforeload", function(){
		var beforeloadParam = {idx: "###"};
		this.baseParams.entityJson = Ext.util.JSON.encode(beforeloadParam);  
	});
	//移除事件
	PartsCheckRegisterDetail.grid.un('rowdblclick',PartsCheckRegisterDetail.grid.toEditFn,PartsCheckRegisterDetail.grid);
	//登帐并新增
	PartsCheckRegisterDetail.saveFun = function(){
		var form = PartsCheckRegister.form.getForm();
	    if (!form.isValid()) return;
	    var whData = form.getValues();
		var record = PartsCheckRegisterDetail.grid.store.getModifiedRecords();
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("请添加明细！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			data.checkEmpId = whData.checkEmpId;
			data.handOverEmp = whData.handOverEmp;
			data.checkTime = whData.checkTime;
			datas.push(data);
		}
		PartsCheckRegisterDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsCheck!savePartsCheckBatch.action',
            params : {registerDatas : Ext.util.JSON.encode(datas)},
            success: function(response, options){
            	PartsCheckRegisterDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
                    PartsCheckRegisterDetail.grid.store.removeAll();
                    PartsCheckRegisterDetail.grid.getView().refresh();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
            	PartsCheckRegisterDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	}
	PartsCheckRegisterDetail.batchWin=new Ext.Window({
     title:'待校验配件选择', width:1000, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
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