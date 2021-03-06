package ext.wisplm.util;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import wt.change2.ChangeOrder2;
import wt.doc.WTDocument;
import wt.enterprise.RevisionControlled;
import wt.epm.EPMDocument;
import wt.fc.ObjectIdentifier;
import wt.fc.ObjectReference;
import wt.fc.PersistInfo;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.folder.Cabinet;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.folder.SubFolder;
import wt.iba.definition.StringDefinition;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.service.IBADefinitionHelper;
import wt.iba.value.StringValue;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.maturity.PromotionNotice;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pds.StatementSpec;
import wt.query.ArrayExpression;
import wt.query.ClassAttribute;
import wt.query.OrderBy;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.query.SubSelectExpression;
import wt.query.TableColumn;
import wt.session.SessionServerHelper;
import wt.type.ClientTypedUtility;
import wt.type.TypeDefinitionReference;
import wt.type.Typed;
import wt.util.WTException;
import wt.util.WTStandardDateFormat;
import wt.vc.Iterated;
import wt.vc.IterationIdentifier;
import wt.vc.IterationInfo;
import wt.vc.VersionIdentifier;
import wt.vc.Versioned;
import wt.vc.baseline.ManagedBaseline;
import wt.vc.config.ConfigSpec;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;
import wt.vc.wip.CheckoutInfo;
import wt.vc.wip.WorkInProgressState;
import wt.vc.wip.Workable;

import com.ptc.core.meta.type.mgmt.server.TypeDefinition;
import com.ptc.core.meta.type.mgmt.server.impl.WTTypeDefinitionObjectLocator;

/**
 * 
 * @author BZH
 * @date   2019-11-17
 */
public class SearchUtil implements RemoteAccess,java.io.Serializable{
	
	public static void main(String args[]) throws Exception{
		SearchUtil su = new SearchUtil();
		su.setObjectClass(WTPart.class);
		su.setAccessEnforced(false);
		su.setConfigSpec(new wt.vc.config.LatestConfigSpec());
		su.setState("INWORK");
		su.setContainerName("?????????");
		su.setFolderPath("/Default");
		su.setFullType("wt.doc.WTDocument");
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		QueryResult qr = (QueryResult) rms.invoke("queryObjects", CLASSNAME, su, null, null);
		while(qr.hasMoreElements()){
			WTObject obj = (WTObject) qr.nextElement();
			System.out.println("??????????????????,???????????????:"+obj.getDisplayIdentity());
		}
		su.setFolderPath("/Default/???????????????/?????????/???");
		qr = (QueryResult) rms.invoke("queryObjects", CLASSNAME, su, null, null);
		while(qr.hasMoreElements()){
			WTObject obj = (WTObject) qr.nextElement();
			System.out.println("/Default/???????????????/?????????/???,???????????????:"+obj.getDisplayIdentity());
		}
	}
	
	private static final String CLASSNAME = SearchUtil.class.getName();
	
	private static final Logger logger = Logger.getLogger(SearchUtil.class.getName());
	//??????????????????????????????????????????
	public static Map<Class<?>,String> numberColumnMap = new HashMap<Class<?>,String>();
	public static Map<Class<?>,String> nameColumnMap   = new HashMap<Class<?>,String>();
	static{
		numberColumnMap.put(WTPart.class, WTPart.NUMBER);
		numberColumnMap.put(WTDocument.class, WTDocument.NUMBER);
		numberColumnMap.put(EPMDocument.class, EPMDocument.NUMBER);
		numberColumnMap.put(PromotionNotice.class, PromotionNotice.NUMBER);
		numberColumnMap.put(ManagedBaseline.class, ManagedBaseline.NUMBER);
		
		nameColumnMap.put(WTPart.class, WTPart.NAME);
		nameColumnMap.put(WTDocument.class, WTDocument.NAME);
		nameColumnMap.put(EPMDocument.class, EPMDocument.NAME);
		nameColumnMap.put(PromotionNotice.class, PromotionNotice.NAME);
		nameColumnMap.put(ChangeOrder2.class, ChangeOrder2.NAME);
		nameColumnMap.put(ManagedBaseline.class, ManagedBaseline.NAME);
	}
	@Deprecated
	public SearchUtil(){
		
	}

	public SearchUtil(Class<?> objectClass){
		this.objectClass = objectClass;
	}
	
	public SearchUtil(Class<?> objectClass,List<String[]> customColumns){
		this.objectClass 	= objectClass;
		this.customColumns  = customColumns;
	}
	
	//????????????
	private Class<?> objectClass;
	/**
	 * ????????????????????????
	 * String ???????????????4,0???????????????,1??????????????????-SearchCondition?????????,2???value,3????????????????????????,true??????,false?????????,???????????????(true???false????????????)
	 */
	private List<String[]> customColumns;
	//??????????????????
	private String fullType;
	//???????????????????????????????????????
	private boolean querySubType = false;
	//????????????
	private String containerName;

