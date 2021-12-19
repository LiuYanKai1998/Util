package ext.wisplm.apiexercise.ccreatedoc;

import wt.fc.ObjectIdentifier;
import wt.fc.ObjectReference;
import wt.fc.Persistable;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceServerHelper;
import wt.fc.QueryResult;
import wt.iba.definition.litedefinition.AbstractAttributeDefinizerView;
import wt.iba.definition.litedefinition.AttributeDefDefaultView;
import wt.iba.definition.litedefinition.StringDefView;
import wt.iba.definition.service.IBADefinitionHelper;
import wt.iba.value.AttributeContainer;
import wt.iba.value.DefaultAttributeContainer;
import wt.iba.value.IBAHolder;
import wt.iba.value.StringValue;
import wt.iba.value.litevalue.AbstractValueView;
import wt.iba.value.litevalue.StringValueDefaultView;
import wt.iba.value.service.IBAValueDBService;
import wt.iba.value.service.IBAValueDBServiceInterface;
import wt.iba.value.service.IBAValueHelper;
import wt.inf.container.WTContainerHelper;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTOrganization;
import wt.org.WTPrincipal;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.services.ManagerServiceFactory;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTProperties;

public class IBAUtil implements RemoteAccess {
	static WTProperties wtp;
	static String CLASSNAME = IBAUtil.class.getName();
	static {
		try {
			wtp = WTProperties.getLocalProperties();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static String getWindchillProperty(String key, String defaultValue) {
		return wtp.getProperty(key, defaultValue);
	}

	/**
	 * 给指定对象设定指定的IBA属性值
	 * 
	 * @param ibaholder
	 *            指定的对象
	 * @param ibaName
	 *            指定的IBA属性
	 * @param ibaValue
	 *            IBA属性值
	 * @return 设置IBA属性后的对象
	 */
	public static IBAHolder setIBAValue(IBAHolder ibaholder, String ibaName, String ibaValue) {
		if (!RemoteMethodServer.ServerFlag) {
			String method = "setObjectStringIbaValue";
			Class<?>[] types = { IBAHolder.class, String.class, String.class };
			Object[] vals = { ibaholder, ibaName, ibaValue };
			try {
				ibaholder = (IBAHolder) RemoteMethodServer.getDefault().invoke(method, CLASSNAME, null, types, vals);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ibaholder;
		}
		boolean accessEnforced = false;
		try {
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			AbstractAttributeDefinizerView aadv = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(ibaName);
			if (aadv == null) {
				System.out.println("Not found IBA=" + ibaName + "] Definition.");
			} else {
				Object obj = new StringValueDefaultView((StringDefView) aadv, ibaValue);
				ibaholder = (IBAHolder) PersistenceHelper.manager.refresh((Persistable) ibaholder);
				ibaholder = (IBAHolder) IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
				DefaultAttributeContainer defaultattributecontainer = (DefaultAttributeContainer) ibaholder.getAttributeContainer();
				if (defaultattributecontainer == null) {
					System.out.println("DefaultAttributeContainer is null");
					defaultattributecontainer = new DefaultAttributeContainer();
					defaultattributecontainer.addAttributeValue(((AbstractValueView) (obj)));
					ibaholder.setAttributeContainer(defaultattributecontainer);
				} else {
					AbstractValueView oldavv[] = defaultattributecontainer.getAttributeValues((AttributeDefDefaultView) aadv);
					for (int k = 0; k < oldavv.length; k++) {
						defaultattributecontainer.deleteAttributeValue(oldavv[k]);
					}
					defaultattributecontainer.addAttributeValue((AbstractValueView) (obj));
					IBAValueDBServiceInterface dbService = (IBAValueDBServiceInterface) ManagerServiceFactory.getDefault().getManager(
							IBAValueDBService.class);
					defaultattributecontainer = (DefaultAttributeContainer) dbService.updateAttributeContainer(ibaholder, defaultattributecontainer
							.getConstraintParameter(), null, null);
				}
				ibaholder = (IBAHolder) IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
		return ibaholder;
	}

	/**
	 * 根据IBA属性名称的获取对象的IBA属性值
	 * 
	 * @param ibaholder
	 *            指定的对象
	 * @param ibaName
	 *            指定的IBA属性名称
	 * @return IBA属性值
	 */
	public static String getIBAValue(IBAHolder ibaholder, String ibaName) {
		if (!RemoteMethodServer.ServerFlag) {
			String method = "getIBAValue";
			Class<?>[] types = { IBAHolder.class, String.class };
			Object[] vals = { ibaholder, ibaName };
			try {
				return (String) RemoteMethodServer.getDefault().invoke(method, CLASSNAME, null, types, vals);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		boolean accessEnforced = false;
		try {
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			AbstractAttributeDefinizerView aadv = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(ibaName);
			if (aadv == null) {
				System.out.println("未找到IBA名称为[" + ibaName + "]的IBA属性被定义.");
				return null;
			} else {
				ibaholder = (IBAHolder) PersistenceHelper.manager.refresh((Persistable) ibaholder);
				ibaholder = (IBAHolder) IBAValueHelper.service.refreshAttributeContainer(ibaholder, "CSM", null, null);
				DefaultAttributeContainer defaultattributecontainer = (DefaultAttributeContainer) ibaholder.getAttributeContainer();
				if (defaultattributecontainer == null) {
					return null;
				} else {
					AbstractValueView avv[] = defaultattributecontainer.getAttributeValues((AttributeDefDefaultView) aadv);
					if (avv.length <= 0)
						return null;
					StringBuffer sbuf = new StringBuffer();
					for (int k = 0; k < avv.length; k++) {
						StringValueDefaultView svdv = (StringValueDefaultView) avv[k];
						if (sbuf.length() != 0)
							sbuf.append(",");
						sbuf.append(svdv.getValue());
					}
					return sbuf.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
		return null;
	}

	/**
	 * @param ibaholder
	 * @param ibaName
	 * @return
	 */
	public static String getObjectStringIbaValue(IBAHolder ibaholder, String ibaName) {
		if (!RemoteMethodServer.ServerFlag) {
			String method = "getObjectStringIbaValue";
			Class<?>[] types = { IBAHolder.class, String.class };
			Object[] vals = { ibaholder, ibaName };
			try {
				return (String) RemoteMethodServer.getDefault().invoke(method, CLASSNAME, null, types, vals);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		SessionServerHelper.manager.setAccessEnforced(false);
		try {
			AttributeDefDefaultView addv = IBADefinitionHelper.service.getAttributeDefDefaultViewByPath(ibaName);
			if (addv == null) {
				System.out.println("未找到IBA属性名称为[" + ibaName + "]的IBA属性被定义.");
				return null;
			}
			long ibaDefId = addv.getObjectID().getId();
			long prjObid = PersistenceHelper.getObjectIdentifier((Persistable) ibaholder).getId();
			QuerySpec qs = new QuerySpec(StringValue.class);
			qs.appendWhere(new SearchCondition(StringValue.class, StringValue.DEFINITION_REFERENCE + "." + ObjectReference.KEY + "."
					+ ObjectIdentifier.ID, SearchCondition.EQUAL, ibaDefId), new int[] { 0 });
			qs.appendAnd();
			qs.appendWhere(new SearchCondition(StringValue.class, StringValue.IBAHOLDER_REFERENCE + "." + ObjectReference.KEY + "."
					+ ObjectIdentifier.ID, SearchCondition.EQUAL, prjObid), new int[] { 0 });
			qs.setAdvancedQueryEnabled(true);
			QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
			if (qr.hasMoreElements())
				return ((StringValue) qr.nextElement()).getValue();
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SessionServerHelper.manager.setAccessEnforced(true);
		}
		return null;
	}

	/**
	 * 判断当前登录用户是否为站点管理员
	 * 
	 * @return 是站点管理员返回true,不是返回false
	 * @throws WTException
	 */
	public static Boolean isSiteOrOrgAdmin() throws WTException {
		if (!RemoteMethodServer.ServerFlag) {
			String method = "isSiteOrOrgAdmin";
			try {
				return (Boolean) RemoteMethodServer.getDefault().invoke(method, CLASSNAME, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
				return Boolean.FALSE;
			}
		}
		WTPrincipal principal = SessionHelper.getPrincipal();
		boolean check = false;
		WTContainerRef exchangeRef = WTContainerHelper.service.getExchangeRef();
		check = WTContainerHelper.service.isAdministrator(exchangeRef, principal);
		if (check)
			return Boolean.TRUE;
		WTOrganization org = OrganizationServicesHelper.manager.getOrganization(principal);
		if (org == null)
			return Boolean.FALSE;
		WTContainerRef orgContainerRef = WTContainerHelper.service.getOrgContainerRef(org);
		check = WTContainerHelper.service.isAdministrator(orgContainerRef, principal);
		return Boolean.valueOf(check);
	}

	public static boolean updateIBAHolder(IBAHolder ibaholder) throws WTException {
		IBAValueDBService ibavaluedbservice = new IBAValueDBService();
		boolean flag = true;
		try {
			PersistenceServerHelper.manager.update((Persistable) ibaholder);
			AttributeContainer attributecontainer = ibaholder.getAttributeContainer();
			Object obj = ((DefaultAttributeContainer) attributecontainer).getConstraintParameter();
			AttributeContainer attributecontainer1 = ibavaluedbservice.updateAttributeContainer(ibaholder, obj, null, null);
			ibaholder.setAttributeContainer(attributecontainer1);
		} catch (WTException wtexception) {
			throw new WTException("IBAUtility.updateIBAHolder() - Couldn't update IBAHolder : " + wtexception);
		}
		return flag;
	}
}
