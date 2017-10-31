Ext.onReady(function(){
	Ext.namespace('MatTypeList');                       //定义命名空间
	MatTypeList.searchParam = {};
	MatTypeList.uploadWin = new Ext.Window({
		title:"上传", width:400, height:120, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:false, modal: true,
		items: uploadFrom = new Ext.form.FormPanel({
			layout:"form", border:false, style:"padding:10px" , fileUpload:true,
			align:'center',baseCls: "x-plain", defaultType:'textfield',defaults:{anchor:"95%"},
			items:[{
				xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
				items:[{
					align:'center',	defaultType:'textfield', border:false, layout:'form',
					labelWidth: 60,	columnWidth:1,	defaults:{anchor:"95%"}, baseCls: "x-plain",
					buttonAlign:'center',
					items:[{
						fileUpload:true,
						fieldLabel:'选择',
						id: 'imageV',
						name:'image',
						labelWidth:60,
						width:160,
						xtype : "fileuploadfield",
						allowBlank:false,
						buttonText: '浏览...'
					}],
					buttons:[{
						text: "导入", iconCls: "saveIcon", scope: this, handler: function(){
							var form = uploadFrom.getForm();
							var imagePath = Ext.getCmp("imageV").getValue();
							if(imagePath==null||imagePath==""){
								alertFail("尚未选择excel文件！");
								return;
							}
							if(imagePath.indexOf(".xls")==-1||imagePath.indexOf(".xlsx")!=-1){
								alertFail("请导入excel2003格式文件！");
								return;
							}
							form.submit({  
	                        	url: ctx+'/matTypeListFileUpload.action',  
	                       	 	waitMsg: '正在上传Excel文件请稍候...', 
	                       	 	method: 'POST',
	                       	 	enctype: 'multipart/form-data',
	                        	success: function(response, options) { 
	                            	if(MatTypeList.grid.loadMask)   MatTypeList.grid.loadMask.hide();
					                var result = Ext.util.JSON.decode(options.response.responseText);
					                if(result.success==true){
					                	form.getEl().dom.reset();
					                	MatTypeList.uploadWin.hide();
					                	if(result.resultMsg.errMsg == null){ //数据导入成功
					                		 MatTypeList.grid.store.reload();
        									 alertSuccess();
					                		//MatTypeList.grid.afterSaveSuccessFn(result, response, options);
					                	} else {
					                		//MatTypeList.grid.afterSaveFailFn(result.resultMsg, response, options);
					                		 MatTypeList.grid.store.reload();
        									 alertFail(result.resultMsg);
					                	}
					                } else {
					                	Ext.Msg.alert('上传失败!',"没有选择文件或文件体积过大！");
					                }
	                        	}
	                    	}); 
						}
					}]
				}]
			}]
		})
	});
	MatTypeList.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/matTypeList!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matTypeList!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/matTypeList!delete.action',            //删除数据的请求URL
	    tbar:['search','add',
	        { text:"下载模板", iconCls:"application-vnd-ms-excel", 
	            handler:function(){
	            	window.location.href = ctx + '/matTypeList!download.action';
	            }
	        },
	    	{ text:"导入", iconCls:"page_excelIcon", 
              handler:function(){
                MatTypeList.uploadWin.show();
             }
	        },'delete','refresh'],
	    //viewConfig: null,
		fields: [{
			header:'物料编码', dataIndex:'matCode', editor:{ id:'matCode', maxLength:50, allowBlank: false }, width: 120
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:50, allowBlank: false }, width: 420
		},{
			header:'计量单位', dataIndex:'unit', editor:{  maxLength:20, allowBlank: false },
			searcher: {disabled: true}, width: 80				
		},{
			header:'计划单价', dataIndex:'price',  editor:{ xtype:"numberfield", maxLength:6, vtype: 'nonNegativeFloat'  },
			searcher: {disabled: true}		
		}],
		storeId: 'matCode',
		searchFn: function(searchParam){
			MatTypeList.searchParam = searchParam ;
			this.store.load();
		},
		afterShowSaveWin: function(){
			this.saveForm.getForm().findField("matCode").enable();
		},
		afterShowEditWin: function(record, rowIndex){
			this.saveForm.getForm().findField("matCode").disable();
		},
		beforeGetFormData: function(){
			this.saveForm.getForm().findField("matCode").enable();
		},
		afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        this.saveForm.getForm().findField("matCode").disable();
	        this.saveWin.hide();
	    },
	    afterSaveFailFn: function(result, response, options){
	    	this.store.reload();
	        alertFail(result.errMsg);
	        this.saveForm.getForm().findField("matCode").enable();
	    }
	});
	MatTypeList.grid.un("rowdblclick",MatTypeList.grid.toEditFn,MatTypeList.grid);
	//查询前添加过滤条件
	MatTypeList.grid.store.on('beforeload' , function(){
		var searchParam = MatTypeList.searchParam ;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
	//页面自适应布局
	var viewport = new Ext.Viewport({ layout:'fit', items:MatTypeList.grid });	
});