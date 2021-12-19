<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
Search and navigate the windchill data structures

Parameters:
oid - specifies the id to be displayed.  This is the value of field ida2a2.
table - display this table.  If oid is specified, it is presumed to be in this table.
refer - if present, references to the oid are found rather than the oid itself.  Oid must be present and table is ignored.
refresh - recreate the table structure cache
jdbcDriver,jdbcUrl,dbUser,dbPassword,serviceName - optional DB connection specification

Functions:
Search for the object with a particular id (aka oid, ida2a2)
Search for usages of a particular id and display the tables using it.
Display a specified table.
On display of a table, provide links to all referenced tables.

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

<c:catch>
<%@ page import="wt.dataservice.DSProperties" %>
<%//String HOSTPORTSERVICE = DSProperties.JDBC_HOST + ":" + DSProperties.JDBC_PORT + ":WIND"; %>
<!--jdbc:oracle:thin:@//localhost:1521:SERVICENAME-->
<c:set var="jdbcDriverWC"     value="oracle.jdbc.OracleDriver"/>  <!--DSProperties.JDBC_DRIVER-->
<c:set var="jdbcUrlWC"        value="jdbc:oracle:thin:"/> 	<!--DSProperties.JDBC_URL   -->
<c:set var="dbUserWC"         value="wcadmin"/>  	<!--DSProperties.DB_USER    -->
<c:set var="dbPasswordWC"     value="wcadmin"/>  <!--DSProperties.DB_PASSWORD-->
<c:set var="serviceNameWC"    value="wisplm.com:1521:WIND"/> <!--HOSTPORTSERVICE-->
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
    password="${striderDbPassword}"
    />

<%-- Help text to be displayed as needed --%>
<c:set var="helpText">
<br><br>
<h3><a name="help">Windchill Data Strider</a></h3>
This utility allows navigating through a Windchill database to examine the contents.
There are four basic operations:
<ul>
<li>Search for a particular id (aka oid)</li>
<li>Show the contents of a particular table</li>
<li>Follow a link from one table to another</li>
<li>Show all objects linking to the currently displayed object</li>
</ul>
Links are generated to the appropriate tables for each foreign reference.  Clicking on a link within a table
moves the display to the specified row in that table.  If a single row is selected then it is displayed vertically.
When multiple rows are selected the information is displayed in a tabular form.  To select a single row within a table, 
click on the desired entry in the IDA2A2 column.
<p>
The windchill database can be searched for a 
particular OID (aka id, IDA2A2) in a reasonably quick search.  Use the "Search" menu function and type in the ID. 
The converse operation can also be done to find all the objects that reference a particular ID.  Select the record
and then select "Tables using this ID" from the menu.  A single record must be selected for that menu item to be 
visible.  A link will be returned for each record that references the specified ID.
<p>
There are "utility" columns that are normally not of interest 
and take up valuable real estate.  Those are hidden by default.  They can be exposed as needed using the "Show Utility Columns" link.
<p>
CLASSNAMEKEY fields exist in the databse but are not displayed.  The CLASSNAMEKEY fields are paired with another column to give the class
and id for the link.  Information from both those columns are displayed as part of the link. 
<p>
A cache of the file structure is generated to make queries execute in a reasonable time frame.  This cache is automatically
generated and will refresh every 2 days.  Two tables are created of the form "strider_%".
<p>
Database connections are taken from the Windchill class, DSProperties.  These connection properties
can also be set as parameters to the URL which are then saved in the session.  Current values are:
<ul>
    <li>jdbcDriver: <b>${striderJdbcDriver}</b></li>
    <li>jdbcUrl: <b>${striderJdbcUrl}</b></li>
    <li>dbUser: <b>${striderDbUser}</b></li>
    <li>dbPassword: <b>${striderDbPassword}</b></li>
    <li>serviceName: <b>${striderServiceName}</b></li>
</ul>
The rules for identifying links and pairing them with classnames/tablenames are not trivial.  I may not have perfectly incorporated them.  
Send any suggestions to <a href="mailto:mcarney@ptc.com">mcarney@ptc.com</a> or update the code.
<p>
The snappy performance of this tool results from using a single query to examine all tables.  
This works great in Oracle but other databases like SQLServer may have difficulty with large queries.  
</c:set>

