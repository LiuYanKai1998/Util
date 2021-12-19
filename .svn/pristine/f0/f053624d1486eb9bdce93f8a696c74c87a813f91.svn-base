package ext.wis.employee.model;

import com.ptc.windchill.annotations.metadata.ColumnProperties;
import com.ptc.windchill.annotations.metadata.GenAsPersistable;
import com.ptc.windchill.annotations.metadata.GeneratedProperty;
import com.ptc.windchill.annotations.metadata.PropertyConstraints;
import com.ptc.windchill.annotations.metadata.TableProperties;

import wt.fc.WTObject;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月18日
 */
/**
 * @GenAsPersistable表示实现wt.fc.Persistable
 * 属性:
 * 1、superClass表示extends的父类
 * 2、properties属性描述持久化对象的字段信息,包括bean的属性名称,数据类型,数据库字段等
 * 		2.1@GeneratedProperty 用于描述一个属性的信息
 * 			2.1.1 name为bean的属性名称,数据库字段名
 * 			2.1.2 type为数据类型
 * 			2.1.3 constraints为约束条件
 * 最终结果:
 * 1、数据表名:WISPet,类名:Pet
 * 2、bean属性name对应数据库字段为petName
 * 			
 */
@GenAsPersistable(
		tableProperties=@TableProperties(tableName="employee"),
		superClass     =WTObject.class,
		properties     = {
				@GeneratedProperty(name    ="name",   
								   type    = String.class,
								   javaDoc ="姓名",
								   constraints=@PropertyConstraints(
										   			required=true,
										   			upperLimit=20),
								   					columnProperties=@ColumnProperties(
								   														columnName="name",
								   														index     =true
								   													   )
								  ),
				@GeneratedProperty(name="age",     type = Integer.class,javaDoc="年龄",constraints=@PropertyConstraints(required=true,upperLimit=100),columnProperties=@ColumnProperties(columnName="age",    index=true)),
				@GeneratedProperty(name="dept",    type = String.class,javaDoc="部门", constraints=@PropertyConstraints(required=true,upperLimit=10),columnProperties=@ColumnProperties( columnName="dept",   index=true))	
		}
)
public class Employee extends _Employee{
/**
 * 姓名name-string
 * 年龄age-int
 * 生日birthday-string
 */
    static final long serialVersionUID = 1;

    public static Employee newEmployee()
            throws WTException {
    	Employee instance = new Employee();
        instance.initialize();
        return instance;
    }
}
