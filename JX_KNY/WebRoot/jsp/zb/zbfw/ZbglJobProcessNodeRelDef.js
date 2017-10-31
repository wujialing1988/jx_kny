/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbglJobProcessNodeRelDef');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	ZbglJobProcessNodeRelDef.zbfwIDX = "";									// 作业流程idx主键
	ZbglJobProcessNodeRelDef.nodeIDX = "";										// 作业流程节点idx主键
	ZbglJobProcessNodeRelDef.parentIDX = "";										// 上级作业流程节点idx主键
	
	ZbglJobProcessNodeRelDef.fieldWidth = 180;
	ZbglJobProcessNodeRelDef.labelWidth = 100;
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义前后置节点行编辑表格开始 ************** */
	ZbglJobProcessNodeRelDef.grid = new Ext.yunda.RowEditorGrid({
		//调用该grid的table标签页给出了title
//		title: '前置节点明细',
		iconCls:'pageIcon',
	    loadURL: ctx + '/zbglJobProcessNodeRelDef!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/zbglJobProcessNodeRelDef!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/zbglJobProcessNodeRelDef!delete.action',            //删除数据的请求URL
	    tbar:['add', 'delete'],
	    storeAutoLoad: false,
	    fields:[{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'节点主键', dataIndex:'nodeIDX', hidden:true
		},{
			header:'前置节点idx主键', dataIndex:'preNodeIDX', hidden: true
		},{
			header:'顺序号', dataIndex:'preSeqNo', hidden:true
		},{
			header:'前置节点名称', dataIndex:'preNodeName',editor:{
				width: ZbglJobProcessNodeRelDef.fieldWidth,
				fieldLabel:'前置节点名称',
				maxLength:50,
				hiddenName: 'preNodeIDX',
				id:"PreWPNode_Combo", 
				xtype: 'Base_combo',
				entity:'com.yunda.zb.zbfw.entity.ZbglJobProcessNodeDef',
				displayField:'nodeName',valueField:'idx',fields:['idx', 'nodeName'],
				allowBlank:false
			}
		}],
		defaultData: {idx: null},                 //新增时默认Record记录值，必须配置
		afterAddButtonFn: function(){  
			Ext.getCmp('PreWPNode_Combo').clearValue();
		},
		beforeSaveFn: function(rowEditor, changes, record, rowIndex){
	    	record.data.nodeIDX = ZbglJobProcessNodeRelDef.nodeIDX ;
	    	record.data.preNodeIDX = Ext.getCmp('PreWPNode_Combo').getValue();
	    	delete record.data.preNodeName ;
	    	return true ;
	    },
	    beforeEditFn: function(rowEditor, rowIndex){
	    	var record = rowEditor.grid.store.getAt(rowIndex);
	    	// 编辑前置节点时的“前置节点”下拉框候选数据的过滤
	    	Ext.getCmp('PreWPNode_Combo').queryHql="From ZbglJobProcessNodeDef Where recordStatus=0 And parentIDX='" + ZbglJobProcessNodeRelDef.parentIDX + "' And zbfwIDX ='" + ZbglJobProcessNodeRelDef.zbfwIDX +
	    	"' And idx<> '" +  ZbglJobProcessNodeRelDef.nodeIDX +"' And idx Not In (Select preNodeIDX From ZbglJobProcessNodeRelDef Where nodeIDX ='" + ZbglJobProcessNodeRelDef.nodeIDX + "')";
	    	// 动态加载Base_Combo控件的候选数据
	    	Ext.getCmp('PreWPNode_Combo').cascadeStore();
		    return true;
	    },
		// 打开编辑窗口时的函数处理
//		afterShowEditWin: function(record, rowIndex){
//	    	// 设置“前置节点”Base_Combo的回显
//	    	Ext.getCmp('PreWPNode_Combo').setDisplayValue(record.data.preNodeIDX, record.data.preNodeName);
//	    	
//			// 编辑前置节点时的“前置节点”下拉框候选数据的过滤
//	    	Ext.getCmp('PreWPNode_Combo').queryHql="From JobProcessNodeDef Where recordStatus=0 And parentIDX='" + ZbglJobProcessNodeRelDef.parentIDX + "' And zbfwIDX ='" + ZbglJobProcessNodeRelDef.zbfwIDX + "' And idx<> '" +  ZbglJobProcessNodeRelDef.nodeIDX +"'";
//	    	// 动态加载Base_Combo控件的候选数据
//	    	Ext.getCmp('PreWPNode_Combo').cascadeStore();
//		},
		
		// 打开新增窗口时的函数处理
//		afterShowSaveWin: function(){
//			// 设置【节点前后置关系】的“节点主键”的字段值
//			this.saveForm.find('name', 'nodeIDX')[0].setValue(ZbglJobProcessNodeRelDef.nodeIDX);
//			
//			// 设置【节点前后置关系】的“前置节点主键”的字段默认值
//			var PreWPNode_Combo =  Ext.getCmp('PreWPNode_Combo');
//			// 新增前置节点时的“前置节点”下拉框候选数据的过滤，该节点已有的前置节点不再出现在候选数据中
//			PreWPNode_Combo.queryHql="From ZbglJobProcessNodeDef Where recordStatus=0 And parentIDX='" + ZbglJobProcessNodeRelDef.parentIDX + "' And zbfwIDX ='" + ZbglJobProcessNodeRelDef.zbfwIDX + "' And idx<> '" +  ZbglJobProcessNodeRelDef.nodeIDX +"' And idx Not In(Select preNodeIDX From ZbglJobProcessNodeRelDef Where recordStatus=0 And nodeIDX ='" + ZbglJobProcessNodeRelDef.nodeIDX + "')";
//			// 动态加载Base_Combo控件的候选数据
//			PreWPNode_Combo.cascadeStore();
//			
//			// 此处使用了延迟设置，保证“前置节点”下拉框的数据容器（Store）已根据过滤条件成功加载了候选数据
//			setTimeout(function(){
//				var PreWPNode_Combo_Store = PreWPNode_Combo.getStore();
//				if (PreWPNode_Combo_Store.getCount() > 0) {
//					var data = PreWPNode_Combo_Store.getAt(0).data;
//					// 设置“前置节点”下拉框的初始值（仅用于页面展示）
//					PreWPNode_Combo.setDisplayValue(data.idx, data.nodeName);
//				}
//				
//			}, 500);
//		    
//			// 设置【节点前后置关系】的“类型”的字段默认值为“完成-开始（FS）”
//			Ext.getCmp('seqClass_k').setDisplayValue(SEQ_CLASS_FS, SEQ_CLASS_FS_NAME);
//		},
		
		// 保存成功后执行的函数处理
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        // 重新加载节点列表
	        ZbglJobProcessNodeDef.grid.store.reload();
	    },
		// 删除成功后执行的函数处理
	    afterDeleteFn: function(){ 
	        // 重新加载节点列表
	        ZbglJobProcessNodeDef.grid.store.reload();
	    }
	});
	
	ZbglJobProcessNodeRelDef.grid.store.on('beforeload', function() {
		var searchParams = {};
		searchParams.nodeIDX = ZbglJobProcessNodeRelDef.nodeIDX;				// 仅加载正在被编辑的【作业节点】的前置节点数据
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	});
	/** ************** 定义前后置节点行编辑表格结束 ************** */
	
});