<%--
select column_name
from strider_table_columns 
where column_name like 'BRANCHID%' or column_name like 'IDA3%'
minus
select b.column_name
from strider_table_columns a, strider_table_columns b 
where a.table_name = b.table_name 
	and a.column_name like 'CLASSNAMEKEY%'
	and b.column_name != 'IDA2A2'
	and (
		(a.column_name = concat('CLASSNAMEKEY', substr(b.column_name, 5, 16)) and b.column_name like 'IDA3%')
		or (a.column_name = concat('CLASSNAMEKEY', substr(b.column_name, 11, 16)) and b.column_name like 'BRANCHIDA3%')
		or (a.column_name = 'CLASSNAMEKEYROLEAOBJECTREF' and b.column_name in ('IDA3A5','BRANCHIDA3A5'))
    		or (a.column_name = 'CLASSNAMEKEYROLEBOBJECTREF' and b.column_name in ('IDA3B5','BRANCHIDA3B5'))
); 
--%>

<%-- Determine if table structure cache exists and is reasonably current --%>
<c:catch var="exception">
    <sql:query var="lastUpdated" >
        select round((sysdate - lastUpdated), 1) "cacheAge" from strider_updated
    </sql:query>
    <c:set var="cacheAge" value="${lastUpdated.rows[0].cacheAge}"/>
</c:catch>
<%-- If cache needs refreshing, display patience message and restart --%>
<c:if test="${not refreshCache}">
    <c:if test="${not empty exception or lastUpdated.rowCount == 0 or lastUpdated.rows[0].cacheAge > 2 or param.refresh != null}">
        <c:set var="refreshCache" scope="session" value="true"/>
        <meta http-equiv="refresh" content="1;url=<%= request.getRequestURL()%>">
        <h3>Filling data structure cache, please wait a moment ...</h3>
        While you are waiting, may I offer you some documentation?
        ${helpText}  <%-- something to read while they wait --%>
        <% if(true) return; %>
    </c:if>
</c:if>

<%-- Create/recreate table structure cache --%>
<c:if test="${refreshCache}">
    <c:remove var="refreshCache" scope="session"/>
    <c:catch>
        <sql:update var="createUpdated">
            drop table strider_updated
        </sql:update>
    </c:catch>
    <sql:update var="createUpdated">
        create table strider_updated as 
            select sysdate "LASTUPDATED" from dual
    </sql:update>
    <c:catch>
        <sql:update var="createTableList">
            drop table strider_table_columns
        </sql:update>
    </c:catch>
    <sql:update var="createTableList">
        create table strider_table_columns as
            select table_name, column_name, column_id
            from all_tab_columns 
            where owner = upper('${striderDbUser}')            
    </sql:update>
    <sql:update var="createTableList">
        create index tatc_tables on strider_table_columns(table_name)
    </sql:update>
    <sql:update var="createTableList">
        create index tatc_columns on strider_table_columns(column_name)
    </sql:update>
</c:if>

<%-- Function: Find the owner of an oid --%>
<c:if test="${not empty param.oid and empty param.table and not param.refer != null}">
    <%-- Get all column/table pairs for the key column --%>
    <sql:query var="tableList">
            SELECT *
            FROM strider_table_columns
            WHERE column_name = 'IDA2A2'
    </sql:query>
    
    <%-- Create query as union of queries for each table with a key --%>
    <c:set var="bigQuery" value=""/>
    <c:forEach var="aTable" items="${tableList.rows}">
    	<c:set var="quotedTableName" value="'${aTable.table_name}'"/>
    	<c:set var="columnTitle" value='"table_name"'/>
        <c:set var="bigQuery" value="${bigQuery} ${not empty bigQuery ? 'union' : ''} select ${quotedTableName} ${empty bigQuery ? columnTitle : ''} from ${aTable.table_name} where ida2a2=${param.oid}"/>
    </c:forEach>
    
    <%-- Execute the query --%>
    <sql:query var="bigQueryResult">
    	${bigQuery}
    </sql:query>
    
    <%-- Check if a row matching that oid was found --%>
    <c:set var="tableFound" value="${bigQueryResult.rowCount > 0 ? bigQueryResult.rows[0].table_name : ''}"/>
    <c:set var="oidNotFound" value="${empty tableFound}"/>
    
    <%-- Special case of multiple rows with same oid --%>
    <c:if test="${bigQueryResult.rowCount > 1}">
    	<br><b>Bad news.  Id ${param.oid} found as primary key (aka ida2a2) in more than one table.</b>
    	<table>
    	    <c:forEach var="dupTable" items="${bigQueryResult.rows}">
                <tr><td>${dupTable.table_name}</td></tr>
    	    </c:forEach>
    	</table>
    </c:if>
    <%-- Result is in tableFound --%>
