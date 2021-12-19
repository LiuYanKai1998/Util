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
public class SynUserData implements RemoteAccess{
	
	public static void main(String args[]) throws Exception{
		//java ext.wis.queue.SynUserData
		//createSynUserDataQueue();
		QueueUtil.cresteProcessingQueue("数据导入队列");
		
	}
	private static final String CLASSNAME = SynUserData.class.getName();
	
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	/**
	 * 同步用户数据主方法
	 * @description
	 */
	public static void SynUserData(){
		//select * from wtuser
		logger.debug("定时同步用户数据,当前时间:" + WTUtil.formatTime("yyyy-MM-dd HH:mm:ss"));
	}

	public static void importData(String s) throws InterruptedException{
		logger.debug("开始通过队列导入数据,参数s:" + s);
		for(int i = 0 ; i < 60 ; i ++){
			Thread.sleep(1000);
		}
		logger.debug("通过队列导入数据完毕");
	}
}
