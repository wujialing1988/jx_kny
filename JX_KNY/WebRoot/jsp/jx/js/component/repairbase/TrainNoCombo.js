/**
 * 车号选择列表
 * 控件使用方法：
 * 1.在页面jsp中引入js文件:<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script> 
 * 2.在表单页面js添加下例标签代码 
 * {id:自定义, xtype: 'TrainNo_combo',hiddenName: 自定义,fieldLabel:自定义,
 * displayField:自定义,valueField:自定义,queryHql:''}
 * 3.配置项说明：
 * a.id：控件id，必填
 * b.hiddenName：提交表单域名称，必填
 * c.fieldLabel: 在组件旁边那里显示的label文本,必填
 * d.displayField配置项：配置控件显示字段名,必填
 * e.valueField配置项：配置控件提交值,必填
 * f.returnField配置项:widgetId为需回显信息的field标签的id,propertyName为需回显信息的字段名称,回显字段名称列表：根据配置实体类具体配置，,选填
 * g.queryParams配置项：Hql的where条件，选填,注意：格式必须为json格式。
 * 例如{'wareHouseIDX': houseId,'locationName':locationName},其中'wareHouseIDX'与'locationName'为对应实体类字段；houseId、locationName为js变量
 * h.queryHql配置项：自定义Hql，暂只接受单实体对象查询，配置queryHql优先，如已配置queryHql则queryParams条件无效，选填 * 
 
 * 4.设置默认值或回显值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).setDisplayValue(valueField,displayField);
 *   componentId 控件id，参数说明：valueField：该控件valueField值、displayField：该控件displayField值
 * 5.清空值，需在相应事件方法中加入以下代码：
 *   Ext.getCmp(componentId).clearValue();
 *   componentId 控件id
 */
Ext.namespace("jx.js.component.repairbase");
jx.js.component.repairbase.TrainNo_combo = function(){
	this.maxHeight = arguments[0].maxHeight || arguments[0].height || this.maxHeight;
	this.store = new Ext.data.SimpleStore({fields:[],data:[[]]});
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
	this.typeAhead = true;
	this.forceSelection = true;
	this.isAll = 'yes';//设置是否只显示本段机车，默认显示所有机车
	this.isCx = 'yes';//设置是选择配属机车还是承修机车,默认查询承修机车
	this.isIn = false;//是否查询调入机车
	this.isRemoveRun = false;//设置是否排除在修机车，默认不排除
	this.dataurl = ctx + '/jczlTrain!pageListForTNCombo.action';//数据源路径
	this.fields = ["trainNo","holdOrgId","holdOrgName","holdOrgSeq","makeFactoryIDX","makeFactoryName",
		{name:"leaveDate", type:"date", dateFormat: 'time'},
		"buildUpTypeIDX","buildUpTypeCode","buildUpTypeName","trainUse","trainUseName","bId","dId","bName","dName","bShortName","dShortName"];//数据字段
	jx.js.component.repairbase.TrainNo_combo.superclass.constructor.apply(this, arguments);
	
}
Ext.extend(jx.js.component.repairbase.TrainNo_combo,Ext.form.ComboBox,{	
	 setStore : function(){		
			this.store = new Ext.data.JsonStore({
				root:"root", totalProperty:"totalProperty", 
				url:this.dataurl+ '?queryParams='+Ext.util.JSON.encode(this.queryParams)+'&queryHql='+this.queryHql+'&isAll='+this.isAll+'&isCx='+this.isCx+'&isRemoveRun='+this.isRemoveRun,
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
	 
	//重置store，用于级联查询
	cascadeStore : function(){
		this.store.proxy = new Ext.data.HttpProxy( {   
            url : this.dataurl+ '?queryParams='+Ext.util.JSON.encode(this.queryParams)+'&queryHql='+this.queryHql+'&isAll='+this.isAll+'&isCx='+this.isCx + '&isIn='+ this.isIn+'&isRemoveRun='+this.isRemoveRun
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
		//设置store
		this.setStore();
		
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
    collapse : function(record, index){
        if(!this.isExpanded()){
            return;
        }
        this.list.hide();
        this.assertValue();
        Ext.getDoc().un('mousewheel', this.collapseIf, this);
        Ext.getDoc().un('mousedown', this.collapseIf, this);
        this.fireEvent('collapse', this, record, index);
    },
    collapseIf : function(e){
        if(!this.isDestroyed && !e.within(this.wrap) && !e.within(this.list)){
            this.collapse();
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
                if (this.value == val){
                    return;
                }
                val = rec.get(this.valueField || this.displayField);
            }
            this.setValue(rec);
            this.setReturnValue(rec);
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
						Ext.getCmp(returnField[i].widgetId).setValue(fieldName==null?"":fieldName);
					}else if(typeof(Ext.get(returnField[i].widgetId)) != 'undefined' && Ext.get(returnField[i].widgetId) != null && fieldName != ''){
						Ext.get(returnField[i].widgetId).dom.value = (fieldName == null?"":fieldName);
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
Ext.reg('TrainNo_combo', jx.js.component.repairbase.TrainNo_combo);