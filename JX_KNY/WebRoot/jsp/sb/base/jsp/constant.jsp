<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script language="javascript">
	<%-- 系统业务常量定义开始 --%>
	// 固资起始值
	var fixed_asset_value = <%=com.yunda.sb.base.constant.BizConstant.FIXED_ASSET_VALUE %>;
	// 主设备过滤状态 （只查询这几种状态）
	var primary_dynamic = [<%=com.yunda.jx.util.MixedUtils.parseArray(com.yunda.sb.base.constant.BizConstant.PRIMARY_DYNAMIC_FILTER) %>];
	<%-- 系统业务常量定义结束 --%>
</script>