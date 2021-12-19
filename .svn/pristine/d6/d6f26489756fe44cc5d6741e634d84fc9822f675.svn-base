<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.ptc.netmarkets.util.beans.NmCommandBean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ include file="/netmarkets/jsp/components/beginWizard.jspf"%>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<fmt:setLocale value="${localeBean.locale}"/>
<fmt:setBundle basename="ext.wis.resource.EmployeeRB"/>
<fmt:message   var="WizardTitle" key="createEmployeeWizard.title"/>

<c:set var="buttonList" value="DefaultWizardButtonsNoApply"/>
<jca:wizard title="${WizardTitle}" buttonList="${buttonList}">
	<jca:wizardStep action="createEmployeeWizardStep"  type="employee" label="新建员工"/>
</jca:wizard>

<script type="text/javascript">

</script>

<%@ include file="/netmarkets/jsp/util/end.jspf"%>
