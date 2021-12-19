<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="wctags" tagdir="/WEB-INF/tags" %>
<!-- 其他为Windchill常用标签,可在页面一次引入 -->
<%@ taglib uri="http://www.ptc.com/windchill/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/components"  prefix="jca"%>
<!--引入mvc标签,构建mvc表格时需要用到该标签-->
<%@ taglib uri="http://www.ptc.com/windchill/taglib/mvc" prefix="mvc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://www.ptc.com/windchill/taglib/wrappers"  prefix="wrap"%>
<!-- 需要显示导航菜单 -->
<%@ include file="/netmarkets/jsp/util/beginPopup.jspf"%>
<!– 以下三行视情况三选一,不能同时引入-->
<!– include file="/netmarkets/jsp/util/begin.jspf"通用页面-->
<!– include file="/netmarkets/jsp/util/beginPopup.jspf"弹窗页面-->
<!– include file="/netmarkets/jsp/components/beginWizard.jspf"适用于Windchill标准的表单提交-->
<fieldset class="x-fieldset x-form-label-left" id="Visualization_and_Attributes" style="width: 600px;">
<legend>员工搜索</legend>
<!-- 引入mvc表格 -->
<table style="width: 100%" border="0">
	<tr>
		<td class="pplabel" align="left" >员工姓名:</td>
		<td><wrap:textBox name="employeeName" id="employeeName" maxlength="20" size="20" value="" /></td>
		
		<td class="pplabel" align="left">员工部门:</td>
		<td><wrap:textBox name="employeeDept" id="employeeDept" maxlength="20" size="20" value="" /></td>
	</tr>
	<tr>
		<td colspan="4" class="tableColumnHeaderfont" align="center">
		<wrap:button name="searchEmployeeBT" id="searchEmployeeBT" value="搜索"               onclick="searchEmployee();"/>             &nbsp;&nbsp;&nbsp;&nbsp;
		<wrap:button name="getSelectedBT"    id="getSelectedBT"    value='获取表格选中的对象' onclick="getSelectedEmployee();"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<wrap:button name="closeWindowBT"    id="closeWindowBT"    value='关闭'               onclick="closePopup();"/>
	</tr>
	</table>
</fieldset>
<!-- 注意: getComponentURL的参数值与mvc表格类注解@ComponentBuilder的参数值相同-->
<jsp:include page="${mvc:getComponentURL('ext.wis.employee.mvc.builder.SearchEmployeeBuilder')}" flush="true">
	<jsp:param name="param2021" value="wisplm"/>
</jsp:include>
<script>
	//此id为mvc表格TableConfig所设置的id,table.setId(TABLEID);
	var tableID="ext.wis.employee.mvc.builder.SearchEmployeeBuilder";
	function searchEmployee(){
		var employeeNameValue   = $("employeeName").value;
		var employeeDeptValue   = $("employeeDept").value;
		//参数传递,一组参数为参数名:参数值,多组参数之间用,隔开
		var params ={employeeName:employeeNameValue,employeeDept:employeeDeptValue};  
		PTC.jca.table.Utils.reload(tableID, params, true);
	}
	
	//MVC表格,获取选中对象
	function getSelectedEmployee(){
		//获取选中的对象oid,多个之间用#隔开
		var selectedOidString = getSelectedOidString(tableID);
		alert("选中的对象oid:"+selectedOidString);
		//获取选中的行对象
		var table = PTC.jca.table.Utils.getTable(tableID);
		//获取选中的行对象
		var allSelectionItems = table.getSelectionModel().getSelections();
		for(var i = 0 ; i < allSelectionItems.length; i ++){
			var employeeRow = allSelectionItems[i];
			//通过.get(属性名)获取表格列的值
			alert("员工姓名:"+partRow.get("name") + ",oid:" + employeeRow.get("oid"));
		}
		//获取选中的checkbox对象
		var allSelectionCheckboxObject  = PTC.jca.table.Utils.getTableSelectedRowsById(tableID);
		for(var i = 0 ; i < allSelectionCheckboxObject.length; i ++){
			var checkbox = allSelectionCheckboxObject[i];
			alert("name:"+checkbox.name + ",type:" + checkbox.type + ",value:" + checkbox.value);
		}
	}
</script>
<%@ include file="/netmarkets/jsp/util/end.jspf"%>
