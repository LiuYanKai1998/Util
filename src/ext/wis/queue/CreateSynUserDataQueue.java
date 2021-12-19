package ext.wis.queue;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import ext.wisplm.util.QueueUtil;
import ext.wisplm.util.WTUtil;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;

/**
 *
 *Zhong Binpeng Jul 12, 2021
 */
public class CreateSynUserDataQueue implements RemoteAccess{
	
	public static void main(String args[]) throws Exception{
		//java ext.wis.queue.CreateSynUserDataQueue
		createSynUserDataQueue();
		QueueUtil.cresteProcessingQueue("数据导入队列");
		QueueUtil.addEntryToProcessingQueue("数据导入队列", "importData", SynUserData.class.getName(), new Class[]{String.class}, new String[]{"123.zip"});
	}
	
	private static final String CLASSNAME = SynUserData.class.getName();
	
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	public static void createSynUserDataQueue( ) throws RemoteException, InvocationTargetException{
		//队列名称
		String queueName = "同步用户数据队列";
		//条目名称
		String itemName  = "同步用户数据队列Item";
		//条目描述
		String itemDescription = "用于定期同步系统用户数据";
		//定时执行的目标类
		String targetClass     = SynUserData.class.getName();
		//定时执行的目标方法
		String targetMethod    = "SynUserData";
		//第一次执行时间
		Date today = new Date();
        //创建完成立即执行,此后每隔指定时间执行一次,17.20执行,时区为GMT+00,需要加8个小时
        Timestamp startTime = new Timestamp(today.getYear(), today.getMonth(), today.getDate(),9,20,0,0);
	    //间隔时间,1分钟执行一次
        long periodicity = 60;
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		if(!RemoteMethodServer.ServerFlag){
			Class[] cla  = {String.class,String.class,String.class,String.class,String.class,Timestamp.class,long.class};
			Object[] obj = {queueName,itemName,itemDescription,targetClass,targetMethod,startTime,periodicity};
			rms.invoke("createScheduleQueue",QueueUtil.class.getName(), null, cla,obj);
		}
	}
	
	/**
	 * 同步用户数据主方法
	 * @description
	 */
	public static void SynUserData(){
		//select * from wtuser
		logger.debug("定时同步用户数据,当前时间:" + WTUtil.formatTime("yyyy-MM-dd HH:mm:ss"));
	}

}
