/**
 * Eos业务字典，数据容器，接口方法
 */
Ext.namespace("EosDictEntry");
//Eos业务字典，数据容器
EosDictEntry.store = new Ext.data.JsonStore({
    id: "idx", data: EosDictEntry_storeData,
    fields: [ "idx","dicttypeid","dictid","dictname","status","sortno","rank","parentid","seqno","filter1","filter2" ]
});
/**
 * Eos业务字典，根据字典类型编号、字典项编号
 * @param {} dicttypeid 字典类型编号
 * @param {} dictid 字典项编号
 * @return 返回字典项名称
 */
EosDictEntry.getDictname = function(dicttypeid, dictid){
    var record = EosDictEntry.store.getById(dicttypeid + "_" + dictid);
    if(typeof(record) != "undefined" && record != null && record != "" && record != "null" && typeof(record.get("dictname")) != "undefined"){
    	return record.get("dictname");
    }
    return "";
    
}
/**
 * 业务字典选择列表
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script> 
 * 2.在表单页面js添加下例标签代码 
 * {id:自定义, xtype: 'EosDictEntry_combo',hiddenName: 自定义,fieldLabel:自定义,
 * displayField:"dictname",valueField:"dictid",dicttypeid:自定义需显示业务代码,如ABF_EMPLEVEL,queryWhere:''}
 * 3.配置项说明：
 * a.id：控件id，必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本
 * d.displayField：必须写为"dictname"，必填
 * e.valueField:必须写为"dictid"，必填
 * f.dicttypeid:自定义需显示业务代码,如ABF_EMPLEVEL、必填
 * g.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称
 * 回显字段名称列表：根据EosDictEntry实体类具体配置
 * h.queryWhere配置项：Hql的where条件，选填
 * I.其他配置项参照EXTJS的combo组件
 * 
 * 4.
 * a.在双击弹出编辑表单时，为回显专业类型选择控件值需在页面js的rowdblclick事件方法中加入以下代码  
 * 				Ext.getCmp(componentId).loadRecord(r,id_field,name_field,componentId);              
	            参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
   b.设置默认值，代码如下：
   				Ext.getCmp(componentId).loadRecord('',id_field,name_field,componentId);
   				参数说明：r:设为空，id_field：需要设置的dictid值、name_field：需要设置的dictname值、componentId：该控件id
   5.在页面js的新增及重置方法中加入以下代码
 * 	Ext.getCmp(componentId).clear(componentId)以清除回显项;
 *  参数说明：componentId：该控件id
 * 6.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(valueField,displayField);
 *   componentId 控件id，参数说明：valueField：该控件valueField值、displayField：该控件displayField值
 * 7.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id
 */
