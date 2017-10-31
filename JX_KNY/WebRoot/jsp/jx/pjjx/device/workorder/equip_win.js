Ext.onReady(function(){
	
	Ext.ns("EquipWin");
	
	var win, panel, initialized = false;
	
	function createWin(){
		win = new Ext.Window({
			title:'查看机务设备工单',
			//plain:true,
			modal: true,
			closeAction:'hide',
			layout:'fit',
			width:900,
			height:450,
			border: false,
			maximizable: true,
			items:{
				layout: "column",
				columnWidth: .5,
				border: false,
				items:[{
					columnWidth: .5,
					layout: "fit",
					title: "设备工单列表",
					items: EquipCard.createGrid()
				},{
					columnWidth: .5,
					layout: "fit",
					title: "工单检测项列表",
					items: EquipCardDI.createGrid()
				}],
				listeners:{
					"resize" : function(){
						this.items.items[0].setHeight(this.getHeight());
						this.items.items[1].setHeight(this.getHeight());
					}
				}
			},
			buttonAlign: 'center',
			buttons:[{
				text: "关闭",
				iconCls: "closeIcon",
				handler: function(){
					win.hide();
				}
			}]
		});
	}
	
	function init(){
		createWin();
		initialized = true;
	}
	
	EquipWin.showWin = function(rdpIdx){
		if(initialized == false) init();
		win.show();
		EquipCard.reload(rdpIdx);
	};
});