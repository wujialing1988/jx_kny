/**
 * 扣车管理
 */
Ext.onReady(function(){
	
Ext.namespace('DetainTrainCompleted');                       //定义命名空间

//定义全局变量保存查询条件
DetainTrainCompleted.searchParam = {} ;
DetainTrainCompleted.labelWidth = 40;                        //表单中的标签名称宽度
DetainTrainCompleted.fieldWidth = 130;                       //表单中的标签宽度

// 新增窗口
DetainTrainCompleted.saveForm = new Ext.form.FormPanel({
	border: false,
	labelAlign:"left", 
	layout:"form",
	bodyStyle:"padding:10px;",
	defaults: {
		xtype:"container", autoEl:"div", layout:"form"
	},
	items:[
	{
        xtype:'fieldset',
        title: i18n.TruckDetainRegSumm.detainInformation,
        autoHeight:true,
        layout: 'column',
        defaults: {
        	columnWidth:.5,
			layout: 'form',
			border: false,
			defaults: {
        		xtype:"displayfield", 
        		anchor:"98%",
    		    labelWidth:DetainTrainCompleted.labelWidth, 
				fieldWidth:DetainTrainCompleted.fieldWidth
        	}
        },
        items :[
        	{xtype: "hidden", name: "idx"},
        	{xtype: "hidden", name: "detainStatus"},
        	{
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.trainType,
                        name: "trainTypeCode"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.trainNum,
                        name: "trainNo"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.registerName,
                        name: "proposerName"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.registerDate,
                        name: "proposerDate"
                    }]
            },
        	{
        		columnWidth:1,
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.detainType,
                        name: "detainTypeName"
                    }]
            },
        	{
        		columnWidth:1,
            	items:[{
                		fieldLabel: i18n.TruckDetainRegSumm.detainReason,
                        name: "detainReason"
                    }]
            },{
            	items:[{
            		fieldLabel: i18n.TruckDetainRegSumm.repairClassName,
                    name: "repairClassName"
                }]
            },{
            	items:[{
            		fieldLabel: i18n.TruckDetainRegSumm.overhualDate,
                    name: "jxTime"
                }]
            }            
        ]
    }
	]
});

DetainTrainCompleted.queryTimeout;	// 间隔查询

DetainTrainCompleted.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/detainTrain!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/detainTrain!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/detainTrain!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, 
    saveFormColNum:1,
    saveForm:DetainTrainCompleted.saveForm,
    tbar : ['refresh','&nbsp;&nbsp;',
    {
    	xtype:'textfield', id:'query_input_completed', enableKeyEvents:true, emptyText:i18n.TruckDetainRegSumm.search, width:200,
    	listeners: {
    		keyup: function(filed, e) {
    			if (DetainTrainCompleted.queryTimeout) {
    				clearTimeout(DetainTrainCompleted.queryTimeout);
    			}
    			
    			DetainTrainCompleted.queryTimeout = setTimeout(function(){
					DetainTrainCompleted.grid.store.load();
    			}, 1000);
    		}
		}
    },'->','<span style="color:grey;">' + i18n.TruckDetainRegSumm.doubleRem + '&nbsp;&nbsp;</span>'],    
	fields: [
     	{
		header:i18n.TruckDetainRegSumm.trainTypeID, dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.trainType, dataIndex:'trainTypeCode',width: 120,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.trainTypeName, dataIndex:'trainTypeName',hidden:true,width: 120,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.trainNum, dataIndex:'trainNo',width: 120,editor: {}
	}, {
		header:i18n.TruckDetainRegSumm.detainTypeCode, dataIndex:'detainTypeCode',width: 120,hidden:true,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.detainType, dataIndex:'detainTypeName',width: 120,editor: {}
	},{
		header:i18n.TruckDetainRegSumm.registerID, dataIndex:'proposerIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.registerName, dataIndex:'proposerName',width: 120,editor: {}
	},
       {
		header:i18n.TruckDetainRegSumm.registerDate, dataIndex:'proposerDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},
     	{
		header:i18n.TruckDetainRegSumm.detainReason, dataIndex:'detainReason',width: 120,editor: {}
	},
	{
		header:i18n.TruckDetainRegSumm.repairClassName, dataIndex:'repairClassName',width: 120,editor: {}
	},
	{
		header:i18n.TruckDetainRegSumm.overhualDate, dataIndex:'jxTime', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},
     	{
		header:i18n.TruckDetainRegSumm.status, dataIndex:'detainStatus',width: 120,renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if(value == "10"){
						return  '<div style="background:#d2d6de;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + i18n.TruckDetainRegSumm.unrepaired + '</div>';
					}else if(value == "20"){
						return  '<div style="background:#f39c12;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + i18n.TruckDetainRegSumm.repairing + '</div>';
					}else if(value == "30"){
						return  '<div style="background:#00a65a;color:white;width:48px;height:18px;line-height:18px;text-align:center;border-radius:8px;margin-left:10px;">' + i18n.TruckDetainRegSumm.repaired + '</div>';
					}
				},editor: {}
	},{
		header:i18n.TruckDetainRegSumm.stationID, dataIndex:'siteID',width: 120,hidden:true ,editor: {}
	},
     	{
		header:i18n.TruckDetainRegSumm.stationName, dataIndex:'siteName',width: 120,hidden:true , editor: {}
	},
		{
		header:i18n.TruckDetainRegSumm.idx, dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		DetainTrainCompleted.searchParam = searchParam ;
        DetainTrainCompleted.grid.store.load();
	},
	afterShowEditWin: function(record, rowIndex){
		this.saveWin.setTitle(i18n.TruckDetainRegSumm.readDetainInfo);
		var proposerDate = Ext.isEmpty(record.data.proposerDate) ? "" :new Date(record.data.proposerDate).format('Y-m-d H:i');
		var approveDate = Ext.isEmpty(record.data.approveDate) ? "" :new Date(record.data.approveDate).format('Y-m-d H:i');
		var jxTime = Ext.isEmpty(record.data.jxTime) ? "" :new Date(record.data.jxTime).format('Y-m-d H:i');
		DetainTrainCompleted.saveForm.getForm().findField("proposerDate").setValue(proposerDate);
		DetainTrainCompleted.saveForm.getForm().findField("approveDate").setValue(approveDate);
		DetainTrainCompleted.saveForm.getForm().findField("jxTime").setValue(jxTime);
	},	
	createSaveWin: function(){
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	            title:i18n.TruckDetainRegSumm.readDetainInfo, width:800, height: 280, closeAction:"hide",
	            layout: 'fit',
	            iconCls: 'searchIcon',
	            defaults: {layout: 'fit', border: false},
	            items:DetainTrainCompleted.saveForm , 
	            modal:true,
	            buttonAlign:'center', 
	            buttons: [{
	                text: i18n.TruckDetainRegSumm.close, iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
	            }]
	        });
	}    
});
	
//查询前添加过滤条件
DetainTrainCompleted.grid.store.on('beforeload' , function(){
		var whereList = [];
		if(!Ext.isEmpty(Ext.getCmp('query_input_completed').getValue())){
			whereList.push({ propName: 'trainNo', propValue: Ext.getCmp('query_input_completed').getValue(), compare: Condition.EQ, stringLike: true });
		}
		whereList.push({ propName: 'vehicleType', propValue:vehicleType, compare: Condition.EQ, stringLike: false});			
		whereList.push({ propName: 'detainStatus', propValue:"10", compare: Condition.NE});
		this.baseParams.whereListJson = Ext.util.JSON.encode(whereList);
});

});