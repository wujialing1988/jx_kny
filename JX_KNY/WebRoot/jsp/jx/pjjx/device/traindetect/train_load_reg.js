Ext.onReady(function(){
	Searcher.regSearcher(function(sp){
		sp = MyJson.deleteBlankProp(sp);
		DeviceRdp.reload(sp);
		DetectItem.reload(sp);
	});
	
	Searcher.search();
	
	DeviceRdp.rowClickCallback(function(g, rIdx){
		var r = g.store.getAt(rIdx);
		DetectItem.rdpFilter(r.get("rdpIdx"));
	});
	
	DeviceRdp.noData = function(){
		DetectItem.clear();//清空数据
	}
});