</c:if>

<%-- Function: Find the tables that reference an oid --%>
<c:if test="${not empty param.oid and param.refer != null}">    
    <%-- Find columns that contain references to keys (aka ida2a2, oid, id) --%>
    <sql:query var="tableList">
            SELECT *
            FROM strider_table_columns
            WHERE column_name != 'IDA2A2' 
                and (column_name like 'IDA3%' 
                    or column_name like 'BRANCHIDA3%'
                    or column_name in ('BRANCHIDITERATIONINFO','VIEWID','IDA2TYPEDEFINITIONREFERENCE','BRANCHIDA2TYPEDEFINITIONREFE'))
            order by table_name, column_name
    </sql:query>
    
    <%-- Generated a union of selects looking for sepecified id --%>
    <c:set var="bigQuery" value=""/>
    <c:set var="prevTab" value=""/>
    <c:forEach var="aTable" items="${tableList.rows}">
    	<c:if test="${prevTab != aTable.table_name}">
    	    <c:set var="quotedTableName" value="'${aTable.table_name}'"/>
    	    <c:set var="columnTitle" value='"table_name"'/>
            <%-- the following assumes that a table with a reference has an ida2a2 column --%>
            <c:set var="bigQuery" value="${bigQuery} ${not empty bigQuery ? 'union' : ''} select ${quotedTableName} ${empty bigQuery ? columnTitle : ''},ida2a2 from ${aTable.table_name} where 1=0 "/>
            <c:set var="prevTab" value="${aTable.table_name}"/>
        </c:if>
        <c:set var="bigQuery" value="${bigQuery} or ${aTable.column_name}=${param.oid}"/>
    </c:forEach>
    
    <%-- Execute query --%>
    <sql:query var="bigQueryResult">
    	${bigQuery}
    </sql:query>
    
    <%-- Display objects found --%>
    <br><b>Id ${param.oid} is used in the following table records:</b>
    ${bigQueryResult.rowCount == 0 ? "<br><b>-- no tables found using this id --</b><br>" : ''}
    <table>
        <c:forEach var="dupTable" items="${bigQueryResult.rows}">
            <tr><td><a href="<%= request.getRequestURL()%>?table=${dupTable.table_name}&oid=${dupTable.ida2a2}${param.showAll != null ? '&showAll' : ''}">${dupTable.table_name} (${dupTable.ida2a2})</a></td></tr>
        </c:forEach>
    </table>
</c:if>

<%-- Set "utility" columns --%>
<c:set var="utilityColumns" value="BLOB$ENTRYSETADHOCACL,CREATESTAMPA2,ENTRYSETADHOCACL,MARKFORDELETEA2,MODIFYSTAMPA2,UPDATECOUNTA2,UPDATESTAMPA2,BLOB$ENTRYSET,ENTRYSET"/>
<c:set var="ignoreColumns" value="${param.showAll != null ? '' : utilityColumns}"/>

<%-- Figure out what we are querying for --%>
<c:set var="tableName" value="${not empty tableFound ? tableFound : (not empty param.table ? param.table : 'WTOrganization')}"/>
<c:set var="where" value="${not empty param.oid or not empty tableFound ? 'ida2a2 = ' : ''}"/>
<c:set var="where" value="${where}${not empty param.oid or not empty tableFound ? param.oid : ''}"/>

<%-- Retreive the target records --%>
<sql:query var="table">
    SELECT *
    FROM ${tableName} ${not empty where ? 'WHERE' : ''} ${where}
</sql:query> 
<c:set var="singleRow" value="${table.rowCount == 1}"/>

