/**
 * 机车零部件 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
//定义命名空间
	Ext.namespace("JcpjzdBuild");
	JcpjzdBuild.searchParams = {};
	JcpjzdBuild.treePath = "";
	JcpjzdBuild.nodepjbm = "";
	JcpjzdBuild.currentNodeId = "";
	
//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
JcpjzdBuild.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});
//表单组件高宽等设置
JcpjzdBuild.labelWidth = 200;
JcpjzdBuild.fieldWidth = 300;
JcpjzdBuild.addlabelWidth = 100;
JcpjzdBuild.addfieldWidth = 180;

	//表单字段控件失效
	JcpjzdBuild.disableForm = function(){
	    Ext.getCmp("partsName").disable();
	    Ext.getCmp("specificationModel").disable();
	}
	//表单字段控件生效
	JcpjzdBuild.enableForm = function(){
	    Ext.getCmp("partsName").enable();
	    Ext.getCmp("specificationModel").enable();
	}
	//作废状态时表单字段控件失效
	JcpjzdBuild.disableForm_zf = function(){
		JcpjzdBuild.disableForm();
	    Ext.getCmp("unitCmbo").disable();
	    Ext.getCmp("matCode").disable();
	    Ext.getCmp("isHighterPricedParts").disable();
	    Ext.getCmp("isHasSeq").disable();
	    Ext.getCmp("timeLimit").disable();
	    Ext.getCmp("submitBtn1").setVisible(false);
	}
	//将在作废状态时设置为失效的表单字段控件生效
	JcpjzdBuild.enableForm_zf = function(){
		JcpjzdBuild.disableForm();
	    Ext.getCmp("unitCmbo").enable();
	    Ext.getCmp("matCode").enable();
	    Ext.getCmp("isHighterPricedParts").enable();
	    Ext.getCmp("isHasSeq").enable();
	    Ext.getCmp("timeLimit").enable();
	    Ext.getCmp("submitBtn1").setVisible(true);
	}

//规格型号新增表单信息
JcpjzdBuild.partsTypeform = new Ext.form.FormPanel({
	    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
	    layout: "form",		border: false,		style: "padding:10px",		labelWidth: JcpjzdBuild.labelWidth,
	    buttonAlign: "center",
	    buttons: [{
	        id:"submitBtn1", text:"保存", iconCls:"saveIcon",
	        handler:function(){
	            var form = JcpjzdBuild.partsTypeform.getForm();
	            if (!form.isValid()) return;
	            var url = ctx + "/partsType!saveOrUpdate.action";
	            JcpjzdBuild.enableForm_zf();
	            JcpjzdBuild.enableForm();
	    		Ext.getCmp("specificationModelCode").enable();
	            var data = form.getValues();
	            if(data["status"]=='1'){
	            	JcpjzdBuild.disableForm();
	            }else if(data["status"]=='2'){
	            	JcpjzdBuild.disableForm_zf();
	            }
	            if(data["isHighterPricedParts"]=='是'){
	                data["isHighterPricedParts"]='1';
	            }else if(data["isHighterPricedParts"]=='否'){
	               data["isHighterPricedParts"]='0';
	            }else if(data["isHighterPricedParts"]=='请选择...'){
	               data["isHighterPricedParts"]="";
	            }
	            if(data["isExtra"]=='是'){
	                data["isExtra"]='1';
	            }else if(data["isExtra"]=='否'){
	               data["isExtra"]='0';
	            }else if(data["isExtra"]=='请选择...'){
	               data["isExtra"]="";
	            }	            
	            if(data["isHasSeq"]=='是'){
	                data["isHasSeq"]='1';
	            }else if(data["isHasSeq"]=='否'){
	               data["isHasSeq"]='0';
	            }else if(data["isHasSeq"]=='请选择...'){
	               data["isHasSeq"]="";
	            }
	            Ext.getCmp("specificationModelCode").disable();
	            //手工输入计量单位
	            if(Ext.isEmpty(data.unit) && !Ext.isEmpty(Ext.get("unitCmbo").dom.value)) {
	            	data.unit = Ext.get("unitCmbo").dom.value;
	            }
	            JcpjzdBuild.loadMask.show();
	            Ext.Ajax.request({
	                url: url,
	                jsonData: data,
	                success: function(response, options){
	                  	JcpjzdBuild.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        JcpjzdBuild.baseinfogrid.store.reload();
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
    	 	JcpjzdBuild.partsTypewin.hide();
    	 }
    }],
	    items: [{
	        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
	        items: [
	        {
	            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:JcpjzdBuild.labelWidth,
	            columnWidth: 1, 
	            items: [
//	            	{ id:"jcpjbm", name:"jcpjbm", fieldLabel:"零部件名称编码",hidden:true},
//					{ id:"specificationModelCode", name:"specificationModelCode", fieldLabel:"规格型号编码",maxLength:100 , allowBlank:false,width:JcpjzdBuild.fieldWidth },
					{ id:"specificationModel", name:"specificationModel", fieldLabel:"规格型号",maxLength:100 , allowBlank:false,width:JcpjzdBuild.fieldWidth },
					{ id:"partsName", name:"partsName", fieldLabel:"配件名称",maxLength:100 , allowBlank:false,width:JcpjzdBuild.fieldWidth },
					{ 
						id:"isHasSeq",
						xtype:'combo',
			            fieldLabel: '是否自带编号',
			            store:new Ext.data.SimpleStore({
			               fields:['text','value'],
			               data:[['是','1'],['否','0']]
			            }),
			            width:JcpjzdBuild.fieldWidth,
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
			               data:[['是','1'],['否','0']]
			            }),
			            width:JcpjzdBuild.fieldWidth,
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
			               data:[['是','1'],['否','0']]
			            }),
			            width:JcpjzdBuild.fieldWidth,
			            triggerAction:'all',
			            emptyText:'请选择...',
			            valueField:'value',
			            displayField:'text',
			            editable:false,
			            mode:'local'
				},
					{ id:"matCode", name:"matCode", fieldLabel:"物料编码",maxLength:50 , width:JcpjzdBuild.fieldWidth },
                    {id:"unitCmbo",xtype:"EosDictEntry_combo",fieldLabel:"计量单位",hiddenName:"unit",allowBlank:false,editable:true,
				 	displayField:"dictname",valueField:"dictname",dicttypeid:"PJWZ_Parts_TYPE_UNIT",width:JcpjzdBuild.fieldWidth },
					{ id:"timeLimit", name:"timeLimit", fieldLabel:"最大库存限额(月)",vtype: "positiveInt",maxLength:8 ,width:JcpjzdBuild.fieldWidth }
	            ]
	        },
	        {xtype:"hidden", name:"recordStatus"},
	        {xtype:"hidden", name:"idx"}
//	        {xtype:"hidden", id:"status", name:"status"}
	        ]
	    }]
	});

	/** 导入导入机车零部件 */
	JcpjzdBuild.importWin = new Ext.Window({
		title:"导入车辆零部件", 
		width:450, height:120, 
		plain:true, maximizable:false, modal: true,
		closeAction:"hide",
		layout:'fit',
		items: [{
			xtype:"form", id:'form', border:false, style:"padding:10px" ,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			baseCls: "x-plain", defaults:{anchor:"100%"},
			labelWidth:80,
			items:[{
				fieldLabel:'选择文件',
				name:'jcpjzd',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: '浏览文件...'
			}]
		}],
		listeners:{
			// 隐藏窗口时重置上传表单
			hide: function(window) {
				window.find('xtype', 'form')[0].getForm().getEl().dom.reset();
			}
		},
		buttonAlign:'center', 
		buttons:[{
			text: "导入", iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var filePath = window.find('name', 'jcpjzd')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert('该功能仅支持<span style="color:red;"> Excel2003（*.xls） </span>版本文件！');
					return;
				}
				form.submit({  
                	url: ctx+'/jcpjzdBuild!saveImport.action',  
                	waitTitle:'请稍候',
               	 	waitMsg: '正在导入数据请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',
                	// 请求成功后的回调函数
                	success: function(form, action) {
                		var result = Ext.util.JSON.decode(action.response.responseText);
		                if(result.success == true){
							form.getEl().dom.reset();
							// 隐藏文件上传窗口
		                	window.hide();
							alertSuccess();
							JcpjzdBuild.grid.store.reload();
		                	JcpjzdBuild.reloadTree(JcpjzdBuild.treePath);
		                } else {
							alertFail(result.errMsg);
		                }
                	},
                	// 请求失败后的回调函数
				    failure: function(form, action){
                		var result = Ext.util.JSON.decode(action.response.responseText);
                		if (!Ext.isEmpty(result.errMsg)) {
 							alertFail(result.errMsg);
                		} else {
				       	 	Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + action.response.status + "\n" + action.response.responseText);
                		}
				    }
            	}); 
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})

