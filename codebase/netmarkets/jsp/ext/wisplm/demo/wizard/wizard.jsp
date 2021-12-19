<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="com.ptc.netmarkets.util.beans.NmCommandBean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>


<%@ include file="/netmarkets/jsp/components/beginWizard.jspf"%>
<%@ include file="/netmarkets/jsp/components/includeWizBean.jspf"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

<!-- ################################ -->
<!-- jcaWizard为Windchill标准表单数据查看、编辑、提交技术、支持单页或多页数据一起提交给processor类 -->
<!-- 1、buttonList          :定义了本wizard需使用的按钮,本示例使用的按钮组包括:上一步、下一步、确定、取消-->
<!-- 2、其他action定义请查看xml文件:codebase\config\actions\actionmodels.xml -->
<!-- 3、jca:wizardStep所需action通过xxx-actions.xml文件定义-->
<!-- ################################ -->
<!--引入fmt标签-->
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<!--设置语言环境--><fmt:setLocale value="${localeBean.locale}"/>
<!--设置要读取的rbInfo类--><fmt:setBundle basename="ext.wisplm.demo.resources.CustomOperationRB" />
<!--设置变量WizardTitle--><fmt:message var="WizardTitle" key="CustomWizardTitle"/>
<!--读取变量WizardTitle-->${WizardTitle}
<!-- title,label,buttonList可使用标签取rbinfo值实现国际化 -->
<!--buttionList通过actionmodel定义,文件位置:codebase/config/actions/actionmodels.xml-->
<!-- 每一个按钮都是一个action,可自定义action作为按钮使用,按需编写action对应的处理逻辑即可-->
<!-- 此处 DefaultWizardButtonsNoApply为已定义的actionmodel的名称-->
   	  <!--上一步-->
      <!--<action name="prevButton"        type="object"/>-->
      <!--下一步-->
      <!--<action name="nextButton"        type="object"/>-->
      <!--确定-->
      <!--<action name="okButton"          type="object"/>-->
      <!--应用-->
      <!--<action name="applyButton"       type="object"/>-->
      <!-- 取消 -->
      <!--<action name="cancelButton"      type="object"/>-->
      <!--保存-->
       <!--<action name="saveButton"       type="object"/>-->
       <!-- 关闭 -->
       <!--<action name="closeButton"      type="object"/>-->
       <!-- 取消编辑 -->
       <!--<action name="editCancelButton" type="object"/>-->
   
<c:set var="buttonList" value="DefaultWizardButtonsNoApply"/>
<jca:wizard title="${WizardTitle}" buttonList="${buttonList}">
	<jca:wizardStep action="wizardStep1"  type="wis" label="步骤1标题"/>
	<jca:wizardStep action="wizardStep2"  type="wis" label="步骤2标题"/>
	<jca:wizardStep action="wizardStep3"  type="wis" label="步骤3标题"/>
</jca:wizard>
<script type="text/javascript">

</script>
<!-- 若当前页面不是通过action发起,可自定义hidden域或地址栏传递以下两个参数设置processsor和对应method,如下
<input type="hidden" name="wizardActionClass"  value="ext.wisplm.demo.wizard.processor.WizardProcessor">
<input type="hidden" name="wizardActionMethod" value="execute">-->

<%@ include file="/netmarkets/jsp/util/end.jspf"%>
