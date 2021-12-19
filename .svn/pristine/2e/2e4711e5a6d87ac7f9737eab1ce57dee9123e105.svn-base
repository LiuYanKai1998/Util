package ext.wis.doc.workflow;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;

import ext.wisplm.util.ContentUtil;
import ext.wisplm.util.WfUtil;
import ext.wisplm.util.WorkableUtil;
import wt.doc.WTDocument;
import wt.fc.ObjectReference;
import wt.fc.WTObject;
import wt.method.RemoteAccess;
import wt.util.WTException;
import wt.vc.wip.Workable;

/**
 *技术文件工作流操作相关代码
 *
 *Zhong Binpeng Apr 23, 2021
 */
public class TechDocWorkflowHelper implements RemoteAccess{
	
	private static final String CLASSNAME = TechDocWorkflowHelper.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	
	/**
	 * 需求分析说明书,编制/修改环节选择"提交审阅"路由完成校验
	 * 1、文档必须有主内容;
	 * 2、文档不能为检出状态;
	 * 3、校对者、批准者必须选择了用户。
	 * 
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @throws PropertyVetoException 
	 * @throws WTException 
	 */
	public static void submitReviewValidate(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		String errorMsg = "";
		try{ 
			logger.debug("需求分析说明书审批流程的提交审阅环节校验开始:" + primaryBusinessObject.getDisplayIdentifier());
			//1.主内容文件校验
			if(ContentUtil.getPrimary((wt.content.ContentHolder)primaryBusinessObject) == null){
				errorMsg +=("主内容文件为空,不能提交审阅;\n");
			}
			WTDocument doc = (WTDocument) primaryBusinessObject;
			//2.检出状态校验
			if(WorkableUtil.isCheckedOut(doc)){
				errorMsg +=("文档为检出状态,不能提交审阅;\n");
			}
			//3.参与者校验:校对者和批准者不能为空
			if(!WfUtil.hasUserInRole(self, primaryBusinessObject, "WISJIAODUIZHE")){
				errorMsg +="请为校对者选择用户;\n";
			}
			if(!WfUtil.hasUserInRole(self, primaryBusinessObject, "WISPIZHUNZHE")){
				errorMsg +="请为批准者选择用户。";
			}
		}catch(Exception e){
			logger.error("需求分析说明书审批流程的提交审阅环节校验出错",e);
		}
		if(!"".equals(errorMsg)){
			throw new WTException(errorMsg);
		}
	}
	
	
	/**
	 * 需求分析说明书,编制/修改环节选择"取消审阅"路由完成校验
	 * 1、文档不能为检出状态。
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @throws WTException 
	 */
	public static void cancelReviewValidate(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		if(WorkableUtil.isCheckedOut((Workable)primaryBusinessObject)){
			throw new WTException("文档为检出状态,不能提交审阅;\n");
		}
	}
}
