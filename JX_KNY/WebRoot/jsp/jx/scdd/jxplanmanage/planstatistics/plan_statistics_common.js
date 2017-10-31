
var c1c6_val = 'C1C6', fxdx_val = '辅修大修';


/**
 * 按钮查找grid（查询表格）
 */
function buttonFindGrid(callback){
	var tab = this.findParentByType("tabpanel");
	if(tab){
		var panel = tab.getActiveTab().items.items[0].items.items[1];
		if(panel){
			panel.findBy(function(){
				var grid = arguments[0];
				if(grid && grid.store){
					callback(grid);
				}
			});
		}
	}
}

/**
 * 已启动和未启动查询表单的创建
 * @return
 */
function createSearchForm(xcType, grid){
	
	return defineFormPanel({
		defaultType: "textfield",
		labelWidth: 60,
		rows:[{
			cw: [200, 200, 200, 80, 80],
			cols: [{
				xtype: "Base_combo",
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				business:"trainType",
				fields:['typeID','shortName'],
				hiddenName: "trainTypeIdx", 
				displayField: "shortName",
				valueField: "typeID",
				pageSize: 20, minListWidth: 200,
				queryParams: {repairType: xcType},
				fieldLabel: "车型",
				editable:false
			},{
				fieldLabel: "车号",
				name: "trainNo"
			},{
				fieldLabel: "修程",
				xtype: "Base_combo",
				entity:'com.yunda.jx.base.jcgy.entity.XC',
				fields:['xcID','xcName'],
				hiddenName: "repairClass", 
				displayField: "xcName",
				valueField: "xcID",
				pageSize: 20, minListWidth: 200,
				queryParams: {xcType: xcType},
				editable:false
			},{
				xtype: "button",
				style: "margin-left: 20px",
	            text: "查询",
	            iconCls: "searchIcon",
	            handler: function(){
	            	buttonFindGrid.call(this, function(grid){
	            		var sp = grid.searchForm.getForm().getValues();
	            		grid.searchFn(sp);
	            	});
				}
			},{
				xtype: "button",
				text: "重置",
				style: "margin-left: 20px",
				iconCls: "resetIcon",
				handler: function(){
					var form = this.findParentByType("form");
					form.getForm().reset();
					var combo = form.findByType("Base_combo");
					combo[0].clearValue();
					combo[1].clearValue();
					
					buttonFindGrid.call(this, function(grid){
	            		var sp = form.getForm().getValues();
	            		grid.searchFn(sp);
	            	});
				}
			}]
		}]
	}, {labelAlign: "right"});
}