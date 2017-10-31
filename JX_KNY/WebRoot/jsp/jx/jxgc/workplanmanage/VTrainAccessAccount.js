/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	
	Ext.ns('VTrainAccessAccount');						// 定义命名空间
	
	/** **************** 定义全局变量开始 **************** */ 
	VTrainAccessAccount.labelWidth = 100;
	VTrainAccessAccount.fieldWidth = 150;
	VTrainAccessAccount.loadMask = new Ext.LoadMask(Ext.getBody(), {msg:"请稍候..."});
	
	VTrainAccessAccount.editForm = new Ext.form.FormPanel({
		frame: true,
		padding: '10px',
	    labelWidth:VTrainAccessAccount.labelWidth,
		layout:"column",
    	defaults: { 
			columnWidth:0.5,
    		xtype:"container", autoEl:"div", layout:"form",
    		defaults: {
	    		xtype:"textfield", width: VTrainAccessAccount.fieldWidth
	    	}
    	},
		items:[{		    	
	    	items:[{
				fieldLabel: "车型",
				hiddenName: "trainTypeShortName", 
				returnField: [{
					widgetId:"trainTypeIDX_Id",propertyName:"typeID"
				}],
				displayField: "shortName", valueField: "shortName",
				pageSize: 0, minListWidth: 200,
				editable:true, allowBlank: false, forceSelection: true,
				xtype: "Base_combo",
	        	business: 'trainType',													
				fields:['typeID','shortName'],
				queryParams: {'isCx':'yes'},
				entity:'com.yunda.jx.base.jcgy.entity.TrainType',
				listeners : {   
		        	"select" : function() {   
		            	//重新加载车号下拉数据
		                var trainNo_comb = Ext.getCmp("trainNo_comb_Id");   
		                trainNo_comb.reset();  
		                trainNo_comb.clearValue(); 
		                trainNo_comb.queryParams.trainTypeShortName = this.getValue();
		                trainNo_comb.cascadeStore();	
		        	}   
		    	}
    		},{
				xtype: 'Base_comboTree',hiddenName: 'toGo',
				fieldLabel: '入段去向',
				allowBlank: false,
				treeUrl: ctx + '/eosDictEntrySelect!tree.action', rootText: '入段去向', 
				queryParams: {'dicttypeid':'TWT_TRAIN_ACCESS_ACCOUNT_TOGO'},
				selectNodeModel: 'leaf'
    		},{
    			fieldLabel: "入段时间", name: "inTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
    		},{
    			fieldLabel: "入段司机", name: "inDriver", maxLength:20
    		},{
    			fieldLabel: "计划车次", name: "planOrder", maxLength:20
    		}]
		},{    	
	    	items:[{
				id:"trainNo_comb_Id",	
				fieldLabel: "车号",
				hiddenName: "trainNo", 
				displayField: "trainNo", valueField: "trainNo",
				pageSize: 20, minListWidth: 200,
				minChars : 1,
				minLength : 4, 
				maxLength : 5,
				xtype: "Base_combo",
				business: 'trainNo',
				entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
				fields:["trainNo","makeFactoryIDX","makeFactoryName",
				{name:"leaveDate", type:"date", dateFormat: 'time'},
				"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName",
				"bId","dId","bName","dName","bShortName","dShortName"],
				queryParams:{'isCx':'no','isIn':'false','isRemoveRun':'true'},
				isAll: 'yes',
				returnField: [
		              {widgetId:"did_edit", propertyName:"dId"},//配属段ID
		              {widgetId:"dName_edit", propertyName:"dName"}//配属段名称 -
				],
				editable:true,
				allowBlank: false,
				listeners : {
					"beforequery" : function(){
						//选择车号前先选车型
						var trainTypeShortName =  VTrainAccessAccount.editForm.getForm().findField("trainTypeShortName").getValue();
						if(Ext.isEmpty(trainTypeShortName)){
							MyExt.Msg.alert("请先选择车型！");
							return false;
						}
					}
				}
		
    		},{
    			id: "dName_edit", fieldLabel: "配属段", name: "dName", maxLength:50, disabled: true
    		},{
    			fieldLabel: "到达车次", name: "arriveOrder", maxLength:20
    		},{
    			fieldLabel: "计划出段时间", name: "planOutTime", xtype: 'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}, initNow: false
    		},{
    			fieldLabel: "机车别名", name: "trainAliasName", disabled: true
    			
			/** 以下字段为隐藏字段 */
    		},{ 
    			xtype: 'hidden', name: 'idx', fieldLabel: 'idx主键'
    		},{ 
    			xtype: 'hidden', id: 'did_edit', name: 'dID', fieldLabel: '配属段id'
			},{
    			xtype: 'hidden', id:'trainTypeIDX_Id', name:'trainTypeIDX', fieldLabel: '车型主键' 		
    		}]
		}]
		
	});
	/** **************** 定义全局变量结束 **************** */ 
	
	/** **************** 定义全局函数开始 **************** */ 
	// 显示机车出入段信息编辑窗口
	VTrainAccessAccount.showEditWin = function(idx) {
		if (!VTrainAccessAccount.win) VTrainAccessAccount.win = new Ext.Window({
			layout: 'fit',
			title: '编辑',
			height: 230, width: 580,
			closeAction: 'hide',
			items: [VTrainAccessAccount.editForm],
			buttonAlign: 'center',
			buttons: [{
				text: '保存', iconCls: 'saveIcon', handler: function() {
				    VTrainAccessAccount.loadMask.show();
					var form = VTrainAccessAccount.editForm.getForm(); 
				    if (!form.isValid()) return;
				    form.findField("trainNo").enable();
					form.findField("trainTypeShortName").enable();
//					form.findField("toGo").enable();
					form.findField("dName").enable();
					// 获取数据
				    var data = form.getValues();	
				    form.findField("trainNo").disable();
					form.findField("trainTypeShortName").disable();
//					form.findField("toGo").disable();
					form.findField("dName").disable();
				    if (Ext.isEmpty(data.trainNo) && !Ext.isEmpty(Ext.get("trainNo_comb_Id").dom.value)) {
						data.trainNo = Ext.get("trainNo_comb_Id").dom.value;
					}
				    var cfg = {
				        scope: this, url: ctx + '/trainAccessAccount!updateIn.action', jsonData: data,
				        success: function(response, options){
				            VTrainAccessAccount.loadMask.hide();
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null) {
				            	// 隐藏编辑窗口
				            	VTrainAccessAccount.win.hide();
				            	// 查询查询统计信息
				            	VTrainAccessAccount.showStatisticsInfoFn();
							    alertSuccess();
				            } else {
		    					alertFail(result.errMsg);
				            }
				        }
				    };
				    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));    
				}
			}, {
				text: '取消', iconCls: 'closeIcon', handler: function() {
					this.findParentByType('window').hide();
				}
			}],
			
			listeners: {
				// 显示编辑窗口时，隐藏可能已经打开的查询窗口
				show: function() {
					if (VTrainAccessAccount.searchWin) VTrainAccessAccount.searchWin.hide();
				}
			}
		});
		// 赋值
		VTrainAccessAccount.win.find('name', 'idx')[0].setValue(idx);
		VTrainAccessAccount.loadMask.show();
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), {
			url: ctx + '/trainAccessAccount!getModelByIdx.action',
			scope: VTrainAccessAccount.editForm,
			params: {
				id: idx
			},
			success: function(response, options){
				VTrainAccessAccount.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        if (Ext.isEmpty(result.errMsg)) {       //操作成功    
		        	var entity = result.entity
		        	if (Ext.isEmpty(entity)) {
		        		MyExt.Msg.alert('未能查询到该机车的入段信息，请刷新后重试！');
		        		return;
		        	}
		        	this.getForm().reset();
		        	this.getForm().setValues(entity);
					// 显示窗口
					VTrainAccessAccount.win.show();
					
		        	// 设置车型
					if (Ext.isEmpty(entity.trainTypeIDX) || Ext.isEmpty(entity.trainTypeShortName)) {
		        		this.find('name', 'trainTypeIDX')[0].setValue("");
						this.find('hiddenName', 'trainTypeShortName')[0].enable()
		        		this.find('hiddenName', 'trainTypeShortName')[0].clearValue();
					} else {
			        	this.find('name', 'trainTypeIDX')[0].setValue(entity.trainTypeIDX);
						this.find('hiddenName', 'trainTypeShortName')[0].disable()
			        	this.find('hiddenName', 'trainTypeShortName')[0].setDisplayValue(entity.trainTypeShortName, entity.trainTypeShortName);
					}
					// 设置车号
					if (Ext.isEmpty(entity.trainNo)) {
						this.find('hiddenName', 'trainNo')[0].enable()
		        		this.find('hiddenName', 'trainNo')[0].clearValue();
					} else {
						this.find('hiddenName', 'trainNo')[0].disable()
			        	this.find('hiddenName', 'trainNo')[0].setDisplayValue(entity.trainNo, entity.trainNo);
					}
					// 设置入段时间
					if (Ext.isEmpty(entity.inTime)) {
						this.find('name', 'inTime')[0].setValue('');
					} else {
						this.find('name', 'inTime')[0].setValue(new Date(entity.inTime));
					}
					// 设置计划出段时间
					if (Ext.isEmpty(entity.planOutTime)) {
						this.find('name', 'planOutTime')[0].setValue('');
					} else {
						this.find('name', 'planOutTime')[0].setValue(new Date(entity.planOutTime));
					}
		        	// 设置入段去向
					if (Ext.isEmpty(entity.toGo)) {
						this.find('hiddenName', 'toGo')[0].clearValue();
					} else {
						this.find('hiddenName', 'toGo')[0].setDisplayValue(entity.toGo, entity.toGoName);
					}
		        } else {                           		 //操作失败
		            alertFail(result.errMsg);
		        }
		    }
		}));
		
	}
	
	// 动态加载入段机车分类信息展示面板数据的函数处理
	VTrainAccessAccount.showStatisticsInfoFn = function(trainNo) {
		VTrainAccessAccount.loadMask.show();
		Ext.Ajax.request({
			params: {trainNo: trainNo},
			url: ctx + '/trainAccessAccount!statistics.action',
			//请求成功后的回调函数
		    success: function(response, options){
				VTrainAccessAccount.loadMask.hide();
		        var result = Ext.util.JSON.decode(response.responseText);
		        
		        if (result.errMsg == null) {       //操作成功     
	        		var html = [];
		        	for(var prop in result) {
		        		html.push('<div class="infoPanel" style="box-shadow:2px 2px 2px ' + prop.split(',')[1] + ';border-color:'+ prop.split(',')[1] +';">');
		        		html.push('<div class="infoPanel_title " ' + 'style="background-color:'+ prop.split(',')[1] +';"' +'>');
		        		html.push('<span class="status">');
	        			html.push(prop.split(',')[0]);							// 状态名称
		        		html.push('</span>');
		        		html.push('<span class="number">');
		        		html.push(result[prop].length);				// 机车统计数量
		        		html.push('</span>');
		        		html.push('</div>');
			        	for (var i = 0; i < result[prop].length; i++) {
			        		html.push('<div class="train_pic_bg infoPanel_items">');
			        		var displayInfo = result[prop][i];
			        		// 机车出入段台账(TWT_TRAIN_ACCESS_ACCOUNT)idx主键
			        		var idx = displayInfo.substring(0, displayInfo.indexOf(','));
			        		// 车型车号
			        		var content = displayInfo.substring(displayInfo.indexOf(',') + 1);
			        		html.push("<a href='#' onclick='VTrainAccessAccount.showEditWin(\""+ idx + "\")'>" + content + "</a>");				
			        		html.push('</div>');
			        	}
			        	html.push('</div>');
		        	}
		        	if (0 >= html.length) {
		        		Ext.getCmp('infoRegion').collapse();
		        	} else {
		        		Ext.getCmp('infoRegion').expand();
			        	Ext.getDom('infoPanel').innerHTML = html.join('');
		        	}
		        	Ext.getDom('infoPanel').innerHTML = html.join('');
		        } else {                           //操作失败
		            alertFail(result.errMsg);
		        }
		    },
		    //请求失败后的回调函数
		    failure: function(response, options){
				VTrainAccessAccount.loadMask.hide();
		        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
		    }
		});
	};
	/** **************** 定义全局函数结束 **************** */ 
	
	/** **************** 定义信息面板开始 **************** */ 
	VTrainAccessAccount.panel = new Ext.Panel({
		region: 'south', height: 165, border: false, 
		id:'infoRegion',
		split: true,
		iconCls: 'pageIcon',
		autoScroll : true,
		title: '<span style="font-weight:normal;">在修机车分类信息</span>',
		
		items: [{
			id: 'infoPanel',
			listeners: {
				render: function() {
					// 显示入段机车分类信息
					VTrainAccessAccount.showStatisticsInfoFn();
				}
			}
		}],
		tools:[{
			id:'search',		
		    qtip: '查询在修机车分类信息',
		    handler: function() {
		    	if (!VTrainAccessAccount.searchWin) {
		    		VTrainAccessAccount.searchWin = new Ext.Window({
		    			title: '查询',
		    			closeAction: 'hide',
		    			height: 120, width: 300, padding: 10,
		    			layout: 'form',
		    			items: [{
		    				xtype: 'textfield', width: 140, fieldLabel: '机车型号',
		    				emptyText: '输入车型车号检索',
		    				enableKeyEvents: true,
		    				listeners: {
		    					keyup: function(self, e) {
		    						var trainNo = self.getValue();
		    						if (Ext.isEmpty(trainNo)) {
		    							return;
		    						}
									// 如果敲下Enter（回车键），则触发添加按钮的函数处理
									if (e.getKey() == e.ENTER){
		    							VTrainAccessAccount.showStatisticsInfoFn(trainNo);
									}
					    		}
		    				}
		    			}],
		    			buttonAlign: 'center',
		    			buttons: [{
		    				text: '查询', iconCls: 'searchIcon', handler: function() {
		    					var win = this.findParentByType('window');
		    					var trainNo = win.items.items[0].getValue();
		    					VTrainAccessAccount.showStatisticsInfoFn(trainNo);
		    				}
		    			}, {
		    				text: '重置', iconCls: 'resetIcon', handler: function() {
		    					var win = this.findParentByType('window');
		    					var trainNo = win.items.items[0].reset();
		    					VTrainAccessAccount.showStatisticsInfoFn();
		    				}
		    			}, {
		    				text: '关闭', iconCls: 'closeIcon', handler: function() {
		    					this.findParentByType('window').hide();
		    				}
		    			}]
		    		});
		    	}
		    	VTrainAccessAccount.searchWin.show();
		    }
		}, {
		    id:'refresh',
		    qtip: '刷新在修机车分类信息',
		    handler: function(event, toolEl, panel){
				// 显示入段机车分类信息
				VTrainAccessAccount.showStatisticsInfoFn();
		    }
		}]
	})
	/** **************** 定义信息面板结束 **************** */ 
	
});