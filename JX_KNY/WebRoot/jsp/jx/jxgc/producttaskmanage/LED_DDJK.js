/**
 * 机车施修任务兑现单 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('TrainEnforcePlanRdp');                       //定义命名空间
	
	TrainEnforcePlanRdp.rdpIdx  = "";
	TrainEnforcePlanRdp.selected = false;
	TrainEnforcePlanRdp.clicked = false;
	TrainEnforcePlanRdp.clickRdpIdx  = "";
	TrainEnforcePlanRdp.nodes = [];
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
	    autoScroll : true,
	    animate : false,
	    useArrows : false,
	    border : false,
	    style:"background-color:red;",
	    cls:".span",
	    collapsed:false,
	    listeners: {
	        beforeload: function(node,e) {
	        	TrainEnforcePlanRdp.enforcePlanRdpTree.loader.dataUrl = ctx + '/trainEnforcePlanRdp!findEnforcePlanRdpTree.action?parentIDX='+node.id ;
	        	TrainEnforcePlanRdp.nodes = [];
	        },
	        beforechildrenrendered:function(n){
	        	if(n.leaf){
	        		TrainEnforcePlanRdp.nodes.push(n.id);
	        	}
	        	if(n.leaf == true && !TrainEnforcePlanRdp.selected){
	        		TrainEnforcePlanRdp.selected = true;
	        		n.select();
	        		TrainEnforcePlanRdp.clicked = true;
	        		n.ui.textNode.click();
	        	}
	        	if(n.id == TrainEnforcePlanRdp.clickRdpIdx){
	        		TrainEnforcePlanRdp.selected = true;
	        		n.select();
	        		TrainEnforcePlanRdp.clicked = true;
	        		n.ui.textNode.click();
	        	}
	        	n.ui.textNode.style.color='red';
	        },
	        click: function(node,e){        	
	        	if(node.leaf == true){
	        		TrainEnforcePlanRdp.rdpIdx = node.id;
	        		if(TrainEnforcePlanRdp.clicked == false){
	        			TrainEnforcePlanRdp.clickRdpIdx = node.id;
	        		}else{
	        			TrainEnforcePlanRdp.clicked = false;
	        		}     
	        		if(mode == 'gantt'){
		        		var ganttsrc = ctx + "/jsp/jx/jxgc/producttaskmanage/LED_Gantt.jsp?rdpIDX="+node.id;
			    		var gantth = jQuery("#gantt").height();
			    		document.getElementById("gantt").innerHTML = "<iframe style='width:100%;height:"+gantth+"px;overflow:auto;' frameborder='0' src='" + ganttsrc + "'></iframe>";
	        		}
	        		jQuery.ajax({
						url: ctx + "/trainEnforcePlanRdp!getBeseInfo.action",
						data:{rdpId: TrainEnforcePlanRdp.rdpIdx},
						dataType:"json",
						type:"post",
						success:function(data){
							var record=new Ext.data.Record();
							for(var i in data){
								record.set(i,data[i]);
							}
							var instId = record.get("processInstID");
							TrainEnforcePlanRdp.LoadInfo(record);
							
				    		//if(record.get("isDelayTrain")==isDelayTrain) Ext.getCmp("isDelayTrain").setValue("此检修机车已延时交车！");
				    		if(instId == null || instId=='undefined'){
				    			MyExt.Msg.alert("无对应的BPS流程实例！");
				    		}else{
				    			//var src = ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?processInstID="+instId+"&rdpId="+TrainEnforcePlanRdp.rdpIdx;
				    			var src = ctx + "/jsp/jx/workflow/WorkflowGraphic_v.jsp?color=black&processInstID="+instId+"&rdpId="+TrainEnforcePlanRdp.rdpIdx;
					    		var h = jQuery("#flowFrame").height();
					    		var ifrm = document.getElementById("flow_id");
					    		if(ifrm){
					    			ifrm.src = src;
					    			ifrm.style.height = h + "px";
					    		}else{
					    			document.getElementById("flowFrame").innerHTML = "<iframe id='flow_id' style='width:100%;height:"+h+"px;overflow:auto;' frameborder='0' src='" + src + "'></iframe>";
					    		}
				    		}
						}
					});
	        	}
	    		
	        	
	        }
	    }
	});
	
	
	
	TrainEnforcePlanRdp.enforcePlanRdpTree.expandAll();

	//页面自适应布局
	var viewport = new Ext.Viewport({ 
		layout:'border', 
		items:[
	       {
		        title : '在修机车',
		        iconCls : 'chart_organisationIcon',
		        tools : [ {
		            id : 'refresh',
		            handler : function() {
		                TrainEnforcePlanRdp.enforcePlanRdpTree.root.reload();
		                TrainEnforcePlanRdp.enforcePlanRdpTree.expandAll();
		                TrainEnforcePlanRdp.selected = false;
		            }
		        } ],
		        collapsible : true,
		        width : 120,
		        minSize : 160,
		        maxSize : 280,
		        split : true,
		        region : 'west',
		        layout : "border",
		        bodyBorder: false,
		        autoScroll : false,
		        items : [
		                 {
							region : "center",
							xtype:'panel',
							layout : "border",
							items : [
				  				new Ext.Panel({
									region:"center",
									layout:"fit",
									items:[TrainEnforcePlanRdp.enforcePlanRdpTree]
								}),
				  				new Ext.Panel({
				  					region:"south",
				  					xtype:'panel',
				  					layout:"fit",
				  					height:70,
				  					items:[TrainEnforcePlanRdp.bottomForm]
				  				})
					         ]
		        		}]
				},
				new Ext.Panel({
					region : "center",
					layout : "border",
					items : [
				         new Ext.Panel({
							region:"north",
							layout:"fit",
							height:70,
							split:true,
							maxSize:70,
							minSize:70,
							frame: true,
							items:[
							       TrainEnforcePlanRdp.baseForm
							      ]
						}),
						new Ext.Panel({
							region:"center",
							layout:"border",
							split : true,
							style: 'background-color:black',
							items:[
							       	content
						       	  ]
							
						})]
					})
				]
		 });
	/**
	 * 如果需要变背景为黑色
	 */
	jQuery("div").each(function(){
		jQuery(this).css("backgroundColor","black");
	});
	
	function setDJS(a, b){
		jQuery("#train").html("刷新剩余" + a + " 秒");
		jQuery("#change").html("切换剩余" + b + " 秒");
	}
	
	setDJS(ts, cs);
	
	setInterval(function(){
		
		var t = jQuery("#train").html();
		var c = jQuery("#change").html();
		var its = parseInt(t.substring(4, t.length-2));
		var ics = parseInt(c.substring(4, c.length-2));
		
		if(ics == 0){
			ics = cs;
			for(var i = 0; i < TrainEnforcePlanRdp.nodes.length; i++){
				if(TrainEnforcePlanRdp.nodes[i] == TrainEnforcePlanRdp.rdpIdx){
					var j = i + 1;
					if(i == TrainEnforcePlanRdp.nodes.length - 1){
						j = 0;
					}
					TrainEnforcePlanRdp.enforcePlanRdpTree.getNodeById(TrainEnforcePlanRdp.nodes[j]).ui.textNode.click();
					break;
				}
			}
			if(its == 0){
				its = 1;
			}
		}else if(its == 0){
			its = ts;
			TrainEnforcePlanRdp.enforcePlanRdpTree.root.reload();
            TrainEnforcePlanRdp.enforcePlanRdpTree.expandAll();
            TrainEnforcePlanRdp.selected = false;
		}
		setDJS(its - 1, ics -1);
		
	}, 1000);
	
	function toNextNode(){
		var rootNode = TrainEnforcePlanRdp.enforcePlanRdpTree.getRootNode();
	}
});