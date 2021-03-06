package ext.wisplm.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ptc.core.components.util.BeanHelper;
import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.common.TypeInstanceIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptor;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptorFactory;
import com.ptc.core.meta.server.TypeIdentifierUtility;
import com.ptc.core.meta.type.mgmt.server.impl.TypeDomainHelper;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.doc.WTDocument;
import wt.doc.WTDocumentMasterIdentity;
import wt.enterprise.BasicTemplateProcessor;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentMasterIdentity;
import wt.fc.Identified;
import wt.fc.IdentityHelper;
import wt.fc.ObjectIdentifier;
import wt.fc.ObjectMappable;
import wt.fc.ObjectReference;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.folder.FolderNotFoundException;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleManaged;
import wt.locks.LockException;
import wt.locks.LockHelper;
import wt.maturity.PromotionNotice;
import wt.maturity.PromotionNoticeIdentity;
import wt.method.MethodContext;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.part.WTPartMasterIdentity;
import wt.pom.PersistenceException;
import wt.pom.PersistentObjectManager;
import wt.pom.Transaction;
import wt.pom.WTConnection;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.series.Series;
import wt.series.SeriesException;
import wt.services.applicationcontext.implementation.DefaultServiceProvider;
import wt.session.SessionAuthenticator;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.type.ClientTypedUtility;
import wt.type.Typed;
import wt.util.IconSelector;
import wt.util.WTException;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.Iterated;
import wt.vc.IterationIdentifier;
import wt.vc.Mastered;
import wt.vc.VersionControlException;
import wt.vc.VersionControlHelper;
import wt.vc.VersionControlServerHelper;
import wt.vc.VersionForeignKey;
import wt.vc.VersionReference;
import wt.vc.Versionable;
import wt.vc.Versioned;
import wt.vc._IterationInfo;
import wt.vc.config.MultipleLatestConfigSpec;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;

public class WTUtil implements RemoteAccess{
	
	private static final String CLASSNAME = WTUtil.class.getName();
	
    private static final Logger log = LoggerFactory.getLogger(WTUtil.class);
	
  //11.0??????
   //???????????????
    public static void setCreator(Iterated obj, WTPrincipalReference principalRef) throws Exception {
     Class[] pp = new Class[] { WTPrincipalReference.class };
     Method setCreator = _IterationInfo.class.getDeclaredMethod("setCreator", pp);// getDeclaredMethod("setCreator",
                         // pp);
     setCreator.setAccessible(true);
     obj = (Iterated) PersistenceHelper.manager.refresh((Persistable) obj, false, true);
     setCreator.invoke(obj.getIterationInfo(), new Object[] { principalRef });
     PersistenceServerHelper.manager.update(obj);
    }
   
  //11.0??????
    //???????????????
    public static void setModifier(Iterated obj, WTPrincipalReference principalRef) throws Exception {
		 Class[] pp = new Class[] { WTPrincipalReference.class };
		 Method setCreator = _IterationInfo.class.getDeclaredMethod("setModifier", pp);
		 setCreator.setAccessible(true);
		 obj = (Iterated) PersistenceHelper.manager.refresh((Persistable) obj, false, true);
		 setCreator.invoke(obj.getIterationInfo(), new Object[] { principalRef });
		 PersistenceServerHelper.manager.update(obj);
    }
       
   /* 9.0??????
   // ???????????????
   public static void setModifier(Iterated obj, WTPrincipalReference uref) throws NoSuchMethodException,
           IllegalAccessException, InvocationTargetException {
       obj.getCreator(); // ?
       Class[] pp = new Class[]{WTPrincipalReference.class};
       Method setModifier = _IterationInfo.class.getDeclaredMethod("setModifier", pp);
       setModifier.setAccessible(true);
       setModifier.invoke(obj.getIterationInfo(), new Object[]{uref});
   }*/
    
