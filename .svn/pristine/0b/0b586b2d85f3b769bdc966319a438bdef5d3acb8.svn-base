package ext.wis.employee.processor;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.misc.NmContext;

import ext.wis.employee.model.Employee;
import wt.fc.PersistenceHelper;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
	自定义action processor示例(不继承DefaultObjectFormProcessor也可以)
 * action中class使用此类名
 * method使用此方法名(自定义即可),参数NmCommandBean为前端传过来的固定格式
 * 示例:
	<action name="deleteEmployee">
		<command class="ext.wis.employee.processor.DeleteEmployeeProcessor" method="execute" windowType="normal"/>
		<includeFilter name="DownloadEBOMListFilter" />
	</action>
 */
/**
 * 表格工具栏菜单对应的processor类-获取表格选中对象示例
 */
public class DeleteEmployeeProcessor {

	private static final Logger logger = Logger.getLogger(DeleteEmployeeProcessor.class);
	
    public static FormResult deleteEmployee(NmCommandBean nmcommandbean) throws WTException {

        FormResult formResult = new FormResult();
        //执行状态,默认为成功
        formResult.setStatus(FormProcessingStatus.SUCCESS);
        //返回的提示信息
        FeedbackMessage feedbackmessage = null;
        
		NmOid nmOid 	= nmcommandbean.getActionOid();
		Object obj      = nmOid.getRef();
		WTSet deleteSet = new WTHashSet();
		String employeeNames = "";
		//表格右键选中删除
		if(obj instanceof Employee){
			Employee employee = (Employee) obj;
			 employeeNames    += employee.getName()+";";
			deleteSet.add(employee);
		}else{
			//表格工具栏选中删除
	        //获取mvc表格选中的对象oid
	        ArrayList<NmContext> selectedOids = nmcommandbean.getSelectedContextsForPopup();
	        logger.debug("选中的对象数量:" + selectedOids.size());
	        if(selectedOids == null || selectedOids.size() == 0){
	        	throw new WTException("请至少选择一个员工");
	        } 
	        for (int i = 0; i < selectedOids.size(); i++) {
		         NmContext nmcontext = (NmContext) selectedOids.get(i);
		         //获取选中对象,转换为对应的持久化类即可
		         Employee employee = (Employee) nmcontext.getTargetOid().getWtRef().getObject();
		         deleteSet.add(employee);
		         employeeNames += employee.getName()+";";
		         logger.debug("DeleteEmployeeProcessor获取到选中的员工:" + employee.getDisplayIdentity());
	        }
		}
        try{
        	//执行删除
        	PersistenceHelper.manager.delete(deleteSet);
        	//FeedbackType为成功或出错标识,在界面显示文字不同
    		feedbackmessage = new FeedbackMessage(FeedbackType.SUCCESS,SessionHelper.getLocale(), "", null, "以下员工删除成功:" + employeeNames);
    		//删除成功,设置formresult下一步动作,通过js刷新表格
    		formResult.setNextAction(FormResultAction.REFRESH_OPENER);
			
    		//3.定义下一步动作,刷新父页面
			//formResult.setNextAction(FormResultAction.REFRESH_OPENER);
			//formResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
			//2,执行一段js代码,按需选择
			//formResult.setJavascript("location.reload();");
			//formResult.setNextAction(FormResultAction.JAVASCRIPT);
			
        }catch(Exception e){
        	logger.error("删除员工出错:" + e.getMessage());
        	feedbackmessage = new FeedbackMessage(FeedbackType.FAILURE,SessionHelper.getLocale(), "", null, "以下员工删除失败:" + employeeNames + ",错误信息:" + e.getMessage());
        }
        formResult.addFeedbackMessage(feedbackmessage);
        return formResult;
    }
}
