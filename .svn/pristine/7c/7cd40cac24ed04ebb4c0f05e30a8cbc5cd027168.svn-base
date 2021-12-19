package ext.wis.employee.filter;

import org.apache.log4j.Logger;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

import wt.fc.Persistable;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月5日
 */
/**
 * 第一步 ：继承DefaultSimpleValidationFilter
 */
public class EmployeeTableToolBarFilter extends DefaultSimpleValidationFilter{
	
	private static final Logger logger = Logger.getLogger(EmployeeTableToolBarFilter.class);
	
	/**
	 * 第二步:重写preValidateAction方法
	 */
    public UIValidationStatus preValidateAction(UIValidationKey key, UIValidationCriteria criteria){
    	UIValidationStatus status = UIValidationStatus.HIDDEN;
    	try{
    	  	/**
        	 * UIValidationKey key:当前菜单信息
        	 * UIValidationCriteria criteria:当前所操作的对象
        	 */
        	logger.debug("当前菜单name:" + key.getComponentID() + ",objecttype name:" + key.getObjectType());
        	
        	/**
        	 * 第三步:获取当前操作的上下文对象
        	 */
            WTReference objectRef = criteria.getContextObject();
            if(objectRef != null){
                WTObject obj 	  	   = (WTObject) objectRef.getObject();
                logger.debug("当前所操作对象:" + obj.getDisplayIdentity());
            }
            /**
             * 第四步:根据当前操作的业务对象
             * 执行业务逻辑判断,管理员可见可操作,其他用户可见不可操作
             */
    		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
    		if("Administrator".equalsIgnoreCase(user.getName())){
    			status = UIValidationStatus.ENABLED;
    		}else{
    			status = UIValidationStatus.DISABLED;
    		}
    	}catch(Exception e){
    		logger.error("员工管理菜单权限控制出错:" + e.getMessage());
    		e.printStackTrace();
    		status = UIValidationStatus.HIDDEN;
    	}
    	return status;
    }
}
