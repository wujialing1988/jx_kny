/**
 * ExtJS grid 右键菜单插件
 */
Ext.ns('Ext.ux.grid');
Ext.ux.grid.RightMenu = function(options) {
	var currRecord = false;
	var currRowIndex = false;
	var currGrid = false;
	var menuItems = Ext.each(options.items, function() {
		var item = this;
		this.handler = function() {
			item.recHandler && item.recHandler(currRecord, currRowIndex, currGrid);
		};
	});
	var menu = new Ext.menu.Menu({
		items : options.items
	});
	this.init = function(grid) {
		grid.addListener('rowcontextmenu', function(client, rowIndex, e) {
			e.preventDefault();
			if (rowIndex < 0) {
				return;
			}
			currRowIndex = rowIndex;
			currRecord = grid.getStore().getAt(rowIndex);
			currGrid = grid;
			menu.showAt(e.getXY());
		});
	};
};