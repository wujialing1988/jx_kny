Ext.onReady(function(){
    Ext.namespace("PartsCheckRegisterSearch");
	PartsCheckRegisterSearch.fieldWidth = 160;
	PartsCheckRegisterSearch.labelWidth = 80;
	PartsCheckRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsCheckRegisterSearch.callReturnFn=function(node,e){
	  PartsCheckRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  PartsCheckRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	PartsCheckRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	PartsCheckRegisterSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsCheckRegisterSearch.labelWidth,
	    buttonAlign:"center",
	    defaults:{
	      xtype: "panel",
	      border: false,
	      baseCls: "x-plain",
	      layout: "column",
	      align: "center", 
	      	defaults:{
		      	 baseCls:"x-plain", 
		      	 align:"center",
		      	 layout:"form",
		      	 defaultType:"textfield", 
		      	 labelWidth:PartsCheckRegisterSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: PartsCheckRegisterSearch.callReturnFn
	        	},{
                  	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
	        	},{
            		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '校验日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'checkTime', id: 'startDate_d', format:'Y-m-d', value: PartsCheckRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
						my97cfg: {maxDate: '#F{$dp.$D(\'endDate_d\')}'}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height: 23px;line-height:23px;'
					}, {
						xtype:'my97date', name: 'checkTime', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
						my97cfg: {minDate: '#F{$dp.$D(\'startDate_d\')}'}
					}]
	        	}]
	        },{
	        	items:[{
	        		id:"partsNo", name:"partsNo", fieldLabel:"配件编号"
	        	}]
	        }]
	    }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsCheckRegisterSearch.searchParam = PartsCheckRegisterSearch.searchForm.getForm().getValues();
			    PartsCheckRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsCheckRegisterSearch.searchParam);
			    PartsCheckRegisterSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                PartsCheckRegisterSearch.searchForm.getForm().reset();
            	PartsCheckRegisterSearch.searchParam = {};
			    PartsCheckRegisterSearch.grid.store.load();
            }
         }]
	});
	
	PartsCheckRegisterSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsCheck!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: {forceFit: true},
    tbar: ['refresh'],
	fields: [{
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
	        	sortable:false,dataIndex:'checkResult',header:'校验结果'
	        },{
	        	sortable:false,dataIndex:'remark',header:'备注'
	        },{
	        	sortable:false,dataIndex:'handOverEmp',header:'校验人'
	        },{
				sortable:false,header:'校验日期', dataIndex:'checkTime', width:150,xtype:'datecolumn', editor:{ xtype:'my97date' }
			}]
	});
	
	
	//移除监听
	PartsCheckRegisterSearch.grid.un('rowdblclick', PartsCheckRegisterSearch.grid.toEditFn, PartsCheckRegisterSearch.grid);
	
	
	//查询前添加过滤条件
	PartsCheckRegisterSearch.grid.store.on('beforeload',function(){
		PartsCheckRegisterSearch.searchParam = PartsCheckRegisterSearch.searchForm.getForm().getValues();
		PartsCheckRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsCheckRegisterSearch.searchParam);
		var searchParam = PartsCheckRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('checkTime' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'checkTime',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'checkTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'checkTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'checkTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
			whereList.push({ propName: prop, propValue:searchParam[prop], compare: Condition.EQ, stringLike: true });
		}
	    this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	var viewport=new Ext.Viewport({
	        layout:'fit',
	        items:[{
		    layout:'border',frame:true,
		    items:[{
		       	region:'north',
		       	collapsible :true,
		       	title:'查询',
			   	height:150,
		       	frame:true,
		       	items:[PartsCheckRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsCheckRegisterSearch.grid]
		    }]
		 }]
	 });
});
