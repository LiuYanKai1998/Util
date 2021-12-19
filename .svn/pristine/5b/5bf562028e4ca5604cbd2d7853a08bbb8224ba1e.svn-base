package ext.wisplm.demo;

import wt.fc.ObjectReference;
import wt.fc.WTObject;

public class 工作流进程WfProcess {
	
	/**
	 * 模拟工作流进程
	 */
	public static void run(){
		工作流活动_编制();
		工作流活动_校对();
		表达式1();
		表达式2();
	}
	
	/**
	 * 工作流默认全局变量:工作流进程WfProcess的reference,getObject=WfProcess
	 */
	public static ObjectReference self;
	
	/**
	 *工作流默认全局变量:流程主对象(业务对象),简称pbo
	 */
	public static WTObject primaryBusinessObject;
	
	/**
	 * 工作流自定义全局变量
	 */
	public static String  comment = "审批意见信息::::";
	
	public static void 工作流活动_编制(){
		/**
		 * 工作流活动默认局部变量,pbo初始化自全局变量primaryBusinessObject
		 */
		WTObject primaryBusinessObject = 工作流进程WfProcess.primaryBusinessObject;
		/**
		 * 工作流活动默认局部变量,工作流活动WfActivity的reference,getObject()=WfActivity
		 * self对象未从全局变量中初始化,为当前活动局部变量
		 */
		ObjectReference self;
		
		/**
		 * 工作流活动自定义局部变量,初始化自全局变量comment,此例定义为可修改
		 */
		String comment_编制 = 工作流进程WfProcess.comment;
		
		
		/**
		 * 定义一个编制环节的局部变量,不初始化自任何全局变量,也不复制到任何全局变量
		 */
		String userName = "张三";
		
		/**
		 * 模拟任务界面修改
		 */
		comment_编制 = "审批意见信息::::编制审批意见";
		
		
		/**
		 * **********************复制到***********************************
		 */
		工作流进程WfProcess.comment               = comment_编制;
		
		工作流进程WfProcess.primaryBusinessObject = primaryBusinessObject;
	}
	
	
	public static void 工作流活动_校对(){
		/**
		 * 工作流活动默认局部变量,pbo初始化自全局变量primaryBusinessObject
		 */
		WTObject primaryBusinessObject = 工作流进程WfProcess.primaryBusinessObject;
		/**
		 * 工作流活动默认局部变量,工作流活动WfActivity的reference,getObject()=WfActivity
		 * self对象未从全局变量中初始化,为当前活动局部变量
		 */
		ObjectReference self;
		
		/**
		 * 工作流活动自定义局部变量,初始化自全局变量comment,此例定义为可修改
		 */
		String comment_校对 = 工作流进程WfProcess.comment;
		
		
		/**
		 * 模拟任务界面修改
		 */
		comment_校对 = "审批意见信息::::编制审批意见+校对审批通过";
		
		
		/**
		 * **********************复制到***********************************
		 */
		工作流进程WfProcess.comment               = comment_校对;
		
		工作流进程WfProcess.primaryBusinessObject = primaryBusinessObject;
	}
	
	
	public static void 表达式1(){
		
		System.out.println(self.getObject());
		System.out.println(primaryBusinessObject.getDisplayIdentifier());
	}

	public static void 表达式2(){
		
		System.out.println(self.getObject());
		System.out.println(primaryBusinessObject.getDisplayIdentifier());
	}
}
