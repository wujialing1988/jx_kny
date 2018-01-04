
Ext.onReady(function(){
	    Ext.namespace("testComponent");
	    testComponent.labelWidth = 200;
		testComponent.fieldWidth = 200;		
		testComponent.callReturnFn = function(node, e){
			alert("0");
			testComponent.form.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["text"]);
		}
		testComponent.callReturnFn2 = function(node, e){
			alert("2");
			testComponent.form.findByType("PartsTypeTreeSelect")[1].setValue(node.attributes["text"]);
		}
		testComponent.callReturnFn1 = function(grid, rowIndex, e){
			var record = grid.store.getAt(rowIndex);
		}
		testComponent.callReturnFn3 = function(node, e){
			alert("3");
			testComponent.form.findByType("TrainTypeTreeSelect")[0].setValue(node.attributes["text"]);
		}
		testComponent.callReturnFn4 = function(value, jsonValue){
			alert("4");
			testComponent.form.findByType("PartsExtendNoSelect")[0].setValue(value);
			Ext.getCmp("textId").setValue(jsonValue);
		}
	    	//信息表单
		testComponent.form = new Ext.form.FormPanel({
		    baseCls: "x-plain", align: "center",	defaultType: "textfield",	defaults: { anchor: "98%" },
		    layout: "form",		border: false,		style: "padding:10px",		labelWidth: testComponent.labelWidth,
		    buttonAlign: "center",
		    renderTo:Ext.getBody(),	
		    buttons: [{
			        	id:"submitBtn", text:i18n.TestComponent.save, iconCls:"saveIcon",
			        	handler:function(){
				            var form = testComponent.form.getForm();
				            if (!form.isValid()) return;			            
				            var data = form.getValues();            
	        		    }
		    }, {
		        id:"resetBtn",text: i18n.TestComponent.reset, iconCls:"resetIcon",
		        handler: function(){
//		        	Ext.getCmp("ProfessionalType_comboTree_Id").clearValue();
//		        	Ext.getCmp("PartsClass_comboTree_Id").clearValue();
//		        	Ext.getCmp("MatClass_comboTree_Id").clearValue();
//		        	Ext.getCmp("OmOrganizationCustom_comboTree_Id").clearValue();
////		        	Ext.getCmp("warehouseId").clearValue();
//		        	Ext.getCmp("EosDictEntry_combo_Id").clearValue();
//		        	Ext.getCmp("OmEmployee_SelectWin_Id").clearValue();
//		        	Ext.getCmp("GyjcFactory_SelectWin_Id").clearValue();
//		        	Ext.getCmp("PartsAccount_SelectWin_Id").clearValue();
//		        	Ext.getCmp("PartsStock_SelectWin_Id").clearValue();
//		        	Ext.getCmp("PartsTypeAndQuota_SelectWin_Id").clearValue();
		        	testComponent.form.findByType("PartsTypeTreeSelect")[0].setValue("");
		        }
		    },{
			        	text:i18n.TestComponent.institutionTree, iconCls:"saveIcon",
			        	handler:function(){
				            OmOrganizationTreeWin.win.show();           
	        		    }
		    },{
			        	text:i18n.TestComponent.TestThePhoneAPP, iconCls:"saveIcon",
			        	handler:function(){
			        		//工长派工-1.1 查询已派工列表
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryDispatchedList.action",
//								params : {
//									operatorid : 800109,
//									searchJson : '{"haveDefaultPerson":"#1#","workStationBelongTeam":"#2#","workers":"#3#"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										
//									} 
//								}
//							});
							//工长派工-1.1 查询未派工列表
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryNotDispatchList.action",
//								params : {
//									operatorid : 800109,
//									searchJson : '{"haveDefaultPerson":"#1#","workStationBelongTeam":"#2#","workers":"#3#"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										
//									} 
//								}
//							});
							//人员查询-2.1 根据组织获取人员列表
//							Ext.Ajax.request({
//								url : ctx + "/employee!queryEmpListByTeam.action",
//								params : {
//									orgId : 106
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										
//									} 
//								}
//							});
							//工长派工-1.3 工长派工、批量工长派工
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!saveDispatch.action",
//								params : {
//									operatorid : 800109,
//									workCardIds : '40DBB0ED75444FAC877767521ADCB2DB,68A7A2C3812C4ED6AE5B185FE80A8AC9',
//									empids : '109,112'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.4 默认上次工长派工
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!saveDefaultLastTimeDispatch.action",
//								params : {
//									operatorid : 800109,
//									workCardIds : '40DBB0ED75444FAC877767521ADCB2DB,68A7A2C3812C4ED6AE5B185FE80A8AC9'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.5 全部工长派工
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!saveAllDispatch.action",
//								params : {
//									operatorid : 800109,
//									empids : '109,112',
//									searchJson : '{"haveDefaultPerson":"0","workStationBelongTeam":"#2#","rdpIDX":"8a828464490303dc014907529c6d00e1"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.6 查询生产任务单【已派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryDispatchedRdpList.action",
//								params : {
//									operatorid : 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.6 查询生产任务单【未派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryNotDispatchRdpList.action",
//								params : {
//									operatorid : 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.8 查询检修活动列表【已派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryDispatchedRepairActivityList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464490303dc014907529c6d00e1'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.8 查询检修活动列表【未派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryNotDispatchRepairActivityList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464486891cd014876f295790162'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.10 查询工艺节点列表【已派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryDispatchedTecNodeList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464490303dc014907529c6d00e1'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.11 查询工艺节点列表【未派工】
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryNotDispatchTecNodeList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464486891cd014876f295790162'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//工长派工-1.12 查询处理状态列表
//				            Ext.Ajax.request({
//								url : ctx + "/gzpgProc!queryStatusList.action",
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.1 查询待领取作业工单列表
//				            Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryReceiveWorkCardList.action",
//								params : {
//									operatorid : 800109,
//									searchJson : ',"rdpIDX":"8a828464490303dc01490834ad110280"'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.2 查询待处理作业工单列表
//				            Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandlerWorkCardList.action",
//								params : {
//									operatorid : 800109,
//									searchJson : ',"rdpIDX":"8a828464490303dc0149085016e20307"'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.3 领取作业工单
//				            Ext.Ajax.request({
//								url : ctx + "/workCardProc!updateReceiveWorkCard.action",
//								params : {
//									operatorid : 800109,
//									idxs : '2F3D76E2AD294F34B9778DD629D7631D,C19C7D2EC4BC4466B2B53EA2C1692F5B'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.4 领取全部作业工单
//				            Ext.Ajax.request({
//								url : ctx + "/workCardProc!updateReceiveAllWorkCard.action",
//								params : {
//									operatorid : 800109,
//									searchJson : '{"rdpIDX":"8a8284fc48c43e2e0148c44390f90001"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.5 查询作业工单信息
//				            Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryWorkCardInfo.action",
//								params : {
//									idx: '8484FB70BCE34F3BB8E838E33ABB3B66'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.6 查询未处理作业任务列表
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandlerWorkTaskList.action",
//								params : {
//									operatorid : 800109,
//									workCardIdx : '1D5705067B1F4D47813E65A29310382A'	
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.6 查询已处理作业任务列表
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandledWorkTaskList.action",
//								params : {
//									operatorid : 800109,
//									workCardIdx : '1D5705067B1F4D47813E65A29310382A'	
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.8 查询检测项列表
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryDetectResultList.action",
//								params : {
//									workTaskIdx: "005D1675363843DD9D4776E72D00C02C"	
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.9 保存检测项并完成作业任务
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!saveDetectResultAndWorkTask.action",
//								params : {
//									operatorid : 800109,
//									data : Ext.util.JSON.encode(["BDB473D08A084FB388EB4D9329541BDF,12"]),
//									taskIdx : 'F580BCFE54E645F48B390323FCE78B40',
//									result : '222',
//									remarks : '333'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.10 保存检测项
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!saveDetectResult.action",
//								params : {
//									operatorid : 800109,
//									idx : 'EECBB8426E7E415B80285B2DB76231B5',
//									value : '222'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.11 完工、批量完工
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!completeWorkCard.action",
//								params : {
//									operatorid : 800109,
//									idxs : '39CA80FF8F0F4D2E88AF558722C27EF1'/*,
//									judgment : '222',
//									isOvertime : '',
//									overtime : '',
//									remarks : ''*/
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.12 全部完工
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!completeAllWorkCard.action",
//								params : {
//									operatorid : 800109/*,
//									judgment : '222',
//									overtime : ''*/
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.13 查询生产任务单【待处理】
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandlerRdpList.action",
//								params : {
//									operatorid : 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							
							//作业工单处理-3.13 查询生产任务单【待领取】
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryReceiveRdpList.action",
//								params : {
//									operatorid : 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							
							//作业工单处理-3.15 查询工艺节点列表【待处理】
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandlerTecNodeList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464490303dc0149085016e20307'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业工单处理-3.18 查询检修活动列表【待处理】
//							Ext.Ajax.request({
//								url : ctx + "/workCardProc!queryHandlerRepairActivityList.action",
//								params : {
//									operatorid : 800109,
//									rdpIdx : '8a828464490303dc0149085016e20307'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票信息查询-5.1 根据查询条件查询提票
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeQuery!queryFaultNoticeList.action",
//								params : {
//									searchJson : '{"worker_id":"109"}' 
////									"type":"#[t]#$[SF]$10","train_type_idx":"#[t]#$[SF]$207","train_no":"#[t]#$[SF]$0003","fault_fix_place_idx":"#[t]#$[SF]$207ZCG9000","repair_team":"作业班组：#[t]#$[SF]$1",
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票信息查询-5.2 查询提票类型业务字典列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeQuery!queryFaultTypeList.action",								
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//车型查询-6.1 查询承修车型列表
//							Ext.Ajax.request({
//								url : ctx + "/trainType!queryUndertakeTrainTypeList.action",								
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//车号查询-6.1 查询车号列表
//							Ext.Ajax.request({
//								url : ctx + "/jczlTrain!queryTrainNoByTrainTypeList.action",		
//								params : {
//									trainTypeIdx: '207'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//组成型号查询-4.2 根据车型车号查询组成位置根节点列表
//							Ext.Ajax.request({
//								url : ctx + "/buildUpTypeQuery!queryRootBuildByTrain.action",		
//								params : {
//									trainTypeIDX: '207',
//									trainNo: '0003'
//									
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//组成型号查询-4.3 根据组成型号查询组成位置根节点列表
//							Ext.Ajax.request({
//								url : ctx + "/buildUpTypeQuery!queryRootBuildByBuildType.action",		
//								params : {
//									buildUpTypeIDX: '207'
//									
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//组成型号查询-4.1 查询组成树
//							Ext.Ajax.request({
//								url : ctx + "/buildUpTypeQuery!queryBuildPlaceList.action",		
//								params : {
////									parentIDX: 'ROOT_0',
////									partsBuildUpTypeIdx: '207'
//									
////									parentIDX: '207ZCG9000',
////									partsBuildUpTypeIdx:'207'
//									
//									parentIDX:'207XHG0203',
//									partsBuildUpTypeIdx:'207XHG0203'
////									isVirtual: 'false'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//作业班组查询-8.1 查询段机构树列表
//							Ext.Ajax.request({
//								url : ctx + "/omOrganizationSelect!querySegmentOrgTreeList.action",		
//								params : {
//									orgid: '-1',//(获取根节点时为-1)
//									operatorid:800109
//									
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票处理-9.1 查询提票处理列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeProc!queryFaultNoticeProcList.action",		
//								params : {
//									uid: 'wangqian',
//									uname:'王谦',
//									queryString : '{"rdpIDX":"4028868147c114440147c23e214d002e"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票处理-9.2 点击完工时-获取提票信息
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeProc!queryFaultInfo.action",		
//								params : {
//									faultIdx: '8a8284fc48bf0d300148bf58ee170007'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//故障现象查询-10.1 查询故障现象列表
//							Ext.Ajax.request({
//								url : ctx + "/placeFault!findFault.action",		
////								params : {
////									buildUpPlaceIdx: '8a8284fc48bf0d300148bf58ee170007'
////								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//故障现象处理方法查询-11.1 查询故障现象列表
//							Ext.Ajax.request({
//								url : ctx + "/faultMethod!pageList.action",
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票调度派工-5.1 查询已派工列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeDdpg!queryDispatchedList.action",
//								params : {
//									operatorid: 800109,
//									searchJson: '{"type":"20","train_type_shortname":"SS4B"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票调度派工-5.4 提票调度派工处理 
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeDdpg!saveDdpg.action",
//								params : {
//									operatorid: 800024,
//									idxs: '8a8284fc4916b523014917037d0c0001',
//									orgId:'106',
//									orgname:'电检一组',
//									orgseq:'.0.1.106.'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票调度派工-5.3 查询提票调度派工班组列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeDdpg!queryFaultDdpgTeamList.action",
//								params : {
//									operatorid: 800024
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票工长派工-6.1 查询已派工列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeGzpg!queryDispatchedList.action",
//								params : {
//									operatorid: 800109,
//									searchJson: '{"type":"20","train_type_shortname":"SS4B"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票工长派工-6.2 查询未派工列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeGzpg!queryNotDispatchedList.action",
//								params : {
//									operatorid: 800109,
//									searchJson: '{"type":"20","train_no":"0221"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票工长派工-6.3 提票工长派工处理
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeGzpg!saveGzpg.action",
//								params : {
//									operatorid: 800109,
//									empids: '109,120',
//									faultIdx: '4028868148566caf01485adc94940a21,4028868148566caf01485adc961b0a25'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//检查提票-7.1 保存提票并实例化提票
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeJctp!saveFaultNoticeAndInstanceFlow.action",
//								params : {
//									operatorid: 800109,
//									faultNoticeDatas: '[{   "discoverer": "109",   "trainTypeIDX": "207",   "trainNo": "0004",   "trainTypeShortName": "SS4B",   "type": "20",   "faultID": "GZXX0153",   "faultName": "不动作",   "faultDesc": "不动作",   "faultFixPlaceIDX": "207ZCC0072",   "fixPlaceFullCode": "207ZCG9000B/207ZCG0001B/207ZCC0069B/207ZCC0070/207ZCC0072",   "fixPlaceFullName": "SS4B/B节/司机室/刮雨器/电动刮雨器(SS4B)/司机侧",   "partsAccountIDX": "",   "partsTypeIDX": "8aaae0463eda4b9a013edb42ff200052",   "partsName": "",   "specificationModel": "",   "nameplateNo": "",   "partsNo": "",   "noticePersonId": "800109",   "noticePersonName": "王谦",   "faultNoticeTime": "2014-10-16 16:10",   "faultOccurDate": "2014-10-16 16:10",   "discovererName": "王谦",   "professionalTypeIdx": "",   "professionalTypeName": "刮雨器",   "faultKind": "",   "faultKindName": "" }]'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.1 查询必检列表
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!queryBjList.action",
//								params : {
//									uid: 'wangqian',
//									uname: '王谦',
//									queryString: '{"rdpIDX":"8a8284644862c24d01486324a9b40001"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.2 查询抽检列表
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!queryCjList.action",
//								params : {
//									uid: 'hongli',
//									uname: '洪利'/*,
//									queryString: '{"rdpIDX":"8a8284644862c24d01486324a9b40001"}'*/
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.3 批量指派互检
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveReassignNext.action",
//								params : {
//									operatorid: 800109,
//									json: '[{"workItemIdStr":"hjzp","workcardId":"000B9149F71D4CCC9F5E1FE94A4004A1","workItemID":"000B9149F71D4CCC9F5E1FE94A4004A1","processInstIDStr":"1413184346347"}]',
//									empids:'109,120'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.4 全部指派互检
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveAllReassignNext.action",
//								params : {
//									operatorid: 800109,
//									queryString: '{"rdpIDX":"8a828464490303dc0149085016e20307","workItemName":"","taskDepict":""}',
//									empids:'109,120'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.5 单条处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveQualityControlCheckInfo.action",
//								params : {
//									jsonData: '{"checkPersonName": "王谦","checkPersonIdx": "800109", "checkTime": "2014-10-17 10:10", "remarks": "111"}',
//									processInstID: '1413184199270',
//									workCardId:'00879A47127B4532940D23749C303A7D',
//									workItemID:'hj'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.6 批量处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveBatchQualityControlCheckInfo.action",
//								params : {
//									checkInfoJson: '{"checkPersonName": "王谦","checkPersonIdx": "800109", "checkTime": "2014-10-17 10:10", "remarks": "111"}',
//									taskJson: '[{"workItemIdStr":"hj","workcardId":"01E3AC4BDF884231B0FF9CCCD8E0F8D5","processInstIDStr":"1413184262387"},{"workItemIdStr":"hj","workcardId":"02D7C30EDBC84C749408735FC1DCC8E6","processInstIDStr":"1413184256615"}]'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.7 全部处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveAllQualityControlCheckInfo.action",
//								params : {
//									operatorid: 800109,
//									queryString: '{"rdpIDX":"8a828464490303dc01490834ad110280","workItemName":"","taskDepict":""}',
//									remarks:'222'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（神木）-8.8 查询生产任务单列表
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!queryRdpList.action",
//								params : {
//									operatorid: 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查-9.1 查询必检列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!queryBjList.action",
//								params : {
//									uid: 'wangqian',
//									uname: '王谦'//,
////									queryString: '{"rdpIDX":"8a8284644862c24d01486324a9b40001"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查-9.2 查询抽检列表
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!queryCjList.action",
//								params : {
//									uid: 'hongli',
//									uname: '洪利',
//									queryString: '{"rdpIDX":"8a8284644862c24d01486324a9b40001"}'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查 -9.3 提票质量检查任务确认，保存检验信息
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!saveFaultNoticeQCInfo.action",
//								params : {
//									operatorid: 800109,
//									disposeOpinionData: '{"businessIDX":"8a828464490303dc014907f66546027f","processInstID":"207816","activityInstID":"1894009","workItemID":"2528836","workItemName":"提票-工长必检","disposeTime":"2014-10-17 11:45:28"}',
//									token: 'TpQuality'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查 -9.4 批量处理提票质量检查任务确认，保存检验信息
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!saveBatchFaultNoticeQCInfo.action",
//								params : {
//									operatorid: 800109,
//									disposeOpinionDatas: '[{"businessIDX":"8a8284fc48fd0d900148fd1014710002","processInstID":"207701","activityInstID":"1893704","workItemID":"2528741","workItemName":"提票-工长必检","disposeTime":"2014-10-17 11:45:28"},{"businessIDX":"8a8284fc48bf0d300148bf3e534d0001","processInstID":"207621","activityInstID":"1893484","workItemID":"2528641","workItemName":"提票-工长必检","disposeTime":"2014-10-17 11:45:28"}]',
//									token: 'TpQuality'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查 -9.5 全部处理提票质量检查（必检）
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!saveAllBatchFaultNoticeQCInfo.action",
//								params : {
//									operatorid: 800109/*,
//									disposeIdea: '',
//									queryString: ''*/
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查 -9.6 查询生产任务单列表（提票必检）
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!queryBjRdpList.action",
//								params : {
//									operatorid: 800109
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//提票质量检查 -9.7 查询生产任务单列表（提票抽检）
//							Ext.Ajax.request({
//								url : ctx + "/faultNoticeZljc!queryCjRdpList.action",
//								params : {
//									operatorid: 800557
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查(产品化) -10.1 查询必检列表
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!queryBjList.action",
//								params : {
//									uid: 1572,
//									uname: '王大鹏'/*,
//									queryString: ''*/
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查(产品化) -10.5 单条处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveQualityControlCheckInfo.action",
//								params : {
//									jsonData: '{"checkPersonName": "王大鹏","checkPersonIdx": "963", "checkTime": "2014-10-17 10:10", "remarks": "111"}',
//									processInstID: '16454',
//									workCardId:'CC2636E213784F24AFD9C7565A405F48',
//									workItemID:'9229'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查(产品化) -10.6 批量处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveBatchQualityControlCheckInfo.action",
//								params : {
//									checkInfoJson: '{"checkPersonName": "宋海泉","checkPersonIdx": "1230", "checkTime": "2014-10-17 10:10", "remarks": "111"}',
//									taskJson: '[{"workItemIdStr":"9188","workcardId":"CC2636E213784F24AFD9C7565A405F48","processInstIDStr":"16454"}]'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查(产品化) -10.7 全部处理质量检查信息
//							Ext.Ajax.request({
//								url : ctx + "/zljcProc!saveAllQualityControlCheckInfo.action",
//								params : {
//									operatorid: 963,
////									queryString: '{"rdpIDX":"8a828464490303dc01490834ad110280","workItemName":"","taskDepict":""}',
//									remarks:'222'
//								},
//								success : function(response, options) {
//									var result = Ext.util.JSON.decode(response.responseText);
//									if (!Ext.isEmpty(result)) {								
//										alertSuccess();
//									} 
//								}
//							});
							//质量检查（产品化）-10.8 查询生产任务单列表（必检）
							Ext.Ajax.request({
								url : ctx + "/zljcProc!queryBjRdpList.action",
								params : {
									operatorid: 1230
								},
								success : function(response, options) {
									var result = Ext.util.JSON.decode(response.responseText);
									if (!Ext.isEmpty(result)) {								
										alertSuccess();
									} 
								}
							});
	        		    }
	        		    
		    }],    	       
		    items: [{
		        xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
		        items: [
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:testComponent.labelWidth,
		            columnWidth: .5, 
		            items: [						
						{ id: 'ProfessionalType_comboTree_Id', xtype: 'ProfessionalType_comboTree', fieldLabel: i18n.TestComponent.typpeSelect, 
						  hiddenName: 'professionalType', returnField: [{widgetId:"textId",propertyName:"text"}], selectNodeModel: 'all',
						  width: testComponent.fieldWidth },						
						{ id: 'OmOrganizationCustom_comboTree_Id', xtype: 'OmOrganizationCustom_comboTree', fieldLabel: i18n.TestComponent.customControl,
						  hiddenName: 'orgId', returnField: [{widgetId:"textId",propertyName:"orgname"}], selectNodeModel:'all',							   
						  //queryHql: "from OmOrganization where 1=1 and status = 'running' and orgdegree='oversea'",
						  queryHql: '[degree]tream',
						  width: testComponent.fieldWidth, editable: true
						}, 
						{ id: 'trainTypeId', xtype: 'Base_combo',	fieldLabel: i18n.TestComponent.paginationList,
						  hiddenName: 'trainType', returnField: [{widgetId:"textId",propertyName:"typeName"}],
						  displayField: 'typeName', valueField: 'typeID',
						  entity: 'com.yunda.jx.base.jcgy.entity.TrainType', 
						  fields: ["typeID","typeName"],
						  pageSize: 20, minListWidth: 200, width: testComponent.fieldWidth, editable: true },
						 
						{ id: 'BuildUpType_comboTree_Id', xtype: 'BuildUpType_comboTree', fieldLabel: i18n.TestComponent.locationSelect, configType: '20',
						  partsBuildUpTypeIdx: '8a9309943e872e2f013e87b6999b000f', partsBuildUpTypeName: 'HXN5',
						  hiddenName: 'buildUpTypeIDX', returnField: [{widgetId:"textId",propertyName:"fixPlaceFullName"}], selectNodeModel: 'all',
						  width: testComponent.fieldWidth },
						{ id: 'textId', name: 'text', fieldLabel:i18n.TestComponent.echoText, width: testComponent.fieldWidth },
//						{ id: 'textId', name: 'text', fieldLabel: '回显文本', width: testComponent.fieldWidth },
						{ id: 'Bureau_comboTree_Id', xtype: 'BureauSelect_comboTree', fieldLabel: i18n.TestComponent.belongBSelect,
						  hiddenName: 'orgId', returnField: [{widgetId:"textId",propertyName:"text"}], selectNodeModel: 'leaf',						  
						  width: testComponent.fieldWidth,
						  listeners : {
							  	"select" : function() {
							  		Ext.getCmp("DeportSelect_comboTree_Id").orgid = this.getValue();
							  		Ext.getCmp("DeportSelect_comboTree_Id").orgname = this.lastSelectionText;
							  		//Ext.getCmp("DeportSelect_comboTree_Id").queryHql = "from JgyjcDeport where attribute = '1' and ownBureau = '" + this.getValue() + "' order by dId ";
							  	}
							  }
						  },
						  { id: 'Base_comboTree_Id', xtype: 'Base_comboTree', fieldLabel:i18n.TestComponent.dropDTreeselect, 
						  hiddenName: 'professionalType1', returnField: [{widgetId:"textId",propertyName:"text"}], selectNodeModel: 'all',
						  business: 'professionalType', rootText:i18n.TestComponent.profession,valueField:'professionalTypeID',displayField:'professionalTypeID',
						  width: testComponent.fieldWidth },
						  { id: 'Base_multyComboTree_Id', xtype: 'Base_multyComboTree', fieldLabel: i18n.TestComponent.dropDTreeselect, 
						  hiddenName: 'professionalType2', returnField: [{widgetId:"textId",propertyName:"text"}], selectNodeModel: 'all',
						  business: 'professionalType', rootText:i18n.TestComponent.profession,valueField:'professionalTypeID',displayField:'professionalTypeID',
						  width: testComponent.fieldWidth },
						  { id: 'OmOrganizationWin_Id', xtype: 'OmOrganization_Win', fieldLabel:i18n.TestComponent.organSelct,
						  hiddenName: 'orgId', returnField: [{widgetId:"textId",propertyName:"orgname"}],
						  rootId: 0, rootText:i18n.TestComponent.railwayCorporation, displayField: 'orgcode',
						  width: testComponent.fieldWidth, editable: true
						},
						{
							xtype:"PartsTypeTreeSelect",fieldLabel:i18n.TestComponent.accessoriesModel,
						  	hiddenName: 'orgId', editable:false,
						  	returnFn: testComponent.callReturnFn
						},
						{
							xtype:"PartsTypeTreeSelect",fieldLabel: i18n.TestComponent.accessoriesModel,
						  	hiddenName: 'orgId', editable:false,
						  	returnFn: testComponent.callReturnFn2
						},
						{name:'nodeCaseName', fieldLabel:i18n.TestComponent.process, xtype:'FactorTrainSelect', editable:false, width:testComponent.fieldWidth, allowBlank :false, returnFn:testComponent.callReturnFn1 },
						{
							xtype:"TrainTypeTreeSelect",fieldLabel:i18n.TestComponent.repairing,
						  	hiddenName: 'trainType', editable:false,
						  	returnFn: testComponent.callReturnFn3
						},
						{ xtype: 'Base_comboTree',hiddenName: 'dictType', //isExpandAll: true,
						  fieldLabel:i18n.TestComponent.dataDicDown,returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'exceptRoot',
						  treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: i18n.TestComponent.dataDic, queryParams: {'dicttypeid':'PJWZ_PARTS_ACCOUNT_STATUS'},
						  rootId: '0101', rootText: i18n.TestComponent.toBeRepaired
						 },
						{ xtype: 'Base_multyComboTree',hiddenName: 'dictType', //isExpandAll: true,
						  fieldLabel:i18n.TestComponent.dataDicDownMul,returnField:[{widgetId:"textId",propertyName:"text"}],selectNodeModel:'exceptRoot',
						  treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: i18n.TestComponent.dataDic, queryParams: {'dicttypeid':'PJWZ_PARTS_ACCOUNT_STATUS'}
						 },
						 {
							xtype:"PartsExtendNoSelect",fieldLabel: i18n.TestComponent.extensionNumber,
						  	hiddenName: 'extendNo', editable:false,
						  	partsTypeIDX: "100676",
						  	formColNum: 2,
						  	returnFn: testComponent.callReturnFn4
						  	//fields: Ext.getCmp("textId").value
						},
						{ id: 'OmEmployee_MultSelectWin_Id',xtype: 'OmEmployee_MultSelectWin', fieldLabel:i18n.TestComponent.peopleSelect,
						  hiddenName: 'omEmployee', displayField:'empname', valueField: 'empid',
						  returnField :[{widgetId: "textId", propertyName: "empid"}],
						  editable: false, width: testComponent.fieldWidth}
		            ]		            
		        },
		        {
		            baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:testComponent.labelWidth,
		            columnWidth: .5, 
		            items: [
		            	{ id: 'EosDictEntry_combo_Id', xtype: 'EosDictEntry_combo', fieldLabel:i18n.TestComponent.dataDicChoiceCon,
						  hiddenName: 'eosDictEntry', displayField: 'dictname', valueField: 'dictid',
						  returnField :[{widgetId: "textId", propertyName: "dictname"}],
						  dicttypeid:'PJWZ_Parts_Account_Turnover_Status', 		
						  width:testComponent.fieldWidth},
						{ id: 'OmEmployee_SelectWin_Id',xtype: 'OmEmployee_SelectWin', fieldLabel: i18n.TestComponent.peopleSelect = i18n.TestComponent.peopleSelect,
						  hiddenName: 'omEmployee', displayField:'empname', valueField: 'empid',
						  returnField :[{widgetId: "textId", propertyName: "empid"}],
						  editable: false, width: testComponent.fieldWidth},
						{ id: 'GyjcFactory_SelectWin_Id',xtype: 'GyjcFactory_SelectWin', fieldLabel :i18n.TestComponent.productorSelect,
						  hiddenName: 'gyjcFactory', returnField: [{widgetId: "textId", propertyName: "fName"}], 
						  editable: false, //queryHql:"from GyjcFactory ",
						  width:testComponent.fieldWidth		
						},
						{ id: 'PartsAccount_SelectWin_Id', xtype: 'PartsAccount_SelectWin', fieldLabel :i18n.TestComponent.exchangeInfSelect,
						  hiddenName: 'partsAccount', returnField: [{widgetId: "textId", propertyName: "partsName"}],
						  editable: false, queryHql: "from PartsAccount where 1=1",
						  width: testComponent.fieldWidth							
						},
				        { id: 'PartsTypeAndQuota_SelectWin_Id', xtype: 'PartsTypeAndQuota_SelectWin', fieldLabel : i18n.TestComponent.exchangeModelSelect,
				          hiddenName: 'PartsTypeAndQuota', valueField: 'partsClassIdx', displayField: 'partsName',
				          returnField: [{widgetId: "textId", propertyName: "partsName"}],
				          editable:false, width:testComponent.fieldWidth
				        },
				        { 
				        	xtype: 'compositefield', fieldLabel :i18n.TestComponent.compositionControl, combineErrors: false,
				        	items: [
                           {
                               xtype: 'Base_combo',
                               hiddenName: 'buildUpType', returnField: [{widgetId:"textId",propertyName:"buildUpTypeName"}],
							   displayField: 'buildUpTypeName', valueField: 'idx',
							   entity: 'com.yunda.jx.jxgc.buildupmanage.entity.BuildUpType', 
							   fields: ["idx","buildUpTypeName"],
							   pageSize: 20, minListWidth: 200,
                               width: testComponent.fieldWidth-44,
                               listeners : {
                               		"select" : function(combo,record){
                               			alert(record.data["buildUpTypeName"]);
                               		}
                               }
                           },
                           {
                               xtype: 'button',
                               text:i18n.TestComponent.view,
                               width: 40
                           }]
				        },
//						{ id: "rcIDX", xtype: "combo",	fieldLabel: "下拉分页列表选择控件(修次)",
//						  displayField: "repairtimeName", valueField: "rcIDX",
//						  store: new Ext.data.JsonStore({
//										root:"root", totalProperty:"totalProperty", autoLoad:true,
//										url:ctx + '/baseCombo!pageList.action?entity=com.yunda.jx.jczl.repairbase.entity.RCRT',
//										fields:["rcIDX","repairtimeName"]
//									}),
//						  triggerAction : 'all',
//						  width: testComponent.fieldWidth }
				        { id: 'DeportSelect_comboTree_Id', xtype: 'DeportSelect_comboTree', fieldLabel: i18n.TestComponent.belongSSelect,
						  hiddenName: 'deportId', returnField: [{widgetId:"textId",propertyName:"text"}], selectNodeModel: 'leaf',						  
						  width: testComponent.fieldWidth },
						  {
						  	id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel:i18n.TestComponent.trainNumber,
							hiddenName: "trainNo", 
							displayField: "trainNo", valueField: "trainNo",
							pageSize: 20, minListWidth: 200,
							returnField: [{widgetId:"textId",propertyName:"dShortName"}/*,//配属局ID
							              {widgetId:"bName",propertyName:"bName"},//配属局名称
							              {widgetId:"bShortName",propertyName:"bShortName"},//配属局简称
							              {widgetId:"did",propertyName:"dId"},//配属段ID
							              {widgetId:"dName",propertyName:"dName"},//配属段名称
							              {widgetId:"dShortName2",propertyName:"dShortName"}//配属段简称
*/							              ],
							editable:true,
							allowBlank: false,
							width: testComponent.fieldWidth
							//isCx: "no"有问题
							
						  },
						  {
								id: 'PartsBuild_SelectWin_Id', xtype: 'PartsBuild_SelectWin', 
							    hiddenName: 'buildUpTypeDesc', fieldLabel: i18n.TestComponent.compositionParts,
							    valueField: 'buildUpTypeIdx', displayField: 'buildUpTypeDesc',			    
							    editable: false, width: testComponent.fieldWidth,
							    isSingSelect: true,//单选为true，默认为false多选
							    listeners: {
									"select" : function(r){
										
									}
							    }
						  },
						  {
								fieldLabel: i18n.TestComponent.peopleSelectDown,
								id: 'OmEmpBase_Combo_Id', xtype: 'Om_Emp_combo', 
							    hiddenName: 'empname', 
							    valueField: 'empid', displayField: 'empname',			    
							    width: testComponent.fieldWidth,
							    listeners: {
									"select" : function(me, record, index){
										Ext.Msg.alert("", Ext.encode(record.data));
									}
							    }
						  }
		            ]
		        }
		        ]
		    }]
		});
