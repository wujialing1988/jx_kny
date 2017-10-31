Ext.onReady(function(){
	Ext.namespace('PersonalDesk');                       //定义命名空间
	
	//保存当前的宽 高
	PersonalDesk.initHeight = this.innerHeight;
	PersonalDesk.initWidth = this.innerWidth;
	
	PersonalDesk.panelNum = 0;
	PersonalDesk.columnNum = 1;
	PersonalDesk.viewport = null;
	//PersonalDesk.urlList = [{"showname":"待办事项","url":"/frame/yhgl/Default.jsp"},{"showname":"整备待办事项","url":"/frame/yhgl/ZBDefault.jsp"}];
	PersonalDesk.urlList = [{"showname":"待办事项","url":"/frame/yhgl/Default.jsp"}];
	//PersonalDesk.urlList = [];
	//默认页面个数，注意，上面push的个数和下面的n一致,默认只push了待办事项，所以n=1
	var n = 1;
	//通过ajax查询panelNum 列数 和对应的url
	Ext.Ajax.request({
		url: ctx + "/acPersonalDeskLayout!findAcPersonalDeskLayout.action",
		success: function(r){
			var retn = Ext.util.JSON.decode(r.responseText);
			if(retn.success){
			
				//通过后台获取panel个数
				PersonalDesk.panelNum = retn.panelNum + n;
				
				if(PersonalDesk.panelNum == 0){
					var html = "<br>";
					html += "<br>";
					html += "<h1 style='color:blue; text-align:center;font-size:30px'>自定义桌面配置</h1>";
					html += "<br>";
					html += "<br>";
					
					PersonalDesk.centerPanel = new Ext.Panel({
						layout : 'column', 
						frame: true,//渲染面板 
						border: true,
						items: [{ 
							title:'自定义桌面配置图', 
							html : "<img src='" + ctx + "/jsp/jx/common/PersonalDeskExplain.jpg'/>",
							columnWidth:.5   //指定列宽为容器剩余宽度的50% 
						},{ 
							title:'自定义桌面效果图', 
							html : "<img src='" + ctx + "/jsp/jx/common/PersonalDeskExplain2.jpg'/>",
							columnWidth:.5   //指定列宽为容器剩余宽度的50% 
						}]
					});
					PersonalDesk.panel = new Ext.Panel({
						height : this.innerHeight, 
   						width : this.innerWidth, 
						layout: "border", 
						frame: true,
						defaults : {//设置默认属性 
							border: true
						}, 
					    items: [{
					        region: 'north',
					        html : html
					        
					    },{
					        region : 'center', items: PersonalDesk.centerPanel
					    }]
					});
					
					PersonalDesk.viewport = new Ext.Viewport({ items: PersonalDesk.panel });
				}else{
					PersonalDesk.viewport = null;
				}
				
				//获取列数
				PersonalDesk.columnNum = retn.columnNum;
				for(var i = 0;i<retn.objMapList.length;i++){
					//获取每个panel的url和他的showname
					PersonalDesk.urlList.push(retn.objMapList[i]);
				}
				//创建window
				//行数
				var heightNum = getPersonalDeskHeightNum(PersonalDesk.panelNum,PersonalDesk.columnNum);
				//全局高度，针对每一个panel高
				PersonalDesk.height = (this.innerHeight - 10) / heightNum;
				//全局宽度，针对每一个panel宽
				PersonalDesk.width = (this.innerWidth  - 10) / PersonalDesk.columnNum;
				//存放所有window的名称
				PersonalDesk.windowNameArray = getWindowNameArray(PersonalDesk.panelNum);
				//判断循环生成的宽高对数，和panel数量一致
				var pNum = PersonalDesk.panelNum;	
				//封装宽高数据 如果有2个panel那么里面就有2组数据 ["11,22","222,444"] 保存的是x和y
				var wh = [];
				for(var h = 0;h < heightNum;h++){
				
					//计算的时候多+10，让window和边框分离
					var thisWindowY = 10 + PersonalDesk.height * h;
					for(var w = 0;w < PersonalDesk.columnNum;w++){
						if(pNum >= 0){
							//计算的时候多+10，让window和边框分离
							var thisWindowX = 10 + PersonalDesk.width * w;
							pNum--;
							wh.push(thisWindowX +"," +thisWindowY);
						}
					}
				}
				
				//准备序号，只创建对应的panel数量的window
				var sq = 1;
				for(var i in PersonalDesk.windowNameArray){
				
					if( sq <= i+1){
						var xValue = parseInt(wh[i].split(",")[0]);
						var yValue = parseInt(wh[i].split(",")[1]);
						var windowName = PersonalDesk.windowNameArray[i];
						//拷贝window属性到PersonalDesk中，有多少个，就拷贝多少个
						PersonalDesk[windowName] = new Ext.Window({
							title: PersonalDesk.urlList[parseInt(i)].showname,
							
							//panel的长宽缩小10，使得每一个window之间有缝隙，视觉好看
							width: PersonalDesk.width - 10, 
							height: PersonalDesk.height - 10, 
							layout: "fit", 
							closable:false,
							resizable:false,
							collapsible : true,//面板是可收缩
							monitorResize : true,
							x: xValue,
							y: yValue,
							draggable : false,
							maximizable : true,
							autoScroll: true,
							//根据不同的应用配置不一样的jsp地址
							items: new Ext.Panel({
							    renderTo: Ext.getBody(),
							    bodyCfg: {
							        tag: 'center',
							        cls: 'x-panel-body'
							    },
							    items: [{
									xtype: "panel",
									layout: "fit",	
									closable : false,
									html: "<iframe src='" + ctx + PersonalDesk.urlList[parseInt(i)].url + "' scrolling='auto' frameborder=0 width=100% height=100%></iframe>",
									listeners: {  
										
									}
							    }]
							})
						});
						
						//将window窗口设置位置
						PersonalDesk[windowName].show();
					}
					sq++;
				}
				
				
			}else{
				alertFail(retn.errMsg);
			}
		},
		failure: function(){
			alertFail("请求超时！");
		}
	});
	
	//监控浏览器放大缩小
	window.onresize=function(){
		var heightBL = PersonalDesk.initHeight/this.innerHeight;
		var widthBL = PersonalDesk.initWidth/this.innerWidth;
	
		//获取当前页面中的window数组
		if(PersonalDesk.windowNameArray && PersonalDesk.windowNameArray.length > 0){
			//便利数组
			for(var i in PersonalDesk.windowNameArray){
				var windowName = PersonalDesk.windowNameArray[i];
				var wind = PersonalDesk[windowName];
				//如果不是一个window对象
				if(wind){
					
					//按比例缩放框体大小
					wind.setHeight(wind.getHeight() / heightBL);
					wind.setWidth(wind.getWidth() / widthBL);
					
					//按比例调整window位置
					//边缘window不做位置调整
					if(wind.y != 10){
						//wind.y = wind.y / heightBL;
						wind.setPagePosition(wind.x,wind.y / heightBL);
					}
					if(wind.x != 10){
						//wind.x = wind.x / widthBL;
						wind.setPagePosition(wind.x / widthBL,wind.y);
					}
					//强制重新计算组件的大小尺寸
					wind.syncSize();
				}
			}
		}
		
		if(PersonalDesk.panel){
			PersonalDesk.panel.setWidth(this.innerWidth);
			PersonalDesk.panel.setHeight(this.innerHeight);
			PersonalDesk.syncSize();
		}
		
		//保存当前的宽 高
		PersonalDesk.initHeight = this.innerHeight;
		PersonalDesk.initWidth = this.innerWidth;
	}
	
	//页面自适应布局
	
});

//通过传入的panel个数和列数算出平局每一个panel的height值
function getPersonalDeskHeightNum(panelNum,columnNum){
	var personalDeskHeightNum = 0;
	
	//如果个数余列数不为0，说明需要额外的行来存放多出的window
	if(panelNum%columnNum != 0){
		personalDeskHeightNum = (Math.round(panelNum/columnNum - 0.5) + 1);
	}else{
		personalDeskHeightNum = (panelNum/columnNum);
	}
	return personalDeskHeightNum;
}

//通过传入的panel个数返回window名称数组
function getWindowNameArray(panelNum){
	var windowNameArray = [];
	for(var i = 0; i < panelNum ; i++){
		windowNameArray.push("window"+i);
	}
	return windowNameArray;
}
