Ext.onReady(function(){
	
	if(typeof(PlanningEdit) === 'undefined')
		Ext.ns("PlanningEdit");
	
	var tab, win, initialized = false;
	
	function createTab(){
		var form = PlanningEdit.createForm();
		
		tab = new Ext.TabPanel({
			activeTab: 0,
			items: [{
				title: "基本信息",
				layout:"fit",
				frame: true,
				items: form
			},{
				title: "计划明细",
				layout:"fit",
				items: []
			}],
			listeners: {
				tabchange: function(tab, panel){
					if(panel.items.length == 0){
						panel.add(PlanningEdit.createPanel());
						panel.doLayout();
					}
					panel.doLayout();
				}
			}
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "计划编辑",width:900, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: false, resizable: false,
			items: tab
		});
	}
	
	function initialize(){
		createTab();
		createWin();
		initialized = true;
	}
	
	PlanningEdit.showWin = function(_record){		
		if(initialized == false) initialize();
		win.show();
		//PartsPlanEdit.setForm(_record);
		//PartsPlanEdit.setJobOrder(_record);
		
	}
});