/**
 * 按专业月度统计表 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('workDayCount');                       //定义命名空间
/** 获取当前月份的第一天和最后一天*/
workDayCount.tabs = new Ext.TabPanel({
	    activeTab: 0,
	    frame:true,
	    items:[{
	            id:"card",title: "作业工单", layout: "fit", border: false, items: [ ]
	        },{
	        	id:"fault",title: "提票工单", layout: "fit", border: false, items:[ ]
	        }]
	});
//页面自适应布局
var viewport = new Ext.Viewport({ 
	layout:"fit",items:[workDayCount.tabs]
});
workDayCount.loadReport = function(){
//	var ctx_v = ctx.substring(1);
	var url_card = getReportEffectivePath("/jxgc/workdaycount_card.cpt&empname="+empname_v+"&beginDate="+beginDate+"&endDate="+endDate+"&orgseq="+orgseq);
	var card_h = jQuery("#card").height();
	document.getElementById("card").innerHTML = "<iframe style='width:100%;height:"+card_h+"px;overflow:auto;' frameborder='0' src=" + url_card + "></iframe>";
	var url_fault = getReportEffectivePath("/jxgc/workdaycount_fault.cpt&empname="+empname_v+"&beginDate="+beginDate+"&endDate="+endDate+"&orgseq="+orgseq);
	workDayCount.tabs.setActiveTab(1);
	var fault_h = jQuery("#fault").height();
	document.getElementById("fault").innerHTML = "<iframe style='width:100%;height:"+fault_h+"px;overflow:auto;' frameborder='0' src=" + url_fault + "></iframe>";
	workDayCount.tabs.setActiveTab(0);
}
workDayCount.loadReport();
workDayCount.tabs.get(1).on("activate", function(){	
	var url_fault = getReportEffectivePath("/jxgc/workdaycount_fault.cpt&empname="+empname_v+"&beginDate="+beginDate+"&endDate="+endDate+"&orgseq="+orgseq);
	var fault_h = jQuery("#fault").height();
	document.getElementById("fault").innerHTML = "<iframe style='width:100%;height:"+fault_h+"px;overflow:auto;' frameborder='0' src=" + url_fault + "></iframe>";
});
});