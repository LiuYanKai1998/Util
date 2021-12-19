package ext.wisplm.util;

import java.beans.PropertyVetoException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.foundation.container.common.FdnWTContainerHelper;
import com.ptc.core.foundation.type.server.impl.TypeHelper;
import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.netmarkets.model.NmObjectHelper;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;
import com.ptc.wvs.server.util.Util;
import ext.wisplm.util.IBAUtil;

import wt.admin.DomainAdministered;
import wt.admin.DomainAdministeredHelper;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.doc.DepartmentList;
import wt.doc.DocumentType;
import wt.doc.WTDocument;
import wt.doc.WTDocumentDependencyLink;
import wt.doc.WTDocumentHelper;
import wt.doc.WTDocumentMaster;
import wt.doc.WTDocumentMasterIdentity;
import wt.enterprise.RevisionControlled;
import wt.fc.Identified;
import wt.fc.IdentityHelper;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTHashSet;
import wt.fc.collections.WTSet;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.lifecycle.LifeCycleException;
import wt.lifecycle.LifeCycleHelper;
import wt.lifecycle.LifeCycleManaged;
import wt.lifecycle.LifeCycleServerHelper;
import wt.lifecycle.LifeCycleTemplate;
import wt.lifecycle.LifeCycleTemplateReference;
import wt.lifecycle.State;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.part.WTPartDescribeLink;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.representation.Representation;
import wt.representation.StandardRepresentationService;
import wt.series.HarvardSeries;
import wt.series.MultilevelSeries;
import wt.series.Series;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.type.ClientTypedUtility;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.Iterated;
import wt.vc.IterationIdentifier;
import wt.vc.IterationInfo;
import wt.vc.VersionControlException;
import wt.vc.VersionControlHelper;
import wt.vc.VersionIdentifier;
import wt.vc.Versioned;
import wt.vc.config.LatestConfigSpec;
import wt.vc.wip.WorkInProgressHelper;

/**
 * 文档(wt.doc.WTDocument)对象相关操作类
 *
 */
public class DocUtil implements RemoteAccess{
	
	private static final Logger logger       = LoggerFactory.getLogger(DocUtil.class);
	private static final String CLASSNAME    = DocUtil.class.getName();
	private static final ReferenceFactory RF =  new ReferenceFactory();
	
	/**
	 * 获取文档的子文档集合
	 * @param parent 根文档
	 * @param result 子文档集合
	 * @param allLevel 是否查询所有层级,true查询全部,false只查询第一层子文档
	 */
	public static List getDocumentChildren(WTDocument parent,List result,boolean allLevel) throws WTException{
		if(result == null){
			result = new Vector();
		}
		QueryResult qr = WTDocumentHelper.service.getUsesWTDocumentMasters(parent);
		while(qr.hasMoreElements()){
			WTDocumentMaster master = (WTDocumentMaster) qr.nextElement();
			QueryResult childQR 	= new LatestConfigSpec().process(VersionControlHelper.service.allIterationsOf(master));
			WTDocument latestChild  = (WTDocument) childQR.nextElement();
			result.add(latestChild);
			if(allLevel){
				getDocumentChildren(latestChild,result,allLevel);
			}
		}
		return result;
	}
	
	/**
	 * 获取文档主内容及附件集
	 * @param doc 文档对象
	 * @return    返回描述主内容或附件的ApplicationData对象,包含基本信息和文件流
	 * @throws WTException
	 * @throws PropertyVetoException
	 */
	public static List<ApplicationData> getPrimaryAndSecondary(WTDocument doc) throws WTException, PropertyVetoException{
		List<ApplicationData> result = new ArrayList<ApplicationData>();
		wt.content.ContentHolder contentHolder = ContentHelper.service.getContents(doc);
        Vector items = ContentHelper.getContentListAll(contentHolder);
        for (int i = 0; i < items.size(); i++) {
            ContentItem item = (ContentItem) items.get(i);
            if(item.getRole().equals(ContentRoleType.PRIMARY) || item.getRole().equals(ContentRoleType.SECONDARY)){
            	if(item instanceof ApplicationData){
            		result.add((ApplicationData)item);
            	}
            }
        }
		return result;
	}
	
	/**
	 * 获取文档主内容ApplicationData对象
	 * @description
	 * @param doc 文档对象
	 * @return ApplicationData对象,包含主内容基本信息和文件流
	 * @throws WTException
	 * @throws PropertyVetoException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static ApplicationData getPrimary(WTDocument doc) throws WTException, PropertyVetoException, RemoteException, InvocationTargetException{
		if(doc == null){
			throw new WTException("文档为空");
		}
		ContentItem item = ContentHelper.service.getPrimary(doc);
		if (item != null && item instanceof ApplicationData) {
			ApplicationData primaryContent = (ApplicationData) item;
			return primaryContent;
		}
		logger.info("未发现主内容文件,WTDocument=>" + doc);
		return null;
	}	

	/**
	 * 下载文档主内容到指定文件夹
	 * @description
	 * @param doc 文档对象
	 * @param folderPath 指定的服务器存储路径(文件夹),文件将下载到该目录
	 * @return 如果文档主内容不为空,返回下载到制定路径的主内容文件File,不改变源文件名称;如果主内容为空,返回null
	 * @throws InvocationTargetException
	 * @throws WTException
	 * @throws PropertyVetoException
	 * @throws IOException
	 */
	public static File downloadPrimary(WTDocument doc,String downloadFolderPath) throws InvocationTargetException, WTException, PropertyVetoException, IOException{
		if (!(downloadFolderPath.endsWith("/") || downloadFolderPath.endsWith("\\"))) {
			downloadFolderPath = downloadFolderPath + File.separator;
		}
		new File(downloadFolderPath).mkdirs();
		ApplicationData data = getPrimary(doc);
		if(data != null){
			String fileName      = data.getFileName(); 
			String fullPath = downloadFolderPath + fileName;
			ContentServerHelper.service.writeContentStream(data,fullPath);
			return new File(fullPath);
		}
		return null;
	}
	
