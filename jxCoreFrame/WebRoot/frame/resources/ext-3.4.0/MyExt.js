/**
 * 改JS文件包含了基本的对Extjs的修正补充，封装常用的方法
 */
Ext.BLANK_IMAGE_URL = ctx + '/frame/resources/ext-3.4.0/resources/images/default/s.gif';
//初始化显示提示信息，没有它提示信息出不来。
Ext.QuickTips.init();
//修改这里的值msgTarget:"title"，msgTarget:"under"，msgTarget:"side"
Ext.form.Field.prototype.msgTarget = "title";
Ext.namespace("MyExt.Msg");
/**
 * 该消息提示框根据Ext demo 中的 Ext.example修改而成，参考网上其他实现
 * MyExt.TopMsg.msg('标题', '信息');                //不自动隐藏
 * MyExt.TopMsg.msg('标题', '信息',true);           //默认3秒后自动隐藏
 * MyExt.TopMsg.msg('标题', '信息',true,5);         //5秒后自动隐藏
 */
MyExt.TopMsg = function() {
    var msgCt;
    function createBox(t, s, icon) {
        icon = icon || 'del-red.gif';
        return [
                '<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc" style="font-size=12px;"><h3>',t,
                '<span class="msg-close" style="cursor:hand;position:absolute;right:10px" onclick="MyExt.TopMsg.hide(this);">',
                '<img src="' + ctx + '/frame/resources/images/toolbar/' + icon + '"/></span>',                
                '</h3>',
                s,'</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
        msg : function(title, message, autoHide, pauseTime, icon) {
            if (!msgCt) {
                msgCt = Ext.DomHelper.insertFirst(document.body, {
                    id : 'topmsg-div',
                    style : 'position:absolute;top:10px;width:250px;margin:0 auto;z-index:99999;'
                }, true);
            }
            msgCt.alignTo(document, 't-t');
            var m = Ext.DomHelper.append(msgCt, { html : createBox(title, message, icon) }, true);
            m.slideIn('t');
            if (!Ext.isEmpty(autoHide) && autoHide == true) {
                if (Ext.isEmpty(pauseTime)) pauseTime = 3;
                m.pause(pauseTime).ghost('t', { duration : 2, remove : true, useDisplay : false });
            }
        },
        successMsg: function(msg){
            this.msg(i18n.common.tip.prompt, msg||i18n.common.tip.operateSuccess, true, 1, 'check.png');
        },
        failMsg: function(errMsg){
            var message = '<font color="red">'+i18n.common.tip.operateFail+'</br>' + errMsg + '</font>';
            this.msg(i18n.common.tip.prompt, message, true, 3, 'del-red.gif');
        },
        exceptionMsg: function(exMsg){
            var message = '<font color="red">'+i18n.common.tip.operateException+'</br>' + exMsg + '</font>';
            this.msg(i18n.common.tip.prompt, message, true, 3, 'del-red.gif');            
        },
        hide : function(v) {
            var msg = Ext.get(v.parentElement.parentElement.parentElement.parentElement.parentElement);
            msg.ghost("t", { remove : true });
        }
    };
}();
//简单封装alert
MyExt.Msg.alert = function(msg){
    MyExt.TopMsg.msg(i18n.common.tip.prompt, msg, true, 1);
}
//全局方法，操作成功、失败消息提示框
window.alertSuccess = function(tip){ MyExt.TopMsg.successMsg(tip); }
window.alertFail = function(msg){
	var errMsg = "";
	if(msg instanceof Array){
		for(var i = 0; i < msg.length; i++){
			errMsg += (i + 1) + ". " + msg[ i ] + "</br>";
		}
	} else {
		errMsg = msg;
	}
    MyExt.TopMsg.failMsg(errMsg);
}
//全局方法，异常信息提示
window.alertException = function(msg){ MyExt.TopMsg.exceptionMsg(msg); }
//设置extjs的数据字段校验，在此处设置了多种通用的数据校验类型
Ext.apply(Ext.form.VTypes, {
    //年龄的数据格式验证
    "age": function(_v){
        if(/^\d+$/.test(_v)){
            try{      
                if(parseInt(_v) >= 1 && parseInt(_v) <= 120)      
                    return true;      
                return false;      
            } catch(err){      
                return false;      
            }
        }
        return false;
    },
    "ageText": i18n.common.verification.ageText,
    "ageMask": /[0-9]/,
    //邮编
    "postalcode": function(_v){
        return /^[0-9]\d{5}$/.test(_v);
    },
    "postalcodeText": i18n.common.verification.postalcodeText,
    "postalcodeMask": /[0-9]/,
    //座机电话
    "telphone": function(_v){
        return /(^\d{3}\-\d{7,8}$)|(^\d{4}\-\d{7,8}$)|(^\d{3}\d{7,8}$)|(^\d{4}\d{7,8}$)|(^\d{7,8}$)/.test(_v);
    },
    "telphoneText": i18n.common.verification.telphoneText,
    "telphoneMask": /[0-9\-]/,
    //手机号码
    "mobile": function(_v){
        return /^1[1-9][0-9]\d{8}$/.test(_v);
    },
    "mobileText": i18n.common.verification.mobileText,
    "mobileMask": /[0-9]/,
    //身份证号码
    "IDCard": function(_v){
        return /^[0-9]{17}([0-9]|[Xx])$/.test(_v);
    },
    "IDCardText": i18n.common.verification.IDCardText,
    "IDCardMask": /[0-9]|[Xx]/,

   	positiveInt1To100: function(val,field){ return /^(?:0|[1-9][0-9]?|100)$/.test(val) },
    positiveInt1To100Text: i18n.common.verification.positiveInt1To100Text,
    positiveInt1To100Mask: /[\d]/,  
    chinese: function(val,field){ return /^[\u4e00-\u9fa5]+$/i.test(val) },
    chineseText: i18n.common.verification.chineseText,
    chineseMask: /[\u4e00-\u9fa5]/,
    alpha2: function(val,field){ return /^[a-zA-Z]+$/.test(val) },
    alpha2Text: i18n.common.verification.alpha2Text,
    alpha2Mask: /[a-zA-Z]/,
    upperCase: function(val,field){ return /^[A-Z]+$/.test(val) },
    upperCaseText: i18n.common.verification.upperCaseText,
    upperCaseMask: /[A-Z]/,
    lowerCase: function(val,field){ return /^[a-z]+$/.test(val) },
    lowerCaseText: i18n.common.verification.lowerCaseText,
    lowerCaseMask: /[a-z]/,    
    alphanum2: function(val,field){ return /^[a-zA-Z\d]+$/.test(val) },
    alphanum2Text: i18n.common.verification.alphanum2Text,
    alphanum2Mask: /[a-zA-Z\d]/,
    integer: function(val,field){ return /^[-]?[\d]+$/.test(val) },
    integerText: i18n.common.verification.integerText,
    integerMask: /[-\d]/,
    positiveInt: function(val,field){ return /^[1-9]*[1-9][0-9]*$/.test(val) },
    positiveIntText: i18n.common.verification.positiveIntText,
    positiveIntMask: /[\d]/,
    numberInt: function(val,field){ return /^[0-9]*[0-9][0-9]*$/.test(val) },
    numberIntText: i18n.common.verification.numberIntText,
    numberIntMask: /[\d]/,
    negativeInt: function(val,field){ return /^-[1-9]*[1-9][0-9]*$/.test(val) },
    negativeIntText: i18n.common.verification.negativeIntText,
    negativeIntMask: /[-\d]/,
    nonPositiveInt: function(val,field){ return /^((-\d+)|(0+))$/.test(val) },
    nonPositiveIntText: i18n.common.verification.nonPositiveIntText,
    nonPositiveIntMask: /[-\d]/,
    nonNegativeInt: function(val,field){ return /(^[0-9]?$)|(^[1-9]+[\d]+$)/.test(val) },
    nonNegativeIntText: i18n.common.verification.nonNegativeIntText,
    nonNegativeIntMask: /[\d]/,
    positiveFloat: function(val,field){ return /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test(val) },
    positiveFloatText: i18n.common.verification.positiveFloatText,
    positiveFloatMask: /[\d.]/, 
    nonPositiveFloat: function(val,field){ return /^((-\d+(\.\d+)?)|(0+(\.0+)?))$/.test(val) },
    nonPositiveFloatText: i18n.common.verification.nonPositiveFloatText,
    nonPositiveFloatMask: /[-\d.]/,
    negativeFloat: function(val,field){ return /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/.test(val) },
    negativeFloatText: i18n.common.verification.negativeFloatText,
    negativeFloatMask: /[-\d.]/, 
    nonNegativeFloat: function(val,field){ return /(^[0](\.\d+)?$)|(^[1-9]+[\d]*(\.\d+)?$)/.test(val) },
    nonNegativeFloatText: i18n.common.verification.nonNegativeFloatText,
    nonNegativeFloatMask: /[\d.]/,
    nonNegativeNumber: function(val,field){ return /(^[0](\.\d+)?$)|(^[1-9]+[\d]*(\.\d+)?$)/.test(val) },
    nonNegativeNumberText: i18n.common.verification.nonNegativeNumberText,
    nonNegativeNumberMask: /[\d.]/,
    ip: function(val,field){ return /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(val) },
    ipText:i18n.common.verification.ipText,
    ipMask: /[\d.]/,
    port: function(val,field){ return /^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/.test(val) },
    portText:i18n.common.verification.portText,
    portMask: /[\d]/,    
    qq: function(val,field){ return /^[1-9]*[1-9][0-9]*$/.test(val) },
    qqText:i18n.common.verification.qqText,
    qqMask: /[\d]/,
    //安全账号，用户名
    username: function(val,field){ return /^[a-zA-Z][a-zA-Z0-9_]{4,15}$/.test(val) },
    usernameText:i18n.common.verification.usernameText,
    usernameMask: /[a-zA-Z0-9_]/,
    //有效字符，中文、英文、数字及下划线
    validChar: function(val,field){ return /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(val) },
    validCharText:i18n.common.verification.validCharText,
    validCharMask: /[\u4e00-\u9fa5_a-zA-Z0-9]/,    
    date:function(val,field){
        try{
            var regex = /^(\d{4})-(\d{2})-(\d{2})$/;      
            if(!regex.test(val)) return false;      
            var d = new Date(val.replace(regex, '$1/$2/$3'));      
            return (parseInt(RegExp.$2, 10) == (1+d.getMonth())) && (parseInt(RegExp.$3, 10) == d.getDate())&&(parseInt(RegExp.$1, 10) == d.getFullYear());      
        } catch(e) {      
            return false;      
        }
    }, 
    dateText:i18n.common.verification.dateText,
    dateMask: /[\d-]/,
    //分数
    "score": function(_v){
        if(/^\d+$/.test(_v)){
            try{      
                if(parseInt(_v) >= 0 && parseInt(_v) <= 100)      
                    return true;      
                return false;      
            } catch(err){      
                return false;      
            }
        }
        return false;
    },
    "scoreText": i18n.common.verification.scoreText,
    "scoreMask": /[0-9]/,
    // 颜色码
    "color": function(_v) {
		if (/^#[0-9a-fA-F]{6}$/.test(_v)) return true;
		if (/^#[0-9a-fA-F]{3}$/.test(_v)) return true;
		return false;	
    },
    "colorText": i18n.common.verification.colorText,
    "colorMask": /^[#0-9a-fA-F]/,
    dateRange: function(_v, field) {
		if (field.dateRange) {
			// 开始日期
			var startId = field.dateRange.startDate;
			this.startField = Ext.getCmp(startId);
			var startDate = this.startField.getValue();
			// 结束日期
			var endId = field.dateRange.endDate;
			this.endField = Ext.getCmp(endId);
			var endDate = this.endField.getValue();
			if (Ext.isEmpty(startDate) || Ext.isEmpty(endDate)) {
				return true;
			}
			return startDate <= endDate ? true : false
		}
	},
	// 验证失败信息
	dateRangeText: "开始日期不能大于结束日期"
});
// 修复ExtJS3.2中TextField allowBlank : true 失效的Bug   
Ext.override(Ext.form.TextField, {   
    getErrors : function(value) {
        var errors = Ext.form.TextField.superclass.getErrors.apply(this, arguments);
        value = value || this.processValue(this.getRawValue());   
        if (Ext.isFunction(this.validator)) {   
          var msg = this.validator(value);   
          if (msg !== true) {   
            errors.push(msg);   
          }   
        }   
        if (value.length < 1 || value === this.emptyText) {
            if (this.allowBlank) {   
//                此处修改有问题，直接返回true而忽略了前面validator校验失败，errors可能存在错误信息
//                this.clearInvalid();
//                return true;   
                //改为直接返回errors
                return errors;
            } else {   
                errors.push(this.blankText);   
            }
        }   
        if (value.length < this.minLength) {   
            errors.push(String.format(this.minLengthText, this.minLength));   
        }   
        if (value.length > this.maxLength) {   
            errors.push(String.format(this.maxLengthText, this.maxLength));   
        }   
        if (this.vtype) {   
            var vt = Ext.form.VTypes;   
            if (!vt[this.vtype](value, this)) {   
                errors.push(this.vtypeText || vt[this.vtype + 'Text']);   
            }   
        }   
        if (this.regex && !this.regex.test(value)) {   
            errors.push(this.regexText);   
        }   
        return errors;   
    }   
});
//3.2版本后发现下拉树有些异常：之前展开下来树的下级节点时，下拉菜单不会关掉。
//但是在3.2版本中无论你在弹出的选择框中点击任何部分，包括选中节点、点击空白处以及点击展开下级节点，弹出框都会自动关闭。
// 修复ExtJS3.2中自动关闭下来树的Bug  
Ext.override(Ext.form.ComboBox, {  
    onViewClick : function(doFocus) {  
        var index = this.view.getSelectedIndexes()[0], s = this.store, r = s.getAt(index);  
        if (r) {  
            this.onSelect(r, index);  
        } else if (s.getCount() === 0) {  
            this.collapse();  
        } 
        if (doFocus !== false) {  
            this.el.focus();  
        }
    }
});
//以下代码实现:grid单元格提示+复制
Ext.override(Ext.grid.GridView, {  
    initTemplates : function() {
        var ts = this.templates || {};
		if (!ts.master) {
			ts.master = new Ext.Template(
					'<div class="x-grid3" hidefocus="true">',
					'<div class="x-grid3-viewport">',
					'<div class="x-grid3-header"><div class="x-grid3-header-inner"><div class="x-grid3-header-offset" style="{ostyle}">{header}</div></div><div class="x-clear"></div></div>',
					'<div class="x-grid3-scroller"><div class="x-grid3-body" style="{bstyle}">{body}</div><a href="#" class="x-grid3-focus" tabIndex="-1"></a></div>',
					'</div>',
					'<div class="x-grid3-resize-marker">&#160;</div>',
					'<div class="x-grid3-resize-proxy">&#160;</div>', '</div>');
		}
		if (!ts.header) {
			ts.header = new Ext.Template(
					'<table border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
					'<thead><tr class="x-grid3-hd-row">{cells}</tr></thead>',
					'</table>');
		}
		if (!ts.hcell) {
			ts.hcell = new Ext.Template(
					'<td class="x-grid3-hd x-grid3-cell x-grid3-td-{id} {css}" style="{style}"><div {tooltip} {attr} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">',
					this.grid.enableHdMenu
							? '<a class="x-grid3-hd-btn" href="#"></a>'
							: ' ',
					'{value}<img class="x-grid3-sort-icon" src="',
					Ext.BLANK_IMAGE_URL, '" />', '</div></td>');
		}
		if (!ts.body) {
			ts.body = new Ext.Template('{rows}');
		}
		if (!ts.row) {
			ts.row = new Ext.Template(
					'<div class="x-grid3-row {alt}" style="{tstyle}"><table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}"',
					'<tbody><tr>{cells}</tr>',
					(this.enableRowBody
							? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body>"{body}</div></td></tr>'
							: ' '), '</tbody></table></div>');
		}

		if (!ts.cell) {
			ts.cell = new Ext.Template(
					'<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>',
					'<div class="x-grid3-cell-inner x-grid3-col-{id}" title="{tip}" {attr}>{value}</div>',
					'</td>');
		}
        /** @刘晓斌2012-10-15修改，开始
         * 在使用行编辑表格时，grid单元格提示+复制的这一段代码造成重新刷新Row出错，
         * 原因:这段代码覆盖了原GridView的templates，但templates缺少（模版）属性rowInner
         */
        var rowBodyText = [
            '<tr class="x-grid3-row-body-tr" style="{bodyStyle}">',
                '<td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on">',
                    '<div class="x-grid3-row-body">{body}</div>',
                '</td>',
            '</tr>'
        ].join(""),        
        innerText = [
            '<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
                 '<tbody>',
                    '<tr>{cells}</tr>',
                    this.enableRowBody ? rowBodyText : '',
                 '</tbody>',
            '</table>'
        ].join("");
        if(!ts.rowInner){
            ts.rowInner = new Ext.Template(innerText);
        }
        /** @刘晓斌2012-10-15修改，结束        */
        
		for (var k in ts) {
			var t = ts[k];
			if (t && typeof t.compile == 'function' && !t.compiled) {
				t.disableFormats = true;
				t.compile();
			}
		}
		this.templates = ts;
		this.colRe = new RegExp("x-grid3-td-([^\\s]+)", "");
	},
	//private  
	doRender : function(cs, rs, ds, startRow, colCount, stripe) {
		var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount - 1;
		var tstyle = 'width:' + this.getTotalWidth() + ';';
		//buffers  
		var buf = [], cb, c, p = {}, rp = {
			tstyle : tstyle
		}, r;
		for (var j = 0, len = rs.length; j < len; j++) {
			r = rs[j];
			cb = [];
			var rowIndex = (j + startRow);
			for (var i = 0; i < colCount; i++) {
				c = cs[i];
				p.id = c.id;
				p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
						? 'x-grid3-cell-last '
						: ' ');
				p.attr = p.cellAttr = "";
				p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
				p.style = c.style;
				if (p.value == undefined || p.value === "") {
					p.value = "&#160;";
					p.tip = new String(""); //当grid单元格没有内容时,鼠标悬停不显示提示信息
				} else {
					p.tip = new String(p.value).replace(/<\/?.+?>/g, "");
				}
				if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
					p.css += ' x-grid3-dirty-cell';
				}

				cb[cb.length] = ct.apply(p);
			}
			var alt = [];
			if (stripe && ((rowIndex + 1) % 2 == 0)) {
				alt[0] = "x-grid3-row-alt";
			}
			if (r.dirty) {
				alt[1] = "x-grid3-dirty-row";
			}
			rp.cols = colCount;
			if (this.getRowClass) {
				alt[2] = this.getRowClass(r, rowIndex, rp, ds);
			}
			rp.alt = alt.join(" ");
			rp.cells = cb.join("");
			buf[buf.length] = rt.apply(rp);
		}
		return buf.join("");
	}  
});
//覆盖onRender，保证所有extjs输入控件失去焦点后，自动去除前后空格
Ext.override(Ext.form.Field, {
    afterRender : function(){
        Ext.form.Field.superclass.afterRender.call(this);
        this.initEvents();
        this.initValue();
        this.on('blur', function(this_){
            var v = this_.el.dom.value;
            if(typeof(v) != 'string')   return;
            this_.el.dom.value = v.trim();
        });
    },
    onRender : function(ct, position){
        if(!this.el){
            var cfg = this.getAutoCreate();

            if(!cfg.name){
                cfg.name = this.name || this.id;
            }
            if(this.inputType){
                cfg.type = this.inputType;
            }
            this.autoEl = cfg;
        }
        Ext.form.Field.superclass.onRender.call(this, ct, position);
        if(this.submitValue === false){
            this.el.dom.removeAttribute('name');
        }
        var type = this.el.dom.type;
        if(type){
            if(type == 'password'){
                type = 'text';
            }
            this.el.addClass('x-form-'+type);
        }
        if(this.readOnly){
            this.setReadOnly(true);
        }
        if(this.tabIndex !== undefined){
            this.el.dom.setAttribute('tabIndex', this.tabIndex);
        }
        //程锐添加，实现功能：如表格控件的allowBlank==false表示必填项，则feildLabel加上红色的*标识
		if(this.allowBlank == false){
			//this.fieldLabel = "*";
			if(!Ext.isEmpty(Ext.query("*[for=" + this.id + "]")) && Ext.isArray(Ext.query("*[for=" + this.id + "]"))){
				Ext.query("*[for=" + this.id + "]")[0].innerHTML = this.fieldLabel + ":<font color='red'>*</font>";
			}
		}
        this.el.addClass([this.fieldClass, this.cls]);
    }
});
//阻止按下Backspace键使页面后退
Ext.EventManager.on(Ext.isIE ? document : window, 'keydown', function(eventObject, target) { 
	if (eventObject.getKey() == eventObject.BACKSPACE && (target.disabled || target.readOnly 
            || (target.type != 'text' && target.type != 'password' && target.type != 'textarea'))) { 
	   eventObject.stopEvent(); 
	} 
}); 