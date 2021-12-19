package ext.wisplm.util;

import java.beans.PropertyVetoException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.common.TypeIdentifierHelper;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;
import ext.wisplm.util.EPMUtil.EPMDepencency;
import ext.wisplm.util.LWCUtil;

import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.access.AdHocAccessKey;
import wt.build.BuildHistory;
import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.FormatContentHolder;
import wt.epm.EPMApplicationType;
import wt.epm.EPMAuthoringAppType;
import wt.epm.EPMContextHelper;
import wt.epm.EPMDocConfigSpec;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentHelper;
import wt.epm.EPMDocumentMaster;
import wt.epm.EPMDocumentType;
import wt.epm.build.EPMBuildRule;
import wt.epm.structure.EPMDescribeLink;
import wt.epm.structure.EPMMemberLink;
import wt.epm.structure.EPMReferenceLink;
import wt.epm.structure.EPMStructureHelper;
import wt.epm.workspaces.CheckinOption;
import wt.epm.workspaces.EPMBaselineHelper;
import wt.epm.workspaces.EPMWorkspace;
import wt.epm.workspaces.EPMWorkspaceHelper;
import wt.epm.workspaces.WorkspaceCheckpoint;
import wt.fc.ObjectIdentifier;
import wt.fc.ObjectReference;
import wt.fc.PersistInfo;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.fc.collections.WTArrayList;
import wt.fc.collections.WTCollection;
import wt.fc.collections.WTKeyedHashMap;
import wt.fc.collections.WTKeyedMap;
import wt.fc.collections.WTValuedHashMap;
import wt.folder.Cabinet;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipal;
import wt.org.WTPrincipalReference;
import wt.part.PartDocHelper;
import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.part.WTPartConfigSpec;
import wt.part.WTPartStandardConfigSpec;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.vc.Iterated;
import wt.vc.Mastered;
import wt.vc.VersionControlHelper;
import wt.vc.baseline.BaselineMember;
import wt.vc.config.ConfigHelper;
import wt.vc.config.ConfigSpec;
import wt.vc.config.LatestConfigSpec;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.WorkInProgressState;

public class EPMUtil {

    private static final Logger logger = LoggerFactory.getLogger(EPMUtil.class);
    //EPM二维图类型
    private static String EPM_DOCTYPE_CADDRAWING = "CADDRAWING";
    /**
     * 
     * @param epmNumber EPM文档编号
     * @param epmName   EPM文档名称
     * @param cadName   EPM文件名称
     * @param epmDocumentType EPM文档类型,常用可选值CADASSEMBLY|CADCOMPONENT|CADDRAWING,分别表示CAD装配、CAD部件、绘图,其他可选值查看src/wt/epm/EPMDocumentTypeRB_zh_CN
     * @param epmAuthoringAppType EPM设计工具类型,可选值参考src/wt/epm/EPMAuthoringAppTypeRB_zh_CN
     * @param epmSubType EPM软类型,如果有,写全路径,如wt.epm.EPMDocument|com.pdm10.XXXEPM
     * @param attsValueMap 软属性key和value的Map
     * @param primaryContent 主内容文件路径
     * @param newFileName    主内容文件新文件名称
     * @param secondaryContents 附件文件路径集合
     * @param isNeedSetAttrs    是否需要在创建时设置软属性
     * @param wtContainer       要存储的容器对象
     * @param folderPath        容器文件夹全路径,/Default开头
     * @return
     * @throws Exception
     */
    public static EPMDocument createEPMDoc(String epmNumber, String epmName, String cadName,
            String epmDocumentType, String epmAuthoringAppType, String epmSubType,
            Map<String, Object> attsValueMap, String primaryContentPath, String newFileName,
            List<String> secondaryContents, boolean isNeedSetAttrs, WTContainer wtContainer,
            String folderPath) throws Exception {
        EPMDocument epmDoc = null;

        EPMContextHelper.setApplication(EPMApplicationType.getEPMApplicationTypeDefault());
        EPMDocumentType epmType = EPMDocumentType.toEPMDocumentType(epmDocumentType);//CADASSEMBLY,CADCOMPONENT,CADDRAWING

        EPMAuthoringAppType appType = EPMAuthoringAppType
                .toEPMAuthoringAppType(epmAuthoringAppType);

        Map<String, Object> objMBAs = new HashMap<String, Object>();
        objMBAs.put("Number", epmNumber);
        objMBAs.put("Name", epmName);

        epmDoc = EPMDocument.newEPMDocument(epmNumber, epmName, appType, epmType, cadName,
                QuantityUnit.EA);

        if (StringUtils.isBlank(epmSubType)) {
            //default EPM Type
            epmSubType = "wt.epm.EPMDocument";
        }
        TypeIdentifier id = TypeIdentifierHelper.getTypeIdentifier(epmSubType);
        epmDoc = (EPMDocument) CoreMetaUtility.setType(epmDoc, id);
        if (wtContainer == null) {
            throw new WTException("Get AssignedContainer Fail by [" + epmSubType + "]...");
        }
        wtContainer = (WTContainer) PersistenceHelper.manager.refresh(wtContainer);
        WTContainerRef wtContainerRef = WTContainerRef.newWTContainerRef(wtContainer);

        epmDoc.setDomainRef(wtContainer.getDefaultDomainReference());
        epmDoc.setContainerReference(wtContainerRef);

        if (StringUtils.isNotBlank(folderPath)) {
            Folder location = null;
            try {
                location = FolderHelper.service.getFolder(folderPath, wtContainerRef);
            } catch (Exception e) {
                logger.error("Get partFolder[" + folderPath + "] error...");
                location = null;
            }
            // If folder is not exist, create folder
            if (location == null) {
                location = FolderHelper.service.saveFolderPath(folderPath, wtContainerRef);
            }
            // set object to folder
            if (location != null) {
                WTValuedHashMap map = new WTValuedHashMap();
                map.put(epmDoc, location);
                FolderHelper.assignLocations(map);
            }
        }

        //Set Attribute
        if (isNeedSetAttrs && attsValueMap != null && attsValueMap.size() > 0) {
            LWCUtil.setValueBeforeStore(epmDoc, attsValueMap);
        }

        PersistenceHelper.manager.save(epmDoc);
        epmDoc = (EPMDocument) PersistenceHelper.manager.refresh(epmDoc);

        // set primary content
        if (StringUtils.isNotBlank(primaryContentPath)) {
            ContentUtil.addApplicationData(epmDoc, newFileName, primaryContentPath, "",
                    ContentRoleType.PRIMARY.toString());
        }

        // set secondary content
        if (secondaryContents != null && secondaryContents.size() > 0) {
            for (int i = 0; i < secondaryContents.size(); i++) {
                String secondaryContent = secondaryContents.get(i);
                if (StringUtils.isNoneBlank(secondaryContent)) {
                    ContentUtil.addApplicationData(epmDoc, "", secondaryContent, "",
                            ContentRoleType.SECONDARY.toString());
                }
            }
        }
        return epmDoc;
    }

