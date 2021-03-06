package ext.wis.workflow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ptc.core.components.descriptor.ModelContext;
import com.ptc.core.components.factory.AbstractDataUtility;
import com.ptc.core.components.factory.dataUtilities.AttributeDataUtilityHelper;
import com.ptc.core.components.rendering.AbstractGuiComponent;
import com.ptc.core.components.rendering.PickerRenderConfigs;
import com.ptc.core.components.rendering.guicomponents.CheckBox;
import com.ptc.core.components.rendering.guicomponents.ComboBox;
import com.ptc.core.components.rendering.guicomponents.GUIComponentArray;
import com.ptc.core.components.rendering.guicomponents.IconComponent;
import com.ptc.core.components.rendering.guicomponents.PickerInputComponent;
import com.ptc.core.components.rendering.guicomponents.PushButton;
import com.ptc.core.components.rendering.guicomponents.StringInputComponent;
import com.ptc.core.components.rendering.guicomponents.TextArea;
import com.ptc.core.components.rendering.guicomponents.TextDisplayComponent;
import com.ptc.core.components.rendering.guicomponents.UrlDisplayComponent;
import com.ptc.core.meta.type.common.TypeInstance;
import com.ptc.core.ui.resources.ComponentType;
import com.ptc.netmarkets.util.misc.NmAction;
import com.ptc.netmarkets.util.misc.NmActionServiceHelper;
import com.ptc.netmarkets.work.NmWorkItemCommands;
import com.ptc.netmarkets.work.StandardNmWorkItemService;
import com.ptc.windchill.enterprise.attachments.dataUtilities.AttachmentsDataUtilityHelper;
import com.ptc.windchill.enterprise.wip.datautilities.rendering.guicomponents.RawStringDisplayComponent;
import com.ptc.windchill.enterprise.workitem.ComponentId;

import ext.wisplm.util.ObjectInfoUtil;
import wt.access.AccessControlHelper;
import wt.access.AccessPermission;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.FormatContentHolder;
import wt.facade.netmarkets.NetmarketsHref;
import wt.fc.EnumeratedType;
import wt.fc.EnumeratedTypeUtil;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.inf.container.OrgContainer;
import wt.inf.container.PrincipalSpec;
import wt.inf.container.WTContained;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.inf.team.ContainerTeamHelper;
import wt.log4j.LogR;
import wt.method.MethodContext;
import wt.org.DirectoryContextProvider;
import wt.org.OrganizationServicesHelper;
import wt.org.OrganizationServicesMgr;
import wt.org.WTGroup;
import wt.org.WTOrganization;
import wt.org.WTPrincipal;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.team.TeamHelper;
import wt.team.TeamReference;
import wt.team.TeamTemplate;
import wt.team.TeamTemplateReference;
import wt.util.InstalledProperties;
import wt.util.WTException;
import wt.util.WTMessage;
import wt.util.WTProperties;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;
import wt.workflow.SortedEnumByPrincipal;
import wt.workflow.definer.ProcessDataInfo;
import wt.workflow.definer.WfAssignedActivityTemplate;
import wt.workflow.definer.WfDefinerHelper;
import wt.workflow.definer.WfVariableInfo;
import wt.workflow.engine.ProcessData;
import wt.workflow.engine.WfActivity;
import wt.workflow.engine.WfDueDate;
import wt.workflow.engine.WfVariable;
import wt.workflow.work.WfHtmlFormat;
import wt.workflow.work.WorkItem;
import wt.workflow.worklist.WfTaskProcessor;
import wt.workflow.worklist.worklistResource;



/**
 * Data utility for getting workflow custom variables.
 */
/**
 * ????????????????????????datautility,??????OOTB workitem_customvariable ????????????,site.xconf??????????????????,xconfmanager -p??????
	<Property name="wt.services/svc/default/com.ptc.core.components.descriptor.DataUtility/workitem_customvariable/java.lang.Object/0" 
			  overridable="true" 
			  targetFile="codebase/com/ptc/core/components/components.dataUtilities.properties" 
			  value="ext.wis.workflow.WISCustomVariablesDataUtility/singleton" />
 * 
 * 1?????????????????????,???html??????????????????,??????displayTextAreaGui
 * 2?????????WTObject???????????????,?????????????????????,????????????????????????
 */
public final class WISCustomVariablesDataUtility extends AbstractDataUtility {

	/** Log4j Logger.  */
	private static final Logger LOGGER = LogR.getLogger(WISCustomVariablesDataUtility.class.getName());

	/** Locale, from the NmCommandBean, from the JSP. Gets set in the <code>getDataValue(...)</code> method. */
	private Locale locale;

	/** The Work Item from which to get an attribute. */
	private WorkItem workItem;

	/** Task processor object, formerly used by HTML Template Processor pages. */
	private WfTaskProcessor wfTaskProcessor;

	/** The model context passed to the <code>getDataValue(...)</code> or <code>getLabel(...)</code> methods. */
	private ModelContext modelContext;
	private String componentid;

	/** The HTML representing a blank space. */
	private static final String BLANK_SPACE = "&nbsp;";

	/** custom variable width and height property name */
	public static final String WIDTH_HEIGHT = "width_height";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	private static final int DEFAULT_HEIGHT = 1;
	private static final int DEFAULT_WIDTH = 50;

	private static final String WF_DISPLAY_USERS = "WF_DISPLAY_USERS";
	private static final String WF_USERS = "WF_USERS";
	private static final String WF_DISPLAY_GROUPS = "WF_DISPLAY_GROUPS";
	private static final String WF_GROUPS = "WF_GROUPS";

	/** signifies that all custom variables should be shown */
	private static final String ALL_ACTIVITY_VARIABLES = "all_activity_variables";

	/** name of special instructions workflow variable */
	private static final String SPECIAL_INSTRUCTIONS = "special_instructions";
	private static boolean DISPLAY_WTUSER_PICKER;
	public static int TRUNCATION_LENGTH = 0;
	private boolean lwcDisplay = true;
	static {
		try {
			// name size determination
			WTProperties properties = WTProperties.getLocalProperties();
			DISPLAY_WTUSER_PICKER = properties.getProperty("wt.workflow.work.DisplayWtUserPicker", false); 
		}
		catch (Throwable t)
		{
			System.err.println("Error initializing " + StandardNmWorkItemService.class.getName ());
			t.printStackTrace(System.err);
			throw new ExceptionInInitializerError(t);
		}
	}

	/**
	 * Constructs a new instance of this data utility.
	 */
	public WISCustomVariablesDataUtility() {
		super();
		wfTaskProcessor = new WfTaskProcessor();
	}


	/**
	 * Method that gets called on a Data Utility, when JCA attempts to get the value of an
	 * attribute, given an Id.
	 * @see com.ptc.core.components.descriptor.DataUtility#getDataValue(java.lang.String,
	 * java.lang.Object, com.ptc.core.components.descriptor.ModelContext)
	 * @throws WTException If there is a problem getting the <code>Locale</code> from the <code>
	 * NmCommandBean</code>, in turn from the passed in <code>ModelContext</code>.  Also, if there
	 * is a problem getting the correct value to set on the Common Components GUI Component.
	 */
	public final Object getDataValue(final String componentId,  Object datum, final ModelContext mc) throws WTException {
		try{

			if(datum instanceof TypeInstance){
				datum = mc.getNmCommandBean().getPageOid().getRefObject();
			}
			if (!WorkItem.class.isAssignableFrom(datum.getClass())) {

				final StringBuffer buffer = new StringBuffer(256);
				buffer.append(getClass()).append(".getDataValue() - datum: ").append(datum.getClass());
				LOGGER.warn(buffer);

				return TextDisplayComponent.NBSP;
			}
			lwcDisplay = mc.getDescriptorType().equals(ComponentType.INFO_ATTRIBUTES_TABLE);
			locale = mc.getNmCommandBean() == null ? Locale.getDefault() : mc.getNmCommandBean().getLocale();

			workItem = (WorkItem)datum;
			wfTaskProcessor.setWorkItem(workItem);
			modelContext = mc;
			componentid = componentId;

			return getCustomVariables(componentId);
		
		}catch(Exception e){
			e.printStackTrace();
			throw new WTException(e);
		}

	}

