Cloud = {
	id_num: 1,
	getNum: function(){
		return this.id_num++;
	},
    $:function (el){
       return typeof el=="string" ? document.getElementById(el) : el;
    },
    on:function (el, type, fn){
        el = this.$(el);
        if(el.attachEvent){        	
        	el.attachEvent('on'+type, function(){
        		fn.call(el,event);
        	});
        }else{        	
        	el.addEventListener(type, fn, false);
        }
        return this
    },
    stopUp:function (e){
        e = e || window.event;
        e.stopPropagation && e.stopPropagation();
        e.cancelBubble = true;
    },
    onblur:function (el, fn){
        el = this.$(el);
        Cloud.on(el, 'click', function (e){        	
        	Cloud.stopUp(e)
        }).on(document, 'click', function(e){
        	fn.call(el, e);
        });
    },
    show: function(el){
    	for(var i = 0; i < this.positions.length; i++){
    		if(this.positions[i] !== el){
    			if(!el.display){
    				this.hide(this.positions[i]);
    			}
    		}
    	} 
    	if(el.style)
    		el.style.display = 'inline';
    },
    childShow: function(el){
    	el.style.display = 'inline';
    },
    hide: function(el){
    	el.style.display = 'none';
    },
    positions: []
};

RangeText = function(){
	this.type = 'number';	
	this.value1 = null;
	this.value2 = null;
	this.position = null;
	this.clientWidth = 50;
	this.readOnly = true;
	var _ = this;
	this.alias = null;
	this.my97cfg = null;
	this.format = null;
	this.key = null;
	this.condition = new Ext.form.ComboBox({
		store:new Ext.data.SimpleStore({
		    fields: ['value', 'text'],
			data: [['r', '范围'], ['>=', '大于等于'], ['<=', '小于等于'], ['=', '等于']]
		}),
		valueField:'value',
		width: 60,
		editable:false,
		displayField:'text',
		triggerAction:'all',
		mode:'local',
		listeners:{
			render: function(){
				this.setValue('r');
			},
			select: function(){
				_.position.hideable = false;
		    	if(this.getValue() == 'r')
		    	{
		    		Cloud.childShow(_.position.children[2]);
		    	}
		    	else
		    	{
		    		Cloud.hide(_.position.children[2]);//隐藏[至[文本框]]
		    	}
		    	_.setPosition();
				_.valueChange();
			}
		}
	});
	this.id = null;
	this.name = null;
	RangeText.superclass.constructor.apply(this, arguments);	
}
Ext.extend(RangeText, Ext.form.TextField,{	
	//重写onRender方法	
	onRender : function(ct, position){
		//this.key = this.name;
        if(this.hiddenName && !Ext.isDefined(this.submitValue)){
            this.submitValue = false;
        }        
        Ext.form.TextField.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({
            	tag:'input', type:'hidden', name: this.hiddenName,
                    id: (this.hiddenId || Ext.id())
            }, 'before', true);
        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }
    },
    onFocus: function(){
    	if(this.position == null)
    		this.init();
    	this.setPosition();
    	Cloud.show(this.position);
    	if(this.type === 'number'){
    		this.value1.focus();
    	}
    	this.position.isDisplay = true;
    },
    createContainer: function(){
    	this.position = document.createElement("div");
    	this.position.className = 'x-container';
    	this.position.hideable = true;
    	this.position.innerHTML = "<span id='" + this.id + "_condition'>"
    			+ "</span><span id='" + this.id + "_value1'></span><span><label>至</label><span id='"
    			+ this.id + "_value2'></span></span>";
    	this.position.id = this.id + '_container';
    	document.body.appendChild(this.position);
    	Cloud.positions.push(this.position);
    },
    valueChange: function(){
    	var i = this.condition.getValue();
    	var val1 = this.value1.getValue();
    	var val2 = this.value2.getValue();
    	if( val1 != ''){
    		if(i == 'r')
        	{
    			if(val2 != '')
    				this.setValue(val1 + ' - ' + val2);
    			else
    				this.setValue(">" + val1);        		
        	}        	
        	else
        	{
        		this.setValue(i + val1);
        	}
    	}else{
    		if(i == 'r' && val2 != '' ){
    			this.setValue('<' + val2);
    		}else{    			
    			this.setValue('');
    		}
    	}
    },
    initNumber: function(_){
    	this.value1 = new Ext.form.NumberField({
			width:this.clientWidth,
			listeners:{
				change: function(){
					_.valueChange();
				},
				specialkey: function(field, e){
					if(e.getKey() == Ext.EventObject.ENTER && this.getValue()){
						if(_.condition.getValue() === 'r')
							_.value2.focus();
						else{
							Cloud.hide(_.position);
						}
					}
				}
			}
		});
		this.value2 = new Ext.form.NumberField({
			width: this.clientWidth,
			listeners:{
				change: function(){
					_.valueChange();
				},
				specialkey: function(field, e){
					if(e.getKey() == Ext.EventObject.ENTER && this.getValue()){
						_.valueChange();
						Cloud.hide(_.position);
					}
				}
			}
		});
    },
    bindFunc: function(func, bind){
    	var _ = this;
    	if(this.my97cfg[func]){
    		var func = objthis.my97cfg[func];
    		this.my97cfg[func] = function(){
    			_[bind]();
    			func();
    		}
    	}else{
    		this.my97cfg[func] = function(){
    			_[bind]();
    		}
    	}
    },
    initDate: function(_){
    	this.my97cfg = this.my97cfg || {};    	
    	this.bindFunc('onpicked', 'valueChange');
    	this.bindFunc('oncleared', 'valueChange');
    	var v1_id = 'range_my97_gen' + Cloud.getNum();
    	var v2_id = 'range_my97_gen' + Cloud.getNum();
    	this.value1 = new Ext.yunda.My97DatePicker({
    		width: this.clientWidth,
    		initNow: false,
    		id: v1_id,
    		my97cfg: Ext.apply({
    			maxDate: "#F{$dp.$D('" + v2_id + "')}"
    		}, this.my97cfg),
    		format: this.format || 'Y-m-d'
    	});
    	this.value1.getValue = function(){
    		return this.el.dom.value;
    	}
    	
		this.value2 = new Ext.yunda.My97DatePicker({
			width: this.clientWidth,
			id: v2_id,
			my97cfg: Ext.apply({
				minDate: "#F{$dp.$D('" + v1_id + "')}"
			}, this.my97cfg),
			format: this.format || 'Y-m-d'
		});
		this.value2.getValue = function(){
			return  this.el.dom.value;
		}
    },
    blurClearValue: function(o){
    	var _ = this;
		o.on("blur",function(){
			if(this.el.dom.value == ''){
				this.setValue('')
				_.valueChange();
			}
		});
	},
    renderValue: function(){
    	if(this.type === 'number'){
    		this.initNumber(this);
    	}else if(this.type === 'date'){
    		this.initDate(this);
    	}
    	this.value1.render(this.id + "_value1");
    	this.value2.render(this.id + "_value2");
    	this.blurClearValue(this.value1);
    	this.blurClearValue(this.value2);
    },
    init: function(){
    	this.createContainer();
    	this.condition.render(this.id + "_condition");
    	this.renderValue();
    	this.el.dom.removeAttribute("name");//移除name,不参与查询
    	var _ = this;
    	Cloud.on(this.id, 'click', function (e){
    		//_.setPosition();
    		//Cloud.show(_.position);
    		Cloud.stopUp(e)
    		
		}).onblur(this.position, function (){
			if(_.position.hideable && _.position.isDisplay){				
				Cloud.hide(_.position);
				_.position.isDisplay = false;
			}
			_.position.hideable = true;
		});
    	/*document.body.addEventListener("click", function(e){
    		e = e || window.event;
    		var tar = e.srcElement ? e.srcElement : e.target;
    		if(target)
    		Cloud.hide(_.position);
    	}, false);*/
    },
    setPosition: function(){
    	var el = this.el.dom;
    	var left = xtop = 0;
    	do{
    		left += el.offsetLeft;
    		xtop += el.offsetTop;
    	}while((el = el.offsetParent));
    	
    	var bodyWidth = document.body.offsetWidth;
    	
		var child = this.position.children;
		var containerWidth = 0;
    	for(var i = 0; i < child.length; i++){
    		
    		if(child[i].style.display != 'none'){
    			containerWidth += child[i].offsetWidth;
    		}
    	}
    	if(bodyWidth <= containerWidth + left){
    		left = bodyWidth - containerWidth - 20;
    	}
		
    	this.position.style.left = left + "px";
    	
    	this.position.style.top = (xtop + this.el.dom.offsetHeight) + "px";
    },
    //清空(new)
    clearValue :  function(){
    	this.value1 && this.value1.setValue('');
    	this.value2 && this.value2.setValue('');
    	this.setValue('');
    },
    getValues: function(){
    	var v1 = this.value1 ? this.value1.getValue() : undefined;
    	var v2 = this.value2 ? this.value2.getValue() : undefined;
    	var cdn = this.condition ? this.condition.getValue() : undefined;
    	return [v1, v2, cdn];
    },
    getName: function(){
    	return this.name;
    }
});
Ext.reg('rangetext', RangeText);