//零部件名称新增弹出窗口
JcpjzdBuild.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:20px",		labelWidth: JcpjzdBuild.addlabelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:JcpjzdBuild.addlabelWidth,
            columnWidth: 1, 
            items: [
				{ id:"jcpjbmForm", name:"jcpjbm", fieldLabel:"编码",maxLength: 50, allowBlank:false,width:JcpjzdBuild.addfieldWidth },
				{ id:"jcpjmcForm", name:"jcpjmc", fieldLabel:"标准名称",maxLength: 100, allowBlank:false,width:JcpjzdBuild.addfieldWidth }
            ]
        },
        {xtype:"hidden", id:"fjdIdForm", name:"fjdId"}
        ]
    }]
        
});


//规格型号新增弹出窗口
JcpjzdBuild.partsTypewin = new Ext.Window({
    title:"新增", maximizable:true, width:560, height:320,
    plain:true,  layout:"fit", closeAction:"hide",
    items: JcpjzdBuild.partsTypeform
});

//零部件名称新增弹出窗口
JcpjzdBuild.win = new Ext.Window({
    title:"新增", maximizable:true, width:380, height:180,
    plain:true, closeAction:"hide", items:JcpjzdBuild.form,
    buttonAlign:"center",
    buttons: [{
        text:"保存", iconCls:"saveIcon",
        handler:function(){
        	Ext.getCmp("jcpjbmForm").enable();
            var form = JcpjzdBuild.form.getForm();            
            if (!form.isValid()) return;
            var url = ctx + "/jcpjzdBuild!saveOrUpdate.action";
            var data = form.getValues();
            if(data.fjdId == ""){
            	data.fjdId = JcpjzdBuild.currentNodeId; 
            }
            JcpjzdBuild.loadMask.show();
            Ext.Ajax.request({
                url: url,
                jsonData: data,
                success: function(response, options){
                  	JcpjzdBuild.loadMask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        alertSuccess();
                        Ext.getCmp("jcpjbmForm").disable();
                        JcpjzdBuild.win.hide();
                        JcpjzdBuild.grid.store.reload();
		                JcpjzdBuild.reloadTree(JcpjzdBuild.treePath);
                    } else {
                        alertFail(result.errMsg);
                    }
                },
                failure: function(response, options){
                    JcpjzdBuild.loadMask.hide();
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	JcpjzdBuild.win.hide();
    	 }
    }]
});

//选择关联规格型号弹出界面
JcpjzdBuild.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:false});//设置多选框
JcpjzdBuild.connectfogrid = new Ext.yunda.Grid({
	region : 'center',
	singleSelect : true,
	loadURL : ctx + "/partsType!pageQuery.action",
    stripeRows: true,
    selModel: JcpjzdBuild.sm,
	height:630,
	tbar: ['search',{
			iconCls : "refreshIcon",
			text:"刷新",
			handler : function(){
					JcpjzdBuild.connectfogrid.store.reload();
			},			
			width : 40
		}],
	fields : [{
		header : '主键',dataIndex : 'idx',hidden : true,editor: { xtype:'hidden' }
	}, {
		header : '规格型号', dataIndex : 'specificationModel'
	}, {
		header : '配件名称', dataIndex : 'partsName'
	}, {
		header : '计量单位', dataIndex : 'unit',editor: { xtype:'hidden' }
	}, {
		header : '物资编码', dataIndex : 'matCode',editor: { xtype:'hidden' }
	}, {
		header : '状态', dataIndex : 'status',editor: { xtype:'hidden' },renderer:JX.getBizStatus
	}],
	searchFn: function(searchParam){ 
		JcpjzdBuild.searchParams = searchParam ;
        JcpjzdBuild.connectfogrid.store.load();
	},
	 toEditFn: Ext.emptyFn
});

