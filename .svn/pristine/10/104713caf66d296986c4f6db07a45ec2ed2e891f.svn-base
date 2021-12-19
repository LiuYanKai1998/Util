package ext.wisplm.demo;

import org.apache.log4j.Logger;

import wt.log4j.LogR;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

/**
 * @author: Loong
 * @date: 2020年4月2日 下午1:44:50
 * @version: V1.0
 *
 * <p>远程方法调用测试</P>
 *
 */
public class RemoteMethod implements RemoteAccess{
	private static final String CLASS_NAME = RemoteMethod.class.getName();
	private static final Logger LOG = LogR.getLogger(CLASS_NAME);
	
	
	public static void main(String[] args) {
		try {
			RemoteMethodServer.getDefault().setUserName("wcadmin");
			RemoteMethodServer.getDefault().setPassword("wcadmin");
			String str = getString();
			System.out.println("str=====>"+str);
			getString("Windchill=====>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static String  getString() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            return (String)RemoteMethodServer.getDefault().invoke(
                    "getString", CLASS_NAME, null,
                    new Class[] {},
                    new Object[] {});
        }
        String str = "Windchill!";
        return str;
	}
	
	
	
	public static void  getString(String str) throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "getString", CLASS_NAME, null,
                    new Class[] {String.class},
                    new Object[] {str});
            //注意:没有返回值的方法一定要在此添加return,否则代码会在MethodServer和shell中各执行一次
            return;
        }
        System.out.println("传入的参数===System===》"+str);
        LOG.info("传入的参数===LOG===》"+str);
	}

}
