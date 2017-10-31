<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>调度监控设置</title>
    <style type="text/css">
    	#look:hover{
    		color:red;
    	}
    </style>
    <script language="javascript" src="<%=ctx%>/frame/resources/jquery/jquery.js"></script>
    <script type="text/javascript">
    	var normal = ${ empty param.sx && empty param.lx};
    	var sx = ${ not empty param.sx};
    	var lx = ${ not empty param.lx};
    	var page;
    	if(normal){
			page = "LED_DDJK.jsp?a=b";
        }else if(sx){
			page = "LED_DDJKforZB.jsp?type=<%=com.yunda.jx.jxgc.zb.rdp.entity.ZbRdp.TYPE_TJ  %>";
        }else if(lx){
			page = "LED_DDJKforZB.jsp?type=<%=com.yunda.jx.jxgc.zb.rdp.entity.ZbRdp.TYPE_LX  %>";
        }
    	var trainTreeRefreshTime = 3600;
    	var trainCutoverTime = 1200;
    	var processRefreshTime = parseInt(trainCutoverTime/2);
	    function show(a){
		    if(jQuery("#ts").val() && jQuery("#qh").val()){
				var mode = jQuery("input:checked").val();
				a.target = "target";
				if(mode == "monthPlan"){
					a.href="monthPlan.jsp";
				}else{					
					var ts = jQuery("#ts").val();
					var cs = jQuery("#qh").val();
					a.href = page + "&ts="+ ts + "&cs="+ cs + "&mode=" + mode;
				}
		    }else{
			    jQuery(a).removeAttr("target");
			    return false;
		    }
		}

		function change(){
			var cutoverTime = jQuery("#qh").val();
			if(isNaN(cutoverTime)){
				jQuery("#qh").val(trainCutoverTime);
				cutoverTime = trainCutoverTime;
			}
		}
		function changet(){
			var ts = jQuery("#ts").val();
			if(isNaN(ts)){
				jQuery("#ts").val(trainTreeRefreshTime);
				return;
			}
			if(ts < jQuery("#qh").val()){
				jQuery("#ts").val(jQuery("#qh").val() * 2);
			}
		}
		onload = function(){
			jQuery("#ts").val(trainTreeRefreshTime);
			jQuery("#qh").val(trainCutoverTime);
		}
    </script>	
  </head>
  
   <body style='background-color:#dfe8f6; font-size:14px;'>
		<table>
			<tr>
				<td>
					检修机车树刷新时间：
				</td>
				<td>
					<input id="ts" maxlength="5" onchange="changet()">
					（秒）
				</td>
			</tr>
			<tr>
				<td>
					在修机车切换时间：
				</td>
				<td>
					<input id="qh" onkeyup='change()' maxlength="5">
					（秒）
				</td>
			</tr>
			<tr>
				<td>
					查看模式：
				</td>
				<td>
					<label>
						<input type="radio" value="flow" checked="true" name='mode' />
						流程图
					</label>
					<span style="display:${ (not empty param.sx || not empty param.lx) ? 'none' : 'inline' }">
						<label>
							<input type="radio" value="gantt" name='mode' />
							甘特图
						</label>
						<%--<label>
							<input type="radio" value="monthPlan" name='mode' />
							月计划
						</label>
					--%></span>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<table cellspacing="0" class="x-toolbar-ct";>
						<tbody>
							<tr>
								<td class="x-toolbar-left" align="left">
									<table cellspacing="0">
										<tbody>
											<tr class="x-toolbar-left-row">
												<td class="x-toolbar-cell" id="ext-gen141">
													<table id="ext-comp-1333" cellspacing="0"
														class="x-btn x-btn-text-icon" style="width: 75px;">
														<tbody class="x-btn-small x-btn-icon-small-left">
															<tr>
																<td class="x-btn-tl">
																	<i>&nbsp;</i>
																</td>
																<td class="x-btn-tc"></td>
																<td class="x-btn-tr">
																	<i>&nbsp;</i>
																</td>
															</tr>
															<tr>
																<td class="x-btn-ml">
																	<i>&nbsp;</i>
																</td>
																<td class="x-btn-mc">
																	<em class="" unselectable="on"> <a id="look"
																		class=" x-btn-text searchIcon" href="#"
																		onclick="show(this)">查看</a> </em>
																</td>
																<td class="x-btn-mr">
																	<i>&nbsp;</i>
																</td>
															</tr>
															<tr>
																<td class="x-btn-bl">
																	<i>&nbsp;</i>
																</td>
																<td class="x-btn-bc"></td>
																<td class="x-btn-br">
																	<i>&nbsp;</i>
																</td>
															</tr>
														</tbody>
													</table>
												</td>
											</tr>
										</tbody>
									</table>
								</td>
								<td class="x-toolbar-right" align="right">
								</td>
							</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
