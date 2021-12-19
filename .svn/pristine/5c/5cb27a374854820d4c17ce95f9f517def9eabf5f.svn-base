package ext.wis.ebom.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ptc.core.components.descriptor.DescriptorConstants.TableTreeProperties;
import com.ptc.jca.mvc.components.JcaComponentParams;
import com.ptc.jca.mvc.components.JcaTreeConfig;
import com.ptc.mvc.components.AbstractComponentBuilder;
import com.ptc.mvc.components.ColumnConfig;
import com.ptc.mvc.components.ComponentBuilder;
import com.ptc.mvc.components.ComponentConfig;
import com.ptc.mvc.components.ComponentConfigFactory;
import com.ptc.mvc.components.ComponentParams;
import com.ptc.mvc.components.ComponentResultProcessor;
import com.ptc.mvc.components.TreeConfig;
import com.ptc.mvc.components.TreeDataBuilderAsync;
import com.ptc.mvc.components.TreeNode;
import com.ptc.mvc.components.ds.DataSourceMode;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import wt.fc.ReferenceFactory;
import wt.fc._WTObject;
import wt.lifecycle._LifeCycleManaged;
import wt.util.WTException;
import wt.vc._Iterated;

@ComponentBuilder("ext.wis.ebom.mvc.EBOMTreeBuilder")
public class EBOMTreeBuilder extends  AbstractComponentBuilder  implements TreeDataBuilderAsync {
	
	private static final String CLASSNAME = EBOMTreeBuilder.class.getName();
    private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
    private EBOMTreeHandler treeHandler = null;
	@Override
	public Object buildComponentData(ComponentConfig arg0, ComponentParams params) throws Exception {
		try{
			String partOid    = params.getParameter("partOid").toString();
			logger.debug("EBOMTreeBuilder.buildComponentData方法partOid:" + partOid);
			return new EBOMTreeHandler(partOid);
		}catch(Exception e){
			logger.error("EBOMTreeBuilder.buildComponentData()出错");
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public ComponentConfig buildComponentConfig(ComponentParams arg0)	throws WTException {
		ComponentConfigFactory factory = getComponentConfigFactory();
		
		TreeConfig tree = factory.newTreeConfig();
		tree.setLabel("EBOM结构");
		((JcaTreeConfig) tree).setDataSourceMode(DataSourceMode.ASYNCHRONOUS);
		
		ColumnConfig numberCol = factory.newColumnConfig("number", false);
		numberCol.setInfoPageLink(false);
		numberCol.setLabel("编号");
		tree.addComponent(numberCol);
		
		ColumnConfig nameCol  = factory.newColumnConfig("name" ,false);
		nameCol.setLabel("名称");
		tree.addComponent(nameCol);
		
		ColumnConfig versionCol = factory.newColumnConfig("version", false);
		versionCol.setLabel("版本");
		tree.addComponent(versionCol);
		
		ColumnConfig stateCol = factory.newColumnConfig(_LifeCycleManaged.STATE, false);
		stateCol.setLabel("生命周期状态");
		tree.addComponent(stateCol);
		
		ColumnConfig modifyTimeCol = factory.newColumnConfig(_WTObject.MODIFY_TIMESTAMP, false);
		modifyTimeCol.setLabel("修改时间");
		tree.addComponent(modifyTimeCol);
		
		ColumnConfig modifierCol = factory.newColumnConfig(_Iterated.MODIFIER_FULL_NAME, false);
		modifierCol.setLabel("修改者");
		tree.addComponent(modifierCol);

		tree.setNodeColumn("number");
		//设置展开层级,逐级展开或全部展开
		tree.setExpansionLevel(TableTreeProperties.ONE_EXPAND);
		
		tree.setId("ext.wis.ebom.mvc.EBOMTreeBuilder");
		return tree;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override 
	public void buildNodeData(Object node,ComponentResultProcessor resultProcessor) throws Exception {
		try{
			NmCommandBean commandBean = (NmCommandBean) ((JcaComponentParams) resultProcessor.getParams()).getHelperBean().getNmCommandBean();
			if (node == TreeNode.RootNode) {
				String partOid = commandBean.getTextParameter("partOid");
				logger.debug("当前节点类型为根节点,获取部件oid参数,partOid:" + partOid); 
				treeHandler = new EBOMTreeHandler(partOid);
				Object rootObject = new ReferenceFactory().getReference(partOid).getObject();
				resultProcessor.addElement(rootObject);
				
			} else {
				logger.debug("当前节点类型不是根节点,传进来的为部件对象,获取子件"); 
				List nodeList = new ArrayList();
				nodeList.add(node);
				Map<Object, List> map = treeHandler.getNodes(nodeList);
				Set keySet = map.keySet();
				for (Object key : keySet) {
					List childList = map.get(key);
					if (childList != null && childList.size() > 0) {
						resultProcessor.addElements(map.get(key));
					}
				}
			}
		}catch(Exception e){
			logger.error("EBOMTreeBuilder.buildNodeData()出错");
			e.printStackTrace();
			throw e;
		}
	}


}
