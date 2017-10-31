Ext.onReady(function(){
	
	var reload = [false, false];
	
	Ext.ns("RdpTab");
	var tab;
	function createTab(){
		tab = new Ext.TabPanel({
			activeTab: 0,
			items: [{
				title: "配件任务单",
				layout: "fit",
				items: PartsRdpList.createGrid()
			},{
				title: "机车任务单",
				layout: "fit",
				items: []
			}],
			listeners:{
				tabchange: function(tab, panel){
					if(panel.items.length == 0){
						panel.add(TrainRdpList.createGrid());
						panel.doLayout();
					}					
					panel.doLayout();
					reloadPanel(panel);
				}
			}
		});
		return tab;
	}
	
	function reloadPanel(panel){
		if(panel.title.charAt(0) == '机' && reload[1]){
			TrainRdpList.reload(deviceCode);
		}else if(reload[0]){
			PartsRdpList.reload(deviceCode);
		}
	}
	
	RdpTab.createTab = createTab;
	
	RdpTab.reload = function(_deviceCode){
		deviceCode = _deviceCode;
		reload = [true, true];
		reloadPanel(tab.getActiveTab());
	}
});