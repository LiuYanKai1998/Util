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
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wis.employee.EmployeeHelper;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTMessage;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月17日
 */
public class CreateEmployeeWizardProcessor extends DefaultObjectFormProcessor{
	
	private static final String CLASSNAME   = CreateEmployeeWizardProcessor.class.getName();
	private static final Logger logger      = Logger.getLogger(CLASSNAME);
	private static final String RBCLASSNAME = "ext.wis.resource.EmployeeRB";
	
	@Override
	public FormResult preProcess(NmCommandBean nmCommandBean, List<ObjectBean> arg1) throws WTException {
		FormResult formResult = null;
		try{
			//1.接收参数进行业务处理
			HttpServletRequest request = nmCommandBean.getRequest();
			String employeeName = nmCommandBean.getTextParameter("employeeName");
			//String employeeName = request.getParameter("employeeName");
			String employeeAge  = request.getParameter("employeeAge");
			String employeeDept = request.getParameter("employeeDept");
			
			int age = Integer.parseInt(employeeAge);
			EmployeeHelper.createEmployee(employeeName, employeeDept, age);
			logger.debug("姓名:" + employeeName + "----" + "年龄:" + employeeAge + "----" + "部门:" + employeeDept);
			//2.返回成功的处理结果
			String msg = WTMessage.getLocalizedMessage(RBCLASSNAME,"CreateEmployeeSuccess",null,SessionHelper.getLocale());
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
			logger.error("创建员工出错:" + e.getMessage());
			e.printStackTrace();
			String msg = WTMessage.getLocalizedMessage(RBCLASSNAME,"CreateEmployeeFailure",null,SessionHelper.getLocale());
			FeedbackMessage failureFeedbackMessage = new FeedbackMessage(FeedbackType.FAILURE, null,null, null,new String[]{ msg});
			
			formResult = new FormResult(FormProcessingStatus.FAILURE);
			formResult.addFeedbackMessage(failureFeedbackMessage);
		}

		return formResult;
		
	}
}
