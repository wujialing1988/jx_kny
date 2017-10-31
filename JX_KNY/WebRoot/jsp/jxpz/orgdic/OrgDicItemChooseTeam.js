/**
 * 施修班组选择
 */
Ext.onReady(function(){
	Ext.namespace('TeamSelect');                       //定义命名空间
	
	TeamSelect.orgname;
	//已选择人员Grid
	TeamSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/omOrganizationSelect!pageQuery.action",                 //装载列表数据的请求URL    
	    tbar : ['班组名称',{	            
            xtype:"textfield",								                
            name : "parts",
	        width: 240,
            id:"partsId"
		},{
			text : "搜索",
			iconCls : "searchIcon",
			handler : function(){
				TeamSelect.orgname = Ext.getCmp("partsId").getValue();				
				TeamSelect.grid.getStore().load();
				
			},
			title : "按输入框条件查询",
			scope : this
		},{
			text : "重置",
			iconCls : "resetIcon",
			handler : function(){
				delete TeamSelect.orgname;
				TeamSelect.grid.getStore().load();
				
				Ext.getCmp("partsId").setValue("");
			},
			scope : this
		}], singleSelect: false,		
		fields: [{
			header:'orgid', dataIndex:'orgid', hidden:true, editor:{  maxLength:2 }
		},{
			header:'orgseq', dataIndex:'orgseq', hidden:true, editor:{  maxLength:2 }
		},{
			header:'班组编号', dataIndex:'orgcode', editor:{ maxLength:18 }
		},{
			header:'班组名称', dataIndex:'orgname', editor:{ maxLength:18 }
		}]
	});
	
	
	
	//移除事件
	TeamSelect.grid.un('rowdblclick',TeamSelect.grid.toEditFn,TeamSelect.grid);
	TeamSelect.grid.store.on("beforeload", function(){
		var sqlStr = " orgid not in (select ORG_ID from JXPZ_ORG_DIC_ITEM where DICT_TYPE_ID='"+OrgDicItem.dictTypeId+"')";
		var whereList = [
		 {sql: sqlStr, compare: Condition.SQL} //排除已选择过的数据
		]
		var sp = {};
		if(TeamSelect.orgname){
			whereList.push({
							propName : 'orgname',
							propValue : TeamSelect.orgname,
							compare : Condition.EQ
						});
		}
		whereList.push({
							propName : 'orgdegree',
							propValue : orgdegree,
							compare : Condition.EQ
						});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	//定义选择窗口
	TeamSelect.selectWin = new Ext.Window({
		title:"常用部门选择", width:600, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ TeamSelect.grid ], modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				TeamSelect.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ TeamSelect.selectWin.hide(); }
		}]
	});
	TeamSelect.submit = function(){
		var org = TeamSelect.grid.selModel.getSelections();
		if(org.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		var datas = new Array();
		for(var i=0;i<org.length;i++){
			var data = {} ;
//			data.id.dictTypeId = OrgDicItem.dictTypeId ;
			data.orgid = org[i].get("orgid");
			data.orgSeq = org[i].get("orgseq");
			data.orgName = org[i].get("orgname");
			datas.push(data);
		}
		TeamSelect.grid.loadMask.show();
		Ext.Ajax.request({
			url: ctx + "/orgDicItem!saveItems.action",
			jsonData: datas,
			params : {dictTypeId : OrgDicItem.dictTypeId},
			success: function(response, options){
				TeamSelect.grid.loadMask.hide();
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.success){
					alertSuccess();
					OrgDicItem.grid.store.reload();
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
	}
});