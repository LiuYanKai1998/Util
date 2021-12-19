package ext.wisplm.demo.part.datautility;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.AbstractDataUtility;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.GUIComponentArray;
import com.ptc.core.components.rendering.guicomponents.UrlDisplayComponent;
import com.ptc.windchill.enterprise.wip.datautilities.rendering.guicomponents.RawStringDisplayComponent;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.inf.container.WTContainer;
import wt.util.WTException;

public class PartNameDataUtility  extends AbstractDataUtility {
	
	@Override
	public Object getDataValue(String component_Id, Object datum, ModelContext mc) throws WTException {
		/**
		 * component_Id:属性名称/表格列名
		 * datum:当前操作上下文对象
		 * rawValue:当前显示值
		 */
		Object rawValue = DefaultDataUtility.getRawValue(datum, mc);
		System.out.println("datum------------------>" + datum);
		System.out.println("rawValue-------------->" + rawValue);
		System.out.println("component_Id---------->" + component_Id);
		
		GUIComponentArray guicomponentarray = new GUIComponentArray();  
		try{
			String htmlString = "<a href='baidu.com'>点击此处</a>";
			/**
			 * 万金油HTML对象
			 */
			RawStringDisplayComponent typeComponent = new RawStringDisplayComponent("",htmlString);
			return typeComponent;
			//return guicomponentarray;
		}catch(Exception e){
			e.printStackTrace();
		}
		return rawValue;
		
	}
}
