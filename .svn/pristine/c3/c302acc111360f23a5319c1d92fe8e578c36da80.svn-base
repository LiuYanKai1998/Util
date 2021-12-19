package ext.wisplm.apiexercise.icreatepartcadlink;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.vecmath.Matrix4d;

import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTList;
import wt.inf.container.WTContainer;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class Link implements RemoteAccess{

	public static void main(String args[]) throws RemoteException, InvocationTargetException{
		String partNumber 		     = args[0];
		String cadNumber 	     	 = args[1];
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class};
		Object obj[] = {partNumber,cadNumber};
		rms.invoke("createUsageLink", Link.class.getName(), null, cla, obj);
		return;
	}
	
	/**
	 * 创建三维模型和部件之间的关系
	 * @param cadDoc
	 * @param part
	 * @throws WTException 
	 */
	public static void create3DEpmPartLink(String partNumber,String cadNumber) throws WTException{
		
		EPMDocument cadDoc= queryEPMDocumentByNumber(cadNumber);
		WTPart part = queryPartByNumber(partNumber);
		
		WTList newLinks = new WTArrayList();
        EPMBuildRule link = EPMBuildRule.newEPMBuildRule(cadDoc, part);
        EPMBuildHistory ebh = EPMBuildHistory.newEPMBuildHistory(cadDoc, part, link.getUniqueID());
        newLinks.add(link);
        
        PersistenceServerHelper.manager.insert(newLinks);
	}
	
	
	/**
	 * 创建二维模型和部件之间的关系
	 * @param partNumber
	 * @param cadNumber
	 * @throws WTException
	 * @throws WTPropertyVetoException
	 */
	public static void create2DEpmPartLink(String partNumber,String cadNumber) throws WTException, WTPropertyVetoException{
		EPMDocument cadDoc= queryEPMDocumentByNumber(cadNumber);
		WTPart part = queryPartByNumber(partNumber);
		
		WTList newLinks = new WTArrayList();
		EPMDescribeLink link = EPMDescribeLink.newEPMDescribeLink(part,cadDoc);
	    link.setBuiltFlag(false);
	    newLinks.add(link);
	    
	    PersistenceServerHelper.manager.insert(newLinks);
	}
	
	//emlink.setTransform(Transform.newTransform(toMatrix4d(transform)));
    private static Matrix4d toMatrix4d() {
        final String defaultM = "0 0 0 0";
        final String[] defaultS = { "0", "0", "0", "0" };
        double[] dd = new double[16];
        for (int i = 0; i < dd.length; i++) {
            dd[i] = 0.0;
        }
        String m0 = "1 0 0 0";
        String m1 = "0 1 0 0";
        String m2 = "0 0 1 0";
        String m3 = "0 0 0 1";
        String[] mm = new String[] { m0, m1, m2, m3 };
        for (int row = 0; row < mm.length; row++) {
            String s = mm[row];
            if (s == null)
                s = defaultM;
            String[] ss = s.trim().split("\\s+");
            if (ss.length != 4) {
            	System.out.println("坐标变换对象向量定义错误：==>" + s);
                ss = defaultS;
            }
            for (int j = 0; j < ss.length; j++) {
                try {
                    double m = Double.parseDouble(ss[j]);
                    dd[row * 4 + j] = m;
                } catch (NumberFormatException nfe) {
                	System.out.println("坐标变换对象中包含非法数值：==>" + s);
                }
            }
        }
        return new Matrix4d(dd);
    }
    
	public static EPMDocument queryEPMDocumentByNumber(String number) throws WTException{
		QuerySpec qs = new QuerySpec(EPMDocument.class);
		number = number.toUpperCase();
		SearchCondition numberSC = new SearchCondition(EPMDocument.class,WTDocument.NUMBER,SearchCondition.EQUAL,number);
		qs.appendWhere(numberSC);
		//构建查找最新版的配置规范
		wt.vc.config.LatestConfigSpec latestconfigspec = new wt.vc.config.LatestConfigSpec();
		//添加到查询条件
        latestconfigspec.appendSearchCriteria(qs);
		PersistenceManager pm = PersistenceHelper.manager;
		QueryResult qr = pm.find(qs);
		System.out.println("找到编号为" + number+"的文档数:"  + qr.size());
		//过滤出最新版
        qr = latestconfigspec.process(qr);
		if(qr.size() > 0){
			return (EPMDocument)qr.nextElement();
		}
		return null;
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