	/**
	 * 下载文档附件到指定目录
	 * @description
	 * @param doc 文档对象
	 * @param downloadFolderPath 指定的服务器存储路径(文件夹)
	 * @return 返回下载下来的文件File集合
	 * @throws WTException
	 * @throws PropertyVetoException
	 * @throws IOException
	 */
	public static List<File> downloadSecondary(WTDocument doc,String downloadFolderPath) throws WTException, PropertyVetoException, IOException{
		if (!downloadFolderPath.endsWith("/") || downloadFolderPath.endsWith("\\")) {
			downloadFolderPath = downloadFolderPath + File.separator;
		}
		List<File> fileList = new ArrayList<File>();
		new File(downloadFolderPath).mkdirs();
		wt.content.ContentHolder contentHolder = ContentHelper.service.getContents(doc);
		Vector apps = ContentHelper.getApplicationData(contentHolder);
		FileOutputStream out = null;
		for (int i = 0; i < apps.size(); i++) {
			ApplicationData app = (ApplicationData) apps.elementAt(i);
			String role = app.getRole().getStringValue();
			//解决jpg格式的主内容可视化后的缩略图也被打印的bug
			if("wt.content.ContentRoleType.SECONDARY".equals(role)){
				String appFileName = app.getFileName();
				logger.debug("找到附件:" + appFileName);
				InputStream inputstream = ContentServerHelper.service.findContentStream(app);
				String filePath = downloadFolderPath + appFileName;
				out = new FileOutputStream(filePath);
				byte abyte1[] = new byte[4096];
				int j = 0;
				while ((j = inputstream.read(abyte1, 0, abyte1.length)) >= 0){
					out.write(abyte1, 0, j);
				}
				out.flush();
				out.close();
				fileList.add(new File(filePath));
			}
		}
		return fileList;
	}

	  /**
	   * 获取大版本对象(文档、部件、EPM文档等)的最新大版的最新小版
	   * @description
	   * @param resultItem
	   * @return
	   */
	  public static Iterated queryLatestVersionObject(Versioned resultItem) {
	      try {
	         Iterated lastVersion = null;
	         QueryResult allVersions = VersionControlHelper.service.allVersionsOf(resultItem);
	         while (allVersions.hasMoreElements()) {
	            lastVersion = ((Iterated) allVersions.nextElement());
	            break;
	         }
	         lastVersion = VersionControlHelper.service.getLatestIteration(lastVersion, false);
	         return lastVersion;
	      } catch (Throwable wte) {
	         wte.printStackTrace();
	         return null;
	      }
	   }
	   
    
	/**
	 * 获取文档所说明的部件(添加当前文档为说明文档的部件)
	 * @description
	 * @param doc 文档
	 * @return 部件集合QueryResult
	 * @throws WTException
	 */
    public static QueryResult getDescribeParts(WTDocument doc) throws WTException {
        LatestConfigSpec lcs = new LatestConfigSpec();
        QueryResult qr = PersistenceHelper.manager.navigate(doc,WTPartDescribeLink.DESCRIBES_ROLE, WTPartDescribeLink.class, true);
        qr = lcs.process(qr); 
        return qr;
    }
    
    /**
     * 获取文档所参考的部件(添加当前文档为参考文档的部件)
     * @description
     * @param doc 文档
     * @return 部件集合QueryResult
     * @throws WTException
     */
    public static QueryResult getReferenceParts(WTDocument doc) throws WTException{
		QueryResult qr = PersistenceHelper.manager.navigate(
				doc.getMaster(), "referencedBy",
				wt.part.WTPartReferenceLink.class);
		return qr;
    }
    
