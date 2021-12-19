package ext.wisplm.util;

import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.core.meta.common.impl.WCTypeInstanceIdentifier;
import com.ptc.core.meta.type.common.impl.DefaultTypeInstance;

/**
 *
 *Zhong Binpeng Jun 9, 2021
 */
public class DataUtilityUtil {

	/**
	 * 对象创建或编辑界面,获取对象类型值,例:wt.doc.WTDocument|com.wisplm.WISXUQIUFENXI
	 * @description
	 * @param datum
	 * @return
	 */
	public static String getTypeNameOnCreateOrUpdate(Object datum){
		 if(datum instanceof DefaultTypeInstance){ 
				DefaultTypeInstance dti = (DefaultTypeInstance) datum;
				WCTypeInstanceIdentifier instanceIdentifier = (WCTypeInstanceIdentifier) dti.getIdentifier();
		        DefinitionIdentifier definitionIdentifier = instanceIdentifier.getDefinitionIdentifier();
		        if ((definitionIdentifier instanceof WCTypeIdentifier)) {
			          WCTypeIdentifier typeIdentifier = (WCTypeIdentifier)definitionIdentifier;
			          return typeIdentifier.getTypename();
		        }
		 } 
		 return "";
	}
}
