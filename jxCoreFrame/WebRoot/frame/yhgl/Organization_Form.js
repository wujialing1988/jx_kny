Ext.onReady(function() {

	/*******************************************************************************/
	/*                                 本机构信息表单                                     */
    /*******************************************************************************/
	Ext.namespace('orgForm');
	
	orgForm.labelWidth = 90;
	orgForm.anchor = '85%';
	orgForm.fieldWidth = 160;
	
	orgForm.currentInfoForm = new Ext.form.FormPanel({
		layout:"form", border:true, style:"padding:10px" , baseCls: "x-plain", 
		labelWidth: orgForm.labelWidth, align:'center',  
		defaultType:'textfield',defaults:{anchor:"100%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain", 
			items:[{
				align:'center',	defaultType:'textfield', border:false,  layout:"form",
				labelWidth: orgForm.labelWidth,	columnWidth:0.5, baseCls: "x-plain", 
				items:[
					//隐藏部分
					{ id:'_orgid', fieldLabel:'机构ID',name:'orgid', disabled:false,hidden:true},
					{ id:'_createtime', fieldLabel:'创建时间',name:'createtime', disabled:false,hidden:true},
					{ id:"_parentorgid", fieldLabel:'上级机构', name:'parentorgid', 	disabled:false, hidden:true},
					{ id:"_orglevel", fieldLabel:'机构层次', name:'orglevel', 	disabled:false, hidden:true},
					{ id:"_orgseq", fieldLabel:'机构序列', name:'orgseq', 	disabled:false, hidden:true},
					{ id:"_orgmanager", fieldLabel:'机构管理员', name:'orgmanager', 	disabled:false, hidden:true},
					{ id:"_isleaf", fieldLabel:'是否是叶子节点', name:'isleaf', 	disabled:false, hidden:true},
					{ id:"_subcount", fieldLabel:'子节点数', name:'subcount', 	disabled:false, hidden:true},
					//显示部分
					{ id:"_orgcode",	fieldLabel:'机构代码', allowBlank:false,	name:'orgcode',	 width:orgForm.fieldWidth, maxLength:32},
					{ id:'_orgdegree', fieldLabel: '机构等级', name:'orgdegree', xtype: 'EosDictEntry_combo', hiddenName: 'orgdegree', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_ORGDEGREE'},
					{ id:"_sortno", fieldLabel:'排列顺序', name:'sortno',  width:orgForm.fieldWidth, vtype:'positiveInt',maxLength:4},
					{ id:"_weburl", fieldLabel:'主页', name:'weburl',  width:orgForm.fieldWidth,maxLength:200},
					{ id:"_linkman", fieldLabel:'联系人', name:'linkman',  width:orgForm.fieldWidth,maxLength:15},
					{ id:"_managerid", fieldLabel:'机构主管人员', name:'managerid', xtype: "OmEmployee_SelectWin", 
					  hiddenName: "managerid", displayField:"empname", valueField: "empid", width:orgForm.fieldWidth,
					  returnField :[{widgetId: '_orgmanager',propertyName:'empname'}],
					  editable: false
					},
					{ id:"_email", fieldLabel:'电子邮件', name:'email',  width:orgForm.fieldWidth,maxLength:100 },
					{ id:"_startdate", fieldLabel:'生效日期', name:'startdate',  width:orgForm.fieldWidth,xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:"_orgaddr", fieldLabel:'机构地址', name:'orgaddr', xtype:'textarea', maxLength:100,width:orgForm.fieldWidth}
				]
			},{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain", layout:"form",
				labelWidth: orgForm.labelWidth,	columnWidth:0.5,
				items:[
					{ id:"_orgname", fieldLabel:'机构名称', name:'orgname', allowBlank:false, width:orgForm.fieldWidth,maxLength:30},
					{ id:'_orgtype', fieldLabel:'机构类型', name : 'orgtype', xtype: 'EosDictEntry_combo', hiddenName: 'orgtype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_ORGTYPE'},
		            { id:'_status', fieldLabel:'机构状态', name : 'status', xtype: 'EosDictEntry_combo', hiddenName: 'status', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_ORGSTATUS'},
					{ id:"_area", fieldLabel:'所属地域', name:'area',  width:orgForm.fieldWidth,maxLength:15 },
					{ id:"_zipcode", fieldLabel:'邮政编码', name:'zipcode',  width:orgForm.fieldWidth,vtype:'postalcode',maxLength:10 },
					{ id:"_manaposition", fieldLabel:'机构主管岗位', name:'manaposition',  width:orgForm.fieldWidth , xtype: "OmPosition_SelectWin",
					  hiddenName: "manaposition", displayField:"posiname", valueField: "positionid", 
					  returnField :[{widgetId: '_temp',propertyName:'posiname'}],
					  editable: false 
					},
					{ id:"_linktel", fieldLabel:'联系电话', name:'linktel',  width:orgForm.fieldWidth, vtype:'mobile'},
					{ id:"_enddate", fieldLabel:'失效时期', name:'enddate',  width:orgForm.fieldWidth,xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:'_remark', fieldLabel:'备注', name:'remark',xtype:'textarea',maxLength:200,width:orgForm.fieldWidth }
				]
			}]
		}]
	});
	
	/**
	 * 读取当前所选机构节点的详细信息，并填充表单
	 */
	orgForm.findCurrentOrgInfo = function (){
		if(OrgTab.OrgNodeId!=null){
			Ext.Ajax.request({
				url: ctx + '/organization!findCurrentOrgInfo.action',//数据源路径
				params: {'nodeid' : OrgTab.OrgNodeId}, //查询可承修机车
				success: function(response, options){
				       var result = Ext.util.JSON.decode(response.responseText);
				       if (result.errMsg == null) {
			           		if(result!=null){
			           			//设置隐藏值
			           			orgForm.currentInfoForm.getForm().findField("_orgid").setValue(result.orgid); //机构ID
			           			orgForm.currentInfoForm.getForm().findField("_createtime").setValue(result.createtime); //创建时间
			           			orgForm.currentInfoForm.getForm().findField("_parentorgid").setValue(result.parentorgid); //上级机构
			           			orgForm.currentInfoForm.getForm().findField("_orglevel").setValue(result.orglevel); //机构层次
			           			orgForm.currentInfoForm.getForm().findField("_orgseq").setValue(result.orgseq); //机构序列
			           			orgForm.currentInfoForm.getForm().findField("_orgmanager").setValue(result.orgmanager); //机构管理员
			           			orgForm.currentInfoForm.getForm().findField("_isleaf").setValue(result.isleaf); //是否是叶子节点
			           			orgForm.currentInfoForm.getForm().findField("_subcount").setValue(result.subcount); //子节点数
			           			//设置回显值,第1列
			           			orgForm.currentInfoForm.getForm().findField("_orgcode").setValue(result.orgcode); //机构代码
			           			orgForm.currentInfoForm.getForm().findField("_orgdegree").setValue(result.orgdegree); //机构等级
			           			orgForm.currentInfoForm.getForm().findField("_sortno").setValue(result.sortno); //排列顺序
			           			orgForm.currentInfoForm.getForm().findField("_weburl").setValue(result.weburl); //主页
			           			orgForm.currentInfoForm.getForm().findField("_managerid").setDisplayValue(result.managerid,result.orgmanager);//机构主管人员
			           			orgForm.currentInfoForm.getForm().findField("_linkman").setValue(result.linkman); //联系人
			           			orgForm.currentInfoForm.getForm().findField("_email").setValue(result.email); //电子邮件
			           			if(result.startdate!=null)
			           			orgForm.currentInfoForm.getForm().findField("startdate").setValue(new Date(result.startdate).format('Y-m-d')); //生效日期
			           			orgForm.currentInfoForm.getForm().findField("_orgaddr").setValue(result.orgaddr); //机构地址
			           			//设置回显值,第2列
			           			orgForm.currentInfoForm.getForm().findField("_orgname").setValue(result.orgname); //机构名称
			           			orgForm.currentInfoForm.getForm().findField("_orgtype").setValue(result.orgtype); //机构类型
			           			orgForm.currentInfoForm.getForm().findField("_status").setValue(result.status); //机构状态
			           			orgForm.currentInfoForm.getForm().findField("_area").setValue(result.area); //所属地域
			           			orgForm.currentInfoForm.getForm().findField("_zipcode").setValue(result.zipcode); //邮政编码
			           			orgForm.currentInfoForm.getForm().findField("_manaposition").setDisplayValue(result.manaposition,result.posiname); //机构主管岗位
			           			orgForm.currentInfoForm.getForm().findField("_linktel").setValue(result.linktel); //联系电话
			           			if(result.enddate!=null)
			           			orgForm.currentInfoForm.getForm().findField("_enddate").setValue(new Date(result.enddate).format('Y-m-d')); //失效时期
			           			orgForm.currentInfoForm.getForm().findField("_remark").setValue(result.remark); //备注
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
	}
	orgForm.ZZMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请稍侯..."});
	orgForm.panel = new Ext.Panel({
		title: "编辑", layout: "fit", plain:true, 
		closeAction: "hide", modal: true, buttonAlign: "center",
		items : [{
			xtype: 'panel',	border:false,  layout:'column',	align:'center',  buttonAlign: "center", baseCls: "x-plain", 
			items: orgForm.currentInfoForm,
			buttons: [{
            	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            		var form = orgForm.currentInfoForm.getForm();
            		if (!form.isValid()) return;
            		var data = form.getValues();
            		orgForm.ZZMask.show();
			        var cfg = {
			            scope: this, url: ctx+"/organization!saveOrUpdate.action", jsonData: data,
			            success: function(response, options){
			                if(orgForm.ZZMask)   orgForm.ZZMask.hide();
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
			                    orgtree.tree.root.reload();
		    					orgtree.tree.getRootNode().expand();
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
	
	/*******************************************************************************/
	/*                                 本岗位信息表单                                     */
    /*******************************************************************************/
	Ext.namespace('posForm');
	
	posForm.labelWidth = 90;
	posForm.anchor = '85%';
	posForm.fieldWidth = 160;
	
	posForm.cPosForm = new Ext.form.FormPanel({
		layout:"form", border:true, style:"padding:10px" , baseCls: "x-plain", 
		labelWidth: posForm.labelWidth, align:'center',  
		defaultType:'textfield',defaults:{anchor:"100%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain", 
			items:[{
				align:'center',	defaultType:'textfield', border:false,  layout:"form",
				labelWidth: posForm.labelWidth,	columnWidth:0.5, baseCls: "x-plain", 
				items:[
					//隐藏部分
					{ id:'_positionid_2', fieldLabel:'岗位编码', name:'positionid', disabled:false, hidden:true},
					{ id:'_manaposi_2',   fieldLabel:'上级岗位', name:'manaposi',   disabled:false, hidden:true},
					{ id:'_posilevel_2',  fieldLabel:'岗位层次', name:'posilevel',  disabled:false, hidden:true},
					{ id:'_positionseq_2',fieldLabel:'岗位序列', name:'positionseq',disabled:false, hidden:true},
					{ id:'_createtime_2', fieldLabel:'创建时间', name:'createtime', disabled:false, hidden:true},
					{ id:'_lastupdate_2', fieldLabel:'最后一次修改时间', name:'lastupdate', 	disabled:false, hidden:true},
					{ id:'_updator_2',    fieldLabel:'最近更新人员', name:'updator', 	disabled:false, hidden:true},
					{ id:'_isleaf_2', fieldLabel:'是否是叶子节点', name:'isleaf', disabled:false, hidden:true},
					{ id:'_subcount_2',   fieldLabel:'子节点数', name:'subcount',   disabled:false, hidden:true},
					//显示部分
					{ id:'_posicode_2', fieldLabel:'岗位代码',	name:'posicode', allowBlank:false, width:posForm.fieldWidth, maxLength:20},
					{ id:'_orgid_2', fieldLabel:'所属机构', name:'orgid',  width:posForm.fieldWidth, maxLength:20},
					{ id:'_positype_2', fieldLabel: '岗位类别', name:'positype', xtype: 'EosDictEntry_combo', disabled:true, allowBlank:false,
					  hiddenName: 'positype', displayField: 'dictname', valueField: 'dictid', status:'1', dicttypeid:'ABF_POSITYPE'},
					{ id:'_startdate_2', fieldLabel:'有效开始时间', name:'startdate', width:posForm.fieldWidth,xtype:'my97date', format: 'Y-m-d',initNow:false}
				]
			},{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain", layout:"form",
				labelWidth: posForm.labelWidth,	columnWidth:0.5,
				items:[
					{ id:'_posiname_2', fieldLabel:'岗位名称', name:'posiname', allowBlank:false, width:posForm.fieldWidth,maxLength:128},
					{ id:'_dutyid_2',   fieldLabel:'所属职务', name:'dutyid',    width:posForm.fieldWidth,maxLength:30,
						xtype: "WorkDuty_comboTree",
			    		hiddenName: "dutyid", disabled:false,
			    		selectNodeModel: "leaf" ,
				    	allowBlank: false
					},
				    { id:'_status_2', fieldLabel: '岗位状态', name:'status', xtype: 'EosDictEntry_combo', 
					  hiddenName: 'status', displayField: 'dictname', valueField: 'dictid', status:'1', dicttypeid:'ABF_POSISTATUS'},
					{ id:'_enddate_2', fieldLabel:'有效结束时间', name:'enddate', width:posForm.fieldWidth,xtype:'my97date', format: 'Y-m-d',initNow:false}
				]
			}]
		}]
	});
	
	/**
	 * 读取当前所选岗位节点的详细信息，并填充表单
	 */
	posForm.findCPosInfo = function (){
		if(OrgTab.OrgNodeId!=null){
			Ext.Ajax.request({
				url: ctx + '/position!findCurrentPosInfo.action',//数据源路径
				params: {'nodeid' : OrgTab.PosNodeId}, 
				success: function(response, options){
				       var result = Ext.util.JSON.decode(response.responseText);
				       if (result.errMsg == null) {
			           		if(result!=null){
			           			//设置隐藏值
			           			posForm.cPosForm.getForm().findField("_positionid_2").setValue(result.positionid); //岗位编码
			           			posForm.cPosForm.getForm().findField("_manaposi_2").setValue(result.manaposi); //上级岗位
			           			posForm.cPosForm.getForm().findField("_posilevel_2").setValue(result.posilevel);  //岗位层次
			           			posForm.cPosForm.getForm().findField("_positionseq_2").setValue(result.positionseq); //岗位序列
			           			if(result.createtime!=null)
			           			posForm.cPosForm.getForm().findField("_createtime_2").setValue(new Date(result.createtime).format('Y-m-d'));  //创建时间
			           			if(result.lastupdate!=null)
			           			posForm.cPosForm.getForm().findField("_lastupdate_2").setValue(new Date(result.lastupdate).format('Y-m-d'));  //最后一次修改时间
			           			posForm.cPosForm.getForm().findField("_updator_2").setValue(result.updator);  //最近更新人员
			           			posForm.cPosForm.getForm().findField("_isleaf_2").setValue(result.isleaf);   //是否是叶子节点
			           			posForm.cPosForm.getForm().findField("_subcount_2").setValue(result.subcount);  //子节点数
			           			//设置显示值
			           			posForm.cPosForm.getForm().findField("_posicode_2").setValue(result.posicode); //岗位代码
			           			posForm.cPosForm.getForm().findField("_orgid_2").setValue(result.orgid); //所属机构
			           			posForm.cPosForm.getForm().findField("_positype_2").setValue(result.positype); //岗位类别
			           			if(result.startdate!=null)
			           			posForm.cPosForm.getForm().findField("_startdate_2").setValue(new Date(result.startdate).format('Y-m-d')); //有效开始时间
			           			posForm.cPosForm.getForm().findField("_posiname_2").setValue(result.posiname); //岗位名称
			           			posForm.cPosForm.getForm().findField("_dutyid_2").setDisplayValue(result.dutyid,result.dutyname); //所属职务
			           			posForm.cPosForm.getForm().findField("_status_2").setValue(result.status); //岗位状态
			           			if(result.enddate!=null)
			           			posForm.cPosForm.getForm().findField("_enddate_2").setValue(new Date(result.enddate).format('Y-m-d')); //有效结束时间
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
	}
	
	posForm.panel = new Ext.Panel({
		title: "编辑", layout: "fit", plain:true, 
		closeAction: "hide", modal: true, buttonAlign: "center",
		items : [{
			xtype: 'panel',	border:false,  layout:'column',	align:'center',  buttonAlign: "center", baseCls: "x-plain", 
			items: posForm.cPosForm,
			buttons: [{
            	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            		var form = posForm.cPosForm.getForm();
            		if (!form.isValid()) return;
            		Ext.getCmp('_positype_2').setDisabled(false); //解除岗位类别的disabled状态
            		var data = form.getValues();
            		Ext.getCmp('_positype_2').setDisabled(true); //设定岗位类别的disabled状态
			        var cfg = {
			            scope: this, url: ctx+"/position!saveOrUpdate.action", jsonData: data,
			            success: function(response, options){
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
			                    orgtree.tree.root.reload();
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
	
	/*******************************************************************************/
	/*                                 当前人员信息表单                                     */
    /*******************************************************************************/
	Ext.namespace('empForm');
	
	empForm.labelWidth = 90;
	empForm.anchor = '85%';
	empForm.fieldWidth = 160;
	
	empForm.cEmpForm = new Ext.form.FormPanel({
		layout:"form", border:true, style:"padding:10px" , baseCls: "x-plain", 
		labelWidth: empForm.labelWidth, align:'center',  
		defaultType:'textfield',defaults:{anchor:"100%"}, buttonAlign:'center',
		items:[{
			xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain", 
			items:[{
				align:'center',	defaultType:'textfield', border:false,  layout:"form",
				labelWidth: empForm.labelWidth,	columnWidth:0.5, baseCls: "x-plain", 
				items:[
					//隐藏部分
					{ id:'_empid_3', fieldLabel:'人员ID', name:'empid', disabled:false, hidden:true},
					{ id:'_operatorid_3', fieldLabel:'操作员ID', name:'operatorid', disabled:false, hidden:true},
					{ id:'_userid_3', fieldLabel:'操作员登录号', name:'userid', disabled:false, hidden:true},
					{ id:'_realname_3', fieldLabel:'人员全名', name:'realname', disabled:false, hidden:true},
					{ id:'_position_3', fieldLabel:'基本岗位', name:'position', disabled:false, hidden:true},
					{ id:'_cardNum_3', fieldLabel:'员工IC卡号', name:'cardNum', disabled:false, hidden:true},
					{ id:'_payId_3', fieldLabel:'工资代码', name:'payId', disabled:false, hidden:true},
					{ id:'_createtime_3', fieldLabel:'创建时间', name:'createtime', disabled:false, hidden:true},
					{ id:'_lastmodytime_3', fieldLabel:'最后更新时间', name:'lastmodytime', disabled:false, hidden:true},
					{ id:'_orgid_3', fieldLabel:'主机构编号', name:'orgid', disabled:false, hidden:true},
					//显示部分
					{ id:'_empcode_3', fieldLabel:'人员代码',	name:'empcode', allowBlank:false, width:empForm.fieldWidth, maxLength:30, vtype:'alphanum2'},
					{ id:'_regdate_3', fieldLabel:'注册日期',	name:'regdate', width:empForm.fieldWidth, xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:'_gender_3', fieldLabel:'性别',	name:'gender', width:empForm.fieldWidth, maxLength:20,
						xtype: 'EosDictEntry_combo', hiddenName: 'gender', displayField: 'dictname', valueField: 'dictid',status:'1',dicttypeid:'ABF_GENDER'
					},
					{ id:'_party_3', fieldLabel:'政治面貌',	name:'party',  width:posForm.fieldWidth, 
						xtype: 'EosDictEntry_combo', hiddenName: 'party', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_PARTYVISAGE'
					},
					{ id:'_cardtype_3', fieldLabel:'证件类型',	name:'cardtype',  width:posForm.fieldWidth, 
						xtype: 'EosDictEntry_combo', hiddenName: 'cardtype', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_CARDTYPE'
					},
					{ id:'_indate_3', fieldLabel:'入职时间',	name:'indate', width:posForm.fieldWidth, xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:'_otel_3', fieldLabel:'办公室电话',	name:'otel', width:posForm.fieldWidth, vtype:'telphone',maxLength:12},
					{ id:'_ozipcode_3', fieldLabel:'办公邮编',	name:'ozipcode', width:posForm.fieldWidth, vtype:'postalcode',maxLength:10},
					{ id:'_faxno_3', fieldLabel:'传真号码',	name:'faxno', width:posForm.fieldWidth, maxLength:14,vtype:'telphone'},
					{ id:'_htel_3', fieldLabel:'家庭电话',	name:'htel', width:posForm.fieldWidth, vtype:'telphone',maxLength:12},
					{ id:'_hzipcode_3', fieldLabel:'家庭邮编',	name:'hzipcode', width:posForm.fieldWidth, vtype:'postalcode',maxLength:10},
					{ id:'_msn_3', fieldLabel:'IM号码',	name:'msn', width:posForm.fieldWidth, maxLength:16,hidden:true},
					{ id:'_major_3', fieldLabel:'直接主管',	name:'major', width:posForm.fieldWidth, maxLength:20,hidden:true},
					{ id:'_workexp_3', fieldLabel:'工作描述',	name:'workexp', width:posForm.fieldWidth, xtype:'textarea',maxLength:512}
				]
			},{
				align:'center',	defaultType:'textfield', border:false, baseCls: "x-plain", layout:"form",
				labelWidth: empForm.labelWidth,	columnWidth:0.5,
				items:[
					{ id:'_empname_3', fieldLabel:'人员姓名',	name:'empname', allowBlank:false, width:empForm.fieldWidth, maxLength:25,vtype:'chinese'},
					{ id:'_birthdate_3', fieldLabel:'出生日期',	name:'birthdate', width:empForm.fieldWidth, xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:'_empstatus_3', fieldLabel:'人员状态',	name:'empstatus', width:empForm.fieldWidth, 
						xtype: 'EosDictEntry_combo', hiddenName: 'empstatus', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPSTATUS'
					},
					{ id:'_degree_3', fieldLabel:'职级',	name:'degree', width:posForm.fieldWidth, 
						xtype: 'EosDictEntry_combo', hiddenName: 'degree', status:'1', displayField: 'dictname', valueField: 'dictid', dicttypeid:'ABF_EMPZC'
					},
					{ id:'_cardno_3', fieldLabel:'证件号码',	name:'cardno', width:posForm.fieldWidth, maxLength:20},
					{ id:'_outdate_3', fieldLabel:'离职时间',	name:'outdate', width:posForm.fieldWidth, xtype:'my97date', format: 'Y-m-d',initNow:false},
					{ id:'_oaddress_3', fieldLabel:'办公室地址',	name:'oaddress', width:posForm.fieldWidth, maxLength:255},
					{ id:'_oemail_3', fieldLabel:'办公邮箱',	name:'oemail', width:posForm.fieldWidth, maxLength:128},
					{ id:'_mobileno_3', fieldLabel:'手机号码',	name:'mobileno',  width:posForm.fieldWidth, vtype:'mobile',maxLength:14},
					{ id:'_haddress_3', fieldLabel:'家庭地址',	name:'haddress',  width:posForm.fieldWidth, maxLength:128},
					{ id:'_pemail_3', fieldLabel:'个人邮箱',	name:'pemail', width:posForm.fieldWidth, maxLength:128},
					{ id:'_specialty_3', fieldLabel:'可授权角色',	name:'specialty', width:posForm.fieldWidth, maxLength:20,hidden:true},
					{ id:'_orgidlist_3', fieldLabel:'可管理机构',	name:'orgidlist', width:posForm.fieldWidth, maxLength:20,hidden:true},
					{ id:'_remark_3', fieldLabel:'备注',	name:'remark', width:posForm.fieldWidth, xtype:'textarea',maxLength:200}
				]
			}]
		}]
	});
	
	/**
	 * 读取当前所选岗位节点的详细信息，并填充表单
	 */
	empForm.findCEmpInfo = function (arg){
		if(arg==null||arg=='') return;
		if(OrgTab.OrgNodeId!=null){
			Ext.Ajax.request({
				url: ctx + '/employee!findCurrentEmpInfo.action',//数据源路径
				params: {'nodeid' : arg}, 
				success: function(response, options){
				       var result = Ext.util.JSON.decode(response.responseText);
				       if (result.errMsg == null) {
			           		if(result!=null){
			           			//设置隐藏值
			           			empForm.cEmpForm.getForm().findField("_empid_3").setValue(result.empid); //人员ID
			           			empForm.cEmpForm.getForm().findField("_operatorid_3").setValue(result.operatorid); //操作员ID
			           			empForm.cEmpForm.getForm().findField("_userid_3").setValue(result.userid); //操作员登录号
			           			empForm.cEmpForm.getForm().findField("_realname_3").setValue(result.realname); //人员全名
			           			empForm.cEmpForm.getForm().findField("_position_3").setValue(result.position); //基本岗位
			           			empForm.cEmpForm.getForm().findField("_cardNum_3").setValue(result.cardNum); //员工IC卡号
			           			empForm.cEmpForm.getForm().findField("_payId_3").setValue(result.payId); //工资代码
			           			empForm.cEmpForm.getForm().findField("_createtime_3").setValue(result.createtime); //创建时间
			           			empForm.cEmpForm.getForm().findField("_lastmodytime_3").setValue(result.lastmodytime); //最后更新时间
			           			empForm.cEmpForm.getForm().findField("_orgid_3").setValue(result.orgid); //主机构编号
			           			//设置显示值
			           			empForm.cEmpForm.getForm().findField("_empcode_3").setValue(result.empcode); //人员代码
			           			if(result.regdate!=null)
			           			empForm.cEmpForm.getForm().findField("_regdate_3").setValue(new Date(result.regdate).format('Y-m-d')); //注册日期
			           			empForm.cEmpForm.getForm().findField("_gender_3").setValue(result.gender); //性别
			           			empForm.cEmpForm.getForm().findField("_party_3").setValue(result.party); //政治面貌
			           			empForm.cEmpForm.getForm().findField("_cardtype_3").setValue(result.cardtype); //证件类型
			           			if(result.indate!=null)
			           			empForm.cEmpForm.getForm().findField("_indate_3").setValue(new Date(result.indate).format('Y-m-d')); //入职时间
			           			empForm.cEmpForm.getForm().findField("_otel_3").setValue(result.otel); //办公室电话
			           			empForm.cEmpForm.getForm().findField("_ozipcode_3").setValue(result.ozipcode); //办公邮编
			           			empForm.cEmpForm.getForm().findField("_faxno_3").setValue(result.faxno); //传真号码
			           			empForm.cEmpForm.getForm().findField("_htel_3").setValue(result.htel); //家庭电话
			           			empForm.cEmpForm.getForm().findField("_hzipcode_3").setValue(result.hzipcode); //家庭邮编
			           			empForm.cEmpForm.getForm().findField("_msn_3").setValue(result.msn); //IM号码
			           			empForm.cEmpForm.getForm().findField("_major_3").setValue(result.major); //直接主管
			           			empForm.cEmpForm.getForm().findField("_workexp_3").setValue(result.workexp); //工作描述
			           			
			           			empForm.cEmpForm.getForm().findField("_empname_3").setValue(result.empname); //人员姓名
			           			if(result.birthdate!=null)
			           			empForm.cEmpForm.getForm().findField("_birthdate_3").setValue(new Date(result.birthdate).format('Y-m-d')); //出生日期
			           			empForm.cEmpForm.getForm().findField("_empstatus_3").setValue(result.empstatus); //人员状态
			           			empForm.cEmpForm.getForm().findField("_degree_3").setValue(result.degree); //职级
			           			empForm.cEmpForm.getForm().findField("_cardno_3").setValue(result.cardno); //证件号码
			           			if(result.outdate!=null)
			           			empForm.cEmpForm.getForm().findField("_outdate_3").setValue(new Date(result.outdate).format('Y-m-d')); //离职时间
			           			empForm.cEmpForm.getForm().findField("_oaddress_3").setValue(result.oaddress); //办公室地址
			           			empForm.cEmpForm.getForm().findField("_oemail_3").setValue(result.oemail); //办公邮箱
			           			empForm.cEmpForm.getForm().findField("_mobileno_3").setValue(result.mobileno); //手机号码
			           			empForm.cEmpForm.getForm().findField("_haddress_3").setValue(result.haddress); //家庭地址
			           			empForm.cEmpForm.getForm().findField("_pemail_3").setValue(result.pemail); //个人邮箱
			           			empForm.cEmpForm.getForm().findField("_specialty_3").setValue(result.specialty); //可授权角色
			           			empForm.cEmpForm.getForm().findField("_orgidlist_3").setValue(result.orgidlist); //可管理机构
			           			empForm.cEmpForm.getForm().findField("_remark_3").setValue(result.remark); //备注
			           		}
				       } else {
				              alertFail(result.errMsg);
				              return true;
				       }
				},
				failure: function(response, options){
				       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
				}
			});
		}
	}
	
	empForm.panel = new Ext.Panel({
		title: "编辑", layout: "fit", plain:true, 
		closeAction: "hide", modal: true, buttonAlign: "center",
		items : [{
			xtype: 'panel',	border:false,  layout:'column',	align:'center',  buttonAlign: "center", baseCls: "x-plain", 
			items: empForm.cEmpForm,
			buttons: [{
	            	id: '_operatorBtn_3', text: "登录设置", iconCls: "cogIcon", hidden:false, scope: this, handler: function(){
	            		saveFnEmpId = empForm.cEmpForm.getForm().findField("_empid_3").getValue();
		            	operator.win.show();
		            	/*
		            	 * 重置控件
		            	 */
		            	operator.operatorForm.getForm().reset();  //重置表单
	    				var my97Ary = operator.operatorForm.findByType('my97date');
	                    if(!Ext.isEmpty(my97Ary) && Ext.isArray(my97Ary)){
	                    	for(var i = 0; i < my97Ary.length; i++){
		                        my97Ary[ i ].setValue('');
		                    }
	                    }
	    				var component = operator.operatorForm.findByType("EosDictEntry_combo"); //获取页面中所有控件
	    				if(!Ext.isEmpty(component) && Ext.isArray(component)){
	                    	for(var i = 0; i < component.length; i++){
			            	    component[ i ].clearValue(); //重置控件
			                }
	                    }
		            	//如果当前表单中操作人员id不为空，则说明已添加该人员的登录信息，执行Ajax查询方法
	            		if(Ext.getCmp('_operatorid_3').getValue()!=null&&Ext.getCmp('_operatorid_3').getValue()!=''){
		            		Ext.Ajax.request({
								url: ctx + '/operator!findOperatorInfo.action', //数据源路径
								params: {'operatorid' : Ext.getCmp('_operatorid_3').getValue()}, //操作员Id
								success: function(response, options){
								       var result = Ext.util.JSON.decode(response.responseText);
								       if (result != null && result.errMsg == null &&result.acOperator != null) {
							           		operator.operatorForm.getForm().findField('_operatorid_1').setValue(result.acOperator.operatorid); //操作员ID
							           		operator.operatorForm.getForm().findField('_operatorname_1').setValue(result.acOperator.operatorname);//操作员名称
							           		operator.operatorForm.getForm().findField('_unlocktime_1').setValue(result.acOperator.unlocktime);//解锁时间
							           		operator.operatorForm.getForm().findField('_lastlogin_1').setValue(result.acOperator.lastlogin);//最后一次登录时间
							           		operator.operatorForm.getForm().findField('_errcount_1').setValue(result.acOperator.errcount);//最新登录次数
							           		operator.operatorForm.getForm().findField('_validtime_1').setValue(result.acOperator.validtime);//有效时间范围
							           		operator.operatorForm.getForm().findField('_maccode_1').setValue(result.acOperator.maccode);//MAC码
							           		operator.operatorForm.getForm().findField('_ipaddress_1').setValue(result.acOperator.ipaddress);//iP地址
							           		operator.operatorForm.getForm().findField('_email_1').setValue(result.acOperator.email);//邮箱
							           		operator.operatorForm.getForm().findField('_userid_1').setValue(result.acOperator.userid);//登录名
							           		operator.operatorForm.getForm().findField('_status_1').setValue(result.acOperator.status);//操作员状态
							           		operator.operatorForm.getForm().findField('_menutype_1').setValue(result.acOperator.menutype);//菜单风格
							           		if(result.acOperator.startdate!=null&&result.acOperator.startdate!='')
							           		operator.operatorForm.getForm().findField('_startdate_1').setValue(new Date(result.acOperator.startdate).format('Y-m-d'));//生效日期
							           		operator.operatorForm.getForm().findField('_password_1').setValue(result.acOperator.password);//登录密码
							           		operator.operatorForm.getForm().findField('_authmode_1').setValue(result.acOperator.authmode);//认证模式
							           		if(result.acOperator.invaldate!=null&&result.acOperator.invaldate!='')
							           		operator.operatorForm.getForm().findField('_invaldate_1').setValue(new Date(result.acOperator.invaldate).format('Y-m-d'));//密码失效日期
							           		if(result.acOperator.enddate!=null&&result.acOperator.enddate!='')
							           		operator.operatorForm.getForm().findField('_enddate_1').setValue(new Date(result.acOperator.enddate).format('Y-m-d'));//失效日期
								       } else {
								       		alertFail(result.errMsg);
								       }
								},
								failure: function(response, options){
								       MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
								}
							});
	            		} 
	            		//当前表单中操作员id为空，未曾设置该人员的登录信息
	            		else {
	            			Ext.getCmp('_operatorname_1').setValue(Ext.getCmp('_empname_3').getValue()); //登录信息表单中设置操作员姓名
	            			//saveFnEmpId = empForm.cEmpForm.getForm().findField("_empid_3").getValue();
	            		}
	            	}
	            },{
            	text: "保存", iconCls: "saveIcon", scope: this, handler: function(){
            		var form = empForm.cEmpForm.getForm();
            		if (!form.isValid()) return;
            		var data = form.getValues();
			        var cfg = {
			            scope: this, url: ctx+"/employee!saveOrUpdate.action", jsonData: data,
			            success: function(response, options){
			                var result = Ext.util.JSON.decode(response.responseText);
			                if (result.errMsg == null) {
			                    alertSuccess();
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