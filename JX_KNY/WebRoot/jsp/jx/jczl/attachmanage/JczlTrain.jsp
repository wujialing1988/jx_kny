<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/frame/jspf/header.jspf" %> 
<%@include file="/jsp/jx/include/OmOrganizationSelect.jspf" %>
<%@include file="/frame/jspf/EosDictEntry.jspf" %>
<%try{ %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机车信息</title>
<script type="text/javascript">
	var typeAdd = <%=com.yunda.jx.jczl.attachmanage.entity.TrainTransferDetail.Type_ADD%>    //新配
	var typeTurn = <%=com.yunda.jx.jczl.attachmanage.entity.TrainTransferDetail.TYPE_TURN%>  //转配
	var transferIn = <%=com.yunda.jx.jczl.attachmanage.entity.TrainTransferDetail.TRANSFER_IN%> //调入
	var transferOut = <%=com.yunda.jx.jczl.attachmanage.entity.TrainTransferDetail.TRANSFER_OUT%> //调出

	var haveResume = <%=com.yunda.frame.common.Constants.YES %>;
	var notHaveResume = <%=com.yunda.frame.common.Constants.NO %>;	
	var trainStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_USE %>;
	var trainStateRepair = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_REPAIR %>;
	var trainStateSpare = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_STATE_SPARE %>;
	var assetStateUse = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_USE %>;
	var assetStateScrap = <%=com.yunda.jx.jczl.attachmanage.entity.JczlTrain.TRAIN_ASSET_STATE_SCRAP %>;
	var orgRoot = '<%=com.yunda.frame.common.Constants.ORG_ROOT_NAME%>';
</script>
<script language="javascript" src="<%=ctx%>/jsp/jx/jczl/baseselect/TrainSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/GyjcFactorySelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainTypeCombo.js"></script> 
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/repairbase/TrainNoCombo.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/pjwz/BaseCombo.js"></script>
<script language="javascript" src="<%=ctx%>/frame/resources/js/component/EosDictEntry.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/OmOrganizationCustom.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/BureauSelect.js"></script>
<script language="javascript" src="<%=ctx%>/jsp/jx/js/component/DeportSelect.js"></script>
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainTransferDetailAdd.js"></script> <!-- 新配机车 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainTransferDetailMoveIn.js"></script> <!-- 调入机车 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainTransferDetailBatchMoveIn.js"></script> <!-- 批量调入机车 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainTransferDetailMoveOut.js"></script> <!-- 调出机车 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainUseChange.js"></script> <!-- 变更使用别 -->
<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/TrainScrap.js"></script> <!--机车报废 -->

<script language="javascript" src="<%=ctx %>/jsp/jx/jczl/attachmanage/JczlTrain.js"></script>
</head>
<body>
</body>
</html>
<%
}catch(Exception e){
	e.printStackTrace();
}
%>