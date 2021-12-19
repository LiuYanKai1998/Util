<%@ page contentType="text/html" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
Remove configurable views 

Parameters:
jdbcDriver,jdbcUrl,dbUser,dbPassword,serviceName - optional DB connection specification

Functions:
Delete the tables containing the configurable views so they can be recreated at next access.  
This routine is necessary since there is no mechanism to restore all OOTB views in the user
interface. 

Environment:
Expects access to a Windchill database.  Database access parameters are retreived from the class DSProperties.
Alternatively database parameters can be specified on the URL.
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html>

<style TYPE="text/css">
table td {font-family: Arial, Helvetica, sans-serif; font-size: 90%}
body {font-family: Arial, Helvetica, sans-serif; font-size: 90%}
</style>
<title>Remove Configurable View Tables</title>
<c:catch>
<%@ page import="wt.dataservice.DSProperties" %>
<c:set var="jdbcDriverWC"     value="<%= DSProperties.JDBC_DRIVER%>"/>
<c:set var="jdbcUrlWC"        value="<%= DSProperties.JDBC_URL%>"/>
<c:set var="dbUserWC"         value="<%= DSProperties.DB_USER%>"/>
<c:set var="dbPasswordWC"     value="<%= DSProperties.DB_PASSWORD%>"/>
<c:set var="serviceNameWC"    value="<%= DSProperties.SERVICE_NAME%>"/>
</c:catch>

<%-- Set DB connection; first from parameters, then session, then DSProperties --%>
<c:set scope="session" var="striderJdbcDriver"     value="${not empty param.jdbcDriver     ? param.jdbcDriver  : (not empty striderJdbcDriver     ? striderJdbcDriver    : jdbcDriverWC)}"/>
<c:set scope="session" var="striderJdbcUrl"        value="${not empty param.jdbcUrl        ? param.jdbcUrl     : (not empty striderJdbcUrl        ? striderJdbcUrl       : jdbcUrlWC)}"/>
<c:set scope="session" var="striderDbUser"         value="${not empty param.dbUser         ? param.dbUser      : (not empty striderDbUser         ? striderDbUser        : dbUserWC)}"/>
<c:set scope="session" var="striderDbPassword"     value="${not empty param.dbPassword     ? param.dbPassword  : (not empty striderDbPassword     ? striderDbPassword    : dbPasswordWC)}"/>
<c:set scope="session" var="striderServiceName"    value="${not empty param.serviceName    ? param.serviceName : (not empty striderServiceName    ? striderServiceName   : serviceNameWC)}"/>
<%-- Service name fix --%>
<c:set var="serviceNameFix" value="${fn:split(striderServiceName, ':')}"/>
<c:set var="serviceNameFix" value="${serviceNameFix[0]}:${serviceNameFix[1]}/${serviceNameFix[2]}"/>

<sql:setDataSource 
    driver="${not empty striderJdbcDriver ? striderJdbcDriver : 'oracle.jdbc.driver.OracleDriver'}"
    url="${striderJdbcUrl}@//${serviceNameFix}" 
    user="${striderDbUser}"
    password="${striderDbPassword}"/>
<%-- just a comment --%>
    
<c:if test="${param.delete}">
    <c:catch>
        <sql:update var="tvd">
                delete from TableViewDescriptor
        </sql:update>
        <sql:update var="avl">
                delete from ActiveViewLink
        </sql:update>
    </c:catch>
</c:if>
        
<sql:query var="tvdList">
    select * from TableViewDescriptor
</sql:query>
<sql:query var="avlList">
    select * from ActiveViewLink
</sql:query>

<table width="500px">
            <tr>
        <td>
            The configurable view information is kept in two tables.  Testing changes to configurable view information often requires
            deleting these tables to force the recreation of the views and thus incorporate any changes.  This page allows the deletion
            of those two tables.
        </td>
    </tr>
</table>
<br>
<table>
            <tr>
        <td><a href="db.jsp?table=TABLEVIEWDESCRIPTOR">TableViewDescriptor:</a> </td><td>${tvdList.rowCount} rows</td>
            </tr>
            <tr>
    <td><a href="db.jsp?table=ACTIVEVIEWLINK">ActiveViewLink:</a> </td><td>${avlList.rowCount} rows</td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
        <td colspan="2"><a href="remove_configurable_views.jsp">Refresh</a></td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <c:if test="${tvdList.rowCount != 0 or avlList.rowCount != 0}">
            <td colspan="2"><a href="remove_configurable_views.jsp?delete=true">Delete all rows in TableViewDescriptor and ActiveViewLink</a></td>
                </c:if>
            </tr>
</table>



    
