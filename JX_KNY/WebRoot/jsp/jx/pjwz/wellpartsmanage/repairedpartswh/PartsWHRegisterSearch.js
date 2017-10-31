Ext.onReady(function(){
	Ext.namespace('PartsWHRegisterSearch');  
	PartsWHRegisterSearch.fieldWidth = 160;
	PartsWHRegisterSearch.labelWidth = 80;
	PartsWHRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsWHRegisterSearch.callReturnFn=function(node,e){
	PartsWHRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	PartsWHRegisterSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	PartsWHRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	PartsWHRegisterSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: PartsWHRegisterSearch.labelWidth,
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
		      	 labelWidth:PartsWHRegisterSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: PartsWHRegisterSearch.callReturnFn
	        	},{
                  	 id:"partsTypeIdx_id", name:"partsTypeIDX", fieldLabel:"规格型号id",hidden:true
	        	},{
            		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '交件日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'whTime', id: 'startDate_d', format:'Y-m-d', value: PartsWHRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
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
	        	}/*,{
					xtype: 'compositefield', fieldLabel : '单据类型', combineErrors: false,
		        	items: [
			           {   
					    xtype:'checkbox', name:'billType', id: 'billType_sef',boxLabel:'自修&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	checked:true,
						    handler: function(){
						    	PartsWHRegisterSearch.checkQuery();
						    }
					  },{   
					    xtype:'checkbox', name:'billType', id: 'billType_out', boxLabel:'委外修&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	checked:true,
						    handler: function(){
						    	PartsWHRegisterSearch.checkQuery();
						    }
					  }]
	        	}*/]
	        }]
	    }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	PartsWHRegisterSearch.searchParam = PartsWHRegisterSearch.searchForm.getForm().getValues();
			    PartsWHRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsWHRegisterSearch.searchParam);
			    PartsWHRegisterSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                PartsWHRegisterSearch.searchForm.getForm().reset();
            	PartsWHRegisterSearch.searchParam = {};
			    PartsWHRegisterSearch.grid.store.load();
            }
         }]
	}); 
	
	PartsWHRegisterSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/partsWHRegister!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: {forceFit: true},
    tbar: ['refresh'],
	fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'接收库房主键', dataIndex:'whIdx', hidden:true
		},{
			header:'接收库房', dataIndex:'whName'
		},{
			header:'接收人主键', dataIndex:'takeOverEmpId', hidden:true
		},{
			header:'接收人', dataIndex:'takeOverEmp'
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
			header:'交件日期', dataIndex:'whTime',xtype:'datecolumn'
		},{
			header:'交件部门主键', dataIndex:'handOverOrgId',hidden:true
		},{
			header:'交件部门', dataIndex:'handOverOrg'
		},{
			header:'交件人主键', dataIndex:'handOverEmpId', hidden:true
		},{
			header:'交件人', dataIndex:'handOverEmp'
		},{
			header:'单据类型', dataIndex:'billType',hidden:true,
			renderer:function(v){
				if(v == BILLTYPE_SELF ) 
					return '自修';
				if(v == BILLTYPE_OUTSOURCING)
					return '委外修';
				else
				  	return '未知状态';
			   
			}
		},{
			header:'单据状态', dataIndex:'status',hidden:true
		}]
	});
	
	//移除监听
	PartsWHRegisterSearch.grid.un('rowdblclick', PartsWHRegisterSearch.grid.toEditFn, PartsWHRegisterSearch.grid);
	PartsWHRegisterSearch.grid.store.setDefaultSort('whTime', 'DESC');//设置默认排序
	
	//状态多选按钮
	PartsWHRegisterSearch.checkQuery = function(){
		PartsWHRegisterSearch.source = [-1];
		if(Ext.getCmp("billType_sef").checked){
			PartsWHRegisterSearch.source.push('SELF');
		} 
		if(Ext.getCmp("billType_out").checked){
			PartsWHRegisterSearch.source.push('OUTSOURCING');
		} 
		PartsWHRegisterSearch.grid.store.load();
	}
	
	
	//查询前添加过滤条件
	PartsWHRegisterSearch.grid.store.on('beforeload',function(){
		PartsWHRegisterSearch.searchParam = PartsWHRegisterSearch.searchForm.getForm().getValues();
		PartsWHRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsWHRegisterSearch.searchParam);
		var searchParam = PartsWHRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if ('billType' == prop) continue;
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
		if (!Ext.isEmpty(PartsWHRegisterSearch.source)) {
			whereList.push({propName:'billType',propValues:PartsWHRegisterSearch.source,compare:Condition.IN});
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
		       	items:[PartsWHRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsWHRegisterSearch.grid]
		    }]
		 }]
	 });
});