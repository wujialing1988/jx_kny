Ext.onReady(function(){
	if(typeof(SR) === 'undefined'){
		Ext.ns("SR");
	}
	
	var js = {Images: ctx + "/jsp/sb/base/js/Images.js",
			  Info:ctx + "/jsp/sb/base/js/Info.js",
			  AttachEquipment:ctx +"/jsp/sb/base/js/AttachEquipment.js"
			 };
	//当前激活的面板
	SR.activePanel = null;
	
	var win, initialized = false, idx, _context, loader = { length: 1 }, loaded = {};;
	
	var loadMask;
	/*
	 * 显示处理遮罩
	 */
	function showtip(){
		loadMask = new Ext.LoadMask(win.getEl(),{
			msg: "正在加载页面，请稍后...",
			removeMask:true
		});
		loadMask.show();
	}
	/*
	 * 隐藏处理遮罩
	 */
	function hidetip(){
		loadMask.hide();
	}
	
	function createWin(){
		
		(function(){
			var style = document.createElement("link");
			style.rel="stylesheet";
			style.href = ctx + "/jsp/sb/base/css/list.css";
			style.type = 'text/css';
			document.body.appendChild(style);
			var div = document.createElement("div");
			div.style.display = 'none';
			div.innerHTML = '<div id="accordion"><div src="Info">设备概况</div><div src="Images">设备照片</div><div src="AttachEquipment">附属设备</div></div>';
			document.body.appendChild(div);
			
			
			MyEach(document.querySelectorAll("#accordion div"), function(){
				this.onclick = elSwitch;
			});
		})();
		
		
		win = new Ext.Window({ 
		   	title: "主设备查看",width:900, height:520, maximizable:true, layout: "fit", 
			closeAction: "hide", modal: true, maximized: false , buttonAlign:"center",
			items: {
				xtype: "panel", layout: "border",
				border: false,
				items:[{
			        layout: "fit",
			        region : 'west',
			        width: 200,
			        maxWidth: 200,
			        minWidth:100,
			        border: true,
			        split: true,
			        bodyStyle: "background-color:rgb(212, 221, 243);",
			        contentEl: "accordion"
				 },{
			        layout: "card",			        
			        border: true,
			        region : 'center',
			        id: "content_panel",
			        xtype: "panel",
			        bodyStyle: "background-color:rgb(212, 221, 243);",
			        activeItem: 0,
			        items: [{
			        	html: "&nbsp;页面加载中..."
			        }]
				 }]
			},
			buttons:[{
				text: "关闭", iconCls: "closeIcon", 
				handler: function(){
					win.hide();
		        }
		    }],
		    listeners:{
		    	hide: function() {
		    		var el = document.querySelector(".active");
		    		if(el) el.className = '';
		    	},
		    	show: function() {
		    		document.querySelector("#accordion div").click();
		    	}
		    }
		});
		
		_context = Ext.getCmp("content_panel");
		_hide = Ext.getCmp("hide_panel");
	}
	
	function loadItem(target){
		_context.getLayout().setActiveItem(loader[target]);
		window[target].load(idx);
	}
	function loadScript(target){
		showtip();
		var s = document.createElement("script");
		s.type='text/javascript';
		s.src = js[target];
		document.body.appendChild(s);
	}
	/**
	 * 元素切换调用事件
	 */
	function elSwitch(){
		if(this.attributes.src == undefined) return;		
		var el = document.querySelector(".active");
		if(el == this) return;
		if(el) el.className = '';
		this.className = "active";
		var target = this.attributes.src.value;
		
		SR.activePanel = target;
		
		if(target && loader[target] === undefined){
			loader[target] = loader.length++;
			loadScript(target);
		}else if(loaded[target]){
			loadItem(target);
		}
	}
	
	SR.addContent = function(id, content){
		loaded[id] = true;
		_context.add(content);
		_context.doLayout();
		loadItem(id);
		hidetip();
	};

	
	SR.show = function(r){
		idx = r.get("idx");
		if(initialized == false){
			initialized = true;
			createWin();
		}
		win.show();
		win.setTitle("主设备查看（" + r.get("equipmentCode") + " - " + r.get("equipmentName")+ "）");
	};
});