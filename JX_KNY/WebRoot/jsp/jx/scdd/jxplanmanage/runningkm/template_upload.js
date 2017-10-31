Ext.ns("Signature");

Signature.progress = document.querySelector(".progress");

Signature.uploadSuccess = function(){
	alertSuccess("上传成功！");
	Signature.FileID = null;
	Signature.ApplyWin.hide();	
};



function runner(progress){
	Signature.progress.style.width = progress + "%";
}

Signature.btnDisable = function(disable, selector){
	if(selector)
		if(disable)
			document.querySelector("." + selector).className = selector + " btndisable";
		else
			document.querySelector("." + selector).className = selector;
	else{
		var dom = document.querySelectorAll(".btndisable");
		for(var i in dom){
			dom[i].className = dom[i].className.replace(" btndisable", "");
		}
	}
};

Signature.enable = function(disable){	
	this.setButtonDisabled(disable);
	if(disable)
    	this.setButtonCursor(SWFUpload.CURSOR.ARROW);
	else
		this.setButtonCursor(SWFUpload.CURSOR.HAND);
};

Signature.log = function(info){
	document.querySelector(".fileTip").innerHTML = info;
};

Signature.FileID = null;

Signature.initSwf = function() {
	Signature.swf = new SWFUpload({
        upload_url: ctx + uploadAction,
        file_post_name : 'importFile',
        file_size_limit: 1024 * 100,
        flash_url: ctx + '/frame/resources/SWFUpload/swfupload.swf',
        button_placeholder_id: 'apply_x',
        button_width: 150,
        button_height: 50,
        file_types:"*.xls",
        button_cursor: SWFUpload.CURSOR.HAND,
        button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
        file_queued_handler: function (file) {
        	if(Signature.FileID){
        		Signature.swf.cancelUpload(Signature.FileID, false);
        		Signature.FileID = null;
        	}
        	Signature.log("已选择：" + file.name);
        	var dom = document.querySelector(".btndisable");
        	if(dom){        		
        		dom.className = 'mybtn';
        		dom.onclick = Signature.startUpload;            	
        	}
        	Signature.FileID = file.id;
        },
        upload_progress_handler: function (file, complete, total) {            
            //处理进度
        	var i = complete / total * 100;
        	if(i >= 100){
        		Signature.log("文件上传成功，正在处理数据...");
        	}
        	runner(i);
        },
        upload_error_handler: function (file, errorcode, message) {                    
        	Signature.log("上传错误：请检查上传文件内容是否符合格式规范" /*+ errorcode + ":" + message*/);
        	Signature.enable.call(Signature.swf, false);
        	Signature.btnDisable(false, "myselect");
        	Signature.FileID = null;
        },
        upload_success_handler: function (file, serverdata) {
        	Signature.uploadSuccess();
        },
        /*upload_complete_handler: function (file) {
            startSwfUpload();
        },*/
        file_queue_error_handler: function (file) {
        	MyExt.Msg.alert('文件过大或不允许上传的格式');
        }
    });
}();

Signature.startUpload = function(){
	if(this.className.indexOf("btndisable") == -1){
		Signature.enable.call(Signature.swf, true);
    	Signature.btnDisable(true, "myselect");
    	Signature.btnDisable(true, "mybtn");
    	if (Signature.swf.getStats().files_queued > 0) {    		
            Signature.swf.startUpload();
        }
	}
};

Signature.Apply = function(){
	if(Signature.ApplyWin == undefined){
		Signature.ApplyWin = new Ext.Window({
			title: uploadTitle, closeAction:"hide",
			width: 360, height:150, contentEl:'window_el',
			resizable:false, modal:true
		});
	}else{		
		Signature.enable.call(Signature.swf, false);
	}
	Signature.ApplyWin.show();
	Signature.log("尚未选择文件");	
	Signature.btnDisable(false, "myselect");
	Signature.btnDisable(true, "mybtn");
	runner(0);
};
Signature.Apply();