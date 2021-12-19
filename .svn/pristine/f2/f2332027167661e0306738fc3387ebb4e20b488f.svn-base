package ext.wisplm.demo.part.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;
import com.ptc.mvc.components.TableConfig;

import ext.wisplm.demo.part.PartService;
import wt.fc._WTObject;
import wt.part.WTPart;
import wt.util.WTException;
import wt.vc._Iterated;

/**
 * 1、定义builder name,值自定义,唯一即可,一般和类名完全一致
 * 2、继承AbstractComponentBuilder,实现buildComponentData()和buildComponentConfig
 * 3、注册到codebase/config/mvc/custom.xml文件,使得spring可以扫描该类
 */

@ComponentBuilder("ext.wisplm.demo.part.mvc.SearchPartBuilder")
public class SearchPartBuilder extends AbstractComponentBuilder{

	private static final Logger logger  = Logger.getLogger(SearchPartBuilder.class);
	
	private static final String TABLEID = "ext.wisplm.demo.part.mvc.SearchPartBuilder";
	@Override
	public Object buildComponentData(ComponentConfig arg0, ComponentParams arg1) throws Exception {
		/**
		 * 获取参数
		 */
		String oid    = (String) arg1.getParameter("oid");
		
		String number = (String) arg1.getParameter("partNumber");
		String name   = (String) arg1.getParameter("partName");
		String requestor = (String) arg1.getParameter("requestor");
		
		logger.debug("搜索部件-页面输入参数oid:" + oid);
		logger.debug("搜索部件-页面输入参数partNumber:" + number);
		logger.debug("搜索部件-页面输入参数partNumber:" + name);
		logger.debug("搜索部件-地址栏请求参数requestor:" + requestor);
		
		if(StringUtils.isEmpty(number) && StringUtils.isEmpty(name)){
			logger.debug("参数为空,返回空");
			return new ArrayList<WTPart>();
		}
		List<WTPart> result = PartService.queryPart(number, name);
		return result;
	}

	@Override
	public ComponentConfig buildComponentConfig(ComponentParams arg0) throws WTException {
		
	   	//1,构建表格对象
    	ComponentConfigFactory factory = getComponentConfigFactory();
        TableConfig table = factory.newTableConfig();
        //2,设置表格ID
        table.setId(TABLEID);
        
        //3,设置表格标题
        table.setLabel("部件查询结果"); 
        //4,配置单选按钮
        //table.setSingleSelect(true);
        //5,配置多选(如果为false,则不可见)
        table.setSelectable(true);
        //6,是否显示总数
        table.setShowCount(true);
        //工具栏增加导出菜单(一个actionModel)
        table.setActionModel("exportTable");
        //7,定义表格列,属性名称,列名称,是否可以排序,true可以排序,false不可以排序
        ColumnConfig columnNumber = factory.newColumnConfig("number","编号", true);
        //是否显示对象详情超链接,只适用于Windchill 持久类
        //columnNumber.setInfoPageLink(true);
        table.addComponent(columnNumber);
        
        ColumnConfig columnName = factory.newColumnConfig("name","名称", true);
        table.addComponent(columnName);
               
        ColumnConfig columnPartVersion = factory.newColumnConfig("version","大版", false);
        table.addComponent(columnPartVersion);
        
        ColumnConfig columnIteration = factory.newColumnConfig("state","生命周期状态", false);
        table.addComponent(columnIteration);
        
        ColumnConfig columnModifyTime = factory.newColumnConfig(_WTObject.MODIFY_TIMESTAMP,"修改时间",false);
        table.addComponent(columnModifyTime);
        
    	ColumnConfig columnModifier = factory.newColumnConfig(_Iterated.MODIFIER_FULL_NAME,"修改者",false);
        table.addComponent(columnModifier);
        
    	ColumnConfig columnContainerName = factory.newColumnConfig("containerName","产品名称",false);
        table.addComponent(columnContainerName);
        //IBA属性值显示方式:固定前缀"IBA|"+属性的逻辑标识符
    	ColumnConfig ibaChangjia = factory.newColumnConfig("IBA|CHANGJIA","厂家",false);
        table.addComponent(ibaChangjia);
        
       	ColumnConfig ibaMiji = factory.newColumnConfig("IBA|MIJI","密级",false);
        table.addComponent(ibaMiji);
        return table;
    }

}
