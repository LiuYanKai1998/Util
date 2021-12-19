package ext.wis.ebom.filter;

import org.apache.log4j.Logger;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

import ext.wis.constants.BusinessConstants;
import ext.wisplm.util.PrincipalUtil;
import wt.fc.Persistable;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * EBOM提交审阅过滤器
 * 部件为正在工作状态可见，其他条件设置为灰色
 */
/**
 * 第一步 ：继承DefaultSimpleValidationFilter
 */
public class SubmitReviewEBOMFilter extends DefaultSimpleValidationFilter{
	
	private static final Logger logger = Logger.getLogger(SubmitReviewEBOMFilter.class);
	
	/**
	 * 第二步:重写preValidateAction方法
	 */
    public UIValidationStatus preValidateAction(UIValidationKey key, UIValidationCriteria criteria){
    	UIValidationStatus status = UIValidationStatus.DISABLED;
        WTReference objectRef     = criteria.getContextObject();
        WTObject obj 	  	      = (WTObject) objectRef.getObject();
        WTPart  part = (WTPart) obj;
        if(BusinessConstants.State.INWORK.equals(part.getLifeCycleState().toString())){
        	status = UIValidationStatus.ENABLED;
        }
    	return status;
    }
}
