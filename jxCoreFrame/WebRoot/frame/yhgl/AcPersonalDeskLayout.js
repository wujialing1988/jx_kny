Ext.onReady(function(){
	Ext.namespace('AcPersonalDeskLayout');                       //定义命名空间
	AcPersonalDeskLayout.fieldWidth = 120;
	AcPersonalDeskLayout.labelWidth = 80;
	
	Ext.Ajax.request({
		url: ctx + "/acPersonalDeskLayout!findAcPersonalDeskLayout.action",
		success: function(r){
			var retn = Ext.util.JSON.decode(r.responseText);
			if(retn.success){
				Ext.getCmp("columNumCombo").setValue(retn.columnNum);
			}else{
				alertFail(retn.errMsg);
			}
		},
		failure: function(){
			alertFail("请求超时！");
		}
	});
	
	//树
	AcPersonalDeskLayout.tree = new Ext.tree.TreePanel({
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/acPersonalDeskLayout!getRoleFuncByAppCode.action"
		}),
		beforeload: function(node, e){
          	AcPersonalDeskLayout.tree.getLoader().dataUrl = ctx + '/acPersonalDeskLayout!getRoleFuncByAppCode.action'
      	},
		root: new Ext.tree.AsyncTreeNode({
		    text: '应用功能(子节点双击添加应用)',
	        leaf: false,
	        expanded :true
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	height : 330,
    	listeners : {
    		dblclick: function(node, e) {
    			if(node.leaf){
    				//添加的时候，判断是否已经添加上去
    				
    				//获取总条数
    				var recordCount = AcPersonalDeskLayout.editGrid.store.getCount();
    				if(recordCount > 0){
	    				for(var i = 0;i<recordCount;i++){
	    					//获取每一行对象
	    					var recordV = AcPersonalDeskLayout.editGrid.store.getAt(i);
	    					//比较唯一标示funccode
	    					if(recordV.data.funccode == node.attributes.funccode){
		    					MyExt.Msg.alert("表格中已经有相同的数据！");
								return;
	    					}
	    				}
    				}
    				
    				//新增一行对象，加入editGrid中
   					var record = new Ext.data.Record();
					record.set("sequenceNumber",recordCount+1);
					record.set("funccode",node.attributes.funccode);
					record.set("funcName",node.attributes.funcname);
					record.set("showname",node.attributes.funcname);
					AcPersonalDeskLayout.editGrid.store.add(record);
					
					//添加都是添加的最后一行，所以修改上一行的按钮状态即可
					recordCount = AcPersonalDeskLayout.editGrid.store.getCount();
					var record = AcPersonalDeskLayout.editGrid.store.getAt(recordCount-2);
					//如果上一条数据是表格中的第一条数据，只显示下移
					if(record){
						var lastFunctionCode = record.get("funccode");
						if(record.get("sequenceNumber") == 1){
							Ext.getDom(lastFunctionCode + "_Down").style.display = '';
						//如果上一条数据序号不是1 第二行或者第三行数据
						}else{
							Ext.getDom(lastFunctionCode + "_Down").style.display = '';
							Ext.getDom(lastFunctionCode + "_Up").style.display = '';
						}
					}
    			}
	        }
		}
	});
	
	//列数
	AcPersonalDeskLayout.infoForm = new Ext.form.FormPanel({
		labelWidth:AcPersonalDeskLayout.labelWidth, border: false,
		labelAlign:"center", layout:"column",
		bodyStyle:"padding:10px;",
		defaults:{
			xtype:"container", autoEl:"div", layout:"form", 
			defaults:{
				xtype:"textfield",
				anchor:"100%"
			}
		},
		items:[{
	        items: [{ 
	        	id : 'columNumCombo',
	        	xtype: 'combo',	        	        
				fieldLabel : '显示列数',
				name : 'columNum',
		        hiddenName:'columNum',
		        store:new Ext.data.SimpleStore({
				    fields: ['v', 't'],
					data : [[1, "1"], [2, "2"], [3, "3"]]
				}),
				valueField:'v',
				displayField:'t',
				triggerAction:'all',
				mode:'local',
				value:'1',
				editable: false,
				allowBlank: false
			}]
		}]
	});
	
	//应用editGrid
	AcPersonalDeskLayout.editGrid = new Ext.yunda.EditGrid({
		loadURL: ctx + "/acPersonalDeskLayout!findAcPersonalDeskList.action",
		autoSubmit: false,
		storeAutoLoad: false,
		singleSelect: true,
		isEdit: false,
		defaultTar: false,
		page: false,
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true
		},{
			header:'操作员IDx', dataIndex:'operatorid', hidden:true
		},{
			header: "序号", dataIndex: "sequenceNumber", width:25
		},{
			header: "功能编号", dataIndex: "funccode", width:80
		},{
			header: "功能名称", dataIndex: "funcName",width:80
		},{
			//header: "显示名称", dataIndex: "showname",editor:new Ext.grid.GridEditor(new Ext.form.TextField()),width:150
			header: "显示名称(可自定义窗口名称)", dataIndex: "showname",editor:{ maxLength:60 },width:150
		},{
			header:'操作', layout:"fit",width:100,
		    renderer :function(a,b,c,d){
		    	var str = "";
		    	
		    	//获取总条数
		    	var storeCount = AcPersonalDeskLayout.editGrid.store.getCount();
		    	
		    	// 隐藏按钮考虑 doto upShow upHide downShow downHide
				//var deleteString =  "<img src='" + ctx + "/frame/resources/images/toolbar/delete1.gif' alt='删除' style='cursor:pointer' onclick='AcPersonalDeskLayout.deleteFn(\"" + c.data.funccode + "\")'/>";
				//删除 deleteIcon
		    	var str = str + "<button type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over ' onclick='AcPersonalDeskLayout.deleteFn(\"" + c.data.funccode + "\")'>删除</button>";
		    	if(d == 0){
			    	//如果是第一条,并且总数为1
		    		if(storeCount == 1){
		    			str += getButtonString(c,d,"downHide");
		    			str += getButtonString(c,d,"upHide");
			    	//如果是第一条，并且总数不为1
		    		}else{
		    			str += getButtonString(c,d,"downShow");
		    			str += getButtonString(c,d,"upHide");
		    		}
		    	//如果是最后一行
		    	}else if(d != 0 && d == (storeCount - 1)){
		    		str += getButtonString(c,d,"downHide");
		    		str += getButtonString(c,d,"upShow");
		    	//其他情况
		    	}else{
		    		str += getButtonString(c,d,"downShow");
		    		str += getButtonString(c,d,"upShow");
		    	}
		    
		    	return str;
		    }
		}]
	});
	
	//页面删除行数据
	AcPersonalDeskLayout.deleteFn = function(obj){
		//获取总条数
		var recordCount = AcPersonalDeskLayout.editGrid.store.getCount();
		if(recordCount > 0){
			for(var i = 0;i<recordCount;i++){
				var recordV = AcPersonalDeskLayout.editGrid.store.getAt(i);
				if(recordV && recordV.data.funccode == obj){
					//删除行数据
					AcPersonalDeskLayout.editGrid.store.remove(recordV);  
				}
			}
			
		//重新排列序号
		updateSequenceNumber(AcPersonalDeskLayout.editGrid.store);
		
		//更新上下数据的按钮状态
		updateFnState(AcPersonalDeskLayout.editGrid.store);
		
		}else{
			MyExt.Msg.alert("数据错误！");
			return;
		}
	}
	
	//页面上移数据
	AcPersonalDeskLayout.upFn = function(d){
		var store = AcPersonalDeskLayout.editGrid.store;
		var recordCount = store.getCount();
		
		//当前选中行
		var thisRecord = store.getAt(d);
		//上一行
		var lastRecord = store.getAt(d - 1);
		////交换record1，record2的数据
		setRecordValue(thisRecord,lastRecord);
		
	}
	
	//页面下移数据
	AcPersonalDeskLayout.downFn = function(d){
		var store = AcPersonalDeskLayout.editGrid.store;
		var recordCount = store.getCount();
		//当前选中行
		var thisRecord = store.getAt(d);
		//下一行
		var nextRecord = store.getAt(parseInt(d) + 1);
		////交换record1，record2的数据
		setRecordValue(thisRecord,nextRecord);
	}
	
	//中心panel
	AcPersonalDeskLayout.centerPanel = new Ext.Panel({
		layout: "border",
		items: [{
			region: "west",
			width: 300,
			layout: "fit",
			items: AcPersonalDeskLayout.tree
		},{
			region: "center",
			layout: "fit",
			items: AcPersonalDeskLayout.editGrid
		}]
	});
	
	AcPersonalDeskLayout.panel = new Ext.Panel({
	    layout: "border", 
	    defaults: {
	    	border: false, layout: "fit"
	    },
	    items: [{
	        region: 'north', collapsible:true, collapsed: false,  height: 70, 
	        items:[AcPersonalDeskLayout.infoForm], frame: true
	    },{
	        region : 'center', items: AcPersonalDeskLayout.centerPanel
	    }]
	});
	
	AcPersonalDeskLayout.win = new Ext.Window({
	    title:"用户自定义桌面", 
	    layout: 'fit',
		height: 600, width: 1000,
		items:AcPersonalDeskLayout.panel,
		closable : false,
		buttonAlign: 'center',
		buttons: [{
			text: "保存", iconCls: "saveIcon", handler: function() {
				//判断提交数据是否有空值
				var count = AcPersonalDeskLayout.editGrid.store.getCount();
				var datas = [];
				for(var i = 0; i < count; i++){
					var r = MyJson.clone(AcPersonalDeskLayout.editGrid.store.getAt(i).data);
					
					//判断显示名称是否为空
					if(!r.showname){
						MyExt.Msg.alert("显示名称未输入，请重新输入！");
						return;
					}
					if(r.showname.length > 40){
						MyExt.Msg.alert("显示名称长度超过40，请重新输入！");
						return;
					}
					
					datas.push(r);
				}
				
				//获取列数
				var form = AcPersonalDeskLayout.infoForm.getForm()
				if (!form.isValid()) return;
				var dataFrom = form.getValues();
				
				var me = this;
				Ext.Ajax.request({
					url: ctx + "/acPersonalDeskLayout!saveAcPersonalDeskInfo.action?columNum="+dataFrom.columNum,
					jsonData: datas,
					success: function(r){
						var retn = Ext.util.JSON.decode(r.responseText);
						if(retn.success){
							alertSuccess();
							AcPersonalDeskLayout.win.hide();
							//加载编辑gird
							AcPersonalDeskLayout.editGrid.store.load();
							self.location.reload();
						}else{
							alertFail(retn.errMsg);
						}
					},
					failure: function(){
						alertFail("请求超时！");
					}
				});
			}
		},{
			text: '刷新', iconCls: 'refreshIcon', handler: function() {
				//加载编辑gird
				AcPersonalDeskLayout.editGrid.store.load();
				
				AcPersonalDeskLayout.tree.root.reload();
				AcPersonalDeskLayout.tree.getRootNode().expand();
			}
		},{
			text: '取消', iconCls: 'closeIcon', handler: function() {
				AcPersonalDeskLayout.win.hide();
			}
		}]
	});
	
	AcPersonalDeskLayout.showWin = function(operatorid) {
		//加载编辑gird
		AcPersonalDeskLayout.editGrid.store.load();
		
		AcPersonalDeskLayout.win.show();
	}
     
});

