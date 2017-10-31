Ext.onReady(function(){
	
	if(typeof(PartsPlanEdit) === 'undefined')
		Ext.ns("PartsPlanEdit");
	
	var tab, win, initialized = false;
	
	function createTab(){
		
		if(PartsPlanEdit.baseForm === undefined) PartsPlanEdit.createBaseForm(); 
		
		tab = new Ext.TabPanel({
			activeTab: 0,
			items: [{
				title: "基本信息",
				layout:"fit",
				frame: true,
				items: PartsPlanEdit.baseForm
			}/*,{
				title: "计划排程",
				layout:"fit",			
				items: []
			}*/,{
				title: "作业工单编辑",
				layout:"fit",			
				items: []
			}],
			listeners: {
				tabchange: function(tab, panel){
					if(panel.items.length == 0){
						panel.add(PartsPlanEdit.jobOrderPanel());
						panel.doLayout();
					}
					panel.doLayout();
				}
			}
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "作业计划编辑",width:900, height:500, layout: "fit", 
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
	PartsPlanEdit.showWin = function(_record){		
		if(initialized == false) initialize();
		win.show();
		PartsPlanEdit.setForm(_record);
		PartsPlanEdit.setJobOrder(_record);
		
	}
});