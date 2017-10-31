/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('FaultTicketRoleDefineView');                       //定义命名空间

	/** ************** 定义全局变量开始 ************** */
	FaultTicketRoleDefineView.searchParams = {};
	FaultTicketRoleDefineView.faultTicketType = "###";
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义全局函数开始 ************** */
	// 【设置角色】函数处理
	FaultTicketRoleDefineView.configRoleFn = function() {
		// 隐藏【提票】表格的行编辑控件
		if (FaultTicketRuleDefine.grid.rowEditor) {
			FaultTicketRuleDefine.grid.rowEditor.slideHide(); 
		}
		if(Ext.isEmpty(FaultTicketRoleDefineView.faultTicketType) || "###" == FaultTicketRoleDefineView.faultTicketType)	     
		{
        	  MyExt.Msg.alert("请先选择一条提票过程！");
        	  return;
        }
		SysRoleView.faultTicketType = FaultTicketRoleDefineView.faultTicketType,
		SysRoleView.grid.store.load();
		SysRoleView.win.show();
	}
	
	// 选择角色之后执行确认的操作（生成一条记录）
	SysRoleView.submit = function(){	
        var sm = SysRoleView.grid.getSelectionModel();
        if (sm.getCount() < 1) {
            MyExt.Msg.alert("尚未选择一条记录！");
            return;
        }     
        var data = sm.getSelections();
        var datas = new Array();
        for (var i = 0; i < data.length; i++){
        	var data_v = {}; 
        	data_v.roleId = data[ i ].data.roleid;
        	data_v.roleName = data[ i ].data.rolename;
        	data_v.faultTicketType = FaultTicketRoleDefineView.faultTicketType;
            datas.push(data_v);
        }
        Ext.Ajax.request({
            url: ctx + "/faultTicketCheckRole!saveCheckRoles.action",
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    alertSuccess();
				    FaultTicketRoleDefineView.grid.store.load();
				    SysRoleView.win.hide();
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
        });    
	};	
	/** ************** 定义全局函数结束 ************** */
	
	/** ************** 定义提票角色表格开始 ************** */
	FaultTicketRoleDefineView.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/faultTicketCheckRole!pageQuery.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/faultTicketCheckRole!logicDelete.action',                 //删除列表数据的请求URL
	    storeAutoLoad: false,
	    title: '角色',
	    tbar:[{
	    	text:'设置确认角色', iconCls:'configIcon', handler: FaultTicketRoleDefineView.configRoleFn
    	},'delete'],
		fields: [{
			header:'关联主键', dataIndex:'idx', hidden: true
		},{
			header:'提票类型', dataIndex:'faultTicketType', hidden: true
		},{
			header:'角色名称', dataIndex:'roleName', width:120, editor:{  maxLength:25 }
		},{
			header:'角色id', dataIndex:'roleId', hidden: true		
		}], 
		beforeDeleteFn: function(){              
			if (FaultTicketRuleDefine.grid.rowEditor) {
				FaultTicketRuleDefine.grid.rowEditor.slideHide(); 
			}             
	        return true;
	    }
	});

	// 取消表格双击进行编辑的事件监听
	FaultTicketRoleDefineView.grid.un('rowdblclick', FaultTicketRoleDefineView.grid.toEditFn, FaultTicketRoleDefineView.grid);
	// 数据加载时的参数设置
	FaultTicketRoleDefineView.grid.store.on('beforeload', function(){
		var searchParams = FaultTicketRoleDefineView.searchParams;
		var whereList = [];
		for (prop in searchParams) {
			whereList.push({propName : prop, propValue : searchParams[prop], compare:Condition.LIKE});
		}
		whereList.push({propName :'faultTicketType', propValue :FaultTicketRoleDefineView.faultTicketType, compare:Condition.EQ,stringLike:false});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	/** ************** 定义提票角色表格结束 ************** */
	
});