//通过editGrid重新排列序号
function updateSequenceNumber(store){
	var recordCount1 = store.getCount();
	for(var i = 0;i<recordCount1;i++){
		var recordV = store.getAt(i);
		recordV.set("sequenceNumber",i+1);
	}
}

//通过传入的按钮状态获取对应的button字符串
//c:传递的那一行对象
//d:传递的那一行对象 行号
//buttonState:按钮状态，upShow upHide downShow downHide
function getButtonString(c,d,buttonState){
	//初始化上移下移的2种状态对象
	var fnObj = {
		//上移显示
		upShow : "<button id='" + c.data.funccode + "_Up' type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over moveUpIcon' onclick='AcPersonalDeskLayout.upFn(\"" + d + "\")'>上移</button>",
		//上移隐藏
		upHide : "<button id='" + c.data.funccode + "_Up' type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over moveUpIcon' style='display:none' onclick='AcPersonalDeskLayout.upFn(\"" + d + "\")'>上移</button>",
		//下移显示
		downShow : "<button id='" + c.data.funccode + "_Down' type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over moveDownIcon' onclick='AcPersonalDeskLayout.downFn(\"" + d + "\")'>下移</button>",
		//下移隐藏
		downHide : "<button id='" + c.data.funccode + "_Down' type='button' class='x-btn-small x-btn-icon-small-left x-btn-text x-btn x-btn-text-icon x-btn-over moveDownIcon' style='display:none' onclick='AcPersonalDeskLayout.downFn(\"" + d + "\")'>下移</button>"
	}
	
	return fnObj[buttonState];
}