//		testComponent.form.findByType("PartsTypeTreeSelect")[0].returnFn = testComponent.callReturnFn;
//		testComponent.form.findByType("PartsTypeTreeSelect")[1].returnFn = testComponent.callReturnFn2;
		//testComponent.form.getForm().findField("dictType").tree.on("render", function(){this.expandAll()});//默认全展开
		//设置数据字典选择控件默认显示值
		var dictName = EosDictEntry.getDictname("PJWZ_Parts_Account_Turnover_Status","10");
		//Ext.getCmp("EosDictEntry_combo_Id").setDisplayValue("10",dictName);
		//设置人员选择控件默认显示值
		Ext.getCmp("OmEmployee_SelectWin_Id").setDisplayValue("5507857",i18n.TestComponent.materialLibrarian);
		Ext.getCmp("GyjcFactory_SelectWin_Id").setDisplayValue("0002",i18n.TestComponent.ZiYanICEDepot);
		Ext.getCmp("PartsAccount_SelectWin_Id").setDisplayValue("8a8284e339fd5e85013a00ba8cbf00da",i18n.TestComponent.mainCircuitBreaker);
		Ext.getCmp("PartsTypeAndQuota_SelectWin_Id").setDisplayValue("297eb73439f20d0c0139f270f1e1003a","BVAC.N99D");
		Ext.getCmp("ProfessionalType_comboTree_Id").setDisplayValue("297eb73439f20d0c0139f230fa63002b",i18n.TestComponent.electricalSignalSensor);
		Ext.getCmp("OmOrganizationWin_Id").setDisplayValue("100011",i18n.TestComponent.secondGroupPower);
		Ext.getCmp("Base_comboTree_Id").setDisplayValue("08","08");
//		Ext.getCmp("PartsBuild_SelectWin_Id").win.buildGrid.store.proxy = new Ext.data.HttpProxy({
//				        												url: ctx + "/partsBuildSelect!buildUpTypeList.action" +
//				        													"?typeIDX=161"
//				        											        + "&type=0"});
		Ext.getCmp("PartsBuild_SelectWin_Id").win.buildGrid.store.baseParams = {};
        Ext.getCmp("PartsBuild_SelectWin_Id").win.buildGrid.store.baseParams.typeIDX = 161;//车型id
        Ext.getCmp("PartsBuild_SelectWin_Id").win.buildGrid.store.baseParams.type = 0;//类型： 0 机车 1 配件
        Ext.getCmp("PartsBuild_SelectWin_Id").win.buildGrid.store.load();				
        //testComponent.form.findByType("PartsTypeTreeSelect")[0].setValue("111");
//		Ext.getCmp("OmOrganizationCustom_comboTree_Id").setDisplayValue("100011","牵电二组");
//		Ext.getCmp("warehouseId").setDisplayValue("KF-00001","检修车间材料库房");		
	});