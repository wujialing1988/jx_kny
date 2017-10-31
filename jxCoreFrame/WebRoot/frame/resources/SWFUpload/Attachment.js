/**
 * 附件管理 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 * 对客户端做了部分修改，以实现完整的附件上传功能模块。
 * 如果上传成功，服务端应返回: {"success":true}
 * 如果上传失败，服务端应返回: {"success":false,"error":"错误的原因!"}
 */
//文件上传面板
Attachment.UploadPanel = function(cfg){  
    this.width = 500;
    this.height = 200;
    Ext.apply(this,cfg);
    this.gp = new Ext.grid.GridPanel({
        border :false,
        store: new Ext.data.Store({
            id: 'id', fields:['id','name','type','size','state','percent']
        }),
        columns: [
            new Ext.grid.RowNumberer(),
            {header: '文件名', width: 180, sortable: false,dataIndex: 'name', menuDisabled:true},
            {header: '类型', width: 50, sortable: false,dataIndex: 'type', menuDisabled:true},
            {header: '大小', width: 70, sortable: false,dataIndex: 'size', menuDisabled:true,renderer:this.formatFileSize},
            {header: '进度', width: 150, sortable: false,dataIndex: 'percent', menuDisabled:true,renderer:this.formatProgressBar,scope:this},
            {header: '状态', width: 60, sortable: false,dataIndex: 'state', menuDisabled:true,renderer:this.formatFileState,scope:this},
            {header: '&nbsp;',width:40,dataIndex:'id', menuDisabled:true,renderer:this.formatDelBtn}       
        ]           
    });
    this.setting = {
        upload_url : this.uploadUrl, 
        flash_url : this.flashUrl,
        file_size_limit : 1024 * Attachment.uploadMaxSize,  //上传文件体积上限，单位MB
        file_post_name : this.filePostName,
        file_types : Attachment.uploadFileType,              //允许上传的文件类型 
        file_types_description : Attachment.uploadFileDescription,             //允许上传文件类型的描述
        file_upload_limit : "0",                                //限定用户一次性最多上传多少个文件，在上传过程中，该数字会累加，如果设置为“0”，则表示没有限制 
        //file_queue_limit : "10",                              //上传队列数量限制，该项通常不需设置，会根据file_upload_limit自动赋值              
        post_params : this.postParams||{savePath:'upload\\'},
        use_query_string : true,
        debug : false,
        button_cursor : SWFUpload.CURSOR.HAND,
        button_window_mode : SWFUpload.WINDOW_MODE.TRANSPARENT,
        custom_settings : {                                     //自定义参数
            scope_handler : this
        },
        file_queued_handler : this.onFileQueued,
        swfupload_loaded_handler : function(){},                // 当Flash控件成功加载后触发的事件处理函数
        file_dialog_start_handler : function(){},               // 当文件选取对话框弹出前出发的事件处理函数
        file_dialog_complete_handler : this.onDiaogComplete,    //当文件选取对话框关闭后触发的事件处理
        upload_start_handler : this.onUploadStart,              // 开始上传文件前触发的事件处理函数
        upload_success_handler : this.onUploadSuccess,          // 文件上传成功后触发的事件处理函数 
        swfupload_loaded_handler : function(){},                // 当Flash控件成功加载后触发的事件处理函数  
        upload_progress_handler : this.uploadProgress,
        upload_complete_handler : this.onUploadComplete,
        upload_error_handler : this.onUploadError,
        file_queue_error_handler : this.onFileError
    };
    Attachment.UploadPanel.superclass.constructor.call(this,{   
        tbar : [
            {text:'选择文件',iconCls:'addIcon',ref:'../addBtn'},'-',
            {text:'开始上传',ref:'../uploadBtn',iconCls:'yesIcon',handler:this.startUpload,scope:this},'-',
            {text:'停止上传',ref:'../stopBtn',iconCls:'cancelIcon',handler:this.stopUpload,scope:this,disabled:true},'-',
            {text:'全部移除',ref:'../deleteBtn',iconCls:'dustbinIcon',handler:this.deleteAll,scope:this}
        ],
        layout : 'fit',
        items : [this.gp],
        listeners : {
            'afterrender':function(){
                var em = this.getTopToolbar().get(0).el.child('em');
                var placeHolderId = Ext.id();
                em.setStyle({
                    position : 'relative',
                    display : 'block'
                });
                em.createChild({
                    tag : 'div',
                    id : placeHolderId
                });
                this.swfupload = new SWFUpload(Ext.apply(this.setting,{
                    button_width : em.getWidth(),
                    button_height : em.getHeight(),
                    button_placeholder_id :placeHolderId
                }));
                this.swfupload.uploadStopped = false;
                Ext.get(this.swfupload.movieName).setStyle({
                    position : 'absolute',
                    top : 0,
                    left : -2
                });             
            },
            scope : this,
            delay : 100
        }
    });
}
//文件上传窗口组件继承自Ext.Panel
Ext.extend(Attachment.UploadPanel,Ext.Panel,{
    toggleBtn :function(bl){
        this.addBtn.setDisabled(bl);
        this.uploadBtn.setDisabled(bl);
        this.deleteBtn.setDisabled(bl);
        this.stopBtn.setDisabled(!bl);
        this.gp.getColumnModel().setHidden(6,bl);
    },
    onUploadStart : function(file) {
       var post_params = this.settings.post_params;  
       Ext.apply(post_params,{
            //在此添加动态参数
            'entity.attachmentKeyIDX': Attachment.entityJson.attachmentKeyIDX,                //所属业务表记录的idx主键
            'entity.attachmentKeyName': Attachment.entityJson.attachmentKeyName               //所属业务表名称
            //动态传参数，解决文件名中文乱码问题 
//            fileName : encodeURIComponent(file.name)
       });  
       this.setPostParams(post_params);  
    },
    startUpload : function() {
        if (this.swfupload) {
            if (this.swfupload.getStats().files_queued > 0) {
                this.swfupload.uploadStopped = false;
                this.toggleBtn(true);
                this.swfupload.startUpload();
            }
        }
    },
    formatFileSize : function(_v, celmeta, record) {
        return Ext.util.Format.fileSize(_v);
    },
    //文件状态
    formatFileState : function(n){
        switch(n){
            case -1 : return '未上传';//队列中
            break;
            case -2 : return '正在上传';//上传中
            break;
            case -3 : return '<div style="color:red;">上传失败</div>';//错误
            break;
            case -4 : return '上传成功';//失败
            break;
            case -5 : return '取消上传';//已取消
            break;
            default: return n;
        }
    },
    formatProgressBar : function(v){
        if(this.getTplStr == null){
            return this.scope.getTplStr(v);
        } else {
            return this.getTplStr(v);
        }
//        var progressBarTmp = this.scope.getTplStr(v);
//        return progressBarTmp;
    },
    getTplStr : function(v){
        var bgColor = "orange";
        var borderColor = "#008000";
        return String.format(
            '<div>'+
                '<div style="border:1px solid {0};height:10px;width:{1}px;margin:4px 0px 1px 0px;float:left;">'+        
                    '<div style="float:left;background:{2};width:{3}%;height:10px;"><div></div></div>'+
                '</div>'+
            '<div style="text-align:center;float:right;width:40px;margin:3px 0px 1px 0px;height:10px;font-size:12px;">{3}%</div>'+          
        '</div>', borderColor,(90),bgColor, v);
    },
    onUploadComplete : function(file) {
        var me = this.customSettings.scope_handler;
        if(file.filestatus==-4){
            var ds = me.gp.store;
            for(var i=0;i<ds.getCount();i++){
                var record =ds.getAt(i);
                if(record.get('id')==file.id){
                    record.set('percent', 100);
                    if(record.get('state')!=-3){
                        record.set('state', file.filestatus);
                    }
                    record.commit();
                }
            }
        }
        if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
            this.startUpload();
        }else{          
//        上传成功后刷新附件列表
            Attachment.grid.store.reload();
            me.toggleBtn(false);
            me.linkBtnEvent();
        }       
    },
    onFileQueued : function(file) {
        var me = this.customSettings.scope_handler;
        var rec = new Ext.data.Record({
            id : file.id,
            name : file.name,
            size : file.size,
            type : file.type,
            state : file.filestatus,
            percent : 0
        })
        me.gp.getStore().add(rec);
    },
    onUploadSuccess : function(file, serverData) {
        var me = this.customSettings.scope_handler;
        var ds = me.gp.store;
        if (Ext.util.JSON.decode(serverData).success) {         
            for(var i=0;i<ds.getCount();i++){
                var rec =ds.getAt(i);
                if(rec.get('id')==file.id){
                    rec.set('state', file.filestatus);
                    rec.commit();
                }
            }           
        }else{
            for(var i=0;i<ds.getCount();i++){
                var rec =ds.getAt(i);
                if(rec.get('id')==file.id){
                    rec.set('percent', 0);
                    rec.set('state', -3);
                    rec.commit();
                }
            }
        }
        me.linkBtnEvent();
    },
    uploadProgress : function(file, bytesComplete, totalBytes){     //处理进度条
        var me = this.customSettings.scope_handler;
        var percent = Math.ceil((bytesComplete / totalBytes) * 100);
        percent = percent == 100? 99 : percent;
        var ds = me.gp.store;
        for(var i=0;i<ds.getCount();i++){
            var record =ds.getAt(i);
            if(record.get('id')==file.id){
                record.set('percent', percent);
                record.set('state', file.filestatus);
                record.commit();
            }
        }
    },
    onUploadError : function(file, errorCode, message) {
        var me = this.customSettings.scope_handler;
        me.linkBtnEvent();
        var ds = me.gp.store;
        for(var i=0;i<ds.getCount();i++){
            var rec =ds.getAt(i);
            if(rec.get('id')==file.id){
                rec.set('percent', 0);
                rec.set('state', file.filestatus);
                rec.commit();
            }
        }
    },
    onFileError : function(file,n){
        switch(n){
            case -100 : tip('待上传文件列表数量超限，不能选择！');
            break;
            case -110 : tip('上传文件最大不能超过' + Attachment.uploadMaxSize + 'MB！');
            break;
            case -120 : tip('该文件大小为0，不能选择！');
            break;
            case -130 : tip('该文件类型不可以上传！');
            break;
        }
        function tip(msg){
            Ext.Msg.show({
                title : '提示',
                msg : msg,
                width : 280,
                icon : Ext.Msg.WARNING,
                buttons :Ext.Msg.OK
            });
        }
    },
    onDiaogComplete : function(){
        var me = this.customSettings.scope_handler;
        me.linkBtnEvent();
    },        
    stopUpload : function() {
        if (this.swfupload) {
            this.swfupload.uploadStopped = true;
            this.swfupload.stopUpload();
        }
    },
    deleteAll : function(){
        var ds = this.gp.store;
        for(var i=0;i<ds.getCount();i++){
            var record =ds.getAt(i);
            var file_id = record.get('id');
            this.swfupload.cancelUpload(file_id,false);  
        }
        ds.removeAll();
        this.swfupload.uploadStopped = false;
    },
    formatDelBtn : function(v){
        return "<a href='#' id='"+v+"'  style='color:blue' class='link-btn' ext:qtip='移除该文件'>移除</a>";
    },
    linkBtnEvent : function(){
        Ext.select('a.link-btn',false,this.gp.el.dom).on('click',function(o,e){
            var ds = this.gp.store;
            for(var i=0;i<ds.getCount();i++){
                var rec =ds.getAt(i);
                if(rec.get('id')==e.id){
                    ds.remove(rec);
                }
            }
            this.swfupload.cancelUpload(e.id,false);
        },this);
    }
});
//在ext中注册上传组件
Ext.reg('uploadPanel',Attachment.UploadPanel);
//创建文件上传窗口
Attachment.uploadWin = new Ext.Window({
    title:'上传文件', width:600, height:350, layout:'fit', closeAction:'hide', modal:true, maximizable:true, buttonAlign:'center',
    buttons: [{
        text:"关闭", iconCls:"closeIcon", scope:this, handler:function(){ Attachment.uploadWin.hide(); }
    }],    
    items : [{
        xtype:'uploadPanel', border : false, 
        fileSize : 1024*64,                                     //限制文件大小
        uploadUrl : ctx + '/attachment!swfUpload.action',       //上传文件请求地址
        flashUrl : ctx + '/frame/resources/SWFUpload/swfupload.swf',     //swfupload.swf文件路径
        filePostName : 'file',                                  //后台接收参数
        fileTypes : '*.*',                                      //可上传文件类型
        postParams : {savePath:'upload\\'}                      //上传文件存放目录
    }]
});
//附件列表查询参数对象 
Attachment.entityJson = {};
//临时存放附件列表关联业务表名
Attachment.tmpattachmentKeyName = null;
/**
 * 说明：创建附件列表对象并返回
 * @return Ext.yunda.Grid Attachment.grid
 */
