package ext.wis.doc.dalegate;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessorDelegate;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wisplm.util.DocUtil;
import ext.wisplm.util.WTUtil;
import wt.doc.WTDocument;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * 通过dalegate在创建文档时修改默认编号
 */
public class DocumentProcessorDalegate  extends DefaultObjectFormProcessorDelegate{
	
	private static final String CLASS_NAME = DocumentProcessorDalegate.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASS_NAME);
	
	/**
	 * 重写postProcess
	 */
	@Override
	public FormResult postProcess(NmCommandBean nmCommandBean, List<ObjectBean> objectBeans) throws WTException {
		
		FormResult formResult = null;
		try {
			//1、继承父类的postProcess逻辑
			formResult = super.postProcess(nmCommandBean, objectBeans);
			
			HttpServletRequest request = nmCommandBean.getRequest();
			//2.获取action name判断是否为创建文档
	        String actionName = request.getParameter("actionName");
	        WTUser user = (WTUser)SessionHelper.manager.getPrincipal();
			for (ObjectBean objbean : objectBeans) {
				Object obj = objbean.getObject();
				//3、获取创建的文档对象
				if (obj instanceof WTDocument) {
					WTDocument doc = (WTDocument) obj;
					if("create".equalsIgnoreCase(actionName)){
						String number = WTUtil.formatTime(new Timestamp(System.currentTimeMillis()), "yyyy-MM-ddHH:mm:ss");
						DocUtil.changeNumber(doc, number, "");
					}
				}
			}	
			return formResult;
		}catch(WTException e){
			throw e;
		}catch(Exception e){
			formResult = new FormResult();
			formResult.setStatus(FormProcessingStatus.FAILURE);
			FeedbackMessage feedbackmessage = new FeedbackMessage(FeedbackType.FAILURE, null, null, null,
					new String[] {"创建失败，请联系管理员解决：" + e.getMessage()});
			formResult.addFeedbackMessage(feedbackmessage);
			e.printStackTrace();
		}
		return formResult;
	}
	
}
