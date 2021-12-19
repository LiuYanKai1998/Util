package ext.wisplm.util;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTPrincipalReference;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.queue.ProcessingQueue;
import wt.queue.QueueHelper;
import wt.queue.WtQueue;
import wt.scheduler.ScheduleItem;
import wt.scheduler.SchedulingHelper;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;

/**
 * 队列操作工具类
 * @author ZhongBinpeng
 * @date   2020年1月9日
 * 
 * wt.properties
 * 进程最大数量配置:wt.queue.max.processQueues=100
 */
public class QueueUtil implements RemoteAccess {
	
	public static void main(String args[]) throws RemoteException, InvocationTargetException{
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("wcadmin");
		rms.setPassword("wcadmin");
		if(!RemoteMethodServer.ServerFlag){
			rms.invoke("createSynQueue",CLASSNAME, null, null,null);
		}
	}
	
    private static final String CLASSNAME = QueueUtil.class.getName();
    private static final Logger logger    = Logger.getLogger(CLASSNAME);
    
    /**
     * 
     *  Date today = new Date();
        //创建完成立即执行,此后每隔指定时间执行一次
        Timestamp startTime = new Timestamp(
        today.getYear(), 
        today.getMonth(), 
        today.getDate(),"00", "00", 0, 0);
     *  创建计划(定时)执行队列,执行队列+条目同时创建的策略
     * @param queueName 队列名称
     * @param itemName  条目名称(自定即可)
     * @param itemDescription 队列描述
     * @param targetClass 要执行的目标类
     * @param targetMethod 要执行的目标方法(static)
     * @param startTime    第一次执行的时间
     * @param Periodicity  执行间隔,单位:秒
     */
    public static void createScheduleQueue(String queueName,String itemName,String itemDescription,String targetClass,String targetMethod,Timestamp startTime,long Periodicity){
        try {
            ScheduleItem scheduleitem = getScheduleItem(itemName,queueName);
            if (scheduleitem == null) {
            	logger.debug("开始创建计划执行队列,queueName:" + queueName + ",item:" + itemName);
                scheduleitem = ScheduleItem.newScheduleItem();
                scheduleitem.setItemDescription(itemDescription);
                scheduleitem.setQueueName(queueName);
                scheduleitem.setTargetClass(targetClass);
                scheduleitem.setTargetMethod(targetMethod);
                scheduleitem.setToBeRun(-1l);
                scheduleitem.setStartDate(startTime);
                scheduleitem.setPeriodicity(Periodicity);

                scheduleitem.setPrincipalRef(WTPrincipalReference.newWTPrincipalReference(SessionHelper.manager.getAdministrator()));
                scheduleitem.setItemName(itemName);
                scheduleitem = SchedulingHelper.service.addItem(scheduleitem, null);
                logger.debug("队列&条目创建完毕,queueName:"+queueName+",itemName:" +  itemName + ",itemDescription:" + itemDescription);
            } else {
            	logger.debug("队列&条目存在,更新配置信息itemName:" +  itemName + ",itemDescription:" + itemDescription);
                boolean needModify = false;
                if (scheduleitem.getStartDate().getHours() != startTime.getHours()
                        || scheduleitem.getStartDate().getMinutes() != startTime.getMinutes()) {
                    scheduleitem.setStartDate(startTime);
                    needModify = true;
                }

                if (scheduleitem.getItemDescription().equalsIgnoreCase(itemDescription)) {
                    scheduleitem.setItemDescription(itemDescription);
                    needModify = true;
                }

                if (scheduleitem.getPeriodicity() != Periodicity) {
                    scheduleitem.setPeriodicity(Periodicity);
                    needModify = true;
                }

                if (scheduleitem.getToBeRun() != -1l) {
                    scheduleitem.setToBeRun(-1l);
                    needModify = true;
                }
                if(!scheduleitem.getTargetMethod().equals(targetMethod)){
                	scheduleitem.setTargetMethod(targetMethod);
                	needModify = true;
                }
                if(!scheduleitem.getTargetClass().equals(targetClass)){
                	 scheduleitem.setTargetClass(targetClass);
                	needModify = true;
                }
                
                if (needModify == true) {
                    SchedulingHelper.service.modifyItem(scheduleitem);
                }
            }
           logger.debug("队列下次执行时间为:"+ scheduleitem.getNextTime());
           logger.debug("创建计划任务执行队列:" + scheduleitem.getDisplayIdentity()
                    + ",启动时间:" + scheduleitem.getStartDate() + "'，间隔时间:" + scheduleitem.getPeriodicity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个进程队列
     * @param processingQueueName 进程队列名称
     * @return 返回创建成功的进程队列
     * @throws Exception
     */
    public static ProcessingQueue cresteProcessingQueue(String processingQueueName) throws Exception{
		if(!RemoteMethodServer.ServerFlag){
			Class  cla[] = {String.class};
			Object obj[] = {processingQueueName};
			return (ProcessingQueue) RemoteMethodServer.getDefault().invoke("cresteProcessingQueue",CLASSNAME, null,cla,obj);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			ProcessingQueue processingQueue = QueueHelper.manager.getQueue(processingQueueName);
			if (processingQueue == null) {	
				logger.debug("ProcessingQueue队列不存在,创建:" + processingQueueName);
				processingQueue = QueueHelper.manager.createQueue(processingQueueName);
			}else{
				logger.debug("ProcessingQueue队列存在:" + processingQueueName);
			}
			logger.debug("QueueState:" + processingQueue.getQueueState());
			if(!processingQueue.getQueueState().equals(WtQueue.STARTED)){
				QueueHelper.manager.startQueue(processingQueue);
			}
			logger.debug("队列启动成功:" + processingQueueName);
			return processingQueue;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
    }
    
    /**
     * 将静态业务方法添加到排程队列中执行,只执行一次
     * @param processingQueueName 队列名称
     * @param methodName 业务方法
     * @param className  业务类名
     * @param cla        业务方法参数类型数组
     * @param obj        业务方法参数值数组
     * @return 添加成功或失败
     * @throws Exception
     */
    public static Boolean addEntryToProcessingQueue(String processingQueueName,String methodName,String className,Class[] cla,Object[] obj) throws Exception{
		if(!RemoteMethodServer.ServerFlag){
			Class  clas[] = {String.class,String.class,String.class,Class[].class,Object[].class};
			Object objs[] = {processingQueueName,methodName,className,cla,obj};
			return (Boolean) RemoteMethodServer.getDefault().invoke("addEntryToProcessingQueue",CLASSNAME, null,clas,objs);
		}
		boolean accessEnforced = false;
		try{
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			ProcessingQueue processingQueue = cresteProcessingQueue(processingQueueName);
			processingQueue.addEntry(SessionHelper.manager.getAdministrator(),methodName, className, cla, obj);
			return true;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
		}
    }
    
    public static ScheduleItem getScheduleItem(String itemName,String queueName) throws WTException {
        ScheduleItem scheduleItem = null;
        QuerySpec queryspec = new QuerySpec(ScheduleItem.class);
        queryspec.appendWhere(new SearchCondition(ScheduleItem.class, ScheduleItem.ITEM_NAME, SearchCondition.EQUAL,
        		itemName));
        queryspec.appendAnd();
        queryspec.appendWhere(new SearchCondition(ScheduleItem.class, ScheduleItem.QUEUE_NAME, SearchCondition.EQUAL,
        		queueName));
        queryspec.appendOrderBy(ScheduleItem.class, ScheduleItem.START_DATE, true);

        QueryResult queryResult = PersistenceHelper.manager.find(queryspec);
        while (queryResult.hasMoreElements()) {
            scheduleItem = (ScheduleItem) queryResult.nextElement();
        }

        return scheduleItem;
    }
}
