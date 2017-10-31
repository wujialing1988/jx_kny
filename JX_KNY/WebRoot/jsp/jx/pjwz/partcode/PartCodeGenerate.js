/**
 * 机车出入段台账查询 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
Ext.namespace('PartCodeGenerate'); 
/*** 查询表单 start ***/
PartCodeGenerate.searchLabelWidth = 110;
PartCodeGenerate.searchFieldWidth = 270;
PartCodeGenerate.searchAnchor = '100%';
PartCodeGenerate.searchForm = new Ext.form.FormPanel({
	layout:"form", border:false, style:"padding:10px" ,
	labelWidth: PartCodeGenerate.searchLabelWidth, align:'center',baseCls: "x-plain",
	defaults:{anchor:"98%"}, buttonAlign:'center',
	items:[{
		xtype: 'panel',	border:false,  layout:'column',	align:'center', baseCls: "x-plain",
		items:[{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:.5,
			fieldWidth: PartCodeGenerate.searchFieldWidth,labelWidth: PartCodeGenerate.searchLabelWidth, 
			defaults:{anchor:PartCodeGenerate.searchAnchor},
			items:[{    			
	            	xtype: 'radiogroup',
		            fieldLabel: '每个编码生成数量',
		            name: 'createNum',
		            items: [{ 
		                	id: 'Radio1', boxLabel: '1&nbsp;&nbsp;&nbsp;&nbsp;',inputValue: 1, name: 'createNum', checked: true,
		                	handler:function(radio, checked){
				                if(checked){
			            			num = 1;
				                }
		                	}
		                },{ 
		                	id: 'Radio2', boxLabel: '2&nbsp;&nbsp;&nbsp;&nbsp;',inputValue: 2, name: 'createNum',
		                	handler:function(radio, checked){
				                if(checked){
			            			num = 2;
				                }
		                	}
		                },{ 
		                	id: 'Radio3', boxLabel: '3&nbsp;&nbsp;&nbsp;&nbsp;',inputValue: 3, name: 'createNum',
		                	handler:function(radio, checked){
				                if(checked){
			            			num = 3;
				                }
		                	}
		                },{ 
		                	id: 'Radio4', boxLabel: '4&nbsp;&nbsp;&nbsp;&nbsp;',inputValue: 4, name: 'createNum',
		                	handler:function(radio, checked){
				                if(checked){
			            			num = 4;
				                }
		                	}
		                },{ 
		                	id: 'Radio5', boxLabel: '5&nbsp;&nbsp;&nbsp;&nbsp;',inputValue: 5, name: 'createNum',
		                	handler:function(radio, checked){
				                if(checked){
			            			num = 5;
				                }
		                	}
		                }]
				            
				}]
		},{
			align:'center',	layout:'form', defaultType:'textfield', baseCls: "x-plain", columnWidth:.5,
			fieldWidth: PartCodeGenerate.searchFieldWidth,labelWidth: PartCodeGenerate.searchLabelWidth, 
			defaults:{anchor:PartCodeGenerate.searchAnchor},
			items:[{
					fieldLabel: '编码数量',
		            name: 'Number',
		            id: 'Number',
		            vtype: 'positiveInt'
				}]
		}]
	}],
	buttons:[{
			id: 'QRcode', text:'生成二维码', iconCls:'addIcon', 
			handler: Generate
		},{
            id: 'print', text: "打印", iconCls: "printerIcon", handler: SetPrint, disabled: true
		}]
});
/*** 查询表单 end ***/

/*** 界面布局 start ***/
PartCodeGenerate.panel = {
    xtype: "panel", layout: "border", 
    items: [{
        region: 'north', layout: "fit",bodyStyle:'padding-left:20px;',height: 100, bodyBorder: false,
        items:[PartCodeGenerate.searchForm], frame: true, title: "", xtype: "panel"
    },{
         region : 'center', layout : 'border', bodyBorder: false, autoScoll: true, items : [{
		        id: 'qrcodelist', region: 'center', layout: "fit",bodyStyle:'padding-left:100px; padding-right:100px;', bodyBorder: false,
		        items:[], frame: true,  xtype: "panel"
		    },{
		        id: 'qrcode', region: 'east', layout: "fit", bodyBorder: false, 
		        items:[], frame: true, title: "", xtype: "panel"
		    }
        ]
    }]
};
var viewport = new Ext.Viewport({ layout:'fit', items:PartCodeGenerate.panel });
});