    /*  
    // 9.0??????
    //???????????????
    public static void setCreator(Iterated obj, WTPrincipalReference uref) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        obj.getCreator(); // ?
        Class[] pp = new Class[] { WTPrincipalReference.class };
        Method setCreator = IterationInfo.class.getDeclaredMethod("setCreator", pp);
        setCreator.setAccessible(true);
        setCreator.invoke(obj.getIterationInfo(), new Object[] { uref });
    }*/
    

	   
    /**
     * ???????????????????????????
     * @description
     * @param v
     * @return
     * @throws PersistenceException
     * @throws WTException
     */
	public static Versioned getBeforeVersioned(Versioned v) throws PersistenceException, WTException{
		QueryResult qr = new MultipleLatestConfigSpec().process(VersionControlHelper.service.allVersionsOf(v));
		while(qr.hasMoreElements()){
			Versioned temp = (Versioned) qr.nextElement(); 
			if((temp.getVersionIdentifier().getValue().hashCode() + 1 ) == (v.getVersionIdentifier().getValue().hashCode())){
				return temp;
			}
		} 
		return null;
	}
	
   public static Folder setFolder(WTContainerRef wtcontainerref, FolderEntry folderentry, String s)throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			Class []cla = {WTContainerRef.class,FolderEntry.class,String.class};
			Object[]obj = {wtcontainerref,folderentry,s};
			return (Folder) RemoteMethodServer.getDefault().invoke("setFolder",CLASSNAME, null,cla,obj);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		    if(s != null && wtcontainerref != null){
		        Folder folder = null;
		        try{
		        	folder = FolderHelper.service.getFolder(s, wtcontainerref);
		        }
		        catch(FolderNotFoundException foldernotfoundexception){
		        	folder = null;
		        }
		        if(folder == null){
		        	folder = FolderHelper.service.createSubFolder(s, wtcontainerref);
		        }
		        FolderHelper.assignLocation(folderentry, folder);
		        return folder;
		    }
		    return null;
		   }finally{
				SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			}
    }
    
   /**
    * ??????????????????????????????
    * @description
    * @author      ZhongBinpeng
    * @param containerName
    * @return
    * @throws WTException
    * @throws RemoteException
    * @throws InvocationTargetException
    */
	public static WTContainer getContainerByName(String containerName) throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			Class []cla = {String.class};
			Object[]obj = {containerName};
			return(WTContainer)RemoteMethodServer.getDefault().invoke("getContainerByName",CLASSNAME, null,cla,obj);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		
			WTContainer container = null;
			QuerySpec qs = new QuerySpec(WTContainer.class);
			SearchCondition sc = new SearchCondition(WTContainer.class,WTContainer.NAME,SearchCondition.EQUAL,containerName);
			qs.appendSearchCondition(sc);
			QueryResult qr = PersistenceHelper.manager.find(qs);
			if(qr.hasMoreElements()){
				container = (WTContainer) qr.nextElement();
			}
			return container;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ????????????????????????????????????WCTYPE|
	 * @description
	 * @param obj
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 */
	public static String getType(WTObject obj) throws RemoteException, WTException{
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			String type = ClientTypedUtility.getExternalTypeIdentifier(obj).replace("WCTYPE|", "");	
			return type;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	public static boolean isSoftType(TypeIdentifier objType){
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			WCTypeIdentifier localWCTypeIdentifier = (WCTypeIdentifier)objType;
			return localWCTypeIdentifier.isSoftType();
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @description
	 * @param fullType ??????????????????,??????WCTYPE|??????
	 * @return
	 */
	public static boolean isSoftType(String fullType){
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			TypeIdentifier objType = CoreMetaUtility.getTypeIdentifier(fullType);
			return isSoftType(objType);
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ????????????????????????????????????????????????
	 * @description
	 * @param fullType
	 * @return
	 */
	public static TypeIdentifier getTypeIdentifier(String fullType){
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			return CoreMetaUtility.getTypeIdentifier(fullType);
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ????????????????????????????????????
	 * @description
	 * @param obj
	 * @return
	 */
	public static TypeIdentifier getTypeIdentifier(WTObject obj){
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			
			TypeIdentifier	typeidentifier = null;
	        TypeInstanceIdentifier typeinstanceidentifier = TypeIdentifierUtility.getTypeInstanceIdentifier(obj);
	        if(typeinstanceidentifier != null){
	            typeidentifier = (TypeIdentifier)typeinstanceidentifier.getDefinitionIdentifier();
			}
	        return typeidentifier;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ???????????????????????????????????? 
	 * @param obj
	 * @param fullType ?????????,??????WCTYPE|??????
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 */
    public static boolean typeEquals(WTObject obj,String fullType) throws RemoteException, WTException{
    	String type = getType(obj);
    	fullType   = fullType.replace("WCTYPE|", "");
    	return type.equals(fullType);
    }
    
	/**
	 * ???????????????????????????????????? 
	 * @param obj
	 * @param fullType ?????????,??????WCTYPE|??????
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 */
	public static boolean typeInstanceEquals(WTObject obj,String fullType) throws Exception{
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			TypeIdentifier	typeidentifier = null;
	        TypeInstanceIdentifier typeinstanceidentifier = TypeIdentifierUtility.getTypeInstanceIdentifier(obj);
	        if(typeinstanceidentifier != null){
	            typeidentifier = (TypeIdentifier)typeinstanceidentifier.getDefinitionIdentifier();
			}
	        if(!fullType.startsWith("WCTYPE|")){
	        	fullType = "WCTYPE|" + fullType;
	        }
	        //logger.debug("?????????????????????:" + typeidentifier.getTypename() +",?????????????????????:" + ClientTypedUtility.getTypeIdentifier(type).getTypename());
			return typeidentifier.equals(ClientTypedUtility.getTypeIdentifier(fullType));			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("??????????????????????????????????????????,obj:" + obj + ",type:" + fullType);
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * ??????????????? ???"WCTYPE|" ??????
	 * @param typed
	 */
	public static String getObjectType(Typed typed) throws RemoteException, WTException{
		return ClientTypedUtility.getExternalTypeIdentifier(typed);
	}
	
	/**
	 * ??????????????????????????????
	 * @param obj
	 * @return
	 * @throws WTException
	 */
	public static String getObjectTypeDisplay(WTObject obj) throws WTException{
        TypeInstanceIdentifier tii = TypeIdentifierUtility.getTypeInstanceIdentifier(obj);
		TypeIdentifier	ti         = (TypeIdentifier)tii.getDefinitionIdentifier();
        return getObjectTypeDisplay(ti);
	}
	
	public static String getObjectTypeDisplay(String fullType) throws WTException{
        if(!fullType.startsWith("WCTYPE|")){
        	fullType = "WCTYPE|" + fullType;
        }
		TypeIdentifier	typeidentifier = ClientTypedUtility.getTypeIdentifier(fullType);
		DefinitionIdentifier[] dis = {typeidentifier};
		DefinitionDescriptorFactory DESCRIPTOR_FACTORY = (DefinitionDescriptorFactory)DefaultServiceProvider.getService(DefinitionDescriptorFactory.class, "default" );
		DefinitionDescriptor[] defs = DESCRIPTOR_FACTORY.get(dis, null, Locale.SIMPLIFIED_CHINESE);
		if ((defs != null) && (defs.length > 0)){
			DefinitionDescriptor def = defs[0];
			return def.getDisplay(); // ?????????
		}
		return "";
	}
	
	public static String getObjectTypeDisplay(TypeIdentifier ti) throws WTException{
		DefinitionIdentifier[] dis = {ti};
		DefinitionDescriptorFactory DESCRIPTOR_FACTORY = (DefinitionDescriptorFactory)DefaultServiceProvider.getService(DefinitionDescriptorFactory.class, "default" );
		DefinitionDescriptor[] defs = DESCRIPTOR_FACTORY.get(dis, null, Locale.SIMPLIFIED_CHINESE);
		if ((defs != null) && (defs.length > 0)){
			DefinitionDescriptor def = defs[0];
			return def.getDisplay(); // ?????????
		}
		return "";
	}
	/**
	 * ???????????????????????????
	 * @param typed
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 */
	public static String getObjectLogicId(Typed typed){
		try{
			String type =  ClientTypedUtility.getExternalTypeIdentifier(typed);
			return type.substring(type.lastIndexOf(".") + 1,type.length());
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * ???????????????????????????
	 * @param typed
	 * @return
	 * @throws RemoteException
	 * @throws WTException
	 */
	public static String getObjectLogicId(String fullType){
		return fullType.substring(fullType.lastIndexOf(".") + 1,fullType.length());
	}
	
	public static String getObjectMasterOid(WTObject obj) throws WTException{
		ReferenceFactory rf = new ReferenceFactory();
		Mastered master = getObjectMaster(obj);
		if(master != null){
			return rf.getReferenceString(master);
		}
		return "";
	}
	
	public static Mastered getObjectMaster(WTObject obj) throws WTException{
		ReferenceFactory rf = new ReferenceFactory();
		Mastered master = null;
		if(obj instanceof Mastered){
			master = (Mastered) obj;
		}else if(obj instanceof WTPart){
			WTPart part = (WTPart) obj;
			master = part.getMaster();
		}else if(obj instanceof WTDocument){
			WTDocument doc = (WTDocument) obj;
			master = doc.getMaster();
		}else if(obj instanceof EPMDocument){
			EPMDocument epm = (EPMDocument) obj;
			master = epm.getMaster();
		}
		return master;
	}
	
	/**
	 * ???????????????ida2a2???
	 */
	public static String getIda2a2(WTObject obj){
		long ida2a2 = obj.getPersistInfo().getObjectIdentifier().getId();
		return ida2a2+"";
	}
	
	public static  boolean isCreatorOrModifier(RevisionControlled rc) throws WTException{
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		return isCreatorOrModifier(rc,user);
	}
	
	public static  boolean isCreatorOrModifier(Iterated rc) throws WTException{
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		return isCreatorOrModifier(rc,user);
	}
	
	public static  boolean isCreatorOrModifier(RevisionControlled rc,WTUser user) throws WTException{
		return (rc.getModifierName().equals(user.getName()) || rc.getCreatorName().equals(user.getName()));
	}
	
	public static  boolean isCreatorOrModifier(Iterated rc,WTUser user) throws WTException{
		return (rc.getModifierName().equals(user.getName()) || rc.getCreatorName().equals(user.getName()));
	}
	
	/**
	 * ?????????
	 * @param versioned
	 * @return
	 * @throws Exception
	 */
	public static Versioned newVersion(Versioned versioned) throws Exception{
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]  = {Versioned.class};
			Object obj[] = {versioned};
			return (Versioned)RemoteMethodServer.getDefault().invoke("newVersion",CLASSNAME, null, cla,obj);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			versioned =  VersionControlHelper.service.newVersion(versioned);
          FolderHelper.assignLocation((FolderEntry) versioned, FolderHelper.getFolder((FolderEntry) versioned));
          versioned = (Versioned) PersistenceHelper.manager.save(versioned);
          versioned = (Versioned) PersistenceHelper.manager.refresh(versioned);
          return versioned;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	 }
	
	public static Workable newIteration(Workable able) throws LockException, WTException, WTPropertyVetoException{
		//?????????				
        if (!(WorkInProgressHelper.isCheckedOut(able))) {
        	able = getCheckOutObject(able);
        }
        if (WorkInProgressHelper.isCheckedOut(able)){
        	able = WorkInProgressHelper.service.checkin(able, "???????????????");
        }
        return able;
	}
	
	/**
	 * ?????????????????????????????????
	 * @param workable
	 * @return
	 * @throws LockException
	 * @throws WTException
	 */
    public static Workable getCheckOutObject(Workable workable) throws LockException, WTException {
        Workable retVal = null;
        try {
            if (isCheckoutAllowed(workable)) {
                WorkInProgressHelper.service.checkout(workable, WorkInProgressHelper.service.getCheckoutFolder(),
                        "????????????");
                retVal = WorkInProgressHelper.service.workingCopyOf(workable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (retVal == null)
            throw new WTException("??????????????????!");
        return retVal;
    }
    
    
    public static boolean isCheckoutAllowed(Workable workable) throws LockException, WTException {
        return !(WorkInProgressHelper.isWorkingCopy(workable) || WorkInProgressHelper.isCheckedOut(workable) || LockHelper
                .isLocked(workable));
    }
    
	public static String getWTObjectOid(WTObject obj) throws WTException{
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]  = {WTObject.class};
			Object objs[] = {obj};
			try{
				return (String)RemoteMethodServer.getDefault().invoke("getWTObject",CLASSNAME, null, cla,objs);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			return new ReferenceFactory().getReferenceString(ObjectReference.newObjectReference((obj.getPersistInfo().getObjectIdentifier())));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	public static WTObject getWTObject(String oid){
		if(!RemoteMethodServer.ServerFlag){
			Class cla[]  = {String.class};
			Object obj[] = {oid};
			try{
				return (WTObject)RemoteMethodServer.getDefault().invoke("getWTObject",CLASSNAME, null, cla,obj);
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		boolean accessEnforced = false;
		try{
			accessEnforced 	   = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			return (WTObject) new ReferenceFactory().getReference(oid).getObject();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	
	/**
	 * ??????????????????????????????
	 */
	public static boolean equal(WTObject obj1,WTObject obj2) throws WTException{
		long obj1ID  = obj1.getPersistInfo().getObjectIdentifier().getId();
		long obj2ID  = obj2.getPersistInfo().getObjectIdentifier().getId();
		return obj1ID == obj1ID;
	}
	
    public static String getObjectIconSrc(WTObject obj) {
        String ret;
        if (!RemoteMethodServer.ServerFlag) {
            String method = "getObjectIconSrc";
            Class[] types = { WTObject.class };
            Object[] values = { obj };
            try {
                ret = (String) RemoteMethodServer.getDefault().invoke(method, CLASSNAME, null, types, values);
            } catch (Exception e) {
                ret = "";
            }
            return ret;
        }
        try {
            IconSelector iconselector = BasicTemplateProcessor.getIconSelector(obj, null, null, null, null);
            ret = BasicTemplateProcessor.getIconResource(iconselector, true);
        } catch (Exception e) {
            ret = "";
        }
        return ret;
    }
    
    /**
     * ?????????????????????,A.1??????A.2,??????A.1??????
     * @param iterated
     * @param s 
     * 2011-9-22 ??????10:54:45
     */
    public static void setIteration(Iterated iterated, String s) {
        try {
            if (s != null) {
                Series series = Series.newSeries("wt.vc.IterationIdentifier", s);
                IterationIdentifier iterationidentifier = IterationIdentifier.newIterationIdentifier(series);
                VersionControlHelper.setIterationIdentifier(iterated, iterationidentifier);
            }
        } catch (WTPropertyVetoException e) {
        		e.printStackTrace();
        } catch (SeriesException e) {
        	e.printStackTrace();
        } catch (WTException e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * ???WTDocument???EPMDocument???WTPart?????????
     */
    public static void resetWTObjectNumberName(WTObject wto, String name,String number) throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
			 RemoteMethodServer.getDefault().invoke("resetWTObjectNumberName",CLASSNAME, null,new Class[] { WTObject.class, String.class},new Object[] { wto,name});
			 return;
		}
		
       //boolean AccessEnforced = false;
        Transaction transaction = null;
        String userId = SessionHelper.manager.getPrincipal().getName();
		try{
			SessionHelper.manager.setAdministrator();// ?????????????????????
			//AccessEnforced = SessionServerHelper.manager.setAccessEnforced(AccessEnforced);
            transaction = new Transaction();
            transaction.start();
            Identified identified = null;
            if (wto instanceof WTPart) {
                WTPart thePart = (WTPart) wto;
                identified = (Identified) thePart.getMaster();
                WTPartMasterIdentity masteridentity = (WTPartMasterIdentity) identified.getIdentificationObject();
                if(name != null && !"".equals(name)){
                	masteridentity.setName(name);
                }
                if(number != null  && !"".equals(number)){
                	masteridentity.setNumber(number);
                }
                identified = IdentityHelper.service.changeIdentity(identified, masteridentity); // ?????????              
            } else if (wto instanceof EPMDocument) {
                EPMDocument theDoc = (EPMDocument) wto;
                identified = (Identified) theDoc.getMaster();
                EPMDocumentMasterIdentity masteridentity = (EPMDocumentMasterIdentity) identified
                        .getIdentificationObject();
                if(name != null  && !"".equals(name)){
                	masteridentity.setName(name);
                }
                if(number != null  && !"".equals(number)){
                	masteridentity.setNumber(number);
                }
                identified = IdentityHelper.service.changeIdentity(identified, masteridentity); // ?????????
            } else if (wto instanceof WTDocument) {
                WTDocument theDoc = (WTDocument) wto;
                identified = (Identified) theDoc.getMaster();
                WTDocumentMasterIdentity masteridentity = (WTDocumentMasterIdentity) identified
                        .getIdentificationObject();
                if(name != null  && !"".equals(name)){
                	masteridentity.setName(name);
                }
                if(number != null  && !"".equals(number)){
                	masteridentity.setNumber(number);
                }
                identified = IdentityHelper.service.changeIdentity(identified, masteridentity); // ?????????
            }else if(wto instanceof PromotionNotice){
            	PromotionNotice pn = (PromotionNotice) wto;
				PromotionNoticeIdentity pi = (PromotionNoticeIdentity) pn.getIdentificationObject();
                if(name != null  && !"".equals(name)){
                	pi.setName(name);
                }
                if(number != null  && !"".equals(number)){
                	pi.setNumber(number);
                }
				identified = IdentityHelper.service.changeIdentity(pn, pi);
            }
            if(identified != null){
            	PersistenceServerHelper.manager.update(identified);
            }
            transaction.commit();
            transaction = null;
        }finally {
            if (transaction != null){
            	transaction.rollback();
            } 
           // SessionServerHelper.manager.setAccessEnforced(AccessEnforced);
            SessionHelper.manager.setPrincipal(userId);
        }
    }
    
	public static String getExchangeDomain() throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			return (String) RemoteMethodServer.getDefault().invoke("getExchangeDomain",CLASSNAME, null, null, null);
		}
        MethodContext mc = MethodContext.getContext(Thread.currentThread());
        if (mc == null){
        	mc = new MethodContext(null, null);
        }
		return TypeDomainHelper.getExchangeDomain();
	}
	
	/**
	 * ??????oid??????ObjectReference
	 * @param oid	??????oid
	 * @return	Object Reference
	 * @throws WTException 
	 */
	public static WTReference getObjectRefByOid(String oid) throws WTException{

		boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
		try {
			ReferenceFactory referencefactory = new ReferenceFactory();
			WTReference wtreference = referencefactory.getReference(oid);					
			return wtreference;
		} catch (Exception e) {
			e.printStackTrace();
			throw new WTException(e);
		} finally {
			SessionServerHelper.manager.setAccessEnforced(enforce);
		}
	
	}
	
	/**
	 * ??????oid??????Object
	 * @param oid
	 * @return
	 * @throws WTException 
	 * @throws WTRuntimeException 
	 */
	public static Object getObjectByOid(String oid) throws WTRuntimeException, WTException{
		return getObjectRefByOid(oid).getObject();
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param iterated	????????????
	 * @return	?????????????????????????????????
	 * @throws VersionControlException 
	 * @throws WTRuntimeException 
	 */
	public static Persistable getObjectPredecessor(Iterated iterated) throws WTRuntimeException, VersionControlException{
        boolean flag = SessionServerHelper.manager.setAccessEnforced(false);
        try {
        	return VersionControlServerHelper.getControlBranch(iterated).getBranchPoint().getObject();
        } finally {
            SessionServerHelper.manager.setAccessEnforced(flag);
        }	       
	}

	/**
	 * ?????????????????????????????????URL
	 * @param persistable	????????????
	 * @return	??????????????????URL
	 * @throws WTException 
	 */
	public static String getObjectURL(Persistable persistable) throws WTException{
        boolean flag = SessionServerHelper.manager.setAccessEnforced(false);
        try {
        	ReferenceFactory factory = new ReferenceFactory();
            String oid = "";
            URL url = null;

            oid = factory.getReferenceString(persistable);
            Properties properties1 = new Properties();
            properties1.put("oid", oid);
            properties1.put("action", "ObjProps");
            url = wt.httpgw.GatewayURL.getAuthenticatedGateway(null).getURL("wt.enterprise.URLProcessor", "URLTemplateAction", null, properties1);

            return url.toString();
        } finally {
            SessionServerHelper.manager.setAccessEnforced(flag);
        }	       
	}

	/**
	 * ?????????????????????
	 * @param persist ?????????????????????
	 * @param type	?????????????????? ????????????LogicalIdentifier???
	 * @throws WTException 
	 */
	public static void setObjectType(Persistable persist, String type) throws WTException{
		boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
		try {
			TypeIdentifier id = TypeHelper.getTypeIdentifier(type);
			TypeHelper.setType(persist, id);
			PersistenceServerHelper.manager.update(persist);
		}finally {
			SessionServerHelper.manager.setAccessEnforced(enforce);
		}
	}
	
	public static String getVROid(Versioned obj){
		return "VR:" + obj.getClass().getName() + ":" + obj.getBranchIdentifier();
	}
	
	public static String getVROid(String oid) throws WTRuntimeException, WTException{
		RevisionControlled obj = (RevisionControlled) new ReferenceFactory().getReference(oid).getObject();
		return "VR:" + obj.getClass().getName() + ":" + obj.getBranchIdentifier();
	}
	
	//yyyy-MM-dd HH:mm:ss
	public static String formatTime(Date stamp,String format){
		if(stamp == null){
			stamp = new Timestamp(System.currentTimeMillis());
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String time = sdf.format(stamp);
		return time;
	}
	
	//yyyy-MM-dd HH:mm:ss
	public static String formatTime(String format){
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String time = sdf.format(stamp);
		return time;
	}
	
	public static String formatTimeyyyyMMddHHmmss(){
		Timestamp stamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		String time = sdf.format(stamp);
		return time;
	}
	
	
	public static Transaction startTransaction() throws PersistenceException, WTException{
		Transaction tx = null;
        if (PersistentObjectManager.getTransactionManager().isTransactionActive()){
            tx = Transaction.getCurrentTransaction();
        } else {
            tx = new Transaction();
            tx.start();
        }
        return tx;
	}
	
	/**
	 * Windchill?????????temp????????????????????????
	 * @param folderName
	 * @return
	 * @throws IOException
	 */
	public static File createWTTempFolder(String folderName) throws IOException{
		if(folderName == null || "".equals(folderName)){
			folderName = formatTime(new Timestamp(System.currentTimeMillis()),"yyyyMMddHHmmssSSS");
		}
        File folder = new File(WTProperties.getLocalProperties().getProperty("wt.home") + File.separator + "temp" + File.separator + folderName);
        folder.mkdirs();
        return folder;
	}
	
	/**
	 * codebase/temp????????????????????????
	 * @param folderName
	 * @return
	 * @throws IOException
	 */
	public static File createCodebaseTempFolder(String folderName) throws IOException{
		if(folderName == null || "".equals(folderName)){
			folderName = formatTime(new Timestamp(System.currentTimeMillis()),"yyyyMMddHHmmssSSS");
		}
        File folder = new File(WTProperties.getLocalProperties().getProperty("wt.codebase.location") + File.separator + "temp" + File.separator + folderName);
        folder.mkdirs();
        return folder;
	}
	
	   /**
     * ??????jdbc??????string iba???
     * @param object
     * @param ibaName
     * @return
     * @throws Exception
     */
    public static String getStringIBAByJDBC(WTObject object, String ibaName) throws Exception {
        return getStringIBAByJDBC(object, new String[]{ibaName}).get(ibaName);
    }

    /**
     * ??????jdbc??????string iba???
     * @param object
     * @param ibaNames
     * @return
     * @throws Exception
     */
    public static Map<String, String> getStringIBAByJDBC(WTObject object, String[] ibaNames) throws Exception {
        if (!RemoteMethodServer.ServerFlag) {
            return (Map<String, String>) RemoteMethodServer.getDefault().invoke("getStringIBAByJDBC", CLASSNAME, null,
                    new Class[]{WTObject.class, String[].class},
                    new Object[]{object, ibaNames});
        }
        MethodContext mc = MethodContext.getContext();
        Connection conn = ((WTConnection) mc.getConnection()).getConnection();
        Map<String, String> result = new HashMap<String, String>();
        String className = object.getClass().getName();
        String ida2a2a = PersistenceHelper.getObjectIdentifier(object).getId() + "";
        String ibaNameCondition = "";
        for (int i = 0; i < ibaNames.length; i++) {
            String or = "";
            if (i != 0) {
                or = "  or  ";
            }
            ibaNameCondition += or + " t2.name= '" + ibaNames[i] + "'";
            result.put(ibaNames[i], "");
        }

        String sql = "select t2.name as ibaName,t1.value2 as ibaValue from stringvalue t1,stringdefinition t2";
        sql += " where (" + ibaNameCondition + ") ";
        sql += " and t1.ida3a4='" + ida2a2a + "'";
        sql += " and t1.classnamekeya4='" + className + "'";
        sql += " and t1.ida3a6 = t2.ida2a2 ";

        Statement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                String ibaName = rs.getString("IBANAME");
                String ibaValue = rs.getString("IBAVALUE");
                ibaValue = ibaValue == null ? "" : ibaValue;
                result.put(ibaName, ibaValue);
            }
            return result;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
    }
	/**
	 * ????????????????????????????????????????????????
	 * @param inputPath
	 * @return 
	 * Feb 25, 2012 10:02:38 PM
	 */
    public static boolean deleteFiles(String inputPath) {
        try {
            File f = new File(inputPath);
            if (f.isDirectory()) {
                File[] flist = f.listFiles();
                for (int i = 0; i < flist.length; i++) {
                    File tmpfile = flist[i];
                    deleteFiles(tmpfile.getAbsolutePath());
                }
                f.delete(); 
            } else
                f.delete();

        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @description
     * @param mappa
     * @return
     * @throws WTException
     */
    public static Object getObjectdisplayIdentifier(ObjectMappable mappa) throws WTException{
    	Object object = BeanHelper.getProperty("displayIdentifier", mappa);
    	return object;
    }
    
    public static String getObjectdisplayIdentifierString(ObjectMappable mappa) throws WTException{
    	return getObjectdisplayIdentifier(mappa).toString();
    }
    
    /** 
     * ????????????????????????
     * 
     * @return
     * @throws Exception
     */
    public static boolean checkUserObjAccess(Object object, WTUser user, AccessPermission accessPermission)
            throws Exception {
        if (object == null) {
            throw new WTException("??????????????????");
        }
        if (user == null) {
            user = (WTUser) SessionHelper.manager.getPrincipal();
        }
        return AccessControlHelper.manager.hasAccess(user, object, accessPermission);
    }
    
    public static MethodContext getContext() {
        return MethodContext.getContext(Thread.currentThread());
    }

    public static void setAuthentication(MethodContext context, boolean createContext, String userName) {
        if(createContext) {
            context = new MethodContext((String)null, (Object)null);
            if(context.getAuthentication() == null) {
                SessionAuthenticator sa = new SessionAuthenticator();
                context.setAuthentication(sa.setUserName(userName));
            }
        }

    }

    public static void unregisterContext(MethodContext context, boolean createContext) {
        if(createContext && context != null) {
            context.unregister();
        }
    }
    
    public static WTReference getWTReference(Persistable effRecordable) {
        try {
            if (effRecordable != null)
                if (effRecordable instanceof Versionable)
                    return VersionReference.newVersionReference(VersionForeignKey.newVersionForeignKey(effRecordable.getClass(),
                            ((Versionable) effRecordable).getBranchIdentifier()));
                else
                    return ObjectReference.newObjectReference(ObjectIdentifier.newObjectIdentifier(effRecordable.getClass(), effRecordable
                            .getPersistInfo().getObjectIdentifier().getId()));
            else
                return null;
        } catch (WTException wtEx) {
            throw new WTRuntimeException(wtEx);
        }
    }
    public static String obj2String(Object obj){
        return obj==null?"":obj.toString().trim();
    }
    
    public static String getVersion(Object obj) {
        if(obj == null) {
            return "";
        }
        if(obj instanceof Versioned){
            Versioned ver = (Versioned)obj;
            return ver.getVersionIdentifier().getValue() + "."
                    + ver.getIterationIdentifier().getValue();
        }else if(obj instanceof Iterated){
            return ((Iterated)obj).getIterationIdentifier().getValue();
          }
       return "";
    }
    
    public static String getDisplayState(LifeCycleManaged lifeCycleManaged) throws WTException{
        return lifeCycleManaged.getState().getState().getDisplay(SessionHelper.getLocale());
    }
    
    public static WTPrincipal getCheckOutUser(Workable workable) throws WTException{
        Workable copy = null;
        if(WorkInProgressHelper.isWorkingCopy(workable)){
            copy = workable;
        }
        else if(WorkInProgressHelper.isCheckedOut(workable)){
            copy =  WorkInProgressHelper.service.workingCopyOf(workable);
        }
        if(copy !=null ){
            WTPrincipal p = (WTPrincipal)copy.getOwnership().getOwner().getObject();
            return p;
        }
        return null;
    }
    public static WTPrincipal getCheckOutUser(Persistable per) throws WTException{
        if(per instanceof Workable){
            Workable workable = (Workable)per;
            return getCheckOutUser(workable);
        }
        return null;
    }    
    
    @SuppressWarnings("unchecked")
    public static <T extends Versioned> T getLatestObject(Mastered mastered,Class<T > clazz) throws WTException {
        QueryResult queryResult = VersionControlHelper.service.allVersionsOf(mastered);
        return (T) queryResult.nextElement();
    }
    
    
}
