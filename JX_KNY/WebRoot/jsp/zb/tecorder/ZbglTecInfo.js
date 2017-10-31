function toDate(val){
	if(val){
		return new Date(val).format("Y-m-d H:i");
	}
	return "";
}

function toStr(val){
	if(val){
		return val;
	}
	return "无";
}

Ext.onReady(function(){
Ext.namespace("ZbglTecInfo");
ZbglTecInfo.baseInfo = [{
		align : 'center',
		layout : 'column',
		baseCls : 'x-plain',
		items:[{
			align : 'center',
			layout : 'form',
			defaultType : 'textfield',
			baseCls : 'x-plain',
			columnWidth : .5,
			items : [{
				name : 'trainType', 
				fieldLabel : '车型车号', 
				width : "99%",
				style:"border:none;background:none;",
				readOnly:true
			}]
		},{
			align : 'center',
			layout : 'form',
			defaultType : 'textfield',
			baseCls : 'x-plain',
			columnWidth : .5,
			items : [{
				name : 'xtype', 
				fieldLabel : '技术措施类型', 
				width : "99%",
				style:"border:none;background:none;",
				readOnly:true
			}]
		},{
			align : 'center',
			layout : 'form',
			defaultType : 'textarea',
			baseCls : 'x-plain',
			columnWidth : 1,
			items : [{
				name : 'content', 
				fieldLabel : '指令内容', 
				width : "99%",
				style:"border:none;background:none;",
				readOnly:true
			}]
		}]
	}];
	
ZbglTecInfo.Items = [{
		xtype:'fieldset',
		title: '基本信息',
		collapsible: false,
		autoHeight:true,
		frame : true,
		items :ZbglTecInfo.baseInfo
	},{
		xtype:'fieldset',
		title: '处理信息',
		collapsible: false,
		autoHeight:true,
		frame : true,
		html: "<div class='info' id='infos'></div>"
	}];

ZbglTecInfo.form = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		items: [{
        	align: 'center',
    		layout : 'form',
    		baseCls: 'x-plain',
    		style : 'padding : 10px',
    		defaults : { anchor : '98%'},
    		labelWidth:80,
    		buttonAlign : 'center', 
    		items: ZbglTecInfo.Items
        }]
	});
	
	
ZbglTecInfo.win = new Ext.Window({
		title:"详细信息", width:600, height:420, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
		items: [ZbglTecInfo.form] , modal:true,
		buttons:[{
    		text: "关闭", iconCls:"closeIcon", handler:function(){ ZbglTecInfo.win.hide();}
    	}]
	});
	
ZbglTecInfo.LoadInfo = function(idx){
		Ext.Ajax.request({
			url: ctx + "/zbglTecOrder!findTecInfo.action",
			params:{zbglTecIdx: idx},
			success:function(response, options){
				var data = Ext.util.JSON.decode(response.responseText);
				if(!data.list || data.list.length == 0){
					ZbglTecInfo.SetInfo("没有任何数据");
				}else{
					ZbglTecInfo.SetData(data.list);
				}
			}
		});
	}
	
ZbglTecInfo.SetData = function(list){
		var html = '<table  border="1px" cellspacing="0" cellpadding="0" bordercolor="#000000" style="font-size:12px;border-collapse:collapse" class="table">'
				+ '<thead style="color:blue;"><tr><th class="th ff" style="width:120px;">机车入库时间</th>'
				+ '<th class="th ff" style="width:100px;">处理人员</th>'
				+ '<th class="th ff">处理情况</th>'
				+ '<th class="th ff" style="width:120px;">处理时间</th>';
		
		for(var i = 0; i < list.length; i++){
			var d = "<tr><td class='td fl' style='width:100px;'>" + toDate(list[i].inTime) + "</td>";
			d += "<td class='td fl'>" + toStr(list[i].handlePersonName )+ "</td>";
			d += "<td class='td fl' style='width:150px;'>" + toStr(list[i].wiStatus) + "</td>";
			d += "<td class='td fl' style='width:100px;'>" + toDate(list[i].handleTime) + "</td>";
			d += "</tr>";
			html += d;
		}
		
		html += "</tbody></table>";
		ZbglTecInfo.SetInfo(html);
	}
	
ZbglTecInfo.SetInfo = function(html){
		document.getElementById("infos").innerHTML = html
	}
	
ZbglTecInfo.showWin = function(record){
		record.set("trainType", record.get("trainTypeShortName") + " | " + record.get("trainNo"));
		record.set("xtype", record.get("orderClass"));
		record.set("content", record.get("orderContent"));
		ZbglTecInfo.form.getForm().loadRecord(record);
		ZbglTecInfo.win.show();
		ZbglTecInfo.LoadInfo(record.get("idx"));
	}
});