var tree = 
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
        id:'基础', text:'基础', leaf:false, children:[
            {id:'component', text:'测试组件', leaf:true, funcaction:'/jsp/jx/test/TestComponent.jsp'}, 
            {id:'常用部门字典维护', text:'常用部门字典维护', leaf:true, funcaction:'/jsp/jxpz/orgdic/OrgDicType.jsp'},
            {id:'业务编码规则配置', text:'业务编码规则配置', leaf:true, funcaction:'/jsp/jx/config/coderule/CodeRuleConfig.jsp'},
            {id: '站点标识维护', text: '站点标识维护', leaf: true, funcaction: '/jsp/jxpz/workplace/WorkPlace.jsp'},
            {id:'物料信息维护', text:'物料信息维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/MatTypeList.jsp'},
            {id:'列车运用基础维护', text:'列车运用基础维护', leaf:true, funcaction:'/jsp/freight/base/classmaintain/ClassMaintainDesign.jsp'},
            {id:'乘务检测人员维护', text:'乘务检测人员维护', leaf:true, funcaction:'/jsp/passenger/base/traininspector/TrainInspector.jsp'},
            {id:'车辆系统分类维护',text:'车辆系统分类维护',leaf:true,funcaction:'/jsp/jcbm/jxctfl/JcxtflBuildWh.jsp?vehicleType=10'},
       		{id:'车辆构型',text:'车辆构型',leaf:true,funcaction:'/jsp/jcbm/TrainGxgl.jsp'}
        ]
    },{
        id:'货车运用', text:'货车运用', leaf:false, children:[
                {id:'货车车辆类型', text:'货车车辆类型', leaf:true, funcaction:'/jsp/freight/base/vehicle/TrainVehicleType.jsp?vehicleType=10'},
                {id:'货车车辆信息维护', text:'货车车辆信息维护', leaf:true, funcaction:'/jsp/jx/jczl/attachmanage/JczlTrainService.jsp?vehicleType=10'},
                {id:'货车车辆列检范围维护', text:'货车车辆列检范围维护', leaf:true, funcaction:'/jsp/zb/zbfw/ZbFw.jsp?vehicleType=10'},
                {id:'列检所维护', text:'列检所维护', leaf:true, funcaction:'/jsp/freight/base/trainInspection/TrainInspection.jsp'},
                {id:'货车列检计划', text:'货车列检计划', leaf:true, funcaction:'/jsp/freight/zb/planhc/ZbglRdpPlanDesign.jsp?vehicleType=10'},
                {id:'交接项维护', text:'交接项维护', leaf:true, funcaction:'/jsp/zb/trainhandover/ZbglHoModelItem.jsp'},
                {id:'班次交接', text:'班次交接', leaf:true, funcaction:'/jsp/freight/base/classtransfer/ClassTransfer.jsp'},
                {id:'扣车审批', text:'扣车审批', leaf:true, funcaction:'/jsp/freight/zb/detain/DetainTrain.jsp?vehicleType=10'},
              	{id:'故障登记_货车运用', text:'故障登记_货车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/Gztp.jsp?subSysCode=00&vehicleType=10'},
              	{id:'故障上报处理_货车运用', text:'故障上报处理_货车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/ddcl/Gztpcl.jsp?subSysCode=00&vehicleType=10'},
              	{id:'故障登记查询_货车运用', text:'故障登记查询_货车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/GztpQuery.jsp?subSysCode=00&vehicleType=10'},
              	{id:'质量检验配置_货车运用', text:'质量检验配置_货车运用', leaf:true, funcaction:'/jsp/freight/zb/qualitycontrol/qualitycontrol.jsp?subSysCode=00&vehicleType=10'},
              	{id:'货车巡检信息录入', text:'货车巡检信息录入', leaf:true, funcaction:'/jsp/freight/zb/inspectrecord/TrainInspectRecord.jsp?vehicleType=10'},
              	{id:'货车巡检信息查询', text:'货车巡检信息查询', leaf:true, funcaction:'/jsp/freight/zb/inspectrecord/query/TrainInspectRecord.jsp?vehicleType=10'}
        ]
    },{
        id:'客车运用', text:'客车运用', leaf:false, children:[
            {id:'客车车辆类型', text:'客车车辆类型', leaf:true, funcaction:'/jsp/freight/base/vehicle/TrainVehicleType.jsp?vehicleType=20'},
            {id:'客车车辆信息维护', text:'客车车辆信息维护', leaf:true, funcaction:'/jsp/jx/jczl/attachmanage/JczlTrainService.jsp?vehicleType=20'},
            {id:'客车编组信息维护', text:'客车编组信息维护', leaf:true, funcaction:'/jsp/passenger/marshalling/marshalling.jsp'},
			{id:'交路信息维护', text:'交路信息维护', leaf:true, funcaction:'/jsp/passenger/routing/routing.jsp'},
			{id:'编组需求维护', text:'编组需求维护', leaf:true, funcaction:'/jsp/passenger/traindemand/TrainDemand.jsp'},
			{id:'运行安全信息记录', text:'运行安全信息记录', leaf:true, funcaction:'/jsp/passenger/Operationsafetyrecord/OperationSafetyRecord.jsp'},
			{id:'客车车辆列检范围维护', text:'客车车辆列检范围维护', leaf:true, funcaction:'/jsp/zb/zbfw/ZbFw.jsp?vehicleType=20'},
			{id:'客车列检计划', text:'客车列检计划', leaf:true, funcaction:'/jsp/freight/zb/plankc/ZbglRdpPlanDesign.jsp?vehicleType=20'},
			{id:'运行安全信息记录', text:'运行安全信息记录', leaf:true, funcaction:'/jsp/passenger/Operationsafetyrecord/OperationSafetyRecord.jsp'},
			{id:'人员排班查看', text:'人员排班查看', leaf:true, funcaction:'/jsp/passenger/traindemand/TrainInspectorDemand.jsp'},
			{id:'扣车审批_客车', text:'扣车审批', leaf:true, funcaction:'/jsp/freight/zb/detain/DetainTrain.jsp?vehicleType=20'},
			{id:'故障登记_客车运用', text:'故障登记_客车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/Gztp.jsp?subSysCode=00&vehicleType=20'},
            {id:'故障上报处理_客车运用', text:'故障上报处理_客车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/ddcl/Gztpcl.jsp?subSysCode=00&vehicleType=20'},
            {id:'故障登记查询_客车运用', text:'故障登记查询_客车运用', leaf:true, funcaction:'/jsp/freight/zb/gztp/GztpQuery.jsp?subSysCode=00&vehicleType=20'},
            {id:'质量检验配置_客车运用', text:'质量检验配置_客车运用', leaf:true, funcaction:'/jsp/freight/zb/qualitycontrol/qualitycontrol.jsp?subSysCode=00&vehicleType=20'},
            {id:'客车巡检信息录入', text:'客车巡检信息录入', leaf:true, funcaction:'/jsp/freight/zb/inspectrecord/TrainInspectRecord.jsp?vehicleType=20'},
            {id:'客车巡检信息查询', text:'客车巡检信息查询', leaf:true, funcaction:'/jsp/freight/zb/inspectrecord/query/TrainInspectRecord.jsp?vehicleType=20'}
        ]
    }, {
        id:'技术管理', text:'技术管理', leaf:false, children:[
        	{
        	id: 'jsgl货车', text: '货车', leaf: false, children: [
        		{id:'货车修程对应修次', text:'货车修程对应修次', leaf:true, funcaction:'/jsp/jxpz/rcrtset/RcRt.jsp?vehicleType=10'},
        		{id:'货车检修标准', text:'货车检修标准', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/jxstandard/repair_standard.jsp?vehicleType=10'},
        		{id:'货车修程预警', text:'货车修程预警', leaf:true, funcaction:'/jsp/freight/jx/classwarning/RepairClassWarningHC.jsp'},
        		{id:'货车车辆检修记录单维护', text:'货车车辆检修记录单维护', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/RepairProject.jsp?vehicleType=10'},
        		{id:'货车检修作业流程维护',text:'货车检修作业流程维护',leaf:true,funcaction:'/jsp/jx/jxgc/processdef/JobProcessDef.jsp?vehicleType=10'},
        		{id:'货车车辆状态跟踪',text:'货车车辆状态跟踪',leaf:true,funcaction:'/jsp/freight/base/ztgz/ZtgzDesign.jsp?vehicleType=10'},
        		{id:'货车车辆档案管理', text:'货车车辆档案管理', leaf:true, funcaction:'/jsp/jx/jcll/TrainRecord.jsp?vehicleType=10'}
        	]
        	},
        	{
        	id: 'jsgl客车', text: '客车', leaf: false, children: [
        		{id:'客车修程对应修次', text:'客车修程对应修次', leaf:true, funcaction:'/jsp/jxpz/rcrtset/RcRt.jsp?vehicleType=20'},
        		{id:'客车检修标准', text:'客车检修标准', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/jxstandard/repair_standard.jsp?vehicleType=20'},
        		{id:'客车走行公里', text:'客车走行公里', leaf:true, funcaction:'/jsp/passenger/base/runningkm/Runningkm.jsp'},
        		{id:'客车修程预警', text:'客车修程预警', leaf:true, funcaction:'/jsp/freight/jx/classwarning/RepairClassWarning.jsp'},
        		{id:'客车车辆检修记录单维护', text:'客车车辆检修记录单维护', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/RepairProject.jsp?vehicleType=20'},
        		{id:'客车检修作业流程维护',text:'客车检修作业流程维护',leaf:true,funcaction:'/jsp/jx/jxgc/processdef/JobProcessDef.jsp?vehicleType=20'},
        		{id:'客车车辆状态跟踪',text:'货车车辆状态跟踪',leaf:true,funcaction:'/jsp/freight/base/ztgz/ZtgzDesign.jsp?vehicleType=20'},
        		{id:'客车车辆档案管理', text:'客车车辆档案管理', leaf:true, funcaction:'/jsp/jx/jcll/TrainRecord.jsp?vehicleType=20'}
        	]},        
            {id:'车辆检修流水线维护', text:'车辆检修流水线维护', leaf:true, funcaction:'/jsp/jx/jxgc/dispatchmanage/RepairLine.jsp'},
            {id:'车辆检修质量检验项维护', text:'车辆检修质量检验项维护', leaf:true, funcaction:'/jsp/jx/jxgc/base/jcqcitemdefine/JCQCItemDefine.jsp'},
            {id:'车辆检修提票质量检验项维护', text:'车辆检修提票质量检验项维护', leaf:true, funcaction:'/jsp/jx/jxgc/base/tpqcitemdefine/TPQCItemDefine.jsp'},
            {id:'作业指导书管理', text:'作业指导书管理', leaf:true, funcaction:'/jsp/jx/base/workInstructor/WorkInstructor.jsp'}
        ]
    }, {
        id:'生产调度', text:'生产调度', leaf:false, children:[
         	{
    		 	id: 'scdd货车', text: '货车', leaf: false, children: [
    				{id:'货车检修计划编制', text:'货车检修计划编制', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlan.jsp?vehicleType=10'},
             		{id:'货车检修计划查看', text:'货车检修计划查看', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlanSearch.jsp?vehicleType=10'},
    		 	    {id:'货车车辆出入段', text:'货车车辆出入段', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccount.jsp?vehicleType=10'},
             		{id:'货车出入段查询', text:'货车出入段查询', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccountSearch.jsp?vehicleType=10'},
    		 		{id:'货车车辆检修作业编辑（段调度）', text:'货车车辆检修作业编辑（段调度）', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplannew/TrainWorkPlanForFirstNode.jsp?vehicleType=10'},
    		 		{id:'货车生产计划作业编制（车间调度）',text:'货车生产计划作业编制（车间调度）',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanLeafnode/TrainWorkPlanForLeafNode.jsp?vehicleType=10'},
    		 		{id:'货车生产计划作业流程图',text:'货车生产计划作业流程图',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanflowsheet/TrainWorkPlanFlowsheet.jsp?vehicleType=10'}
    		 	]
    		},
         	{
    		 	id: 'scdd客车', text: '客车', leaf: false, children: [
    				 {id:'客车检修计划编制', text:'客车检修计划编制', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlan.jsp?vehicleType=20'},
		             {id:'客车检修计划查看', text:'客车检修计划查看', leaf:true, funcaction:'/jsp/jx/scdd/enforceplan/TrainEnforcePlanSearch.jsp?vehicleType=20'},
		             {id:'客车车辆出入段', text:'客车车辆出入段', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccount.jsp?vehicleType=20'},
		             {id:'客车出入段查询', text:'客车出入段查询', leaf:true, funcaction:'/jsp/twt/trainaccessaccount/TrainAccessAccountSearch.jsp?vehicleType=20'},
    		 		 {id:'客车车辆检修作业编辑（段调度）', text:'客车车辆检修作业编辑（段调度）', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplannew/TrainWorkPlanForFirstNode.jsp?vehicleType=20'},
    		 		 {id:'客车生产计划作业编制（车间调度）',text:'客车生产计划作业编制（车间调度）',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanLeafnode/TrainWorkPlanForLeafNode.jsp?vehicleType=20'},
    		 		 {id:'客车生产计划作业流程图',text:'客车生产计划作业流程图',leaf:true,funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplanflowsheet/TrainWorkPlanFlowsheet.jsp?vehicleType=20'}
    		 	]
    		},
    		{id:'货车检修流程维护（新）',text:'货车检修流程维护（新）',leaf:true,funcaction:'/jsp/jx/jxgc/processdef/JobProcessDef.jsp?vehicleType=10'},
    		{id:'货车检修作业编辑（新）', text:'货车检修作业编辑（新）', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/trainworkplan/TrainWorkPlanEdit.jsp?vehicleType=10'},
    		{id:'调度命令单申请', text:'调度命令单申请', leaf:true, funcaction:'/jsp/zb/chedule/propose/ScheduleProposeAndApprove.jsp'},
    		{id:'调度命令单审批', text:'调度命令单审批', leaf:true, funcaction:'/jsp/zb/chedule/approve/ScheduleProposeAndApprove.jsp'}
    		
        ]
    }, {
    	id:'检修管理', text:'检修管理', leaf:false, children:[
	    	{
	    		id: 'jxgl货车', text: '货车', leaf: false, children: [
	    			{id:'货车检修提票', text:'货车检修提票', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicket.jsp?vehicleType=10'},
	    			{id:'货车检修提票查询', text:'货车检修提票查询', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketSearch.jsp?vehicleType=10'},
	    			{id:'货车车辆验收', text:'货车车辆验收', leaf:true, funcaction:'/jsp/jx/acceptance/TrainAcceptance.jsp?vehicleType=10'},
	    			{id:'货车检修质量检验',text:'货车检修质量检验',leaf:true,funcaction:'/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp?vehicleType=10'}
/*					{id:'货车检修提票处理', text:'货车检修提票处理', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketHandle.jsp?vehicleType=10'}
	    			{id:'货车检修提票质量检查', text:'货车检修提票质量检查', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp?vehicleType=10'}*/
	    		]
	    	},
	    	{
	    		id: 'jxgl客车', text: '客车', leaf: false, children: [
	    			{id:'客车检修提票', text:'客车检修提票', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicket.jsp?vehicleType=20'},
	    			{id:'客车检修提票查询', text:'客车检修提票查询', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketSearch.jsp?vehicleType=20'},
	    			{id:'客车车辆验收', text:'客车车辆验收', leaf:true, funcaction:'/jsp/jx/acceptance/TrainAcceptance.jsp?vehicleType=20'},
	    			{id:'客车检修质量检验',text:'客车检修质量检验',leaf:true,funcaction:'/jsp/jx/jxgc/producttaskmanage/qualitychek/QCHandle.jsp?vehicleType=20'}
/*	    			{id:'客车检修提票处理', text:'客车检修提票处理', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketHandle.jsp?vehicleType=20'}
	    			{id:'客车检修提票质量检查', text:'客车检修提票质量检查', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultQCResult.jsp?vehicleType=20'}*/
	    		]
	    	},
	    	{
	    		id: '检修统计分析', text: '统计分析', leaf: false, children: [
	    			{id:'检修提票综合分析', text:'提票综合分析', leaf:true, funcaction:'/jsp/jx/jxgc/tpmanage/FaultTicketIntegrateAnasys.jsp'}
	    		]
	    	}
    	]
    }, {
    	id: '设备管理', text: '设备管理', leaf: false, children: [
    		{
    		 	id: '新购设备管理', text: '新购设备', leaf: false, children: [{
    		 		id: '新购设备', text: '新购设备', leaf: true, funcaction: '/jsp/sb/newbuyequipment/NewBuyEquipment.jsp'
    		 	}]
    		},
    		{
    			id: '设备类别维护管理', text: '设备类别维护', leaf: false, children: [{
    		 		id: '设备类别维护', text: '设备类别维护', leaf: true, funcaction: '/jsp/sb/classfication/Classification.jsp'
    		 	}]
    		},
    		{
    		 	id: '设备主要信息管理', text: '设备主要信息', leaf: false, children: [{
    		 		id: '设备主要信息', text: '设备主要信息', leaf: true, funcaction: '/jsp/sb/equipmentInfo/EquipmentPrimaryInfo.jsp'
    		 	}]
    		}, {
    		 	id: '设备检修管理', text: '设备检修', leaf: false, children: [{
    		 		id: '检修周期', text: '检修周期', leaf: true, funcaction: '/jsp/sb/repair/period/RepairPeriod.jsp'
    		 	},{
    		 		id: '设备检修范围', text: '设备检修范围', leaf: true, funcaction: '/jsp/sb/repair/scope/RepairScope.jsp'
    		 	},{
    		 		id: '设备检修年计划', text: '设备检修年计划', leaf: true, funcaction: '/jsp/sb/repair/plan/year/RepairPlanYear.jsp'
    		 	},{
    		 		id: '设备检修月计划', text: '设备检修月计划', leaf: true, funcaction: '/jsp/sb/repair/plan/month/VRepairPlanMonth.jsp'
    		 	},{
    		 		id: '设备检修周计划', text: '设备检修周计划', leaf: true, funcaction: '/jsp/sb/repair/plan/week/RepairPlanMonth.jsp'
    		 	},{
    		 		id: '检修作业工单处理', text: '检修作业工单处理', leaf: true, funcaction: '/jsp/sb/repair/task/dispose/RepairTaskList.jsp'
    		 	},{
    		 		id: '检修作业工单查询', text: '检修作业工单查询', leaf: true, funcaction: '/jsp/sb/repair/task/query/RepairTaskList.jsp'
    		 	}]
    		}, {
    			id: '设备巡检管理', text: '设备巡检', leaf: false, children: [{
    		 		id: '设备巡检标准', text: '设备巡检标准', leaf: true, funcaction: '/jsp/sb/inspect/scope/InspectScope.jsp'
    			}, {
    		 		id: '设备巡检目录', text: '设备巡检目录', leaf: true, funcaction: '/jsp/sb/inspect/route/InspectRoute.jsp'
    			}, {
    		 		id: '设备巡检计划', text: '设备巡检计划', leaf: true, funcaction: '/jsp/sb/inspect/plan/InspectPlan.jsp'
    			}, {
    		 		id: '设备巡检处理', text: '设备巡检处理', leaf: true, funcaction: '/jsp/sb/inspect/dispose/InspectDispose.jsp'
    			}, {
    		 		id: '设备巡检日志', text: '设备巡检日志', leaf: true, funcaction: '/jsp/sb/inspect/search/EquipmentInspectLog.jsp'
    			}]
    		}, {
    			id: '故障提票管理', text: '故障提票', leaf: false, children: [{
    		 		id: '故障提票', text: '故障提票', leaf: true, funcaction: '/jsp/sb/fault/tp/FaultOrder.jsp'
    			}, {
    		 		id: '提票调度派工', text: '提票调度派工', leaf: true, funcaction: '/jsp/sb/fault/ddpg/FaultOrder.jsp'
    			}, {
    		 		id: '提票工长派工', text: '提票工长派工', leaf: true, funcaction: '/jsp/sb/fault/gzpg/FaultOrder.jsp'
    			}, {
    		 		id: '设备提票查询', text: '设备提票查询', leaf: true, funcaction: '/jsp/sb/fault/query/FaultOrder.jsp'
    			}, {
    		 		id: '设备故障统计', text: '设备故障统计', leaf: true, funcaction: '/jsp/sb/fault/statistics/FaultStatistics.jsp'
    			}] 
    		}, {
    			id: '设备点检管理', text: '设备点检', leaf: false, children: [{
    		 		id: '设备点检项目', text: '设备点检项目', leaf: true, funcaction: '/jsp/sb/check/scope/PointCheckScope.jsp'
    		}, {
    		 		id: '设备点检目录', text: '设备点检目录', leaf: true, funcaction: '/jsp/sb/check/catalog/PointCheckCatalog.jsp'
    		}, {
    		 		id: '设备点检统计', text: '设备点检统计', leaf: true, funcaction: '/jsp/sb/check/statistic/PointCheckStatistic.jsp'
    			}] 
    		}, {
    		 	id: '使用人确认', text: '使用人确认', leaf: false, children: [{
    		 		id: '使用人确认1', text: '使用人确认', leaf: true, funcaction: '/jsp/sb/userconfirm/UserConfirm.jsp'
    		 	}]
    		}
    	]
    },{
    	id: '配件管理', text: '配件管理', leaf: false, children: [
    		{
    		 	id: '配件基础', text: '配件基础', leaf: false, children: [
    		 		{id:'配件二维码生成', text:'配件二维码生成', leaf:true, funcaction:'/jsp/jx/pjwz/partcode/PartCodeGenerate.jsp'},
    		 		{id:'配件生产厂家维护', text:'配件生产厂家维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/madefactory/PartsMadeFactory.jsp'},
    		 		{id:'配件库房维护', text:'配件库房维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/warehouse/Warehouse.jsp'},
    		 		{id:'配件规格型号维护', text:'配件规格型号维护', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/partstype/PartsType.jsp'},
    		 		{id:'配件零部件维护', text:'配件零部件维护', leaf:true, funcaction:'/jsp/jcbm/jcpjzd/JcpjzdBuild.jsp'},
    		 		{id:'配件自修目录', text:'配件自修目录', leaf:true, funcaction:'/jsp/jx/pjwz/partbase/repairlist/SelfRepairDir.jsp'}
    		 	]
    		},
    		{
    		 	id: '配件周转', text: '配件周转', leaf: false, children: [
    		 		{id:'配件下车登记', text:'配件下车登记', leaf:true, funcaction:'/jsp/jx/pjwz/unloadpartsnew/PartsUnloadRegister.jsp'},
    		 		{id:'配件上车登记', text:'配件上车登记', leaf:true, funcaction:'/jsp/jx/pjwz/fixpartsnew/PartsFixRegisterNew.jsp'},
    		 		{id:'良好配件登记', text:'良好配件登记', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsregister/wellPartsRegister.jsp'},
    		 		{id:'修竣配件入库', text:'修竣配件入库', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegister.jsp'},
    		 		{id:'配件出库登记', text:'配件出库登记', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/exwh/WellPartsExwh.jsp'},
    		 		{id:'配件报废登记', text:'配件报废登记', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapRegister.jsp'},
    		 		{id:'配件校验', text:'配件校验', leaf:true, funcaction:'/jsp/jx/pjwz/partsCheck/PartsCheckRegister.jsp'}
    		 	]
    		},
    		{
    		 	id: '配件查询', text: '配件查询', leaf: false, children: [
    		 		{id:'下车配件登记查询', text:'下车配件登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/unloadparts/PartsUnloadRegisterSearch.jsp'},
    		 		{id:'上车配件登记查询', text:'上车配件登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/fixparts/PartsFixRegisterSearch.jsp'},
    		 		{id:'良好配件登记查询', text:'良好配件登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsregister/wellPartsRegisterSearch.jsp'},
    		 		{id:'修竣配件入库查询', text:'修竣配件入库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/repairedpartswh/PartsWHRegisterSearch.jsp'},
    		 		{id:'配件出库查询', text:'配件出库查询', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/exwh/WellPartsExwhSearch.jsp'},
    		 		{id:'配件报废登记查询', text:'配件报废登记查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsscrap/PartsScrapRegisterSearch.jsp'},
    		 		{id:'配件校验查询', text:'配件校验查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsCheck/PartsCheckRegisterSearch.jsp'},
    		 		{id:'良好配件库存统计', text:'良好配件库存统计', leaf:true, funcaction:'/jsp/jx/pjwz/wellpartsmanage/wellpartsstock/WellPartsStockStatistics.jsp'},
    		 		{id:'配件保有量统计', text:'配件保有量统计', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsQuantityReport.jsp'},
					{id:'配件周转情况统计', text:'配件周转情况统计', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountReport.jsp'},
					{id:'配件检修结果查询', text:'配件检修结果查询', leaf:true, funcaction:'/jsp/jx/pjwz/query/PartsRdpSearch.jsp'},
					{id:'配件综合查询', text:'配件综合查询', leaf:true, funcaction:'/jsp/jx/pjwz/integrateQuery/PartsIntegrateQuery.jsp'},
					{id:'配件信息查询', text:'配件信息查询', leaf:true, funcaction:'/jsp/jx/pjwz/partsmanage/PartsAccountSearch.jsp'}
    		 		
    		 	]
    		},
    		{
    		 	id: '配件检修', text: '配件检修', leaf: false, children: [			
    		 			{id:'检修工艺维护', text:'检修工艺维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/tecdefine/TecDefine.jsp'},
						{id:'检修记录单维护', text:'检修记录单维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/recorddefine/Record.jsp'},
						{id:'检修需求维护', text:'检修需求维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/wpdefine/WP.jsp'},
						{id:'配件检修质量检查维护', text:'配件检修质量检查维护', leaf:true, funcaction:'/jsp/jx/pjjx/base/qcitemdefine/QCItem.jsp'},						
						{id:'配件检修作业计划编制', text:'配件检修作业计划编制', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/planedit/parts_rdp_plan_establishment.jsp'},
						{id:'配件检修任务单派工', text:'配件检修任务单派工', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/dispatch/PartsRdpDispatcher.jsp'},	
						{id:'配件检修任务处理', text:'配件检修任务处理', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/rdpprocess/PartsRdpProcess.jsp'},
						{id:'配件检修质量检验', text:'配件检修质量检验', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/qccheckprocess/PartsRdpQcCheck.jsp'},
						{id:'修竣配件合格验收', text:'修竣配件合格验收', leaf:true, funcaction:'/jsp/jx/pjjx/partsrdp/check/PartsRdpCheck.jsp'},	
						{id:'配件检修记录单结果打印', text:'配件检修记录单结果打印', leaf:true, funcaction:'/jsp/jx/pjjx/recordresultprint/RecordResultPrint.jsp'}
    		 	]
    		}	    		
    	]
    },{
        id:'柴油发电机组管理', text:'柴油发电机组管理', leaf:false, children:[
              {id:'柴油发电机类型', text:'柴油发电机类型', leaf:true, funcaction:'/jsp/freight/base/vehicle/generator/TrainVehicleType.jsp?vehicleType=30'},
              {id:'柴油发电机台账信息维护', text:'柴油发电机台账信息维护', leaf:true, funcaction:'/jsp/jx/jczl/generator/JczlTrainService.jsp?vehicleType=30'},
              {id:'柴油发电机修程对应修次', text:'柴油发电机修程对应修次', leaf:true, funcaction:'/jsp/jxpz/rcrtset/generator/RcRt.jsp?vehicleType=30'},
              {id:'柴油发电机检修标准', text:'柴油发电机检修标准', leaf:true, funcaction:'/jsp/jx/scdd/jxplanmanage/jxstandard/generator/repair_standard.jsp?vehicleType=30'},
              {id:'柴油发电机检修记录单维护', text:'柴油发电机检修记录单维护', leaf:true, funcaction:'/jsp/jx/jxgc/repairrequirement/generator/RepairProject.jsp?vehicleType=30'},
              {id:'柴油发电机检修作业流程维护',text:'柴油发电机检修作业流程维护',leaf:true,funcaction:'/jsp/jx/jxgc/generatorProcessdef/JobProcessDef.jsp?vehicleType=30'},
              {id:'柴油发电机检修作业编辑', text:'柴油发电机检修作业编辑', leaf:true, funcaction:'/jsp/jx/jxgc/workplanmanage/generator/TrainWorkPlanEdit.jsp?vehicleType=30'}
            ]
    	},{
        id:'test', text:'测试', leaf:false, children:[
                {id:'VIS测试', text:'VIS测试', leaf:true, funcaction:'/jsp/vis/vis_demo.jsp'},
                {id:'VIS测试1', text:'VIS测试1', leaf:true, funcaction:'/jsp/vis/vis_demo2.jsp'},
                {id:'文件操作', text:'文件操作', leaf:true, funcaction:'/jsp/test/office.jsp'}
        ]
    },{
        id:'bootstrap', text:'新页面开发', leaf:false, children:[
                    {id:'检修月计划编制', text:'检修月计划编制', leaf:true, funcaction:'/pages/view/scdd/enforceplan/trainEnforcePlan.jsp'}
                                            ]
    },{
        id:'oldtree', text:'开发调试菜单导航', leaf:false, children:tree1
    }]
;
