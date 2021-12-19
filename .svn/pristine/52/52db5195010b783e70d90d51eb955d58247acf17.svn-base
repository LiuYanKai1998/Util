package ext.wisplm.util;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.access.AdHocAccessKey;
import wt.access.AdHocControlled;
import wt.change2.Changeable2;
import wt.change2.WTChangeActivity2;
import wt.change2.WTChangeOrder2;
import wt.enterprise.RevisionControlled;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTRuntimeException;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;
import wt.workflow.engine.WfEngineServerHelper;
import wt.workflow.work.WorkItem;

/**
 * 权限管理相关代码
 *
 *Zhong Binpeng Aug 18, 2021
 */
public class AccessUtil implements RemoteAccess{
	
	private static final Logger logger 	  = Logger.getLogger(AccessUtil.class);
	
	private static final String CLASSNAME = AccessUtil.class.getName();
	
	public static void testAccess() throws RemoteException, InvocationTargetException, WTException{
		String oid   = "VR:wt.doc.WTDocument:259021";
		WTObject obj = WTUtil.getWTObject(oid);
		//设置权限的对象
		AdHocControlled adHocControlled = (AdHocControlled) obj;
		//用户
		WTPrincipal user1     = PrincipalUtil.getUserByName("user1");
		//权限
		AccessPermission read = AccessPermission.DOWNLOAD;
		
		boolean hasAccess = AccessUtil.hasAccess(adHocControlled,user1, read);
		System.out.println("user1是否有下载权限:" + hasAccess);
		//设置读取权限
		//AccessUtil.setOOTBPermission(adHocControlled, user1, read);
		//hasAccess = AccessUtil.hasAccess(adHocControlled,user1, read);
		System.out.println("user1是否有下载权限:" + hasAccess);
		
	}
	/**
	 * 为工作任务的参与者设置任务关联PBO的读取下载权限
	 */
	public static void setUserPermissionOnLoadTable(List<WorkItem> workItems) throws Exception{
		for(WorkItem workItem : workItems){
			Map 	   result = WfUtil.getWfObjects(workItem);
			Object pboObject  = result.get("PBO");
			String processOid = (String) result.get("PROCESSOID");
			if(pboObject != null){ 
				List<WTObject> results = getDynamicPermissionObjects(processOid,pboObject);
				for(WTObject  wtobj: results){
					setREADDOWNLADPermission(wtobj);
				}
			}
		}
	}
	
	
	/**
	 * 获取pbo及相关需设置动态读取下载权限的对象
	 * @throws Exception 
	 */
	public static List<WTObject> getDynamicPermissionObjects(String processOid,Object pboObject) throws Exception{
		List<WTObject> result = new ArrayList<WTObject>();
		boolean enforce = false;
		try{
			enforce = SessionServerHelper.manager.setAccessEnforced(enforce);
			WTObject pbo = (WTObject) pboObject;
			//所有流程的PBO均允许访问
			result.add(pbo);
			
			//ECN,CA,改后对象
			if(pbo instanceof WTChangeOrder2){
				List<Changeable2> changeAfters = ChangeUtil.getChangeNoticeItemsAfter((WTChangeOrder2)pbo);
				List<WTChangeActivity2> cas    = ChangeUtil.getChangeActivityByChangeNotice((WTChangeOrder2)pbo);
				result.addAll(cas);
				for(Changeable2 able : changeAfters){
					result.add((WTObject) able);
				}
			}
		}finally{
			enforce = SessionServerHelper.manager.setAccessEnforced(enforce);
		}
		return result;
	}
	
