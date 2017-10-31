/**
 * 设置EXT控件显示文本框名称为需要提交的名称
 * 如：render: setTextName("orgName")
 * @param name
 * @return
 */
function setTextName(name){
	return function(){
		document.getElementById(this.autoEl.id).name = name;
	}
}

function notEidt(){}

/**
 * 已启动和未启动查询表单的创建
 * @return
 */
function createSearchForm(){
	
	function callback(node, e){
		form.findByType("PartsTypeTreeSelect")[0].setValue(node.attributes["specificationModel"]);
	}
	var form = defineFormPanel({
		defaultType: "textfield",
		labelWidth: 70,
		rows:[{
			cw: [200, 200, 200, 200, 200],
			cols: [{
				fieldLabel: "下车车型",
				xtype: "Base_combo",
				hiddenName: "unloadTrainTypeIdx",
			    business: 'trainVehicleType',
			    entity:'com.yunda.freight.base.vehicle.entity.TrainVehicleType',
                fields:['idx','typeName','typeCode','vehicleType'],
                returnField: [{widgetId:"PartsUnloadRegister_unloadTrainType",propertyName:"typeCode"}],
                queryParams: {},// 表示客货类型
    		    displayField: "typeCode", valueField: "idx",
                pageSize: 20, minListWidth: 200,
                editable:false		
			},{
				fieldLabel: "下车车号",
				name: "unloadTrainNo",
				maxLength: 4
			},{
				fieldLabel: "配件名称",
				name: "partsName"
			},{
				fieldLabel: "配件编号",
				name: "partsNo"
			},{
	         	xtype:"PartsTypeTreeSelect",
	         	fieldLabel: '规格型号',
				name: 'specificationModel',
				returnFn: callback,
				editable:false
         	}]
		}]
	}, {labelAlign: "right"});
	return form;
}