    /**
     * 从三维数模获取最新版关联的WTPart
     * 
     * @param epm
     * @return
     * @throws Exception
     */
    public static WTPart getLatestBuidTarget(EPMDocument epm) throws Exception {
        WTPart part = null;
        if (epm != null) {
            EPMDocumentType epmDocumentType = epm.getDocType();
            if (EPMDocumentType.toEPMDocumentType(EPM_DOCTYPE_CADDRAWING).equals(epmDocumentType)) {
                throw new WTException("数模类型是绘图");
            }

            QuerySpec qs = new QuerySpec(WTPart.class, EPMBuildRule.class);
            qs.appendWhere(VersionControlHelper.getSearchCondition(WTPart.class, true),
                    new int[] { 0, 1 });
            QueryResult qr = PersistenceHelper.manager.navigate(epm, EPMBuildRule.BUILD_TARGET_ROLE,
                    qs, true);
            LatestConfigSpec latestCS = new LatestConfigSpec();
            qr = latestCS.process(qr);
            while (qr.hasMoreElements()) {
                part = (WTPart) qr.nextElement();
            }
        }
        return part;
    }

    /**
     * 清除EPM的MemberLink
     * @param epm
     * @throws WTException
     */
    public static void clearEpmMemberLink(EPMDocument epm) throws WTException {
        if (epm != null) {
            QueryResult qr = PersistenceHelper.manager.navigate(epm, EPMMemberLink.USES_ROLE,
                    EPMMemberLink.class, false);
            while (qr.hasMoreElements()) {
                EPMMemberLink emlink = (EPMMemberLink) qr.nextElement();
                PersistenceServerHelper.manager.remove(emlink);
            }
        }
    }
    
