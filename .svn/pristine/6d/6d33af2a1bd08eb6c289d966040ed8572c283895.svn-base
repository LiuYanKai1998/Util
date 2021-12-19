package ext.wis.employee.processor;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wis.employee.EmployeeHelper;
import ext.wis.employee.model.Employee;
import ext.wisplm.util.WTUtil;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTMessage;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月17日
 */
public class UpdateEmployeeWizardProcessor extends DefaultObjectFormProcessor{
	
	private static final String CLASSNAME   = UpdateEmployeeWizardProcessor.class.getName();
	private static final Logger logger      = Logger.getLogger(CLASSNAME);
	private static final String RBCLASSNAME = "ext.wis.resource.EmployeeRB";
	
	@Override
	public FormResult preProcess(NmCommandBean nmCommandBean, List<ObjectBean> arg1) throws WTException {
		FormResult formResult = null;
		try{
			//1.获取到员工oid
			HttpServletRequest request = nmCommandBean.getRequest();
			//nmCommandBean或request获取界面参数均可
    		String employeeOid = nmCommandBean.getTextParameter("employeeOid");
			String employeeName = nmCommandBean.getTextParameter("employeeName");
			String employeeAge  = request.getParameter("employeeAge");
			String employeeDept = request.getParameter("employeeDept");
			
			Employee employee  = (Employee) WTUtil.getWTObject(employeeOid);
			int age = Integer.parseInt(employeeAge);
			EmployeeHelper.update(employee,employeeName, employeeDept, age);
			logger.debug("姓名:" + employeeName + "----" + "年龄:" + employeeAge + "----" + "部门:" + employeeDept);
			//2.返回成功的处理结果,读取rbinfo文件进行国际化显示
			String msg = WTMessage.getLocalizedMessage(RBCLASSNAME,"UpdateEmployeeSuccess",null,SessionHelper.getLocale());
			FeedbackMessage successFeedbackMessage = new FeedbackMessage(FeedbackType.SUCCESS, null,null, null,new String[]{ msg});
			
			formResult = new FormResult(FormProcessingStatus.SUCCESS);
			formResult.addFeedbackMessage(successFeedbackMessage);
			//3.定义下一步动作,刷新父页面
			formResult.setNextAction(FormResultAction.REFRESH_OPENER);
			//formResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
			//2,执行一段js代码,按需选择
			//formResult.setJavascript("location.reload();");
			//formResult.setNextAction(FormResultAction.JAVASCRIPT);
			
		}catch(Exception e){
			logger.error("更新员工出错:" + e.getMessage());
			e.printStackTrace();
			//读取rbinfo文件进行国际化显示,key为UpdateEmployeeFailure
			String msg  = WTMessage.getLocalizedMessage(RBCLASSNAME,"UpdateEmployeeFailure",null,SessionHelper.getLocale());
			msg+=e.getMessage();
			FeedbackMessage failureFeedbackMessage = new FeedbackMessage(FeedbackType.FAILURE, null,null, null,new String[]{ msg});
			
			formResult = new FormResult(FormProcessingStatus.FAILURE);
			formResult.addFeedbackMessage(failureFeedbackMessage);
		}

		return formResult;
		
	}
}