Attachment.createGrid = function(){
    Attachment.grid = new Ext.yunda.Grid({
        loadURL: ctx + '/attachment!pageList.action',                 //装载列表数据的请求URL
        saveURL: ctx + '/',                                           //保存数据的请求URL
        deleteURL: ctx + '/attachment!del.action',                    //删除数据的请求URL
        storeAutoLoad: false,
        tbar: ['search','add','delete'],
        fields: [{
            header:'idx主键', dataIndex:'idx', hidden:true, editor: { xtype:'hidden' }
        },{
            header:'附件保存名称', dataIndex:'attachmentSaveName', hidden:true
        },{
            header:'文件名称', dataIndex:'attachmentRealName', 
            renderer: function(v, celmeta, record){
                var url = ctx + '/attachment!download.action' + '?id=' + record.get("idx");
                return ['<a target="_blank" href="', url, '">', v, '</a>'].join('');        
            },
            editor:{  maxLength:100 }
        },{
            header:'大小', dataIndex:'attachmentSize', 
            renderer: function(v, celmeta, record){ return Ext.util.Format.fileSize(v); },
            editor:{ xtype:'hidden' }
        },{
            header:'附件所属的关键值', dataIndex:'attachmentKeyIDX', hidden:true
        },{
            header:'附件所属的业务表单', dataIndex:'attachmentKeyName', hidden:true
        },{
            header:'文件类型', dataIndex:'fileType', editor:{  maxLength:100 }
        },{
            header:'类型', dataIndex:'attachmentClass', hidden:true, editor:{ xtype:'hidden' }
        },{
            header:'上传人', dataIndex:'uploadPerson', hidden:true, editor:{ xtype:'hidden'}
        },{
            header:'上传人', dataIndex:'uploadPersonName', editor:{  maxLength:25 }
        },{
            header:'上传时间', dataIndex:'uploadTime', xtype:'datecolumn', format:'Y-m-d H:i', editor:{ xtype:'hidden' }, searcher:{disabled:true}
        }],
        searchFn: function(searchParam){
            Ext.apply(searchParam, Attachment.entityJson);
            this.store.load({
                params: { entityJson: Ext.util.JSON.encode(searchParam) }       
            }); 
        },        
        toEditFn: function(){},
        addButtonFn: function(){ 
        	Attachment.uploadWin.show();
        	Attachment.uploadWin.items.items[0].gp.store.removeAll();
        }
    });
    return Attachment.grid;
}
/**
 * 说明：创建附件列表窗体，首先判断Attachment.win，如果为空则实例化一个附件窗体赋值给Attachment.win并返回，如果不为空直接返回Attachment.win
 * @return Ext.Window Attachment.win
 */
