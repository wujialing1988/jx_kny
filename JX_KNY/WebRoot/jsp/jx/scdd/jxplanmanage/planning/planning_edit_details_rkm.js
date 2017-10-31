Ext.onReady(function(){
	
	Ext.ns("RKM");
	
	var tab, win, initialized = false;
	var loadFlag = [false, false];
	
	function borderLayout(xcType, obj){
		var grid = obj.createGrid(function(whereList){
			var s = [" repair_class is not null and repair_order is not null a",
			         "nd not exists(select 1 from scdd_train_enforce_plan_d",
			         "etail d where this_.train_type_idx = d.train_type_idx an",
			         "d this_.train_no = d.train_no an",
			         "d d.record_status = 0 and d.plan_status != ",
			         status_detail_complete,
			         ")"
			         ];
			whereList.push({sql: s.join(''), compare: Condition.SQL});
		});
		grid.searchForm =  createSearchForm(xcType, grid);
		return {
			xtype: "panel",
			layout: "border",
			border: false,
			items: [{
				region: "north",
				minHeight: 100,
				autoHeight: true,
				baseCls: "x-plain",
				items: grid.searchForm
			},{
				region: "center",
				layout:"fit",
				items: grid
			}],
			listeners:{
				afterrender: function(){
					grid.topToolbar.setVisible(false);
				}
			}
		};
	}
	
	function createTab(){
	
		tab = new Ext.TabPanel({
			activeTab: 0,
			items: [{
				title: c1c6_val,
				layout:"fit",
				items: borderLayout(c1c6_val, C1C6)
			},{
				title: fxdx_val,
				layout:"fit",
				items: []
			}],			
			listeners: {
				tabchange: function(tab, panel){
					if(panel.items.length == 0){
						panel.add(borderLayout(fxdx_val, FXDX));
						panel.doLayout();
						loadFlag[tab.title == c1c6_val ? 0: 1] = false;
					}else{
						reload();
					}
					panel.doLayout();
				},
				resize: function(){
					this.activeTab.doLayout();
				}
			}
		});
	}
	
	function createWin(){
		win = new Ext.Window({
		   	title: "根据走行公里生成计划明细",width:800, height:500, layout: "fit", 
			closeAction: "hide", modal: true , buttonAlign:"center",
			border: false, resizable: false,
			items: tab,
			buttons: [{
				text: "选择勾选",
				iconCls: "addIcon",
				handler: function(){
					var tb = tab.getActiveTab();
					var grid = tb.items.items[0].items.items[1].items.items[0];
					var selections = grid.selModel.getSelections();
					var tips = "确认将 " + tb.title + " <span style='color:red'>" + selections.length  + "</span>台机车加入计划明细";
					var me = this;
					Ext.Msg.confirm("提示", tips, function(b){
						if(b === 'yes'){
							callback.call(me,selections, win, grid);
						}
					});
				}
			},{
				text: "关闭",
				iconCls: "closeIcon",
				handler: function(){
					win.hide();
				}
			}]
		});
	}
	function getGrid(){
		return tab.getActiveTab().items.items[0].items.items[1].items.items[0];
	}
	
	function reload(){
		var index = -1;
		if(tab.getActiveTab().title == c1c6_val && loadFlag[0]){
			index = 0;
		}else if(loadFlag[1]){
			index = 1;
		}
		if(index != -1){
			loadFlag[index] = false;
			getGrid().store.load();
		}
	}
	
	function initialize(){
		createTab();
		createWin();
		initialized = true;
	}
	
	var callback;
	
	RKM.showWin = function(_callback){		
		if(initialized == false) initialize();
		win.show();
		loadFlag[0] = loadFlag[1] = true;
		reload();
		callback = _callback;
	}
});