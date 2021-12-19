package ext.wisplm.util;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
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
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ptc.core.htmlcomp.util.TypeHelper;
import com.ptc.core.lwc.common.view.AttributeDefinitionReadView;
import com.ptc.core.lwc.common.view.ConstraintDefinitionReadView;
import com.ptc.core.lwc.common.view.ConstraintDefinitionReadView.RuleDataObject;
import com.ptc.core.lwc.common.view.EnumerationDefinitionReadView;
import com.ptc.core.lwc.common.view.EnumerationEntryReadView;
import com.ptc.core.lwc.common.view.TypeDefinitionReadView;
import com.ptc.core.lwc.server.LWCNormalizedObject;
import com.ptc.core.lwc.server.TypeDefinitionServiceHelper;
import com.ptc.core.meta.common.CreateOperationIdentifier;
import com.ptc.core.meta.common.DataSet;
import com.ptc.core.meta.common.EnumeratedSet;
import com.ptc.core.meta.common.EnumerationEntryIdentifier;
import com.ptc.core.meta.common.IllegalFormatException;
import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.common.TypeIdentifierHelper;
import com.ptc.core.meta.common.TypeInstanceIdentifier;
import com.ptc.core.meta.common.UpdateOperationIdentifier;
import com.ptc.core.meta.container.common.AttributeTypeSummary;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptor;
import com.ptc.core.meta.descriptor.common.DefinitionDescriptorFactory;
import com.ptc.core.meta.server.TypeIdentifierUtility;
import com.ptc.core.meta.type.common.TypeInstance;
import com.ptc.core.meta.type.common.TypeInstanceFactory;
import ext.wisplm.util.PartUtil;

import wt.fc.ObjectReference;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.iba.definition.BooleanDefinition;
import wt.iba.definition.FloatDefinition;
import wt.iba.definition.IntegerDefinition;
import wt.iba.definition.StringDefinition;
import wt.iba.definition.TimestampDefinition;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.litedefinition.BooleanDefView;
import wt.iba.definition.litedefinition.FloatDefView;
import wt.iba.definition.litedefinition.IntegerDefView;
import wt.iba.definition.litedefinition.RatioDefView;
import wt.iba.definition.litedefinition.ReferenceDefView;
import wt.iba.definition.litedefinition.StringDefView;
import wt.iba.definition.litedefinition.TimestampDefView;
import wt.iba.definition.litedefinition.URLDefView;
import wt.iba.definition.litedefinition.UnitDefView;
import wt.iba.definition.service.IBADefinitionHelper;
import wt.iba.value.BooleanValue;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.FloatValue;
import wt.iba.value.IBAHolder;
import wt.iba.value.IntegerValue;
import wt.iba.value.StringValue;
import wt.iba.value.TimestampValue;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.litevalue.StringValueDefaultView;
import wt.iba.value.service.IBAValueHelper;
import wt.iba.value.service.StandardIBAValueService;
import wt.method.RemoteMethodServer;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ServiceProviderHelper;
import wt.session.SessionHelper;
import wt.type.ClientTypedUtility;
import wt.type.TypedUtility;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
/**
 * 软属性操作类
 */
public class LWCUtil {

    private static final Logger log = Logger.getLogger(LWCUtil.class);

    private static final DefinitionDescriptorFactory DESCRIPTOR_FACTORY = (DefinitionDescriptorFactory) ServiceProviderHelper
            .getService(DefinitionDescriptorFactory.class, "default");

    public static boolean hasAttribute(Persistable p, String attName) {
        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return false;
        }
        TypeInstance typeInstance;
        try {
            LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
            TypeInstanceIdentifier typeinstanceidentifier = ClientTypedUtility.getTypeInstanceIdentifier(p);
            typeInstance = TypeInstanceFactory.newTypeInstance(typeinstanceidentifier);

            TypeIdentifier typeidentifier = (TypeIdentifier) typeInstance.getIdentifier().getDefinitionIdentifier();
            Set attrs = TypeHelper.getSoftAttributes(typeidentifier);
            Iterator attIt = attrs.iterator();
            String attrFullName = "";
            String attrName = "";
            int idx = 0;
            while (attIt.hasNext()) {
                attrFullName = attIt.next().toString();
                idx = attrFullName.lastIndexOf("|");
                attrName = attrFullName.substring(idx + 1);
                if (attrName.equals(attName)) {
                    return true;
                }
            }

        } catch (IllegalFormatException e) {
            e.printStackTrace();
        } catch (WTException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 
     * @param p
     * @return
     */
    public static Map<String, Object> getAllAttribute(Persistable p, Locale loc) {
        log.info("$$$$$$$$ getAllAttribute Begin.......");
        TypeInstance typeInstance;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        try {
            LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
            TypeInstanceIdentifier typeinstanceidentifier = ClientTypedUtility.getTypeInstanceIdentifier(p);
            typeInstance = TypeInstanceFactory.newTypeInstance(typeinstanceidentifier);

            TypeIdentifier typeidentifier = (TypeIdentifier) typeInstance.getIdentifier().getDefinitionIdentifier();
            Set attrs = TypeHelper.getSoftAttributes(typeidentifier);
            Iterator attIt = attrs.iterator();
            String attrFullName = "";
            String attrName = "";
            int idx = 0;
            while (attIt.hasNext()) {
                attrFullName = attIt.next().toString();
                idx = attrFullName.lastIndexOf("|");
                attrName = attrFullName.substring(idx + 1);
                lwcObject.load(attrName);
                dataMap.put(attrName, lwcObject.get(attrName));
            }

        } catch (IllegalFormatException e) {

            e.printStackTrace();
        } catch (WTException e) {

            e.printStackTrace();
        }
        log.info("$$$$$$$$ getAllAttribute End.......>");
        return dataMap;
    }

    /**
     * 
     * @param p
     * @return
     */
    public static Map<String, Object> getAllAttribute(Persistable p) {
        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return null;

        }
        return getAllAttribute(p, loc);
    }

