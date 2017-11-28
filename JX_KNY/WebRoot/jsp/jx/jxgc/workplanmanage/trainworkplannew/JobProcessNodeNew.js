/**
 * 机车检修作业计划-流程节点 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('JobProcessNode');                       //定义命名空间
JobProcessNode.labelWidth = 90;
JobProcessNode.fieldWidth = 150;
JobProcessNode.idx = '';
JobProcessNode.nodeIDX = '';
JobProcessNode.workPlanIDX = '';
JobProcessNode.processIDX = '';
JobProcessNode.parentIDX = '';
JobProcessNode.planBeginTime = null;
JobProcessNode.workstationReturnFn = function(stationObject) {
	var form = JobProcessNode.form;
	form.find("name", "workStationIDX")[0].setValue(stationObject.workStationIDX);
	form.find("name", "workStationName")[0].setValue(stationObject.workStationName);
	form.getForm().findField("workStationBelongTeam").setDisplayValue(stationObject.teamOrgId, stationObject.teamOrgName);
	form.find("name", "workStationBelongTeamName")[0].setValue(stationObject.teamOrgName);
}
JobProcessNode.form = new Ext.form.FormPanel({
    baseCls: "x-plain", align: "center",	defaultType: "textfield",
	layout: "form",		border: false,	labelWidth: JobProcessNode.labelWidth,
    defaults: {
    	anchor: "95%",
    	defaults: {
    		defaults:{
    			xtype:"textfield", 
			    labelWidth:JobProcessNode.labelWidth, anchor:"98%",
			    defaults: {
			    	width: JobProcessNode.fieldWidth
			    }
    		}    		
    	}
    },
    items: [{
    	xtype: "panel",	border: false,	baseCls: "x-plain",	layout: "column",	align: "center", 
    	items:[{
				baseCls:"x-plain", align:"center", layout:"form", labelWidth:JobProcessNode.labelWidth, columnWidth: 0.5,
		    	items:[{
		    			name: "nodeName", fieldLabel: "名称", maxLength: 100, allowBlank: false
		    		}]
			},{
				baseCls:"x-plain", align:"center", layout:"form", labelWidth:JobProcessNode.labelWidth, columnWidth: 0.5,
		    	items:[{ name: "ratedWorkMinutes",    fieldLabel: "工期", xtype: 'compositefield', combineErrors: false, 
						  items:[{
						  	xtype: "numberfield", id: "workHours", allowBlank: false, width: 70, maxLength: 3, vtype: "nonNegativeInt", value: 0
						  },{
						  	 xtype: 'label', text: '小时', style: 'height:23px; line-height:23px;'
						  },{
						  	xtype: "numberfield", id: "workMinutes", allowBlank: false, width: 70, maxLength: 3, vtype: "nonNegativeInt", value: 0
						  },{
						  	 xtype: 'label', text: '分钟', style: 'height:23px; line-height:23px;'
						  }]
						}]
			},{
				baseCls:"x-plain", align:"center", layout:"form", defaultType:"textfield", labelWidth:90, columnWidth: 1, 
		        items: [{
		        	name: "nodeDesc", fieldLabel: "节点描述",  xtype:"textarea", maxLength:1000  
		        }]
			},{
				baseCls:"x-plain", align:"center", layout:"form", labelWidth:JobProcessNode.labelWidth, columnWidth: 0.5,
		    	items:[{
		    			name: "planBeginTime", fieldLabel: "开始时间",  xtype:"my97date",format: "Y-m-d H:i",
			        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"}, allowBlank:false
		    		},{
		    			fieldLabel: '作业工位', name: 'workStationName', 
		    			xtype: 'JobNodeStationDefSelect', nodeIDX: JobProcessNode.nodeIDX,
						editable: false, returnFn: JobProcessNode.workstationReturnFn

		    		},{
						  xtype: 'Base_combo',hiddenName: 'workCalendarIDX', fieldLabel: '日历',
						  entity:'com.yunda.baseapp.workcalendar.entity.WorkCalendarInfo',
						  fields: ["idx","calendarName"],displayField:'calendarName',valueField:'idx',
				    	  queryHql: 'from WorkCalendarInfo where recordStatus = 0'
					}]
			},{
				baseCls:"x-plain", align:"center", layout:"form", labelWidth:JobProcessNode.labelWidth, columnWidth: 0.5,
		    	items:[{
		    			name: "planEndTime", fieldLabel: "结束时间",  xtype:"my97date",format: "Y-m-d H:i",
			        	my97cfg: {dateFmt:"yyyy-MM-dd HH:mm"} //, disabled: true
		    		},{
//		    		   fieldLabel:"作业班组", xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'workStationBelongTeam',
//		               returnField:[{widgetId:"workStationBelongTeamName_ID",propertyName:"text"}],
//		      		   forDictHql:"[onlyFirstLevel]",
//			  		   queryHql:" and orgdegree = 'tream' ", 
//			  		   selectNodeModel: 'leaf', width: 170
		    			
		    			fieldLabel:"作业班组", xtype: 'OmOrganizationCustom_comboTree',hiddenName: 'workStationBelongTeam',
		                returnField:[{widgetId:"workStationBelongTeamName_ID",propertyName:"orgname"}],
		      		    queryHql:"[degree]tream|fullName",
		      		    fullNameDegree: "plant",
			  		    selectNodeModel: 'leaf', width: 170
		    		},{    			
		            	xtype: 'radiogroup',
			            fieldLabel: '启动模式',
			            name: 'planMode',
			            items: [
			                { boxLabel: '定时',inputValue: PLANMODE_TIMER, name: 'planMode', checked: true
			                },
			                { boxLabel: '手动',inputValue: PLANMODE_MUNAUL, name: 'planMode'
//			                },  
//			                { boxLabel: '自动',inputValue: PLANMODE_AUTO, name: 'planMode'
			                }  
			            ]
					            
					},{
		    			id: "workStationBelongTeamName_ID", name: "workStationBelongTeamName", xtype:"hidden"
		    		},{
		    			name: "workStationIDX", xtype:"hidden"
		    		},{
		    			name: 'idx', value: JobProcessNode.idx , xtype:"hidden"
		    		},{
		    			name: 'workPlanIDX', xtype:"hidden"
		    		},{
		    			name: 'processIDX',  xtype:"hidden"
		    		},{
		    			name: 'parentIDX', value: JobProcessNode.parentIDX , xtype:"hidden"
		    		},{
		    			name: 'nodeIDX', xtype:"hidden"
		    		},{
		    			name: 'status', xtype:"hidden"
		    		},{
		    			name: 'isLeaf', xtype:"hidden"
		    		},{
		    			name: 'realBeginTime', xtype:"hidden"
    				},{
		    			name: 'newPlanBeginTime', xtype:"hidden"
		    		},{
		    			name: 'newPlanEndTime', xtype:"hidden"
		    		},{
		    			name: 'editStatus', xtype:"hidden"
		    		},{
		    			name: 'seqNo', xtype:"hidden"
		    		}]
			}]	
    }]
	
});
});