package ext.wisplm.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import wt.enterprise.RevisionControlled;
import wt.fc.Persistable;
import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.httpgw.URLFactory;
import wt.iba.value.IBAHolder;
import wt.org.WTPrincipalReference;
import wt.org.WTUser;
import wt.session.SessionHelper;
import wt.util.WTException;
import wt.vc.Iterated;

import com.ptc.netmarkets.util.misc.NmActionServiceHelper;



/**
 * 获取基本文件的属性
 */
public class ObjectInfoUtil {
	
		/**
		 * 获取对象pdm系统号
		 * @param obj
		 * @return
		 * @throws SecurityException
		 * @throws NoSuchMethodException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 */
		public static String GetPDMNumber(Persistable obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Method mth_get_number = obj.getClass().getMethod("getNumber");
			return mth_get_number.invoke(obj).toString();
		}
		
		/**
		 * 获取名字
		 * @param obj
		 * @return
		 * @throws SecurityException
		 * @throws NoSuchMethodException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 */
		public static String GetPDMName(Persistable obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Method mth_get_number = obj.getClass().getMethod("getName");
			return mth_get_number.invoke(obj).toString();
		}
		
		/**
		 * 获取描述信息
		 * @param obj
		 * @return
		 * @throws SecurityException
		 * @throws NoSuchMethodException
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 */
		public static String GetPDMDescription(Persistable obj) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Method mth_get_number = obj.getClass().getMethod("getDescription");
			return mth_get_number.invoke(obj).toString();
		}
		
		/**
		 * 获取作者
		 * @param obj   主业务对象
		 * @return
		 */
		public static String GetCreator(Persistable obj) {
			String rs = "";
			if (obj instanceof Iterated) {
				Iterated rev = (Iterated) obj;
				WTPrincipalReference userRef = rev.getCreator();
				if (userRef != null) {
					rs = ((WTUser)userRef.getObject()).getFullName();
				}
			}
			return rs;
		}
		
		public static WTPrincipalReference GetCreatorRef(Persistable obj) {
			WTPrincipalReference principalRef = null;
			try {
				Method getCreatorMethod = obj.getClass().getMethod("getCreator");
				if (getCreatorMethod != null) {
					principalRef = (WTPrincipalReference) getCreatorMethod.invoke(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return principalRef;
		}
		
		/**
		 * 获取修改者
		 * @param obj  主业务对象
		 * @return
		 */
		public static String GetModifer(Persistable obj) {
			String rs = "";
			if (obj instanceof Iterated) {
				Iterated rev = (Iterated) obj;
				WTPrincipalReference userRef = rev.getModifier();
				if (userRef != null) {
					rs = ((WTUser)userRef.getObject()).getFullName();
				}
			}
			return rs;
		}
		
		/**
		 * 获取创建时间
		 * @param obj
		 * @param sf
		 * @return
		 */
		public static String GetCreateTime(Persistable obj, SimpleDateFormat sf) {
			String rs = "";
			if (sf == null) {
				sf = new SimpleDateFormat("yyyy/MM/dd");
			}
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				Timestamp time = rev.getCreateTimestamp();
				if (time != null) {
					rs = sf.format(new Date(time.getTime()));
				}
			}
			return rs;
		}
		
		/**
		 * 获取修改时间
		 * @param obj  主业务对象
		 * @param sf   修改时间格式对象
		 * @return
		 */
		public static String GetModifyTime(Persistable obj, SimpleDateFormat sf) {
			String rs = "";
			if (sf == null) {
				sf = new SimpleDateFormat("yyyy/MM/dd");
			}
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				Timestamp time = rev.getModifyTimestamp();
				if (time != null) {
					rs = sf.format(new Date(time.getTime()));
				}
			}
			return rs;
		}
		
		/**
		 * 获取版本信息
		 * @param obj  主业务对象
		 * @return
		 */
		public static String GetVersion(Persistable obj) {
			String rs = "";
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				rs = rev.getIterationDisplayIdentifier().toString();
			}
			return rs;
		}
		
		public static String GetRev(Persistable obj) {
			String rs = "";
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				rs = rev.getVersionDisplayIdentifier().toString();
			}
			return rs;
		}
		
		
		/**
		 * 获取生命周期
		 * @param obj  主业务对象   
		 * @return
		 */
		public static String GetLifecyceDisplay(Persistable obj) {
			String rs = "";
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				try {
					rs = rev.getLifeCycleState().getDisplay(SessionHelper.getLocale());
				} catch (WTException e) {
					e.printStackTrace();
				}
			}
			return rs;
		}
		
		public static String GetLifecyceKey(Persistable obj) {
			String rs = "";
			if (obj instanceof RevisionControlled) {
				RevisionControlled rev = (RevisionControlled) obj;
				rs = rev.getLifeCycleState().toString();
			}
			return rs;
		}
		
		
		/**
		 * 获取对象url
		 * @create date: 2011-9-6下午2:38:39
		 * @methodName: getURL
		 * @return: String
		 * @param obj
		 * @return
		 * @throws WTException
		 */
		public static String getURL(Object obj) throws WTException {
			ReferenceFactory referencefactory = new ReferenceFactory();
			String s1 = referencefactory.getReferenceString((Persistable) obj);
			URLFactory urlfactory = new URLFactory();
			HashMap hashmap = new HashMap();
			hashmap.put("oid", s1);
			String s2 = NmActionServiceHelper.service.getAction("object", "view").getUrl();
			return urlfactory.getHREF(s2, hashmap, true);
		}

		
		public static String getOid(Persistable obj) throws WTException{
			return new ReferenceFactory().getReferenceString(obj);
		}
		
		public static Persistable getObject(String  oid) throws WTException{
			return new ReferenceFactory().getReference(oid).getObject();
		}
	}