/**
 * 常用物料清单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('UsedMat');                       //定义命名空间
	
	/** ****************** 定义全局变量开始 ****************** */
	UsedMat.labelWidth = 100;
	UsedMat.fieldWidth = 140;
	/** ****************** 定义全局变量结束 ****************** */
	
	/** ****************** 定义全局函数开始 ****************** */
	// 显示清单明细
	UsedMat.showDetail = function(rowIndex) {
		// 重新加载清单信息
		var record = UsedMat.grid.store.getAt(rowIndex);
		UsedMatDetail.form.getForm().loadRecord(record);
		// 根据清单信息主键，重新加载清单信息明细
		UsedMatDetail.usedMatIdx = record.get('idx');
		UsedMatDetail.grid.store.load();
		// 显示清单信息明细窗口
		UsedMatDetail.detailWin.show();
		UsedMatDetail.detailWin.setTitle('编辑');
	}	
	UsedMat.operate = function(idx) {
		UsedMatPerson.usedMatIdx = idx;
		UsedMatPerson.win.show();
		UsedMatPerson.grid.store.load();
	}
	/** ****************** 定义全局函数结束 ****************** */
	
	/** ****************** 定义查询表单开始 ****************** */
	UsedMat.searchForm = new Ext.form.FormPanel({
		labelWidth:UsedMat.labelWidth,
		baseCls:"x-plain", border: false, 
		style: "padding-left:20px;", 
		layout:"column",
		padding:"10px",
		items:[
			{
				layout:"form",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[{ 
	            	fieldLabel:"清单名称",name:"usedMatName",xtype: 'textfield',width:UsedMat.fieldWidth,maxLength:50 
	            }]
			},
			{
				layout:"column",
				baseCls:"x-plain", border: false,
				columnWidth:0.33,
				items:[{
					columnWidth:0.33, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
						xtype:'button', text:'查询', iconCls:'searchIcon', width: 80, handler: function() {
							UsedMat.grid.store.load();
						}
					}]
				}, {
					columnWidth:0.33, layout: 'form', baseCls:"x-plain", border: false,
					items:[{
						xtype:'button', text:'重置', iconCls:'resetIcon', width: 80, handler: function() {
							UsedMat.searchForm.getForm().reset();
							UsedMat.grid.store.load();
						}
					}]
				}]
			}
		]
	})
	/** ****************** 定义查询表单结束 ****************** */
	
	/** ****************** 定义主体表格开始 ****************** */
	UsedMat.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/usedMat!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/usedMat!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/usedMat!logicDelete.action',            //删除数据的请求URL
		tbar: [{
			text:'新增', iconCls:'addIcon', handler: function(){
				// 重新【清单信息】表单
				Ext.getCmp("idx_d").setValue("");
				Ext.getCmp("usedMatName_d").setValue("");
				Ext.getCmp("usedMatDesc_d").setValue("");
				// 重新【清单明细】列表
				UsedMatDetail.grid.store.removeAll();
				// 显示【常用物料清单信息维护】窗口
				UsedMatDetail.detailWin.show();
				UsedMatDetail.detailWin.setTitle('新增');
			}}, 'delete', {
			text:'刷新', iconCls:"refreshIcon", handler: function() {
           		self.location.reload();
           }
       }],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'清单名称', dataIndex:'usedMatName', editor:{  maxLength:50 }, width: 20,
			renderer: function(value, metaData, record, rowIndex, colIndex, store){ 
	            var html = "";
	    		html = "<span><a href='#' onclick='UsedMat.showDetail(" + rowIndex + ")'>"+value+"</a></span>";
	            return html;
			}
		},{
			header:'清单描述', dataIndex:'usedMatDesc', editor:{  maxLength:200 }, width: 60
		},{
			header:'适用人维护', dataIndex:'idx', width:20, editor:{ disabled:true, hidden:true },
			renderer:function(value, metaData, record, rowIndex, colIndex, store){			
				return "<img src='" + imgpathx + "' alt='操作' style='cursor:pointer' onclick='UsedMat.operate(\"" + value + "\")'/>";
			}, sortable:false
		
		}]
	});
	// 设置默认已“清单名称”进行升序排序
	UsedMat.grid.store.setDefaultSort('usedMatName', 'ASC');
	UsedMat.grid.un('rowdblclick', UsedMat.grid.toEditFn, UsedMat.grid);
	UsedMat.grid.store.on('beforeload', function() {
		var searchParams = UsedMat.searchForm.getForm().getValues();
		searchParams = MyJson.deleteBlankProp(searchParams);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParams);
	})
	/** ****************** 定义主体表格结束 ****************** */
	
	//页面自适应布局
	var viewport=new Ext.Viewport({
		layout:'fit',
		items:[{
		    layout:'border',frame:true,
		    // 查询表单
		    items:[{
		       region:'north',
		       collapsible :true,
		       title:'查询',
			   height:80,
		       frame:true,
		       items:[UsedMat.searchForm]
		    // 主体表格
		    },{
		      region:'center',
		      frame:true,
		      layout:'fit',
		      items:[UsedMat.grid]
		    }]	
		}]
	});
	
});