/**
 * 特殊字符 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('SystemChar');                       //定义命名空间
SystemChar.grid = new Ext.yunda.RowEditorGrid({
    loadURL: ctx + '/systemChar!pageQuery.action',                 //装载列表数据的请求URL
    saveURL: ctx + '/systemChar!saveOrUpdate.action',             //保存数据的请求URL
    deleteURL: ctx + '/systemChar!delete.action',            //删除数据的请求URL
    storeId: 'id',
    tbar:['	字符描述：',{xtype:'textfield',id:'Special_Char_Desc',width:150},
	    	 {
		        text:"查询", iconCls:"searchIcon", handler: function(){
		        	var specialCharDesc = Ext.getCmp("Special_Char_Desc").getValue();		        	
	        		var searchParam = {};
	        		var whereList = [];
	        		if(!Ext.isEmpty(specialCharDesc)){
	        			searchParam = {propName:"specialCharDesc", propValue: specialCharDesc, compare: Condition.LIKE};
	        			whereList.push(searchParam);
	        		} 	
		        	
		        	var params = {
				        whereListJson: Ext.util.JSON.encode(whereList)
				    };    
				    SystemChar.grid.store.baseParams = params;
					SystemChar.grid.store.load();
		        	
		       	}		      
	    	 },'add','delete','refresh'],
	fields: [{
		header:'编码', dataIndex:'id', editor:{id:"systemCharId", disabled:true, maxLength:50 }
	},{
		header:'特殊字符', dataIndex:'specialChar', editor:{  maxLength:5, allowBlank: false }
	},{
		header:'特殊字符描述', dataIndex:'specialCharDesc', editor:{  maxLength:500 }
	}],
	defaultData: {id: null},                 //新增时默认Record记录值，必须配置
	beforeEditFn: function(rowEditor, rowIndex){
    	var record = rowEditor.grid.store.getAt(rowIndex);
    	setTimeout(function(){ //延迟加载该方法体，实现赋值			
				Ext.getCmp("systemCharId").disable();
				if(Ext.isEmpty(Ext.getCmp("systemCharId").getValue())){
					Ext.Ajax.request({
				        url: ctx + "/codeRuleConfig!getConfigRule.action",
				        params: {ruleFunction : "JXPZ_SYSTEM_CHAR_ID"},
				        success: function(response, options){
				            var result = Ext.util.JSON.decode(response.responseText);
				            Ext.getCmp("systemCharId").setValue(result.rule) ;
				        }
			    	});
				}				
			},100)
	        return true;
    },
    deleteButtonFn: function(){                         //点击删除按钮触发的函数，默认执行删除操作
        //未选择记录，直接返回
        if(!$yd.isSelectedRecord(this)) return;
        //执行删除前触发函数，根据返回结果觉得是否执行删除动作
        if(!this.beforeDeleteFn()) return;
        //弹出提示信息让用户确认是否删除，在确认后执行删除的AJAX请求
        $yd.confirmAndDelete({
            scope: this, url: this.deleteURL, params: {ids: $yd.getSelectedIdx(this, this.storeId)}
        });
    }
});
//页面自适应布局
var viewport = new Ext.Viewport({ layout:'fit', items:SystemChar.grid });
});