	/**
	 * Get a custom workflow variable for the current Work Item.
	 * @return A specific custom workflow variable.
	 * @throws WTRuntimeException If there is a problem getting the display name for the custom
	 * variable.
	 */
	@SuppressWarnings("unchecked")
	private final Object getCustomVariables(String componentId) throws WTRuntimeException {

		final WfActivity assignedActivity = wfTaskProcessor.getActivity();
		ProcessData activityContext = assignedActivity.getContext();
		ProcessData assignedActivityContext = null;
		if (workItem.getContext() == null || wfTaskProcessor.getWorkItem().getContext() == null) {
			activityContext = assignedActivity.getContext();
		} else {
			activityContext = workItem.getContext();

			//This context is used to get the map of display names. SPR : 1538443 
			assignedActivityContext = assignedActivity.getContext();
		}

		//Shital: Non-member user should be able to see variables, so bypassing access control for fetching template object
		final WfAssignedActivityTemplate activityTemplate ;
		boolean access = SessionServerHelper.manager.setAccessEnforced (false);
		try {
			activityTemplate = (WfAssignedActivityTemplate)assignedActivity.getTemplate().getObject();
		}catch(WTRuntimeException e){
			throw e;
		}finally {
			SessionServerHelper.manager.setAccessEnforced (access);
		}

		final ProcessDataInfo processDataInfo = (ProcessDataInfo) activityTemplate.getContextSignature();
		WfVariableInfo[] varInfoArr = null;
		WfVariableInfo[] varInfoArrTemp = processDataInfo.getVariableList();


		String ignoreWorkflowAttrList = "";
		final String TOKEN_SEPARATORS = ",";
		varInfoArr = new WfVariableInfo[varInfoArrTemp.length];

		WTProperties properties=null;
		try {
			properties = WTProperties.getLocalProperties();
			if(properties != null){ // This specifies comma sepearate list of workflow variables to be ignored on UI
				ignoreWorkflowAttrList = properties.getProperty("wt.workflow.ignoreAttrList","");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(componentId.equals(ALL_ACTIVITY_VARIABLES)){
			LOGGER.debug("getCustomVariables() - ignoreWorkflowAttrList: " + ignoreWorkflowAttrList);
			if ((ignoreWorkflowAttrList != null) && (ignoreWorkflowAttrList.length() > 0)) {
				int cnt=0;
				for (WfVariableInfo varInfo : varInfoArrTemp) {
					StringTokenizer variable_tokens = new StringTokenizer(ignoreWorkflowAttrList, TOKEN_SEPARATORS);
					boolean flag=false;
					while (variable_tokens.hasMoreTokens()) {
						String current_variable = (String) variable_tokens.nextToken().trim();
						if(varInfo.getName().equalsIgnoreCase(current_variable)){	 	           		
							flag=true;
							break;
						}	 	        	 	        	
					}	 	
					if(!flag)
						varInfoArr[cnt++] = varInfo;	 	       
				}	 	       
			}
			else{
				varInfoArr=varInfoArrTemp;
			}
		}
		else{
			LOGGER.debug("Copied Variable for componentId " + componentId);
			varInfoArr=varInfoArrTemp;
		}



		int width = DEFAULT_WIDTH;
		int height = DEFAULT_HEIGHT;

		String propertyWidthHeight = (String) modelContext.getDescriptor()
				.getProperty(WIDTH_HEIGHT);

		int[] customWidthHeight = getCustomWidthHeight(propertyWidthHeight);

		if(customWidthHeight[0] > 0)
			width = customWidthHeight[0];
		if(customWidthHeight[1] > 0)
			height = customWidthHeight[1];


		final GUIComponentArray result = new GUIComponentArray();

		LOGGER.debug("getCustomVariables() - varInfoArr: " + Arrays.deepToString(varInfoArr));

		result.setId(ComponentId.WORKITEM_CUSTOMVARIABLE.getId());


		WfVariable wfv,wfv1;
		Class variableClass;
		String varTypeName = "";
		String variableName = "";
		String variableDisplayName = "";
		TextDisplayComponent displayComponent;

		for (final WfVariableInfo varInfo : varInfoArr) {


			if (!displayCustomVariable(varInfo, componentId))
				continue;

			if(assignedActivityContext!=null)
			{
				activityContext.getVariable(varInfo.getName()).setDisplayNameMap(assignedActivityContext.getVariable(varInfo.getName()).getDisplayNameMap());
			}

			wfv = activityContext.getVariable(varInfo.getName());
			if(assignedActivityContext!=null){
				wfv1 = assignedActivityContext.getVariable(varInfo.getName());
				variableDisplayName=wfv1.getDisplayName(locale);
			}else{
				variableDisplayName=wfv.getDisplayName(locale);
			}                

			variableClass = wfv.getVariableClass();
			varTypeName = wfv.getTypeName();
			variableName=wfv.getName();

			if(variableDisplayName.equalsIgnoreCase("")){

				variableDisplayName=wfv.getDisplayName();
				if(variableDisplayName.equalsIgnoreCase(""))
					variableDisplayName=wfv.getName();
			}


			result.setRequired(varInfo.isRequired());

			if(componentId.equals("all_activity_variables")){

				if (wt.fc.WTObject.class.isAssignableFrom(variableClass)) {
					try {
						displayWTObjectAssignables(variableName, variableDisplayName, varInfo.isReadOnly(), varInfo.isRequired(), result);
						continue;

					} catch (WTException e) {
						e.printStackTrace();
					}
				}
				// Check for enumerated types
				if (EnumeratedType.class.isAssignableFrom(variableClass)) {

					if (varInfo.isReadOnly()) {
						String enumType = (activityContext.getValue (variableName) == null ? BLANK_SPACE :
							((EnumeratedType) activityContext.getValue (variableName)).getDisplay (locale));

						displayFormatting(variableDisplayName, result);
						result.addGUIComponent(createLabelForTextDisplayComponent(enumType));
						result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
					} else {

						EnumeratedType selectedValue = null;

						// get the current value of the variable from the activity
						// context
						if (activityContext.getValue(variableName) != null) {
							Object val = activityContext.getValue(variableName);
							if (val instanceof String)
								selectedValue = EnumeratedTypeUtil.toEnumeratedType((String)val);
							else if (val instanceof EnumeratedType)
								selectedValue = ((EnumeratedType) val);

						}
						displayFormatting(variableDisplayName, result);
						ComboBox comboBox=enumeratedTypeSelector(variableClass, selectedValue, variableName, variableDisplayName, locale, result);
						result.addGUIComponent(comboBox);



					}
					continue;
				}
				// Strings -> textarea
				if (varTypeName.equals("java.lang.String")) {

					String propertyTruncationLength = (String) modelContext.getDescriptor()
							.getProperty("truncation_length");

					if(propertyTruncationLength !=null && !propertyTruncationLength.equals("")){				
						TRUNCATION_LENGTH = Integer.parseInt(propertyTruncationLength);
					}

					String displayValue = (String) activityContext.getValue(variableName);
					displayFormatting(variableDisplayName, result);
					displayTextAreaGui(variableName, displayValue, varInfo.isReadOnly(), result,width,height);

				}

				// Boolean -> checkbox
				else if ((varTypeName.equals("java.lang.Boolean")) || (varTypeName.equals("boolean"))) {
					boolean checkboxval = ((Boolean) activityContext.getValue(variableName)).booleanValue();
					displayFormatting(variableDisplayName, result);
					CheckBox chkbox = createCheckBoxComponent(variableName, checkboxval);
					if (varInfo.isReadOnly()) {
						chkbox.setEditable(false);
					} 
					result.addGUIComponent(chkbox);
				}

				// java.net.URL -> textbox + link
				else if (varTypeName.equals("java.net.URL")) {

					String displayValue = activityContext.getValue (variableName) == null ? "" :activityContext.getValue(variableName).toString();
					displayFormatting(variableDisplayName, result);
					displayTextGuiWithURL(variableName,displayValue,varInfo.isReadOnly(), result,width);

				}

				// Date -> textbox
				else if (varTypeName.equals("java.util.Date") ) {
					ResourceBundle dateFormatRB = java.util.ResourceBundle.getBundle("wt.util.utilResource", locale);

					String displayValue = "";
					String dateFormat = dateFormatRB.getString(wt.util.utilResource.WF_STANDARD_DATE_ONLY_FORMAT);
					if (activityContext.getValue(variableName) != null) {
						displayValue = wt.util.WTStandardDateFormat.format((java.util.Date) activityContext.getValue(variableName), dateFormat);
					}
					displayFormatting(variableDisplayName, result);

					if (varInfo.isReadOnly()) {
						result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));

					} else {
						result.addGUIComponent(createTextBox(variableName,displayValue,varInfo.isReadOnly(), width));
					}
					result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
				}
				//        Date -> textbox
				else if ( varTypeName.equals("wt.workflow.engine.WfDueDate")) {
					ResourceBundle dateFormatRB = java.util.ResourceBundle.getBundle("wt.util.utilResource", locale);
					String dateFormat = dateFormatRB.getString(wt.util.utilResource.WF_STANDARD_DATE_ONLY_FORMAT);

					String displayValue = "";
					if (activityContext.getValue(variableName) != null) {
						displayValue = wt.util.WTStandardDateFormat.format((java.sql.Date) ((WfDueDate) activityContext.getValue(variableName)).getDeadline(),
								dateFormat);
					}
					displayFormatting(variableDisplayName, result);

					if (varInfo.isReadOnly()) {
						result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
					} else {
						result.addGUIComponent(createTextBox(variableName,displayValue,varInfo.isReadOnly(), width));
					}
					result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
				}
				// All others -> textbox
				else {
					String displayString =  (activityContext.getValue (variableName) == null ? null :activityContext.getValue (variableName).toString ());
					displayFormatting(variableDisplayName, result);
					displayTextBoxGui(variableName, displayString ,varInfo.isReadOnly(),  result,width);
				}
				// else if user only requests individual custom variables
			}
			else
			{

				if (wt.fc.WTObject.class.isAssignableFrom(variableClass)) {
					try {
						displayWTObjectAssignablesComponent(variableName, variableDisplayName, varInfo.isReadOnly(), varInfo.isRequired(), result);
						continue ;

					} catch (WTException e) {
						e.printStackTrace();
					}
				}
				// Check for enumerated types
				if (EnumeratedType.class.isAssignableFrom(variableClass)) {
					if (varInfo.isReadOnly()) {
						String enumType = (activityContext.getValue (variableName) == null ? BLANK_SPACE :
							((EnumeratedType) activityContext.getValue (variableName)).getDisplay (locale));
						result.addGUIComponent(createLabelForTextDisplayComponent(enumType));

					} else {

						EnumeratedType selectedValue = null;

						// get the current value of the variable from the activity
						// context
						if (activityContext.getValue(variableName) != null) {
							Object val = activityContext.getValue(variableName);
							if (val instanceof String)
								selectedValue = EnumeratedTypeUtil.toEnumeratedType((String)val);
							else if (val instanceof EnumeratedType)
								selectedValue = ((EnumeratedType) val);

						}

						ComboBox comboBox=enumeratedTypeSelector(variableClass, selectedValue, variableName, variableDisplayName, locale, result);
						result.addGUIComponent(comboBox);

					}
					continue;
				}
				if(SPECIAL_INSTRUCTIONS.equals(componentId) && "java.lang.String".equals(varTypeName)) {
					String displayValue = (String) activityContext.getValue(variableName);
					if(!lwcDisplay)                
						displayFormatting(WTMessage.getLocalizedMessage("wt.workflow.worklist.worklistResource",worklistResource.SPECIAL_INSTRUCTIONS_LABEL), result);
					displayTextAreaGuiComponent(variableName, displayValue, varInfo.isReadOnly(), result,width,height);
				}
				// Strings -> textarea
				else if (varTypeName.equals("java.lang.String")) {
					// Shital: 1958903- Wrap text area in TD if its a special instruction box.
					//if(SPECIAL_INSTRUCTIONS.equals(componentId)) {
					//result.addGUIComponent(applyFormatingOnComponent("<td>"));
					//}
					String displayValue = (String) activityContext.getValue(variableName);
					displayFormatting(variableDisplayName, result);
					displayTextAreaGuiComponent(variableName, displayValue, varInfo.isReadOnly(), result,width,height);
					//if(SPECIAL_INSTRUCTIONS.equals(componentId)) {
					//result.addGUIComponent(applyFormatingOnComponent("</td>"));
					//}
				}

				// Boolean -> checkbox
				else if ((varTypeName.equals("java.lang.Boolean")) || (varTypeName.equals("boolean"))) {
					boolean checkboxval = ((Boolean) activityContext.getValue(variableName)).booleanValue();
					CheckBox chkbox = 	createCheckBoxComponent(variableName, checkboxval);

					if (varInfo.isReadOnly()) {
						chkbox.setEditable(false);
					} 
					result.addGUIComponent(chkbox);
				}

				// java.net.URL -> textbox + link
				else if (varTypeName.equals("java.net.URL")) {

					String displayValue = activityContext.getValue (variableName) == null ? "" :activityContext.getValue(variableName).toString();

					if (varInfo.isReadOnly()) {
						result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));

					} else {
						result.addGUIComponent(createTextBox(variableName,displayValue,varInfo.isReadOnly(), width));

					}   

				}

				// Date -> textbox
				else if (varTypeName.equals("java.util.Date") ) {
					ResourceBundle dateFormatRB = java.util.ResourceBundle.getBundle("wt.util.utilResource", locale);
					String displayValue = "";
					String dateFormat = dateFormatRB.getString(wt.util.utilResource.WF_STANDARD_DATE_ONLY_FORMAT);
					if (activityContext.getValue(variableName) != null) {
						displayValue = wt.util.WTStandardDateFormat.format((java.util.Date) activityContext.getValue(variableName), dateFormat);
					}


					if (varInfo.isReadOnly()) {
						result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
					} else {
						result.addGUIComponent(createTextBox(variableName, displayValue, varInfo.isReadOnly(), width));
					}
				}
				//        Date -> textbox
				else if ( varTypeName.equals("wt.workflow.engine.WfDueDate")) {
					ResourceBundle dateFormatRB = java.util.ResourceBundle.getBundle("wt.util.utilResource", locale);
					String dateFormat = dateFormatRB.getString(wt.util.utilResource.WF_STANDARD_DATE_ONLY_FORMAT);
					String displayValue = "";
					if (activityContext.getValue(variableName) != null) {
						displayValue = wt.util.WTStandardDateFormat.format((java.sql.Date) ((WfDueDate) activityContext.getValue(variableName)).getDeadline(),dateFormat);
					}

					if (varInfo.isReadOnly()) {
						result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
					} else {
						result.addGUIComponent(createTextBox(variableName, displayValue, varInfo.isReadOnly(), width));
					}
				}
				// All others -> textbox
				else {
					String displayString =  (activityContext.getValue (variableName) == null ? null :activityContext.getValue (variableName).toString ());
					result.addGUIComponent(createTextBox(variableName, displayString, varInfo.isReadOnly(), width));
				}
				// else if user only requests individual custom variables


			}
		}
		result.setRequired(false);
		return result;
	}


	private CheckBox createCheckBoxComponent(String variableName, boolean checkboxval)
	{
		CheckBox chkbox = new CheckBox();
		chkbox.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + variableName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
		chkbox.setChecked(checkboxval);
		chkbox.setColumnName("");
		return chkbox;
	}

	private TextDisplayComponent applyFormatingOnComponent(String format)
	{
		TextDisplayComponent displayComponent;
		displayComponent = new TextDisplayComponent("");
		displayComponent.setValue(format);
		displayComponent.setCheckXSS(false);
		return displayComponent;
	}

	private void displayFormatting(String displayName,final GUIComponentArray result){
		if(lwcDisplay) {
			displayFormatting(displayName, result, lwcDisplay);
		} else {
			result.addGUIComponent(applyFormatingOnComponent("<tr><td align=\"right\" valign=\"top\" nowrap><FONT class=tabledatafont>"));

			if(result.isRequired())
			{
				result.addGUIComponent(applyFormatingOnComponent("*"));
			}
			result.addGUIComponent(applyFormatingOnComponent("<b>" + displayName + ":</b>"));

			result.addGUIComponent(applyFormatingOnComponent("</td><td align=\"left\" valign=\"top\"><FONT class=tabledatafont>"));
		}
	}

	private void displayFormatting(String displayName, final GUIComponentArray result, final boolean lwcDisplay){
		result.addGUIComponent(applyFormatingOnComponent("<tr>"));

		if(result.isRequired())
		{
			result.addGUIComponent(applyFormatingOnComponent("<td class=\"attributePanel-asterisk\">*</td><td align=\"left\" valign=\"top\" nowrap><FONT class=tabledatafont>"));
		} else {
			result.addGUIComponent(applyFormatingOnComponent("<td class=\"attributePanel-asterisk\"></td><td align=\"left\" valign=\"top\" nowrap><FONT class=tabledatafont>"));
		}
		result.addGUIComponent(applyFormatingOnComponent("<b>" + displayName + ":</b>"));

		result.addGUIComponent(applyFormatingOnComponent("</td><td align=\"left\" valign=\"top\"><FONT class=tabledatafont>"));
	}
	private void displayTextGuiWithURL(String displayName, String displayValue, boolean isReadOnly,
			final GUIComponentArray result,int width) {
		TextDisplayComponent displayComponent;

		if (isReadOnly) {
			result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
			result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
		} else {

			result.addGUIComponent(createTextBox(displayName,displayValue,isReadOnly, width));
			if (displayValue != null) {
				result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
			}

			result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
		}

	}


	private AbstractGuiComponent createTextArea(String displayName, String displayValue,boolean isReadOnly,int width,int height)
	{

		StringInputComponent textInput=new StringInputComponent(displayName,1000,true); 

		//TextArea textInput;
		//textInput = new TextArea();
		textInput.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + displayName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
		//textInput.setLabel(displayName);
		textInput.setId(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + displayName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
		textInput.setValue(displayValue);
		textInput.setReadOnly(isReadOnly);
		textInput.setRenderLabel(false);
		if(height > 1)
			textInput.setInputHeight(height);
		else
			textInput.setInputHeight(2);
		textInput.setInputWidth(width);
		//textInput.setMaxLength(1000);

		return textInput;

	}

	private void displayTextAreaGui(String displayName, String displayValue, boolean isReadOnly, final GUIComponentArray result,int width,int height) {
		TextDisplayComponent displayComponent;
		LOGGER.debug("displayTextAreaGui,?????????????????????...,displayName:" + displayName);
		//LOGGER.debug("displayName : " + displayName + ",displayValue:" + displayValue);
		
		final TextArea textInput;
		if (isReadOnly) {
			LOGGER.debug("???????????????,???html????????????,displayValue:" + displayValue);
			//???html????????????
			RawStringDisplayComponent temp = new RawStringDisplayComponent("",displayValue);
			result.addGUIComponent(temp);
			
/*			displayComponent = createLabelForTextDisplayComponent(displayValue);
			displayComponent.setTruncationLength(TRUNCATION_LENGTH);
			displayComponent.setCheckXSS(false);
			result.addGUIComponent(displayComponent);
*/
		} else {
			result.addGUIComponent(createTextArea(displayName,displayValue,isReadOnly, width, height));
		}
		result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));

	}


	private TextDisplayComponent createLabelForTextDisplayComponent(String displayValue)
	{
		TextDisplayComponent displayComponent;
		displayComponent = new TextDisplayComponent("");
		displayComponent.setValue(displayValue);
		displayComponent.setCheckXSS(false);
		return displayComponent;
	}


	private AbstractGuiComponent createTextBox(String displayName, String displayValue,boolean isReadOnly,int width)
	{
		StringInputComponent textbox = new StringInputComponent(displayName, 150, false);
		textbox.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + displayName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
		//textbox.setLabel(displayName);
		textbox.setId(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + displayName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
		textbox.setValue(displayValue);
		textbox.setReadOnly(isReadOnly);
		textbox.setInputWidth(width);
		//textbox.setMaxLength(150);
		textbox.setRenderLabel(false);
		return textbox;
	}

	private void displayTextBoxGui(String displayName, String displayValue, boolean isReadOnly, final GUIComponentArray result,int width) {
		TextDisplayComponent displayComponent;
		if (isReadOnly) {
			result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));

		} else {
			result.addGUIComponent(createTextBox(displayName,displayValue,isReadOnly, width));
		}
		result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
	}

	private ComboBox createTeamComponent(ProcessData activityContext,String variableName,boolean isRequired)throws WTException 
	{
		String contextEnumValue = null;
		if (activityContext.getValue(variableName) != null) {
			TeamReference ref = TeamReference.newTeamReference((wt.team.Team) activityContext.getValue(variableName));
			contextEnumValue = ref.getIdentity();
		}
		Vector allTeams = TeamHelper.service.findTeams();
		ReferenceFactory referenceFactory = new ReferenceFactory();
		TeamReference tr;
		Vector<String> values = new Vector<String>(allTeams.size());
		Vector<String> displays = new Vector<String>(allTeams.size());
		int selected = 0;
		String teamId;
		String value;
		boolean addBlank = !isRequired;
		for (int j = 0; j < allTeams.size(); j++) {
			tr = (TeamReference) allTeams.elementAt(j);
			value = referenceFactory.getReferenceString(tr);
			values.addElement(value);
			teamId = tr.getIdentity();
			displays.addElement(teamId);
			if ((contextEnumValue != null) && (contextEnumValue.equals(teamId))) {
				selected = j;
				if (addBlank) {
					selected++;
				}
			}
		}

		ArrayList<String> arlist = new ArrayList<String>();
		arlist.add("");
		arlist.addAll(displays);
		ArrayList<String> valuesArlist = new ArrayList<String>();
		valuesArlist.add("");
		valuesArlist.addAll(values);
		ComboBox comboboxTeams = new ComboBox(valuesArlist, arlist, new ArrayList());
		comboboxTeams.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + variableName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);

		String selectedTeam = ((activityContext.getValue(variableName) == null) ?
				null : ((wt.team.Team) activityContext.getValue(variableName)).getName());
		if (selectedTeam != null)
			comboboxTeams.setSelected(selectedTeam);
		else
			comboboxTeams.setSelected("");

		return comboboxTeams;

	}

	private ComboBox createTeamTemplateComponent(ProcessData activityContext,String variableName,boolean isRequired)throws WTException 
	{

		String contextEnumValue = null;
		if (activityContext.getValue(variableName) != null) {
			TeamTemplateReference ref = TeamTemplateReference.newTeamTemplateReference((TeamTemplate) activityContext.getValue(variableName));
			contextEnumValue = ref.getIdentity();
		}

		WTContainerRef contextRef = null;
		Object contextObj = wfTaskProcessor.getContextObj();
		if (contextObj instanceof WTContained) {
			contextRef = ((WTContained) contextObj).getContainerReference();
		}

		Vector allTeamTemplates = TeamHelper.service.findTeamTemplates(contextRef);
		ReferenceFactory referenceFactory = new ReferenceFactory();
		TeamTemplateReference ttr;
		Vector<String> values = new Vector<String>(allTeamTemplates.size());
		Vector<String> displays = new Vector<String>(allTeamTemplates.size());
		int selected = 0;
		String teamTemplateId;
		String value;
		boolean addBlank = !isRequired;
		TeamTemplate teamtmpl = null;
		for (int j = 0; j < allTeamTemplates.size(); j++) {
			ttr = (TeamTemplateReference) allTeamTemplates.elementAt(j);
			teamtmpl = (TeamTemplate)ttr.getObject();
			if(!teamtmpl.isEnabled())
				continue;
			value = referenceFactory.getReferenceString(ttr);
			values.addElement(value);
			teamTemplateId = ttr.getIdentity();
			displays.addElement(teamTemplateId);
			if ((contextEnumValue != null) && (contextEnumValue.equals(teamTemplateId))) {
				selected = j;
				if (addBlank) {
					selected++;
				}
			}
		}
		ArrayList<String> arlist = new ArrayList<String>();
		arlist.add("");
		arlist.addAll(displays);
		ArrayList<String> valuesArlist = new ArrayList<String>();
		valuesArlist.add("");
		valuesArlist.addAll(values);
		ComboBox comboboxTeams = new ComboBox(valuesArlist, arlist, new ArrayList());
		comboboxTeams.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + variableName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);

		String selectedTeam = ((activityContext.getValue(variableName) == null) ? null : ((TeamTemplate) activityContext.getValue(variableName))
				.getName());
		if (selectedTeam != null)
			comboboxTeams.setSelected(selectedTeam);
		else
			comboboxTeams.setSelected("");

		return comboboxTeams;


	}

	private GUIComponentArray createFormatContentHolder(WTObject myobject,boolean normalRoute )throws WTException
	{
		boolean origEnforce = false;
		GUIComponentArray gui = new GUIComponentArray();
		try
		{

			FormatContentHolder holder = null;
			ContentItem contentItem = null;

			origEnforce = SessionServerHelper.manager.setAccessEnforced(false);
			holder = (FormatContentHolder) ContentHelper.service.getContents((ContentHolder) myobject);
			contentItem = ContentHelper.getPrimary( holder );
			String detailImage = "/netmarkets/images/details.gif";
			if (contentItem != null) 
			{                    	


				NmAction viewAction = NmActionServiceHelper.service.getAction("object","view");
				String detailPath = viewAction.getUrl(); //Info page path.

				String detailLink =  WfHtmlFormat.getBaseURL();
				detailLink += detailPath;

				detailLink += "?oid=";
				detailLink += new ReferenceFactory().getReferenceString((Persistable) myobject); 

				UrlDisplayComponent nameDisplay = new UrlDisplayComponent();
				String displayName = AttachmentsDataUtilityHelper.getDisplayName(contentItem);
				nameDisplay.setLabelForTheLink(displayName);
				nameDisplay.setLink(detailLink);
				gui.addGUIComponent(nameDisplay);


				IconComponent detailIconDisplay = new IconComponent();
				detailIconDisplay.setSrc(WfHtmlFormat.getBaseURL() + detailImage);
				detailIconDisplay.setUrl(detailLink);
				gui.addGUIComponent(detailIconDisplay);

				//Now if user has read permission and pbo can have a forum, create a link to forum
				if (myobject instanceof wt.workflow.forum.SubjectOfForum) {
					if ( AccessControlHelper.manager.hasAccess( myobject, AccessPermission.READ )) {
						String link = NetmarketsHref.getForumHref((Persistable)myobject);
						String forumLink = "javascript:var forumWindow = wfWindowOpen('" + link + "','nmforumdiscuss',config='resizable=yes,scrollbars=yes,menubar=no,toolbar=no,location=no,status=no')";                                
						String forumImage = "/netmarkets/images/forum.gif";
						IconComponent discussion = new IconComponent();
						discussion.setTooltip("Discuss");
						discussion.setSrc(WfHtmlFormat.getBaseURL() + forumImage);
						discussion.setUrl(forumLink);
						gui.addGUIComponent(discussion);
					}
				}                    	

				AbstractGuiComponent formatIconDisplay = AttachmentsDataUtilityHelper.getContentFormatIcon(contentItem, locale);
				gui.addGUIComponent(formatIconDisplay);


			} else {
				NmAction viewAction = NmActionServiceHelper.service.getAction("object","view");
				String detailPath = viewAction.getUrl(); //Info page path.

				String detailLink =  WfHtmlFormat.getBaseURL();
				detailLink += detailPath;

				detailLink += "?oid=";
				detailLink += new ReferenceFactory().getReferenceString((Persistable) myobject); 

				UrlDisplayComponent nameDisplay = new UrlDisplayComponent();
				String displayName = ObjectInfoUtil.GetPDMNumber(myobject);
				nameDisplay.setLabelForTheLink(displayName);
				nameDisplay.setLink(detailLink);
				gui.addGUIComponent(nameDisplay);


				IconComponent detailIconDisplay = new IconComponent();
				detailIconDisplay.setSrc(WfHtmlFormat.getBaseURL() + detailImage);
				detailIconDisplay.setUrl(detailLink);
				gui.addGUIComponent(detailIconDisplay);

				//Now if user has read permission and pbo can have a forum, create a link to forum
				if (myobject instanceof wt.workflow.forum.SubjectOfForum) {
					if ( AccessControlHelper.manager.hasAccess( myobject, AccessPermission.READ )) {
						String link = NetmarketsHref.getForumHref((Persistable)myobject);
						String forumLink = "javascript:var forumWindow = wfWindowOpen('" + link + "','nmforumdiscuss',config='resizable=yes,scrollbars=yes,menubar=no,toolbar=no,location=no,status=no')";                                
						String forumImage = "/netmarkets/images/forum.gif";
						IconComponent discussion = new IconComponent();
						discussion.setTooltip("Discuss");
						discussion.setSrc(WfHtmlFormat.getBaseURL() + forumImage);
						discussion.setUrl(forumLink);
						gui.addGUIComponent(discussion);
					}
				}
			}
		}            
		catch (Exception e) 
		{            		
			/*If we can't get the image just keep going*/
		}
		finally{
			SessionServerHelper.manager.setAccessEnforced(origEnforce);
		}

		return gui;
	}

	private void displayWTObjectAssignables(String variableName, String variableDisplayName, boolean isReadOnly, boolean isRequired, final GUIComponentArray result)
			throws WTException {

		final WfActivity assignedActivity = wfTaskProcessor.getActivity();
		ProcessData activityContext = assignedActivity.getContext();
		if (workItem.getContext() == null || wfTaskProcessor.getWorkItem().getContext() == null) {
			activityContext = assignedActivity.getContext();
		} else {
			activityContext = workItem.getContext();
		}
		WfVariable wfv = activityContext.getVariable(variableName);
		Class variableClass = wfv.getVariableClass();
		String varTypeName = wfv.getTypeName();

		// WTPrincipal

		if (wt.org.WTPrincipal.class.isAssignableFrom(variableClass)) {

			if (isReadOnly) {
				String str = (activityContext.getValue(variableName) == null ? BLANK_SPACE
						: (wt.org.WTUser.class.isAssignableFrom(variableClass) ? ((WTUser) activityContext.getValue(variableName)).getFullName()
								: ((WTPrincipal) activityContext.getValue(variableName)).getName()));
				/*
				 * Wrt code of TextDisplaycomponent and readonly value with
				 * label
				 */
				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(createLabelForTextDisplayComponent(str));
				result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));


			} else {

				displayFormatting(variableDisplayName,result);
				AbstractGuiComponent  component=principalSelector(varTypeName, (WTPrincipal) activityContext.getValue(variableName), variableName, variableDisplayName, result);
				if(component instanceof ComboBox)
				{
					ComboBox comboboxUsers=(ComboBox)component;
					result.addGUIComponent(comboboxUsers);
				}

			}
		}
		// Teams
		else if (varTypeName.equals("wt.team.Team")) {
			if (isReadOnly) {
				String team = ((activityContext.getValue(variableName) == null) ? null : ((wt.team.Team) activityContext.getValue(variableName)).getName());

				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(createLabelForTextDisplayComponent(team));
				result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
			}

			else {
				// Build a selectable list of teams
				ComboBox comboboxTeams = createTeamComponent(activityContext,variableName,isRequired);
				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(comboboxTeams);
			}
		}
		// Team Templates
		else if (varTypeName.equals("wt.team.TeamTemplate")) {
			if (isReadOnly) {
				String team = ((activityContext.getValue(variableName) == null) ?
						null : ((TeamTemplate) activityContext.getValue(variableName)).getName());
				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(createLabelForTextDisplayComponent(team));
				result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
			}
			else {
				// Build a selectable list of team templates
				ComboBox comboboxTeams = createTeamTemplateComponent(activityContext,variableName,isRequired);
				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(comboboxTeams);
			}
		}
		else {

			boolean normalRoute = true;
			WTObject myobject = (WTObject) activityContext.getValue (variableName);

			if((myobject instanceof FormatContentHolder)&&(InstalledProperties.isInstalled(InstalledProperties.PDML_PROI)))
			{
				displayFormatting(variableDisplayName, result);
				result.addGUIComponent(createFormatContentHolder(myobject,normalRoute));
			} else 
			{
				// Fall Back for WTObject descendents not specially handled above
				displayFormatting(variableDisplayName, result);            
				String displayValue = WfHtmlFormat.createObjectLink ((WTObject) activityContext.getValue (variableName), null, locale);
				result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
				result.addGUIComponent(applyFormatingOnComponent("</td></tr>"));
			}      


		}

	}

	/**
	 * Construct a drop list of the available enumerated type values
	 *
	 * @param enumeratedTypeClass The class of the enumerated type with which the list
	 *                            will be populated
	 * @oaram selectedItem       The internal value of the item to select in the list
	 *
	 * @param controlName        This should be the name given to the process variable.
	 *                      It will be used to determine what
	 *                           variable will be updated when this form is posted.
	 * @param controlDisplayName The name to be displayed for the drop list
	 * @return HTML code to display a choice control populated with the Enumerated Type values
	 *
	 * <BR><BR><B>Supported API: </B>false
	 *
	 * @exception WTException
	 */
	public ComboBox enumeratedTypeSelector(Class enumeratedTypeClass, EnumeratedType selectedItem, String controlName, String controlDisplayName, Locale locale, final GUIComponentArray result)
	{
		Vector<String> values = new Vector<String>();
		Vector<String> displayValues = new Vector<String>();
		int selectedIndex = 0;
		String simpleName = getSimpleName(enumeratedTypeClass);
		try {

			Method method = null;
			method = enumeratedTypeClass.getMethod("get" + simpleName + "Set", null);
			Object obj = method.invoke(null, null);
			EnumeratedType[] valueSet = (EnumeratedType[]) obj;
			for (int i = 0; i < valueSet.length; i++) {
				if (valueSet[i].isSelectable()) {
					values.addElement(valueSet[i].toString());
					displayValues.addElement(valueSet[i].getDisplay(locale));
					if (valueSet[i].equals(selectedItem)) {
						selectedIndex = i + 1;
					}
				}
			}
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
		} catch (java.lang.NoSuchMethodException nsm) {
			nsm.printStackTrace();
		} catch (java.lang.IllegalAccessException iae) {
			iae.printStackTrace();
		}
		ArrayList<String> arlist = new ArrayList<String>();
		arlist.add("");
		arlist.addAll(displayValues);
		ArrayList<String> valuesArlist = new ArrayList<String>();
		valuesArlist.add("");
		valuesArlist.addAll(values);
		ComboBox comboEnumeratedType = new ComboBox(valuesArlist, arlist, new ArrayList());
		comboEnumeratedType.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + controlName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);

		if (selectedItem != null)
			comboEnumeratedType.setSelected(selectedItem.toString());
		else
			comboEnumeratedType.setSelected("");

		return comboEnumeratedType;

	}
	/**
	 * Construct a drop list of the available principals
	 *
	 * @param type
	 *            The type of principals to include in the drop list
	 *            wt.org.WTPrincipal wt.org.WTUser wt.org.WTGroup
	 * @param selectedPrincipal
	 *            The principal in the drop list to select
	 * @param controlName
	 *            This should be the name given to the process variable.
	 *            It will be used to determine what variable will be updated
	 *            when this form is posted.
	 * @param controlDisplayName
	 *            The name to be displayed for the drop list,
	 * <BR>
	 * <BR>
	 * <B>Supported API: </B>false
	 *
	 * @exception WTException
	 */
	public AbstractGuiComponent principalSelector(String type, WTPrincipal selectedPrincipal, String controlName, String controlDisplayName, final GUIComponentArray result) throws WTException
	{

		AbstractGuiComponent pickerInputComponent = null;
		try
		{

			Vector<String> values = new Vector<String>();
			Vector<String> displayValues = new Vector<String>();

			if(DISPLAY_WTUSER_PICKER)
			{
				if ((type.equals("wt.org.WTPrincipal")) || (type.equals("wt.org.WTUser")))
				{
					Map<Object, Object> config = modelContext.getDescriptor().getProperties();
					String tempControlName = new String(controlName);
					// pickerId value has to have a substring of the value of hidden_id
					if(tempControlName.contains(" "))
						tempControlName = tempControlName.replace(" ", "_");
					config.put(PickerRenderConfigs.PICKER_ID, NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + tempControlName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
					config.put(PickerRenderConfigs.OBJECT_TYPE, "wt.org.WTUser");
					// To use full search picker:
					config.put("pickerType", "search");
					// To use auto-select version of picker, use the following instead:
					config.put(PickerRenderConfigs.PICKER_ATTRIBUTES, "fullName");
					config.put(PickerRenderConfigs.READ_ONLY_TEXTBOX, "true");
					// to see only users in current container/org:

					WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
					WTOrganization wtorg = user.getOrganization();
					OrgContainer c = WTContainerHelper.service.getOrgContainer(wtorg);

					if ( c != null && c.isRestrictedDirectorySearchScope() ) 
						config.put("containerRef", modelContext.getNmCommandBean().getContainer().getPersistInfo().getObjectIdentifier().toString());
					else
						config.put("containerRef", "");

					config.put(PickerRenderConfigs.PICKER_CALLBACK, "PickerInputComponentCallback");
					config.put(PickerRenderConfigs.SUGGEST_SERVICE_KEY, "userPicker");
					config.put(PickerRenderConfigs.INCLUDE_TII, "false");
					config.put(PickerRenderConfigs.SHOW_ROLES, "false");
					config.put("CONTAINER_TEAM_MEMBERS_ONLY", "true");
					if(selectedPrincipal != null)
					{
						pickerInputComponent = new PickerInputComponent("",selectedPrincipal.getPrincipalDisplayIdentifier(), PickerRenderConfigs.getPickerConfigs(config));
					}
					else
					{
						pickerInputComponent = new PickerInputComponent("","", PickerRenderConfigs.getPickerConfigs(config));
					}

					pickerInputComponent.setColumnName(AttributeDataUtilityHelper.getColumnName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + tempControlName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR, workItem, modelContext));
					pickerInputComponent.setRequired(AttributeDataUtilityHelper.isInputRequired(modelContext));

					//displayFormatting(controlDisplayName,result);
					result.addGUIComponent(pickerInputComponent);
					PushButton clearButton = new PushButton("Clear");
					clearButton.setName("Clear_" + tempControlName);
					clearButton.setId("Clear_" + tempControlName);
					clearButton.addJsAction("onClick","clearUserPickerTextBox('"+clearButton.getName()+"')");
					result.addGUIComponent(clearButton);
				}
				if ((type.equals("wt.org.WTPrincipal")) || (type.equals("wt.org.WTGroup"))) 
				{
					// get Groups
					wt.org.WTGroup group;
					Vector tmpDisplayValues = (Vector) MethodContext.getContext().get(WF_GROUPS);
					Vector tmpValues = (Vector) MethodContext.getContext().get(WF_DISPLAY_GROUPS);

					if (tmpDisplayValues == null || tmpValues == null) 
					{
						/*Enumeration e = new SortedEnumeration(OrganizationServicesMgr.allGroups(), new CollationKeyFactory(WTContext.getContext().getLocale()));

		    	           while (e.hasMoreElements()) {
		    	              group = (wt.org.WTGroup) e.nextElement();
		    	              values.addElement(group.getName());
		    	              displayValues.addElement(group.getName());
		    	           }*/
						ArrayList providers = null;
						WorkItem currWI = wfTaskProcessor.getWorkItem();
						WfActivity currActivity = (WfActivity)currWI.getSource().getObject();
						providers = getContextProviders(currActivity.getContainerReference());

						PrincipalSpec principalSpec = new PrincipalSpec(currActivity.getContainerReference(), WTGroup.class);
						principalSpec.setInternalGroupSet(ContainerTeamHelper.ORG_GROUPS);

						DirectoryContextProvider[] dirContextProviders = (DirectoryContextProvider[]) providers.toArray(new DirectoryContextProvider[providers.size()]);
						for (int i = 0; i < dirContextProviders.length; ++i) {
							dirContextProviders[i].setInternalGroupsSearchCriteria(principalSpec);
						}
						Enumeration e1 = OrganizationServicesHelper.manager.queryPrincipals(WTGroup.class, WTGroup.NAME + "=*" , dirContextProviders);

						while(e1.hasMoreElements())
						{
							Object obj = e1.nextElement();
							if(!obj.getClass().equals(wt.org.WTOrganization.class))
							{
								group = (WTGroup)obj;
								values.addElement(group.getName());
								displayValues.addElement(group.getName());
							}
						}
						MethodContext.getContext().put(WF_GROUPS, displayValues);
						MethodContext.getContext().put(WF_DISPLAY_GROUPS, values);
					}
					else 
					{
						displayValues = tmpDisplayValues;
						values = tmpValues;
					}

					ArrayList<String> arlist = new ArrayList<String>();
					arlist.add("");
					arlist.addAll(displayValues);
					ArrayList<String> valuesArlist = new ArrayList<String>();
					valuesArlist.add("");
					valuesArlist.addAll(values);
					ComboBox comboboxUsers =null;
					comboboxUsers = new ComboBox(valuesArlist,arlist,new ArrayList());
					comboboxUsers.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + controlName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
					if(selectedPrincipal != null)
						comboboxUsers.setSelected(selectedPrincipal.getName());
					else
						comboboxUsers.setSelected("");
					pickerInputComponent=comboboxUsers;
					result.addGUIComponent(pickerInputComponent);
				}
			}
			else
			{
				if ((type.equals("wt.org.WTPrincipal")) || (type.equals("wt.org.WTUser")))
				{
					// get Users
					WTUser user;
					// Enumeration e = new
					// SortedEnumeration(OrganizationServicesMgr.allUsers(),
					// new
					// CollationKeyFactory(WTContext.getContext().getLocale()));

					Vector tmpDisplayValues = (Vector) MethodContext.getContext().get(WF_USERS);
					Vector tmpValues = (Vector) MethodContext.getContext().get(WF_DISPLAY_USERS);

					if (tmpDisplayValues == null || tmpValues == null)
					{
						Enumeration e = new SortedEnumByPrincipal(OrganizationServicesMgr.allUsers(), false, 1);

						while (e.hasMoreElements())
						{
							user = (WTUser) e.nextElement();
							values.addElement(user.getName());
							displayValues.addElement (SortedEnumByPrincipal.getLastNameFirstName(user));
						}
						MethodContext.getContext().put(WF_USERS,displayValues);
						MethodContext.getContext().put(WF_DISPLAY_USERS,values);
					}
					else{
						displayValues = tmpDisplayValues;
						values = tmpValues;
					}
				}

				if ((type.equals("wt.org.WTPrincipal")) || (type.equals("wt.org.WTGroup"))) 
				{
					// get Groups
					wt.org.WTGroup group;
					Vector tmpDisplayValues = (Vector) MethodContext.getContext().get(WF_GROUPS);
					Vector tmpValues = (Vector) MethodContext.getContext().get(WF_DISPLAY_GROUPS);

					if (tmpDisplayValues == null || tmpValues == null) 
					{
						/*Enumeration e = new SortedEnumeration(OrganizationServicesMgr.allGroups(), new CollationKeyFactory(WTContext.getContext().getLocale()));

				           while (e.hasMoreElements()) {
				              group = (wt.org.WTGroup) e.nextElement();
				              values.addElement(group.getName());
				              displayValues.addElement(group.getName());
				           }*/
						ArrayList providers = null;
						WorkItem currWI = wfTaskProcessor.getWorkItem();
						WfActivity currActivity = (WfActivity)currWI.getSource().getObject();
						providers = getContextProviders(currActivity.getContainerReference());

						PrincipalSpec principalSpec = new PrincipalSpec(currActivity.getContainerReference(), WTGroup.class);
						principalSpec.setInternalGroupSet(ContainerTeamHelper.ORG_GROUPS);

						DirectoryContextProvider[] dirContextProviders = (DirectoryContextProvider[]) providers.toArray(new DirectoryContextProvider[providers.size()]);
						for (int i = 0; i < dirContextProviders.length; ++i) {
							dirContextProviders[i].setInternalGroupsSearchCriteria(principalSpec);
						}
						Enumeration e1 = OrganizationServicesHelper.manager.queryPrincipals(WTGroup.class, WTGroup.NAME + "=*" , dirContextProviders);

						while(e1.hasMoreElements())
						{
							Object obj = e1.nextElement();
							if(!obj.getClass().equals(wt.org.WTOrganization.class))
							{
								group = (WTGroup)obj;
								values.addElement(group.getName());
								displayValues.addElement(group.getName());
							}
						}
						MethodContext.getContext().put(WF_GROUPS, displayValues);
						MethodContext.getContext().put(WF_DISPLAY_GROUPS, values);
					}
					else 
					{
						displayValues = tmpDisplayValues;
						values = tmpValues;
					}
				}
				ArrayList<String> arlist = new ArrayList<String>();
				arlist.add("");
				arlist.addAll(displayValues);
				ArrayList<String> valuesArlist = new ArrayList<String>();
				valuesArlist.add("");
				valuesArlist.addAll(values);
				ComboBox comboboxUsers =null;
				comboboxUsers = new ComboBox(valuesArlist,arlist,new ArrayList());
				comboboxUsers.setName(NmWorkItemCommands.CUSTOM_ACTIVITY_VAR + controlName + NmWorkItemCommands.CUSTOM_ACTIVITY_VAR);
				if(selectedPrincipal != null)
					comboboxUsers.setSelected(selectedPrincipal.getName());
				else
					comboboxUsers.setSelected("");
				pickerInputComponent=comboboxUsers;
				result.addGUIComponent(pickerInputComponent);
			}
		}
		catch (WTException wte) 
		{
			wte.printStackTrace();
		}
		catch (WTPropertyVetoException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void displayTextAreaGuiComponent(String displayName, String displayValue, boolean isReadOnly, final GUIComponentArray result,int width,int height) {

		TextDisplayComponent displayComponent;
		final TextArea textInput;

		if (isReadOnly) 
			result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
		else 
			result.addGUIComponent(createTextArea(displayName,displayValue,isReadOnly, width, height));

	}

	private void displayWTObjectAssignablesComponent(String variableName, String variableDisplayName, boolean isReadOnly, boolean isRequired, final GUIComponentArray result) 
			throws WTException {

		final WfActivity assignedActivity = wfTaskProcessor.getActivity();
		ProcessData activityContext = assignedActivity.getContext();
		if (workItem.getContext() == null || wfTaskProcessor.getWorkItem().getContext() == null) {
			activityContext = assignedActivity.getContext();
		} else {
			activityContext = workItem.getContext();
		}
		WfVariable wfv = activityContext.getVariable(variableName);
		Class variableClass = wfv.getVariableClass();
		String varTypeName = wfv.getTypeName();

		// WTPrincipal
		if (wt.org.WTPrincipal.class.isAssignableFrom(variableClass)) {

			if (isReadOnly) {
				String str = (activityContext.getValue(variableName) == null ? BLANK_SPACE
						: (wt.org.WTUser.class.isAssignableFrom(variableClass) ? ((WTUser) activityContext.getValue(variableName)).getFullName()
								: ((WTPrincipal) activityContext.getValue(variableName)).getName()));
				/*
				 * Wrt code of TextDisplaycomponent and readonly value with
				 * label
				 */
				result.addGUIComponent(createLabelForTextDisplayComponent(str));


			} else {

				AbstractGuiComponent  component =principalSelector(varTypeName, (WTPrincipal) activityContext.getValue(variableName), variableName, variableDisplayName, result);

				if(component instanceof ComboBox)
				{
					ComboBox comboboxUsers=(ComboBox)component;
					result.addGUIComponent(comboboxUsers);
				}
				else if(component instanceof PickerInputComponent)
				{
					PickerInputComponent pickerInputComponent=(PickerInputComponent)component;
					result.addGUIComponent(pickerInputComponent);
				}



			}
		}
		// Teams
		else if (varTypeName.equals("wt.team.Team")) {
			if (isReadOnly) {
				String team = ((activityContext.getValue(variableName) == null) ? null : ((wt.team.Team) activityContext.getValue(variableName)).getName());
				result.addGUIComponent(createLabelForTextDisplayComponent(team));
			}
			else {
				// Build a selectable list of teams
				ComboBox comboboxTeams = createTeamComponent(activityContext,variableName,isRequired);
				result.addGUIComponent(comboboxTeams);
			}
		}
		// Team Templates
		else if (varTypeName.equals("wt.team.TeamTemplate")) {
			if (isReadOnly) {
				String team = ((activityContext.getValue(variableName) == null) ?
						null : ((TeamTemplate) activityContext.getValue(variableName)).getName());
				result.addGUIComponent(createLabelForTextDisplayComponent(team));
			}
			else {
				// Build a selectable list of team templates
				ComboBox comboboxTeams = createTeamTemplateComponent(activityContext,variableName,isRequired);
				result.addGUIComponent(comboboxTeams);
			}
		}else {
			boolean normalRoute = true;
			WTObject myobject = (WTObject) activityContext.getValue (variableName);
			if((myobject instanceof FormatContentHolder)&&(InstalledProperties.isInstalled(InstalledProperties.PDML_PROI)))
			{
				result.addGUIComponent(createFormatContentHolder(myobject,normalRoute));
			} else 
			{
				// Fall Back for WTObject descendents not specially handled above
				String displayValue = WfHtmlFormat.createObjectLink ((WTObject) activityContext.getValue (variableName), null, locale);
				result.addGUIComponent(createLabelForTextDisplayComponent(displayValue));
			}        
		}

	}


	public final String getLabel(final String componentId, final ModelContext mc) throws WTException {

		String result="";

		locale = mc.getNmCommandBean() == null ? Locale.getDefault() : mc.getNmCommandBean().getLocale();
		modelContext = mc;

		final WfActivity assignedActivity = wfTaskProcessor.getActivity();
		ProcessData activityContext = assignedActivity.getContext();
		ProcessData assignedActivityContext = null;
		if (workItem.getContext() == null || wfTaskProcessor.getWorkItem().getContext() == null) {
			activityContext = assignedActivity.getContext();
		} else {
			activityContext = workItem.getContext();

			//This context is used to get the map of display names. SPR : 1538443 
			assignedActivityContext = assignedActivity.getContext();
		}

		final WfAssignedActivityTemplate activityTemplate = (WfAssignedActivityTemplate) assignedActivity.getTemplateReference().getObject();

		final ProcessDataInfo processDataInfo = (ProcessDataInfo) activityTemplate.getContextSignature();
		final WfVariableInfo[] varInfoArr = processDataInfo.getVariableList();
		WfVariable wfv,wfv1;
		Class variableClass;
		String varTypeName = "";
		String variableName = "";
		String variableDisplayName = "";


		for (final WfVariableInfo varInfo : varInfoArr) {
			System.out.println("varInfo :: "+ varInfo + " : componentId :: "+ componentId);



			wfv = activityContext.getVariable(varInfo.getName());
			if(assignedActivityContext!=null){
				wfv1 = assignedActivityContext.getVariable(varInfo.getName());
				variableDisplayName=wfv1.getDisplayName(locale);
			}else{
				variableDisplayName=wfv.getDisplayName(locale);
			}                

			variableClass = wfv.getVariableClass();
			varTypeName = wfv.getTypeName();
			variableName=wfv.getName();

			if(variableDisplayName.equalsIgnoreCase("")){

				variableDisplayName=wfv.getDisplayName();
				if(variableDisplayName.equalsIgnoreCase(""))
					variableDisplayName=wfv.getName();
			}
			// SPR:1711516- if componentId is special instructions then return it's localized name.(This is a special case)
			if(SPECIAL_INSTRUCTIONS.equals(variableName) && SPECIAL_INSTRUCTIONS.equals(componentId)){
				Object a = mc.getModelData().get(componentId);
				if(a==null) {
					return "";
				}else if ( a instanceof GUIComponentArray && ((GUIComponentArray)a).size()==0) {
					return "";
				}else 
					return WTMessage.getLocalizedMessage("wt.workflow.worklist.worklistResource",worklistResource.SPECIAL_INSTRUCTIONS_LABEL);
			}
			if(componentId.equals(variableName))
			{
				result=variableDisplayName;
				break;
			}

		}

		return result;
	}


	/**
	 * Helper method to get the name of the class without the
	 * package qualification.
	 */

	protected String getSimpleName (Class a_class)
	{
		char[] characters = a_class.getName ().toCharArray ();
		String simpleName = null;

		for (int i = characters.length -1; i > 0; i--)
		{
			if (characters[i] == '.')
			{
				simpleName = a_class.getName ().substring (i+1);
				break;
			}
		}
		return simpleName;
	}

	/**
	 * Should the custom variable, associated with the passed <code>WfVariableInfo</code>,
	 * be displayed?
	 * @param varInfo Information about the variable for which this question is asked.
	 * @param customVariableArr An array of variable names indicating which custom variables
	 * are being requested to be displayed.
	 * @return A boolean indication of whether the variable in question should be displayed.
	 */
	private final boolean displayCustomVariable(final WfVariableInfo varInfo,
			final String componentId) {

		if (componentId != null && varInfo != null && varInfo.isVisible() &&
				(((componentId.equals(ALL_ACTIVITY_VARIABLES) || componentId.equals("workitem_customvariable")) &&
						!varInfo.getName().equals(WfDefinerHelper.PRIMARY_BUSINESS_OBJECT) &&
						!varInfo.getName().equals(WfDefinerHelper.INSTRUCTIONS) &&
						!varInfo.getName().equals(SPECIAL_INSTRUCTIONS))
						|| componentId.equals(varInfo.getName()))) {
			return true;
		}
		return false;
	}

	//Returns an ArrayList of DirectoryContextProviders
	private ArrayList getContextProviders (WTContainerRef containerRef)
			throws WTException, WTPropertyVetoException {

		try {

			ArrayList resultDirContextProviderList = null;

			PrincipalSpec principalSpec = new PrincipalSpec(containerRef, WTGroup.class);
			principalSpec.setIncludeAllServices(true);
			DirectoryContextProvider[] dirContextProviders = WTContainerHelper.service.getPublicContextProviders(principalSpec);

			resultDirContextProviderList = new ArrayList(dirContextProviders.length);
			for (int i = 0; i < dirContextProviders.length; ++i) {
				resultDirContextProviderList.add( dirContextProviders[i] );
			}

			return resultDirContextProviderList;
		}

		catch ( Exception e ) {

			if ( e instanceof WTException )
				throw (WTException)e;
			else
				throw new WTException (e);
		}
	}

	/**
	 * If a custom width and height have been specified parse for the values
	 * @param propertyWidthHeight the String from the property model
	 * @return An Integer array of size 2 with element 0 width, element 1 height
	 */
	private int[] getCustomWidthHeight(String propertyWidthHeight) {

		int[] widthHeight = {-1, -1};

		try {

			if (propertyWidthHeight != null) {

				propertyWidthHeight = propertyWidthHeight.replace('}', ' ')
						.trim();

				String properties[] = { propertyWidthHeight };

				if (propertyWidthHeight.contains(";"))
					properties = propertyWidthHeight.split(";");

				for (String property : properties) {

					if (property.contains(":")) {
						String propertyValuePair[] = property.split(":");

						if (propertyValuePair.length == 2
								&& propertyValuePair[0] != null
								&& propertyValuePair[1] != null) {
							if (propertyValuePair[0].toLowerCase().trim()
									.equals(HEIGHT))
								widthHeight[1] = Integer.valueOf(propertyValuePair[1]
										.trim());
							if (propertyValuePair[0].toLowerCase().trim()
									.equals(WIDTH))
								widthHeight[0] = Integer.valueOf(propertyValuePair[1]
										.trim());
						}
					}
				}
			}
		}
		catch(Exception e) {
			LOGGER.error("error in formating width and height of custom variable"); // TODO
		}
		return widthHeight;
	}




}
