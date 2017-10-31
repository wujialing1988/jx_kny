Ext.onReady(function(){
	Ext.namespace('ZbglTpInfoFormWin');//定义命名空间
	//最近一个月
	ZbglTpInfoFormWin.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth-1,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	//车号
	ZbglTpInfoFormWin.trainNo;
	//车型idx
	ZbglTpInfoFormWin.trainTypeIDX;
	//车型简称
	ZbglTpInfoFormWin.trainTypeShortName;
	//提票主键idx
	ZbglTpInfoFormWin. jt6IDX;
	//提票单号
	ZbglTpInfoFormWin.faultNoticeCode;
	
	//表单布局
	ZbglTpInfoFormWin.infoForm = new Ext.form.FormPanel({
		layout:"form",
		frame:true,
		closable : false,
		plain:true,
		border:false, //style:"padding:10px" ,
		labelWidth:80,
		labelAlign:"right",
		//baseCls: "x-plain",
		items:[{
			layout:"column",
			items:[{
				columnWidth:.5,
				layout:"form",
				items:[{
					id:"faultNoticeCode_idx",
					xtype:"label",
					fieldLabel:"提票单号",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"trainTypeShortName_idx",
					xtype:"label",
					fieldLabel:"车型",
					anchor:"98%",
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"trainNo_idx",
					xtype:"label",
					fieldLabel:"车号",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"noticePersonName_idx",
					xtype:"label",
					fieldLabel:"提票人",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"noticeTime_idx",
					xtype:"label",
					fieldLabel:"提票时间",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"discover_idx",
					xtype:"label",
					fieldLabel:"发现人",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultFixFullName_idx",
					xtype:"label",
					fieldLabel:"故障部件",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultName_idx",
					xtype:"label",
					fieldLabel:"故障现象",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultDesc_idx",
					xtype:"label",
					fieldLabel:"故障描述",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultReason_idx",
					xtype:"label",
					fieldLabel:"故障原因",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultOccurDate_idx",
					xtype:"label",
					fieldLabel:"故障发生日期",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultNoticeStatus_idx",
					xtype:"label",
					fieldLabel:"提票状态",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"dID_idx",
					xtype:"label",
					fieldLabel:"配属段编码",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"dName_idx",
					xtype:"label",
					fieldLabel:"配属段名称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultID_idx",
					xtype:"label",
					fieldLabel:"故障ID",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"professionalTypeName_idx",
					xtype:"label",
					fieldLabel:"专业类型名称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"noticeSource_idx",
					xtype:"label",
					fieldLabel:"提票来源",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"noticePersonId_idx",
					xtype:"label",
					fieldLabel:"提票人编码",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"siteName_idx",
					xtype:"label",
					fieldLabel:"提票站场名称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"revOrgName_idx",
					xtype:"label",
					fieldLabel:"处理班组名称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"revPersonName_idx",
					xtype:"label",
					fieldLabel:"接票人",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"revTime_idx",
					xtype:"label",
					fieldLabel:"接票时间",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"methodDesc_idx",
					xtype:"label",
					fieldLabel:"施修方法",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"repairResult_idx",
					xtype:"label",
					fieldLabel:"处理结果",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"repairDesc_idx",
					xtype:"label",
					fieldLabel:"处理结果描述",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"handlePersonName_idx",
					xtype:"label",
					fieldLabel:"销票人",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"handleTime_idx",
					xtype:"label",
					fieldLabel:"销票时间",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"repairEmp_idx",
					xtype:"label",
					fieldLabel:"处理人",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"handleSiteID_idx",
					xtype:"label",
					fieldLabel:"销票站场",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"accPersonName_idx",
					xtype:"label",
					fieldLabel:"验收人名称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"accTime_idx",
					xtype:"label",
					fieldLabel:"验收时间",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"repairClass_idx",
					xtype:"label",
					fieldLabel:"检修类型",
					anchor:"98%"
				}]
			}]
		}]
	});
	
	ZbglTpInfoFormWin.win = new Ext.Window({
	    title:"JT6提票信息", 
	    layout: 'fit',
		height: 480, width: 400,
		items:ZbglTpInfoFormWin.infoForm,
		closable : true,
		plain:true,
		closeAction:"hide",
		buttonAlign: 'center'
	});
	
	//定义出现时间的idx字符串
	var timeIDString = "noticeTime_idx,faultOccurDate_idx,revTime_idx,handleTime_idx,accTime_idx";
	var noticeSourceObj = ["运用","整备","质检","技术"];
	var repairResultObj = ["修复","观察运用","转JT28","转临修","返本段修","扣车等件"];
	ZbglTpInfoFormWin.showWin = function(jt6IDX) {
		//提票表单初始化数据
		Ext.Ajax.request({
			url: ctx + "/zbglTp!getZbglTpById.action",
			params:{jt6IDX:jt6IDX},
			success: function(r){
				var retn = Ext.util.JSON.decode(r.responseText);
				if(retn != null){
				//为表单对象赋值
				var tp = retn.tp
				for(var p in tp){
					var cmpID = p + "_idx";
					var object = Ext.getCmp(cmpID);
					if(object){
						if(p && timeIDString.indexOf(cmpID) > -1){
							if(tp[p]){
								var date = new Date(tp[p]).format('Y-m-d');
								object.setText(date);
							}
						}else{
							//提票来源
							if(p && "noticeSource_idx" == cmpID){
								object.setText(noticeSourceObj[parseInt(tp[p]) - 1]);
							//处理结果
							}else if(p && "repairResult_idx" == cmpID){
								if(tp[p] != null){
								    object.setText(repairResultObj[parseInt(tp[p])- 1]);
								}
							//检修类型
							}else if(p && "repairClass_idx" == cmpID){
								object.setText(tp[p] == 10 ? "碎修" : "临修");
							//提票状态
							}else if(p && "faultNoticeStatus_idx" == cmpID){
								if(tp[p] == "TODO")  object.setText("待接活");
								if(tp[p] == "INITIALIZE")  object.setText("初始化");
								if(tp[p] == "ONGOING")  object.setText("待销活");
								if(tp[p] == "COMPLETE")  object.setText("已处理");
								if(tp[p] == "CHECKED")  object.setText("已检验");
							}else{
								object.setText(tp[p]);
							}
						
						}
					
					}
				}
					/*
					Ext.getCmp("faultNoticeCode_idx").setText(tp.faultNoticeCode);
					Ext.getCmp("trainTypeShortName_idx").setText(tp.trainTypeShortName);
					Ext.getCmp("trainNo_idx").setText(tp.trainNo);
					Ext.getCmp("noticePersonName_idx").setText(tp.noticePersonName);
					Ext.getCmp("discover_idx").setText(tp.discover);
					Ext.getCmp("faultFixFullName_idx").setText(tp.noticeTime);
					Ext.getCmp("faultName_idx").setText(tp.noticeTime);
					Ext.getCmp("faultDesc_idx").setText(tp.noticeTime);
					Ext.getCmp("faultReason_idx").setText(tp.noticeTime);
					Ext.getCmp("faultOccurDate_idx").setText(tp.noticeTime);
					Ext.getCmp("faultNoticeStatus_idx").setText(tp.noticeTime);
					Ext.getCmp("dID_idx").setText(tp.noticeTime);
					Ext.getCmp("dName_idx").setText(tp.noticeTime);
					Ext.getCmp("faultID_idx").setText(tp.noticeTime);
					Ext.getCmp("professionalTypeName_idx").setText(tp.noticeTime);
					Ext.getCmp("noticeSource_idx").setText(tp.noticeTime);
					Ext.getCmp("noticePersonId_idx").setText(tp.noticeTime);
					Ext.getCmp("siteName_idx").setText(tp.noticeTime);
					Ext.getCmp("revOrgName_idx").setText(tp.noticeTime);
					Ext.getCmp("revPersonName_idx").setText(tp.noticeTime);
					Ext.getCmp("revTime_idx").setText(tp.noticeTime);
					Ext.getCmp("methodDesc_idx").setText(tp.noticeTime);
					Ext.getCmp("repairResult_idx").setText(tp.noticeTime);
					Ext.getCmp("repairDesc_idx").setText(tp.noticeTime);
					Ext.getCmp("handlePersonName_idx").setText(tp.noticeTime);
					Ext.getCmp("handleTime_idx").setText(tp.noticeTime);
					Ext.getCmp("repairEmp_idx").setText(tp.noticeTime);
					Ext.getCmp("handleSiteID_idx").setText(tp.noticeTime);
					Ext.getCmp("accPersonName_idx").setText(tp.noticeTime);
					Ext.getCmp("accTime_idx").setText(tp.noticeTime);
					Ext.getCmp("repairClass_idx").setText(tp.noticeTime);
					*/
				
				}
			},
			failure: function(){
				alertFail("请求超时！");
			}
		});
		
		ZbglTpInfoFormWin.win.show();
	}
});
