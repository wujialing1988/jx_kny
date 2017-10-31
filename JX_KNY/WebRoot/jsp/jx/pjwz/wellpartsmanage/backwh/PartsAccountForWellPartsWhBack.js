Ext.onReady(function(){
    Ext.namespace("PartsAccountForWellPartsWhBack");
    PartsAccountForWellPartsWhBack.searchParam={};
	PartsAccountForWellPartsWhBack.labelWidth = 90;
	PartsAccountForWellPartsWhBack.specificationModel = "";//规格型号
	
	 //获取当前日期
   	PartsAccountForWellPartsWhBack.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
   }
	
    PartsAccountForWellPartsWhBack.batchForm=new Ext.form.FormPanel({
	  layout:'column',
	  baeCls:'x-plain',
	  frame:true,
	  labelWidth: PartsAccountForWellPartsWhBack.labelWidth,
	  buttonAlign:"center",
	  defaults:{
	      columnWidth:.5,
	      labelWidth: PartsAccountForWellPartsWhBack.labelWidth,
		  layout:'form',
		  baseCls:'x-plain'
	  },
      items:[{
		  items:[{
		     	xtype:'textfield',fieldLabel:'配件编号',id:'partsNoBill',name:"partsNo"
		  	 },{
	  	       	xtype: "TrainType_combo",
	  	       	id:"trainType_comb_s",
	  	       	fieldLabel: "上车车型",
			   	hiddenName: "aboardTrainTypeIdx",
			   	displayField: "shortName",
			   	valueField: "typeID",
			   	width:150,
			  	 pageSize: 20, 
			   	minListWidth: 200,
			   	editable:false
		   	}]
	      },{
		  items:[{
		      	xtype: 'compositefield', fieldLabel : '是否新品', combineErrors: false ,
		  	  items:[{
		  	      		xtype:'checkbox', name:'isNewParts', id: 'isNewParts_new', boxLabel:'新&nbsp;&nbsp;&nbsp;&nbsp;', 
				  		inputValue:'新', checked:true,handler: function(){PartsAccountForWellPartsWhBack.checkQuery();}
		  	   		},{
		  	      		xtype:'checkbox', name:'isNewParts', id: 'isNewParts_old', boxLabel:'旧&nbsp;&nbsp;&nbsp;&nbsp;', 
                  		inputValue:'旧',checked:true,handler: function(){PartsAccountForWellPartsWhBack.checkQuery();}
		  	   		}]
		  	   },{
		         xtype:'textfield',fieldLabel:'上车车号',name:"aboardTrainNo"		  	
		  	}]
         }],
	    buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsAccountForWellPartsWhBack.searchParam = PartsAccountForWellPartsWhBack.batchForm.getForm().getValues();
			    PartsAccountForWellPartsWhBack.grid.searchFn(PartsAccountForWellPartsWhBack.searchParam);
            }
        	},{
            text: "重置", iconCls: "resetIcon", handler: function(){
            	PartsAccountForWellPartsWhBack.batchForm.getForm().reset();
            	Ext.getCmp("trainType_comb_s").clearValue();
            	PartsAccountForWellPartsWhBack.searchParam.specificationModel="";
            	PartsAccountForWellPartsWhBack.searchParam = {};
	            PartsAccountForWellPartsWhBack.specificationModel = "";
	            Ext.getCmp("partsAcId").setValue("");
            	PartsAccountForWellPartsWhBack.grid.store.load();
            }
        }]
	});
    PartsAccountForWellPartsWhBack.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsAccount!pageQuery.action',                 //装载列表数据的请求URL
		storeAutoLoad : false,
		viewConfig:{},// 显示grid组件滚动体
	    tbar:['配件名称(规格型号)：',{xtype:'textfield',width:150,id:'partsAcId',disabled:true},'&nbsp;&nbsp;',
	              {text:'确定',iconCls:'yesIcon',handler:function(){
	                   var records=PartsAccountForWellPartsWhBack.grid.selModel.getSelections();
	                   if(records.length<1){
	                   	  MyExt.Msg.alert("尚未选择一条记录！");
	                   	  return ;
	                   }
	                    var record_v;
		                var count=WellPartsWhBackDetail.grid.store.getCount();
                		for(var i=0;i<records.length;i++){
                			var entity = records[i].data;
                			record_v = new Ext.data.Record();
                			//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
           	          		if(count!=0){
           	          			for(var j=0;j<count;j++){
           	          				var record=WellPartsWhBackDetail.grid.store.getAt(j);
           	          				if(entity.partsNo==record.get('partsNo')){
           	          					MyExt.Msg.alert("配件编号为<font color='red'>【"+record.get('partsNo')+"】</font>已存在列表中，请不要重复添加");
           	          					return ;
           	          				}
           	          			}
           	          		}
           	          		record_v = records[i];
                			WellPartsWhBackDetail.grid.store.insert(0, record_v); 
					        WellPartsWhBackDetail.grid.getView().refresh(); 
					        WellPartsWhBackDetail.grid.getSelectionModel().selectRow(0);
                		}
	              }},'&nbsp;&nbsp;',
	              {text:'取消',iconCls:'deleteIcon',handler:function(){
	              	    WellPartsWhBackDetail.batchWin.hide();
	              }}
	    ],
	    fields: [  {
	             	sortable:false,header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	sortable:false,width:100,header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
	             	sortable:false,width:100,header:'配件名称',dataIndex:'partsName',editor:{}
	           },{
	             	sortable:false,width:100,header:'配件识别码',dataIndex:'identificationCode',editor:{}
	           },{
	             	sortable:false,width:100,header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{
	             	sortable:false,width:100,header:'存放位置',dataIndex:'location',editor:{}
	           },{
	         		sortable:false,header:'是否新品',align:'center',dataIndex:'isNewParts',editor:{}
	           },{
	         	 	sortable:false,header:'上车车型',width:60,align:'center',dataIndex:'aboardTrainType',editor:{}
	           },{
	         	 	sortable:false,header:'上车车号',width:60,align:'center',dataIndex:'aboardTrainNo',editor:{}
	           },{
	         	 	sortable:false,header:'上车修程',width:60,align:'center',hidden:true,dataIndex:'aboardRepairClass',editor:{}
	           },{
	         	 	sortable:false,header:'上车修次',width:60,align:'center',hidden:true,dataIndex:'aboardRepairTime',editor:{}
	           },{
	         	 	sortable:false,header:'配件状态',width:80,dataIndex:'partsStatusName',editor:{}
	           },{
	         	 	sortable:false,width:110,header:'存放地点',dataIndex:'location',hidden:true,editor:{}
	           },{
	          		sortable:false,width:250,header:'详细配置',dataIndex:'configDetail',hidden:true,editor:{}
	           },{
	         		sortable:false,header:'配件规格型号主键',dataIndex:'partsTypeIDX',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'计量单位',dataIndex:'unit',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		sortable:false,header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	sortable:false,header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           }],
	           searchFn: function(searchParam){
	                searchParam.specificationModel=PartsAccountForWellPartsWhBack.specificationModel;
	                PartsAccountForWellPartsWhBack.searchParam=searchParam;
	           		this.store.load();	
               }
	});
	//树形面板选择按钮
  jx.pjwz.partbase.component.returnFn = function(node){
	     PartsAccountForWellPartsWhBack.searchParam=PartsAccountForWellPartsWhBack.batchForm.getForm().getValues();
	     if(node.id == 'ROOT_0'){
	   		 Ext.getCmp("partsAcId").setValue("");
		     PartsAccountForWellPartsWhBack.searchParam.specificationModel="";
		     PartsAccountForWellPartsWhBack.specificationModel = "";
	   	}else {
	   		Ext.getCmp("partsAcId").setValue(node.text);
	        PartsAccountForWellPartsWhBack.searchParam.specificationModel = node.attributes.specificationModel;
	        PartsAccountForWellPartsWhBack.searchParam.partsName = node.attributes.partsName;
	        PartsAccountForWellPartsWhBack.specificationModel = node.attributes.specificationModel;
	   	}
	     PartsAccountForWellPartsWhBack.searchParam=MyJson.deleteBlankProp(PartsAccountForWellPartsWhBack.searchParam);
		 PartsAccountForWellPartsWhBack.grid.searchFn(PartsAccountForWellPartsWhBack.searchParam);
   }
   
   //公用checkbox查询方法
	PartsAccountForWellPartsWhBack.isNewParts = "新,旧";
	//状态多选按钮
	PartsAccountForWellPartsWhBack.checkQuery = function(){
		PartsAccountForWellPartsWhBack.isNewParts = "-1";
		if(Ext.getCmp("isNewParts_new").checked){
			PartsAccountForWellPartsWhBack.isNewParts = PartsAccountForWellPartsWhBack.isNewParts + ",新";
		} 
		if(Ext.getCmp("isNewParts_old").checked){
			PartsAccountForWellPartsWhBack.isNewParts = PartsAccountForWellPartsWhBack.isNewParts + ",旧";
		} 
		PartsAccountForWellPartsWhBack.grid.store.load();
	}
	
	PartsAccountForWellPartsWhBack.grid.un("rowdblclick",PartsAccountForWellPartsWhBack.grid.toEditFn,PartsAccountForWellPartsWhBack.grid);
	PartsAccountForWellPartsWhBack.grid.store.on('beforeload',function(){
	     var searchParam = PartsAccountForWellPartsWhBack.searchParam;
	     searchParam.isNewParts = PartsAccountForWellPartsWhBack.isNewParts;
	     searchParam=MyJson.deleteBlankProp(searchParam);
		 //2016年5月16日修改：去掉良好在库和良好不在库状态，都统一成良好；责任部门为【机构】就是不在库
	     var sqlStr = " (parts_status  like '" + PARTS_STATUS_LH + "%' and MANAGE_DEPT_Type=" + MANAGE_DEPT_TYPE_ORG + ")";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} 
					]
	     for(prop in searchParam){
	     	if(prop=='partsNo' && searchParam[prop]!=null){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	     		continue;
	     	}
	     	if('isNewParts' == prop){
				var val = searchParam[prop];
				val = val.toString();
				val = val.split(",");
                if(val instanceof Array){
                    whereList.push({propName:'isNewParts', propValues:val, compare:Condition.IN });
                } else {
                    var valAry = [];
                    valAry.push(val);
                    whereList.push({propName:'isNewParts', propValues:valAry, compare:Condition.IN });
                }
                continue;
		 	}else{
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
	     		continue;
	     	}
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});