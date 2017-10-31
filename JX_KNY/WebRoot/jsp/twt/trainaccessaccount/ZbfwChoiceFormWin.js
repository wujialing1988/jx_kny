Ext.onReady(function(){
	Ext.namespace('ZbfwChoiceFormWin');//定义命名空间
	//车型idx
	ZbfwChoiceFormWin.trainTypeIDX;
	//车型简称
	ZbfwChoiceFormWin.trainTypeShortName;
	//车号
	ZbfwChoiceFormWin.trainNo;
	//入段参数
	ZbfwChoiceFormWin.cfg;
	
	//表单布局
	ZbfwChoiceFormWin.form = new Ext.form.FormPanel({
		layout:"form",
		frame:true,
		closable : false,
		plain:true,
		border:false, //style:"padding:10px" ,
		labelWidth:80,
		labelAlign:"right",
		buttonAlign:'center',
		//baseCls: "x-plain",
		items:[{
			items:[{
				layout:"form",
				items:[{
					id:"trainTypeShortName_idx",
					xtype:"label",
					fieldLabel:"车型",
					anchor:"98%"
				},{ 
					id:"zbfw_comb",	
					fieldLabel: "整备范围",
					hiddenName: "idx", 
					displayField: "fwName", valueField: "idx",
					minChars : 1,
					minLength : 4, 
					maxLength : 5,
					xtype: "Base_combo",
					business: 'zbFw',
					entity:'com.yunda.jx.jczl.attachmanage.entity.jczlTrain',
					fields:["idx","fwName"],
					queryParams:{trainTypeIDX:ZbfwChoiceFormWin.trainTypeIDX},
					isAll: 'yes',
					returnField: [
			              {widgetId:"fwName", propertyName:"fwName"}//整备范围流程名称
					],
					editable:true,
					allowBlank: false,
				},{
					id:'fwName', xtype:'hidden'
				}]
			}]
		}],
		buttons:[{
			text:'确认', iconCls:'saveIcon', 
			handler: function(){ 
				//先保存中间表信息
				var form = ZbfwChoiceFormWin.form.getForm();				
		        var data = form.getValues();
		        
		        //判断数据
		        //如果范围流程没有选择，返回提示
		        if(!data.idx){
		        	MyExt.Msg.alert("请选择范围流程！");
					return;
		        }
		        
		        var zbfwTrainCenter = {};
		        zbfwTrainCenter.fwName = data.fwName;//整备范围名称
		        zbfwTrainCenter.zbfwIDX = data.idx;//整备范围idx
		        zbfwTrainCenter.trainNo = ZbfwChoiceFormWin.trainNo;//车号
		        zbfwTrainCenter.trainTypeIDX = 	ZbfwChoiceFormWin.trainTypeIDX;//车型
		        zbfwTrainCenter.trainTypeShortName = ZbfwChoiceFormWin.trainTypeShortName;//车型简称
		        var dataAry = new Array();
		        dataAry.push(zbfwTrainCenter);
		        //先保存车号和范围关系
		        Ext.Ajax.request({
					url: ctx + "/zbfwTrainCenter!saveOrUpdateZbfwTrainCenterInfo.action",
					jsonData: dataAry,
					success: function(r){
						var retn = Ext.util.JSON.decode(r.responseText);
						if(retn.success){
							//车型车号和范围绑定完毕后，执行入段操作
							Ext.Ajax.request(Ext.apply($yd.cfgAjaxRequest(), ZbfwChoiceFormWin.cfg));  
							ZbfwChoiceFormWin.win.hide();  
						}else{
							alertFail(retn.errMsg);
						}
					},
					failure: function(){
						alertFail("请求超时！");
					}
				});
			}
		},{
            text: "关闭", iconCls: "closeIcon", 
            handler: function(){ 
            	ZbfwChoiceFormWin.win.hide();
            }
		}]
	});
	
	ZbfwChoiceFormWin.win = new Ext.Window({
	    title:"JT6提票信息", 
	    layout: 'fit',
		height: 200, width: 400,
		items:ZbfwChoiceFormWin.form,
		closable : true,
		plain:true,
		closeAction:"hide",
		buttonAlign: 'center'
	});
	
	ZbfwChoiceFormWin.showWin = function() {
		ZbfwChoiceFormWin.win.show();
	}
});
