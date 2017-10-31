Ext.onReady(function(){
	Ext.namespace('MatTypeSelect');
	MatTypeSelect.searchOftenParam = {};
	MatTypeSelect.searchParam = {};
	//确定
	MatTypeSelect.confirm = function(grid){
		//未选择记录，直接返回
		if(!$yd.isSelectedRecord(grid)) return;
		var datas = grid.selModel.getSelections();
		var dataArray = new Array();
		for(var i = 0; i < datas.length;i++){
			var data = {};
			data.matCode = datas[i].get("matCode");
			data.matDesc = datas[i].get("matDesc");
			data.unit = datas[i].get("unit");
			data.price = datas[i].get("price");
			data.rdpIDX = dataParam.rdpIDX;
			data.trainTypeIDX = dataParam.trainTypeIDX;
			data.trainNo = dataParam.trainNo;
			data.trainTypeShortName = dataParam.trainTypeShortName;
			data.repairClassIDX = dataParam.repairClassIDX;
			data.repairClassName = dataParam.repairClassName;
			data.repairtimeIDX = dataParam.repairtimeIDX;
			data.repairtimeName = dataParam.repairtimeName;
			data.repairOrgID = dataParam.repairOrgID;
			dataArray.push(data);
		}
		var cfg = {
            url: ctx + "/matConsumeInfo!saveInfo.action", 
            jsonData: dataArray,
            success: function(response, options){
                if(grid.loadMask)   grid.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null && result.success == true ) {
                    grid.store.reload();
                    alertSuccess();
                    MatTypeSelect.win.hide();                   
					MatConsumeInfo.grid.store.load();
                } else {
                    alertFail(result.errMsg);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	MatTypeSelect.operate = function(grid,type){
		//未选择记录，直接返回
		if(!$yd.isSelectedRecord(grid)) return;
		var datas = grid.selModel.getSelections();
		var dataArray = new Array();
		for(var i = 0; i < datas.length;i++){
			var data = {};
			data.matCode = datas[i].get("matCode");
			data.repairOrgID = teamOrgId;
			dataArray.push(data);
		}
		var cfg = null;
		//进入常用物料
		if(type == 'inCome'){
			cfg = {
	            url: ctx + "/matUnionTeam!saveInfo.action", 
	            jsonData: dataArray,
	            success: function(response, options){
	                if(grid.loadMask)   grid.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.success == true ) {
	                    grid.store.reload();
	                    alertSuccess();               
						MatTypeSelect.oftenGrid.store.load();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
		}
		//删除常用物料
		else if(type == 'outCome'){
			cfg = {
	            url: ctx + "/matUnionTeam!deleteInfo.action", 
	            jsonData: dataArray,
	            success: function(response, options){
	                if(grid.loadMask)   grid.loadMask.hide();
	                var result = Ext.util.JSON.decode(response.responseText);
	                if (result.errMsg == null && result.success == true ) {
	                    grid.store.reload();
	                    alertSuccess();               
						MatTypeSelect.grid.store.load();
	                } else {
	                    alertFail(result.errMsg);
	                }
	            }
	        };
		}
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	}
	//常用物料
	MatTypeSelect.oftenGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!pageQuery.action',                 //装载列表数据的请求URL	    
	    tbar:['search',
	    	{ text:"确定", iconCls:"saveIcon", 
              handler:function(){
              	MatTypeSelect.confirm(MatTypeSelect.oftenGrid);
              }
	        },{ text:"删除常用物料", iconCls:"deleteIcon", 
              handler:function(){
              	MatTypeSelect.operate(MatTypeSelect.oftenGrid,'outCome');
              }
	        }],
	    storeAutoLoad: false,
	    //viewConfig: null,
		fields: [{
			header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50, allowBlank: false }, width: 120
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:50, allowBlank: false }, width: 420
		},{
			header:'计量单位', dataIndex:'unit', editor:{  maxLength:20, allowBlank: false },
			searcher: {disabled: true}, width: 80				
		},{
			header:'计划单价', dataIndex:'price',  editor:{ xtype:"numberfield", maxLength:12, vtype: 'nonNegativeFloat'  },
			searcher: {disabled: true}		
		}],
		storeId: 'matCode',
		searchFn: function(searchParam){
			MatTypeSelect.searchOftenParam = searchParam ;
			this.store.load();
		}
	});
	//全部物料
	MatTypeSelect.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!pageQuery.action',                 //装载列表数据的请求URL	    
	    tbar:['search',
	    	{ text:"确定", iconCls:"saveIcon", 
              handler:function(){
              	MatTypeSelect.confirm(MatTypeSelect.grid);
              }
	        },{ text:"进入常用物料", iconCls:"saveIcon", 
              handler:function(){
              	MatTypeSelect.operate(MatTypeSelect.grid,'inCome');
              }
	        }],
	    storeAutoLoad: false,
	    //viewConfig: null,
		fields: [{
			header:'物料编码', dataIndex:'matCode', editor:{  maxLength:50, allowBlank: false }, width: 120
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:50, allowBlank: false }, width: 420
		},{
			header:'计量单位', dataIndex:'unit', editor:{  maxLength:20, allowBlank: false },
			searcher: {disabled: true}, width: 80				
		},{
			header:'计划单价', dataIndex:'price',  editor:{ xtype:"numberfield", maxLength:12, vtype: 'nonNegativeFloat'  },
			searcher: {disabled: true}		
		}],
		storeId: 'matCode',
		searchFn: function(searchParam){
			MatTypeSelect.searchParam = searchParam ;
			this.store.load();
		}
	});
	MatTypeSelect.oftenGrid.un('rowdblclick', MatTypeSelect.oftenGrid.toEditFn, MatTypeSelect.oftenGrid);
	MatTypeSelect.grid.un('rowdblclick', MatTypeSelect.grid.toEditFn, MatTypeSelect.grid);
	MatTypeSelect.oftenGrid.createSearchWin();
	MatTypeSelect.oftenGrid.searchWin.modal = true;
	MatTypeSelect.grid.createSearchWin();
	MatTypeSelect.grid.searchWin.modal = true;
	
	MatTypeSelect.oftenGrid.store.on("beforeload", function(){
		var searchParam = MatTypeSelect.searchOftenParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		var sqlStr = " mat_code in (select MAT_CODE from JXGC_MAT_UNION_TEAM where ORGID = " + teamOrgId + " and record_status = 0)";//当前班组的常用物料
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	MatTypeSelect.grid.store.on("beforeload", function(){
		var searchParam = MatTypeSelect.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		var sqlStr = " mat_code not in (select MAT_CODE from JXGC_MAT_UNION_TEAM where ORGID = " + teamOrgId + " and record_status = 0)";//去除当前班组的常用物料
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	MatTypeSelect.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            title: "常用物料", layout: "fit", border: false, items:[MatTypeSelect.oftenGrid]
	        },{
	        	title: "全部物料", layout: "fit", border: false , items:[MatTypeSelect.grid]
	        }]
	});
	MatTypeSelect.win = new Ext.Window({
		title:"物料清单选择", width: 700, height: 400, closeAction:"hide", modal:true, layout:"fit",
		items:[MatTypeSelect.tabs]
	});
});