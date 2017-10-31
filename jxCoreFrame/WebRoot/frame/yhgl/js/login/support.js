/**
*flash 验证
*/
function isInstallFlash(){
	var hasFlash=0;//是否安装了flash
	var flashVersion=0;//flash版本
	try{
		if(document.all) {
			var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
			if(swf) {
				hasFlash=1;
				VSwf=swf.GetVariable("$version");
				flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
			}
		}else{
			if (navigator.plugins && navigator.plugins.length > 0){
				var swf=navigator.plugins["Shockwave Flash"];
				if (swf){
					hasFlash=1;
					var words = swf.description.split(" ");
					for (var i = 0; i < words.length; ++i){
						if (isNaN(parseInt(words[i]))) continue;
						flashVersion = parseInt(words[i]);
					}
				}
			}
		}
	}catch(e){
	}
	return {f:hasFlash,v:flashVersion};
}

/**
*Silverlight验证
*/
function isInstallSilverlight() {
	var hasSilverlight = 0;//是否安装了Silverlight
	var silverlightVersion = 0;//Silverlight版本
    var browser = navigator.appName;
    if (document.all) {
        try {
           var slControl = new ActiveXObject('AgControl.AgControl');
           hasSilverlight = 1;
           silverlightVersion = GetSilverlightVersion(slControl);
           slControl = null ;
        } catch (e) {
        }
    } else {
        try {
            if (navigator.plugins('Silverlight Plug-In')) {
                hasSilverlight = 1;
            }
        } catch (e) {
        }
    }
    return {f:hasSilverlight,v:silverlightVersion};
}

//获取当前的状态 
function  getNowState(value){
	this.html = '';
	if('flash' == value){
		var fls = isInstallFlash();
		if(fls.f) {
			this.html = '<img src="' + ctx + '/frame/resources/images/toolbar/startup.png"/>';
		}else{
			this.html = '<img src="' + ctx + '/frame/resources/images/toolbar/del.png"/>';
		}
	}else if('silverlight' == value){
		var installSiverlight = isInstallSilverlight();
		if(installSiverlight.f){
			this.html = '<img src="' + ctx + '/frame/resources/images/toolbar/startup.png"/>';
		}else{
			this.html = '<img src="' + ctx + '/frame/resources/images/toolbar/del.png"/>';
		}
	}else{
		this.html = '<img src="' + ctx + '/frame/resources/images/toolbar/alert.gif"/>';
	}
	return this.html;
}

//检测的结果
function toCheck(selId){
	var url = '';//ctx + '/cPlugin!toView.action?id=' + selId;
 	alert(url);
}

//检测的html
function checkPlugin(enname, downLoadUrl){
	var thisHtml ="<span><a href='javascript:void(0);' onclick='validatePlugin(\"" +enname+"\",\""+downLoadUrl + "\");'>检测</a></span>";
	return thisHtml;
}

//进行下载
function toDown(fileUrl){
	var url = ctx + fileUrl;
	window.open(url,'_self','width=0,height=0');
}

//下载的html
function downPlugin(value){
	var fileUrl = value;
	var thisHtml ="<span><a href='javascript:void(0);' onclick='toDown(\"" +fileUrl + "\");'>下载</a></span>";
	return thisHtml;
}

/** 检验插件 */
function validatePlugin(enname, downLoadUrl){
	var vali;
	switch (enname){
		case 'flash': 
			vali = isInstallFlash();
			break;
		case 'silverlight':
			vali = isInstallSilverlight();
			break;
	}
	if(vali.f){
		Support.plugin.success("恭喜您，【"+enname+"】插件运行正常，版本为："+ vali.v);
	} else {
		//没有安装插件
		Support.plugin.error("很抱歉，【"+enname+"】插件未正确安装，是否下载安装包？", downLoadUrl);
	}
}

Ext.onReady(function(){
	Ext.namespace('Support');
	Ext.namespace('Support.plugin'); //系统插件
	
	Support.plugin.success = function(message){
		Ext.Msg.alert("检查结果", message);
	};
	
	Support.plugin.error = function(message, downLoadUrl){
		Ext.MessageBox.confirm('检查结果', message,  function(btn){
		    if (btn == 'yes'){
		    	toDown(downLoadUrl);
		    }
		});
	};
	

	
	//插件目录
	Support.plugin.catalogue = [
	        /* flash插件 */
		    {id   : '1',  enname      : 'flash',      fullname : '浏览器flash插件', 
		     size : '8.79M', downLoadUrl : '/frame/installfiles/flash.exe', fileImgUrl : 'installflash.png',
		     desc : '主要用作在用户上传文档型文件以后，用户可以直接通过浏览器查看上传的各种类型的文档。'},
		    /* silverlight插件 */ 
		    {id   : '2',  enname      : 'silverlight',      fullname : '浏览器silverlight插件', 
			 size : '6.63M', downLoadUrl : '/frame/installfiles/silverlight.exe', fileImgUrl : 'installsilverlight.png',
			 desc : 'silverlight。'}
	];
	
	//数据容器
	Support.plugin.store  = new Ext.data.JsonStore({
		 data : Support.plugin.catalogue,
		 fields: ['id', 'enname', 'fullname', 'size', 'downLoadUrl', 'desc']
	});

	Support.plugin.grid = new Ext.grid.GridPanel({
			selModel: new Ext.grid.CheckboxSelectionModel({singleSelect:false}),
			bbar: null,
		    border: false,loadMask:true,
		    //随着窗口（父容器）大小自动调整,true表示不出现滚动条，列宽会自动压缩
		    viewConfig: {forceFit: true},
		    //该高度设置在IE、Google浏览器显示正常，在Opera显示不正常
		    height: document.documentElement.scrollHeight,
		    //可移动列
		    enableColumnMove: true,
		    //偶数行变色
		    stripeRows: true,
		    colModel: new Ext.grid.ColumnModel([
		        new Ext.grid.RowNumberer(),
		        {header:'插件简称', dataIndex:'enname' , hidden:true},
			    {header:'插件名称', dataIndex:'fullname', width: 150},
			    {header:'描述', dataIndex:'desc', width: 185},
			    {header:'大小', dataIndex:'size', width: 55},
		        {header:'状态', dataIndex:'', width: 45, renderer: function(v,metadata, record,rowIndex, colIndex, store){
		        	return getNowState(record.data.enname);
		        }},
		        {header:'检测', dataIndex:'', width: 45, renderer: function(v,metadata, record,rowIndex, colIndex, store){
		        	return checkPlugin(record.data.enname, record.data.downLoadUrl);
		        }},
		        {header:"下载", dataIndex:"", width: 45, renderer: function(v,metadata, record,rowIndex, colIndex, store){
		        	return downPlugin(record.data.downLoadUrl);
		        }}
		    ]),
		    store: Support.plugin.store
		});
	
	//备用tab
	Support.tabPanel = new Ext.TabPanel({
		 activeTab: 0,
		 id : 'tabPanel',
		 enableTabScroll : true,
		 items: [{
			 title:'插件下载',
			 items: [Support.plugin.grid]
		 }]
	});
	
	Support.panel = new Ext.Panel({
		 id : 'tabPanel',
		 enableTabScroll : true,
		 items: [{
			 title:'插件下载',
			 items: [Support.plugin.grid]
		 }]
	});
	
	var viewport = new Ext.Viewport({ 
		layout:'fit', border:false, 
		items:[{
			title:'插件下载',
			layout:'fit',
			items:[Support.plugin.grid]
		}] 
	});
	
});