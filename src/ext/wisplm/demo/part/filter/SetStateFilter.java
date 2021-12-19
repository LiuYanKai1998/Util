package ext.wisplm.demo.part.filter;

import org.apache.log4j.Logger;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

import wt.fc.Persistable;
import wt.fc.WTReference;
import wt.part.WTPart;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月5日
 */
/**
 * 第一步 ：继承DefaultSimpleValidationFilter
 */
public class SetStateFilter extends DefaultSimpleValidationFilter{
	
	private static final Logger logger = Logger.getLogger(SetStateFilter.class);
	
	/**
	 * 第二步:重写preValidateAction方法
	 */
    public UIValidationStatus preValidateAction(UIValidationKey key, UIValidationCriteria criteria){
    	/**
    	 * UIValidationKey key:当前菜单信息
    	 * UIValidationCriteria criteria:当前所操作的对象
    	 */
    	logger.debug("当前菜单name:" + key.getComponentID() + ",objecttype name:" + key.getObjectType());
    	
    	/**
    	 * 第三步:获取当前所操作对象
    	 */
        WTReference objectRef = criteria.getContextObject();
        Persistable obj 	  = objectRef.getObject();
        WTPart part = (WTPart) obj;
        logger.debug("当前所操作对象:" + part.getDisplayIdentity());
        /**
         * 第四步:根据当前操作的业务对象
         * 执行业务逻辑判断
         */
        
        /**
         * 第五步:返回可见可操作、隐藏、可见不可操作
         */
        //可见可操作
        UIValidationStatus enabledStatus  = UIValidationStatus.ENABLED;
        //隐藏
        UIValidationStatus hiddenStatus   = UIValidationStatus.HIDDEN;
        //可见不可操作
        UIValidationStatus disabledStatus = UIValidationStatus.DISABLED;
        return enabledStatus;
        
        /*//隐藏HIDDEN，不可操作DISABLED,不可见HIDDEN
        UIValidationStatus status = UIValidationStatus.HIDDEN;

        if (obj instanceof WTDocument) {
			WTDocument craftDoc = (WTDocument) obj;
			String state = craftDoc.getLifeCycleState().toString();
			System.out.println("CraftDocSubmitFilter,当前对象状态:" + state);
			// 获取当前登录用户
			WTUser current = null;
			WTPrincipal principal = null;
			try {
				// 获取的为参与者对象,用户和组的父类
				principal = SessionHelper.manager.getPrincipal();
				current = (WTUser) principal;
				// 如果对象是正在工作状态,则可见
				if ("INWORK".equals(state)) {
					status = UIValidationStatus.ENABLED;
				}
				System.out.println("对象是否为检出状态："
						+ WorkInProgressHelper
								.isCheckedOut(craftDoc, principal));
				// 如果对象被检出,不可操作
				if (WorkInProgressHelper.isCheckedOut(craftDoc, principal)) {
					status = UIValidationStatus.DISABLED;
				}
				String name = current.getName();
				System.out.println("当前登录的用户名:" + name + "创建者名称："
						+ craftDoc.getCreatorName());
				// 如果当前用户不是创建者,不可见
				if (!name.equals(craftDoc.getCreatorName())) {
					status = UIValidationStatus.HIDDEN;
				}
			} catch (WTException e) {
				e.printStackTrace();
			}
		}*/
    }

}
