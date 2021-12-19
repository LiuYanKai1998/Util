package ext.wis.employee.datautility;

import java.util.Map;

import org.apache.log4j.Logger;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.dataUtilities.DefaultDataUtility;
import com.ptc.core.components.rendering.guicomponents.GUIComponentArray;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;

import ext.wis.employee.model.Employee;
import ext.wisplm.util.NmUtil;
import wt.util.WTException;


/**
 * 界面数据重新组织显示的处理类
 * 1、extends DefaultDataUtility；
 * 2、重写getDataValue方法
 * 3、注册该类到xconf文件codebase\ext\wis\config\wis.xconf并通过xconfmanager -p发布
	    <Service context="default" name="com.ptc.core.components.descriptor.DataUtility" targetFile="codebase/wt.properties">
			<Option serviceClass="ext.wis.employee.datautility" 
					selector="EmployeeInfoDataUtility"  
					requestor="java.lang.Object" 
					cardinality="duplicate"/>
	   </Service>
   
 * 4、构建mvc表格时,为列设置datautility属性为注册的值,此处注册值selector="EmployeeInfoDataUtility"
 */
public class EmployeeInfoDataUtility extends DefaultDataUtility {
	private static final Logger logger = Logger.getLogger(EmployeeInfoDataUtility.class);

	@Override
	public Object getDataValue(String component_Id, Object datum, ModelContext mc) throws WTException {
		GUIComponentArray componentArray = new GUIComponentArray();
		try {
			//1、component_Id,列名
			//2、表格列原属性值
			String rawValue = (String) mc.getRawValue();
			//3、表格行数据对象,表格里返回的类型,此处为Map
			Map employeeMap = (Map) datum;
			String employeeName  = (String) employeeMap.get("employeeName");
			Integer employeeAge  = (Integer) employeeMap.get("employeeAge");
			
			//构建一个html对象
			String htmlString = "<input type='button' value='点一下' onclick=\"alert('姓名:"+employeeName+",年龄:"+employeeAge+",component_Id:"+component_Id+"');\">";
			TextDisplayComponent tdc = new TextDisplayComponent("");
			tdc.setValue(htmlString);
			tdc.setCheckXSS(false);
			tdc.setColumnName(component_Id);
			
			componentArray.addGUIComponent(tdc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return componentArray;
	}

}
