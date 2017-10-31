Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsType");
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	PartsType.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
	//规格型号id，用于过滤配件属性信息
//	PartsType.partsTypeIdx = "";
//	//配件分类id，用于过滤规格型号信息
//	PartsType.partsClassIdx = "";
//	//配件分类名称
//	PartsType.className = "";
	//配件分类的状态
	PartsType.status = "";
	//表单组件高宽等设置
	PartsType.labelWidth = 130;
	PartsType.fieldWidth = 180;
	//信息表单
	PartsType.form = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsType.labelWidth,
	    buttonAlign: "center",
	    buttons: [{
	        id:"submitBtn1", text:"保存", iconCls:"saveIcon",
	        handler:function(){
	            var form = PartsType.form.getForm();
	            if (!form.isValid()) return;
	            var url = ctx + "/partsType!saveOrUpdate.action";
	            PartsType.enableForm_zf();
	            PartsType.enableForm();
	    		Ext.getCmp("specificationModelCode").enable();
	            var data = form.getValues();
	            if(data["status"]==status_use){
	            	PartsType.disableForm();
	            }else if(data["status"]==status_invalid){
	            	PartsType.disableForm_zf();
	            }
	            if(data["isHighterPricedParts"]=='是'){
	                data["isHighterPricedParts"]=yes;
	            }else if(data["isHighterPricedParts"]=='否'){
	               data["isHighterPricedParts"]=no;
	            }else if(data["isHighterPricedParts"]=='请选择...'){
	               data["isHighterPricedParts"]="";
	            }
	            if(data["isExtra"]=='是'){
	                data["isExtra"]=yes;
	            }else if(data["isExtra"]=='否'){
	               data["isExtra"]=no;
	            }else if(data["isExtra"]=='请选择...'){
	               data["isExtra"]="";
	            }	            
	            if(data["isHasSeq"]=='是'){
	                data["isHasSeq"]=yes;
	            }else if(data["isHasSeq"]=='否'){
	               data["isHasSeq"]=no;
	            }else if(data["isHasSeq"]=='请选择...'){
	               data["isHasSeq"]="";
	            }
	            Ext.getCmp("specificationModelCode").disable();
	            //手工输入计量单位
	            if(Ext.isEmpty(data.unit) && !Ext.isEmpty(Ext.get("unitCmbo").dom.value)) {
	            	data.unit = Ext.get("unitCmbo").dom.value;
	            }
	            PartsType.loadMask.show();
	            Ext.Ajax.request({
	                url: url,
	                jsonData: data,
	                success: function(response, options){
	                  	PartsType.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        PartsType.store.reload();
	                        if(PartsType.win.title == '新增'){
		                        var url = ctx + "/codeRuleConfig!getConfigRule.action";
								Ext.Ajax.request({
					                url: url,
					                params: {ruleFunction: "PJWZ_PARTS_TYPE_SPECIFICATION_MODEL_CODE"},
					                success: function(response, options){
					                    var result = Ext.util.JSON.decode(response.responseText);
					                    if (result.errMsg == null) {
					                        Ext.getCmp("specificationModelCode").setValue(result.rule);
					                        Ext.getCmp("specificationModelCode").disable();
					                    }
					                },
					                failure: function(response, options){
					                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					                }
					            });
	                        }
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    PartsType.loadMask.hide();
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        }
	    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsType.win.hide();
    	 }
    }],
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:PartsType.labelWidth,
	            columnWidth: 1, 
	            items: [
					{ id:"specificationModelCode", name:"specificationModelCode", fieldLabel:"规格型号编码",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ id:"specificationModel", name:"specificationModel", fieldLabel:"规格型号",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:100 , allowBlank:false,width:PartsType.fieldWidth },
					{ 
						id:"isHasSeq",
						xtype:'combo',
			            fieldLabel: '是否自带编号',
			            store:new Ext.data.SimpleStore({
			               fields:['text','value'],
			               data:[['是',yes],['否',no]]
			            }),
			            width:PartsType.fieldWidth,
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            editable:false,
			            mode:'local'
				},
				{ 
						id:"isHighterPricedParts",
						xtype:'combo',
			            fieldLabel: '是否高价互换配件',
			            store:new Ext.data.SimpleStore({
			               fields:['text','value'],
			               data:[['是',yes],['否',no]]
			            }),
			            width:PartsType.fieldWidth,
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            editable:false,
			            mode:'local'
				},
				{ 
						id:"isExtra",
						xtype:'combo',
			            fieldLabel: '是否额外放行',
			            store:new Ext.data.SimpleStore({
			               fields:['text','value'],
			               data:[['是',yes],['否',no]]
			            }),
			            width:PartsType.fieldWidth,
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            editable:false,
			            mode:'local'
				},
					{ id:"matCode", name:"matCode", fieldLabel:"物料编码",maxLength:50 , width:PartsType.fieldWidth },
                    {id:"unitCmbo",xtype:"EosDictEntry_combo",fieldLabel:"计量单位",hiddenName:"unit",allowBlank:false,editable:true,
				 	displayField:"dictname",valueField:"dictname",dicttypeid:"PJWZ_Parts_TYPE_UNIT",width:PartsType.fieldWidth },
					{ id:"timeLimit", name:"timeLimit", fieldLabel:"最大库存限额(月)",vtype: "positiveInt",maxLength:8 ,width:PartsType.fieldWidth }
	            ]
	        },
	        {xtype:"hidden", name:"recordStatus"},
	        {xtype:"hidden", name:"idx"},
	        {xtype:"hidden", id:"status", name:"status"}
	        ]
	    }]
	});
	PartsType.win = new Ext.Window({
	    title:"新增", maximizable:true, width:400, height:320,
	    plain:true,  layout:"fit", closeAction:"hide",
	    items: PartsType.form
	});
	//数据容器
	PartsType.store = new Ext.data.JsonStore({
		id:"idx", root:"root", totalProperty:"totalProperty", autoLoad:true,
	    url: ctx + "/partsType!findpageList.action",
	    fields: [ "specificationModel","specificationModelCode","partsName","unit","timeLimit","limitKm","limitYears","status","recordStatus",
	    "siteID","creator","createTime","updator","updateTime","idx","partsClassIdx","className","professionalTypeIdx",
	    "professionalTypeName","matCode","isHighterPricedParts","isHasSeq","isExtra" ]
	});
	//选择模式，勾选框可多选
	PartsType.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
	//分页工具
	PartsType.pagingToolbar = Ext.yunda.createPagingToolbar({store: PartsType.store});
	PartsType.grid = new Ext.grid.GridPanel({
	    border: false,
	    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
	    viewConfig: {forceFit: true},
	    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
	    height: document.documentElement.scrollHeight,
	    //可移动列
	    enableColumnMove: true,
	   loadMask: {msg:"正在加载表格数据，请稍等..."},
	    //偶数行变色
	    stripeRows: true,
	    //多选行
	    selModel: PartsType.sm,
	    colModel: new Ext.grid.ColumnModel([
	        PartsType.sm,
	        new Ext.grid.RowNumberer(),
	        { sortable:true, header:"规格型号编码", dataIndex:"specificationModelCode",width:130 },	
	        { sortable:true, header:"规格型号", dataIndex:"specificationModel" },		
	        { sortable:true, header:"配件名称", dataIndex:"partsName" },			
	        { sortable:true, header:"计量单位", dataIndex:"unit" },		
        	{ sortable:true, header:"物料编码", dataIndex:"matCode" },
	        { sortable:true, header:"是否自带编号", dataIndex:"isHasSeq" ,width:130,
	        	renderer : function(value, metaData, record, rowIndex, colIndex, store){
		          	if(value==yes) return "是";
		          	if(value==no) return "否";
		          }
	        	},			
	        { sortable:true, header:"是否高价互换配件", dataIndex:"isHighterPricedParts",width:130,
	        	renderer : function(value, metaData, record, rowIndex, colIndex, store){
		          	if(value==yes) return "是";
		          	if(value==no) return "否";
		          } },	
	        { sortable:true, header:"是否额外放行", dataIndex:"isExtra",width:130,
	        	renderer : function(value, metaData, record, rowIndex, colIndex, store){
		          	if(value==yes) return "是";
		          	if(value==no) return "否";
		          } },			     
	        { sortable:true, header:"最大库存限额(月)", dataIndex:"timeLimit" },			
//	        { sortable:true, header:"最大走行公里", dataIndex:"limitKm" },			
//	        { sortable:true, header:"最大使用年限", dataIndex:"limitYears" },			
	        { sortable:true, header:"状态", 
	          dataIndex:"status", renderer:JX.getBizStatus
	        }		
	    ]),
	    store: PartsType.store,
	    //工具栏
	    tbar: [{
	        text:"查询", iconCls:"searchIcon", handler: function(){
	        	PartsType.win.hide();
        	    PartsType.searchWin.show(); 
	       	}
	    },{
	        text:"新增", iconCls:"addIcon",
	        handler: function(){
	        	PartsType.searchWin.hide();
	            PartsType.form.getForm().reset();
	            //清空专业类型选择控件的值
	            //清空计量单位选择控件的值
            	Ext.getCmp("unitCmbo").clear("unitCmbo");
	            PartsType.win.setTitle("新增");
	            PartsType.win.show();
	            var url = ctx + "/codeRuleConfig!getConfigRule.action";
				Ext.Ajax.request({
	                url: url,
	                params: {ruleFunction: "PJWZ_PARTS_TYPE_SPECIFICATION_MODEL_CODE"},
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        Ext.getCmp("specificationModelCode").setValue(result.rule);
	                        Ext.getCmp("specificationModelCode").disable();
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
	        	Ext.getCmp("status").setValue("0");	             
	            PartsType.enableForm_zf();
	            PartsType.enableForm();
	        }
	    },{
	        text:"启用", iconCls:"acceptIcon",
	        handler: function(){
	            var sm = PartsType.grid.getSelectionModel();
	            var data = sm.getSelections();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
//	            else if(data[0].get("status")!="0"){
//	            	MyExt.Msg.alert("只有新增状态的记录才能启用！");
//	                return;
//	            }
	            var ids = new Array();
	            var flag = new Array(); //标记选择项目
	            for (var i = 0; i < data.length; i++){
	            	if(data[i].data.status==0){
		                ids.push(data[ i ].get("idx"));
	            	}else{
	            		 flag.push(data[i]);
	            	}
	            }
	            PartsType.win.hide();
	            //Ext.Msg.confirm("提示  ", "确定启用所选项？  ", function(btn){
	            if(ids.length>0){
	            	Ext.Msg.confirm('提示',alertOperate(flag,'是否继续启用，新增的项！','start'), function(btn){
		                if(btn == "yes"){
		                    PartsType.win.hide();
		                    Ext.Ajax.request({
		                        url: ctx + "/partsType!updateStatus.action",
		                        params: {ids: ids,flag:"start"},
		                        success: function(response, options){
		                            var result = Ext.util.JSON.decode(response.responseText);
		                            if (result.errMsg == null) {
		                                alertSuccess();
		                                PartsType.store.reload();    
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
	            }else{
	            	 MyExt.Msg.alert(alertOperate(flag,'','start')+'【新增】状态记录可启用！');
	            	 return ;
	            }
	           
	        }
	     },{
	        text:"作废", iconCls:"dustbinIcon",
	        handler: function(){
	            var sm = PartsType.grid.getSelectionModel();
	            var data = sm.getSelections();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
//	            else if(data[0].get("status")!="1"){
//	            	MyExt.Msg.alert("只有启用状态的记录才能作废！");
//	                return;
//	            }
	            var ids = new Array();
	            var flag = new Array(); //标记选择的项目
	            for (var i = 0; i < data.length; i++){
	            	if(data[i].data.status==1 || data[i].data.status==0 ){
		                ids.push(data[ i ].get("idx"));
	            	}else{
	            		 flag.push(data[i]);
	            	}
	            }
	            PartsType.win.hide();
	           // Ext.Msg.confirm("提示  ", "确定作废所选项？  ", function(btn){
		        if(ids.length>0){
		            Ext.Msg.confirm('提示',alertOperate(flag,'是否继续作废所选的项！','invalid'), function(btn){ 
		                if(btn == "yes"){
		                    PartsType.win.hide();
		                    Ext.Ajax.request({
		                        url: ctx + "/partsType!updateStatus.action",
		                        params: {ids: ids,flag:"invalid"},
		                        success: function(response, options){
		                            var result = Ext.util.JSON.decode(response.responseText);
		                            if (result.errMsg == null) {
		                                alertSuccess();
		                                PartsType.store.reload();    
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
	           }else{
	            	 MyExt.Msg.alert(alertOperate(flag,'','invalid')+'【启用】【新增】状态记录可作废！');
	            	 return ;
	            }
	     }
	    },{
	        text:"删除", iconCls:"deleteIcon",
	        handler: function(){
	            var sm = PartsType.grid.getSelectionModel();
	            var data = sm.getSelections();
	            var ids = new Array();
           	    var flag = new Array(); //标记选择的项目
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
				for(var i=0;i<sm.getCount();i++){
//					if(data[i].get("status")!="0"){
//						MyExt.Msg.alert("只有新增状态的记录才能删除！");
//	                	return;
//					}
					if(data[i].data.status==0){
		                ids.push(data[ i ].get("idx"));
	            	}else{
	            		 flag.push(data[i]);
	            	}
	            }
	            PartsType.win.hide();
	           // Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn){
	          if(ids.length>0){
	            Ext.Msg.confirm('提示',alertOperate(flag,'是否继续删除，新增的项！','del'), function(btn){ 	
	                if(btn == "yes"){
	                    PartsType.win.hide();
	                    Ext.Ajax.request({
	                        url: ctx + "/partsType!logicDelete.action",
	                        params: {ids: ids},
	                        success: function(response, options){
	                            var result = Ext.util.JSON.decode(response.responseText);
	                            if (result.errMsg == null) {
	                                alertSuccess();
	                                PartsType.store.reload();    
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
           	 }else{
	            	 MyExt.Msg.alert(alertOperate(flag,'','del')+'【新增】状态记录可删除！');
	            	 return ;
	            }
	        }
	    },{
	        text:"刷新", iconCls:"refreshIcon",
	        handler: function(){
//			    PartsType.store.load({
//			        params: {
//			        start: 0,    limit: PartsType.pagingToolbar.pageSize,
//			        partsClassIdx: PartsType.partsClassIdx,statue:PartsType.statue}
//			    });
	        	self.location.reload();
	        }
	        },"-","状态： ",
			    {   id:"xz",
			    	xtype:"checkbox", boxLabel:"新增&nbsp;&nbsp;&nbsp;&nbsp;", 
			    	checked:true, 
			    	handler: function(v1){
                		checkQuery();
               		 }
			    },
			    {   id:"qy",
			    	xtype:"checkbox", boxLabel:"启用&nbsp;&nbsp;&nbsp;&nbsp;", checked:true,
			    	handler: function(v1){
                		checkQuery();
               		 }},
			    {   id:"zf",
			    	xtype:"checkbox", boxLabel:"作废",
			    	handler: function(v1){
                		checkQuery();
               		 }
		}],
	    bbar: PartsType.pagingToolbar,
	    listeners: {
	        "rowdblclick": {
	            fn: function(grid, idx, e){
	            	PartsType.searchWin.hide();
	                var r = grid.store.getAt(idx);
	                PartsType.win.setTitle("编辑");
	                PartsType.win.show();
	                PartsType.form.getForm().reset();
	                PartsType.form.getForm().loadRecord(r);
	                //回显专业类型选择控件值
//	                var node = {id: 0, text: " "};
//		            if (r.get("professionalTypeIdx") != null)    node = {id: r.get("professionalTypeIdx"), text: r.get("professionalTypeName")};
//		            Ext.getCmp("professionalTypeId").setValue(node);
//	                Ext.getCmp("professionalTypeName").setValue(r.get("professionalTypeName"));
//	                Ext.getCmp("professionalTypeId").loadRecord(r,"professionalTypeIdx","professionalTypeName","professionalTypeId");
	                //回显计量单位值
//	                var dictName = EosDictEntry.getDictname("PJWZ_Parts_TYPE_UNIT",r.get("unit"));
					Ext.getCmp("unitCmbo").loadRecord(null,r.get("unit"),r.get("unit"),'unitCmbo');
	                Ext.getCmp("specificationModelCode").disable();	
                	//启用状态下，只能修改“最大库存期限”，“最大使用年限”，“最大走行公里”，专业类型、物料编码、计量单位
                	if(r.get("status")==status_use){
                		PartsType.enableForm_zf();
	            	}else if(r.get("status")==status_invalid){//作废状态下，不能修改
	            		PartsType.disableForm_zf();
	            	}else {
	            		PartsType.enableForm_zf();
	            		PartsType.enableForm();
	            	}
	            }
	        }
	    }
	});
	//表单字段控件失效
	PartsType.disableForm = function(){
	    Ext.getCmp("partsName").disable();
	    Ext.getCmp("specificationModel").disable();
	}
	//表单字段控件生效
	PartsType.enableForm = function(){
	    Ext.getCmp("partsName").enable();
	    Ext.getCmp("specificationModel").enable();
	}
	//作废状态时表单字段控件失效
	PartsType.disableForm_zf = function(){
		PartsType.disableForm();
	    Ext.getCmp("unitCmbo").disable();
	    Ext.getCmp("matCode").disable();
	    Ext.getCmp("isHighterPricedParts").disable();
	    Ext.getCmp("isHasSeq").disable();
	    Ext.getCmp("timeLimit").disable();
	    Ext.getCmp("submitBtn1").setVisible(false);
	}
	//将在作废状态时设置为失效的表单字段控件生效
	PartsType.enableForm_zf = function(){
		PartsType.disableForm();
	    Ext.getCmp("unitCmbo").enable();
	    Ext.getCmp("matCode").enable();
	    Ext.getCmp("isHighterPricedParts").enable();
	    Ext.getCmp("isHasSeq").enable();
	    Ext.getCmp("timeLimit").enable();
	    Ext.getCmp("submitBtn1").setVisible(true);
	}
	//查询参数表单
	PartsType.searchForm = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: PartsType.labelWidth,
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:100,
	            columnWidth: 1, 
	            items: [
					{ name:"specificationModel", fieldLabel:"规格型号",width: PartsType.fieldWidth },
					{ name:"partsName", fieldLabel:"配件名称",width: PartsType.fieldWidth }
	            ]
	        }
	        ]
	    }]
	});
	//查询参数对象
	PartsType.searchParam = {};
	//查询窗口
	PartsType.searchWin = new Ext.Window({
	    title:"查询", items:PartsType.searchForm,
	    width: 360, height: 150, plain: true, closeAction: "hide",buttonAlign:'center',
	    buttons: [{
	        text: "查询", iconCls: "searchIcon",
	        handler: function(){  
			    getStatue();
			    PartsType.searchParam = PartsType.searchForm.getForm().getValues();
			    var searchParam = PartsType.searchForm.getForm().getValues();
			    searchParam = MyJson.deleteBlankProp(searchParam);
			    PartsType.store.load({
			        params: {
			        start: 0,    limit: PartsType.pagingToolbar.pageSize,
			        entityJson: Ext.util.JSON.encode(searchParam),statue:PartsType.statue}
			    });
	        }
	    }, {
	        text:"重置", iconCls:"resetIcon",
	        handler:function(){	
	        	PartsType.searchForm.getForm().reset();
		        PartsType.searchParam = {} ;
                PartsType.store.load();
	        }
	    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	PartsType.searchWin.hide();
    	 }
    }]
	});
		//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout:"fit", 
		items: [PartsType.grid] 
	});
	
	function getStatue(){
		PartsType.statue="5";
			if(Ext.getCmp("xz").checked){
				PartsType.statue = PartsType.statue+","+status_add;
			}
			if(Ext.getCmp("qy").checked){
				PartsType.statue = PartsType.statue+","+status_use;
			}
			if(Ext.getCmp("zf").checked){
				PartsType.statue = PartsType.statue+","+status_invalid;
			}
	}
	//点击复选框更新数据
	function checkQuery(){
			getStatue();
			PartsType.store.load({
				        params: {
				        	start: 0,    limit: PartsType.pagingToolbar.pageSize
//					        partsClassIdx: PartsType.partsClassIdx
					        //statue:PartsType.statue
					        }
				    });

//			PartsType.searchParam = PartsType.searchForm.getForm().getValues();
//		    var searchParam = PartsType.searchForm.getForm().getValues();
//		    for(prop in searchParam){
//		    	if(searchParam[prop] == "")	delete searchParam[prop];
//		    }
//		    PartsType.store.load({
//		        params: {entityJson: Ext.util.JSON.encode(searchParam),partsClassIdx: PartsType.partsClassIdx,statue:PartsType.statue}
//		    });
		}
	PartsType.statue = "";
	PartsType.store.on("beforeload", function(){
//		var beforeloadParam = {partsClassIdx: PartsType.partsClassIdx};
		this.baseParams.statue = PartsType.statue;  
		this.baseParams.entityJson = Ext.util.JSON.encode(PartsType.searchParam);  
	});
});
	
alertOperate = function(infoArray,msg,status){
	var info = "";
	var msgInfo = "" ;
	var operInfo = "" ;
	if(status=='del'){
		msgInfo = "该操作将不能恢复，是否继续？";
		operInfo = "删除！";
	}
	if(status=='start'){
		msgInfo = "确定【启用】所选项吗？";
		operInfo = "启用！";
	}
	if(status=='invalid'){
		msgInfo = "确定【作废】所选项吗？";
		operInfo = "作废！";
	}
	var titleInfo = "";
	if(infoArray instanceof Array){
		for(var i = 0; i < infoArray.length; i++){
//			if(infoArray[ i ].get("status") == 0){
//				info += (i + 1) + ". 【" + infoArray[ i ].get("specificationModel") + "】刚新增不能"+operInfo+"</br>";
//				msgInfo = msg;
//				titleInfo = "选择的记录！";
//			}
			if(infoArray[ i ].get("status") == 1){
				info += (i + 1) + ". 【" + infoArray[ i ].get("specificationModel") + "】已经启用不能"+operInfo+"</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
			if(infoArray[ i ].get("status") == 2){
				info += (i + 1) + ". 【" + infoArray[ i ].get("specificationModel") + "】已经作废不能"+operInfo+"</br>";
				msgInfo = msg;
				titleInfo = "选择的记录！";
			}
		}
	} else {
		info = infoArray;
	}
	return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}