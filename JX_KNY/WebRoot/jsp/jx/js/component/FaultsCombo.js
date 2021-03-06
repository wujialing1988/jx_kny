/**
 * 故障现象选择
 * 控件使用方法参见TrainNoCombo.js
 */
Ext.namespace("jx.js.component");
jx.js.component.Faults_combo = function(){
	this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
	this.store = null;
	this.mode = 'remote';
    this.triggerAction = 'all';
    this.editable = false;
    this.emptyText = "";
    this.returnField;
    this.pageSize = 20;//设置分页,默认为20条记录分页
    this.minListWidth = 200;//设置下拉列表显示宽度
    this.queryHql = '';//查询Hql    
    this.queryParams;
    this.minChars = 1;//设置字符个数开始触发  
	this.selectOnFocus = true;
	this.isSeleced = false;
//	this.typeAhead = true;
//	this.forceSelection = true;
	this.dataurl = ctx + '/equipFault!searchByFaultName.action';//数据源路径
	this.fields = ["FaultID", "FaultName"];//数据字段
	jx.js.component.Faults_combo.superclass.constructor.apply(this, arguments);
	
}
Ext.extend(jx.js.component.Faults_combo,Ext.form.ComboBox,{	
	 setStore : function(){		
			this.store = new Ext.data.JsonStore({
				root:"root", totalProperty:"totalProperty", 
				url:this.dataurl+ '?queryParams='+Ext.util.JSON.encode(this.queryParams)+'&queryHql='+this.queryHql,
				fields:this.fields
			});
			if(this.pageSize != 0){
				this.store.load({
					params :{
						start:0,
						limit:this.pageSize
					}
				});				
			}else{
				this.getStore().reload();
			}		
		
	},
	 
	//重写onRender方法	
	onRender : function(ct, position){
		if(!this.store){
			//设置store
			this.setStore();
		}
		
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
            this.isSelected = true;
        }
    },
    collapse : function(record, index){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        this.assertValue();
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
        if(!this.isSelected){
        	if(this.getValue() != null && this.getValue() != ''){
        	record = this.getStore().getAt(0);        	
        	this.setValue(record);
            this.setReturnValue(record);
            this.isSelected = false;
        	}
        }
        this.fireEvent('collapse', this);
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
        if(r != null && r != "" && typeof(r) != 'undefined' && typeof(r) == 'object')
		{
			if (this.hiddenField && typeof(this.hiddenField) != 'undefined') {
				
				this.hiddenField.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);
			}			
			this.lastSelectionText = r.get(this.displayField);
			Ext.form.ComboBox.superclass.setValue.call(this, r.get(this.displayField));			
			this.value = typeof(r.get(this.valueField)) == 'undefined' || r.get(this.valueField)=='undefined'?'':r.get(this.valueField);			          
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
					
					if(typeof(Ext.getCmp(returnField[i].widgetId)) != 'undefined' && Ext.getCmp(returnField[i].widgetId) != null && fieldName != ''){
						Ext.getCmp(returnField[i].widgetId).setValue(fieldName);
					}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null && fieldName != ''){
						Ext.get(returnField[i].widgetId).dom.value = fieldName;
					}		        		
	        	}        	
	        }      
		}
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
Ext.reg('Faults_Combo', jx.js.component.Faults_combo);