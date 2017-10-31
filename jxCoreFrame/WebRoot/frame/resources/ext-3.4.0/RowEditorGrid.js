/**
 * 该组件继承Ext.grid.GridPanel，基于行编辑表格模式的封装，
 * 扩展内容：根据配置项自动生成数据容器，工具栏按钮（新增、删除、查询、刷新），分页工具，默认可实现增删改查的函数调用，新增修改都基于行编辑模式（包括输入控件验证），支持多选删除等。
 * 统一了表格界面样式，操作方式，数据访问接口等。
 * @class Ext.yunda.RowEditorGrid
 * @extends Ext.grid.GridPanel
 */
Ext.yunda.RowEditorGrid = Ext.extend(Ext.grid.GridPanel, {
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
     *      editor{                                 ////字段编辑控件（表单组件如datefield,numberfield等），默认textfield，对于combo及自定义组件，必须在此详细配置
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
    labelWidth: 80,                                     //查询表单中的标签宽度
    fieldWidth: 150,                                    //查询表单中的输入控件宽度
    remoteSort: true,                                   //默认true使用远程排序
    border: false,                                      //表格是否显示边框，默认false不显示边框
    viewConfig: {forceFit: true, markDirty: false},     //随着窗口（父容器）大小自动调整,默认true表示不出现滚动条，列宽会自动压缩
    enableColumnMove: false,                            //默认false不能移动列
    loadMask: {msg: "正在处理，请稍侯..."},                //表格加载数据时，出现遮罩效果
    stripeRows: true,                                   //默认true，奇偶行变色
    selModel: null,                                     //默认null，表格行选择模式
    tbar: ['search','add','delete','refresh'],          //默认tbar按钮显示及顺序
    page: true,                                         //默认true将自动在表格底部生成分页工具栏，false不设置分页工具栏
    pageSize: null,                                     //默认分页大小
    pagingToolbar: null,                                //分页控件，默认null
    recordConstructor: null,                            //符合该表格Store容器中Record的构造函数
    defaultData: {},                                    //新增时默认Record对象（json）
    rowEditor: null,                                    //行编辑插件，默认null，可自定义配置
    searchForm: null,                                   //查询表单，默认null，可自定义配置，该表单存放在查询窗口容器中
    searchWin: null,                                    //查询窗口，默认null，可自定义配置，点击查询按钮显示该窗口
    searchWinWidth: null,                               //查询窗口宽度，默认null，可自定义配置，若为null将自动根据this.labelWidth + this.fieldWidth + 30进行计算
    searchWinHeight: null,                              //查询窗口高度，默认null，可自定义配置，若为null查询窗口自适应高度
    storeId:'idx',                                      //设置store的id，默认为idx
    searchOrder: null,                                  //设置查询表单中的显示位置的顺序，先行后列，数据结构如:['trainTypeIDX','trainTypeShortName','trainNo']
    /**
     * 执行新增按钮前，设置动态属性值，该函数依赖addButtonFn方法，会在用户第一次点击“新增”按钮而被调用
     * @return {Boolean}
     */
    beforeAddButtonFn: function(){
		return true;   	
    },
    /**点击新增按钮后的操作*/
    afterAddButtonFn: function(){   },
    addButtonFn: function(){                            //点击新增按钮触发的函数，默认显示行编辑新增记录界面，不建议覆盖该方法
        this.rowEditor.stopEditing(false); 
        //点击新增按钮前，操作事件，主要针对默认数据的动态赋值
        if(!this.beforeAddButtonFn()) return;
        var initData = Ext.apply({}, this.defaultData); 
        var record = new this.recordConstructor(initData);
        this.store.insert(0, record); 
        this.getView().refresh(); 
        this.getSelectionModel().selectRow(0); 
        this.rowEditor.startEditing(0);
        this.afterAddButtonFn();
    },
    /**
     * 执行删除操作前触发的函数，如果返回false不执行删除动作
     * 该函数依赖deleteButtonFn，如果deleteButtonFn被覆盖则失效
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
        //判断查询询窗体是否为null，如果为null则自动创建后显示
        if(this.searchWin == null)  this.createSearchWin();
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
        this.searchWinWidth = this.labelWidth + this.fieldWidth + 50;
        this.searchWin = new Ext.Window({
            title:"查询", width:this.searchWinWidth, height:this.searchWinHeight, plain:true, closeAction:"hide", buttonAlign:'center', maximizable:true, items:this.searchForm, 
            buttons: [{
                text: "查询", iconCls: "searchIcon", scope: this, handler: function(){ 
                    var searchParam = this.searchForm.getForm().getValues();
                    searchParam = MyJson.deleteBlankProp(searchParam);
                    this.searchFn(searchParam) ;
                }
            }, {
                text: "重置", iconCls: "resetIcon", scope: this, handler: function(){ 
                    this.searchForm.getForm().reset();
                    var my97Ary = this.searchWin.findByType('my97date');
                    
                    if(my97Ary != null && my97Ary.length > 0){
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
                    var searchParam = {};
                    this.searchFn(searchParam);                    
                }
            }, {
                text: "关闭", iconCls: "closeIcon", scope: this, handler: function(){ this.searchWin.hide(); }                
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
                    searcher.format = searcher.format || (field.format || (editor.format || "Y-m-d") );
                }                
                searcher.fieldLabel = searcher.fieldLabel || field.header;
                searcher.name = searcher.name || field.dataIndex;
                searcher.width = searcher.width || this.fieldWidth;
                searchFields.push(searcher);
                continue;
            }            
            //如果没有查询配置项，则使用字段编辑配置项来生成查询表单字段
            //如果是日期性
            if(editor.xtype == 'datecolumn'){
                editor.xtype = 'my97date';
                editor.format = editor.format || (col.format || "Y-m-d");
            }
            delete editor.allowBlank;
            delete editor.vtype;
            delete editor.validator;
            
            editor.disabled = false;
            editor.fieldLabel = field.header;
            editor.name = field.dataIndex;
            editor.width = this.fieldWidth;
            searchFields.push(editor);
        }
        // 修改在chrome浏览器中，行编辑表格自动生成的查询窗口不能正常显示高度的问题（HeTao modified by 2015-05-25）
        // 根据查询窗口字段数计算查询窗口高度
        this.searchWinHeight = searchFields.length * 28 + 80;
        //设置编辑表单的字段显示顺序
        searchFields = $yd.GridUtil.orderSearchForm(searchFields, this.searchOrder);
        this.searchForm = new Ext.form.FormPanel({
            layout: "form",     border: false,      style: "padding:10px",      labelWidth: this.labelWidth,
            baseCls: "x-plain", align: "center",    defaultType: "textfield",   defaults: { anchor: "98%" },
            items: searchFields
        });
    },
    /**
     * 行编辑操作之前触发的函数，如果返回false将不执行行编辑动作
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Number} rowIndex The rowIndex of the row just edited
     * @return {Boolean} 如果返回fasle将不执行行编辑动作
     */
    beforeEditFn: function(rowEditor, rowIndex){
        return true;
    },
    /**
     * 保存记录之前的触发动作，如果返回fasle将不保存记录
     * 该函数依赖saveFn触发，如果saveFn被覆盖则失效
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Object} changes Object with changes made to the record.
     * @param {Ext.data.Record} record The Record that was edited.
     * @param {Number} rowIndex The rowIndex of the row just edited
     * @return {Boolean} 如果返回fasle将不保存记录
     */
    beforeSaveFn: function(rowEditor, changes, record, rowIndex){
        return true;
    },
    /**
     * 行编辑保存按钮触发的函数
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Object} changes Object with changes made to the record.
     * @param {Ext.data.Record} record The Record that was edited.
     * @param {Number} rowIndex The rowIndex of the row just edited
     */
    saveFn: function(rowEditor, changes, record, rowIndex){
        //调用保存前触发函数，如果返回fasle将不保存记录
        if(!this.beforeSaveFn(rowEditor, changes, record, rowIndex)) {
            rowEditor.stopEditing(false);
            return;
        }
        if(self.loadMask)   self.loadMask.show();
        var cfg = {
            scope: this, url: this.saveURL, jsonData: record.data,
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
     * private:行编辑取消按钮触发的函数
     * @param {Ext.ux.grid.RowEditor} rowEditor This object
     * @param {Boolean} forced True if the cancel button is pressed, false is the editor was invalid.
     */
    cancelFn: function(rowEditor, pressed){
        var idx = rowEditor.record.get(this.storeId);
        //如果是临时记录，删除该记录
        if(idx == null || '' == idx.trim()) {
            this.store.removeAt(rowEditor.rowIndex);
            rowEditor.grid.view.refresh();
        }
        rowEditor.grid.view.refresh();
    },
    /**
     * 根据dataIndexAry，设置这些列的输入控件，不可用
     * @param {Array} dataIndexAry 列配置项dataIndex
     */
    disableColumns: function(dataIndexAry){
        if(dataIndexAry == null || dataIndexAry.length < 1) return;
        var colCfg = this.colModel.config;
        for (var i = 0; i < dataIndexAry.length; i++) {
            for (var j = 0; j < colCfg.length; j++) {
                if(dataIndexAry[ i ] == colCfg[ j ].dataIndex && colCfg[ j ].editor != null ){
                    colCfg[ j ].editor.disable();
                    break;
                }
            }
        }
    },
    /**
     * 设置表格所有列的输入控件，不可用，不影响多选框列、序号列
     */    
    disableAllColumns: function(){
        var colCfg = this.colModel.config;
        for (var j = 0; j < colCfg.length; j++) {
            if(colCfg[ j ].editor != null ) colCfg[ j ].editor.disable();
        }
    },    
    /**
     * 根据dataIndexAry，设置这些列的输入控件，不可用
     * @param {Array} dataIndexAry 列配置项dataIndex
     */    
    enableColumns: function(dataIndexAry){
        if(dataIndexAry == null || dataIndexAry.length < 1) return;
        var colCfg = this.colModel.config;
        for (var i = 0; i < dataIndexAry.length; i++) {
            for (var j = 0; j < colCfg.length; j++) {
                if(dataIndexAry[ i ] == colCfg[ j ].dataIndex && colCfg[ j ].editor != null ){
                    colCfg[ j ].editor.enable();
                    break;
                }
            }
        }        
    },
    /**
     * 设置表格所有列的输入控件，可用，不影响多选框列、序号列
     */    
    enableAllColumns: function(){
        var colCfg = this.colModel.config;
        for (var j = 0; j < colCfg.length; j++) {
            if(colCfg[ j ].editor != null ) colCfg[ j ].editor.enable();
        }
    },     
    //private:构造器
    constructor: function(cfg){
        this.viewConfig = {forceFit: true, markDirty: false};   //随着窗口（父容器）大小自动调整,默认true表示不出现滚动条，列宽会自动压缩
        this.loadMask = {msg: "正在处理，请稍侯..."};              //表格加载数据时，出现遮罩效果，默认显示'正在处理，请稍侯...'
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
                if(field.xtype == 'datecolumn') {
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
                fields: store_fields
            }); 
        }
        //设置新增时Record的构造函数
        this.recordConstructor = this.recordConstructor || Ext.data.Record.create(this.store.fields.items);
        if(this.tbar != null){
	        //新增按钮的配置,配置工具栏按钮,['add','delete','search','refresh']
	        if (null != this.saveURL && this.saveURL.trim() != "") {
	            this.tbar = $yd.repalceArrayNode(this.tbar, 'add', { text:"新增", iconCls:"addIcon", scope:this, handler: this.addButtonFn });
	        } else {
	            this.tbar.remove('add');
	        }
	        //删除按钮的配置
	        if (null != this.deleteURL && this.deleteURL.trim() != "") {
	            this.tbar = $yd.repalceArrayNode(this.tbar, 'delete', { text:"删除", iconCls:"deleteIcon", scope:this, handler: this.deleteButtonFn });            
	        } else {
	            this.tbar.remove('delete');   
	        }
	        //查询按钮的配置
	        this.tbar = $yd.repalceArrayNode(this.tbar, 'search', { text:"查询", iconCls:"searchIcon", scope:this, handler: this.searchButtonFn });        
	        //刷新按钮的配置
	        this.tbar = $yd.repalceArrayNode(this.tbar, 'refresh', { text:"刷新", iconCls:"refreshIcon", handler: this.refreshButtonFn });     
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
                //如果隐藏列
//                if(col.hidden){
//                    colModel.push(col);
//                    continue;
//                }
                //如果是日期性
                if(col.xtype == 'datecolumn' || editor.xtype == 'my97date' || editor.xtype == 'datefield') {
                    col.format = col.format || (editor.format || "Y-m-d");
                    editor.xtype = 'my97date';
                    editor.format = editor.format || (col.format || "Y-m-d");
                }
                col.editor = editor;
                if(col.sortable == null)	col.sortable = col.dataIndex == null ? false : true;
                col.menuDisabled = true;
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
        
        //加入行编辑插件
        if(this.rowEditor == null){
            this.rowEditor = new Ext.ux.grid.RowEditor({
                saveText: '保存',
                cancelText: '取消',
                commitChangesText: '请保存或取消更改',
                errorText: '提示'
            });
            this.rowEditor.on({
                scope: this,
                beforeedit: this.beforeEditFn,
                afteredit: this.saveFn,
                canceledit: this.cancelFn
            });
        }
        this.plugins = this.plugins || [];
        this.plugins.push(this.rowEditor);
        
        //调用父类的构造器
        Ext.yunda.RowEditorGrid.superclass.constructor.call(this);
    },
    //private:初始化组件
    initComponent : function() {
        Ext.yunda.RowEditorGrid.superclass.initComponent.call(this);
    },
    //private:销毁组件
    destroy : function() {
        Ext.yunda.RowEditorGrid.superclass.destroy.apply(this,arguments);
    }
});
