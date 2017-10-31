/**
 * 台位绑定工位 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('BindsensorStation');
	/** **************** 定义全局变量开始 **************** */
	BindsensorStation.labelWidth = 100;
	BindsensorStation.fieldWidth = 140;
	BindsensorStation.idx = null;
	/** **************** 定义全局变量结束 **************** */
	
	/** **************** 定义全局函数开始 **************** */
	// 初始化
	BindsensorStation.initFn = function(form) {
		
        // 设置基本信息
		form.find('fieldLabel', '台位编号')[0].setValue(deskCode);
		form.find('fieldLabel', '台位名称')[0].setValue(deskName);
		form.find('fieldLabel','站场')[0].setValue(mapcode)
	}
	/** **************** 定义全局函数结束 **************** */
	
	/** **************** 定义基本信息表单开始 **************** */
	BindsensorStation.form = new Ext.form.FormPanel({
		style: 'padding-left:20px;',
		layout: 'column', labelWidth: BindsensorStation.labelWidth,
		defaults: {
			xtype: 'container', columnWidth: .25, layout: 'form',
			defaults: {
				xtype: 'textfield', readOnly: true,
				width: BindsensorStation.fieldWidth, 
				style: 'background:none; border: none;', anchor: '100%'
			}
		},
		items: [{
			items: [{ fieldLabel: '台位编号' }]
		}, {
			items: [{ fieldLabel: '台位名称' }]
		},{
			items: [ {fieldLabel: '站场',hidden:true}]  //不显示站场的数据
		}]
	});
	/** **************** 定义基本信息表单结束 **************** */
	BindsensorStation.grid = new Ext.yunda.Grid({
		loadURL: ctx + '/tWTSensor!pageQuery.action',                 //装载列表数据的请求URL
	    deleteURL: ctx + '/tWTSensor!logicDelete.action',            		//删除数据的请求URL
	    searchFormColNum: 1,
	    saveFormColNum: 2,
	    tbar: [
//	    '集线盒编号：',{xtype:'Base_combo',id:'boxCodeId',hiddenName: "boxCode",
//	    	entity:"com.yunda.twt.sensor.entity.TWTSensor",
//	    	displayField:"boxCode",valueField:"boxCode",
//	    	fields:["boxCode","boxCode"],					
//			returnField:[{widgetId: 'boxCodeId', propertyName: 'boxCode'}],
//			queryHql: 'FROM TWTSensor WHERE  stationCode is null',    //拼写查询条件
//            listeners : {
//	        	"select" : function() {
//	            Ext.getCmp("sensorCodeId").reset();
//	            Ext.getCmp("sensorCodeId").clearValue();
//	            Ext.getCmp("sensorCodeId").queryHql = "FROM TWTSensor where boxCode= " + this.getValue() ;
//	            Ext.getCmp("sensorCodeId").cascadeStore();
//        }
//      }
//	    },'&nbsp;&nbsp;','传感器编号', {
//	 		xtype:'Base_combo',id:'sensorCodeId',hiddenName: "sensorCode",
//	    	entity:"com.yunda.twt.sensor.entity.TWTSensor",
//	    	displayField:"sensorCode",valueField:"sensorCodeR",
//	    	fields:["sensorCode","sensorCode"],					
//			returnField:[{widgetId: 'sensorCodeId', propertyName: 'sensorCode'}],
//            listeners : {
//				"beforequery" : function() {
//					// 选择段前先选局
//					var com = Ext.getCmp("boxCodeId").getValue();
//					if (com == "" || com == null) {
//						MyExt.Msg.alert("请先选择集线盒号！");
//						return false;
//					}
//				}
//			}
//	    },'&nbsp;&nbsp;',
//	           {text:'确定绑定',iconCls:'addIcon',handler:function(){
//              	   var boxCodeId=Ext.getCmp("boxCodeId").getValue();
//              	   var sensorCodeId=Ext.getCmp("sensorCodeId").getValue();
//           	       if(boxCodeId=="" || sensorCodeId==""){
//           	       	  MyExt.Msg.alert("集线盒编号与传感器编号确定一个传感器");
//           	       	  return ;
//           	       }
//           	       Ext.Ajax.request({
//           	          url: ctx + '/tWTSensor!bindSensorSave.action',
//			          params : {deskCode : deskCode , deskName : deskName ,mapcode : mapcode ,boxCodeId : boxCodeId ,sensorCodeId: sensorCodeId},
//           	          success:function(response,options){
//           	          	var result=Ext.util.JSON.decode(response.responseText);
//           	          	if (result.errMsg == null) {
//			                    alertSuccess();
//			                    BindsensorStation.grid.store.load();
//			                } else {
//			                    alertFail(result.errMsg);
//			                    BindsensorStation.grid.store.load();
//			                }
//           	          },
//           	          failure: function(response, options){
//               			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
//                      }
//           	       });
//           } },
	    'refresh',
           '&nbsp;&nbsp;',
	           {text:'选择绑定传感器',iconCls:'chart_attributeConfigIcon',handler:function(){
       	           BindsensorStation.batchWin.show();
       	           BindsensorStationSelect.grid.store.load();
	           }},{
					text:'置顶', iconCls:'moveTopIcon', handler: function() {
						BindsensorStation.moveOrder(BindsensorStation.grid, ORDER_TYPE_TOP);
					}
				}, {
					text:'上移', iconCls:'moveUpIcon', handler: function() {
						BindsensorStation.moveOrder(BindsensorStation.grid, ORDER_TYPE_PRE);
					}
				}, {
					text:'下移', iconCls:'moveDownIcon', handler: function() {
						BindsensorStation.moveOrder(BindsensorStation.grid, ORDER_TYPE_NEX);
					}
				}, {
					text:'置底', iconCls:'moveBottomIcon', handler: function() {
						BindsensorStation.moveOrder(BindsensorStation.grid, ORDER_TYPE_BOT);
					}
				}
				],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'集线盒编号', dataIndex:'boxCode', editor:{  allowBlank:false, maxLength:50 }
		},{
			header:'传感器编号', dataIndex:'sensorCode', editor:{  allowBlank:false, maxLength:100 }
		},{
			header:'最小感应门限(mm)', dataIndex:'minLimit', editor:{ xtype:'hidden', maxLength:50 }
		},{
			header:'最高感应门限(mm)', dataIndex:'maxLimit', editor:{ xtype:'hidden', maxLength:100 }
		},{
			header:'检查周期', dataIndex:'checkCycle', editor:{  xtype:'hidden' }
		},{
			header:'安装位置', dataIndex:'location', editor:{  xtype:'hidden'}
		},{
			header:'绑定序列号', dataIndex:'seqNo', editor:{ disabled: true }
		},{
			header:'绑定台位号', dataIndex:'stationCode', editor:{  disabled: true }
		},{
			header:'绑定台位名称', dataIndex:'stationName', editor:{  disabled: true }
		},{
			header : '解除绑定',align: 'center',editor: {},
			renderer:function(v,x,r){			
			return "<img src='"+deleteIcon+"' alt='解除绑定' style='cursor:pointer' onclick='BindsensorStation.delect(\"" + v + "\",1)'/>";
		}
		}],toEditFn: function(grid, rowIndex, e){}
	});
	// 设置默认排序
	BindsensorStation.grid.store.setDefaultSort('seqNo', 'ASC');
	// 手动排序 
    BindsensorStation.moveOrder = function(grid, orderType) {
    	if(!$yd.isSelectedRecord(grid)) {
			return;
		}
		var idx = $yd.getSelectedIdx(grid)[0];
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
        	scope: grid,
        	url: ctx + '/tWTSensor!moveOrder.action',
			params: {idx: idx, orderType: orderType},
			//请求成功后的回调函数
		    success: function(response, options){
		        if(self.loadMask)    self.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (result.errMsg == null) {       //操作成功     
		            alertSuccess();
		            this.store.reload();
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    }
        }));
    }
		/** ****定义删除方法***** */
		BindsensorStation.delect = function() {
				var records = BindsensorStation.grid.selModel.getSelections();
				if (records.length < 1) {
					MyExt.Msg.alert("尚未选择一条记录！");
					return;
				}
				Ext.Msg.confirm('提示',"解除绑定后不能恢复，是否继续？", function(btn){
					if(btn == "yes"){
				Ext.Ajax.request({
					url : ctx+ '/tWTSensor!twtRelieveBin.action',
					params : {ids : $yd.getSelectedIdx(BindsensorStation.grid,BindsensorStation.grid.storeId)},
					success : function(response, options) {
						var result = Ext.util.JSON.decode(response.responseText);
						if (result.errMsg == null) {
						alertSuccess();
							BindsensorStation.grid.store.reload();
						} else {
							alertFail(result.errMsg);
						}
					},
					failure : function(response, options) {
						MyExt.Msg.alert("请求失败，服务器状态代码：\n"+ response.status + "\n"+ response.responseText);
					}
					});
					}
				});
				
			}
		/** ******定义删除方法结束****** */

		/** **************定义批量添加的窗体*************** */
		BindsensorStation.batchWin=new Ext.Window({
		     title:'传感器选择', width:700, height:400, plain:true, closeAction:"hide", buttonAlign:'center', layout:'border',frame:true,
		    	maximizable:false,  modal:true,
		    	items:[{
		    	      region:'north',
		    	      collapsible :true,
		    	      height:66,
		    	      title:'查询',
		    	      items:[BindsensorStationSelect.batchForm]   //查询的表单
		    	   },{
		    	      region:'center',
		    	      layout:'fit',
		    	      items:[BindsensorStationSelect.grid]   //加载数据的grid
		    	   }],
		       buttons:[{text:'确定',iconCls:'yesIcon',handler:function(){
		       		BindsensorStation.addBindSensor();
		       	
		       }},'&nbsp;&nbsp;',
	              {text:'关闭',iconCls:'closeIcon',handler:function(){
	              	    BindsensorStation.batchWin.hide();
	              }}]
		});
		/** ***********定义批量添加表单结束************** */
		
		//加载数据
		BindsensorStation.grid.store.on('beforeload',function(){
			var searchParams ={};
		    searchParams = MyJson.deleteBlankProp(searchParams);
			var whereList = [];
			for(prop in searchParams){
				whereList.push({propName:prop, propValue:searchParams[prop]});
			}
			var sql = "STATION_CODE = '"+deskCode+"' ";
			whereList.push({sql: sql, compare:Condition.SQL});
			this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
			
		});
		
		//点击批量添加按钮点击确定选择绑定数据
		BindsensorStation.addBindSensor=function(){
           var records=BindsensorStationSelect.grid.selModel.getSelections();
           if(records.length<1){
           	  MyExt.Msg.alert("尚未选择一条记录！");
           	  return ;
           }
           var datas=new Array();
	       for(var i=0;i<records.length;i++){
	           var data=records[i].data;
	           datas.push(data);
	       }
           Ext.Ajax.request({
   	          url: ctx + '/tWTSensor!saveStationBySelect.action',
   	          jsonData:datas,
	          params : {deskCode : deskCode , deskName : deskName ,mapcode : mapcode },
   	          success:function(response,options){
   	          	var result=Ext.util.JSON.decode(response.responseText);
   	          	if (result.errMsg == null) {
	                    alertSuccess();
	                    BindsensorStation.grid.store.load();
	                    BindsensorStationSelect.grid.store.load();
	                } else {
	                    alertFail(result.errMsg);
	                    BindsensorStation.grid.store.load();
	                }
   	          },
   	          failure: function(response, options){
       			 MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
              }
   	       });
          BindsensorStationSelect.grid.store.load();
		}
	// 自适应页面布局
	new Ext.Viewport({
		layout: 'border', defaults: { layout: 'fit', border: false },
		items: [{
			region: 'north',
			height: 60,
			frame: true,
			title: '绑定工位',
			collapsible: true,
			collapseMode: 'mini',
			items: BindsensorStation.form
		}, {
			region: 'center', autoScroll : false,
			items:[BindsensorStation.grid]
		}], 
		listeners: {
			render: function(form) {
				BindsensorStation.initFn(form);
			}
		}
	});
})