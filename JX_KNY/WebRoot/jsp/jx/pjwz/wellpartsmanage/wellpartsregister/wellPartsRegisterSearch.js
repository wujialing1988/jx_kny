Ext.onReady(function(){
    Ext.namespace("wellPartsRegisterSearch");
	wellPartsRegisterSearch.fieldWidth = 160;
	wellPartsRegisterSearch.labelWidth = 80;
	wellPartsRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	wellPartsRegisterSearch.callReturnFn=function(node,e){
	  wellPartsRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	  wellPartsRegisterSearch.searchForm.find("name","partsTypeIdx")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	wellPartsRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	 
	wellPartsRegisterSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: wellPartsRegisterSearch.labelWidth,
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
		      	 labelWidth:wellPartsRegisterSearch.labelWidth,
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
                     xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
					 hiddenName: 'specificationModel', editable:false,
					 returnFn: wellPartsRegisterSearch.callReturnFn
	        	},{
                  	 id:"partsTypeIdx_id", name:"partsTypeIdx", fieldLabel:"规格型号id",hidden:true
	        	},{
            		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
	        	}]
	        },{
	        	items:[{
	        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
	        	},{
					xtype: 'compositefield', fieldLabel : '接收日期', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'acceptTime', id: 'startDate_d', format:'Y-m-d', value: wellPartsRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
						my97cfg: {maxDate: '#F{$dp.$D(\'endDate_d\')}'}
					}, {
						xtype: 'label',
						text: '至',
						style: 'height: 23px;line-height:23px;'
					}, {
						xtype:'my97date', name: 'acceptTime', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
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
					    xtype:'checkbox', name:'source', id: 'source_new',boxLabel:'新购&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	checked:true,
						    handler: function(){
						    	wellPartsRegisterSearch.checkQuery();
						    }
					  },{   
					    xtype:'checkbox', name:'source', id: 'source_imp', boxLabel:'调入&nbsp;&nbsp;&nbsp;&nbsp;', 
					    	checked:true,
						    handler: function(){
						    	wellPartsRegisterSearch.checkQuery();
						    }
					  }]
	        	}*/]
	        }]
	    }],
	     buttons: [{
            text: "查询", iconCls: "searchIcon", handler: function(){
            	wellPartsRegisterSearch.searchParam = wellPartsRegisterSearch.searchForm.getForm().getValues();
			    wellPartsRegisterSearch.searchParam = MyJson.deleteBlankProp(wellPartsRegisterSearch.searchParam);
			    wellPartsRegisterSearch.grid.store.load();
            }
          },{
            text: "重置", iconCls: "resetIcon", handler: function(){
                wellPartsRegisterSearch.searchForm.getForm().reset();
            	wellPartsRegisterSearch.searchParam = {};
			    wellPartsRegisterSearch.grid.store.load();
            }
         }]
	});
	
	wellPartsRegisterSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/wellPartsRegister!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: {forceFit: true},
    tbar: ['refresh'],
	fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'责任部门ID', dataIndex:'acceptDeptId', hidden:true
		},{
			header:'责任部门序列', dataIndex:'acceptDeptOrgSeq', hidden:true
		},{
			header:'责任部门', dataIndex:'acceptDept'
		},{
			header:'责任部门类型', dataIndex:'acceptDeptType',
			renderer:function(v){
				if(v == ACCEPT_DEPT_TYPE_ORG)
				  return '机构';
				if(v == ACCEPT_DEPT_TYPE_WH) 
				  return '库房';
				else return '未知类型'
			}
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
			header:'配件规格型号主键', dataIndex:'partsTypeIdx',hidden:true
		},{
			header:'接收日期', dataIndex:'acceptTime',xtype:'datecolumn'
		},{
			header:'接收人主键', dataIndex:'takeOverEmpId',hidden:true
		},{
			header:'接收人', dataIndex:'takeOverEmp'
		},{
			header:'单据类型', dataIndex:'source',hidden:true
		},{
			header:'单据状态', dataIndex:'status',hidden:true
		},{
			header:'生产厂家主键', dataIndex:'madeFactoryIdx',hidden:true
		},{
			header:'生产厂家', dataIndex:'madeFactoryName', width:150
		},{
			header:'详细配置', dataIndex:'configDetail', width:150
		},{
			header:'创建人名称', dataIndex:'creatorName', hidden:true
		}]
	});
	
	
	//移除监听
	wellPartsRegisterSearch.grid.un('rowdblclick', wellPartsRegisterSearch.grid.toEditFn, wellPartsRegisterSearch.grid);
	
	
	//状态多选按钮
	wellPartsRegisterSearch.checkQuery = function(){
		wellPartsRegisterSearch.source = [-1];
		if(Ext.getCmp("source_new").checked){
			wellPartsRegisterSearch.source.push('新购');
		} 
		if(Ext.getCmp("source_imp").checked){
			wellPartsRegisterSearch.source.push('调入');
		} 
		wellPartsRegisterSearch.grid.store.load();
	}
	
	//查询前添加过滤条件
	wellPartsRegisterSearch.grid.store.on('beforeload',function(){
		wellPartsRegisterSearch.searchParam = wellPartsRegisterSearch.searchForm.getForm().getValues();
		wellPartsRegisterSearch.searchParam = MyJson.deleteBlankProp(wellPartsRegisterSearch.searchParam);
		var searchParam = wellPartsRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if ('source' == prop) continue;
			if('acceptTime' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'acceptTime',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'acceptTime',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'acceptTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'acceptTime',propValue:whTimeVal[0]+' 23:59:59',compare:6});
			 			}
	              } 
		 		continue;
		 	}
			whereList.push({propName:prop,propValue:searchParam[prop]});
		}
		if (!Ext.isEmpty(wellPartsRegisterSearch.source)) {
			whereList.push({propName:'source',propValues:wellPartsRegisterSearch.source,compare:Condition.IN});
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
		       	items:[wellPartsRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[wellPartsRegisterSearch.grid]
		    }]
		 }]
	 });
});
