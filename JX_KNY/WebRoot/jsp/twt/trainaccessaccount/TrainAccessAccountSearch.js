/**
 * 机车出入段台账查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('TrainAccessAccountSearch');                       
TrainAccessAccountSearch.searchParam = {};
/*** 查询表单 start ***/
TrainAccessAccountSearch.searchLabelWidth = 90;
TrainAccessAccountSearch.searchAnchor = '95%';
TrainAccessAccountSearch.searchFieldWidth = 270;
/** 获取当前日期及上个月当天的日期*/
TrainAccessAccountSearch.getCurrentMonth = function(arg){
	var Nowdate = new Date();//获取当前date
	var currentYear = Nowdate.getFullYear();//获取年度
	var currentMonth = Nowdate.getMonth();//获取当前月度
	var currentDay = Nowdate.getDate();//获取当前日
	var MonthFirstDay = new Date(currentYear,currentMonth-1,currentDay);
	if(arg == 'begin'){
		return MonthFirstDay.format('Y-m-d');
	}
	else if (arg == 'end'){
		return Nowdate.format('Y-m-d');
	}
}
TrainAccessAccountSearch.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: TrainAccessAccountSearch.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: TrainAccessAccountSearch.searchFieldWidth, labelWidth: TrainAccessAccountSearch.searchLabelWidth, defaults:{anchor:TrainAccessAccountSearch.searchAnchor},
			items:[{ 
				fieldLabel: "车型",
				xtype: "Base_combo",
				hiddenName: "trainTypeIDX",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:false,	
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = TrainAccessAccountSearch.searchForm.getForm().findField("trainNo");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.vehicleType = vehicleType ;
		                trainNo_comb.queryParams.trainTypeIDX = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	},
				hasEmpty: false,
				emptyText: ''
			},{
				id:"beginDate", fieldLabel: '入段日期<br>(开始)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
				value: TrainAccessAccountSearch.getCurrentMonth('begin'), width:TrainAccessAccountSearch.searchFieldWidth
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.33,
			fieldWidth: TrainAccessAccountSearch.searchFieldWidth, labelWidth: TrainAccessAccountSearch.searchLabelWidth, defaults:{anchor:TrainAccessAccountSearch.searchAnchor},
			items:[{
				id: "trainNo_comb_search",
			    fieldLabel: "车号",
				xtype: "Base_combo",
				name:'trainNo',
				hiddenName: "trainNo",
			    business: 'jczlTrain',
			    entity:'com.yunda.jx.jczl.attachmanage.entity.JczlTrain',
                fields:['trainTypeIDX','trainNo','trainTypeShortName'],
                queryParams: {'vehicleType':vehicleType},// 表示客货类型
    		    displayField: "trainNo", valueField: "trainNo",
                pageSize: 20, minListWidth: 200,
                disabled:false,
                editable:true
			},{
				id:"endDate", fieldLabel: '入段日期<br>(结束)', xtype: "my97date", my97cfg: {dateFmt:"yyyy-MM-dd"}, initNow: false,
				value: TrainAccessAccountSearch.getCurrentMonth('end'), width:TrainAccessAccountSearch.searchFieldWidth
			}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:0.34,
			fieldWidth: TrainAccessAccountSearch.searchFieldWidth, labelWidth: TrainAccessAccountSearch.searchLabelWidth, defaults:{anchor:TrainAccessAccountSearch.searchAnchor},
			items:[{
				xtype: "textfield", name: 'trainAliasName', fieldLabel: '机车别名'
			},{				
				fieldLabel: '站场',
				name: 'siteName',
				xtype:"Base_combo",
				business:'workPlace',
				entity:"com.yunda.jxpz.workplace.entity.WorkPlace",		
				queryHql:"from WorkPlace",
				fields:['workPlaceName'],
				displayField:"workPlaceName",
				valueField: "siteName",
				isAll: 'yes'
			}]
		}]
	}],
	buttons:[{
			text:'查询', iconCls:'searchIcon', 
			handler: function(){ 
				var form = TrainAccessAccountSearch.searchForm.getForm();				
				TrainAccessAccountSearch.searchParam = form.getValues();
				var searchParam = form.getValues();
		        if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
					searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
				}
                searchParam = MyJson.deleteBlankProp(searchParam);
				TrainAccessAccountSearch.grid.searchFn(searchParam); 
			}
		},{
            text: "重置", iconCls: "resetIcon", handler: function(){ 
            	var form = TrainAccessAccountSearch.searchForm;
            	form.getForm().reset();
            	//清空自定义组件的值
                var componentArray = ["Base_combo"];
                for (var j = 0; j < componentArray.length; j++) {
                	var component = form.findByType(componentArray[j]);
                	if (!Ext.isEmpty(component) && Ext.isArray(component)) {
						for (var i = 0; i < component.length; i++) {
							component[i].clearValue();
						}						
					}	                    
                }
                var trainNo_comb = TrainAccessAccountSearch.searchForm.getForm().findField("trainNo");   
                delete trainNo_comb.queryParams.trainTypeIDX;
                trainNo_comb.cascadeStore();	
            	searchParam = {};
            	TrainAccessAccountSearch.grid.searchFn(searchParam);
            }
		}]
});
/*** 查询表单 end ***/

