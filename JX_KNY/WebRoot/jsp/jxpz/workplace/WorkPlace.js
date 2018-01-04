/**
 * 站点维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("WorkPlace");
	
	WorkPlace.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workPlace!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workPlace!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workPlace!logicDelete.action',            //删除数据的请求URL
	    storeId:'workPlaceCode', singleSelect: true,
		saveFormColNum:1, fieldWidth:200,
		tbar: ["search", "add", "delete", "refresh"],
		fields: [{
			header:i18n.WorkPlace.workPlaceCode, dataIndex:'workPlaceCode', editor:{ allowBlank:false, maxLength:50}
		},{
			header:i18n.WorkPlace.workPlaceName, dataIndex:'workPlaceName', editor:{ allowBlank:false, maxLength:100 }, width: 150
		},{
			header:i18n.WorkPlace.workPlaceDesc, dataIndex:'workPlaceDesc', editor:{ maxLength:500, xtype: "textarea" },width: 200
		}],
		afterDeleteFn: function(){ 
			WorkPlace.workPlaceCode = "##";
			WorkPlace.xgrid.store.load();
		}
	});
	
	WorkPlace.xgrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/workPlaceToOrg!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/workPlaceToOrg!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/workPlaceToOrg!logicDelete.action',            //删除数据的请求URL
		saveFormColNum:2,fieldWidth:200, page: false,
		tbar:[{
			text:i18n.WorkPlace.add,
			iconCls: 'addIcon',
			handler: function(){
				OmOrganizationTreeWin.win.show();
			}
		}, 'delete'],
		storeAutoLoad: false,
		fields: [{
			header:'IDX', dataIndex:'idx',hidden:true,editor:{ xtype: "hidden" }
		},{
			header:i18n.WorkPlace.orgid, dataIndex:'orgid', hidden:true, editor:{ xtype: "hidden" }
		},{
			header:i18n.WorkPlace.orgseq, dataIndex:'orgseq', hidden:true, editor:{ xtype: "hidden" }
		},{
			header:i18n.WorkPlace.orgname, dataIndex:'orgname', editor:{ }, width: 200
		},{
			header:i18n.WorkPlace.workPlaceCode, dataIndex:'workPlaceCode', editor:{ }, hidden: true
		}],
		toEdit: function(){
			return false ;
		},
		beforeShowEditWin: function(record, rowIndex){
			return false ;
		},
		searchFn: function(sp){
			this.sp = sp;
			this.store.load();
		}
	});
	
	WorkPlace.grid.on("rowclick", function(grid, rowIndex){
		WorkPlace.workPlaceCode = grid.store.getAt(rowIndex).id;
		WorkPlace.xgrid.store.load();
	})
	
	WorkPlace.xgrid.store.on("beforeload", function(){
		var sp = WorkPlace.xgrid.sp || {};
		sp.workPlaceCode = WorkPlace.workPlaceCode;
		this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
	
	
	WorkPlace.initTree = function(){
		var tree = OmOrganizationTreeWin.tree;
		tree.on("beforeload", function(node, e){
//			tree.getLoader().dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' 
//			+ node.id + '&isChecked=true&queryHql=[degree]oversea';
			
			tree.getLoader().dataUrl = ctx + '/omOrganizationSelect!customTree.action?parentIDX=' 
			+ node.id + '&isChecked=true';
		});
		OmOrganizationTreeWin.submit = function(){
			var workPlaceCode = $yd.getSelectedIdx(WorkPlace.grid, "workPlaceCode");
			if(workPlaceCode.length > 1){
				MyExt.Msg.alert(i18n.WorkPlace.onlyOne);
				return;
			}else if(workPlaceCode.length < 1){
				MyExt.Msg.alert(i18n.WorkPlace.choiceOne);
				return;
			}
			var nodes = tree.getChecked();
			var data = [];
			for(var i = 0; i < nodes.length; i++){				
				data.push({
					workPlaceCode: workPlaceCode[0],
					orgid: nodes[i].id,
					orgseq: nodes[i].attributes.orgseq,
					orgname: nodes[i].text
				});
			}
			if(nodes.length <= 0){
				MyExt.Msg.alert(i18n.WorkPlace.noChoice);
				return;
			}
			WorkPlace.submit(data);
		}
	}
	WorkPlace.initTree();//初始化树事件
	
	WorkPlace.submit = function(nodeData){
		
		Ext.Ajax.request({
			url: ctx + "/workPlaceToOrg!siteAddOrg.action",
			jsonData: nodeData,
			success: function(response, options){
	            var result = Ext.util.JSON.decode(response.responseText);
	            if(result.success){
	            	alertSuccess();
	            	WorkPlace.xgrid.store.load();
	            	OmOrganizationTreeWin.win.hide();
	            }else{
	            	alertFail(result.errMsg);
	            }
			},
	        failure: function(response, options){
	            MyExt.Msg.alert(i18n.WorkPlace.false+"\n" + response.status + "\n" + response.responseText);
	        }
		});
	}
	//页面布局
	var viewport = new Ext.Viewport({
	    layout:"fit",
	    items: [{
	    	layout:"border",
		    items : [{
		        width : 700,
		        minSize : 500,
		        maxSize : 1000,
		        split : true,
		        region : 'west',
		        layout : 'fit',
		        autoScroll : true,
		        items : [ WorkPlace.grid ]
		    }, {
		        region : 'center',
		        layout : 'fit',
		        title: i18n.WorkPlace.mapping,
		        items: [ WorkPlace.xgrid ]
		    }]
	    }]
	});
});