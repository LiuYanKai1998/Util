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

<!– 以下三行视情况三选一,不能同时引入-->
<!– include file="/netmarkets/jsp/util/begin.jspf"通用页面-->
<!– include file="/netmarkets/jsp/util/beginPopup.jspf"弹窗页面-->
<!– include file="/netmarkets/jsp/components/beginWizard.jspf"适用于Windchill标准的表单提交-->


<%
	String oid     = request.getParameter("oid");
	request.setAttribute("oid", oid);
	String display = "";
	if(oid != null && !"".equals(oid)){
		wt.fc.WTObject currentObject = (wt.fc.WTObject)new wt.fc.ReferenceFactory().getReference(oid).getObject();
		display = currentObject.getDisplayIdentity()+"";
	}

	out.println("当前操作对象:" + display);
%>
<BR>
<script>
	//此id为mvc表格TableConfig所设置的id,table.setId(TABLEID);
	var tableID="ext.wisplm.demo.part.mvc.SearchPartBuilder";
	function searchPart(){
		
		var partNameValue   = document.getElementById("partName").value;
		var partNumberValue = document.getElementById("partNumber").value;
		//参数传递,一组参数为参数名:参数值,多组参数之间用,隔开
		var params ={partName:partNameValue,partNumber:partNumberValue};  
		PTC.jca.table.Utils.reload(tableID, params, true);
	}
	
	//MVC表格,获取选中对象
	function getSelectedParts(){
	
		//获取选中的对象oid,多个之间用#隔开
		var selectedOidString = getSelectedOidString(tableID);
		alert("选中的对象oid:"+selectedOidString);
		//获取选中的行对象
		var table = PTC.jca.table.Utils.getTable(tableID);
		//获取选中的行对象
		var allSelectionItems = table.getSelectionModel().getSelections();
		for(var i = 0 ; i < allSelectionItems.length; i ++){
			var partRow = allSelectionItems[i];
			//通过.get(属性名)获取表格列的值
			alert("编号:"+partRow.get("number") + ",oid:" + partRow.get("oid"));
		}
		//获取选中的checkbox对象
		var allSelectionCheckboxObject  = PTC.jca.table.Utils.getTableSelectedRowsById(tableID);
		for(var i = 0 ; i < allSelectionCheckboxObject.length; i ++){
			var checkbox = allSelectionCheckboxObject[i];
			alert("name:"+checkbox.name + ",type:" + checkbox.type + ",value:" + checkbox.value);
		}
}
</script>
<!-- 引入mvc表格 -->
部件名称：<input type="text" name="partName" id="partName"><BR>
部件编号：<input type="text" name="partNumber" id="partNumber"><BR>
<input type=button value="搜索部件(给后台传参数)" onclick="searchPart();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type=button value="获取表格选中的对象" onclick="getSelectedParts();">
<!-- 注意: getComponentURL的参数值与mvc表格类注解@ComponentBuilder的参数值相同-->
<jsp:include page="${mvc:getComponentURL('ext.wisplm.demo.part.mvc.SearchPartBuilder')}" flush="true">
	<jsp:param value="${oid}" name="oid"/>
</jsp:include>
<%@ include file="/netmarkets/jsp/util/end.jspf"%>
