/**
 * 摄像头绑定 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
	Ext.ns('SiteVideoBind');			// 定义命名空间
	
	/** ******************** 定义全局变量开始 ******************** */
	SiteVideoBind.labelWidth = 80;
	SiteVideoBind.fieldWidth = 150;
	/** ******************** 定义全局变量结束 ******************** */
	
	/** ******************** 定义全局函数开始 ******************** */
	// 绑定操作的保存函数
	SiteVideoBind.saveFn = function() {
		var form = SiteVideoBind.form.getForm();
		if (!form.isValid()) {
			return;
		}
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + "/siteVideoNvrChanel!saveOrUpdate.action",
			jsonData: Ext.encode(form.getValues()),
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	// 页面初始化函数
	SiteVideoBind.initFn = function() {
		var queryEntiy = {
			siteID: siteID,
			videoCode: videoCode,
			videoName: videoName
		}
		// 页面初始化时，如果摄像头以及存在绑定信息，则对绑定信息进行回显
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + "/siteVideoNvrChanel!getModel.action",
			jsonData: Ext.encode(queryEntiy),
			scope: SiteVideoBind.form,
			success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		        	if (!Ext.isEmpty(result.entity)) {
		        		this.find('name', 'idx')[0].setValue(result.entity.idx);
		        		this.find('name', 'chanelName')[0].setValue(result.entity.chanelName);
		        		this.find('hiddenName', 'videoNvrIDX')[0].setDisplayValue(result.entity.siteVideoNvr.idx, result.entity.siteVideoNvr.nvrName);
		        		var chanelID_combo = this.find('hiddenName', 'chanelID')[0];
		        		chanelID_combo.setDisplayValue(result.entity.chanelID, result.entity.chanelName);
		        		// 设置通道号下拉框的数据集
		        		chanelID_combo.queryParams.videoNvrIDX = result.entity.videoNvrIDX;
		                chanelID_combo.cascadeStore();
		        	}
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
	}
	// 视频预览函数
	SiteVideoBind.previewVideoFn = function(){
		var form = SiteVideoBind.form.getForm();
		if (!form.isValid()) {
			return;
		}
		var videoNvrIDX  = Ext.getCmp('id_videoNvrIDX').getValue();
		var chanelID  = Ext.getCmp('id_chanelID').getValue();
		var src = ctx + '/jsp/twt/twtinfo/video/VideoPlayer.jsp?videoNvrIDX=' + videoNvrIDX + "&chanelID=" + chanelID + "&previewType=1";
		if (!SiteVideoBind.win) {
			SiteVideoBind.win = new Ext.Window({
				width:300, height: 300,
				title: '视频监控预览',
				layout:'fit',
//				maximizable: true,
				maximized: true,
				closeAction: 'hide',
				html:['<div id="iframe"></div>'].join(''),
				buttonAlign: 'center',
				buttons:[{
					text: '关闭', iconCls: 'closeIcon', handler: function(){
						this.findParentByType('window').hide();
					}
				}],
				listeners: {
					resize: function(win, width, height) {
						document.getElementById("iframe").innerHTML = [
							'<iframe style="width:100%;height:'+ height +'px;", src="' + src + '">',
							'</iframe>'
						].join('');
					}
				}
			});
			
		}
		SiteVideoBind.win.show();
		document.getElementById("iframe").innerHTML = [
			'<iframe style="width:100%;height:'+ SiteVideoBind.win.getHeight() +'px;", src="' + src + '">',
			'</iframe>'
		].join('');
	}
	/** ******************** 定义全局函数结束 ******************** */
	
	/** ******************** 定义全局表单开始 ******************** */
	SiteVideoBind.form = new Ext.form.FormPanel({
		title:'摄像头绑定', iconCls:'connectIcon', 
		layout:"column", padding:"10px",
		frame:true,
		defaults: {
			xtype: 'container', autoEl:"div", layout:"form", columnWidth:0.5,
			defaults: {
				xtype: 'textfield', anchor:"80%", allowBlank:false
			}
		},
		items:[{
			items:[{
				fieldLabel: '监控点编码', name: 'videoCode', value: videoCode, readOnly:true, 
				style:'background:none;border:none;', allowBlank:true
			}, {
				fieldLabel: '硬盘录像机',
				id:"id_videoNvrIDX", xtype: 'Base_combo', hiddenName: 'videoNvrIDX',
				entity:'com.yunda.twt.twtinfo.entity.SiteVideoNvr',
				queryParams:{siteID: siteID},
				displayField:'nvrName',valueField:'idx',fields:["idx", "nvrName"],
				listeners : {   
		        	"select" : function() {   
		            	// 通道号下拉数据
		                var chanelID_comb = Ext.getCmp("id_chanelID");   
		                chanelID_comb.reset();  
		                chanelID_comb.clearValue(); 
		                chanelID_comb.queryParams.videoNvrIDX = this.getValue();
		                chanelID_comb.cascadeStore();	
		        	}   
		    	}
			}]
		}, {
			items:[{
				fieldLabel: '监控点名称', name: 'videoName', value: videoName, readOnly:true,
				style:'background:none;border:none;', allowBlank:true
			}, {
				fieldLabel: '通道号',
				id:"id_chanelID", xtype: 'Base_combo', hiddenName: 'chanelID',
				entity:'com.yunda.twt.twtinfo.entity.SiteVideoNvrChanel',
				queryParams:{},
				displayField:'chanelName', valueField:'chanelID', fields:["idx", "chanelID", "chanelName"],
				returnField:[{
					widgetId:'id_idx', propertyName:'idx'
				}, {
					widgetId:'id_chanelName', propertyName:'chanelName'
				}],
				listeners : {
					"beforequery" : function(){
						// 选择车号前先选车型
						var videoNvrIDX =  SiteVideoBind.form.getForm().findField("videoNvrIDX").getValue();
						if(Ext.isEmpty(videoNvrIDX)){
							MyExt.Msg.alert("请先选择硬盘录像机！");
							return false;
						}	
		                return true;
					}
				}
			}]
		}, {
			columnWidth:1,
			defaults: {
				xtype: 'hidden', anchor:"90%", allowBlank:false
			},
			items:[{
				fieldLabel: 'idx主键', name: 'idx', id:'id_idx'
			}, {
				fieldLabel: '通道名称', name: 'chanelName', id:'id_chanelName'
			}, {
				fieldLabel: '站场ID', name: 'siteID', value: siteID
			}]
		}],
		buttonAlign:'center',
		buttons: [{
			text:'绑定', iconCls:'saveIcon', handler: SiteVideoBind.saveFn
		}, {
			text:'视频预览', iconCls:'previewIcon', handler: SiteVideoBind.previewVideoFn
		}],
		listeners: {
			render: function() {
				// 页面初始化相关操作
				SiteVideoBind.initFn();
			}
		}
	})
	/** ******************** 定义全局表单结束 ******************** */
		
	// 页面自适应布局
	new Ext.Viewport({
		layout: 'fit',
		items: SiteVideoBind.form
	});
	
});