//选择关联规格型号前添加查询条件
JcpjzdBuild.connectfogrid.store.on('beforeload', function(){
	var whereList = [];
	var searchParam = JcpjzdBuild.searchParams;
	if (!Ext.isEmpty(JcpjzdBuild.nodepjbm)) {
		sql = "(jcpjbm <> '" + JcpjzdBuild.nodepjbm + "' or jcpjbm is null) and status in (0,1)";
		whereList.push({compare: Condition.SQL, sql: sql});
	}
	for(porp in searchParam){
		whereList.push({propName:porp, propValue: searchParam[porp] }) ; 
	}
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
//	this.baseParams.whereListJson = Ext.encode(whereList);
});


//渲染规格型号状态
JX.getBizStatus = function(status){
    if(status == 0) return "新增";
    if(status == 1) return "启用";
    if(status == 2) return "作废";
}
//选择关联规格型号
JcpjzdBuild.baseinfowin = new Ext.Window({
    title:"选择关联规格型号", maximizable:true, width:700, height:700,
    plain:true, closeAction:"hide", items:JcpjzdBuild.connectfogrid,
    buttonAlign:"center",
    buttons: [{
        text:"保存", iconCls:"saveIcon",
        handler:function(){
           if(!$yd.isSelectedRecord(JcpjzdBuild.connectfogrid)) return;
        		var tempData = JcpjzdBuild.connectfogrid.selModel.getSelections();
        		var dataAry = new Array();
        		if(tempData.length < 1){
        			 MyExt.Msg.alert("请选择关联规格型号");
        			 return;
        		}
        		for(var i = 0;i < tempData.length;i++){
        			var data = {};
        			data.jcpjbm = JcpjzdBuild.nodepjbm;
        			data.idx = tempData[i].get("idx");
        			dataAry.push(data);
        		}
        		Ext.Ajax.request({
	                url: ctx + "/partsType!updateJcpjbm.action",
	                params: {entityJson : Ext.util.JSON.encode(dataAry),Jcpjbm:JcpjzdBuild.nodepjbm},
	               // jsonData: dataAry,
	                success: function(response, options){	                    
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                       // JcxtflFault.faultSelectGrid.getStore().reload();
	                        jcpjzdBildPartsType.grid.getStore().reload();
	                        JcpjzdBuild.baseinfowin.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
        }
    },{
    	 text:"关闭", iconCls:"closeIcon",handler:function(){
    	 	JcpjzdBuild.baseinfowin.hide();
    	 }
    }],
    listeners:{
    	"beforeShow" : function(){
    		JcpjzdBuild.connectfogrid.store.reload();
    	}    	
    }
});

//零部件名称
JcpjzdBuild.grid = new Ext.yunda.Grid({
		region : 'center',
		singleSelect : false,
		loadURL : ctx + "/jcpjzdBuild!pageList.action",
		saveURL : ctx +　"/jcpjzdBuild!saveOrUpdate.action",
		tbar: [{
        	xtype:"label", text:"  标准名称：" 
    	},{			
            xtype: "textfield",    
            id: "jcpjmc_searchId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				var jcpjmc = Ext.getCmp("jcpjmc_searchId").getValue();
				var searchParam = {};
			    searchParam.jcpjmc = jcpjmc;	
			    searchParam.fjdId = JcpjzdBuild.currentNodeId;
			    JcpjzdBuild.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		    	JcpjzdBuild.grid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});	
			},			
			width : 40
		},{
			text : "新增",
			iconCls : "addIcon",
			handler : function(){
				var url = ctx + "/codeRuleConfig!getConfigRule.action";
				Ext.Ajax.request({
	                url: url,
	                params: {ruleFunction: "JCBM_JCPJZD_COID"},
	                success: function(response, options){
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                    	
	                        Ext.getCmp("jcpjbmForm").setValue(result.rule);
	                        Ext.getCmp("jcpjbmForm").disable();
	                        Ext.getCmp("jcpjmcForm").setValue("");
	                    }
	                },
	                failure: function(response, options){
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            });
				JcpjzdBuild.win.show();
			}
		},{
			text: '删除', iconCls:"deleteIcon", handler: function() {
            if (!$yd.isSelectedRecord(JcpjzdBuild.grid)) return;
            	var sm = JcpjzdBuild.grid.getSelectionModel();
            	var data = sm.getSelections();
	            var ids = new Array();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
				for(var i=0;i<sm.getCount();i++){
	                ids.push(data[ i ].get("jcpjbm"));
	            }
				Ext.Msg.confirm("提示  ", "该操作将不能恢复，是否继续？  ", function(btn) {
					if (btn != 'yes') return;
					if (self.loadMask) self.loadMask.show();
					Ext.Ajax.request({
						url: ctx + "/jcpjzdBuild!deleteNode.action",
						params: {
							ids: ids
						},
						success: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.success == true) {
								JcpjzdBuild.grid.store.reload();
		                		JcpjzdBuild.reloadTree(JcpjzdBuild.treePath);
								MyExt.Msg.alert("删除操作成功！");
							} else {
								alertFail(result.errMsg);
							}
						},
						failure: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						}
					});
				});
			}
		}/*,
		 '-', {
			text: '导入机车零部件', iconCls:"page_excelIcon", handler: function() {
				JcpjzdBuild.importWin.show();
			}
		}*/],
		fields : [{
			header : '父节点ID',dataIndex : 'fjdId',hidden : true,editor: { xtype:'hidden' }
		}, {
			header : '编码', dataIndex : 'jcpjbm',editor: { readOnly:true }
		}, {
			header : '标准名称', dataIndex : 'jcpjmc'
		}],
		afterSaveSuccessFn: function(result, response, options){
			this.store.reload();
			JcpjzdBuild.reloadTree(JcpjzdBuild.treePath);
        	alertSuccess();
		}
	});
	
	
