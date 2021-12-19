package ext.wis.ebom.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ptc.core.components.beans.TreeHandlerAdapter;

import ext.wisplm.util.PartUtil;
import ext.wisplm.util.WTUtil;
import wt.fc.ObjectSetVector;
import wt.part.WTPart;
import wt.util.WTException;

public class EBOMTreeHandler extends TreeHandlerAdapter {
	private static final String CLASSNAME = EBOMTreeHandler.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);

	private String partOid;
	
	public EBOMTreeHandler(String partOid) {
		this.partOid 	 = partOid;
	}

	@Override
	public Map<Object, List> getNodes(List parents) throws WTException {
		//key为父件,value为子件集合
		Map<Object,List> result = new HashMap<Object,List>();
		try{
			for (Object parent : parents) {
				logger.debug("查找子件,父件:" + WTUtil.getObjectdisplayIdentifier((WTPart)parent));
				Vector childPartsVector = PartUtil.getSubParts((WTPart)parent, PartUtil.DesignConfigSpec).getObjectVectorIfc().getVector(); 
				result.put(parent,childPartsVector);
			}			
		}catch(Exception e){
			e.printStackTrace();
			logger.debug("获取子件出错");
		}
		return result;
	}

	@Override
	public List<Object> getRootNodes() throws WTException {
		return Collections.singletonList((Object) WTUtil.getWTObject(partOid));
	}
}