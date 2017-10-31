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
        title: '扣车信息',
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
                		fieldLabel: '车型',
                        name: "trainTypeCode"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: '车号',
                        name: "trainNo"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: '申请人',
                        name: "proposerName"
                    }]
            },
        	{
            	items:[{
                		fieldLabel: '申请时间',
                        name: "proposerDate"
                    }]
            },
        	{
        		columnWidth:1,
            	items:[{
                		fieldLabel: '扣车类型',
                        name: "detainTypeName"
                    }]
            },
        	{
        		columnWidth:1,
            	items:[{
                		fieldLabel: '扣车原因',
                        name: "detainReason"
                    }]
            }            
        ]
    },
		// 交接项
		{
        xtype:'fieldset',
        title: '审批意见',
        autoHeight:true,
        layout: 'column',
        defaults: {
        	columnWidth:0.9,
			layout: 'form',
			border: false,
			defaults: {
        		xtype:"displayfield", 
        		anchor:"98%",
    		    labelWidth: DetainTrainCompleted.labelWidth, 
				fieldWidth: DetainTrainCompleted.fieldWidth
        	}
        },
        items :[
        	{
            	items:[{
                		fieldLabel: '命令号',
                        name: "orderNo",
                        maxLength:50
                    }]
            },  
        	{
            	items:[{
                		fieldLabel: '发布人',
                        name: "orderUser",
                        maxLength:50
                    }]
            },  
        	{
            	items:[{
                		fieldLabel: '发布时间',
	                    name: "orderDate"                       
                    }]
            },  
        	{
            	items:[{
                		fieldLabel: '审批人',
	                    name: "approveName"                       
                    }]
            },  
        	{
            	items:[{
                		fieldLabel: '审批时间',
	                    name: "approveDate"                       
                    }]
            },              
        	{
            	items:[{
                		fieldLabel: '审批意见',
                        name: "approveOpinion",
	 					maxLength:100,
	 					height: 55                        
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
    	xtype:'textfield', id:'query_input_completed', enableKeyEvents:true, emptyText:'输入车号快速检索...', width:200,
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
    },'->','<span style="color:grey;">双击数据查看详情!&nbsp;&nbsp;</span>'],    
	fields: [
     	{
		header:'车辆车型ID', dataIndex:'trainTypeIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车型', dataIndex:'trainTypeCode',width: 120,editor: {}
	},
     	{
		header:'车辆车型名称', dataIndex:'trainTypeName',hidden:true,width: 120,editor: {}
	},
     	{
		header:'车号', dataIndex:'trainNo',width: 120,editor: {}
	}, {
		header:'扣车类型编码', dataIndex:'detainTypeCode',width: 120,hidden:true,editor: {}
	},
     	{
		header:'扣车类型', dataIndex:'detainTypeName',width: 120,editor: {}
	},{
		header:'申请人ID', dataIndex:'proposerIdx',hidden:true,width: 120,editor: {}
	},
     	{
		header:'申请人', dataIndex:'proposerName',width: 120,editor: {}
	},
       {
		header:'申请时间', dataIndex:'proposerDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},
     	{
		header:'扣车原因', dataIndex:'detainReason',width: 120,editor: {}
	},
     	{
		header:'审批人ID', dataIndex:'approveIdx',width: 120,hidden:true,editor: {}
	},
     	{
		header:'审批人', dataIndex:'approveName',width: 120,editor: {}
	},{
		header:'审批时间', dataIndex:'approveDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},{
		header:'审批意见', dataIndex:'approveOpinion',width: 120,editor: {}
	},
     	{
		header:'命令发布者', dataIndex:'orderUser',width: 120,editor: {}
	}, {
		header:'命令发布时间', dataIndex:'orderDate', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},
     	{
		header:'命令号', dataIndex:'orderNo',width: 120,editor: {}
	},{
		header:'状态', dataIndex:'detainStatus',width: 120,renderer:function(value, metaData, record, rowIndex, colIndex, store){
					if(value == "10"){
						return '<span style="color:blue;">申请中</span>';
					}else if(value == "20"){
						return '<span style="color:red;">拒绝</span>';
					}else if(value == "30"){
						return '<span style="color:green;">审批完成</span>';
					}
				},editor: {}
	},{
		header:'站点ID', dataIndex:'siteID',width: 120,hidden:true ,editor: {}
	},
     	{
		header:'站点名称', dataIndex:'siteName',width: 120,hidden:true , editor: {}
	},
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		DetainTrainCompleted.searchParam = searchParam ;
        DetainTrainCompleted.grid.store.load();
	},
	afterShowEditWin: function(record, rowIndex){
		this.saveWin.setTitle("扣车信息查看");
		var proposerDate = Ext.isEmpty(record.data.proposerDate) ? "" :new Date(record.data.proposerDate).format('Y-m-d H:i');
		var orderDate = Ext.isEmpty(record.data.orderDate) ? "" :new Date(record.data.orderDate).format('Y-m-d H:i');
		var approveDate = Ext.isEmpty(record.data.approveDate) ? "" :new Date(record.data.approveDate).format('Y-m-d H:i');
		DetainTrainCompleted.saveForm.getForm().findField("proposerDate").setValue(proposerDate);
		DetainTrainCompleted.saveForm.getForm().findField("orderDate").setValue(orderDate);
		DetainTrainCompleted.saveForm.getForm().findField("approveDate").setValue(approveDate);
	},	
	createSaveWin: function(){
	        //计算查询窗体宽度
	        this.saveWin = new Ext.Window({
	            title:"扣车信息查看", width:800, height: 500, closeAction:"hide",
	            layout: 'fit',
	            iconCls: 'searchIcon',
	            defaults: {layout: 'fit', border: false},
	            items:DetainTrainCompleted.saveForm , 
	            modal:true,
	            buttonAlign:'center', 
	            buttons: [{
	                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
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