    /**
     * 创建文档对象
     * @param number 文档编号，不能为空
     * @param name   文档名称，不能为空
     * @param description 文档说明信息,可以为空
     * @param containerName 容器名称，要存储的容器名称(产品库、存储库、项目库名称)
     * @param fullType      文档类型名称全路径(必须在类型管理器中已定义该类型),多级类型之间用|隔开,如wt.doc.WTDocument或wt.doc.WTDocument|com.wisplm.TechDoc
     * @param lifecycleTemplateName 生命周期模板名称,如果为空,使用对象初始化规则定义的模板
     * @param state 要初始设置的目标生命周期状态的key,如果为空,使用生命周期模板初始状态
     * @param folderPath 要存储的文件夹路径(如存储到产品库根目录下的"工艺文件"文件夹,则传递/Default/工艺文件,Default表示产品库存储库等容器的根路径),如果为空,使用对象初始化规则设置的文件夹
     * @param ibaMap 要设置的软属性key和value,对于多值的软属性,多个值中间用||隔开；如果没有软属性,传null。
     * @return
     * @throws Exception
     */
    public static WTDocument createDoc(String number, String name,String description,String containerName,String fullType
    							, 
    							String lifecycleTemplateName,String state, String folderPath,Map ibaMap) throws Exception{
    	if(fullType == null || fullType.length() == 0){
    		fullType = "wt.doc.WTDocument";
    	}
    	TypeIdentifier objType = CoreMetaUtility.getTypeIdentifier(fullType);
        WTDocument doc		   = (WTDocument) CoreMetaUtility.newInstance(objType);
        
        doc.setNumber(number);
        doc.setName(name);
        doc.setDocType(DocumentType.getDocumentTypeDefault());
        doc.setDepartment(DepartmentList.getDepartmentListDefault());
        description = description == null ? "" : description;
        doc.setDescription(description);
        WTContainer container = WTUtil.getContainerByName(containerName);
        WTContainerRef cref   = WTContainerRef.newWTContainerRef(container);
        doc.setContainerReference(cref);
        Folder folder  = null;
        if(folderPath != null && !"".equals(folderPath)){
            folder = FolderUtil.getFolderByPath(folderPath, containerName);
            if(folder == null){
            	folder = FolderUtil.createSubFolder(folderPath, containerName);
            }
        }

/*        Folder folder = WTUtil.setFolder(cref, doc,folderPath);
        if(folderPath != null){
            Folder folder = FolderHelper.service.getFolder(folderPath, cref);
            FolderHelper.assignLocation(doc, folder);
        }*/
        FolderHelper.assignLocation(doc, folder);
        if(folder != null){
        	doc.setDomainRef( DomainAdministeredHelper.getAdminDomainRef((DomainAdministered)folder));
        }else{
        	doc.setDomainRef(cref.getReferencedContainerReadOnly().getDefaultDomainReference());
        }
        if(lifecycleTemplateName != null && lifecycleTemplateName.length() > 0 ){
        	LifeCycleTemplate lt = LifeCycleHelper.service.getLifeCycleTemplate(lifecycleTemplateName,cref);
        	if(lt == null){
        		throw new WTException("未在系统中找到生命周期模板:" + lifecycleTemplateName);
        	}
        	LifeCycleHelper.setLifeCycle(doc,lt);
        }
        doc = (WTDocument) PersistenceHelper.manager.save(doc);
        doc = (WTDocument) PersistenceHelper.manager.refresh(doc);
        if(state != null && state.length() > 0 ){
        	WfUtil.changeLifeCycle(doc, state);
        }
        int ibaSize = ibaMap == null ? 0 : ibaMap.size();
        logger.debug("开始设置软属性,数:" + ibaSize);
		if(ibaSize > 0){
			Set keySet = ibaMap.keySet();
			for(Iterator it = keySet.iterator();it.hasNext();){
				String ibaName = (String) it.next();
				Object value = ibaMap.get(ibaName);
				if(value != null){
					IBAUtil.setIBAValue(doc, ibaName, value.toString());
				}
				
			}
		}
		logger.debug("软属性设置结束...");
		logger.debug("文档创建成功:" + doc.getDisplayIdentity());
        return doc;
    }
    
    /**
     * 修订文档到指定版本版次
     * @description
     * @param oldDoc  要修订版本的文档对象
     * @param version 要修订的目标大版本(A,B,C等)
     * @param revision要修订的目标小版本(1,2,3等)
     * @param lifecycleTemplateName 修订后的文档要设置的生命周期模板
     * @param state 修订后的文档要设置的生命周期状态key
     * @param seriesVersionTemplate 要使用的版本序列名称(客制化版本序列必须是已导入到系统中的),如果为空,则使用系统默认的HarvardSeries(大版本字母,小版本数字)
     * @return
     * @throws WTPropertyVetoException
     * @throws VersionControlException
     * @throws WTException
     */
    public static WTDocument newVersion(WTDocument oldDoc,String version,String revision,String lifecycleTemplateName,String state,String seriesVersionTemplate) throws WTPropertyVetoException, VersionControlException, WTException{
		//将当前文档修订到目标版本
        Series series = Series.newSeries("wt.vc.IterationIdentifier", revision);
        MultilevelSeries ms = null;
        if(seriesVersionTemplate == null || seriesVersionTemplate.length() == 0){
        	HarvardSeries hs = HarvardSeries.newHarvardSeries();
        	hs.setValue(version);
        	ms = hs;
        }else{
            ms = MultilevelSeries.newMultilevelSeries(seriesVersionTemplate);//wt.series.HarvardSeries.SACVersion
            ms.setValueWithoutValidating(version);
        }
        WTDocument newDoc = (WTDocument) VersionControlHelper.service.newVersion(oldDoc,VersionIdentifier.newVersionIdentifier(ms), IterationIdentifier
                        .newIterationIdentifier(series));
        //与原版本文件夹相同
        FolderHelper.assignLocation((FolderEntry) newDoc, FolderHelper.getFolder(oldDoc));
        
        //调整生命周期
        if(lifecycleTemplateName != null && lifecycleTemplateName.length() > 0 ){
        	LifeCycleTemplate lt = LifeCycleHelper.service.getLifeCycleTemplate(lifecycleTemplateName,oldDoc.getContainerReference());
        	if(lt == null){
        		throw new WTException("未在系统中找到生命周期模板:" + lifecycleTemplateName);
        	}
        	LifeCycleHelper.setLifeCycle(newDoc,lt);
        }
        if(state != null && state.length() > 0 ){
        	LifeCycleServerHelper.setState(newDoc, State.toState(state));
        }
        newDoc = (WTDocument) PersistenceHelper.manager.save(newDoc);
        newDoc = (WTDocument) PersistenceHelper.manager.refresh(newDoc);
        return newDoc;
    }
		
