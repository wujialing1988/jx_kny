/**
 * 任务单派工 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('PartsRdpDispatchered');                       //定义命名空间
	
	/** ************** 定义全局变量开始 ************** */
	PartsRdpDispatchered.fieldWidth = 150;
	PartsRdpDispatchered.labelWidth = 60;
	PartsRdpDispatchered.rdpId = "";//任务单id
	/** ************** 定义全局变量结束 ************** */

	/** ************** 定义任务单基本信息表单开始 ************** */
	PartsRdpDispatchered.baseForm = new Ext.form.FormPanel({
		labelWidth: PartsRdpDispatchered.labelWidth,
		style: "padding:10px",		
		defaults: {
			layout: "column", 
			defaults: { 
				columnWidth:.33, layout:'form', 
				defaults: {
					xtype: 'textfield',
					readOnly: true,
					style: 'background: none; border:none;'
				}
			}
		},
	    items: [{
			items: [{
				items:[
					{fieldLabel:'配件编号',name:"partsNo"},
					{fieldLabel:'扩展编号',name:"extendNo"},
					{fieldLabel:'下车修程',name:"unloadRepairClass"}
		        ]
			},{
				items:[
					{fieldLabel:'配件名称',name:"partsName"},
					{fieldLabel:'下车车型',name:"unloadTrainType"},
					{fieldLabel:'下车修次',name:"unloadRepairTime"}
				]
			},{
				items:[
					{fieldLabel:'规格型号',name:"specificationModel"},
					{fieldLabel:'下车车号',name:"unloadTrainNo"},
					{fieldLabel:'检修班组',name:"repairOrgName"}
			     ]
	        }]
	    },{
			labelWidth: PartsRdpDispatchered.labelWidth  + 40,
	        items: [{
			     columnWidth:.5,
			     items:[{
			     	fieldLabel:'计划开始时间',name:"planStartTime"
			     }]
			}, {
				columnWidth:.5,
				items:[{
					fieldLabel:'计划结束时间',name:"planEndTime"
				}]
			}]
	    }]
	});
	/** ************** 定义任务单基本信息表单开始 ************** */
		
	//施修人员列表
	PartsRdpDispatchered.workerGrid = new Ext.yunda.Grid({
	    loadURL: ctx + '/partsRdpWorker!pageList.action',                 //装载列表数据的请求URL
	    saveURL: ctx + '/partsRdpWorker!saveOrUpdate.action',             //保存数据的请求URL
	    deleteURL: ctx + '/partsRdpWorker!logicDelete.action',            //删除数据的请求URL
	    storeAutoLoad: false,
	    tbar: ['delete'],
		fields: [{
			header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
		},{
			header:'作业主键', dataIndex:'rdpIDX',hidden:true, editor:{  maxLength:50 }
		},{
			header:'作业人员', dataIndex:'workEmpID', editor:{ xtype:'numberfield', maxLength:10 }
		},{
			header:'作业人员名称', dataIndex:'workEmpName', editor:{  maxLength:25 }
		}],
		afterDeleteFn: function(){ 
			PartsRdpDispatcher.grid.store.reload();
		}
	});
	//store载入前查询
	PartsRdpDispatchered.workerGrid.store.on("beforeload", function(){
		var sp = {};
		sp.rdpIDX = PartsRdpDispatchered.rdpId;
	    this.baseParams.entityJson = Ext.util.JSON.encode(sp);
	});
	    
	/** ************** 定义组织机构树开始 ************** */
	PartsRdpDispatchered.orgTree =  new Ext.tree.TreePanel({
		tbar : new Ext.Toolbar(),
		plugins : ['multifilter'],
		loader : new Ext.tree.TreeLoader({
			dataUrl : ctx + "/organization!tree.action"
		}),
		root: new Ext.tree.TreeLoader({
			text : teamOrgName,
			disabled : false,
			id : 'ROOT_0',
			nodetype : 'org',
			leaf : false,
			iconCls : 'chart_organisationIcon'
		}),
		rootVisible : true,
    	autoScroll : true,
    	animate : false,
    	useArrows : false,
    	border : false,
    	collapsed : false,
//    	enableDrag:true,
	    listeners: {
    		render : function() {
    			PartsRdpDispatchered.orgTree.root.reload();
			    PartsRdpDispatchered.orgTree.getRootNode().expand();
    		},
	        dblclick: function(node) {
	        	if (node.attributes.nodetype == 'emp'){
//	        		var record = new Ext.data.Record();
//	        		record.set("empid",node.attributes.empid);
//	        		record.set("empcode",node.attributes.empcode);
//	        		record.set("empname",node.attributes.empname);
//	        		PartsRdpDispatchered.workerGrid.store.insert(0, record); 
//					PartsRdpDispatchered.workerGrid.getView().refresh(); 
	        		var data = {} ;
	        		data.workEmpID = node.attributes.empid;
	        		data.workEmpName = node.attributes.empname;
	        		data.rdpIDX = PartsRdpDispatchered.rdpId;
	        		var cfg = {
				        scope: PartsRdpDispatchered.workerGrid, 
				        url: PartsRdpDispatchered.workerGrid.saveURL, 
				        jsonData: data, 
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            if (result.errMsg == null) {
				            	PartsRdpDispatcher.grid.store.reload();
						    	PartsRdpDispatchered.workerGrid.afterSaveSuccessFn(result, response, options)
				            } else {
				                PartsRdpDispatchered.workerGrid.afterSaveFailFn(result, response, options);
				            }
				        }
				    };
				    Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    			}
	        },
	        beforeload: function(node){
		        var tempid;
				if(node.id=='ROOT_0') tempid = teamOrgId;
				else tempid = node.id.substring(2,node.id.length);
		    	this.loader.dataUrl = ctx + '/organization!tree.action?nodeid='+tempid+'&nodetype='+node.attributes.nodetype;}
		    }    
	});
	/** ************** 定义组织机构树结束 ************** */

	/** ************** 定义单条数据派工窗口开始 ************** */
	PartsRdpDispatchered.dispatchWin = new Ext.Window({
		title: "作业人员",
		width: 800, height: 560,
		maximizable:false, modal: true, maximized: false,
		layout: "border", 
		closeAction: "hide",
		defaults: {border: false},
		items:[{
			height: 130, layout: "fit",
			region: 'north', frame: true,
	        items: [PartsRdpDispatchered.baseForm]
		},{
			region:"center", layout: "border",
			items:[{
				collapsible: true,
				title : '<span style="font-weight:normal">选择施修人员</span>',
	        	iconCls : 'icon-expand-all',
	        	tools : [ {
		            id : 'refresh',
		            handler: function() {
		            	PartsRdpDispatchered.orgTree.getRootNode().reload();
		            }
		        }],
				width: 200,layout: "fit",
				region: 'west',
		        items: [PartsRdpDispatchered.orgTree]
			},{
				title:'<span style="font-weight:normal">已选择施修人员列表</span>',
				region: 'center',
		        xtype: "panel", layout: "fit",
		        items:[PartsRdpDispatchered.workerGrid]
			}]
		}],
		buttonAlign:"center",
		buttons: [{
	        text: "关闭", iconCls: "closeIcon", handler: function(){ 
	        	this.findParentByType('window').hide();
	        	PartsRdpDispatcher.grid.store.reload();
	    	}
	    }]
	});
	/** ************** 定义单条数据派工窗口结束 ************** */
	
});