<%-- Print title and header information --%>
<c:set var="t1" value="Id ${param.oid} not found"/>
<c:set var="title" value="${oidNotFound ? t1 : tableName}"/>
<head>
    <title>${title}</title>
</head>
<body> 

<%-- Table name and actions ---%>
<h2>${title}
${singleRow ? '(id=' : ''}${singleRow ? table.rows[0].ida2a2 : ''}${singleRow ? ')' : ''}
<font size="+1">${table.rowCount > 1 ? '(' : ''}${table.rowCount > 1 ? table.rowCount : ''}${table.rowCount > 1 ? ' records)' : ''}</font></h2>
<c:if test="${table.rowCount > 0}">
    <c:if test="${not empty param.oid}">
        <a href="<%= request.getRequestURL()%>?table=${tableName}${param.showAll != null ? '&showAll' : ''}">Show All Rows</a>
        &nbsp;&nbsp;&nbsp;
    </c:if>
    <a href="<%= request.getRequestURL()%>?table=${tableName}${param.showAll != null ? '' : '&showAll'}${not empty param.oid ? '&oid=' : ''}${not empty param.oid ? param.oid : ''}">${param.showAll != null ? 'Hide Utility Columns' : 'Show Utility Columns'}</a>
    &nbsp;&nbsp;&nbsp;
    <c:if test="${not empty param.oid}">
        <a href="<%= request.getRequestURL()%>?table=${tableName}${param.showAll != null ? '&showAll' : ''}&oid=${param.oid}&refer">Tables using this ID</a>
        &nbsp;&nbsp;&nbsp;
    </c:if>
</c:if>

<%-- If no rows found, at least print the columnn names --%>
<c:if test="${table.rowCount == 0 and not empty tableName and not oidNotFound}">
    <h2>No rows found ${not empty param.table ? 'in table' : ''} ${param.table} ${not empty param.oid ? 'with id' : ''} ${param.oid} </h2>
    <sql:query var="columns">
        select column_name from strider_table_columns 
            where table_name = ? and column_name not like 'CLASSNAMEKEY%' 
            order by column_name
        <sql:param value="${tableName}"/>
    </sql:query>
    Table columns: <br>
    <c:forEach var="column" items="${columns.rows}">
        ${column.column_name != columns.rows[0].column_name ? ', ' : ''}${column.column_name} 
    </c:forEach>
    <br><br>
</c:if>

<script type="text/javascript">
    function showHide(element) {
        display = document.getElementById(element).style.display;
        document.getElementById(element).style.display = (display != 'none') ? 'none' : 'inline';
    }
</script>

<a href="#search" onclick="showHide('search');document.getElementById('table').focus();return false;">Search</a>
&nbsp;&nbsp;&nbsp;
<a href="#help" onclick="showHide('help');document.getElementById('help').focus();">Help</a>

${table.rowCount > 0 ? '<table width="98%" border=1 cellspacing=3>' : ''}

<%-- Get column list --%>
<c:set var="columnList" value=""/>
<c:set var="ignoreColumnsComma" value="${ignoreColumns},"/>
<c:forEach var="column" items="${table.rows[0]}">
    <c:choose>
        <c:when test="${fn:startsWith(column.key, 'CLASSNAMEKEY') or column.key == 'CLASSNAMEA2A2' or fn:contains(ignoreColumnsComma, column.key)}">
            <%-- drop column --%>
        </c:when>
        <c:when test="${column.key == 'IDA2A2'}">
            <c:set var="columnList" value="${column.key}${not empty columnList ? ',' : ''}${columnList}"/>
        </c:when>
        <c:otherwise>
            <c:set var="columnList" value="${columnList}${not empty columnList ? ',' : ''}${column.key}"/>
        </c:otherwise>
    </c:choose>
</c:forEach>

<%-- Print headings for table --%>
<c:if test="${not singleRow}">        
    <thead>
    <c:forTokens var="column" items="${columnList}"  delims=",">
        <th>${column}</th>
    </c:forTokens>
    </thead>
</c:if>