RangeText.setReportValue = function(id, param){
	var cmp = Ext.getCmp(id);
	if(!cmp) return;
	if(cmp.getValue() == '') return; //文本框没有值跳过
	var val = cmp.getValues();
	if( val[0] != ''){
		if(val[2] == 'r')
    	{
			if(val[1] != ''){
				param[cmp.getName() + "_start"] = val[0];
				param[cmp.getName() + "_end"] = val[1];
			}
			else{
				
				param[cmp.getName() + "_start"] = val[0];
			}
    	}
    	else
    	{
    		var reportname;
    		switch(val[2]){
    			case '>=':
    				reportname = cmp.getName()+"_start";
    				break;
    			case '<=':
    				reportname = cmp.getName()+"_end";
    				break;
    			default:
    				reportname = cmp.getName();
    			}
    		param[reportname] = val[0];
    	}
	}else{
		if(val[2] == 'r' && val[1] != '' ){
			param[cmp.getName() + "_start"] = val[1];
		}
	}
};
RangeText.setPageQuery = function(id, whereList){
	var cmp = typeof(id) === 'string' ? Ext.getCmp(id) : id;
	if(!cmp) return;
	if(cmp.getValue() == '') return; //文本框没有值跳过
	var val = cmp.getValues();
	if( val[0] != ''){
		if(val[2] == 'r')
    	{
			if(val[1] != ''){
				//this.setValue(val[0] + ' - ' + val[1]);
				whereList.push({propName: cmp.getName(), compare:Condition.GE, propValue: val[0]});
				whereList.push({propName: cmp.getName(), compare:Condition.LE, propValue: val[1]});
			}
			else{
				//this.setValue(">" + val[0]);
				whereList.push({propName: cmp.getName(), compare:Condition.GE, propValue: val[0]});
			}
    	}
    	else
    	{
    		//this.setValue(val[2] + val[0]);
    		var condition;
    		switch(val[2]){
    			case '>=':
    				condition = Condition.GE;
    				break;
    			case '<=':
    				condition = Condition.LE;
    				break;
    			default:
    				condition = Condition.EQ;
    		}
    		whereList.push({propName: cmp.getName(), compare: condition, propValue: val[0]});
    	}
	}else{
		if(val[2] == 'r' && val[1] != '' ){
			//this.setValue('<' + val[1]);
			whereList.push({propName: cmp.getName(), compare:Condition.LE, propValue: val[1]});
		}
	}
};
RangeText.setPageList = function(id, data, alias){
	var cmp = typeof(id) === 'string' ? Ext.getCmp(id) : id;
	if(!cmp) return;
	if(cmp.getValue() == '') return;
	var val = cmp.getValues();
	if(val[2] == undefined) return;
	val[0] = val[0] !== '' ? val[0] : undefined;
	val[1] = val[1] !== '' ? val[1] : undefined;
	if(val[2] !== 'r'){
		if(val[2] === '>=')
			val[1] = undefined;
		else if(val[2] === '<='){
			val[1] = val[0];
			val[0] = undefined;
		}
	}
	alias = (cmp.alias || alias)
	alias = alias ? "#[" + alias + "]#" : '';
	if(val[2] === '='){
		data[cmp.getName()] = alias + val[0];
	}else if(val[0] || val[1]){
		data[cmp.getName()] = alias + "$BETWEEN$" + val[0] + "|" + val[1];
	}
};

/**
 * 快速设置查询值
 */
RangeText.queryValue = function(form, store, alias){
	if(!form) return;
	var cmps = form.findByType("rangetext");
	for(var i = 0; i < cmps.length; i++){
		if(store instanceof Array)
			RangeText.setPageQuery(cmps[i], store);
		else
			RangeText.setPageList(cmps[i], store, alias);
	}
}

Ext.override(Ext.Window, {
	onHide: function(){
		if(Cloud.positions.length > 0)
			Cloud.show({});
	}
});