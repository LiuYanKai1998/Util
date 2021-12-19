package ext.wis;

/**
 *
 *Zhong Binpeng Apr 21, 2021
 */
public class 工作流变量 {
	
	public static void main(String args[]){
		编制();
		批准();
	}
	//全局变量
	private static String BIANZHIDANWEI = "";

	
	public static void 编制(){
		//初始化自BIANZHIDANWEI
		String BIANZHIDANWEI2 = BIANZHIDANWEI;
		//填写值
		BIANZHIDANWEI2 = "8厂";
		
		//复制到BIANZHIDANWEI
		BIANZHIDANWEI = BIANZHIDANWEI2;
	}
	
	public static void 批准(){
		//初始化自BIANZHIDANWEI
		String BIANZHIDANWEI3 = BIANZHIDANWEI;
		System.out.println(BIANZHIDANWEI3);
	}
}
