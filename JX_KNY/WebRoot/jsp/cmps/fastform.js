/**
 * 内部使用方法，外部无用
 * @returns {___anonymous666_763}
 */
function getColumnTmpl(){
	return  {
		baseCls:"x-plain",
		items: [],
		push: function(json){
			this.items.push(json);
		}
	};
}

/**
 * 创建显示Form
 * @param p{fields[grid.fields], cols: [4 | [0.25,0.25, 0.25, 0.25]], labelWidth： 100}
 * @returns {Ext.form.FormPanel}
 */
function getDsiplayForm(p, annexation){
	var isArray = true;
	function getDefaults(cols){
		var defaults = {
			layout:'form',
			defaultType: "displayfield",
			defaults:{
				anchor:'99%'
			}
		};
		if(cols instanceof Array == false){
			defaults.columnWidth = 1/cols;
			isArray = false;
		}
		return defaults;
	}
	
	function getItems(fields, cols){
		var col_count = 0;
		if(isArray){
			col_count = cols.length;
			isArray = true;
		}else{
			col_count = parseInt(1/cols);			
		}
		var items = [];
		var tmpl = null;
		for(var i = 0; i < col_count; i++){
			tmpl = getColumnTmpl();
			if(isArray){
				tmpl.columnWidth = cols[i];
			}else{
				tmpl.columnWidth = cols;
			}
			items[i] = tmpl;
		}
		
		for(var i = j = 0; i < fields.length; i++){
			if(fields[i].hide || !fields[i].dataIndex) continue;
			tmpl = {name: fields[i].dataIndex, fieldLabel: fields[i].header, style: "color:blue;"};
			if(fields[i].id) tmpl.id = fields[i].id;
			items[j%col_count].push(tmpl);
			j++;
		}
		return items;
	}
	
	var construct = {
		layout: "column",
		labelWidth: p.labelWidth,
		border: false,
		baseCls:"x-plain",
		style: "padding:5px",
		defaults: getDefaults(p.cols),
		items:getItems(p.fields, p.cols)
	};
	Ext.apply(construct, annexation); //合并参数
	return new Ext.form.FormPanel(construct);
}

/**
 * 设置DisplayField日期字段格式化
 *　id格式: x_$_id; 对应field格式 $Date
 * record
 * fmt
 */
function setDateText(id, r, fmt){
	var key = id.split("_")[1] + "Date";
	if(r.get(key)){
		Ext.getCmp(id).setValue(new Date(r.get(key)).format(fmt || "Y-m-d"))
	}
}

function setDateTextForForm(form, r, key, fmt){
	var val;
	if(val = r.get(key)){
		form.getForm().findField(key).setValue(new Date(val).format(fmt || 'Y-m-d'));
	}
}
/**
 * 内部方法
 * @param column
 * @param cw
 * @return
 */
function setColumnWidth(column, cw){
	if(cw > 1)
		column.width =  cw;
	else
		column.columnWidth = cw;
}

/**
 * 定义复杂表单
 * @param p
 * {
 * 	labelWidth: x,
 * defaultType: 'displayfield',
 *  rows:[{
 *  	／／列宽
 *  	cw: [.25, .25, 3]
 *  	cw: .5
 *  	／／列
 *  	cols: [{
 *  		xtype: "displayfield",
 *  		value: "xxx"
 *  		fieldLabel: "Test"
 *  	},{
 *  		xtype: "my97date",
 *  		fieldLabel: "Test"
 *  	}]
 *  },{
 *  	cw: 1,
 *  	cols:[{
 *  		xtype: "textarea",
 *  		fieldLabel: "123'
 *  	}]
 *  },{
 *  	//自定义行
 *  	define: true,
 *  	content:{
 *  		xtype: "pnael",
 *  		contentEl: "id"
 *  	}
 *  }]
 * }
 * 
 * annexation：需要覆盖的表单参数
 * @returns {Ext.form.FormPanel}
 */
function defineFormPanel(p, annexation){
	
	function getColumns(row, col_count){
		var columns = [];
		var isArray = false;
		if(row.cw instanceof Array){
			col_count = row.cw.length;
			isArray = true;
		}else{
			col_count = row.colNum || parseInt(1/row.cw);
		}
		for(var i = 0; i < col_count; i++){
			columns[i] = getColumnTmpl();
			if(isArray){
				setColumnWidth(columns[i], row.cw[i]);
			}else{
				setColumnWidth(columns[i], row.cw);
			}
		}
		var control
		for(var i = 0; i < row.cols.length; i++){			
			columns[i%col_count].push(row.cols[i]);
		}
		return columns
	}
	
	function getItems(){
		var rows = [];
		for(var i = 0; i < p.rows.length; i++){
			
			if(p.rows[i].define){
				rows.push(p.rows[i].content);
				continue;
			}
			rows.push({
				layout: "column",
				baseCls:"x-plain",
				defaults: {
					layout:'form',
					labelWidth: p.rows[i].labelWidth || p.labelWidth,
					defaultType: p.defaultType || "textfield",
					defaults:{
						anchor:'99%'
					}
				},
				items: getColumns(p.rows[i])
			});
		}
		return rows;
	}
	
	var construct = {
		layout:"form",
		labelWidth: p.labelWidth,
		border: false,
		style: "padding:5px",
		baseCls:"x-plain",
		items: getItems()
	};
	Ext.apply(construct, annexation); //合并参数
	return new Ext.form.FormPanel(construct);
}

/**
 * 根据grid生成defineFormPanel方法需要的参数
 * field上加cw属性，以指定宽度创建列
 * field上加newrow，开始新的一行
 * 调用方式 gridEditorFormJSON.call(grid)
 * @returns {___anonymous5398_5439}
 */
function gridEditorFormJSON(){
	var items = [];
	var fields = $yd.GridUtil.orderEditForm(this.fields, this.editOrder);
	var name, editor, initCw, sum, cw = initCw = 1 / this.saveFormColNum;
	var hidden = [], index = 0;
	
	for(var i = 0 ; i < fields.length; i++){
		if(fields[i].newrow){
			index++;
		}
		if(fields[i].cw){
			cw = fields[i].cw;
		}else{
			cw = initCw;
		}
		
		if(items[index] == undefined)
			items[index] = {cw: cw , cols: []};
		
		editor = fields[i].editor; 
		if(editor){
			if(editor.hiddenName == undefined){
				editor.name = fields[i].dataIndex;
			}
			if(editor.fieldLabel == undefined){						
				editor.fieldLabel = fields[i].header;
			}
		}else{
			editor = {};
			editor.name = fields[i].dataIndex;
			editor.fieldLabel = fields[i].header;
		}
		if(editor.xtype === 'hidden'){
			hidden.push(editor);
			continue;
		}
		items[index].cols.push(editor);
	}
	for(var i = 0; i < hidden.length; i++){
		items[index].cols.push(hidden[i]);
	}
	return {labelWidth: this.labelWidth, rows: items};
}

/**
 * 创建Grid表单
 * @param grid
 * @param annexation
 * @returns
 */
function createGridForm(grid, annexation){
	return defineFormPanel(gridEditorFormJSON.call(grid), annexation);
}
