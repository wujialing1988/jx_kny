/**
 * 下车配件登记明细 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	//配件选择窗口,数据容器
	Ext.namespace("PartsUnloadRegisterDetail");
	PartsUnloadRegisterDetail.fieldWidth = 150;
	PartsUnloadRegisterDetail.labelWidth = 100;
	PartsUnloadRegisterDetail.rowIndex = "";
	PartsUnloadRegisterDetail.rdpIdx = "";
	
	//规格型号选择控件赋值函数
	PartsUnloadRegisterDetail.callReturnFn=function(node,e){
	  var defaultData = {specificationModel:node.attributes["specificationModel"],partsName:node.attributes["partsName"],partsTypeIDX:node.attributes["id"],isInRange:'否',unloadReason:'超范围'};
  	  var initData = Ext.apply({}, defaultData); 
	  var record = new Ext.data.Record(defaultData);
  	  PartsUnloadRegisterDetail.grid.store.insert(0, record); 
      PartsUnloadRegisterDetail.grid.getView().refresh(); 
      PartsUnloadRegisterDetail.grid.getSelectionModel().selectRow(0);
      PartsUnloadRegisterDetail.setPartsCounts();
	};
	
	// 设置已登记 未登记数量
	PartsUnloadRegisterDetail.setPartsCounts = function(){
		var length = PartsUnloadRegisterDetail.store.getCount();
		var record = PartsUnloadRegisterDetail.store.getRange(0,length);
		if(record == null || record.length == 0){
			Ext.getCmp("checkQty").setValue("0");
			Ext.getCmp("unCheckQty").setValue("0");
			return ;
		}
		var count = 0 ;
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			if(!Ext.isEmpty(data.idx)){
				count++;
			}
		}
		Ext.getCmp("checkQty").setValue(count+"");
		Ext.getCmp("unCheckQty").setValue((record.length-count)+"");
		
	};
	
	
	PartsUnloadRegisterDetail.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	PartsUnloadRegisterDetail.store = new Ext.data.JsonStore({
	    id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:false, pruneModifiedRecords: true,
	    url: ctx + "/partsUnloadRegister!findPartsUnloadRegisterAll.action",
	    fields: [ "idx","partsTypeIDX","partsName","unloadPlace","specificationModel","partsTypeIDX","partsNo","identificationCode"
	    	,"runingKM","unloadReason","partsAccountIDX","isInRange","jcpjbm","partsStatusName","partsStatus"
	    ]
	});
	//材料规格型号，选择模式，勾选框可多选
	PartsUnloadRegisterDetail.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//材料规格型号，分页工具
	PartsUnloadRegisterDetail.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsUnloadRegisterDetail.store});
	PartsUnloadRegisterDetail.grid = new Ext.grid.EditorGridPanel({
	    border: false, enableColumnMove: false, stripeRows: true, selModel: PartsUnloadRegisterDetail.sm,loadMask:true,
	    clicksToEdit: 1,
	    viewConfig: {forceFit: true},
	    selModel: PartsUnloadRegisterDetail.sm,
	    colModel: new Ext.grid.ColumnModel([
	    new Ext.grid.RowNumberer(), PartsUnloadRegisterDetail.sm,
	        { sortable:false, header:'idx', dataIndex:'idx',hidden: true},
	        { sortable:false, header: '配件名称',  dataIndex: 'partsName',width:100,renderer: function(v, metaData, record) {
 				if (!Ext.isEmpty(record.get('idx'))) {
	 				return "<span style='color:green'>" + v + "</span>";
 				}else{
 					return v ;
 				}
 			}},
	        { sortable:false, header: "下车位置",  dataIndex: "unloadPlace",
	    		editor:{maxLength:50}
			},
		   { sortable:false, header: "规格型号",  dataIndex: "specificationModel",width:100,
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},
	    	    editor: { id:"specificationModel_comb", 
	    	    		 xtype: 'Base_combo',hiddenName: "specificationModel",fieldLabel:"规格型号",
		 				 entity:"com.yunda.jx.pjwz.partsBase.partstype.entity.PartsType",
		 				 displayField:"specificationModel",valueField:"specificationModel",fields:["idx","partsName","specificationModel","specificationModelCode"],			 
		 				 returnField:[{widgetId:"partsTypeIDX_id", propertyName:"idx"}], 
		 				 width: PartsUnloadRegisterDetail.fieldWidth,allowBlank: false,
		 				 listeners:{
		 				 	'beforeshow': function(me){
		 				 		var record_v = PartsUnloadRegisterDetail.store.getAt(PartsUnloadRegisterDetail.rowIndex);
		 				 		var rc_comb = Ext.getCmp("specificationModel_comb");
				                rc_comb.reset();
				                rc_comb.clearValue();
				                rc_comb.getStore().removeAll();
				                rc_comb.queryParams = {"jcpjbm":record_v.json.jcpjbm,"status":"1"};
				                rc_comb.cascadeStore();
		 				 	}
		 				 },
		 				 'select': function(me){
		 				 	var rc_comb = Ext.getCmp("specificationModel_comb");
		 				 }
		 				 }
		   
		   },
	    	{ sortable:false, header:'配件规格型号id', dataIndex:'partsTypeIDX', hidden: true, editor:{id:"partsTypeIDX_id", xtype:'hidden'}},
	    	{ sortable:false, header: "配件编号",  dataIndex: "partsNo",width:100,
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	    	
		    	editor: new  Ext.form.TextField({
		    		id:"aaaa",
		    		maxLength:25,
	                allowBlank: true,
	                listeners:{
	                	'blur': function(){
	                		
	                	}
	                }
	         	})},
	         { sortable:false, header: "配件识别码",  dataIndex: "identificationCode",width:100,
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	         
		    	editor: new  Ext.form.TextField({
		    		maxLength:25
	         	})
	         	},
	        { sortable:false, header: "走行公里数",  dataIndex: "runingKM",hidden:true,
		   	    renderer: function(v,m){
     	   	   	 	m.css = 'x-grid-col';
     	   	   	 	return v;
     	   	   	},	        
	    		editor:{maxLength:8,vtype : "positiveInt"}
				},
	    	{ sortable:false, header: "下车原因",renderer: function(v, metaData, record) {
 				metaData.css = 'x-grid-col';
 				if (Ext.isEmpty(v) && record.get('isInRange') == '是') {
 					var nloadRepairClass = Ext.getCmp('PartsUnloadRegister_unloadRepairClass').getValue();
 					record.data.unloadReason = nloadRepairClass;
	 				return nloadRepairClass;
 				}
 				return v;
 			}, dataIndex: "unloadReason",
	    		editor: new  Ext.form.TextField({
		    		maxLength:50
	         	})},
        	{ sortable:false, header: "配件状态",  dataIndex: "partsStatusName"
			},
	    	{ header:'partsAccountIDX', dataIndex:'partsAccountIDX',hidden: true},
	    	{ header:'isInRange', dataIndex:'isInRange',hidden: true}
	    	
	    ]),
		listeners: {
			cellclick:function(grid, rowIndex, columnIndex, e) {
                var fieldName = grid.getColumnModel().getDataIndex(columnIndex); 
				if(fieldName == "specificationModel"){
					PartsUnloadRegisterDetail.rowIndex = rowIndex;
				}
			},
			afteredit:function(e){
				var partsTypeIDX = Ext.getCmp("partsTypeIDX_id").getValue();
				if(!Ext.isEmpty(partsTypeIDX) && e.field == "specificationModel"){
						e.record.data.partsTypeIDX = partsTypeIDX;
				}
				PartsUnloadRegisterDetail.grid.getView().refresh();
			},
			beforeedit:function(e){
				// 已登记的不能编辑
				if(e.record.data.idx){
       	 			return false ;
       	 		}else{
       	 			return true ;
       	 		}
				
				// 规格型号超范围不能编
				if(e.field == 'specificationModel'){
					if(e.record.data.isInRange == '否' || (!Ext.isEmpty(e.record.data.partsStatusName) && e.record.data.partsStatusName != '待修')){
						// MyExt.Msg.alert("！");
						return false ;
					}else{
						return true ;
					}
				}
				
				// 下车位置范围内数据可编辑
				if(e.field == 'unloadPlace'){
					if(e.record.data.isInRange == '是' || (!Ext.isEmpty(e.record.data.partsStatusName) && e.record.data.partsStatusName != '待修')){
						return false ;
					}else{
						return true ;
					}
				}
				
				// 配件编号 只有【待修】或者未登记的才能修改
				if(e.field == 'partsNo'){
					if(!Ext.isEmpty(e.record.data.partsStatusName) && e.record.data.partsStatusName != '待修'){
						return false ;
					}else{
						return true ;
					}
				}
				
			}
		},
	    store: PartsUnloadRegisterDetail.store,
	    tbar: [
	        	{ text:'撤销', iconCls:'deleteIcon', handler:function(){
		        var data = PartsUnloadRegisterDetail.grid.selModel.getSelections();
		        if(data.length == 0 ){
		        	MyExt.Msg.alert("尚未选择一条记录！");
		            return;
		        }
		        
		        PartsUnloadRegisterDetail.deleteFun(data);
		        
        }},{
	    	text: "刷新", iconCls: "refreshIcon", handler: function(){
	    			PartsUnloadRegisterDetail.store.reload();
	    		}
	    },'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','*配件名称颜色为<span style="color:green">绿色</span>表示已登记数据，状态【待修】的数据可修改'
	    ,'->','已登记数量：',{xtype:'displayfield',id:'checkQty',cls:'checkQty_css'},'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','-','&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;','未登记数量：',{xtype:'displayfield',id:'unCheckQty',cls:'unCheckQty_css'},'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;']
	});
	
	PartsUnloadRegisterDetail.store.on("beforeload", function(){
		this.baseParams.workPlanId = PartsUnloadRegisterDetail.rdpIdx ;
	});
	
	
	PartsUnloadRegisterDetail.store.on("load", function(){
		PartsUnloadRegisterDetail.setPartsCounts();
	});
	
	// 数据删除
	PartsUnloadRegisterDetail.deleteFun = function(data){
		
		var ids = [] ;
	    for (var i = 0; i < data.length; i++){
	        if(!Ext.isEmpty(data[i].data.idx)){
	        	ids.push(data[i].data.idx);
	        }else if(!Ext.isEmpty(data[i].data.isInRange) && data[i].data.isInRange == '否'){
	    		PartsUnloadRegisterDetail.grid.store.remove(data[i]);
	    	}
	    }
		
	    if(ids != null && ids.length > 0){
			Ext.Ajax.request({
				url: ctx + "/partsUnloadRegister!updateUnloadRegisterForCancelNew.action",
				params: {
					ids:ids
				},
				success: function(response, options) {
					if (self.loadMask) self.loadMask.hide();
					var result = Ext.util.JSON.decode(response.responseText);
					if (result.success == true) {
						PartsUnloadRegisterDetail.deletLocal(data);
						PartsUnloadRegisterDetail.setPartsCounts();
						MyExt.Msg.alert("撤销操作成功！");
					} else {
						alertFail(result.errMsg);
					}
				},
				failure: function(response, options) {
					if (self.loadMask) self.loadMask.hide();
					MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
	    }else{
	    	// 刷新数量
	    	PartsUnloadRegisterDetail.setPartsCounts();
	    }
	}
	
	// 本地删除
	PartsUnloadRegisterDetail.deletLocal = function(data){
			var storeAt = PartsUnloadRegisterDetail.grid.store.indexOf(data[0]);
	        var records = PartsUnloadRegisterDetail.store.getModifiedRecords();
	        var count = records.length; 
	        var j = storeAt + 1;
	        if(count-1 == storeAt){
	        	j = storeAt-1;
	        }
		    PartsUnloadRegisterDetail.grid.getSelectionModel().selectRow(j);
			
			for (var i = 0; i < data.length; i++){
		        if(!Ext.isEmpty(data[i].data.isInRange) && data[i].data.isInRange == '否'){
		        	PartsUnloadRegisterDetail.grid.store.remove(data[i]);
		        }else{
		        	data[i].set("idx",null);
		        	data[i].set("partsNo",null);
		        	data[i].set("partsStatus",null);
		    		data[i].set("partsStatusName",null);
		    		data[i].set("partsAccountIDX",null);
		    		data[i].set("runingKM",null);
		    		PartsUnloadRegisterDetail.grid.getView().refresh();
		    	}
		    }
	}
	
	//登帐并新增
	PartsUnloadRegisterDetail.saveFun = function(){
		var form = PartsUnloadRegister.form.getForm();
	    if (!form.isValid()) return;
	    var regData = form.getValues();
	    // 设置人员组织为当前登录人
	    regData.takeOverDeptId = teamOrgId ;
	    regData.takeOverDept = teamOrgName ;
	    regData.takeOverDeptOrgseq = teamOrgSeq ;
	    regData.takeOverEmpId = empId ;
	    regData.takeOverEmp = empName ;
	    regData.takeOverTime = new Date() ;
	    var length = PartsUnloadRegisterDetail.store.getCount();
		var record = PartsUnloadRegisterDetail.store.getRange(0,length);
		var datas = new Array();
		if(record.length == 0){
			MyExt.Msg.alert("没有保存的数据！");
			return ;
		}
		for (var i = 0; i < record.length; i++) {
			var data = {} ;
			data = record[i].data;
			for (var j = i+1; j < record.length; j++) {
				var data_v = {} ;
				data_v = record[j].data;
				if(!Ext.isEmpty(data.partsNo) && !Ext.isEmpty(data_v.partsNo) && data.partsNo == data_v.partsNo && data.partsTypeIDX == data_v.partsTypeIDX){
					MyExt.Msg.alert("配件编号不能重复！");
					return ;
				}
			}
			for (var j = i+1; j < record.length; j++) {
				var data_c = {} ;
				data_c = record[j].data;
				if(!Ext.isEmpty(data.identificationCode) && !Ext.isEmpty(data_c.identificationCode) && data.identificationCode == data_c.identificationCode){
					MyExt.Msg.alert("配件识别码不能重复！");
					return ;
				}
			}
			
//			for (var j = i+1; j < record.length; j++) {
//				var data_l = {} ;
//				data_l = record[j].data;
//				if(!Ext.isEmpty(data.unloadPlace) && !Ext.isEmpty(data_l.unloadPlace) && data.unloadPlace == data_l.unloadPlace){
//					MyExt.Msg.alert("下车位置不能重复！");
//					return ;
//				}
//			}
			
			delete data.jcpjbm;
			delete data.partsStatusName ;
			delete data.partsStatus ;
			delete regData.PartsTypeTreeSelect_select;
			
			// 只保存配件不为空的数据
			if(!Ext.isEmpty(data.partsNo) && !Ext.isEmpty(data.partsTypeIDX) && !Ext.isEmpty(data.specificationModel)
			&& (Ext.isEmpty(data.partsStatusName) || data.partsStatusName == '待修')){
				datas.push(data);
			}
		}
		
		if(datas == null || datas.length == 0){
				MyExt.Msg.alert("没有可保存的数据，请查看配件编号和规格型号是否填写！");
				return ;
		}
		PartsUnloadRegisterDetail.loadMask.show();
        Ext.Ajax.request({
            url: ctx + '/partsUnloadRegister!saveUnloadRegisterNew.action',
            jsonData: datas,
            params : {regData : Ext.util.JSON.encode(regData)},
            success: function(response, options){
              	PartsUnloadRegisterDetail.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	PartsUnloadRegisterDetail.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                PartsUnloadRegisterDetail.loadMask.hide();
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });
	};
});