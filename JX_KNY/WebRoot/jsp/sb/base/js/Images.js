/**
 * 
 */
Ext.onReady(function() {
	
	var id = "Images";
	Ext.ns(id);
	var form;
	
	window[id].load = function(idx) {
		var src = ctx + "/attachment!images.action?key=" + idx;
		var page = document.getElementById("img_page");
		if(page.src && page.src.lastIndexOf(src) != -1) {
			return;
		}
		page.src = src
	};
	
	form = new Ext.Panel({
		layout: "fit",
		//html: "<iframe frameborder='0' src='" + ctx + "/view/cmps/seeImage/index.html' width='100%' height='100%'></iframe>"
		html: "<iframe frameborder='0' id='img_page' width='100%' height='100%'></iframe>"
	});
	
	SR.addContent(id, form);
});