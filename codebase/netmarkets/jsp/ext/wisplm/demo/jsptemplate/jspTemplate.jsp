<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components"  prefix="jca"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/mvc" prefix="mvc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/wrappers"  prefix="wrap"%>
<!– 以下三行视情况三选一,不能同时引入-->
<%@ include file="/netmarkets/jsp/util/begin.jspf"%><!– 通用页面-->
<%@ include file=“/netmarkets/jsp/util/beginPopup.jspf”%><!–弹窗页面-->
<%@ include file=“/netmarkets/jsp/components/beginWizard.jspf”%><!—适用于Windchill标准的表单提交-->
<!– 此处编写业务代码-->
<%out.println("Hello Windchill!");%>
<%@ include file="/netmarkets/jsp/util/end.jspf"%>
