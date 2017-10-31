Ext.onReady(function(){
	
	DeviceClass.regTreeNodeClick(function(node){
		
		if(node.leaf){
			DeviceList.reload(node.id);
		}else{
			DeviceList.reload(null);
		}
	});
	
	DeviceList.regGridClick(function(record){
		
		RdpTab.reload(record.get("deviceTypeCode"));
	});
});