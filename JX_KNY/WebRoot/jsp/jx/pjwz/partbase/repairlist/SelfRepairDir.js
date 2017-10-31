
Ext.onReady(function(){
	
	Ext.namespace("Dir");
	
	/** **************** 定义全局变量开始 **************** */
	Dir.labelWidth = 100;
	Dir.fieldWidth = 140;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义查询表单开始 **************** */
	Dir.searchForm = new Ext.form.FormPanel({
		labelWidth: Dir.labelWidth,
		padding: 10,
		layout: 'column', defaults: {
			columnWidth: .33,
			layout: 'form',
			defaults: { width: Dir.fieldWidth, xtype: 'textfield' }
		},
		items: [{
			items: [{
				fieldLabel: '配件名称', name: 'partsName'
			}]
		}, {
			items: [{
				fieldLabel: '规格型号', name: 'specificationModel'
			}]
		}, {
			items: [{
				fieldLabel: '检修班组', name: 'orgname'
			}]
		}],
		
		buttonAlign: 'center',
		buttons: [{
			text: '查询', iconCls: 'searchIcon', handler: function() {
				Dir.grid.store.load();
			}
		}, {
			text: '重置', iconCls: 'resetIcon', handler: function() {
				this.findParentByType('form').getForm().reset();
				Dir.grid.store.load();
			}
		}]
	});
	/** **************** 定义查询表单结束 **************** */
	
	Dir.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/partsRepairList!findPageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRepairList!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRepairList!logicDelete.action',            //删除数据的请求URL
	    saveFormColNum:2, searchFormColNum:2,
	    tbar:[/*'search',*/{
	    	text: '新增',
	    	iconCls: 'addIcon',
	    	handler: function(){
	    		PartsTypeSelect.selectWin.show();
	    	}
	    },'delete','refresh','->',{
	    	text: '设置施修班组',
	    	iconCls: 'editIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(Dir.grid)) return;
	    		TeamSelect.selectWin.show();
	    	}
	    },{
	    	text: '设置是否合格验收',
	    	iconCls: 'acceptIcon',
	    	handler: function(){
	    		if(!$yd.isSelectedRecord(Dir.grid)) return;
	    		Dir.isHgys.show();
	    	}
	    }],
	    fields: [{
			header:'IDX', dataIndex:'idx', editor: { xtype:"hidden"}, hidden:true
		},{
			header:'配件名称', dataIndex:'partsName', editor: { allowBlank:false, xtype:"textfield", readOnly:true, style:"color:gray" }, searcher: {}
		},{
			header:'规格型号', dataIndex:'partsTypeIDX', 
			editor: {}, hidden:true
		},{
			header:'规格型号', dataIndex:'specificationModel', editor: { xtype: "hidden" }, searcher:{}
		},{
			header:'检修班组', dataIndex:'repairOrgID', hidden:true,
			editor: { 
				id: 'repairOrg_Id',
				xtype: 'OmOrganizationCustom_comboTree',
				hiddenName: 'repairOrgID',	
				orgid: systemOrgid,			
				orgname: systemOrgname,
				selectNodeModel:"exceptRoot",
				fullNameDegree:'false',
				returnField:[{widgetId:"orgname",propertyName:"orgname"}],
				allowBlank:false
			}
		},{
			header:'检修班组', dataIndex:'orgname', editor: { xtype: "hidden" }, searcher:{}
		},{
			header:'是否合格验收', dataIndex:'isHgys',editor: { xtype: "hidden" }
		}],
		afterShowEditWin: function(r, rowIndex){
			Ext.getCmp("partsAccount_IDX").setDisplayValue(r.get("partsTypeIDX"), r.get("specificationModel"));
			Ext.getCmp("repairOrg_Id").setDisplayValue(r.get("repairOrgID"), r.get("orgname"));
		},
		afterShowSaveWin: function(){
			Ext.getCmp("partsAccount_IDX").clearValue();
			Ext.getCmp("partsAccount_IDX").clearInvalid();
			Ext.getCmp("repairOrg_Id").clearValue();
			Ext.getCmp("repairOrg_Id").clearInvalid();
		},
		// 禁用编辑事件
		toEditFn: Ext.emptyFn
	});
	
	Dir.grid.store.on('beforeload', function(){
		var form = Dir.searchForm.getForm();
		var searchParams = MyJson.deleteBlankProp(form.getValues());
		this.baseParams.entityJson = Ext.encode(searchParams);
		
	});
	PartsTypeSelect.submit = function(){
		
		var idxs = $yd.getSelectedIdx(PartsTypeSelect.grid);
		if(idxs.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		PartsTypeSelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsRepairList!newList.action",
			params: {idxs: idxs + ""},
			success: function(response, options){
	            PartsTypeSelect.grid.loadMask.hide();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if(result.success){
	            	alertSuccess();
	            	Dir.grid.store.reload();
	            	PartsTypeSelect.selectWin.hide();
	            }else{
	            	alertFail(result.errMsg);
	            }
			},
	        failure: function(response, options){
				PartsTypeSelect.grid.loadMask.hide();
	            MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	        }
		});
	}
	TeamSelect.submit = function(){
		var org = TeamSelect.grid.selModel.getSelections();
		if(org.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		var idxs = $yd.getSelectedIdx(Dir.grid);
		TeamSelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/partsRepairList!setRepairOrg.action",
			params: {idxs: idxs + "", org: org[0].get("orgid")},
			success: function(response, options){
				TeamSelect.grid.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					Dir.grid.store.reload();
					TeamSelect.selectWin.hide();
				}else{
					alertFail(result.errMsg);
				}
			},
			failure: function(response, options){
				TeamSelect.grid.loadMask.hide();
				MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			}
		});
	};
	
	//设置是否合格验收
	Dir.isHgys = new Ext.Window({
		title:"设置是否合格验收", width:250, height:120, plain:true, closeAction:"hide", buttonAlign:'center',padding:15,
    	maximizable:false,  modal:true,
    	items:[{
        	xtype: 'radiogroup',
        	fieldLabel: "是否合格验收",
        	id: 'isHgys',
        	style: "padding-left:30",
            items: [
                { boxLabel: '是',inputValue: '是', name: 'isHgys', checked: true
                },
                { boxLabel: '否',inputValue: '否', name: 'isHgys'
                }  
            ]
		}],
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				var ids = $yd.getSelectedIdx(Dir.grid);
				var hgysCode = Ext.getCmp("isHgys").getValue().inputValue;
				Ext.Ajax.request({
					url: ctx + "/partsRepairList!updateisHgys.action",
					params: {ids: ids, hgysCode:hgysCode},
					success: function(response, options){
				        var result = Ext.util.JSON.decode(response.responseText);
				        if (result.errMsg == null) {
							alertSuccess();
							Dir.isHgys.hide();
							Dir.grid.store.reload();
						}else{
							alertFail(result.errMsg);
							Dir.isHgys.hide();
						}
					},
					failure: function(response, options){
						MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
					}
				});
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ Dir.isHgys.hide(); }
		}]
	});	
	
	var viewport = new Ext.Viewport({
		layout:'border', 
		items: [{
			title: '查询',
			region: 'north', height: 120,
			frame: true, collapsible: true,
			items: Dir.searchForm
		}, {
			region: 'center', 
			layout: 'fit',
			items: Dir.grid 
		}]
	
	});
});