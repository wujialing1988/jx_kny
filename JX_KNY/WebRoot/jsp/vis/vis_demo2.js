/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('VIS');
	
	VIS.parentNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	
	VIS.edges = new vis.DataSet();
	
	VIS.network = null;					// 时间轴network对象
	
	VIS.options = {};
	
	VIS.data = {		
		 nodes: VIS.parentNodes,
         edges: VIS.edges
	};
	
	
	//机车详情页面
	VIS.Panel =  new Ext.Panel({		
				region: 'center',
				border: false, autoScroll : false, anchor:'100%',
				tbar: [{
					text : '刷新',
					iconCls : 'refreshIcon',
					handler : function() {
						self.location.reload();
					}			
				}],			
				html : [
					'<div style="background-color:white" id= "visualization">',
					'</div>'
				].join(""),
				listeners: {
					afterrender: function(window) {
						
						addDatas();
						
/*						VIS.network = new vis.Network(
							document.getElementById('visualization'),  
							VIS.data, 				
							VIS.options
						);*/
						
					}
				}
				
	});
	
		//页面适应布局
	VIS.viewport = new Ext.Viewport({
		layout:'fit', 
		items:[{
			layout: 'border',
			items: [
				VIS.Panel
				]
		}]
	});
	
	function addDatas(){
	        // create an array with nodes
/*        var nodesArray = [
            {id: 1, label: 'Node 1'},
            {id: 2, label: 'Node 2'},
            {id: 3, label: 'Node 3'},
            {id: 4, label: 'Node 4'},
            {id: 5, label: 'Node 5'}
        ];
        
        VIS.parentNodes = new vis.DataSet(nodesArray);

        // create an array with edges
        var edgesArray = [
            {from: 1, to: 3},
            {from: 1, to: 2},
            {from: 2, to: 4},
            {from: 2, to: 5}
        ];
        
        VIS.edges = new vis.DataSet(edgesArray);*/
        
                // this list is kept to remove a random node.. we do not add node 1 here because it's used for changes
        nodeIds = [2, 3, 4, 5];
        shadowState = false;


        // create an array with nodes
        nodesArray = [
            {id: 1, label: 'Node 1',x:120,y:50},
            {id: 2, label: 'Node 2',x:220,y:50},
            {id: 3, label: 'Node 3',x:320,y:50},
            {id: 4, label: 'Node 4',x:420,y:50},
            {id: 5, label: 'Node 5',x:520,y:50}
        ];
        nodes = new vis.DataSet(nodesArray);

        // create an array with edges0
        edgesArray = [
            {from: 1, to: 3},
            {from: 1, to: 2},
            {from: 2, to: 4},
            {from: 2, to: 5}
        ];
        edges = new vis.DataSet(edgesArray);

        // create a network
        var container = document.getElementById('visualization');
        var data = {
            nodes: nodes,
            edges: edges
        };
        var options = {
			interaction:{
			    dragNodes:false, // 是否可移动节点
			    dragView: true // 是否可移动整个图
			  },
			  nodes:{
			  	    fixed: {
				      x:true,
				      y:true
				    },
				    size:10,
				    shape:'circle'
			  }
		}
        network = new vis.Network(container, data, options);
	}

});