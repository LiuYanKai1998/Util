package ext.wis.ebom.filter;

import org.apache.log4j.Logger;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

import ext.wisplm.util.PrincipalUtil;
import wt.fc.Persistable;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * 下载EBOM清册过滤器
 * 1、"结构设计"组里的用户可见;
 * 2、产品库团队角色"结构设计师"里的用户可见;
 * 3、其他用户隐藏。
 */
/**
 * 第一步 ：继承DefaultSimpleValidationFilter
 */
public class DownloadEBOMListFilter extends DefaultSimpleValidationFilter{
	
	private static final Logger logger = Logger.getLogger(DownloadEBOMListFilter.class);
	
	/**
	 * 第二步:重写preValidateAction方法
	 */
    public UIValidationStatus preValidateAction(UIValidationKey key, UIValidationCriteria criteria){
    	UIValidationStatus status = UIValidationStatus.HIDDEN;
    	try{
    		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
    		//"结构设计"组里的用户可见
    		if(PrincipalUtil.isInGroup("结构设计", user)){
    			status = UIValidationStatus.ENABLED;
    		}else{
    			//获取部件对象
    			//产品库团队角色"结构设计师"里的用户可见
                WTReference objectRef = criteria.getContextObject();
                WTObject obj 	  	  = (WTObject) objectRef.getObject();
                WTPart  part = (WTPart) obj;
                if(PrincipalUtil.isContainerRoleMember(user, part.getContainer(), "WISJIEGOUSHEJISHI")){
                	status = UIValidationStatus.ENABLED;
                }
    		}
    	}catch(Exception e){
    		logger.error("下载EBOM清册权限控制出错:" + e.getMessage());
    		e.printStackTrace();
    		status = UIValidationStatus.HIDDEN;
    	}
    	return status;
    }
}
