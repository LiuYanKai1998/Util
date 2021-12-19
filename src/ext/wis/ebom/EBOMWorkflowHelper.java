package ext.wis.ebom;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import ext.wis.constants.BusinessConstants;
import ext.wisplm.util.IBAUtil;
import ext.wisplm.util.PromotionNoticeUtil;
import ext.wisplm.util.WTUtil;
import ext.wisplm.util.WfUtil;
import ext.wisplm.util.WorkableUtil;
import wt.epm.EPMDocument;
import wt.fc.ObjectReference;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.lifecycle.LifeCycleException;
import wt.maturity.MaturityException;
import wt.maturity.Promotable;
import wt.maturity.PromotionNotice;
import wt.method.RemoteAccess;
import wt.part.WTPart;
import wt.pom.Transaction;
import wt.util.WTException;
import wt.util.WTInvalidParameterException;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.Versioned;
import wt.vc.wip.Workable;

/**
 * EBOM签审流程相关业务代码
 */
public class EBOMWorkflowHelper implements RemoteAccess{

	private static final String CLASSNAME = EBOMWorkflowHelper.class.getName();
	
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	/**
	 * 编制活动-转变条件-提交审阅校验
	 * @description
	 * @param self
	 * @param PrimaryBusinessObject
	 * @throws WTException 
	 */
	public static void validateOnSubmit(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		String errorMsg = "";
		/**
		 * 1、参与者校验:校对者和批准者不能为空
		 */
		if(!WfUtil.hasUserInRole(self, primaryBusinessObject, "WISJIAODUIZHE")){
			errorMsg +="请为校对者选择用户;\n";
		}
		if(!WfUtil.hasUserInRole(self, primaryBusinessObject, "WISPIZHUNZHE")){
			errorMsg +="请为批准者选择用户;\n";
		}
		/**
		 * 2、签审对象没有被检出并且为当前大版本的最版
		 */
		PromotionNotice pn                = (PromotionNotice) primaryBusinessObject;
		ArrayList<Promotable> signObjects = PromotionNoticeUtil.getPromotionNoticeItems(pn);
		for(Promotable signObject: signObjects){
			String displayIdentifierString = WTUtil.getObjectdisplayIdentifierString((Workable)signObject);
			
			if(WorkableUtil.isCheckedOut((Workable)signObject)){
				errorMsg+="对象被检出,请先检入:" + displayIdentifierString + ";\n";
			}
			//获取oid
			String oid1   = WTUtil.getWTObjectOid((WTObject)signObject);
			//获取VR oid再转换为大版本最新小版本对象,比较两个oid
			String vrOid1 = WTUtil.getVROid((Versioned)signObject);
			String oid2   = WTUtil.getWTObjectOid(WTUtil.getWTObject(vrOid1));
			if(!oid1.equals(oid2)){
				errorMsg+="对象不是当前大版本的最新小版本:" + displayIdentifierString + ";\n";
			}
		}
		/**
		 * 汇总校验信息,抛出异常
		 */
		if(!"".equals(errorMsg)){
			throw new WTException(errorMsg);
		}
	}
	
	/**
	 * 编制活动-转变条件-取消审阅校验
	 * @description
	 * @param self
	 * @param PrimaryBusinessObject
	 * @throws WTException 
	 */
	public static void validateOnCancel(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		String errorMSG = "";
		/**
		 * 1、校对者、批准者必须有人
		 */
		
		/**
		 * 2、其他业务校验
		 */
		
		/**
		 * 汇总校验信息,抛出异常
		 */
		if(!"".equals(errorMSG)){
			throw new WTException(errorMSG);
		}
	}
	