Attachment.createWin = function(cfg){
    if (Attachment.win != null) return Attachment.win;
    //若窗体为空，先创建附件表格，再创建附件窗口，并将附件表格放在窗口中
    Attachment.createGrid();
    var w = cfg == null ? 650 : (cfg.width || 650);
    var h = cfg == null ? 400 : (cfg.height || 400);
    //实例化附件窗体
    Attachment.win = new Ext.Window({
	    title:'附件列表', width:w, height:h, layout:'fit', closeAction:'hide', modal:true, maximizable:true, buttonAlign:'center', items:Attachment.grid,
        buttons: [{
            text:"关闭", iconCls:"closeIcon", handler:function(){ Attachment.win.hide(); }
        }]
    });
    return Attachment.win;
}
//附件列表查询的基本参数
Attachment.setBaseParams = function(){
    Attachment.grid.store.baseParams.entityJson = Ext.util.JSON.encode(Attachment.entityJson);
}
/**
 * 生成表格中附件列配置的json数据 
 * @param {} cfg
 * {
 *   attachmentKeyName:     业务表名称
 *   disableButton:         要禁用的按钮，如['新增']
 * }
 * @return {} json
 */
Attachment.createColModeJson = function(cfg){
    if(cfg == null || cfg.attachmentKeyName == null)   return {};
    var showCfg = {};
    showCfg.attachmentKeyName = cfg.attachmentKeyName;
    showCfg.disableButton = cfg.disableButton;
    var attachmentKeyIDX = "idx" ; //默认主键 
    if(!Ext.isEmpty(cfg.attachmentKeyIDX)) attachmentKeyIDX = cfg.attachmentKeyIDX ; //手动配置主键
    var col = {
        header:'附件', width:45, scope:col, attachmentKeyName:cfg.attachmentKeyName, renderer: function(value, metadata, record, rowIndex, colIndex, store){
            showCfg.attachmentKeyIDX = record.get(attachmentKeyIDX);
            var html = '<img src="' + ctx 
                + '/frame/resources/images/toolbar/attach.gif" border="0" style="cursor:hand" onclick=Attachment.showInWin(' + Ext.util.JSON.encode(showCfg) + ')>';
            return html;
        }
    }
    delete cfg.renderer;
    delete cfg.attachmentKeyName;
    delete cfg.attachmentKeyIDX;
    delete cfg.disableButton;
    return Ext.apply(col, cfg);
}
/**
 * 显示附件列表窗体
 * @param {} cfg 配置业务表、idx主键参数，同时也会作为基本查询参数
 * {
 *   attachmentKeyName: 业务表名称
 *   attachmentKeyIDX： 业务表记录主键idx
 *   disableButton：     要禁用的按钮，如['新增']
 * }
 * @return void
 */
