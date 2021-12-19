package ext.wisplm.util;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.OrganizationServicesHelper;
import wt.org.WTUser;
import wt.preference.PreferenceHelper;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;


/**
 * 
 * @author BZH
 * @date   2020-2-19
 */
public class PreferenceUtil implements RemoteAccess{
	

	public static void main(String[] args) throws WTException, RemoteException, InvocationTargetException{
		WTUser wcadmin = getUserByName("Administrator");
		//USER_ONLY范围
		System.out.println("USER_ONLY用户首选项-USER_ONLYCustomKey1-当前登录用户:"+getUserStringValue("USER_ONLYCustomKey1"));
		System.out.println("USER_ONLY用户首选项-USER_ONLYCustomKey1-wcadmin:"+getUserStringValue(wcadmin,"USER_ONLYCustomKey1"));
		System.out.println("-----------------------------------------");
		//USER范围
		System.out.println("USER用户首选项-USERCustomKey1-当前登录用户:"+getUserStringValue("USERCustomKey1"));
		System.out.println("USER用户首选项-USERCustomKey1-wcadmin:"+getUserStringValue(wcadmin,"USERCustomKey1"));
		System.out.println("-----------------------------------------");
		System.out.println("USER站点首选项-USERCustomKey1:"+getSiteStringValue("USERCustomKey1"));
		//SITE范围
		System.out.println("SITE站点首选项-WISCustomKey1:"+getSiteStringValue("WISCustomKey1"));
		System.out.println("SITE Boolean站点首选项:"+getSiteBooleanValue("SITEBOOLEAN1"));
		System.out.println("SITE Float站点首选项:"+getSiteFloatValue("002"));
		System.out.println("SITE Integer站点首选项:"+getSiteIntegerValue("003"));
	}
	
	private static final String CLASSNAME = PreferenceUtil.class.getName();
	
	/**
	 * 获取当前登录用户首选项值
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static String getUserStringValue(String definitionName) throws WTException{
		WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
		return getUserStringValue(user,definitionName);
	}
	
	/**
	 * 获取指定用户的首选项值
	 * @param user
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static String getUserStringValue(WTUser user,String definitionName) throws WTException{
		Object obj = getUserObjectValue(user,definitionName);
		if(obj == null){
			return "";
		}else if(obj instanceof String){
			return (String)obj;
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * 获取指定用户的首选项值
	 * @param user
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static Boolean getUserBooleanValue(WTUser user,String definitionName) throws WTException{
		Object obj = getUserObjectValue(user,definitionName);
		return getBooleanValue(obj);
	}
	
	/**
	 * 获取指定用户的首选项值
	 * @param user
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static Float getUserFloatValue(WTUser user,String definitionName) throws WTException{
		Object obj = getUserObjectValue(user,definitionName);
		return getFloatValue(obj);
	}
	
	/**
	 * 获取站点首选项值,并按指定分隔符转换为数组
	 * @param definitionName
	 * @param separator
	 * @return
	 * @throws WTException
	 */
	public static String[] getSiteStringValue(String definitionName,String separator) throws WTException{
		String value = getSiteStringValue(definitionName);
		if(StringUtils.isEmpty(value)){
			return null;
		}
		return StringUtils.split(value, separator);
	}
	
	/**
	 * 获取String类型站点首选项值
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static String getSiteStringValue(String definitionName) throws WTException{
		Object obj = getSiteObjectValue(definitionName);
		return getStringValue(obj);
	}
	
	/**
	 * 获取Boolean类型站点首选项
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static Boolean getSiteBooleanValue(String definitionName) throws WTException{
		Object obj = getSiteObjectValue(definitionName);
		return getBooleanValue(obj);
	}
	
	/**
	 * 获取Float类型站点首选项值
	 * 导入时若声明为Float则在编辑首选项时会进行浮点数格式校验
	 * 最终存储格式为String
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static Float getSiteFloatValue(String definitionName) throws WTException{
		Object obj = getSiteObjectValue(definitionName);
		return getFloatValue(obj);
	}
	
	/**
	 * 获取Integer类型站点首选项值
	 * 最终存储格式为String
	 * @param definitionName
	 * @return
	 * @throws WTException
	 */
	public static Integer getSiteIntegerValue(String definitionName) throws WTException{
		Object obj = getSiteObjectValue(definitionName);
		return getIntegerValue(obj);
	}
	
	private static String getStringValue(Object obj){
		if(obj == null){
			return "";
		}else if(obj instanceof String){
			return (String)obj;
		}else{
			return obj.toString();
		}
	}
	
	private static Boolean getBooleanValue(Object obj) throws WTException{
		if(obj == null || !(obj instanceof Boolean)){
			return null;
		}
		return (boolean) obj;
	}
	
	private static Float getFloatValue(Object obj) throws WTException{
		if(obj == null){
			return null;
		}
		return Float.parseFloat(obj.toString());
	}
	
	private static Integer getIntegerValue(Object obj) throws WTException{
		if(obj == null){
			return null;
		}
		return (Integer) obj;
	}
	
	private static Object getUserObjectValue(WTUser user,String definitionName) throws WTException{
		Object obj = PreferenceHelper.service.getValue(user,definitionName, null);
		return obj;
	}
	
	private static Object getSiteObjectValue(String definitionName) throws WTException{
		Object obj = PreferenceHelper.service.getValue(definitionName, null);
		return obj;
	}
	
	/**
	 * 根据用户登录名得到用户
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
	public static WTUser getUserByName(String name) throws WTException, RemoteException, InvocationTargetException {
		if(!RemoteMethodServer.ServerFlag){
			Class 	cla[] = {String.class};
			Object  obj[] = {name};
			return (WTUser) RemoteMethodServer.getDefault().invoke("getUserByName",CLASSNAME,null,cla,obj);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			if(name == null || "".equals(name)){
				return null;
			}
			Enumeration enu = OrganizationServicesHelper.manager.findUser(WTUser.NAME, name);
			WTUser user = null;
			if (enu.hasMoreElements()) {
				user = (WTUser) enu.nextElement();
				return user;
			}
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
		return null;
	}
}
