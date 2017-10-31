Ext.onReady(function(){
	Ext.namespace('PartsCancelRegisterSearch');  //定义命名空间
	
	PartsCancelRegisterSearch.fieldWidth = 160;
	PartsCancelRegisterSearch.labelWidth = 80;
	PartsCancelRegisterSearch.searchParam = {};
	
	//规格型号选择控件赋值函数
	PartsCancelRegisterSearch.callReturnFn=function(node,e){
	PartsCancelRegisterSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	PartsCancelRegisterSearch.searchForm.find("name","partsTypeIdx")[0].setValue(node.attributes["id"]);
	}
	
	//获取当前日期
	PartsCancelRegisterSearch.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	 }
	
	PartsCancelRegisterSearch.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain",
    align: "center",
    defaultType: "textfield",
    defaults: { anchor: "98%" },
    layout: "form",
    border: false,
    style: "padding:10px",
    labelWidth: PartsCancelRegisterSearch.labelWidth,
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
	      	 labelWidth:PartsCancelRegisterSearch.labelWidth,
	         columnWidth: 0.33
      	}
    },
    items: [{
        items: [{
        	items:[{
                 xtype:"PartsTypeTreeSelect",fieldLabel: '配件规格型号',id:'PartsTypeTreeSelect_select',
				 hiddenName: 'specificationModel', editable:false,
				 returnFn: PartsCancelRegisterSearch.callReturnFn
        	},{
              	 id:"partsTypeIdx_id", name:"partsTypeIdx", fieldLabel:"规格型号id",hidden:true
        	},{
        		 id:"identificationCode", name:"identificationCode", fieldLabel:"配件识别码" 
        	}]
        },{
        	items:[{
        		 id:"partsName", name:"partsName", fieldLabel:"配件名称" 
        	},{
				xtype: 'compositefield', fieldLabel : '销账日期', combineErrors: false,
		        items: [{
					xtype:'my97date', name: 'canceledDate', id: 'startDate_d', format:'Y-m-d', value: PartsCancelRegisterSearch.getCurrentMonth(), width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "开始日期不能大于结束日期";
						}
					}
				}, {
					xtype: 'label',
					text: '至',
					style: 'height: 23px;line-height:23px;'
				}, {
					xtype:'my97date', name: 'canceledDate', id: 'endDate_d', format:'Y-m-d', width: 100, allowBlank: false,
					// 日期校验器
					validator: function() {
						var startDate =new Date(Ext.getCmp('startDate_d').getValue());
						var endDate = new Date(Ext.getCmp('endDate_d').getValue());
						if (startDate > endDate) {
							return "结束日期不能小于开始日期";
						}
					}
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
        	PartsCancelRegisterSearch.searchParam = PartsCancelRegisterSearch.searchForm.getForm().getValues();
		    PartsCancelRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsCancelRegisterSearch.searchParam);
		    PartsCancelRegisterSearch.grid.store.load();
        }
      },{
        text: "重置", iconCls: "resetIcon", handler: function(){
            PartsCancelRegisterSearch.searchForm.getForm().reset();
        	PartsCancelRegisterSearch.searchParam = {};
		    PartsCancelRegisterSearch.grid.store.load();
        }
     }]
	});
	PartsCancelRegisterSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsCancelRegister!pageQuery.action',                 //装载列表数据的请求URL
	    searchFormColNum:2,
	    tbar:[{text:'撤销', iconCls:'chart_attributeConfigIcon',
	     handler: function(){
	     	var data = PartsCancelRegisterSearch.grid.selModel.getSelections();
	     	if(data.length<1){
	       	  MyExt.Msg.alert("尚未选择一条记录！");
	       	  return ;
	       }
	       var ids = new Array();
		   for(var i=0;i<data.length;i++){
		 		ids.push(data[i].get('idx'));
		    }
		 	Ext.Msg.confirm('提示', "是否确认撤销？", function(btn){
				if (btn == 'yes') {
				 	Ext.Ajax.request({
				        url: ctx + "/partsCancelRegister!updatePartsCancelWeb.action",
				        params: {ids : ids},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null) {
				                alertSuccess();
				                PartsCancelRegisterSearch.grid.store.reload(); 
				            } else {
				                alertFail(result.errMsg);
				            }
				        },
				        failure: function(response, options){
				            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				        }
					});
				}
		 	});
	     }},'refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'销账人主键', dataIndex:'canceledEmpId', hidden:true
		},{
			header:'配件信息主键', dataIndex:'partsAccountIdx',hidden:true
		},{
			header:'配件规格型号主键', dataIndex:'partsTypeIdx',hidden:true
		},{
			header:'配件名称', dataIndex:'partsName'
		},{
			header:'规格型号', dataIndex:'specificationModel'
		},{
			header:'销账人', dataIndex:'canceledEmp'
		},{
			header:'销账部门主键', dataIndex:'canceledOrgId', hidden:true
		},{
			header:'销账部门', dataIndex:'canceledOrg'
		},{
			header:'销账日期', dataIndex:'canceledDate', xtype:'datecolumn'
		},{
			header:'销账方式', dataIndex:'canceledType'
		},{
			header:'销账理由', dataIndex:'canceledReason'
		},{
			header:'计量单位', dataIndex:'unit', hidden:true
		},{
			header:'配件编号', dataIndex:'partsNo'
		},{
			header:'配件识别码', dataIndex:'identificationCode'
		},{
			header:'是否新品', dataIndex:'isNewParts',hidden:true
		},{
			header:'配件状态编码', dataIndex:'partsStatus',hidden:true
		},{
			header:'配件状态名称', dataIndex:'partsStatusName',hidden:true
		},{
			header:'单据状态', dataIndex:'status', hidden:true
		},{
			header:'创建人名称', dataIndex:'creatorName',hidden:true
		}]
	});
	
	//移除监听
	PartsCancelRegisterSearch.grid.un('rowdblclick', PartsCancelRegisterSearch.grid.toEditFn, PartsCancelRegisterSearch.grid);
	
	//查询前添加过滤条件
	PartsCancelRegisterSearch.grid.store.on('beforeload',function(){
		PartsCancelRegisterSearch.searchParam = PartsCancelRegisterSearch.searchForm.getForm().getValues();
		PartsCancelRegisterSearch.searchParam = MyJson.deleteBlankProp(PartsCancelRegisterSearch.searchParam);
		var searchParam = PartsCancelRegisterSearch.searchParam;
		delete searchParam.PartsTypeTreeSelect_select ;
		var whereList = []; 
		for(prop in searchParam){
			if('canceledDate' == prop){
			 	var whTimeVal = searchParam[prop];
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'canceledDate',propValue:whTimeVal[0]+' 00:00:00',compare:4});
			 				whereList.push({propName:'canceledDate',propValue:whTimeVal[1]+' 23:59:59',compare:6});
			 			}else{
			 				whereList.push({propName:'canceledDate',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'canceledDate',propValue:whTimeVal[0]+' 23:59:59',compare:6});
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
		       	items:[PartsCancelRegisterSearch.searchForm]
		    },{
		      	region:'center',
		      	frame:true,
		      	layout:'fit',
		      	items:[PartsCancelRegisterSearch.grid]
		    }]
		 }]
	 });
});