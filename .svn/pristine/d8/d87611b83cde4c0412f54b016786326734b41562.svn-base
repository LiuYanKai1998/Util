package ext.wis.ebom.datautility;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.AttributeDataUtilityHelper;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.GUIComponentArray;
import com.ptc.core.components.rendering.guicomponents.TextBox;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.core.components.rendering.guicomponents.UrlDisplayComponent;
import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.core.meta.common.impl.WCTypeInstanceIdentifier;
import com.ptc.core.meta.type.common.impl.DefaultTypeInstance;
import com.ptc.core.ui.resources.ComponentMode;

import ext.wis.constants.BusinessConstants;
import ext.wis.employee.model.Employee;
import ext.wisplm.common.WISConstants;
import ext.wisplm.util.DataUtilityUtil;
import ext.wisplm.util.NmUtil;
import ext.wisplm.util.WTUtil;
import wt.part.WTPart;
import wt.util.WTException;


/**
 * EBOM签审包详情页,部件oid转换为部件详情页的链接
 * 1、extends DefaultDataUtility；
 * 2、重写getDataValue方法
 * 3、注册该类到xconf文件codebase\ext\wis\config\wis.xconf并通过xconfmanager -p发布
	<Option serviceClass="ext.wis.ebom.datautility.EbomPackageRootPartInfoUtility" 
				selector="EbomPackageRootPartInfoUtility"  
				requestor="java.lang.Object" 
				cardinality="duplicate"/>	
   </Service>
   4、打开类型属性管理器,找到升级请求子类型"EBOM签审包",点击右侧"操作"-"编辑",点击 布局-Info Page Layout,点击根节点部件属性,编辑设置数据实用程序值为EbomPackageRootPartInfoUtility
 */
public class EbomPackageRootPartInfoUtility extends DefaultDataUtility {
	private static final Logger logger = Logger.getLogger(EbomPackageRootPartInfoUtility.class);

	@Override
	public Object getDataValue(String component_Id, Object datum, ModelContext mc) throws WTException {
		//界面元素集合
		GUIComponentArray componentArray = new GUIComponentArray();
		String rawValue = "";
		try {
			//1、component_Id,列名
			//2、原属性值
			rawValue       = (String) mc.getRawValue();
			WTPart part           = (WTPart) WTUtil.getWTObject(rawValue);
			String infoPageUrl    = BusinessConstants.infoPageBasicURL + rawValue;
			UrlDisplayComponent partInfoRef = NmUtil.createHref(part.getDisplayIdentity().toString(), infoPageUrl, "_blank");
			/*String htmlString = "<input type='button' value='生成编号' onclick=\"document.getElementById('"+textID+"').value='"+docNumber+"';\">";
			TextDisplayComponent tdc = new TextDisplayComponent("");
			tdc.setValue(htmlString);
			tdc.setCheckXSS(false);
			tdc.setColumnName(component_Id);
			//添加两个界面元素,输入框和旁边的按钮
			componentArray.addGUIComponent(textBox);*/
			componentArray.addGUIComponent(partInfoRef);
		} catch (Exception e) {
			e.printStackTrace();
			return rawValue;
		}
		return componentArray;
	}
}
