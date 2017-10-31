Ext.onReady(function(){
	Ext.namespace('MainFrame'); 
	
	/** 系统权限菜单树 */
	MainFrame.tree = new Ext.tree.TreePanel({
		title: '用户权限菜单导航',
		tbar : null,//new Ext.Toolbar(),
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/menu!authorityMenuTree.action"
		}),
		tools: [{
			id: 'refresh', handler: function() {
				MainFrame.tree.root.reload();
			}
		}],
		root: new Ext.tree.TreeLoader({
			text : '功能权限',
			id : 'ROOT_0'
		}),
		rootVisible : false,
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false,
	    collapsed : false,
	    listeners: {
	    	render : function() {},
	    	click : function (node, e) {
	    		if(typeof(node) =='undefined' || node == null) return;
	    		var nodeUrl = node.attributes.funcaction;
	    		if(nodeUrl == null || nodeUrl == 'null') {
	    			node.expand();
	    		} else {
	    			//判断是否是台位图功能
	    			if(node.attributes && node.attributes.text == '台位图查看'){
	    				var twtWin = window.open(ctx + nodeUrl,node.attributes.text);
	    			}else{
	    				MainFrame.openNewTab(node.id, nodeUrl, node.attributes.text, true, node.attributes.funcType);//调用函数，打开新页面
	    			}
	    			
	    		}
	    	}
	    }
	});
		
	MainFrame.tree.on('beforeload', function(node){
		var menuid = node.id;
		MainFrame.tree.loader.dataUrl = ctx + '/menu!authorityMenuTree.action?&menuid='+menuid;
	});	
	
	/** 系统调试菜单 */
	MainFrame.testtree = new Ext.tree.TreePanel({
		tbar : null,//new Ext.Toolbar(),
		loader: new Ext.tree.TreeLoader(),
		root: new Ext.tree.AsyncTreeNode({
			expanded: true,
			children: tree
		 }),
		 rootVisible: false,
		 autoScroll : true,
	     animate : false,
	     useArrows : false,
	     border : false,
		 listeners: {
		    	render : function() {},
		    	click : function (node, e) {
		    		if(typeof(node) =='undefined' || node == null) return;
		    		var nodeUrl = node.attributes.funcaction;
		    		if(nodeUrl == null || nodeUrl == 'null') {
		    			node.expand();
		    		} else {
		    			//判断是否是台位图功能
		    			if(node.attributes && node.attributes.text == '台位图查看'){
		    				var twtWin = window.open(ctx + nodeUrl,node.attributes.text);
		    			}else{
			    			MainFrame.openNewTab(node.id, nodeUrl, node.attributes.text, true);//调用函数，打开新页面
		    			}
		    		}
		    	}
		    }
	});
	
	/**
	 * Modified by hetao on 2016-04-11 增加自定义报表功能，支持用户自己配件报表查询统计菜单
	 * @param id 应用功能id
	 * @param nodeUrl 应用功能路径
	 * @param tabTitle tab页名称
	 * @param allowClose 是否允许关闭
	 * @param funcType 应用功能类型：前台功能、后台服务、报表功能...
	 */
	MainFrame.openNewTab = function(id, nodeUrl, tabTitle, allowClose, funcType){
		if(typeof(nodeUrl) == 'undefined' || nodeUrl == null || nodeUrl == 'null') return;
		var center = Ext.getCmp('MainFrameTabPanel');
		var html = "";
		// 如果应用功能类型为“报表功能”则打开报表页面
		if (FUNC_TYPE_REPORT === funcType) {
			var url = nodeUrl + "?ctx=" + ctx.substring(1);
			html = "<iframe src='" + ctx + "/jsp/jx/common/report.jsp?url=" + encodeURIComponent(url)+"' scrolling='auto' frameborder=0 width=100% height=100%></iframe>"
		} else {
			html = "<iframe src='" + ctx + nodeUrl + "' scrolling='auto' frameborder=0 width=100% height=100%></iframe>"
		}
		var cfg = {
			id: id,
			title: tabTitle,
			tabTip: tabTitle,
			xtype: "panel",
			closable: (typeof(allowClose) == 'undefined') ? true : allowClose,
			layout: "fit",	
			html: html,
			listeners: {  
				
			}
		};
		center.add(cfg);
		center.setActiveTab(center.getItem(id));
	};
	
	/** 功能页Tab */
	MainFrame.tabPanel = new Ext.TabPanel({
		 activeTab: 0,
		 id : 'MainFrameTabPanel',
		 enableTabScroll : true,
		 items: [{
			id:'personalDesk',
			title: '自定义用户桌面',
			tabTip: '自定义用户桌面',
			xtype: "panel",
			layout: "fit",	
			closable : false,
			autoScroll: true,
			html: "<iframe src='" + ctx + "/jsp/jx/common/personalDesk.jsp" + "' scrolling='auto' frameborder=0 width=100% height=100%></iframe>",
			listeners: {  
				
			}
		 }
		 //,{
		//	 id:'welcome',
		//	 title: '欢迎',
		//	 contentEl : 'center'
		// }
		],
		 listeners : {
	 	 	//欢迎界面被自定义桌面替代
		 	/* tabchange : function(p,t){
		 	 	if(t.id == 'welcome'){
		 	 		Ext.Ajax.request({
						url: ctx + "/todoJob!getInfo.action",
						success: function(response, options){
						       var result = Ext.util.JSON.decode(response.responseText);
						       if (result.errMsg == null && result.success == true) {
						       		document.getElementById("todoJobDiv").innerHTML = result.info;
						       } else {
						              alertFail(result.errMsg);
						       }
						},
						failure: function(response, options){
						       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						}
					});
					if (systemType == 'zb') {
						Ext.Ajax.request({
							url: ctx + '/trainAccessAccount!statistics.action',
							//请求成功后的回调函数
						    success: function(response, options){
						        var result = Ext.util.JSON.decode(response.responseText);
						        
						        if (result.errMsg == null) {       //操作成功     
					        		var html = [];
						        	for(var prop in result) {
						        		html.push('<div class="infoPanel" style="box-shadow:2px 2px 2px ' + prop.split(',')[1] + ';border-color:'+ prop.split(',')[1] +';">');
						        		html.push('<div class="infoPanel_title " ' + 'style="background-color:'+ prop.split(',')[1] +';"' +'>');
						        		html.push('<span class="status">');
					        			html.push(prop.split(',')[0]);							// 状态名称
						        		html.push('</span>');
						        		html.push('<span class="number">');
						        		html.push(result[prop].length);				// 机车统计数量
						        		html.push('</span>');
						        		html.push('</div>');
							        	for (var i = 0; i < result[prop].length; i++) {
							        		html.push('<div class="train_pic_bg infoPanel_items">');
							        		var displayInfo = result[prop][i];
							        		// 车型车号
							        		var content = displayInfo.substring(displayInfo.indexOf(',') + 1);
							        		html.push(content);							        		
							        		html.push('</div>');
							        	}
							        	html.push('</div>');
						        	}
						        	if (html.length > 0) {
						        		document.getElementById("tranAccessAccountDiv").innerHTML = html.join('');
						        	}
						        } else {                           //操作失败
						            alertFail(result.errMsg);
						        }
						    },
						    //请求失败后的回调函数
						    failure: function(response, options){
						        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
						    }
						});
					}
				}
		 	 },*/
			 tabchange : function(p,t){
		 	 	if(t.id == 'personalDesk'){
		 	 		var fr = $(window.frames[0].document.getElementsByTagName('iframe'))[0];
		 	 		//debugger;
		 	 		//fr.attr('src', fr.attr('src'));
		 	 		fr.src = fr.src;
		 	 	}
		 	 },
			 contextmenu : function (tab, target, e) {
				 if(target == null) return;
				 if(typeof(menu)!='undefined') {
					 menu.destroy();
					 menu = null;
				 }
				 if(typeof(menu)=='undefined'||menu == null) {
					 menu = new Ext.menu.Menu({
				          id: 'closeMenu',
				          width:120,
				          height:55,
				          border : true,
				          items : [{
				        	  text : '关闭当前标签',
				        	  //以前的欢迎界面屏蔽
				        	  //iconCls: target.id == 'welcome'?'cancelIcon':"deleteIcon",
				        	 // disabled : target.id == 'welcome'?true:false,
				        	  iconCls: target.id == 'personalDesk'?'cancelIcon':"deleteIcon",
				        	  disabled : target.id == 'personalDesk'?true:false,
				        	  handler : function() {
						         tab.remove(target);
						      }
				          },{
				        	  text : '关闭其他标签',
				        	  iconCls:"closeIcon",
				        	  handler : function() {
				        		var arr = new Array();
				        		tab.items.each(function(item){
				        			//以前的欢迎界面屏蔽
				        			//if(item.id != 'welcome' && item.id != target.id)
				        			if(item.id != 'personalDesk' && item.id != target.id)
				        				tab.remove(item);
				        		});
							  }
				          }]
					 });
					 menu.showAt(e.getXY());
				 }
			 }
		 }
	});
	
	//权限与调试菜单并存的开发模式
	MainFrame.codePanel = new Ext.Panel({
		layout:'border', border:false, 
		defaults: { border: false },
		items:[{
			region:'center',
			autoScroll: true,
			items:[MainFrame.tree]
		},{
			title : '开发调试菜单导航',
			region: 'south',
			height:500,
			layout:'fit',
			collapsible : true,
			collapsed:true,
			items:[MainFrame.testtree]
		}]
		
	});
	
	/** 系统主页框架 */
	MainFrame.mainPanel = new Ext.Panel({
		layout : 'border', border : false, 
		items : [{
			id:'sysMainNorth',
			//顶部栏
			region: 'north',
			contentEl : 'north',
			height : 60,
//			collapsed :true,
			collapseMode:'mini',
			border : false
		},{
			//导航树
			title : '系统菜单导航',
			iconCls : 'icon-expand-all',
			region: 'west',
			collapsible : true,
			width : 250,
			minSize:200, 
			maxSize:500,
			split: true,
			border : false,
			layout:'fit',
			id:'sysMenuTree',
			items :[]
		},{
			//tab区
			region:'center',
			border:false,
			layout:'fit',
			items :[MainFrame.tabPanel]
		}],
		listeners: {
			render : function() {
				//如果当前登录人是超级管理员，则显示调试菜单，否则显示权限菜单
				if(superUsers == null || superUsers == '' || superUsers == 'null'){
					Ext.getCmp('sysMenuTree').items.items[0] = MainFrame.tree;           //放开注释时，只显示权限菜单
					Ext.getCmp('sysMenuTree').items.items[0] = MainFrame.codePanel;		 //放开注释时，上半部为权限菜单，下半部为调试菜单
					MainFrame.tree.root.reload();
					MainFrame.tree.getRootNode().expand();
				} else {
					Ext.getCmp('sysMenuTree').items.items[0] = MainFrame.testtree;
					MainFrame.testtree.getRootNode().expand();
				}
			}
		}
	});
	
	var viewport = new Ext.Viewport({ layout:'fit', border:false, items:MainFrame.mainPanel });
});