	/**
	 * 设置状态_已取消
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @throws WTException 
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 * @throws LifeCycleException 
	 * @throws WTInvalidParameterException 
	 */
	public static void cancelSubmit(ObjectReference self,WTObject primaryBusinessObject) throws WTInvalidParameterException, LifeCycleException, RemoteException, InvocationTargetException, WTException{
		//设置签审包状态为已取消
		WfUtil.changeLifeCycle(primaryBusinessObject, BusinessConstants.State.WISYIQUXIAO);
		//设置签审对象状态为正在工作
		PromotionNotice pn                = (PromotionNotice) primaryBusinessObject;
		ArrayList<Promotable> signObjects = PromotionNoticeUtil.getPromotionNoticeItems(pn);
		for(Promotable signObject: signObjects){
			WfUtil.changeLifeCycle((WTObject)signObject, BusinessConstants.State.INWORK);
		}
	}
	
	/**
	 * EBOM签审,修改签审包和签审对象的生命周期状态
	 * @description
	 * @param primaryBusinessObject
	 * @throws WTException 
	 * @throws MaturityException 
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 * @throws WTInvalidParameterException 
	 */
	public static void changeState(WTObject primaryBusinessObject,String stateKey) throws MaturityException, WTException, WTInvalidParameterException, RemoteException, InvocationTargetException{
		logger.debug("开始修改EBOM签审包和签审对象生命周期状态,primaryBusinessObject:" + primaryBusinessObject.getDisplayIdentity()+",stateKey:" + stateKey);
		//获取签审包
		PromotionNotice signPackage       = (PromotionNotice) primaryBusinessObject;
		//获取签审对象
		ArrayList<Promotable> signObjects = PromotionNoticeUtil.getPromotionNoticeItems(signPackage);
		//修改签审包生命周期状态
		WfUtil.changeLifeCycle(signPackage, stateKey);
		//循环修改签审对象的生命周期状态
		for(Promotable pt : signObjects){
			WfUtil.changeLifeCycle((WTObject)pt, stateKey);
		}
		logger.debug("修改EBOM签审包和签审对象生命周期状态完毕,primaryBusinessObject:" + primaryBusinessObject.getDisplayIdentity());
	}
	
	/**
	 * EBOM签审包,修改任务完成后执行
	 * 1、刷新签审包里的对象为当前大版本的最新小版本；
	 * 2、重置EBOM上移除的part和epm生命周期状态为正在工作。
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @throws WTException 
	 * @throws WTRuntimeException 
	 * @throws WTPropertyVetoException 
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
	public static void refreshEBOMSignPackage(ObjectReference self,WTObject primaryBusinessObject) throws WTRuntimeException, WTException, RemoteException, InvocationTargetException, WTPropertyVetoException{
		Transaction tx = null;
		try{
			tx = new Transaction();
			tx.start();
			
			//1、获取根节点部件,此处假设从签审包软属性"ROOTPART"上读取oid
			PromotionNotice signPackage = (PromotionNotice) primaryBusinessObject;
			String rootPartOid          = IBAUtil.getIBAValue(signPackage, "ROOTPART");
			//2、通过根节点部件oid获取对应大版本的最新小版本部件
			//2.1获取当前版本部件
			WTPart rootPart = (WTPart) new ReferenceFactory().getReference(rootPartOid).getObject();
			//2.2获取对应大版本最新小版本
			/*方法1String rootPartVersion = rootPart.getVersionIdentifier().getValue();
			QueryResult qr         = PartUtil.getParts(rootPart.getNumber(),rootPartVersion, "", rootPart.getViewName());
			rootPart = (WTPart) qr.nextElement();*/
			//oid-->vroid->获取对象
			rootPart = (WTPart) WTUtil.getWTObject(WTUtil.getVROid(rootPartOid));
			
			//获取原签审对象的masterOid集合
			Map<String,WTObject> existsPartEpmMap = new HashMap<String,WTObject>();
			//获取原签审对象,遍历得到masterOid
			ArrayList<Promotable> existsPartEpms = PromotionNoticeUtil.getPromotionNoticeItems(signPackage);
			for(Promotable promotable : existsPartEpms){
				if(promotable instanceof WTPart){
					WTPart part = (WTPart) promotable;
					String masterOid = WTUtil.getWTObjectOid((WTObject)part.getMaster());
					existsPartEpmMap.put(masterOid,part);
				}else if(promotable instanceof EPMDocument){
					EPMDocument epm = (EPMDocument) promotable;
					String masterOid = WTUtil.getWTObjectOid((WTObject)epm.getMaster());
					existsPartEpmMap.put(masterOid,epm);
				}
			}
			
