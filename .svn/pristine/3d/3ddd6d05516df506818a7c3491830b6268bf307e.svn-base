package ext.wisplm.demo.part.processor;

import java.util.List;

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

import wt.doc.WTDocument;
import wt.part.WTPart;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月5日
 */
/**
 * 第一步:继承DefaultObjectFormProcessor
 * action.xml 设置的method值为execute
 * 是因为DefaultObjectFormProcessor类里的execute方法依次执行了preProcess、doOperation、postProcess
 * 故自定义的processor类,如果继承DefaultObjectFormProcessor,重写preProcess方法即可
 */
public class SetStateProcessor  extends DefaultObjectFormProcessor {

	private static final Logger logger = Logger.getLogger(SetStateProcessor.class);
	/**
	 * 第二步:重写preProcess方法
	 */
	public FormResult preProcess(NmCommandBean nmCommandBean, List<ObjectBean> list) throws WTException {
		/**
		 * 第三步:定义默认返回结果
		 */
		FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
		FeedbackMessage feedbackmessage = null;
		/**
		 * 第四步:获取当前菜单对应的业务对象
		 */
		//通过界面参数获取当前操作关联的对象
		NmOid nmOid 	= nmCommandBean.getActionOid();
		WTPart part  = (WTPart) nmOid.getRef();
		logger.debug("设置状态,获取的主对象:" + part.getDisplayIdentity());
		
		try {
			/**
			 * 第五步:编写处理业务逻辑的代码
			 */
			/**
			 * 第六步:定义执行完该操作后的下一步操作
			 * 以下内容根据实际情况选择,如果无后续操作则直接将执行结果反馈给前台
			 */
			/**
			 * 6-1 设置执行该操作后的下一步动作(更多操作查看FormResultAction常量)
			 */
			//1,刷新当前页面,按需选择
			//formResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
			//2,执行一段js代码,按需选择
			//formResult.setJavascript("location.reload();");
			//formResult.setNextAction(FormResultAction.JAVASCRIPT);
			
			/**
			 * 6-2:设置返回给前端的成功信息
			 */
			String msg = "设置状态成功";
			//下面这段代码是固定格式,用于封装操作成功的信息
			feedbackmessage = new FeedbackMessage(FeedbackType.SUCCESS, null, null, null, new String[] { msg});
		} catch (Exception e) {
			e.printStackTrace();
			formResult = new FormResult(FormProcessingStatus.FAILURE);
			/**
			 * 6-3:设置失败信息
			 */
			//设置返回给页面的提示信息
			String msg = "设置状态失败:" + e.getLocalizedMessage();
			//下面这段代码是固定格式,用于封装操作成功的信息
			feedbackmessage = new FeedbackMessage(FeedbackType.FAILURE, null, null, null, new String[] { msg});
		}
		formResult.addFeedbackMessage(feedbackmessage);
		return formResult;
	}
}