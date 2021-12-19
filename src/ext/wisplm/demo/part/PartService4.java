package ext.wisplm.demo.part;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import wt.doc.WTDocument;
import wt.fc.PersistenceServerHelper;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.Quantity;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartMaster;
import wt.part.WTPartReferenceLink;
import wt.part.WTPartUsageLink;
import wt.util.WTException;
import ext.wisplm.util.IBAUtil;
import ext.wisplm.util.PartUtil;

/**
 * @ClassName: PartService2
 * @author: Loong
 * @date: 2020年4月10日 下午1:41:19
 * @version: V1.0
 *
 * <p>TODO</P>
 *
 */
public class PartService4 implements RemoteAccess {
	private static final String CLASSNAME = PartService4.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);

	public static void main(String args[]) throws WTException, RemoteException, InvocationTargetException {
		
		try {
			RemoteMethodServer.getDefault().setUserName("wcadmin");
			RemoteMethodServer.getDefault().setPassword("wcadmin");
			
			//创建部件
			//createPart();
			
			//创建部件部门关系
			//createUsageLink();
			
			//创建部件与文档参考关系
			//createReferenceLink();
			
			//创建部件与文档说明关系
			//createDescribeLink();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void createPart() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createPart", CLASSNAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		WTPart part = PartUtil.createPart("BJ-001", "部件1", "C919", "Design", "/Default/自制件/");
		System.out.println("创建的部件对象=====》"+part);
	}
	
	
	
	public static void createUsageLink() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createUsageLink", CLASSNAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		createUsageLink("OR:wt.part.WTPart:134620","OR:wt.part.WTPart:134675");
	}
	
	
	//设置部件参考文档
	public static void createReferenceLink() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createReferenceLink", CLASSNAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		ReferenceFactory rf = new ReferenceFactory();
		WTPart part = (WTPart)rf.getReference("OR:wt.part.WTPart:134714").getObject();
		WTDocument doc = (WTDocument)rf.getReference("OR:wt.doc.WTDocument:134771").getObject();
		PartUtil.setPartReference(part, doc);
	}
	
	
	
	//设置部件说明文档
	public static void createDescribeLink() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createDescribeLink", CLASSNAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		ReferenceFactory rf = new ReferenceFactory();
		WTPart part = (WTPart)rf.getReference("OR:wt.part.WTPart:134714").getObject();
		WTDocument doc = (WTDocument)rf.getReference("OR:wt.doc.WTDocument:134759").getObject();
		PartUtil.setPartDescribe(part, doc);
	}
	
	
	
	public static void createUsageLink(String parentOid,String childOid) throws Exception{
		ReferenceFactory rf = new ReferenceFactory();
		WTPart parent = (WTPart)rf.getReference(parentOid).getObject();
		WTPart child =  (WTPart)rf.getReference(childOid).getObject();
		WTPartUsageLink partLink = WTPartUsageLink.newWTPartUsageLink(parent,(WTPartMaster)child.getMaster());
		Quantity q = Quantity.newQuantity();
		q.setAmount(10);
		partLink.setQuantity(q);
		PersistenceServerHelper.manager.insert(partLink); 
		
		/*
		System.out.println("bom结构搭建完毕,父:" + parent.getDisplayIdentity() + ",子:" + child.getDisplayIdentity());
		IBAUtil.setIBAValue(partLink, "route", "15-23-库");
		System.out.println("link软属性设置完毕,路线:" + IBAUtil.getIBAValue(partLink, "route"));
		*/
	}
	
	
	
	
}