	//???????????????
	private String keyWord;//?????????????????????????????????
	
	//??????,??????????????????
	private String number;
	//????????????,?????????????????????,?????????number????????????
	private List<String> numbers;
	//????????????-???
	private String createTimeFrom;
	//????????????-???
	private String createTimeTo;
	//????????????-???
	private String modifyTimeFrom;

	//????????????-???
	private String modifyTimeTo;
	//??????,??????????????????
	private String name;
	//?????????map,,??????????????????
	private Map ibaMap;
	//??????????????????
	private String state;
	//????????????
	private String viewName;
	//????????????
	private ConfigSpec configSpec;
	//???????????????
	private String folderPath;
	//???????????????
	private String version;
	//???????????????
	private String iteration;
	//???????????????
	private String creatorName;
	//?????????????????????
	private String creatorFullName;
	
	//???????????????
	private String modifierName;
	//?????????????????????
	private String modifierFullName;
	
	//????????????????????????
	private boolean filterCheckOuted = false;
	//????????????????????????
	private  boolean accessEnforced  = false;
	//??????
	private TimeZone timeZone = TimeZone.getTimeZone("GMT+08:00");
	//
	private Locale   locale   = Locale.CHINA;
	//??????????????????,??????????????????????????????????????????
	private boolean querySubFolder = false;
	
	private QuerySpec qs;

