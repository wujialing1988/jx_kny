Ext.onReady(function(){
	//修次选择
	Ext.namespace('RT');                       //定义命名空间
	//修次选择列表
	RT.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/rT!pageQuery.action',                 //装载列表数据的请求URL
	    storeAutoLoad: false,
	    tbar: [{
	    	text: "确定", iconCls: "saveIcon", handler: function(){
	    		var sm = RT.grid.getSelectionModel();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("尚未选择一条记录！");
	                return;
	            }
	            var xcData = sm.getSelections();
	            var dataAry = new Array();
	            for (var i = 0; i < xcData.length; i++){
	            	var data = {};
	            	data.rcIDX = RcRt.rcIDX; //修程主键
	            	data.repairtimeIDX = xcData[i].get("rtID");  //修次编码
	            	data.repairtimeName = xcData[i].get("rtName"); //修次名称
	                dataAry.push(data);
	            }
	            this.loadMask.show();
	            Ext.Ajax.request({
	                url: ctx + '/rcRt!saveOrUpdateList.action',
	                jsonData: dataAry,
	                success: function(response, options){
	                  	this.loadMask.hide();
	                    var result = Ext.util.JSON.decode(response.responseText);
	                    if (result.errMsg == null) {
	                        alertSuccess();
	                        RT.grid.store.reload();
	                        RcRt.grid.store.reload();
	                        RT.selectWin.hide();
	                    } else {
	                        alertFail(result.errMsg);
	                    }
	                },
	                failure: function(response, options){
	                    this.loadMask.hide();
	                    MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
	                }
	            })
	    	},
	    	scope: this
	    },{
	    	text: "关闭", iconCls: "closeIcon", handler: function(){
	    		RT.selectWin.hide();
	    	}
	    }],
		fields: [{
			header:'修次编码', dataIndex:'rtID', editor:{   }
		},{
			header:'修次名称', dataIndex:'rtName', editor:{   }
		}]
	});
	RT.selectWin = new Ext.Window({
		title:"选择修次", width:320, height:280, closeAction:"hide", modal:true, layout:"fit", items:RT.grid
	});
	//移除监听
	RT.grid.un('rowdblclick', RT.grid.toEditFn, RT.grid);
	RT.grid.store.on('beforeload' , function(){
		var whereList = [] ;
		var sqlStr = " RT_ID not in (select nvl(t.repair_time_idx,'-1') from jxpz_rc_rt t where t.record_status=0 and t.rc_idx = '"+RcRt.rcIDX+"')" ;
		whereList.push({sql: sqlStr, compare: Condition.SQL});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	
	Ext.namespace('RcRt');                       //定义命名空间
	RcRt.rcIDX = "";
	//修程列表
	RcRt.rcGrid =  new Ext.yunda.Grid({
		loadURL: ctx + "/xC!pageQuery.action",
		singleSelect: true,
		tbar:[],
		storeId:'xcID',
		fields: [{
			header:'修程编码', dataIndex:'xcID', editor:{   }
		},{
			header:'修程名称', dataIndex:'xcName', editor:{   }
		}]
	});
	RcRt.rcGrid.on("rowclick", function(grid, rowIndex, e){
		var record = grid.getStore().getAt(rowIndex);
		RcRt.rcIDX = record.get("xcID");
		RcRt.grid.store.load();
	});
	
	RcRt.rcGrid.store.on('beforeload' , function(){
		var whereList = [] ;
		whereList.push({ propName: 'vehicleType', propValue:vehicleType , compare: Condition.EQ, stringLike: false });
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});	
	
	//移除侦听器
	RcRt.rcGrid.un('rowdblclick', RcRt.rcGrid.toEditFn, RcRt.rcGrid);
	//修程对应修次
	RcRt.grid = new Ext.yunda.RowEditorGrid({
		loadURL: ctx + '/rcRt!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/rcRt!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/rcRt!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar:[{text: "选择修次", iconCls: "addIcon" , handler: function(){
	    		var sm = RcRt.rcGrid.getSelectionModel();
	            if (sm.getCount() < 1) {
	                MyExt.Msg.alert("请先选择一条修程记录！");
	                return;
	            }
	    		RT.selectWin.show();
	    		RT.grid.store.load();
	    	}
	    },'delete'],
	    fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'修程主键', dataIndex:'rcIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'修次编码', dataIndex:'repairtimeIDX', editor:{  maxLength:8 ,disabled: true }
		},{
			header:'修次名称', dataIndex:'repairtimeName', editor:{  maxLength:50 ,disabled: true }
		},{
			header:'修次顺序', dataIndex:'repairtimeSeq', editor:{vtype:'positiveInt', maxLength:8 , allowBlank: false }
		}]
	});
	RcRt.grid.store.on('beforeload' , function(){
		var sm = RcRt.rcGrid.getSelectionModel();
        if (sm.getCount() < 1) {
            MyExt.Msg.alert("请先选择一条修程记录！");
            return;
        }
		var searchParam = {};
		searchParam.rcIDX = RcRt.rcIDX;
		var whereList = [] ;
		for (prop in searchParam) {			
	        if(prop == 'rcIDX'){
				whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
			}else{
	        	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
			}
		}		
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	RcRt.panel =  new Ext.Panel( {
	    layout : 'border',
	    items : [ {
	        title: '修程', width: 350, minSize: 160, maxSize: 400, split: true, region: 'west', bodyBorder: false,
	        collapsible : true,
	        autoScroll: true, layout: 'fit', items : [ RcRt.rcGrid ]
	    }, {
	        title: '修程对应修次', region : 'center', layout: 'fit', bodyBorder: false, items: [ RcRt.grid ]
	    } ]
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:RcRt.panel });
});