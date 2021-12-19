package ext.wisplm.demo.part.processor;

import java.util.List;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import wt.pom.Transaction;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年5月11日
 */
public class ProcessorDemo{
	
	public FormResult submit(NmCommandBean paramNmCommandBean)throws WTException{
		FormResult formResult = new FormResult();
		
		FeedbackMessage feedbackMessage = null;
		Transaction tx = null;
		try{
			tx.start();
			//001,动作1
			//002,动作2
			//003,动作3
			//事物:1个以上c,u,d使用事物,没有r
			tx.commit();
			tx = null;
			formResult.setStatus(FormProcessingStatus.SUCCESS);
			feedbackMessage = new FeedbackMessage(FeedbackType.SUCCESS, null,null, null,new String[]{ "提交审阅成功,请前往主页-任务界面完成编制任务。"});
		}catch(Exception e){
			formResult.setStatus(FormProcessingStatus.FAILURE);
			feedbackMessage = new FeedbackMessage(FeedbackType.SUCCESS, null,null, null,new String[]{ "提交审阅出错,请联系管理员处理,信息:" + e.getLocalizedMessage()});
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
		}
		formResult.addFeedbackMessage(feedbackMessage);
		return formResult;
	}	
}
