/**
 * 
 */
package ext.wis.resource;

import wt.util.resource.RBEntry;
import wt.util.resource.RBPseudo;
import wt.util.resource.WTListResourceBundle;


public class EmployeeRB extends WTListResourceBundle {
	
	//1.菜单描述文字
	@RBEntry("员工信息管理")
	public static final String PRIVATE_EMPLOYEE_1 = "employee.basicInfoManagement.description";
	//2.菜单标题文字
	@RBEntry("员工信息管理")
	public static final String PRIVATE_EMPLOYEE_2 = "employee.basicInfoManagement.title";
	//3.菜单图标
	//图标相对根路径为/netmarkets/images
	@RBEntry("actionitem.gif")
	@RBPseudo(false)
	public static final String PRIVATE_EMPLOYEE_3 = "employee.basicInfoManagement.icon";
	//4.鼠标移到菜单上显示的文字
	@RBEntry("员工信息管理")
	public static final String PRIVATE_EMPLOYEE_4 = "employee.basicInfoManagement.tooltip";
	//5.弹窗大小
	@RBEntry("height=600,width=600")
	public static final String PRIVATE_EMPLOYEE_5 = "employee.basicInfoManagement.moreurlinfo";
	
	@RBEntry("新建员工")
	public static final String PRIVATE_EMPLOYEE_6 = "employee.createEmployeeWizard.description";
	
	@RBEntry("新建员工")
	public static final String PRIVATE_EMPLOYEE_7 = "employee.createEmployeeWizard.title";
	
	@RBEntry("actionitem.gif")
	@RBPseudo(false)
	
	public static final String PRIVATE_EMPLOYEE_8 = "employee.createEmployeeWizard.icon";
	@RBEntry("新建员工")
	
	public static final String PRIVATE_EMPLOYEE_9 = "employee.createEmployeeWizard.tooltip";
	@RBEntry("height=300,width=300")
	public static final String PRIVATE_EMPLOYEE_10 = "employee.createEmployeeWizard.moreurlinfo";
	
	@RBEntry("CreateEmployee")
	public static final String PRIVATE_EMPLOYEE_11 = "createEmployeeWizard.title";
	
	@RBEntry("创建成功")
	public static final String PRIVATE_EMPLOYEE_12 = "CreateEmployeeSuccess";
	
	@RBEntry("创建失败")
	public static final String PRIVATE_EMPLOYEE_13 = "CreateEmployeeFailure";
	
	@RBEntry("更新成功")
	public static final String PRIVATE_EMPLOYEE_131 = "UpdateEmployeeSuccess";
	
	@RBEntry("更新失败")
	public static final String PRIVATE_EMPLOYEE_132 = "UpdateEmployeeFailure";
	
	
	@RBEntry("删除员工")
	public static final String PRIVATE_EMPLOYEE_14 = "employee.deleteEmployee.description";
	
	@RBEntry("删除员工")
	public static final String PRIVATE_EMPLOYEE_15 = "employee.deleteEmployee.title";
	
	@RBEntry("delete.gif")
	@RBPseudo(false)
	public static final String PRIVATE_EMPLOYEE_16 = "employee.deleteEmployee.icon";
	
	@RBEntry("删除员工")
	public static final String PRIVATE_EMPLOYEE_17 = "employee.deleteEmployee.tooltip";
	
	@RBEntry("员工报表")
	public static final String PRIVATE_EMPLOYEE_18 = "employee.employeeReport.description";
	
	@RBEntry("员工报表")
	public static final String PRIVATE_EMPLOYEE_19 = "employee.employeeReport.title";
	
	@RBEntry("user.gif")
	@RBPseudo(false)
	public static final String PRIVATE_EMPLOYEE_20 = "employee.employeeReport.icon";
	
	@RBEntry("员工报表")
	public static final String PRIVATE_EMPLOYEE_21 = "employee.employeeReport.tooltip";
	
	@RBEntry("修改员工")
	public static final String PRIVATE_EMPLOYEE_22 = "employee.updateEmployeeWizard.description";
	
	@RBEntry("修改员工")
	public static final String PRIVATE_EMPLOYEE_23= "employee.updateEmployeeWizard.title";
	
	@RBEntry("update.gif")
	@RBPseudo(false)
	public static final String PRIVATE_EMPLOYEE_24= "employee.updateEmployeeWizard.icon";
	@RBEntry("修改员工")
	public static final String PRIVATE_EMPLOYEE_25= "employee.updateEmployeeWizard.tooltip";
	@RBEntry("height=300,width=300")
	public static final String PRIVATE_EMPLOYEE_26 = "employee.updateEmployeeWizard.moreurlinfo";

	@RBEntry("修改员工")
	public static final String PRIVATE_EMPLOYEE_27 = "updateEmployeeWizard.title";
	
}
