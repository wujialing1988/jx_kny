/**
 * 工位人员选择
 */
function add(empid){
	var store = WorkStationEmp.NoDispatchGrid.store;
	for(var i = 0; i < store.getCount(); i++){
		var record = store.getAt(i);
		if(record.get("empid") == empid){
			var recordx = copyRecord(record);			
			WorkStationEmp.grid.store.add(recordx);
			WorkStationEmp.NoDispatchGrid.store.removeAt(i);
			break;
		}
	}
		
}
function del(empid){
	var store = WorkStationEmp.grid.store;
	for(var i = 0; i < store.getCount(); i++){
		var record = store.getAt(i);
		if(record.get("empid") == empid){
			var recordx = copyRecord(record);
			WorkStationEmp.NoDispatchGrid.store.add(recordx);
			WorkStationEmp.grid.store.removeAt(i);
			break;
		}		
	}
}
function copyRecord(record){
	var recordx = new Ext.data.Record();
	recordx.set("empid",record.get("empid"));
	recordx.set("empcode",record.get("empcode"));
	recordx.set("empname",record.get("empname"));
	return recordx;
}
Ext.onReady(function(){
	Ext.namespace('WorkStationEmp');                       //定义命名空间
	WorkStationEmp.idx; 									//作业卡主键	
	WorkStationEmp.orgseq;									// 班组
	
	//已选择人员Grid
	WorkStationEmp.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/omEmployeeSelect!findListForZbPlanRecordEmpSelect.action',                 //装载列表数据的请求URL    
	    tbar:['<span style="font-weight:normal">已选择人员</span>'], page:false,
	    singleSelect:true,hideRowNumberer:true, storeAutoLoad:false,
		fields: [{
			header:'人员id', dataIndex:'empid', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'人员代码', dataIndex:'empcode', editor:{  maxLength:2 }
		},{
			header:'人员名称', dataIndex:'empname', editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'操作', dataIndex:'empid', editor:{ xtype:"hidden" },
			renderer:function(v,x,r){
				return "<img src='"+imgpath+"/delete.png' alt='删除作业人员' onclick='del(" + v + ")' style='cursor:pointer'/>";
			}
		}]
	});
	WorkStationEmp.filterBy = function(){
		var emp_code = Ext.get("emp_code");
		var emp_name = Ext.get("emp_name");		
		var searchArray = [];
		if(!Ext.isEmpty(emp_code) && !Ext.isEmpty(emp_code.getValue())){
			var searchParam = {};
			searchParam.property = "empcode";
			searchParam.value = emp_code.getValue();
			searchParam.anyMatch = true;
			searchParam.caseSensitive = false;
			searchArray.push(searchParam);
		}
		if(!Ext.isEmpty(emp_name) && !Ext.isEmpty(emp_name.getValue())){
			var searchParam = {};
			searchParam.property = "empname";
			searchParam.value = emp_name.getValue();
			searchParam.anyMatch = true;
			searchParam.caseSensitive = false;
			searchArray.push(searchParam);
		}
		WorkStationEmp.NoDispatchGrid.store.filter(searchArray);
	}
	//未选择人员Grid
	WorkStationEmp.NoDispatchGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/omEmployeeSelect!findListForZbPlanRecordEmpSelect.action',                 //装载列表数据的请求URL    
	    tbar:['<span style="font-weight:normal">未选择人员</span>','-',
	    	  '人员代码   ',{xtype:'textfield',id:'emp_code',width:70},
	    	  '人员名称   ',{xtype:'textfield',id:'emp_name',width:70},
	    	 {
		        text:"查询", iconCls:"searchIcon", handler: function(){
		        	WorkStationEmp.filterBy();
		       	}
	    }],
	    page:false,
	    singleSelect:true,hideRowNumberer:true, storeAutoLoad:false,
		fields: [{
			header:'人员id', dataIndex:'empid', hidden:true, editor: { xtype:'hidden' }	
		},{
			header:'人员代码', dataIndex:'empcode', editor:{  maxLength:2 }
		},{		
			header:'人员名称', dataIndex:'empname', editor:{ xtype:'numberfield', maxLength:18 }
		},{
			header:'操作', dataIndex:'empid', editor:{ xtype:"hidden" },
			renderer:function(v,x,r){
				return "<img src='"+imgpath+"/add.png' alt='添加作业人员' onclick='add(" + v + ")' style='cursor:pointer'/>";
			}
		}]
	});	
	
	//移除事件
	WorkStationEmp.grid.un('rowdblclick',WorkStationEmp.grid.toEditFn,WorkStationEmp.grid);
	WorkStationEmp.NoDispatchGrid.un('rowdblclick',WorkStationEmp.NoDispatchGrid.toEditFn,WorkStationEmp.NoDispatchGrid);
	
	//定义点击确定按钮的操作
	WorkStationEmp.submit = function(){
		alert("请覆盖方法（WorkStationEmp.submit）！");
	}
	
	
	//定义选择窗口
	WorkStationEmp.selectWin = new Ext.Window({
		title:"作业人员选择", width:800, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
    	maximizable:false, items:[{
		    region : 'center', layout : 'fit', bodyBorder: false, items : {
		    	xtype: "panel", layout: "border",
		    	items:[{
		    		region: 'west', layout: "fit", width :390, minSize : 300, split : true, maxSize : 450, bodyBorder: false,items: [ WorkStationEmp.NoDispatchGrid]
		    	},{
		    		 region : 'center', layout : 'fit', bodyBorder: false, items : [ WorkStationEmp.grid ]
		    	}]
			}
    	}],modal:true,
    	buttons: [{
			text : "确定",iconCls : "saveIcon", handler: function(){
				WorkStationEmp.submit(); 
			}
		},{
	        text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ WorkStationEmp.selectWin.hide(); }
		}]
	});
	
	//已选择过滤
	WorkStationEmp.grid.store.on("beforeload",function(){
	    this.baseParams.cardId = WorkStationEmp.idx; 
	    this.baseParams.orgseq = WorkStationEmp.orgseq;
	    this.baseParams.type = "type";
	});
	//未选择过滤
	WorkStationEmp.NoDispatchGrid.store.on("beforeload",function(){
		this.baseParams.cardId = WorkStationEmp.idx; 
		this.baseParams.orgseq = WorkStationEmp.orgseq;
	});
	
	
});