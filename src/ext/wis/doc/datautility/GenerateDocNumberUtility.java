package ext.wis.doc.datautility;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.AttributeDataUtilityHelper;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.GUIComponentArray;
import com.ptc.core.components.rendering.guicomponents.TextBox;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.core.meta.common.impl.WCTypeInstanceIdentifier;
import com.ptc.core.meta.type.common.impl.DefaultTypeInstance;
import com.ptc.core.ui.resources.ComponentMode;

import ext.wis.employee.model.Employee;
import ext.wisplm.util.DataUtilityUtil;
import ext.wisplm.util.NmUtil;
import ext.wisplm.util.WTUtil;
import wt.util.WTException;


/**
 * 创建WIS需求分析说明书时,生成自定义编号的datautility类
 * 1、extends DefaultDataUtility；
 * 2、重写getDataValue方法
 * 3、注册该类到xconf文件codebase\ext\wis\config\wis.xconf并通过xconfmanager -p发布
   <Service context="default" name="com.ptc.core.components.descriptor.DataUtility" targetFile="codebase/wt.properties">
		<Option serviceClass="ext.wis.employee.datautility.EmployeeInfoDataUtility" 
				selector="EmployeeInfoDataUtility"  
				requestor="java.lang.Object" 
				cardinality="duplicate"/>
		<!-- 创建WIS需求分析说明书时,生成文档编号的datautility类,在该类型布局Create New Layout -编号-设置数据实用程序值为GenerateDocNumberUtility-->
		<Option serviceClass="ext.wis.doc.datautility.GenerateDocNumberUtility" 
				selector="GenerateDocNumberUtility"  
				requestor="java.lang.Object" 
				cardinality="duplicate"/>	
   </Service>
   4、打开类型属性管理器,找到文档子类型"WIS需求分析说明书",点击右侧"操作"-"编辑",点击 布局-Create New Layout,点击编号属性,编辑设置数据实用程序值为GenerateDocNumberUtility
 */
public class GenerateDocNumberUtility extends DefaultDataUtility {
	private static final Logger logger = Logger.getLogger(GenerateDocNumberUtility.class);

	@Override
	public Object getDataValue(String component_Id, Object datum, ModelContext mc) throws WTException {
		//界面元素集合
		GUIComponentArray componentArray = new GUIComponentArray();
		try {
			//1、component_Id,列名
			//2、原属性值
			String rawValue       = (String) mc.getRawValue();
			//获取当前界面布局类型,EDIT、CREATE、VIEW等
			//ComponentMode.CREATE;ComponentMode.EDIT;
			ComponentMode mode    = mc.getDescriptorMode();
			//获取当前界面传递的类型参数
			String docType        = DataUtilityUtil.getTypeNameOnCreateOrUpdate(datum);
			//截取最后一段逻辑标识符,作为文档编号前缀
			String docTypeLogicId = docType.substring(docType.lastIndexOf(".") + 1,docType.length());
			//获取OOTB输入框名称,存在特定命名规则,不要修改
			String name           = AttributeDataUtilityHelper.getColumnName(component_Id,datum, mc);
			//获取原输入框id
			String textID         = component_Id;
			TextBox textBox       = new TextBox();
			//注意此处不要设置nametextBox.setName(name);
			textBox.setId(textID);
			//
			textBox.setColumnName(name);
			//设置输入的最大长度
			textBox.setMaxLength(50);
			//设置文本框Size属性
			textBox.setWidth(70);
			//textBox.set
			//构建生成编号按钮
			//yyyyMMddHHmmss
			String docNumber = docTypeLogicId + "_"+WTUtil.formatTime(new Timestamp(System.currentTimeMillis()),"yyyyMMddHHmmss");
			
			String htmlString = "<input type='button' value='生成编号' onclick=\"document.getElementById('"+textID+"').value='"+docNumber+"';\">";
			TextDisplayComponent tdc = new TextDisplayComponent("");
			tdc.setValue(htmlString);
			tdc.setCheckXSS(false);
			tdc.setColumnName(component_Id);
			//添加两个界面元素,输入框和旁边的按钮
			componentArray.addGUIComponent(textBox);
			componentArray.addGUIComponent(tdc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return componentArray;
	}
}