			//获取最新版的对象,用于添加到签审对象
			Set<WTObject> allEBOMPartEpms = EBOMHelper.getEBOMPartEpms(rootPart); 
			//移除签审包已有的签审对象
			PromotionNoticeUtil.removeAllPromotionNoticeItems(signPackage);
			//添加最新的EBOM对象到签审包
			for(WTObject partEpm : allEBOMPartEpms){
				if(partEpm instanceof WTPart){
					WTPart part = (WTPart) partEpm;
					String masterOid = WTUtil.getWTObjectOid((WTObject)part.getMaster());
					
					//如果对象存在于原签审对象中,则移除
					if(existsPartEpmMap.containsKey(masterOid)){
						existsPartEpmMap.remove(masterOid);
					}
				}else if(partEpm instanceof EPMDocument){
					EPMDocument epm = (EPMDocument) partEpm;
					String masterOid = WTUtil.getWTObjectOid((WTObject)epm.getMaster());
					
					//如果对象存在于原签审对象中,则移除
					if(existsPartEpmMap.containsKey(masterOid)){
						existsPartEpmMap.remove(masterOid);
					}
				}
				PromotionNoticeUtil.addPromotionNoticeItem(signPackage, (Promotable)partEpm);
			}
			
			//existsPartEpmMap,仅剩下被移除的签审对象,转换为对应大版本最新小版本,重置生命周期状态为正在工作
			for(String masterOid : existsPartEpmMap.keySet()){
				WTObject partEpm = existsPartEpmMap.get(masterOid);
				if(partEpm instanceof WTPart){
					WTPart part = (WTPart) partEpm;
					//获取当前大版本的最新小版本
					part = (WTPart) WTUtil.getWTObject(WTUtil.getVROid(part));
					//重置生命周期状态为正在工作
					WfUtil.changeLifeCycle(part, BusinessConstants.State.INWORK);
				}else if(partEpm instanceof EPMDocument){
					EPMDocument epm = (EPMDocument) partEpm;
					epm  = (EPMDocument) WTUtil.getWTObject(WTUtil.getVROid(epm));
					WfUtil.changeLifeCycle(epm, BusinessConstants.State.INWORK);
				}
			}
			
			tx.commit();
			tx = null;
		}finally{
			if(tx != null){
				tx.rollback();
			}
		}
	}
	
	/**
	 * 初始化查看EBOM结构超链接
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @return
	 * @throws WTException 
	 */
	public static String createViewEBOMHref(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		String partOid = IBAUtil.getIBAValue(primaryBusinessObject, BusinessConstants.IBA.ROOTPART);
		String url = BusinessConstants.baseURL + "/netmarkets/jsp/ext/wis/part/viewEBOM.jsp?oid=" + partOid;
		String hrefHtml = "<a href=\"javascript:void(0);\"  "
				+ "onclick=\"window.open('"+url+"',null,'resizable=yes,height=600,width=400,status=no,toolbar=no,menubar=no,location=no');\">"
						+ "查看EBOM结构</a>";
		return hrefHtml;
	}
	
	/**
	 * 初始化下载EBOM数模链接
	 * @description
	 * @param self
	 * @param primaryBusinessObject
	 * @return
	 * @throws WTException 
	 */
	public static String createDownloadEBOMModelHref(ObjectReference self,WTObject primaryBusinessObject) throws WTException{
		String partOid = IBAUtil.getIBAValue(primaryBusinessObject, BusinessConstants.IBA.ROOTPART);
		String url = BusinessConstants.baseURL + "/netmarkets/jsp/ext/wis/part/downloadEbomModel.jsp?oid=" + partOid;
		String hrefHtml = "<a href=\"javascript:void(0);\"  "
				+ "onclick=\"window.open('"+url+"',null,'resizable=yes,height=600,width=400,status=no,toolbar=no,menubar=no,location=no');\">"
						+ "点击此处</a>";
		return hrefHtml;
	}
}
