package ext.wisplm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringUtils;

import wt.fc.Persistable;
import wt.fc.WTObject;
import wt.fc.WTReference;
import wt.fc.collections.WTCollection;
import wt.httpgw.URLFactory;
import wt.util.WTException;
import wt.vc.Versioned;

import com.ptc.core.components.beans.ObjectBean;
import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.rendering.GuiComponent;
import com.ptc.core.components.rendering.RenderingContext;
import com.ptc.core.components.rendering.RenderingException;
import com.ptc.core.components.rendering.guicomponents.CheckBox;
import com.ptc.core.components.rendering.guicomponents.ComboBox;
import com.ptc.core.components.rendering.guicomponents.DateInputComponent;
import com.ptc.core.components.rendering.guicomponents.PushButton;
import com.ptc.core.components.rendering.guicomponents.PushButton.ButtonType;
import com.ptc.core.components.rendering.guicomponents.TextArea;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.core.components.rendering.guicomponents.UrlDisplayComponent;
import com.ptc.core.components.util.OidHelper;
import com.ptc.core.meta.common.DefinitionIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.core.meta.common.impl.WCTypeInstanceIdentifier;
import com.ptc.core.meta.type.common.impl.DefaultTypeInstance;
import com.ptc.core.ui.resources.ComponentMode;
import com.ptc.netmarkets.model.NmObject;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.misc.NmAction;
import com.ptc.netmarkets.util.misc.NmActionServiceHelper;
import com.ptc.netmarkets.util.misc.NmHTMLActionModel;
import com.ptc.netmarkets.util.table.NmDefaultHTMLTable;
import com.ptc.windchill.enterprise.wip.datautilities.rendering.guicomponents.RawStringDisplayComponent;

/**
 * <a href="javascript:void(0);" onclick="window.open('xxxx.jsp',null,
	'resizable=yes,height=600,width=300,status=no,toolbar=no,menubar=no,location=no');">??????</a>

 * @author china
 *
 */
public class NmUtil {
	
	
/*	public class ReviewForBatchMainDataProcessor   extends DefaultObjectFormProcessor {
		@Override
		public FormResult doOperation(NmCommandBean clientData, List list) throws WTException {
			
		}*/
	
	
	/**
	 * ???????????????????????? ?????????Windchill 9
	 * @description
	 * @author      ZhongBinpeng
	 * @param tableName
	 * @param columnNames
	 * @return
	 * @throws WTException
	 */
	public static NmDefaultHTMLTable buildTable(String tableName,String[] columnNames) throws WTException{
		if(columnNames == null || columnNames.length == 0){
			throw new WTException("???????????????");
		}
		tableName = tableName == null ?"":tableName;
		
		NmDefaultHTMLTable table = new NmDefaultHTMLTable();
		table.setName(tableName);
		
		int length = columnNames.length ;
		for(int i = 0 ; i < length; i ++ ){
			table.addColumn(columnNames[i]);
		}
		return table;
	}
	
	/**
	 * ????????????????????? ?????????Windchill 9
	 * @description
	 * @author      ZhongBinpeng
	 * @param table
	 * @return
	 */
	public static NmDefaultHTMLTable sortableFalse(NmDefaultHTMLTable table){
		int column = table.getColumnModel().getColumnCount();
		 for (int k = 0; k < column; k++) {
			 table.getColumnModel().getColumn(k).setSortable(false);
		 }
		 return table;
	}
	
	/**
	 * ????????????????????? ?????????Windchill 9
	 * @description
	 * @author      ZhongBinpeng
	 * @param table
	 * @param columnIndex
	 * @return
	 */
	public static NmDefaultHTMLTable sortableFalse(NmDefaultHTMLTable table,int columnIndex){
		table.getColumnModel().getColumn(columnIndex).setSortable(false);
		return table;
	}
	
	/**
	 * ????????????????????????
	 * @param clientData
	 * @return
	 * @throws WTException
	 */
	public static List<WTObject> getSelectedObjects(NmCommandBean nmCommandBean) throws WTException{
		List<WTObject> selectedObjects = new ArrayList<WTObject>();
	      List sel = nmCommandBean.getSelected();
	      if(sel == null || sel.size() == 0){
	    	  sel = nmCommandBean.getSelectedContextsForPopup();
	      }
	      if(sel == null || sel.size() == 0){
	    	  sel = nmCommandBean.getSelectedOidForPopup();
	      }
	      if(sel == null || sel.size() == 0){
	    	  sel =  nmCommandBean.getSelectedFromOpenedWizard();
	      }
	      System.out.println("selected.size:"+sel.size());
	      for(Object item:sel){
	    	  NmOid nmOid = NmCommandBean.getOidFromObject(item);
	    	  System.out.println("nmOid"+nmOid);
	    	  Object obj  = nmOid.getRef();
	    	  selectedObjects.add((WTObject)obj);
	      }
		return selectedObjects;
	}
	
