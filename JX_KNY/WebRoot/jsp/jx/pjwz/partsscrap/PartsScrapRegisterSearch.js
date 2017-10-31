 Ext.onReady(function(){
	Ext.namespace('PartsScrapRegisterSearch');//定义命名空间
	PartsScrapRegisterSearch.fieldWidth = 160;
	PartsScrapRegisterSearch.labelWidth = 80;
	PartsScrapRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsScrapRegisterSearch.callReturnFn=function(node,e){
	PartsScrapRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	PartsScrapRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
		//获取当前日期
	PartsScrapRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	
	PartsScrapRegisterSearch.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain",
    align: "center",
    defaultType: "textfield",
    defaults: { anchor: "98%" },
    layout: "form",
    border: false,
    style: "padding:10px",
    labelWidth: PartsScrapRegisterSearch.labelWidth,
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
	      	 labelWidth:PartsScrapRegisterSearch.labelWidth,
	         columnWidth: 0.33
      	}
    },
    items: [{
        items: [{
        	items:[{
                 xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
				 hiddenName: 'specificationModel', editable:false,
				 returnFn: PartsScrapRegisterSearch.callReturnFn
        	},{
              	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
        	},{
        		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
        	}]
        },{
        	items:[{
        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
        	},{
				xtype: 'compositefield', fieldLabel : '报废日期', combineErrors: false,
		        items: [{
					xtype:'my97date', name: 'scrapDate', id: 'startDate_d', format:'Y-m-d', value: PartsScrapRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
					my97cfg: {maxDate: '#F{$dp.$D(\'endDate_d\')}'}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height: 23px;line-height:23px;'
				}, {
					xtype:'my97date', name: 'scrapDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
        	PartsScrapRegisterSearch.searchParam = PartsScrapRegisterSearch.searchForm.getForm().getValues();
		    PartsScrapRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsScrapRegisterSearch.searchParam);
		    PartsScrapRegisterSearch.grid.store.load();
        }
      },{
        text: "重置", iconCls: "resetIcon", handler: function(){
            PartsScrapRegisterSearch.searchForm.getForm().reset();
        	PartsScrapRegisterSearch.searchParam = {};
		    PartsScrapRegisterSearch.grid.store.load();
        }
      }]
	});
	
	
	PartsScrapRegisterSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsScrapRegister!pageQuery.action',                 //装载列表数据的请求URL
	    tbar:['refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'报废人主键', dataIndex:'scrapEmpId', hidden:true
		},{
			header:'报废人', dataIndex:'scrapEmp'
		},{
			header:'报废部门主键', dataIndex:'scrapOrgId', hidden:true
		},{
			header:'报废部门', dataIndex:'scrapOrg'
		},{
			header:'报废日期', dataIndex:'scrapDate', xtype:'datecolumn'
		},{
			header:'报废理由', dataIndex:'scrapReason'
		},{
			header:'配件信息主键', dataIndex:'partsAccountIdx', hidden:true
		},{
			header:'配件规格型号主键', dataIndex:'partsTypeIDX', hidden:true
		},{
			header:'配件名称', dataIndex:'partsName'
		},{
			header:'规格型号', dataIndex:'specificationModel'
		},{
			header:'配件编号', dataIndex:'partsNo'
		},{
			header:'配件识别码', dataIndex:'identificationCode'
		},{
			header:'单据状态', dataIndex:'status', hidden:true
		}]
	});
	
	//移除监听
	PartsScrapRegisterSearch.grid.un('rowdblclick',PartsScrapRegisterSearch.grid.toEditFn,PartsScrapRegisterSearch.grid);
	PartsScrapRegisterSearch.grid.store.setDefaultSort('scrapDate', 'DESC');//设置默认排序
	//查询前添加过滤条件
	PartsScrapRegisterSearch.grid.store.on('beforeload',function(){
		PartsScrapRegisterSearch.searchParam = PartsScrapRegisterSearch.searchForm.getForm().getValues();
		PartsScrapRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsScrapRegisterSearch.searchParam);
		var searchParam = PartsScrapRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('scrapDate' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'scrapDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'scrapDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
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
		       	items:[PartsScrapRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsScrapRegisterSearch.grid]
		    }]
		 }]
	 });
});