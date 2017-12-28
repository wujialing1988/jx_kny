/**
 * 班次交接
 */
Ext.onReady(function(){
	
Ext.namespace('ClassTransfer');                       //定义命名空间

//定义全局变量保存查询条件
ClassTransfer.searchParam = {} ;

ClassTransfer.labelWidth = 60;                        //表单中的标签名称宽度
ClassTransfer.fieldWidth = 130;                       //表单中的标签宽度
ClassTransfer.delname = [] ;						  // 需要删除的属性
ClassTransfer.dynamicColumuns = [] ;						  // 动态列



// 新增窗口
ClassTransfer.saveForm = new Ext.form.FormPanel({
	border: false,
	labelAlign:"left", 
	layout:"form",
	bodyStyle:"padding:10px;",
	defaults: {
		xtype:"container", autoEl:"div", layout:"form"
	},
	items:[
	{
        xtype:'fieldset',
        id:'id_base',
        title: i18n.ClassTransfer.transferInfo,
        autoHeight:true,
        layout: 'column',
        defaults: {
        	columnWidth:.5,
			layout: 'form',
			border: false,
			defaults: {
        		xtype:"textfield", 
        		anchor:"98%",
    		    labelWidth:ClassTransfer.labelWidth, 
				fieldWidth: ClassTransfer.fieldWidth
        	}
        },
        items :[
        	{xtype: "hidden", name: "idx"}, 
        	{xtype: "hidden", name: "empid",id:'empid'}, 
        	{xtype: "hidden", name: "transferEmpid",id:'transferEmpid'}, 
        	{xtype: "hidden", name: "classNo",id:'classNo'},
        	{xtype: "hidden", name: "transferClassNo",id:'transferClassNo'},
 			{
            	items:[{
                    id: 'empname_SelectWin_Id',xtype: 'OmEmployee_SelectWin', fieldLabel: i18n.ClassTransfer.empname,
						  hiddenName: 'empname', displayField:'empname', valueField: 'empname',
						  returnField :[{widgetId: "empid", propertyName: "empid"}],
						  allowBlank:false,editable: false
                }]
            },{
            	items:[{
                    id: 'transferName_SelectWin_Id',xtype: 'OmEmployee_SelectWin', fieldLabel: i18n.ClassTransfer.transferName,
						  hiddenName: 'transferName', displayField:'empname', valueField: 'empname',
						  returnField :[{widgetId: "transferEmpid", propertyName: "empid"}],
						  allowBlank:false,editable: false
                }]
            },{
            	items:[{
            		fieldLabel: i18n.ClassTransfer.className,
                	id:"className_combo",	
    				hiddenName: "className",
    				xtype: "Base_combo",
    			    entity:'com.yunda.freight.base.classMaintain.entity.ClassMaintain',
					business: 'classMaintain',	
                    returnField: [{widgetId:"transferClassNo",propertyName:"classNo"}],
                    fields:["classNo","className","idx"],
        		    displayField: "className", valueField: "className",
                    pageSize: 0, minListWidth: 200,
                    allowBlank:false,editable:false
                }]
            },{
            	items:[{
            		fieldLabel: i18n.ClassTransfer.transferClass,
               		id:"transferClassName_combo",	
    				hiddenName: "transferClassName",
    				xtype: "Base_combo",
    			    entity:'com.yunda.freight.base.classMaintain.entity.ClassMaintain',
					business: 'classMaintain',	
                    returnField: [{widgetId:"classNo",propertyName:"classNo"}],
                    fields:["classNo","className","idx"],
        		    displayField: "className", valueField: "className",
                    pageSize: 0, minListWidth: 200,
                    allowBlank:false,editable:false                    
                }]
            },{
            	items:[{
            		fieldLabel: i18n.ClassTransfer.transferDate,
            		xtype:'my97date',
            		format: 'Y-m-d H:i',
                    name: "transferDate",
                    allowBlank:false
                }]
            }
        ]
    },
		// 交接项
		{
        xtype:'fieldset',
        title: i18n.ClassTransfer.transferItem,
        id:'id_item',
        autoHeight:true,
        layout: 'column',
        defaults: {
        	columnWidth:1,
			layout: 'form',
			border: false,
			defaults: {
        		xtype:"textfield", 
        		anchor:"98%",
    		    labelWidth:ClassTransfer.labelWidth, 
				fieldWidth: ClassTransfer.fieldWidth
        	}
        },
        items :[
        ],
   		listeners : {
   			// 加载后动态添加数据
			"render" : function(me) {

	         }
		}
    }
	]
});

/**
 * 动态加载交接项
 */
ClassTransfer.addItem = function(){
		var me = Ext.getCmp('id_item');
		Ext.Ajax.request({
			url: ctx + "/zbglHoModelItem!findItemList.action",
			async:false,
			params: {parentName: i18n.ClassTransfer.knyTruckUse},
			success: function(response, options){
				var result = Ext.util.JSON.decode(response.responseText);
				if(result.list){
					var list = result.list ;
					// 先清除全部组件
					me.removeAll();
					for (var i = 0; i < list.length; i++) {
						var obj = list[i] ;
						if(Ext.isEmpty(obj.handOverItemStatus)){
							ClassTransfer.addTextField(me,obj,i);
						}else{
							ClassTransfer.addCheckBox(me,obj,i);
						}
						ClassTransfer.delname.push("item"+i);
						
					}
				}
			}
		});
};


/**
 * 添加输入表单
 */
ClassTransfer.addTextField = function(me,obj,i){
	me.add({
            	items:[{
 					name: "item"+i,
 					id:'item'+i,
 					submitValue:false,
 					fieldLabel: obj.handOverItemName,
 					xtype:'textarea',
 					maxLength:100,
 					height: 55
        }]
    });
}

/**
 * 添加选择框
 */
ClassTransfer.addCheckBox = function(me,obj,i){
	var saveFields = [];
	var objList = obj.handOverItemStatus.split(",");
	for (var j = 0; j < objList.length; j++) {
	        var field = objList[ j ];
	        var editor = {};  //定义检验项
	        editor.xtype = "checkbox";
	        editor.name = "item"+i, 
	        editor.boxLabel = field ;
	        saveFields.push(editor);
	} 
	me.add({
            	items:[{
 					xtype:'checkboxgroup',
 					submitValue:false,
 					name: "item"+i, 
 					id:'item'+i,
 					columns:objList.length,
 					fieldLabel: obj.handOverItemName, 
 					items: saveFields
                }]
            });
}

ClassTransfer.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/classTransfer!pageClassTransferList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/classTransfer!saveClassTransfer.action',             //保存数据的请求URL
    deleteURL: ctx + '/classTransfer!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, 
    saveForm:ClassTransfer.saveForm,
    saveFormColNum:2,
	fields: dynamicColumuns,
	searchFn: function(searchParam){ 
		ClassTransfer.searchParam = searchParam ;
        ClassTransfer.grid.store.load();
	},
	afterShowEditWin: function(record, rowIndex){
		Ext.getCmp('empid').setValue(record.get('empid'));
		Ext.getCmp("empname_SelectWin_Id").setDisplayValue(record.get('empname'),record.get('empname'));
		Ext.getCmp('transferEmpid').setValue(record.get('transferEmpid'));
		Ext.getCmp("transferName_SelectWin_Id").setDisplayValue(record.get('transferName'),record.get('transferName'));
		
		// 交接项
		var detailsrecord = record.get('details');
		if(!Ext.isEmpty(ClassTransfer.delname)){
			for (var i = 0; i < ClassTransfer.delname.length; i++) {
				var delname = ClassTransfer.delname[i];
				// 获取交接项
				var item = Ext.getCmp(delname);
				if(item.xtype == 'checkboxgroup'){
					var dr = ClassTransfer.getDetailRecord(detailsrecord,item.fieldLabel);
					if(dr && !Ext.isEmpty(dr.transferContent)){
						var transferContent = dr.transferContent.split(",");
						var itemArray = item.items.items;
						for (var j = 0; j < itemArray.length; j++) {
							var checkBox = itemArray[j];
							if(ClassTransfer.isContainsArray(transferContent,checkBox.boxLabel)){
								checkBox.setValue(true);
							}
						}
					}
				}else{
					var dr = ClassTransfer.getDetailRecord(detailsrecord,item.fieldLabel);
					item.setValue(dr.transferContent);
				}
			}
		}
	},
	afterShowSaveWin: function(){
		// 默认带出当前登录人
		Ext.getCmp('empid').setValue(empId);
		Ext.getCmp("empname_SelectWin_Id").setDisplayValue(empName,empName);
	},
	beforeSaveFn: function(data){ 
		// 保存之前将数据分割开
		var details = [] ;
		if(!Ext.isEmpty(ClassTransfer.delname)){
			for (var i = 0; i < ClassTransfer.delname.length; i++) {
				var delname = ClassTransfer.delname[i];
				// 获取交接项
				var item = Ext.getCmp(delname);
				if(item.xtype == 'checkboxgroup'){
					var values = "" ;
					var checkeds = item.getValue();
					if(checkeds){
						for (var j = 0; j < checkeds.length; j++) {
							values += checkeds[j].boxLabel + "," ;
						}
						if(!Ext.isEmpty(values)){
							values = values.substring(0,values.length - 1);
						}
					}
					details.push({
						transferItem:item.fieldLabel,
						transferType:item.xtype,
						transferContent:values
					});
				}else{
					var values = item.getValue();
					details.push({
						transferItem:item.fieldLabel,
						transferType:item.xtype,
						transferContent:values
					});
				}
				// 删除多余字段
				eval("delete data."+delname);
			}
		}
		// 取交接项
		data.details = details ;
		return true; 
	},
	afterSaveSuccessFn: function(result, response, options){
		ClassTransfer.grid.saveWin.hide();
        ClassTransfer.grid.store.reload();
        alertSuccess();
    }
});

// 添加加载结束事件
ClassTransfer.grid.getStore().addListener('load',function(me, records, options ){
	for (var i = 0; i < records.length; i++) {
		var record = records[i];
		var details = record.data.details;
		for (var j = 0; j < details.length; j++) {
			record.set("item"+j,details[j].transferContent);
		}
		record.commit();
	}
})

// 默认排序		
ClassTransfer.grid.store.setDefaultSort("transferDate", "DESC");

/**
 * 通过交接项名称取后台交接项
 */
ClassTransfer.getDetailRecord = function(records,lable){
	for (var i = 0; i < records.length; i++) {
		var record = records[i];
		if(record && record.transferItem == lable){
			return record ;
		}
	}
	return null ;
}

/**
 * 数据是否包含元素
 */
ClassTransfer.isContainsArray = function(arrays,lable){
	for (var i = 0; i < arrays.length; i++) {
		if(arrays[i] == lable){
			return true;
		}
	}
	return false;
}

//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:ClassTransfer.grid });
	
//查询前添加过滤条件
ClassTransfer.grid.store.on('beforeload' , function(){
		var searchParam = ClassTransfer.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

// 调用组装
ClassTransfer.addItem();

});