	public  QueryResult  queryObjects() throws Exception{
		if(!RemoteMethodServer.ServerFlag){
			return (QueryResult) RemoteMethodServer.getDefault().invoke("queryObjects", CLASSNAME, this, null, null);
		}
		logger.debug("????????????:"+toString()); 
		if(objectClass == null){
			throw new WTException("objectClass??????????????????,?????????");
		}
		if(!WTObject.class.isAssignableFrom(objectClass)){
			throw new WTException("objectClass????????????,?????????WTObject???????????????");
		}
		
		boolean accessEnforcedFinal = false;
		try{
			accessEnforcedFinal = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		
			qs = new QuerySpec(objectClass);
			qs.setAdvancedQueryEnabled(true);
			//???????????????????????????
			appendCustomColumnSC(); 
	        //????????????????????????????????????
	        QueryResult qr = appendContainerAndFolderSC();
	        if(qr != null){
	        	return qr;
	        }
	        
	        //????????????????????????
	        appendNumberNameSC();
	        
	        //???????????????
	        appendKeyWordSC();
	        
	        //??????????????????
	        appendVersionSC();
	        //????????????????????????
	        appendTimeSC(createTimeFrom,createTimeTo,WTObject.CREATE_TIMESTAMP);
	        //????????????????????????
	        appendTimeSC(modifyTimeFrom,modifyTimeTo,WTObject.MODIFY_TIMESTAMP);
	        //???????????????
	        appendCreatorSC();
	        //???????????????
	        appendMofidierSC();
			// ???????????????
			if(Typed.class.isAssignableFrom(objectClass)){
		        if (fullType != null && !"".equals(fullType)){
		        	logger.debug("???????????????????????????:" + fullType);
			        long[] partBranchId = getSelfAndChildTypeIds(fullType);
			        SearchCondition sc = new SearchCondition(objectClass,Typed.TYPE_DEFINITION_REFERENCE + ".key.branchId",partBranchId);
			        appendSC(qs,sc);
		        }
			}
			if(Workable.class.isAssignableFrom(objectClass)){
				//??????????????????????????????
				if(filterCheckOuted){
		            if (qs.getConditionCount() > 0){
		                qs.appendAnd();
		            }
		            qs.appendWhere(new SearchCondition(objectClass,Workable.CHECKOUT_INFO + "." + CheckoutInfo.STATE,SearchCondition.EQUAL,WorkInProgressState.CHECKED_IN));
				}
			}
	        //????????????
			if(objectClass.equals(WTPart.class)){
				if(viewName!= null && !viewName.equals("")){
					logger.debug("????????????????????????????????????:" + viewName);
			        View view = ViewHelper.service.getView(viewName);
			        if (view != null) {
			            if (qs.getConditionCount() > 0){
			                qs.appendAnd();
			            }
			            qs.appendWhere(new SearchCondition(WTPart.class, "view.key", "=", PersistenceHelper.getObjectIdentifier(view)));
			        }
				}
			}
			//????????????????????????
			if(RevisionControlled.class.isAssignableFrom(objectClass)){
				if(state!= null && !state.equals("")){
					logger.debug("????????????????????????????????????:" + state);
					 if (qs.getConditionCount() > 0){
			                qs.appendAnd();
			            }
					 qs.appendWhere(new SearchCondition(objectClass,RevisionControlled.LIFE_CYCLE_STATE, SearchCondition.EQUAL,state));
				}
			}
			
	        //???????????????
	        if(ibaMap != null && ibaMap.size() > 0){
		        Set ibaKey = ibaMap.keySet();
		        Iterator it = ibaKey.iterator();
		        while(it.hasNext()){
		        	String ibaName  = (String) it.next();
		        	String ibaValue = (String) ibaMap.get(ibaName);
		        	if(ibaValue != null && ibaValue.length()>0){
		        		QuerySpec ibaQs = createIbaQuerySpec(ibaName,ibaValue);
		        		if(ibaQs == null){
		        			continue;
		        		}
		    	      String info = Persistable.PERSIST_INFO + "." + PersistInfo.OBJECT_IDENTIFIER + "."+ ObjectIdentifier.ID;
		    	      ClassAttribute classAttribute = new ClassAttribute(objectClass,info);
		    	      SearchCondition sc = new SearchCondition(classAttribute,SearchCondition.IN,new SubSelectExpression(ibaQs));
		    	      appendSC(qs,sc);
		        	}
		        }
	        }
	       //??????????????????????????????
		    ClassAttribute createStampAttr = new ClassAttribute(objectClass,WTObject.CREATE_TIMESTAMP);
		    int index 					   = qs.getFromClause().getPosition(objectClass);
		    qs.appendOrderBy(new OrderBy(createStampAttr, true), new int[]{index});  //????????????desc
		   logger.debug("QS----->" + qs.toString());
	       qr = PersistenceHelper.manager.find((StatementSpec) qs);
	        if(configSpec != null){
	        	qr = configSpec.process(qr);
	        }
	        return qr;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforcedFinal);
		}
	}
	
	/**
	 * ?????????????????????
	 * @throws WTException
	 */
	private void appendCustomColumnSC() throws WTException {
		if(customColumns == null){
			return;
		}
		for(int i = 0 ; i < customColumns.size() ; i ++){
			String[] customColumn = customColumns.get(i);
			if(customColumn == null){
				continue;
			}
			if(customColumn.length < 4){
				throw new WTException("??????????????????????????????4???:" + Arrays.asList(customColumn));
			}
			//????????????
			String columnName       = customColumn[0];
			if(StringUtils.isEmpty(columnName)){
				throw new WTException("???????????????,???????????????,??????:" + i);
			}
			//?????????like,equal???
			String operator         = customColumn[1];
			if(StringUtils.isEmpty(operator)){
				throw new WTException("???????????????,???????????????,??????:" + i);
			}
			String columnValue      = customColumn[2];
			//?????????????????????
			boolean caseUpperLower = "true".equalsIgnoreCase(customColumn[3]);
            columnValue = replaceForSearch(columnValue,!caseUpperLower);
            //???????????????,???????????????IS_NULL???,????????????????????????
            if("".equals(columnValue) && SearchCondition.IS_NULL.equals(columnValue)){
                if (qs.getConditionCount() > 0){
                    qs.appendAnd();
                }
                qs.appendWhere(new SearchCondition(objectClass,columnName,
                        SearchCondition.IS_NULL, true));
            }
            //?????????????????????
            else if(!"".equals(columnValue)){
                if (qs.getConditionCount() > 0){
                    qs.appendAnd();
                }
            	qs.appendWhere(new SearchCondition(objectClass,columnName, operator.toUpperCase(), columnValue, caseUpperLower));
            }
            //?????????????????????,???????????????
		}
	}

	private void appendVersionSC() throws QueryException{
		if(RevisionControlled.class.isAssignableFrom(objectClass)){
            if (version != null && !"".equals(version)) {
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd();
	            }
                qs.appendWhere(new SearchCondition(objectClass, Versioned.VERSION_IDENTIFIER + "."
                        + VersionIdentifier.VERSIONID, SearchCondition.EQUAL, version, false));
            }
            if (iteration != null && !"".equals(iteration)) {
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd();
	            }
                qs.appendWhere(new SearchCondition(objectClass, Iterated.ITERATION_IDENTIFIER + "."
                        + IterationIdentifier.ITERATIONID, SearchCondition.EQUAL, iteration, false));
            }
		}
	}
	
	private void appendTimeSC(String timeFrom,String timeTo,String columnName) throws ParseException, WTException{
        //????????????????????????
        if (timeFrom != null && !"".equals(timeFrom)) {
        	timeFrom = timeFrom.replace('-','/');
            Date dateFrom = null;
            try{
            	dateFrom = WTStandardDateFormat.parse(timeFrom, "yyyy/M/d", locale, timeZone);
            }catch(Exception e){
            	e.printStackTrace();
            	throw new WTException("timeFrom????????????,????????????:yyyy-M-d???yyyy/M/d," + e.getLocalizedMessage());
            }
            if (qs.getConditionCount() > 0) 
                qs.appendAnd();
            qs.appendWhere(new SearchCondition(objectClass,columnName,SearchCondition.GREATER_THAN_OR_EQUAL,new Timestamp(dateFrom.getTime())), new int[]{0});
        }
        Calendar cale = Calendar.getInstance(timeZone);
        //????????????????????????
        if (timeTo != null && !"".equals(timeTo)) {
        	timeTo = timeTo.replace('-','/');
            Date dateTo = null;
            try{
            	dateTo = WTStandardDateFormat.parse(timeTo, "yyyy/M/d", locale, timeZone);
            }catch(Exception e){
            	e.printStackTrace();
            	throw new WTException("timeTo????????????,????????????:yyyy-M-d???yyyy/M/d," + e.getLocalizedMessage());
            }
            // ??????????????????????????????
            cale.setTime(dateTo);
            cale.add(Calendar.DAY_OF_MONTH, 1);
            dateTo = cale.getTime();
            
            if (qs.getConditionCount() > 0){
            	qs.appendAnd();
            }
            qs.appendWhere(new SearchCondition(objectClass,columnName,SearchCondition.LESS_THAN,new Timestamp(dateTo.getTime())), new int[]{0});
        }
	}
	
	private void appendNumberNameSC() throws QueryException{
        number = replaceForSearch(number,true);
        name   = replaceForSearch(name,false);
        
        String numberColumn = numberColumnMap.get(objectClass);
        String nameColumn   = nameColumnMap.get(objectClass);
        if(numberColumn == null){
        	logger.warn("??????????????????????????????????????????,??????????????????????????????:" + objectClass);
        }
        if(nameColumn == null){
        	logger.warn("??????????????????????????????????????????,??????????????????????????????:" + objectClass);
        }
        if(!"".equals(number) && numberColumn != null){
        	logger.debug("????????????????????????:" + number);
            if (qs.getConditionCount() > 0){
                qs.appendAnd();
            }
            qs.appendWhere(new SearchCondition(objectClass,numberColumn, SearchCondition.LIKE,
                    number, false));
        } else if(numbers != null && numbers.size() > 0 && numberColumn!=null) {
            logger.debug("??????????????????????????????:" + numbers);
            if (qs.getConditionCount() > 0){
                qs.appendAnd();
            }
            qs.appendOpenParen();
            for (int i = 0; i < numbers.size(); i++) {
                String tempNumber = numbers.get(i);
                tempNumber = StringUtils.trimToEmpty(tempNumber).toUpperCase();
                tempNumber = tempNumber.replace("*","%").replace('?', '_');
                qs.appendWhere(new SearchCondition(objectClass,numberColumn, SearchCondition.LIKE,
                        tempNumber, false));
                
                if(i != (numbers.size()-1)) {
                    qs.appendOr();
                }
            }
            qs.appendCloseParen();
        }
        if(!"".equals(name) && nameColumn != null){
        	logger.debug("????????????????????????:" + name);
            if (qs.getConditionCount() > 0){
                qs.appendAnd();
            }
            qs.appendWhere(new SearchCondition(objectClass,nameColumn,SearchCondition.LIKE, name));
        }
	}
	
    private void appendKeyWordSC() throws QueryException {
        //?????????number???name??????????????????????????????
        if (StringUtils.isBlank(number) && StringUtils.isBlank(number)
                && StringUtils.isNotBlank(keyWord)) {
            logger.debug("???????????????????????????:" + keyWord);

            String keyWordNumber = replaceForSearch(keyWord, true);
            String keyWordName = replaceForSearch(keyWord, false);

            String numberColumn = numberColumnMap.get(objectClass);
            String nameColumn = nameColumnMap.get(objectClass);

            if (numberColumn == null) {
                logger.warn("??????????????????????????????????????????,??????????????????????????????:" + objectClass);
            }
            if (nameColumn == null) {
                logger.warn("??????????????????????????????????????????,??????????????????????????????:" + objectClass);
            }

            if (qs.getConditionCount() > 0) {
                qs.appendAnd();
            }
            qs.appendOpenParen();

            qs.appendWhere(new SearchCondition(objectClass, numberColumn, SearchCondition.LIKE,
                    keyWordNumber, false));

            qs.appendOr();

            qs.appendWhere(new SearchCondition(objectClass, nameColumn, SearchCondition.LIKE,
                    keyWordName));

            qs.appendCloseParen();
        }
    }

	public static void appendSC(QuerySpec qs,SearchCondition sc) throws QueryException{
        if (qs.getConditionCount() > 0){
        	qs.appendAnd();
        }
        qs.appendWhere(sc,new int[]{0});
	}
	//??????????????????????????????
	private QueryResult appendContainerAndFolderSC() throws RemoteException, InvocationTargetException, WTException{
        if(WTContained.class.isAssignableFrom(objectClass)){
	        if(containerName != null && !containerName.equals("")){
	        	WTContainer container = WTUtil.getContainerByName(containerName);
	        	if(container == null){
	        		throw new WTException("???????????????:" + containerName);
	        	}
	        	logger.debug("????????????????????????:" + containerName);
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd(); 
	            }
	            WTContainerRef containerRef = WTContainerRef.newWTContainerRef(container);
	        	qs.appendWhere(WTContainerHelper.getWhereContainerIs(containerRef));
	        	if(folderPath != null && !"".equals(folderPath)){
	        		if(!folderPath.startsWith("/Default")){
	        			throw new WTException("?????????????????????????????????:" + folderPath);
	        		}
	        		logger.debug("???????????????????????????:" + folderPath);
	        		String tableName = qs.getFromClause().getAliasAt(0);
	        		if("/Default".equals(folderPath)){
	        			Cabinet rootFolder = container.getDefaultCabinet();
	                    if (qs.getConditionCount() > 0){
	                        qs.appendAnd();
	                    }
	                       
        				long id = PersistenceHelper.getObjectIdentifier(rootFolder).getId();
        				String[] ida2a2 = {id+""};
        				
	    				TableColumn tc1 = new TableColumn(tableName, "IDA3A2FOLDERINGINFO");
	    				//cabinet id
	    				qs.appendWhere(new SearchCondition(tc1,SearchCondition.IN, new ArrayExpression(ida2a2)));
	    				if(!querySubFolder){
	    					//????????????????????????
		                    if (qs.getConditionCount() > 0){ 
		                        qs.appendAnd();
		                        //0???????????????
		                        ida2a2 = new String[]{0+""}; 
		                        tc1 = new TableColumn(tableName, "IDA3B2FOLDERINGINFO");
		                        qs.appendWhere(new SearchCondition(tc1,SearchCondition.IN, new ArrayExpression(ida2a2)));
		                    }
	    				}
	        		}else{
	        			Folder subFolder = null;
	        			try{
	        				subFolder = FolderHelper.service.getFolder(folderPath, containerRef);
	        			}catch(Exception e){
	        				e.printStackTrace();
	        				throw new WTException("?????????????????????,????????????:"+containerRef.getName()+"?????????:" + folderPath + ",????????????:" + e.getLocalizedMessage());
	        			}
	        			String[] ida2a2s = null;
	        			if(subFolder != null){
	        				logger.debug("???????????????????????????:" + folderPath + ",????????????????????????:" + querySubFolder);
	        				if(querySubFolder){
	        					//??????????????????????????????
	        					List<SubFolder> subFolders = getSubFolders(subFolder, true);
	        					subFolders.add((SubFolder)subFolder);
	        					ida2a2s = new String[subFolders.size()];
	        					for(int i = 0 ; i < subFolders.size(); i ++ ){
	        						long id = PersistenceHelper.getObjectIdentifier(subFolders.get(i)).getId();
	        						ida2a2s[i] = id+"";
	        					}
	        				}else{
		        				long id = PersistenceHelper.getObjectIdentifier(subFolder).getId();
		        				ida2a2s = new String[]{id+""};
	        				}
	        			}
	        			
	        			if(ida2a2s != null){
		                    if (qs.getConditionCount() > 0){
		                        qs.appendAnd();
		                    }
		    				TableColumn tc1 = new TableColumn(tableName, "IDA3B2FOLDERINGINFO");
		    				qs.appendWhere(new SearchCondition(tc1,SearchCondition.IN, new ArrayExpression(ida2a2s)));
	        			}else{
	        				return new QueryResult();
	        			}
	        		}
	        	}
	        }
        }
        return null;
	}
		
	
	//????????????????????????
	private void appendCreateTimeToSC() throws ParseException, QueryException{
		Calendar cale = Calendar.getInstance(timeZone);
        //????????????????????????
        if (createTimeTo != null && !"".equals(createTimeTo)) {
        	createTimeTo = createTimeTo.replace('-','/');
            Date dateTo = WTStandardDateFormat.parse(createTimeTo, "yyyy/M/d", locale, timeZone);
            // ??????????????????????????????
            cale.setTime(dateTo);
            cale.add(Calendar.DAY_OF_MONTH, 1);
            dateTo = cale.getTime();
            
            if (qs.getConditionCount() > 0){
            	qs.appendAnd();
            }
            qs.appendWhere(new SearchCondition(objectClass,
                    Persistable.PERSIST_INFO + "." 
                    + PersistInfo.CREATE_STAMP,
                    SearchCondition.LESS_THAN,
                    new Timestamp(dateTo.getTime())), new int[]{0});
        }
	}
	
	private void appendCreatorSC() throws WTException{
		if(!Iterated.class.isAssignableFrom(objectClass)){
			return;
		}
		logger.debug("???????????????,creatorFullName:" + creatorFullName +",creatorName:" + creatorName);
        // ?????????????????????
        if (StringUtils.isNotEmpty(creatorFullName) && !creatorFullName.equals("*")) {
            // ?????????????????????????????????50??????
            HashSet users = new HashSet();
            Enumeration en = OrganizationServicesHelper.manager.findLikeUser(WTUser.FULL_NAME, creatorFullName);
            for (int i = 0; i < 50 && en.hasMoreElements(); i++){
                users.add(en.nextElement());
            }
            if(users.size() != 0){
                long[] uids = new long[users.size()];
                int i = 0;
                for (Iterator it = users.iterator(); it.hasNext() ; i++) {
                    uids[i] = ((WTUser) it.next()).getPersistInfo().getObjectIdentifier().getId();
                }
                
                // ???????????????
                if (qs.getConditionCount() > 0){
                	qs.appendAnd();
                }
                qs.appendWhere(new SearchCondition(objectClass,
                        Iterated.ITERATION_INFO + "." 
                        + IterationInfo.CREATOR + "."
                        + ObjectReference.KEY + "."
                        + ObjectIdentifier.ID, 
                        uids), new int[]{0});
            }
        }
        
        // ????????????????????????
        if (StringUtils.isNotEmpty(creatorName) && !creatorName.equals("*")) {
            // ?????????????????????????????????50??????
            HashSet users = new HashSet();
            Enumeration en = OrganizationServicesHelper.manager.findLikeUser(WTUser.NAME, creatorName);
            for (int i = 0; i < 50 && en.hasMoreElements(); i++){
                users.add(en.nextElement());
            }
            if(users.size() != 0){
                long[] uids = new long[users.size()];
                int i = 0;
                for (Iterator it = users.iterator(); it.hasNext() ; i++) {
                    uids[i] = ((WTUser) it.next()).getPersistInfo().getObjectIdentifier().getId();
                }
                
                // ???????????????
                if (qs.getConditionCount() > 0){
                	qs.appendAnd();
                }
                qs.appendWhere(new SearchCondition(objectClass,
                        Iterated.ITERATION_INFO + "." 
                        + IterationInfo.CREATOR + "."
                        + ObjectReference.KEY + "."
                        + ObjectIdentifier.ID, 
                        uids), new int[]{0});
            }
        }
	}
	
	
	private void appendMofidierSC() throws WTException{
		if(!Iterated.class.isAssignableFrom(objectClass)){
			return;
		}
		logger.debug("???????????????,modifierFullName:" + modifierFullName +",modifierName:" + modifierName);
        // ?????????????????????
        if (StringUtils.isNotEmpty(modifierFullName) && !modifierFullName.equals("*")) {
            // ?????????????????????????????????50??????
            HashSet users = new HashSet();
            Enumeration en = OrganizationServicesHelper.manager.findLikeUser(WTUser.FULL_NAME, modifierFullName);
            for (int i = 0; i < 50 && en.hasMoreElements(); i++){
                users.add(en.nextElement());
            }
            if(users.size() != 0){
                long[] uids = new long[users.size()];
                int i = 0;
                for (Iterator it = users.iterator(); it.hasNext() ; i++) {
                    uids[i] = ((WTUser) it.next()).getPersistInfo().getObjectIdentifier().getId();
                }
                
                // ???????????????
                if (qs.getConditionCount() > 0){
                	qs.appendAnd();
                }
                qs.appendWhere(new SearchCondition(objectClass,
                        Iterated.ITERATION_INFO + "." 
                        + IterationInfo.MODIFIER + "."
                        + ObjectReference.KEY + "."
                        + ObjectIdentifier.ID, 
                        uids), new int[]{0});
            }
        }
        
        // ????????????????????????
        if (StringUtils.isNotEmpty(modifierName) && !modifierName.equals("*")) {
            // ?????????????????????????????????50??????
            HashSet users = new HashSet();
            Enumeration en = OrganizationServicesHelper.manager.findLikeUser(WTUser.NAME, modifierName);
            for (int i = 0; i < 50 && en.hasMoreElements(); i++){
                users.add(en.nextElement());
            }
            if(users.size() != 0){
                long[] uids = new long[users.size()];
                int i = 0;
                for (Iterator it = users.iterator(); it.hasNext() ; i++) {
                    uids[i] = ((WTUser) it.next()).getPersistInfo().getObjectIdentifier().getId();
                }
                
                // ???????????????
                if (qs.getConditionCount() > 0){
                	qs.appendAnd();
                }
                qs.appendWhere(new SearchCondition(objectClass,
                        Iterated.ITERATION_INFO + "." 
                        + IterationInfo.MODIFIER + "."
                        + ObjectReference.KEY + "."
                        + ObjectIdentifier.ID, 
                        uids), new int[]{0});
            }
        }
	}
	
	private static String replaceForSearch(String s,boolean toUpperCase){
		if(s == null || "".equals(s.trim())){
			return "";
		}
		if(toUpperCase){
			s = s.toUpperCase();
		}
		s = s.replace("*","%");
		s = s.replace('?', '_');
		return s;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param ibaName
	 * @param ibaValue
	 */
	public static  QuerySpec createIbaQuerySpec(String ibaName,String ibaValue) throws Exception{
        AttributeDefDefaultView addv = 
            IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(ibaName);
        if (addv == null){
            throw new Exception("????????????????????????" + ibaName);
        }
        if (!StringDefinition.class.getName().equals(addv.getAttributeDefinitionClassName())){
            throw new Exception("?????????????????????????????????" + addv.getAttributeDefinitionClassName());
        }
        if(ibaName == null || "".equals(ibaName)){
        	return null;
        }
        ObjectIdentifier ibaDefObjectId = addv.getObjectID();

        // ????????????????????????????????????
        QuerySpec ss = new QuerySpec();
        int iSS = ss.addClassList(StringValue.class, false);
        ClassAttribute ibaHolderId = new ClassAttribute(StringValue.class,
                StringValue.IBAHOLDER_REFERENCE + "." + ObjectReference.KEY + "."
                        + ObjectIdentifier.ID);
        // ?????????ibaHolderId
        ss.appendSelect(ibaHolderId, new int[] { iSS }, false);
        ss.getFromClause().setAliasPrefix("C");

        // ??????1???????????????ID=ibaDefObjectId
        ss.appendWhere(new SearchCondition(StringValue.class,
                StringValue.DEFINITION_REFERENCE + "." + ObjectReference.KEY,
                SearchCondition.EQUAL, ibaDefObjectId), new int[] { iSS });
        // ??????2????????????????????????
        ss.appendAnd();
        if(ibaValue == null || "".equals(ibaValue)){
            ss.appendWhere(new SearchCondition(StringValue.class, StringValue.VALUE,
                    SearchCondition.IS_NULL, true));
        }else{
        	ibaValue = ibaValue.replace("*","%").replace('?', '_');
            ss.appendWhere(new SearchCondition(StringValue.class, StringValue.VALUE,
                    SearchCondition.LIKE, ibaValue.toUpperCase()), new int[] { iSS });
        }

        return ss;
	}
	
	 /**
     * ??????????????????????????????????????????????????? branch id (sorted)
     * @param fromTypeId,????????????wt.part.WTPart|techDoc??????wt.part.WTPart|com.pdmtest.techDoc?????????
     */
    public  long[] getSelfAndChildTypeIds(String logicType) throws Exception {
        if (logicType == null)
            return new long[0];

        //WTTypeDefinition td = null;
        // ?????????????????????????????????
        String typeHead = "WCTYPE|";
        if (logicType.startsWith(typeHead)){
        	logicType = logicType.substring(typeHead.length());
        }
        // logicType = DocUtil.getFullType(logicType);
        TypeDefinitionReference tdr = null;
        try {
            tdr = ClientTypedUtility.getTypeDefinitionReference(logicType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (tdr == null){
        	return new long[0];
        }
        
        HashSet idSet = new HashSet();
        idSet.add(new Long(tdr.getKey().getBranchId()));
        
        if(querySubType){
        	logger.debug("????????????????????????????????????");
            //??????????????????????????????
            Vector allTypes = (Vector) new WTTypeDefinitionObjectLocator().locate(WTTypeDefinitionObjectLocator.LATEST);
            boolean moreFound = true;
            while (moreFound) {
                moreFound = false;
                //????????????????????????
                for (int i = 0; allTypes != null && i < allTypes.size(); i++) {
                    TypeDefinition ttd = (TypeDefinition) allTypes.get(i);
                    //??????????????????
                    if (ttd.isDeleted() || ttd.getParent() == null || ttd.getParent().isDeleted())
                        continue;
                    //
                    Long parentId = new Long(ttd.getParent().getBranchIdentifier());
                    //????????????????????????id????????????set,??????????????????????????????
                    Long typeId = new Long(ttd.getBranchIdentifier());
                    if (idSet.contains(parentId) && !idSet.contains(typeId)) {
                        idSet.add(typeId);
                        moreFound = true;
                    }
                }
            }
        }
        
        int i = 0;
        long[] ida = new long[idSet.size()];
        for (Iterator it = idSet.iterator(); it.hasNext();) 
            ida[i++] = ((Long) it.next()).longValue();
        Arrays.sort(ida);

        return ida;
    }
    
	/**
	 * ???????????????????????????????????????????????????,???????????????yyyy-MM-dd???yyyy/MM/dd
	 * ????????????,?????????true,???????????????
	 * @param date 
	 * @param dateFrom 
	 * @param dateTo
	 * @return
	 * @throws WTException 
	 */
	public static boolean inDateRange(String date,String dateFrom,String dateTo) throws WTException{
		try{
			boolean inFrom = true;
			boolean inTo   = true;
			if(StringUtils.isEmpty(date)){
				return false;
			}
			date = date.replace("-", "").replace("/", "");
			int dateInt = Integer.parseInt(date);
			if((dateInt+"").length() != 8){
				throw new WTException("date??????????????????,?????????yyyy-MM-dd???yyyy/MM/dd");
			}
			if(StringUtils.isNotEmpty(dateFrom)){
				dateFrom = dateFrom.replace("-", "").replace("/", ""); 
				int dateFromInt = Integer.parseInt(dateFrom);
				if((dateFromInt+"").length() != 8){
					throw new WTException("dateFrom??????????????????,?????????yyyy-MM-dd???yyyy/MM/dd");
				}
				if(dateInt < dateFromInt){
					inFrom = false;
				}
			}
			if(StringUtils.isNotEmpty(dateTo)){
				dateTo = dateTo.replace("-", "").replace("/", ""); 
				int dateToInt = Integer.parseInt(dateTo);
				if((dateToInt+"").length() != 8){
					throw new WTException("dateTo??????????????????,?????????yyyy-MM-dd???yyyy/MM/dd");
				}
				if(dateInt > dateToInt){
					inTo = false;
				}
			}
			return inFrom && inTo;
			
		}catch(Exception e){
			throw new WTException("inDateRange????????????:" + e.getMessage());
		}
	}
	
	public void setFilterCheckOuted(boolean filterCheckOuted) {
		this.filterCheckOuted = filterCheckOuted;
	}

	public void setObjectClass(Class objectClass) {
		this.objectClass = objectClass;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	
	public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public void setNumber(String number) {
		this.number = number;
	}
	
	public void setNumbers(List<String> numbers) {
	    this.numbers = numbers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIbaMap(Map ibaMap) {
		this.ibaMap = ibaMap;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setConfigSpec(ConfigSpec configSpec) {
		this.configSpec = configSpec;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public void setAccessEnforced(boolean accessEnforced) {
		this.accessEnforced = accessEnforced;
	}

	public void setCreateTimeFrom(String createTimeFrom) {
		this.createTimeFrom = createTimeFrom;
	}

	public void setCreateTimeTo(String createTimeTo) {
		this.createTimeTo = createTimeTo;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public void setIteration(String iteration) {
		this.iteration = iteration;
	}

	
	public String getModifyTimeFrom() {
		return modifyTimeFrom;
	}

	public void setModifyTimeFrom(String modifyTimeFrom) {
		this.modifyTimeFrom = modifyTimeFrom;
	}

	public String getModifyTimeTo() {
		return modifyTimeTo;
	}

	public void setModifyTimeTo(String modifyTimeTo) {
		this.modifyTimeTo = modifyTimeTo;
	}

	public void setQuerySubFolder(boolean querySubFolder) {
		this.querySubFolder = querySubFolder;
	}
	
	public void setQuerySubType(boolean querySubType) {
		this.querySubType = querySubType;
	}
	
	public void setCustomColumns(List<String[]> customColumns) {
		this.customColumns = customColumns;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public void setCreatorFullName(String creatorFullName) {
		this.creatorFullName = creatorFullName;
	}

	
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public void setModifierFullName(String modifierFullName) {
		this.modifierFullName = modifierFullName;
	}

	/**
	 * ??????????????????????????????
	 * @param parent
	 * @param allLevel
	 * @return
	 * @throws WTException
	 */
	public static List<SubFolder> getSubFolders(Folder parent,boolean allLevel) throws WTException{
		List<SubFolder> result = new ArrayList<SubFolder>();
		QueryResult qr = FolderHelper.service.findSubFolders(parent);
		while(qr.hasMoreElements()){
			Object obj = qr.nextElement();
			if(obj instanceof SubFolder){
				result.add((SubFolder)obj);
				if(allLevel){
					result.addAll(getSubFolders((SubFolder)obj,allLevel));
				}
			}
		}
		return result;
	}
}
