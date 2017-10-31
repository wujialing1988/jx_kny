/**
 * 流程图点击事件
 */
Ext.onReady(function(){
	//定义命名空间
	Ext.namespace("WorkflowGraphic");
	
});	
function aclick(actObj,rdpId){
	
	var parentProcInstId = actObj.getAttribute('processInstID');
	var parentActiInstId = actObj.getAttribute('activityInstID'); 
	var processDefId = actObj.getAttribute('processDefId');  
	if(actObj.getAttribute('processInstID') == null){
		return;
	}else{
		Ext.Ajax.request({
		    url: ctx + "/wfprocessinst!isSubProc.action",
		    params: {parentProcInstID: parentProcInstId,parentActiInstId:parentActiInstId},
		    success: function(response, options){
                var result = Ext.util.JSON.decode(response.responseText);
                if (result.errMsg == null) {
                    var processInstId = result.processInstId;    //流程实例id
                    var activitydefId = result.activitydefId;   //活动定义id
                    var processdefId = result.processdefId;     //流程定义id
                    var activityinst = result.activityinst;     //活动实例信息
                    var record=new Ext.data.Record();
					for(var i in activityinst){
						record.set(i,activityinst[i]);
					}
					//子流程为实例化子流程
                    if(processdefId!=null && activitydefId!=null){
                    	var paramx = "activitydefId=" + activitydefId
                    				+ "&processdefId=" + processDefId
                    				+ "&rdpId=" + rdpId
                    				+ "&activityinstid=" + parentActiInstId
                    				+ "&processInstID=" + parentProcInstId
                    				+ "&isparts=" + (typeof(isparts) != "undefined" ? isparts : "");
                    	window.open(ctx + "/jsp/jx/jxgc/producttaskmanage/subFlowInfo.jsp?" + paramx);
                    }else if(processInstId!=null && processInstId!="undefined"){  //子流程为非实例化子流程 
                    	window.open(ctx + "/jsp/jx/jxgc/producttaskmanage/flowFrame.jsp?processInstID=" + processInstId+"&rdpId="+rdpId);
                    }else{
                    	if(!activityinst.activitytype){
                    		return;
                    	}
                    	//节点为人工活动
                    	if(activityinst.activitytype == 'manual'){
                    		//活动实例状态为已完成
                    		if(activityinst.currentstate == 7){
                    			window.open(ctx + "/jsp/jx/jxgc/producttaskmanage/WIParticipantInfo.jsp?processInstID=" + parentProcInstId+"&activityinstid="+record.get("activityinstid")+"&rdpId="+rdpId+"&isparts=" + (typeof(isparts) != "undefined" ? isparts : ""));
                    		}else{
                    			if(rdpId == 'null' || !rdpId){
                    				return;
                    			}
				        		var action;
								var trainX = '/trainEnforcePlanRdp';
								var partsX = '/partsEnforcePlanRdp';
								var zb = '/zbRdp';
				        		if(typeof(isparts) != "undefined" && isparts){
				        			action = partsX;
								}else if(typeof(iszb) != "undefined" && iszb){
									action = zb;
								}else{	
									action = trainX;
								}
								jQuery.ajax({
									url: ctx + action + "!getBeseInfo.action",
									data:{rdpId: rdpId},
									dataType:"json",
									type:"post",
									success:function(data){
										var record_v = new Ext.data.Record();
										for(var i in data){
											if(i == 'transInTime' || i == 'planTrainTime' || i == 'planBeginTime' || i == 'planEndTime'){					
												record_v.set(i, new Date(data[i]).format("Y-m-d H:i"));					
												continue;
											}
											record_v.set(i,data[i]);
										}
//										activityInfo.baseForm.getForm().loadRecord(record_v);
										if(action == partsX){
											if(!activityInfo.PartsWin) activityInfo.createPartsWin();
											activityInfo.PartsWin.show();
											activityInfo.baseForm_parts.getForm().reset();
				                        	activityInfo.baseForm_parts.getForm().loadRecord(record);
				                        	if(record.get("starttime")!=null){
							        			Ext.getCmp("starttime_p").setValue(new Date(record.get("starttime")).format("Y-m-d H:i")) ;
							        		}
							        		if(record.get("endtime")!=null){
							        			Ext.getCmp("endtime_p").setValue(new Date(record.get("endtime")).format("Y-m-d H:i")) ;
							        		}
											activityInfo.baseForm_parts.getForm().loadRecord(record_v);
										}else{
											if(!activityInfo.Win) activityInfo.createTrainWin();
											activityInfo.Win.show();
											activityInfo.baseForm.getForm().reset();
				                        	activityInfo.baseForm.getForm().loadRecord(record);
				                        	if(record.get("starttime")!=null){
							        			Ext.getCmp("starttime").setValue(new Date(record.get("starttime")).format("Y-m-d H:i")) ;
							        		}
							        		if(record.get("endtime")!=null){
							        			Ext.getCmp("endtime").setValue(new Date(record.get("endtime")).format("Y-m-d H:i")) ;
							        		}
											activityInfo.baseForm.getForm().loadRecord(record_v);
										}
									}
								});
				        		
				        		
                    		}
                    		
                        }else{
                        	MyExt.Msg.alert("无针对此活动的描述");
                        }
                    }
                    
                } else {
                    alertFail(result.errMsg);
                }
            },
            failure: function(response, options){
                MyExt.Msg.alert("请求失败，服务器状态代码：\n" + response.status + "\n" + response.responseText);
            }
		})
		
	}
} 
