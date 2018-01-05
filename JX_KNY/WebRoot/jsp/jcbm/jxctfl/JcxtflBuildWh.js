Ext.onReady(function(){
	Ext.namespace('JcxtflBuildWh');
	JcxtflBuildWh.searchParam = {};
	JcxtflBuildWh.shortName = "";
	JcxtflBuildWh.treePath = "";
	JcxtflBuildWh.path;
	JcxtflBuildWh.editRowRec;

		/** 导入车辆构型分类 */
	JcxtflBuildWh.importWin = new Ext.Window({
		title:i18n.JcxtflBuildWh.title, 
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
				fieldLabel:i18n.JcxtflBuildWh.choiceFile,
				name:'jcxtfl',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: i18n.JcxtflBuildWh.browseFile
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
			text: i18n.JcxtflBuildWh.load, iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var filePath = window.find('name', 'jcxtfl')[0].getValue();
				var hzm = filePath.substring(filePath.lastIndexOf("."));
				if(hzm !== ".xls"){
					MyExt.Msg.alert(i18n.JcxtflBuildWh.msg);
					return;
				}
				form.submit({  
                	url: ctx+'/jcxtflBuild!saveImport.action',  
                	waitTitle:i18n.JcxtflBuildWh.wait,
               	 	waitMsg: i18n.JcxtflBuildWh.loading, 
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
							JcxtflBuildWh.grid.store.reload();
							JcxtflBuildWh.reloadTree();
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
				       	 	Ext.Msg.alert(i18n.JcxtflBuildWh.prompt, i18n.JcxtflBuildWh.false+"\n" + action.response.status + "\n" + action.response.responseText);
                		}
				    }
            	}); 
			}
		}, {
			text:i18n.JcxtflBuildWh.close, iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});


	JcxtflBuildWh.grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + "/jcxtflBuild!pageQuery.action",                //装载列表数据的请求URL
	    saveURL: ctx + "/jcxtflBuild!saveOrUpdate.action",             //保存数据的请求URL
	    storeAutoLoad: false,
		storeId: 'coID',
		tbar: ['search','add',{
			text: i18n.JcxtflBuildWh.delete,
			iconCls: "deleteIcon",
			handler: function() {
				if (!$yd.isSelectedRecord(JcxtflBuildWh.grid)) return;
				Ext.Msg.confirm(i18n.JcxtflBuildWh.prompt, i18n.JcxtflBuildWh.YN, function(btn) {
					if (btn != 'yes') return;
					if (self.loadMask) self.loadMask.show();
					Ext.Ajax.request({
						url: ctx + "/jcxtflBuild!deleteNode.action",
						params: {
							ids: $yd.getSelectedIdx( JcxtflBuildWh.grid, JcxtflBuildWh.grid.storeId)
						},
						success: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							var result = Ext.util.JSON.decode(response.responseText);
							if (result.success == true) {
								JcxtflBuildWh.grid.store.reload();
								JcxtflBuildWh.reloadTree(JcxtflBuildWh.treePath);
								MyExt.Msg.alert(i18n.JcxtflBuildWh.deleteOk);
							} else {
								alertFail(result.errMsg);
							}
						},
						failure: function(response, options) {
							if (self.loadMask) self.loadMask.hide();
							MyExt.Msg.alert(i18n.JcxtflBuildWh.false+"\n" + response.status + "\n" + response.responseText);
						}
					});
				});
			}
		}, '->', '-', {
			text: i18n.JcxtflBuildWh.textD, iconCls:"page_excelIcon", handler: function() {
				var url = ctx + '/jsp/jcbm/jxctfl/'+i18n.JcxtflBuildWh.importXLS+'(t_Jcbm_Jcxtfl).xls';
				window.open(url, '_self', 'width:0,height:0');
			}
		}, {
			text:i18n.JcxtflBuildWh.textU, iconCls:"page_excelIcon", handler: function() {
				JcxtflBuildWh.importWin.show();
			}
		}],
		fields: [{
			header:i18n.JcxtflBuildWh.coID, dataIndex:'coID', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.JcxtflBuildWh.fjdID, dataIndex:'fjdID', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.JcxtflBuildWh.coHaschild, dataIndex:'coHaschild', hidden:true, editor: { xtype:'hidden' }
		},{
			header:i18n.JcxtflBuildWh.flbm, dataIndex:'flbm',editor:{  maxLength:50, allowBlank: false }
		},{
			header:i18n.JcxtflBuildWh.flmc, dataIndex:'flmc',editor:{  maxLength:50, allowBlank: false }
		},{
			header:i18n.JcxtflBuildWh.fljc, dataIndex:'fljc',editor:{  maxLength:50, allowBlank: false }
		},{
			header: i18n.JcxtflBuildWh.professionType,
			dataIndex: 'zylxID',
			editor: {
				id: 'id_zylx_id',
				xtype: "ProfessionalType_comboTree",
				fieldLabel: i18n.JcxtflBuildWh.professionType,
				hiddenName: "zylxID",
				returnField: [{ widgetId: "id_zylx", propertyName: "text" }],
				selectNodeModel: "all"
			},
			renderer: function(value, mateData, record) {
				return record.get('zylx');
			}
		}, {
			header: i18n.JcxtflBuildWh.professionType, dataIndex: 'zylx', hidden: true, editor: { id: "id_zylx" }
		},{
			header:i18n.JcxtflBuildWh.pyjc, dataIndex:'pyjc',editor:{  maxLength:50, readOnly:true }
		},{
			header:i18n.JcxtflBuildWh.lbjbm, dataIndex:'lbjbm'
		},{
			header:i18n.JcxtflBuildWh.sycx, dataIndex:'sycx', editor: { xtype : 'JobProcessDefSelect',allowBlank: false, onTriggerClick:function(){
				if (JcxtflBuildWh.editRowRec && JcxtflBuildWh.editRowRec.data.sycx) {
				    TrainType.trainVehicleCode = JcxtflBuildWh.editRowRec.data.sycx.replace(/\//g, ',');
				}
				TrainType.selectWin.show();
				TrainType.selectWin.getEl().setStyle('z-index','80000');
    			TrainType.grid.store.reload();
			}, id: 'id_sycx'}
		}],
		beforeEditFn: function(rowEditor, rowIndex){
			JcxtflBuildWh.editRowRec = rowEditor.grid.store.getAt(rowIndex);
		    return true;
		},
		searchFn: function(searchParam){ 
			JcxtflBuildWh.searchParam = searchParam ;
	        JcxtflBuildWh.grid.store.load();
		},
		afterSaveSuccessFn:function(){
			JcxtflBuildWh.reloadTree(JcxtflBuildWh.treePath);
			JcxtflBuildWh.grid.store.reload();
		},
		beforeAddButtonFn: function(){
			if(JcxtflBuildWh.nodeIDX == "" || JcxtflBuildWh.nodeIDX == null)
			{
				MyExt.Msg.alert(i18n.JcxtflBuildWh.choiceFatherNode);
				return false;
			}else{
				return true;   	
			}
	    },
		beforeSaveFn:function(rowEditor, changes, record, rowIndex){
			if(record.data.coID == "" || record.data.coID == null){
				record.data.fjdID = JcxtflBuildWh.nodeIDX;
				record.data.coHaschild = 0;
				record.data.coID = null;
			}
			return true;
		},
		click:function(e){
			
		}
	});
	
	// 以下代码用于重置“专业类型”字段
	JcxtflBuildWh.grid.rowEditor.on('beforeedit', function(me, rowIndex) {
		var record = JcxtflBuildWh.grid.store.getAt(rowIndex);
		// 目前发现一个问题，在第一次触发行编辑时，不能正常的回显“专业类型”控件值，暂时未找到解决方案
		Ext.getCmp('id_zylx_id').setDisplayValue(record.get('zylxID'),
				record.get('zylx'));
	});
	JcxtflBuildWh.grid.rowEditor.on('afteredit', function(me, rowIndex) {
		Ext.getCmp('id_zylx_id').clearValue();
	});
	JcxtflBuildWh.grid.rowEditor.on('canceledit', function(me, rowIndex) {
		Ext.getCmp('id_zylx_id').clearValue();
	});

	//确认提交方法，后面可覆盖此方法完成查询
	TrainType.submit = function(){
		var sm = TrainType.grid.getSelectionModel();
	    if (sm.getCount() < 1) {
	        MyExt.Msg.alert(i18n.JcxtflBuildWh.Nochoice);
	        return;
	    }
	    if (sm.getCount() > 40) {
	        MyExt.Msg.alert(i18n.JcxtflBuildWh.maxNum);
	        return;
	    }
	    var objData = sm.getSelections();
	    var dataAry = "";
	    for (var i = 0; i < objData.length; i++){
	    	var data ;
	    	if(i ==  objData.length -1){
	    		data = objData[i].get("typeCode");
	    	}else{
		    	data = objData[i].get("typeCode")+"/" ;	    	
	    	}
	    	if(dataAry == ""){
	    		dataAry = data;
	    	}else{
		        dataAry += data;
	    	}
	    }
	    Ext.getCmp('id_sycx').setValue(dataAry);
		TrainType.selectWin.hide();
	};

	// 重新加载树
	JcxtflBuildWh.reloadTree = function(path) {
		JcxtflBuildWh.tree.root.reload(function() {
			if (!path) JcxtflBuildWh.tree.getSelectionModel().select(JcxtflBuildWh.tree.root);
		});
		if (path == undefined || path == "" || path == "###") {
			JcxtflBuildWh.tree.root.expand();
		} else {
			// 展开树到指定节点
			JcxtflBuildWh.tree.expandPath(path);
			JcxtflBuildWh.tree.selectPath(path);
		}
	}

	JcxtflBuildWh.tree =  new Ext.tree.TreePanel({
		tbar: new Ext.Toolbar(),
		plugins: ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/jcxtflBuild!tree.action"
		}),
		root: new Ext.tree.AsyncTreeNode({
		    text: i18n.JcxtflBuildWh.trainSysCFication,
	        id: "1",
	        leaf: false,
	        expanded :true,
	        listeners: {
	        	expand: function(node){
	        		JcxtflBuildWh.tree.getSelectionModel().select(node);
	        	}
	        }
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	collapsed : false,
    	listeners : {
			beforeload: function(node, e){	        	
            	JcxtflBuildWh.tree.getLoader().dataUrl = ctx + '/jcxtflBuild!tree.action?parentIDX=' + node.id + '&shortName=' + JcxtflBuildWh.shortName;
	        },
    		click: function(node, e) {
    			
	        }
		}
	});
	JcxtflBuildWh.tree.getSelectionModel().on('selectionchange', function( me, node ){
		JcxtflBuildWh.initFn(node)
	})
	
	JcxtflBuildWh.initFn = function(node) {
		if (node == null || node.id == null) {
			return;
		}
		// 获取当前节点的路径信息
		JcxtflBuildWh.treePath = node.getPath();
		JcxtflBuildWh.nodeIDX = node.id;
		JcxtflBuildWh.grid.store.load();	
		JcxtflBuildWh.path = node.getPath('text');	
	}
	
	JcxtflBuildWh.grid.store.on('beforeload', function() {
		var whereList = [];
		var searchParam = JcxtflBuildWh.searchParam;
		whereList.push({
			propName : 'fjdID',
			propValue : JcxtflBuildWh.nodeIDX,
			compare : Condition.EQ,
			stringLike : false
		});
		for(porp in searchParam){
			whereList.push({propName:porp, propValue: searchParam[porp] }) ; 
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
		this.baseParams.sort = "flmc";
		this.baseParams.dir = "ASC";
	});
	
	//tab选项卡布局
	JcxtflBuildWh.tabs = new Ext.TabPanel({
	    activeTab: 0, frame:true, singleSelect: true,
	    items:[{  
	       id: "jcxtflBuildWhTab", title: i18n.JcxtflBuildWh.trainSysCFication, layout:'fit',items:[JcxtflBuildWh.grid]
	    }]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({
	    layout : 'border',
	    items : [ {
	        title:i18n.JcxtflBuildWh.trainSysCFication, width: 280, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        collapsible : true,
		tools: [{
			id: 'refresh', handler: function() {
				JcxtflBuildWh.reloadTree(JcxtflBuildWh.treePath);
			}
		}],
	        autoScroll: true, layout: 'fit',items:[JcxtflBuildWh.tree]
	    }, {
	        region : 'center', layout: 'fit', bodyBorder: false, items: [JcxtflBuildWh.tabs]
	    } ]
	});
});