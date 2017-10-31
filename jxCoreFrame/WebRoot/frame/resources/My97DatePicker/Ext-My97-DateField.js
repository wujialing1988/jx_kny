Ext.namespace("Ext.yunda");
//Ext时间格式：年-月-日，如：2012-12-25
Ext.yunda.EXT_YMD = "Y-m-d";
//Ext时间格式：年-月-日 时:分:秒，如：2012-12-25 13:59:59
Ext.yunda.EXT_YMDHMS = "Y-m-d H:i:s";
//Ext时间格式：年-月-日 时:分，如：2012-12-25 13:59
Ext.yunda.EXT_YMDHM = "Y-m-d H:i";
//Ext时间格式：年-月-日 时:分，如：2012-12-25 13
Ext.yunda.EXT_YMDH = "Y-m-d H";
//My97时间格式：年-月-日，如：2012-12-25
Ext.yunda.MY97_YMD = "yyyy-MM-dd";
//My97时间格式：年-月-日 时:分:秒，如：2012-12-25 13:59:59
Ext.yunda.MY97_YMDHMS = "yyyy-MM-dd HH:mm:ss";
//My97时间格式：年-月-日 时:分，如：2012-12-25 13:59
Ext.yunda.MY97_YMDHM = "yyyy-MM-dd HH:mm";
//My97时间格式：年-月-日 时，如：2012-12-25 13
Ext.yunda.MY97_YMDH = "yyyy-MM-dd HH";
/**
 * 在Extjs中集成My97DatePicker，该组件继承Ext.form.DateField，控件使用及接口与Ext.form.DateField类似。
 * 配置项略有不同，xtype（必须是'my97date'）, my97cfg（My97DatePicker控件自身的配置项，具体属性参考My97DatePicker帮助文档）。
 * 代码示例：{ xtype:'my97date', my97cfg: {dateFmt:'yyyy-MM-dd HH:mm:ss'}}
 * @class Ext.yunda.My97DatePicker
 * @extends Ext.form.TextField
 */
Ext.yunda.My97DatePicker = Ext.extend(Ext.form.DateField, {
    format : "Y-m-d",                                       //默认日期格式，年-月-日
    initNow: true,                                          //默认true，使用当前时间初始化控件值
    altFormats : "m/d/Y|n/j/Y|n/j/y|m/j/y|n/d/y|m/j/Y|n/d/Y|m-d-y|m-d-Y|m/d|m-d|md|mdy|mdY|d|Y-m-d|n-j|n/j", 
    //初始化组件
    initComponent : function() {
        if(this.my97cfg){
            if(this.my97cfg.dateFmt == Ext.yunda.MY97_YMD)  this.format = Ext.yunda.EXT_YMD;
            if(this.my97cfg.dateFmt == Ext.yunda.MY97_YMDHMS) this.format = Ext.yunda.EXT_YMDHMS;
            if(this.my97cfg.dateFmt == Ext.yunda.MY97_YMDHM) this.format = Ext.yunda.EXT_YMDHM;
            if(this.my97cfg.dateFmt == Ext.yunda.MY97_YMDH) this.format = Ext.yunda.EXT_YMDH;
        } else if(this.format == Ext.yunda.EXT_YMDHMS){
            this.my97cfg = this.my97cfg || {};
            this.my97cfg.dateFmt = Ext.yunda.MY97_YMDHMS;
        } else if(this.format == Ext.yunda.EXT_YMDHM){
            this.my97cfg = this.my97cfg || {};
            this.my97cfg.dateFmt = Ext.yunda.MY97_YMDHM;
        } else if(this.format == Ext.yunda.EXT_YMDH){
            this.my97cfg = this.my97cfg || {};
            this.my97cfg.dateFmt = Ext.yunda.MY97_YMDH;         
        }
        //设置初始化默认日期值
        if(this.initNow && (this.value == null || this.value.trim() == ''))  this.value = new Date().format(this.format);
        Ext.yunda.My97DatePicker.superclass.initComponent.call(this);
        this.on("render", function(this_){
//            this.getEl().on('click', function(){
//                WdatePicker(this_.my97cfg || {});
//            });
//            查找父容器xtype为window，注册事件：当父容器window关闭时隐藏my97控件
            try{
                this_.el.dom.onclick = function(){WdatePicker(this_.my97cfg || {});};
	            var ownerCt = this_;
	            while (ownerCt) {
	                ownerCt = ownerCt.ownerCt;
                    if(ownerCt == null) break;
	                if('window' != ownerCt.getXType())   continue;
	                ownerCt.on('hide', function(_win){
	                    try{
	                        $dp.hide();    
	                    } catch(e){}
	                    return true;
	                });                
	                ownerCt.on('close', function(_win){
	                    try{
	                        $dp.hide();    
	                    } catch(e){}
	                    return true;
	                });
	                break;
	            }
            } catch(e){}
        });

    },
	onTriggerClick : function(){
        if(this.disabled){
            return;
        }
        if(this.el.dom.onclick == null){
            this.el.dom.onclick = function(){WdatePicker(this_.my97cfg || {});};
        }
        this.el.dom.click();
	},
    //取值，直接返回文本框中的字符串
    getValueText: function(){
        return Ext.yunda.My97DatePicker.superclass.getValue.call(this) || "";
    },
    //基于安全的解析方式
    safeParse : function(value, format) {
        if (Date.formatContainsHourInfo(format)) {
            // if parse format contains hour information, no DST adjustment is necessary
            return Date.parseDate(value, format);
        } else {
            // set time to 12 noon, then clear the time
            var parsedDate = Date.parseDate(value, format);
            if (parsedDate) {
                return parsedDate.clearTime();
            }
        }
    }
    // PUBLIC -- to be documented
//    safeParse : function(value, format) {
//        if (Date.formatContainsHourInfo(format)) {
//            // if parse format contains hour information, no DST adjustment is necessary
//            return Date.parseDate(value, format);
//        } else {
//            // set time to 12 noon, then clear the time
//            var parsedDate = Date.parseDate(value + ' ' + this.initTime, format + ' ' + this.initTimeFormat);
// 
//            if (parsedDate) {
//                return parsedDate.clearTime();
//            }
//        }
//    }  
});
Ext.reg("my97date",Ext.yunda.My97DatePicker);