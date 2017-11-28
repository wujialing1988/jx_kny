Ext.onReady(function(){
	Ext.namespace('MainFrame'); 
	
	MainFrame.loadCount = 0 ;
	
	// 遍历权限菜单
	MainFrame.loadMenus = function(nodes){
		if(MainFrame.loadCount == 1){
			return ;
		}
		MainFrame.loadCount = 1 ;
		// 判断是否有子节点
		if(nodes && nodes.length > 0){
			for (var i = 0; i < nodes.length; i++){
				var node = nodes[0];
				if(!node.leaf){
					MainFrame.addMeun(MainFrame.tb,node,node.children);
					MainFrame.tb.addSeparator();
				}
			}
			MainFrame.tb.doLayout();
		}else{
			MainFrame.tb.add({
		        cls: 'x-btn-text-icon',
		        text: '*没有权限菜单,请联系管理员进行配置'
		    });
			MainFrame.tb.doLayout();
		}
	};
	
	MainFrame.addMeun = function(meun,node,childNodes){
		var scrollMenu = new Ext.menu.Menu();
	    for (var i = 0; i < childNodes.length; i++){
	    	var childNode = childNodes[i];
	    	if(!childNode.leaf){
	    		MainFrame.addItems(scrollMenu,childNode,childNode.children);
	    	}else{
	    		scrollMenu.add({
			        cls: 'x-btn-text-icon',
			        id:childNode.id,
			        iconCls: childNode.iconCls,
			        text: childNode.text,
			        funcaction:childNode.funcaction,
			        funcType:childNode.funcType,
			        handler:function(item){
			        	MainFrame.openNewTab(item.initialConfig.id, item.initialConfig.funcaction, item.text, true, item.initialConfig.funcType);//调用函数，打开新页面
			        }
		        });
	    		if(childNodes.length-1 != i){
	    			scrollMenu.addSeparator();
	    		}
	    	}
	    }
		meun.add({
	        cls: 'x-btn-text-icon',
	        iconCls: node.iconCls,
	        text: node.text,
	        menu: scrollMenu
	    });
	};
	
	MainFrame.addItems = function(meun,node,childNodes){
		var childMenu = new Ext.menu.Menu();
	    for (var i = 0; i < childNodes.length; i++){
	    	var childNode = childNodes[i];
	    	if(!childNode.leaf){
	    		MainFrame.addItems(childMenu,childNode,childNode.children);
	    	}else{
	    		childMenu.add({
			        cls: 'x-btn-text-icon',
			        id:childNode.id,
			        iconCls: childNode.iconCls,
			        text: childNode.text,
			        funcaction:childNode.funcaction,
			        funcType:childNode.funcType,
			        handler:function(item){
			        	MainFrame.openNewTab(item.initialConfig.id, item.initialConfig.funcaction, item.text, true, item.initialConfig.funcType);//调用函数，打开新页面
			        }
		        });
	    		if(childNodes.length-1 != i){
	    			childMenu.addSeparator();
	    		}
	    	}
	    }
	    
	    meun.add({
	        cls: 'x-btn-text-icon',
	        ignoreParentClicks:true,
	        id:node.id,
	        iconCls: node.iconCls,
	        text: node.text,
	        funcaction:node.funcaction,
	        funcType:node.funcType,
	        menu: childMenu
		});

	};
	
	
	/**
	 * Modified by hetao on 2016-04-11 增加自定义报表功能，支持用户自己配件报表查询统计菜单
	 * @param id 应用功能id
	 * @param nodeUrl 应用功能路径
	 * @param tabTitle tab页名称
	 * @param allowClose 是否允许关闭
	 * @param funcType 应用功能类型：前台功能、后台服务、报表功能...
	 */
	MainFrame.openNewTab = function(id, nodeUrl, tabTitle, allowClose, funcType){
		if(tabTitle == '台位图查看' || nodeUrl == '/jsp/twt/html5/index.jsp'){
			window.open(ctx + nodeUrl,tabTitle);
			return;
		}
		if(typeof(nodeUrl) == 'undefined' || nodeUrl == null || nodeUrl == 'null') return;
		var center = Ext.getCmp('MainFrameTabPanel');
		var html = "";
		// 如果应用功能类型为“报表功能”则打开报表页面
		if (FUNC_TYPE_REPORT === funcType) {
			var url = nodeUrl + "?ctx=" + ctx.substring(1);
			html = "<iframe src='" + ctx + "/jsp/jx/common/report.jsp?url=" + encodeURIComponent(url)+"' scrolling='auto' frameborder=0 width=100% height=100%></iframe>";
		} else {
			html = "<iframe src='" + ctx + nodeUrl + "' scrolling='auto' frameborder=0 width=100% height=100%></iframe>";
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
			title: '个人桌面',
			xtype: "panel",
			layout: "fit",	
			closable : false,
			autoScroll: true,
			html: "<iframe src='" + ctx + "/frame/yhgl/DefaultNew.jsp" + "' scrolling='none' frameborder=0 width=100% height=100%></iframe>",
			listeners: {  
				
			}
		 }
		],
		 listeners : {
			 tabchange : function(p,t){
		 	 	if(t.id == 'personalDesk'){
		 	 		var fr = $(document.getElementsByTagName('iframe'))[0];
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
	
	// 获取权限菜单
	var getAuthorityMenuTreeAll = function(){
		Ext.Ajax.request({
			url: ctx + '/menu!authorityMenuTreeAll.action',
			params:{menuid: 'ROOT_0'},
		    //请求成功后的回调函数
		    success: function(response, options){
		        var result = Ext.util.JSON.decode(response.responseText);
		        MainFrame.loadMenus(result);
		    }
		});
	};
	
	
	/** 菜单 **/
	MainFrame.tb = new Ext.Toolbar();

	/** 系统主页框架 */
	MainFrame.mainPanel = new Ext.Panel({
		layout : 'border', border : false, 
		items : [{
			id:'sysMainNorth',
			//顶部栏
			region: 'north',
			contentEl : 'north',
			height : 86,
			collapsed :false,
			collapseMode:'mini',
			border : false,
			bbar:MainFrame.tb
		},{
			//tab区
			region:'center',
			border:false,
			layout:'fit',
			items :[MainFrame.tabPanel]
		}],
		listeners: {
			render : function() {
				getAuthorityMenuTreeAll();
			}
		}
	});
	
	
	var viewport = new Ext.Viewport({ layout:'fit', border:false, items:MainFrame.mainPanel });
});


