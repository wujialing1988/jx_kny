/**
 * 调度命令单申请 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('ScheduleProposeAndApprove');                       //定义命名空间

	/* ************* 定义全局变量开始 ************* */
    ScheduleProposeAndApprove.searchParams = {status : STATUS_DSP}; ;
	ScheduleProposeAndApprove.labelWidth = 100;
	ScheduleProposeAndApprove.fieldWidth = 140;
	/* ************* 定义全局变量结束 ************* */
	
ScheduleProposeAndApprove.grid = new Ext.yunda.Grid({
	saveURL: ctx + '/scheduleProposeAndApprove!saveOrUpdate.action',
    loadURL: ctx + '/scheduleProposeAndApprove!pageQuery.action',                 //装载列表数据的请求URL
    saveFormColNum:1,
    tbar: [{/*text:"审批", iconCls:"bookIcon", 
    	   handler: function(){
    	   		var sm = ScheduleProposeAndApprove.grid.getSelectionModel();
				if (sm.getCount() <= 0) {
					MyExt.Msg.alert('尚未选择任何数据！');
					return;
				}
				var items = sm.selections.items;
				var ids = [];
				for (var i = 0; i<items.length; i ++) {
					if (STATUS_DSP === items[i].data.status) {
						MyExt.Msg.alert('只能审批待审批的命令单！');
						return;
					}
					ids.push(items[i].id);
				}
				Ext.Msg.confirm('提示', '是否撤销所选命令单，是否确认继续？？', function(btn) {
					if ('yes' == btn) {
						$yd.request({
		        			url: ctx + '/scheduleProposeAndApprove!approveApplication.action',
		        			params: {
		        				ids: Ext.encode(ids)
		        			}
		        		}, function () {
		        			alertSuccess();
		        			ScheduleProposeAndApprove.grid.store.load();
		        		});
					}
				});
    	   }
     */},{
		id: 'topic', xtype: 'textfield', enableKeyEvents: true,  emptyText: '输入主题关键字回车查询...', 
		listeners: {
			keyup: function(me, e) {
				if (e.keyCode === e.ENTER) {
					ScheduleProposeAndApprove.grid.store.load();
				}
			}
		}
	},{
    	text: '重置', iconCls: 'resetIcon', handler: function() {
    		Ext.getCmp('topic').reset();
    		ScheduleProposeAndApprove.grid.store.load();
    	}
	}, '<span style="color:gray;">&nbsp;双击一条记录进行审批操作！</span>', '->', '&nbsp;&nbsp;&nbsp;&nbsp;',  {   
    	xtype:"radio", name:"status", boxLabel:'待审批'+"&nbsp;&nbsp;&nbsp;&nbsp;", checked:true,
    	handler: function(radio, checked){
    		if(checked){
    			ScheduleProposeAndApprove.searchParams = {status: STATUS_DSP};
    			ScheduleProposeAndApprove.grid.store.load();
    		}
    	}
    },
    {   
    	xtype:"radio", name:"status", boxLabel:'已审批',
    	handler: function(radio, checked){
    		if(checked){
    			ScheduleProposeAndApprove.searchParams = {status : STATUS_YSP};
    			ScheduleProposeAndApprove.grid.store.load();
    		}
    	}
	}],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor:{xtype:'hidden'}
	},{
		header:'主题', dataIndex:'topic', editor: {maxLength: 100, xtype: 'hidden'}
	},{
		header:'命令号', dataIndex:'orderNum', editor:{ maxLength: 20, xtype: 'hidden'}
	},{
		header:'申请人Id', dataIndex:'proposerId', hidden:true, editor:{xtype: 'hidden'}
	},{
		header:'申请人', dataIndex:'proposerName', width: 60, editor:{xtype: 'hidden'}
	},{
		header:'审批人ID', dataIndex:'approverId', hidden: true, editor:{id: 'approverId', hiddenName: 'approverId', xtype: 'hidden'}
	},{
		header:'审批人', dataIndex:'approverName', width: 60, editor:{
			id:'OmEmployee_SelectWin', xtype: 'OmEmployee_SelectWin', hiddenName: 'approverName', valueField : 'empname',
			returnField: [{widgetId: "approverId", propertyName: "empid"}]
		}
	},{
		header:'内容', dataIndex:'content',	width: 200, editor:{xtype: 'hidden'}
	},{
		header:'申请时间', dataIndex:'proposeTime', xtype:'datecolumn', format: 'Y-m-d h:m', editor:{ xtype:'my97date', format: 'Y-m-d H:i', hidden:true }
	},{
		header:'审批时间', dataIndex:'approveTime', xtype:'datecolumn', format: 'Y-m-d h:m', editor:{id:'approveTime', xtype:'my97date', format: 'Y-m-d H:i'}
	},{
		header:'审批意见', dataIndex:'approvalOpinion', editor:{xtype: 'textarea', maxLength: 200}
	},{
		header:'命令单状态', dataIndex:'status', editor:{xtype: 'hidden'}, 
		renderer: function(v, m, r) {
			if (STATUS_DSP === r.get('status')) {
				return '<span style="color:red">' + v + '</span>';
			}else {
				return '<span style="color:green">' + v + '</span>';	
			}
		}
	}], 
	   toEditFn: function(grid, rowIndex, e){
	   	var record = grid.store.getAt(rowIndex);
	   	if (record && STATUS_YSP == record.get('status')) {
	   		MyExt.Msg.alert('已审批的命令单不能再修改！');
			return;
	   	}
        //判断新增编辑窗体是否为null，如果为null则自动创建后显示
        if(this.saveWin == null)  this.createSaveWin();
        if(this.searchWin != null)  this.searchWin.hide();  
        var record = this.store.getAt(rowIndex);
        //是否显示编辑窗口，中止或继续编辑动作
        if(!this.beforeShowEditWin(record, rowIndex))     return;
        //显示编辑窗口
        if(this.isEdit){
        	this.saveWin.setTitle(i18n.common.button.edit);
	        this.saveWin.show();
	        this.saveForm.getForm().reset();
	        this.saveForm.getForm().loadRecord(record);
        } else {   //显示查看窗口
        	this.saveWin.setTitle(i18n.common.button.view);
        	this.saveWin.show();
	        this.saveForm.getForm().reset();
	        this.saveForm.getForm().loadRecord(record);
	        this.saveWin.buttons[0].setVisible(false);
	        this.disableAllColumns();
        }        
        //显示编辑窗口后触发函数（可能需要对某些特殊控件赋值等操作）
        this.afterShowEditWin(record, rowIndex);        
   	 }, 
   	 // 回显人员
   	 afterShowEditWin: function(record, rowIndex){
   	 	Ext.getCmp('approveTime').setValue(new Date());
   	 	Ext.getCmp('OmEmployee_SelectWin').setDisplayValue(record.get('approverName'), record.get('approverName'));
   	 }
	}),
	
	ScheduleProposeAndApprove.grid.store.on('beforeload', function() {
		var whereList = []; 
		var searchParam = ScheduleProposeAndApprove.searchParams;
		searchParam.topic = Ext.getCmp('topic').getValue();
		searchParam.approverId = empId;
		var whereList = [];
		for (prop in searchParam) {
			whereList.push({propName : prop, propValue : searchParam[prop], compare:8});
		}
		
  	    //设置查询条件
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	})

  	// 页面自适应布局
ScheduleProposeAndApprove.viewport = new Ext.Viewport({
		layout: 'border',
		items:[{
			// 结果显示列表区域
			region: 'center', 
			layout: "fit",
			bodyStyle:'padding-left:0px;', 
            bodyBorder: true,
			items: [ScheduleProposeAndApprove.grid]
		}]
	});
})