package ext.wisplm.demo;

import org.apache.log4j.Logger;

import wt.doc.WTDocument;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.log4j.LogR;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;

/**
 * @ClassName: OidToObjectTest
 * @author: Loong
 * @date: 2020年4月2日 下午3:35:48
 * @version: V1.0
 *
 * <p>OID与对象的之间的转换</P>
 *
 */
public class OidObjectTransfer implements RemoteAccess{
	private static final String CLASS_NAME = OidObjectTransfer.class.getName();
	private static final Logger LOG = LogR.getLogger(CLASS_NAME);
	
	
	public static void main(String[] args) {
		try {
			RemoteMethodServer.getDefault().setUserName("wcadmin");
			RemoteMethodServer.getDefault().setPassword("wcadmin");
			getObejctByOid("OR:wt.doc.WTDocument:130044");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void getObejctByOid(String oid) throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
           RemoteMethodServer.getDefault().invoke(
                    "getObejctByOid", CLASS_NAME, null,
                    new Class[] {String.class},
                    new Object[] {oid});
           return;
        }
        ReferenceFactory rf = new ReferenceFactory();
        Persistable p = rf.getReference(oid).getObject();
		if(p instanceof WTPart){
			WTPart part = (WTPart)p;
			System.out.println("部件对象========>"+part);
		}else if(p instanceof WTDocument){
			WTDocument doc = (WTDocument)p;
			String num = doc.getNumber();
			String name = doc.getName();
			System.out.println("文档对象===num======>"+num);
			System.out.println("文档对象==name======>"+name);
		}
	}
	
	
	
	public static void getOidByObject(Object obj) throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
           RemoteMethodServer.getDefault().invoke(
                    "getOidByObject", CLASS_NAME, null,
                    new Class[] {Object.class},
                    new Object[] {obj});
           return;
        }
        ReferenceFactory rf = new ReferenceFactory();
        String oid = rf.getReferenceString((Persistable)obj);
        System.out.println("oid========>"+oid);
	}
	
	
	
	//忽略权限
	public static void access() throws Exception{
		//关闭系统权限验证,同时返回关闭之前的上下文权限启用状态
		boolean accessEnforced =  SessionServerHelper.manager.setAccessEnforced(false);
		try{
			//此处编写业务代码
			//查询代码
			
			
			
		}finally{
			//无论业务代码执行成功或失败，均恢复原上下文权限启用状态
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	
	public static void setPrincipal() throws Exception{
		//获取当前登录用户
		WTUser currentUser = (WTUser) SessionHelper.manager.getPrincipal();
		try{
			//切换为指定用户执行代码
			String userName = "";//SessionHelper.manager.getAdministrator().getName();
			SessionHelper.manager.setPrincipal(userName);
			
			
			
			
			
		}finally{
			//代码执行完毕，finally块里将用户切换回来
		    SessionHelper.manager.setPrincipal(currentUser.getName());
		}

	}
	
	

}