<%-- Display table; single row displayed vertically, multiple rows displayed as table --%>
${table.rowCount > 0 ? '<tbody>' : ''}
<c:set var="first" value="true"/>
<c:forEach var="aRow" items="${table.rows}">   
    ${not singleRow ? "<tr>" : ""}  
    <c:forTokens var="column" items="${columnList}" delims=",">
        <c:set var="columnValue" value="${aRow[column]}"/>
        ${singleRow ? "<tr><td>" : ""}${singleRow ? column : ""}${singleRow and column == "IDA2A2" ? " (primary key)" : ""}${singleRow ? "</td>" : ""}
        <td>
        <%-- These columns have matching classname fields we can use to generate a link --%>
        <c:set var="ahref" value="${(fn:startsWith(column, 'IDA3')                              
                                        or fn:startsWith(column, 'BRANCHIDA3')
                                        or column == 'BRANCHIDITERATIONINFO' 
                                        or column == 'VIEWID' 
                                        or column == 'IDA2A2' 
                                        or column == 'IDA2TYPEDEFINITIONREFERENCE' 
                                        or column == 'BRANCHIDA2TYPEDEFINITIONREFE' 
                                        or column == 'REMOTEID') 
                                    and not empty columnValue}"/> <%-- removed  and columnValue != 0 --%>
        <c:if test="${ahref}">
            <c:if test="${fn:startsWith(column, 'IDA3') or fn:startsWith(column, 'BRANCHIDA3')}">
                <c:set var="suffix" value="${fn:replace(fn:replace(column, 'BRANCHIDA3', ''), 'IDA3', '')}"/>
                <c:set var="columnName" value="CLASSNAMEKEY${suffix}"/>                             <%-- Construct column name containing class of this link --%>
                <c:set var="columnName" value="${fn:substring(columnName, 0, 28)}"/>                <%-- Truncate --%>
                <c:set var="className" value="${fn:split(aRow[columnName], '.')}"/>                 <%-- Retreive classname from row --%>
                <c:if test="${empty className[0]}">                                                    <%-- If column not found try "ROLE.." columns --%>
                    <c:set var="suffix" value="${suffix == 'A5' ? 'ROLEAOBJECTREF' : (suffix == 'B5' ? 'ROLEBOBJECTREF' : '')}"/>
                    <c:set var="columnName" value="CLASSNAMEKEY${suffix}"/>                         <%-- Construct column name containing class of this link --%>
                    <c:set var="columnName" value="${fn:substring(columnName, 0, 28)}"/>            <%-- Truncate --%>
                    <c:set var="className" value="${fn:split(aRow[columnName], '.')}"/>             <%-- Retreive classname from row and split path into array --%>
                </c:if>
                <c:set var="destTableName" value="${className[fn:length(className)-1]}"/>           <%-- Retreive simple classname as table name --%>
            </c:if>
            <c:if test="${column == 'BRANCHIDITERATIONINFO'}">                                  <%-- Set the implied classname based upon key column name --%>
                <c:set var="destTableName" value="ControlBranch"/>     
            </c:if>
            <c:if test="${column == 'BRANCHIDA2TYPEDEFINITIONREFE'}">
                <c:set var="destTableName" value="ControlBranch"/>     
            </c:if>
            <c:if test="${column == 'VIEWID'}">
                <c:set var="destTableName" value="WTView"/>            
            </c:if>
            <c:if test="${column == 'IDA2A2'}">
                <c:set var="destTableName" value="${tableName}"/>      
            </c:if>
            <c:if test="${column == 'IDA2TYPEDEFINITIONREFERENCE'}">
                <c:set var="destTableName" value="WTTypeDefinition"/>      
            </c:if>
            <c:if test="${column == 'REMOTEID'}">
                <c:set var="destTableName" value="RemoteObjectID"/>      
            </c:if>
            <c:set var="destTableName" value="${destTableName == 'View' ? 'WTView' : destTableName}"/>      <%-- apparent inconsistency between class and table name --%>
            <a href="<%= request.getRequestURL()%>?${not empty destTableName ? '&table=' : ''}${destTableName}${not empty columnValue ? '&oid=' : ''}${columnValue}${param.showAll != null ? '&showAll' : ''}">
        </c:if>
        <%-- Display cell value --%>
        <c:if test="${fn:contains(columnValue, 'oracle.sql.BLOB')}">
           <%-- it would be nice to handle blobs --%>
        </c:if>
        ${columnValue == null ? 'null' : columnValue}
        <c:if test="${ahref}">
            ${destTableName != '' and destTableName != tableName ? ' in ' : ''}${destTableName != tableName ? destTableName : ''}
            </a>
        </c:if>
        ${singleRow ? "</tr>" : ""}
        </td>
    </c:forTokens>
    ${not singleRow ? "</tr>" : ""}
