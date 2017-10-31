/*var onmessage = function(e) {    
	var data = Ext.util.JSON.decode(e.data);
	if(data.success){
		alertSuccess("操作成功！");
	}
	reg.setStatus(data);
};
if (typeof window.addEventListener != 'undefined') {    
  window.addEventListener('message', onmessage, false);    
} else if (typeof window.attachEvent != 'undefined') {    
  window.attachEvent('onmessage', onmessage);
}*/


/**
 * 警告： 此JS依赖变量uploadAction与uploadTitle
 */


var currentFilePath = document.scripts;
currentFilePath = currentFilePath[currentFilePath.length - 1].src;
currentFilePath = currentFilePath.substring(0, currentFilePath.lastIndexOf("/") + 1);
Ext.onReady(function(){
	Ext.ns("reg");
	/**
	 * 显示上传窗口
	 */
	reg.showUpload = function(grid){
		if(grid) reg.grid = grid;
		if(!reg.loaded){
			reg.appendHTML();
			reg.loadUsing();
			reg.loaded = true;
			setTimeout(function(){
				if(typeof Signature == 'undefined')					
					setTimeout(arguments.callee, 100);
			}, 1000);
			//第一次加载JS是会调用Signature.Apply方法，所以此处不需要调用
		}else{						
			Signature.Apply();
		}
	};
	/**
	 * 加载需要依赖的文件
	 */
	reg.loadUsing = function(){
		if(document.querySelector("apply_x") == null){			
			reg.getUsingCss(currentFilePath + "template_upload.css");
			reg.getUsingJs(ctx + "/frame/resources/SWFUpload/swfupload.js", function(){
				reg.getUsingJs(currentFilePath + "template_upload.js", reg.override);
			});		
		}else{			
			setTimeout(arguments.callee, 100);
		}
	};
	/**
	 * 重载方法
	 */
	reg.override = function(){
		if(typeof Signature != 'undefined' && Signature.uploadSuccess){
			Signature.uploadSuccess = function(){
				alertSuccess("上传成功！");
				Signature.FileID = null;
				Signature.ApplyWin.hide();
				reg.grid.store.load();
			};
		}else{			
			setTimeout(arguments.callee, 100);
		}
	};
	/**
	 * 添加必须的HTML代码
	 */
	reg.appendHTML = function(){
		var div = document.createElement("div");
		//div.id = 'sdfx';
		div.innerHTML = "<div id='window_el'><div class='myselect'>" +
				"<div class='btnUpload'><div id='apply_x'></div></div>" +
				"<div class='btnText'>选择导入文件</div></div>" +
				"<div class='mybtn btndisable'><div class='btnText'>上传导入文件</div>" +
				"</div><div style='clear:both'></div><div class='fileTip'></div>" +
				"<div class='progressbar'><div class='progress'></div></div></div>";
		div.style.display='none';
		document.body.appendChild(div);
	};
	/**
	 * 加载Js文件
	 */
	reg.getUsingJs =function(src, callfunc){
		var s = document.createElement("script");
		s.type = "text/javascript";
		s.src = src;
		reg.jsloaded.call(s, callfunc);
		var head = document.querySelector("head");
		head.appendChild(s);
	};
	/**
	 * js文件加载完毕
	 */
	reg.jsloaded = function(callfunc){
		if(this.onreadystatechange){
			this.onreadystatechange = function(){
				if(this.readyState == 'loaded' || this.readyState == 'complete'){
					callfunc();
					return;
				}
			};
		}else{
			this.onload = function(){
				callfunc();
				return;
			};
		}
	};
	/**
	 * 加载css文件
	 */
	reg.getUsingCss = function(src){
		var l = document.createElement("link");
		l.type = 'text/css';
		l.href = src;
		l.rel = 'stylesheet';
		var head = document.querySelector("head");
		head.appendChild(l);
	};
	
});