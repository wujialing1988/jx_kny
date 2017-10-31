

Ext.onReady(function() {
	Ext.namespace('EquipForWPSelect');
	
	/** ************* 定义全局变量开始 ************* */
	EquipForWPSelect.searchParams = {};
	EquipForWPSelect.wPIDX = "###";
	EquipForWPSelect.labelWidth = 80,
	EquipForWPSelect.fieldWidth = 120,
	/** ************* 定义全局变量结束 ************* */
	
	/** ************* 定义全局函数开始 ************* */
	// 【确定】按钮触发的函数处理
	EquipForWPSelect.addFn = function() {
		if (!$yd.isSelectedRecord(EquipForWPSelect.grid)) {
			return;
		}
		var ids = $yd.getSelectedIdx(EquipForWPSelect.grid);
		
		var datas = [];
		
		for (var i = 0; i < ids.length; i++) {
			var wPNodeUnionEquipCard = {};
			wPNodeUnionEquipCard.wPIDX = EquipForWPSelect.wPIDX;
			wPNodeUnionEquipCard.equipCardIDX = ids[i];
			datas.push(wPNodeUnionEquipCard);
		}
    	// Ajax后台数据处理
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest, {
            url: ctx + '/wPNodeUnionEquipCard!save.action',
            jsonData: datas,
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                	// 重新加载 【候选表格】
                    EquipForWPSelect.grid.store.reload();
                	// 重新加载 【作业流程所用机务设备工单表格】
                    EquipForWP.grid.store.reload();
                } else {
                    alertFail(result.errMsg);
                }
            }
        }));
		
	}
	/** ************* 定义全局函数结束 ************* */
	
	/** ************* 定义查询表单开始 ************* */
	EquipForWPSelect.searchForm = new Ext.form.FormPanel({
		labelWidth: EquipForWPSelect.labelWidth,
		baseCls: "x-plain", border: false,
		style: 'padding: 15px',
		items:[{
			// 查询表单第1行
			baseCls: "x-plain",
			layout: 'column',
			items:[{													// 第1行第2列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '工单编码',
					maxLength:50,
					name: 'equipCardNo',
					xtype: 'textfield',
					width: EquipForWPSelect.fieldWidth
				}]
			},{												// 第1行第1列
				columnWidth: .33,
				layout: 'form',
				items: [{
					id:"deviceType_comb", editable:true,selectOnFocus:false,forceSelection:false,typeAhead:false,
    	    		 xtype: 'Base_combo',hiddenName: "deviceTypeName",fieldLabel:"设备类别",width: EquipForWPSelect.fieldWidth,
	 				 entity:"com.yunda.jxpz.equipinfo.entity.DeviceType",displayField:"deviceTypeName",valueField:"deviceTypeName",
	 				 fields:["deviceTypeCode","deviceTypeName"]		 
				}]
			},  {													// 第1行第3列
				columnWidth: .33,
				layout: 'form',
				items: [{
					fieldLabel: '工单描述',
					maxLength:500,
					name: 'equipCardDesc',
					xtype: 'textfield',
					width: EquipForWPSelect.fieldWidth
				}]
			}]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '查询', iconCls: 'searchIcon', handler: function() {
				// 重新加载表格
				EquipForWPSelect.grid.store.load();
			}
		}, {
			xtype: "button", text: '重置', iconCls: 'resetIcon', handler: function() {
				EquipForWPSelect.searchForm.getForm().reset();
				Ext.getCmp("deviceType_comb").clearValue();
				// 重新加载表格
				EquipForWPSelect.grid.store.load();
			}
		}]
	});
	/** ************* 定义查询表单结束 ************* */
	
	/** ************* 定义候选表格开始 ************* */
	EquipForWPSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/equipCard!pageQuery.action',                 //装载列表数据的请求URL
	    tbar: null,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'工单编码', dataIndex:'equipCardNo', editor:{  maxLength:30 }
		},{
			header:'设备类别', dataIndex:'deviceTypeName', editor:{  maxLength:50 }
		},{
			header:'工单描述', dataIndex:'equipCardDesc', editor:{  maxLength:500 }
		}],
		storeAutoLoad: false
	});
	EquipForWPSelect.grid.un('rowdblclick', EquipForWPSelect.grid.toEditFn, EquipForWPSelect.grid);
	//查询前添加过滤条件
	EquipForWPSelect.grid.store.on('beforeload' , function(){
		EquipForWPSelect.searchParams = EquipForWPSelect.searchForm.getForm().getValues();
	     var searchParam=EquipForWPSelect.searchParams;
	     searchParam=MyJson.deleteBlankProp(searchParam);
	     //排除已选择的机务设备工单
	     var sqlStr = " idx not in (select nvl(Equip_Card_IDX,'0') from PJJX_WP_Node_Union_Equip_Card where Record_Status=0 and WP_IDX ='"+EquipForWP.wPIDX+"')";
		 var whereList = [
		          		{sql: sqlStr, compare: Condition.SQL} //通过回段日期过滤
					]
	     for(prop in searchParam){
	     		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.EQ});
	     		continue;
	     }
	     this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
	});
	/** ************* 定义候选表格结束 ************* */
	
	EquipForWPSelect.win = new Ext.Window({
		title:"选择机务设备工单",
		width: 800,
		height: 500,
		modal: true,
		layout: 'border',
		closeAction: 'hide',
		items:[{
			// 查询表单区域
			frame:true, title:'查询',
			region: 'north',
			height: 120,
			border: true,
			collapsible: true,
			split: true,
			items: [EquipForWPSelect.searchForm]
		}, {
			// 页面主体表格
			region: 'center',
			layout: 'fit',
			items: [EquipForWPSelect.grid]
		}],
		buttonAlign: 'center',
		buttons: [{
			xtype: "button", text: '添加', iconCls: 'yesIcon', handler: EquipForWPSelect.addFn
		}, {
			xtype: "button", text: '关闭', iconCls: 'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});