//零部件规格型号
JcpjzdBuild.baseinfogrid = new Ext.yunda.Grid({
	region : 'center',
	singleSelect : true,
	loadURL : ctx + "/partsType!findpageList.action",
	saveURL : ctx +　"",
	tbar: [{
		text : "新增",
		iconCls : "addIcon",
		handler : function(){
			JcpjzdBuild.partsTypewin.show();
			if(JcpjzdBuild.partsTypewin.title == '新增'){
                var url = ctx + "/codeRuleConfig!getConfigRule.action";
				Ext.Ajax.request({
	             url: url,
	             params: {ruleFunction: "PJWZ_PARTS_TYPE_SPECIFICATION_MODEL_CODE"},
	             success: function(response, options){
	                 var result = Ext.util.JSON.decode(response.responseText);
	                 if (result.errMsg == null) {
	                 	Ext.getCmp("jcpjbm").setValue(JcpjzdBuild.nodepjbm);
	                 	Ext.getCmp("status").setValue(0);
	                     Ext.getCmp("specificationModelCode").setValue(result.rule);
	                     Ext.getCmp("specificationModelCode").disable();
	                 }
	             },
	             failure: function(response, options){
	                 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	             }
	         });
           }
		}
	}, {
		text: '选择关联', iconCls:"addIcon", handler: function() {
					
			JcpjzdBuild.baseinfowin.show();
		}
	}],
	fields : [{
		header : '主键',dataIndex : 'idx',hidden : true,editor: { xtype:'hidden' }
	}, {
		header : '规格型号', dataIndex : 'specificationModel'
	}, {
		header : '配件名称', dataIndex : 'partsName'
	}, {
		header : '计量单位', dataIndex : 'unit'
	}, {
		header : '物资编码', dataIndex : 'matCode'
	}, {
		header : '状态', dataIndex : 'status',renderer:JX.getBizStatus
	}]
});
	
