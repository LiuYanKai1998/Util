package ext.wisplm.demo;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Vector;

import ext.wisplm.util.IBAUtil;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.util.WTRuntimeException;

/**
 * 获取设置软属性值示例
 * 前置条件:已在部件类型上配置了以下三个全局属性
 * 1.密级,内部名称:MIJI,字符串,单值约束
 * 2.重量,内部名称:ZHONGLIANG,字符串,单值约束
 * 3.厂家,内部名称:CHANGJIA,字符串,没有单值约束,即可设置多个值
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月6日
 */
public class IBAGetSet implements RemoteAccess{

	public static void main(String[] args) throws WTRuntimeException, WTException, RemoteException, WTPropertyVetoException, ParseException, InvocationTargetException {
		
		
		/**
		 * 创建完软属性后,在系统中找到任意部件,编辑软属性值,通过地址栏找到该部件的oid
		 */
		String partOid = "VR:wt.part.WTPart:136453";
		/**
		 * oid转换为部件对象
		 */
		wt.part.WTPart part = (wt.part.WTPart) new ReferenceFactory().getReference(partOid).getObject();	
		/**
		 * 获取密级、重量、厂家的属性值
		 */
		String miji 		= IBAUtil.getIBAValue(part, "MIJI");
		String zhongliang 	= IBAUtil.getIBAValue(part, "ZHONGLIANG");
		/**
		 * 多值约束条件的软属性返回值的Vector集合
		 */
		java.util.Vector changjia   = IBAUtil.getIBAValues(part, "CHANGJIA");
		
		System.out.println("部件:" + part.getDisplayIdentity());
		System.out.println("当前密级:" + miji);
		System.out.println("当前重量:" + zhongliang);
		System.out.println("当前厂家:" + changjia);
		/**
		 * 通过程序设置软属性值
		 */
		changeStandardPartIBA();

	}
	
	/**
	 * 远程调用,切换到MethodServer中执行
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 * @throws WTRuntimeException
	 * @throws WTException
	 * @throws WTPropertyVetoException
	 * @throws ParseException
	 */
	public static void changeStandardPartIBA() throws RemoteException, InvocationTargetException, WTRuntimeException, WTException, WTPropertyVetoException, ParseException{
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer rms = RemoteMethodServer.getDefault();
			rms.invoke("changeStandardPartIBA", IBAGetSet.class.getName(), null,null,null);
			/**
			 * 注意:没有返回值的方法一定要在此添加return,否则代码会在MethodServer和shell中各执行一次
			 */
			return;
		}
		String partOid = "VR:wt.part.WTPart:136453";
		WTPart part = (WTPart) new ReferenceFactory().getReference(partOid).getObject();
		/**
		 * 设置值,多个值通过||隔开
		 */
		IBAUtil.setIBAValue(part, "MIJI", "重要");
		IBAUtil.setIBAValue(part, "ZHONGLIANG", "8888");
		IBAUtil.setIBAValue(part, "CHANGJIA", "99厂||100厂||101厂");
		
		/**
		 * 获取值,输出测试
		 */
		String miji       = IBAUtil.getIBAValue(part, "MIJI");
		String zhongliang = IBAUtil.getIBAValue(part, "ZHONGLIANG");
		Vector changjia   = IBAUtil.getIBAValues(part, "CHANGJIA");
		
		System.out.println("123设置后密级:" + miji); 
		System.out.println("123设置后重量:" + zhongliang);
		System.out.println("123设置后厂家:" + changjia); 
		test();
	}
	
	public static void test(){
		System.out.println("11111111111111111112222222222222222222222111111111111111111");
	}

}
