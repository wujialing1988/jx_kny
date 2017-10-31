<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%try{ 
	String infoIdx = request.getParameter("infoIdx");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>工作日历</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<STYLE type="text/css">
		html{overflow-x: hidden;overflow-y:auto;}
		<!-- 整个日历面板的边框,背景色等样式 -->
		#myrl{width:750px;height:352px;margin:0px auto;background:none;top:0px;left:0px;position:relative;border:0px dashed #ccc;}
		<!-- 日历面板table的内部布局样式,可以去掉 -->
		#box{width:750px;height:352px;position:absolute;top:0px;right:0px;}
		<!-- 系统当前日期的颜色背景,必须 -->
		.todayColor{ color:GREEN;}
		table {font-size: 18px;}
		.transpar{
			border-top:0px;
			border-left:0px;
			border-right:0px;
			border-bottom:0px;
			background-color:transparent;
			width:120px;
			font-size:15px;
		}
		.timeSetInfo{
			font-size:15px;
			width:75px;
			text-align:center;
		}
	</STYLE>
	<script type="text/javascript" src="WorkCalendar.js"></script>
	<script type="text/javascript" src="WorkCalendarFun.js"></script>
	<script type="text/javascript">
	var targetTdObj;
	var LevTargetTdObj;
	var infoIdx = '<%=infoIdx %>';  //日历主表主键
	var ZEROTIME = '<%=com.yunda.baseapp.workcalendar.entity.WorkCalendarBean.ZEROTIME%>';
	var TWENTYFOURTIME = '<%=com.yunda.baseapp.workcalendar.entity.WorkCalendarBean.TWENTYFOURTIME%>';
	function resetChooseColor(){
		if(LevTargetTdObj!=null&&LevTargetTdObj!=""){
			if(LevTargetTdObj.style.background == "yellow"){
				LevTargetTdObj.style.backgroundColor="transparent";
			}
		}
	}
	

	/**
	 * 月份及日期不够两位补足两位
	 */
	function formatDay(y,m,d){
		var _y = y;
		var _m = m;
		var _d = d;
		if(parseInt(_m)<10) _m = 0+String(_m);
		if(parseInt(_d)<10) _d = 0+String(_d); 
		//alert( String(_y)+String(_m)+String(_d));
		return String(_y)+String(_m)+String(_d);
	}
	
	function validate(){
		var t = /([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])/;
		//if(!/([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])/.test(this.value)){alert('时间格式不正确!');this.select();};
		if(!t.test(event.srcElement.value)){
			alert('时间格式不正确!');
		}
	}
	</script>
  </head>
  
  <body bgColor="#dae7f6" onLoad="initial()">

  	<br/>
	<div id="myrl">
		<FORM  name=CLD>
    	<DIV id="detail" style="position:absolute;z-index:9999;"></DIV>
    		<TABLE id="box" border="0" style="table-layout:fixed;word-break: break-all; word-wrap: break-word;">
      			<TBODY>
        			<TR>
          				<TD  align=middle style="width:525px">
          					<style>
           						table.biao{border-collapse:collapse; font-family:"Times New Roman","宋体";}
   								// table.biao td{border:1px solid #fff;}
          					</style>
            				<TABLE class="biao" border="3">
              					<TBODY>
                					<TR>
                  						<TD bgColor=#0099FF  colSpan=3 style="height:36px;padding-top:3px;text-align:center;">
                  						<FONT  style="FONT-SIZE:9pt" color=#ffffff  size=2>公历
                    						<SELECT id="choYear" style="FONT-SIZE:  9pt;" onchange=changeCld()  name=SY>
                      							<SCRIPT language=JavaScript>
         											for(i=1900;i<2030;i++) document.write('<option value="'+i+'">'+i)
            									</SCRIPT>
                    						</SELECT>年
                    						<SELECT id="choMonth" style="FONT-SIZE:  9pt"  onchange=changeCld() name=SM>
     											<SCRIPT  language=JavaScript>
     												for(i=1;i<13;i++)  document.write('<option value="'+i+'">'+i)
     											</SCRIPT>
                    						</SELECT>月
                    					</FONT>
                    					<FONT  id=GZ style="color:#fff;font:18px 'Times New Roman','宋体';"></FONT>
                    					</TD>
                    					<TD bgColor=#0099FF  colSpan=4 style="height:36px;padding-top:3px;text-align:center;">
 		            						<BUTTON style="width:45px;FONT-SIZE:  9pt;border: 1px outset #FFFFFF;"  onclick="pushBtm('YU');return false;">上年</BUTTON>&nbsp;&nbsp;
		            						<BUTTON style="width:45px;FONT-SIZE:  9pt;border: 1px outset #FFFFFF;"  onclick="pushBtm('YD');return false;">下年</BUTTON>&nbsp;&nbsp;
		           							<BUTTON  style="width:45px;FONT-SIZE:  9pt;border: 1px outset #FFFFFF;" onClick="pushBtm('MU');return false;">上月</BUTTON>&nbsp;&nbsp;
		             						<BUTTON  style="width:45px;FONT-SIZE:  9pt;border: 1px outset #FFFFFF;" onClick="pushBtm('MD');return false;">下月</BUTTON>&nbsp;&nbsp;
  	              							<BUTTON  style="width:45px;FONT-SIZE:  9pt;border: 1px outset #FFFFFF;"  onclick="pushBtm('');return false;">本月</BUTTON>
		  
                   						</TD>
                   
                					</TR>
                					<TR style="color:#333;font:16px '宋体';height:28px;background:#A8E1FD;font-weight:bold;text-align:center;vertical-align:middle;">
                  						<TD width="70">日</TD>
                  						<TD width="70">一</TD>
                  						<TD width="70">二</TD>
                  						<TD width="70">三</TD>
                  						<TD width="70">四</TD>
                  						<TD width="70">五</TD>
                  						<TD width="70">六</TD>
                					</TR>
                		<SCRIPT  language="JavaScript"><!--
							function cMyBg(myobj){
								//myobj.style.backgroundColor='#D2FFF5';
							}
							function bMyBg(myobj){
								//alert(myobj.style.backgroundColor=="#d2FFF5"+":"+myobj.style.backgroundColor);
								//if(myobj.style.backgroundColor=="#d2FFF5"){
								//	myobj.style.backgroundColor='transparent';
								//}
							}
						
							var  gNum
							for(i=0;i<6;i++)  {
								document.write('<tr  align=center height="44">')
								for(j=0;j<7;j++)  {
									gNum  =  i*7+j;
								
									document.write('<td id="GD'  +  gNum  +'" height=60 onclick="workCalendar.getChooseDay(this)" onMouseOver="mOvr('  +  gNum  +');cMyBg(this)"  onMouseOut="mOut();bMyBg(this)"><font  id="SD'  +  gNum  +'" size=6  face="Arial"')
									if(j  ==  0)  document.write(" color=red ");
									if(j  ==  6) {
										if(i%2==1) {
											document.write(' color=red ')
										} else {
											document.write(' color=red ')
										}
									}
									document.write(' TITLE=""></font><br><font id="LD'  +  gNum  +  '"  >  </font></td>')
								}
								document.write('</tr>')
							}
							//--></SCRIPT>
                		</TABLE>
                		<table style="width:500px;">
                			<tr>
                				<td>
									<div border="0" style="float:left;">
									 	<span style="width:22px;background-color:green;border:1px #FFF solid;">&nbsp;</span>
									 	<span style="vertical-align:top;">今天</span>&nbsp;
                    					<span style="width:22px;background-color:black;border:1px #FFF solid;">&nbsp;</span>
                    					<span style="vertical-align:top;">工作日</span>&nbsp;
				                    	<span style="width:22px;background-color:red;border:1px #FFF solid;">&nbsp;</span>
				                    	<span style='vertical-align:top;'>节假日</span>
				                    	<span style="width:22px;background-color:blue;border:1px #FFF solid;">&nbsp;</span>
				                    	<span style='vertical-align:top;'>设为非默认工作时间</span>
                					</div>
								</td>
							</tr>
                		</table>
                	</TD>
                	<TD valign="middle">
                		
                		<Div id="aas"  style="width:210px;height:430px;"><!-- border:1px solid #fff; -->
                			<table id="formTab" border="0" style="width:205px;font-size:20px;">
                				<tr>
                					<td colspan="3">
                						<input type="hidden" id="idx" value=""/>
                						<input type="hidden" id="infoIdx" value="402880f23eab2bed013eac082ffb000c">
                						已选中:&nbsp;&nbsp;<span><input type="text" value="" value2="" id="currentDay" class="transpar"/></span>
                					</td>
                				</tr>
                				<tr>
                					<td colspan="3">
                						工作日历设置:
                					</td>
                				</tr>
                				<tr>
                					<td>&nbsp;</td>
                					<td colspan="2">
                						<input type="radio" name="calendarSetUp" value="2" checked="checked" onclick="workCalendar.typeOption()"><font style="font-size:15px;font-family:微软雅黑">默认工作时间</font></input>
                					</td>
                				</tr>
                				<tr>
                					<td>&nbsp;</td>
                					<td colspan="2">
                						<input type="radio" name="calendarSetUp" value="1" onclick="workCalendar.typeOption()"><font style="font-size:15px;font-family:微软雅黑">非工作日</font></input>
                					</td>
                				</tr>
                				<tr>
                					<td>&nbsp;</td>
                					<td colspan="2">
                						<input type="radio" name="calendarSetUp" value="0" onclick="workCalendar.typeOption()"><font style="font-size:15px;font-family:微软雅黑">非默认工作时间</font></input>
                					</td>
                				</tr>
                				<tr>
                					<td><input type="checkbox" name="period1" id="period1" onclick="workCalendar.changeCheckBox()"></td>
                					<td width="110">
                						<div id="period1BeginDiv"></div>
                					</td>
                					<td width="110">
                						<div id="period1EndDiv"></div>
                					</td>
                				</tr>
                				<tr>
                					<td><input type="checkbox" name="period2" id="period2" onclick="workCalendar.changeCheckBox()"></td>
                					<td><div id="period2BeginDiv"></div></td>
                					<td><div id="period2EndDiv"></div></td>
                				</tr>
                				<tr>
                					<td><input type="checkbox" name="period3" id="period3" onclick="workCalendar.changeCheckBox()"></td>
                					<td><div id="period3BeginDiv"></div></td>
                					<td><div id="period3EndDiv"></div></td>
                				</tr>
                				<tr>
                					<td><input type="checkbox" name="period4" id="period4" onclick="workCalendar.changeCheckBox()"></td>
                					<td><div id="period4BeginDiv"></div></td>
                					<td><div id="period4EndDiv"></div></td>
                				</tr><%--
                				
                				<tr>
                					<td colspan="3">
                						批量设置:
                					</td>
                				</tr>
                				<tr>
                					<td><div id="volumeBeginDiv"></div></td>
                					<td><div id="volumeEndDiv"></div></td>
                				</tr>
                				
                				--%><tr><td colspan="3">&nbsp;</td></tr>
                				<tr>
                					<td colspan="3">
                							<font style="font-size:15px;font-family:微软雅黑">
                							<b>注:</b><br/>时间采用24小时制(00:00:00)<br/>当前默认工作时间为:&nbsp;<a href="#" style="font-size:13px;" onclick="workCalendar.showWin()">点此修改</a><!--非工作日转工作日请选择非默认工作时间，并设置实际的工作时间段 -->
                							<label id="showDefaultInfo"></label>
                							</font>
                					</td>
                				</tr>
                				<tr>
                					<td colspan="3" height="50" align="center">
                						<input type="button" id="sureSet" value="确认设置" onclick="workCalendar.saveOrUpdate()"/>
                						&nbsp;<a href="#" style="font-size:13px;" onclick="workCalendar.showVolumeTypeWin()">批量设置</a>
                					</td>
                				</tr>
                			</table>
                		</Div>
                	</TD>
        		</TR>
      		</TBODY>
    	</TABLE>
    	</FORM>
	</div>
</html>
<SCRIPT  language="JavaScript">
    //设置各种初始化参数
	var myDate = new Date();
	//默认选中当年当月当日
	var currentDayShow = myDate.getFullYear()+'年'+(parseInt(myDate.getMonth())+1)+'月'+myDate.getDate()+'日';
	document.getElementById("currentDay").value = currentDayShow;
	var _a = formatDay(myDate.getFullYear(),(myDate.getMonth()+1),myDate.getDate());
	document.getElementById("currentDay").value2 = _a;
	var radioObj = document.getElementsByName("calendarSetUp");
	<%--
	var isDefault = '<%=isDefault %>';  //是否为默认日历
	function init(){
		//默认日历
		if(isDefault == '1'){
			//不可编辑修改
			radioObj[0].disabled = true ;
			radioObj[1].disabled = true ;
			radioObj[2].disabled = true ;
			document.getElementsByName("period1")[0].disabled = true ;
			document.getElementsByName("period2")[0].disabled = true ;
			document.getElementsByName("period3")[0].disabled = true ;
			document.getElementsByName("period4")[0].disabled = true ;
			document.getElementById("sureSet").disabled = true ;
		}else{
			//可以修改编辑 
			//选中单选按钮
			radioObj[0].checked="checked";
		}
	}
	init();--%>
</SCRIPT>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>