Attachment.showInWin = function(cfg){
    if(Attachment.win == null)    Attachment.createWin();
    Attachment.processShow(cfg);
    Attachment.win.show();
}
/**
 * 在Tab中显示附件列表
 * @param {} cfg 配置业务表、idx主键参数，同时也会作为基本查询参数
 * {
 *   attachmentKeyName：业务表名称
 *   attachmentKeyIDX： 业务表记录主键idx
 *   disableButton：    要禁用的按钮，如['新增']
 * }
 * @return void
 */
Attachment.showInTab = function(cfg){
    if(Attachment.grid == null)    Attachment.createGrid();
    Attachment.processShow(cfg);
}
/**
 * private私用方法，外部不应调用和覆盖，显示grid前的参数处理
 * @param {} cfg
 * {
 *   attachmentKeyName：业务表名称
 *   attachmentKeyIDX： 业务表记录主键idx
 *   disableButton：    要禁用的按钮，如['新增']
 * } 
 * @return {}
 */
Attachment.processShow = function(cfg){
    if(cfg.disableButton != null)   Attachment.disableButton(cfg.disableButton);
    Attachment.entityJson.attachmentKeyName = cfg.attachmentKeyName;
    Attachment.entityJson.attachmentKeyIDX = cfg.attachmentKeyIDX;
    Attachment.grid.store.un('beforeload', Attachment.setBaseParams);
    Attachment.grid.store.on('beforeload', Attachment.setBaseParams);
    Attachment.grid.store.load();
}
/**
 * 生成Tab中子项配置的json数据 
 * @param {} cfg Tab.Items的配置项
 * @return {} json
 */
Attachment.createTabJson = function(cfg){
    var tabItem = { title:"附件", layout:"fit", items:Attachment.createGrid() };
    return Ext.apply(tabItem, cfg);
}
/**
 * 使附件列表工具栏上的功能按钮成为不可用状态，根据入参text来定位按钮
 * @param [] texts 按钮上的文本值数组，如['新增','删除']
 * @return void
 */
Attachment.disableButton = function(texts){
    if(texts == null || texts.length < 1)   return;
    for (var i = 0; i < texts.length; i++) {
        Attachment.grid.topToolbar.find('text',texts[ i ])[ 0 ].disable();
    }
}
/**
 * 使附件列表工具栏上的功能按钮成为可用状态，根据入参text来定位按钮
 * @param {} texts 按钮上的文本值数组，如['新增','删除']
 * @return void
 */
Attachment.enableButton = function(texts){
    if(texts == null || texts.length < 1)   return;
    for (var i = 0; i < texts.length; i++) {
        Attachment.grid.topToolbar.find('text',texts[ i ])[ 0 ].enable();
    }    
}