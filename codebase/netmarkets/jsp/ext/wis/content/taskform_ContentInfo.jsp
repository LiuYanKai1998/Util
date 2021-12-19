<%@page language="java" session="true" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="ext.wisplm.util.WfUtil"%>
<%@page import="com.ptc.netmarkets.util.table.NmDefaultHTMLTable"%>
<%@page import="wt.content.ContentHolder"%>
<%@page import="wt.content.ContentRoleType"%>
<%@page import="wt.fc.Persistable"%>
<!-- 主内容和附件表格,任务表单模板中调用,loadFiles\ext\wis\taskform\Site5_wt.fc.WTObject_WfTask_需求分析说明书签审模板.jsp-->
<%
	String workItemOid = request.getParameter("oid");
	Persistable pbo = WfUtil.getPBO(workItemOid);
	if(pbo !=null && pbo instanceof ContentHolder){
	request.setAttribute("CONTENTHOLDER", (ContentHolder)pbo);
	request.setAttribute("ROLEPRIMARY", ContentRoleType.PRIMARY);
	request.setAttribute("ROLESECONDARY", ContentRoleType.SECONDARY);

%>	
	<tr>
		<td align="right" valign="top" nowrap width="11%">
			<FONT class=tabledatafont><b>
				<span align="right" class="pp" style="font-weight: bold;">
				<label for="workitem_pbolink" ></label>
				</span>
	   </b></font>		
		</td>



	  <td valign="top">
		<jca:describeTable var="attTable" id="attTableTable" type="wt.content.ContentItem" label="主要内容" configurable="false" >
			<jca:describeColumn id="attachmentsName" label="文件名称或标签" />
			<!-- 系统bug,删除infoPageAction列 -->
			<jca:describeColumn id="formatIcon"      sortable="false"  />
			<jca:describeColumn id="formatName"      label="格式" />
			<jca:describeColumn id="description"     label="附件说明" />
			<jca:describeColumn id="thePersistInfo.modifyStamp" />
			<jca:describeColumn id="modifier"        need="modifiedBy" targetObject="modifiedBy"/>
		</jca:describeTable>

		<!-- Define the query for the Attachments table -->
		<jca:getModel var="attTableModel" descriptor="${attTable}"
					   serviceName="com.ptc.windchill.enterprise.attachments.server.AttachmentsService"
					   methodName="getAttachments">
			<jca:addServiceArgument value="${CONTENTHOLDER}" type="wt.content.ContentHolder"/>
			<jca:addServiceArgument value="${ROLEPRIMARY}"/>
		</jca:getModel>  	
			
		<%-->Get the NmHTMLTable from the command<--%>
		<jca:renderTable model="${attTableModel}" pageLimit="0" showCount="true" helpContext="doc_references_doc"/>
		</td>
	</tr>
	<!--------------------------------------------------附件表格---------------------------------------------->
	<tr>
		<td align="right" valign="top" nowrap width="11%">
			<FONT class=tabledatafont><b>
				<span align="right" class="pp" style="font-weight: bold;">
				<label for="workitem_pbolink" ></label>
				</span>
			 </b></font>		
		</td>
	  <td valign="top">
			<jca:describeTable var="attTable2" id="attTableTable2" type="wt.content.ContentItem" label="附件" configurable="false" >
				<jca:describeColumn id="attachmentsName" label="文件名称或标签" />
				<!-- 系统bug,删除infoPageAction列 -->
				<jca:describeColumn id="formatIcon"      sortable="false"  />
				<jca:describeColumn id="formatName"      label="格式" />
				<jca:describeColumn id="description"     label="附件说明" />
				<jca:describeColumn id="thePersistInfo.modifyStamp" />
				<jca:describeColumn id="modifier"        need="modifiedBy" targetObject="modifiedBy"/>
			</jca:describeTable>

			<!-- Define the query for the Attachments table -->
			<jca:getModel var="attTableModel2" descriptor="${attTable2}"
						   serviceName="com.ptc.windchill.enterprise.attachments.server.AttachmentsService"
						   methodName="getAttachments">
				<jca:addServiceArgument value="${CONTENTHOLDER}" type="wt.content.ContentHolder"/>
				<jca:addServiceArgument value="${ROLESECONDARY}"/>
			</jca:getModel>  	
				
			<%-->Get the NmHTMLTable from the command<--%>
			<jca:renderTable model="${attTableModel2}" pageLimit="0" showCount="true" helpContext="doc_references_doc"/>  	           	           	
		</td>
	</tr>
<%
	}
%>
