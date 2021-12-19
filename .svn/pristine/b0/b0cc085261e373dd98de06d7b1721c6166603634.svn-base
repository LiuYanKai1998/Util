package ext.wisplm.demo.part;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import wt.fc.PersistenceServerHelper;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.Quantity;
import wt.part.WTPart;
import wt.part.WTPartMaster;
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
public class PartService3 implements RemoteAccess {
	private static final String CLASSNAME = PartService3.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);

	public static void main(String args[]) throws WTException, RemoteException, InvocationTargetException {
		
		try {
			RemoteMethodServer.getDefault().setUserName("wcadmin");
			RemoteMethodServer.getDefault().setPassword("wcadmin");
			
			//创建部件
			//createPart();
			
			//创建部件部门关系
			createUsageLink();
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
