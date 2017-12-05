/**
 * 编组信息维护
 */
Ext.onReady(function(){
	Ext.namespace('MarshallingTrain');                       //定义命名空间
	//定义全局变量保存查询条件
	MarshallingTrain.searchParam = {} ;
	MarshallingTrain.nodes = new vis.DataSet(); // 数据集对象
	MarshallingTrain.edges = new vis.DataSet();
	MarshallingTrain.network = null; // network对象
	MarshallingTrain.marshallingCode = "###" ; // 编组编号
	MarshallingTrain.marshallingStatus = "###" ; 
	MarshallingTrain.trainCount = 1;
	MarshallingTrain.planSuffixChar = '_';			// 计划对比数据主键字符后缀
	MarshallingTrain.seqNo = 1;			// 计划对比数据主键字符后缀
	var processTips;
	/*
	 * 显示处理遮罩
	 */
	function showtip(msg){
		processTips = new Ext.LoadMask(Ext.getBody(),{
			msg: msg || "正在处理你的操作，请稍后...",
			removeMask:true
		});
		processTips.show();
	};
	
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		processTips.hide();
	};

	// 覆盖MatInforList.submit()方法，添加配件检修用料信息
	JczlTrainServiceWin.submit = function(){
		//判断是否选择了数据
		var grid = JczlTrainServiceWin.grid;
		if(!$yd.isSelectedRecord(grid)) {
			MyExt.Msg.alert("请选择一条数据！");
			return;
		}
	   var records = grid.getSelectionModel().getSelections();
	   var datas = new Array();
	   for(var i=0; i<grid.getSelectionModel().getCount(); i++){
	   	  var data = {} ;
		  var  temp = records[i].data;
		  data.vehicleKindName = temp.vehicleKindName
		  data.vehicleKindCode = temp.vehicleKindCode
		  data.trainNo = temp.trainNo
		  data.trainTypeShortName = temp.trainTypeShortName
		  data.trainTypeIDX = temp.trainTypeIDX
		  data.marshallingCode = MarshallingTrain.marshallingCode;
		  data.seqNo = MarshallingTrain.seqNo++;
		  datas.push(data);
	   }
		var cfg = {
	        url: ctx + "/marshallingTrain!saveOrUpdateList.action", 
			params : {datas : Ext.util.JSON.encode(datas)},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                MarshallingTrain.grid.store.load();
	                marshalling.grid.store.load();
	                JczlTrainServiceWin.addWin.hide();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "确认是否添加车辆信息？  ", function(btn){
	        if(btn != 'yes')    return;
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	};
	
	// 修改车辆数
	MarshallingTrain.updateMarshalling = function(nodeID){
		var data = {};
		var array = nodeID.split(MarshallingTrain.planSuffixChar);
		data.trainCount = array[0]; 
		data.idx = array[1]; 
		var cfg = {
	        url: ctx + "/marshalling!updateMarshalling.action", 
			params : {data : Ext.util.JSON.encode(data)},
	        timeout: 600000,
	        success: function(response, options){
	        	if(processTips) hidetip();
	            var result = Ext.util.JSON.decode(response.responseText);
	            if (result.errMsg == null && result.success == true) {
	                alertSuccess();
	                MarshallingTrain.grid.store.load();
	                marshalling.grid.store.load();
	            } else {
	                alertFail(result.errMsg);
	            }
	        },
	        failure: function(response, options){
	        	if(processTips) hidetip();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
	    };
	    Ext.Msg.confirm("提示  ", "该操作不能恢复是否继续？  ", function(btn){
	        if(btn != 'yes'){ 
	        	MarshallingTrain.grid.store.load();
	            marshalling.grid.store.load();return;
            }
	        showtip();
	        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
	    });
	}
	
	/**-----车辆表格---------------------------*/
	MarshallingTrain.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/marshallingTrain!pageList.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/marshallingTrain!logicDelete.action',            //删除数据的请求URL
	    tbar: ['search',
//	    	{
//	    	text : "新增", iconCls : "addIcon",
//			handler : function(){
//				JczlTrainServiceWin.addWin.show();
//			}},
			'delete',{
	    	text : "刷新", iconCls : "refreshIcon",
			handler : function(){
				MarshallingTrain.grid.store.reload();
			}},'->',{
				xtype : 'label',
				text : '双击添加车辆'},'-', {
				xtype : 'label',
				text : "点击[edit]添加或删除车辆"}], 
	    labelWidth: 100,                                     //查询表单中的标签宽度
	    fieldWidth: 180,
		fields: [{
			header:'编组编号', dataIndex:'marshallingCode',width: 50,hidden: true, searcher: { hidden: true }
		},{
			header:'顺序号', dataIndex:'seqNo',width: 60
		},{
			header:'车种', dataIndex:'vehicleKindName',width: 60
		},{
			header:'车种编码', dataIndex:'vehicleKindCode',hidden:true, width: 60, editor: {xtype:"hidden",id:"vehicleKindCode"},searcher: { hidden: true }
		},{
			header:'车号', dataIndex:'trainNo',width:60
		},{
			header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" },searcher: { hidden: true }
		},{
			header:'车型', dataIndex:'trainTypeShortName',width: 60, searcher: { hidden: true }
		},{
			header:'车型idx', dataIndex:'trainTypeIDX',hidden:true, width: 60, searcher: { hidden: true }
		}],
		toEditFn: Ext.emptyFn,  //覆盖双击编辑事件
		searchFn: function(searchParam){ 
			MarshallingTrain.searchParam = searchParam ;
	        MarshallingTrain.grid.store.load();
		}
	});
	
	//查询前添加过滤条件
	MarshallingTrain.grid.store.on('beforeload' , function(){
	   var searchParam = MarshallingTrain.searchParam ;
	   var sm = marshalling.grid.getSelectionModel();
	   if (sm.getCount() > 0) {
			var records = sm.getSelections();
			searchParam.marshallingCode = records[0].data.marshallingCode;
		}		
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
	});
	
	// 添加加载结束事件
	MarshallingTrain.grid.getStore().addListener('load',function(me, records, options ){
	       	MarshallingTrain.setNetwork(records);
	});
	
	// 添加network条件项
	MarshallingTrain.options = {
		  locale: 'ch',
		  manipulation: {
		     addEdge:false,
		     addNode: false,
			 deleteNode: function(nodeData,callback) {
		      	MarshallingTrain.updateMarshalling(nodeData.nodes[0]);
		        callback(nodeData);
		    },
		     addNode: function(nodeData, callback) {
		     	var heightY = parseInt(MarshallingTrain.trainCount / 10);
			    var widthX = (MarshallingTrain.trainCount+1) - heightY * 10 ;
		    	var bodyImgUrl = getBodyImgUrl('', getBodyColorByStatus(MarshallingTrain.marshallingStatus));
				nodeData.id = (MarshallingTrain.trainCount + 1) + MarshallingTrain.planSuffixChar + MarshallingTrain.idx;
				nodeData.label = MarshallingTrain.trainCount+1 + "辆";
				nodeData.size = 50 ;
				nodeData.x = -50 + widthX * 153;
				nodeData.y = -500 + heightY * 100;
				nodeData.value = MarshallingTrain.idx;
				nodeData.fixed = true;
				nodeData.shape = 'image';
				nodeData.image = bodyImgUrl;
				nodeData.font = { size : 10,strokeWidth : 3,	'face' : 'Monospace',	align : 'left'};
				MarshallingTrain.updateMarshalling(nodeData.id);
		        callback(nodeData);
		    }
		  }
	}
	MarshallingTrain.data = {
		nodes : MarshallingTrain.nodes,
		edges : MarshallingTrain.edges
	};
	
	// 刷新图表
	MarshallingTrain.setNetwork = function(marshallingTrainList) {
		// 清空节点
		MarshallingTrain.nodes.clear();
		var sm = marshalling.grid.getSelectionModel();
		if (sm.getCount() == 0) {
			return ;
		}		
		var records = sm.getSelections();
		var record = records[0];
		MarshallingTrain.marshallingCode = record.data.marshallingCode;
		MarshallingTrain.idx = record.data.idx;
		MarshallingTrain.trainCount = record.data.trainCount;
		var headImgUrl = getHeadImgUrl('', getHeadColorByStatus(''));
		// 添加车头
		MarshallingTrain.nodes.add({
			id : "id_train_head",
			label : "车头",
			size : 50,
			x : -50 ,
			y : -500,
			value : "车头",
			fixed : true,
			shape : 'image',
			image : headImgUrl,
			font : {
				size : 10,
				strokeWidth : 3,
				'face' : 'Monospace',
				align : 'left'
			}
		});
		// 添加车辆
		for (var k = 0; k < record.data.trainCount; k++) {
			var heightY =parseInt(k / 10);
			var widthX = (k+1) - heightY * 10 ;
			var title = '';
			if(marshallingTrainList.length > 0){
				for(var i = 0; i< marshallingTrainList.length;i++){
					var trainData = marshallingTrainList[i].data;
					if((trainData.seqNo-1) == k){
						var vehicleKindName = Ext.isEmpty(trainData.vehicleKindName) ? "" : trainData.vehicleKindName ;
						var trainNo = Ext.isEmpty(trainData.trainNo) ? "" : trainData.trainNo ;
						title = vehicleKindName + trainNo;
						break;
					}
				}
			}
			var bodyImgUrl = getBodyImgUrl(title, getBodyColorByStatus(MarshallingTrain.marshallingStatus));
			// 添加【配件流程节点显示】
			MarshallingTrain.nodes.add({
				id : k+1 + MarshallingTrain.planSuffixChar + record.data.idx,
				label : k+1 + "辆",
				size : 50,
				x : -50 + widthX * 153,
				y : -500 + heightY * 100,
				value : record.data.idx,
				fixed : true,
				shape : 'image',
				image : bodyImgUrl,
				font : {
					size : 12,
					strokeWidth : 3,
					'face' : 'Monospace',
					align : 'left'
				}
			});
		}
		// 创建实例
		if(!MarshallingTrain.network){
			MarshallingTrain.network = new vis.Network(document.getElementById('visualization'),
					MarshallingTrain.data, 	MarshallingTrain.options);
		}
		// 点击事件
     	MarshallingTrain.network.on("doubleClick", function(params, boolens) {
	        if (params.nodes.length == 1) {
	        	  var array = params.nodes[0].split(MarshallingTrain.planSuffixChar);
	        	  if(array.length == 2){
	        		  MarshallingTrain.seqNo = array[0];
	        		  JczlTrainServiceWin.addWin.show();
	        	  }
	            }
		});
		
		// 选中事件
     	MarshallingTrain.network.on("selectNode", function(params, boolens) {
     		var bodyImgUrl = getBodyImgUrl(title, getBodyColorByStatus("SELECTED"));
	        if (params.nodes.length == 1) {
	        	
	       	}
		});
	}

	MarshallingTrain.MarshallingTrainVis = new Ext.Panel({
		region : 'center',
		border : false,
		autoScroll : false,
		anchor : '100%',
		tbar : [],
		html : [
				'<div style="background-color:black" id= "visualization">',
				'</div>'].join("")
	});
	
	MarshallingTrain.panel =  new Ext.Panel({
	    layout : 'border',frame:true,
	    items : [ {
	        region: 'center', bodyBorder: false,
	        layout: 'fit', items : [MarshallingTrain.MarshallingTrainVis]
	    }, {
	         region : 'south',   height: 250,  layout: 'fit', bodyBorder: false, items: [MarshallingTrain.grid]
	    }]
	});
});