/**
 * UI表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.ns('VIS');
	
	VIS.parentNodes = new vis.DataSet();		// 机车检修作业流程计划数据集对象
	
	VIS.edges = new vis.DataSet();
	
	VIS.network = null;					// 时间轴network对象
	
	VIS.options = {
        groups: {
	          usergroups: {
	            shape: 'image',  image: '1'
	          },
	          users: {
	            shape: 'square'
	          },
	          dot: {
	            shape: 'dot'
	         },
	         main:{main:"adsf adsf asd",width:'100%'}
        }
	//        ,layout: {
	//                hierarchical: {
	//                    direction: 'LR'
	//                }
	//            }
	 };
	
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
				}, '-', {
				text : '批量派工',				
				iconCls:"chart_organisationIcon",
		    	handler: function(){
		    		
		    	}}, '->', '图例：', '-', {
					xtype : 'label',
					text : '未开工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_wkg'
				}, '-', {
					xtype : 'label',
					text : '已开工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_ykg'
				}, '-', {
					xtype : 'label',
					text : '已超时',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_yyq'
				}, '-', {
					xtype : 'label',
					text : '已完工',
					style: 'font-weight:bold;',
					cls : 'status_example status_example_ywg'
				}],			
				html : [
					'<div style="background-color:black" id= "visualization">',
					'</div>'
				].join(""),
				listeners: {
					afterrender: function(window) {
						
						addDatas();
						
						VIS.network = new vis.Network(
							document.getElementById('visualization'),  
							VIS.data, 				
							VIS.options
						);
						
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
		
	var svg1 = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
            '<polygon points="0,60 40,10  150,10 150,60" style="stroke:white;stroke-width:5;fill-rule:nonzero;" />' +
            '<line x1="50" y1="10" x2="50" y2="60" style="stroke:white;stroke-width:5" />' +
            '<circle cx="50" cy="65" r="15" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<circle cx="110" cy="65" r="15" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<text style="fill:red;font-size:12pt;" x="55" y="40">SS4B0544</text>'+
            '</svg>';
			

        var url = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg1);
        
        
        

	var svg1 = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
            '<rect x = "10" y="10" width = "140" height="50" style="stroke:white;stroke-width:5;fill-rule:nonzero;"/>' +
            '<line x1="0" y1="20" x2="10" y2="20" style="stroke:white;stroke-width:5" />' +
            '<line x1="0" y1="50" x2="10" y2="50" style="stroke:white;stroke-width:5" />' +
            '<line x1="50" y1="10" x2="50" y2="60" style="stroke:white;stroke-width:5" />' +
            '<circle cx="50" cy="65" r="15" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<circle cx="110" cy="65" r="15" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<text style="fill:red;font-size:12pt;" x="55" y="40">车厢</text>'+
            '</svg>';
			

        var url1 = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg1);
		
		for(var k=0; k < 20; k++){
				var imgUrl = getSvg(k) ;
				var lable = "";
				if(k==0){
					lable = "车头";
				}else{
					lable = "车厢"+k;
				}
			   // 添加【配件流程节点显示】
				VIS.parentNodes.add({
					id : "id"+k,
					label : lable,
					//shape: "",
					size:20,
				    color:"red",
	   	     		x:-50 + k*153,
					y: 200,
					value: "value"+k,
					fixed:true,
					shape: 'image',
					image: imgUrl,
					font: {size:10,strokeWidth: 3,'face': 'Monospace', align: 'left'}
				});
			}
	}
	
	function getSvg(k){
		var svg = "";
		var url = "";
		var color = "white";
		if(k == 1){
			color = "green";
		}else if(k==2){
			color = "yellow";
		}
		if(k == 0){
			svg =  '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
            '<polygon points="0,60 40,10  150,10 150,60" style="stroke:white;stroke-width:5;fill-rule:nonzero;" />' +
            '<line x1="50" y1="10" x2="50" y2="60" style="stroke:white;stroke-width:5" />' +
            '<circle cx="50" cy="65" r="13" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<circle cx="110" cy="65" r="13" stroke="white" stroke-width="3" fill="#9b8885"/>' +
            '<text style="fill:red;font-size:12pt;" x="55" y="40">SS4B0544</text>'+
            '</svg>';
		}else{
			svg = '<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="153" height="80" style="background-color:black">' +
            '<rect x = "10" y="10" width = "140" height="50" style="stroke:'+color+';stroke-width:5;fill-rule:nonzero;"/>' +
            '<line x1="0" y1="20" x2="10" y2="20" style="stroke:'+color+';stroke-width:5" />' +
            '<line x1="0" y1="50" x2="10" y2="50" style="stroke:'+color+';stroke-width:5" />' +
            '<line x1="50" y1="10" x2="50" y2="60" style="stroke:'+color+';stroke-width:5" />' +
            '<circle cx="50" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
            '<circle cx="110" cy="65" r="13" stroke="'+color+'" stroke-width="3" fill="#9b8885"/>' +
            '<text style="fill:red;font-size:12pt;" x="55" y="40">'+k+'节</text>'+
            '</svg>';
		}
		url = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg);
		return url ;
	}

});