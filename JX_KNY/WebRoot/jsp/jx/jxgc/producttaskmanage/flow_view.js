/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainEnforcePlanRdp');                       //定义命名空间
	
	function setHeight(){
		var frame = document.getElementById("frame_Id");
		if(frame){
			var h = jQuery("body").height();
			jQuery(frame).height(h-82);
		}
	}
	
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout:'border', 
		items:[new Ext.Panel({
				region:"north",
				layout:"fit",
				height:70,
				split:true,
				maxSize:60,
				minSize:60,
				frame:true,
				items:[TrainEnforcePlanRdp.baseForm]
			}),new Ext.Panel({
				id:"flowFrame_v",
				region:"center",
				layout:"fit",
				items:[],
				listeners: {
					"resize": function(){
						setHeight();
					}
				}
			})
		]
	});
	//TrainEnforcePlanRdp.baseForm.getForm().loadRecord(record);
	var src = ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID="+processInstID+"&rdpId="+rdpId;
	var h = jQuery("#flowFrame_v").height();
	document.getElementById("flowFrame_v").innerHTML = "<iframe id='frame_Id' style='width:100%;overflow:auto;' frameborder='0' src='" + src + "'></iframe>";
	jQuery.ajax({
		url: ctx + "/trainEnforcePlanRdp!getBeseInfo.action",
		data:{rdpId: rdpId},
		dataType:"json",
		type:"post",
		success:function(data){
			var record=new Ext.data.Record();
			for(var i in data){
				record.set(i,data[i]);
			}
			TrainEnforcePlanRdp.LoadInfo(record);
		}
	});	
	setHeight();
});	
