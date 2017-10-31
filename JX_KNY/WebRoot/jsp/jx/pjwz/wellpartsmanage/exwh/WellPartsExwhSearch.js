Ext.onReady(function(){
	Ext.namespace('WellPartsExwhSearch'); //定义命名空间
	WellPartsExwhSearch.fieldWidth = 160;
	WellPartsExwhSearch.labelWidth = 80;
	WellPartsExwhSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	WellPartsExwhSearch.callReturnFn=function(node,e){
	WellPartsExwhSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	WellPartsExwhSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	WellPartsExwhSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	WellPartsExwhSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: WellPartsExwhSearch.labelWidth,
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
		      	 labelWidth:WellPartsExwhSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: WellPartsExwhSearch.callReturnFn
	        	},{
                  	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
	        	},{
            		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '领件日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'whTime', id: 'startDate_d', format:'Y-m-d', value: WellPartsExwhSearch.getCurrentMonth(), width: 100, allowBlank: false,
						my97cfg: {maxDate: '#F{$dp.$D(\'endDate_d\')}'}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height: 23px;line-height:23px;'
					}, {
						xtype:'my97date', name: 'whTime', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
            	WellPartsExwhSearch.searchParam = WellPartsExwhSearch.searchForm.getForm().getValues();
			    WellPartsExwhSearch.searchParam = MyJson.deleteBlankProp(WellPartsExwhSearch.searchParam);
			    WellPartsExwhSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                WellPartsExwhSearch.searchForm.getForm().reset();
            	WellPartsExwhSearch.searchParam = {};
			    WellPartsExwhSearch.grid.store.load();
            }
         }]
	});
	
	WellPartsExwhSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wellPartsExwh!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig: {forceFit: true},
	    tbar: ['refresh'],
		fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'接收库房主键', dataIndex:'whIdx', hidden:true
			},{
				header:'接收库房', dataIndex:'whName'
			},{
				header:'库位名称', dataIndex:'locationName'
			},{
				header:'接收人主键', dataIndex:'acceptEmpId', hidden:true
			},{
				header:'接收人', dataIndex:'acceptEmp'
			},{
				header:'配件识别码', dataIndex:'identificationCode'
			},{
				header:'规格型号', dataIndex:'specificationModel', width:150
			},{
				header:'配件名称', dataIndex:'partsName'
			},{
				header:'配件编号', dataIndex:'partsNo', width:120
			},{
				header:'配件信息主键', dataIndex:'partsAccountIdx',hidden:true
			},{
				header:'配件规格型号主键', dataIndex:'partsTypeIDX',hidden:true
			},{
				header:'领件日期', dataIndex:'whTime',xtype:'datecolumn'
			},{
				header:'接收部门', dataIndex:'acceptOrg'
			},{
				header:'接收部门主键', dataIndex:'acceptOrgID',hidden:true
			},{
				header:'交件人主键', dataIndex:'handOverEmpId', hidden:true
			},{
				header:'交件人', dataIndex:'handOverEmp'
			},{
				header:'出库去向', dataIndex:'toGo'
			},{
				header:'是否配送', dataIndex:'isDeliver'
			},{
				header:'配送时间', dataIndex:'deliverTime',xtype:'datecolumn',format: "Y-m-d H:i"
			},{
				header:'配送地址', dataIndex:'deliverLocation'
			}]
	 });
	//移除监听
	WellPartsExwhSearch.grid.un('rowdblclick', WellPartsExwhSearch.grid.toEditFn, WellPartsExwhSearch.grid);
	
	//查询前添加过滤条件
	WellPartsExwhSearch.grid.store.on('beforeload',function(){
		WellPartsExwhSearch.searchParam = WellPartsExwhSearch.searchForm.getForm().getValues();
		WellPartsExwhSearch.searchParam = MyJson.deleteBlankProp(WellPartsExwhSearch.searchParam);
		var searchParam = WellPartsExwhSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('whTime' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'whTime',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'whTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'whTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'whTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
			whereList.push({propName:prop,propValue:searchParam[prop]});
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
		       	items:[WellPartsExwhSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[WellPartsExwhSearch.grid]
		    }]
		 }]
	 });

});