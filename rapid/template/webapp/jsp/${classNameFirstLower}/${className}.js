<#include "/macro.include"/> 
<#include "/custom.include"/> 
<#assign className = table.className> 
<#assign classNameCN = table.tableAlias> 
<#assign classNameLower = className?uncap_first>
<#assign classNameFirstLower = table.classNameFirstLower>
/**
 * ${classNameCN}
 */
Ext.onReady(function(){
	
Ext.namespace('${className}');                       //定义命名空间

//定义全局变量保存查询条件
${className}.searchParam = {} ;
${className}.grid = new Ext.yunda.Grid({
    loadURL: ctx + '/${classNameLower}!pageList.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/${classNameLower}!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/${classNameLower}!logicDelete.action',            //删除数据的请求URL
    singleSelect: true, saveFormColNum:1,
    labelWidth: 60,                                     //查询表单中的标签宽度
    fieldWidth: 130,
	fields: [
	<#list table.notPkColumns?chunk(1) as row>
	<#list row as column>
	<#if column.sqlName != 'RECORD_STATUS' 
        && column.sqlName != 'CREATOR' 
        && column.sqlName != 'CREATE_TIME'
        && column.sqlName != 'UPDATOR'
        && column.sqlName != 'UPDATE_TIME' >
                <#if '${column.javaType}' == 'java.util.Date'>
            	{
		header:'${column.columnAlias}', dataIndex:'${column.columnNameLower}', xtype:'datecolumn', format:'Y-m-d H:i', width:100, xtype:'datecolumn', editor:{ xtype:'my97date' }
	},
                <#else>
     	{
		header:'${column.columnAlias}', dataIndex:'${column.columnNameLower}',width: 120,editor: {}
	},
                </#if>
	</#if>
	</#list>
	</#list>
		{
		header:'主键ID', dataIndex:'idx',hidden:true ,editor: { xtype:"hidden" }
	}],
	searchFn: function(searchParam){ 
		${className}.searchParam = searchParam ;
        ${className}.grid.store.load();
	}
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:${className}.grid });
	
//查询前添加过滤条件
${className}.grid.store.on('beforeload' , function(){
		var searchParam = ${className}.searchParam ;
		searchParam = MyJson.deleteBlankProp(searchParam);
		this.baseParams.entityJson = Ext.util.JSON.encode(searchParam);
});

});