	/**
	 * 为对象添加主内容或附件
	 */
    public static FormatContentHolder addApplicationDataToDoc(FormatContentHolder holder, File file, boolean isPrimary) throws WTException, WTPropertyVetoException,
            java.beans.PropertyVetoException, IOException {
        holder = (FormatContentHolder) ContentHelper.service.getContents(holder);
/*		Vector apps = ContentHelper.getApplicationData(holder);
		logger.debug("旧附件数量:" + apps.size());
		if (apps != null && apps.size() > 0) {
			for (int i = 0; i < apps.size(); i++) {
				ApplicationData app = (ApplicationData) apps.get(i);
				ContentServerHelper.service.deleteContent(holder, app);
				logger.debug("删除原有附件:" + app.getFileName());
			}
		}*/
		holder = (FormatContentHolder) PersistenceHelper.manager.refresh(holder);
        
        ApplicationData applicationdata = ApplicationData.newApplicationData(holder);
        applicationdata.setFileName(file.getName());
        applicationdata.setUploadedFromPath(file.getParent());
        if (isPrimary)
           applicationdata.setRole(ContentRoleType.PRIMARY);
        wt.content.ContentHolder ch = wt.content.ContentHelper.service.getContents(holder);
        ContentServerHelper.service.updateContent(ch, applicationdata,file.getPath());
        return holder;
    }
    
    /**
     * 通过编号、大版本、小版本精确搜索指定的文档对象
     * @description
     * @param number 文档编号
     * @param version   大版本 如果为空,搜索最新版本
     * @param iteration 小版本 如果为空,搜索大版本的最新小版本
     * @return 如果对应版本被检出,则返回原始版本
     * @throws WTException
     */
    public static WTDocument getDocument(String number,String version, String iteration) throws WTException {
        if (number == null)
            return null;
        LatestConfigSpec latestconfigspec = null;
        QuerySpec queryspec = new QuerySpec(WTDocument.class);
            queryspec.appendWhere(new SearchCondition(WTDocument.class, WTDocument.NUMBER, SearchCondition.EQUAL,
                    number.toUpperCase(), false));

            if (StringUtils.isNotBlank(version)) {
                queryspec.appendAnd();
                queryspec.appendWhere(new SearchCondition(WTDocument.class, Versioned.VERSION_IDENTIFIER + "."
                        + VersionIdentifier.VERSIONID, SearchCondition.EQUAL, version, false));
                if (StringUtils.isNotBlank(iteration)) {
                    queryspec.appendAnd();
                    queryspec.appendWhere(new SearchCondition(WTDocument.class, Iterated.ITERATION_IDENTIFIER + "."
                            + IterationIdentifier.ITERATIONID, SearchCondition.EQUAL, iteration, false));
                } else {
                    queryspec.appendAnd();
                    queryspec.appendWhere(new SearchCondition(WTDocument.class, Iterated.ITERATION_INFO + "."
                            + IterationInfo.LATEST, "TRUE"));
                }
            } else {
                latestconfigspec = new LatestConfigSpec();
                latestconfigspec.appendSearchCriteria(queryspec);
            }
            QueryResult qr = PersistenceHelper.manager.find(queryspec);
            if (latestconfigspec != null)
                qr = latestconfigspec.process(qr);
            while(qr.hasMoreElements()){
            	WTDocument doc = (WTDocument) qr.nextElement();
            	if(WorkInProgressHelper.isWorkingCopy(doc)){
            		doc = (WTDocument) WorkInProgressHelper.service.derivedFrom(doc);
            	}
            	return doc;
            }
        return null;
    }
    
    /**
     * 重置对象的生命周期模板和状态
     */
	public static void reassignLifeCycle(WTContainerRef cref,LifeCycleManaged obj,String LifeCycleTemplateName,String state) throws LifeCycleException, WTException{
		if(LifeCycleTemplateName != null && LifeCycleTemplateName.length() > 0 ){
			LifeCycleTemplate template = LifeCycleHelper.service.getLifeCycleTemplate(LifeCycleTemplateName,cref);
	        LifeCycleTemplateReference templateRef = template.getLifeCycleTemplateReference();
	        obj = LifeCycleHelper.service.reassign(obj, templateRef);
		}
		if(state != null && state.length() > 0){
			 LifeCycleHelper.service.setLifeCycleState(obj, State.toState(state)); 
		}
	}
	
