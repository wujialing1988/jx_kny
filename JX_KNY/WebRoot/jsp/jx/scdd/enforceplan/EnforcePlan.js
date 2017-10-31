//TODO 未发现使用，待删除
var index=0;
function setColor(i, color){
	document.getElementById("id_"+i).parentNode.parentNode.parentNode.style.backgroundColor=color;
}


/**
 * 生产任务计划 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('EnforcePlan');                       //定义命名空间
EnforcePlan.checkStartDate = function(){
	var date1 = Ext.getCmp("planStartDate_detailId").getValue() ;
	var date2 = Ext.getCmp("planEndDate_detailId").getValue() ;
	var flag = true; 
	if(date1 != "" && date2 != ""){		
		if(date1.format('Y-m-d') > date2.format('Y-m-d')){
			flag = false ;
			return "计划开始日期不能晚于计划结束日期！";
		}		
	}
	return flag;
}
//验证计划结束日期
EnforcePlan.checkEndDate = function(){
	var date1 = Ext.getCmp("planStartDate_detailId").getValue() ;
	var date2 = Ext.getCmp("planEndDate_detailId").getValue() ;
	var flag = true; 
	if(date1 != "" && date2 != ""){				
		if(date1.format('Y-m-d') > date2.format('Y-m-d')){
			flag = false ;
			return "计划结束日期不能早于计划开始日期！";
		}		
	}
	return flag;
}

var year = new Date().getFullYear();

var yearData = [];
for(var i = year+1; i> year - 9; i--){
	yearData.push([i,i]);
}
var yearStore = new Ext.data.SimpleStore({
    fields: ['v', 't'],
	data : yearData
})
EnforcePlan.startDate = year + "-01-01";
EnforcePlan.endDate = year + "-12-31";

EnforcePlan.rootId = "Y_" + year;
//当前点击的树节点id
EnforcePlan.currentNodeId = EnforcePlan.rootId;
//专业类型树 
EnforcePlan.tree = new Ext.tree.TreePanel( {
  loader : new Ext.tree.TreeLoader( {
      dataUrl : ctx + "/trainEnforcePlan!getDateTree.action"
  }),
  root: new Ext.tree.AsyncTreeNode({
      text: year,
      id: EnforcePlan.rootId,
      leaf: false,
      expanded :true,
      start:EnforcePlan.startDate,
      end:EnforcePlan.endDate 
  }),
  rootVisible: true,
  autoScroll : false,
  animate : false,
  useArrows : false,
  border : false,
  listeners: {
	click: function(node, e) {
		EnforcePlan.startDate = node.attributes.start;
	    EnforcePlan.endDate = node.attributes.end;
		EnforcePlan.grid.store.load();
      }
  }    
});
EnforcePlan.tree.TrainNo = null;
EnforcePlan.tree.on('beforeload', function(node){
	var param = "?id="+node.id;
	EnforcePlan.tree.loader.dataUrl = ctx + '/trainEnforcePlan!getDateTree.action' + param;
});

EnforcePlan.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainEnforcePlanDetail!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/trainEnforcePlanDetail!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/trainEnforcePlanDetail!logicDelete.action',            //删除数据的请求URL
    searchFormColNum: 2,
    saveFormColNum: 2,
    tbar:['search','refresh','->',
          '<span style="background-color:yellow;border:solid 1px white;width:15px;height:15px">&nbsp;</span>未执行',
          '<span style="background-color:#81BEF7;border:solid 1px white;width:15px;height:15px">&nbsp;</span>执行中',
          '<span style="background-color:#04B45F;border:solid 1px white;width:15px;height:15px">&nbsp;</span>已执行'
          ],
    fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'机车施修计划主键', dataIndex:'trainEnforcePlanIDX', hidden: true, editor:{  xtype: 'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeIDX', hidden: true, 
		editor:{  
			id:"trainType_comb",xtype: "TrainType_combo",	fieldLabel: "车型",
			hiddenName: "trainTypeIDX", 
			returnField: [{widgetId:"trainTypeShortName",propertyName:"shortName"}],
			displayField: "shortName", valueField: "typeID",
			pageSize: 20, minListWidth: 200,
			editable:true,
			allowBlank: false,
			listeners : {   
	        	"select" : function() {   
	            	//重新加载车号下拉数据
	                var trainNo_comb = Ext.getCmp("trainNo_comb");   
	                trainNo_comb.reset();  
	                trainNo_comb.clearValue(); 
	                trainNo_comb.queryParams = {"trainTypeIDX":this.getValue()};
	                trainNo_comb.cascadeStore();
	                //重新加载修程下拉数据
	                var rc_comb = Ext.getCmp("rc_comb");
	                rc_comb.reset();
	                rc_comb.clearValue();
	                rc_comb.queryParams = {"TrainTypeIdx":this.getValue()};
	                rc_comb.cascadeStore();
	                Ext.getCmp("rt_comb").reset();
	                Ext.getCmp("rt_comb").clearValue();
	        	}   
	    	}}	    
	},{
		header:'车型', dataIndex:'trainTypeShortName', editor:{id: 'trainTypeShortName', xtype: 'hidden' },
		searcher:{xtype: 'textfield'}
	},{
		header:'车号', dataIndex:'trainNo',
		editor:{
			id:"trainNo_comb",xtype: "TrainNo_combo",	fieldLabel: "车号",
			hiddenName: "trainNo", 
			displayField: "trainNo", valueField: "trainNo",
			pageSize: 20, minListWidth: 200,
			returnField: [{widgetId:"holdOrgId",propertyName:"holdOrgId"},
			              {widgetId:"holdOrgSeq",propertyName:"holdOrgSeq"},
			              {widgetId:"holdOrgName",propertyName:"holdOrgName"}],
			editable:true,
			allowBlank: false,
			listeners : {
				"beforequery" : function(){
					//选择车号前先选车型
					var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
					if(trainTypeIdx == "" || trainTypeIdx == null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
				}
			}
		},
		searcher:{xtype: 'textfield'}
	},{
		header:'修程', dataIndex:'repairClassIDX', hidden: true,
		editor:{
			id:"rc_comb",
			xtype: "TrainRC_combo",
			fieldLabel: "修程",
			hiddenName: "repairClassIDX", 
			returnField: [{widgetId:"repairClassName",propertyName:"xcName"}],
			displayField: "xcName",
			valueField: "xcID",
			pageSize: 0, minListWidth: 200,
			allowBlank: false,
			listeners : {   
            	"select" : function() {   
                	var rt_comb = Ext.getCmp("rt_comb");
                	rt_comb.clearValue();
                 	rt_comb.reset();
                    rt_comb.queryParams = {"rcIDX":this.getValue()};
                    rt_comb.cascadeStore();   
            	},
            	"beforequery" : function(){
            		var trainTypeIdx =  Ext.getCmp("trainType_comb").getValue();
					if(trainTypeIdx == "" || trainTypeIdx == null){
						MyExt.Msg.alert("请先选择车型！");
						return false;
					}
            	}
            }
		}
	},{
		header:'修程', dataIndex:'repairClassName', editor:{  xtype:'hidden' }
	},{
		header:'修次主键', dataIndex:'repairtimeIDX', hidden:true,
		editor:{
			id:"rt_comb",
			xtype: "Base_combo",
			fields: ["repairtimeIDX","repairtimeName"],
			business: 'rcRt',
			fieldLabel: "修次",
			hiddenName: "repairtimeIDX", 
			returnField: [{widgetId:"repairtimeName",propertyName:"repairtimeName"}],
			displayField: "repairtimeName",
			valueField: "repairtimeIDX",
			pageSize: 0,
			minListWidth: 200,
			allowBlank: false,
			listeners : {
				"beforequery" : function(){
					//选择修次前先选修程
            		var rcIdx =  Ext.getCmp("rc_comb").getValue();
					if(rcIdx == "" || rcIdx == null){
						MyExt.Msg.alert("请先选择修程！");
						return false;
					}
            	}
			}
		}
	},{
		header:'修次', dataIndex:'repairtimeName', editor:{  xtype: 'hidden' }
	},{
		header:'计划进车日期', dataIndex:'planStartDate', xtype:'datecolumn', 
		editor:{ id: 'planStartDate_detailId', xtype:'my97date', initNow: false, allowBlank: false, validator: EnforcePlan.checkStartDate},
		searcher:{disabled: true}
	},{
		header:'实际进车日期', dataIndex:'realStartDate', xtype:'datecolumn', 
		editor:{ 
			xtype:'hidden',
			id: 'realStartDate_Id'
		},
		searcher:{id: 'realStartDate_searchId', xtype:'my97date', initNow: false}
	},{
		header:'计划出厂日期', dataIndex:'planEndDate', xtype:'datecolumn', 
		editor:{ id: 'planEndDate_detailId', xtype:'my97date', initNow: false, allowBlank: false, validator: EnforcePlan.checkEndDate },
		searcher:{disabled: true}
	},{
		header:'实际出厂日期', dataIndex:'realEndDate', xtype:'datecolumn', 
		editor:{ 
			xtype:'hidden',
			id: 'realEndDate_Id'
		},
		searcher:{id: 'realEndDate_searchId', xtype:'my97date', initNow: false}
	},{
		header:'计划状态', dataIndex:'planStatus', hidden: true, editor:{ xtype:'hidden' }
	
	
	},{
		header:'配属单位ID', dataIndex:'holdOrgId', hidden: true, editor:{ xtype:'hidden'}
	},{
		header:'配属单位部门序列', dataIndex:'holdOrgSeq', hidden:true, editor:{  xtype:'hidden' }
	},{
		header:'planStatus', dataIndex:'planStatus', hidden:true, editor:{  xtype:'hidden', value: status_Plan }
	},{
		header:'配属单位', dataIndex:'holdOrgName', 
		editor:{  
			id: 'holdOrg_Name',			
			xtype: 'textfield',			
			readOnly:true 
		},
		searcher:{disabled: true}
	},{
		header:'备注', dataIndex:'remarks', editor:{ xtype:'textarea',  maxLength:1000 },
		searcher:{disabled: true},
		renderer:function(v,g,r){
			var color = '';
			if(status_Plan == r.get("planStatus")){
				color = 'yellow';
			}else if(r.get("planStatus") == status_redemption){
				color = '#81BEF7'
			}else if(r.get("planStatus") == status_completePlan){
				color = "#04B45F";
			}
			setTimeout("setColor('" + index +"','" + color + "')",100);
			if(v){
				return "<span id='id_" + index++ + "'>"+v+"</span>";
			}else{
				return "<span id='id_" + index++ + "'></span>";
			}
		}
	}],
	searchFn:function(s){
		EnforcePlan.s = s;
		this.store.load();
	},
	//覆盖双击处理事件，使其不能双击出现编辑框
	toEditFn:function(){}
});

EnforcePlan.grid.store.on("beforeload", function(){
	var whereList = [];
	for(prop in EnforcePlan.s){
        whereList.push({propName:prop,propValue:EnforcePlan.s[prop]});
    }
	
	whereList.push({propName:'planStartDate', propValue: EnforcePlan.startDate, compare:Condition.GE });
	whereList.push({propName:'planEndDate', propValue: EnforcePlan.endDate, compare:Condition.LE });
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList) 
});

//页面自适应布局
var viewport = new Ext.Viewport({
	layout:"fit",
	items: {
		layout:"border",
	    items : [ {
	        title : '<span style="font-weight:normal">计划日期段</span>',
	        iconCls : 'chart_organisationIcon',
	        tbar:[{
	        	xtype:"label",
	        	text:"年份："
	        },{
	        	id:"year_id",
	        	xtype:"combo",
	        	store:yearStore,
				valueField:'v',
				editable:false,
				displayField:'t',
				triggerAction:'all',
				value:year,
				mode:'local',
				listeners: {
	        		"select" : function(){        			
						
	        			year = Ext.getCmp("year_id").getValue();
	        		    EnforcePlan.rootId = "Y_"+year,
	        		    EnforcePlan.startDate = year + "-01-01";
	        		    EnforcePlan.endDate = year + "-12-31";
	        		    EnforcePlan.tree.setRootNode({id:"Y_"+year, text:year,  start: EnforcePlan.startDate, end: EnforcePlan.endDate });
	        			EnforcePlan.tree.root.reload();
			        	EnforcePlan.tree.getRootNode().expand();
			        	
			        	EnforcePlan.startDate = EnforcePlan.tree.root.attributes.start;
			    	    EnforcePlan.endDate = EnforcePlan.tree.root.attributes.end;
			    		EnforcePlan.grid.store.load();
	        		}
	        	}
	        }],
	        collapsible : true,
	        width : 210,
	        minSize : 180,
	        maxSize : 280,
	        split : true,
	        region : 'west',
	        bodyBorder: false,
	        autoScroll : true,
	        items : [ EnforcePlan.tree ]
	    }, {
	        region : 'center',
	        layout : 'fit',
	        bodyBorder: false,
	        items : [ EnforcePlan.grid ]
	    }]
	}
});
});