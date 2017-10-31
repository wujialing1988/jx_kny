/**
 * 视频监控信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('SiteVideoNvrConfig');                       //定义命名空间
	
	/** **************** 定义全局变量开始 **************** */
	SiteVideoNvrConfig.labelWidth = 100;
	SiteVideoNvrConfig.fieldWidth = 140;
	// 默认值
	SiteVideoNvrConfig.nvrIP = "127.0.0.1";					// 默认NVR网络IP地址
	SiteVideoNvrConfig.nvrPort = "80";						// 默认NVR端口号
	SiteVideoNvrConfig.username = "admin";					// 默认NVR登录用户名
	SiteVideoNvrConfig.password = "12345678a";				// 默认NVR登录密码
	
	SiteVideoNvrConfig.isSaveOrAdd = false;					// 是否点击了【保存并新增】按钮的标识
	SiteVideoNvrConfig.nvrName = "";						// 当前选中的NVR名称
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 保存函数
	SiteVideoNvrConfig.saveFn = function(){
        //表单验证是否通过
        var form = SiteVideoNvrConfig.form.getForm(); 
        if (!form.isValid()) return;
        
        var data = form.getValues();
        
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
        	scope: SiteVideoNvrConfig.form,
            url: ctx + "/siteVideoNvr!saveOrUpdate.action", 
            jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (Ext.isEmpty(result.errMsg)) {
	                // 保存成功后，设置重新加载树后选中新增的NVR
	                SiteVideoNvrConfig.nvrName = result.entity.nvrName;
	                if (!SiteVideoNvrConfig.isSaveOrAdd) {
	                	this.find('name', 'idx')[0].setValue(result.entity.idx);
	                } else {
	                	this.getForm().reset();
	                }
	                // 显示通道号tab
					Ext.getCmp('id_tabpanel').unhideTabStripItem(1);
	                // 重新加载树
	                SiteVideoNvrConfig.tree.root.reload();
                } else {
  					MyExt.TopMsg.failMsg(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    },	
    // 删除函数
    SiteVideoNvrConfig.deleteFn = function() {
    	if (!SiteVideoNvrConfig.tree.root.hasChildNodes) {
    		return;
    	}
    	// Modified by hetao on 2016-06-08 增加勾选的方式，以改进删除功能，使能进行批量删除
    	var nodes = SiteVideoNvrConfig.tree.root.childNodes;
    	var ids = [];
    	for (var i = 0; i < nodes.length; i++) {
    		if (nodes[i].attributes.checked) {
    			ids.push(nodes[i].id);
    		}
    	}
    	if (Ext.isEmpty(ids)) {
    		var node = SiteVideoNvrConfig.tree.getSelectionModel().getSelectedNode();
    		if (node) ids.push(node.id);
    		if (Ext.isEmpty(ids)) {
	    		MyExt.Msg.alert("尚未选择任何记录！");
	    		return;
    		}
    	}
        if(self.loadMask)   self.loadMask.show();
    	var cfg = {
    		scope: SiteVideoNvrConfig.tree,
    		url: ctx + "/siteVideoNvr!delete.action", 
    		params: {ids: ids},
    		//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            SiteVideoNvrConfig.nvrName = null;
		            this.root.reload(); 
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
    	};
    	$yd.confirmAndDelete(cfg);
    }
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义全局树开始 **************** */
	// 机车检修作业流程节点树型列表
	SiteVideoNvrConfig.tree =  new Ext.tree.TreePanel( {
		tbar: [{
			text:'新增', iconCls: 'addIcon', handler: function() {
				SiteVideoNvrConfig.form.setTitle("新增");
				SiteVideoNvrConfig.form.getForm().reset();
        		Ext.getCmp('id_tabpanel').setActiveTab(0);
				Ext.getCmp('id_tabpanel').unhideTabStripItem(0);
				Ext.getCmp('id_tabpanel').hideTabStripItem(1);
			}
		}, {
			text:'删除', iconCls: 'deleteIcon', handler: SiteVideoNvrConfig.deleteFn
		}, {
			text:'刷新', iconCls: 'refreshIcon', handler: function() {
				self.location.reload();
			}
		}],
		plugins: ['multifilter'],
	    loader : new Ext.tree.TreeLoader( {
	        dataUrl : ctx + "/siteVideoNvr!tree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	       	text: '',
	        id: "ROOT_0",
	        leaf: false,
	        listeners: {
	        	expand: function(node) {
	    			if (node.hasChildNodes()) {
	    				if (Ext.isEmpty(SiteVideoNvrConfig.nvrName)) {
		    				SiteVideoNvrConfig.tree.getSelectionModel().select(node.childNodes[0]);
	    				} else {
	    					for (var i = 0; i < node.childNodes.length; i++) {
	    						if (node.childNodes[i].text.split('(')[0] === SiteVideoNvrConfig.nvrName) {
				    				SiteVideoNvrConfig.tree.getSelectionModel().select(node.childNodes[i]);
				    				break;
	    						}
	    					}
	    				}
	    			} else {
	    				SiteVideoNvrConfig.form.getForm().reset();
        				Ext.getCmp('id_tabpanel').setActiveTab(0);
	    				Ext.getCmp('id_tabpanel').hideTabStripItem(1);
	    			}
	        	}
	        }
	    }),
		rootVisible: false,
	    autoScroll : true,
	    autoShow: true,
	    useArrows : true,
	    border : false,
	    listeners: {
	    	click: function(node, e) {
	    		if (SiteVideoNvrConfig.isSaveOrAdd) {
	    			SiteVideoNvrConfig.form.setTitle("编辑");
					SiteVideoNvrConfig.form.find('name', 'idx')[0].setValue(node.id);
					SiteVideoNvrConfig.form.find('name', 'nvrName')[0].setValue(node.attributes["name"]);
					SiteVideoNvrConfig.form.find('name', 'nvrIP')[0].setValue(node.attributes["ip"]);
					SiteVideoNvrConfig.form.find('name', 'nvrPort')[0].setValue(node.attributes["port"]);
					SiteVideoNvrConfig.form.find('name', 'username')[0].setValue(node.attributes["username"]);
					SiteVideoNvrConfig.form.find('name', 'password')[0].setValue(node.attributes["password"]);
					Ext.getCmp('id_tabpanel').unhideTabStripItem(1);
					// 加载NVR下属通道号表格数据
					SiteVideoNvrChanel.videoNvrIDX = node.id;
					SiteVideoNvrChanel.grid.store.load();
	    		}
	    		SiteVideoNvrConfig.isSaveOrAdd = false;
	    	}
	    }    
	});
	
	// 选中的树节点变化时的事件监听函数
	SiteVideoNvrConfig.tree.getSelectionModel().on('selectionchange', function(dsm, node) {
		if (Ext.isEmpty(node)) {
			return;
		}
		if (!SiteVideoNvrConfig.isSaveOrAdd) {
			SiteVideoNvrConfig.form.setTitle("编辑");
			SiteVideoNvrConfig.form.find('name', 'idx')[0].setValue(node.id);
			SiteVideoNvrConfig.form.find('name', 'nvrName')[0].setValue(node.attributes["name"]);
			SiteVideoNvrConfig.form.find('name', 'nvrIP')[0].setValue(node.attributes["ip"]);
			SiteVideoNvrConfig.form.find('name', 'nvrPort')[0].setValue(node.attributes["port"]);
			SiteVideoNvrConfig.form.find('name', 'username')[0].setValue(node.attributes["username"]);
			SiteVideoNvrConfig.form.find('name', 'password')[0].setValue(node.attributes["password"]);
			Ext.getCmp('id_tabpanel').unhideTabStripItem(1);
		} else {
			Ext.getCmp('id_tabpanel').hideTabStripItem(1);
		}
		// 加载NVR下属通道号表格数据
		SiteVideoNvrChanel.videoNvrIDX = node.id;
		SiteVideoNvrChanel.grid.store.load();
	});
	/** **************** 定义全局树结束 **************** */
	
	/** **************** 定义保存表单开始 **************** */
	SiteVideoNvrConfig.form = new Ext.form.FormPanel({
		title:'编辑', frame:true, iconCls: 'nvrIcon',
		padding:"10px",
		labelWidth:SiteVideoNvrConfig.labelWidth,
		defaults: {
			width: SiteVideoNvrConfig.fieldWidth, xtype:"textfield", allowBlank: false, maxLength:50
		},
		items:[{
			fieldLabel:"idx主键", name:"idx", allowBlank: true, hidden:true
		}, {
			fieldLabel:"名称", name:"nvrName"
		}, {
			fieldLabel:"IP地址", name:"nvrIP", value: SiteVideoNvrConfig.nvrIP, vtype:'ip' 
		}, {
			fieldLabel:"端口号", name:"nvrPort", value: SiteVideoNvrConfig.nvrPort, maxLength:10, vtype:'port' 
		}, {
			fieldLabel:"用户名", name:"username", value: SiteVideoNvrConfig.username, maxLength:20
		}, {
			fieldLabel:"密码", name:"password", value: SiteVideoNvrConfig.password, maxLength:20
		}],
					buttonAlign:"center",
					buttons:[{
						text:"保存", iconCls:"saveIcon", handler:function(){
							SiteVideoNvrConfig.isSaveOrAdd = false;
							SiteVideoNvrConfig.saveFn();
						}
					}, {
						text:"保存并新增", iconCls:"addIcon", handler:function(){
							SiteVideoNvrConfig.isSaveOrAdd = true;
							SiteVideoNvrConfig.saveFn();
						}
					}]
								
	});
	/** **************** 定义保存表单结束 **************** */
	
	// 页面自适应布局
	var MyViewport=new Ext.Viewport({
		layout:"border", defaults: {layout:"fit"},
		items:[{
			xtype:"container", region:"center",
			items:[{
				xtype:"tabpanel",
				id:"id_tabpanel",
				activeTab:0,
				defaults: {layout:"fit"},
				items:[{
					title:"基本信息",
					layout:"fit",
					items:SiteVideoNvrConfig.form
				}, {
					title:"视频通道",
					items:SiteVideoNvrChanel.grid
				}]
			}]
		}, {
			xtype:"panel",
			title:"网络硬盘录像机",
			/**
			 * 关于批量导入的说明：
			 * 1、批量导入的数据源必须严格按照导入模板要求的格式进行填写；
			 * 2、对于重复导入的情况，系统处理为更新原有历史数据；
			 */
			tbar:['批量导入：', {
		    	text: '下载导入模板', iconCls: 'application-vnd-ms-excel', handler: 	function() {
		    		self.location.href = ctx + '/siteVideoNvrChanel!downloadImpStencil.action';
		    	}
		    }, '|', {
				text: '导入', iconCls: 'page_excelIcon', handler: function() {
					SiteViedoNvrImport.win.show();
				}
		    }],
			split:true,
			region:"west",
			width:350,
			collapsible:true,
			items: SiteVideoNvrConfig.tree,
	        iconCls : 'icon-expand-all',
	        tools : [ {
	            id : 'refresh',
	            handler: function() {
	            	SiteVideoNvrConfig.tree.root.reload();
	            }
	        }]
		}]
	})
});