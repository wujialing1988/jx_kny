/**
 * 该组件继承Ext.grid.GridPanel，基于GridPanel标准表格增删改查模式的封装。
 * 扩展内容：根据配置项自动生成数据容器，工具栏按钮（新增、删除、查询、刷新），分页工具，默认可实现增删改查的函数调用，支持多选删除等。
 * 统一了表格界面样式，操作方式，数据访问接口等。
 * @class Ext.yunda.Grid
 * @extends Ext.grid.GridPanel
 */
Ext.grid.ColumnModel.override({
	getTotalWidth: function(includeHidden){
    	var off = 0;
    	if(Ext.isChrome) off = 2;
    	
    	if(!this.totalWidth){
    		this.totalWidth = 0;
    		for(var i = 0, len = this.config.length; i < len; i++){
    			if(includeHidden || ! this.isHidden(i)){
    				this.totalWidth += this.getColumnWidth(i) + off;
    			}
    		}
    	}
    	return this.totalWidth;
    }
});

 
Ext.yunda.Grid = Ext.extend(Ext.grid.GridPanel, {
    /**
     * 该配置项非常重要，描述了store、cm、editor相关信息，基本配置项如下
     * [{
     *      dataIndex: "birthday",                   //绑定store的record字段名称
     *      header: "出生日期",                       //列头名
     *      xtype: "datecolumn",                    //列控件类型(如：datecolumn,numbercolumn,booleancolumn)，不设置该项默认为普通Column
     *      mapping: "birthday"                     //在store的reader中映射服务器端返回的字段
     *      hidden:false,                           //在列表中隐藏，行编辑模式下同时在新增修改界面中隐藏
     *      format:"Y-m-d",                         //如果使用日历控件，必须定义日期时间格式
     *      convert:                                //用于在Store容器中，Record字段的数据转换
     *      renderer:                               //在列中如何绘制显示
     *      editor:{                                 //字段编辑控件（配置项等同表单组件如datefield,numberfield等），默认textfield，对于combo及自定义组件，必须在此详细配置
     *         allowBlank: false,                   //是否可为空
     *         vtype:                               //Ext.form.VTypes简单通用的验证 
     *         validator：                          //验证器
     *      },
     *      //查询字段配置项（配置项等同表单组件如datefield,numberfield等），默认textfield，对于查询不建议使用combo及自定义组件，
     *      //如果不配置searcher，查询表单将依据editor配置项来生成
     *      searcher:{                              
     *         disabled:                            //默认false可用，作为查询字段在查询表单显示；true不可用，不出现在查询表单中
     *      }
     * },{
     *      ......
     * }]
     * 除了以上基本配置项之外，还可以使用基于GridPanel（Column）、JsonReader（Record）、FormPanel（Field）的配置项进行补充完善
     * @type [] 字段配置项数组
     */
    fields: null,                                       //*必须设置
    loadURL: "",                                        //*必须设置，装载列表数据的请求URL
    saveURL: "",                                        //*必须设置，保存数据的请求URL
    deleteURL: "",                                      //*必须设置，删除数据的请求URL
    storeAutoLoad: true,                                //表格的store初始化时是否自动装载数据
    singleSelect: false,                                //表格记录的行选择模式，true单选，默认false多选（表格出现多选框）
    hideRowNumberer: false,                             //默认false显示行号，true隐藏行号
    labelWidth: 125,                                     //查询表单中的标签宽度
    fieldWidth: 200,                                    //查询表单中的输入控件宽度
    remoteSort: true,                                   //默认true使用远程排序
    border: false,                                      //表格是否显示边框，默认false不显示边框
    viewConfig: {forceFit: true, markDirty: false},     //随着窗口（父容器）大小自动调整,默认true表示不出现滚动条，列宽会自动压缩
    enableColumnMove: true,                             //默认true可移动列
    loadMask: {msg: i18n.common.tip.loading },          //表格加载数据时，出现遮罩效果
    stripeRows: true,                                   //默认true，奇偶行变色
    selModel: null,                                     //默认null，表格行选择模式
    tbar: ['search','add','delete','refresh'],          //默认tbar按钮显示及顺序
    page: true,                                         //默认true将自动在表格底部生成分页工具栏，false不设置分页工具栏
    pageSize: null,                                     //默认分页大小
    pagingToolbar: null,                                //分页控件，默认null
    defaultData: {},                                    //新增时默认Record对象（json）
    searchForm: null,                                   //查询表单，默认null，可自定义配置，该表单存放在查询窗口容器中
    searchFormColNum: 1,                                //新增编辑表单中的控件用几列进行布局，默认1（大于4会强制等于4），可自定义配置，当列数>=2时，表单采用列布局
    searchWin: null,                                    //查询窗口，默认null，可自定义配置，点击查询按钮显示该窗口
    searchWinWidth: null,                               //查询窗口宽度，默认null，可自定义配置，若为null将自动根据this.labelWidth + this.fieldWidth + 30进行计算
    searchWinHeight: null,                              //查询窗口高度，默认null，可自定义配置，若为null查询窗口自适应高度
    saveForm: null,                                     //新增编辑表单，默认null，可自定义配置，该表单存放在新增编辑窗口容器中
    saveFormColNum: 1,                                  //新增编辑表单中的控件用几列进行布局，默认1（大于4会强制等于4），可自定义配置，当列数>=2时，表单采用列布局
    saveWin: null,                                      //新增编辑窗口，默认null，可自定义配置，点击新增按钮或双击行记录显示该窗口
    saveWinWidth: null,                                 //新增编辑窗口宽度，默认null，可自定义配置，若为null将自动根据this.labelWidth + this.fieldWidth + 30进行计算
    saveWinHeight: null,                                //新增编辑窗口高度，默认null，可自定义配置，若为null新增编辑窗口自适应高度 
    isEdit: true,										//设置是否可新增编辑删除的grid列表，默认为true可新增编辑删除，设为false则仅提供查询查看功能
    storeId:'idx',                                      //设置store的id，默认为idx
    editOrder: null,                                    //设置编辑表单中的显示位置的顺序，先行后列，数据结构如:['trainTypeIDX','trainTypeShortName','trainNo']
    searchOrder: null,                                  //设置查询表单中的显示位置的顺序，先行后列，数据结构如:['trainTypeIDX','trainTypeShortName','trainNo']
    sortInfo: null,									    //默认排序规划
    addButtonFn: function(){                            //点击新增按钮触发的函数，默认显示行编辑新增窗口，不建议覆盖该方法
    	if(this.beforeAddButtonFn() == false)   return;
        //判断新增删除窗体是否为null，如果为null则自动创建后显示
        if(this.saveWin == null)  this.createSaveWin();
        if(this.searchWin)  this.searchWin.hide();
        if(this.saveWin.isVisible())    this.saveWin.hide();
        if(this.beforeShowSaveWin() == false)   return;
        
        this.saveWin.setTitle(i18n.common.button.add);
        this.saveWin.show();
        this.saveForm.getForm().reset();
        this.saveForm.getForm().setValues(this.defaultData);
        
        this.afterShowSaveWin();
    },
    /**
     * 执行删除操作前触发的函数，如果返回false不执行删除动作，该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    beforeDeleteFn: function(){                           
        return true;
    },
    /**
     * 执行删除操作后触发的函数，
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
     * @return {Boolean}
     */
    afterDeleteFn: function(){ },
    deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
        if(this.saveWin)    this.saveWin.hide();
        if(this.searchWin)  this.searchWin.hide();        
        //未选择记录，直接返回
        if(!$yd.isSelectedRecord(this)) return;
        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
        if(!this.beforeDeleteFn()) return;
        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: this, url: this.deleteURL, params: {ids: $yd.getSelectedIdx(this, this.storeId)}
        });
    },
    searchButtonFn: function(){                         //点击查询按钮触发的函数
        //判断查询窗体是否为null，如果为null则自动创建后显示
        if(this.searchWin == null)  this.createSearchWin();
        if(this.saveWin)    this.saveWin.hide();
        this.searchWin.show();
    },
    refreshButtonFn: function(){                        //点击刷新按钮触发的函数
        self.location.reload();
    },
    /**
     * 查询前获取查询表单参数，此函数依赖searchForm（查询窗口是否创建）（可覆盖此方法重构查询）
     * @param searchParam 查询表单的Json对象
     * @return {} 返回的Json数据格式对象,
     */
    searchFn: function(searchParam){
    	this.store.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
        this.store.load({
            params: { entityJson: Ext.util.JSON.encode(searchParam) }       
        });	
    },
    /**
     * 基于该表格配置项信息，创建查询窗口，该函数依赖searchButtonFn，会在用户第一次点击“查询”按钮而被调用
     * 如果searchButtonFn被覆盖则失效不被调用，或者手动调用生成查询窗口
     */
    createSearchWin: function(){
        if(this.searchForm == null) this.createSearchForm();
        //计算查询窗体宽度
        if(this.searchWinWidth == null)
        this.searchWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.searchFormColNum + 60;
        this.searchWin = new Ext.Window({
            title:i18n.common.button.research, width:this.searchWinWidth, height:this.searchWinHeight, plain:true, closeAction:"hide",
            buttonAlign:'center', maximizable:true, items:this.searchForm, autoHeight:true,
            buttons: [{
                text: i18n.common.button.research, iconCls: "searchIcon", scope: this, handler: function(){ 
                    var searchParam = this.searchForm.getForm().getValues();
                    searchParam = MyJson.deleteBlankProp(searchParam);
                    this.searchFn(searchParam) ;
                }
            }, {
                text: i18n.common.button.reset, iconCls: "resetIcon", scope: this, handler: function(){ 
                    this.searchForm.getForm().reset();
                    var my97Ary = this.searchWin.findByType('my97date');
                    if(Ext.isEmpty(my97Ary) || !Ext.isArray(my97Ary)){
                    	
                    }else{
                    	for(var i = 0; i < my97Ary.length; i++){
	                        my97Ary[ i ].setValue('');
	                    }
                    }                           
                 	//清空自定义组件的值
                    var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree","TecProcessNodeSelect_comboTree",
                    		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree",
                    		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
                    		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","RCRT_combo","TrainRC_combo","RT_SelectWin",
                    		"BureauSelect_comboTree","DeportSelect_comboTree","TeamEmployee_SelectWin","combo","Base_multyComboTree","Base_comboTree"];
                    for(var j = 0; j < componentArray.length; j++){
                    	var component = this.searchWin.findByType(componentArray[j]);
                    	if(Ext.isEmpty(component) || !Ext.isArray(component)){
                    		continue;
                    	}else{
                    		for(var i = 0; i < component.length; i++){
		                        component[ i ].clearValue();
		                    }
                    	}	                    
                    }
                    searchParam = {} ;
                    this.searchFn(searchParam);
                }
            }, {
                text: i18n.common.button.close, iconCls: "closeIcon", scope: this, handler: function(){ this.searchWin.hide(); }                
            }]
        });
    },
    /**
     * 基于该表格配置项信息，创建查询表单
     * 不建议直接调用该函数，默认的createSearchWin函数体中会判断this.searchForm是否为空再调用该函数创建searchForm
     */
    createSearchForm: function(){
        //根据fields配置项来生成查询字段
        if (this.fields == null) return;
        var searchFields = [];
        var fields = this.fields;
        
        for (var i = 0; i < fields.length; i++) {
            var field = fields[ i ];
            if(field.hidden == true || field.dataIndex == null)  continue;
            
            var editor = {};
            Ext.apply(editor, field.editor);            
            //该字段是否有查询配置项，则使用查询配置生成查询表单字段
            if(field.searcher != null){
	            var searcher = {};
	            Ext.apply(searcher, field.searcher);
	            if(searcher.disabled == true) continue;
                
	            //如果是日期类型，使用my97date控件
	            if(field.xtype == 'datecolumn' || searcher.xtype == 'my97date' || searcher.xtype == 'datefield') {
	                searcher.xtype = 'my97date';
	                searcher.format = searcher.format || (field.format || (editor.format || "Y-m-d"));
	            }                
	            searcher.fieldLabel = searcher.fieldLabel || field.header;
	            searcher.name = searcher.name || field.dataIndex;
	            searcher.width = searcher.width || this.fieldWidth;
                searchFields.push(searcher);
                continue;
            }
            //如果没有查询配置项，则使用字段编辑配置项来生成查询表单字段

            //如果是隐藏控件，则不添加到查询表单容器
            if(editor.xtype == 'hidden')    continue;
            //如果是日期类型，使用my97date控件
            if(field.xtype == 'datecolumn' || editor.xtype == 'my97date' || editor.xtype == 'datefield') {
                editor.xtype = 'my97date';
                editor.format = editor.format || (field.format || "Y-m-d");
            }
            delete editor.allowBlank;
            delete editor.vtype;
            delete editor.validator;
            delete editor.id;
            
            editor.disabled = false;
            editor.fieldLabel = editor.fieldLabel || field.header;
            editor.name = editor.name || field.dataIndex;
            editor.width = editor.width || this.fieldWidth;
            
            searchFields.push(editor);
        }
        //设置编辑表单的字段显示顺序
        searchFields = $yd.GridUtil.orderSearchForm(searchFields, this.searchOrder);
        var formItems = searchFields;
        if(this.searchFormColNum >= 2){
            //当列数saveFormColNum>=2时，表单采用列布局
            formItems = {xtype:"panel", border:false, baseCls:"x-plain", layout:"column", align:"center", items:[]};
            //每列百分比宽度
            var columnWidth = 1 / this.searchFormColNum;
            //创建每列panle的配置项
            for(var i = 0; i < this.searchFormColNum; i++){
                formItems.items.push({
                    baseCls:"x-plain", align:"center", style:"padding:3px", layout:"form", defaultType:"textfield", labelWidth:this.labelWidth, columnWidth: columnWidth, items:[]
                });
            }
            var j = 0;
            for(var i = 0; i < searchFields.length; i++){
                formItems.items[ j ].items.push(searchFields[ i ]);
                j++;
                if(j >= formItems.items.length) j = 0;
            }
        }   
        this.searchForm = new Ext.form.FormPanel({
            layout: "form",     border: false,      style: "padding:10px",      labelWidth: this.labelWidth,
            baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "95%" },
            items: formItems
        });
    },
    /**
     * 基于该表格配置项信息，创建新增编辑窗口该函数依赖addButtonFn或toEditFn，会在用户第一次点击“新增”按钮或双击行记录而被调用
     * 如果searchButtonFn或toEditFn都被覆盖则失效不被调用，或者手动调用生成窗口
     */
    createSaveWin: function(){
        if(this.saveForm == null) this.createSaveForm();
        //计算查询窗体宽度
        if(this.saveWinWidth == null)   this.saveWinWidth = (this.labelWidth + this.fieldWidth + 8) * this.saveFormColNum + 60;
        this.saveWin = new Ext.Window({
            title:i18n.common.button.add, width:this.saveWinWidth, height:this.saveWinHeight, plain:true, closeAction:"hide",
            buttonAlign:'center', maximizable:true, items:this.saveForm, autoHeight:true,
            buttons: [{
                text: i18n.common.button.save, iconCls: "saveIcon", scope: this, handler: this.saveFn
            }, {
                text: i18n.common.button.close, iconCls: "closeIcon", scope: this, handler: function(){ this.saveWin.hide(); }
            }]
        });
    },
    /**
     * 基于该表格配置项信息，创建新增编辑表单，根据fields配置项来生成输入控件
     * 不建议直接调用该函数，默认的createSaveWin函数体中会判断this.saveForm是否为空再调用该函数创建saveForm
     */
    createSaveForm: function(){
        if (this.fields == null) return;
        //可现实的输入控件
        var saveFields = [];
        //隐藏的控件
        var hiddenFields = [];
        var fields = this.fields;
        //设置编辑表单的字段显示顺序
        fields = $yd.GridUtil.orderEditForm(fields, this.editOrder); 
        
        for (var i = 0; i < fields.length; i++) {
            var field = fields[ i ];
            if(field.dataIndex == null)  continue;

            var editor = {};
            Ext.apply(editor, field.editor);
            editor.name = editor.name || field.dataIndex;
            
            //如果是隐藏控件，添加到hiddenFields数组中， 在后面统一处理
            if(editor.xtype == 'hidden'){
                hiddenFields.push(editor);
                continue;
            }
            //如果是日期类型，使用my97date控件
            if(field.xtype == 'datecolumn' || editor.xtype == 'my97date' || editor.xtype == 'datefield') {
                editor.xtype = 'my97date';
                editor.format = editor.format || (field.format || "Y-m-d");
            }
            
            editor.fieldLabel = editor.fieldLabel || field.header;
            editor.name = editor.name || field.dataIndex;
            editor.width = editor.width || this.fieldWidth;
            saveFields.push(editor);
        }
        
        var formItems = saveFields;
        if(this.saveFormColNum >= 2){
            //当列数saveFormColNum>=2时，表单采用列布局
            formItems = {xtype:"panel", border:false, baseCls:"x-plain", layout:"column", align:"center", items:[]};
            //每列百分比宽度
            var columnWidth = 1 / this.saveFormColNum;
            //创建每列panle的配置项
            for(var i = 0; i < this.saveFormColNum; i++){
                formItems.items.push({
                    baseCls:"x-plain", align:"center", style:"padding:3px", layout:"form", defaultType:"textfield", labelWidth:this.labelWidth, columnWidth: columnWidth, items:[]
                });
            }
            var j = 0;
            for(var i = 0; i < saveFields.length; i++){
                formItems.items[ j ].items.push(saveFields[ i ]);
                j++;
                if(j >= formItems.items.length) j = 0;
            }
            
            //添加隐藏的控件
            for(var i = 0; i < hiddenFields.length; i++){
                formItems.items.push( hiddenFields[ i ] );
            }
        } else {
            //添加隐藏的控件
            for(var i = 0; i < hiddenFields.length; i++){
                formItems.push( hiddenFields[ i ] );
            }            
        }
        //生成FormPanel
        this.saveForm = new Ext.form.FormPanel({
            layout: "form",     border: false,      style: "padding:10px",      labelWidth: this.labelWidth,
            baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "98%" },
            items: formItems
        });
    },
    /**
     * 在双击表格中记录时（rowdblclick）触发该函数，获取当前选中记录，并将选中记录加载到编辑表单，显示编辑窗口
     * 在加载数据显示窗口之前，将执行beforeShowEditWin函数，返回false将不显示编辑窗口，中止编辑动作
     * @param {Ext.yunda.Grid} grid 当前表格对象
     * @param {Number} rowIndex 选中行下标
     * @param {Ext.EventObject} e Ext事件对象
     */    
    toEditFn: function(grid, rowIndex, e){
        //判断新增编辑窗体是否为null，如果为null则自动创建后显示
        if(this.saveWin == null)  this.createSaveWin();
        if(this.searchWin != null)  this.searchWin.hide();  
        var record = this.store.getAt(rowIndex);
        //是否显示编辑窗口，中止或继续编辑动作
        if(!this.beforeShowEditWin(record, rowIndex))     return;
        //显示编辑窗口
        if(this.isEdit){
        	this.saveWin.setTitle(i18n.common.button.edit);
	        this.saveWin.show();
	        this.saveForm.getForm().reset();
	        this.saveForm.getForm().loadRecord(record);
        } else {   //显示查看窗口
        	this.saveWin.setTitle(i18n.common.button.view);
        	this.saveWin.show();
	        this.saveForm.getForm().reset();
	        this.saveForm.getForm().loadRecord(record);
	        this.saveWin.buttons[0].setVisible(false);
	        this.disableAllColumns();
        }        
        //显示编辑窗口后触发函数（可能需要对某些特殊控件赋值等操作）
        this.afterShowEditWin(record, rowIndex);        
    },
    /**
     * 进入编辑窗口之前触发的函数，如果返回false将不显示编辑窗口
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     * @return {Boolean} 如果返回fasle将不显示编辑窗口
     */
    beforeShowEditWin: function(record, rowIndex){  
    	//清空自定义组件的值
        var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree","BureauSelect_comboTree","DeportSelect_comboTree",
        		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree",
        		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
        		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","RCRT_combo","TrainRC_combo","RT_SelectWin"];
        for(var j = 0; j < componentArray.length; j++){
        	var component = this.saveWin.findByType(componentArray[j]);
        	if(Ext.isEmpty(component) || !Ext.isArray(component)){
        	}else{
        		for(var i = 0; i < component.length; i++){
                    component[ i ].clearValue();
                }
        	}	                    
        }
    	return true; 
    },    
    /**
     * 选中记录加载到编辑表单，显示编辑窗口后触发该函数
     * 该函数依赖toEditFn，若默认toEditFn被覆盖则失效
     * @param {Ext.data.Record} record 当前选中记录
     * @param {Number} rowIndex 选中行下标
     */
    afterShowEditWin: function(record, rowIndex){},
    /**
     * 进入新增窗口之前触发的函数，如果返回false将不显示新增窗口
     * 该函数依赖saveFn，若默认saveFn被覆盖则失效
     */
    beforeShowSaveWin: function(){  
    	//清空自定义组件的值
        var componentArray = ["TrainType_combo","Base_combo","TrainNo_combo","ProfessionalType_comboTree","Base_comboTree",
        		"PartsClass_comboTree","MatClass_comboTree","OmOrganization_comboTree","OmOrganizationCustom_comboTree", "UnderTakeDep_comboTree",
        		"EosDictEntry_combo","OmEmployee_SelectWin","GyjcFactory_SelectWin","PartsAccount_SelectWin","PartsStock_SelectWin",
        		"PartsTypeAndQuota_SelectWin","XC_combo","BuildUpType_comboTree","RCRT_combo","TrainRC_combo","RT_SelectWin","BureauSelect_comboTree","DeportSelect_comboTree"];
        for(var j = 0; j < componentArray.length; j++){
        	var component = this.saveWin.findByType(componentArray[j]);
        	if(Ext.isEmpty(component) || !Ext.isArray(component)){
        	}else{
        		for(var i = 0; i < component.length; i++){
                    component[ i ].clearValue();
                }
        	}	                    
        }
        return true; 
    },   
    /**
     * 点击新增按钮触发事件前调用的方法。
     * */
    beforeAddButtonFn: function(){
    	return true;
    },
    /**
     * 显示新增窗口后触发该函数，该函数依赖addButtonFn，若默认addButtonFn被覆盖则失效
     */
    afterShowSaveWin: function(){},    
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Object} data 要保存的数据记录，json格式
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(data){ return true; },
    
    /**
     * 新增编辑窗口保存按钮触发的函数，执行数据数据保存动作
     */
    saveFn: function(){
        //表单验证是否通过
        var form = this.saveForm.getForm(); 
        if (!form.isValid()) return;
        
        //获取表单数据前触发函数
        this.beforeGetFormData();
        var data = form.getValues();
        //获取表单数据后触发函数
        this.afterGetFormData();
        
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(data)) return;
        
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: data,
            success: function(response, options){
                if(self.loadMask)   self.loadMask.hide();
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    this.afterSaveSuccessFn(result, response, options);
                } else {
                    this.afterSaveFailFn(result, response, options);
                }
            }
        };
        Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
    },
    /**
     * 获取表单数据前触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */
    beforeGetFormData: function(){},
    /**
     * 获取表单数据后触发(覆盖该函数可用于修改表单字段enable、disabled等)，
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     */    
    afterGetFormData: function(){},    
    /**
     * 保存成功之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveSuccessFn: function(result, response, options){
        this.store.reload();
        alertSuccess();
    },
    /**
     * 保存失败之后执行的函数，该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {} result 服务器端返回的json对象
     * @param {} response 原生的服务器端返回响应对象
     * @param {} options 参数项
     */
    afterSaveFailFn: function(result, response, options){
    	this.store.reload();
        alertFail(result.errMsg);
    },
    /**
     * 根据dataIndexAry，设置saveForm中这些列的输入控件，不可用
     * @param {Array} dataIndexAry 列配置项dataIndex
     */
    disableColumns: function(dataIndexAry){
        if(Ext.isEmpty(this.saveForm)){
            alert(i18n.common.tip.disableColumns);
            return;
        }        
        if(Ext.isEmpty(dataIndexAry) || !Ext.isArray(dataIndexAry))   return;
        for(var i = 0; i < dataIndexAry.length; i++){
            var field = this.saveForm.find('name', dataIndexAry[ i ]);
            if(Ext.isEmpty(field) || !Ext.isArray(field))  continue;
            field[ 0 ].disable();
        }        
    },
   /**
     * 设置saveForm中所有列的输入控件，不可用
     */    
    disableAllColumns: function(){
        if(Ext.isEmpty(this.saveForm)){
            alert(i18n.common.tip.disableAllColumns);
            return;
        }
    	var colCfg = this.fields ;
        for (var j = 0; j < colCfg.length; j++) {
            if(colCfg[ j ].dataIndex != null )  {
            	var field = this.saveForm.find('name',colCfg[ j ].dataIndex);
                if(field[ 0 ] && field[ 0 ].disable)    field[ 0 ].disable();
            }
        }
    },      
    /**
     * 根据dataIndexAry，设置saveForm中这些列的输入控件，不可用
     * @param {Array} dataIndexAry 列配置项dataIndex
     */    
    enableColumns: function(dataIndexAry){
        if(Ext.isEmpty(this.saveForm)){
            alert(i18n.common.tip.enableColumns);
            return;
        }        
        if(Ext.isEmpty(dataIndexAry) || !Ext.isArray(dataIndexAry))   return;
        for(var i = 0; i < dataIndexAry.length; i++){
            var field = this.saveForm.find('name', dataIndexAry[ i ]);
            if(Ext.isEmpty(field) || !Ext.isArray(field))  continue;
            field[ 0 ].enable();
        }
    },
    /**
     * 设置saveForm中所有列的输入控件，可用
     */    
    enableAllColumns: function(){
        if(Ext.isEmpty(this.saveForm)){
            alert(i18n.common.tip.enableAllColumns);
            return;
        }        
        var fields = this.fields;
        for(var i = 0; i < fields.length; i++){
            if(fields[ i ].dataIndex){
            	var field = this.saveForm.find('name', fields[ i ].dataIndex)[ 0 ];
            	if(field != null && field.enable)  	field.enable();
            }
        }
    },    
    
    /**
     * 根据dataIndexAry，设置saveForm中这些列的输入控件，只读
     * @param {Array} dataIndexAry 列配置项dataIndex
     */
    setReadOnlyColumns: function(dataIndexAry,flag){
        if(Ext.isEmpty(this.saveForm)){
            alert(i18n.common.tip.disableColumns);
            return;
        }        
        if(Ext.isEmpty(dataIndexAry) || !Ext.isArray(dataIndexAry))   return;
        for(var i = 0; i < dataIndexAry.length; i++){
            var field = this.saveForm.find('name', dataIndexAry[ i ]);
            if(Ext.isEmpty(field) || !Ext.isArray(field)) {
            	continue;
            }else{
            	field[0].setReadOnly(flag);
            	if(flag){
            		field[0].addClass(field[0].disabledClass);
            	}else{
					field[0].removeClass(field[0].disabledClass);          	
            	}
            }
        }        
    },
    
    //private:构造器
    constructor: function(cfg){
        this.viewConfig = {forceFit: true, markDirty: false};   //随着窗口（父容器）大小自动调整,默认true表示不出现滚动条，列宽会自动压缩
        this.loadMask = {msg: i18n.common.tip.loading};              //表格加载数据时，出现遮罩效果，默认显示'正在处理，请稍侯...'
        this.tbar = ['search','add','delete','refresh'];        //默认tbar按钮显示及顺序
        this.defaultData = {};                                  //新增时默认Record对象（json）
        //如果配置项为null，给cfg赋值一个实例化的空JSON
        cfg = cfg || {};
        Ext.apply(this, cfg);

        //配置表格数据容器，在this.store未定义的情况下，根据fields配置项来生成Store
        if (this.store == null && this.fields != null) {
            var store_fields = [];
            var fields = this.fields;
            for (var i = 0; i < fields.length; i++) {
                var field = Ext.apply({}, fields[ i ]);
                if(field.dataIndex == null) continue;
                
                field.name = field.dataIndex;
                if(field.xtype == 'datecolumn' || (field.editor != null && ( field.editor.xtype == 'my97date' || field.editor.xtype == 'datefield'))) {
                    field.type = 'date';
                    field.dateFormat = field.dateFormat || "time";
                }
                delete field.dataIndex;
                delete field.header;
                delete field.hidden;
                delete field.xtype;
                delete field.format;
                delete field.allowBlank;
                delete field.renderer;
                delete field.validator;
                delete field.vtype;                
                store_fields.push(field);
            }
            this.store = new Ext.data.JsonStore({
                id:this.storeId, 
                root:"root", 
                totalProperty:"totalProperty", 
                autoLoad: this.storeAutoLoad,
                remoteSort: this.remoteSort,
                url: this.loadURL,
                fields: store_fields,
                sortInfo: this.sortInfo
            }); 
        }
        
        if(this.tbar != null){
	        //新增按钮的配置,配置工具栏按钮,['search','add','delete','refresh']
	        if (null != this.saveURL && this.saveURL.trim() != "") {
	            this.tbar = $yd.repalceArrayNode(this.tbar, 'add', { text:i18n.common.button.add, iconCls:"addIcon", scope:this, handler: this.addButtonFn });
	        } else {
	            this.tbar.remove('add');
	        }
	        //删除按钮的配置
	        if (null != this.deleteURL && this.deleteURL.trim() != "") {
	            this.tbar = $yd.repalceArrayNode(this.tbar, 'delete', { text:i18n.common.button.del, iconCls:"deleteIcon", scope:this, handler: this.deleteButtonFn });            
	        } else {
	            this.tbar.remove('delete');   
	        }
	        //查询按钮的配置
	        this.tbar = $yd.repalceArrayNode(this.tbar, 'search', { text:i18n.common.button.research, iconCls:"searchIcon", scope:this, handler: this.searchButtonFn });        
	        //刷新按钮的配置
	        this.tbar = $yd.repalceArrayNode(this.tbar, 'refresh', { text:i18n.common.button.refresh, iconCls:"refreshIcon", handler: this.refreshButtonFn });     
        }
        //行选择模式，singleSelect=true表示单选，默认false多选并在表格中显示多选框
        if(this.selModel == null && this.sm == null){
            if(this.singleSelect){
                this.selModel = new Ext.grid.RowSelectionModel({singleSelect:true});
            } else {
                this.selModel = new Ext.grid.CheckboxSelectionModel({singleSelect:false});
                this.selModel.getEditor = function(){  return null; };
            }
        }
      
        //在columns、cm和colModel未定义的情况下，根据fields配置项来生成colModel，生成表格列配置项，同时配置列编辑模式
        if (this.cm == null && this.colModel == null && this.columns == null && this.fields != null) {
            var colModel = [];
            //添加序号
            if(this.hideRowNumberer == false) colModel.push(new Ext.grid.RowNumberer());
            //如果行选择模式为多选框列添加到colModel
            var sm = this.sm || this.selModel;
            if(sm instanceof Ext.grid.CheckboxSelectionModel)   colModel.push(sm);
            
            var fields = this.fields;
            for (var i = 0; i < fields.length; i++) {
                //配置列
                var col = {};
                Ext.apply(col, fields[ i ]);
                //配置editor
                var editor = {};
                Ext.apply(editor, fields[ i ].editor);
                //如果是日期性
                if(col.xtype == 'datecolumn' || editor.xtype == 'my97date' || editor.xtype == 'datefield') {
                    col.format = col.format || (editor.format || "Y-m-d");
                    editor.xtype = 'my97date';
                    editor.format = editor.format || (col.format || "Y-m-d");
                }
                col.editor = editor;
                if(col.sortable == null)	col.sortable = col.dataIndex == null ? false : true;
                if(col.dataIndex == null && col.getEditor == null)   col.getEditor = function(){  return null; };
                
                delete col.mapping;
                delete col.allowBlank;
                delete col.vtype;
                delete col.validator;
                delete col.convert;                
                colModel.push(col);
            }
            //行编辑模式下暂不考虑列锁定模式，以普通列方式实例化
            this.colModel = new Ext.grid.ColumnModel(colModel);
        }
        
        //配置分页工具栏
        if(this.bbar == null && this.pagingToolbar == null && this.page){
            this.pagingToolbar = $yd.createPagingToolbar({pageSize:this.pageSize, store: this.store});
            this.bbar = this.pagingToolbar;
        }
        //表单列布局数，大于4会强制等于4
        this.saveFormColNum = this.saveFormColNum > 4 ? 4 : this.saveFormColNum;
        this.searchFormColNum = this.searchFormColNum > 4 ? 4 : this.searchFormColNum;        
        
        //调用父类的构造器
        Ext.yunda.Grid.superclass.constructor.call(this);
        
        //注册表格行双击事件：双击行记录进行编辑操作
        this.on('rowdblclick', this.toEditFn, this);
        
        //判断基于当前页面的遮罩控件是否存在，若不存在创建一个便于后续操作过程中调用
        if(self.loadMask == null)  self.loadMask = new Ext.LoadMask(Ext.getBody(), { msg: i18n.common.tip.loading });
    },
    //private:初始化组件
    initComponent : function() {
        Ext.yunda.Grid.superclass.initComponent.call(this);
    },
    //private:销毁组件
    destroy : function() {
        Ext.yunda.Grid.superclass.destroy.apply(this,arguments);
    }
});
