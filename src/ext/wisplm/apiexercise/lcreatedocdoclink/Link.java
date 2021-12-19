package ext.wisplm.apiexercise.lcreatedocdoclink;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4d;

import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.TypeIdentifier;

import wt.doc.WTDocument;
import wt.doc.WTDocumentDependencyLink;
import wt.doc.WTDocumentMaster;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMaster;
import wt.epm.build.EPMBuildHistory;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.epm.structure.EPMMemberLink;
import wt.epm.structure.Transform;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTList;
import wt.inf.container.WTContainer;
import wt.locks.LockException;
import wt.locks.LockHelper;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.PartDocHelper;
import wt.part.Quantity;
import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.part.WTPartDescribeLink;
import wt.part.WTPartReferenceLink;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.Iterated;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

public class Link implements RemoteAccess{

	public static void main(String args[]) throws RemoteException, InvocationTargetException{
		String docNumber	 		     = args[0];
		String refDocNumber 	     	 = args[1];
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class};
		Object obj[] = {docNumber,refDocNumber};
		rms.invoke("addDescriptionDocument", Link.class.getName(), null, cla, obj);
		return;
	}
	
	/**
	 * 为文档添加说明文档
	 * @param doc
	 * @param descriptionDoc
	 * @throws WTException
	 */
	public static void addDescriptionDocument(String docNumber,String  refDocNumber) throws WTException {
		//
		WTDocument doc = queryDocumentByNumber(docNumber);
		
		WTDocument refDoc = queryDocumentByNumber(refDocNumber);
		
		WTDocumentDependencyLink link = WTDocumentDependencyLink.newWTDocumentDependencyLink(doc,refDoc);
		PersistenceServerHelper.manager.insert(link);
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
	
	/**
	 * 创建EPM文档之间的结构关系
	 * @param parentEPMNumber
	 * @param childEPMNumber
	 * @throws WTException 
	 * @throws WTPropertyVetoException 
	 */
	public static void createEpmToEpmLink(String parentEPMNumber,String childEPMNumber) throws WTException, WTPropertyVetoException{
		//一、获取EPMDocument
		EPMDocument parent = queryEPMDocumentByNumber(parentEPMNumber);
		EPMDocument child  = queryEPMDocumentByNumber(childEPMNumber);
		
/*		//首先检出父EPMDocument
        if (!(WorkInProgressHelper.isCheckedOut(parent))) {
        	parent = (EPMDocument) getCheckOutObject(parent);
        }*/
        //System.out.println("父EPMDocument检出成功:" + parent.getDisplayIdentity());
        EPMMemberLink emlink = EPMMemberLink.newEPMMemberLink(parent, (EPMDocumentMaster)child.getMaster());
        
        String typeString = "wt.epm.structure.EPMMemberLink|com.pdmtest.DefaultEPMMemberLink";
        
        TypeIdentifier memtype = TypeHelper.getTypeIdentifier(typeString);
        //设置类型
        TypeHelper.setType(emlink, memtype);
        emlink.setTransform(Transform.newTransform(toMatrix4d()));
        //设置数量及计量单位
        emlink.setQuantity(Quantity.newQuantity(1, QuantityUnit.EA));
        emlink.setPlaced(true);
        emlink.setRequired(true);
        
        PersistenceServerHelper.manager.insert(emlink);
        //PersistenceHelper.manager.save(empLinks);
        System.out.println("EPMMemberLink创建成功.....");
/*        if (WorkInProgressHelper.isCheckedOut(parent)){
        	parent = (EPMDocument) WorkInProgressHelper.service.checkin(parent, "");
        }
        System.out.println("父EPMDocument检入成功:" + parent.getDisplayIdentity());*/
	}
	    
	/**
	 * 为部件创建说明或者参考文档
	 */
	public static boolean createPartDocRelation(String partNumber,String docNumber)throws WTException {
		WTPart 	   part = queryPartByNumber(partNumber);
		WTDocument doc  = queryDocumentByNumber(docNumber);
		WTList newLinks = new WTArrayList();
		//判断文档是否为参考文档
		boolean isReference = PartDocHelper.isReferenceDocument(doc);
		//如果是参考文档,建立参考关系
		if (isReference) {
			WTPartReferenceLink link = WTPartReferenceLink
					.newWTPartReferenceLink(part, (WTDocumentMaster) doc
							.getMaster());
			newLinks.add(link);
		} else {
			//如果是说明文档,建立说明关系
			// 先移除相同编号的旧说明文档
			Map map = getDescribeReferenceDoc(part);
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				WTDocument oldDocument = (WTDocument) it.next();
				System.out.println("找到部件已存在的说明文档:" + oldDocument.getNumber());
				if (oldDocument.getNumber().equals(doc.getNumber())) {
					System.out.println("与新说明文档编号相同,移除.");
					WTPartDescribeLink link = (WTPartDescribeLink) map.get(oldDocument);
					PersistenceServerHelper.manager.remove(link);
				} 
			}
			WTPartDescribeLink link = WTPartDescribeLink.newWTPartDescribeLink(
					part, doc);
			newLinks.add(link);
		}
		PersistenceServerHelper.manager.insert(newLinks);
		System.out.println("部件和文档的关系建立成功,部件:" + part.getDisplayIdentity() + ",文档:" + doc.getDisplayIdentity());
		return true;
	}
	
	/**
	 * 给部件设置参考文档
	 * 
	 * @param curr_prt
	 * @param wtdoc
	 */
	public static void setPartReference(WTPart curr_prt, WTDocument wtdoc) {
		try {
			WTDocumentMaster docMaster = (WTDocumentMaster) wtdoc.getMaster();
			WTPartReferenceLink linkObj = WTPartReferenceLink
					.newWTPartReferenceLink(curr_prt, docMaster);
			PersistenceServerHelper.manager.insert(linkObj);
		} catch (WTException wte) {
			wte.printStackTrace();
		}
	}
	
	/**
	 * 获取部件的参考文档
	 */
	public static List getReferenceDoc(WTPart part) throws WTException {
		List list = new ArrayList();
		QueryResult qr2 = PersistenceHelper.manager.navigate(part,
				WTPartReferenceLink.REFERENCES_ROLE, WTPartReferenceLink.class,
				false);
		while (qr2.hasMoreElements()) {
			WTObject wtobject = (WTObject) qr2.nextElement();
			if (wtobject instanceof WTPartReferenceLink) {
				WTPartReferenceLink reflink = (WTPartReferenceLink) wtobject;
				WTDocumentMaster master = (WTDocumentMaster) reflink
						.getReferences();
				list.add(master);
			}
		}
		return list;
	}

	/**
	 * 获取部件的说明文档
	 * 
	 * @throws WTException
	 */
	public static Map getDescribeReferenceDoc(WTPart part) throws WTException {
		Map map = new HashMap();
		QueryResult qr = PersistenceHelper.manager.navigate(part,
				WTPartDescribeLink.DESCRIBED_BY_ROLE, WTPartDescribeLink.class,
				false);
		while (qr.hasMoreElements()) {
			WTPartDescribeLink link = (WTPartDescribeLink) qr.nextElement();
			Iterated it = link.getDescribedBy();
			if (it instanceof WTDocument) {
				WTDocument doc = (WTDocument) it;
				map.put(doc, link);
			}
		}
		return map;
	}
	
	
	/**
	 * 后台操作对象检出检入,升小版
	 * Workable类是所有可检出检入的类型的父类
	 * @throws WTException 
	 * @throws LockException 
	 * @throws WTPropertyVetoException 
	 */
	public static Workable newRevision(Workable workable) throws LockException, WTException, WTPropertyVetoException{
		
		//检出对象,获取对象父本
		workable = getCheckOutObject(workable);
		
		//检入对象
		//首先判断对象是否已检出
        if (WorkInProgressHelper.isCheckedOut(workable)){
        	workable = WorkInProgressHelper.service.checkin(workable, "后台升小版");
        }
        
        //返回升小版后的对象
        System.out.println("对象升小版成功:" + ((WTObject)workable).getDisplayIdentity());
        return workable;
	}
	
	/**
	 * 检出对象,返回对象的副本
	 * @param workable
	 * @return
	 * @throws LockException
	 * @throws WTException
	 */
    private static Workable getCheckOutObject(Workable workable) throws LockException, WTException {
        Workable retVal = null;
        try {
            if (isCheckoutAllowed(workable)) {
                WorkInProgressHelper.service.checkout(workable, WorkInProgressHelper.service.getCheckoutFolder(),
                        "Updating attributes during load.");
                retVal = WorkInProgressHelper.service.workingCopyOf(workable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WTException(e.getMessage());
        }
        if (retVal == null)
            throw new WTException("Checkout Failed!");
        return retVal;
    }
    
    //判断对象是否允许被检出
    private static boolean isCheckoutAllowed(Workable workable) throws LockException, WTException {

        return !(WorkInProgressHelper.isWorkingCopy(workable) || WorkInProgressHelper.isCheckedOut(workable) || LockHelper
                .isLocked(workable));
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
	
	public static WTDocument queryDocumentByNumber(String number) throws WTException{
		QuerySpec qs = new QuerySpec(WTDocument.class);
		number = number.toUpperCase();
		SearchCondition numberSC = new SearchCondition(WTDocument.class,WTDocument.NUMBER,SearchCondition.EQUAL,number);
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
			return (WTDocument)qr.nextElement();
		}
		return null;
	}
	
}