//查询参数表单
JcpjzdBuild.searchForm = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
    layout: "form",		border: false,		style: "padding:10px",		labelWidth: JcpjzdBuild.labelWidth,
    items: [{
        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
        items: [
        {
            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:JcpjzdBuild.labelWidth,
            columnWidth: 1, 
            items: [
				{ name:"jcpjbm", fieldLabel:"编码",maxLength: 50, allowBlank:false,width:JcpjzdBuild.addfieldWidth },
				{ name:"jcpjmc", fieldLabel:"标准名称",maxLength: 100, allowBlank:false,width:JcpjzdBuild.addfieldWidth }
            ]
        }
        ]
    }]
});
//查询参数对象
JcpjzdBuild.searchParam = {};
//查询窗口
JcpjzdBuild.searchWin = new Ext.Window({
    title:"查询数据", items:JcpjzdBuild.searchForm, iconCls:"searchIcon",
    width: 350, height: 170, plain: true, closeAction: "hide",
    buttons: [{
        id: "searchBtn", text: "查询", iconCls: "searchIcon",
        handler: function(){  
		    JcpjzdBuild.searchParam = JcpjzdBuild.searchForm.getForm().getValues();
		    var searchParam = JcpjzdBuild.searchForm.getForm().getValues();
		    JcpjzdBuild.store.load({
		        params: {entityJson: Ext.util.JSON.encode(searchParam)}
		    });        
        }
    }, {
        text:"重置", iconCls:"resetIcon",
        handler:function(){	JcpjzdBuild.searchForm.getForm().reset(); }
    }]
});
//当前点击的树节点id
JcpjzdBuild.currentNodeId = "ROOT_0";
//专业类型树 
JcpjzdBuild.tree = new Ext.tree.TreePanel({
	tbar :new Ext.Toolbar(),
	plugins: ['multifilter'],
    loader : new Ext.tree.TreeLoader( {
        dataUrl : ctx + "/jcpjzdBuild!tree.action"
    }),
    root: new Ext.tree.AsyncTreeNode({
        text: '零部件名称',
        id: "ROOT_0",
        leaf: false
    }),
    rootVisible: true,
    autoScroll : false,
    animate : false,
    useArrows : false,
    border : false,
    listeners: {
        click: function(node, e) {
        	JcpjzdBuild.currentNodeId = node.id ;
        	JcpjzdBuild.treePath = node.getPath();
        	JcpjzdBuild.nodepjbm = node.attributes.jcpjbm;
            if(node.id=="ROOT_0"){
            	//初始化动作
				JcpjzdBuild.tabs.activate("childTypeTab");
				JcpjzdBuild.tabs.hideTabStripItem("baseinfoTab");
				JcpjzdBuild.tabs.unhideTabStripItem("childTypeTab");
				var searchParams = {};
				JcpjzdBuild.grid.getStore().load({
					params:{
						entityJson:Ext.util.JSON.encode(searchParams)
						}																
				});	
            }else{
				JcpjzdBuild.tabs.setActiveTab(0);
            	JcpjzdBuild.tabs.unhideTabStripItem("baseinfoTab");
				JcpjzdBuild.proSeq = node.attributes.proSeq;
				var searchParams = {};
			    searchParams.fjdId = JcpjzdBuild.currentNodeId;
			    JcpjzdBuild.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParams);
		    	JcpjzdBuild.grid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(searchParams)
					}																
				});	
				jcpjzdBildPartsType.jcpjbm = JcpjzdBuild.nodepjbm;
				var searchParam = {};
			    searchParam.jcpjbm = JcpjzdBuild.nodepjbm;	
			    jcpjzdBildPartsType.grid.getStore().baseParams.entityJson = Ext.util.JSON.encode(searchParam);
		    	jcpjzdBildPartsType.grid.getStore().load({
				params:{
					entityJson:Ext.util.JSON.encode(searchParam)
					}																
				});	
            }
         }
      }
});

