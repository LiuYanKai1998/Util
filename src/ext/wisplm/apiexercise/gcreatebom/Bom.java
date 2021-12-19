package ext.wisplm.apiexercise.gcreatebom;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;

import ext.wisplm.util.IBAUtil;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.Quantity;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class Bom  implements RemoteAccess{
	public static void main(String [] args) throws RemoteException, InvocationTargetException{
		String parent 		 = args[0];
		String child 	     = args[1];
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class};
		Object obj[] = {parent,child};
		rms.invoke("createUsageLink", Bom.class.getName(), null, cla, obj);
	}
	
	
	public static void createUsageLink(String parentNumber,String childNumber) throws WTException, RemoteException, WTPropertyVetoException, ParseException{
		WTPart parent = queryPartByNumber(parentNumber);
		if(parent == null){
			System.out.println("未找到编号为"+parentNumber+"的部件" );
			return;
		}
		WTPart child = queryPartByNumber(childNumber);
		if(child == null){
			System.out.println("未找到编号为"+child+"的部件" );
			return;
		}
		
		WTPartUsageLink partLink = 
			WTPartUsageLink.newWTPartUsageLink(parent,(WTPartMaster)child.getMaster());
		Quantity q = Quantity.newQuantity();
		q.setAmount(10);
		partLink.setQuantity(q);
		PersistenceServerHelper.manager.insert(partLink); 
		System.out.println("bom结构搭建完毕,父:" + parent.getDisplayIdentity() + ",子:" + child.getDisplayIdentity());
		IBAUtil.setIBAValue(partLink, "route", "15-23-库");
		System.out.println("link软属性设置完毕,路线:" + IBAUtil.getIBAValue(partLink, "route"));
	}
	
	
	public static WTPart queryPartByNumber(String number) throws WTException{
		QuerySpec qs = new QuerySpec(WTPart.class);
		number = number.toUpperCase();
		SearchCondition numberSC = new SearchCondition(WTPart.class,
				WTPart.NUMBER,SearchCondition.EQUAL,number);
		qs.appendWhere(numberSC);
		//构建查找最新版的配置规范
		wt.vc.config.LatestConfigSpec latestconfigspec = new wt.vc.config.LatestConfigSpec();
		//添加到查询条件
        latestconfigspec.appendSearchCriteria(qs);
;
		PersistenceManager pm = PersistenceHelper.manager;
		QueryResult qr = pm.find(qs);
		System.out.println("找到编号为" + number+"的部件数:"  + qr.size());
		//过滤出最新版
        qr = latestconfigspec.process(qr);
		if(qr.size() > 0){
			return (WTPart)qr.nextElement();
		}
		return null;
	}
	
	
}

