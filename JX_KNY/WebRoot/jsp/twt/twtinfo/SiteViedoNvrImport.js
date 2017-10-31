/**
 * 视频监控信息维护 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('SiteViedoNvrImport');        

	/** **************** 定义文件上传窗口开始 **************** */
	SiteViedoNvrImport.win = new Ext.Window({
		title:"上传", 
		width:450, height:140, 
		plain:true, maximizable:false, modal: true,
		closeAction:"hide",
		layout:'fit',
		items: [{
			xtype:"form", id:'form', border:false, style:"padding:10px" ,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			baseCls: "x-plain", defaults:{anchor:"100%"},
			labelWidth:80,
			items:[{
				fieldLabel:'选择文件',
				name:'impStencil',
				xtype: "fileuploadfield",
				allowBlank:false,
				editable:false,
				buttonText: '浏览文件...'/*,
	            buttonCfg: {
	                iconCls: 'upload-icon'
	            }*/
			}, {
				xtype:'label',
				html:['提示：只能上传后缀名为<span style="color:red;font-weight:bold;">.xls</span>的文件。'],
				style:'margin-bottom:10px;padding-left:85px;'
			}]
		}],
		listeners:{
			// 隐藏窗口时重置上传表单
			hide: function(window) {
				window.find('xtype', 'form')[0].getForm().getEl().dom.reset();
    			SiteVideoNvrConfig.tree.root.reload();
			}
		},
		buttonAlign:'center', 
		buttons:[{
			text: "导入", iconCls: "saveIcon", handler: function(){
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var fileUploadPath = window.find('name', 'impStencil')[0].getValue();
				if(fileUploadPath.indexOf(".xls") < 0){
					MyExt.Msg.alert('只能上传后缀名为<span style="color:red;font-weight:bold;">.xls</span>的文件。');
					return;
				}
				form.submit({  
                	url: ctx+'/siteVideoNvrChanel!saveUpload.action',  
                	waitTitle:'请稍候',
               	 	waitMsg: '正在上传报表文件请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',
                	// 请求成功后的回调函数
                	success: function(form, action) { 
                		var result = Ext.util.JSON.decode(action.response.responseText);
		                if(result.success){
							alertSuccess();
							// 导入成功后隐藏导入窗口
	            			SiteViedoNvrImport.win.hide();
//							setTimeout(function(){
//								self.location.reload();
//							}, 1000);
		                } else {
							alertFail(result.errMsg);
		                }
                	},
                	// 请求失败后的回调函数
				    failure: function(form, action){
				        if(self.loadMask)    self.loadMask.hide();
				        Ext.Msg.alert('提示', action.result.errMsg);
				    }
            	}); 
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	})
	/** **************** 定义文件上传窗口结束 **************** */
	
})