	/**
	 * ?????????????????????????????????
	 * @param table
	 * @param rowIndex
	 * @param obj
	 * @throws WTException
	 */
	public static void setNmObject(NmDefaultHTMLTable table,int rowIndex,WTObject obj) throws WTException{
		NmObject nmObject = createNmObject(obj);
		table.addObject(rowIndex, nmObject);
	}
	
	/**
	 * ??????WTObject ???NmObject
	 * @param obj
	 * @return
	 * @throws WTException
	 */
	public static NmObject createNmObject(WTObject obj) throws WTException{
		NmObject nmObject = NmObject.newNmObject(NmOid.newNmOid(obj.getPersistInfo().getObjectIdentifier()));
		return nmObject;
	}
	
	/**
	 * objectType???name???action???name?????????|??????
	 * @param actions
	 * @throws WTException
	 */
	public static NmHTMLActionModel createActionModel(String[] actionNames) throws WTException{
		NmHTMLActionModel actionModel = new NmHTMLActionModel();
		for(String s : actionNames){
			String objectTypeName = StringUtils.split(s, "|")[0];
			String actionName     = StringUtils.split(s, "|")[1];
			NmAction nmAction = NmActionServiceHelper.service.getAction(objectTypeName, actionName, Locale.SIMPLIFIED_CHINESE);
			actionModel.addAction(nmAction);
		}
		return actionModel;
	}
	
/*	*//**
	 * ???????????????actionModel
	 * @param table
	 * @param actionModelName
	 * @throws WTException
	 *//*
	public static void setActionModel(NmDefaultHTMLTable table,String actionModelName) throws WTException{
		try{
			NmHTMLActionModel tableActions = (NmHTMLActionModel) NmActionServiceHelper.service.getact(actionModelName).get(0);
			table.setActionModel(tableActions);
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	
	/**
	 * ??????action?????????????????????actionModel
	 * @param actionModelName
	 * @param wto
	 * @return
	 * @throws WTException
	 */
	public static NmHTMLActionModel getActionModel(String actionModelName,WTObject wto) throws WTException{
		try{
		NmHTMLActionModel actionModel = (NmHTMLActionModel) NmActionServiceHelper.service.getActionModel(actionModelName,wto);
		return actionModel;
		}catch(Exception e){
			e.printStackTrace();
			//throw new WTException("?????????actionModel:" + actionModelName);
		}
		return null;
	}
	
	/**
	 * ??????????????????????????????
	 * @param obj
	 * @return
	 * @throws WTException
	 */
	public static String getORInfoPageURL(WTObject obj) throws WTException {
		String baseURL 	   = new URLFactory().getBaseURL().toExternalForm();
		String infoPageURL = baseURL + "servlet/TypeBasedIncludeServlet?oid=" + WTUtil.getWTObjectOid(obj);
		return infoPageURL;
	}
	
	/**
	 * ??????????????????????????????
	 * @param obj
	 * @return
	 * @throws WTException
	 */
	public static String getVRInfoPageURL(WTObject obj) throws WTException {
		String baseURL 	   = new URLFactory().getBaseURL().toExternalForm();
		String oid = "";
		if(obj instanceof Versioned){
			Versioned versioned = (Versioned) obj;
			oid = WTUtil.getVROid(versioned);
		}else{
			oid = WTUtil.getWTObjectOid(obj);
		}
		String infoPageURL = baseURL + "servlet/TypeBasedIncludeServlet?oid=" + oid;
		return infoPageURL;
	}
	
	/**
	 * ????????????RawStringDisplayComponent??????,??????????????????html????????????
	 * @param htmlString ???:"<input type='text' name='' id=''>"
	 * @return
	 */
    public static RawStringDisplayComponent createRawStringDisplayComponent(String htmlString){
    	RawStringDisplayComponent temp = new RawStringDisplayComponent("",htmlString);
    	return temp;
    }

	private NmCommandBean commandBean;
	
	private HashMap       parameterMap;
	
	private HashMap       parameterMap_old;
	
	private HashMap       parameterMap_current;
	