JcpjzdBuild.tree.on('beforeload', function(node){
//	var fjdId = node.attributes.jcpjbm;
//	if("ROOT_0"==node.id){
      	var fjdId = node.id;
//    }
    JcpjzdBuild.tree.loader.dataUrl = ctx + '/jcpjzdBuild!tree.action?fjdId=' + fjdId;
});

// 重新加载树
JcpjzdBuild.reloadTree = function(path) {
	JcpjzdBuild.tree.root.reload(function() {
		if (!path) JcpjzdBuild.tree.getSelectionModel().select(JcpjzdBuild.tree.root);
	});
	if (path == undefined || path == "" || path == "###") {
		JcpjzdBuild.tree.root.expand();
	} else {
		// 展开树到指定节点
		JcpjzdBuild.tree.expandPath(path);
		JcpjzdBuild.tree.selectPath(path);
	}
}

//tab选项卡布局
JcpjzdBuild.tabs = new Ext.TabPanel({
    activeTab: 0,
    frame:true,
    items:[{ 
          id: "childTypeTab",
          title: '零部件名称',
          layout: 'fit' ,
          items: [JcpjzdBuild.grid]
       },{  
           id: "baseinfoTab",
           title: '零部件规格型号',
           layout:'fit',
           frame:true,
           items: [jcpjzdBildPartsType.grid]
        }]
});

