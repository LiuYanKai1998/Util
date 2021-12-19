package ext.wisplm.demo.wizard.processor;

import java.util.List;

import org.apache.log4j.Logger;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wisplm.util.NmUtil;
import ext.wisplm.util.WTMessageUtil;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月17日
 */
public class WizardProcessor extends DefaultObjectFormProcessor{
	
	private static final String CLASSNAME = WizardProcessor.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	@Override
	public FormResult preProcess(NmCommandBean nmCommandBean, List<ObjectBean> arg1) throws WTException {
		FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
		logger.debug("preProcess-执行");
		String number = new NmUtil(nmCommandBean).getParamValue("step1Number");
		String name   = new NmUtil(nmCommandBean).getParamValue("step2Name");
		String version = new NmUtil(nmCommandBean).getParamValue("step3Version");
		logger.debug("编号:" + number + "----" + "名称:" + name + "----" + "版本:" + version);
		WTMessageUtil.addSuccessFeedbackMessage(formResult, "ext.wisplm.demo.resources.CustomOperationRB", "WizardProcessorMsgSuccess");
		return formResult;
		
	}
}
