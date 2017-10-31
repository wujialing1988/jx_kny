Ext.namespace("Ext.yunda");
Ext.yunda.caret = 0;//文本光标位置（在文本字符的第几个字符）
/**
 * texts: ['￥','＄','℃'],
 * @class Ext.yunda.InsertCharField
 * @extends Ext.form.TextField
 */
Ext.yunda.InsertCharField = Ext.extend(Ext.form.TextField, {
    colWidth: 10,
//    texts: ['￥','＄','℃'],
    isNumber: false,//是否是数字型文本框，true是，不显示特殊字符选择按钮；false否，默认显示特殊字符按钮
    textPicker: null,
    //初始化组件
    initComponent : function() {
        Ext.yunda.InsertCharField.superclass.initComponent.call(this);
        if(this.id == null || this.id.trim() == '') this.id = Ext.id();
        this.on('focus', function(this_){
        	if(this.isNumber) return;
            if(this_.texts == null)   return;
            if(this_.textPicker == null)  this_.createTextPicker();
            var xy = this_.el.getXY();
            this_.textPicker.setXY([xy[0], xy[1] + 20]);
            this_.textPicker.show();
            //对文本框添加click事件，鼠标点击时记录文本位置
            this_.el.dom.onclick = function(){
            	Ext.yunda.InsertCharField.setCaret(this);
            }
            //对文本框添加keyup事件，鼠标点击时记录文本位置
            this_.el.dom.onkeyup = function(){
            	Ext.yunda.InsertCharField.setCaret(this);
            }
        });
        this.on('blur', function(this_){ 
        	if(this.isNumber) return;
            if(document.activeElement === this_.el.dom) return;
            if(document.activeElement.parentNode === this_.textPicker.dom) return;
            this_.textPicker.hide();  
            
        });
    },
    //销毁组件，释放资源
    destroy : function() {
        Ext.yunda.InsertCharField.superclass.destroy.apply(this,arguments); 
    },
    createTextPicker: function(){
        var len = this.texts.length;
        var buttons = [];
        for (var i = 0; i < len; i++) {
            buttons.push(['<button style="width:33", onclick=Ext.yunda.InsertCharField.insertText("', this.id, '")>', this.texts[ i ], '</button>'].join(''));
            if(i > 0 && ((i+1) % 10 == 0))  buttons.push('<br/>');
        }
//        var picker = ['<div id=', Ext.id(), '>', buttons.join(''), '</div>'].join('');
        var picker = buttons.join('');
        this.textPicker = Ext.DomHelper.append(document.body, {
            html: picker
        }, true);
        this.pickerID = this.textPicker.id;
        this.textPicker.dom.style.zIndex = 99999;
    }
});
//获取光标位置
Ext.yunda.InsertCharField.setCaret = function(textObj){
	if (textObj.createTextRange) {
		textObj.caretPos = document.selection.createRange().duplicate(); 
		textObj.caretPos.setEndPoint("StartToStart",textObj.createTextRange());
		Ext.yunda.caret = textObj.caretPos.text.length;
	} 
} 
//插入特殊字符
Ext.yunda.InsertCharField.insertText = function(cmpid){
    var ch = event.srcElement.innerText.trim();
    var inputText = Ext.getCmp(cmpid).el.dom;
    inputText.focus();
    var value = inputText.value;
    inputText.value = value.substr(0,Ext.yunda.caret) + ch + value.substr(Ext.yunda.caret);
    Ext.yunda.caret = inputText.value.length;
    inputText.focus();        
}
Ext.reg("insertCharField",Ext.yunda.InsertCharField);