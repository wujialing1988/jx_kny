/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainEnforcePlanRdp');  //定义命名空间
	TrainEnforcePlanRdp.rdpId;
	//在修机车树
	TrainEnforcePlanRdp.enforcePlanRdpTree = new Ext.tree.TreePanel( {
	    loader : new Ext.tree.TreeLoader( {
	    	//TODO V3.2.1代码重构
	        dataUrl : ctx + "/trainEnforcePlanRdp!findEnforcePlanRdpTree.action"
	    }),
	    root: new Ext.tree.AsyncTreeNode({
	        text: '在修机车',
	        id: 'ROOT_0',
	        leaf: false,
		    parentIDX : ""
	    }),
	    rootVisible: true,
	    autoScroll : false,
	    animate : false,
	    useArrows : false,
	    border : false,
	    collapsed:false,
	    listeners: {
	        beforeload: function(node,e) {
	        	//TODO V3.2.1代码重构
	        	TrainEnforcePlanRdp.enforcePlanRdpTree.loader.dataUrl = ctx + '/trainEnforcePlanRdp!findEnforcePlanRdpTree.action?parentIDX='+node.id ;
	        },
	        beforechildrenrendered:function(n){
	        	if(n.leaf){
	        		if(!TrainEnforcePlanRdp.nodes){
	        			TrainEnforcePlanRdp.nodes = [];
	        		}
	        		TrainEnforcePlanRdp.nodes.push(n.id);
	        	}
	        },
	        click: function(node,e){
	        	if(node.leaf == true){
	        	   TrainEnforcePlanRdp.rdpId = node.id; //赋值全局变量
	        	   var rdpId = node.id;
	        	   TrainEnforcePlanRdp.getFlowAndGannt(rdpId);
	        	}
	        }
	    }
	});
	
	//设置工作流程信息form
	TrainEnforcePlanRdp.flowbaseForm = new Ext.form.FormPanel({
		align: 'center',
		layout : 'form',
		baseCls: 'x-plain',
		style : 'padding :5px',
		defaults : { anchor : '98%'},
        labelAlign : 'right',
		labelWidth:100,
		buttonAlign : 'center',
		html: "<label><div id='flowBaseInfoForm'></div></label>"
	});
		
	TrainEnforcePlanRdp.getFlowAndGannt = function(rdpId){
		//获取作业基本信息
    	jQuery.ajax({
			url: ctx + "/trainEnforcePlanRdp!getBeseInfo.action",
		    data:{rdpId: rdpId},
			dataType:"json",
			type:"post",
			success:function(data){
				var record=new Ext.data.Record();
				for(var i in data){
				record.set(i,data[i]);
					}
				var instId = record.get("processInstID");
				TrainEnforcePlanRdp.instId = record.get("processInstID");
				
				TrainEnforcePlanRdp.LoadInfo(record); 
	
			    if(instId == null || instId=='undefined'){
			    	MyExt.Msg.alert("无对应的BPS流程实例！");
			    }else{
			    	jQuery.ajax({
						url: ctx + "/wfprocessinst!findProcessInstByID.action",
					    data:{processinstId: instId},
						dataType:"json",
						type:"post",
						success:function(data){
							if(data.entity == null){
								MyExt.Msg.alert("无对应的BPS流程实例！");
							}else{
								//加载流程图
						    	var flowsrc = ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID="+instId+"&rdpId="+rdpId;	                       
						    	document.getElementById("flow").innerHTML = "<iframe id='flowFrame' style='width:100%;' frameborder='0' src='" + flowsrc + "'></iframe>";     //正常切换tab			    	
						        //设置甘特图
							    var ganttsrc = ctx + "/jsp/jx/jxgc/producttaskmanage/LED_Gantt.jsp?rdpIDX="+rdpId;
							    //var ht = jQuery("#gantt").height();
							    Ext.getCmp("gantt").html = "<iframe id='ganttFrame' style='width:100%;height:100%;overflow:auto;' frameborder='0' src='" + ganttsrc + "'></iframe>";
								if(typeof document.getElementById("ganttFrame") != "undefined" && document.getElementById("ganttFrame") != null) {
									document.getElementById("ganttFrame").src = ganttsrc;
								}
								setFlowHeight();//移到这里，否则会影响甘特图显示-程锐
							}
						}
					});					
	        	}
	    	}
		});
	};
	
	//定义流程和甘洛图tabs
	TrainEnforcePlanRdp.tabs = new Ext.TabPanel({
	       activeTab: 0,
		   frame:true,
		   items:[{
			   title: "流程图", layout: "border", border: false,
			   items:[new Ext.Panel({
				   	region:"north",
					layout:"fit",
					width : "100%",
					height:75,
					frame: true,
					items:[TrainEnforcePlanRdp.flowbaseForm]
			   	}),
			   	new Ext.Panel({
					id:"flow",
					region:"center",
					layout:"fit",
					width : "100%",
					height: "100%"
				})],
				listeners:{
		   			"activate": function(){					   
			   			setFlowHeight();
		   			},
		   			"resize": function(){
		   				setFlowHeight();
		   			}
	   			}
		     },{
	        	 title: "甘特图", layout: "border", border: false,
	        	 listeners:{
	        	 	"activate":function(){
	        	 	   TrainEnforcePlanRdp.getFlowAndGannt(TrainEnforcePlanRdp.rdpId);  //加载作业基本信息   
	        	 	}
	        	 },
	        	 items:[
	        	        new Ext.Panel({
				        	region:"north",
				        	layout:"fit",
				        	width : "100%",
				        	split:true,
				        	height:80,
				        	maxSize:70,
				        	minSize:70,
				        	frame: true,
				        	items:[TrainEnforcePlanRdp.baseForm]
				        }),
				        new Ext.Panel({
							id:"gantt",
						    region : "center",
							layout:"fit",
							width : "100%",
							height: "100%",
							split : true,
							frame: true
						})]
	        }]
	 });
	 
	 TrainEnforcePlanRdp.enforcePlanRdpTree.expandAll();
	 
	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout:'border', 
		items:[{
			        title : '<span style="font-weight:normal">在修机车</span>',
			        iconCls : 'chart_organisationIcon',
			        tools : [ {
			            id : 'refresh',
			            handler : function() {
			                TrainEnforcePlanRdp.enforcePlanRdpTree.root.reload();
		            		TrainEnforcePlanRdp.enforcePlanRdpTree.getRootNode().expand();
			            }
			        } ],
			        collapsible : true,
			        width : 250,
			        minSize : 160,
			        maxSize : 280,
			        split : true,
			        region : 'west',
			        bodyBorder: false,
			        autoScroll : true,
			        items : [ TrainEnforcePlanRdp.enforcePlanRdpTree ]
				},{
				   region : "center",
				   layout : "fit",
				   bodyBorder: false, 
				   items: [ TrainEnforcePlanRdp.tabs ]
				}
		]
	 });
	var interval = setInterval(function(){
		if(TrainEnforcePlanRdp.nodes.length > 0){
			TrainEnforcePlanRdp.enforcePlanRdpTree.getNodeById(TrainEnforcePlanRdp.nodes[0]).ui.textNode.click();			
			clearInterval(interval);
		}
	}, 1000);
	
	/*
	 * 设置流程图显示高度 
	 */
	function setFlowHeight(){
		var frame = document.getElementById("flowFrame");		
		if(frame){
			var parent = jQuery("#flow").parent();
			var i = (parent.height() - 80);	//80是form panel的高度
			jQuery(frame).height(i);
		}
	}
});