	/**
	 * 获取指定编号文档的各个大版本的最新小版本
	 */
	public static Set getAllLatestDocs(String docNumber){
		Set docSet = new HashSet();
		try{
			docNumber = docNumber == null ? "":docNumber.trim().toUpperCase();
			if(docNumber.length() == 0){
				return docSet;
			}
			QuerySpec qs = new QuerySpec(WTDocument.class);
			SearchCondition numberSC = new SearchCondition(WTDocument.class,WTDocument.NUMBER, SearchCondition.EQUAL,docNumber.toUpperCase());
			qs.appendSearchCondition(numberSC);
			qs.appendAnd();
			SearchCondition latestSC = VersionControlHelper.getSearchCondition(WTDocument.class, true);
			qs.appendSearchCondition(latestSC);
			QueryResult qr = PersistenceHelper.manager.find(qs);
			while(qr.hasMoreElements()){
				WTDocument doc 	    = (WTDocument) qr.nextElement();
				docSet.add(doc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return docSet;
	}
	
	
	/**
	 * 获取文档所参考(被参考)的文档
	 * @param srcDoc
	 */
	public static List<WTDocument> getHasDependentWTDocuments(WTDocument srcDoc) throws RemoteException, InvocationTargetException {
		List<WTDocument> rsList = new ArrayList<WTDocument>();
		boolean enforce = SessionServerHelper.manager.setAccessEnforced(false);
		try {
			QueryResult qr = WTDocumentHelper.service.getHasDependentWTDocuments(srcDoc, true);
			while (qr.hasMoreElements()) {
				Object tempObj = qr.nextElement();
				if (tempObj instanceof WTDocument) {
					WTDocument doc = (WTDocument)tempObj;
					rsList.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SessionServerHelper.manager.setAccessEnforced(enforce);
		}
		return rsList;
	}
	
	/**
	 * 获取文档的参考文档
	 * @description
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static List<WTDocument> getDependsOnWTDocuments(WTDocument doc) throws Exception {
		List<WTDocument> docList = getDependsOnWTDocuments(doc,null);
		return docList;
	}
	
	/**
	 * 获取文档指定类型的参考文档
	 * @param doc
	 * @param refType
	 * @return
	 * @throws Exception 
	 */
	public static List<WTDocument> getDependsOnWTDocuments(WTDocument doc,String refType) throws Exception {
		List<WTDocument> rsList = new ArrayList<WTDocument>();
		boolean AccessEnforced = SessionServerHelper.manager.setAccessEnforced(false);
		try {
			QueryResult qr = WTDocumentHelper.service.getDependsOnWTDocuments(doc,true);
			while (qr.hasMoreElements()) {
				WTDocument refDoc = (WTDocument) qr.nextElement();
				if(refType != null && !"".equals(refType)){
					if(WTUtil.typeEquals(refDoc,refType)){
						rsList.add(refDoc);
					}
				}else{
					rsList.add(refDoc);
				}				
			}
		}finally {
			SessionServerHelper.manager.setAccessEnforced(AccessEnforced);
		}
		return rsList;
	}

	/**
	 * 获取文档的参考文档编号
	 * @description
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public static List<String> getDependsOnWTDocumentNumbers(WTDocument doc) throws Exception {
		List<String> numberList  = new ArrayList<String>();
		List<WTDocument> docList = getDependsOnWTDocuments(doc,null);
		for(WTDocument refDoc : docList){
			numberList.add(refDoc.getNumber());
		}
		return numberList;
	}
	
	/**
	 * 获取文档与参考文档之间的link
	 * @param doc
	 * @return
	 * @throws WTException 
	 */
	public static List<WTDocumentDependencyLink> getWTDocumentDependencyLink(WTDocument doc) throws WTException{
		boolean accessEnforced = false;
		try{
			accessEnforced  = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			List<WTDocumentDependencyLink> result = new ArrayList<WTDocumentDependencyLink>();
			//参考关系=ROLEB,所参考关系=ROLEA
			QueryResult qr = PersistenceHelper.manager.navigate(doc,WTDocumentDependencyLink.ROLE_BOBJECT_ROLE,
	                WTDocumentDependencyLink.class, false);
	        while (qr.hasMoreElements()) {
	            WTDocumentDependencyLink link = (WTDocumentDependencyLink) qr.nextElement();
	            result.add(link);
	        }
	        return result;
		}finally{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * 删除文档所有的参考文档关系
	 * @description
	 * @param doc
	 * @throws WTException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static void deleteDependencyLink(WTDocument doc) throws WTException, RemoteException, InvocationTargetException{
		boolean accessEnforced = false;
		try{
			accessEnforced  = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			List<WTDocumentDependencyLink> result = getWTDocumentDependencyLink(doc);
			WTSet set = new WTHashSet();
			set.addAll(result);
	        PersistenceServerHelper.manager.remove(set);
		}finally{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	
	/**
	 * 删除文档与指定文档之间的参考关系
	 * @description
	 * @param doc       文档
	 * @param dependDoc 参考文档
	 * @throws WTException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static void deleteDependencyLink(WTDocument doc,WTDocument dependDoc) throws WTException, RemoteException, InvocationTargetException{
		boolean accessEnforced = false;
		try{
			accessEnforced  = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			List<WTDocumentDependencyLink> result = getWTDocumentDependencyLink(doc);
			WTSet set = new WTHashSet();
			for(WTDocumentDependencyLink link : result){
				//roleA为文档,roleB为参考文档
	            WTDocument roleA = (WTDocument) link.getRoleAObject();
	            WTDocument roleB = (WTDocument) link.getRoleBObject();
	            if(roleB.getNumber().equals(dependDoc.getNumber())){
	            	set.add(link);
	            }
	        }
	        PersistenceServerHelper.manager.remove(set);
		}finally{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * 获取指定编号的参考文档
	 * @param doc
	 * @param docNumber
	 */
	public static WTDocument getDependsOnWTDocumentsByRefNumber(WTDocument doc,String docNumber) throws WTException, RemoteException, InvocationTargetException{
		boolean accessEnforced = false;
		try{
			accessEnforced  = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			//参考关系=ROLEB,所参考关系=ROLEA
			QueryResult qr = PersistenceHelper.manager.navigate(doc,WTDocumentDependencyLink.ROLE_BOBJECT_ROLE,
	                WTDocumentDependencyLink.class, false);
	        while (qr.hasMoreElements()) {
	            WTDocumentDependencyLink link = (WTDocumentDependencyLink) qr.nextElement();
	            WTDocument roleA = (WTDocument) link.getRoleAObject();
	            WTDocument roleB = (WTDocument) link.getRoleBObject();
	            if(roleB.getNumber().equals(docNumber)){
	            	return roleB;
	            }
	        }
	        return null;
		}finally{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
	}
	
	/**
	 * 修改文档对象的编号和名称
	 * @description
	 * @param doc
	 * @param newNumber
	 * @param newName
	 * @throws WTException
	 */
	public static void changeNumber(WTDocument doc,String newNumber,String newName) throws WTException{
		if (!RemoteMethodServer.ServerFlag) {
			Class[] clas = { WTDocument.class, String.class, String.class};
			Object[] obj = { doc, newNumber ,newName};
			try {
				RemoteMethodServer.getDefault().invoke("changeNumber", CLASSNAME, null, clas, obj);
			} catch (Exception e) {
				e.printStackTrace();
				throw new WTException("设置文档编号失败:" + newNumber);
			}
			return;
		}
		if((newNumber==null||newNumber.trim().isEmpty())&&(newName==null||newName.trim().isEmpty())){
			return;
		}
		String userId = SessionHelper.manager.getPrincipal().getName();
		SessionHelper.manager.setAdministrator();// 使用管理员权限
		try{
			WTDocumentMaster master = (WTDocumentMaster)doc.getMaster();
			WTDocumentMasterIdentity docIdentity = WTDocumentMasterIdentity.newWTDocumentMasterIdentity(master);
			if(newNumber!=null&&!newNumber.trim().isEmpty()){
				docIdentity.setNumber(newNumber);
			}
			if(newName!=null&&!newName.trim().isEmpty()){
				docIdentity.setName(newName);
			}
			Identified identified = IdentityHelper.service.changeIdentity(master, docIdentity);//修改名称
			PersistenceServerHelper.manager.update(identified);
			logger.debug("======设置文档编号成功："+doc.getDisplayIdentity());			
		}catch(Exception e){
			e.printStackTrace();
			throw new WTException("设置文档编号失败:" + newNumber);
		}finally{
			SessionHelper.manager.setPrincipal(userId);
		}
	}
	/**
	 * 判断文档是否为当前大版本的最新小版本
	 * @param part
	 * @return
	 */
	public static boolean isCurrentVersionLatest(WTDocument doc) throws WTException{
		long currentID  = doc.getPersistInfo().getObjectIdentifier().getId();
		WTDocument currentVersionLatestDoc = getDocument(doc.getNumber(),doc.getVersionIdentifier().getValue(),null);
		long latestID = currentVersionLatestDoc.getPersistInfo().getObjectIdentifier().getId();
		return currentID == latestID;
	}
	
	/**
	 * 判断文档是否为最新大版本的最新小版本
	 * @param part
	 * @return
	 */
	public static boolean isLatest(WTDocument doc) throws WTException{
		long currentID  				   = doc.getPersistInfo().getObjectIdentifier().getId();
		WTDocument latestDoc			    = getDocument(doc.getNumber(),null,null);
		long latestID = latestDoc.getPersistInfo().getObjectIdentifier().getId();
		return currentID == latestID;
	}
	
	
	/**
	 * 获取表示法里的PDF文件
	 * @param object
	 * @param tempPath
	 * @return
	 * @throws Exception
	 */
	public static File getPDFFileFromRepContent(WTDocument object,String tempPath)throws Exception {
		File pdfFile = null;
		
		logger.debug("----->>>>>即将获取文档可视化文件："+object.getDisplayIdentity()+".......");
		// 主内容文件名
		String primaryFileName  = DocUtil.getPrimaryContentFileName(object);
		
		StandardRepresentationService srs = StandardRepresentationService.newStandardRepresentationService();
		Representation representation = srs.getDefaultRepresentation(object);
		if (representation != null) {
			// 获取表示法名称
			String repreName = representation.getName();
			// 是否与主内容文件名相等
			logger.debug("----->>>>>获取可视化文件：representation.getName()：" + repreName);
			logger.debug("----->>>>>获取主内容文件：primaryFileName：" + primaryFileName);
			if(primaryFileName.startsWith(repreName)){
				logger.debug("可视化文件名与主内容文件名相符.......");
				representation = (Representation) ContentHelper.service.getContents(representation);
				Enumeration enuma = ContentHelper.getContentListAll(representation).elements();
				while (enuma != null && enuma.hasMoreElements()) {
					ContentItem item = (ContentItem) enuma.nextElement();
					if (item instanceof ApplicationData) {
						ApplicationData appData = (ApplicationData) item;
						String filename = appData.getFileName();
						String extention = Util.getExtension(filename);
						if (extention.equalsIgnoreCase("PDF")) {
							if (filename.indexOf("@") > -1) {
								filename = filename.replace("@", "\\u");
								filename = decodeUnicode(filename);
							}
							String fileAbsolutePath = tempPath + File.separator + filename;
							pdfFile = new File(fileAbsolutePath);
							ContentServerHelper.service.writeContentStream(appData,fileAbsolutePath);
							return pdfFile;
						}
					}
				}
			}else{
				logger.debug("可视化文件名与主内容文件名不相符.......");
			}
		}
		return null;
	}
	
	// unicode转中文
	private static String decodeUnicode(String dataStr)throws UnsupportedEncodingException {
		String decodeDataStr = java.net.URLDecoder.decode(dataStr, "UTF-8");
		return decodeDataStr;
	}
	
	
	/**
	 * 
	 * @param doc
	 * @param tempPath
	 * @return
	 * @throws Exception
	 * 
	 * 请使用getPDFFileFromRepContent方法,处理了文件名中的转码
	 */
	@Deprecated 
	public static File getPDFFileFromRep(WTDocument doc, String tempPath) throws Exception {
		logger.debug("----->>>>>即将获取文档可视化文件："+doc.getDisplayIdentity()+".......");
		String primaryFileName = getPrimaryContentFileName(doc);// 获取主内容名称

		File pdfFile = null;
		Representation representation = null;
		StandardRepresentationService srs;
		srs = StandardRepresentationService.newStandardRepresentationService();
		representation = srs.getDefaultRepresentation(doc);
		if (representation != null) {
			// 获取可视化表示法的名称
			String repreName = representation.getName();
			logger.debug("----->>>>>获取可视化文件：representation.getName()：" + repreName);
			logger.debug("----->>>>>获取主内容文件：primaryFileName：" + primaryFileName);
			// 主内容文件名 <大于等于> 可视化文件名
			if(primaryFileName.startsWith(repreName)) {
				logger.debug("可视化文件名与主内容文件名相符.......");
				representation = (Representation) ContentHelper.service.getContents(representation);
				// 获取可视化内容content
				Enumeration enuma = ContentHelper.getContentListAll(representation).elements();
				while (enuma != null && enuma.hasMoreElements()) {
					ContentItem item = (ContentItem) enuma.nextElement();
					if (item instanceof ApplicationData) {
						ApplicationData appData = (ApplicationData) item;
						String filename = appData.getFileName();
						if (filename.toLowerCase().endsWith(".pdf")) {
							String fileAbsolutePath = tempPath + File.separator + filename;
							ContentServerHelper.service.writeContentStream(appData, fileAbsolutePath);
							pdfFile = new File(fileAbsolutePath);
							return pdfFile;
						}
					}
				}
			}else{
				logger.debug("可视化文件名与主内容文件名不相符.......");
			}
		}
		return null;
	}

	
	/**
     * 获取文档主内容名称
     * @param theDocument
     * @return
     */
    public static String getPrimaryContentFileName(WTDocument theDocument) {
        String fileName = "";
        try{
            wt.content.ContentItem docContentItem = ContentHelper.service.getPrimary(theDocument);
            if(docContentItem != null) {
                ApplicationData applicationdataPrimary = (ApplicationData)docContentItem;
                fileName = applicationdataPrimary.getFileName();
                return fileName;
            }
        } catch(WTException wte) {
            wte.printStackTrace();
        } catch(java.beans.PropertyVetoException pve) {
            pve.printStackTrace();
        }
        return null;
    }

    
    /**
     * 判断当前登录用户是否为的创建者或修改者
     * @description
     * @param rc
     * @return
     * @throws WTException
     */
	public static boolean isCreatorOrModifier(RevisionControlled rc) throws WTException{
		WTPrincipal current = SessionHelper.manager.getPrincipal();
		return isCreatorOrModifier(rc,current);
	}
	
	/**
	 * 判断指定用户是否为创建者或修改者
	 * @description
	 * @param rc
	 * @param user
	 * @return
	 * @throws WTException
	 */
	public static boolean isCreatorOrModifier(RevisionControlled rc,WTPrincipal user) throws WTException{
		return (rc.getModifierName().equals(user.getName()) || rc.getCreatorName().equals(user.getName()));
	}
	
	/**
	 * 获取【文档】主内容名称
	 * @param doc
	 * @return
	 * @throws WTException
	 * @throws PropertyVetoException
	 */
	public static String getWTDocPrimaryFileName(WTDocument doc) throws WTException, PropertyVetoException{
        wt.content.ContentHolder contentholder = doc;
        contentholder = wt.content.ContentHelper.service.getContents(contentholder);
        if (contentholder instanceof FormatContentHolder) {
            ContentItem contentItem = wt.content.ContentHelper.getPrimary((FormatContentHolder) contentholder);
            if (contentItem != null && contentItem instanceof ApplicationData) {
            	ApplicationData app = (ApplicationData) contentItem;
            	return app.getFileName();
            }
        }
        return null;
    }
	
	/**
	 * 为对象添加主内容或附件--不删除原有附件内容
	 */
    public static FormatContentHolder addAppDataToDoc(FormatContentHolder holder, File file, boolean isPrimary) throws WTException, WTPropertyVetoException,
            java.beans.PropertyVetoException, IOException {
        holder = (FormatContentHolder) ContentHelper.service.getContents(holder);
        //如果是主内容先删除原有内容
        if(isPrimary){
	        ContentItem item = ContentHelper.getPrimary(holder);      
	        ContentServerHelper.service.deleteContent(holder, item);    
	        holder = (FormatContentHolder) PersistenceHelper.manager.refresh(holder);
        }
        ApplicationData applicationdata = ApplicationData.newApplicationData(holder);
        applicationdata.setFileName(file.getName());
        applicationdata.setUploadedFromPath(file.getParent());
        if (isPrimary)
           applicationdata.setRole(ContentRoleType.PRIMARY);
        wt.content.ContentHolder ch = wt.content.ContentHelper.service.getContents(holder);
        ContentServerHelper.service.updateContent(ch, applicationdata,file.getPath());
        return holder;
    }
    
    /**
     * 创建文档的参考关系
     * @param doc        文档
     * @param refDocList 参考文档集合
     * @throws WTException 
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static void createDependencyLinks(WTDocument doc, Set<WTDocument> refDocSet) throws WTException, RemoteException, InvocationTargetException{
    	WTSet linkSet = new WTHashSet();
    	if(null != doc && null != refDocSet && refDocSet.size() > 0){
    		Iterator it = refDocSet.iterator();
    		while(it.hasNext()){
    			WTDocument refDoc = (WTDocument) it.next();
    			WTDocument tempDoc = getDependsOnWTDocumentsByRefNumber(doc,refDoc.getNumber());
    			//如果参考关系已经存在，则不需重新添加
    			if(null == tempDoc){
    				WTDocumentDependencyLink link = WTDocumentDependencyLink.newWTDocumentDependencyLink(doc, refDoc);
        			linkSet.add(link);
    			}
    		}
    		PersistenceServerHelper.manager.insert(linkSet);
    	}
    	return;
    }
    
    /**
     * 为文档添加参考文档
     * @description
     * @param doc
     * @param dependDoc
     * @throws WTException
     * @throws RemoteException
     * @throws InvocationTargetException
     */
    public static void createDependencyLink(WTDocument doc, WTDocument dependDoc) throws WTException, RemoteException, InvocationTargetException{
		WTDocument tempDoc = getDependsOnWTDocumentsByRefNumber(doc,dependDoc.getNumber());
		//如果参考关系已经存在，则不需重新添加
		if(tempDoc == null){
			WTDocumentDependencyLink link = WTDocumentDependencyLink.newWTDocumentDependencyLink(doc, dependDoc);
			PersistenceServerHelper.manager.insert(link);
		}
    }
    
    /**
     * 修改文档软类型
     * @description
     * @param newType 新类型全路径如:wt.doc.WTDocument|com.wisplm.TechDoc
     * @param docOid
     * @throws WTException
     * @throws RemoteException
     * @throws WTPropertyVetoException
     * @throws ParseException
     */
	public static void changeDocType(WTDocument doc,String newType) throws WTException, RemoteException, WTPropertyVetoException, ParseException{
		QueryResult qr     = wt.vc.VersionControlHelper.service.allIterationsOf(doc.getMaster());
		/**
		 * 循环设置所有版本的类型
		 */
		while(qr.hasMoreElements()){
			doc = (WTDocument) qr.nextElement();
			String oldPartType = ClientTypedUtility.getExternalTypeIdentifier(doc).replace("WCTYPE|", "");
			if(!oldPartType.equals(newType)){
				TypeIdentifier typeIdentifier = FdnWTContainerHelper.toTypeIdentifier(newType);
				TypeHelper.setType(doc, typeIdentifier);
				PersistenceServerHelper.manager.update(doc);
			}
		}
	}
}