</c:forEach>

${table.rowCount > 0 ? '</tbody></table>' : ''}

<div id="search" style="display: none">
<%-- Form for inputting id or table name --%>
<sql:query var="tableList">
    select table_name from strider_table_columns group by table_name order by table_name
</sql:query>
<br><br>
<h4><a name="search">Perform a search:</a></h4>
<form action="<%= request.getRequestURL()%>">
    ${param.showAll != null ? '<input type="hidden" name="showAll" value="">' : ''}
    <label for="oid" title="Search for id">Search for an id: </label>
    <input type="text" size="8" name="oid" id="oid">
    <br><br>&nbsp;&nbsp;or<br><br>
    <label for="table" title="Select Table">Select a Table to Display: </label>
    <select id="table" name="table" onchange="submit();">
        <option selected></option>
        <c:forEach var="table" items="${tableList.rows}">
            <option>${table.table_name}</option>
        </c:forEach>
     </select>
     <br><br>
     <font size="-1">
         File structure cache is ${cacheAge} days old.
         &nbsp;(<a href="<%= request.getRequestURL()%>?${getParameters}&refresh">Refresh?</a>)
     </font>
</form>
</div>

<%-- Help information --%>
<div id="help" style="display: none">
${helpText}
</div>

</body>
</html>


<%--
information for adding SQLServer support to strider
[AH-FUSION\\FUSION_ESQL05\:1433;DATABASENAME\=mcarney_x10]

wt.pom.dbPassword=SFISCHER_X10
wt.pom.dbUser=SFISCHER_X10
wt.pom.jdbc.database=SFISCHER_X10
wt.pom.jdbc.host=AH-FUSION
wt.pom.jdbc.port=1433
wt.pom.jdbc.service=FUSION_ESQL05
wt.pom.serviceName=//AH-FUSION\\FUSION_ESQL05\:1433;DatabaseName\=SFISCHER_X10

com.ptc.jdbc.sqlserver.SQLServerDriver

jdbc:ptc:sqlserver:

<c:set scope="session" var="jdbcDriver"     value="${not empty param.jdbcDriver     ? param.jdbcDriver  : (not empty jdbcDriver     ? jdbcDriver    : jdbcDriverWC)}"/>
<c:set scope="session" var="jdbcUrl"        value="${not empty param.jdbcUrl        ? param.jdbcUrl     : (not empty jdbcUrl        ? jdbcUrl       : jdbcUrlWC)}"/>
<c:set scope="session" var="dbUser"         value="${not empty param.dbUser         ? param.dbUser      : (not empty dbUser         ? dbUser        : dbUserWC)}"/>
<c:set scope="session" var="dbPassword"     value="${not empty param.dbPassword     ? param.dbPassword  : (not empty dbPassword     ? dbPassword    : dbPasswordWC)}"/>
<c:set scope="session" var="serviceName"    value="${not empty param.serviceName    ? param.serviceName : (not empty serviceName    ? serviceName   : serviceNameWC)}"/>
 Service name fix 
<c:set var="serviceNameFix" value="${fn:split(serviceName, ':')}"/>
<c:set var="serviceNameFix" value="${serviceNameFix[0]}:${serviceNameFix[1]}/${serviceNameFix[2]}"/>

<sql:setDataSource 
    driver="${not empty jdbcDriver ? jdbcDriver : 'oracle.jdbc.driver.OracleDriver'}"
    url="${jdbcUrl}@//${serviceNameFix}" 
    user="${dbUser}"
    password="${dbPassword}"
    />

strider.jsp?&jdbcDriver=com.ptc.jdbc.sqlserver.SQLServerDriver&dbUser=SFISCHER_X10&dbPassword=SFISCHER_X10&jdbcUrl=jdbc:ptc:sqlserver&serviceName=//AH-FUSION\\FUSION_ESQL05\:1433;DatabaseName\=SFISCHER_X10

--%>