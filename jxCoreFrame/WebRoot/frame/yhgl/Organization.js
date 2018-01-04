/**
 * 机构人员信息主js文件，本文件构建主体页面布局
 */
Ext.onReady(function(){
	Ext.namespace('orgFrame');                       //机构人员树命名空间
	
	/**
	 * 机构人员树图例
	 */
	orgFrame.orgIconCase = new Ext.Panel({
		layout : 'column',
		frame : false,
		xtype : 'panel',	
		border : false,	
		align : 'center',	
		items : [{
			xtype: 'panel',	border:false, layout:'column', align:'center', columnWidth:0.2, 
			items : []
		},{
			xtype: 'panel',	border:false, layout:'column', align:'center', columnWidth:0.4, 
			items : [{
				border : false, height : 3
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/award_star_gold_1.png"/><label style="font-size:13px"> '+i18n.Organization.unit+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/award_star_gold_3.png"/><label style="font-size:13px">'+ i18n.Organization.section+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/medal_silver_2.png"/><label style="font-size:13px">'+ i18n.Organization.team+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/building.png"/><label style="font-size:13px">'+ i18n.Organization.post+' </label>'
			}]
		},{
			xtype: 'panel',	border:false,	layout:'column',	align:'center', columnWidth:0.4, 
			items : [{
				border : false, height : 3
			},{
				border : false,
				height : 28,				
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/award_star_gold_2.png"/><label style="font-size:13px"> '+i18n.Organization.Bureau+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/medal_silver_1.png"/><label style="font-size:13px"> '+i18n.Organization.workshop+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/medal_silver_3.png"/><label style="font-size:13px"> '+i18n.Organization.Department+' </label>'
			},{
				border : false,
				height : 28,
				html : '<img src="'+ctx+'/frame/resources/images/toolbar/user_suit.png"/><label style="font-size:13px"> '+i18n.Organization.personnel+' </label>'
			}]
		}]
	});
	
	/** 下载构型模板 */
	orgFrame.downloadTemplate = function() {
		window.location.href = ctx + i18n.Organization.importXLS;
	}
	
	// 重新加载树
	orgFrame.reloadTree = function(path) {
		orgtree.tree.root.reload(function() {
			if (!path) orgtree.tree.getSelectionModel().select(orgtree.tree.root);
		});
		if (path == undefined || path == "" || path == "###") {
			orgtree.tree.root.expand();
		} else {
			// 展开树到指定节点
			orgtree.tree.expandPath(path);
			orgtree.tree.selectPath(path);
		}
	}
	
	/** 导入机构人员 */
	orgFrame.importWin = new Ext.Window({
		title:i18n.Organization.importPers,
		width:450, height:160,
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
				fieldLabel:i18n.Organization.chooseFile,
				name:'orgEmpImport',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: i18n.Organization.browseFile
			},{
				xtype:'displayfield'
			},{
				id:'orgImportTip',
				xtype:'displayfield'
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
			text: i18n.Organization.import, iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var filePath = window.find('name', 'orgEmpImport')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert(i18n.Organization.support);
					return;
				}
				form.submit({
                	url: ctx+'/orgEmpImport!saveImport.action',
                	waitTitle:i18n.Organization.wait,
               	 	waitMsg: i18n.Organization.importing, 
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
							orgFrame.reloadTree();
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
				       	 	Ext.Msg.alert(i18n.Organization.import, i18n.Organization.false + action.response.status + "\n" + action.response.responseText);
                		}
				    }
            	}); 
			}
		}, {
			text:i18n.Organization.close, iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
	/** 打开机构人员导入窗口 */
	orgFrame.openImportWin = function() {
		orgFrame.importWin.show();
		
		Ext.Ajax.request({
			url: ctx + "/orgEmpImport!isCanImport.action",
			timeout: 60000,
			success: function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if (result.success == true) {
					var orgImportTip = Ext.getCmp('orgImportTip');
					if (result.isCanImport == false) {
						//alertFail("系统已存在机构数据，Excel中的机构数据将忽略导入！");
						orgImportTip.setValue(i18n.Organization.exit);
					} else {
						orgImportTip.setValue('');
					}
				}
			},
			failure: function(response, options) {
				if (self.loadMask) self.loadMask.hide();
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	}
	
	//组织机构树及图例布局
	orgFrame.orgTreePanel = new Ext.Panel( {
	    layout : 'border',
	    border : false,
	    items : [{
	        region : 'center',  //border布局主区域
	        bodyBorder: false,  //不显示边框
	        autoScroll : true,  //自动滚动条
	        layout : 'fit',     //自动填充
	        bbar: [
			{
				text: '<span style="color:gray;">'+i18n.Organization.TemplateDownload+'</span>', iconCls: 'downloadIcon', handler: orgFrame.downloadTemplate
			},
			'-',
			{
				text: '<span style="color:gray;">'+i18n.Organization.import+'</span>', iconCls: 'page_excelIcon', handler: orgFrame.openImportWin
			}],
	        items : [orgtree.tree]
	    }, {
	    	collapsible : true, //面板可收缩
	    	title : i18n.Organization.GraphicLogo,	//标题
	        region : 'south',   //位于主区域下部
			height : 140,       //图示区域高度
			bodyBorder: false,	//自动滚动条
//	        baseCls: "x-plain",
	        items : [orgFrame.orgIconCase]
	    } ]
	});
	
	
	//页面主框架布局，左：机构人员树及图例，右：多tab列表
	var viewport = new Ext.Viewport({ 
		layout : 'border', 
		border : false,
		items : [{
			region : 'west',
			xtype : 'panel',
			width : 200,
			border : true,
			layout : 'fit',
			items : orgFrame.orgTreePanel
		},{
			id: 'censusPanel',
			region:'center',
			layout:'fit',
			xtype:'panel',
			border:true,
			bodyStyle : "background:#dae7f6;border-style:solid;border-top:1px solid #99bbe8;",
			autoScroll:true,
			items:OrgTab.topTab
		}]
	});
});