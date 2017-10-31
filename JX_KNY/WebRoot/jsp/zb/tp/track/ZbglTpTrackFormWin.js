Ext.onReady(function(){
	Ext.namespace('ZbglTpTrackFormWin');//定义命名空间
	//最近一个月
	ZbglTpTrackFormWin.getCurrentMonth = function(arg){
		var Nowdate = new Date();//获取当前date
		var currentYear = Nowdate.getFullYear();//获取年度
		var currentMonth = Nowdate.getMonth();//获取当前月度
		var currentDay = Nowdate.getDate();//获取当前日
		var MonthFirstDay=new Date(currentYear,currentMonth,currentDay);
		return MonthFirstDay.format('Y-m-d');
	}
	//车号
	ZbglTpTrackFormWin.trainNo;
	//车型idx
	ZbglTpTrackFormWin.trainTypeIDX;
	//车型简称
	ZbglTpTrackFormWin.trainTypeShortName;
	//提票主键idx
	ZbglTpTrackFormWin. jt6IDX;
	//提票单号
	ZbglTpTrackFormWin.faultNoticeCode;
	
	//列数
	ZbglTpTrackFormWin.infoForm = new Ext.form.FormPanel({
		labelAlign:"right", layout:"form", border:false, //style:"padding:10px" ,
		labelWidth: 65,//baseCls: "x-plain",
		frame:true,
		
		items:[{
			layout:"column",
			items:[{
				columnWidth:.5,
				layout:"form",
				items:[{
					id:"trainTypeShortName_id",
					xtype:"label",
					fieldLabel:"车型简称",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
					id:"trainNo_id",
					xtype:"label",
					fieldLabel:"车号",
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    name:"trackDate",
					xtype:"my97date",
					format: "Y-m-d H:i:s",  
					fieldLabel:"跟踪时间",
					value : ZbglTpTrackFormWin.getCurrentMonth(),
				    width: 100,
					anchor:"98%"
				}]
			},{
				columnWidth:.5,
				layout:"form",
				items:[{
				    id:"faultNoticeCode_id",
					xtype:"label",
					fieldLabel:"提票单号",
					anchor:"98%"
				}]
			},{
				columnWidth:1,
				layout:"form",
				items:[{
				    name:"trackReason",
					xtype:"textarea",
					fieldLabel:"跟踪原因",
					anchor:"98%"
				}]
			}]
		}]
	});		
		
	
	
	
	ZbglTpTrackFormWin.win = new Ext.Window({
	    title:"提票跟踪单", 
	    layout: 'fit',
		height: 200, width: 600,
		items:ZbglTpTrackFormWin.infoForm,
		closable : false,
		plain:true,
		closeAction:"hide",
		buttonAlign: 'center',
		buttons: [{
			text: "保存", iconCls: "saveIcon", handler: function() {
				//获取列数
				var form = ZbglTpTrackFormWin.infoForm.getForm()
				if (!form.isValid()) return;
				var dataFrom = form.getValues();
				
				//添加车型idx数据
				dataFrom.trainTypeIDX = ZbglTpTrackFormWin.trainTypeIDX;
				//添加车型简称数据
				dataFrom.trainTypeShortName = ZbglTpTrackFormWin.trainTypeShortName;
				//添加车号数据
				dataFrom.trainNo = ZbglTpTrackFormWin.trainNo;
				//添加提票主键idx数据
				dataFrom.jt6IDX = ZbglTpTrackFormWin.jt6IDX;
				//添加提票单号数据
				dataFrom.faultNoticeCode = ZbglTpTrackFormWin.faultNoticeCode;
				//添加跟踪状态数据(值在jsp定义)
				dataFrom.status = TRACKING;
				//添加已记录次数数据(第一次的时候，提票记录单次数为0)
				dataFrom.recordCount = 0;
				//单次跟踪操作状态默认0
				dataFrom.singleStatus = 1;
				
				datas = Ext.util.JSON.encode(dataFrom)
				
				var me = this;
				Ext.Ajax.request({
					url: ctx + "/zbglTpTrackRdp!saveZbglTpTrackRdpInfo.action",
					jsonData: datas,
					success: function(r){
						var retn = Ext.util.JSON.decode(r.responseText);
						if(retn.success){
							alertSuccess();
							ZbglTpTrackFormWin.win.hide();
							//加载编辑gird
							ZbglTpSearch.grid.store.load();
						}else{
							alertFail(retn.errMsg);
						}
					},
					failure: function(){
						alertFail("请求超时！");
					}
				});
			}
		},{
			text: '取消', iconCls: 'closeIcon', handler: function() {
				ZbglTpTrackFormWin.win.hide();
			}
		}]
	});
	
	ZbglTpTrackFormWin.showWin = function() {
		//提票表单初始化数据
		Ext.getCmp("trainNo_id").setText(ZbglTpTrackFormWin.trainNo);
		Ext.getCmp("trainTypeShortName_id").setText(ZbglTpTrackFormWin.trainTypeShortName);
		Ext.getCmp("faultNoticeCode_id").setText(ZbglTpTrackFormWin.faultNoticeCode);
		
		ZbglTpTrackFormWin.win.show();
	}
});
