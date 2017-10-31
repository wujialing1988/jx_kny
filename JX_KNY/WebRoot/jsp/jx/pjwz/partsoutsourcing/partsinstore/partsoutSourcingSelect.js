Ext.onReady(function(){
Ext.namespace('PartsOutSourcSelect');                       //定义命名空间
PartsOutSourcSelect.searchParam={};

// 显示扩展编号详情的函数
PartsOutSourcSelect.searchExtendNo = function(rowIndex, formColNum) {
	var extendNoJson = PartsOutSourcSelect.grid.store.getAt(rowIndex).get("extendNoJson");
	jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
}
    

PartsOutSourcSelect.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsOutsourcing!findPageQuery.action',                 //装载列表数据的请求URL
    viewConfig:{},		// 显示grid组件滚动条
    fieldWidth:200,
    tbar:['配件编号:',{xtype:'textfield',id:'partsNo_1',width:120},
          '维修厂家',{xtype:'PartsOutSourcingFactory_combo',fieldLabel:'委外厂家',allowBlank:true,
          	        displayField:'factoryName',valueField:'id',
          	        hiddenName:'factoryId',editable:false,id:'factoryId_1'//outsourcingFactoryId
          	        //returnField:[{widgetId:'outsourcingFactory',propertyName:'factoryName'}]},
          	   },
          {text:'查询',iconCls:'searchIcon',handler:function(){
             var partsNo=Ext.getCmp("partsNo_1").getValue();
          	var factoryId=Ext.getCmp("factoryId_1").getValue();
          	var searchParam={};
          	searchParam.partsNo=partsNo;
          	searchParam.factoryId=factoryId;
          	searchParam=MyJson.deleteBlankProp(searchParam);
          	PartsOutSourcSelect.grid.searchFn(searchParam);
          }},{
             text:'重置',iconCls:'resetIcon',handler:function(){
                var searchParam={};
                Ext.getCmp("partsNo_1").setValue("");
                Ext.getCmp("factoryId_1").clearValue();
                PartsOutSourcSelect.grid.searchFn(searchParam);
             }
          },
          {text:'确定',iconCls:'yesIcon',handler:function(){
              var records=PartsOutSourcSelect.grid.selModel.getSelections();
              if(records.length<1){
                MyExt.Msg.alert("尚未选择一条记录！");
                return ;
              }
              //遍历所有选择的记录，并将每条记录添加到明细中
          	for(var i=0;i<records.length;i++){
          		var data=records[i].data;
          		var count=RepairedPartsWHDetail.grid.store.getCount();
   	          		//判断是否是第一次添加，如果不是，则判断当前添加的配件是否在列表中存在，如果存在，则不允许重复添加
   	          	if(count!=0){
   	          		for(var j=0;j<count;j++){
   	          			var record1=RepairedPartsWHDetail.grid.store.getAt(j);
   	          			if(record1.get('partsOutNo')==data.partsOutNo){
   	          				MyExt.Msg.alert("配件编号为<font color='red'>【"+data.partsOutNo+"】</font>已存在列表中，请不要重复添加");
   	          				return ;
   	          			}
   	          		}
   	          	}
          	  	var defaultData={partsAccountIdx:data.partsAccountIDX,partsNo:data.partsOutNo,partsOutNo:data.partsOutNo,extendNoJson:data.extendNoJson,partsName:data.partsName,specificationModel:data.specificationModel,partsTypeIDX:data.partsTypeIdx};;
				var record = new Ext.data.Record(defaultData);
               	RepairedPartsWHDetail.grid.store.insert(0, record); 
				RepairedPartsWHDetail.grid.getView().refresh(); 
				RepairedPartsWHDetail.grid.getSelectionModel().selectRow(0);
          	}
          	RepairedPartsWHDetail.win.hide();
          	//判断是否添加了明细，如果有明细，则不允许再修改接受库房，除非明细为空
          	var count_1=RepairedPartsWHDetail.grid.store.getCount();
            if(count_1>0){
        	  Ext.getCmp("whName_comb").disable();
            }else{
              Ext.getCmp("whName_comb").enable();
            }
          }
      },{text:'取消',iconCls:'deleteIcon',handler:function(){
        RepairedPartsWHDetail.win.hide();
      }}],
	fields: [
	{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'配件编号', width:100, dataIndex:'partsOutNo', editor:{}
	},{
		sortable:false,header:'扩展编号', dataIndex:'extendNoJson', searcher : { disabled : true }, width: 150, 
		renderer: function(v,metadata, record, rowIndex, colIndex, store) {
			var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
			if (Ext.isEmpty(extendNo)) {
				return "";
			}
			var html = "";
    		html = "<span><a href='#' onclick='PartsOutSourcSelect.searchExtendNo(" + rowIndex + ", 1)'>"+extendNo+"</a></span>";
            return html;
		}
	},{
		header:'配件名称', width:150, dataIndex:'partsName', editor:{ }
	},{
		header:'规格型号', width:200, dataIndex:'specificationModel', editor:{ }
	},{
		header:'委外厂家', dataIndex:'outsourcingFactory', editor:{   }
	},{
		header:'委外日期', width:80, dataIndex:'outsourcingDate', editor:{   }
	},{
		header:'详细配置', width:250, dataIndex:'configDetail', editor:{  }
	},{
		/************ 以下字段为隐藏字段 *************/
		header:'配件信息主键', dataIndex:'partsAccountIDX',hidden:true, editor:{ }
	},{
		header:'规格型号主键', dataIndex:'partsTypeIdx', hidden:true,editor:{  }
	},{
		header:'委外厂家主键', dataIndex:'outsourcingFactoryId',hidden:true, editor:{  }
	}],searchFn: function(searchParam){
		PartsOutSourcSelect.searchParam=searchParam;
        this.store.load();	
    }
  });
   //树形面板选择按钮
   jx.pjwz.partbase.component.returnFn = function(node){
   	        var partsNo=Ext.getCmp("partsNo_1").getValue();
          	var factoryId=Ext.getCmp("factoryId_1").getValue();
          	var searchParam={};
          	searchParam.partsNo=partsNo;
          	searchParam.factoryId=factoryId;
          	searchParam.specificationModel=node.attributes.specificationModel;
          	searchParam=MyJson.deleteBlankProp(searchParam);
	 PartsOutSourcSelect.grid.searchFn(searchParam);
   }
  PartsOutSourcSelect.grid.un("rowdblclick",PartsOutSourcSelect.grid.toEditFn,PartsOutSourcSelect.grid);
  PartsOutSourcSelect.grid.store.on('beforeload',function(){
     var searchParam=PartsOutSourcSelect.searchParam
  	  searchParam=MyJson.deleteBlankProp(searchParam);
  	  searchParam.status=out;//委外
  	  var whereList=[];
  	  for(prop in searchParam){
  	  	if(prop=='partsNo' &&searchParam[prop]!=null ){
  	     whereList.push({propName:prop,propValue:searchParam[prop]});
  	     continue;
  	  	}
  	  	if(prop=='status' &&searchParam[prop]!=null ){
  	     whereList.push({propName:prop,propValue:searchParam[prop]});
  	     continue;
  	  	}
  	  	if(prop=='factoryId' &&searchParam[prop]!=null ){
  	     whereList.push({propName:prop,propValue:searchParam[prop]});
  	     continue;
  	  	}
  	  	if(prop=='specificationModel' &&searchParam[prop]!=null ){
  	     whereList.push({propName:prop,propValue:searchParam[prop]});
  	     continue;
  	  	}
  	  }
  	  this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
  });
});