	/**
	 * 完成任务时移除用户的动态权限
	 * @param workItem
	 * @throws WTException 
	 */
	public static void removeUserPermissionOnCompleteTask(WorkItem workItem) throws WTException{
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]  = {WorkItem.class};
			Object obj[] = {workItem};
			try {
				RemoteMethodServer.getDefault().invoke("removeUserPermissionOnCompleteTask",CLASSNAME, null, cla, obj);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				throw new WTException("动态权限处理出错,请联系管理员.");
			} 
		}
		boolean accessEnforced = false;
		try{
			accessEnforced    = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			Map 	result 	  = WfUtil.getWfObjects(workItem);
			Object pboObject  = result.get("PBO");
			String processOid = (String) result.get("PROCESSOID");
			if(pboObject == null){
				return;
			}
			List<WTObject> results = getDynamicPermissionObjects(processOid,pboObject);
			for(WTObject  wtobj: results){
				removeREADDOWNLADPermission(wtobj);
			}
			

		}catch(Exception e){
			e.printStackTrace();
			throw new WTException("动态权限处理出错,请联系管理员.");
		}finally{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	public static boolean isDynamicPermission(WTObject obj,WTPrincipal wtp){
		try{
			String dynamicUsersIBA = "";
	        String oldValue = IBAUtil.getIBAValue(obj, dynamicUsersIBA);
	        if(oldValue.contains(wtp.getName()+";")){
	        	return true;
	        }
		}catch(Exception e){
			logger.debug("判断是否设置过动态权限出错:" + obj.getDisplayIdentity() + "," + wtp.getName());
			e.printStackTrace();
			return true;
		}
        return false;
	}
	
	/**
	 * 移除用户的动态读取下载权限
	 * @param obj
	 * @throws WTException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static void removeREADDOWNLADPermission(WTObject obj) throws WTException, RemoteException, InvocationTargetException{
		WTPrincipal current = SessionHelper.manager.getPrincipal();
		if(isDynamicPermission(obj,current)){
			boolean canDownload = AccessControlHelper.manager.hasAccess(obj, AccessPermission.DOWNLOAD);
			boolean canRead 	= AccessControlHelper.manager.hasAccess(obj, AccessPermission.READ);
			if(canDownload){
				removeOOTBPermission((AdHocControlled)obj,current,AccessPermission.DOWNLOAD);
			}
			if(canRead){
				removeOOTBPermission((AdHocControlled)obj,current,AccessPermission.READ);
			}
			removeRecordFromIBA((WTObject)obj,current);
		}
	} 
	
	/**
	 * 为当前用户设置对象的读取下载权限
	 * @param pboObject
	 * @throws WTException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static void setREADDOWNLADPermission(WTObject pboObject) throws WTException, RemoteException, InvocationTargetException{
		boolean canDownload = AccessControlHelper.manager.hasAccess(pboObject, AccessPermission.DOWNLOAD);
		boolean canRead 	= AccessControlHelper.manager.hasAccess(pboObject, AccessPermission.READ);
		WTPrincipal current = SessionHelper.manager.getPrincipal();
		if(!canDownload){
			logger.debug("用户无下载权限,设置.");
			setOOTBPermission((AdHocControlled)pboObject,current,AccessPermission.DOWNLOAD);
		}
		if(!canRead){
			logger.debug("用户无读取权限,设置.");
			setOOTBPermission((AdHocControlled)pboObject,current,AccessPermission.READ);
		}
		if(!canDownload || !canRead){
			recordToIBA((WTObject)pboObject,current);
		}
	}
	
	/**
	 * 设置承担者对对象的操作权限
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
    public static void setOOTBPermission(AdHocControlled obj, WTPrincipal wtp, AccessPermission ap)
            throws WTException, RemoteException, InvocationTargetException {
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]   = {AdHocControlled.class,WTPrincipal.class,AccessPermission.class};
			Object objs[] = {obj,wtp,ap};
			RemoteMethodServer.getDefault().invoke("setOOTBPermission",CLASSNAME, null, cla, objs);
			return;
		}
        Transaction tx = null;	
		boolean accessEnforced = false;
        try {
        	tx = new Transaction();
        	tx.start();
        	accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
            WTPrincipalReference tempPrinRef = WTPrincipalReference.newWTPrincipalReference(wtp);
            obj = (AdHocControlled) WfEngineServerHelper.lock(obj);
            obj =  AccessControlHelper.manager.addPermission(obj,tempPrinRef,ap, AdHocAccessKey.WNC_ACCESS_CONTROL); //WNC_ACCESS_CONTROL
            PersistenceServerHelper.manager.update(obj);
            obj = (AdHocControlled) PersistenceHelper.manager.refresh(obj);
            tx.commit();
            tx = null;
            logger.debug("设置用户权限成功,权限:"+ ap + ",用户:"  + wtp.getName()+",对象:" + ((WTObject)obj).getDisplayIdentity());
        }finally {
        	if(tx != null){
        		tx.rollback();
        	}
        	SessionServerHelper.manager.setAccessEnforced(accessEnforced);
        }
    }
    
	/**
	 * 判断用户对对象是否具有指定权限
	 * @description
	 * @param adHocControlled
	 * @param user
	 * @param permission
	 * @return
	 * @throws WTException 
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
	public static Boolean hasAccess(AdHocControlled adHocControlled,WTPrincipal user,AccessPermission permission) throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]   = {AdHocControlled.class,WTPrincipal.class,AccessPermission.class};
			Object objs[] = {adHocControlled,user,permission};
			return (Boolean) RemoteMethodServer.getDefault().invoke("hasAccess",CLASSNAME, null, cla, objs);
		}	
		return AccessControlHelper.manager.hasAccess(user, adHocControlled, permission);
	}
	
	/**
	 * 移除承担者对对象的操作权限
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
    public static void removeOOTBPermission(AdHocControlled obj, WTPrincipal wtp, AccessPermission ap)throws WTException, RemoteException, InvocationTargetException {
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]  = {AdHocControlled.class,WTPrincipal.class,AccessPermission.class};
			Object objs[] = {obj,wtp,ap};
			RemoteMethodServer.getDefault().invoke("removeOOTBPermission",CLASSNAME, null, cla, objs);
			return;
		}
        Transaction tx = null;	
		boolean accessEnforced = false;
        try {
        	tx = new Transaction();
        	tx.start();
        	accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		    WTPrincipalReference tempPrinRef = WTPrincipalReference.newWTPrincipalReference(wtp);
		    obj = (AdHocControlled) WfEngineServerHelper.lock(obj);
		    AccessControlHelper.manager.removePermission(obj, tempPrinRef, ap, AdHocAccessKey.WNC_ACCESS_CONTROL); //AdHocAccessKey.WNC_ACCESS_CONTROL
		    //PersistenceServerHelper.manager.update(obj, false);
			PersistenceServerHelper.manager.update(obj);
			obj = (AdHocControlled) PersistenceHelper.manager.refresh(obj);
            tx.commit();
            tx = null;
		    logger.debug("移除用户权限成功,权限:"+ ap + ",用户:"  + wtp.getName()+",对象:" + ((WTObject)obj).getDisplayIdentity());
		}finally {
        	if(tx != null){
        		tx.rollback();
        	}
        	SessionServerHelper.manager.setAccessEnforced(accessEnforced);
        }
    }
    
    public static void recordToIBA(WTObject obj, WTPrincipal wtp){
    	try{
    		String dynamicUsersIBA = "";
            String oldValue = IBAUtil.getIBAValue((WTObject)obj, dynamicUsersIBA);
            if(!oldValue.contains(wtp.getName()+";")){
            	IBAUtil.setIBAValue((WTObject)obj, dynamicUsersIBA,oldValue + wtp.getName()+";");
            }
    	}catch(Exception e){
    		logger.debug("用户动态权限配置记录到IBA出错:" + obj.getDisplayIdentity() + "," + wtp.getName());
    		e.printStackTrace();
    	}
    }
    
    public static void removeRecordFromIBA(WTObject obj, WTPrincipal wtp){
    	try{
    		String dynamicUsersIBA = "";
            String oldValue = IBAUtil.getIBAValue((WTObject)obj, dynamicUsersIBA);
            String newValue = oldValue.replace(wtp.getName()+";","");
            IBAUtil.setIBAValue((WTObject)obj, dynamicUsersIBA,newValue);
    	}catch(Exception e){
    		logger.debug("用户动态权限配置记录到IBA出错:" + obj.getDisplayIdentity() + "," + wtp.getName());
    		e.printStackTrace();
    	}

    }
    
  /*  *//**
     * 
     * @param principal
     * @param object
     * @param permission
     * @param OOTBHasAccess
     * @return
     *//*
    public static boolean hasAccess(WTPrincipal principal,Object object,AccessPermission permission,boolean OOTBHasAccess){
    	try{
        	//OOTB无权限访问,不进行二次过滤
        	if(!OOTBHasAccess){
        		return OOTBHasAccess;
        	}
        	//未启用客制化权限控制
        	if(!customPermissionController){
        		return OOTBHasAccess;
        	}
        	
        	if(principal == null || object == null || permission == null || !(object instanceof WTObject)){
        		return OOTBHasAccess;
        	}
        	if(!(principal instanceof WTUser)){
        		return OOTBHasAccess;
        	}
        	//排除使用OOTB权限控制的用户
        	WTUser user = (WTUser) principal;
        	if(OOTBPermissionUsers.contains(user.getName().toLowerCase())){
        		return OOTBHasAccess;
        	}
        	//当前上下文关闭权限控制
        	if(!SessionServerHelper.manager.isAccessEnforced()){
        		return OOTBHasAccess;
        	}
        	String className = object.getClass().getName().toLowerCase();
        	//当前类型未启用权限控制
        	if(!classNames.contains(className)){
        		return OOTBHasAccess;
        	}
        	WTObject obj = (WTObject) object;
        	//忽略工作流设置了动态权限的用户(读取下载),动态授权之后,继续通过数据密级和用户密级进行二次过滤。若忽略密级匹配,取消该段注释即可
        	if(permission.getStringValue().equals(AccessPermission.READ.getStringValue()) || permission.getStringValue().equals(AccessPermission.DOWNLOAD.getStringValue())){
        		if(!controlWFTempPermission){
            		if(isDynamicPermission(obj,user)){
            			return OOTBHasAccess;
            		}
        		}
        	}
        	return hasAccess(user,obj,OOTBHasAccess);
    	}catch(Exception e){
    		logger.error("客制化权限处理出错,使用OOTB权限:" + e.getMessage());
    		e.printStackTrace();
    		return OOTBHasAccess;
    	}

    	
    } 
    
    //用户密级与数据密级匹配验证
    public static boolean hasAccess(WTUser user,WTObject obj,boolean OOTBHasAccess) throws WTException{
    	String mj = IBAUtil.getIBAValue(obj, "");
    	return hasAccess(user, mj, OOTBHasAccess);
    }
    
    //用户密级与数据密级匹配验证
    public static boolean hasAccess(WTUser user,String mj,boolean OOTBHasAccess) throws WTException{ 	
    	mj = mj == null ? "" : mj.trim();
    	//数据无密级
    	if("".equals(mj)){
    		//1用户有密级,数据无密级  + 2用户无密级,数据无密级,使用OOTB权限控制
    		return OOTBHasAccess;
    	}else{
    		//数据有密级
        	String userMJ = user.getPostalAddress();
        	userMJ = userMJ == null ? "" : userMJ.trim();
        	
        	//3用户无密级,数据有密级,不允许访问
        	if("".equals(userMJ)){
        		return false;
        	}
        	//4用户有密级,数据有密级
        	else{
            	ArrayList<String> dataMJList = userPermissionMap.get(userMJ);
            	//用户可访问的数据密级包含当前数据的密级
            	if(dataMJList.contains(mj)){
            		return true;
            	}else{
            		return false;
            	}
        	}
    	}
    }*/
    
    /**
     * 判断用户是否为创建者或者修改权限
     * 
     * @param obj
     * @param user
     * @return
     * @throws Exception
     */
    public boolean checkCreatorOrModify(Object obj, WTUser user) throws Exception {
        boolean isCreator = false;
        boolean isModify = false;
        if (obj instanceof RevisionControlled) {
            isCreator = WTUtil.isCreatorOrModifier((RevisionControlled) obj, user);
        }
        if (!isCreator) {
            WTUtil.checkUserObjAccess(obj, user, AccessPermission.MODIFY);
        }

        return isCreator || isModify;
    }

    public boolean allowModify(Object obj, WTUser user) throws Exception {
        return WTUtil.checkUserObjAccess(obj, user, AccessPermission.MODIFY);
    }



    
    public boolean isCheckoutedBySelf(String oid) throws Exception {
        WTPrincipal principal = SessionHelper.manager.getPrincipal();
        return WorkInProgressHelper.isCheckedOut((Workable)WTUtil.getObjectByOid(oid), principal);
    }
    public boolean isCheckoutedBySelf(Workable workable) throws Exception {
        WTPrincipal principal = SessionHelper.manager.getPrincipal();
        return WorkInProgressHelper.isCheckedOut(workable, principal);
    }
    
    private static void initOOTBPermissionUser(){
    	String OOTBPermissionUser = "";
    	OOTBPermissionUser = OOTBPermissionUser == null ? "":OOTBPermissionUser.trim().replace("，", ",");
    	logger.debug("加载客制化权限配置,使用OOTB权限控制的用户:" + OOTBPermissionUser);
    	if(!"".equals(OOTBPermissionUser)){
    		String [] ss = OOTBPermissionUser.split(",");
    		for(String s : ss){
    			if(!"".equals(s)){
    				OOTBPermissionUsers.add(s.toLowerCase());
    			}
    		}
    	}
    }
    
    private static void initPermissionControlClassName(){
    	String className = "";
    	className = className == null ? "":className.trim().replace("，", ",");
    	logger.debug("加载客制化权限配置,需要使用客制化权限控制的对象类型:" + className);
    	if(!"".equals(className)){
    		String [] ss = className.split(",");
    		for(String s : ss){
    			if(!"".equals(s)){
    				classNames.add(s.toLowerCase());
    			}
    		}
    	}
    }
    
    /**
     * 加载用户密级和数据密级的映射关系
     */
    private static void initUserPermissionMap(){
    	Set keySet = null;//Properties.getAllKeys("人员密级.");
    	//key用户密级,value可访问的数据密级集合
    	for(Iterator it = keySet.iterator();it.hasNext();){
    		ArrayList<String> values = new ArrayList<String>();
    		String key = (String) it.next();
    		String value = "";
    		value = value == null ? "":value.trim().replace("，", ",");
        	if(!"".equals(value)){
        		String [] ss = value.split(",");
        		for(String s : ss){
        			if(!"".equals(s)){
        				values.add(s);
        			}
        		}
        	}
        	key = key.replace("人员密级.", "");
        	logger.debug("加载客制化权限配置,用户密级:" + key + ",可访问数据密级:" + value);
        	userPermissionMap.put(key, values);
    	}
    }
    
    //是否启用客制化权限控制
    private static boolean customPermissionController = false;
    //使用OOTB权限控制的用户集
    private static List<String> OOTBPermissionUsers = new ArrayList<String>();
    //需要使用权限控制的对象类型
    private static List<String> classNames = new ArrayList<String>();
    //是否控制工作流中设置了动态权限的用户
    private static boolean controlWFTempPermission = true;
    //用户与数据密级匹配关系
    private static Map<String,ArrayList<String>> userPermissionMap = new HashMap<String,ArrayList<String>>();
    
    static{
    	customPermissionController = true;
    	logger.debug("加载客制化权限配置,是否启用了客制化权限控制:" + customPermissionController);
    	
    	controlWFTempPermission = true;
    	logger.debug("加载客制化权限配置,是否控制工作流中设置了动态权限的用户:" + controlWFTempPermission);
    	
    	//initOOTBPermissionUser();
    	//initPermissionControlClassName();
    	//initUserPermissionMap();
    }
}
