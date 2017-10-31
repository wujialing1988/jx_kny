Ext.onReady(function(){
	Ext.namespace('MatConsumeInfo');
	MatConsumeInfo.searchParam = {};
	MatConsumeInfo.uploadWin = new Ext.Window({
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
						id: 'image',
						name:'image',
						labelWidth:60,
						width:160,
						inputType:'file',
						xtype : "field",
						allowBlank:false
					}],
					buttons:[{
						text: "导入", iconCls: "saveIcon", scope: this, handler: function(){
							var form = uploadFrom.getForm();
							var imagePath = Ext.getCmp("image").getValue();
							if(imagePath==null||imagePath==""){
								alertFail("尚未选择excel文件！");
								return;
							}
							if(imagePath.indexOf(".xls")==-1||imagePath.indexOf(".xlsx")!=-1){
								alertFail("请导入excel2003格式文件！");
								return;
							}
							form.submit({  
	                        	url: ctx+'/matConsumeUpload.action',  
	                       	 	waitMsg: '正在上传Excel文件请稍候...', 
	                       	 	method: 'POST',
	                       	 	//jsonData: dataParam,
	                       	 	params:{
	                       	 		rdpIDX: dataParam.rdpIDX,
	                       	 		trainTypeIDX: dataParam.trainTypeIDX,
	                       	 		trainNo: dataParam.trainNo,
	                       	 		trainTypeShortName: dataParam.trainTypeShortName,
	                       	 		repairClassIDX: dataParam.repairClassIDX,
	                       	 		repairClassName: dataParam.repairClassName,
	                       	 		repairtimeIDX: dataParam.repairtimeIDX,
	                       	 		repairtimeName: dataParam.repairtimeName,
	                       	 		repairOrgID: dataParam.repairOrgID
	                       	 	},
	                       	 	enctype: 'multipart/form-data',
	                        	success: function(response, options) { 
	                            	if(MatConsumeInfo.grid.loadMask)   MatConsumeInfo.grid.loadMask.hide();
					                var result = Ext.util.JSON.decode(options.response.responseText);
					                if(result.success==true){
					                	form.getEl().dom.reset();
					                	MatConsumeInfo.uploadWin.hide();
					                	if(result.resultMsg.errMsg == null){ //数据导入成功
					                		MatConsumeInfo.grid.afterSaveSuccessFn(result, response, options);
					                	} else {
					                		MatConsumeInfo.grid.afterSaveFailFn(result.resultMsg, response, options);
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
	MatConsumeInfo.grid = new Ext.yunda.RowEditorGrid({
	    loadURL: ctx + '/matConsumeInfo!pageQuery.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/matConsumeInfo!saveOrUpdate.action',
	    deleteURL: ctx + '/matConsumeInfo!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar:['search',{ 
	    		text:"新增", iconCls:"addIcon", 
              	handler:function(){
                	MatTypeSelect.win.show();
                	MatTypeSelect.tabs.activate(0);
                	MatTypeSelect.oftenGrid.store.load();
                	MatTypeSelect.grid.store.load();
             	}
	        },
	        { text:"下载模板", iconCls:"application-vnd-ms-excel", 
	            handler:function(){
	            	window.location.href = ctx + '/matConsumeInfo!download.action';
	            }
	        },{ 
	    		text:"导入", iconCls:"page_excelIcon", 
              	handler:function(){
                	MatConsumeInfo.uploadWin.show();
             	}
	        },'delete'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'物料编码', dataIndex:'matCode', editor:{ maxLength:50 }, width: 120
		},{
			header:'物料描述', dataIndex:'matDesc', editor:{  maxLength:50 }, width: 420
		},{
			header:'计量单位', dataIndex:'unit', editor:{  maxLength:20, readOnly:true, style:"color:gray" },
			searcher: {disabled: true}, width: 80				
		},{
			header:'计划单价', dataIndex:'price',  editor:{ xtype:"numberfield", maxLength:8, vtype: 'nonNegativeFloat', readOnly:true, style:"color:gray"  },
			searcher: {disabled: true}		
		},{
			header:'数量', dataIndex:'qty',  editor:{ xtype:"numberfield", maxLength:8, vtype: 'nonNegativeInt'  },
			searcher: {disabled: true}		
		},{
			header:'日期', dataIndex:'consumeDate',xtype: "datecolumn", format: "Y-m-d", 
			editor:{xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd'}, initNow: true},
			searcher: {disabled: true}		
		},{
			header:'rdpIDX', dataIndex:'rdpIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'trainTypeIDX', dataIndex:'trainTypeIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'trainTypeShortName', dataIndex:'trainTypeShortName', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'trainNo', dataIndex:'trainNo', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairClassIDX', dataIndex:'repairClassIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairClassName', dataIndex:'repairClassName', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairtimeIDX', dataIndex:'repairtimeIDX', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairtimeName', dataIndex:'repairtimeName', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairOrgID', dataIndex:'repairOrgID', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'repairOrgName', dataIndex:'repairOrgName', hidden:true, editor: { xtype:'hidden' }
		}],		
	    searchFn: function(searchParam){
			MatConsumeInfo.searchParam = searchParam ;
	        MatConsumeInfo.grid.store.load();
		}
	});
	MatConsumeInfo.grid.createSearchWin();
	MatConsumeInfo.grid.searchWin.modal = true;
	MatConsumeInfo.grid.store.on("beforeload", function(){
		var searchParam = MatConsumeInfo.searchParam;
		var whereList = [] ;
		for (prop in searchParam) {			
	        whereList.push({propName:prop, propValue: searchParam[prop]}) ;
		}
		whereList.push({propName:'repairOrgID', propValue: teamOrgId}) ;//当前班组的人只能看到当前班组添加的物料消耗情况
		whereList.push({propName:'rdpIDX', propValue: dataParam.rdpIDX}) ;
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
	});
});