/**
 * 机车检修流水线 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('RepairLine');                       //定义命名空间
RepairLine.idx = '';                               //主键全局变量
RepairLine.repairLineName = '';						//流水线名称全局变量
RepairLine.searchParams = {};					   //全局查询参数集
RepairLine.workStationIDX = ""                   //定义全局工位主键
RepairLine.buildUpType = "" ;    				  //定义全局查询条件（质量记录单类型）
//根据业务编码生成规则生成流水号编码
RepairLine.getRepairLineCode = function(){
	var url = ctx + "/codeRuleConfig!getConfigRule.action";
	Ext.Ajax.request({
                url: url,
                params: {ruleFunction: "JXGC_REPAIR_LINE_REPAIR_LINE_CODE"},
                success: function(response, options){
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.errMsg == null) {
                        Ext.getCmp("repairLineCode_Id").setValue(result.rule);
                    }
                },
                failure: function(response, options){
                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
                }
            });
}
/**
 * 判断选中的记录是否可以执行启用、作废、删除等操作，返回提示信息
 * @param infoArray 遍历的数组
 * @param msg 显示的字符串说明
 * @param status 
 * @return 提示信息字符串
 */
RepairLine.alertOperate = function(infoArray,msg,status){
    if(infoArray == null || infoArray.length <= 0)  return msg;
    var info = "";
    var msgInfo = "" ;
    var operInfo = "" ; //启用消息
    var invalidInfo = "" ; //作废消息
    var titleInfo = "";
    if(status=='del'){
        msgInfo = "该操作将不能恢复，是否继续【删除】？";
        operInfo = "不能删除";
        invalidInfo = "不能删除";
        titleInfo = '只能删除状态为【新增】的记录！';
    }
    if(status=='start'){
        msgInfo = "确定【启用】选择的项？";
        operInfo = "";
        invalidInfo = "不能启用";
        titleInfo = '只能启用状态为【新增】的记录！';
    }
    if(status=='invalid'){
        msgInfo = "确定【作废】选择的项？";
        operInfo = "不能作废";
        invalidInfo = "";
        titleInfo = '只能作废状态为【启用】的记录！';
    }
    
    if(infoArray instanceof Array){
        for(var i = 0; i < infoArray.length; i++){
            if(infoArray[ i ].get("status") == status_new){
                info += (i + 1) + ". 【" + infoArray[ i ].get("repairLineCode") + "】为新增"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == status_use){
                info += (i + 1) + ". 【" + infoArray[ i ].get("repairLineCode") + "】已经启用"+operInfo+"！</br>";
                msgInfo = msg;
            }
            if(infoArray[ i ].get("status") == status_nullify){
                info += (i + 1) + ". 【" + infoArray[ i ].get("repairLineCode") + "】已经作废"+invalidInfo+"！</br>";
                msgInfo = msg;
            }
        }
    } else {
        info = infoArray;
    }
    return   titleInfo + '</br>' + info + '</br>' + msgInfo;
}
//更新流水线记录业务状态，启用或作废
RepairLine.updateStatus = function(validStatus, _grid, _operate){
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(_grid)) return;
    
    var data = _grid.selModel.getSelections();
    var ids = new Array();
    var flag = new Array(); //标记选择的项目
    for (var i = 0; i < data.length; i++){
        if(data[i].get('status') == validStatus){
            ids.push(data[ i ].get("idx"));
        }else{
            flag.push(data[i]);
        }
    }
    //提示信息，请求参数
    var action, msgOnly, url, params;
    switch(_operate){
    case 'del':
        action = '删除';
        msgOnly = '只能【删除】新增状态的记录！';
        url = RepairLine.grid.deleteURL;
        params = {ids: ids};
        break;
    case 'start':
        action = '启用';
        msgOnly = '只能【启用】新增状态的记录！';
        url = ctx + '/repairLine!updateStatus.action';
        params = {ids: ids, status: status_use};
        break;
    case 'invalid':
        action = '作废';
        msgOnly = '只能【作废】启用状态的记录！';  
        url = ctx + '/repairLine!updateStatus.action';
        params = {ids: ids, status: status_nullify};
        break;
    }
    if(ids.length <= 0){
        MyExt.Msg.alert(msgOnly);
        return;
    }
    //弹出信息确认框，根据用户确认后执行操作
    Ext.Msg.confirm('提示',RepairLine.alertOperate(flag,'是否执行' + action + '操作，该操作将不能恢复！',_operate), function(btn){   
        if(btn != 'yes')    return;
        var cfg = Ext.apply($yd.cfgAjaxRequest(), {
            url: url, params: params,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {
		            //操作成功            
		            alertSuccess();
		            _grid.store.reload(); 
		            _grid.afterDeleteFn();
		        } else {
		            //操作失败
		            alertFail(result.errMsg);
		        }                
            }            
        });
        Ext.Ajax.request(cfg);
    });
}
//删除流水线函数，点击删除按钮触发的函数
RepairLine.deleteFn = function(){
    if(this.saveWin)    this.saveWin.hide();
    if(this.searchWin)  this.searchWin.hide();        
    //未选择记录，直接返回
    if(!$yd.isSelectedRecord(this)) return;
    //执行删除前触发函数，根据返回结果觉得是否执行删除动作
    if(!this.beforeDeleteFn()) return;
    RepairLine.updateStatus(status_new, this, 'del');      
}
RepairLine.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/repairLine!findPageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/repairLine!saveRepairLine.action',             //保存数据的请求URL
    deleteURL: ctx + '/repairLine!logicDelete.action',            //删除数据的请求URL
    searchFormColNum: 2,
    saveFormColNum: 2,
    tbar: ['search','add',{
	    	text:"启用", iconCls:"acceptIcon", handler:function(){
	        RepairLine.updateStatus(status_new, RepairLine.grid, 'start');
    	}
    },{
        text:"作废", iconCls:"dustbinIcon", handler:function(){
            RepairLine.updateStatus(status_use, RepairLine.grid, 'invalid');
        }
    },'delete','refresh',{
    	xtype:'label', text: '状态：'
    },{   
	        xtype:'checkbox', name:'status', id:'status_new', boxLabel:'新增&nbsp;&nbsp;&nbsp;&nbsp;', inputValue:status_new, checked:true,
	        handler:function(){
            	RepairLine.checkQuery(status_new);
    		}
	    },{   
	        xtype:'checkbox', name:'status', id:'status_use', boxLabel:'启用&nbsp;&nbsp;&nbsp;&nbsp;', inputValue:status_use, checked:true,
	        handler:function(){
            	RepairLine.checkQuery(status_use);
    		}
	    },{   
	        xtype:'checkbox', name:'status', id:'status_nullify', boxLabel:'作废', inputValue:status_nullify , 
	        handler:function(){
            	RepairLine.checkQuery(status_nullify);
    		}
	    }
    ],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: {id: 'idx', xtype:'hidden' }
	},{
		header:'流水线编码', dataIndex:'repairLineCode', width: 80, 
		editor:{id: 'repairLineCode_Id', disabled: true, allowBlank:false, maxLength:50}, 
		searcher: {anchor:'98%'}
	},{
		header:'流水线名称', dataIndex:'repairLineName', 
		editor:{ allowBlank:false, maxLength:100 }, 
		searcher: {anchor:'98%'}
	},{
		header:'流水线类型', dataIndex:'repairLineType', hidden: true,
		editor:{
				xtype: 'hidden'
		},
		searcher:{}	
	},{
		header:'所属车间', dataIndex:'plantOrgId', hidden: true,
		editor:{ xtype:"hidden"}
		
	},{
		header:'所属车间', dataIndex:'plantOrgName', editor:{id:'plantOrgName_Id', xtype: 'hidden' }, width: 150,  hidden:true,
		searcher:{
			id: 'plantOrgId_searchId', xtype: 'OmOrganizationCustom_comboTree', fieldLabel: '所属车间',
			hiddenName: 'plantOrgId',
			selectNodeModel: "exceptRoot",
			queryHql:"[degree]plant"
		}
	},{
		header:'所属车间序列', dataIndex:'plantOrgSeq', hidden: true, editor:{ id:'plantOrgSeq_Id', xtype: 'hidden' }
	},{
		header:'所属股道编码', dataIndex:'trackCode', hidden: true, editor:{ xtype:'hidden'}
	},{
		header:'所属股道', dataIndex:'trackName', editor:{  id:'trackName_Id', maxLength:100 },searcher:{disabled: true}, width: 80
	},{
		header:'状态', dataIndex:'status', 
		renderer : function(v){if(v == status_new)return "新增";else if(v == status_use) return "启用";else return "作废";},
		editor:{ xtype:'hidden', value: status_new },
		searcher:{ disabled: true }			
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea', maxLength:1000 }, width: 180, 
		searcher:{disabled: true}
	}],
	deleteButtonFn: RepairLine.deleteFn,
	/**
     * 进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
     * 设置【工位】tab禁用，清空控件显示值
     * 该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
	beforeShowSaveWin: function(){
		this.enableAllColumns();
		this.saveForm.buttons[0].setVisible(true);
	    this.saveForm.buttons[1].setVisible(true);
		Ext.getCmp("repairLine_Tabs").getItem("workStation_tabId").disable();
		Ext.getCmp("repairLine_Tabs").activate("repairLine_tabId");
		RepairLine.getRepairLineCode();		
		return true;
	},
	
	/**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * 
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
	afterSaveSuccessFn: function(result, response, options){
        RepairLine.grid.store.load();
        alertSuccess();
        var result = Ext.util.JSON.decode(response.responseText);  
        //获取保存成功后的“流水线”主键并保存到全局变量
	    RepairLine.idx = result.entity.idx;
	    RepairLine.repairLineName = result.entity.repairLineName;
	    RepairLine.buildUpType = result.entity.repairLineType ; //流水线类型
	    RepairLine.grid.saveForm.getForm().getValues().idx = result.entity.idx;
	    Ext.getCmp("idx").setValue(result.entity.idx);
	    this.saveWin.setTitle('编辑');	
	    this.enableAllColumns();
	    //设置【工位】tab可用
    	Ext.getCmp("repairLine_Tabs").getItem("workStation_tabId").enable();
    	
    	//重新加载工位数据表格
    	WorkStation.grid.store.load();
    	//重新加载工位树
    	WorkStation.tree.root.reload();
    	
	    //状态为启用状态
	    if(result.entity.status == status_use){
	    	this.disableAllColumns();
	    	this.enableColumns(['idx','repairLineName','trackName','remarks']);
	    }
    },
    /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
	afterShowEditWin:function(record, rowIndex){
		//设置“流水线主键”全局变量
		RepairLine.idx = record.get("idx");	
		RepairLine.buildUpType = record.get("repairLineType") ; //流水线类型
		RepairLine.repairLineName = record.get("repairLineName");
		
    	//设置【工位】tab可用
    	Ext.getCmp("repairLine_Tabs").getItem("workStation_tabId").enable();
    	Ext.getCmp("repairLine_Tabs").activate("repairLine_tabId");
    	WorkStation.grid.getTopToolbar().enable();
    	WorkStation.grid.on('rowdblclick', WorkStation.grid.toEditFn, WorkStation.grid);
    	
    	//重新加载工位数据表格
    	WorkStation.grid.store.load();
    	//重新加载工位树
    	WorkStation.tree.root.reload();
    	
		//状态为启用则不可编辑流水线表单并显示【工位】
	    if(record.get("status") == status_use){
	    	this.disableAllColumns();
	    	this.enableColumns(['idx','repairLineName','trackName','remarks']);
	    	this.saveForm.buttons[1].setVisible(false);
	    	
	    }
	    //状态为新增则可编辑流水线表单、禁用【工位】
	    else if(record.get("status") == status_new){
	    	this.enableAllColumns();
			this.saveForm.buttons[0].setVisible(true);
		    this.saveForm.buttons[1].setVisible(true);		    
	    }
	    //状态为作废则不可编辑流水线表单、可查看【工位】菜单
	    else if(record.get("status") == status_nullify){
	    	this.disableAllColumns();
			this.saveForm.buttons[0].setVisible(false);
		    this.saveForm.buttons[1].setVisible(false);
		    Ext.getCmp("repairLine_Tabs").getItem("workStation_tabId").enable();
			Ext.getCmp("repairLine_Tabs").activate("repairLine_tabId");
			WorkStation.grid.getTopToolbar().disable();
			WorkStation.grid.un('rowdblclick', WorkStation.grid.toEditFn, WorkStation.grid);
			WorkStation.grid.store.load();
	    }
		return true;
	},
	/**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){
    	RepairLine.searchParams = {};	
    	var type = "";   
    	delete searchParam["status"];
    	//将查询表单数据设置到全局查询参数集
    	for(prop in searchParam){
	    	RepairLine.searchParams[prop] = searchParam[prop];
		}
		
		//将全局查询参数集设置到baseParams，解决分页控件刷新
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(RepairLine.searchParams);
    	//访问后台，根据查询参数刷新列表
        this.store.load({
            params: {                    
                    entityJson: Ext.util.JSON.encode(searchParam),
                    status: RepairLine.status
                }       
        });	
    },
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){
    	this.enableAllColumns();
    },
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){
    	this.disableAllColumns();
	    this.enableColumns(['idx','repairLineName','trackName','remarks']);
    },
    beforeSaveFn: function(data){
    	data.repairLineType = type;//机车检修流水线类型
    	return true; 
    }
});

//创建表单
RepairLine.grid.createSaveForm();
//创建完成表单后给表单添加操作按钮
RepairLine.grid.saveForm.buttonAlign = "center" ;
RepairLine.grid.saveForm.addButton({
	text:"保存", iconCls:"saveIcon",
	handler: function(){
		RepairLine.grid.saveFn();
	}
});
RepairLine.grid.saveForm.addButton({
	text:"启用", iconCls:"acceptIcon",handler:function(){
		 //弹出信息确认框，根据用户确认后执行操作
	    Ext.Msg.confirm('提示','是否执行启用操作，该操作将不能恢复！', function(btn){   
	        if(btn != 'yes')    return;
	        //表单验证是否通过
	        var form = RepairLine.grid.saveForm.getForm(); 
	        if (!form.isValid()) return;
	        
	        //获取表单数据前触发函数
	        RepairLine.grid.beforeGetFormData();
	        var data = form.getValues();
	        //获取表单数据后触发函数
	        RepairLine.grid.afterGetFormData();
	        data.status = status_use;
	        //调用保存前触发函数，如果返回fasle将不保存记录
	        if(!RepairLine.grid.beforeSaveFn(data)) return;
	        
	        if(self.loadMask)   self.loadMask.show();
	        var cfg = {
	            url: RepairLine.grid.saveURL, jsonData: data,
	            success: function(response, options){
	                if(self.loadMask)   self.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null) {
	                    RepairLine.grid.afterSaveSuccessFn(result, response, options);
	                } else {
	                    RepairLine.grid.afterSaveFailFn(result, response, options);
	                }
	            }
	        };
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		});
	}
});
RepairLine.grid.saveForm.addButton({
	text:"关闭", iconCls:"closeIcon",handler:function(){
		RepairLine.grid.saveWin.hide();
	}
});
RepairLine.grid.saveForm.style = "padding:30px",
//重构新增编辑窗口
RepairLine.grid.saveWin  = new Ext.Window({
    title: "新增", maximizable: true, width: 700, height: 350, layout: "fit", plain: true, closeAction: "hide", modal: true,
    items: [{
        xtype: "tabpanel", id:"repairLine_Tabs",  border: false, activeTab:0, enableTabScroll:true, items:[{
            id:"repairLine_tabId", title: "流水线", layout: "fit", frame: true, border: false, items: RepairLine.grid.saveForm
        },{
            id:"workStation_tabId", title: "工位", layout: "border", border: false, 
            defaults:{
            	layout: 'fit'
            },
            items: [{
            	region:'west', width: 200, items: WorkStation.tree
            }, {
            	region:'center', items: WorkStation.grid
            }]
        }]
    }]
});
//设置查询窗口为模态窗口
RepairLine.grid.createSearchWin();
RepairLine.grid.searchWin.modal = true;
RepairLine.status =status_new + "," + status_use; //默认查看新增启用状态的
//状态多选按钮
RepairLine.checkQuery = function(status){
	RepairLine.status = "-1";
	if(Ext.getCmp("status_new").checked){
		RepairLine.status = RepairLine.status + "," + status_new;
	} 
	if(Ext.getCmp("status_use").checked){
		RepairLine.status = RepairLine.status + "," + status_use;
	} 
	if(Ext.getCmp("status_nullify").checked){
		RepairLine.status = RepairLine.status + "," + status_nullify;
	} 
	RepairLine.grid.store.load({
		params: {
			entityJson: Ext.util.JSON.encode(RepairLine.searchParams)
		}
	});
}
//store载入前查询，为了分页查询时不至于出差错
RepairLine.grid.store.on("beforeload", function(){
	this.baseParams.status = RepairLine.status;  
	this.baseParams.repairLineType = type;
	this.baseParams.entityJson = Ext.util.JSON.encode(RepairLine.searchParams);
});
//清空查询参数集并刷新列表
RepairLine.clearSearchParams = function(_grid){
	_grid.store.load({
        	params:{
        		 entityJson: Ext.util.JSON.encode({}),
                 status: "",
                 repairLineType: ""
        	}    
        });
        _grid.store.baseParams.entityJson = Ext.util.JSON.encode({});
        _grid.store.baseParams.status = "";
        _grid.store.baseParams.repairLineType = "";
        RepairLine.searchParams = {};
};
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:RepairLine.grid });
});