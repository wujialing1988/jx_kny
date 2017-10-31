/**
 * 节点前后置关系 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('WPNodeSeq');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	WPNodeSeq.searchParams = {};
	WPNodeSeq.wpIDX = "###";
	WPNodeSeq.wpNodeIDX = "###";
	WPNodeSeq.parentWPNodeIDX = "###";
	WPNodeSeq.fieldWidth = 180;
	WPNodeSeq.labelWidth = 100;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义保存表单开始 ************** */
	WPNodeSeq.saveForm = new Ext.form.FormPanel({
		labelWidth: WPNodeSeq.labelWidth,
		layout: 'column', 
		baseCls:'x-plain', border:false,
		style: 'padding:15px;',
		items:[{
			columnWidth: 1,
			baseCls:'x-plain', border:false,
			layout: 'form',
			items:[{
				width: WPNodeSeq.fieldWidth,
				fieldLabel:'前置节点',
				maxLength:50,
				hiddenName: 'preWPNodeName',
				id:"PreWPNode_Combo", 
				xtype: 'Base_combo',
//				queryHql: "From WPNode Where recordStatus=0 And parentWPNodeIDX='" + WPNodeSeq.parentWPNodeIDX + "' And wpIDX ='" + WPNodeSeq.wpIDX + "' And idx<> '" +  WPNodeSeq.wpNodeIDX +"'",
				entity:'com.yunda.jx.pjjx.base.wpdefine.entity.WPNode',
				displayField:'wpNodeName',valueField:'wpNodeName',fields:['idx', 'wpNodeName'],
				returnField :[{widgetId: "preWPNodeIDX", propertyName: "idx"}],
				allowBlank:false
			}]
		}, {
			columnWidth: 1,
			layout: 'form',
			baseCls:'x-plain', border:false,
			items:[{
				width: WPNodeSeq.fieldWidth,
				fieldLabel:'前置类型',
				id:"seqClass_k",
				xtype: 'EosDictEntry_combo', 
				hiddenName: 'seqClass', 
				dicttypeid: 'PJJX_WP_NODE_SEQ_TYPE',
				allowBlank:false,
				displayField:'dictname',valueField:'dictid'
			}]
		}, {
			columnWidth: 1,
			layout: 'form',
			baseCls:'x-plain', border:false,
			items:[{
					xtype: 'compositefield', fieldLabel: '延隔时间', combineErrors: false,
					items: [{
						xtype: 'numberfield', id:"beforeDelayTime_h", name: 'beforeDelayTime_h',maxLength: 2, width: 70
					}, {
						xtype: 'label',
						text: '时',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}, {
						xtype: 'numberfield', id:"beforeDelayTime_m", name: 'beforeDelayTime_m', maxLength: 2, width: 70
					}, {
						xtype: 'label',
						text: '分',
						style: 'height:23px; line-height:23px;'		// 用于设置文件的垂直居中显示
					}]
				}]
		}, {
			columnWidth: 1,
			border: false,
			layout: 'form',
			defaultType: 'hidden',
			items:[{
				fieldLabel: 'idx主键', name:'idx'
			}, {
				fieldLabel: '节点idx主键', name:'wpNodeIDX'
			}, {
				fieldLabel: '前置节点idx主键', name:'preWPNodeIDX', id: 'preWPNodeIDX'
			}]
		}],
		listeners: {
			render: function(form) {
				// 新增前置节点时的“前置节点”下拉框候选数据的过滤，该节点已有的前置节点不再出现在候选数据中
				Ext.getCmp('PreWPNode_Combo').queryHql="From WPNode Where recordStatus=0 And parentWPNodeIDX='" + WPNodeSeq.parentWPNodeIDX + "' And wpIDX ='" + WPNodeSeq.wpIDX + "' And idx<> '" +  WPNodeSeq.wpNodeIDX +"' And idx Not In(Select preWPNodeIDX From WPNodeSeq Where recordStatus=0 And wpNodeIDX ='" + WPNodeSeq.wpNodeIDX + "')";
				// 动态加载Base_Combo控件的候选数据
				Ext.getCmp('PreWPNode_Combo').cascadeStore();
			}
		}
	});
	/** ************** 定义保存表单结束 ************** */
	
	/** ************** 定义前后置节点行编辑表格开始 ************** */
	WPNodeSeq.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/wPNodeSeq!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/wPNodeSeq!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/wPNodeSeq!logicDelete.action',            //删除数据的请求URL
	    tbar:['add', 'delete'],
	    storeAutoLoad: false,
	    saveForm: WPNodeSeq.saveForm,
	    saveWinWidth: 360,
	    saveWinHeight: 180,
	    
	    fields:[{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'节点主键', dataIndex:'wpNodeIDX', hidden:true
		},{
			header:'前置节点', dataIndex:'preWPNodeIDX', hidden: true
		},{
			header:'前置节点', dataIndex:'preWPNodeName'
		},{
			header:'前置类型', dataIndex:'seqClass', 
			renderer: function(v) {
				if (SEQ_CLASS_SS == v) return SEQ_CLASS_SS_NAME;			// 开始-开始
				if (SEQ_CLASS_SF == v) return SEQ_CLASS_SF_NAME;			// 开始-完成
				if (SEQ_CLASS_FF == v) return SEQ_CLASS_FF_NAME;			// 完成-完成
				if (SEQ_CLASS_FS == v) return SEQ_CLASS_FS_NAME;			// 完成-开始
			}
		},{
			header:'延隔时间', dataIndex:'beforeDelayTime'
		}],
		
		// 打开编辑窗口时的函数处理
		afterShowEditWin: function(record, rowIndex){
			// 设置“工期”字段的回显
	    	var beforeDelayTime = record.get('beforeDelayTime');
	    	if (!Ext.isEmpty(beforeDelayTime)) {
				var beforeDelayTime_h = Math.floor(beforeDelayTime/60);
				var beforeDelayTime_m = beforeDelayTime%60;
				// 设置 额定工期_时
				Ext.getCmp("beforeDelayTime_h").setValue(beforeDelayTime_h);
				// 设置 额定工期_分
				Ext.getCmp("beforeDelayTime_m").setValue(beforeDelayTime_m);
	    	}
	    	// 设置“前置节点”Base_Combo的回显
	    	Ext.getCmp('PreWPNode_Combo').setDisplayValue(record.data.preWPNodeName, record.data.preWPNodeName);
	    	
			// 编辑前置节点时的“前置节点”下拉框候选数据的过滤
	    	Ext.getCmp('PreWPNode_Combo').queryHql="From WPNode Where recordStatus=0 And parentWPNodeIDX='" + WPNodeSeq.parentWPNodeIDX + "' And wpIDX ='" + WPNodeSeq.wpIDX + "' And idx<> '" +  WPNodeSeq.wpNodeIDX +"'";
	    	// 动态加载Base_Combo控件的候选数据
	    	Ext.getCmp('PreWPNode_Combo').cascadeStore();
		},
		
		// 打开新增窗口时的函数处理
		afterShowSaveWin: function(){
			// 设置【节点前后置关系】的“节点主键”的字段值
			this.saveForm.find('name', 'wpNodeIDX')[0].setValue(WPNodeSeq.wpNodeIDX);
			
			// 设置【节点前后置关系】的“前置节点主键”的字段默认值
			var PreWPNode_Combo =  Ext.getCmp('PreWPNode_Combo');
			// 新增前置节点时的“前置节点”下拉框候选数据的过滤，该节点已有的前置节点不再出现在候选数据中
			PreWPNode_Combo.queryHql="From WPNode Where recordStatus=0 And parentWPNodeIDX='" + WPNodeSeq.parentWPNodeIDX + "' And wpIDX ='" + WPNodeSeq.wpIDX + "' And idx<> '" +  WPNodeSeq.wpNodeIDX +"' And idx Not In(Select preWPNodeIDX From WPNodeSeq Where recordStatus=0 And wpNodeIDX ='" + WPNodeSeq.wpNodeIDX + "')";
			// 动态加载Base_Combo控件的候选数据
			PreWPNode_Combo.cascadeStore();
			
			// 此处使用了延迟设置，保证“前置节点”下拉框的数据容器（Store）已根据过滤条件成功加载了候选数据
			setTimeout(function(){
				var PreWPNode_Combo_Store = PreWPNode_Combo.getStore();
				if (PreWPNode_Combo_Store.getCount() > 0) {
					var data = PreWPNode_Combo_Store.getAt(0).data;
					// 设置“前置节点”下拉框的初始值（仅用于页面展示）
					PreWPNode_Combo.setDisplayValue(data.wpNodeName, data.wpNodeName);
					// 设置“前置节点主键”下拉框的初始值
					WPNodeSeq.saveForm.find('name', 'preWPNodeIDX')[0].setValue(data.idx);
				}
				
			}, 500);
		    
			// 设置【节点前后置关系】的“类型”的字段默认值为“完成-开始（FS）”
			Ext.getCmp('seqClass_k').setDisplayValue(SEQ_CLASS_FS, SEQ_CLASS_FS_NAME);
		},
		
		// 保存数据时，对冗余字段和组合字段的特殊处理
		beforeSaveFn: function(data){ 
			// 删除用于界面显示的“前置节点名称”冗余字段
			delete data.preWPNodeName;
			
			// 对“延搁时间（分钟）”字段保存时的特殊处理
			var beforeDelayTime_h = data.beforeDelayTime_h;
			var beforeDelayTime_m = data.beforeDelayTime_m;
			data.beforeDelayTime = parseInt(beforeDelayTime_h * 60);
			if (!Ext.isEmpty(beforeDelayTime_m)) {
				 data.beforeDelayTime += parseInt(beforeDelayTime_m);
			}
			delete data.beforeDelayTime_h;
			delete data.beforeDelayTime_m;
			return true; 
		},
		
		// 保存成功后执行的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 对idx主键字段进行回显
	        this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        // 重新加载节点列表
	        WPNode.grid.store.reload();
	    },
	    
		// 删除成功后执行的函数处理
	    afterDeleteFn: function(){ 
	        // 重新加载节点列表
	        WPNode.grid.store.reload();
	    }
	});
	
	WPNodeSeq.grid.store.on('beforeload', function() {
		var searchParams = WPNodeSeq.searchParams;
		searchParams.wpNodeIDX = WPNodeSeq.wpNodeIDX;				// 仅加载正在被编辑的【作业节点】的前置节点数据
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义前后置节点行编辑表格结束 ************** */
	
})