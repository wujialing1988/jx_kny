/**
 * 权限页面
 */
Ext.onReady(function(){
	
	Ext.namespace('DeskPrivilige'); 
	
	DeskPrivilige.grid = new Ext.yunda.Grid({
	    loadURL: ctx + '/deskPrivilige!findDeskPriviligeList.action',                 //装载列表数据的请求URL
	    singleSelect: true, 
	    saveFormColNum:1,
	    page:false,
	    storeId:'dictid',
	    tbar:[
	          '<div></div>'
	    ],    
		fields: [
	     	{
			header:'模块id', dataIndex:'dictid',hidden:true,width: 120,editor: {}
		},{
			header:'模块名称', dataIndex:'dictname',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
					return value;
			}
		},{
			header:'是否显示', dataIndex:'isShow',width: 120,editor: {},renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				   if(value == 1){
					   return '<a style="font-size: 18px;cursor:pointer" onclick="DeskPrivilige.saveDeskPrivilige()"><i class="fa fa-star text-yellow"></i></a>';
				   }
				   return '<a style="font-size: 18px;cursor:pointer" onclick="DeskPrivilige.saveDeskPrivilige()"><i class="fa fa-star-o text-yellow"></i></a>';
				}
		}],
		searchFn: function(searchParam){ 
		}
	});
	
	/**
	 * 保存数据
	 */
	DeskPrivilige.saveDeskPrivilige = function(){
		var record = DeskPrivilige.grid.getSelectionModel().getSelections();
		var obj =  record[0].data ;
		var data = {} ;
		data.dictCode = obj.dictid ;
		data.dictName = obj.dictname ;
		var cfg = {
		        url: ctx + "/deskPrivilige!saveDeskPrivilige.action", 
		        jsonData: data,
		        timeout: 600000,
		        success: function(response, options){
		            var result = Ext.util.JSON.decode(response.responseText);
		            if (result.errMsg == null && result.success == true) {
		                alertSuccess();
		                DeskPrivilige.grid.store.load();
		            } else {
		                alertFail(result.errMsg);
		            }
		        },
		        failure: function(response, options){
		        	if(processTips) hidetip();
			        Ext.Msg.alert('提示', "请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
			    }
		    };
		Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), cfg));
		
	};
	
	
	DeskPrivilige.win = new Ext.Window({
		title:'首页显示内容设置', 
		layout:"fit", closeAction:"hide",
		modal: true,
		height: 500, width: 450,
		items:[DeskPrivilige.grid],
		buttonAlign:'center',
		buttons:[{
			text:'关闭', iconCls:'closeIcon', handler:function(){
				this.findParentByType('window').hide();
			}
		}], 
		listeners : {
			
		}
	});
	
	DeskPrivilige.showWin = function(){
		DeskPrivilige.grid.store.load();
		DeskPrivilige.win.show();
	};
	
});