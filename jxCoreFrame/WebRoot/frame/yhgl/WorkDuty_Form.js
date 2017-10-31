Ext.onReady(function() {
	/*******************************************************************************/
	/*                                 本职务信息表单                                     */
    /*******************************************************************************/
	Ext.namespace('DutyForm');
	
	DutyForm.labelWidth = 90;
	DutyForm.anchor = '85%';
	DutyForm.fieldWidth = 160;
	
	DutyForm.currentInfoForm = new Ext.form.FormPanel({
		layout:"form", border:true, style:"padding:10px" , baseCls: "x-plain", 
		labelWidth: DutyForm.labelWidth, align:'center',  
		defaultType:'textfield',defaults:{anchor:"100%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain", 
			items:[{
				align:'center',	defaultType:'textfield', border:false,  layout:"form",
				labelWidth: DutyForm.labelWidth,	columnWidth:0.5, baseCls: "x-plain", 
				items:[
					//隐藏部分
					{ id:'_dutyid_0', fieldLabel:'职务ID',name:'dutyid', disabled:false,hidden:true},
					{ id:'_parentduty_0', fieldLabel:'父职务ID',name:'parentduty', disabled:false,hidden:true},
					{ id:'_dutylevel_0', fieldLabel:'职务层次',name:'dutylevel', disabled:false,hidden:true},
					{ id:'_dutyseq_0', fieldLabel:'职务序列号',name:'dutyseq', disabled:false,hidden:true},
					{ id:'_isleaf_0', fieldLabel:'是否叶子节点',name:'isleaf', disabled:false,hidden:true},
					{ id:'_subcount_0', fieldLabel:'子节点数',name:'subcount', disabled:false,hidden:true},
					//显示部分
					{ id:'_parentdutyname_0', fieldLabel:'上级职务',name:'parentdutyname', disabled:true,hidden:false,width:DutyForm.fieldWidth},
					{ id:'_dutycode_0', fieldLabel:'职务代码',name:'dutycode', vtype:'alphanum2', maxLength:20,allowBlank:false,width:DutyForm.fieldWidth},
					{ id:'_remark_0', fieldLabel:'备注',name:'remark', xtype:'textarea', maxLength:256,allowBlank:true,width:DutyForm.fieldWidth}
				]
			},{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain", layout:"form",
				labelWidth: DutyForm.labelWidth,	columnWidth:0.5,
				items:[
					{ id:'_dutytype_0', fieldLabel: '职务类别', name:'dutytype', xtype: 'EosDictEntry_combo', width:DutyForm.fieldWidth,
					  hiddenName: 'dutytype', displayField: 'dictname', valueField: 'dictid', status:'1', dicttypeid:'ABF_DUTYTYPE'},
					{ id:'_dutyname_0', fieldLabel:'职务名称',name:'dutyname', vtype:'validChar', maxLength:15,allowBlank:false,width:DutyForm.fieldWidth}
				]
			}]
		}]
	});
	
	/**
	 * 读取当前所选职务节点的详细信息，并填充表单
	 */
	DutyForm.findCurrentDutyInfo = function (){
		if(DutyTab.DutyNodeId!=null){
			Ext.Ajax.request({
				url: ctx + '/workDuty!findCurrentDutyInfo.action',//数据源路径
				params: {'nodeid' : DutyTab.DutyNodeId}, //查询职务id匹配的职务
				success: function(response, options){
				       var result = Ext.util.JSON.decode(response.responseText);
				       if (result.errMsg == null) {
			           		if(result!=null){
			           			DutyForm.currentInfoForm.getForm().findField("_dutyid_0").setValue(result.dutyid);
			           			DutyForm.currentInfoForm.getForm().findField("_parentduty_0").setValue(result.parentduty);
			           			DutyForm.currentInfoForm.getForm().findField("_dutylevel_0").setValue(result.dutylevel);
			           			DutyForm.currentInfoForm.getForm().findField("_dutyseq_0").setValue(result.dutyseq);
			           			DutyForm.currentInfoForm.getForm().findField("_isleaf_0").setValue(result.isleaf);
			           			DutyForm.currentInfoForm.getForm().findField("_subcount_0").setValue(result.subcount);
			           			DutyForm.currentInfoForm.getForm().findField("_parentdutyname_0").setValue(result.parentdutyname);
			           			DutyForm.currentInfoForm.getForm().findField("_dutycode_0").setValue(result.dutycode);
			           			DutyForm.currentInfoForm.getForm().findField("_remark_0").setValue(result.remark);
			           			DutyForm.currentInfoForm.getForm().findField("_dutytype_0").setValue(result.dutytype);
			           			DutyForm.currentInfoForm.getForm().findField("_dutyname_0").setValue(result.dutyname);
			           		}
				       } else {
				              alertFail(result.errMsg);
				       }
				},
				failure: function(response, options){
				       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	};
	
	DutyForm.panel = new Ext.Panel({
		title: "编辑", layout: "fit", plain:true, 
		closeAction: "hide", modal: true, align:'center',
		items : [{
			xtype: 'panel',	border:false,  layout:'column',	align:'center',  buttonAlign: "center", baseCls: "x-plain", buttonAlign: "center",
			items: DutyForm.currentInfoForm,
			buttons: [{
            	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            		var form = DutyForm.currentInfoForm.getForm();
            		if (!form.isValid()) return;
            		var data = form.getValues();
			        var cfg = {
			            scope: this, url: ctx+"/workDuty!saveOrUpdate.action", jsonData: data,
			            success: function(response, options){
//			                if(self.loadMask)   self.loadMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
			                    var selectNode = dutytree.tree.getSelectionModel().getSelectedNode(); //获取当前选中节点
			                    selectNode.setText(data.dutyname); //更改节点text显示内容
			                } else {
			                    alertFail(result.errMsg);
			                }
			            }
			        };
			        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
            	}
	        }]
		}]
	});
});