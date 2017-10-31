//弹出选择情况窗口
var showSelectWin = function(rowIndex){
	var record = HandOverModelList.grid.getStore().getAt(rowIndex);
	var orginalRecord = orginalRecords[rowIndex];
	rowNum = rowIndex;
	ZbglHoModelItemResultSelect.grid.store.on("beforeload",function(){
		var searchParam = {};
		searchParam.handOverItemIDX = orginalRecord.idx;
		var whereList = [];
		for (prop in searchParam) {
			if(prop == 'handOverItemIDX'){
				whereList.push({propName:prop, propValue: searchParam[prop], stringLike: false}) ;
			}else{
		    	whereList.push({propName:prop, propValue: searchParam[prop]}) ;
			}
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);	
	})
	ZbglHoModelItemResultSelect.grid.store.load();
	ZbglHoModelItemResultSelect.selectWin.show();
}
	//清空【情况记录】
var clearDec = function(rowIndex){
	var record = HandOverModelList.grid.getStore().getAt(rowIndex);
	record.data.handOverResultDesc = "";
//	HandOverModelList.grid.getView().refresh();
	HandOverModelList.grid.getView().refreshRow(record);
}
//点击复选框事件：保存页面缓存的【结果记录】
var checkboxClick = function(rowIndex,id){
	var record = HandOverModelList.grid.getStore().getAt(rowIndex);	
	var orginalRecord = orginalRecords[rowIndex].handOverItemStatus;//原始记录
	var statusRecord = record.data.handOverItemStatus;//页面缓存记录
	if(!Ext.isEmpty(orginalRecord)){
		var statusArray = orginalRecord.split(",");
		var handOverItemStatus = "";
		var checkbox = document.getElementById(id);
		var status = checkbox.value;
		var statusRecordArray = statusRecord.split(",");
		for (var i = 0; i < statusArray.length; i++){
			var isCheck = false;
			//勾选记录加入新页面缓存
			if(statusArray[i] == status && checkbox.checked) {
				isCheck = true;
			}
			//非勾选记录中，如页面缓存已存在，也加入新的页面缓存
			else{
				for(var j = 0; j < statusRecordArray.length;j++){
					if(statusArray[i] == statusRecordArray[j] && statusRecordArray[j] != status){
						isCheck = true;
						break;
					}
				}
			}
			//页面缓存为空时，新勾选记录加入新页面缓存
			if(Ext.isEmpty(statusRecord)){
				record.data.handOverItemStatus = status;
				return;
			}
			//加入新页面缓存
			if(isCheck) handOverItemStatus+= statusArray[i]+",";
		}
		handOverItemStatus = handOverItemStatus.substring(0,handOverItemStatus.length-1);
		record.data.handOverItemStatus = handOverItemStatus;
	}
}
Ext.onReady(function(){
	Ext.namespace('HandOverModelList');                       //定义命名空间
	HandOverModelList.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: "正在处理，请等待......" });
	HandOverModelList.sm = new Ext.grid.CheckboxSelectionModel({singleSelect:true});
	HandOverModelList.store = new Ext.data.JsonStore({
		id: "idx", root: "root", totalProperty: "totalProperty", autoLoad: true,
	    url: ctx + "/mobile!getHoModelItem.action",
	    fields: [ "idx","handOverItemName", "handOverItemStatus","parentItemName", "parentIDX","handOverResultDesc"]
	});
	//交接情况列表
	HandOverModelList.grid = new Ext.grid.EditorGridPanel({	
		border: false, enableColumnMove: true, stripeRows: true, 
	    viewConfig: {forceFit: true}, selModel: HandOverModelList.sm,
	    clicksToEdit: 1,
	    store: HandOverModelList.store, loadMask: HandOverModelList.loadMask,
		hideRowNumberer: true,
		colModel: new Ext.grid.ColumnModel([
			{ header:'idx', dataIndex:'idx',hidden: true},
			{ header:'parentIDX', dataIndex:'parentIDX',hidden: true},
	    	{ header: "交接项",  dataIndex: "parentItemName",width:100},
	    	{ header: "检查项",  dataIndex: "handOverItemName",width:100},
	    	{ header: "结果记录",  dataIndex: "handOverItemStatus",width:100,
	    	  renderer:function(v, metaData, record, rowIndex, colIndex, store){
	    	  	var html = "";
	    	  	//初始显示
	    	  	if(!Ext.isEmpty(v) ){
	    	  		var statusArray = v.split(",");
					for (var i = 0; i < statusArray.length; i++){
						var id = (rowIndex+1) + "" + (i+1);
						html+= "<INPUT TYPE='checkbox' id='" + id + "' CHECKED onclick='checkboxClick(" + rowIndex + "," + id + ")' value = " + statusArray[i] + ">"+statusArray[i];
					}
	    	  	}
	    	  	//加载后显示
	    	  	if (orginalRecords.length > 0 && !Ext.isEmpty(orginalRecords[rowIndex].handOverItemStatus)){
	    	  		html = "";
	    	  		var orginalRecord = orginalRecords[rowIndex];
	    	  		var statusArray = orginalRecord.handOverItemStatus.split(",");
	    	  		var statusRecordArray = "";
	    	  		if(!Ext.isEmpty(record.data.handOverItemStatus) ) statusRecordArray = record.data.handOverItemStatus.split(",");
					for (var i = 0; i < statusArray.length; i++){
						var id = (rowIndex+1) + "" + (i+1);
						var isCheck = "";
						for (var j = 0; j < statusRecordArray.length; j++){	
							//页面缓存中存在就勾选
							if(statusRecordArray[j] == statusArray[i]){
								isCheck = "checked";
								break;
							}
						}
						html+= "<INPUT TYPE='checkbox' id='" + id + "'" + isCheck + " onclick='checkboxClick(" + rowIndex + "," + id + ")' value = " + statusArray[i] + ">"+statusArray[i]+"</input>";
					}
	    	  	}
	    	  	return html;
	    	  }
	    	},
	    	{ header: "情况记录",  dataIndex: "handOverResultDesc",width:200,
	    	  editor:new Ext.form.TextField({
                maxLength:1000
	    	  })
	    	},
	    	{ header: "情况选择",  dataIndex: "",
	    	  renderer: function(value, metaData, record, rowIndex, colIndex, store){
	    	  	var html = "<INPUT TYPE='button' VALUE='选择' onclick='showSelectWin("+ rowIndex +")' title='情况选择'>&nbsp;&nbsp;";
	    	  	html+="<INPUT TYPE='button' VALUE='清空' onClick='clearDec("+ rowIndex +")' title='清空情况记录'>&nbsp;&nbsp;";
	    	  	return html;
	    	  }
	        }
		])	
	});
	HandOverModelList.grid.store.on("load", function(store,records){
		orginalRecords = [];
		if(records.length > 0){
			for(var i = 0;i < records.length; i++){
				var orginalRecord = {};
				orginalRecord.handOverItemStatus = records[i].data.handOverItemStatus;
				orginalRecord.idx = records[i].data.idx;
				orginalRecords.push(orginalRecord);
			}
		}
	})
});