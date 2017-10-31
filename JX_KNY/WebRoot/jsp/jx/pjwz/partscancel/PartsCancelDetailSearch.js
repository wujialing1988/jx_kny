Ext.onReady(function(){
    Ext.namespace("PartsCancelDetailSearch");
    
     // 显示扩展编号详情的函数
	PartsCancelDetailSearch.searchExtendNo = function(rowIndex, formColNum) {
		var extendNoJson = PartsCancelDetailSearch.grid.store.getAt(rowIndex).get("extendNoJson");
		jx.pjwz.partbase.component.PartsExtendNoWin.createWin(extendNoJson, formColNum);
	}
    
    PartsCancelDetailSearch.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsCanceledDetail!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:[],
	    pageSize:100,
	    bbar:[],
	    fields: [{
	             	header:'idx主键',dataIndex:'idx',hidden:true,editor:{}
	           },{
	             	header:'配件编号',dataIndex:'partsNo',editor:{}
	           },{
					sortable:false,header:'扩展编号', dataIndex:'extendNoJson', searcher : { disabled : true }, width: 150, 
					renderer: function(v,metadata, record, rowIndex, colIndex, store) {
						var extendNo = jx.pjwz.partbase.component.PartsExtendNoWin.getExtendNo(v);		
						if (Ext.isEmpty(extendNo)) {
							return "";
						}
						var html = "";
			    		html = "<span><a href='#' onclick='PartsCancelDetailSearch.searchExtendNo(" + rowIndex + ", 1)'>"+extendNo+"</a></span>";
			            return html;
					}
			   },{
	         		header:'配件名称',dataIndex:'partsName',editor:{}
	           },{
	         	 	header:'规格型号',dataIndex:'specificationModel',editor:{}
	           },{
	          		header:'详细配置',dataIndex:'configDetail',editor:{}
	           },{
	         	 	header:'存放地点',dataIndex:'location',editor:{}
	           },{
	         	 	header:'下车车型',dataIndex:'unloadTrainType',editor:{}
	           },{
	         	 	header:'下车车号',dataIndex:'unloadTrainNo',editor:{}
	           },{
	         	 	header:'下车修程',dataIndex:'unloadRepairClass',editor:{}
	           },{
	         	 	header:'下车修次',dataIndex:'unloadRepairTime',editor:{}
	           },{
	         	 	header:'配件状态',dataIndex:'partsStatus',hidden:true,editor:{}
	          // 	,renderer:function(v){
//	         	 	  if(v=='01020201') return "检修中";
//	         	 	  else if(v=='010101') return "待修在库";
//	         	 	  else if(v=='010201') return "委外修";
//	         	 	  else if(v=='01020203') return "缺料";
//	         	 	  else if(v=='01020202') return "修竣";
//	         	 	  else if(v=='010203') return "待报废";
//	         	 	  else if(v=='020301') return "已报废";
//	         	 	  else return v;
//	         	 	}
	           },{
	         		header:'配件状态',dataIndex:'partsStatusName',editor:{}
	           },{
	         		header:'是否新品',dataIndex:'isNewParts',editor:{}
	           },{
	         		header:'配件规格型号主键',dataIndex:'partsTypeIdx',hidden:true,editor:{}
	           },{
	          		header:'配件信息主键',dataIndex:'partsAccountIdx',hidden:true,editor:{}
	           },{
	         	 	header:'生产厂家主键',dataIndex:'madeFactoryIdx',hidden:true,editor:{}
	           },{
	          		header:'生产厂家',dataIndex:'madeFactoryName',hidden:true,editor:{}
	           },{
	         	 	header:'出厂日期',dataIndex:'factoryDate',hidden:true,editor:{}
	           },{
	         	 	header:'销账单主键',dataIndex:'accountCancelIdx',hidden:true,editor:{}
	           }]
	});
	//取消监听事件
	PartsCancelDetailSearch.grid.un("rowdblclick",PartsCancelDetailSearch.grid.toEditFn,PartsCancelDetailSearch.grid);
	PartsCancelDetailSearch.formPanel=new Ext.form.FormPanel({
	   layout:'table',frame:true,
	   defaults:{},
	   layoutConfig:{columns:3},
	   defaults:{bodyStyle:'padding:10px',baseCls:'x-plain'},
	   items:[{
	       layout:'form',
	       frame:true,
	       labelWidth:60,
	       items:[{xtype:'textfield',id:'billNo_2',fieldLabel:'单据编号',disabled:true}]
	    },{
	       layout:'form',
	       frame:true,
	       labelWidth:60,
	       items:[{xtype:'textfield',id:'cancelEmp_2',fieldLabel:'销账人',disabled:true}]
	    },{
	       layout:'form',
	       frame:true,
	       labelWidth:60,
	       items:[{xtype:'my97date',format:'Y-m-d',id:'cancelDate_2',fieldLabel:'销账日期',disabled:true}]
	    },{
	       layout:'form',
	       frame:true,
	       labelWidth:60,
	       items:[{xtype:'textfield',format:'Y-m-d',id:'cancelType',fieldLabel:'销账方式',disabled:true}]
	    },{
	       layout:'form',
	        frame:true,
	       labelWidth:60,
	       colspan:2,
	       items:[{xtype:'textarea',fieldLabel:'销账理由',anchor:'95%',id:'canceledReason_2',height:50,width:320,disabled:true}]
	    },{
	       layout:'form',
	        frame:true,
	       labelWidth:60,
	       colspan:3,
	       items:[{xtype:'textarea',fieldLabel:'单据摘要',anchor:'95%',id:'billSummary_2',height:50,width:530,disabled:true}]
	    }]
	});
	PartsCancelDetailSearch.win=new Ext.Window({
	   title:'配件报废单查看', width:900, height:600, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',frame:true,
    	maximizable:false,  modal:true,items:[{
    	     layout:'border',baseCls:'x-plain',frame:true,
    	     items:[{
    	        region:'north',
    	        height:200,
    	        items:[PartsCancelDetailSearch.formPanel]
    	     },{
    	        region:'center',
    	        layout:'fit',
    	        items:[PartsCancelDetailSearch.grid]
    	     }]
    	}],
    	buttonAlign:'center',
    	buttons:[
    	    {text:'关闭',iconCls:'closeIcon',handler:function(){PartsCancelDetailSearch.win.hide();}}
    	]
	});
	//页面加载前调用
	PartsCancelDetailSearch.grid.store.on('beforeload',function(){
	   var searchParam={};
	   searchParam.accountCancelIdx=PartsCancelSearch.idx;
	   searchParam=MyJson.deleteBlankProp(searchParam);
	   var whereList=[];
	   for(prop in searchParam){
	   whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
	   }
	   this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
});