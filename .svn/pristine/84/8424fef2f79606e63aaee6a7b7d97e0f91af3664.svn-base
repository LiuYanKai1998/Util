<%@page import="com.ptc.netmarkets.util.beans.NmCommandBean"%>
<%@page import="com.ptc.netmarkets.model.NmOid "%>
<%@page import="com.ptc.netmarkets.util.misc.NmContext"%>
<%@page import="java.util.*"%>
<%@page import="ext.wis.employee.model.Employee"%>
<%@page import="ext.wisplm.util.WTUtil"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ include file="/netmarkets/jsp/components/beginWizard.jspf"%>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/mvc" prefix="mvc"%>
<%@ taglib prefix="wrap" uri="http://www.ptc.com/windchill/taglib/wrappers"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<%
String employeeOid  = "";
String employeeName = "";
String employeeAge  = "";
String employeeDept = "";
	NmCommandBean nmCommandBean = (NmCommandBean) commandBean;
	//获取表格选中的对象
	ArrayList selectedList      = nmCommandBean.getSelectedOidForPopup();
	if(selectedList.size()!=1){
		%>
		 <script>alert('请选择一位需要编辑的员工');window.close();</script>
		<%
	}else{
		//转换为employee
		NmOid nmOid = (NmOid) selectedList.get(0);
		Employee employee = (Employee) nmOid.getRef();
		employeeOid = WTUtil.getWTObjectOid(employee);
		employeeName= employee.getName();
		employeeDept= employee.getDept();
		employeeAge = employee.getAge()+"";
	}
%>
<div align=center>
姓名:<input type="text" name="employeeName" value="<%=employeeName%>"><BR>
年龄:<input type="text" name="employeeAge"  value="<%=employeeAge%>"><BR>
部门:<input type="text" name="employeeDept" value="<%=employeeDept%>"><BR>
<input type="hidden" name="employeeOid" value="<%=employeeOid%>">
</div>
<%@ include file="/netmarkets/jsp/util/end.jspf"%>
