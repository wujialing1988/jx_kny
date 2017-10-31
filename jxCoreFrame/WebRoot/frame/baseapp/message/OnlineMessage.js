//判断对象是否为数组类型
function isArray(o) {   return Object.prototype.toString.call(o) === '[object Array]';  }

Ext.ns("yd"); 
/**消息记录 Message
{
    idx,        // String idx主键 
    sender,     // Long 发送者empid，-1表示发送者为系统
    senderName, // String 发送者姓名
    sendTime,   // Date 发送时间
    receiver,   // Long 接收者empid
    receiverName,   // String 接收者姓名
    receiveTime,    // Date 接收时间 
    content,    // String 内容 
    pageTitle,  // String 链接页面标题
    url,        // String 链接页面地址 
    showPageMode    // Int 显示url链接页面的方式，1在选项卡tab中打开，2打开新的独立页面    
}*/
/**
 * 消息面板
 * @param {} cfg
 * {
 *  soundSrc 消息提示音文件路径，如：ctx + '/frame/resources/images/sound/msg.mp3'
 *  iconCls 消息面板标题栏图标
 *  width 消息面板宽度
 *  height 消息面板高度
 * }
 */
yd.MessagePanel = function(cfg){
    this.iconCls = 'bellIcon';
    this.width = 300; 
    this.height = 200;
    Ext.apply(this, cfg);
    this.sound = new yd.BackgroundSound(this.soundSrc);
    this.cfgWin = {
        iconCls:this.iconCls, width:this.width, height:this.height, plain:true, draggable:false, autoScroll:"overflow:'auto'", style:"padding:3px"
    };
    this.show = function(msgs){
        if(msgs == null || msgs.length == null || msgs.length < 1)  return;
        
        var title = '新的消息(' + msgs.length + ')';
        var content = this.createRows(msgs);
        var cfg = Ext.apply({
            title:title, html:content,
            x:document.body.clientWidth - (this.width + 20), y:document.body.clientHeight - (this.height + 20)
            }, this.cfgWin);
        yd.MessagePanel.win = new Ext.Window(cfg);
        yd.MessagePanel.win.show();
        this.sound.play();
        var el = yd.MessagePanel.win.getEl();
//        el.slideIn('br', {
//            duration: 1
//        }); 
//        el.frame("C3DAF9", 2, { duration: 1 });
//        el.frame("ff0000", 2, { duration: 1 });
        el.frame("FF8000", 2, { duration: 1 });
    };
    this.createRows = function(msgs){
        if(msgs == null || msgs.length == null || msgs.length < 1) return '';
        var html = '';
        for (var i = 0; i < msgs.length; i++) {
            html += this.createRow(msgs[ i ]);
        }
        return html;
    };
    this.createRow = function(msg){
        if(msg == null)    return null;
        var content = '';
        if(msg.url == null || msg.url.trim() == '') {
            content = msg.content;
        } else {
            var showFun = 'javascript:void(0)';
            if(msg.showPageMode == 2){
                showFun = ['window.open("', ctx, msg.url, '","_blank")'].join('');
            } else {
                showFun = ['OpenPage("', msg.pageTitle,'","', msg.pageTitle, '","', msg.url, '")'].join('');
            }
            content = ['<a href=# onclick=', showFun, '>', msg.content, '</a>'].join('');
        }
        content = [msg.senderName, '&nbsp;&nbsp;', msg.sendTime, '<br/>&nbsp;&nbsp;&nbsp;', content, '<br/>'].join('');
        var html = content;
        return html;
    };    
}
//背景声音对象
yd.BackgroundSound = function(src){
    this.src = src;
//    初始化
    if(typeof(Audio) != 'undefined'){
        this.audio = new Audio(this.src);
    } else {
        this.bgSound = document.createElement("bgsound");
        document.body.appendChild(this.bgSound);
    }    
//    播放声音
    this.play = function(){
        if(this.audio)  this.audio.play();
        else this.bgSound.src = this.src;
    };
}

Ext.ns("yd.msgMgr");
//打开消息管理页面
yd.msgMgr.openPage = function(){
    OpenPage('消息管理','消息管理','/frame/baseapp/message/Message.jsp');
}
//首页消息管理器初始化动作
yd.msgMgr.init = function(){
    yd.msgMgr.failCount = 0;
//    初始化消息面板    
    yd.msgMgr.msgPanel = new yd.MessagePanel({soundSrc:ctx + '/frame/resources/images/sound/msg.mp3'});    
    document.body.onresize = function(){
        if(yd.MessagePanel.win == null) return;
        if(yd.MessagePanel.win.isVisible()) {
            var x = document.body.clientWidth - (yd.msgMgr.msgPanel.width + 20);
            var y = document.body.clientHeight - (yd.msgMgr.msgPanel.height + 20);
            yd.MessagePanel.win.setPosition(x, y);
        }
    };
//    进入首页3秒后开始执行消息轮询
    var delay = new Ext.util.DelayedTask(yd.msgMgr.polling);
    delay.delay(3000);
}
//消息轮询
yd.msgMgr.polling = function(){
    yd.msgMgr.pollTask = {
        run: function(){
            if(yd.msgMgr.failCount > 2){
                Ext.TaskMgr.stop(yd.msgMgr.pollTask);
                return;
            }
            Ext.Ajax.request({
                url: ctx + '/message!getNoReceivedMsg.action',
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
                    if(result.success != true){         //操作失败
                        yd.msgMgr.failCount += 1;
                        return;
                    }
                    if(yd.msgMgr.failCount > 0) yd.msgMgr.failCount -= 1;
                    var msgs = result.root;
                    if(msgs == null || msgs.length < 1)    return;
			        for (var i = 0; i < msgs.length; i++) {
			            msgs[ i ].sendTime = new Date(msgs[ i ].sendTime).format('Y-m-d H:i:s')
			        }
			        yd.msgMgr.msgPanel.show(msgs);
			        yd.msgMgr.afterShowInfo(msgs);                        
		        },
		        //请求失败后的回调函数
		        failure: function(response, options){
                    yd.msgMgr.failCount += 1;
		        }            
            });
        },
        interval: 60000 //60秒一次
    }
    Ext.TaskMgr.start(yd.msgMgr.pollTask);
}

//在显示消息框后出发的动作，向服务器端发出请求表示已接收到该消息，写入接收时间
yd.msgMgr.afterShowInfo = function(message){
    var idxs = [];
    for (var i = 0; i < message.length; i++) {
        idxs.push(message[ i ].idx);
    }
    Ext.Ajax.request({
        url: ctx + '/message!received.action',
        params: {ids: idxs}
    });
}
//初始化并开启在线消息服务
Ext.onReady(function(){
//    如果不想开启在线消息，可以注释掉下面这行代码
    yd.msgMgr.init();
});
