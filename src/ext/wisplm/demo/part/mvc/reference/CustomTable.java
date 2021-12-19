package ext.wisplm.demo.part.mvc.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;
import com.ptc.mvc.components.TableConfig;

import ext.wisplm.util.SearchUtil;
import wt.fc.QueryResult;
import wt.fc._WTObject;
import wt.part.WTPart;
import wt.util.WTException;
import wt.vc._Iterated;

/**
 * 1、定义builder name,值自定义,唯一即可,一般和类名完全一致
 * 2、继承AbstractComponentBuilder,实现buildComponentData()和buildComponentConfig
 * 3、注册到codebase/config/mvc/custom.xml文件,使得spring可以扫描该类
 */
/**
 * 页面显示表格使用以下标签:
 * <jsp:include page="${mvc:getComponentURL('com.arclight.mvc.CustomTable')}" flush="true"></jsp:include>
 * 注意:mvc:getComponentURL里的参数值与表格类ComponentBuilder里的参数值相同
 *
 *ComponentBuilder注解参数值自定义,一般情况下写类的全路径即可
 *ZhongBinpeng May 28, 2020
 */
@ComponentBuilder("com.arclight.mvc.CustomTable")
public class CustomTable  extends AbstractComponentBuilder {
	
	/**
	 * 表格ID,用于界面通过JS操作表格
	 */
	private final String TABLEID = "com.arclight.mvc.CustomTable";
	
	/**
	 * 接收界面参数
	 * 构建表格数据
	 */
	@Override
	public Object buildComponentData(ComponentConfig componentConfig, ComponentParams componentParams) throws Exception {
		System.out.println("部件编号参数:" + componentParams.getParameter("partNumber"));
		System.out.println("部件名称参数:" + componentParams.getParameter("partName"));
		System.out.println("地址栏参数:" + componentParams.getParameter("test"));
		List result = new ArrayList();
		//通过componentParams.getParameter(参数名)方法,获取界面参数值
		//String taskOid = (String)var2.getParameter("oid");
		SearchUtil su  = new SearchUtil(WTPart.class);
		QueryResult qr = su.queryObjects();
		/***********************注意:以下四种返回情况,取其一即可**************************/
		//1,直接返回QueryResult对象
		//return qr;
		//2,返回集合类,里面存储的是Windchill对象
		//result.addAll(qr.getObjectVector().getVector());
		//3,返回Map集合
		while(qr.hasMoreElements()){
			WTPart part = (WTPart) qr.nextElement();
			Map map = new HashMap();
			map.put("number", part.getNumber());
			map.put("name", part.getName());
			map.put("state", part.getLifeCycleState().getDisplay(Locale.SIMPLIFIED_CHINESE));
			map.put("partVersion", part.getVersionIdentifier().getValue());
			map.put("iteration", part.getIterationIdentifier().getValue());
			map.put("containerName", part.getContainerName());
			map.put("creatorName", part.getCreatorName());
			map.put("creatorFullName", part.getCreatorFullName());
			map.put("modifierName", part.getModifierName());
			map.put("modifierFullName", part.getModifierFullName());
			result.add(map);
			
			//4,返回自定义Bean
			PartBean partBean = new PartBean();
			partBean.setPart(part);
			partBean.setNumber(part.getNumber());
			partBean.setName(part.getName());
			result.add(partBean);
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
        table.setLabel("部件清单"); 
        //4,配置单选按钮
        //table.setSingleSelect(true);
        //5,配置多选(如果为false,则不可见)
        table.setSelectable(true);
        //6,是否显示总数
        table.setShowCount(true);
        
        //7,定义表格列,属性名称,列名称,是否可以排序
        //newColumnConfig(String id,String lable,boolean sortable)
        //8、如何设置列id？
        //8.1、如果buildComponentData返回的是List<Map>,id为map的key即可
        //8.2、如果buildComponentData返回的是List<自定义java bean>,id为bean的属性名称
        //8.3、如果buildComponentData返回的是Windchill持久层对象如WTPart
        //查询各个属性的id可通过系统一级菜单customization-tools-Available Attributes
        //输入对象类型如wt.part.WTPart-submit,在结果表格中查找jcaID一列的值
        ColumnConfig columnNumber = factory.newColumnConfig("number","编号", true);
        //是否显示对象详情超链接,只适用于Windchill 持久类
        columnNumber.setInfoPageLink(true);
        table.addComponent(columnNumber);
        
        ColumnConfig columnName = factory.newColumnConfig("name","名称", true);
        table.addComponent(columnName);
               
        ColumnConfig columnPartVersion = factory.newColumnConfig("partVersion","大版本", false);
        table.addComponent(columnPartVersion);
        
        ColumnConfig columnIteration = factory.newColumnConfig("iteration","小版本", false);
        table.addComponent(columnIteration);
        
        ColumnConfig columnState = factory.newColumnConfig("state","状态", false);
        table.addComponent(columnState);
        
        ColumnConfig columnContainerName = factory.newColumnConfig("containerName","容器名称", false);
        table.addComponent(columnContainerName);
        
        return table;
    }
	
	
/*
    //定义表格列,属性名称,列名称,是否可以排序,IBA|软属性key
    
    ColumnConfig columnNumber = factory.newColumnConfig("number","编号", true);
    columnNumber.setInfoPageLink(false);
    table.addComponent(columnNumber);
    
    ColumnConfig columnName = factory.newColumnConfig("name","名称", true);
    columnName.setInfoPageLink(true);
    table.addComponent(columnName);
    
    ColumnConfig columnType = factory.newColumnConfig("docTypeName","类型", true);
    table.addComponent(columnType);
    
    ColumnConfig columnVersion = factory.newColumnConfig("version","版本", false);
    table.addComponent(columnVersion);
    
    ColumnConfig columnState = factory.newColumnConfig("state","生命周期状态", false);
    table.addComponent(columnState);
    
    ColumnConfig columnModifyTime = factory.newColumnConfig(_WTObject.MODIFY_TIMESTAMP,"修改时间",false);
    table.addComponent(columnModifyTime);
    
	ColumnConfig columnModifier = factory.newColumnConfig(_Iterated.MODIFIER_FULL_NAME,"修改者",false);
    table.addComponent(columnModifier);
    
	ColumnConfig columnContainerName = factory.newColumnConfig("containerName","产品名称",false);
    table.addComponent(columnContainerName);*/
}