package ext.wis.workflow;

import org.apache.log4j.Logger;

import com.ptc.core.components.forms.FormResult;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.work.NmWorkItemCommands;

import ext.wisplm.util.NmUtil;
import ext.wisplm.util.WfUtil;
import wt.fc.Persistable;
import wt.fc.PersistentReference;
import wt.util.WTException;
import wt.vc.wip.WorkInProgressHelper;
import wt.vc.wip.Workable;
import wt.workflow.engine.WfActivity;
import wt.workflow.engine.WfProcess;
import wt.workflow.work.WorkItem;

/**
 * 重写完成任务的action,进行相关业务校验
 */
public class WISNmWorkItemCommands{
	
	private static final String CLASSNAME = WISNmWorkItemCommands.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	public static FormResult complete(NmCommandBean nmcommandbean) throws WTException{
	      WorkItem workItem = getWorkItem(nmcommandbean);
	      //依据路由校验备注
	      validateComments(nmcommandbean);
	      WfProcess  process    = null;
	      WfActivity wfActivity = null;
	      Persistable pbo       = null;
	      String wfTemplateName = "";
	      String wfActivityName = "";
	      try{
	    	  wfActivity            = WfUtil.getWfActivity(workItem);
	    	  wfActivityName 		= wfActivity.getName();
	    	  process               = WfUtil.getWfProcess(workItem);
	    	  wfTemplateName        = WfUtil.getWfTemplateName(workItem);
	    	  pbo                   = getPBO(workItem);
	    	  //校验pbo是否为检出状态,按需
	    	  WfUtil.validatePBOCheckout(workItem, true);
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  throw new WTException(e);
	      }
	     return NmWorkItemCommands.complete(nmcommandbean);
	}
	
	public static void validateComments(NmCommandBean nmcommandbean) throws WTException{
	     NmUtil cbUtil = new NmUtil(nmcommandbean);
	     //获取备注
	     String comments  = cbUtil.getParamValueFromTextArea("comments");
	     //获取用户所选路由
	     String userEvent = cbUtil.getParamValueFromRadio("WfUserEvent0");
	    
	     //************************试制/试生产准备状态报告批准流程****************************************
	     WorkItem workItem = getWorkItem(nmcommandbean);
	     WfActivity wfActivity = null;
	      try{
	    	  wfActivity          = WfUtil.getWfActivity(workItem);
	    	  String activityName = wfActivity.getName();
	    	  logger.debug("任务名称:->" + activityName);
	    	  logger.debug("路由选项:->" + userEvent);
	    	  logger.debug("备注信息:->" + comments);
	    	  /*if(wfActivity != null && "试制/试生产准备状态检查".equals(wfActivity.getName())){
		    	  if("不合格".equals(userEvent)){
		 	    	 if(comments == null || "".equals(comments)){
		 	    		 throw new WTException("您选择了不合格，请在\"备注\"一栏填写不合格原因.");
		 	    	 }
		 	     }
		      }*/
	      }catch(Exception e){
	    	  e.printStackTrace();
	    	  throw new WTException(e);
	      }
	      if("驳回".equals(userEvent)){
	    	 if(comments == null || "".equals(comments)){
	    		 throw new WTException("您选择了驳回,请在\"备注\"一栏填写驳回意见.");
	    	 }
	     }
	     
	}
	
	private static WorkItem getWorkItem(NmCommandBean cb) throws WTException {
		      return (WorkItem) cb.getPageOid().getRef();
	}
	
	private static Persistable getPBO(WorkItem paramWorkItem){
		Persistable localPersistable = null;
	    PersistentReference localPersistentReference = paramWorkItem.getPrimaryBusinessObject();
	    try {
	      if ((localPersistentReference != null) && (localPersistentReference.getKey() != null) && (localPersistentReference.getObject() != null)) {
	        localPersistable = localPersistentReference.getObject();
	      }
	    }
	    catch (Exception e){
	    	e.printStackTrace();
	    }
	
	    return localPersistable;
	  }

	private static boolean isCheckedOut(Workable pbo) throws WTException{
		return WorkInProgressHelper.isCheckedOut(pbo);
	}
}
