package ext.wisplm.demo;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;

//实现远程调用接口
/**
 * 示例中的两个方法
 * windchillMethodDemo1为纯远程调用(服务端执行)有返回值的方法
 * windchillMethodDemo2为纯远程调用(服务端执行)无返回值的方法
 * closeAccessToInvoke为远程调用+关闭系统权限控制
 * changeUserToInvoke为后台切换用户执行一段业务逻辑
 * 对于牵扯到windchill对象增删改查的,尽量使用远程调用(切换到MethodServer中执行)
 * Windchill 10及以上版本,无需编写远程调用,MethodServer和Tomcat合并到同一个jvm里了
 * 若需要在windchill shell中执行,则需添加远程调用
 */
public class WindchillClassDemo implements RemoteAccess{
	
	public static void main(String args[]) throws RemoteException, InvocationTargetException{
		//main函数测试示例
		String s1 = "test";
		WTPart part = null;
		windchillMethodDemo1(s1,part);
		
		//写完之后,在windchill shell中运行windchill ext.gace.ebom.imp.WindchillClassDemo
	}
	
	//定义CLASSNAME,用于后续方法的远程调用
	private static final String CLASSNAME = WindchillClassDemo.class.getName();
	//使用log4j进行日志输出
	private static final Logger logger = Logger.getLogger(CLASSNAME);
	
	//windchill方法定义,一般为静态方法
	//远程调用示例,有返回值
	public static List windchillMethodDemo1(String s1,WTPart part) throws RemoteException, InvocationTargetException{
		//对于涉及到windchill对象增删改查的代码，需要通过远程调用方式实现
		//判断当前执行环境是否在Windchill服务端,如果不是,则执行远程调用
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer rms = RemoteMethodServer.getDefault();
			//参数1:当前的方法名称
			//参数2:类名
			//参数3:对象实例(new 当前类)(如果方法为static,则传递null)
			//参数4:当前方法的参数类型数组,有顺序,Class[]
			//参数5:当前方法的参数值数组,有顺序,Object[]
			//方法有返回值的写法(任何类型)
			return (List)rms.invoke("windchillMethodDemo1", CLASSNAME, null, new Class[]{String.class,WTPart.class,}, new Object[]{s1,part});
			//如果当前方法无返回值,则按如下方式写
			//rms.invoke("windchillMethodDemo", CLASSNAME, null, new Class[]{String.class,WTPart.class,}, new Object[]{s1,part});
			//return;
		}
		
		//方法内容
		//xxx
		//xxxxxxx
		//xxxxxx
		//结束
		return new ArrayList();
	}
	
	//远程调用示例,有返回值
	public static void windchillMethodDemo2(String s1,WTPart part) throws RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer rms = RemoteMethodServer.getDefault();
			rms.invoke("windchillMethodDemo2", CLASSNAME, null, new Class[]{String.class,WTPart.class,}, new Object[]{s1,part});
			return;
		}
		
		//方法内容
		//xxx
		//xxxxxxx
		//xxxxxx
		//结束
	}
	
	/**
	 * windchill方法定义,关闭系统权限策略(即当前登录的用户无论是普通用户还是系统管理员,均有权限执行代码里描述的增删改查)
	 * @description
	 * @author      ZhongBinpeng
	 * @param s1
	 * @param part
	 * @return
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 * @throws WTException
	 */
	public static List closeAccessToInvoke(String s1,WTPart part) throws RemoteException, InvocationTargetException, WTException{
		//对于涉及到windchill对象增删改查的代码，需要通过远程调用方式实现
		//判断当前执行环境是否在Windchill服务端,如果不是,则执行远程调用
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer rms = RemoteMethodServer.getDefault();
			return (List)rms.invoke("windchillMethodDemo3", CLASSNAME, null, new Class[]{String.class,WTPart.class,}, new Object[]{s1,part});
			//如果当前方法无返回值,则按如下方式写
			//rms.invoke("windchillMethodDemo", CLASSNAME, null, new Class[]{String.class,WTPart.class,}, new Object[]{s1,part});
			//return;
		}
		
		boolean accessEnforced = false;
		try{
			//关闭系统权限验证,同时返回关闭之前的上下文权限是否启用
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			//示例代码,获取当前上下文的登录用户
			WTUser user = (WTUser) SessionHelper.manager.getPrincipal();
			user.getName();//登录名
			user.getFullName();//中文名
			
			//方法内容
			//xxx
			//xxxxxxx
			//xxxxxx
			//结束
		}finally{
			//恢复关闭之前的上下文权限认证
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
		return new ArrayList();
	}
	
	/**
	 * 切换用户去执行一段业务逻辑
	 * @description
	 * @author      ZhongBinpeng
	 * @param s1
	 * @param part
	 * @return
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 * @throws WTException
	 */
	public static void changeUserToInvoke(String s1,WTPart part) throws RemoteException, InvocationTargetException, WTException{
		WTUser currentUser = null;
		try{
			/**
			 * 1、获取当前环境用户
			 */
			currentUser = (WTUser) SessionHelper.manager.getPrincipal();
			/**
			 * 2、切换后台用户,以另外一个用户身份去执行一段业务逻辑
			 */
			 String anotherUserName = "";//此处为要切换的目标用户登录名
			 SessionHelper.manager.setPrincipal(anotherUserName);
			 /**
			  * 3、执行业务逻辑代码
			  * TODO
			  */
		}finally{
			  /**
			   * 4、将用户切换为原用户
			   */
			if(currentUser != null){
				SessionHelper.manager.setPrincipal(currentUser.getName());
			}
		}
	}
}
