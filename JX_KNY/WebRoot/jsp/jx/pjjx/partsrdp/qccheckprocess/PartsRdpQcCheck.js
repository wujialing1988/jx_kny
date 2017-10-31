/**
 * 配件检修质量检验 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
 
Ext.onReady(function(){
	
	Ext.namespace('PartsRdpQcCheck');			// 定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpQcCheck.labelWidth = 100;
	PartsRdpQcCheck.fieldWidth = 140;
	PartsRdpQcCheck.searchParams = {};
	/** ************** 定义全局变量结束 ************** */
	
	/** ************** 定义查询表单开始 ************** */
	PartsRdpQcCheck.searchForm = new Ext.form.FormPanel({
		labelWidth:PartsRdpQcCheck.labelWidth,
		layout:"column",
		style:'padding:10px 10px 0 10px;',
		defaults:{
			xtype:"container", autoEl:"div", columnWidth:0.33, layout:"form",
			defaults:{xtype:"textfield", width: PartsRdpQcCheck.fieldWidth}
		},
		items:[{
			items:[{
					id:"trainType_comb_s",
					fieldLabel: "下车车型",
					xtype: "Base_combo",
					hiddenName: "unloadTrainTypeIdx",
				    business: 'trainVehicleType',
				    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
	                fields:['idx','typeName','typeCode','vehicleType'],
	                returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"typeCode"}],
	                queryParams: {},// 表示客货类型
	    		    displayField: "typeCode", valueField: "idx",
	                pageSize: 20, minListWidth: 200,
	                width: PartsRdpQcCheck.fieldWidth + 50,
	                editable:true,
					listeners:{   
				      "select" : function(combo, record, index) {   
			                //重新加载修程下拉数据
			                var vehicleType = record.data.vehicleType ;
			                var rc_comb_s = Ext.getCmp("rc_comb_s");
			                rc_comb_s.reset();
			                rc_comb_s.clearValue();
			                rc_comb_s.getStore().removeAll();
			                rc_comb_s.queryParams = {"TrainTypeIdx":this.getValue(),"vehicleType":vehicleType};
			                rc_comb_s.cascadeStore();
				        	}   
				    }	                
				}, {
					fieldLabel: '配件规格型号',
					width: PartsRdpQcCheck.fieldWidth + 50,
					name:'PartsTypeTreeSelect_Text',
					xtype:"PartsTypeTreeSelect", editable:false,
					returnFn: function(node, e){
						PartsRdpQcCheck.searchForm.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["text"]);
						PartsRdpQcCheck.searchForm.find('name', 'specificationModel')[0].setValue(node.attributes["specificationModel"]);
					}
				},{
					name:'specificationModel', fieldLabel:'配件规格型号', xtype:'hidden'
				}]
		}, {
			items:[{
					fieldLabel: "下车车号", name: "unloadTrainNo"
				}, {
					name: 'partsNo', fieldLabel:"配件编号"
				}]
		}, {
			items:[{
					id:"rc_comb_s",
        			xtype: "Base_combo",
        			business: 'trainRC',
					entity:'com.yunda.jx.base.jcgy.entity.TrainRC',
					fields:['xcID','xcName'],
        			fieldLabel: "修程",
        			hiddenName: "unloadRepairClassIdx", 
        			displayField: "xcName", valueField: "xcID",
        			pageSize: 20, minListWidth: 200,
        			queryHql: 'from UndertakeRc'
        		}, {
					name: 'partsName', fieldLabel:"配件名称"
				}]
		}], buttonAlign: 'center',
		buttons:[{
			text:'查询', iconCls:'searchIcon', handler: function() {
				var form = PartsRdpQcCheck.searchForm.getForm();
				// 重新加载【必检】表格数据
				PartsRdpQcCheckBJ.searchParams = form.getValues();
				delete PartsRdpQcCheckBJ.searchParams.PartsTypeTreeSelect_Text
				PartsRdpQcCheckBJ.grid.store.load();
				// 重新加载【抽检】表格数据
				PartsRdpQcCheckCJ.searchParams = form.getValues();
				delete PartsRdpQcCheckCJ.searchParams.PartsTypeTreeSelect_Text
				PartsRdpQcCheckCJ.grid.store.load();
			}
		}, {
			text:'重置', iconCls:'resetIcon', handler: function() {
				// 重置查询表单
				PartsRdpQcCheck.searchForm.getForm().reset();
				// 清空下车车型字段
				PartsRdpQcCheck.searchForm.find('hiddenName', 'unloadTrainTypeIdx')[0].clearValue();
				// 清空下车车号字段
//				PartsRdpQcCheck.searchForm.find('hiddenName', 'unloadTrainNo')[0].clearValue();
				// 清空修程字段
				PartsRdpQcCheck.searchForm.find('hiddenName', 'unloadRepairClassIdx')[0].clearValue();
				// 重新加载【必检】表格数据
				PartsRdpQcCheckBJ.searchParams = {};
				PartsRdpQcCheckBJ.grid.store.load();
				// 重新加载【抽检】表格数据
				PartsRdpQcCheckCJ.searchParams = {};
				PartsRdpQcCheckCJ.grid.store.load();
				
				// 重新加载修程
	            var rc_comb_s = Ext.getCmp("rc_comb_s");
                rc_comb_s.reset();
                rc_comb_s.clearValue();
                rc_comb_s.getStore().removeAll();
                rc_comb_s.queryParams = {};
                rc_comb_s.cascadeStore();				
				
			}
		}]
	})
	/** ************** 定义查询表单结束 ************** */
	
	// 页面自适应布局
	new Ext.Viewport({
		layout:'border',
		items:[{
			title:'查询',
			frame:true,
			collapsible:true,
			region:'north',
			layout:'fit',
			height: 140,
			items: PartsRdpQcCheck.searchForm
		}, {
			region:'center',
			border: false,
			layout:'fit',
			items:[{
				xtype:"tabpanel",
				activeTab:0,
				items:[{
					layout:'fit',
					title:"必检",
					items:PartsRdpQcCheckBJ.grid
				}, {
					layout:'fit',
					title:"抽检",
					items:PartsRdpQcCheckCJ.grid,
					listeners: {
						// 修改在chrome中，首次显示抽检Tab页时，表格列存在错位的兼容性问题
						activate: function(panel) {
							// PartsRdpQcCheckCJ.excuted 是一个临时字段，用于确保该方法只会执行一次
							if (!PartsRdpQcCheckCJ.excuted) {
								PartsRdpQcCheckCJ.grid.store.reload();
								PartsRdpQcCheckCJ.excuted = true;
							}
						}
					}
				}]
			}]
		}]
	})
});

