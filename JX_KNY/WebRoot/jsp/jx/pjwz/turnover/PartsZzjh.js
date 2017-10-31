Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("PartsZzjh");
	//显示处理等待状态的控件，必须在此定义，若在外部定义全局变量会刷新整个页面
	PartsZzjh.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在处理，请等待......"});

	
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
	    layout:"fit", 
	    items:PartsZzjhWin.grid
	});

	PartsZzjh.init = function(){
		if(zajcIdx == "" || zajcIdx == "null"){
			Ext.getCmp("add_button").setVisible(false);
			Ext.getCmp("SC_button").setVisible(false);
			Ext.getCmp("updatePlanTime_button").setVisible(false);
			Ext.getCmp("updateRealTime_button").setVisible(false);
			Ext.getCmp("delete_button").setVisible(false);
			Ext.getCmp("zxjc").setVisible(true);
			Ext.getCmp("trainTypeShortName").setVisible(true);
		}else{
			Ext.getCmp("zxjc").setVisible(false);
			Ext.getCmp("add_button").setVisible(true);
			Ext.getCmp("SC_button").setVisible(true);
			Ext.getCmp("updatePlanTime_button").setVisible(true);
			Ext.getCmp("updateRealTime_button").setVisible(true);
			Ext.getCmp("delete_button").setVisible(true);
			Ext.getCmp("trainTypeShortName").setVisible(false);
			PartsZzjhWin.workPlanIdx = zajcIdx;
			PartsZzjhWin.trainTypeAndNo = trainTypeAndNo;
			PartsZzjhWin.repairClassAndTime = repairClassAndTime;
		}
		
		// 显示机车和修程修次
		if(!Ext.isEmpty(trainTypeAndNo) && !Ext.isEmpty(repairClassAndTime)
			&& trainTypeAndNo != "null" && repairClassAndTime != "null"
		){
			Ext.getCmp('label_train').setText(trainTypeAndNo+" "+repairClassAndTime);
		}else{
			Ext.getCmp('label_train').setText("");
		}
	};
	PartsZzjh.init();

});