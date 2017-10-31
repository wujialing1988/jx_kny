/**
 * 配件检修记录卡实例 表示层，基于Extjs来构造显示页面，使用ajax json实现数据交互
 */
Ext.onReady(function(){
	Ext.namespace('CreateShowTable');                       // 定义命名空间
	
	// 动态生成表格列表（含嵌套列表）
	CreateShowTable.showTrainTable = function(data){ 
	    //此处需要让其动态的生成一个table并填充数据  
	    var list = data;
	    var tableStr = '<table cellpadding="0" cellspacing="0">';
	    tableStr += '<thead>'
	    tableStr += '<tr>'
	    tableStr += '<td width="3%" align="center">序号</td>'
	    tableStr += '<td width="10%" >检修记录卡名称</td>'
	    tableStr += '<td width="10%" >检修检测项</td>'
	    tableStr += '<td width="42%" >技术要求</td>'
	    tableStr += '<td width="20%">结果</td>'
	    tableStr += '<td width="5%" >自检</td>'
	    tableStr += '<td width="10%">质量检查</td>'
	    tableStr += '</tr>'
	    tableStr += '</thead>'
	    tableStr += '<tbody>'
	    for (var i=0 ;i < list.length ; i++) {
	    	tableStr += '<tr>'
	    	tableStr += '<td width="3%">' + (i + 1) + '</td>' 
	    	tableStr += '<td width="10%">' + list[i].workCardName + '</td>' 
	    	if (list[i].workTaskBeanList && list[i].workTaskBeanList.length > 0) {
	    		tableStr += '<td colspan ="3">';'</td>'; 
	    		tableStr += '<table  cellpadding="-1" cellspacing="-1">' 
	    		var riList = list[i].workTaskBeanList;
	    		for (var j = 0; j < riList.length; j++) {
	    			var cls = "";
	    			if (0 === j) {
	    				cls = 'border-top:0;';
	    			}    			
		    		tableStr += '<tr>' 
		    		tableStr += '<td width="9.5%" style=" '+ cls +' border-left:0;">' + riList[j].workTaskName + '</td>'
		    		tableStr += '<td width="42%" style=" '+ cls +' ">' + riList[j].repairStandard + '</td>'
		    		if (riList[j].dataItemList && riList[j].dataItemList.length > 0) {
			    		tableStr += '<td width="20%" style=" '+ cls +' ">'
			    		tableStr += '<table cellpadding="0" cellspacing="0" width="110%">' 
			    		var diList = riList[j].dataItemList;
			    		for (var k = 0; k < diList.length; k++) {
			    			cls = "";
			    			if (0 === k) {
			    				cls = 'border-top:0;';
			    			}
			    			var clsred = (("数字"==diList[k].detectResulttype) && (parseInt(diList[k].detectResult) <= diList[k].minResult || parseInt(diList[k].detectResult) >= diList[k].maxResult))? 'color:red':"";
				    		tableStr += '<tr style =" '+ clsred +' ">'  
				    		tableStr += '<td width="50%" align="right" style=" '+ cls +' border-left:0; ">' + diList[k].detectItemContent + "：" 
				    		tableStr += (0 == diList[k].isNotBlank ? "*" : "&nbsp") + '</td>' 
				    		tableStr += '<td width="50%" align="center" style=" '+ cls  +' border-left:0; ">' + (Ext.isEmpty(diList[k].detectResult) ? "&nbsp;" : diList[k].detectResult) + '</td>' 
				    		tableStr += '</tr>' 
			    		}
			    		tableStr += '</table>' 
			    		tableStr += '</td>' 
		    		} else {
	    				tableStr += '<td width="20%" style=" '+ cls +' ">' + (Ext.isEmpty(riList[j].repairResult) ? "&nbsp;" : riList[j].repairResult) + '</td>'
		    		}
		    		tableStr += '</tr>' 
	    		}
	    		tableStr += '</table>'; 
	    		tableStr += '</td>';
	    	} else {
				tableStr += '<td width="10%" >&nbsp</td>'
			    tableStr += '<td width="42%" >&nbsp</td>'
			    tableStr += '<td width="20%">&nbsp</td>'
	    	}
	    	if(!list[i].updatorName){
	    		tableStr += '<td width="10%">' +""+ '</td>';
	    	}else{
	    		tableStr += '<td width="5%">' +  list[i].updatorName + '</td>';
	    	} 
	    	tableStr += '<td width="10%" >'; 
	    	var qCResultList = list[i].qCResultList;
	       	if(null != qCResultList && 0 < qCResultList.length){   
		        for(var j = 0; j< qCResultList.length; j++){
		        	tableStr = tableStr + "<li>"+ qCResultList[j].checkItemName +":&nbsp&nbsp" + (Ext.isEmpty(qCResultList[j].qcEmpName)? "&nbsp;" :qCResultList[j].qcEmpName) + "</li>" ;  
		        }
	        }
	        tableStr = tableStr + '</td>';
	    	tableStr += '</tr>'
	    }
	    tableStr += '</tbody>'
	    tableStr += '</table>'
	    //将动态生成的table添加的事先隐藏的div中.  
	   return tableStr;    
 	} 
 	
 	
 	// 动态生成表格列表（含嵌套列表）
	CreateShowTable.showPatrsTable = function(data){ 
	    var list = Ext.decode(data.list);
	    var tableStr = '<table cellpadding="0" cellspacing="0">';
	    tableStr += '<thead>'
	    tableStr += '<tr>'
	    tableStr += '<td width="2%" align="center">序号</td>'
	    tableStr += '<td width="10%" >检修记录卡名称</td>'
	    tableStr += '<td width="10%" >检修检测项</td>'
	    tableStr += '<td width="42%" >技术要求</td>'
	    tableStr += '<td width="21%">结果</td>'
	    tableStr += '<td width="5%" >自检</td>'
	    tableStr += '<td width="10%">质量检查</td>'
	    tableStr += '</tr>'
	    tableStr += '</thead>'
	    tableStr += '<tbody>'
	    for (var i=0 ;i < list.length ; i++) {
	    	tableStr += '<tr>'
	    	tableStr += '<td>' + (i + 1) + '</td>' 
	    	tableStr += '<td>' + list[i].recordCardDesc + '</td>' 
	    	if (list[i].partsRdpRecordRiList && list[i].partsRdpRecordRiList.length > 0) {
	    		tableStr += '<td colspan ="3">';'</td>'; 
	    		tableStr += '<table  cellpadding="-1" cellspacing="-1">' 
	    		var riList = list[i].partsRdpRecordRiList;
	    		for (var j = 0; j < riList.length; j++) {
	    			var cls = "";
	    			if (0 === j) {
	    				cls = 'border-top:0;';
	    			}
	    			
		    		tableStr += '<tr>' 
		    		tableStr += '<td width="12%" style=" '+ cls +' border-left:0; ">' + riList[j].repairItemName + '</td>'
		    		tableStr += '<td width="56%" style=" '+ cls +' ">' + riList[j].repairStandard + '</td>'
		    		if (riList[j].partsRdpRecordDiList && riList[j].partsRdpRecordDiList.length > 0) {
			    		tableStr += '<td width="26.9%" style=" '+ cls +' ">'
			    		tableStr += '<table cellpadding="0" cellspacing="0" width="110%">' 
			    		var diList = riList[j].partsRdpRecordDiList;
			    		for (var k = 0; k < diList.length; k++) {
			    			cls = "";
			    			if (0 === k) {
			    				cls = 'border-top:0;';
			    			}
			    			
			    			var clsred = (isNaN(diList[k].dataItemResult) || (diList[k].dataItemResult >= diList[k].minResult && diList[k].dataItemResult <= diList[k].maxResult))? "":'color:red';
				    		tableStr += '<tr style = " '+ clsred +' ">' 
				    		tableStr += '<td width="50%" align="right" style=" '+ cls +' border-left:0; ">' + diList[k].dataItemName + "：" 
				    		tableStr += (1 == diList[k].isBlank ? "*" : "nbsp") + '</td>' 
				    		tableStr += '<td width="50%" align="center" style=" '+ cls +' border-left:0; ">' + (Ext.isEmpty(diList[k].dataItemResult)?"&nbsp;" :diList[k].dataItemResult) + '</td>' 
				    		tableStr += '</tr>' 
			    		}
			    		tableStr += '</table>' 
			    		tableStr += '</td>' 
		    		} else {
	    				tableStr += '<td width="26.9%" align="center" style=" '+ cls +' ">' + (Ext.isEmpty(riList[j].repairResult) ? "&nbsp;" : riList[j].repairResult) + '</td>'
		    		}
		    		tableStr += '</tr>' 
	    		}
	    		tableStr += '</table>' 
	    		tableStr += '</td>' 
	    	} else {
		    	 tableStr += '<td width="9.5%" >&nbsp</td>'
	   			 tableStr += '<td width="42%" >&nbsp</td>'
	   			 tableStr += '<td width="20.5%">&nbsp</td>'
	    	}
	    	if(!list[i].workEmpName){
	    		tableStr += '<td>' +""+'</td>';
	    	}else{
	    		tableStr += '<td>' +  list[i].workEmpName + '</td>';
	    	} 
	    	tableStr += '<td>'; 
	    	var partsRdpQRList = list[i].partsRdpQRList;
	       	if(null != partsRdpQRList && 0 < partsRdpQRList.length){   
		        for(var j = 0; j< partsRdpQRList.length; j++){
		        	tableStr = tableStr + "<li>"+ partsRdpQRList[j].qCItemName +":&nbsp&nbsp" + (Ext.isEmpty(partsRdpQRList[j].qREmpName)? "&nbsp;" :partsRdpQRList[j].qREmpName) + "</li>" ;  
		        }
	        }
	        tableStr = tableStr + "</td>";
	    	tableStr += '</tr>'
	    }
	    tableStr += '</tbody>'
	    tableStr += '</table>'
	    //将动态生成的table添加的事先隐藏的div中.  
	   return tableStr;    
 	}  
});