Ext.onReady(function() {
	
	Ext.ns('InspectScopeImport');
	
	InspectScopeImport.win = new Ext.Window({
		title: '导入巡检标准',
		height: 140, width: 480,
		plain: true,
		modal: true,
		closeAction: 'hide',
		layout: 'fit',
		items: [{
			xtype: 'form', baseCls: 'plain',
			padding: 20, labelWidth: 80,
			
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			fileUpload:true,															
			/** ******** 注意要实现文件上传，必须的字段 ******** */
			
			defaults:{anchor:"100%"},
			items: [{
				fieldLabel:'选择文件',
				name:'file',
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
		buttonAlign:'center', 
		buttons:[{
			text: "导入", iconCls: "saveIcon", handler: function() {
				var window = this.findParentByType('window');
				var form = window.find('xtype', 'form')[0].getForm();
				if (!form.isValid()) {
					return;
				}
				var fileUploadPath = window.find('name', 'file')[0].getValue();
				if(fileUploadPath==null||fileUploadPath=="") {
					MyExt.Msg.alert('请选择导入模板(<span style="color:red;font-weight:bold;">*.xls</span>)文件！');
					return;
				}
				if(fileUploadPath.indexOf(".xls") < 0) {
					MyExt.Msg.alert('只能导入后缀名为<span style="color:red;font-weight:bold;">.xls</span>的文件。');
					return;
				}
				form.submit({  
                	url: ctx+'/inspectScope!importInspectScope.action',  
                	waitTitle:'请稍候',
               	 	waitMsg: '正在导入设备巡检标准，请稍候...', 
               	 	method: 'POST',
               	 	enctype: 'multipart/form-data',

               	 	// 请求成功后的回调函数
               	    success: function(form, action) {
               	    	alertSuccess();
	           	    	window.hide();
	           	    	// 如果选择了设备类别树节点，则重新加载设备巡检标准列表
               	    	var sm = InspectScope.tree.getSelectionModel();
            			var node = sm.getSelectedNode();
            			if (Ext.isEmpty(node)) {
            				return;
               	    	}
               	    	InspectScope.grid.store.reload();
               	    },
                	// 请求失败后的回调函数
               	    failure: function(form, action) {
               	        switch (action.failureType) {
               	            case Ext.form.Action.CLIENT_INVALID:
               	                Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
               	                break;
               	            case Ext.form.Action.CONNECT_FAILURE:
               	                Ext.Msg.alert('Failure', 'Ajax communication failed');
               	                break;
               	            case Ext.form.Action.SERVER_INVALID:
               	            	var responseText = action.response.responseText;
               	           		if (-1 != responseText.indexOf('>')) {
               	           			responseText = responseText.split('>')[1];
               	           		}
               	           		if (-1 != responseText.indexOf('<')) {
               	           			responseText = responseText.split('<')[0];
               	           		}
               	            	var result = Ext.decode(responseText);
               	            	alertFail(result.errMsg);
               	       }
               	    }
            	}); 
			}
		}, {
			text:'关闭', iconCls:'closeIcon', handler: function() {
				this.findParentByType('window').hide();
			}
		}]
	});
	
});