//通过editGrid重新修改每行数据按钮状态(隐藏或者显示)
//store：传递过来的store
function updateFnState(store){
	var recordCount1 = store.getCount();
	for(var i = 0;i<recordCount1;i++){
		var recordV = store.getAt(i);
		var functionCode = recordV.get("funccode");
		
		//如果是第一行数据，并且只有一条数据
		if(recordCount1 == 1){
			Ext.getDom(functionCode + "_Down").style.display = 'none';
			Ext.getDom(functionCode + "_Up").style.display = 'none';
		}else{
			//第一行，数据条数不止一条
			if(i == 0){
				Ext.getDom(functionCode + "_Down").style.display = '';
				Ext.getDom(functionCode + "_Up").style.display = 'none';
			//最后一行
			}else if(i == (recordCount1 - 1)){
				Ext.getDom(functionCode + "_Down").style.display = 'none';
				Ext.getDom(functionCode + "_Up").style.display = '';
			//中间行
			}else{
				Ext.getDom(functionCode + "_Down").style.display = '';
				Ext.getDom(functionCode + "_Up").style.display = '';
			}
		}
	}
}

//交换record1，record2的数据
function setRecordValue(record1,record2){
	//缓存record1的数据
	var updateidx = record1.get("idx");
	var updateoperatorid = record1.get("operatorid");
	var updatefunccode = record1.get("funccode");
	var updatefuncName = record1.get("funcName");
	var updateshowname = record1.get("showname");

	//更新record1
	record1.set("idx",record2.get("idx"));
	record1.set("operatorid",record2.get("operatorid"));
	//不更新序号
	record1.set("funccode",record2.get("funccode"));
	record1.set("funcName",record2.get("funcName"));
	record1.set("showname",record2.get("showname"));

	//更新record2
	record2.set("idx",updateidx);
	record2.set("operatorid",updateoperatorid);
	//不更新序号
	record2.set("funccode",updatefunccode);
	record2.set("funcName",updatefuncName);
	record2.set("showname",updateshowname);
}
