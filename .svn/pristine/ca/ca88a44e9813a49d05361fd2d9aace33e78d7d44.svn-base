<%@page import="com.ptc.netmarkets.util.beans.NmCommandBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components"  prefix="jca"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/mvc" prefix="mvc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/wrappers"  prefix="wrap"%>
<!-- 引入头部样式 -->
<%@ include file="/netmarkets/jsp/util/beginPopup.jspf"%>
<%
	//获取请求对象
	NmCommandBean  nmCommandBean=(NmCommandBean) commandBean;
	//获取表格选中的对象
	java.util.ArrayList localArrayList=nmCommandBean.getSelectedOidForPopup();
	out.println("条数："+localArrayList.size());
	for(int j=0;j<localArrayList.size();j++){
		com.ptc.netmarkets.model.NmOid nmOid= (com.ptc.netmarkets.model.NmOid) localArrayList.get(j);
		wt.fc.WTObject object =(wt.fc.WTObject) nmOid.getRef();
		out.println("选中的对象--->"+object.getDisplayIdentity());
	}
%>
<!-- 引入尾部样式 -->
<%@ include file="/netmarkets/jsp/util/end.jspf"%>