/**
 * 整备作业项目 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('ZbFwWi');//定义命名空间
	
	/* ************* 定义全局变量开始 ************* */
	ZbFwWi.labelWidth = 100;
	ZbFwWi.fieldWidth = 150;
	ZbFwWi.idx = "";
	ZbFwWi.searchParams = {};
	ZbFwWi.zbfwIDX = "";								// 整备范围主键
	ZbFwWi.nodeIDX = "";								// 节点主键
	ZbFwWi.isSaveAndAdd = false;						// 是否是【保存并新增】 是：true, 否：false
	var saveParams = {};
	ZbFwWi.isAddAndNew = false; 
	/* ************* 定义全局变量结束 ************* */
  // 手动排序 
    ZbFwWi.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/zbFwWi!moveOrder.action',
			params: {idx: idx, orderType: orderType}
        }));
    }
	ZbFwWi.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/zbFwWi!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/zbFwWi!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/zbFwWi!logicDelete.action',            //删除数据的请求URL
	    saveWinWidth: 800,
	    saveWinHeight: 600,
	    storeAutoLoad: false,
	    singleSelect: true,
	    tbar:['search','add','delete','->', {
			text:i18n.common.button.moveTop, iconCls:'moveTopIcon', handler: function() {
				ZbFwWi.moveOrder(ZbFwWi.grid, ORDER_TYPE_TOP);
			}
		}, {
			text:i18n.common.button.moveUp, iconCls:'moveUpIcon', handler: function() {
				ZbFwWi.moveOrder(ZbFwWi.grid, ORDER_TYPE_PRE);
			}
		}, {
			text:i18n.common.button.moveDown, iconCls:'moveDownIcon', handler: function() {
				ZbFwWi.moveOrder(ZbFwWi.grid, ORDER_TYPE_NEX);
			}
		}, {
			text:i18n.common.button.moveBottom, iconCls:'moveBottomIcon', handler: function() {
				ZbFwWi.moveOrder(ZbFwWi.grid, ORDER_TYPE_BOT);
			}
		}],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'整备范围主键', dataIndex:'zbfwIDX',hidden:true, editor:{  maxLength:50,hidden:true}
		},{
			header:'节点主键', dataIndex:'nodeIDX',hidden:true, editor:{  maxLength:50,hidden:true}
		},{
			header:'编号', dataIndex:'wICode',hidden:true,editor:{  maxLength:50, hidden:true }
		},{
			header:i18n.ZbFw.seqNo, dataIndex:'seqNo',width:60,editor:{ xtype:'numberfield', maxLength:3, hidden:true }
		},{
			header:i18n.ZbFw.wIName, dataIndex:'wIName',width:30,editor:{  maxLength:100, anchor:"50%", allowBlank:false },	
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	            html = "<span><a href='#' onclick='ZbFwWi.grid.toEditFn(\""+ ZbFwWi.grid + "\",\""+ rowIndex +"\")'>"+value+"</a></span>";
	            return html;
	        }
		},{
			header:i18n.ZbFw.wIDesc, dataIndex:'wIDesc',editor:{  maxLength:500, anchor:"90%", xtype:'textarea' }
		},{
			header:'是否需要照相确认', dataIndex:'isCheckPicture',hidden:true,
			editor:{anchor:"90%", id:"isCheckPicture_edit",xtype:'checkboxgroup', name: "isCheckPicture", fieldLabel: '是否需要照相确认',xtype:'hidden', 
				items: [{   
			        name:'isCheckPicture',id:"isCheckPicture_item", inputValue:DOCHECK
			    }]
			},
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
				var fieldValue;
				if(value && value == DOCHECK){
					fieldValue = i18n.common.tip.yes; 
				}else {
					fieldValue = i18n.common.tip.no; 
				}
				
				return fieldValue;
	        }
		}],
		
		beforeSaveFn: function(data){ 
			//如果前台没选择核实，传递过来为null，赋默认值0,未核实
			if(!data.isCheckPicture){
				data.isCheckPicture = 0
			}
			return true; 
		},
		createSaveWin: function(){
	        if(this.saveForm == null) this.createSaveForm();
	        //计算查询窗体宽度
	        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
	        this.saveWin = new Ext.Window({
	            title:i18n.ZbFw.tabpanelProjectEditTitle,width:650, height:450, plain:true, closeAction:"hide",  maximizable:true,
	            layout:'border',
	            defaults:{border:false},
	            items:[{
	            	region:'north', height: 190, layout:'fit', frame:true, items: this.saveForm ,
		            buttonAlign:'center',
		            buttons: [{
		                text: i18n.common.button.save, iconCls: "saveIcon", scope: this, handler: function() {
							ZbFwWi.isSaveAndAdd = false;
							this.saveFn();
		                }
		            }, {
		                text: i18n.common.button.add, iconCls: "addIcon", scope: this, handler: function() {
		                	 // 禁用【整备范围作业项目】表格的工具栏
		                	ZbFwWidi.grid.getTopToolbar().disable();
							ZbFwWi.isSaveAndAdd = true;
							this.saveFn();
		                }
		            }, {
		                text: i18n.common.button.close, iconCls: "closeIcon", scope: this, handler: function(){ 
		                this.saveWin.hide();
		                 // 启用【整备范围作业项目】表格的工具栏
		                ZbFwWidi.grid.getTopToolbar().enable();
		                }
		            }]
	            }, {
	            	region:'center',  layout:'fit', items: ZbFwWidi.grid
	            }],
	            listeners:{
	            	hide: function (window) {
	            		ZbFwWidi.grid.rowEditor.slideHide();
	            	}
	            }
	        });
	    },
	    afterShowSaveWin: function(){
	    	this.saveForm.find('name', 'zbfwIDX')[0].setValue(ZbFwWi.zbfwIDX);
	    	this.saveForm.find('name', 'nodeIDX')[0].setValue(ZbFwWi.nodeIDX);
	    	ZbFwWidi.grid.getTopToolbar().disable();
	    	ZbFwWidi.grid.store.removeAll();
	    },
	    afterSaveSuccessFn: function(result, response, options){
	        this.store.reload();
	        alertSuccess();
	        if (!ZbFwWi.isSaveAndAdd) {
	        	// 启用【整备范围作业项目】表格的工具栏
	        	ZbFwWidi.grid.getTopToolbar().enable();
	        	this.saveForm.find('name', 'idx')[0].setValue(result.entity.idx);
	        	this.saveForm.find('name', 'seqNo')[0].setValue(result.entity.seqNo);
	        } else {
	        	this.saveForm.getForm().reset();
	    		this.saveForm.find('name', 'zbfwIDX')[0].setValue(ZbFwWi.zbfwIDX);
	    		this.saveForm.find('name', 'nodeIDX')[0].setValue(ZbFwWi.nodeIDX);
		    	ZbFwWidi.grid.store.removeAll();
	        }
	       ZbFwWidi.zbfwwiIDX = result.entity.idx;
	      
	      
	    },
	     //取整备作业项中主键赋值到作业项目中关联查询子表数据
	     afterShowEditWin: function(record, rowIndex){
	     	if(record.get('isCheckPicture') == 1){
	     		Ext.getCmp("isCheckPicture_item").setValue(true);
	     	}
        	// 启用【整备范围作业项目】表格的工具栏
        	ZbFwWidi.grid.getTopToolbar().enable();
	     	var record = ZbFwWi.grid.selModel.getSelections();
	        ZbFwWidi.zbfwwiIDX = record[0].get("idx");
	        ZbFwWidi.grid.store.load();
	     },
	     
	     //重写查询方法
	   createSearchWin: function(){
        if(this.searchForm == null) this.createSearchForm();
        //计算查询窗体宽度
        this.searchWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.searchFormColNum + 60;
        this.searchWin = new Ext.Window({
            title:i18n.common.button.research, width:this.searchWinWidth, height:this.searchWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.searchForm, 
            buttons: [{
                text: i18n.common.button.research, iconCls: "searchIcon", scope: this, handler: function(){ 
                    var searchParam = this.searchForm.getForm().getValues();
                    searchParam.zbfwIDX = ZbFwWi.zbfwIDX;
                    searchParam.nodeIDX = ZbFwWi.nodeIDX ;
                    searchParam = MyJson.deleteBlankProp(searchParam);
                    this.searchFn(searchParam) ;
                }
            }, {
                text: i18n.common.button.reset, iconCls: "resetIcon", scope: this, handler: function(){ 
                    this.searchForm.getForm().reset();
                    ZbFwWi.grid.store.load();
                }
            }, {
                text: i18n.common.button.close, iconCls: "closeIcon", scope: this, handler: function(){ this.searchWin.hide(); }                
            }]
        });
       }
	});
	// 表格数据加载前的参数设置
 	ZbFwWi.grid.store.on('beforeload', function(){
		ZbFwWi.searchParams.nodeIDX = ZbFwWi.nodeIDX;
		this.baseParams.entityJson = Ext.util.JSON.encode(ZbFwWi.searchParams);
	});
	
	// 设置默认排序
	ZbFwWi.grid.store.setDefaultSort('seqNo', 'ASC');
});