	public NmUtil(NmCommandBean commandBean){
		this.commandBean  = commandBean;
		this.parameterMap = commandBean.getParameterMap();
		parameterMap_current = new HashMap();
		parameterMap_old     = new HashMap();
		Set keySet  = parameterMap.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			String value = transferParamValue(parameterMap.get(key));
			if(key.contains("_old")){
				parameterMap_old.put(key, value);
			}else{
				parameterMap_current.put(key, value);
			}
		}
	}

	
	/**
	 * ????????????????????????????????????
	 * @description
	 * @author      ZhongBinpeng
	 * @return
	 * @throws WTException
	 */
	public Object getContextObject() throws WTException{
		NmOid nmOid 	= commandBean.getActionOid();
		if(nmOid != null){
			return nmOid.getRef();
		}
		return null;
	}
	
	/**
	 * ????????????????????????????????????
	 * @description
	 * @author      ZhongBinpeng
	 * @param commandBean
	 * @return
	 * @throws WTException
	 */
	public static Object getContextObject(NmCommandBean commandBean) throws WTException{
		NmOid nmOid 	= commandBean.getActionOid();
		if(nmOid != null){
			return nmOid.getRef();
		}
		return null;
	}
	
	/**
	 * processor??????????????????id????????????????????????
	 * @param objectBean
	 * @param tableID
	 * @return
	 * @throws WTException
	 */
	public static List<Persistable> getTableObjects(ObjectBean objectBean,String tableID) throws WTException{
		
		List<Persistable> result = new ArrayList<Persistable>();
		Set<NmOid> tableItems = new HashSet<NmOid>();
		List initialItems = new ArrayList();//objectBean
		List addedItems   = objectBean.getAddedItemsByName(tableID);
		List removedItems = objectBean.getRemovedItemsByName(tableID);
		if(!initialItems.isEmpty()){
			tableItems.addAll(initialItems);
		}
		if(!addedItems.isEmpty()){
			tableItems.addAll(addedItems);
		}
		if(!removedItems.isEmpty()){
			tableItems.removeAll(removedItems);
		}
		WTCollection refColl     = OidHelper.getWTCollection(tableItems);
		Iterator it  			 = refColl.referenceIterator();
		while(it.hasNext()){
			WTReference ref = (WTReference)it.next();
			result.add(ref.getObject());
		 }
		return result;
	}
	
	/**
	 * ????????????id???????????????????????????
	 * @param tableID
	 * @return
	 * @throws WTException
	 */
	public  List<Persistable> getTableObjects(String tableID) throws WTException{
		
		List<Persistable> result = new ArrayList<Persistable>();
		Set<NmOid> tableItems = new HashSet<NmOid>();
		List initialItems = new ArrayList<Persistable>();//commandBean.getInitialItemsByName(tableID);
		List addedItems   = commandBean.getAddedItemsByName(tableID);
		List removedItems = commandBean.getRemovedItemsByName(tableID);
		if(!initialItems.isEmpty()){
			tableItems.addAll(initialItems);
		}
		if(!addedItems.isEmpty()){
			tableItems.addAll(addedItems);
		}
		if(!removedItems.isEmpty()){
			tableItems.removeAll(removedItems);
		}
		WTCollection refColl     = OidHelper.getWTCollection(tableItems);
		Iterator it  			 = refColl.referenceIterator();
		while(it.hasNext()){
			WTReference ref = (WTReference)it.next();
			result.add(ref.getObject());
		 }
		return result;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @return
	 */
	public String getCreateTypeOnCreate(){
		return getParamValueFromComboBox("createType");
	}
	
	/**
	 * ???????????????checkbox name???
	 * @param nameContains name????????????????????????checkbox
	 * @return
	 */
	public Set<String> getParamValueFromCheckBox(String nameContains){
		Set<String> result = new HashSet<String>();
		Map map = commandBean.getChecked();
		Set allSelectedNames = map.keySet();
		if(StringUtils.isEmpty(nameContains)){
			return allSelectedNames;
		}
		for(Iterator it = allSelectedNames.iterator();it.hasNext();){
			String selectedName = it.next().toString();
			if(selectedName.endsWith("_old") && selectedName.contains(nameContains)){
				result.add(selectedName);
			}
		}
		return result;
	}
	
	/**
	 * ????????????
	 * @param key
	 * @return
	 */
	public  String getParamValueFromComboBox(String key){
		HashMap comboBox = commandBean.getComboBox();
		return getParamValue(comboBox,key);
	}
	
	/**
	 * ?????????????????????
	 * @param key
	 * @return
	 */
	public  String getParamValueFromTextOrTextArea(String key){
		HashMap values = commandBean.getTextArea();
		String result    =  getParamValue(values,key);
		if(result == null || "".equals(result)){
			values = commandBean.getText();
			result = getParamValue(values,key);
		}
		return result;
	}
	
	/**
	 * ??????????????????
	 * @param key
	 * @return
	 */
	public  String getParamValueFromTextArea(String key){
		HashMap textArea = commandBean.getTextArea();
		return getParamValue(textArea,key);
	}
	
	/**
	 * ??????????????????
	 * @param key
	 * @return
	 */
	public  String getParamValueFromText(String key){
		HashMap text = commandBean.getText();
		return getParamValue(text,key);
	}
	
	/**
	 * ?????????????????????
	 * @param key
	 * @return
	 */
	public  String getParamValueFromRadio(String key){
		HashMap text = commandBean.getRadio();
		return getParamValue(text,key);
	}

	public String getParamValue(String key){
		String tempKey = "";
		Iterator ite = parameterMap_current.keySet().iterator();
		while(ite.hasNext()){
			tempKey = (String) ite.next();
			if(tempKey.contains(key)){
				Object obj = parameterMap_current.get(tempKey);
				return transferParamValue(obj);
			}
		}
		return "";
	}

	public String getParamValue(HashMap map,String key){
		return getParamValue(map,key,false);
	}
	
	public String getParamValue(HashMap map,String key,boolean old){
		String tempKey = "";
		Iterator ite = map.keySet().iterator();
		Object result = null;
		while(ite.hasNext()){
			tempKey = (String) ite.next();
			Object obj = map.get(tempKey);
			if(tempKey.contains(key) && old){
				if(tempKey.contains("___old")){
					result = obj;
				}
			}
			if(tempKey.contains(key) && !old){
				if(!tempKey.contains("___old")){
					result = obj;
				}
			}
		}
		return transferParamValue(result);
	}
	
	public static String transferParamValue(Object result){
		if(result != null){
			if(result instanceof String){
				return  strnull((String)result);
			}
			if(result instanceof ArrayList){
				return  strnull(((ArrayList<String>)result).get(0));
			}
			if(result instanceof String[]){
				return  strnull(((String[]) result)[0]);
			}
		}
		
		return "";
	}
	
    /** 
     * ?????????????????????null
     * 
     * @param str
     * @return
     */
    private static String strnull(String str) {
        if (str == null)
            str = "";
        return str.trim();
    }
    
    
    /**
	 * ???????????????
	 * @param value ???????????????
	 * @param columnName ????????????????????????
	 * @param rowOid ?????????????????????????????????oid??????columnName?????????textarea???name ???id
	 * @param width  ??????????????????
	 * @param height ??????????????????
	 * @return
	 */
	public static TextArea createTextArea( String value,String columnName, String rowOid,int width, int height)
	{
		TextArea textArea = new TextArea();
		//textArea.set("text");
		textArea.setId(columnName+"_"+rowOid);
		textArea.setName(columnName+"_"+rowOid);
		textArea.setValue(value);
		textArea.setEditable(true);
		textArea.setWidth(width);
		textArea.setHeight(height);
		return textArea;
	}
	
	/**
	 * ????????????????????????
	 * @param component_id id
	 * @param displayList  ?????????????????????????????????
	 * @param valueList    ???????????????????????????
	 * @param selected     ???????????????
	 * @param columnName   ??????
	 * @return
	 * @throws WTException
	 */
	public static ComboBox createComboBox(String component_id,ArrayList<String> displayList,ArrayList<String> valueList, String selected,String columnName,boolean editable) throws WTException {
		ComboBox comboBox = new ComboBox();
		comboBox.setColumnName(columnName);
		comboBox.setId(component_id);
		comboBox.setValues(displayList);
		comboBox.setInternalValues(valueList);
		comboBox.setSelected(selected);
		comboBox.setEditable(editable);
		comboBox.setRequired(true);
		return comboBox;
	}
	
	/**
	 * ???????????????,name???value???????????????
	 * @param name
	 * @param value
	 * @param checked
	 * @return
	 */
	public static CheckBox createCheckBox(String nameValue,boolean checked){
		CheckBox checkBox = new CheckBox();
		checkBox.setName(nameValue);
		checkBox.setChecked(checked);
		return checkBox;
	}
	
	/**
	 * 
	 * @param labelForTheLink ?????????????????????
	 * @param hrefValue 	     ???????????????
	 * @param targetValue     ????????????,?????????_blank,_self,
	 * @return
	 */
	public static UrlDisplayComponent createHref(String labelForTheLink, String hrefValue,String targetValue) {
		UrlDisplayComponent urlHref = new UrlDisplayComponent();
		urlHref.setLabelForTheLink(labelForTheLink);
		urlHref.setToolTip(labelForTheLink);
		urlHref.setLink(hrefValue);
		if(!StringUtils.isEmpty(targetValue)){
			urlHref.setTarget(targetValue);
		}else{
			urlHref.setTarget("_self");
		}
		return urlHref;
	}
	
	
	/**
	 * ????????????????????????
	 * @param buttonType:ButtonType.BUTTON,ButtonType.RESET,ButtonType.SUBMIT
	 * @param name ????????????
	 * @param id   ??????id
	 * @param onclickJS ???????????????js??????
	 * @return
	 */
	public static PushButton createPushButton(ButtonType buttonType,String name,String id,String onclickJS){
		PushButton button = new PushButton(name);
		button.setName(name);
		if(StringUtils.isNotEmpty(id)){
			button.setId(id);
		}
		button.setButtonType(buttonType);
		if(StringUtils.isNotEmpty(onclickJS)){
			button.addJsAction("onClick", onclickJS);
		}
		return button;
	}
	
	/**
	 * ????????????????????????HTML??????,?????????????????????
	 * @param htmlString html???????????????
	 * @return
	 */
	public static TextDisplayComponent createTextDisplayComponent(String htmlString){
		TextDisplayComponent text = new TextDisplayComponent(null);
		text.setValue(htmlString);
		//????????????html
		text.setCheckXSS(false);
		return text;

	}

	/**
	 * ????????????????????????,???datautility?????????????????????
	 * @param component_Id
	 * @param datum
	 * @param mc
	 * @return
	 */
	public static String getTypeLogicIDOnCreateOrEdit(String component_Id, Object datum, ModelContext mc){
		 String logicID       = "";
		 ComponentMode mode = mc.getDescriptorMode();
		 try{
			 if (mode.equals(ComponentMode.CREATE) || mode.equals(ComponentMode.EDIT)) {
				 if(datum instanceof DefaultTypeInstance){ 
						DefaultTypeInstance dti = (DefaultTypeInstance) datum;
						WCTypeInstanceIdentifier instanceIdentifier = (WCTypeInstanceIdentifier) dti.getIdentifier();
				        DefinitionIdentifier definitionIdentifier = instanceIdentifier.getDefinitionIdentifier();
				        if ((definitionIdentifier instanceof WCTypeIdentifier)) {
					          WCTypeIdentifier typeIdentifier = (WCTypeIdentifier)definitionIdentifier;
					          logicID = WTUtil.getObjectLogicId(typeIdentifier.getTypename());
				        }
				 } 
			 }
		 }catch(Exception e){
			 e.printStackTrace(); 
		 }
		 return logicID;
	}
	
	/**
	 * ?????????????????????Windchill HTML??????
	 * @param component
	 * @param out
	 * @throws RenderingException
	 */
	public static void drawGuiComponent(GuiComponent component,JspWriter out) throws RenderingException{
		RenderingContext rc = new RenderingContext();
		component.draw(out, rc); //DateRangeInputComponent
	}
	
	public static TextDisplayComponent createORInfoPageHref(WTObject obj,String hrefDisplay) throws WTException{
		String url = getORInfoPageURL(obj);
		String hrefHtml = "<a href='"+url+"' target='_blank'>"+hrefDisplay+"</a>";
		return createTextDisplayComponent(hrefHtml);
	}
	
	public static TextDisplayComponent createVRInfoPageHref(WTObject obj,String hrefDisplay) throws WTException{
		String url = getVRInfoPageURL(obj);
		String hrefHtml = "<a href='"+url+"' target='_blank'>"+hrefDisplay+"</a>";
		return createTextDisplayComponent(hrefHtml);
	}
	
	/**
	 * ?????????????????????????????????
	 * @param inputName
	 * @param inputId
	 * @param columnName ????????????
	 * @return
	 */
	public static DateInputComponent createDateInputComponent(String inputName,String inputId,String columnName,boolean editable){
		DateInputComponent dateInput = new DateInputComponent(Locale.SIMPLIFIED_CHINESE);
		dateInput.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		dateInput.setName(inputName);
		dateInput.setId(inputId);
		dateInput.setEditable(editable);
		dateInput.setColumnName(columnName);
		dateInput.setApplyPrePopulate(true);
		return dateInput;
	}
}
