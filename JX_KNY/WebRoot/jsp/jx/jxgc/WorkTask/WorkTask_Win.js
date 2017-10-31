Ext.namespace("win");

/*
 * 任务操作主窗口包含的Tab选项卡
 */
win.editTabs = new Ext.TabPanel({
	activeTab: 0, 
    items:[{
            title: "基本信息", layout: "fit",  border: false, 
            items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region: 'north', title:'检修基本信息',
		            layout: "fit",frame:true,collapsible:true, 
		            id:"basicInfo",
		            height: 110, bodyBorder: false,
		            items:[form.titleForm]
		        },{
		            region : 'center',autoScroll:true,frame:true, layout : 'fit', bodyBorder: false, items : [ form.baseForm ]
		        }],
            	buttons: [{
            		text: "保存", iconCls: "saveIcon", handler:function(){ }, id: "saveParts"
            	},{
					text: "关闭", iconCls:"closeIcon", handler:function(){ win.handlerWin.hide();}
				}]
            }
        },{
            title: "检测/修项目", layout: "fit", border: false, items: [ taskItem.UI ],
            listeners:{
        		"activate":taskItem.removeTbar//移除工具栏
        	}
        },{
            title: "作业人员", layout: "fit", border: false, frame:true, items: [ Worker.grid ], buttonAlign:"center",
        	buttons:[{
        		text: "关闭", iconCls:"closeIcon", handler:function(){ win.handlerWin.hide();}
        	}]
        }]
});
/*
 * 任务操作主窗口
 */
win.handlerWin = null;
win.createHandlerWin = function() {
	win.editTabs = new Ext.TabPanel({
	activeTab: 0, 
    items:[{
            title: "基本信息", layout: "fit",  border: false, 
            items: {
				xtype: "panel",frame:true, autoScroll:true,layout: "border",buttonAlign:"center",
				items:[{
		            region: 'north', title:'检修基本信息',
		            layout: "fit",frame:true,collapsible:true, 
		            id:"basicInfo",
		            height: 110, bodyBorder: false,
		            items:[form.titleForm]
		        },{
		            region : 'center',autoScroll:true,frame:true, layout : 'fit', bodyBorder: false, items : [ form.baseForm ]
		        }],
            	buttons: [{
            		text: "提交工单", iconCls: "saveIcon", handler: handler.beforeSubmit, id: "saveParts"
            	},{
					text: "关闭", iconCls:"closeIcon", handler:function(){ win.handlerWin.hide();}
				}]
            }
        },{
            title: "检测/修项目", layout: "fit", border: false, items: [ taskItem.UI ],
            listeners:{
        		"activate":taskItem.removeTbar//移除工具栏
        	}
        },{
            title: "作业人员", layout: "fit", border: false, frame:true, items: [ Worker.grid ], buttonAlign:"center",
        	buttons:[{
        		text: "关闭", iconCls:"closeIcon", handler:function(){ win.handlerWin.hide();}
        	}]
        }]
	});
	win.handlerWin = new Ext.Window({
		title:"作业任务处理", width:800, height:500, plain:true, closeAction:"hide", buttonAlign:'center', layout:'fit',
		items: win.editTabs , modal:true ,maximized : true
	});
}

win.completeConfirmWin = null;
win.createWin = function(qcList) {	
	win.completeConfirmWin = new Ext.Window({
		title:"处理任务", width:450, height:300,  plain:true,  buttonAlign:'center',layout:"fit",
		items: form.completeConfirm, modal:true, frame: true,
		buttons:[{
			text:"处理完成", iconCls:"checkIcon", handler:function(){
				var data = form.completeConfirm.getForm().getValues();
				if (!form.completeConfirm.getForm().isValid()) return;
				if(!data.realBeginTime){
					MyExt.Msg.alert("请填写开工时间");
					return;
				}
				if(!data.realEndTime){
					MyExt.Msg.alert("请填写完工时间");
					return;
				}
				if(data.realBeginTime > data.realEndTime){
					MyExt.Msg.alert("开工时间不能大于等于完工时间");
					return;
				}
				var qcDatas = [];
			    for (var i = 0; i < qcList.length; i++) {
			        var field = qcList[ i ];
			        var qcData = {};  //定义检验项
			        qcData.checkItemCode = field.checkItemCode;
			        if(!form.completeConfirm.getForm().findField(field.checkItemCode).getValue()) {
			        	MyExt.Msg.alert("请选择质量检查【" + field.checkItemName + "】指派人员！");
						return;
			        }
			        qcData.qcEmpID = form.completeConfirm.getForm().findField(field.checkItemCode).getValue();
			        qcDatas.push(qcData);			        
			    }     
			    var entityJson = {};
				entityJson.idx = handler.currentIdx;
				entityJson.realBeginTime = data.realBeginTime;
				entityJson.realEndTime = data.realEndTime;
				entityJson.remarks = data.remarks;
				Ext.Msg.confirm("提示","确认完工", function(btn){
					if(btn == 'yes'){
						showtip(win.completeConfirmWin.getEl());
						//批量则取批量的IDX，否则取作业卡唯一标识
												
						var cfg = { 
					        url: ctx + "/workCard!complete.action",
					        jsonData: qcDatas, 
					        params: {
					        	entityJson: Ext.util.JSON.encode(entityJson)
					        },
					        success: function(response, options){
					            hidetip();
					            var result = Ext.util.JSON.decode(response.responseText);
								if(result.status == 'success'){
									alertSuccess("操作成功！");
								}else{
									alertFail(result.ex);
								}
								//重新加载数据
								handler.grid.store.load();
								handled.grid.store.load();
								//关闭窗口
								win.completeConfirmWin.close();
								if (win.handlerWin)
									win.handlerWin.hide();
					        }
					    };
					    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
					}
				});
			}
		},{
			text: "关闭", iconCls:"closeIcon", handler:function(){win.completeConfirmWin.close();}
		}],
		listeners:{//隐藏窗口时隐藏MY97的面板
			"hide":function(){
				if($dp) $dp.hide();
			}
		}
	});
}