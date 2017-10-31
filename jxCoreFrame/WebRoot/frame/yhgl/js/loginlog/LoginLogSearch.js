Ext.onReady(function(){
	 Ext.namespace('LoginLogSearch');
	 LoginLogSearch.fieldWidth = 170;
	 LoginLogSearch.labelWidth = 80;
	 LoginLogSearch.searchParam = {};
	 
	 //规格型号选择控件赋值函数
	LoginLogSearch.callReturnFn=function(node,e){
		LoginLogSearch.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
		LoginLogSearch.searchForm.find("name","partsTypeIDX")[0].setValue(node.attributes["id"]);
	 }
	 
	 LoginLogSearch.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain",
	    align: "center",
	    defaultType: "textfield",
	    defaults: { anchor: "98%" },
	    layout: "form",
	    border: false,
	    style: "padding:10px",
	    labelWidth: LoginLogSearch.labelWidth,
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
		         columnWidth: 0.33
	      	}
	    },
	    items: [{
	        items: [{
	        	items:[{
	        		 id:"userid", name:"userid", fieldLabel:"用户名" ,width:LoginLogSearch.fieldWidth 
	        	},{ id:"loginType_comb",xtype: 'EosDictEntry_combo', hiddenName: "loginType",fieldLabel: '登录方式',
						  displayField:'dictname',valueField:'dictname',dicttypeid: 'logintype',width:LoginLogSearch.fieldWidth }]
	        },{
	        	items:[{
	        		 id:"operatorname", name:"operatorname", fieldLabel:"姓名" ,width:LoginLogSearch.fieldWidth 
	        	},{ id:"loginClient_comb",xtype: 'EosDictEntry_combo', hiddenName: "loginClient",fieldLabel: '登录客户端',
						  displayField:'dictname',valueField:'dictname',dicttypeid: 'loginclient',width:LoginLogSearch.fieldWidth }]
	        },{
	        	items:[{
	        			id:"loginLocation", name:"loginLocation", fieldLabel:"登录位置",width:LoginLogSearch.fieldWidth 
	        		},{
					xtype: 'compositefield', fieldLabel : '登入时间', combineErrors: false,
			        items: [{
						xtype:'my97date', name: 'loginInTime', id: 'startDate_d', format:'Y-m-d H:i',initNow:false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "开始日期不能大于结束日期";
							}
						},width:LoginLogSearch.fieldWidth 
					}, {
						xtype: 'label',
						text: '至',
						style: 'height: 23px;line-height:23px;'
					}, {
						xtype:'my97date', name: 'loginInTime', id: 'endDate_d', format:'Y-m-d H:i', initNow:false,
						// 日期校验器
						validator: function() {
							var startDate =new Date(Ext.getCmp('startDate_d').getValue());
							var endDate = new Date(Ext.getCmp('endDate_d').getValue());
							if (startDate > endDate) {
								return "结束日期不能小于开始日期";
							}
						},width:LoginLogSearch.fieldWidth 
					}]
	        	}]
	        }]
	    }],
	     buttons: [{
	        text: "查询", iconCls: "searchIcon", handler: function(){
	        	LoginLogSearch.searchParam = LoginLogSearch.searchForm.getForm().getValues();
			    LoginLogSearch.searchParam = MyJson.deleteBlankProp(LoginLogSearch.searchParam);
			    LoginLogSearch.grid.store.load();
	        }
	      },{
	        text: "重置", iconCls: "resetIcon", handler: function(){
	                LoginLogSearch.searchForm.getForm().reset();
	                Ext.getCmp("loginType_comb").clearValue();
	                Ext.getCmp("loginClient_comb").clearValue();
	            	LoginLogSearch.searchParam = {};
				    LoginLogSearch.grid.store.load();
	            }
	        }]
	  });
	 //配件退库列表
	 LoginLogSearch.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/loginLog!pageQuery.action',                 //装载列表数据的请求URL
	    viewConfig:{},
	    tbar:['refresh'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'用户名', dataIndex:'userid'
		},{
			header:'姓名', dataIndex:'operatorname'
		},{
			header:'登录位置', dataIndex:'loginLocation'
		},{
			header:'登录方式', dataIndex:'loginType'
		},{
			header:'登录客户端', dataIndex:'loginClient'
		},{
			header:'登入时间', dataIndex:'loginInTime', format:'Y-m-d H:i:s',xtype:'datecolumn',width:150
		},{
			header:'登出时间', dataIndex:'loginOutTime', format:'Y-m-d H:i:s',xtype:'datecolumn',width:150
		},{
			header:'IP地址', dataIndex:'ip'
		 }]
	   });
	   
	//移除事件
	LoginLogSearch.grid.un('rowdblclick',LoginLogSearch.grid.toEditFn,LoginLogSearch.grid);
	LoginLogSearch.grid.store.setDefaultSort('loginInTime', 'DESC');//设置默认排序
	//查询前添加过滤条件
	LoginLogSearch.grid.store.on('beforeload',function(){
	LoginLogSearch.searchParam = LoginLogSearch.searchForm.getForm().getValues();
	LoginLogSearch.searchParam = MyJson.deleteBlankProp(LoginLogSearch.searchParam);
	var searchParam = LoginLogSearch.searchParam;
	var whereList = []; 
	for(prop in searchParam){
		 	if('loginInTime' == prop){
			 	var whTimeVal_v = searchParam[prop];
			 	whTimeVal_v = whTimeVal_v.toString();
			 	var whTimeVal = whTimeVal_v.split(",");
			 	if(whTimeVal instanceof Array){
			 		if(whTimeVal.length == 2 ){
			 			if(whTimeVal[1] != "" && whTimeVal[1] != "undefind"){
			 				whereList.push({propName:'loginInTime',propValue:whTimeVal[0],compare:4});
			 				whereList.push({propName:'loginInTime',propValue:whTimeVal[1],compare:6});
			 			}else{
			 				whereList.push({propName:'loginInTime',propValue:whTimeVal[0],compare:4});
			 			}
			 		}else{
			 			whereList.push({propName:'loginInTime',propValue:whTimeVal[0],compare:6});
			 		}
	                } 
		 		continue;
		 	}
		 	whereList.push({propName:prop,propValue:searchParam[prop],compare:8});
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	  	//页面自适应布局
	var viewport=new Ext.Viewport({layout:'fit',items:[{
		 layout:'border',frame:true,
		 items:[{
		       region:'north',
		       collapsible :true,
		       title:'查询',
			   height:140,
		       frame:true,
		       items:[LoginLogSearch.searchForm]
		    },{
		      region:'center',
		      frame:true,
		      layout:'fit',
		      items:[LoginLogSearch.grid]
		    }]	
		}]
	});
});