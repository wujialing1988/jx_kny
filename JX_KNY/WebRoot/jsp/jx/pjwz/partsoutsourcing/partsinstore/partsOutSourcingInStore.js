Ext.onReady(function(){
    Ext.namespace("PartsOutSourcingInStore");
    
    PartsOutSourcingInStore.form=new Ext.form.FormPanel({
    	layout:'table',
    	frame:true,
    	baseCls:'x-plain',
        layoutConfig:{columns:3},margins:'20 0 5 20',
        labelAlign :'center',
        defaults:{bodyStyle:'padding:10px'},
      	items:[{
			layout:'form',
            baseCls:'x-plain',
            labelWidth:80,
            items:[
	            {xtype:'textfield',id:'billNo',fieldLabel:'单据编号',hidden:true},
	           	{xtype:'textfield',id:'whIdx',hidden:true,fieldLabel:'接收库房主键'},
	           	{id:"whName_comb", fieldLabel:"接收库房",hiddenName:"whName",
							xtype:"Base_combo",allowBlank: false,width:120,
							entity:"com.yunda.jx.pjwz.partsBase.warehouse.entity.Warehouse",						  
							queryHql:"from Warehouse where recordStatus=0 and status = 1 and idx in (select w.warehouseIDX From StoreKeeper w where w.recordStatus=0 and w.empID='"+empId+"')",	
							fields:["wareHouseID","wareHouseName","idx"],
							displayField:"wareHouseName",valueField:"wareHouseName",
							returnField:[{widgetId:"whIdx",propertyName:"idx"}]}
           	]
      },{
			layout:'form',
            baseCls:'x-plain',
            labelWidth:60,
            items:[{
            	xtype:'textfield',id:'takeOverEmp',hidden:true,fieldLabel:'接收人'
            }, {
            	id:"takeOver_select",xtype:'OmEmployee_SelectWin',editable:false,allowBlank: false,fieldLabel : "接收人",hiddenName:'takeOverEmpId',width:120,
				returnField:[{widgetId:"takeOverEmp",propertyName:"empname"}]
			}]
      },{
           layout:'form',
           baseCls:'x-plain',
           labelWidth:60,
           items:[{xtype:'my97date',id:'whTime',fieldLabel:'入库日期',width:120,format:'Y-m-d'}]
      }]
    
    }); 
    //生成单据编号
    	var url = ctx + "/codeRuleConfig!getConfigRule.action";
		Ext.Ajax.request({
            url: url,
            params: {ruleFunction: "PJWZ_PARTS_OUTSOURCING_WH_NO"},
            success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    Ext.getCmp("billNo").setValue(result.rule);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
		});
		//页面加载时调用，显示当前登录用户
    PartsOutSourcingInStore.init=function(){
       Ext.getCmp("takeOver_select").setDisplayValue(empId,empName);
	   Ext.getCmp("takeOverEmp").setValue(empName);
	var whName_comb =  Ext.getCmp("whName_comb");
	whName_comb.getStore().on("load",function(store, records){ 
		if(records.length > 0){
	    	whName_comb.setDisplayValue(records[0].get('wareHouseName'),records[0].get('wareHouseName'));	
	    	Ext.getCmp("whIdx").setValue(records[0].get('idx'));
		}
	 });
    }
    
    var viewport=new Ext.Viewport({layout:'fit',items:[{
        layout:'border',frame:true,
        items:[{
           region:'north',
           height:70,
           //baseCls:'x-plain',
           items:[PartsOutSourcingInStore.form]
        },{
          region:'center',
          layout:'fit',
          xtype:'fieldset',
          title:'修竣配件入库明细',
          frame:true,
          items:[RepairedPartsWHDetail.grid]
        }],
        buttonAlign:'center',
        buttons:[{xtype:'button',text:'登帐并新增',handler:function(){
           //获取表单数据
        	Ext.getCmp("whName_comb").enable();
		     var form=PartsOutSourcingInStore.form.getForm();
		     if (!form.isValid()) return;
		     var formData=form.getValues();
		     formData.creatorName=empName;
		     formData.handOverEmp=empName;//交见人
		     formData.handOverEmpId=empId;//交见人主键
		      Ext.getCmp("whName_comb").disable();
		     var records=RepairedPartsWHDetail.grid.store.getModifiedRecords();
		      var datas=new Array();
		     if(records.length<1) return ;
		       for(var i=0;i<records.length;i++){
		           var data=records[i].data;
		           delete data.partsOutNo;
		           datas.push(data);
		       }
		     Ext.Ajax.request({
		      url:ctx+'/repairedPartsWH!savePartsOutSourceToStore.action',
		      jsonData:datas,
		      params:{outInStoreForm:Ext.util.JSON.encode(formData)},
		      success:function(response,options){
    	  	var result=Ext.util.JSON.decode(response.responseText);
    	  	if(result.errMsg==null){
    	  	      alertSuccess();
                  RepairedPartsWHDetail.grid.store.removeAll();
                  RepairedPartsWHDetail.grid.getView().refresh();
                  Ext.getCmp("whName_comb").enable();
                  //RepairedPartsWHDetail.grid.store.load();
            } else {
            	 Ext.getCmp("whName_comb").disable();
                  alertFail(result.errMsg);
            }
    	  },
    	  failure: function(response, options){
             MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
          }
		     });
        }}]
    }]});
	PartsOutSourcingInStore.init();
});