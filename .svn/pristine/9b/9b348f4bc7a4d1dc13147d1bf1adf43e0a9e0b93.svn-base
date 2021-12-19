<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<!-- 其他为Windchill常用标签,可在页面一次引入 -->
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components"  prefix="jca"%>
<!--引入mvc标签,构建mvc表格时需要用到该标签-->
<%@ taglib uri="http://www.ptc.com/windchill/taglib/mvc" prefix="mvc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/wrappers"  prefix="wrap"%>

<%@ include file="/netmarkets/jsp/util/beginPopup.jspf"%>
<%
	String partOid = request.getParameter("oid");
	request.setAttribute("partOid", partOid);
%>
<BR>
<!-- 注意: getComponentURL的参数值与mvc表格类注解@ComponentBuilder的参数值相同-->
<jsp:include page="${mvc:getComponentURL('ext.wis.ebom.mvc.EBOMTreeBuilder')}" flush="true">
	<jsp:param value="${partOid}" name="partOid"/>
</jsp:include>
<%@ include file="/netmarkets/jsp/util/end.jspf"%>
