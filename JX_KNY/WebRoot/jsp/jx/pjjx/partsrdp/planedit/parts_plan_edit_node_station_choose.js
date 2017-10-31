/**
 * 配件检修工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsWorkStation');                       //定义命名空间
	
	var grid, win, rdpNodeIdx, callback, initialized = false;
	
	function createGrid(){
		grid = new Ext.yunda.Grid({
		    loadURL: ctx + '/partsWorkStation!pageQuery.action',                 //装载列表数据的请求URL
		    saveURL: ctx + '/partsWorkStation!saveOrUpdate.action',             //保存数据的请求URL
		    deleteURL: ctx + '/partsWorkStation!logicDelete.action',            //删除数据的请求URL
		    storeAutoLoad: false,
		    tbar : ['工位名称',{	            
		            xtype:"textfield",								                
		            name : "workStationName",
		            id:"workStationNameId"
				},{
					text : "搜索",
					iconCls : "searchIcon",
					handler : function(){
						PartsWorkStation.workStationName = Ext.getCmp("workStationNameId").getValue();				
						PartsWorkStation.grid.getStore().load();
						
					},
					title : "按输入框条件查询",
					scope : this
				},'refresh'],
			fields: [{
				header:'idx主键', dataIndex:'idx', hidden:true
			},{
				header:'工位编码', dataIndex:'workStationCode', width:80
			},{
				header:'工位名称', dataIndex:'workStationName',width:100
			},{
				header:'流水线主键', dataIndex:'repairLineIdx', hidden:true
			},{
				header:'流水线名称', dataIndex:'repairLineName'
			},{
				header:'备注', dataIndex:'remarks'
			}],
			toEditFn: notEidt
		});
		
		grid.store.on('beforeload',function(){//Work_Station_IDX
			var sqlStr = " not exists(select 1 from pjjx_parts_rdp_node_station s where s.work_station_idx = this_.idx and s.rdp_node_idx = '"
				+ rdpNodeIdx + "' and s.record_status=0)";
			var whereList = [{sql: sqlStr, compare: Condition.SQL}]
		    this.baseParams.whereListJson=Ext.util.JSON.encode(whereList);
		});
	}
	
	function createWin(){
		win = new Ext.Window({
			title:"工位选择", width:600, height:400, plain:true,
			closeAction:"hide", buttonAlign:'center', layout:'fit',
			maximizable:false, modal:true,
			items: grid ,
			buttons: [{
				text : "确定",
				iconCls : "saveIcon",
				handler: function(){
					submit(); 
				}
			},{
		        text: "关闭",
		        iconCls: "closeIcon",
		        handler: function(){
					win.hide();
				}
			}]
		});
	}
	
	function submit(){
		var station = grid.selModel.getSelections();
		if(station.length == 0){
			MyExt.Msg.alert("尚未选择一条记录！")
			return;
		}
		
		var datas = [];
		for(var i = 0; i < station.length; i++){
			var data = MyJson.clone(station[i].data);
			data.rdpNodeIdx = rdpNodeIdx;
			data.workStationIdx = data.idx;
			delete data.idx;
			delete data.remarks;
			datas.push(data);
		}
		callback(datas, grid);
	}
	
	function initialize(){
		createGrid();
		createWin();
		initialized = true;
	}
	
	PartsWorkStation.showSelect = function(_rdpNodeIdx, _callback){
		rdpNodeIdx = _rdpNodeIdx;
		if(initialized == false) initialize();
		win.show();
		grid.store.load();
		callback = _callback;
	}
});