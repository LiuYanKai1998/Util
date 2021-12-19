package ext.wisplm.demo.part.filter;

import org.apache.log4j.Logger;

import com.ptc.core.ui.validation.DefaultSimpleValidationFilter;
import com.ptc.core.ui.validation.UIValidationCriteria;
import com.ptc.core.ui.validation.UIValidationKey;
import com.ptc.core.ui.validation.UIValidationStatus;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月5日
 */
/**
 * 第一步 ：继承DefaultSimpleValidationFilter
 */
public class SubmitFilter extends DefaultSimpleValidationFilter{
	
	private static final Logger logger = Logger.getLogger(SubmitFilter.class);
	
	/**
	 * 第二步:重写preValidateAction方法
	 */
    public UIValidationStatus preValidateAction(UIValidationKey key, UIValidationCriteria criteria){
        //可见不可操作
        UIValidationStatus disabledStatus = UIValidationStatus.DISABLED;
        return disabledStatus;
    }
}
