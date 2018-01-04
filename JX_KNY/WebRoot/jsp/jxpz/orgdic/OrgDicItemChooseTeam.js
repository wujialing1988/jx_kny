/**
 * 施修班组选择
 */
Ext.onReady(function(){
	Ext.namespace('TeamSelect');                       //定义命名空间
	
	TeamSelect.orgname;
	//已选择人员Grid
	TeamSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + "/omOrganizationSelect!pageQuery.action",                 //装载列表数据的请求URL    
	    tbar : [i18n.OrgDicItemChooseTeam.teamName,{	            
            xtype:"textfield",								                
            name : "parts",
	        width: 240,
            id:"partsId"
		},{
			text : i18n.OrgDicItemChooseTeam.search,
			iconCls : "searchIcon",
			handler : function(){
				TeamSelect.orgname = Ext.getCmp("partsId").getValue();				
				TeamSelect.grid.getStore().load();
				
			},
			title : i18n.OrgDicItemChooseTeam.searchBy,
			scope : this
		},{
			text : i18n.OrgDicItemChooseTeam.reset,
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
			header:i18n.OrgDicItemChooseTeam.teamNo, dataIndex:'orgcode', editor:{ maxLength:18 }
		},{
			header:i18n.OrgDicItemChooseTeam.teamName, dataIndex:'orgname', editor:{ maxLength:18 }
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
		title:i18n.OrgDicItemChooseTeam.comDepartChoose, width:600, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[ TeamSelect.grid ], modal:true,
    	buttons: [{
			text :i18n.OrgDicItemChooseTeam.comfirm,iconCls : "saveIcon", handler: function(){
				TeamSelect.submit(); 
			}
		},{
	        text: i18n.OrgDicItemChooseTeam.close, iconCls: "closeIcon", scope: this, handler: function(){ TeamSelect.selectWin.hide(); }
		}]
	});
	TeamSelect.submit = function(){
		var org = TeamSelect.grid.selModel.getSelections();
		if(org.length == 0){
			MyExt.Msg.alert("i18n.OrgDicItemChooseTeam.noChoose!")
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
				MyExt.Msg.alert(i18n.OrgDicItemChooseTeam.error+"\n" + response.status + "\n" + response.responseText);
			}
		});
	}
});