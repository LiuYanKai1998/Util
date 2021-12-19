package ext.wisplm.demo.wizard.processor;

import java.util.List;

import org.apache.log4j.Logger;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.DefaultObjectFormProcessorDelegate;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wisplm.util.NmUtil;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月17日
 */
public class WizardStep2Processor extends DefaultObjectFormProcessorDelegate{
	
	private static final String CLASSNAME = WizardStep2Processor.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	@Override
	public FormResult doOperation(NmCommandBean nmCommandBean, List<ObjectBean> arg1) throws WTException {
		FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
		String number = new NmUtil(nmCommandBean).getParamValue("step1Number");
		String name   = new NmUtil(nmCommandBean).getParamValue("step2Name");
		String version = new NmUtil(nmCommandBean).getParamValue("step3Version");
		logger.debug("doOperation-编号:" + number + "----" + "名称:" + name + "----" + "版本:" + version);
		FeedbackMessage feedbackmessage = new FeedbackMessage(FeedbackType.SUCCESS, null, null, null, new String[] { "WizardStep2执行成功"});
		formResult.addFeedbackMessage(feedbackmessage);
		return formResult;
	}
}
