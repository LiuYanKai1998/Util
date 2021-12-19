package ext.wis.ebom.processor;

import java.util.List;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.forms.DefaultObjectFormProcessor;
import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wis.ebom.EBOMHelper;
import ext.wis.resource.BomRB;
import ext.wisplm.util.WTMessageUtil;
import wt.maturity.PromotionNotice;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.util.WTMessage;

/**
 * EBOM提交审阅
 */
public class SubmitReviewEBOMProcessor extends DefaultObjectFormProcessor{
	
	@Override
	public FormResult preProcess(NmCommandBean nmCommandBean, List<ObjectBean> arg1) throws WTException{
        FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
        Transaction tx = null;
        try{
        	tx = new Transaction();
        	tx.start();
        	
    		NmOid nmOid  = nmCommandBean.getActionOid();
    		WTPart part  = (WTPart) nmOid.getRef();
    		//依据语言环境获取rbinfo文字
    		String rbClassName = BomRB.class.getName();//ext.wis.resource.BomRB
    		String rbKey       = "submitEBOMSuccess";
    		String msg         = WTMessage.getLocalizedMessage(rbClassName,rbKey,null,SessionHelper.getLocale());
    		
    		//创建签审包
    		PromotionNotice ebomPackage = EBOMHelper.createEBOMPackage(part);
    		
    		//刷新部件详情页,让filter重新加载
    		formResult.setNextAction(FormResultAction.REFRESH_CURRENT_PAGE);
    		//返回成功的提示信息并增加签审包信息链接
    		WTMessageUtil.addSuccessFeedbackMessage(formResult,msg,ebomPackage);
    		tx.commit();
    		return formResult;
        }catch(Exception e){
        	e.printStackTrace();
        	tx.rollback();
        	WTMessageUtil.addFailureFeedbackMessage(formResult, "EBOM提交审阅失败,请联系管理员:" + e.getLocalizedMessage());
        	return formResult;
        }

	}



}
