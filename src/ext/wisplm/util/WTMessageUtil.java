package ext.wisplm.util;

import java.util.Locale;
import java.util.ResourceBundle;

import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;

import wt.fc.WTObject;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.resource.RBComment;
import wt.util.resource.RBEntry;

/**
 * 资源文件显示处理类
 */
public class WTMessageUtil {
	
	/**
	 * 获取系统当前语言环境对应的rbInfo对象
	 * @description
	 * @param rbClassName 如:com.ptc.core.ui.componentRB
	 * @return
	 * @throws WTException
	 */
	public static ResourceBundle getLocalResourceBundle(String rbClassName) throws WTException{
		ResourceBundle rb = ResourceBundle.getBundle(rbClassName, SessionHelper.getLocale());
		return rb;
	}
	
	/**
	 * 获取指定语言环境对应的rbInfo对象
	 * @description
	 * @param rbClassName 如:com.ptc.core.ui.componentRB
	 * @param local 如:Locale.SIMPLIFIED_CHINESE
	 * @return
	 * @throws WTException
	 */
	public static ResourceBundle getLocalResourceBundle(String rbClassName,Locale local) throws WTException{
		ResourceBundle rb = ResourceBundle.getBundle(rbClassName, local);
		return rb;
	}
	
	/**
	 * 获取系统当前语言环境对应的rbInfo值
	 * @description
	 * @param rbClassName   如:com.ptc.core.ui.componentRB
	 * @param attributeName RBInfo类中的key,即常量值
	 * @return @ RBEntry注解值
	 * @throws WTException
	 */
	public static String getLocalMessage(String rbClassName,String attributeName) throws WTException{
		String msg = WTMessage.getLocalizedMessage(rbClassName,attributeName,null,SessionHelper.getLocale());
		return msg;
	}

	/**
	 * 获取指定语言环境对应的rbInfo值
	 * @description
	 * @param rbClassName
	 * @param attributeName
	 * @param local
	 * @return
	 * @throws WTException
	 */
	public static String getLocalMessage(String rbClassName,String attributeName,Locale local) throws WTException{
		String msg = WTMessage.getLocalizedMessage(rbClassName,attributeName,null,local);
		return msg;
	}
	
	/**
	 * 获取失败类型的FeedbackMessage
	 * @description
	 * @param msg 提示信息
	 * @return
	 * @throws WTException
	 */
	public static FeedbackMessage getFailureFeedbackMessage(String msg) throws WTException{
		FeedbackMessage message = new FeedbackMessage(FeedbackType.FAILURE, null,null, null,new String[]{ msg});
		return message;
	}
	
	/**
	 * 获取失败类型的FeedbackMessage
	 * @description
	 * @param rbClassName   提示信息对应的rbInfo类class名称
	 * @param attributeName RBInfo类中的key,即常量值
	 * @return
	 * @throws WTException
	 */
	public static FeedbackMessage getFailureFeedbackMessage(String rbClassName,String attributeName) throws WTException{
		Locale local = SessionHelper.getLocale();
		String msg = getLocalMessage(rbClassName,attributeName,local);
		return getFailureFeedbackMessage(msg);
	}
	
	public static FeedbackMessage getSuccessFeedbackMessage(String msg) throws WTException{
		FeedbackMessage message = new FeedbackMessage(FeedbackType.SUCCESS, null,null, null,new String[]{ msg});
		return message;
	}
	
	public static FeedbackMessage getSuccessFeedbackMessage(String rbClassName,String attributeName) throws WTException{
		Locale local = SessionHelper.getLocale();
		String msg = getLocalMessage(rbClassName,attributeName,local);
		//FeedbackMessage message = new FeedbackMessage(FeedbackType.SUCCESS, local,"", null,msg);
		return getSuccessFeedbackMessage(msg);
	}
	
	public static FeedbackMessage addFailureFeedbackMessage(FormResult result,String rbClassName,String attributeName) throws WTException{
		FeedbackMessage message = getFailureFeedbackMessage(rbClassName,attributeName);
		result.addFeedbackMessage(message);
		return message;
	}
	
	public static FeedbackMessage addSuccessFeedbackMessage(FormResult result,String rbClassName,String attributeName) throws WTException{
		FeedbackMessage message = getSuccessFeedbackMessage(rbClassName,attributeName);
		result.addFeedbackMessage(message);
		return message;
	}
	
	public static FeedbackMessage addFailureFeedbackMessage(FormResult result,String msg) throws WTException{
		FeedbackMessage message = getFailureFeedbackMessage(msg);
		result.addFeedbackMessage(message);
		return message;
	}
	
	public static FeedbackMessage addSuccessFeedbackMessage(FormResult result,String msg) throws WTException{
		FeedbackMessage message = getSuccessFeedbackMessage(msg);
		result.addFeedbackMessage(message);
		return message;
	}
	
	public static FeedbackMessage addSuccessFeedbackMessage(FormResult result,String msg,WTObject displayObject) throws WTException{
		FeedbackMessage message = addSuccessFeedbackMessage(result,msg);
		if(displayObject != null){
			message.addOidIdentityPair(displayObject,Locale.SIMPLIFIED_CHINESE);
		}
		return message;
	}
}
