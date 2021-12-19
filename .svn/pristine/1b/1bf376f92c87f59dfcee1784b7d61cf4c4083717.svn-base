package ext.wis.employee.mvc.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.jca.mvc.components.JcaColumnConfig;
import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;
import com.ptc.mvc.components.TableConfig;

import ext.wis.employee.EmployeeHelper;
import ext.wis.employee.model.Employee;
import wt.fc.ReferenceFactory;
import wt.util.WTException;

/**
 * 1、定义builder name,值自定义,唯一即可,一般和类名完全一致
 * 2、继承AbstractComponentBuilder,实现buildComponentData()和buildComponentConfig
 * 3、注册到codebase/config/mvc/custom.xml文件,使得spring可以扫描该类
 */
/**
 * 页面显示表格使用以下标签:
 * <jsp:include page="${mvc:getComponentURL('ext.wis.employee.mvc.builder.SearchEmployeeBuilder')}" flush="true"></jsp:include>
 * 注意:mvc:getComponentURL里的参数值与表格类ComponentBuilder里的参数值相同
 *
 *ComponentBuilder注解参数值自定义,一般情况下写类的全路径即可
 *ZhongBinpeng May 28, 2020
 */
@ComponentBuilder("ext.wis.employee.mvc.builder.SearchEmployeeBuilder")
public class SearchEmployeeBuilder  extends AbstractComponentBuilder {
	
	private static final String CLASSNAME = SearchEmployeeBuilder.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	/**
	 * 表格ID,用于界面通过JS操作表格
	 */
	private final String TABLEID = "ext.wis.employee.mvc.builder.SearchEmployeeBuilder";
	
	/**
	 * 接收界面参数
	 * 构建表格数据
	 */
	@Override
	public Object buildComponentData(ComponentConfig componentConfig, ComponentParams componentParams) throws Exception {
		//通过componentParams.getParameter(参数名)方法,获取界面参数值
		String employeeName = (String) componentParams.getParameter("employeeName");
		String employeeDept = (String) componentParams.getParameter("employeeDept");
		String parmFromUrl  = (String) componentParams.getParameter("parmFromUrl");
		logger.debug("员工姓名参数:" + employeeName);
		logger.debug("员工部门参数:" + employeeDept);
		logger.debug("地址栏参数:"   + parmFromUrl);
		
		List result = new ArrayList();
		List<Employee> employeeList = EmployeeHelper.findEmployee(employeeName, employeeDept, 0);
		try{
			for(Employee employee : employeeList){
				Map map = new HashMap();
				map.put("employeeName", employee.getName());
				map.put("employeeAge", employee.getAge());
				map.put("employeeDept", employee.getDept());
				
				map.put("dataUtility", employee.getName() + "-" + employee.getAge());
				//设置表格行数据的主对象为employee
				map.put("oid",new ReferenceFactory().getReferenceString(employee));
				
				//显示自定义的html对象
				TextDisplayComponent moreInfoHref = new TextDisplayComponent(null);
				moreInfoHref.setValue("<a href=\"javascript:void(0);\" onclick=\"alert('显示更多信息');\">查看</a>");
				//是否检查html
				moreInfoHref.setCheckXSS(false);
				map.put("moreInfoHref", moreInfoHref);
				
				result.add(map);
			}
		}catch(Exception e){
			logger.error("查询员工信息出错:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	

	@Override
	public ComponentConfig buildComponentConfig(ComponentParams var1) throws WTException {
	   	//1,构建表格对象
    	ComponentConfigFactory factory = getComponentConfigFactory();
        TableConfig table = factory.newTableConfig();
        //2,设置表格ID
        table.setId(TABLEID);
        
        //3,设置表格标题
        table.setLabel("员工列表"); 
        //4,配置单选按钮
        //table.setSingleSelect(true);
        //5,配置多选(如果为false,则不可见)
        table.setSelectable(true);
        //6,是否显示总数
        table.setShowCount(true);
        
        //工具栏增加菜单(一个actionModel)
        table.setActionModel("SearchEmployeeTableModel");
        
        /**
         * 表格行对象右键操作菜单-删除对象
         */
        ColumnConfig action = factory.newColumnConfig("nmActions", false);
		((JcaColumnConfig) action).setActionModel("SearchEmployeeTableColumnModel");
		table.addComponent(action);
		
        ColumnConfig columnName = factory.newColumnConfig("employeeName","员工姓名", true);
        //是否显示对象详情超链接,只适用于Windchill 持久类
        //columnName.setInfoPageLink(true);
        table.addComponent(columnName);
        
        ColumnConfig columnAge = factory.newColumnConfig("employeeAge","年龄", true);
        table.addComponent(columnAge);
               
        ColumnConfig columnDept = factory.newColumnConfig("employeeDept","部门", false);
        table.addComponent(columnDept);
        
        ColumnConfig columnDelete = factory.newColumnConfig("moreInfoHref","更多信息", false);
        table.addComponent(columnDelete);
        
        ColumnConfig employeeInfoDataUtility = factory.newColumnConfig("dataUtility","DataUtility示例", false);
        employeeInfoDataUtility.setDataUtilityId("EmployeeInfoDataUtility");
        table.addComponent(employeeInfoDataUtility);
        
        return table;
    }
}