var tree1 = 
	[{
		id:'jcgl',text:'基础管理',leaf:false,children:[
				{id:'yhgl_1',text:'机构人员管理',leaf:true,funcaction:'/frame/yhgl/Organization.jsp'},
				{id:'yhgl_2',text:'工作组管理',leaf:true,funcaction:'/frame/yhgl/WorkGroup.jsp'},
				{id:'yhgl_3',text:'职务管理',leaf:true,funcaction:'/frame/yhgl/WorkDuty.jsp'},
				{id:'yhgl_4',text:'人员管理',leaf:true,funcaction:'/frame/yhgl/EmployeeCtl.jsp'},
				{id:'yhgl_5',text:'业务字典',leaf:true,funcaction:'/frame/yhgl/SysEosDict.jsp'},
				{id:'yhgl_6',text:'操作员管理',leaf:true,funcaction:'/frame/yhgl/OperatorCtl.jsp'},
				{id:'yhgl_7',text:'密码设置',leaf:true,funcaction:'/frame/yhgl/NewPassWord.jsp'},
				{id:'yhgl_8',text:'菜单管理',leaf:true,funcaction:'/frame/yhgl/AcMenu.jsp'},
				{id:'yhgl_9',text:'应用功能管理',leaf:true,funcaction:'/frame/yhgl/SysFunction.jsp'},
				{id:'yhgl_10',text:'系统角色',leaf:true,funcaction:'/frame/yhgl/SysRole.jsp'} 
		]
	},{
		id:'exp', text:'事例演示', leaf:false, children:[
				{id:'component', text:'测试组件', leaf:true, funcaction:'/jsp/jx/test/TestComponent.jsp'}, 
				{id:'测试普通签名', text:'测试普通签名', leaf:true, funcaction:'/jsp/jx/workflow/Sign.jsp'},
				{id:'查看机车信息', text:'查看机车信息(测试页面)', leaf:true, funcaction:'/jsp/twt/workplanmanage/TrainWorkPlanInfo.jsp'},
				{id:'台位绑定工位', text:'台位绑定工位(测试页面)', leaf:true, funcaction:'/jsp/twt/station/BindWorkStation.jsp'},
				{id:'台位绑定传感器', text:'台位绑定传感器(测试页面)', leaf:true, funcaction:'/jsp/twt/station/BindSensor.jsp'},
				{id:'视频监控信息配置', text:'视频监控信息配置', leaf:true, funcaction:'/jsp/twt/twtinfo/SiteVideoNvrConfig.jsp'},
				{id:'摄像头绑定(台位图)', text:'摄像头绑定(台位图)', leaf:true, funcaction:'/jsp/twt/twtinfo/SiteVideoBind.jsp'},
				{id:'林欢CURD测试', text:'林欢CURD测试', leaf:true, funcaction:'/jsp/jx/test/LinHuanTest.jsp'}
		]
	},{
		id:'检修基础管理', text:'检修基础管理', leaf:false, children:[
				{id:'rule', text:'业务编码规则配置', leaf:true, funcaction:'/jsp/jx/config/coderule/CodeRuleConfig.jsp'},
				{id:'功能点消息接收维护', text:'功能点消息接收维护', leaf:true, funcaction:'/frame/baseapp/message/MsgCfgReceive.jsp'},
				{id:'系统配置管理', text:'系统配置管理', leaf:true, funcaction:'/jsp/jxpz/systemconfig/SystemConfig.jsp'},
				{id:'机务检修设备管理', text:'机务检修设备管理', leaf:false, children:[
						{id:'检修设备分类维护', text:'检修设备分类维护', leaf:true, funcaction:'/jsp/jxpz/equipinfo/DeviceType.jsp'},
						{id:'检修设备台账维护', text:'检修设备台账维护', leaf:true, funcaction:'/jsp/jxpz/equipinfo/DeviceInfo.jsp'}
					]
				},
				{id:'报表模板维护', text:'报表模板维护', leaf:true, funcaction:'/frame/report/ReportManage.jsp'},
				{id:'多工作日历', text:'多工作日历', leaf:true, funcaction:'/jsp/baseapp/workcalendar/WorkCalendarInfo.jsp'},
				{id:'Hibernate缓存管理', text:'Hibernate缓存管理', leaf:true, funcaction:'/frame/baseapp/cache/CacheManager.jsp'},
				{id: '站点标识维护', text: '站点标识维护', leaf: true, funcaction: '/jsp/jxpz/workplace/WorkPlace.jsp'},
				{id: '修程对应修次', text: '修程对应修次', leaf: true, funcaction: '/jsp/jxpz/rcrtset/RcRt.jsp'},
				{id: '特殊字符维护', text: '特殊字符维护', leaf: true, funcaction: '/jsp/jxpz/systemchar/SystemChar.jsp'},
				
				{id:'机车检修质量检验项维护', text:'机车检修质量检验项维护', leaf:true, funcaction:'/jsp/jx/jxgc/base/jcqcitemdefine/JCQCItemDefine.jsp'},
				{id:'配件检修质量检查维护', text:'配件检修质量检查维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/qcitemdefine/QCItem.jsp'},
				{id:'提票过程角色配置',text:'提票过程角色配置',leaf:true,funcaction:'/jsp/jx/jxgc/base/tpRoleDefine/FaultTicketRuleDefine.jsp'}
		]
	},{
		id:'生产人员调整管理', text:'生产人员调整管理', leaf:false, children:[
				{id: '临时人员设置', text:'临时人员设置', leaf:true, funcaction:'/jsp/jxpz/temporaryemp/TemporaryEmp.jsp'},
				{id: '临时人员查询', text:'临时人员查询', leaf:true, funcaction:'/jsp/jxpz/temporaryemp/TemporaryEmpSearch.jsp'}
		]
	},{id:'站场综合管理平台', text:'站场综合管理平台', leaf:false, children:[
					{id:'机车出入段管理', text:'机车出入段管理', leaf:false, children:[
						{id:'机车出入段', text:'机车出入段', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccount.jsp'},
						{id:'机车出入段查询', text:'机车出入段查询', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccountSearch.jsp'}
					]},
					{id:'台位图查看', text:'台位图查看', leaf:true, funcaction:'/jsp/twt/silverlight.jsp'},
					{id:'台位图机车状态配置', text:'台位图机车状态配置', leaf:true, funcaction:'/jsp/twt/twtinfo/TrainStatusColors.jsp'},
					{id:'台位图停时分析', text:'台位图停时分析', leaf:true, funcaction:'/jsp/twt/stoptimeanalyse/TwtStopTimeAnalyse.jsp'},
					{id:'视频监控信息配置', text:'视频监控信息配置', leaf:true, funcaction:'/jsp/twt/twtinfo/SiteVideoNvrConfig.jsp'}
		]
	},{
		id:'生产调度', text:'生产调度', leaf:false, children:[
				{id:'机车检修月计划管理', text:'机车检修月计划管理', leaf:false, children:[
					{id:'机车检修月计划编制', text:'机车检修月计划编制', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlan.jsp'},
					{id:'机车检修月计划查询', text:'机车检修月计划查询', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlanSearch.jsp'},
					// 带流程修改
					{id:'机车检修月计划编制（流程）', text:'机车检修月计划编制（流程）', leaf:true, funcaction:'/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlan.jsp'},
					{id:'机车检修月计划查询（流程）', text:'机车检修月计划查询（流程）', leaf:true, funcaction:'/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlanSearch.jsp'},
					{id:'机车检修月计划待办（流程）', text:'机车检修月计划待办（流程）', leaf:true, funcaction:'/jsp/jx/scdd/enforceplanSnaker/TrainEnforcePlanTaskTodo.jsp'}
				]},
				{id:'机车检修年计划管理', text:'机车检修年计划管理', leaf:false, children:[
					{id:'机车检修年计划', text:'机车检修年计划', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainYearPlan.jsp'}
				]},
				{id:'传感器管理', text:'传感器管理', leaf:false, children:[
					{id:'传感器注册', text:'传感器注册', leaf:true, funcaction:'/jsp/twt/sensor/TWTSensor.jsp'},
					{id:'台位信息查询', text:'台位信息查询', leaf:true, funcaction:'/jsp/twt/sensor/TWTSiteStationSearch.jsp'}
				]},
				{id:'报表', text:'报表', leaf:false, children:[
					{id:'二年检生产报表', text:'二年检生产报表', leaf:true, funcaction:'/jsp/jx/scdd/report/ScddBiyearlyCheckProduce.jsp'},
					{id:'二年检统计报表', text:'二年检统计报表', leaf:true, funcaction:'/jsp/jx/scdd/report/ScddBiyearlyCheckStat.jsp'},
					{id:'承修机车动态报表', text:'承修机车动态报表', leaf:true, funcaction:'/jsp/jx/scdd/report/ScddCheckTrainDynamic.jsp'},
					{id:'机车在修情况进度表', text:'机车在修情况进度表', leaf:true, funcaction:'/jsp/jx/scdd/report/ScddTrainProgress.jsp'},
					{id:'机车台位停时查询', text:'机车台位停时查询', leaf:true, funcaction:'/jsp/jx/scdd/report/workstationYS.jsp'},
					{id:'机车计划入段离段时间查询', text:'机车计划入段离段时间查询', leaf:true, funcaction:'/jsp/jx/scdd/report/EnforcePlanPlanStartEnd.jsp'},
					{id:'机车计划日完成统计', text:'机车计划日完成统计', leaf:true, funcaction:'/jsp/jx/scdd/report/PlanTodayStatistics.jsp'}
				]},
					{id:'机车检修过程管理', text:'机车检修过程管理', leaf:false, children:[
						{id:'机车检修作业流程维护',text:'机车检修作业流程维护',leaf:true,funcaction:'/jsp/jx/jxgc/processdef/JobProcessDef.jsp'},
						{id:'机车检修作业流程维护(新)',text:'机车检修作业流程维护(新)',leaf:true,funcaction:'/jsp/jx/jxgc/processdefnew/JobProcessDefNew.jsp'},
						{id:'机车检修作业计划编制',text:'机车检修作业计划编制',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/VTrainWorkPlan.jsp'},
						{id:'机车检修作业计划编制(不按工期)',text:'机车检修作业计划编制(不按工期)',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/VTrainWorkPlan2.jsp'},
						{id:'机车检修作业计划查询',text:'机车检修作业计划查询',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/VTrainWorkPlanSearch.jsp'},
						{id:'机车检修台位占用计划', text:'机车检修台位占用计划', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/VTrainStationPlan.jsp'},
			        {id:'机车检修作业编辑（新）', text:'机车检修作业编辑（新）', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplan/TrainWorkPlanEdit.jsp'},
			        {id:'机车检修综合查询', text:'机车检修综合查询', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanquery/TrainWorkPlanQuery.jsp'}
					]},
					{id:'走行公里维护', text:'走行公里维护', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/runningkm/running_km.jsp'},
					{id:'检修标准', text:'检修标准', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/jxstandard/repair_standard.jsp'},
					{id:'检修计划统计', text:'检修计划统计', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/planstatistics/plan_statistics.jsp'},
					{id:'检修计划编制', text:'检修计划编制', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/planning/planning.jsp'},
					{id:'试运计划', text:'试运计划', leaf:true, funcaction:'/jsp/jx/scdd/report/syNodePlan.jsp'},
					{id:'年计划兑现率', text:'年计划兑现率', leaf:true, funcaction:'/jsp/jx/scdd/report/YearPlanReport.jsp'}
				]
	},{
		id:'检修过程', text:'检修过程', leaf:false, children:[
				{id:'技术管理', text:'技术管理', leaf:false, children:[
					{id:'机车检修日报', text:'机车检修日报', leaf:true, funcaction:'/jsp/jx/jsgl/jxrb/DailyReport.jsp'}
				]},
				{id:'生产任务管理', text:'生产任务管理', leaf:false, children:[
					{id:'机车检修明细', text:'机车检修明细', leaf:true, funcaction:'/jsp/jx/jxgc/workhis/workplan/TrainWorkPlanSearchHis.jsp'},
					{id:'机车检修记录单查询', text:'机车检修记录单查询', leaf:true, funcaction:'/jsp/jx/jxgc/workhis/print/TrainWorkPlanPrint.jsp'},
					{id:'机车检修作业处理情况查询', text:'机车检修作业处理情况查询', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/workplanview/TrainWorkHandleConditionSearch.jsp'},
					{id:'今日机车动态', text:'今日机车动态', leaf:true, funcaction:'/jsp/jx/jxgc/workhis/search/TrainWorkPlanDailtReport.jsp'}
				]},
				{id:'调度管理', text:'调度管理', leaf:false, children:[
					{id:'机车检修流水线维护', text:'机车检修流水线维护', leaf:true, funcaction:'/jsp/jx/jxgc/dispatchmanage/RepairLine.jsp'},
					{id:'工长默认派工', text:'工长默认派工', leaf:true, funcaction:'/jsp/jx/jxgc/dispatchmanage/Gzforeman.jsp'},
					{id:'工长派工', text:'工长派工', leaf:true, funcaction:'/jsp/jx/scdd/dispatch/foreman.jsp'}
				]},
				{id:'工单管理', text:'工单管理', leaf:false, children:[
					{id:'作业工单', text:'作业工单', leaf:true, funcaction:'/jsp/jx/jxgc/WorkTask/WorkTask.jsp'},
					{id:'机车质量检查',text:'机车质量检查',leaf:true,funcaction:'/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp'},
					{id:'工序延误', text:'工序延误', leaf:true, funcaction:'/jsp/jx/jxgc/WorkTask/WorkSeqPutOff.jsp'},
					{id:'延误查询', text:'延误查询', leaf:true, funcaction:'/jsp/jx/jxgc/WorkTask/WorkSeqPutOffSearch.jsp'}
				]},
				{id:'组成管理', text:'组成管理', leaf:false, children:[
					{id:'机车组成型号维护', text:'机车组成型号维护', leaf:true, funcaction:'/jsp/jx/jxgc/buildupmanage/TrainBuildUp.jsp'},
					{id:'配件组成型号维护', text:'配件组成型号维护', leaf:true, funcaction:'/jsp/jx/jxgc/buildupmanage/PartsBuildUp.jsp'},
					{id:'机车组成维护改', text:'机车组成维护', leaf:true, funcaction:'/jsp/jx/jxgc/buildupmanage/TrainBuildPlace.jsp'},
					{id:'配件组成维护', text:'配件组成维护', leaf:true, funcaction:'/jsp/jx/jxgc/buildupmanage/PartsBuildPlace.jsp'},
					{id:'机车组成查看', text:'机车组成查看', leaf:true, funcaction:'/jsp/jx/jxgc/buildupmanage/query/TrainBuildPlaceQuery.jsp'}
				]},		
				{id:'检修需求管理', text:'检修需求管理', leaf:false, children:[
					{id:'机车检修记录单维护', text:'机车检修记录单维护', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/RepairProject.jsp'},
					{id:'机车作业工单维护', text:'机车作业工单维护', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/workseq/TrainWorkSeq.jsp'},
					{id:'机车检修作业工单处理优化-历史数据迁移', text:'机车检修作业工单处理优化-历史数据迁移', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/ProjectToRecordCard.jsp'}
				]},
				{id:'机车信息维护', text:'机车信息维护', leaf:true, funcaction:'/jsp/jx/jczl/attachmanage/JczlTrainService.jsp'},
				
				{id:'提票管理', text:'提票管理', leaf:false, children:[
					{id:'机车检修提票质量检验项维护', text:'机车检修提票质量检验项维护', leaf:true, funcaction:'/jsp/jx/jxgc/base/tpqcitemdefine/TPQCItemDefine.jsp'},
					{id:'检查提票', text:'检查提票', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicket.jsp'},
					{id:'提票调度派工', text:'提票调度派工', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketDdpg.jsp'},	
					{id:'提票工长派工', text:'提票工长派工', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketGzpg.jsp'},	
					{id:'提票处理', text:'提票处理', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketHandle.jsp'},	
					{id:'提票质量检验', text:'提票质量检验', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp'},
					{id:'提票查询', text:'提票查询', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketSearch.jsp'},	
					{id:'提票分析', text:'提票分析', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketAnasys.jsp'},	
					{id:'提票综合分析', text:'提票综合分析', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketIntegrateAnasys.jsp'}	
				]},
				{id:'承修管理', text:'承修管理', leaf:false, children:[
					{id:'承修车型', text:'承修车型', leaf:true, funcaction:'/jsp/jx/jczl/undertakemanage/UndertakeTrainType.jsp'},
					{id:'可承修机车查询', text:'可承修机车查询', leaf:true, funcaction:'/jsp/jx/jczl/undertakemanage/TrainSearch.jsp'}
				]},	
				{
				id:'配件检修过程管理', text:'配件检修', leaf:false, children:[
					{id:'配件检修基础维护', text:'配件检修基础维护', leaf:false, children:[
							{id:'检修工艺维护', text:'检修工艺维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/tecdefine/TecDefine.jsp'},
							{id:'检修记录单维护', text:'检修记录单维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/recorddefine/Record.jsp'},
							{id:'检修需求维护', text:'检修需求维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/wpdefine/WP.jsp'},
							{id:'配件检修流水线', text:'配件检修流水线', leaf:true, funcaction:'/jsp/jx/pjjx/repairLine/PartsRepairLine.jsp'},
							{id:'机务设备作业工单维护', text:'机务设备作业工单维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/equipdefine/EquipCard.jsp'}
						]},
					{id:'配件检修生产任务管理', text:'配件检修生产任务管理', leaf:false, children:[
							/*{id:'批量生成配件检修任务单', text:'批量生成配件检修任务单', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/PartsRdp.jsp'},*/
							{id:'配件检修作业计划编制', text:'配件检修作业计划编制', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/planedit/parts_rdp_plan_establishment.jsp'},
							{id:'配件检修任务单派工', text:'配件检修任务单派工', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/dispatch/PartsRdpDispatcher.jsp'},
							{id:'配件检修任务处理', text:'配件检修任务处理', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpProcess.jsp'},
							{id:'配件检修质量检验', text:'配件检修质量检验', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheck.jsp'}
						]},
					{id:'修竣配件合格验收', text:'修竣配件合格验收', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/check/PartsRdpCheck.jsp'},
					{id:'配件检修工时分配', text:'配件检修工时分配', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/worktime/PartsRdpSetWorkTime.jsp'},
					{id:'报表打印', text:'报表打印', leaf:false, children:[
							{id:'配件检修记录单结果打印', text:'配件检修记录单结果打印', leaf:true, funcaction:'/jsp/jx/pjjx/recordresultprint/RecordResultPrint.jsp'}
						]},
					{id:'机务设备', text:'机务设备', leaf:true, funcaction:'/jsp/jx/pjjx/device/workorder/work_order.jsp'}
				  ]
			    },{
			    	id:'检修记录单扫描查看', text:'检修记录单扫描查看', leaf:true, funcaction:'/jsp/jx/jxgc/recordscan/RecordScanQuery.jsp'			
			    },{id:'当日生产动态', text:'当日生产动态', leaf:false, children:[
			    	{id:'当日生产动态编辑', text:'当日生产动态编辑', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainWorkPlanTheDynamic.jsp'},
			    	{id:'当日生产动态报表', text:'当日生产动态报表', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainWorkPlanToday.jsp'},
			    	{id:'当日生产动态报表1', text:'当日生产动态报表1', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainWorkPlanToday1.jsp'},
			    	{id:'当日生产动态报表2', text:'当日生产动态报表2', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanthedynamic/TrainWorkPlanToday2.jsp'}

			    ]}
		    ]
	},{
		id:'机车质量', text:'机车质量', leaf:false, children:[				
				{id:'质量控制', text:'质量控制', leaf:false, children:[					
					{id:'质量分析统计', text:'质量分析统计', leaf:true, funcaction:'/jsp/jx/jczl/faultmanager/FaultAnalysis.jsp'}
				]},			
							
				{id:'提票管理质量', text:'提票管理', leaf:false, children:[
					{id:'统计分析', text:'统计分析', leaf:false, children:[
						{id:'按专业月度统计表', text:'按专业月度统计表', leaf:true, funcaction:'/jsp/jx/jczl/faultmanager/statisticalanalysis/proTypeMonth.jsp'},
						{id:'故障提票多次报活统计', text:'故障提票多次报活统计', leaf:true, funcaction:'/jsp/jx/jczl/faultmanager/statisticalanalysis/FaultTicketTimes.jsp'}/*,
						{id:'检修工时统计', text:'检修工时统计', leaf:true, funcaction:'/jsp/jx/jxgc/workdaycount/workDayCount.jsp'}TODO 有问题*/
					]}
				]}
		]
	},{
		id:'技术预研', text:'技术预研', leaf:false, children:[
			{id:'Vis_Demo', text:'Vis_Demo', leaf:true, funcaction:'/vis_demo/VisTest.jsp'}
		]
	},{
		id:'pjwz', text:'配件物资', leaf:false, children:[
				{id:'配件二维码生成', text:'配件二维码生成', leaf:true, funcaction:'/jsp/jx/pjwz/partcode/PartCodeGenerate.jsp'},
				{id:'partsBase', text:'配件基础', leaf:false, children:[
					{id:'partsBase001', text:'配件规格型号维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/partstype/PartsType.jsp'},
					{id:'partsBase011', text:'配件扩展编号维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/partstype/PartsExtendNo.jsp'},
					{id:'partsBase002', text:'专业类型维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/professionaltype/ProfessionalType.jsp'},
					{id:'partsBase003', text:'配件自修目录', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/repairlist/SelfRepairDir.jsp'},
					{id:'partsBase004', text:'配件委外修目录', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/outsourcinglist/OutsourcingDir.jsp'},
					{id:'partsBase005', text:'配件生产厂家维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/madefactory/PartsMadeFactory.jsp'},
					{id:'partsBase006', text:'配件委外厂家维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/outsourcingfactory/PartsOutSourcingFactory.jsp'},
					{id:'partsBase007', text:'配件定量维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/partsquota/PartsQuota.jsp'},
					{id:'partsBase008', text:'配件互换范围维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/exchangeboundary/PartsClassNew.jsp'},
					{id:'partsBase009', text:'库房维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/warehouse/Warehouse.jsp'},
					{id:'partsBase011', text:'常用部门字典维护', leaf:true, funcaction:'/jsp/jxpz/orgdic/OrgDicType.jsp'},
					{id:'partsBase012', text:'常用短语字典维护', leaf:true, funcaction:'/jsp/jxpz/phrasedic/PhraseDicType.jsp'},
					{id:'partsBase013', text:'机车零部件名称', leaf:true, funcaction:'/jsp/jcbm/jcpjzd/JcpjzdBuild.jsp'},
					{id:'partsBase014', text:'配件委外修目录【新】', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/outsourcinglist/OutsourcingCatalog.jsp'}
				]},
				{id:'partsUnloadRegister', text:'下车配件登记', leaf:false, children:[
					{id:'partsUnloadRegister001', text:'下车配件登记', leaf:true, funcaction:'/jsp/jx/pjwz/unloadparts/PartsUnloadRegister.jsp'},
					{id:'下车配件登记（新）', text:'下车配件登记（新）', leaf:true, funcaction:'/jsp/jx/pjwz/unloadpartsnew/PartsUnloadRegister.jsp'},
					{id:'partsUnloadRegister002', text:'下车配件登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterSearch.jsp'},
					{id:'partsUnloadRegister003', text:'下车配件登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterCheck.jsp'},
					{id:'partsUnloadRegister004', text:'下车配件登记web', leaf:true, funcaction:'/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterWeb.jsp'}
				]},
				{id:'partsFixRegister', text:'配件上车登记', leaf:false, children:[
					{id:'partsFixRegister001', text:'配件上车登记', leaf:true, funcaction:'/jsp/jx/pjwz/fixparts/PartsFixRegister.jsp'},
					{id:'配件上车登记（新）', text:'配件上车登记（新）', leaf:true, funcaction:'/jsp/jx/pjwz/fixpartsnew/PartsFixRegisterNew.jsp'},
					{id:'partsFixRegister002', text:'配件上车登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/fixparts/PartsFixRegisterSearch.jsp'},
					{id:'partsFixRegister003', text:'配件上车登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/fixparts/PartsFixRegisterCheck.jsp'}
				]},
				{id:'良好配件登记', text:'良好配件登记', leaf:false, children:[
					{id:'wellpartsregister001', text:'良好配件登记', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsregister/wellPartsRegister.jsp'},
					{id:'wellpartsregister002', text:'良好配件登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsregister/wellPartsRegisterSearch.jsp'},
					{id:'wellpartsregister003', text:'良好配件登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsregister/wellPartsRegisterCheck.jsp'}
				]},
				{id:'退库管理登记', text:'配件退库登记', leaf:false, children:[
					   {id:'wellPartsWhBack001', text:'配件退库登记', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/WellPartsWhBack.jsp'},
					   {id:'wellPartsWhBack002', text:'配件退库登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/WellPartsWhBackSearch.jsp'},
					   {id:'wellPartsWhBack003', text:'配件退库登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/WellPartsWhBackCheck.jsp'}
				]},
				{id:'配件报废登记', text:'配件报废登记', leaf:false, children:[
					{id:'partsScrapRegister001', text:'配件报废登记', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapRegister.jsp'},
					{id:'partsScrapRegister002', text:'配件报废登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapRegisterSearch.jsp'},
					{id:'partsScrapRegister003', text:'配件报废登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapRegisterCheck.jsp'}
				]},
				{id:'配件调出登记', text:'配件调出登记', leaf:false, children:[
					{id:'partsExportRegister001', text:'配件调出登记', leaf:true, funcaction:'/jsp/jx/pjwz/export/PartsExportRegister.jsp'},
					{id:'partsExportRegistert002', text:'配件调出登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/export/PartsExportRegisterSearch.jsp'},
					{id:'partsExportRegistert003', text:'配件调出登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/export/PartsExportRegisterCheck.jsp'}
				]},
				{id:'配件销账登记', text:'配件销账登记', leaf:false, children:[
					{id:'partsCanceRegisterl001', text:'配件销账登记', leaf:true, funcaction:'/jsp/jx/pjwz/partscancel/PartsCancelRegister.jsp'},
					{id:'partsCanceRegisterl002', text:'配件销账登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/partscancel/PartsCancelRegisterSearch.jsp'},
					{id:'partsCanceRegisterl003', text:'配件销账登记确认', leaf:true, funcaction:'/jsp/jx/pjwz/partscancel/PartsCancelRegisterCheck.jsp'}
				]},
				{id:'库存管理', text:'配件库存管理', leaf:false, children:[
					{id:'修竣配件入库管理', text:'修竣配件入库管理', leaf:false, children:[
						{id:'PartsWHRegister001', text:'修竣配件入库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegister.jsp'},
						{id:'PartsWHRegister002', text:'修竣配件入库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegisterSearch.jsp'},
						{id:'PartsWHRegister003', text:'修竣配件入库确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegisterCheck.jsp'}
					]},
					{id:'配件出库管理', text:'配件出库管理', leaf:false, children:[
						{id:'WellPartsExwh001', text:'配件出库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/exwh/WellPartsExwh.jsp'},
						{id:'WellPartsExwh002', text:'配件出库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/exwh/WellPartsExwhSearch.jsp'},
						{id:'WellPartsExwh003', text:'配件出库确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/exwh/WellPartsExwhCheck.jsp'}
					]}
				]},
				{id:'partsSoucing', text:'配件委外', leaf:false, children:[
					{id:'partsSoucing001', text:'配件委外登记', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/partsoutSourcing.jsp'},
					{id:'partsSoucingNew', text:'配件委外登记（新）', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/out/PartsoutSourcing.jsp'},
					{id:'partsSoucing002', text:'配件委外回段', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingBack.jsp'},
					{id:'partsSoucing005', text:'配件委外回段(新)', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/partsoutsourceregister/PartsOutSourcingBackNew.jsp'},
					{id:'partsSoucing003', text:'委外配件入库<作废>', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/partsinstore/partsOutSourcingInStore.jsp'},
					{id:'partsSoucing004', text:'委外配件查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsoutsourcing/partsoutsearch/partsOutSearch.jsp'}
				]},
				{id:'配件交旧领旧管理', text:'配件交旧领旧管理', leaf:false, children:[
					{id:'outPartsWH', text:'下车配件交旧', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartswh/OldPartsWH.jsp'},
					{id:'outPartsWHCheck', text:'下车配件交旧交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartswh/OldPartsWHCheck.jsp'},
					{id:'outPartsWHSearch', text:'下车配件交旧查询', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartswh/OldPartsWHSearch.jsp'},
					{id:'outPartsOutWH', text:'下车配件领旧', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartsoutwh/OldPartsOutWH.jsp'},
					{id:'outPartsOutWHCheck', text:'下车配件领旧交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartsoutwh/OldPartsOutWHCheck.jsp'},
					{id:'outPartsOutWHSearch', text:'下车配件领旧查询', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartsoutwh/OldPartsOutWHSearch.jsp'}
				]},
				{id:'入库管理', text:'配件入库管理', leaf:false, children:[
					{id:'newPartsWH', text:'新配件入库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/newpartswh/NewPartsWH.jsp'},
					{id:'newPartsWHCheck', text:'新配件入库交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/newpartswh/NewPartsWHCheck.jsp'},
					{id:'newPartsWHSearch', text:'新配件入库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/newpartswh/NewPartsWHSearch.jsp'},
					{id:'RepairedPartsWH', text:'修竣配件入库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/RepairedPartsWH.jsp'},
					{id:'RepairedPartsWHCheck', text:'修竣配件入库交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/RepairedPartsWHCheck.jsp'},
					{id:'RepairedPartsWHSearch', text:'修竣配件入库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/RepairedPartsWHSearch.jsp'}
				]},
				{id:'出库管理', text:'配件出库管理', leaf:false, children:[
					{id:'PartsAboardExWh', text:'配件上车领用', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/aboardexwh/PartsAboardExWh.jsp'},
					{id:'配件上配件领用', text:'配件上配件领用', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/partsexwh/PartsExWh.jsp'},
					{id:'PartsAboardExWhCheck', text:'配件上车领用交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/aboardexwh/PartsAboardExWhCheck.jsp'},
					{id:'PartsAboardExWhSearch', text:'配件上车领用查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/aboardexwh/PartsAboardExWhSearch.jsp'},
					{id:'PartsMoveStock', text:'配件移库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/movestock/PartsMoveStock.jsp'},
					{id:'PartsMoveStockCheck', text:'配件移库确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/movestock/PartsMoveStockCheck.jsp'},
					{id:'PartsMoveStockSearch', text:'配件移库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/movestock/PartsMoveStockSearch.jsp'}
				]},
			   {id:'退库管理', text:'配件退库管理', leaf:false, children:[
					   {id:'PartsBackExWh', text:'配件退库新增', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/PartsBackWh.jsp'},
					   {id:'PartsBackExWhCheck', text:'配件退库交接确认', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/PartsBackWhCheck.jsp'},
					   {id:'PartsBackExWhSearch', text:'配件退库单查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/backwh/PartsBackWhDetailSearch.jsp'}
				]},
				{id:'partsScrap', text:'配件报废管理', leaf:false, children:[
					{id:'partsScrap001', text:'配件报废', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapBill.jsp'},
					{id:'partsScrap002', text:'配件报废单查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapBillSearch.jsp'}
				]},
				{id:'partsCancel', text:'配件销账管理', leaf:false, children:[
					{id:'partsCancel001', text:'配件销账', leaf:true, funcaction:'/jsp/jx/pjwz/partscancel/PartsCanceled.jsp'},
					{id:'partsCancel002', text:'配件销账单查询', leaf:true, funcaction:'/jsp/jx/pjwz/partscancel/PartsCanceledSearch.jsp'}
				]},
				{id:'partsLoanRegister', text:'配件借出归还', leaf:false, children:[
					{id:'partsLoanRegister001', text:'配件借出', leaf:true, funcaction:'/jsp/jx/pjwz/loanregister/PartsLoanRegister.jsp'},
					{id:'partsLoanRegister002', text:'配件归还', leaf:true, funcaction:'/jsp/jx/pjwz/loanregister/PartsLoanRegisterBack.jsp'},
					{id:'partsLoanRegister003', text:'配件借出归还查询', leaf:true, funcaction:'/jsp/jx/pjwz/loanregister/PartsLoanRegisterSearch.jsp'}
				]},
				{id:'newParts', text:'新品管理', leaf:false, children:[
					{id:'newParts', text:'新品入库管理', leaf:false, children:[
						{id:'newPartsInWH', text:'新品入库', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/rkgl/NewPartsINWH.jsp'},
						{id:'NewPartsINWHQuery', text:'新品入库查询', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/rkgl/NewPartsINWHQuery.jsp'}
					]},
					{id:'新品出库检验管理', text:'新品出库检验管理', leaf:false, children:[
						{id:'新品出库检验', text:'新品出库检验', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/ckjygl/NewPartsOutWH.jsp'},
						{id:'新品出库检验确认', text:'新品出库检验确认', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/ckjygl/NewPartsOutWHConfirm.jsp'},
						{id:'新品出库检验查询', text:'新品出库检验查询', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/ckjygl/NewPartsOutWHQuery.jsp'}
					]},
					{id:'合格新配件入库', text:'合格新配件入库', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/NewPartsCheckInWH.jsp'},
					{id:'不合格新品返厂管理', text:'不合格新品返厂管理', leaf:false, children:[
						{id:'不合格新品返厂登记', text:'不合格新品返厂登记', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/bhgfcgl/NewPartsBackCheckIn.jsp'},
						{id:'不合格新品返厂统计', text:'不合格新品返厂统计', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/bhgfcgl/NewPartsBackStatistics.jsp'}
					]},
					{id:'新品库存查询', text:'新品库存查询', leaf:true, funcaction:'/jsp/jx/pjwz/newpartsmanage/NewPartsStockQuery.jsp'}
				]},
				/** 
				 * 日期：2014-06-17
				 * 说明：用于测试报表--良好配件库存统计
				 * 日期：2014-06-18
				 * 说明：规范报表jsp页面的源码存放路径；增加测试报表--待修配件库存统计；
				 */
				{id:'partsSearch', text:'配件查询', leaf:false, children:[
					{id:'partsSearch001', text:'配件信息查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountSearch.jsp'},
					{id:'partsSearch007', text:'上车配件信息查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountAboardSearch.jsp'},
					{id:'partsSearch002', text:'良好配件库存统计', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsstock/WellPartsStockStatistics.jsp'},
					{id:'partsSearch003', text:'待修配件库存统计', leaf:true, funcaction:'/jsp/jx/pjwz/oldpartsmanage/oldpartsstock/OldPartsStockStatistics.jsp'},
					{id:'partsSearch004', text:'配件保有量统计', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsQuantityReport.jsp'},
					{id:'partsSearch005', text:'配件周转情况统计', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountReport.jsp'},
					{id:'partsSearch006', text:'大型互换配件周转记录', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/BigExchangePartsAccount.jsp'},
					{id:'partsSearch008', text:'配件检修结果查询', leaf:true, funcaction:'/jsp/jx/pjwz/query/PartsRdpSearch.jsp'},
					{id:'partsSearch009', text:'配件综合查询', leaf:true, funcaction:'/jsp/jx/pjwz/integrateQuery/PartsIntegrateQuery.jsp'}
				]},
				/*,
				  {id:'配件退库', text:'配件退库', leaf:false, children:[
					{id:'配件退库新增', text:'配件退库新增', leaf:true, funcaction:'/jsp/jx/pjwz/pjtk/PJTK.jsp'}
				]}*/
				  {id:'partsZzjh', text:'大部件周转计划管理', leaf:false, children:[
					{id:'partsZzjh001', text:'大部件周转计划', leaf:true, funcaction:'/jsp/jx/pjwz/turnover/PartsZzjh.jsp'}
				]}
		]
	},{
		id:'wlgl', text:'物料管理', leaf:false, children:[
		     {id:'baseManage', text:'基础管理', leaf:false, children:[
					{id:'物料信息维护', text:'物料信息维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/MatTypeList.jsp'},
					{id:'库房与班组关系维护', text:'库房与班组关系维护', leaf:true, funcaction:'/jsp/jx/wlgl/partsBase/WhOrg.jsp'},
					{id:'保有量维护', text:'保有量维护', leaf:true, funcaction:'/jsp/jx/wlgl/partsBase/WhMatQuota.jsp'},
					{id:'常用物料清单', text:'常用物料清单', leaf:true, funcaction:'/jsp/jx/wlgl/partsBase/UsedMat.jsp'}
				]},
				{id:'inWhManage', text:'入库管理<新>', leaf:false, children:[
					{id:'物料入库', text:'物料入库', leaf:true, funcaction:'/jsp/jx/wlgl/inwh/MatInWHNew.jsp'}	
				]},
				{id:'outWhManage', text:'出库管理<新>', leaf:false, children:[
					{id:'物料出库', text:'物料出库', leaf:true, funcaction:'/jsp/jx/wlgl/outwh/MatOutWH.jsp'}
				]},
				{id:'matSearch', text:'物料查询<新>', leaf:false, children:[
					{id:'机车用料消耗记录查询', text:'机车用料消耗记录查询', leaf:true, funcaction:'/jsp/jx/wlgl/expend/MatTrainExpendAccountQuery.jsp'}
				]},
				{id:'moveWhManage', text:'移库管理<新>', leaf:false, children:[
					{id:'物料移入确认', text:'物料移入确认', leaf:true, funcaction:'/jsp/jx/wlgl/movewh/MatMoveInConfirm.jsp'},
					{id:'物料移出退回', text:'物料移出退回', leaf:true, funcaction:'/jsp/jx/wlgl/movewh/MatMoveOutBack.jsp'},
					{id:'库位调整',text:'库位调整',leaf:true,funcaction:'/jsp/jx/wlgl/changewh/WarehouseLocationChange.jsp'}
				]},
				{id:'stockQueryManage', text:'库存管理', leaf:false, children:[
					{id:'库存查询', text:'库存查询', leaf:true, funcaction:'/jsp/jx/wlgl/stockmanage/MatStockQuery.jsp'}
				]},
				{id:'loanWhManage', text:'借料管理', leaf:false, children:[
					{id:'物料借出归还', text:'物料借出归还', leaf:true, funcaction:'/jsp/jx/wlgl/loan/MatLoan.jsp'},
					{id:'借料台账查询', text:'借出台账查询', leaf:true, funcaction:'/jsp/jx/wlgl/loan/MatLoanQuery.jsp'}
				]},
			{id:'stockManage', text:'入库管理', leaf:false, children:[
					{id:'消耗配件入库', text:'消耗配件入库', leaf:true, funcaction:'/jsp/jx/wlgl/instock/MatInWh.jsp'},
					{id:'消耗配件入库查询', text:'消耗配件入库查询', leaf:true, funcaction:'/jsp/jx/wlgl/instock/MatInWhQuery.jsp'}
				]},
		    {id:'outStockManage', text:'出库管理', leaf:false, children:[
					{id:'机车外用料', text:'机车外用料', leaf:true, funcaction:'/jsp/jx/wlgl/outstock/MatOutWHNoTrain.jsp'},
					{id:'机车外用料查询', text:'机车外用料查询', leaf:true, funcaction:'/jsp/jx/wlgl/outstock/MatOutWHNoTrainQuery.jsp'},
					{id:'机车检修用料', text:'机车检修用料', leaf:true, funcaction:'/jsp/jx/wlgl/outstock/MatOutWHTrain.jsp'},
					{id:'机车检修用料查询', text:'机车检修用料查询', leaf:true, funcaction:'/jsp/jx/wlgl/outstock/MatOutWHTrainQuery.jsp'}
				]},
		    {id:'expendManage', text:'机车用料消耗管理', leaf:false, children:[
					{id:'机车用料消耗记录', text:'机车用料消耗记录', leaf:true, funcaction:'/jsp/jx/wlgl/expend/MatTrainExpendAccount.jsp'},
					{id:'机车用料消耗记录查询', text:'机车用料消耗记录查询', leaf:true, funcaction:'/jsp/jx/wlgl/expend/MatTrainExpendAccountQuery.jsp'}
				]},
		    
	
		    {id:'backWhManager', text:'退库管理', leaf:false, children:[
					{id:'退库', text:'退库', leaf:true, funcaction:'/jsp/jx/wlgl/backwh/MatBackWH.jsp'},
					{id:'退库查询', text:'退库查询', leaf:true, funcaction:'/jsp/jx/wlgl/backwh/MatBackWHQuery.jsp'}
				]},
		    {id:'moveStockManage', text:'移库管理', leaf:false, children:[
					{id:'消耗配件移出', text:'消耗配件移出', leaf:true, funcaction:'/jsp/jx/wlgl/movestock/MatMoveStock.jsp'},
					{id:'消耗配件移入', text:'消耗配件移入', leaf:true, funcaction:'/jsp/jx/wlgl/movestock/MatMoveStockMoveIn.jsp'},
					{id:'消耗配件移库查询', text:'消耗配件移库查询', leaf:true, funcaction:'/jsp/jx/wlgl/movestock/MatMoveStockQuery.jsp'}
				]},
		     {id:'matBackSupplyManage', text:'质量反馈单管理', leaf:false, children:[
					{id:'质量反馈单', text:'质量反馈单', leaf:true, funcaction:'/jsp/jx/wlgl/backsupply/MatBackSupplyStation.jsp'},
					{id:'质量反馈单查询', text:'质量反馈单查询', leaf:true, funcaction:'/jsp/jx/wlgl/backsupply/MatBackSupplyStationQuery.jsp'}
				]},
		     {id:'matPlanManage', text:'用料计划管理', leaf:false, children:[
					{id:'用料计划申请', text:'用料计划申请', leaf:true, funcaction:'/jsp/jx/wlgl/planmanage/MatPlan.jsp'},
					{id:'用料计划查询', text:'用料计划查询', leaf:true, funcaction:'/jsp/jx/wlgl/planmanage/MatPlanQuery.jsp'}
				]},
			{id:'大型消耗配件上车', text:'大型消耗配件上车', leaf:true, funcaction:'/jsp/jx/wlgl/aboard/MatAboardTrainAccount.jsp'},
		    {id:'matCheckManage', text:'盘点管理', leaf:false, children:[
					{id:'物料盘盈处理', text:'物料盘盈处理', leaf:true, funcaction:'/jsp/jx/wlgl/matcheck/MatCheckProfit.jsp'},
					{id:'物料盘盈处理查询', text:'物料盘盈处理查询', leaf:true, funcaction:'/jsp/jx/wlgl/matcheck/MatCheckProfitQuery.jsp'},
					{id:'物料盘亏处理', text:'物料盘亏处理', leaf:true, funcaction:'/jsp/jx/wlgl/matcheck/MatCheckLoss.jsp'},
					{id:'物料盘亏处理查询', text:'物料盘亏处理查询', leaf:true, funcaction:'/jsp/jx/wlgl/matcheck/MatCheckLossQuery.jsp'}
				]},
					{id:'机车物料消耗统计查询', text:'机车物料消耗统计查询', leaf:true, funcaction:'/jsp/jx/wlgl/MatTrainExpendAccountQuery.jsp'},
					
					{id:'立体仓库分拣指令', text:'立体仓库分拣指令', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsstock/Taskins.jsp'},
					{id:'机车检修用料消耗统计', text:'机车检修用料消耗统计', leaf:true, funcaction:'/jsp/jx/wlgl/matexpendaccount/trainMatExpendAccount.jsp'}
				
		]
	},{
		id:'整备产品化V3.2', text:'整备产品化V3.2', leaf:false, children:[ 
				
				{id:'机车上砂管理', text:'机车上砂管理', leaf:false, children:[
				     {id:'机车上砂查询', text:'机车上砂查询', leaf:true, funcaction:'/jsp/zb/trainonsand/ZbglSanding.jsp'}
				]}, 
				{id:'机车整备任务管理', text:'机车整备任务管理', leaf:false, children:[
				      {id:'机车整备范围维护', text:'机车整备范围维护', leaf:true, funcaction:'/jsp/zb/zbfw/ZbFw.jsp'},
				      {id:'机车对应整备范围列表', text:'机车对应整备范围列表', leaf:true, funcaction:'/jsp/zb/zbfw/ZbFwTrainCenter.jsp'}
				]}, 
				{id:'机油消耗管理', text:'机油消耗管理', leaf:false, children:[
				      {id:'机油消耗', text:'机油消耗', leaf:true, funcaction:'/jsp/zb/oilconsumption/OilconSumption.jsp'},
				      {id:'机车上油维护', text:'机车上油维护', leaf:true, funcaction:'/jsp/zb/oilconsumption/EngineOil.jsp'}
				 ]},
				{id:'临碎修提票管理', text:'临碎修提票管理', leaf:false, children:[
					  {id:'临碎修提票', text:'临碎修提票', leaf:true, funcaction:'/jsp/zb/tp/ZbglTp.jsp'},
					  {id:'临碎修提票查询', text:'临碎修提票查询', leaf:true, funcaction:'/jsp/zb/tp/ZbglTpSearch.jsp'},
					  {id:'整备按专业月度统计表', text:'整备按专业月度统计表', leaf:true, funcaction:'/jsp/zb/tp/ZbglProTypeMonth.jsp'},
					  {id:'提票跟踪', text:'提票跟踪', leaf:true, funcaction:'/jsp/zb/tp/track/ZbglTpTrack.jsp'},
					  {id:'检查提票', text:'检查提票', leaf:true, funcaction:'/jsp/zb/tp/ZbglTpCheck.jsp'},
					  {id:'台位图显示临碎修提票查询', text:'台位图显示临碎修提票查询', leaf:true, funcaction:'/jsp/zb/tp/TWTZbglTpSearch.jsp'}
				]},
				{id:'临修管理', text:'临修管理', leaf:false, children:[
					  {id:'转临修', text:'转临修', leaf:true, funcaction:'/jsp/zb/rdp/zbbill/ZbglRdpTempRepair.jsp'},
					  {id:'临修调度派工', text:'临修调度派工', leaf:true, funcaction:'/jsp/zb/tp/LxDdpg.jsp'}
				]},
				{id:'整备接车管理', text:'整备接车管理', leaf:false, children:[
					{id:'交接项维护', text:'交接项维护', leaf:true, funcaction:'/jsp/zb/trainhandover/ZbglHoModelItem.jsp'},
					{id:'整备交接操作', text:'整备交接操作', leaf:true, funcaction:'/jsp/zb/trainhandover/ZbglHoCase.jsp'},
					{id:'整备交接查询', text:'整备交接查询', leaf:true, funcaction:'/jsp/zb/trainhandover/ZbglHoCaseSearch.jsp'},
					{id:'整备迎检操作', text:'整备迎检操作', leaf:true, funcaction:'/jsp/zb/trainhandcheck/ZbglHoCase.jsp'},
					{id:'整备迎检查询', text:'整备迎检查询', leaf:true, funcaction:'/jsp/zb/trainhandcheck/ZbglHoCaseSearch.jsp'}
				]},
				{id:'机车保洁管理', text:'机车保洁管理', leaf:false, children:[
					{id:'机车保洁操作', text:'机车保洁操作', leaf:true, funcaction:'/jsp/zb/trainclean/ZbglCleaning.jsp'},
					{id:'机车保洁查询', text:'机车保洁查询', leaf:true, funcaction:'/jsp/zb/trainclean/ZbglCleaningSearch.jsp'}
				]},
				{id:'技术指令及措施', text:'技术指令及措施', leaf:false, children:[
					{id:'技术指令措施维护', text:'技术指令措施维护', leaf:true, funcaction:'/jsp/zb/tecorder/ZbglTecOrder.jsp'}	
				]},
				{id:'普查整治管理', text:'普查整治管理', leaf:false, children:[
					{id:'普查整治计划', text:'普查整治计划', leaf:true, funcaction:'/jsp/zb/pczz/ZbglPczz.jsp'},	
					{id:'普查整治查询', text:'普查整治查询', leaf:true, funcaction:'/jsp/zb/pczz/ZbglPczzSearch.jsp'}
				]},
				{id:'机务设备接口管理', text:'机务设备接口管理', leaf:false, children:[
					{id:'机车检测预警报活', text:'机车检测预警报活', leaf:true, funcaction:'/jsp/zb/trainwarning/ZbglWarning.jsp'}	
				]},
				{id:'机务交验管理', text:'机务交验管理', leaf:false, children:[
					{id:'机车整备合格交验', text:'机车整备合格交验', leaf:true, funcaction:'/jsp/zb/rdp/zbbill/ZbglRdpJY.jsp'}	
				]},
				{id:'机车整备作业进度监控',text:'机车整备作业进度监控',leaf:true,funcaction:'/jsp/zb/rdp/jdjk/ZbglRdpJDJK.jsp'},
				{id:'提票综合统计',text:'提票综合统计',leaf:true,funcaction:'/jsp/zb/tp/TpZhTj.jsp'},
				{id:'提票综合统计1',text:'提票综合统计1',leaf:true,funcaction:'/jsp/zb/tp/TpZhTjNew.jsp'},
				{id:'整备作业编制查看',text:'整备作业编制查看',leaf:true,funcaction:'/jsp/zb/workplanmanage/ZbglRdpQuery.jsp'},					
				{id:'台位图显示单车整备流程',text:'台位图显示单车整备流程',leaf:true,funcaction:'/jsp/zb/workplanmanage/ZbglRdpStation.jsp'},
				{id:'机车入段编辑',text:'机车入段编辑',leaf:true,funcaction:'/jsp/twt/workplanmanage/TrainAccessAccountEdit.jsp'}
		]
	},
	{id:'系统登录日志查询',text:'系统登录日志查询',leaf:true,funcaction:'/frame/yhgl/js/loginlog/LoginLogSearch.jsp'}, 
	{id:'twtHtml5',text:'台位图HTML5',leaf:true,funcaction:'/jsp/twt/html5/index.jsp'},
	{id:'机车构型管理', text:'机车构型管理', leaf:false, children:[ 
	   {id:'机车系统分类维护',text:'机车系统分类维护',leaf:true,funcaction:'/jsp/jcbm/jxctfl/JcxtflBuildWh.jsp'},
	   {id:'机车构型',text:'机车构型',leaf:true,funcaction:'/jsp/jcbm/TrainGxgl.jsp'},
	   {id:'机车构型复制',text:'机车构型复制',leaf:true,funcaction:'/jsp/jcbm/BuildToJcgx.jsp'}
	]},
	{id:'机车履历管理', text:'机车履历管理', leaf:false, children:[ 
	   {id:'机车主要部件定义',text:'机车主要部件定义',leaf:true,funcaction:'/jsp/jx/jcll/TrainRecordDefinition.jsp'},
	   {id:'机车履历编辑',text:'机车履历编辑',leaf:true,funcaction:'/jsp/jx/jcll/TrainRecord.jsp'},
	   {id:'配件履历',text:'配件履历',leaf:true,funcaction:'/jsp/jx/pjll/PartsRecord.jsp'}
	]},
	{id:'技术改造无修程兑现',text:'技术改造无修程兑现',leaf:true,funcaction:'/jsp/jx/jczl/techreformaccount/ReformCash.jsp'}
	,{id:'区间总走行公里维护',text:'区间总走行公里维护',leaf:true,funcaction:'/jsp/jx/jsgl/jpgl/QJZZXGL.jsp'}
	,{id:'配件详情查询',text:'配件详情查询',leaf:true,funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountInforSearch.jsp'}	
	,{id:'生产计划作业编制（铁盛）',text:'生产计划作业编制（铁盛）',leaf:true,funcaction:'/jsp/jx/jxgc/tiesheng/TrainWorkPlanSaveNode.jsp'}
	,{id:'生产计划作业编制（段调度）',text:'生产计划作业编制（段调度）',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplannew/TrainWorkPlanForFirstNode.jsp'}
	,{id:'生产计划作业编制（车间调度）',text:'生产计划作业编制（车间调度）',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanLeafnode/TrainWorkPlanForLeafNode.jsp'}
	,{id:'生产计划作业流程',text:'生产计划作业流程',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanflowsheet/TrainWorkPlanFlowsheet.jsp'}
	]
	
	
;