//页面布局
var viewport = new Ext.Viewport( {
    layout : 'border',
    items : [ {
        title : '<span style="font-weight:normal">车辆零部件</span>',
        iconCls : 'chart_organisationIcon',
        tools : [ {
            id : 'refresh',
            handler : function() {
                JcpjzdBuild.tree.root.reload();
                JcpjzdBuild.tree.getRootNode().expand();
            }
        } ],
        collapsible : true,
        width : 220,
        minSize : 160,
        maxSize : 280,
        split : true,
        region : 'west',
        bodyBorder: false,
        autoScroll : true,
//        collapseMode:'mini',
        items : [ JcpjzdBuild.tree ]
    }, {
        region : 'center',
        layout : 'fit',
        bodyBorder: false,
        items : [ JcpjzdBuild.tabs ]
    } ]
});
JcpjzdBuild.tree.getRootNode().expand();
JcpjzdBuild.tabs.activate("childTypeTab");
JcpjzdBuild.tabs.hideTabStripItem("baseinfoTab");
JcpjzdBuild.tabs.unhideTabStripItem("childTypeTab");

//store载入前查询
JcpjzdBuild.grid.store.on("beforeload", function(){
	var idx = JcpjzdBuild.currentNodeId ;	
	var searchParam = {};
	if(!"ROOT_0"==idx){
    	searchParam.fjdId = idx;
    }
	this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});