    /**
     * 通过EPMDocument获取子EPMDocument的装配坐标
     * @param epm
     */
    public static List<Map<String,String>> getChildEPMTransformMatrix4D(EPMDocument epm) throws WTException{
    	List<Map<String,String>> result = new ArrayList<Map<String,String>>();
        QueryResult qr = PersistenceServerHelper.manager.expand(epm, "uses",EPMMemberLink.class, false);
        while(qr.hasMoreElements()){
        	EPMMemberLink epmMemberLink = (EPMMemberLink) qr.nextElement();
        	Persistable child = (Persistable) epmMemberLink.getUses();
        	if(!(child instanceof EPMDocumentMaster)){
        		continue;
        	}
        	double amount = epmMemberLink.getQuantity().getAmount();
            String m0 = "1 0 0 0";
            String m1 = "0 1 0 0";
            String m2 = "0 0 1 0";
            String m3 = "0 0 0 1";
            
            EPMDocumentMaster epmDocumentMaster = (EPMDocumentMaster) child;
            String childNumber = epmDocumentMaster.getNumber();
            try{
                m0 = String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m00) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m01) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m02) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m03);
                m1 = String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m10) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m11) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m12) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m13);
                m2 = String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m20) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m21) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m22) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m23);
                m3 = String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m30) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m31) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m32) + " "
                        + String.valueOf((int) epmMemberLink.getTransform().toMatrix4d().m33);
            }catch(Exception e){
            	logger.error("导出TransformMatrix4D出错,使用默认值(1 0 0 0,0 1 0 0,0 0 1 0,0 0 0 1)");
            	logger.error("错误信息"+e.getLocalizedMessage()+",父EPM:" + epm.getNumber() + ",子EPM:" + childNumber);
            }
            Map<String,String> map = new HashMap<String,String>();
            map.put("m0", m0);
            map.put("m1", m1);
            map.put("m2", m2);
            map.put("m3", m3);
            map.put("childNumber", childNumber);
            map.put("amount", amount + "");
            result.add(map);
        }
        return result;
    }
    

    /**
     * 依据Part获取3-D EPM.
     * 
     * @param wtPart
     * @return
     * @throws Exception
     */
    public static EPMDocument getAssociatedEPM(WTPart wtPart) throws Exception {
        EPMDocument epm = null;
        if (wtPart != null) {
            WTKeyedMap wtKeyedMap = PartDocHelper.service
                    .getAssociatedEPMBuildRule(new WTArrayList(Arrays.asList(wtPart)));
            if (wtKeyedMap != null) {
                WTCollection collection = (WTCollection) wtKeyedMap.get(wtPart);
                if (collection != null) {
                    Iterator<Persistable> it = collection.persistableIterator();
                    if (it != null && it.hasNext()) {
                        Persistable per = it.next();
                        if (per instanceof EPMDocument) {
                            epm = (EPMDocument) per;
                        }
                    }
                }
            }
        }
        logger.debug("get 3-D EPM[" + epm + "] from WTPart[" + wtPart.getDisplayIdentifier() + "]");
        return epm;
    }

    /**
     * 依据二维图纸获取关联的三维数模
     * 
     * @param epm2D
     * @return
     * @throws WTException
     */
    public static EPMDocumentMaster getAssociate3DFrom2D(EPMDocument epm2D) throws WTException {
        EPMDocumentMaster epm3dMaster = null;
        if (epm2D != null) {
            QueryResult qr = PersistenceHelper.manager.navigate(epm2D,
                    EPMReferenceLink.REFERENCES_ROLE, EPMReferenceLink.class, true);
            if (qr != null) {
                if (qr.hasMoreElements()) {
                    epm3dMaster = (EPMDocumentMaster) qr.nextElement();
                }
            }
        }
        return epm3dMaster;
    }

    /**
     * 依据三维数模获取关联的二维图纸
     * 取关联二维图最新版本，若检出，则返回检出版本
     * 
     * @param epm2D
     * @return
     * @throws WTException
     */
    public static Set<EPMDocument> getAssociate2DFrom3D(EPMDocument epm3D) throws WTException {
        Set<EPMDocument> epm2ds = new HashSet<EPMDocument>();
        if (epm3D != null) {
            QueryResult epmRefByQr = PersistenceHelper.manager.navigate(
                    (EPMDocumentMaster) epm3D.getMaster(), EPMReferenceLink.REFERENCED_BY_ROLE,
                    EPMReferenceLink.class, true);
            if (epmRefByQr != null) {
                epmRefByQr = new LatestConfigSpec().process(epmRefByQr);
                epm2ds.addAll(epmRefByQr.getObjectVectorIfc().getVector());
            }
        }
        return epm2ds;
    }

    /**
     * 判断EPM是否为二维图
     * @param epmDoc
     * @return
     */
    public static boolean isCADDrawing(EPMDocument epmDoc) {
        return EPMDocumentType.toEPMDocumentType(EPM_DOCTYPE_CADDRAWING).equals(epmDoc.getDocType());
    }

    /**
     * 基于EPMMaster Oid 和 ConfigSpec获取EPMDocument
     * 
     * @param epmMasterOid
     * @param spec
     * @return
     * @throws WTRuntimeException
     * @throws WTException
     */
    public static List<EPMDocument> getEPMDocumentByMasterOid(String epmMasterOid, ConfigSpec spec)
            throws WTRuntimeException, WTException {
        List<EPMDocument> list = new ArrayList<EPMDocument>();
        EPMDocumentMaster master = null;
        try {
            master = (EPMDocumentMaster) new ReferenceFactory().getReference(epmMasterOid)
                    .getObject();
        } catch (Exception e) {
            logger.error("getEPMDocumentByMasterOid error...", e);
            return list;
        }
        if (spec == null) {
            spec = new LatestConfigSpec();
        }
        QueryResult qr = ConfigHelper.service.filteredIterationsOf(master, spec);
        while (qr.hasMoreElements()) {
            EPMDocument epm = (EPMDocument) qr.nextElement();
            list.add(epm);
        }
        return list;
    }
    
    /**
     * 依据Part查询关联的三维模型
     * 
     * @param wtPart
     * @return
     */
    public static EPMDocument getAssociateEPMByPart(WTPart wtPart) {
        EPMDocument epm = null;
        if (wtPart != null) {
            try {
                WTKeyedMap wtKeyedMap = PartDocHelper.service
                        .getAssociatedEPMBuildRule(new WTArrayList(Arrays.asList(wtPart)));
                logger.debug("wtKeyedMap : " + wtKeyedMap);
                if (wtKeyedMap != null) {
                    WTCollection collection = (WTCollection) wtKeyedMap.get(wtPart);
                    if (collection != null) {
                        Iterator<Persistable> it = collection.persistableIterator();
                        if (it != null && it.hasNext()) {
                            Persistable per = it.next();
                            if (per instanceof EPMDocument) {
                                epm = (EPMDocument) per;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("getAssociatePartBy3D error...", e);
            }
        }
        logger.debug("getAssociateEPMByPart by [" + wtPart + "] " + epm);
        return epm;
    }
    
    public static List<ApplicationData> getAttachments(EPMDocument epm) throws WTException, PropertyVetoException {
        List<ApplicationData> result = new ArrayList<ApplicationData>();
        FormatContentHolder holder = (FormatContentHolder) ContentHelper.service.getContents(epm);
        QueryResult existQr = ContentHelper.service.getContentsByRole(holder, ContentRoleType.SECONDARY);
        if (existQr != null && existQr.size() > 0) {
            while (existQr.hasMoreElements()) {
                ContentItem existContentItem = (ContentItem) existQr.nextElement();
                ApplicationData appData = (ApplicationData) existContentItem;
                    result.add(appData);
            }
        }
        return result;
    }
    
    
    /** validReferenceType. */
    private static String validReferenceType = "DRAWING";
    
    private static final String CLASSNAME = EPMUtil.class.getName();
    /**
     * Associate owner - creates EPMBuildRule between parameters. Workspace will
     * be populated using REQUIRED dependency
     * 
     * @param epmDoc
     *            EPMDocument to associate
     * @param part
     *            WTPart used for association
     * @param workspace
     *            Workspace where both Part and EPMDocument will be checked out
     * @param checkinComment
     *            If is null or empty ("") no checkin would be performed.
     * 
     * @return EPMBuildRule newly created link
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static EPMBuildRule associateOwner(EPMDocument epmDoc, WTPart part, EPMWorkspace workspace, String checkinComment) throws WTPropertyVetoException, WTException {
        EPMDocument docToAssociate = null;
        WTPart partToAssociate = null;
        if (WorkInProgressState.CHECKED_IN.toString().equals(WorkInProgressHelper.getState(epmDoc).toString())) {
            docToAssociate = checkout(workspace, epmDoc);
        } else {
            docToAssociate = (EPMDocument) WorkInProgressHelper.service.workingCopyOf(epmDoc);
        }
        if (WorkInProgressState.CHECKED_IN.toString().equals(WorkInProgressHelper.getState(part).toString())) {
            Collection<WTPart> col = new ArrayList<WTPart>();
            col.add(part);
            Collection checkedoutObjects = checkout(workspace, col);
            for (Iterator iterator = checkedoutObjects.iterator(); iterator.hasNext();) {
                Object object = iterator.next();
                if (object instanceof ObjectReference) {
                    if (((ObjectReference) object).getObject() instanceof WTPart) {
                        partToAssociate = (WTPart) ((ObjectReference) object).getObject();
                        break;
                    }
                }
            }
        } else {
            partToAssociate = (WTPart) WorkInProgressHelper.service.workingCopyOf(part);
        }
        EPMBuildRule epmBuildRule = EPMBuildRule.newEPMBuildRule(docToAssociate, partToAssociate);
        PersistenceHelper.manager.save(epmBuildRule);
        if (checkinComment != null && !"".equals(checkinComment)) {
            Collection<Persistable> checkedOutObjects = new ArrayList<Persistable>();
            checkedOutObjects.add(PersistenceHelper.manager.refresh(docToAssociate));
            checkedOutObjects.add(PersistenceHelper.manager.refresh(partToAssociate));
            checkin(workspace, checkedOutObjects, checkinComment);
        }
        return (EPMBuildRule) PersistenceHelper.manager.refresh(epmBuildRule);
    }

    /**
     * Gets latest iterations of EPMDocument models associated with drawing.
     * 
     * @param epmDocument
     *            drawing EPMDocument
     * 
     * @return latest iterations EPMDocument models associated with drawing
     * 
     * @throws WTException
     *             when occurs
     */
    public static Collection<EPMDocument> getModelsForDrawing(EPMDocument epmDocument) throws WTException {
        EPMDocument result = null;
        List<EPMDocument> uniqueReferencedDocs = new ArrayList<EPMDocument>();
        QueryResult referencedLinks = EPMStructureHelper.service.navigateReferences(epmDocument, null, false);
        while (referencedLinks.hasMoreElements()) {
            EPMReferenceLink epmLink = (EPMReferenceLink) referencedLinks.nextElement();
            EPMDocumentMaster referencedDoc = (EPMDocumentMaster) epmLink.getReferences();
            if (epmLink.getReferenceType().toString().equals(EPMUtil.validReferenceType)) {
                result = (EPMDocument) getLatestVersion(referencedDoc);
                if (!uniqueReferencedDocs.contains(result)) {
                    uniqueReferencedDocs.add(result);
                }
            }
        }
        return uniqueReferencedDocs;
    }

    /**
     * Protected constructor.
     */
    protected EPMUtil() {

    }

    /** Identifies CADDrawing as a type of EPMDocument. */
    private static String epmDrawingType = "CADDRAWING";

    /** Identifies Drawing Format as a type of EPMDocument. */
    private static String epmDrawingFormatTypeName = "FORMAT";

    /**
     * Creates new EPMWorkspace. Principal is retrieved from session
     * 
     * @param name
     *            name of workspace to create
     * @param container
     *            container in which workspace will be created
     * 
     * @return newly created workspace
     * 
     * @throws WTException
     *             when occurs
     * @throws WTPropertyVetoException
     *             when occurs
     */
    public static EPMWorkspace createWorkspace(String name, WTContainer container) throws WTException, WTPropertyVetoException {

        return createWorkspace(name, container, SessionHelper.manager.getPrincipal(), null, null);
    }

    /**
     * Creates new EPMWorkspace.
     * 
     * @param name
     *            name of workspace to create
     * @param container
     *            container in which workspace will be created
     * @param principal
     *            principal for who workspace will be created
     * 
     * @return newly created workspace
     * 
     * @throws WTException
     *             when occurs
     * @throws WTPropertyVetoException
     *             when occurs
     */
    public static EPMWorkspace createWorkspace(String name, WTContainer container, WTPrincipal principal) throws WTException, WTPropertyVetoException {

        return createWorkspace(name, container, principal, null, null);
    }

    /**
     * Creates new EPMWorkspace.
     * 
     * @param name
     *            name of workspace to create
     * @param container
     *            container in which workspace will be created
     * @param principal
     *            principal for who workspace will be created
     * @param dcs
     *            EPMDocConfigSpec which will be used by workspace
     * @param pcs
     *            WTPartConfigSpec which will be used by workspace
     * 
     * @return newly created workspace
     * 
     * @throws WTException
     *             when occurs
     * @throws WTPropertyVetoException
     *             when occurs
     */
    public static EPMWorkspace createWorkspace(String name, WTContainer container, WTPrincipal principal, EPMDocConfigSpec dcs, WTPartConfigSpec pcs) throws WTException, WTPropertyVetoException {

        Cabinet cabinet = FolderHelper.service.getPersonalCabinet(principal);

        StringBuffer builder = new StringBuffer();
        builder.append('/');
        builder.append(cabinet.getName());
        builder.append('/');
        builder.append(name);

        Folder folder = FolderHelper.service.saveFolderPath(builder.toString(), WTContainerHelper.service.getExchangeRef());
        return createWorkspace(name, container, principal, dcs, pcs, folder);
    }

    /**
     * Creates new EPMWorkspace.
     * 
     * @param name
     *            name of workspace to create
     * @param container
     *            container in which workspace will be created
     * @param principal
     *            principal for who workspace will be created
     * @param dcs
     *            EPMDocConfigSpec which will be used by workspace
     * @param pcs
     *            WTPartConfigSpec which will be used by workspace
     * @param folder
     *            folder which will be used by workspace
     * 
     * @return newly created workspace
     * 
     * @throws WTException
     *             when occurs
     * @throws WTPropertyVetoException
     *             when occurs
     */
    public static EPMWorkspace createWorkspace(String name, WTContainer container, WTPrincipal principal, EPMDocConfigSpec dcs, WTPartConfigSpec pcs, Folder folder) throws WTException, WTPropertyVetoException {

        if (pcs == null) {
            WTPartStandardConfigSpec spcs = WTPartStandardConfigSpec.newWTPartStandardConfigSpec();
            pcs = WTPartConfigSpec.newWTPartConfigSpec(null, null, spcs);
        } else {
            pcs = (WTPartConfigSpec) pcs.duplicate();
        }

        if (dcs == null) {
            dcs = EPMDocConfigSpec.newEPMDocConfigSpec();
        } else {
            dcs = (EPMDocConfigSpec) dcs.duplicate();
        }

        EPMWorkspace workspace = EPMWorkspace.newEPMWorkspace(name, principal, folder, pcs, dcs, container);
        workspace = (EPMWorkspace) PersistenceHelper.manager.save(workspace);
        return workspace;
    }

    /**
     * Delete an EPMWorkspace from Windchill.
     * 
     * @param workspace
     *            EPMWorkspace
     * 
     * @throws WTException
     *             when occurs
     */
    public static void deleteWorkspace(EPMWorkspace workspace) throws WTException {

        PersistenceHelper.manager.delete(workspace);
    }

    /**
     * Gets latest iterations of drawings associated with EPMDocument.
     * 
     * @param epmDocument
     *            model EPMDocument
     * 
     * @return latest iterations of EPMDocument drawings associated with
     *         epmDocument
     * 
     * @throws WTException
     *             when occurs
     */
    public static Collection<EPMDocument> getDrawings(EPMDocument epmDocument) throws WTException {

        List<EPMDocument> uniqueReferencedDocs = new ArrayList<EPMDocument>();
        QueryResult referencedObjects = EPMStructureHelper.service.navigateReferencedBy((EPMDocumentMaster) epmDocument.getMaster(), null, true);
        while (referencedObjects.hasMoreElements()) {
            Object refObject = referencedObjects.nextElement();
            if (refObject instanceof EPMDocument) {
                EPMDocument referencedDoc = (EPMDocument) refObject;
                if (referencedDoc.getDocType().toString().equals(epmDrawingType)) {
                    EPMDocument drawing = (EPMDocument) VersionControlHelper.getLatestIteration(referencedDoc, false);
                    drawing = (EPMDocument) PersistenceHelper.manager.refresh(drawing);
                    if (!uniqueReferencedDocs.contains(drawing)) {
                        uniqueReferencedDocs.add(drawing);
                    }
                }
            }
        }
        return uniqueReferencedDocs;
    }

    /**
     * Gets latest iteration of Drawing Format associated with given Drawing.
     * 
     * @param drawing
     *            drawing EPMDocument
     * 
     * @return drawing format EPMDocument
     * 
     * @throws WTException
     *             when occurs
     */
    public static EPMDocument getDrawingFormat(EPMDocument drawing) throws WTException {

        QueryResult referencedObjects = EPMStructureHelper.service.navigateReferences(drawing, null, true);
        while (referencedObjects.hasMoreElements()) {
            Object refObject = referencedObjects.nextElement();
            if (refObject instanceof EPMDocumentMaster) {
                EPMDocumentMaster formatMaster = (EPMDocumentMaster) refObject;
                EPMDocument format = (EPMDocument) getLatestVersion(formatMaster);
                if (epmDrawingFormatTypeName.equals(format.getDocType().toString())) {
                    return format;
                }
            }
        }
        return null;
    }

    /**
     * Navigate the build links to find actively linked WTParts to the CAD
     * document and return a collection with the part(s). If no WTParts are
     * found, null is returned
     * 
     * @param source
     *            EPMDocument
     * 
     * @return Collection with the actively linked WTParts
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static Collection getRelatedParts(EPMDocument source) throws WTException {

        QueryResult result = null;
        if (!VersionControlHelper.isLatestIteration(source)) {
            result = PersistenceHelper.manager.navigate(source, BuildHistory.BUILT_ROLE, BuildHistory.class);
        } else {
            result = PersistenceHelper.manager.navigate(source, EPMBuildRule.BUILD_TARGET_ROLE, EPMBuildRule.class);
        }
        if (result != null) {
            return result.getObjectVectorIfc().getVector();
        } else {
            return new ArrayList();
        }
    }
    
    /**
     * Navigate the build links to find actively linked EPMDocument to the part
     * document and return a collection with the EPMDocument(s). If no EPMDocuments are
     * found, null is returned
     * 
     * @param source
     *            WTPart
     * 
     * @return Collection with the actively linked EPMDocument
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static EPMDocument getRelatedEPMDocuments(WTPart source) throws WTException {

        QueryResult result = null;
        if (!VersionControlHelper.isLatestIteration(source)) {
            result = PersistenceHelper.manager.navigate(source, BuildHistory.BUILT_BY_ROLE, BuildHistory.class);
        } else {
            result = PersistenceHelper.manager.navigate(source, EPMBuildRule.BUILD_SOURCE_ROLE, EPMBuildRule.class);
        }
        while(result.hasMoreElements()){
        	EPMDocument doc = (EPMDocument) result.nextElement();
        	return doc;
        }
       return null;
    }

    /**
     * Checkout. Populates workspace using REQUIRED dependency
     * 
     * @param workspace
     *            workspace where EPMDocument will be checked out
     * @param epmDoc
     *            EPMDocument to checkout
     * 
     * @return working copy of EPMDocument
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    public static EPMDocument checkout(EPMWorkspace workspace, EPMDocument epmDoc) throws WTPropertyVetoException, WTException {

        Collection<EPMDocument> col = new ArrayList<EPMDocument>();
        col.add(epmDoc);
        checkout(workspace, col);
        return (EPMDocument) WorkInProgressHelper.service.workingCopyOf(epmDoc);
    }

    /**
     * Checkout. Populates workspace using REQUIRED dependency
     * 
     * @param workspace
     *            workspace where EPMDocument will be checked out
     * @param objectsToCheckout
     *            collection of objects to checkout
     * 
     * @return working copy of EPMDocument (or null in case of failure)
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static Collection checkout(EPMWorkspace workspace, Collection objectsToCheckout) throws WTPropertyVetoException, WTException {

        return checkout(workspace, objectsToCheckout, EPMUtil.EPMDepencency.REQUIRED);
    }

    /**
     * Checkout. Populates workspace using dependency given as a parameter
     * 
     * @param workspace
     *            workspace where EPMDocument will be checked out
     * @param epmDoc
     *            EPMDocument to checkout
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * 
     * @return working copy of EPMDocument (or null in case of failure)
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    public static EPMDocument checkout(EPMWorkspace workspace, EPMDocument epmDoc, EPMUtil.EPMDepencency dependency) throws WTException, WTPropertyVetoException {

        Collection<EPMDocument> col = new ArrayList<EPMDocument>();
        col.add(epmDoc);
        checkout(workspace, col, dependency);
        return (EPMDocument) WorkInProgressHelper.service.workingCopyOf(epmDoc);
    }

    /**
     * Checkout. Populates workspace using dependency given as a parameter
     * 
     * @param workspace
     *            workspace where EPMDocument will be checked out
     * @param objectsToCheckout
     *            collection of objects to checkout
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * 
     * @return Collection of ObjectReference objects (or null in case of
     *         failure)
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Collection checkout(EPMWorkspace workspace, Collection objectsToCheckout, EPMUtil.EPMDepencency dependency) throws WTException, WTPropertyVetoException {

        if (EPMUtil.EPMDepencency.ALL.equals(dependency)) {
            EPMBaselineHelper.service.populateAll(workspace, new Vector(objectsToCheckout));
        } else if (EPMUtil.EPMDepencency.REQUIRED.equals(dependency)) {
            EPMBaselineHelper.service.populateRequired(workspace, new Vector(objectsToCheckout));
        }
        return EPMWorkspaceHelper.manager.checkout(workspace, new WTArrayList(objectsToCheckout)).values();
    }

    /**
     * Checkin. No objects are added to workspace before checkin
     * 
     * @param workspace
     *            workspace where EPMDocument is checked out
     * @param epmDoc
     *            EPMDocument to checkin
     * @param checkinComment
     *            checkin comment
     * 
     * @return Checked in EPMDocument (or null in case of failure)
     * 
     * @throws WTException
     *             when occurs
     */
    public static EPMDocument checkin(EPMWorkspace workspace, EPMDocument epmDoc, String checkinComment) throws WTException {

        return checkin(workspace, epmDoc, checkinComment, EPMUtil.EPMDepencency.NONE);
    }

    /**
     * Checkin. Workspace will be populated using dependency given as a
     * parameter
     * 
     * @param workspace
     *            workspace where EPMDocument is checked out
     * @param epmDoc
     *            EPMDocument to checkin
     * @param checkinComment
     *            checkin comment
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * 
     * @return Checked in EPMDocument (or null in case of failure)
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static EPMDocument checkin(EPMWorkspace workspace, EPMDocument epmDoc, String checkinComment, EPMUtil.EPMDepencency dependency) throws WTException {

        Collection<EPMDocument> checkedOutObjects = new ArrayList<EPMDocument>();
        checkedOutObjects.add(epmDoc);
        Collection checkedInObjects = checkin(workspace, checkedOutObjects, checkinComment, dependency);
        for (Iterator iterator = checkedInObjects.iterator(); iterator.hasNext();) {
            Object object = (Object) iterator.next();
            if (object instanceof EPMDocument) {
                return (EPMDocument) object;
            }
            if (object instanceof ObjectReference) {
                return (EPMDocument) ((ObjectReference) object).getObject();
            }
        }
        return null;
    }

    /**
     * Checkin. No objects are added to workspace before checkin
     * 
     * @param workspace
     *            workspace where EPMDocument is checked out
     * @param checkinComment
     *            checkin comment
     * @param checkedOutObjects
     *            Collection of objects to checkin
     * 
     * @return Collection of ObjectReference objects (or null in case of
     *         failure)
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static Collection checkin(EPMWorkspace workspace, Collection checkedOutObjects, String checkinComment) throws WTException {

        return checkin(workspace, checkedOutObjects, checkinComment, EPMUtil.EPMDepencency.NONE);
    }

    /**
     * Checkin. Workspace will be populated using dependency given as a
     * parameter
     * 
     * @param workspace
     *            workspace where EPMDocument is checked out
     * @param checkinComment
     *            checkin comment
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * @param checkedOutObjects
     *            the checked out objects
     * 
     * @return Collection of ObjectReference objects (or null in case of
     *         failure)
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Collection checkin(EPMWorkspace workspace, Collection checkedOutObjects, String checkinComment, EPMUtil.EPMDepencency dependency) throws WTException {

        Collection objects = new ArrayList();
        for (Iterator iterator = checkedOutObjects.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            if (object instanceof ObjectReference) {
                objects.add(((ObjectReference) object).getObject());
            } else {
                objects.add(object);
            }
        }
        if (EPMUtil.EPMDepencency.ALL.equals(dependency)) {
            EPMBaselineHelper.service.populateAll(workspace, new Vector(objects));
        } else if (EPMUtil.EPMDepencency.REQUIRED.equals(dependency)) {
            EPMBaselineHelper.service.populateRequired(workspace, new Vector(objects));
        }
        WTKeyedMap objectsToCheckin = new WTKeyedHashMap();
        for (Iterator iterator = checkedOutObjects.iterator(); iterator.hasNext();) {
            objectsToCheckin.put(iterator.next(), new CheckinOption(checkinComment));
        }
        return EPMWorkspaceHelper.manager.checkin(workspace, objectsToCheckin);
    }

    /**
     * Adds EPMDocument to workspace. Workspace is populated using REQUIRED
     * dependency
     * 
     * @param workspace
     *            workspace to which EPMDocument will be added
     * @param docToAdd
     *            EPMDocument which will be added to workspace
     * 
     * @throws WTException
     *             when occurs
     */
    public static void addToWorkspace(EPMWorkspace workspace, EPMDocument docToAdd) throws WTException {

        addToWorkspace(workspace, docToAdd, EPMUtil.EPMDepencency.REQUIRED);
    }

    /**
     * Adds EPMDocument to workspace. Workspace is populated using dependency
     * given as a parameter
     * 
     * @param workspace
     *            workspace to which EPMDocument will be added
     * @param docToAdd
     *            EPMDocument which will be added to workspace
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * 
     * @throws WTException
     *             when occurs
     */
    public static void addToWorkspace(EPMWorkspace workspace, EPMDocument docToAdd, EPMUtil.EPMDepencency dependency) throws WTException {

        Collection<EPMDocument> col = new ArrayList<EPMDocument>();
        col.add(docToAdd);
        addToWorkspace(workspace, col, dependency);
    }

    /**
     * Adds objects to workspace. Workspace is populated using REQUIRED
     * dependency
     * 
     * @param workspace
     *            workspace to which EPMDocument will be added
     * @param objectsToAdd
     *            Collection of objects to be added to workspace
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static void addToWorkspace(EPMWorkspace workspace, Collection objectsToAdd) throws WTException {

        addToWorkspace(workspace, objectsToAdd, EPMUtil.EPMDepencency.REQUIRED);
    }

    /**
     * Adds objects to workspace. Workspace is populated using dependency given
     * as a parameter
     * 
     * @param workspace
     *            workspace to which EPMDocument will be added
     * @param dependency
     *            dependency used to populate workspace @see
     *            {@link EPMDepencency}
     * @param objectsToAdd
     *            the objects to add
     * 
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void addToWorkspace(EPMWorkspace workspace, Collection objectsToAdd, EPMUtil.EPMDepencency dependency) throws WTException {

        if (EPMUtil.EPMDepencency.ALL.equals(dependency)) {
            EPMBaselineHelper.service.populateAll(workspace, new Vector(objectsToAdd));
        } else if (EPMUtil.EPMDepencency.REQUIRED.equals(dependency)) {
            EPMBaselineHelper.service.populateRequired(workspace, new Vector(objectsToAdd));
        }
        EPMWorkspaceHelper.manager.addToWorkspace(workspace, new WTArrayList(objectsToAdd));
    }

    /**
     * Creates new EPMDocument.
     * 
     * @param number
     *            Number of new EPMDocument
     * @param name
     *            Name of new EPMDocument
     * @param epmAuthoringAppType
     *            Authoring application type for new EPMDocument
     * @param epmDocumentType
     *            Type of new EPMDocument
     * @param fileName
     *            Filename for new EPMDocument
     * @param applicationType
     *            Application type
     * @param save
     *            Should new EPMDocument be saved or not?
     * 
     * @return Created EPMDocument
     */
    public static EPMDocument createEPMDocument(String number, String name, EPMAuthoringAppType epmAuthoringAppType, EPMDocumentType epmDocumentType, String fileName, EPMApplicationType applicationType, boolean save) {
        try {
            EPMContextHelper.setApplication(applicationType);
            EPMDocument epmDocument = EPMDocument.newEPMDocument(number, name, epmAuthoringAppType, epmDocumentType, fileName, QuantityUnit.getQuantityUnitDefault());
            if (save) {
                PersistenceHelper.manager.save(epmDocument);
            }
            return epmDocument;

        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * Associate content - creates EPMDescribe link between parameters.
     * Workspace will be populated using REQUIRED dependency
     * 
     * @param epmDoc
     *            EPMDocument to associate
     * @param part
     *            WTPart used for association
     * @param workspace
     *            Workspace where both Part and EPMDocument will be checked out
     * @param checkinComment
     *            If is null or empty ("") no checkin would be performed.
     * 
     * @return EPMDescribeLink newly created link
     * 
     * @throws WTPropertyVetoException
     *             when occurs
     * @throws WTException
     *             when occurs
     */
    @SuppressWarnings("rawtypes")
    public static EPMDescribeLink associateContent(EPMDocument epmDoc, WTPart part, EPMWorkspace workspace, String checkinComment) throws WTPropertyVetoException, WTException {

        EPMDocument docToAssociate = null;
        WTPart partToAssociate = null;

        if (WorkInProgressState.CHECKED_IN.toString().equals(WorkInProgressHelper.getState(epmDoc).toString())) {
            docToAssociate = checkout(workspace, epmDoc);
        } else {
            docToAssociate = (EPMDocument) WorkInProgressHelper.service.workingCopyOf(epmDoc);
        }

        if (WorkInProgressState.CHECKED_IN.toString().equals(WorkInProgressHelper.getState(part).toString())) {
            Collection<WTPart> col = new ArrayList<WTPart>();
            col.add(part);
            Collection checkedoutObjects = checkout(workspace, col);
            for (Iterator iterator = checkedoutObjects.iterator(); iterator.hasNext();) {
                Object object = iterator.next();
                if (object instanceof ObjectReference) {
                    if (((ObjectReference) object).getObject() instanceof WTPart) {
                        partToAssociate = (WTPart) ((ObjectReference) object).getObject();
                        break;
                    }
                }
            }
        } else {
            partToAssociate = (WTPart) WorkInProgressHelper.service.workingCopyOf(part);
        }

        EPMDescribeLink epmDescribeLink = EPMDescribeLink.newEPMDescribeLink(partToAssociate, docToAssociate);
        PersistenceHelper.manager.save(epmDescribeLink);

        if (checkinComment != null && !"".equals(checkinComment)) {
            Collection<Persistable> checkedOutObjects = new ArrayList<Persistable>();
            checkedOutObjects.add(PersistenceHelper.manager.refresh(docToAssociate));
            checkedOutObjects.add(PersistenceHelper.manager.refresh(partToAssociate));
            checkin(workspace, checkedOutObjects, checkinComment);
        }
        return (EPMDescribeLink) PersistenceHelper.manager.refresh(epmDescribeLink);
    }

    /**
     * The method returns a set of workspaces in which provided EPMDocument is
     * present.
     * 
     * @param epmDocument
     *            the EPMDocument
     * 
     * @return the sets of EPMWorkspace objects
     * 
     * @throws WTException
     *             the WT exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Set findPresentInWorkspaces(EPMDocument epmDocument) throws WTException {
        // Method navigates through BaselineMember link and WorkspaceCheckpoint
        // link to get set of EPMWorkspace containing given EPMDocument
        Set result = new HashSet();

        QuerySpec querySpec = new QuerySpec();
        int epmWorkspaceIdx = querySpec.appendClassList(EPMWorkspace.class, true);
        int baselineMemberIdx = querySpec.appendClassList(BaselineMember.class, false);
        int workspaceCheckpointIdx = querySpec.appendClassList(WorkspaceCheckpoint.class, false);

        long epmDocumentId = epmDocument.getPersistInfo().getObjectIdentifier().getId();

        querySpec.appendWhere(new SearchCondition(BaselineMember.class, BaselineMember.ROLE_BOBJECT_REF + "." + ObjectReference.KEY + "." + ObjectIdentifier.ID, SearchCondition.EQUAL, epmDocumentId), new int[] { baselineMemberIdx });
        querySpec.appendAnd();
        querySpec.appendWhere(new SearchCondition(WorkspaceCheckpoint.class, WorkspaceCheckpoint.ROLE_BOBJECT_REF + "." + ObjectReference.KEY + "." + ObjectIdentifier.ID, BaselineMember.class, BaselineMember.ROLE_AOBJECT_REF + "." + ObjectReference.KEY + "." + ObjectIdentifier.ID), new int[] { workspaceCheckpointIdx, baselineMemberIdx });
        querySpec.appendAnd();
        querySpec.appendWhere(new SearchCondition(EPMWorkspace.class, EPMWorkspace.PERSIST_INFO + "." + PersistInfo.OBJECT_IDENTIFIER + "." + ObjectIdentifier.ID, WorkspaceCheckpoint.class, WorkspaceCheckpoint.ROLE_AOBJECT_REF + "." + ObjectReference.KEY + "." + ObjectIdentifier.ID), new int[] { epmWorkspaceIdx, workspaceCheckpointIdx });

        QueryResult queryResult = PersistenceHelper.manager.find((StatementSpec) querySpec);

        while (queryResult.hasMoreElements()) {
            Object[] resultArray = (Object[]) queryResult.nextElement();
            EPMWorkspace workspace = (EPMWorkspace) resultArray[0];
            result.add(workspace);
        }

        return result;
    }

    /**
     * Gets the latest version & iteration from Mastered.
     * 
     * @param mastered
     *            the mastered
     * 
     * @return latest version of Mastered
     * 
     * @throws WTException
     *             the WTException
     */
    public static Iterated getLatestVersion(Mastered mastered) throws WTException {
        QueryResult queryResult = VersionControlHelper.service.allVersionsOf(mastered);
        if (queryResult.hasMoreElements()) {
            return (Iterated) queryResult.nextElement();
        }
        return null;
    }

    /**
     * Class EPMDepencency used for choosing desired dependencies while
     * populating EPMWorkspace.
     */
    public static class EPMDepencency {

        /** The dep. */
        private String dep;

        /** NONE dependency - no objects would be added to workspace. */
        public static final EPMUtil.EPMDepencency NONE = new EPMUtil.EPMDepencency("NONE");

        /**
         * REQUIRED dependency - workspace will be populated with objects
         * required to open main object in Pro/E.
         */
        public static final EPMUtil.EPMDepencency REQUIRED = new EPMUtil.EPMDepencency("REQUIRED");

        /**
         * ALL dependency - workspace will be populated with all dependencies of
         * main object.
         */
        public static final EPMUtil.EPMDepencency ALL = new EPMUtil.EPMDepencency("ALL");

        /**
         * private constructor.
         * 
         * @param dependency
         *            the dependency
         */
        private EPMDepencency(String dependency) {

            this.dep = dependency;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        public int hashCode() {

            final int prime = 31;
            int result = 1;
            int hashCode = 0;
            if (dep != null) {
                hashCode = dep.hashCode();
            }
            result = prime * result + hashCode;
            return result;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj) {

            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof EPMDepencency)) {
                return false;
            }
            EPMDepencency other = (EPMDepencency) obj;
            if (dep == null) {
                if (other.dep != null) {
                    return false;
                }
            } else if (!dep.equals(other.dep)) {
                return false;
            }
            return true;
        }

    }
    
    
	/**
	 * @param holder
	 * @param docContentList
	 * @param taskId
	 * @param contentFolder
	 * @return
	 */
	public static boolean updateContents(FormatContentHolder holder,File file) {
		if(!RemoteMethodServer.ServerFlag){
			try{
			return (Boolean)RemoteMethodServer.getDefault().invoke("updateContents", EPMUtil.class.getName(), null, new Class[]{FormatContentHolder.class,File.class}, 
					new Object[]{holder,file});
			}catch(RemoteException e){
				e.printStackTrace();
			}catch(InvocationTargetException e){
				e.printStackTrace();
			}
		}
		boolean success = true;
		try {
			holder = (FormatContentHolder) ContentHelper.service.getContents(holder);
			Vector items = ContentHelper.getContentListAll(holder);
			for (int i = 0; i < items.size(); i++) {
				ContentItem item = (ContentItem) items.get(i);
				ContentServerHelper.service.deleteContent(holder, item);
			}
			holder = (FormatContentHolder) PersistenceHelper.manager.refresh(holder);
			String filename = file.getName();
			if (filename.indexOf('/') >= 0)
				filename = filename.substring(filename.lastIndexOf('/') + 1);
			if (file.exists()) {
				ApplicationData data = ApplicationData.newApplicationData(holder);
				data.setRole(ContentRoleType.PRIMARY);
				data.setCategory("CATIA_MODEL");
				data.setFileName(filename);
				data.setUploadedFromPath(file.getAbsolutePath());
				data = ContentServerHelper.service.updateContent(holder, data, file.getAbsolutePath());
				ContentServerHelper.service.updateHolderFormat(holder);
				PersistenceServerHelper.manager.update(data);
			} else {
				
				
			}
		} catch (Exception e) {
			success = false;
		}
		return success;
	}
	
	
	/**
	 * 创建EPM文档
	 * @param number 编号
	 * @param name 名称
	 * @param fileName 文件名名称
	 * @param filePath 文件路径
	 * @param folder windchill文件夹路径
	 * @param containerRef 上下文对象
	 * @return EPMDocument
	 * @throws WTException
	 * @throws WTPropertyVetoException
	 */
	public static EPMDocument createEPMDocument(String number,String name,String fileName,String filePath,String folder,WTContainerRef containerRef) throws WTException, WTPropertyVetoException{
    	EPMAuthoringAppType appType = EPMAuthoringAppType.getEPMAuthoringAppTypeDefault();
    	EPMDocumentType docType= EPMDocumentType.getEPMDocumentTypeDefault();
    	EPMApplicationType applicationType = EPMApplicationType.getEPMApplicationTypeDefault();
    	EPMDocument epmDoc = EPMUtil.createEPMDocument(number, name, appType, docType, fileName, applicationType, false);
		Folder epmTempFolder = FolderHelper.service.getFolder(folder, containerRef);
		epmDoc.setContainerReference(containerRef);
		FolderHelper.assignLocation(epmDoc, epmTempFolder);
		epmDoc = (EPMDocument) PersistenceHelper.manager.save(epmDoc);
    	File file = new File(filePath);
    	EPMUtil.updateContents(epmDoc, file);
    	epmDoc = (EPMDocument) PersistenceHelper.manager.refresh(epmDoc);
		return epmDoc;
	}
	
	
    
    public static void main(String[] args) throws WTException, WTPropertyVetoException {
/*		WTPart planeType = PartUtil.getWTPart("GC000001", "", PartUtil.E_VIEW, ""); 
    	EPMAuthoringAppType appType = EPMAuthoringAppType.getEPMAuthoringAppTypeDefault();
    	EPMDocumentType docType= EPMDocumentType.getEPMDocumentTypeDefault();
    	EPMApplicationType applicationType = EPMApplicationType.getEPMApplicationTypeDefault();
    	String fileName = "CreateTestEbomData.class";
    	EPMDocument epmDoc = createEPMDocument("number00001", "name00001", appType, docType, fileName, applicationType, false);
    	WTContainerRef containerRef = planeType.getContainerReference();
		String folderPath = "/Default/";
		Folder epmTempFolder = FolderHelper.service.getFolder(folderPath, containerRef);
		epmDoc.setContainerReference(containerRef);
		FolderHelper.assignLocation(epmDoc, epmTempFolder);
		epmDoc = (EPMDocument) PersistenceHelper.manager.save(epmDoc);
		String filePath = "C:\\ptc\\Windchill_9.1\\Windchill\\codebase\\ext\\mj\\ebom\\CreateTestEbomData.class";
    	File file = new File(filePath);
    	updateContents(epmDoc, file);*/
    	
	}
    
    /**
	 * 搜索EPMDocument文档
	 * @param number　编号
	 * @param name　　名称
	 * @return　EPMDocument
	 * @throws WTException
	 */
	public static EPMDocument getEPMDocument(String number,String name) throws WTException {
		if (!RemoteMethodServer.ServerFlag) {
			try {
				return (EPMDocument) RemoteMethodServer.getDefault().invoke("getEPMDocument", CLASSNAME, null,
						new Class[] { String.class, String.class }, new Object[] { number,  name });
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		EPMDocument doc = null;
		if((number==null || number.trim().length()<=0) && (name==null || name.trim().length()<=0)){
			return null;
		}
		QuerySpec qs = new QuerySpec(EPMDocument.class);
		if(number!=null && number.trim().length()>0){
			SearchCondition temp = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER,
					SearchCondition.EQUAL, number.toUpperCase());
			qs.appendWhere(temp, new int[] { 0 });
		}
		if(name!=null && name.trim().length()>0){
			if(qs.getConditionCount()>0){
				qs.appendAnd();
			}
			SearchCondition temp = new SearchCondition(EPMDocument.class, EPMDocument.NAME,SearchCondition.EQUAL, name);
			qs.appendWhere(temp, new int[] { 0 });
		}
		qs = new LatestConfigSpec().appendSearchCriteria(qs);
		QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
		if (qr.hasMoreElements()) {
				doc = (EPMDocument) qr.nextElement();
		}
		return doc;
	}
	
	/**
	 * 通过EPM文档编号和版本查找对应最新小版文档
	 * @description
	 * @param number
	 * @param version
	 * @return
	 * @throws WTException
	 */
    public static EPMDocument getLatestEPMByNumberVersion(String number, String version)
            throws WTException {
        QuerySpec qs = new QuerySpec(EPMDocument.class);

        String equal = SearchCondition.EQUAL;
        SearchCondition numberSC = new SearchCondition(EPMDocument.class, EPMDocument.NUMBER, equal,
                number.toUpperCase(), false);
        qs.appendWhere(numberSC, new int[0]);
        if (!StringUtils.isEmpty(version)) {
            String versionColumn = wt.vc.Versioned.VERSION_INFO + "." + wt.vc.VersionInfo.IDENTIFIER
                    + "." + "versionId";
            SearchCondition versionSC = new SearchCondition(EPMDocument.class, versionColumn, equal,
                    version, false);
            qs.appendAnd();
            qs.appendWhere(versionSC, new int[0]);
        }

        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        qr = new LatestConfigSpec().process(qr);
        if (qr.hasMoreElements()) {
            return (EPMDocument) qr.nextElement();
        }
        return null;
    }
    
    /**
     * 
     * @param epm
     * @param newName
     * @throws WTException
     * @throws RemoteException
     * @throws InvocationTargetException
     */
    public static synchronized void changeCADName(EPMDocument epm, String newName)
            throws WTException, RemoteException, InvocationTargetException {
        if (!RemoteMethodServer.ServerFlag) {
            Class cla[] = { EPMDocument.class, String.class };
            Object obj[] = { epm, newName };
            RemoteMethodServer.getDefault().invoke("changeCADName", CLASSNAME, null, cla, obj);
            return;
        }
        logger.debug("开始对EPM文档进行重命名:" + epm.getDisplayIdentity() + ",新名称:" + newName);

        boolean AccessEnforced = false;
        WTPrincipal current = SessionHelper.manager.getPrincipal();
        try {
            WTKeyedMap docCadNameMap = new WTKeyedHashMap();
            docCadNameMap.put(epm.getMaster(), newName.toLowerCase());

            AccessEnforced = SessionServerHelper.manager.setAccessEnforced(AccessEnforced);
            EPMDocument epmCopy = null;
            //被检出状态下,基于原始版本进行重命名,工作副本的修改者即为检出用户
            if (WorkInProgressHelper.isCheckedOut(epm)) {

                boolean isWorkingCopy = WorkInProgressHelper.isWorkingCopy(epm);
                if (isWorkingCopy) {
                    epmCopy = epm;
                    epm = (EPMDocument) WorkInProgressHelper.service.derivedFrom(epm);
                } else {
                    epmCopy = (EPMDocument) WorkInProgressHelper.service.workingCopyOf(epm);
                }

                WTPrincipalReference checkOuter = epmCopy.getModifier();
                SessionHelper.manager.setPrincipal(checkOuter.getName());

                logger.debug("EPM被检出,临时授权检出用户进行重命名,EPM:" + epm.getDisplayIdentity() + ",检出者:"
                        + checkOuter.getName());
                logger.debug("切换session为检出用户:" + epmCopy.getModifierName());
                AccessControlHelper.manager.addPermission(epm, checkOuter,
                        AccessPermission.MODIFY_IDENTITY, AdHocAccessKey.WNC_ACCESS_CONTROL); //WNC_ACCESS_CONTROL
                PersistenceServerHelper.manager.update(epm, false);
                logger.debug("设置临时权限:"
                        + AccessPermission.MODIFY_IDENTITY.getDisplay(Locale.SIMPLIFIED_CHINESE));

                EPMDocumentHelper.service.changeCADName(docCadNameMap);

                AccessControlHelper.manager.removePermission(epm, checkOuter,
                        AccessPermission.MODIFY_IDENTITY, AdHocAccessKey.WNC_ACCESS_CONTROL);
                PersistenceServerHelper.manager.update(epm, false);
                logger.debug("移除临时权限:"
                        + AccessPermission.MODIFY_IDENTITY.getDisplay(Locale.SIMPLIFIED_CHINESE));
            } else {
                //非检出状态,管理员权限重命名
                logger.debug("EPM未检出,通过管理员身份进行重命名");
                SessionHelper.manager.setAdministrator();
                EPMDocumentHelper.service.changeCADName(docCadNameMap);
            }
        } finally {
            SessionHelper.manager.setPrincipal(current.getName());
            AccessEnforced = SessionServerHelper.manager.setAccessEnforced(AccessEnforced);
        }
    }

}