/*** 机车出入段台账列表 start ***/
TrainAccessAccountSearch.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/trainAccessAccount!pageQuery.action',                 //装载列表数据的请求URL
    viewConfig: null,
    tbar: [{
    	hidden:true,
    	text:"打印", iconCls:"printerIcon", handler: function() {
    			var form = TrainAccessAccountSearch.searchForm.getForm();
				if (!form.isValid()) {
					return;
				}
				var data = form.getValues();
				var startDate = "";
		        var overDate = "";
		        if(Ext.getCmp("beginDate").getValue() != ""){
		        	startDate = Ext.getCmp("beginDate").getValue().format('Y-m-d H:i');
		        }
		        if(Ext.getCmp("endDate").getValue() != ""){
		        	overDate = Ext.getCmp("endDate").getValue().format('Y-m-d') + " 23:59:59";
		        }
				var reportUrl = "/zb/trainaccessaccount/TrainAccessAccount.cpt?ctx=" + ctx.substring(1);
				var dataUrl = reportUrl + "&startDate=" + startDate + "&endDate=" + overDate + "&trainTypeIDX=" + data.trainTypeIDX +
				"&trainNo=" + data.trainNo + "&dName=" + data.dName + "&siteName=" + data.siteName + "&trainAliasName=" + data.trainAliasName;
                window.open(ctx+"/jsp/jx/common/report.jsp?url="+encodeURIComponent(dataUrl)+"&title=" + encodeURI("机车出入段记录"));
    	}
    }, 'refresh'],
	fields: [{
		header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
	},{
		header:'车型', dataIndex:'trainTypeShortName', 
		editor:{id:'trainTypeShortName', name:'trainTypeShortName',xtype:'hidden' },
		searcher:{xtype: 'textfield'}, width: 60
	},{
		header:'车号', dataIndex:'trainNo', editor:{},
		searcher:{xtype: 'textfield'}, width: 60
	},{
		header:'车辆别名', dataIndex:'trainAliasName', editor:{ xtype:'hidden'},
		searcher:{disabled: true}, width: 80
	},{
		header:'入段时间', dataIndex:'inTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{ xtype:'hidden' },
		searcher:{disabled: true}, width: 150
	},{
		header:'入段去向', dataIndex:'toGo', editor:{},
		renderer : function(v){
			if (Ext.isEmpty(v))
				return "";
			return EosDictEntry.getDictname("TWT_TRAIN_ACCESS_ACCOUNT_TOGO",v);
		},
		searcher:{xtype: 'textfield'}, width: 100
	},{
		header:'计划出段时间', dataIndex:'planOutTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{  },
		searcher:{disabled: true}, width: 150
	},{
		header:'实际出段时间', dataIndex:'outTime', xtype:'datecolumn', format: "Y-m-d H:i:s", 
		editor:{  },
		searcher:{disabled: true}, width: 150
	},{
		header:'站场', dataIndex:'siteName', editor:{ },
		searcher:{disabled: true}, width: 80
	}],
	
    searchFn: function(searchParam){
    	TrainAccessAccountSearch.searchParam = searchParam;
    	this.store.load();
    },
    toEditFn: function(grid, rowIndex, e) {}
});
TrainAccessAccountSearch.grid.store.setDefaultSort('inTime', 'DESC');
TrainAccessAccountSearch.grid.store.on("beforeload", function(){	
	var form = TrainAccessAccountSearch.searchForm.getForm();	
	TrainAccessAccountSearch.searchParam = form.getValues();
	var beginDate = form.findField("beginDate").getValue();
    var endDate =form.findField("endDate").getValue();
    if(endDate < beginDate){
    	MyExt.TopMsg.msg('提示',"入段结束日期不能比入段开始日期早！", false, 1);
		return;
    }
    var searchParam = form.getValues();
    if (!Ext.isEmpty(Ext.get("trainNo_comb_search").dom.value)) {
		searchParam.trainNo = Ext.get("trainNo_comb_search").dom.value;
	}
    searchParam = MyJson.deleteBlankProp(searchParam);
	var whereList = [] ;
	for (prop in searchParam) {			
        if(!Ext.isEmpty(searchParam[prop]) && searchParam[prop] != " "){
        	switch(prop){
			 	//入段日期(起) 运算符为">="
			 	case 'beginDate':
			 		whereList.push({propName:'inTime',propValue:searchParam[prop],compare:Condition.GE});
			 		break;
			 	//入段日期(止) 运算符为"<="
			 	case 'endDate':
			 		whereList.push({propName:'inTime',propValue:searchParam[prop]+' 23:59:59',compare:Condition.LE});
			 		break;	 		
			 	default:
	         		whereList.push({propName:prop,propValue:searchParam[prop],compare:Condition.LIKE});
        	}
        }
	}	
	var sqlStr = " Out_Time is not null" ;		
	whereList.push({sql: sqlStr, compare: Condition.SQL});
	whereList.push({propName:'vehicleType',propValue:vehicleType,compare:Condition.EQ,stringLike: false});
	this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});
/*** 机车出入段台账列表 end ***/

/*** 界面布局 start ***/
TrainAccessAccountSearch.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',
        collapsible:true,  height: 202, bodyBorder: false,
        items:[TrainAccessAccountSearch.searchForm], frame: true, title: "查询", xtype: "panel"
    },{
        region : 'center', layout : 'fit', bodyBorder: false, items : [ TrainAccessAccountSearch.grid ]
    }]
};
var viewport = new Ext.Viewport({ layout:'fit', items:TrainAccessAccountSearch.panel });
});