    /**
     * 
     * @param p
     * @param key
     * @return
     * @throws WTException
     */
    public static Object getValue(Persistable p, String key) throws WTException {
        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return null;

        }
        return getValue(p, loc, key);
    }

    /**
     * 
     * @param p
     * @param key
     * @return
     * @throws WTException
     */
    public static Object getValue(Persistable p, Locale loc, String key) throws WTException {
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
        lwcObject.load(key);
        return lwcObject.get(key);
    }

    /***
     * 
     * @param p
     * @param keys
     * @return
     * @throws WTException
     */
    public static Map<String, Object> getMutilValue(Persistable p, String[] keys) throws WTException {

        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return null;

        }
        return getMutilValue(p, loc, keys);
    }

    /**
     * 
     * @param p
     * @param keys
     * @return
     * @throws WTException
     */
    public static Map<String, Object> getMutilValue(Persistable p, Locale loc, String[] keys) throws WTException {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
        for (int i = 0; i < keys.length; i++) {
            lwcObject.load(keys[i]);
            dataMap.put(keys[i], lwcObject.get(keys[i]));
        }

        return dataMap;

    }

    /**
     * 
     * @param p
     * @param dataMap
     * @return
     * @throws WTException
     */
    public static void setValueBeforeStore(Persistable p, Map<String, Object> dataMap) throws WTException {

        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return;

        }
        setValueBeforeStore(p, loc, dataMap);
    }

    /**
     * 
     * @param p
     * @param loc
     * @param dataMap
     * @return
     * @throws WTException
     */
    public static void setValueBeforeStore(Persistable p, Locale loc, Map<String, Object> dataMap) throws WTException {
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, new UpdateOperationIdentifier());
        Iterator<String> keyIt = dataMap.keySet().iterator();
        String key = null;
        lwcObject.load(dataMap.keySet());
        while (keyIt.hasNext()) {
            key = keyIt.next();
            lwcObject.set(key, dataMap.get(key));
        }

        lwcObject.apply();
    }

    /**
     * 
     * @param p
     * @param dataMap
     * @return
     * @throws WTException
     */
    public static void setValue(Persistable p, Map<String, Object> dataMap) throws WTException {

        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return;

        }
        setValue(p, loc, dataMap);
    }

    /**
     * 
     * @param p
     * @param loc
     * @param dataMap
     * @return
     * @throws WTException
     */
    public static Persistable setValue(Persistable p, Locale loc, Map<String, Object> dataMap) throws WTException {
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, new UpdateOperationIdentifier());
        Iterator<String> keyIt = dataMap.keySet().iterator();
        String key = null;
        lwcObject.load(dataMap.keySet());
        while (keyIt.hasNext()) {
            key = keyIt.next();
            lwcObject.set(key, dataMap.get(key));
        }

        lwcObject.apply();
        Persistable newP = PersistenceHelper.manager.modify(p);
        return newP;
    }

    /***
     * 
     * @param p
     * @param key
     * @param value
     * @return
     * @throws WTException
     */
    public static Persistable setValue(Persistable p, String key, Object value) throws WTException {
        Locale loc = null;
        try {
            loc = SessionHelper.getLocale();
        } catch (WTException e) {
            e.printStackTrace();
            return null;
        }
        return setValue(p, loc, key, value);

    }

    /**
     * 
     * @param p
     * @param loc
     * @param key
     * @param value
     * @return
     * @throws WTException
     */
    public static Persistable setValue(Persistable p, Locale loc, String key, Object value) throws WTException {
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, new UpdateOperationIdentifier());
        lwcObject.load(key);
        lwcObject.set(key, value);
        lwcObject.apply();
        Persistable newP = PersistenceHelper.manager.modify(p);

        return newP;

    }

    public static void setValueBeforeStore(Persistable p, String key, Object value) throws WTException {
        setValueBeforeStore(p, SessionHelper.getLocale(), key, value);
    }

    public static void setValueBeforeStore(Persistable p, Locale loc, String key, Object value) throws WTException {
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, new UpdateOperationIdentifier());
        lwcObject.load(key);
        lwcObject.set(key, value);
        lwcObject.apply();
    }

    public static void setStringIBAValue(Persistable persistable, String arrName, String value) throws Exception {
        AttributeDefDefaultView defView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(arrName);
        StringDefinition stringDef = null;
        if (defView instanceof StringDefView) {
            stringDef = (StringDefinition) ObjectReference.newObjectReference(((StringDefView) defView).getObjectID())
                    .getObject();
        }
        if (stringDef == null)
            throw new Exception("IBA Def not found error [" + arrName + "]");
        QuerySpec qs = new QuerySpec(StringValue.class);
        qs.appendWhere(new SearchCondition(StringValue.class, "theIBAHolderReference.key", SearchCondition.EQUAL,
                persistable.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(StringValue.class, "definitionReference.key", SearchCondition.EQUAL,
                stringDef.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        if (qr.hasMoreElements()) {
            StringValue strvalue = (StringValue) qr.nextElement();
            strvalue.setValue(value);
            PersistenceHelper.manager.save(strvalue);
        } else {
            StringValue sv = StringValue.newStringValue(stringDef, (IBAHolder) persistable, value);
            PersistenceHelper.manager.save(sv);
        }
    }

    public static void setIntegerIBAValue(Persistable persistable, String arrName, long value) throws Exception {
        AttributeDefDefaultView defView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(arrName);
        IntegerDefinition stringDef = null;
        if (defView instanceof IntegerDefView) {
            stringDef = (IntegerDefinition) ObjectReference.newObjectReference(((IntegerDefView) defView).getObjectID())
                    .getObject();
        }
        if (stringDef == null)
            throw new Exception("IBA Def not found error [" + arrName + "]");
        QuerySpec qs = new QuerySpec(IntegerValue.class);
        qs.appendWhere(new SearchCondition(IntegerValue.class, "theIBAHolderReference.key", SearchCondition.EQUAL,
                persistable.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(IntegerValue.class, "definitionReference.key", SearchCondition.EQUAL,
                stringDef.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        if (qr.hasMoreElements()) {
            IntegerValue strvalue = (IntegerValue) qr.nextElement();
            strvalue.setValue(value);
            PersistenceHelper.manager.save(strvalue);
        } else {
            IntegerValue sv = IntegerValue.newIntegerValue(stringDef, (IBAHolder) persistable, value);
            PersistenceHelper.manager.save(sv);
        }
    }

    public static void setTimestampIBAValue(Persistable persistable, String arrName, Timestamp value) throws Exception {
        AttributeDefDefaultView defView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(arrName);
        TimestampDefinition stringDef = null;
        if (defView instanceof TimestampDefView) {
            stringDef = (TimestampDefinition) ObjectReference
                    .newObjectReference(((TimestampDefView) defView).getObjectID()).getObject();
        }
        if (stringDef == null)
            throw new Exception("IBA Def not found error [" + arrName + "]");
        QuerySpec qs = new QuerySpec(TimestampValue.class);
        qs.appendWhere(new SearchCondition(TimestampValue.class, "theIBAHolderReference.key", SearchCondition.EQUAL,
                persistable.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(TimestampValue.class, "definitionReference.key", SearchCondition.EQUAL,
                stringDef.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        if (qr.hasMoreElements()) {
            TimestampValue strvalue = (TimestampValue) qr.nextElement();
            strvalue.setValue(value);
            PersistenceHelper.manager.save(strvalue);
        } else {
            TimestampValue sv = TimestampValue.newTimestampValue(stringDef, (IBAHolder) persistable, value);
            PersistenceHelper.manager.save(sv);
        }
    }

    public static void setFloatIBAValue(Persistable persistable, String arrName, float value) throws Exception {
        AttributeDefDefaultView defView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(arrName);
        FloatDefinition stringDef = null;
        if (defView instanceof FloatDefView) {
            stringDef = (FloatDefinition) ObjectReference.newObjectReference(((FloatDefView) defView).getObjectID())
                    .getObject();
        }
        if (stringDef == null)
            throw new Exception("IBA Def not found error [" + arrName + "]");
        QuerySpec qs = new QuerySpec(FloatValue.class);
        qs.appendWhere(new SearchCondition(FloatValue.class, "theIBAHolderReference.key", SearchCondition.EQUAL,
                persistable.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(FloatValue.class, "definitionReference.key", SearchCondition.EQUAL,
                stringDef.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        if (qr.hasMoreElements()) {
            FloatValue strvalue = (FloatValue) qr.nextElement();
            strvalue.setValue(value);
            PersistenceHelper.manager.save(strvalue);
        } else {
            FloatValue sv = FloatValue.newFloatValue(stringDef, (IBAHolder) persistable, value, 0);
            PersistenceHelper.manager.save(sv);
        }
    }

    public static void setBooleanIBAValue(Persistable persistable, String arrName, boolean value) throws Exception {
        AttributeDefDefaultView defView = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(arrName);
        BooleanDefinition stringDef = null;
        if (defView instanceof BooleanDefView) {
            stringDef = (BooleanDefinition) ObjectReference.newObjectReference(((BooleanDefView) defView).getObjectID())
                    .getObject();
        }
        if (stringDef == null)
            throw new Exception("IBA Def not found error [" + arrName + "]");
        QuerySpec qs = new QuerySpec(BooleanValue.class);
        qs.appendWhere(new SearchCondition(BooleanValue.class, "theIBAHolderReference.key", SearchCondition.EQUAL,
                persistable.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        qs.appendAnd();
        qs.appendWhere(new SearchCondition(BooleanValue.class, "definitionReference.key", SearchCondition.EQUAL,
                stringDef.getPersistInfo().getObjectIdentifier()), new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        if (qr.hasMoreElements()) {
            BooleanValue strvalue = (BooleanValue) qr.nextElement();
            strvalue.setValue(value);
            PersistenceHelper.manager.save(strvalue);
        } else {
            BooleanValue sv = BooleanValue.newBooleanValue(stringDef, (IBAHolder) persistable, value);
            PersistenceHelper.manager.save(sv);
        }
    }

    /**
     * 
     * @param p
     * @param key
     * @return
     * @throws WTException
     */
    public static LWCNormalizedObject getValues(Persistable p, List<String> keys) throws WTException {
        Locale loc = SessionHelper.getLocale();
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
        lwcObject.load(keys);
        return lwcObject;
    }

    public static Map<String, String> getIBAValues(Persistable p, List<String> keys) throws WTException {
        Map<String, String> dataMap = new HashMap();
        String returnStr = "";
        Locale loc = SessionHelper.getLocale();
        LWCNormalizedObject lwcObject = new LWCNormalizedObject(p, null, loc, null);
        lwcObject.load(keys);
        for (String key : keys) {
            Object obj = lwcObject.get(key);
            if (obj == null) {
                dataMap.put(key, "");
                continue;
            } else {
                if (obj instanceof String) {
                    returnStr = (obj == null) ? "" : (String) obj;
                    dataMap.put(key, returnStr);
                } else if (obj instanceof Timestamp) {
                    returnStr = (obj == null) ? "" : ((Timestamp) obj).toGMTString();
                    dataMap.put(key, returnStr);
                } else {
                    if (obj != null) {
                        dataMap.put(key, obj.toString());
                    } else {
                        dataMap.put(key, "");
                    }
                }
            }
        }
        return dataMap;
    }

    /**
    *
    * set StandardAttribute of the ibaHolder </p> <br>
    *
    * @param ibaHolder
    * @param attName
    * @param attValue
    * @throws WTException
    *
    */
    public static void updateStandardAttributeIBAValue(IBAHolder ibaHolder, String attName, Object attValue)
            throws WTException {
        try {
            if (!RemoteMethodServer.ServerFlag) {
                Class[] classes = { IBAHolder.class, String.class, Object.class };
                Object[] objs = { ibaHolder, attName, attValue };
                RemoteMethodServer.getDefault().invoke("updateStandardAttributeIBAValue", LWCUtil.class.getName(), null,
                        classes, objs);
            } else {
                Persistable persistable = (Persistable) ibaHolder;
                LWCNormalizedObject obj = new LWCNormalizedObject((Persistable) ibaHolder, null, Locale.US,
                        new UpdateOperationIdentifier());

                TypeIdentifier typeIden = TypeIdentifierUtility.getTypeIdentifier(persistable);
                TypeDefinitionReadView tdrv = TypeDefinitionServiceHelper.service.getTypeDefView(typeIden);
                if (tdrv != null) {
                    AttributeDefinitionReadView attView = tdrv.getAttributeByName(attName);
                    if (attView != null) {
                        obj.load(attName);
                        obj.set(attName, attValue);
                        persistable = obj.apply();
                        PersistenceServerHelper.manager.update(persistable);
                    }
                }
            }
        } catch (Exception e) {
            throw new WTException(e, "Update [" + attName + "]:" + attValue + " failed.");
        }
    }

    /**
    *
    * get attribute definition read view </p> <br>
    *
    * @param ibaHolder
    * @param attName
    * @throws WTException
    *
    */
    public static AttributeDefinitionReadView getAttributeDefinition(IBAHolder ibaHolder, String attName)
            throws WTException {
        try {
            if (!RemoteMethodServer.ServerFlag) {
                Class[] classes = { IBAHolder.class, String.class };
                Object[] objs = { ibaHolder, attName };
                return (AttributeDefinitionReadView) RemoteMethodServer.getDefault().invoke("getAttributeDefinition",
                        LWCUtil.class.getName(), null, classes, objs);
            } else {
                AttributeDefinitionReadView attView = null;
                Persistable persistable = (Persistable) ibaHolder;
                TypeIdentifier typeIden = TypeIdentifierUtility.getTypeIdentifier(persistable);
                TypeDefinitionReadView tdrv = TypeDefinitionServiceHelper.service.getTypeDefView(typeIden);
                if (tdrv != null)
                    attView = tdrv.getAttributeByName(attName);
                return attView;
            }
        } catch (Exception e) {
            throw new WTException(e, "Read Attribute [" + attName + "] failed.");
        }
    }

    /**
     * get attribute definition read view
     *
     * @param softType
     * @param attrname
     * @return
     * @throws WTException
     **/
    public static AttributeDefinitionReadView getAttributeDefinition(String softType, String attrname)
            throws WTException {
        TypeIdentifier ti = TypedUtility.getTypeIdentifier(softType);
        if (ti == null)
            return null;

        TypeDefinitionReadView tv = TypeDefinitionServiceHelper.service.getTypeDefView(ti);
        if (tv == null)
            return null;

        return tv.getAttributeByName(attrname);
    }

    public static Object getIBAValue(Persistable targetObj, String ibaName) throws WTException {
        return getIBAValue((WTObject) targetObj, ibaName);
    }

    /**
    *
    * get Object IBAValue <br>
    *
    * @param targetObj
    * @param ibaName
    * @return
    * @throws WTException
    *
    */
    public static Object getIBAValue(WTObject targetObj, String ibaName) throws WTException {
        Object ibaValue = null;
        try {
            if (!RemoteMethodServer.ServerFlag) {
                return RemoteMethodServer.getDefault().invoke("getIBAValue", LWCUtil.class.getName(), null,
                        new Class[] { WTObject.class, String.class }, new Object[] { targetObj, ibaName });
            } else {
                if (targetObj == null) {
                    return ibaValue;
                }
                LWCNormalizedObject obj = null;
                obj = new LWCNormalizedObject(targetObj, null, null, null);
                obj.load(ibaName);
                ibaValue = obj.get(ibaName);
            }
        } catch (Exception e) {
            log.error("targetObj:" + targetObj.getDisplayIdentity() + " att:" + ibaName, e);
        }
        return ibaValue;
    }
    public static String getStringValue(WTObject targetObj, String ibaName) throws WTException {
        return obj2String(getIBAValue(targetObj,ibaName));
    }

    /**
    *
    * get Object IBA Mult-Value <br>
    *
    * @param targetObj
    * @param ibaName
    * @return List of values
    * @throws WTException
    *
    */
    public static List<Object> getIBAMultiValue(Persistable targetObj, String ibaName) throws WTException {
        return getIBAMultiValue((WTObject) targetObj, ibaName);
    }

    /**
    *
    * get Object IBA Mult-Value <br>
    *
    * @param targetObj
    * @param ibaName
    * @return List of values
    * @throws WTException
    *
    */
    public static List<Object> getIBAMultiValue(WTObject targetObj, String ibaName) throws WTException {
        List<Object> ibaMultiValue = new ArrayList<Object>();
        List<Object> ibaValueList = new ArrayList<Object>();
        Object ibaValue = null;
        try {
            if (!RemoteMethodServer.ServerFlag) {
                return (List<Object>) RemoteMethodServer.getDefault().invoke("getIBAMultiValue",
                        LWCUtil.class.getName(), null, new Class[] { WTObject.class, String.class },
                        new Object[] { targetObj, ibaName });
            } else {
                LWCNormalizedObject obj = null;
                obj = new LWCNormalizedObject(targetObj, null, null, null);
                obj.load(ibaName);
                ibaValue = obj.get(ibaName);
                if (ibaValue != null) {
                    if (ibaValue instanceof Object[])
                        ibaMultiValue = Arrays.asList((Object[]) ibaValue);
                    else
                        ibaMultiValue.add(ibaValue);
                }
            }
        } catch (Exception e) {
            log.error("targetObj:" + targetObj.getDisplayIdentity() + " att:" + ibaName, e);
        }
        ibaValueList.addAll(ibaMultiValue);
        return ibaValueList;
    }

    /**
    *
    * set attributeValue (globeAttribute/StandardAttribute) <br>
    *
    * @param targetObject
    * @param targetAttributeName
    *            ibaName/StandardAttributeName
    * @param ibaValue
    *            List For multi-values
    * @throws WTException
    *
    */
    public static void setListValueForStandAndGlobalIBA(WTObject targetObject, String targetAttributeName,
            List ibaValueList) throws WTException {
        if (ibaValueList != null) {
            Object[] ibaValues = ibaValueList.toArray();
            setValueForStandAndGlobalIBA(targetObject, targetAttributeName, ibaValues);
        } else
            setValueForStandAndGlobalIBA(targetObject, targetAttributeName, (Object) null);
    }

    public static void setValueForStandAndGlobalIBA(WTObject targetObject, String targetAttributeName,
            List ibaValueList) throws WTException {
        if (ibaValueList != null) {
            Object[] ibaValues = ibaValueList.toArray();
            setValueForStandAndGlobalIBA(targetObject, targetAttributeName, ibaValues);
        } else
            setValueForStandAndGlobalIBA(targetObject, targetAttributeName, (Object) null);
    }

    /**
    *
    * set attributeValue (globeAttribute/StandardAttribute) <br>
    *
    * @param targetObject
    * @param targetAttributeName
    *            ibaName/StandardAttributeName
    * @param ibaValue
    * @throws WTException
    *
    */
    public static void setValueForStandAndGlobalIBA(WTObject targetObject, String targetAttributeName, Object ibaValue)
            throws WTException {
        try {
            String databaseColumnsLabel = null;
            TypeIdentifier ti = TypeIdentifierHelper.getType(targetObject);
            AttributeDefDefaultView attributedefdefaultview = null;
            if (ti != null) {
                TypeDefinitionReadView tv = TypeDefinitionServiceHelper.service.getTypeDefView(ti);
                if (tv != null) {
                    AttributeDefinitionReadView av = tv.getAttributeByName(targetAttributeName);
                    if (av != null) {
                        databaseColumnsLabel = av.getDatabaseColumnsLabel();
                        attributedefdefaultview = av.getIBARefView();
                    }
                }
            }
            log.debug(targetObject.getDisplayIdentity() + " 's [" + targetAttributeName + "] databaseColumnsLabel = "
                    + databaseColumnsLabel);

            // if targetAttribute is StandardAttribute
            if (!StringUtils.isEmpty(databaseColumnsLabel))
                updateStandardAttributeIBAValue((IBAHolder) targetObject, targetAttributeName, ibaValue);
            else {
                if (attributedefdefaultview == null) {
                    attributedefdefaultview = IBAUtil.getAttributeDefinition(targetAttributeName, false);
                }

                if (attributedefdefaultview == null) {
                    throw new WTException("Gloabl attribute definition for [" + ti.getTypename() + "/"
                            + targetAttributeName + "] not found.");
                }
                setIBAAnyValue(targetObject, targetAttributeName, ibaValue, attributedefdefaultview);
            }
        } catch (Exception e) {
            throw new WTException(e, "Set Attribute [" + targetAttributeName + "]:" + ibaValue + " failed.");
        }
    }

    public static void setIBAAnyValue(WTObject obj, String ibaName, Object newValue)
            throws WTException, RemoteException, WTPropertyVetoException, ParseException {
        AttributeDefDefaultView attributedefdefaultview = null;
        TypeIdentifier ti = TypeIdentifierHelper.getType(obj);
        if (ti != null) {
            TypeDefinitionReadView tv = TypeDefinitionServiceHelper.service.getTypeDefView(ti);
            if (tv != null) {
                AttributeDefinitionReadView av = tv.getAttributeByName(ibaName);
                if (av != null) {
                    attributedefdefaultview = av.getIBARefView();
                }
            }
        }
        if (attributedefdefaultview == null) {
            attributedefdefaultview = IBAUtil.getAttributeDefinition(ibaName, false);
        }

        if (attributedefdefaultview == null) {
            throw new WTException(
                    "Gloabl attribute definition for [" + ti.getTypename() + "/" + ibaName + "] not found.");
        }
        setIBAAnyValue(obj, ibaName, newValue, attributedefdefaultview);
    }

    /**
    * Set IBA Any Value
    *
    * @param obj
    * @param ibaName
    *            : iba name
    * @param newValue
    *            : iba value
    * @exception WTException
    *                , RemoteException, WTPropertyVetoException, ParseException
    */
    private static void setIBAAnyValue(WTObject obj, String ibaName, Object newValue,
            AttributeDefDefaultView attributedefdefaultview)
            throws WTException, RemoteException, WTPropertyVetoException, ParseException {

        if (attributedefdefaultview == null) {
            throw new WTException("Gloabl attribute definition[" + ibaName + "] not found.");
        }

        IBAHolder ibaholder = (IBAHolder) obj;
        String ibaClass = "";
        if (attributedefdefaultview instanceof FloatDefView) {
            ibaClass = "wt.iba.definition.FloatDefinition";
        } else if (attributedefdefaultview instanceof StringDefView) {
            ibaClass = "wt.iba.definition.StringDefinition";
        } else if (attributedefdefaultview instanceof IntegerDefView) {
            ibaClass = "wt.iba.definition.IntegerDefinition";
        } else if (attributedefdefaultview instanceof RatioDefView) {
            ibaClass = "wt.iba.definition.RatioDefinition";
        } else if (attributedefdefaultview instanceof TimestampDefView) {
            ibaClass = "wt.iba.definition.TimestampDefinition";
        } else if (attributedefdefaultview instanceof BooleanDefView) {
            ibaClass = "wt.iba.definition.BooleanDefinition";
        } else if (attributedefdefaultview instanceof URLDefView) {
            ibaClass = "wt.iba.definition.URLDefinition";
        } else if (attributedefdefaultview instanceof ReferenceDefView) {
            ibaClass = "wt.iba.definition.ReferenceDefinition";
        } else if (attributedefdefaultview instanceof UnitDefView) {
            ibaClass = "wt.iba.definition.UnitDefinition";
        }

        ibaholder = IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);

        ibaholder = IBAValueHelper.service.refreshAttributeContainer(ibaholder, null, SessionHelper.manager.getLocale(),
                null);
        DefaultAttributeContainer defaultattributecontainer = (DefaultAttributeContainer) (ibaholder)
                .getAttributeContainer();

        Vector vAbstractvalueview = IBAUtil.getIBAValueViews(defaultattributecontainer, ibaName, ibaClass);

        AbstractValueView oldAbstractvalueview = null;
        if (vAbstractvalueview != null && vAbstractvalueview.size() > 0) {
            oldAbstractvalueview = (AbstractValueView) vAbstractvalueview.get(0);
        }

        if (vAbstractvalueview.size() > 1) {
            AbstractValueView abstractvalueview = null;
            for (int i = 1; i < vAbstractvalueview.size(); i++) {
                abstractvalueview = (AbstractValueView) vAbstractvalueview.get(i);
                defaultattributecontainer.deleteAttributeValue(abstractvalueview);
                StandardIBAValueService.theIBAValueDBService.updateAttributeContainer(ibaholder, null, null, null);
                ibaholder = IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
            }
        }

        if (newValue != null && !newValue.equals("")) {
            if (attributedefdefaultview instanceof FloatDefView) {
                IBAUtil.setIBAFloatValue(obj, ibaName, newValue.toString());
            } else if (attributedefdefaultview instanceof StringDefView) {
                if (newValue instanceof String) {
                    String strValue = (String) newValue;
                    IBAUtil.setIBAStringValue(obj, ibaName, strValue);
                } else if (newValue instanceof Object[]) {
                    Object[] newMultiObject = (Object[]) newValue;
                    Set<String> values = new HashSet<String>();
                    String[] newMultiString = new String[newMultiObject.length];

                    StringValueDefaultView stringvalueview = (StringValueDefaultView) oldAbstractvalueview;
                    for (int i = 0; i < newMultiString.length; i++) {
                        newMultiString[i] = (String) newMultiObject[i];
                    }
                    IBAUtil.setIBAStringValues(obj, ibaName, newMultiString);
                    if (oldAbstractvalueview != null) {
                        defaultattributecontainer.deleteAttributeValue(oldAbstractvalueview);
                        StandardIBAValueService.theIBAValueDBService.updateAttributeContainer(ibaholder, null, null,
                                null);
                        ibaholder = IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
                    }
                }
            } else if (attributedefdefaultview instanceof IntegerDefView) {
                Integer tempNewValue = null;
                if (newValue instanceof String) {
                    tempNewValue = Integer.valueOf((String) newValue);
                } else {
                    tempNewValue = (Integer) newValue;
                }

                IBAUtil.setIBAIntegerValue(obj, ibaName, tempNewValue);
            } else if (attributedefdefaultview instanceof RatioDefView) {
                Double tempNewValue = null;
                if (newValue instanceof String) {
                    tempNewValue = Double.valueOf((String) newValue);
                } else {
                    tempNewValue = (Double) newValue;
                }

                IBAUtil.setIBARatioValue(obj, ibaName, tempNewValue);
            } else if (attributedefdefaultview instanceof TimestampDefView) {
                Timestamp tempNewValue = null;
                if (newValue instanceof String) {
                    tempNewValue = Timestamp.valueOf((String) newValue);
                } else {
                    tempNewValue = (Timestamp) newValue;
                }

                IBAUtil.setIBATimestampValue(obj, ibaName, tempNewValue);
            } else if (attributedefdefaultview instanceof BooleanDefView) {
                Boolean tempNewValue = null;
                if (newValue instanceof String) {
                    tempNewValue = Boolean.valueOf((String) newValue);
                } else {
                    tempNewValue = (Boolean) newValue;
                }

                IBAUtil.setIBABooleanValue(obj, ibaName, tempNewValue);
            } else if (attributedefdefaultview instanceof URLDefView) {
                IBAUtil.setIBAURLValue(obj, ibaName, String.valueOf(newValue));
            } else if (attributedefdefaultview instanceof ReferenceDefView) {
                //TODO
            } else if (attributedefdefaultview instanceof UnitDefView) {
                Double tempNewValue = null;
                if (newValue instanceof String) {
                    tempNewValue = Double.valueOf((String) newValue);
                } else {
                    tempNewValue = (Double) newValue;
                }
                IBAUtil.setIBAUnitValue(obj, ibaName, tempNewValue);
            }
        } else {
            if (oldAbstractvalueview != null) {
                defaultattributecontainer.deleteAttributeValue(oldAbstractvalueview);
                StandardIBAValueService.theIBAValueDBService.updateAttributeContainer(ibaholder, null, null, null);
                ibaholder = IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
            }
        }
        PersistenceServerHelper.manager.update((Persistable) ibaholder);
    }

    /**
     * To upper the attribute values of object
     *
     * @param targetObject
     * @param targetAttributeName
     * @throws WTException
     */
    public static void toUpperAttributeValue(WTObject targetObject, String targetAttributeName) throws WTException {
        try {
            List<Object> valueList = getIBAMultiValue(targetObject, targetAttributeName);
            List<Object> valueList2 = new ArrayList<Object>();
            for (Object value : valueList) {
                valueList2.add(value.toString().toUpperCase());
            }
            setValueForStandAndGlobalIBA(targetObject, targetAttributeName, valueList2);
        } catch (Exception e) {
            throw new WTException(e, "To upper values for [" + targetObject.getDisplayIdentity() + "] attribute:["
                    + targetAttributeName + "] failed.");
        }
    }

    /**
     * Get Enumeration by enum name
     *
     * @param enumAttributeName
     * @return
     * @throws WTException
     **/
    public static Map<String, String> getEnumSetByIBA(String enumAttributeName) throws WTException {
        return getEnumSetByIBA(enumAttributeName, false);
    }

    /**
    * Get Enumeration by enum name
    *
    * @param enumAttributeName
    * @param includeAll include "remove from selection" entry or not
    * @throws WTException
    **/
    public static Map<String, String> getEnumSetByIBA(String enumAttributeName, boolean includeAll) throws WTException {
        Map<String, String> result = new TreeMap<String, String>();
        try {
            EnumerationDefinitionReadView edr = TypeDefinitionServiceHelper.service.getEnumDefView(enumAttributeName);
            Map<String, EnumerationEntryReadView> views = edr.getAllEnumerationEntries();
            Set<String> keysOfView = views.keySet();
            for (String key : keysOfView) {
                EnumerationEntryReadView view = views.get(key);
                String enumKey = view.getName();
                String enumName = view.getPropertyValueByName("displayName").getValue().toString();
                String selectable = view.getPropertyValueByName("selectable").getValue().toString();
                if (includeAll || "true".equals(selectable)) {
                    result.put(enumKey, enumName);
                }
            }
        } catch (Exception e) {
            throw new WTException(e, "Get Enumeration [" + enumAttributeName + "] failed.");
        }
        return result;
    }

    /**
     * Get Enumeration by softtype and attribute name
     *
     * @param softtype
     * @param attrname
     * @return
     * @throws WTException
     **/
    public static Map<String, String> getEnumSetBySofttypeIBA(String softtype, String attrname) throws WTException {
        return getEnumSetBySofttypeIBA(softtype, attrname, false);
    }

    /**
    * Get Enumeration by softtype and attribute name
    *
    * @param softtype
    * @param attrname
    * @param includeAll include "remove from selection" entry or not
    * @throws WTException
    **/
    public static Map<String, String> getEnumSetBySofttypeIBA(String softtype, String attrname, boolean includeAll)
            throws WTException {
        Map<String, String> result = new TreeMap<String, String>();
        try {
            TypeIdentifier ti = TypedUtility.getTypeIdentifier(softtype);

            TypeDefinitionReadView tv = TypeDefinitionServiceHelper.service.getTypeDefView(ti);
            AttributeDefinitionReadView av = tv.getAttributeByName(attrname);
            Collection<ConstraintDefinitionReadView> constraints = av.getAllConstraints();
            for (ConstraintDefinitionReadView constraint : constraints) {
                String rule = constraint.getRule().getKey().toString();
                if (rule.indexOf("com.ptc.core.lwc.server.LWCEnumerationBasedConstraint") > -1) {
                    RuleDataObject rdo = constraint.getRuleDataObj();
                    if (rdo != null) {
                        Collection coll = rdo.getEnumDef().getAllEnumerationEntries().values();
                        Iterator<EnumerationEntryReadView> it = coll.iterator();
                        while (it.hasNext()) {
                            EnumerationEntryReadView view = it.next();
                            String enumKey = view.getName();
                            String enumName = view.getPropertyValueByName("displayName").getValue().toString();
                            String selectable = view.getPropertyValueByName("selectable").getValue().toString();
                            if (includeAll || "true".equals(selectable)) {
                                result.put(enumKey, enumName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new WTException(e, "Get Enumeration [" + attrname + "] for type [" + softtype + "] failed.");
        }
        return result;
    }

    public static String getObjEnumDisplayValue(WTObject targetObj, String attributeName) throws WTException {
        try {
            Object ibaValue = null;
            String displayValue = null;
            Locale locale = SessionHelper.manager.getLocale();
            if (!RemoteMethodServer.ServerFlag) {
                return (String) RemoteMethodServer.getDefault().invoke("getEnumDisplayValue", LWCUtil.class.getName(),
                        null, new Class[] { WTObject.class, String.class }, new Object[] { targetObj, attributeName });
            } else {
                LWCNormalizedObject obj = null;
                obj = new LWCNormalizedObject(targetObj, null, locale, new CreateOperationIdentifier());
                obj.load(attributeName);
                ibaValue = obj.get(attributeName);
                if (ibaValue != null) {
                    AttributeTypeSummary ats = obj.getAttributeDescriptor(attributeName);
                    DataSet legalValueSet = ats.getLegalValueSet();
                    if (legalValueSet instanceof EnumeratedSet) {
                        EnumeratedSet enumSet = (EnumeratedSet) legalValueSet
                                .getIntersection((EnumeratedSet) legalValueSet);
                        if (enumSet != null) {
                            //for (Object element : enumSet.getElements()) {
                            EnumerationEntryIdentifier eei = enumSet.getElementByKey(ibaValue);
                            if (eei != null && eei.getKey() != null) {
                                String enumKey = (String) eei.getKey();
                                if (enumKey.equals((String) ibaValue)) {
                                    DefinitionDescriptor value = DESCRIPTOR_FACTORY.get(eei, null, locale);
                                    displayValue = value.getDisplay();
                                }
                            }
                        }
                    }
                }
            }
            return displayValue;
        } catch (Exception e) {
            throw new WTException(e, "Get list value failed for [" + targetObj.getDisplayIdentity()
                    + "] for attribute:[" + attributeName + "].");
        }
    }

    public static String StringToNull(String value) throws WTException {
        if (value.equals("null") || value == "null")
            return null;
        return value;
    }
    
    public static String obj2String(Object obj){
        return obj==null?"":obj.toString().trim();
    }

}
