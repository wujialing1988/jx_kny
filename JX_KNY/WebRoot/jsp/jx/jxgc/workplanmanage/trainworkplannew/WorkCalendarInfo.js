/**
 * 查询工作日历名称
 */
Ext.onReady(function(){
	Ext.namespace('WorkCalendarInfo'); 	
	// 查询工作日历名称
	WorkCalendarInfo.store = new Ext.data.JsonStore({
	    id: "idx", data:WorkCalendarInfo_storeData,
	    fields: [ "idx","calendarName"]
	});
	
	WorkCalendarInfo.getCalendarName = function(idx){
	    var record = WorkCalendarInfo.store.getById(idx);
	    if(typeof(record) != "undefined" && record != null && record != "" && record != "null" && typeof(record.get("calendarName")) != "undefined"){
	    	return record.get("calendarName");
	    }
	    return "";    
	}

});