EosDictEntry_combo = function(){	
	this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
	this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
    this.mode = 'local';
    this.triggerAction = 'all';
    this.editable = false;
    this.emptyText = "";
    this.returnField;
    this.dicttypeid;
    this.status = 1; //谭诚 2013-11-07添加，默认为读取未封存的
    this.queryWhere = '';
    this.hasEmpty = 'true';
    this.empText = '请选择...';
	EosDictEntry_combo.superclass.constructor.apply(this, arguments);
}
Ext.extend(EosDictEntry_combo,Ext.form.ComboBox,{	
    //重构初始化，初始化的时候就把store载入
    initComponent : function(){
        EosDictEntry_combo.superclass.initComponent.call(this);
        this.store = new Ext.data.JsonStore({
            url:ctx + '/eosDictEntrySelect!combolist.action?status='+this.status+'&dicttypeid=' + this.dicttypeid + '&queryWhere=' + this.queryWhere 
            		+ '&hasEmpty=' + this.hasEmpty,
            fields:["dictname","dictid","status","sortno","rank","parentid","seqno","dicttypeid"]                       
        });
        this.getStore().reload();        
    },
	setStore : function(){		
			this.store = new Ext.data.JsonStore({
				url:ctx + '/eosDictEntrySelect!combolist.action?dicttypeid=' + this.dicttypeid + '&queryWhere=' + this.queryWhere 
					+ '&hasEmpty=' + this.hasEmpty,
				fields:["dictname","dictid","status","sortno","rank","parentid","seqno","dicttypeid"]						
			});
			this.getStore().reload();
		
	},
	//重写onRender方法	
	onRender : function(ct, position){
		//设置store
//		this.setStore();
        if(this.hiddenName && !Ext.isDefined(this.submitValue)){
            this.submitValue = false;
        }        
        Ext.form.ComboBox.superclass.onRender.call(this, ct, position);
        if(this.hiddenName){
            this.hiddenField = this.el.insertSibling({tag:'input', type:'hidden', name: this.hiddenName,
                    id: (this.hiddenId || Ext.id())}, 'before', true);

        }
        if(Ext.isGecko){
            this.el.dom.setAttribute('autocomplete', 'off');
        }

        if(!this.lazyInit){
            this.initList();
        }else{
            this.on('focus', this.initList, this, {single: true});
        }
    },
    onSelect : function(record, index){
        if(this.fireEvent('beforeselect', this, record, index) !== false){
            this.setValue(record);
            this.setReturnValue(record);
            this.collapse();
            this.fireEvent('select', this, record, index);
        }
    },
    assertValue : function(){
        var val = this.getRawValue(),
            rec;

        if(this.valueField && Ext.isDefined(this.value)){
            rec = this.findRecord(this.valueField, this.value);
        }
        if(!rec || rec.get(this.displayField) != val){
            rec = this.findRecord(this.displayField, val);
        }
        if(!rec && this.forceSelection){
            if(val.length > 0 && val != this.emptyText){
                this.el.dom.value = Ext.value(this.lastSelectionText, '');
                this.applyEmptyText();
            }else{
                this.clearValue();
            }
        }else{
            if(rec && this.valueField){
                // onSelect may have already set the value and by doing so
                // set the display field properly.  Let's not wipe out the
                // valueField here by just sending the displayField.
                if (this.value == val){
                    return;
                }
                val = rec.get(this.valueField || this.displayField);
            }
            this.setValue(rec);
        }
    },
    
    //设值
    setValue : function(r){
        if(r == null || r == '' || typeof(r) == 'undefined')    return this;
        if(typeof(r) == 'object'){
			if (this.hiddenField && typeof(this.hiddenField) != 'undefined') {				
				this.hiddenField.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);
			}
			//EosDictEntry_combo.superclass.setValue.call(this, r.get(this.displayField));
			this.lastSelectionText = r.get(this.displayField)==this.empText?'':r.get(this.displayField);
			Ext.form.ComboBox.superclass.setValue.call(this, r.get(this.displayField)==this.empText?'':r.get(this.displayField));
			this.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);			
		} else if(typeof(r) == 'string'){
            EosDictEntry_combo.superclass.setValue.call(this, r);
        }
        return this;     
    },
    //设置自定义配置项的回显值
	setReturnValue : function(r) {		
		if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{
			var returnField = this.returnField;
	        if(returnField != null && returnField.length > 0){        	
	        	for(var i = 0;i < returnField.length;i++){
					var displaytext = returnField[i].propertyName;
					var fieldName = '';
					if(typeof(r) != 'undefined' && r != null && typeof(r.data[displaytext]) != 'undefined'){
						fieldName = r.data[displaytext];
					}
					
					if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null 
						&& fieldName != '' && fieldName != null && fieldName != 'null'){						
						if(fieldName == this.empText){
							Ext.getCmp(returnField[i].widgetId).setValue("");
						}else{
							Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
						}
					}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null 
						&& fieldName != '' && fieldName != null && fieldName != 'null'){
						Ext.get(returnField[i].widgetId).dom.value = fieldName;
						if(fieldName == this.empText){
							Ext.get(returnField[i].widgetId).dom.value = "";
						}else{
							Ext.get(returnField[i].widgetId).dom.value = fieldName;
						}
					}		        		
	        	}        	
	        }      
		}
	},
	//根据record回显值,参数说明：r：表单记录集record、id_field：该控件id域名、name_field：该控件显示名称域名、componentId：该控件id
	loadRecord: function(r,id_field,name_field,componentId){
		       
        var p_record = new Ext.data.Record();		
        //设置默认值
        if(r == null || r == '' || typeof(r) == 'undefined' || r == 'null'){
        	p_record.set(this.valueField,id_field);
        	p_record.set(this.displayField,name_field);
        }
        //设置回显值		
        else if(r != null && r != '' && r != 'null' && typeof(r) != 'undefined'){
        	if(r.get(id_field) != null){        		
        		p_record.set(this.valueField,r.get(id_field));
        		p_record.set(this.displayField,r.get(name_field));
        	}
        }             
        Ext.getCmp(componentId).setValue(p_record);
	},
	//清空方法,参数说明：componentId:控件Id
	clear: function(componentId){    	
	    var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		Ext.getCmp(componentId).setValue(p_record);
	},
	//回显值（new）,参数说明：valueField：该控件valueField值、displayField：该控件displayField值
    setDisplayValue : function(valueField, displayField){
    	var p_record = new Ext.data.Record();
    	p_record.set(this.valueField,valueField);
        p_record.set(this.displayField,displayField);
        this.setValue(p_record);
    },
    //清空(new)
    clearValue :  function(){
    	var p_record = new Ext.data.Record();
		p_record.set(this.valueField,"");
        p_record.set(this.displayField,"");
		this.setValue(p_record);
    }
});
Ext.reg('EosDictEntry_combo', EosDictEntry_combo);